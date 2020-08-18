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
public enum SearchConfigurationKeys {

	ADVANCED_CONFIGURATION("advanced_configuration"),
	AGGREGATION_CONFIGURATION("aggregation_configuration"),
	CLAUSE_CONFIGURATION("clause_configuration"),
	HIGHLIGHT_CONFIGURATION("clause_configuration"),
	KEYWORD_INDEXING_CONFIGURATION("keyword_indexing_configuration"),
	KEYWORD_SUGGESTER_CONFIGURATION("keyword_suggestions_configuration"),
	REQUEST_PARAMETER_CONFIGURATION("request_parameter_configuration"),
	SORT_CONFIGURATION("sort_configuration"),
	SPELLCHECKER_CONFIGURATION("spellchecker_configuration");

	public static final SearchConfigurationKeys findByJsonKey(String jsonKey) {
		Stream<SearchConfigurationKeys> searchConfigurationKeysStream =
			Arrays.stream(SearchConfigurationKeys.values());

		return searchConfigurationKeysStream.filter(
			value -> value._jsonKey.equals(jsonKey)
		).findFirst(
		).orElse(
			null
		);
	}

	public String getJsonKey() {
		return _jsonKey;
	}

	private SearchConfigurationKeys(String jsonKey) {
		_jsonKey = jsonKey;
	}

	private final String _jsonKey;

}