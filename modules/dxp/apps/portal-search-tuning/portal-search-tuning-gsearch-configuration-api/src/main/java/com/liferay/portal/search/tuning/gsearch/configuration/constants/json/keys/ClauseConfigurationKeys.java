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
public enum ClauseConfigurationKeys {

	ANALYZER("analyzer"), CLAUSES("clauses"), CONDITIONS("conditions"),
	CONFIGURATION("configuration"), CONTEXT("context"),
	DATE_FORMAT("date_format"), ENABLED("enabled"),
	EVALUATION_TYPE("evaluation_type"), HANDLER("handler"),
	MATCH_VALUE("match_value"), OCCUR("occur"), OPERATOR("operator"),
	PARAMETER_NAME("parameter_name"), QUERY("query"), TYPE("type");

	public static final ClauseConfigurationKeys findByJsonKey(String jsonKey) {
		Stream<ClauseConfigurationKeys> clauseConfigurationKeysStream =
			Arrays.stream(ClauseConfigurationKeys.values());

		return clauseConfigurationKeysStream.filter(
			value -> value._jsonKey.equals(jsonKey)
		).findFirst(
		).orElse(
			null
		);
	}

	public String getJsonKey() {
		return _jsonKey;
	}

	private ClauseConfigurationKeys(String jsonKey) {
		_jsonKey = jsonKey;
	}

	private final String _jsonKey;

}