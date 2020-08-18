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

package com.liferay.portal.search.tuning.gsearch.impl.internal.aggregations.facet.handler;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.aggregation.bucket.Bucket;
import com.liferay.portal.search.aggregation.bucket.TermsAggregationResult;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.FacetConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.constants.JSONResponseKeys;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.message.Message;
import com.liferay.portal.search.tuning.gsearch.message.Severity;
import com.liferay.portal.search.tuning.gsearch.parameter.SearchParameterData;
import com.liferay.portal.search.tuning.gsearch.parameter.StringArrayParameter;
import com.liferay.portal.search.tuning.gsearch.spi.aggregation.facet.FacetHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, property = "name=file_extension",
	service = FacetHandler.class
)
public class FileExtensionFacetHandler
	extends BaseFacetHandler implements FacetHandler {

	@Override
	public void addSearchParameter(
		HttpServletRequest httpServletRequest,
		SearchParameterData searchParameterData,
		JSONObject configurationJsonObject) {

		if (!_validateConfiguration(
				searchParameterData, configurationJsonObject)) {

			return;
		}

		JSONObject handlerParametersJsonObject =
			configurationJsonObject.getJSONObject(
				FacetConfigurationKeys.HANDLER_PARAMETERS.getJsonKey());

		String parameterName = configurationJsonObject.getString(
			FacetConfigurationKeys.PARAMETER_NAME.getJsonKey());

		boolean multiValue = configurationJsonObject.getBoolean(
			FacetConfigurationKeys.MULTI_VALUE.getJsonKey(), true);

		String[] requestValues;

		if (multiValue) {
			requestValues = ParamUtil.getStringValues(
				httpServletRequest, parameterName);
		}
		else {
			requestValues = new String[] {
				ParamUtil.getString(httpServletRequest, parameterName)
			};
		}

		JSONArray valueAggregationsJsonArray =
			handlerParametersJsonObject.getJSONArray(
				FacetConfigurationKeys.VALUE_AGGREGATIONS.getJsonKey());

		List<String> values = new ArrayList<>();

		for (String requestValue : requestValues) {
			String[] translatedValueArray = null;

			for (int i = 0; i < valueAggregationsJsonArray.length(); i++) {
				try {
					JSONObject valueAggregationJson =
						valueAggregationsJsonArray.getJSONObject(i);

					if (valueAggregationJson.getString(
							FacetConfigurationKeys.VALUE_AGGREGATION_KEY.
								getJsonKey()).equals(
									requestValue)) {

						translatedValueArray = valueAggregationJson.getString(
							FacetConfigurationKeys.VALUE_AGGREGATION_VALUES.
								getJsonKey()
						).split(
							","
						);

						break;
					}
				}
				catch (Exception e) {
					_log.error(e.getMessage(), e);
				}
			}

			if (translatedValueArray != null) {
				Collections.addAll(values, translatedValueArray);
			}
			else {
				values.add(requestValue);
			}
		}

		searchParameterData.addParameter(
			new StringArrayParameter(
				parameterName, null, null,
				values.stream(
				).toArray(
					String[]::new
				)));
	}

	@Override
	public Optional<JSONObject> getResultsObject(
		SearchRequestContext queryContext, AggregationResult aggregationResult,
		JSONObject configurationJsonObject) {

		TermsAggregationResult termsAggregationResult =
			(TermsAggregationResult)aggregationResult;

		JSONArray termsArray = null;

		try {
			JSONObject handlerParametersJsonObject =
				configurationJsonObject.getJSONObject(
					FacetConfigurationKeys.HANDLER_PARAMETERS.getJsonKey());

			JSONArray aggregations = handlerParametersJsonObject.getJSONArray(
				FacetConfigurationKeys.VALUE_AGGREGATIONS.getJsonKey());

			Map<String, Integer> termsMap = new HashMap<>();

			for (Bucket bucket : termsAggregationResult.getBuckets()) {
				if (Validator.isNull(bucket.getKey())) {
					continue;
				}

				boolean mappingFound = false;

				for (int i = 0; i < aggregations.length(); i++) {
					JSONObject aggregation = aggregations.getJSONObject(i);

					String key = aggregation.getString(
						FacetConfigurationKeys.VALUE_AGGREGATION_KEY.
							getJsonKey());
					String[] values = aggregation.getString(
						FacetConfigurationKeys.VALUE_AGGREGATION_VALUES.
							getJsonKey()
					).split(
						","
					);

					for (int j = 0; j < values.length; j++) {
						if (values[j].equals(bucket.getKey())) {
							if (termsMap.get(key) != null) {
								int newValue =
									termsMap.get(key) +
										(int)bucket.getDocCount();
								termsMap.put(key, newValue);
							}
							else {
								termsMap.put(key, (int)bucket.getDocCount());
							}

							mappingFound = true;
						}
					}
				}

				if (!mappingFound) {
					termsMap.put(bucket.getKey(), (int)bucket.getDocCount());
				}
			}

			Map<String, Integer> termMapOrdered = _sort(termsMap);

			termsArray = _createTermsArray(termMapOrdered);
		}
		catch (Exception exception) {
			queryContext.addMessage(
				new Message(
					Severity.ERROR, "core", "core.error.unknown-exception",
					exception.getMessage(), exception, configurationJsonObject,
					null, null));

			_log.error(exception.getMessage(), exception);
		}

		return createResultObject(termsArray, configurationJsonObject);
	}

	private JSONArray _createTermsArray(Map<String, Integer> termsMap) {
		JSONArray termArray = JSONFactoryUtil.createJSONArray();

		for (Map.Entry<String, Integer> entry : termsMap.entrySet()) {
			JSONObject item = JSONFactoryUtil.createJSONObject();

			item.put(JSONResponseKeys.FREQUENCY, entry.getValue());
			item.put(JSONResponseKeys.NAME, entry.getKey());
			item.put(JSONResponseKeys.VALUE, entry.getKey());

			termArray.put(item);
		}

		return termArray;
	}

	private Map<String, Integer> _sort(Map<String, Integer> termsMap)
		throws Exception {

		return termsMap.entrySet(
		).stream(
		).sorted(
			Map.Entry.comparingByValue(Comparator.reverseOrder())
		).collect(
			Collectors.toMap(
				Map.Entry::getKey, Map.Entry::getValue,
				(oldValue, newValue) -> oldValue, LinkedHashMap::new)
		);
	}

	private boolean _validateConfiguration(
		SearchParameterData searchParameterData,
		JSONObject configurationJsonObject) {

		boolean valid = true;

		JSONObject handlerParametersJsonObject =
			configurationJsonObject.getJSONObject(
				FacetConfigurationKeys.HANDLER_PARAMETERS.getJsonKey());

		if (Validator.isNull(handlerParametersJsonObject)) {
			searchParameterData.addMessage(
				new Message(
					Severity.ERROR, "core",
					"core.error.undefined-facet-handler-parameters", null, null,
					configurationJsonObject,
					FacetConfigurationKeys.HANDLER_PARAMETERS.getJsonKey(),
					null));
			valid = false;
		}

		if ((handlerParametersJsonObject != null) &
			Validator.isNull(
				handlerParametersJsonObject.get(
					FacetConfigurationKeys.VALUE_AGGREGATIONS.getJsonKey()))) {

			searchParameterData.addMessage(
				new Message(
					Severity.ERROR, "core",
					"core.error.undefined-facet-handler-aggregations", null,
					null, configurationJsonObject,
					FacetConfigurationKeys.VALUE_AGGREGATIONS.getJsonKey(),
					null));

			valid = false;
		}

		return valid;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FileExtensionFacetHandler.class);

}