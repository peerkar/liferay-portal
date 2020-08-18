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

package com.liferay.portal.search.tuning.gsearch.configuration.web.internal.util;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration;

/**
 * @author Petteri Karttunen
 */
public class JSONHelperUtil {

	public static String[] getConfigurationSection(
			SearchConfiguration searchConfiguration, String key)
		throws JSONException {

		if (searchConfiguration == null) {
			return new String[] {StringPool.BLANK};
		}

		String configuration = searchConfiguration.getConfiguration();

		if (Validator.isBlank(configuration)) {
			return new String[] {StringPool.BLANK};
		}

		JSONObject configurationObject = JSONFactoryUtil.createJSONObject(
			configuration);

		if (configurationObject == null) {
			return new String[] {StringPool.BLANK};
		}

		JSONArray section = configurationObject.getJSONArray(key);

		if (section == null) {
			return new String[0];
		}
		
		String[] array = new String[section.length()];

		for (int i = 0; i < section.length(); i++) {
			array[i] = section.getJSONObject(i).toString(4);
		}

		return array;
	}

}