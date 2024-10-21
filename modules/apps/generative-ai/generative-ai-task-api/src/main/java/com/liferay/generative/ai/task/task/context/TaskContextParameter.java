/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.task.context;

/**
 * @author Petteri Karttunen
 */
public class TaskContextParameter {

	public TaskContextParameter(String stringValue) {
		_stringValue = stringValue;

		_value = stringValue;
	}

	public TaskContextParameter(String stringValue, Object value) {
		_stringValue = stringValue;
		_value = value;
	}

	public String getStringValue() {
		return _stringValue;
	}

	public Object getValue() {
		return _value;
	}

	private final String _stringValue;
	private final Object _value;

}