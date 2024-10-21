/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.internal.request;

import com.liferay.generative.ai.request.GenerativeAIRequest;
import com.liferay.generative.ai.request.GenerativeAIRequestBuilder;
import com.liferay.generative.ai.task.task.Task;

import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(service = GenerativeAIRequestBuilder.class)
public class GenerativeAIRequestBuilderImpl
	implements GenerativeAIRequestBuilder {

	@Override
	public GenerativeAIRequest build() {
		return new GenerativeAIRequestImpl(_debug, _input, _task);
	}

	@Override
	public GenerativeAIRequestBuilder debug(boolean debug) {
		_debug = debug;

		return this;
	}

	@Override
	public GenerativeAIRequestBuilder input(Map<String, Object> input) {
		_input = input;

		return this;
	}

	@Override
	public GenerativeAIRequestBuilder task(Task task) {
		_task = task;

		return this;
	}

	private boolean _debug;
	private Map<String, Object> _input;
	private Task _task;

}