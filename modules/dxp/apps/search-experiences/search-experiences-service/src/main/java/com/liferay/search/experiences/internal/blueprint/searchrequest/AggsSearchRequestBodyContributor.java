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

package com.liferay.search.experiences.internal.blueprint.searchrequest;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.search.aggregation.Aggregation;
import com.liferay.portal.search.searcher.SearchRequestBuilder;
import com.liferay.search.experiences.blueprint.parameter.SXPParameterData;
import com.liferay.search.experiences.blueprint.searchrequest.SearchRequestBodyContributor;
import com.liferay.search.experiences.blueprint.template.variable.SXPBlueprintTemplateVariableParser;
import com.liferay.search.experiences.internal.blueprint.aggregation.AggregationWrapper;
import com.liferay.search.experiences.internal.blueprint.aggregation.translator.AggregationTranslator;
import com.liferay.search.experiences.internal.blueprint.aggregation.translator.AggregationTranslatorFactory;
import com.liferay.search.experiences.internal.blueprint.util.SXPJSONUtil;
import com.liferay.search.experiences.internal.problem.ProblemUtil;
import com.liferay.search.experiences.model.SXPBlueprint;

import java.util.Optional;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, property = "name=aggs",
	service = SearchRequestBodyContributor.class
)
public class AggsSearchRequestBodyContributor
	implements SearchRequestBodyContributor {

	@Override
	public void contribute(
		SearchRequestBuilder searchRequestBuilder, SXPBlueprint sxpBlueprint,
		SXPParameterData sxpParameterData) {

		// TODO: _sxpBlueprintConfigurationsJSONHelper no more exists

		Optional<JSONObject> optional =
			_sxpBlueprintConfigurationsJSONHelper.getAggsConfigurationOptional(
				sxpBlueprint);

		if (!optional.isPresent()) {
			return;
		}

		_processAggregations(
			searchRequestBuilder, null, optional.get(), sxpParameterData);
	}

	private void _addAggregation(
		SearchRequestBuilder searchRequestBuilder,
		AggregationWrapper aggregationWrapper) {

		if (aggregationWrapper.isPipeline()) {
			searchRequestBuilder.addPipelineAggregation(
				aggregationWrapper.getPipelineAggregation());
		}
		else {
			searchRequestBuilder.addAggregation(
				aggregationWrapper.getAggregation());
		}
	}

	private void _addChildAggregation(
		AggregationWrapper childAggregationWrapper,
		AggregationWrapper parentAggregationWrapper) {

		if (!parentAggregationWrapper.isPipeline()) {
			Aggregation aggregation = parentAggregationWrapper.getAggregation();

			if (childAggregationWrapper.isPipeline()) {
				aggregation.addPipelineAggregation(
					childAggregationWrapper.getPipelineAggregation());
			}
			else {
				aggregation.addChildAggregation(
					childAggregationWrapper.getAggregation());
			}
		}
	}

	private Optional<AggregationWrapper> _getAggregationOptional(
		JSONObject jsonObject, String name, String type,
		SXPParameterData sxpParameterData) {

		if (!_isEnabled(jsonObject)) {
			return Optional.empty();
		}

		Optional<JSONObject> optional =
			_sxpBlueprintTemplateVariableParser.parseObject(
				getClass().getName(), jsonObject, sxpParameterData);

		if (!optional.isPresent()) {
			return Optional.empty();
		}

		AggregationTranslator aggregationTranslator =
			_aggregationTranslatorFactory.getTranslator(type);

		return aggregationTranslator.translate(
			name, optional.get(), sxpParameterData);
	}

	private boolean _isEnabled(JSONObject jsonObject) {
		return jsonObject.getBoolean("enabled", true);
	}

	private void _processAggregation(
		SearchRequestBuilder searchRequestBuilder,
		JSONObject aggregationJSONObject, String aggregationName,
		AggregationWrapper parentAggregationWrapper,
		SXPParameterData sxpParameterData) {

		JSONObject nameJSONObject = aggregationJSONObject.getJSONObject(
			aggregationName);

		Optional<String> typeOptional = SXPJSONUtil.getFirstKeyOptional(
			nameJSONObject);

		if (!typeOptional.isPresent()) {
			return;
		}

		String type = typeOptional.get();

		JSONObject typeJSONObject = nameJSONObject.getJSONObject(type);

		AggregationWrapper aggregationWrapper;

		try {
			Optional<AggregationWrapper> aggregationWrapperOptional =
				_getAggregationOptional(
					typeJSONObject, aggregationName, type, sxpParameterData);

			if (!aggregationWrapperOptional.isPresent()) {
				return;
			}

			aggregationWrapper = aggregationWrapperOptional.get();
		}
		catch (IllegalArgumentException illegalArgumentException) {
			_log.error(illegalArgumentException);

			ProblemUtil.addInvalidConfigurationValueError(
				getClass().getName(), nameJSONObject, null, type,
				illegalArgumentException);

			return;
		}

		if (!aggregationWrapper.isPipeline()) {
			JSONObject aggsJSONObject = nameJSONObject.getJSONObject("aggs");

			if (aggsJSONObject != null) {
				_processAggregations(
					searchRequestBuilder, aggregationWrapper, aggsJSONObject,
					sxpParameterData);
			}
		}

		if (parentAggregationWrapper == null) {
			_addAggregation(searchRequestBuilder, aggregationWrapper);
		}
		else {
			_addChildAggregation(aggregationWrapper, parentAggregationWrapper);
		}
	}

	private void _processAggregations(
		SearchRequestBuilder searchRequestBuilder,
		AggregationWrapper parentAggregationWrapper,
		JSONObject aggregationJSONObject, SXPParameterData sxpParameterData) {

		Set<String> keySet = aggregationJSONObject.keySet();

		keySet.forEach(
			aggregationName -> _processAggregation(
				searchRequestBuilder, aggregationJSONObject, aggregationName,
				parentAggregationWrapper, sxpParameterData));
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AggsSearchRequestBodyContributor.class);

	@Reference
	private AggregationTranslatorFactory _aggregationTranslatorFactory;

	@Reference
	private SXPBlueprintTemplateVariableParser
		_sxpBlueprintTemplateVariableParser;

}