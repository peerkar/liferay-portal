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

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * @author Petteri Karttunen
 */
public interface GSearchWebPortletPreferences {

	public  Optional<String> getDatePickerDateFormatOptional();

	public  String getDatePickerDateFormatString();

	public String[] getFieldsToDisplay();

	public Optional<String> getGoogleMapsAPIKeyOptional();

	public String getGoogleMapsAPIKeyString();

	public  Optional<String> getHelpTextArticleOptional();

	public  String getHelpTextArticleString();

	public String getKeywordPlaceholder(Locale locale);

	public Map<Locale, String> getKeywordPlaceholderMap();

	public int getKeywordSuggesterRequestDelay();	

	public int getQueryMinLength();

	public int getRequestTimeout();

	public String[] getResultLayouts();

	public Optional<Long> getSearchConfigurationIdOptional();	

	public long getSearchConfigurationId();	
	
	public boolean isAppendRedirectToURL();

	public boolean isConversionTrackingEnabled();

	public boolean isKeywordSuggesterEnabled();

	public boolean isScopeFilterVisible();	
	
	public boolean isTimeFilterVisible();

	public boolean isViewInContext();
}
