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
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.search.experiences.blueprints.parameter.IntegerArraySXPParameter;
import com.liferay.search.experiences.blueprints.parameter.LongArraySXPParameter;
import com.liferay.search.experiences.blueprints.parameter.StringArraySXPParameter;
import com.liferay.search.experiences.blueprints.parameter.StringSXPParameter;
import com.liferay.search.experiences.blueprints.parameter.visitor.EvaluationVisitor;
import com.liferay.search.experiences.exception.SXPParameterException;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author Petteri Karttunen
 */
public class ContainsVisitor
	extends BaseEvaluationVisitor implements EvaluationVisitor {

	public ContainsVisitor(JSONObject conditionJSONObject) {
		super(conditionJSONObject);

		_conditionJSONObject = conditionJSONObject;
	}

	@Override
	public boolean visit(IntegerArraySXPParameter parameter)
		throws SXPParameterException {

		Object object = _conditionJSONObject.get("value");

		Integer[] parameterValue = parameter.getValue();

		try {
			boolean match = false;

			if (object instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray)object;

				for (int i = 0; i < jsonArray.length(); i++) {
					Integer value = jsonArray.getInt(i);

					Stream<Integer> stream = Arrays.stream(parameterValue);

					if (stream.anyMatch(
							x -> x.intValue() == value.intValue())) {

						match = true;

						break;
					}
				}
			}
			else {
				Integer value = Integer.valueOf((String)object);

				Stream<Integer> stream = Arrays.stream(parameterValue);

				match = stream.anyMatch(
					x -> x.longValue() == value.longValue());
			}

			return match;
		}
		catch (NumberFormatException numberFormatException) {
			throw new SXPParameterException(
				toErrorMessage(
					getClass().getName(), "illegal-match-value-format", "value",
					object.toString(), numberFormatException));
		}
	}

	@Override
	public boolean visit(LongArraySXPParameter parameter)
		throws SXPParameterException {

		Object object = _conditionJSONObject.get("value");

		Long[] parameterValue = parameter.getValue();

		try {
			boolean match = false;

			if (object instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray)object;

				for (int i = 0; i < jsonArray.length(); i++) {
					Long value = jsonArray.getLong(i);

					Stream<Long> parameterValueStream = Arrays.stream(
						parameterValue);

					if (parameterValueStream.anyMatch(
							x -> x.longValue() == value.longValue())) {

						match = true;

						break;
					}
				}
			}
			else {
				Long value = GetterUtil.getLong(object);

				Stream<Long> parameterValueStream = Arrays.stream(
					parameterValue);

				match = parameterValueStream.anyMatch(
					x -> x.longValue() == value.longValue());
			}

			return match;
		}
		catch (NumberFormatException numberFormatException) {
			throw new SXPParameterException(
				toErrorMessage(
					getClass().getName(), "illegal-match-value-format", "value",
					object.toString(), numberFormatException));
		}
	}

	@Override
	public boolean visit(StringArraySXPParameter parameter)
		throws SXPParameterException {

		Object object = _conditionJSONObject.get("value");

		String[] parameterValue = parameter.getValue();

		boolean match = false;

		if (object instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray)object;

			for (int i = 0; i < jsonArray.length(); i++) {
				String value = jsonArray.getString(i);

				Stream<String> stream = Arrays.stream(parameterValue);

				if (stream.anyMatch(value::equalsIgnoreCase)) {
					match = true;

					break;
				}
			}
		}
		else {
			String value = String.valueOf(object);

			Stream<String> stream = Arrays.stream(parameterValue);

			match = stream.anyMatch(value::equalsIgnoreCase);
		}

		return match;
	}

	@Override
	public boolean visit(StringSXPParameter parameter)
		throws SXPParameterException {

		Object object = _conditionJSONObject.get("value");

		if (Validator.isNull(object)) {
			throw new SXPParameterException(
				toErrorMessage(
					getClass().getName(), "match-value-cannot-be-empty",
					"value", object.toString(),
					new Throwable("Match value cannot be empty")));
		}

		String parameterValue = StringUtil.toLowerCase(parameter.getValue());

		boolean match = false;

		if (object instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray)object;

			for (int i = 0; i < jsonArray.length(); i++) {
				String value = jsonArray.getString(i);

				if (parameterValue.contains(StringUtil.toLowerCase(value))) {
					match = true;

					break;
				}
			}
		}
		else {
			String value = String.valueOf(object);

			if (parameterValue.contains(StringUtil.toLowerCase(value))) {
				match = true;
			}
		}

		return match;
	}

	private final JSONObject _conditionJSONObject;

}