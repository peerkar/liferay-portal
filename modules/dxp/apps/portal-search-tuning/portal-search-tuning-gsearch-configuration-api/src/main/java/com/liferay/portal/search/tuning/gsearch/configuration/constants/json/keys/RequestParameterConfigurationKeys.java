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

package com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author Petteri Karttunen
 */
public enum RequestParameterConfigurationKeys {

	DATE_FORMAT("date_format"), DEFAULT("default"), MAX_VALUE("max_value"),
	MIN_VALUE("min_value"), PARAMETER_NAME("parameter_name"), ROLE("role"),
	TYPE("type");

	public static final RequestParameterConfigurationKeys findByJsonKey(
		String jsonKey) {

		Stream<RequestParameterConfigurationKeys>
			requestParameterConfigurationKeysStream = Arrays.stream(
				RequestParameterConfigurationKeys.values());

		return requestParameterConfigurationKeysStream.filter(
			value -> value._jsonKey.equals(jsonKey)
		).findFirst(
		).orElse(
			null
		);
	}

	public String getJsonKey() {
		return _jsonKey;
	}

	private RequestParameterConfigurationKeys(String jsonKey) {
		_jsonKey = jsonKey;
	}

	private final String _jsonKey;

}