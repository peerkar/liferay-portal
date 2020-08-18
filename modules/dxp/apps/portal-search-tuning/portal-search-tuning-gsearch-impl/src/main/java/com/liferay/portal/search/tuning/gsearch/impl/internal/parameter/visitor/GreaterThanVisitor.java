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

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.ClauseConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.exception.ParameterEvaluationException;
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
public class GreaterThanVisitor implements ClauseConditionEvaluationVisitor {

	public GreaterThanVisitor(
		JSONObject configurationJsonObject, boolean not, boolean equal) {

		_conditionJsonObject = configurationJsonObject;
		_not = not;
		_equal = equal;
	}

	@Override
	public boolean visit(BooleanParameter parameter)
		throws ParameterEvaluationException {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visit(DateParameter parameter)
		throws ParameterEvaluationException {

		String dateString = _conditionJsonObject.getString(
			ClauseConfigurationKeys.MATCH_VALUE.getJsonKey());

		String dateFormatString = _conditionJsonObject.getString(
			ClauseConfigurationKeys.DATE_FORMAT.getJsonKey());

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

			Date date = dateFormat.parse(dateString);

			if (_not) {
				return parameter.getValue(
				).after(
					date
				);
			}

			return parameter.getValue(
			).before(
				date
			);
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

		Double value = _conditionJsonObject.getDouble(
			ClauseConfigurationKeys.MATCH_VALUE.getJsonKey());

		if (_not) {
			if (_equal) {
				if (parameter.getValue().compareTo(value) <= 0) {
					return true;
				}

				return false;
			}

			if (parameter.getValue().compareTo(value) < 0) {
				return true;
			}

			return false;
		}

		if (_equal) {
			if (parameter.getValue().compareTo(value) >= 0) {
				return true;
			}

			return false;
		}

		if (parameter.getValue().compareTo(value) > 0) {
			return true;
		}

		return false;
	}

	@Override
	public boolean visit(FloatParameter parameter)
		throws ParameterEvaluationException {

		Float value = GetterUtil.getFloat(
			_conditionJsonObject.get(
				ClauseConfigurationKeys.MATCH_VALUE.getJsonKey()));

		if (_not) {
			if (_equal) {
				if (parameter.getValue().compareTo(value) <= 0) {
					return true;
				}

				return false;
			}

			if (parameter.getValue().compareTo(value) < 0) {
				return true;
			}

			return false;
		}

		if (_equal) {
			if (parameter.getValue().compareTo(value) >= 0) {
				return true;
			}

			return false;
		}

		if (parameter.getValue().compareTo(value) > 0) {
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

		Integer value = _conditionJsonObject.getInt(
			ClauseConfigurationKeys.MATCH_VALUE.getJsonKey());

		if (_not) {
			if (_equal) {
				if (parameter.getValue().compareTo(value) <= 0) {
					return true;
				}

				return false;
			}

			if (parameter.getValue().compareTo(value) < 0) {
				return true;
			}

			return false;
		}

		if (_equal) {
			if (parameter.getValue().compareTo(value) >= 0) {
				return true;
			}

			return false;
		}

		if (parameter.getValue().compareTo(value) > 0) {
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

		Long value = _conditionJsonObject.getLong(
			ClauseConfigurationKeys.MATCH_VALUE.getJsonKey());

		if (_not) {
			if (_equal) {
				if (parameter.getValue().compareTo(value) <= 0) {
					return true;
				}

				return false;
			}

			if (parameter.getValue().compareTo(value) < 0) {
				return true;
			}

			return false;
		}

		if (_equal) {
			if (parameter.getValue().compareTo(value) >= 0) {
				return true;
			}

			return false;
		}

		if (parameter.getValue().compareTo(value) > 0) {
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

	private final JSONObject _conditionJsonObject;
	private final boolean _equal;
	private final boolean _not;

}