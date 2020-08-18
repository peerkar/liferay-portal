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

package com.liferay.portal.search.tuning.gsearch.configuration.constants.json.values;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author Petteri Karttunen
 */
public enum RequestParameterRoles {

	EXPLAIN("explain"), INCLUDE_RESPONSE_STRING("include_response_string"),
	KEYWORDS("keywords"), PAGE("page"), SORT_DIRECTION("sort_direction"),
	SORT_FIELD("sort_field");

	public static final RequestParameterRoles findByjsonValue(
		String jsonValue) {

		Stream<RequestParameterRoles> requestParameterRolesStream =
			Arrays.stream(RequestParameterRoles.values());

		return requestParameterRolesStream.filter(
			value -> value._jsonValue.equals(jsonValue)
		).findFirst(
		).orElse(
			null
		);
	}

	public String getJsonValue() {
		return _jsonValue;
	}

	private RequestParameterRoles(String jsonValue) {
		_jsonValue = jsonValue;
	}

	private final String _jsonValue;

}