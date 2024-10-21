/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.task;

import com.liferay.generative.ai.task.task.TaskResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public class TaskResponseImpl implements TaskResponse {

	public TaskResponseImpl(
		Map<String, Object> debugInfo, Map<String, Object> output) {

		_debugInfo = debugInfo;
		_output = output;
	}

	@Override
	public Map<String, Object> getDebugInfo() {
		if (_debugInfo == null) {
			_debugInfo = new HashMap<>();
		}

		return _debugInfo;
	}

	@Override
	public Map<String, Object> getOutput() {
		if (_output == null) {
			_output = new HashMap<>();
		}

		return _output;
	}

	private Map<String, Object> _debugInfo;
	private Map<String, Object> _output;

}