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
public enum AdvancedConfigurationKeys {

	EXCLUDE_QUERY_CONTRIBUTORS("exclude_query_contributors"),
	EXCLUDE_QUERY_POST_PROCESSORS("exclude_query_post_processors"),
	FETCH_SOURCE("fetch_source"), SOURCE_EXCLUDES("source_excludes"),
	SOURCE_INCLUDES("source_includes");

	public static final AdvancedConfigurationKeys findByJsonKey(
		String jsonKey) {

		Stream<AdvancedConfigurationKeys> advancedConfigurationKeysStream =
			Arrays.stream(AdvancedConfigurationKeys.values());

		return advancedConfigurationKeysStream.filter(
			value -> value._jsonKey.equals(jsonKey)
		).findFirst(
		).orElse(
			null
		);
	}

	public String getJsonKey() {
		return _jsonKey;
	}

	private AdvancedConfigurationKeys(String jsonKey) {
		_jsonKey = jsonKey;
	}

	private final String _jsonKey;

}