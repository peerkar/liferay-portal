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

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.search.experiences.blueprint.parameter.DateSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.DoubleSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.FloatSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.IntegerSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.LongSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.SXPParameter;
import com.liferay.search.experiences.blueprint.parameter.exception.SXPParameterException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

/**
 * @author Petteri Karttunen
 */
public class InRangeVisitor
	extends BaseEvaluationVisitor implements SXPParameter.EvaluationVisitor {

	public InRangeVisitor(JSONObject conditionJSONObject) {
		super(conditionJSONObject);
	}

	@Override
	public boolean visit(DateSXPParameter parameter)
		throws SXPParameterException {

		JSONArray jsonArray = getConditionValueJSONArray(conditionJSONObject);

		_checkRangeValue(jsonArray);

		String dateFormatString = getDateFormatString();

		String dateString = conditionJSONObject.getString("value");

		try {
			DateFormat dateFormat = new SimpleDateFormat(dateFormatString);

			String lowerBoundString = jsonArray.getString(0);
			String upperBoundString = jsonArray.getString(1);

			Date lowerBound = dateFormat.parse(lowerBoundString);
			Date upperBound = dateFormat.parse(upperBoundString);

			Date parameterValue = parameter.getValue();

			boolean inRange = false;

			if (parameterValue.after(lowerBound) &&
				parameterValue.before(upperBound)) {

				inRange = true;
			}

			return inRange;
		}
		catch (Exception exception) {
			throw new SXPParameterException(
				toErrorMessage(
					getClass().getName(), "date-parsing-failed", "value",
					dateString, exception));
		}
	}

	@Override
	public boolean visit(DoubleSXPParameter parameter)
		throws SXPParameterException {

		JSONArray jsonArray = getConditionValueJSONArray(conditionJSONObject);

		_checkRangeValue(jsonArray);

		Double lowerBound = jsonArray.getDouble(0);
		Double upperBound = jsonArray.getDouble(1);

		Double parameterValue = parameter.getValue();

		boolean inRange = false;

		if ((parameterValue.compareTo(lowerBound) >= 0) &&
			(parameterValue.compareTo(upperBound) <= 0)) {

			inRange = true;
		}

		return inRange;
	}

	@Override
	public boolean visit(FloatSXPParameter parameter)
		throws SXPParameterException {

		JSONArray jsonArray = getConditionValueJSONArray(conditionJSONObject);

		_checkRangeValue(jsonArray);

		Float lowerBound = GetterUtil.getFloat(jsonArray.get(0));
		Float upperBound = GetterUtil.getFloat(jsonArray.get(1));

		Float parameterValue = parameter.getValue();

		boolean inRange = false;

		if ((parameterValue.compareTo(lowerBound) >= 0) &&
			(parameterValue.compareTo(upperBound) <= 0)) {

			inRange = true;
		}

		return inRange;
	}

	@Override
	public boolean visit(IntegerSXPParameter parameter)
		throws SXPParameterException {

		JSONArray jsonArray = getConditionValueJSONArray(conditionJSONObject);

		_checkRangeValue(jsonArray);

		Integer lowerBound = jsonArray.getInt(0);
		Integer upperBound = jsonArray.getInt(1);

		Integer parameterValue = parameter.getValue();

		boolean inRange = false;

		if ((parameterValue.compareTo(lowerBound) >= 0) &&
			(parameterValue.compareTo(upperBound) <= 0)) {

			inRange = true;
		}

		return inRange;
	}

	@Override
	public boolean visit(LongSXPParameter parameter)
		throws SXPParameterException {

		JSONArray jsonArray = getConditionValueJSONArray(conditionJSONObject);

		_checkRangeValue(jsonArray);

		Long parameterValue = parameter.getValue();

		boolean inRange = false;

		if ((parameterValue.compareTo(jsonArray.getLong(0)) >= 0) &&
			(parameterValue.compareTo(jsonArray.getLong(1)) <= 0)) {

			inRange = true;
		}

		return inRange;
	}

	private void _checkRangeValue(JSONArray jsonArray)
		throws SXPParameterException {

		if (jsonArray.length() != 2) {
			throw new SXPParameterException(
				toErrorMessage(
					getClass().getName(), "invalid-range-value", "value",
					jsonArray.toString(),
					new Throwable("Invalid range value")));
		}
	}

}