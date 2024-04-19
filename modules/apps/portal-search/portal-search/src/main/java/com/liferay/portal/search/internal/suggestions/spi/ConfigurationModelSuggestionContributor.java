/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.internal.suggestions.spi;

import com.liferay.configuration.admin.constants.ConfigurationAdminFieldNames;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.permission.PortletPermissionUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Localization;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Portal;
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

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	enabled = true,
	property = "search.suggestions.contributor.name=configuration",
	service = SuggestionsContributor.class
)
public class ConfigurationModelSuggestionContributor
	implements SuggestionsContributor {

	@Override
	public SuggestionsContributorResults getSuggestionsContributorResults(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse,
		SearchContext searchContext,
		SuggestionsContributorConfiguration
			suggestionsContributorConfiguration) {

		if (!_exceedsCharacterThreshold(
				(Map<String, Object>)
					suggestionsContributorConfiguration.getAttributes(),
				searchContext.getKeywords())) {

			return null;
		}

		int size = GetterUtil.getInteger(
			suggestionsContributorConfiguration.getSize(), _DEFAULT_SIZE);

		SearchResponse searchResponse = _searcher.search(
			_getSearchRequest(searchContext, size));

		SearchHits searchHits = searchResponse.getSearchHits();

		if (searchHits.getTotalHits() == 0) {
			return null;
		}

		return _suggestionsContributorResultsBuilderFactory.builder(
		).displayGroupName(
			suggestionsContributorConfiguration.getDisplayGroupName()
		).suggestions(
			_createSuggestions(
				liferayPortletRequest, searchContext.getLocale(),
				searchHits.getSearchHits(), size)
		).build();
	}

	@Activate
	protected void activate() {
		_initializeConfigurationModelIndexer();
	}

	private void _addFilterQueryClauses(BooleanQuery booleanQuery) {
		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		booleanQuery.addFilterQueryClauses(
			_queries.term(
				Field.ENTRY_CLASS_NAME, _CLASS_NAME_CONFIGURATION_MODEL));

		if (!permissionChecker.isCompanyAdmin()) {
			BooleanQuery booleanFilterQuery = _queries.booleanQuery();

			booleanFilterQuery.addMustNotQueryClauses(
				_queries.term(
					ConfigurationAdminFieldNames.CONFIGURATION_MODEL_SCOPE,
					ExtendedObjectClassDefinition.Scope.COMPANY));

			booleanQuery.addFilterQueryClauses(booleanFilterQuery);
		}

		if (!permissionChecker.isOmniadmin()) {
			BooleanQuery booleanFilterQuery = _queries.booleanQuery();

			booleanFilterQuery.addMustNotQueryClauses(
				_queries.term(
					ConfigurationAdminFieldNames.CONFIGURATION_MODEL_SCOPE,
					ExtendedObjectClassDefinition.Scope.SYSTEM));

			booleanQuery.addFilterQueryClauses(booleanFilterQuery);
		}
	}

	private Object _createBreadcrumbs(
		Document document, Locale locale, String portletId) {

		String languageId = _language.getLanguageId(locale);

		StringBundler sb = new StringBundler(5);

		if (portletId.equals(_INSTANCE_SETTINGS_PORTLET_ID)) {
			sb.append(_language.get(locale, "instance-settings"));
		}
		else if (portletId.equals(_SITE_SETTINGS_PORTLET_ID)) {
			sb.append(_language.get(locale, "site-settings"));
		}
		else {
			sb.append(_language.get(locale, "system-settings"));
		}

		sb.append(_SPACE_ARROW_SPACE);
		sb.append(
			document.getString(
				_localization.getLocalizedName(
					ConfigurationAdminFieldNames.CONFIGURATION_CATEGORY,
					languageId)));
		sb.append(_SPACE_ARROW_SPACE);
		sb.append(
			document.getString(
				_localization.getLocalizedName(Field.TITLE, languageId)));

		return sb.toString();
	}

	private Query _createQuery(String keywords, String languageId) {
		BooleanQuery booleanQuery = _queries.booleanQuery();

		_addFilterQueryClauses(booleanQuery);

		booleanQuery.addMustQueryClauses(
			_queries.multiMatch(
				keywords,
				HashMapBuilder.put(
					_localization.getLocalizedName(
						ConfigurationAdminFieldNames.CONFIGURATION_CATEGORY,
						languageId),
					3.0F
				).put(
					_localization.getLocalizedName(
						ConfigurationAdminFieldNames.
							CONFIGURATION_MODEL_ATTRIBUTE_DESCRIPTION,
						languageId),
					1.0F
				).put(
					_localization.getLocalizedName(
						ConfigurationAdminFieldNames.
							CONFIGURATION_MODEL_ATTRIBUTE_NAME,
						languageId),
					2.0F
				).put(
					_localization.getLocalizedName(Field.TITLE, languageId),
					5.0F
				).build()));

		return booleanQuery;
	}

	private List<Suggestion> _createSuggestions(
		LiferayPortletRequest liferayPortletRequest, Locale locale,
		List<SearchHit> searchHits, int size) {

		List<Suggestion> suggestions = new ArrayList<>();

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		for (SearchHit searchHit : searchHits) {
			Document document = searchHit.getDocument();

			String scope = document.getString(
				ConfigurationAdminFieldNames.CONFIGURATION_MODEL_SCOPE);

			if (Validator.isBlank(scope)) {
				continue;
			}

			ThemeDisplay themeDisplay =
				(ThemeDisplay)liferayPortletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			// TODO: check permission checks

			try {
				if (ExtendedObjectClassDefinition.Scope.COMPANY.equals(scope) &&
					permissionChecker.isCompanyAdmin()) {

					suggestions.add(
						_getSuggestion(
							liferayPortletRequest, locale,
							_INSTANCE_SETTINGS_PORTLET_ID, searchHit));
				}
				else if (ExtendedObjectClassDefinition.Scope.GROUP.equals(
							scope) &&
						 PortletPermissionUtil.contains(
							 permissionChecker, themeDisplay.getPlid(),
							 _SITE_SETTINGS_PORTLET_ID, "VIEW")) {

					suggestions.add(
						_getSuggestion(
							liferayPortletRequest, locale,
							_SITE_SETTINGS_PORTLET_ID, searchHit));
				}

				if (permissionChecker.isOmniadmin()) {
					suggestions.add(
						_getSuggestion(
							liferayPortletRequest, locale,
							_SYSTEM_SETTINGS_PORTLET_ID, searchHit));
				}
			}
			catch (PortalException portalException) {
				throw new RuntimeException(portalException);
			}
		}

		if (suggestions.size() > size) {
			return suggestions.subList(0, size - 1);
		}

		return suggestions;
	}

	private Object _createViewURL(
		Document document, LiferayPortletRequest liferayPortletRequest,
		String portletId) {

		String factoryPid = document.getString(
			ConfigurationAdminFieldNames.CONFIGURATION_MODEL_FACTORY_PID);

		String pid = document.getString(
			ConfigurationAdminFieldNames.CONFIGURATION_MODEL_ID);

		PortletURL portletURL = PortletURLBuilder.create(
			_portal.getControlPanelPortletURL(
				liferayPortletRequest.getHttpServletRequest(), portletId,
				PortletRequest.RENDER_PHASE)
		).setParameter(
			"factoryPid", factoryPid
		).buildPortletURL();

		// TODO: check

		if (factoryPid.equals(pid)) {
			portletURL.setParameter(
				"mvcRenderCommandName",
				"/configuration_admin/view_factory_instances");
		}
		else {
			portletURL.setParameter(
				"mvcRenderCommandName",
				"/configuration_admin/edit_configuration");
			portletURL.setParameter("pid", pid);
		}

		return portletURL.toString();
	}

	private boolean _exceedsCharacterThreshold(
		Map<String, Object> attributes, String keywords) {

		int characterThreshold = _getCharacterThreshold(attributes);

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

	private int _getCharacterThreshold(Map<String, Object> attributes) {
		if (attributes == null) {
			return _DEFAULT_CHARACTER_THRESHOLD;
		}

		return MapUtil.getInteger(
			attributes, "characterThreshold", _DEFAULT_CHARACTER_THRESHOLD);
	}

	private SearchRequest _getSearchRequest(
		SearchContext searchContext1, int size) {

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
				searchContext1.getKeywords(),
				_language.getLanguageId(searchContext1.getLocale()))
		).indexes(
			_indexNameBuilder.getIndexName(CompanyConstants.SYSTEM)
		).from(
			0
		);

		return searchRequestBuilder.build();
	}

	private Suggestion _getSuggestion(
		LiferayPortletRequest liferayPortletRequest, Locale locale,
		String portletId, SearchHit searchHit) {

		Document document = searchHit.getDocument();

		return _suggestionBuilderFactory.builder(
		).attribute(
			"breadcrumbs", _createBreadcrumbs(document, locale, portletId)
		).attribute(
			"viewURL",
			_createViewURL(document, liferayPortletRequest, portletId)
		).score(
			searchHit.getScore()
		).text(
			_getText(searchHit.getDocument(), locale)
		).build();
	}

	private String _getText(Document document, Locale locale) {
		String languageId = LocaleUtil.toLanguageId(locale);

		String text = document.getString(
			StringBundler.concat(
				Field.TITLE, StringPool.UNDERLINE, languageId));

		if (Validator.isBlank(text)) {
			text = document.getString(Field.TITLE);
		}

		return text;
	}

	private void _initializeConfigurationModelIndexer() {
		try {
			Indexer<?> indexer = _indexerRegistry.getIndexer(
				_CLASS_NAME_CONFIGURATION_MODEL);

			SearchContext searchContext = new SearchContext();

			searchContext.setEnd(1);
			searchContext.setKeywords(StringUtil.randomString());
			searchContext.setStart(0);

			indexer.search(searchContext);
		}
		catch (SearchException searchException) {
			_log.error(searchException);
		}
	}

	private static final String _CLASS_NAME_CONFIGURATION_MODEL =
		"com.liferay.configuration.admin.web.internal.model.ConfigurationModel";

	private static final int _DEFAULT_CHARACTER_THRESHOLD = 2;

	private static final int _DEFAULT_SIZE = 5;

	private static final String _INSTANCE_SETTINGS_PORTLET_ID =
		"com_liferay_configuration_admin_web_portlet_InstanceSettingsPortlet";

	private static final String _SITE_SETTINGS_PORTLET_ID =
		"com_liferay_configuration_admin_web_portlet_SiteSettingsPortlet";

	private static final String _SPACE_ARROW_SPACE = " > ";

	private static final String _SYSTEM_SETTINGS_PORTLET_ID =
		"com_liferay_configuration_admin_web_portlet_SystemSettingsPortlet";

	private static final Log _log = LogFactoryUtil.getLog(
		ConfigurationModelSuggestionContributor.class);

	@Reference
	private IndexerRegistry _indexerRegistry;

	@Reference
	private IndexNameBuilder _indexNameBuilder;

	@Reference
	private Language _language;

	@Reference
	private Localization _localization;

	@Reference
	private Portal _portal;

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