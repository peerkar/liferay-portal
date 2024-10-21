/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.rest.client.serdes.v1_0;

import com.liferay.generative.ai.rest.client.dto.v1_0.GenerativeAIResponse;
import com.liferay.generative.ai.rest.client.json.BaseJSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * @author Petteri Karttunen
 * @generated
 */
@Generated("")
public class GenerativeAIResponseSerDes {

	public static GenerativeAIResponse toDTO(String json) {
		GenerativeAIResponseJSONParser generativeAIResponseJSONParser =
			new GenerativeAIResponseJSONParser();

		return generativeAIResponseJSONParser.parseToDTO(json);
	}

	public static GenerativeAIResponse[] toDTOs(String json) {
		GenerativeAIResponseJSONParser generativeAIResponseJSONParser =
			new GenerativeAIResponseJSONParser();

		return generativeAIResponseJSONParser.parseToDTOs(json);
	}

	public static String toJSON(GenerativeAIResponse generativeAIResponse) {
		if (generativeAIResponse == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (generativeAIResponse.getDebugInfo() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"debugInfo\": ");

			if (generativeAIResponse.getDebugInfo() instanceof String) {
				sb.append("\"");
				sb.append((String)generativeAIResponse.getDebugInfo());
				sb.append("\"");
			}
			else {
				sb.append(generativeAIResponse.getDebugInfo());
			}
		}

		if (generativeAIResponse.getOutput() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"output\": ");

			if (generativeAIResponse.getOutput() instanceof String) {
				sb.append("\"");
				sb.append((String)generativeAIResponse.getOutput());
				sb.append("\"");
			}
			else {
				sb.append(generativeAIResponse.getOutput());
			}
		}

		if (generativeAIResponse.getTook() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"took\": ");

			sb.append("\"");

			sb.append(_escape(generativeAIResponse.getTook()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		GenerativeAIResponseJSONParser generativeAIResponseJSONParser =
			new GenerativeAIResponseJSONParser();

		return generativeAIResponseJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(
		GenerativeAIResponse generativeAIResponse) {

		if (generativeAIResponse == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (generativeAIResponse.getDebugInfo() == null) {
			map.put("debugInfo", null);
		}
		else {
			map.put(
				"debugInfo",
				String.valueOf(generativeAIResponse.getDebugInfo()));
		}

		if (generativeAIResponse.getOutput() == null) {
			map.put("output", null);
		}
		else {
			map.put("output", String.valueOf(generativeAIResponse.getOutput()));
		}

		if (generativeAIResponse.getTook() == null) {
			map.put("took", null);
		}
		else {
			map.put("took", String.valueOf(generativeAIResponse.getTook()));
		}

		return map;
	}

	public static class GenerativeAIResponseJSONParser
		extends BaseJSONParser<GenerativeAIResponse> {

		@Override
		protected GenerativeAIResponse createDTO() {
			return new GenerativeAIResponse();
		}

		@Override
		protected GenerativeAIResponse[] createDTOArray(int size) {
			return new GenerativeAIResponse[size];
		}

		@Override
		protected boolean parseMaps(String jsonParserFieldName) {
			if (Objects.equals(jsonParserFieldName, "debugInfo")) {
				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "output")) {
				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "took")) {
				return false;
			}

			return false;
		}

		@Override
		protected void setField(
			GenerativeAIResponse generativeAIResponse,
			String jsonParserFieldName, Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "debugInfo")) {
				if (jsonParserFieldValue != null) {
					generativeAIResponse.setDebugInfo(
						(Object)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "output")) {
				if (jsonParserFieldValue != null) {
					generativeAIResponse.setOutput(
						(Object)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "took")) {
				if (jsonParserFieldValue != null) {
					generativeAIResponse.setTook((String)jsonParserFieldValue);
				}
			}
		}

	}

	private static String _escape(Object object) {
		String string = String.valueOf(object);

		for (String[] strings : BaseJSONParser.JSON_ESCAPE_STRINGS) {
			string = string.replace(strings[0], strings[1]);
		}

		return string;
	}

	private static String _toJSON(Map<String, ?> map) {
		StringBuilder sb = new StringBuilder("{");

		@SuppressWarnings("unchecked")
		Set set = map.entrySet();

		@SuppressWarnings("unchecked")
		Iterator<Map.Entry<String, ?>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, ?> entry = iterator.next();

			sb.append("\"");
			sb.append(entry.getKey());
			sb.append("\": ");

			Object value = entry.getValue();

			sb.append(_toJSON(value));

			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("}");

		return sb.toString();
	}

	private static String _toJSON(Object value) {
		if (value instanceof Map) {
			return _toJSON((Map)value);
		}

		Class<?> clazz = value.getClass();

		if (clazz.isArray()) {
			StringBuilder sb = new StringBuilder("[");

			Object[] values = (Object[])value;

			for (int i = 0; i < values.length; i++) {
				sb.append(_toJSON(values[i]));

				if ((i + 1) < values.length) {
					sb.append(", ");
				}
			}

			sb.append("]");

			return sb.toString();
		}

		if (value instanceof String) {
			return "\"" + _escape(value) + "\"";
		}

		return String.valueOf(value);
	}

}