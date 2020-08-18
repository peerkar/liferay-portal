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
public class IntegerArrayParameter implements Parameter {

	public IntegerArrayParameter(
		String name, String role, String configurationVariable,
		Integer[] value) {

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

		evaluationTypes.add(EvaluationType.CONTAINS);
		evaluationTypes.add(EvaluationType.NOT_CONTAINS);

		return evaluationTypes;
	}

	@Override
	public Integer[] getValue() {
		return _value;
	}

	private final String _configurationVariable;
	private final String _name;
	private final String _role;
	private final Integer[] _value;

}