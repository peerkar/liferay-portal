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

package com.liferay.portal.search.tuning.gsearch.web.internal.util;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.portlet.PortletPreferences;

/**
 * @author André de Oliveira
 * @author Petteri Karttunen
 */
public class PortletPreferencesHelper {

		public PortletPreferencesHelper(
			PortletPreferences portletPreferences) {

			_portletPreferencesOptional = Optional.of(portletPreferences);
		}

		public Optional<Boolean> getBoolean(String key) {
			Optional<String> value = getValue(key);

			return value.map(GetterUtil::getBoolean);
		}

		public boolean getBoolean(String key, boolean defaultValue) {
			Optional<Boolean> value = getBoolean(key);

			return value.orElse(defaultValue);
		}

		public Optional<Integer> getInteger(String key) {
			Optional<String> value = getValue(key);

			return value.map(GetterUtil::getInteger);
		}

		public int getInteger(String key, int defaultValue) {
			Optional<Integer> value = getInteger(key);

			return value.orElse(defaultValue);
		}

		public Map<Locale, String> getLocalizationMap(String key) {
			
			if (_portletPreferencesOptional.isPresent()) {
				return LocalizationUtil.getLocalizationMap(_portletPreferencesOptional.get(), key);
			}
			
			return new HashMap<Locale, String>();
		}

		public Optional<Long> getLong(String key) {
			Optional<String> value = getValue(key);

			return value.map(GetterUtil::getLong);
		}

		public long getLong(String key, long defaultValue) {
			Optional<Long> value = getLong(key);

			return value.orElse(defaultValue);
		}
		
		
		public Optional<String> getString(String key) {
			return getValue(key);
		}

		public String getString(String key, String defaultValue) {
			Optional<String> value = getString(key);

			return value.orElse(defaultValue);
		}

		public Optional<String[]> getStringArray(String key) {
			return getValues(key);
		}

		public String[] getStringArray(String key, String[] defaultValue) {
			Optional<String[]> value = getStringArray(key);

			return value.orElse(defaultValue);
		}

		protected Optional<String> getValue(String key) {
			return _portletPreferencesOptional.flatMap(
				portletPreferences -> maybe(
					portletPreferences.getValue(key, StringPool.BLANK)));
		}

		protected Optional<String[]> getValues(String key) {
			return _portletPreferencesOptional.flatMap(
				portletPreferences -> 
					Optional.of(
							portletPreferences.getValues(key, StringPool.EMPTY_ARRAY)));
		}

		private static Optional<String> maybe(String s) {
			s = StringUtil.trim(s);

			if (Validator.isBlank(s)) {
				return Optional.empty();
			}

			return Optional.of(s);
		}

		private final Optional<PortletPreferences> _portletPreferencesOptional;

}
