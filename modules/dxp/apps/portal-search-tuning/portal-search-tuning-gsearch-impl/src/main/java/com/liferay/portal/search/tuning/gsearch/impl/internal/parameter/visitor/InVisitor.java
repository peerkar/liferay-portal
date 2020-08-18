package com.liferay.portal.search.tuning.gsearch.impl.internal.parameter.visitor;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.search.tuning.gsearch.exception.ParameterEvaluationException;
import com.liferay.portal.search.tuning.gsearch.impl.util.ClauseConditionUtil;
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

/**
 * @author Petteri Karttunen
 */
public class InVisitor implements ClauseConditionEvaluationVisitor {

	public InVisitor(JSONObject conditionJsonObject, boolean not) {
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

		JSONArray jsonArray = ClauseConditionUtil.getClauseConditionArrayValue(
			_conditionJsonObject);

		boolean match = false;

		for (int i = 0; i < jsonArray.length(); i++) {
			Double value = jsonArray.getDouble(i);

			if (parameter.equalsTo(value)) {
				match = true;

				break;
			}
		}

		if (_not) {
			return !match;
		}

		return match;
	}

	@Override
	public boolean visit(FloatParameter parameter)
		throws ParameterEvaluationException {

		JSONArray jsonArray = ClauseConditionUtil.getClauseConditionArrayValue(
			_conditionJsonObject);

		boolean match = false;

		for (int i = 0; i < jsonArray.length(); i++) {
			Float value = GetterUtil.getFloat(jsonArray.get(i));

			if (parameter.equalsTo(value)) {
				match = true;

				break;
			}
		}

		if (_not) {
			return !match;
		}

		return match;
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

		boolean match = false;

		for (int i = 0; i < jsonArray.length(); i++) {
			Integer value = jsonArray.getInt(i);

			if (parameter.equalsTo(value)) {
				match = true;

				break;
			}
		}

		if (_not) {
			return !match;
		}

		return match;
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

		boolean match = false;

		for (int i = 0; i < jsonArray.length(); i++) {
			Long value = jsonArray.getLong(i);

			if (parameter.equalsTo(value)) {
				match = true;

				break;
			}
		}

		if (_not) {
			return !match;
		}

		return match;
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

		JSONArray jsonArray = ClauseConditionUtil.getClauseConditionArrayValue(
			_conditionJsonObject);

		boolean match = false;

		for (int i = 0; i < jsonArray.length(); i++) {
			String value = jsonArray.getString(i);

			if (parameter.getValue().equals(value)) {
				match = true;

				break;
			}
		}

		if (_not) {
			return !match;
		}

		return match;
	}

	private final JSONObject _conditionJsonObject;

	boolean _not;

}