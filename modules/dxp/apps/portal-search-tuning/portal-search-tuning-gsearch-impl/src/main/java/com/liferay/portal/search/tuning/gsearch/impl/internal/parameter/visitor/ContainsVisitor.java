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

import java.util.Arrays;

/**
 * @author Petteri Karttunen
 */
public class ContainsVisitor implements ClauseConditionEvaluationVisitor {

	public ContainsVisitor(JSONObject conditionJsonObject, boolean not) {
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

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visit(DoubleParameter parameter)
		throws ParameterEvaluationException {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visit(FloatParameter parameter)
		throws ParameterEvaluationException {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visit(IntegerArrayParameter parameter)
		throws ParameterEvaluationException {

		Object object = _conditionJsonObject.get(
			ClauseConfigurationKeys.MATCH_VALUE.getJsonKey());

		try {
			boolean match = false;

			if (object instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray)object;

				for (int i = 0; i < jsonArray.length(); i++) {
					Integer value = jsonArray.getInt(i);

					if (Arrays.stream(
							parameter.getValue()).anyMatch(
								x -> x.intValue() == value.intValue())) {

						match = true;

						break;
					}
				}
			}
			else {
				Integer value = Integer.valueOf((String)object);

				match = Arrays.stream(
					parameter.getValue()
				).anyMatch(
					x -> x.longValue() == value.longValue()
				);
			}

			if (_not) {
				return !match;
			}

			return match;
		}
		catch (NumberFormatException nfe) {
			throw new ParameterEvaluationException(
				new Message(
					Severity.ERROR, "core",
					"core.error.illegal-clause-condition-match-value", null,
					null, _conditionJsonObject,
					ClauseConfigurationKeys.MATCH_VALUE.getJsonKey(),
					object.toString()));
		}
	}

	@Override
	public boolean visit(IntegerParameter parameter)
		throws ParameterEvaluationException {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visit(LongArrayParameter parameter)
		throws ParameterEvaluationException {

		Object object = _conditionJsonObject.get(
			ClauseConfigurationKeys.MATCH_VALUE.getJsonKey());

		try {
			boolean match = false;

			if (object instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray)object;

				for (int i = 0; i < jsonArray.length(); i++) {
					Long value = jsonArray.getLong(i);

					if (Arrays.stream(
							parameter.getValue()).anyMatch(
								x -> x.longValue() == value.longValue())) {

						match = true;

						break;
					}
				}
			}
			else {
				Long value = Long.valueOf((String)object);

				match = Arrays.stream(
					parameter.getValue()
				).anyMatch(
					x -> x.longValue() == value.longValue()
				);
			}

			if (_not) {
				return !match;
			}

			return match;
		}
		catch (NumberFormatException nfe) {
			throw new ParameterEvaluationException(
				new Message(
					Severity.ERROR, "core",
					"core.error.illegal-clause-condition-match-value", null,
					null, _conditionJsonObject,
					ClauseConfigurationKeys.MATCH_VALUE.getJsonKey(),
					object.toString()));
		}
	}

	@Override
	public boolean visit(LongParameter parameter)
		throws ParameterEvaluationException {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visit(Parameter parameter) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visit(StringArrayParameter parameter)
		throws ParameterEvaluationException {

		Object object = _conditionJsonObject.get(
			ClauseConfigurationKeys.MATCH_VALUE.getJsonKey());

		boolean match = false;

		if (object instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray)object;

			for (int i = 0; i < jsonArray.length(); i++) {
				String value = jsonArray.getString(i);

				if (Arrays.stream(
						parameter.getValue()).anyMatch(
							value::equals)) {

					match = true;

					break;
				}
			}
		}
		else {
			String value = String.valueOf(object);

			match = Arrays.stream(
				parameter.getValue()
			).anyMatch(
				value::equals
			);
		}

		if (_not) {
			return !match;
		}

		return match;
	}

	@Override
	public boolean visit(StringParameter parameter)
		throws ParameterEvaluationException {

		throw new UnsupportedOperationException();
	}

	private final JSONObject _conditionJsonObject;

	boolean _not;

}