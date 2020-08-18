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
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.collector.FacetCollector;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.aggregation.bucket.Bucket;
import com.liferay.portal.search.aggregation.bucket.TermsAggregationResult;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.FacetConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.constants.JSONResponseKeys;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.parameter.SearchParameterData;
import com.liferay.portal.search.tuning.gsearch.parameter.StringArrayParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.StringParameter;
import com.liferay.portal.search.tuning.gsearch.spi.aggregation.facet.FacetHandler;

import java.util.Collection;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Petteri Karttunen
 */
public abstract class BaseFacetHandler implements FacetHandler {

	@Override
	public void addSearchParameter(
		HttpServletRequest httpServletRequest,
		SearchParameterData searchParameterData,
		JSONObject configurationJsonObject) {

		String parameterName = configurationJsonObject.getString(
			FacetConfigurationKeys.PARAMETER_NAME.getJsonKey());

		boolean multiValue = configurationJsonObject.getBoolean(
			FacetConfigurationKeys.MULTI_VALUE.getJsonKey(), true);

		if (multiValue) {
			String[] value = ParamUtil.getStringValues(
				httpServletRequest, parameterName, null);

			if (value != null) {
				searchParameterData.addParameter(
					new StringArrayParameter(parameterName, null, null, value));
			}
		}
		else {
			String value = ParamUtil.getString(
				httpServletRequest, parameterName);

			if (!Validator.isBlank(value)) {
				searchParameterData.addParameter(
					new StringParameter(parameterName, null, null, value));
			}
		}
	}

	@Override
	public Optional<JSONObject> getResultsObject(
		SearchRequestContext queryContext, AggregationResult aggregationResult,
		JSONObject configurationJsonObject) {

		TermsAggregationResult termsAggregationResult =
			(TermsAggregationResult)aggregationResult;

		JSONArray termsArray = JSONFactoryUtil.createJSONArray();

		for (Bucket bucket : termsAggregationResult.getBuckets()) {
			JSONObject item = JSONFactoryUtil.createJSONObject();

			item.put(JSONResponseKeys.FREQUENCY, bucket.getDocCount());
			item.put(JSONResponseKeys.NAME, bucket.getKey());
			item.put(JSONResponseKeys.VALUE, bucket.getKey());

			termsArray.put(item);
		}

		return createResultObject(termsArray, configurationJsonObject);
	}

	protected Optional<JSONObject> createResultObject(
		JSONArray termsArray, JSONObject configurationJsonObject) {

		if ((termsArray == null) || (termsArray.length() == 0)) {
			return Optional.empty();
		}

		JSONObject resultObject = JSONFactoryUtil.createJSONObject();

		resultObject.put(
			JSONResponseKeys.PARAMETER_NAME,
			configurationJsonObject.getString(
				FacetConfigurationKeys.PARAMETER_NAME.getJsonKey()));

		resultObject.put(JSONResponseKeys.VALUES, termsArray);

		return Optional.of(resultObject);
	}

	protected FacetCollector getFacetCollector(
		Collection<Facet> facets, String fieldName) {

		for (Facet facet : facets) {
			if (facet.isStatic()) {
				continue;
			}

			if (facet.getFieldName().equals(fieldName)) {
				return facet.getFacetCollector();
			}
		}

		return null;
	}

}