package com.liferay.portal.search.tuning.gsearch.impl.util;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GSearchJsonUtil {

	public static Optional<int[]> getIntArrayOptional(
		JSONObject jsonObject, String key) {

		JSONArray jsonArray = jsonObject.getJSONArray(key);

		if ((jsonArray == null) || (jsonArray.length() == 0)) {
			return Optional.empty();
		}

		int[] intArray = new int[jsonArray.length()];

		for (int i = 0; i < jsonArray.length(); i++) {
			intArray[i] = jsonArray.getInt(i);
		}

		return Optional.of(intArray);
	}

	public static Optional<long[]> getLongArrayOptional(
		JSONObject jsonObject, String key) {

		JSONArray jsonArray = jsonObject.getJSONArray(key);

		if ((jsonArray == null) || (jsonArray.length() == 0)) {
			return Optional.empty();
		}

		long[] longArray = new long[jsonArray.length()];

		for (int i = 0; i < jsonArray.length(); i++) {
			longArray[i] = jsonArray.getInt(i);
		}

		return Optional.of(longArray);
	}

	public static Optional<String[]> getStringArrayOptional(
		JSONObject jsonObject, String key) {

		JSONArray jsonArray = jsonObject.getJSONArray(key);

		String[] stringArray = jsonArrayToStringArray(jsonArray);

		if (stringArray == null) {
			return Optional.empty();
		}

		return Optional.of(stringArray);
	}

	public static String[] jsonArrayToStringArray(JSONArray jsonArray) {
		if ((jsonArray == null) || (jsonArray.length() == 0)) {
			return new String[0];
		}

		String[] stringArray = new String[jsonArray.length()];

		for (int i = 0; i < jsonArray.length(); i++) {
			stringArray[i] = jsonArray.getString(i);
		}

		return stringArray;
	}

	public static List<String> jsonArrayToStringList(JSONArray jsonArray) {
		List<String> list = new ArrayList<>();

		if ((jsonArray == null) || (jsonArray.length() == 0)) {
			return list;
		}

		for (int i = 0; i < jsonArray.length(); i++) {
			list.add(jsonArray.getString(i));
		}

		return list;
	}

}