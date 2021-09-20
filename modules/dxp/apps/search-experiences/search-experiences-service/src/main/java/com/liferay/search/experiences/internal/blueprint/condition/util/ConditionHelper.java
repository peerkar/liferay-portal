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

package com.liferay.search.experiences.internal.blueprint.condition.util;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.search.experiences.blueprint.parameter.SXPParameter;
import com.liferay.search.experiences.blueprint.parameter.SXPParameterData;
import com.liferay.search.experiences.blueprint.parameter.exception.SXPParameterException;
import com.liferay.search.experiences.blueprint.template.variable.SXPBlueprintTemplateVariableParser;
import com.liferay.search.experiences.internal.problem.ProblemUtil;

import java.util.Optional;
import java.util.function.Function;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(immediate = true, service = ConditionHelper.class)
public class ConditionHelper {

	public boolean evaluate(
		Function<JSONObject, SXPParameter.EvaluationVisitor> function,
		boolean negate, JSONObject jsonObject,
		SXPParameterData sxpParameterData) {

		Optional<SXPParameter> parameterOptional = getParameterOptional(
			sxpParameterData, jsonObject);

		if (!parameterOptional.isPresent()) {
			return false;
		}

		Optional<Object> parsedValueOptional = _getParsedValue(
			jsonObject, sxpParameterData);

		if (!parsedValueOptional.isPresent()) {
			return false;
		}

		jsonObject.put("value", parsedValueOptional.get());

		boolean match = _evaluate(
			parameterOptional.get(), function.apply(jsonObject));

		if (negate) {
			return !match;
		}

		return match;
	}

	public Optional<SXPParameter> getParameterOptional(
		SXPParameterData sxpParameterData, JSONObject jsonObject) {

		return sxpParameterData.
			getSXPParameterOptionalByNameTemplateVariableName(
				jsonObject.getString("parameter_name"));
	}

	private boolean _evaluate(
		SXPParameter parameter, SXPParameter.EvaluationVisitor visitor) {

		try {
			return parameter.accept(visitor);
		}
		catch (SXPParameterException sxpParameterException) {
			_log.error(
				sxpParameterException.getMessage(), sxpParameterException);

			ProblemUtil.addProblem(sxpParameterException.getProblem());

			return false;
		}
	}

	private Optional<Object> _getParsedValue(
		JSONObject jsonObject, SXPParameterData sxpParameterData) {

		return _sxpBlueprintTemplateVariableParser.parse(
			getClass().getName(), jsonObject.get("value"), sxpParameterData);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ConditionHelper.class);

	@Reference
	private SXPBlueprintTemplateVariableParser
		_sxpBlueprintTemplateVariableParser;

}