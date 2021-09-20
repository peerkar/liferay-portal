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

package com.liferay.search.experiences.internal.blueprint.condition.handler;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.search.experiences.blueprint.clause.ConditionHandler;
import com.liferay.search.experiences.blueprint.parameter.SXPParameter;
import com.liferay.search.experiences.blueprint.parameter.SXPParameterData;
import com.liferay.search.experiences.internal.blueprint.condition.util.ConditionHelper;
import com.liferay.search.experiences.internal.blueprint.condition.visitor.InRangeVisitor;

import java.util.function.Function;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, property = "name=not_in_range",
	service = ConditionHandler.class
)
public class NotInRangeConditionHandler implements ConditionHandler {

	@Override
	public boolean isTrue(
		JSONObject jsonObject, SXPParameterData sxpParameterData) {

		Function<JSONObject, SXPParameter.EvaluationVisitor> function =
			conditionJSONObject -> new InRangeVisitor(conditionJSONObject);

		return _conditionHelper.evaluate(
			function, true, jsonObject, sxpParameterData);
	}

	@Reference
	private ConditionHelper _conditionHelper;

}