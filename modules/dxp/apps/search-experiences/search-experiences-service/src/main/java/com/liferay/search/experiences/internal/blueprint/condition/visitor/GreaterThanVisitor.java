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
import com.liferay.search.experiences.blueprint.parameter.DateSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.DoubleSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.FloatSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.IntegerSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.LongSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.SXPParameter;
import com.liferay.search.experiences.blueprint.parameter.exception.SXPParameterException;

import java.util.Date;

/**
 * @author Petteri Karttunen
 */
public class GreaterThanVisitor
	extends BaseEvaluationVisitor implements SXPParameter.EvaluationVisitor {

	public GreaterThanVisitor(
		JSONObject conditionJSONObject, boolean closedRange) {

		super(conditionJSONObject);

		_closedRange = closedRange;
	}

	@Override
	public boolean visit(DateSXPParameter parameter)
		throws SXPParameterException {

		Date parameterValue = parameter.getValue();

		return parameterValue.after(getDateValue(conditionJSONObject));
	}

	@Override
	public boolean visit(DoubleSXPParameter parameter)
		throws SXPParameterException {

		Double value = conditionJSONObject.getDouble("value");

		Double parameterValue = parameter.getValue();

		boolean greaterThan = false;

		if (_closedRange) {
			if (parameterValue.compareTo(value) >= 0) {
				greaterThan = true;
			}
		}
		else if (parameterValue.compareTo(value) > 0) {
			greaterThan = true;
		}

		return greaterThan;
	}

	@Override
	public boolean visit(FloatSXPParameter parameter)
		throws SXPParameterException {

		Float value = GetterUtil.getFloat(conditionJSONObject.get("value"));

		Float parameterValue = parameter.getValue();

		boolean greaterThan = false;

		if (_closedRange) {
			if (parameterValue.compareTo(value) >= 0) {
				greaterThan = true;
			}
		}
		else if (parameterValue.compareTo(value) > 0) {
			greaterThan = true;
		}

		return greaterThan;
	}

	@Override
	public boolean visit(IntegerSXPParameter parameter)
		throws SXPParameterException {

		Integer value = conditionJSONObject.getInt("value");

		Integer parameterValue = parameter.getValue();

		boolean greaterThan = false;

		if (_closedRange) {
			if (parameterValue.compareTo(value) >= 0) {
				greaterThan = true;
			}
		}
		else if (parameterValue.compareTo(value) > 0) {
			greaterThan = true;
		}

		return greaterThan;
	}

	@Override
	public boolean visit(LongSXPParameter parameter)
		throws SXPParameterException {

		Long value = conditionJSONObject.getLong("value");

		Long parameterValue = parameter.getValue();

		boolean greaterThan = false;

		if (_closedRange) {
			if (parameterValue.compareTo(value) >= 0) {
				greaterThan = true;
			}
		}
		else if (parameterValue.compareTo(value) > 0) {
			greaterThan = true;
		}

		return greaterThan;
	}

	private final boolean _closedRange;

}