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

package com.liferay.portal.search.tuning.gsearch.impl.internal.clause.condition.handler;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.ClauseConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.values.EvaluationType;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.exception.ParameterEvaluationException;
import com.liferay.portal.search.tuning.gsearch.impl.internal.parameter.visitor.AnyWordInVisitor;
import com.liferay.portal.search.tuning.gsearch.impl.internal.parameter.visitor.ClauseConditionEvaluationVisitor;
import com.liferay.portal.search.tuning.gsearch.impl.internal.parameter.visitor.ContainsVisitor;
import com.liferay.portal.search.tuning.gsearch.impl.internal.parameter.visitor.EqualsVisitor;
import com.liferay.portal.search.tuning.gsearch.impl.internal.parameter.visitor.GreaterThanVisitor;
import com.liferay.portal.search.tuning.gsearch.impl.internal.parameter.visitor.InRangeVisitor;
import com.liferay.portal.search.tuning.gsearch.impl.internal.parameter.visitor.InVisitor;
import com.liferay.portal.search.tuning.gsearch.message.Message;
import com.liferay.portal.search.tuning.gsearch.message.Severity;
import com.liferay.portal.search.tuning.gsearch.parameter.Parameter;
import com.liferay.portal.search.tuning.gsearch.spi.clause.ClauseConditionHandler;

import java.util.Optional;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, property = "name=default",
	service = ClauseConditionHandler.class
)
public class DefaultClauseConditionHandler implements ClauseConditionHandler {

	@Override
	public boolean isTrue(
		SearchRequestContext searchRequestContext,
		JSONObject conditionJsonObject) {

		if (_validateCondition(searchRequestContext, conditionJsonObject)) {
			return false;
		}

		String parameterName = conditionJsonObject.getString(
			ClauseConfigurationKeys.PARAMETER_NAME.getJsonKey());

		Optional<Parameter> parameterOptional =
			searchRequestContext.getSearchParameterData(
			).getByConfigurationVariableName(
				parameterName
			);

		if (!parameterOptional.isPresent()) {
			searchRequestContext.addMessage(
				new Message(
					Severity.ERROR, "core",
					"core.error.unknown-clause-condition-parameter", null, null,
					conditionJsonObject,
					ClauseConfigurationKeys.PARAMETER_NAME.getJsonKey(),
					parameterName));

			return false;
		}

		String evaluationTypeString = conditionJsonObject.getString(
			ClauseConfigurationKeys.EVALUATION_TYPE.getJsonKey());

		EvaluationType evaluationType;

		try {
			evaluationType = _getEvaluationType(evaluationTypeString);
		}
		catch (IllegalArgumentException illegalArgumentException) {
			searchRequestContext.addMessage(
				new Message(
					Severity.ERROR, "core",
					"core.error.unknown-clause-condition-evaluation-type",
					illegalArgumentException.getMessage(),
					illegalArgumentException, conditionJsonObject,
					ClauseConfigurationKeys.EVALUATION_TYPE.getJsonKey(),
					evaluationTypeString));

			return false;
		}

		Parameter parameter = parameterOptional.get();

		ClauseConditionEvaluationVisitor visitor = null;

		if (EvaluationType.ANY_WORD_IN.equals(evaluationType) &&
			parameter.getSupportedEvaluationTypes().contains(evaluationType)) {

			visitor = new AnyWordInVisitor(conditionJsonObject, false);
		}
		else if (EvaluationType.CONTAINS.equals(evaluationType) &&
				 parameter.getSupportedEvaluationTypes().contains(
					 evaluationType)) {

			visitor = new ContainsVisitor(conditionJsonObject, false);
		}
		else if (EvaluationType.EQ.equals(evaluationType) &&
				 parameter.getSupportedEvaluationTypes().contains(
					 evaluationType)) {

			visitor = new EqualsVisitor(conditionJsonObject, false);
		}
		else if (EvaluationType.GT.equals(evaluationType) &&
				 parameter.getSupportedEvaluationTypes().contains(
					 evaluationType)) {

			visitor = new GreaterThanVisitor(conditionJsonObject, false, false);
		}
		else if (EvaluationType.GTE.equals(evaluationType) &&
				 parameter.getSupportedEvaluationTypes().contains(
					 evaluationType)) {

			visitor = new GreaterThanVisitor(conditionJsonObject, false, true);
		}
		else if (EvaluationType.IN.equals(evaluationType) &&
				 parameter.getSupportedEvaluationTypes().contains(
					 evaluationType)) {

			visitor = new InVisitor(conditionJsonObject, false);
		}
		else if (EvaluationType.IN_RANGE.equals(evaluationType) &&
				 parameter.getSupportedEvaluationTypes().contains(
					 evaluationType)) {

			visitor = new InRangeVisitor(conditionJsonObject, false);
		}
		else if (EvaluationType.LT.equals(evaluationType) &&
				 parameter.getSupportedEvaluationTypes().contains(
					 evaluationType)) {

			visitor = new GreaterThanVisitor(conditionJsonObject, true, false);
		}
		else if (EvaluationType.LTE.equals(evaluationType) &&
				 parameter.getSupportedEvaluationTypes().contains(
					 evaluationType)) {

			visitor = new GreaterThanVisitor(conditionJsonObject, true, true);
		}
		else if (EvaluationType.NE.equals(evaluationType) &&
				 parameter.getSupportedEvaluationTypes().contains(
					 evaluationType)) {

			visitor = new EqualsVisitor(conditionJsonObject, true);
		}
		else if (EvaluationType.NO_WORD_IN.equals(evaluationType) &&
				 parameter.getSupportedEvaluationTypes().contains(
					 evaluationType)) {

			visitor = new AnyWordInVisitor(conditionJsonObject, true);
		}
		else if (EvaluationType.NOT_CONTAINS.equals(evaluationType) &&
				 parameter.getSupportedEvaluationTypes().contains(
					 evaluationType)) {

			visitor = new ContainsVisitor(conditionJsonObject, true);
		}
		else if (EvaluationType.NOT_IN.equals(evaluationType) &&
				 parameter.getSupportedEvaluationTypes().contains(
					 evaluationType)) {

			visitor = new InRangeVisitor(conditionJsonObject, true);
		}
		else if (EvaluationType.NOT_IN_RANGE.equals(evaluationType) &&
				 parameter.getSupportedEvaluationTypes().contains(
					 evaluationType)) {

			visitor = new InRangeVisitor(conditionJsonObject, true);
		}

		if (visitor == null) {
			searchRequestContext.addMessage(
				new Message(
					Severity.ERROR, "core",
					"core.error.unable-to-resolve-clause-condition-handler",
					null, null, conditionJsonObject,
					ClauseConfigurationKeys.EVALUATION_TYPE.getJsonKey(),
					evaluationType.name(
					).toLowerCase()));

			return false;
		}

		try {
			return parameter.accept(visitor);
		}
		catch (ParameterEvaluationException parameterEvaluationException) {
			searchRequestContext.addMessage(
				parameterEvaluationException.getDetailsMessage());

			return false;
		}
	}

	private EvaluationType _getEvaluationType(String s)
		throws IllegalArgumentException {

		s = StringUtil.toUpperCase(s);

		return EvaluationType.valueOf(s);
	}

	private boolean _validateCondition(
		SearchRequestContext searchRequestContext,
		JSONObject conditionJsonObject) {

		boolean valid = true;

		if (Validator.isNull(
				conditionJsonObject.getString(
					ClauseConfigurationKeys.PARAMETER_NAME.getJsonKey()))) {

			searchRequestContext.addMessage(
				new Message(
					Severity.ERROR, "core",
					"core.error.clause-condition-parameter-not-defined", null,
					null, conditionJsonObject,
					ClauseConfigurationKeys.PARAMETER_NAME.getJsonKey(), null));
			valid = false;
		}

		if (Validator.isNull(
				conditionJsonObject.getString(
					ClauseConfigurationKeys.EVALUATION_TYPE.getJsonKey()))) {

			searchRequestContext.addMessage(
				new Message(
					Severity.ERROR, "core",
					"core.error.clause-condition-evaluation-type-not-defined",
					null, null, conditionJsonObject,
					ClauseConfigurationKeys.EVALUATION_TYPE.getJsonKey(),
					null));
			valid = false;
		}

		if (Validator.isNull(
				conditionJsonObject.getString(
					ClauseConfigurationKeys.MATCH_VALUE.getJsonKey()))) {

			searchRequestContext.addMessage(
				new Message(
					Severity.ERROR, "core",
					"core.error.clause-condition-match-value-not-defined", null,
					null, conditionJsonObject,
					ClauseConfigurationKeys.MATCH_VALUE.getJsonKey(), null));
			valid = false;
		}

		return valid;
	}

}