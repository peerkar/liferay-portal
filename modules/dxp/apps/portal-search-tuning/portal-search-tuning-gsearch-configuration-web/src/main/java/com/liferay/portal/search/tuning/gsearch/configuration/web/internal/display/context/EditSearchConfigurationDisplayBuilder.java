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

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.SearchConfigurationTypes;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.SearchConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.exception.NoSuchConfigurationException;
import com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration;
import com.liferay.portal.search.tuning.gsearch.configuration.service.SearchConfigurationService;
import com.liferay.portal.search.tuning.gsearch.configuration.web.internal.constants.SearchConfigurationMVCCommandNames;
import com.liferay.portal.search.tuning.gsearch.configuration.web.internal.constants.SearchConfigurationWebKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.web.internal.util.JSONHelperUtil;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.portlet.ActionRequest;
import javax.portlet.ActionURL;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Kevin Tan
 */
public class EditSearchConfigurationDisplayBuilder {

	public EditSearchConfigurationDisplayBuilder(
		HttpServletRequest httpServletRequest, Language language, Log log,
		JSONFactory jsonFactory, RenderRequest renderRequest,
		RenderResponse renderResponse,
		SearchConfigurationService searchConfigurationService) {

		_httpServletRequest = httpServletRequest;
		_language = language;
		_log = log;
		_jsonFactory = jsonFactory;
		_renderRequest = renderRequest;
		_renderResponse = renderResponse;
		_searchConfigurationService = searchConfigurationService;

		_searchConfigurationId = ParamUtil.getLong(
			renderRequest, SearchConfigurationWebKeys.SEARCH_CONFIGURATION_ID);
		_searchConfigurationType = ParamUtil.getInteger(
			renderRequest, SearchConfigurationWebKeys.SEARCH_CONFIGURATION_TYPE,
			SearchConfigurationTypes.CONFIGURATION);

		_searchConfiguration = _getSearchConfiguration();

		_themeDisplay = (ThemeDisplay)_httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public EditSearchConfigurationDisplayContext build() {
		EditSearchConfigurationDisplayContext
			editSearchConfigurationDisplayContext =
				new EditSearchConfigurationDisplayContext();

		_setConfigurationId(editSearchConfigurationDisplayContext);
		_setConfigurationType(editSearchConfigurationDisplayContext);
		_setData(editSearchConfigurationDisplayContext);
		_setPageTitle(editSearchConfigurationDisplayContext);
		_setRedirect(editSearchConfigurationDisplayContext);

		return editSearchConfigurationDisplayContext;
	}

	private JSONArray _getAvailableLocales() {
		JSONArray jsonArray = _jsonFactory.createJSONArray();

		Set<Locale> locales = _language.getAvailableLocales();

		Stream<Locale> stream = locales.stream();

		stream.map(
			this::_getLocaleJSONObject
		).forEach(
			jsonArray::put
		);

		return jsonArray;
	}

	private Map<String, Object> _getContext() {
		return HashMapBuilder.<String, Object>put(
			"locale", _themeDisplay.getLanguageId()
		).put(
			"namespace", _renderResponse.getNamespace()
		).build();
	}

	private JSONObject _getLocaleJSONObject(Locale locale) {
		JSONObject jsonObject = _jsonFactory.createJSONObject();

		String languageId = LocaleUtil.toLanguageId(locale);

		jsonObject.put(
			"label", StringUtil.replace(languageId, '_', "-")
		).put(
			"symbol",
			StringUtil.replace(
				languageId, '_', "-"
			).toLowerCase()
		);

		return jsonObject;
	}

	private Map<String, Object> _getProps() {
		Map<String, Object> props = HashMapBuilder.<String, Object>put(
			"availableLocales", _getAvailableLocales()
		).put(
			"configurationId", _searchConfigurationId
		).put(
			"configurationType", _searchConfigurationType
		).put(
			"redirectURL", _getRedirect()
		).put(
			"submitFormURL", _getSubmitFormURL()
		).build();

		if (_searchConfiguration != null) {
			try {
				props.put(
					"initialClauseConfiguration",
					JSONHelperUtil.getConfigurationSection(
						_searchConfiguration,
						SearchConfigurationKeys.CLAUSE_CONFIGURATION.
							getJsonKey()));
			}
			catch (JSONException jsonException) {
				_log.error(
					"Unable to parse search configuration JSON", jsonException);
			}

			props.put("initialTitle", _getTitle());
		}

		return props;
	}

	private String _getRedirect() {
		String redirect = ParamUtil.getString(_httpServletRequest, "redirect");

		if (Validator.isNull(redirect)) {
			PortletURL portletURL = _renderResponse.createRenderURL();

			redirect = portletURL.toString();
		}

		return redirect;
	}

	private SearchConfiguration _getSearchConfiguration() {
		SearchConfiguration searchConfiguration = null;

		if (_searchConfigurationId > 0) {
			try {
				searchConfiguration =
					_searchConfigurationService.getSearchConfiguration(
						_searchConfigurationId);

				_searchConfigurationType = searchConfiguration.getType();
			}
			catch (NoSuchConfigurationException noSuchConfigurationException) {
				_log.error(
					"Search configuration " + _searchConfigurationId +
						" not found.",
					noSuchConfigurationException);

				SessionErrors.add(
					_renderRequest, SearchConfigurationWebKeys.ERROR_DETAILS,
					noSuchConfigurationException);
			}
			catch (PortalException portalException) {
				_log.error(portalException.getMessage(), portalException);

				SessionErrors.add(
					_renderRequest, SearchConfigurationWebKeys.ERROR_DETAILS,
					portalException);
			}
		}

		return searchConfiguration;
	}

	private String _getSubmitFormURL() {
		ActionURL actionURL = _renderResponse.createActionURL();

		actionURL.setParameter(
			ActionRequest.ACTION_NAME,
			SearchConfigurationMVCCommandNames.EDIT_SEARCH_CONFIGURATION);
		actionURL.setParameter(
			Constants.CMD,
			(_searchConfiguration != null) ? Constants.EDIT : Constants.ADD);
		actionURL.setParameter("redirect", _getRedirect());

		return actionURL.toString();
	}

	private JSONObject _getTitle() {
		Map<Locale, String> titleMap = _searchConfiguration.getTitleMap();

		JSONObject titleJSONObject = _jsonFactory.createJSONObject();

		titleMap.forEach(
			(key, value) -> titleJSONObject.put(
				StringUtil.replace(key.toString(), '_', "-"), value));

		return titleJSONObject;
	}

	private void _setConfigurationId(
		EditSearchConfigurationDisplayContext
			editSearchConfigurationDisplayContext) {

		editSearchConfigurationDisplayContext.setConfigurationId(
			_searchConfigurationId);
	}

	private void _setConfigurationType(
		EditSearchConfigurationDisplayContext
			editSearchConfigurationDisplayContext) {

		editSearchConfigurationDisplayContext.setConfigurationType(
			_searchConfigurationType);
	}

	private void _setData(
		EditSearchConfigurationDisplayContext
			editSearchConfigurationDisplayContext) {

		editSearchConfigurationDisplayContext.setData(
			HashMapBuilder.<String, Object>put(
				"context", _getContext()
			).put(
				"props", _getProps()
			).build());
	}

	private void _setPageTitle(
		EditSearchConfigurationDisplayContext
			editSearchConfigurationDisplayContext) {

		StringBundler sb = new StringBundler(2);

		sb.append(_searchConfiguration != null ? "edit-" : "add-");

		if (_searchConfigurationType ==
				SearchConfigurationTypes.CONFIGURATION) {

			sb.append("search-configuration");
		}
		else if (_searchConfigurationType == SearchConfigurationTypes.SNIPPET) {
			sb.append("configuration-snippet");
		}
		else if (_searchConfigurationType ==
					SearchConfigurationTypes.TEMPLATE) {

			sb.append("configuration-template");
		}

		editSearchConfigurationDisplayContext.setPageTitle(
			_language.get(_httpServletRequest, sb.toString()));
	}

	private void _setRedirect(
		EditSearchConfigurationDisplayContext
			editSearchConfigurationDisplayContext) {

		editSearchConfigurationDisplayContext.setRedirect(_getRedirect());
	}

	private final HttpServletRequest _httpServletRequest;
	private final JSONFactory _jsonFactory;
	private final Language _language;
	private final Log _log;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private final SearchConfiguration _searchConfiguration;
	private final long _searchConfigurationId;
	private final SearchConfigurationService _searchConfigurationService;
	private int _searchConfigurationType;
	private final ThemeDisplay _themeDisplay;

}