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

/**
 * @author Petteri Karttunen
 */
public class ParameterDefinition {

	public ParameterDefinition(
		String variable, String className, String descriptionKey) {

		_variable = variable;
		_className = className;
		_descriptionKey = descriptionKey;
	}

	public String getClassName() {
		return _className;
	}

	public String getDescriptionKey() {
		return _descriptionKey;
	}

	public String getVariable() {
		return _variable;
	}

	private final String _className;
	private final String _descriptionKey;
	private final String _variable;

}