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

package com.liferay.search.experiences.internal.blueprint.condition.visitor;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.search.experiences.blueprint.parameter.BooleanSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.DateSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.DoubleSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.FloatSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.IntegerSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.LongSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.SXPParameter;
import com.liferay.search.experiences.blueprint.parameter.StringSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.exception.SXPParameterException;

/**
 * @author Petteri Karttunen
 */
public class EqualsVisitor
	extends BaseEvaluationVisitor implements SXPParameter.EvaluationVisitor {

	public EqualsVisitor(JSONObject conditionJSONObject) {
		super(conditionJSONObject);
	}

	@Override
	public boolean visit(BooleanSXPParameter parameter)
		throws SXPParameterException {

		Boolean value = conditionJSONObject.getBoolean("value");

		Boolean parameterValue = parameter.getValue();

		if (value.booleanValue() == parameterValue.booleanValue()) {
			return true;
		}

		return false;
	}

	@Override
	public boolean visit(DateSXPParameter parameter)
		throws SXPParameterException {

		String dateString1 = conditionJSONObject.getString("value");

		String dateFormatString = conditionJSONObject.getString("date_format");

		String dateString2 = getDateAsString(
			parameter.getValue(), dateFormatString);

		return dateString1.equals(dateString2);
	}

	@Override
	public boolean visit(DoubleSXPParameter parameter)
		throws SXPParameterException {

		Double value = conditionJSONObject.getDouble("value");

		return parameter.equalsTo(value);
	}

	@Override
	public boolean visit(FloatSXPParameter parameter)
		throws SXPParameterException {

		return parameter.equalsTo(
			GetterUtil.getFloat(conditionJSONObject.get("value")));
	}

	@Override
	public boolean visit(IntegerSXPParameter parameter)
		throws SXPParameterException {

		return parameter.equalsTo(conditionJSONObject.getInt("value"));
	}

	@Override
	public boolean visit(LongSXPParameter parameter)
		throws SXPParameterException {

		return parameter.equalsTo(conditionJSONObject.getLong("value"));
	}

	@Override
	public boolean visit(StringSXPParameter parameter)
		throws SXPParameterException {

		String parameterValue = parameter.getValue();

		return parameterValue.equals(conditionJSONObject.getString("value"));
	}

}