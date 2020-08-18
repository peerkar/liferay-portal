/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.portal.search.tuning.gsearch.configuration.web.internal.display.context;

import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.search.SearchResult;
import com.liferay.portal.kernel.search.SearchResultUtil;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.search.tuning.gsearch.configuration.comparator.SearchConfigurationModifiedDateComparator;
import com.liferay.portal.search.tuning.gsearch.configuration.comparator.SearchConfigurationTitleComparator;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.SearchConfigurationPortletKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration;
import com.liferay.portal.search.tuning.gsearch.configuration.service.SearchConfigurationServiceUtil;
import com.liferay.portal.search.tuning.gsearch.configuration.web.internal.constants.SearchConfigurationMVCCommandNames;
import com.liferay.portal.search.tuning.gsearch.configuration.web.internal.security.permission.resource.SearchConfigurationEntryPermission;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.portlet.PortletException;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Petteri Karttunen
 */
public class SearchConfigurationEntriesDisplayContext {

	public SearchConfigurationEntriesDisplayContext(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse,
		int searchConfigurationType) {

		_liferayPortletRequest = liferayPortletRequest;
		_liferayPortletResponse = liferayPortletResponse;

		_portalPreferences = PortletPreferencesFactoryUtil.getPortalPreferences(
			liferayPortletRequest);

		_httpServletRequest = _liferayPortletRequest.getHttpServletRequest();

		_searchConfigurationType = searchConfigurationType;
	}

	public List<String> getAvailableActions(
			SearchConfiguration searchConfiguration)
		throws PortalException {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)_httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (SearchConfigurationEntryPermission.contains(
				themeDisplay.getPermissionChecker(), searchConfiguration,
				ActionKeys.DELETE)) {

			return Collections.singletonList(
				"deleteSearchConfigurationEntries");
		}

		return Collections.emptyList();
	}

	public String getDisplayStyle() {
		String displayStyle = ParamUtil.getString(
			_httpServletRequest, "displayStyle");

		if (Validator.isNull(displayStyle)) {
			return _portalPreferences.getValue(
				SearchConfigurationPortletKeys.SEARCH_CONFIGURATION_ADMIN,
				"entries-display-style", "descriptive");
		}

		_portalPreferences.setValue(
			SearchConfigurationPortletKeys.SEARCH_CONFIGURATION_ADMIN,
			"entries-display-style", displayStyle);

		_httpServletRequest.setAttribute(
			WebKeys.SINGLE_PAGE_APPLICATION_CLEAR_CACHE, Boolean.TRUE);

		return displayStyle;
	}

	public SearchContainer<SearchConfiguration> getSearchContainer()
		throws PortalException, PortletException {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)_httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		PortletURL portletURL = _liferayPortletResponse.createRenderURL();

		portletURL.setProperty(
			"mvcRenderCommandName",
			SearchConfigurationMVCCommandNames.VIEW_SEARCH_CONFIGURATIONS);

		SearchContainer<SearchConfiguration> entriesSearchContainer =
			new SearchContainer<>(
				_liferayPortletRequest,
				PortletURLUtil.clone(portletURL, _liferayPortletResponse), null,
				"no-entries-were-found");

		String orderByCol = ParamUtil.getString(
			_httpServletRequest, "orderByCol", "title");

		entriesSearchContainer.setOrderByCol(orderByCol);

		String orderByType = ParamUtil.getString(
			_httpServletRequest, "orderByType", "asc");

		entriesSearchContainer.setOrderByType(orderByType);

		entriesSearchContainer.setOrderByComparator(
			_getOrderByComparator(
				themeDisplay.getLocale(),
				entriesSearchContainer.getOrderByCol(),
				entriesSearchContainer.getOrderByType()));

		entriesSearchContainer.setRowChecker(
			new EmptyOnClickRowChecker(_liferayPortletResponse));

		_populateResults(entriesSearchContainer);

		return entriesSearchContainer;
	}

	private static OrderByComparator<SearchConfiguration> _getOrderByComparator(
		Locale locale, String orderByCol, String orderByType) {

		boolean orderByAsc = true;

		if (orderByType.equals("desc")) {
			orderByAsc = false;
		}

		OrderByComparator<SearchConfiguration> orderByComparator = null;

		if (orderByCol.equals("modified-date")) {
			orderByComparator = new SearchConfigurationModifiedDateComparator(
				orderByAsc);
		}
		else {
			orderByComparator = new SearchConfigurationTitleComparator(
				orderByAsc, locale);
		}

		return orderByComparator;
	}

	private void _populateResults(
			SearchContainer<SearchConfiguration> searchContainer)
		throws PortalException {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)_httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		List<SearchConfiguration> entriesResults = null;

		long groupId = themeDisplay.getCompanyGroupId();

		String keywords = ParamUtil.getString(_httpServletRequest, "keywords");

		if (Validator.isNull(keywords)) {
			searchContainer.setTotal(
				SearchConfigurationServiceUtil.
					getGroupSearchConfigurationsCount(
						groupId, WorkflowConstants.STATUS_ANY,
						_searchConfigurationType));

			entriesResults =
				SearchConfigurationServiceUtil.getGroupSearchConfigurations(
					groupId, WorkflowConstants.STATUS_ANY,
					_searchConfigurationType, searchContainer.getStart(),
					searchContainer.getEnd(),
					searchContainer.getOrderByComparator());
		}
		else {
			Indexer<SearchConfiguration> indexer =
				IndexerRegistryUtil.getIndexer(SearchConfiguration.class);

			SearchContext searchContext = SearchContextFactory.getInstance(
				_httpServletRequest);

			searchContext.setAttribute(
				Field.STATUS, WorkflowConstants.STATUS_ANY);
			searchContext.setAttribute(Field.TYPE, _searchConfigurationType);
			searchContext.setEnd(searchContainer.getEnd());
			searchContext.setGroupIds(new long[] {groupId});
			searchContext.setKeywords(keywords);
			searchContext.setStart(searchContainer.getStart());

			String orderByCol = ParamUtil.getString(
				_httpServletRequest, "orderByCol", "title");
			String orderByType = ParamUtil.getString(
				_httpServletRequest, "orderByType", "asc");

			Sort sort = null;

			boolean orderByAsc = false;

			if (Objects.equals(orderByType, "asc")) {
				orderByAsc = true;
			}

			if (Objects.equals(orderByCol, "modified-date")) {
				sort = new Sort(
					Field.MODIFIED_DATE, Sort.LONG_TYPE, !orderByAsc);
			}
			else {
				sort = new Sort(orderByCol, !orderByAsc);
			}

			searchContext.setSorts(sort);

			Hits hits = indexer.search(searchContext);

			searchContainer.setTotal(hits.getLength());

			List<SearchResult> searchResults =
				SearchResultUtil.getSearchResults(
					hits, LocaleUtil.getDefault());

			Stream<SearchResult> stream = searchResults.stream();

			entriesResults = stream.map(
				this::_toSearchConfigurationOptional
			).filter(
				Optional::isPresent
			).map(
				Optional::get
			).collect(
				Collectors.toList()
			);
		}

		searchContainer.setResults(entriesResults);
	}

	private Optional<SearchConfiguration> _toSearchConfigurationOptional(
		SearchResult searchResult) {

		try {
			return Optional.of(
				SearchConfigurationServiceUtil.getSearchConfiguration(
					searchResult.getClassPK()));
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Search index is stale and contains a Search Configuration entry " +
						searchResult.getClassPK());
			}

			return Optional.empty();
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SearchConfigurationEntriesDisplayContext.class);

	private final HttpServletRequest _httpServletRequest;
	private final LiferayPortletRequest _liferayPortletRequest;
	private final LiferayPortletResponse _liferayPortletResponse;
	private final PortalPreferences _portalPreferences;
	private final int _searchConfigurationType;

}