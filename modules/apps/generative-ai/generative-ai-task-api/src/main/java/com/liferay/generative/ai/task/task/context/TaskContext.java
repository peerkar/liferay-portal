/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.task.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public class TaskContext {

	public TaskContext(long companyId, Locale locale, String taskDefinitionExternalReferenceCode,
					   long userId) {
		_companyId = companyId;
		_locale = locale;
		_taskDefinitionExternalReferenceCode = taskDefinitionExternalReferenceCode;

		_userId = userId;
	}

	public void addTaskContextParameter(
		String key, TaskContextParameter taskContextParameter) {

		if (_taskContextParameters == null) {
			_taskContextParameters = new HashMap<>();
		}

		_taskContextParameters.putIfAbsent(key, taskContextParameter);
	}

	public long getCompanyId() {
		return _companyId;
	}

	public Locale getLocale() {
		return _locale;
	}

	public TaskContextParameter getTaskContextParameter(String name) {
		if (_taskContextParameters == null) {
			return null;
		}

		return _taskContextParameters.get(name);
	}

	public Map<String, TaskContextParameter> getTaskContextParameters() {
		if (_taskContextParameters == null) {
			return Collections.emptyMap();
		}

		return _taskContextParameters;
	}

	public String getTaskDefinitionExternalReferenceCode() {
		return _taskDefinitionExternalReferenceCode;
	}

	public long getUserId() {
		return _userId;
	}

	private final long _companyId;
	private final Locale _locale;
	private Map<String, TaskContextParameter> _taskContextParameters;
	private final String _taskDefinitionExternalReferenceCode;
	private final long _userId;

}