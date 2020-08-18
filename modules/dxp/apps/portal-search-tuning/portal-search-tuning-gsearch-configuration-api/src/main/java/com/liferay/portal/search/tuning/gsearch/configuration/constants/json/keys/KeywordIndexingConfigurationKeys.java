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
public enum KeywordIndexingConfigurationKeys {

	BLACKLIST("blacklist"), BLACKLIST_SPLITTER("blacklist_splitter"),
	ENABLED("enabled"), HITS_THRESHOLD("hits_threshold"),
	KEYWORDS_INDEXING_CONFIGURATION("keyword_indexing_configuration");

	public static final KeywordIndexingConfigurationKeys findByJsonKey(
		String jsonKey) {

		Stream<KeywordIndexingConfigurationKeys>
			keywordIndexingConfigurationKeysStream = Arrays.stream(
				KeywordIndexingConfigurationKeys.values());

		return keywordIndexingConfigurationKeysStream.filter(
			value -> value._jsonKey.equals(jsonKey)
		).findFirst(
		).orElse(
			null
		);
	}

	public String getJsonKey() {
		return _jsonKey;
	}

	private KeywordIndexingConfigurationKeys(String jsonKey) {
		_jsonKey = jsonKey;
	}

	private final String _jsonKey;

}