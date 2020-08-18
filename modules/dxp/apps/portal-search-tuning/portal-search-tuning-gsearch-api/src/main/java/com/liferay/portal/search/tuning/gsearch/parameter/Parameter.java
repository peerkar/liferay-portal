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

import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.values.EvaluationType;
import com.liferay.portal.search.tuning.gsearch.exception.ParameterEvaluationException;

import java.util.List;
import java.util.Optional;

/**
 * @author Petteri Karttunen
 */
public interface Parameter {

	public boolean accept(EvaluationVisitor parameterEvaluationVisitor)
		throws ParameterEvaluationException;

	public String accept(ToStringVisitor parameterToStringVisitor)
		throws Exception;

	public String getConfigurationVariable();

	public String getName();

	public Optional<String> getRoleOptional();

	public List<EvaluationType> getSupportedEvaluationTypes();

	public Object getValue();

}