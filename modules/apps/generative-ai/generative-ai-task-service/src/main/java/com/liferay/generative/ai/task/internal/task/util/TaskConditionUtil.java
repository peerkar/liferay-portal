/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.task.util;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.StringUtil;

/**
 * @author Petteri Karttunen
 */
public class TaskConditionUtil {

	public static boolean validateCondition(
		JSONObject conditionJSONObject, String text) {

		for (String key : conditionJSONObject.keySet()) {
			if (key.equals("allOf")) {
				return _validateAllOf(
					conditionJSONObject.getJSONArray("allOf"), text);
			}
			else if (key.equals("anyOf")) {
				return _validateAnyOf(
					conditionJSONObject.getJSONArray("anyOf"), text);
			}
			else if (key.equals("endsWith")) {
				return _validateEndsWith(
					conditionJSONObject.getJSONObject("endsWith"), text);
			}
			else if (key.equals("equals")) {
				return _validateEquals(
					conditionJSONObject.getJSONObject("equals"), text);
			}
			else if (key.equals("contains")) {
				return _validateContains(
					conditionJSONObject.getJSONObject("contains"), text);
			}
			else if (key.equals("noneOf")) {
				return _validateNoneOf(
					conditionJSONObject.getJSONArray("noneOf"), text);
			}
			else if (key.equals("startsWith")) {
				return _validateStartsWith(
					conditionJSONObject.getJSONObject("startsWith"), text);
			}

			return false;
		}

		return false;
	}

	private static boolean _validateAllOf(JSONArray jsonArray, String text) {
		for (int i = 0; i < jsonArray.length(); i++) {
			if (!validateCondition(jsonArray.getJSONObject(i), text)) {
				return false;
			}
		}

		return true;
	}

	private static boolean _validateAnyOf(JSONArray jsonArray, String text) {
		for (int i = 0; i < jsonArray.length(); i++) {
			if (validateCondition(jsonArray.getJSONObject(i), text)) {
				return true;
			}
		}

		return false;
	}

	private static boolean _validateContains(
		JSONObject jsonObject, String text) {

		String lowerCaseText = StringUtil.toLowerCase(text);
		String lowerCaseValue = StringUtil.toLowerCase(
			jsonObject.getString("value"));

		if (lowerCaseText.contains(lowerCaseValue)) {
			return true;
		}

		return false;
	}

	private static boolean _validateEndsWith(
		JSONObject jsonObject, String text) {

		return StringUtil.startsWith(text, jsonObject.getString("value"));
	}

	private static boolean _validateEquals(JSONObject jsonObject, String text) {
		return StringUtil.equalsIgnoreCase(text, jsonObject.getString("value"));
	}

	private static boolean _validateNoneOf(JSONArray jsonArray, String text) {
		for (int i = 0; i < jsonArray.length(); i++) {
			if (validateCondition(jsonArray.getJSONObject(i), text)) {
				return false;
			}
		}

		return true;
	}

	private static boolean _validateStartsWith(
		JSONObject jsonObject, String text) {

		return StringUtil.startsWith(text, jsonObject.getString("value"));
	}

}