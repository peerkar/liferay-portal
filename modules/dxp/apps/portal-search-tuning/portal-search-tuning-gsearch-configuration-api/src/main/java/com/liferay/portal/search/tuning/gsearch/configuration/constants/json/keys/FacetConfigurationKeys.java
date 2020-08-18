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
public enum FacetConfigurationKeys {

	ENABLED("enabled"), FILTER_MODE("filter_mode"), HANDLER("handler"),
	HANDLER_PARAMETERS("handler_parameters"), INDEX_FIELD("index_field"),
	MULTI_VALUE("multi_value"), MULTI_VALUE_OPERATOR("multi_value_operator"),
	PARAMETER_NAME("parameter_name"), VALUE_AGGREGATION_KEY("key"),
	VALUE_AGGREGATION_VALUES("values"), VALUE_AGGREGATIONS("aggregations");

	public static final FacetConfigurationKeys findByJsonKey(String jsonKey) {
		Stream<FacetConfigurationKeys> facetConfigurationKeysStream =
			Arrays.stream(FacetConfigurationKeys.values());

		return facetConfigurationKeysStream.filter(
			value -> value._jsonKey.equals(jsonKey)
		).findFirst(
		).orElse(
			null
		);
	}

	public String getJsonKey() {
		return _jsonKey;
	}

	private FacetConfigurationKeys(String jsonKey) {
		_jsonKey = jsonKey;
	}

	private final String _jsonKey;

}