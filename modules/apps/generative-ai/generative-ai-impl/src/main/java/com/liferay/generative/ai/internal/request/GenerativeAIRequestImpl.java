/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.internal.request;

import com.liferay.generative.ai.request.GenerativeAIRequest;
import com.liferay.generative.ai.task.task.Task;

import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public class GenerativeAIRequestImpl implements GenerativeAIRequest {

	public GenerativeAIRequestImpl(
		boolean debug, Map<String, Object> input, Task task) {

		_debug = debug;
		_input = input;
		_task = task;
	}

	public Map<String, Object> getInput() {
		return _input;
	}

	public Task getTask() {
		return _task;
	}

	public boolean isDebug() {
		return _debug;
	}

	private GenerativeAIRequestImpl() {
	}

	private boolean _debug;
	private Map<String, Object> _input;
	private Task _task;

}