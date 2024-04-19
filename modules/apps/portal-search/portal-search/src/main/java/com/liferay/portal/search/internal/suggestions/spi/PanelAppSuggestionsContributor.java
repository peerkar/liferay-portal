/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.internal.suggestions.spi;

import com.liferay.application.list.GroupProvider;
import com.liferay.application.list.PanelApp;
import com.liferay.application.list.PanelAppRegistry;
import com.liferay.application.list.constants.ApplicationListWebKeys;
import com.liferay.application.list.constants.PanelAppFieldNames;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Localization;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.document.Document;
import com.liferay.portal.search.hits.SearchHit;
import com.liferay.portal.search.hits.SearchHits;
import com.liferay.portal.search.index.IndexNameBuilder;
import com.liferay.portal.search.query.BooleanQuery;
import com.liferay.portal.search.query.Queries;
import com.liferay.portal.search.query.Query;
import com.liferay.portal.search.rest.dto.v1_0.SuggestionsContributorConfiguration;
import com.liferay.portal.search.searcher.SearchRequest;
import com.liferay.portal.search.searcher.SearchRequestBuilder;
import com.liferay.portal.search.searcher.SearchRequestBuilderFactory;
import com.liferay.portal.search.searcher.SearchResponse;
import com.liferay.portal.search.searcher.Searcher;
import com.liferay.portal.search.spi.suggestions.SuggestionsContributor;
import com.liferay.portal.search.suggestions.Suggestion;
import com.liferay.portal.search.suggestions.SuggestionBuilderFactory;
import com.liferay.portal.search.suggestions.SuggestionsContributorResults;
import com.liferay.portal.search.suggestions.SuggestionsContributorResultsBuilderFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	enabled = true, property = "search.suggestions.contributor.name=panel_app",
	service = SuggestionsContributor.class
)
public class PanelAppSuggestionsContributor implements SuggestionsContributor {

	@Override
	public SuggestionsContributorResults getSuggestionsContributorResults(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse,
		SearchContext searchContext,
		SuggestionsContributorConfiguration
			suggestionsContributorConfiguration) {

		Map<String, Object> attributes =
			(Map<String, Object>)
				suggestionsContributorConfiguration.getAttributes();

		if ((attributes == null) ||
			!attributes.containsKey("panelAppRootCategory")) {

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Attributes do not contain the panel app root category");
			}

			return null;
		}

		if (!_exceedsCharacterThreshold(
				(Map<String, Object>)
					suggestionsContributorConfiguration.getAttributes(),
				searchContext.getKeywords())) {

			return null;
		}

		SearchResponse searchResponse = _searcher.search(
			_getSearchRequest(
				attributes, searchContext,
				GetterUtil.getInteger(
					suggestionsContributorConfiguration.getSize(),
					_DEFAULT_SIZE)));

		SearchHits searchHits = searchResponse.getSearchHits();

		if (searchHits.getTotalHits() == 0) {
			return null;
		}

		return _toSuggestionsContributorResults(
			suggestionsContributorConfiguration.getDisplayGroupName(),
			liferayPortletRequest, searchContext, searchHits.getSearchHits());
	}

	private Object _createBreadcrumbs(Document document, Locale locale) {
		String panelAppCategoryKey = document.getString(
			PanelAppFieldNames.PANEL_APP_CATEGORY_KEY);

		String[] parts = panelAppCategoryKey.split("\\.");

		if (parts.length == 1) {
			return _language.get(locale, parts[0]);
		}

		StringBundler sb = new StringBundler();

		String panelAppRootCategory = _language.get(
			locale,
			StringUtil.replace(parts[0], CharPool.UNDERLINE, CharPool.DASH));

		String languageKey = "category." + parts[0];

		for (int i = 1; i < parts.length; i++) {
			if (sb.length() > 0) {
				sb.append(_SPACE_ARROW_SPACE);
			}

			languageKey = languageKey + "." + parts[i];

			String localization = _language.get(locale, languageKey);

			if (localization.equals(languageKey)) {
				localization = _language.get(
					locale,
					StringUtil.replace(
						parts[i], CharPool.UNDERLINE, CharPool.DASH));
			}

			sb.append(localization);
		}

		return panelAppRootCategory + _SPACE_ARROW_SPACE + sb.toString();
	}

	private Query _createQuery(
		Map<String, Object> attributes, String keywords, String languageId) {

		BooleanQuery booleanQuery = _queries.booleanQuery();

		booleanQuery.addFilterQueryClauses(
			_queries.term(
				PanelAppFieldNames.PANEL_APP_ROOT_CATEGORY,
				(String)attributes.get("panelAppRootCategory")));

		booleanQuery.addMustQueryClauses(
			_queries.multiMatch(
				keywords,
				HashMapBuilder.put(
					_localization.getLocalizedName(
						Field.DESCRIPTION, languageId),
					5.0F
				).put(
					_localization.getLocalizedName(
						PanelAppFieldNames.PANEL_APP_CATEGORY, languageId),
					2.0F
				).put(
					_localization.getLocalizedName(Field.TITLE, languageId),
					5.0F
				).build()));

		return booleanQuery;
	}

	private Object _createViewURL(
		LiferayPortletRequest liferayPortletRequest, PanelApp panelApp) {

		try {
			return String.valueOf(
				panelApp.getPortletURL(
					liferayPortletRequest.getHttpServletRequest()));
		}
		catch (PortalException portalException) {
			_log.error(portalException);
		}

		return null;
	}

	private boolean _exceedsCharacterThreshold(
		Map<String, Object> attributes, String keywords) {

		int characterThreshold = MapUtil.getInteger(
			attributes, "characterThreshold", _DEFAULT_CHARACTER_THRESHOLD);

		if (Validator.isBlank(keywords)) {
			if (characterThreshold == 0) {
				return true;
			}
		}
		else if (keywords.length() >= characterThreshold) {
			return true;
		}

		return false;
	}

	private Group _getGroup(HttpServletRequest httpServletRequest) {
		GroupProvider groupProvider =
			(GroupProvider)httpServletRequest.getAttribute(
				ApplicationListWebKeys.GROUP_PROVIDER);

		if (groupProvider != null) {
			Group group = groupProvider.getGroup(httpServletRequest);

			if (group != null) {
				return group;
			}
		}

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		return themeDisplay.getScopeGroup();
	}

	private PanelApp _getPanelApp(
		LiferayPortletRequest liferayPortletRequest, String panelAppKey,
		String parentPanelCategoryKey) {

		List<PanelApp> panelApps = _panelAppRegistry.getPanelApps(
			parentPanelCategoryKey,
			PermissionThreadLocal.getPermissionChecker(),
			_getGroup(liferayPortletRequest.getHttpServletRequest()));

		for (PanelApp panelApp : panelApps) {
			if (Objects.equals(panelApp.getKey(), panelAppKey)) {
				return panelApp;
			}
		}

		return null;
	}

	private SearchRequest _getSearchRequest(
		Map<String, Object> attributes, SearchContext searchContext1,
		int size) {

		SearchRequestBuilder searchRequestBuilder =
			_searchRequestBuilderFactory.builder();

		searchRequestBuilder.withSearchContext(
			searchContext2 -> {
				searchContext2.setKeywords(searchContext1.getKeywords());
				searchContext2.setLocale(searchContext1.getLocale());
				searchContext2.setTimeZone(searchContext1.getTimeZone());
				searchContext2.setUserId(searchContext1.getUserId());
			});

		searchRequestBuilder.size(
			size
		).query(
			_createQuery(
				attributes, searchContext1.getKeywords(),
				_language.getLanguageId(searchContext1.getLocale()))
		).indexes(
			_indexNameBuilder.getIndexName(CompanyConstants.SYSTEM)
		).from(
			0
		);

		return searchRequestBuilder.build();
	}

	private List<Suggestion> _getSuggestions(
		LiferayPortletRequest liferayPortletRequest, Locale locale,
		List<SearchHit> searchHits) {

		List<Suggestion> suggestions = new ArrayList<>();

		for (SearchHit searchHit : searchHits) {
			Document document = searchHit.getDocument();

			PanelApp panelApp = _getPanelApp(
				liferayPortletRequest,
				document.getString(PanelAppFieldNames.PANEL_APP_KEY),
				document.getString(PanelAppFieldNames.PANEL_APP_CATEGORY_KEY));

			if (panelApp == null) {
				continue;
			}

			suggestions.add(
				_suggestionBuilderFactory.builder(
				).attribute(
					"breadcrumbs", _createBreadcrumbs(document, locale)
				).attribute(
					"viewURL", _createViewURL(liferayPortletRequest, panelApp)
				).attribute(
					"portletIcon",
					document.getString(PanelAppFieldNames.PORTLET_ICON)
				).score(
					searchHit.getScore()
				).text(
					_getText(searchHit.getDocument(), locale)
				).build());
		}

		return suggestions;
	}

	private String _getText(Document document, Locale locale) {
		String text = document.getString(
			StringBundler.concat(
				Field.TITLE, StringPool.UNDERLINE,
				_language.getLanguageId(locale)));

		if (Validator.isBlank(text)) {
			text = document.getString(Field.TITLE);
		}

		return text;
	}

	private SuggestionsContributorResults _toSuggestionsContributorResults(
		String displayGroupName, LiferayPortletRequest liferayPortletRequest,
		SearchContext searchContext, List<SearchHit> searchHits) {

		List<Suggestion> suggestions = _getSuggestions(
			liferayPortletRequest, searchContext.getLocale(), searchHits);

		if (ListUtil.isEmpty(suggestions)) {
			return null;
		}

		return _suggestionsContributorResultsBuilderFactory.builder(
		).displayGroupName(
			displayGroupName
		).suggestions(
			suggestions
		).build();
	}

	private static final int _DEFAULT_CHARACTER_THRESHOLD = 2;

	private static final int _DEFAULT_SIZE = 5;

	private static final String _SPACE_ARROW_SPACE = " > ";

	private static final Log _log = LogFactoryUtil.getLog(
		PanelAppSuggestionsContributor.class);

	@Reference
	private IndexNameBuilder _indexNameBuilder;

	@Reference
	private Language _language;

	@Reference
	private Localization _localization;

	@Reference
	private PanelAppRegistry _panelAppRegistry;

	@Reference
	private Queries _queries;

	@Reference
	private Searcher _searcher;

	@Reference
	private SearchRequestBuilderFactory _searchRequestBuilderFactory;

	@Reference
	private SuggestionBuilderFactory _suggestionBuilderFactory;

	@Reference
	private SuggestionsContributorResultsBuilderFactory
		_suggestionsContributorResultsBuilderFactory;

}