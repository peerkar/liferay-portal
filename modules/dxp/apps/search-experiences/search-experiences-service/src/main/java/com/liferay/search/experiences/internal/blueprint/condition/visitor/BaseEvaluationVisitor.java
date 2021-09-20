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
import com.liferay.portal.kernel.util.Validator;
import com.liferay.search.experiences.blueprint.parameter.BooleanSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.DateSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.DoubleSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.FloatSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.IntegerArraySXPParameter;
import com.liferay.search.experiences.blueprint.parameter.IntegerSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.LongArraySXPParameter;
import com.liferay.search.experiences.blueprint.parameter.LongSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.SXPParameter;
import com.liferay.search.experiences.blueprint.parameter.StringArraySXPParameter;
import com.liferay.search.experiences.blueprint.parameter.StringSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.exception.SXPParameterException;
import com.liferay.search.experiences.problem.Problem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

/**
 * @author Petteri Karttunen
 */
public abstract class BaseEvaluationVisitor
	implements SXPParameter.EvaluationVisitor {

	public BaseEvaluationVisitor(JSONObject conditionJSONObject) {
		this.conditionJSONObject = conditionJSONObject;
	}

	@Override
	public boolean visit(BooleanSXPParameter parameter)
		throws SXPParameterException {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visit(DateSXPParameter parameter)
		throws SXPParameterException {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visit(DoubleSXPParameter parameter)
		throws SXPParameterException {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visit(FloatSXPParameter parameter)
		throws SXPParameterException {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visit(IntegerArraySXPParameter parameter)
		throws SXPParameterException {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visit(IntegerSXPParameter parameter)
		throws SXPParameterException {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visit(LongArraySXPParameter parameter)
		throws SXPParameterException {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visit(LongSXPParameter parameter)
		throws SXPParameterException {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visit(StringArraySXPParameter parameter)
		throws SXPParameterException {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visit(StringSXPParameter parameter)
		throws SXPParameterException {

		throw new UnsupportedOperationException();
	}

	protected JSONArray getConditionValueJSONArray(
			JSONObject conditionJSONObject)
		throws SXPParameterException {

		Object object = conditionJSONObject.get("value");

		if (!(object instanceof JSONArray)) {
			throw new SXPParameterException(
				toErrorMessage(
					getClass().getName(), "match-value-has-to-be-an-array",
					"value", GetterUtil.getString(object),
					new Throwable("Match value has to be an array")));
		}

		return (JSONArray)object;
	}

	protected String getDateAsString(Date date, String dateFormatString)
		throws SXPParameterException {

		if (Validator.isBlank(dateFormatString)) {
			throw new SXPParameterException(
				toErrorMessage(
					getClass().getName(), "date-format-is-required",
					"date_format", dateFormatString,
					new Throwable("Date format is required")));
		}

		try {
			DateFormat dateFormat = new SimpleDateFormat(dateFormatString);

			return dateFormat.format(date);
		}
		catch (Exception exception) {
			throw new SXPParameterException(
				toErrorMessage(
					getClass().getName(), "date-parsing-failed", "value",
					GetterUtil.getString(date), exception));
		}
	}

	protected String getDateFormatString() throws SXPParameterException {
		String dateFormatString = conditionJSONObject.getString("date_format");

		if (Validator.isBlank(dateFormatString)) {
			throw new SXPParameterException(
				toErrorMessage(
					getClass().getName(), "date-format-is-required", "value",
					dateFormatString,
					new Throwable("Date format is required")));
		}

		return dateFormatString;
	}

	protected Date getDateValue(JSONObject conditionJSONObject)
		throws SXPParameterException {

		String dateString = conditionJSONObject.getString("value");

		String dateFormatString = getDateFormatString();

		try {
			DateFormat dateFormat = new SimpleDateFormat(dateFormatString);

			return dateFormat.parse(dateString);
		}
		catch (Exception exception) {
			throw new SXPParameterException(
				toErrorMessage(
					getClass().getName(), "date-parsing-failed", "value",
					dateString, exception));
		}
	}

	protected Problem toErrorMessage(
		String className, String languageKey, String rootJSONObjectPropertyKey,
		String rootJSONObjectValue, Throwable throwable) {

		return new Problem.Builder().className(
			className
		).languageKey(
			languageKey
		).rootObject(
			conditionJSONObject
		).rootProperty(
			rootJSONObjectPropertyKey
		).rootValue(
			rootJSONObjectValue
		).throwable(
			throwable
		).build();
	}

	protected final JSONObject conditionJSONObject;

}