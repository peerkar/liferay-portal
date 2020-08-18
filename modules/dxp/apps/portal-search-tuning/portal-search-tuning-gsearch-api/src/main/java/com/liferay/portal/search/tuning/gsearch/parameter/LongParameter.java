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

package com.liferay.portal.search.tuning.gsearch.parameter;

import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.values.EvaluationType;
import com.liferay.portal.search.tuning.gsearch.exception.ParameterEvaluationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Petteri Karttunen
 */
public class LongParameter implements Parameter {

	public LongParameter(
		String name, String role, String configurationVariable, Long value) {

		_name = name;
		_role = role;
		_configurationVariable = configurationVariable;
		_value = value;
	}

	@Override
	public boolean accept(EvaluationVisitor evaluationVisitor)
		throws ParameterEvaluationException {

		return evaluationVisitor.visit(this);
	}

	@Override
	public String accept(ToStringVisitor toStringVisitor) throws Exception {
		return toStringVisitor.visit(this);
	}

	public boolean equalsTo(Long value) {
		if (_value.longValue() == value.longValue()) {
			return true;
		}

		return false;
	}

	@Override
	public String getConfigurationVariable() {
		return _configurationVariable;
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public Optional<String> getRoleOptional() {
		if (!Validator.isBlank(_role)) {
			return Optional.of(_role);
		}

		return Optional.empty();
	}

	@Override
	public List<EvaluationType> getSupportedEvaluationTypes() {
		List<EvaluationType> evaluationTypes = new ArrayList<>();

		evaluationTypes.add(EvaluationType.EQ);
		evaluationTypes.add(EvaluationType.NE);
		evaluationTypes.add(EvaluationType.GT);
		evaluationTypes.add(EvaluationType.GTE);
		evaluationTypes.add(EvaluationType.LT);
		evaluationTypes.add(EvaluationType.LTE);
		evaluationTypes.add(EvaluationType.IN);
		evaluationTypes.add(EvaluationType.NOT_IN);
		evaluationTypes.add(EvaluationType.IN_RANGE);
		evaluationTypes.add(EvaluationType.NOT_IN_RANGE);

		return evaluationTypes;
	}

	@Override
	public Long getValue() {
		return _value;
	}

	private final String _configurationVariable;
	private final String _name;
	private final String _role;
	private final Long _value;

}