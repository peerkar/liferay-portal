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

package com.liferay.portal.search.tuning.gsearch.impl.internal.parameter.visitor;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.ClauseConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.exception.ParameterEvaluationException;
import com.liferay.portal.search.tuning.gsearch.impl.util.ClauseConditionUtil;
import com.liferay.portal.search.tuning.gsearch.message.Message;
import com.liferay.portal.search.tuning.gsearch.message.Severity;
import com.liferay.portal.search.tuning.gsearch.parameter.BooleanParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.DateParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.DoubleParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.FloatParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.IntegerArrayParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.IntegerParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.LongArrayParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.LongParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.Parameter;
import com.liferay.portal.search.tuning.gsearch.parameter.StringArrayParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.StringParameter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

/**
 * @author Petteri Karttunen
 */
public class InRangeVisitor implements ClauseConditionEvaluationVisitor {

	public InRangeVisitor(JSONObject conditionJsonObject, boolean not) {
		_conditionJsonObject = conditionJsonObject;
		_not = not;
	}

	@Override
	public boolean visit(BooleanParameter parameter)
		throws ParameterEvaluationException {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visit(DateParameter parameter)
		throws ParameterEvaluationException {

		JSONArray jsonArray = ClauseConditionUtil.getClauseConditionArrayValue(
			_conditionJsonObject);

		_checkRangeValue(jsonArray);

		String dateFormatString = _conditionJsonObject.getString(
			ClauseConfigurationKeys.DATE_FORMAT.getJsonKey());

		String dateString = _conditionJsonObject.getString(
			ClauseConfigurationKeys.MATCH_VALUE.getJsonKey());

		if (Validator.isNull(dateFormatString)) {
			throw new ParameterEvaluationException(
				new Message(
					Severity.ERROR, "core",
					"core.error.clause-condition-date-format-missing",
					_conditionJsonObject,
					ClauseConfigurationKeys.DATE_FORMAT.getJsonKey(),
					dateFormatString));
		}

		try {
			DateFormat dateFormat = new SimpleDateFormat(dateFormatString);

			String lowerBoundString = jsonArray.getString(0);
			String upperBoundString = jsonArray.getString(1);

			Date lowerBound = dateFormat.parse(lowerBoundString);
			Date upperBound = dateFormat.parse(upperBoundString);

			if (!(parameter.getValue().before(lowerBound) &&
				  !parameter.getValue().after(upperBound))) {

				return true;
			}

			return false;
		}
		catch (IllegalArgumentException | NullPointerException | ParseException
					e) {

			throw new ParameterEvaluationException(
				new Message(
					Severity.ERROR, "core",
					"core.error.clause-condition-date-parsing-error",
					e.getMessage(), e, _conditionJsonObject,
					ClauseConfigurationKeys.MATCH_VALUE.getJsonKey(),
					dateString));
		}
	}

	@Override
	public boolean visit(DoubleParameter parameter)
		throws ParameterEvaluationException {

		JSONArray jsonArray = ClauseConditionUtil.getClauseConditionArrayValue(
			_conditionJsonObject);

		_checkRangeValue(jsonArray);

		Double lowerBound = jsonArray.getDouble(0);
		Double upperBound = jsonArray.getDouble(1);

		if ((parameter.getValue().compareTo(lowerBound) >= 0) &&
			(parameter.getValue().compareTo(upperBound) <= 0)) {

			return true;
		}

		return false;
	}

	@Override
	public boolean visit(FloatParameter parameter)
		throws ParameterEvaluationException {

		JSONArray jsonArray = ClauseConditionUtil.getClauseConditionArrayValue(
			_conditionJsonObject);

		_checkRangeValue(jsonArray);

		Float lowerBound = GetterUtil.getFloat(jsonArray.get(0));
		Float upperBound = GetterUtil.getFloat(jsonArray.get(1));

		if ((parameter.getValue().compareTo(lowerBound) >= 0) &&
			(parameter.getValue().compareTo(upperBound) <= 0)) {

			return true;
		}

		return false;
	}

	@Override
	public boolean visit(IntegerArrayParameter parameter)
		throws ParameterEvaluationException {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visit(IntegerParameter parameter)
		throws ParameterEvaluationException {

		JSONArray jsonArray = ClauseConditionUtil.getClauseConditionArrayValue(
			_conditionJsonObject);

		_checkRangeValue(jsonArray);

		Integer lowerBound = jsonArray.getInt(0);
		Integer upperBound = jsonArray.getInt(1);

		if ((parameter.getValue().compareTo(lowerBound) >= 0) &&
			(parameter.getValue().compareTo(upperBound) <= 0)) {

			return true;
		}

		return false;
	}

	@Override
	public boolean visit(LongArrayParameter parameter)
		throws ParameterEvaluationException {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visit(LongParameter parameter)
		throws ParameterEvaluationException {

		JSONArray jsonArray = ClauseConditionUtil.getClauseConditionArrayValue(
			_conditionJsonObject);

		_checkRangeValue(jsonArray);

		Long lowerBound = jsonArray.getLong(0);
		Long upperBound = jsonArray.getLong(1);

		if ((parameter.getValue().compareTo(lowerBound) >= 0) &&
			(parameter.getValue().compareTo(upperBound) <= 0)) {

			return true;
		}

		return false;
	}

	@Override
	public boolean visit(Parameter parameter) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visit(StringArrayParameter parameter)
		throws ParameterEvaluationException {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visit(StringParameter parameter)
		throws ParameterEvaluationException {

		throw new UnsupportedOperationException();
	}

	private void _checkRangeValue(JSONArray jsonArray)
		throws ParameterEvaluationException {

		if (jsonArray.length() != 2) {
			throw new ParameterEvaluationException(
				new Message(
					Severity.ERROR, "core",
					"core.error.invalid-clause-condition-range-value", null,
					null, _conditionJsonObject,
					ClauseConfigurationKeys.MATCH_VALUE.getJsonKey(),
					jsonArray.toString()));
		}
	}

	private final JSONObject _conditionJsonObject;

	boolean _not;

}