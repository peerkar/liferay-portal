/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.search.tuning.gsearch.web.internal.portlet.preferences;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.tuning.gsearch.web.internal.constants.GSearchWebPortletPreferenceKeys;
import com.liferay.portal.search.tuning.gsearch.web.internal.util.PortletPreferencesHelper;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.portlet.PortletPreferences;

/**
 * @author Petteri Karttunen
 */
public class GSearchWebPortletPreferencesImpl implements GSearchWebPortletPreferences {

	public GSearchWebPortletPreferencesImpl(
	
			PortletPreferences portletPreferences) {

		_portletPreferencesHelper = new PortletPreferencesHelper(
			portletPreferences);
	}
	
	public  Optional<String> getDatePickerDateFormatOptional() {
		return _portletPreferencesHelper.getString(
				GSearchWebPortletPreferenceKeys.DATE_PICKER_DATE_FORMAT);
	}

	public  String getDatePickerDateFormatString() {
		return getDatePickerDateFormatOptional().orElse(StringPool.BLANK);
	}

	@Override
	public String[] getFieldsToDisplay() {
		
		String[] defaultValue = new String[] {
			"description",
			"type",
			"link",
			"modifiedDate",
			"assetTags",
			"assetCategories",
			"userName",
			"userImage"
		};
		
		return _portletPreferencesHelper.getStringArray(
				GSearchWebPortletPreferenceKeys.FIELDS_TO_DISPLAY,
				defaultValue);
	}
	
	@Override
	public Optional<String> getGoogleMapsAPIKeyOptional() {
		return _portletPreferencesHelper.getString(
				GSearchWebPortletPreferenceKeys.GOOGLE_MAPS_API_KEY);
	}

	@Override
	public String getGoogleMapsAPIKeyString() {
		return getGoogleMapsAPIKeyOptional().orElse(StringPool.BLANK);
	}

	@Override
	public Optional<String> getHelpTextArticleOptional() {
		return _portletPreferencesHelper.getString(
				GSearchWebPortletPreferenceKeys.HELP_TEXT_ARTICLE);
	}

	@Override
	public String getHelpTextArticleString() {
		return getHelpTextArticleOptional().orElse(StringPool.BLANK);
	}

	@Override
	public String getKeywordPlaceholder(Locale locale) {

		Map<Locale, String> keywordPlaceholderMap = getKeywordPlaceholderMap();

		String s = keywordPlaceholderMap.get(locale);
		
		if (Validator.isBlank(s)) {
			StringBundler sb = new StringBundler(2);
			sb.append(LanguageUtil.get(locale, "search"));
			sb.append("...");
			return sb.toString();
		}
		
		return s;
	}

	@Override
	public Map<Locale, String> getKeywordPlaceholderMap() {
		
		return _portletPreferencesHelper.getLocalizationMap(
				GSearchWebPortletPreferenceKeys.KEYWORD_PLACEHOLDER_MAP);
		
	}

	@Override
	public int getKeywordSuggesterRequestDelay() {
		return _portletPreferencesHelper.getInteger(
				GSearchWebPortletPreferenceKeys.KEYWORD_SUGGESTER_REQUEST_DELAY,
					150);
	}
	
	@Override
	public int getQueryMinLength() {
		return _portletPreferencesHelper.getInteger(
				GSearchWebPortletPreferenceKeys.QUERY_MIN_LENGTH,
					3);
	}

	@Override
	public int getRequestTimeout() {
		return _portletPreferencesHelper.getInteger(
				GSearchWebPortletPreferenceKeys.REQUEST_TIMEOUT,
					10000);
	}

	@Override
	public String[] getResultLayouts() {
		
		String[] defaultValue = new String[] {
				"list",
				"card",
				"thumbnails",
				"maps",
				"raw",
				"explain"
			};
			
			return _portletPreferencesHelper.getStringArray(
					GSearchWebPortletPreferenceKeys.RESULT_LAYOUTS,
					defaultValue);
	}

	@Override
	public Optional<Long> getSearchConfigurationIdOptional() {
		return _portletPreferencesHelper.getLong(
				GSearchWebPortletPreferenceKeys.SEARCH_CONFIGURATION_ID);
	}

	@Override
	public long getSearchConfigurationId() {
		return getSearchConfigurationIdOptional().orElse(0L);
	}

	@Override
	public boolean isAppendRedirectToURL() {
		return _portletPreferencesHelper.getBoolean(
				GSearchWebPortletPreferenceKeys.APPEND_REDIRECT_TO_URL,
				true);
	}

	@Override
	public boolean isConversionTrackingEnabled() {
		return _portletPreferencesHelper.getBoolean(
				GSearchWebPortletPreferenceKeys.CONVERSION_TRACKING_ENABLED,
				true);
	}

	@Override
	public boolean isKeywordSuggesterEnabled() {
		return _portletPreferencesHelper.getBoolean(
				GSearchWebPortletPreferenceKeys.KEYWORD_SUGGESTER_ENABLED,
				true);
	}

	@Override
	public boolean isScopeFilterVisible() {
		return _portletPreferencesHelper.getBoolean(
				GSearchWebPortletPreferenceKeys.SCOPE_FILTER_ENABLED,
				true);
	}

	@Override
	public boolean isTimeFilterVisible() {
		return _portletPreferencesHelper.getBoolean(
				GSearchWebPortletPreferenceKeys.TIME_FILTER_ENABLED,
				true);
	}
	
	@Override
	public boolean isViewInContext() {
		return _portletPreferencesHelper.getBoolean(
			GSearchWebPortletPreferenceKeys.VIEW_IN_CONTEXT,
			true);
	}

	private final PortletPreferencesHelper _portletPreferencesHelper;
}
