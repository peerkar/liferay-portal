/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.internal.request;

import com.liferay.generative.ai.request.GenerativeAIRequest;
import com.liferay.generative.ai.request.GenerativeAIRequestExecutor;
import com.liferay.generative.ai.response.GenerativeAIResponse;
import com.liferay.generative.ai.response.GenerativeAIResponseBuilder;
import com.liferay.generative.ai.response.GenerativeAIResponseBuilderFactory;
import com.liferay.generative.ai.task.task.Task;
import com.liferay.generative.ai.task.task.TaskResponse;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(service = GenerativeAIRequestExecutor.class)
public class GenerativeAIRequestExecutorImpl
	implements GenerativeAIRequestExecutor {

	public GenerativeAIResponse execute(
		GenerativeAIRequest generativeAIRequest) {

		GenerativeAIResponseBuilder builder =
			_generativeAIResponseBuilderFactory.builder();

		Task task = generativeAIRequest.getTask();

		long currentTimeMillis = System.currentTimeMillis();

		try {
			TaskResponse taskResponse = task.execute(
				generativeAIRequest.isDebug(), generativeAIRequest.getInput());

			builder.output(taskResponse.getOutput());

			if (generativeAIRequest.isDebug()) {

				Map<String, Object> debugInfo = taskResponse.getDebugInfo();

				debugInfo.put("executionTime", (System.currentTimeMillis() - currentTimeMillis) + "ms");

				builder.debugInfo(
					HashMapBuilder.put(
						task.getName(), taskResponse.getDebugInfo()
					).build());
			}
		}
		catch (Exception exception) {

			_log.error(exception);

			builder.debugInfo(
				HashMapBuilder.<String, Map<String, Object>>put(
					task.getName(),
					HashMapBuilder.<String, Object>put(
						"exception", exception.toString()
					).build()
				).build());

			builder.output(
				HashMapBuilder.<String, Object>put(
					"text",
					StringBundler.concat(
						"Oh dear, something went wrong! Please check the task ",
						"configuration and try again.\n\nReason:\n",
						"```json\n", exception.getMessage(), "\n```\n")
				).build());
		}

		builder.took((System.currentTimeMillis() - currentTimeMillis) + "ms");

		return builder.build();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		GenerativeAIRequestExecutorImpl.class);

	@Reference
	private GenerativeAIResponseBuilderFactory
		_generativeAIResponseBuilderFactory;

}