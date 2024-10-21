/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.task;

import com.liferay.generative.ai.task.configuration.GenerativeAITaskConfigurationProvider;
import com.liferay.generative.ai.task.exception.TaskDefinitionConfigurationJSONException;
import com.liferay.generative.ai.task.exception.TaskTestException;
import com.liferay.generative.ai.task.internal.task.util.TaskConditionUtil;
import com.liferay.generative.ai.task.task.Task;
import com.liferay.generative.ai.task.task.TaskBuilder;
import com.liferay.generative.ai.task.task.TaskResponse;
import com.liferay.generative.ai.task.task.context.TaskContextParameter;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.HashMapBuilder;

import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public class TaskContextParameterAgent extends BaseTask implements Task {

	public TaskContextParameterAgent(
		JSONObject configurationJSONObject,
		GenerativeAITaskConfigurationProvider generativeAIConfigurationProvider,
		TaskBuilder taskBuilder) {

		super(
			null, configurationJSONObject, generativeAIConfigurationProvider,
			"task_context_parameter_agent");

		_taskBuilder = taskBuilder;
	}

	@Override
	public TaskResponse execute(boolean debug, Map<String, Object> input) {
		TaskContextParameter taskContextParameter =
			taskContext.getTaskContextParameter(
				attributesJSONObject.getString("task_context_parameter_name"));

		Task task = _getTask(taskContextParameter.getStringValue());

		if (task == null) {
			return toTaskResponse(
				HashMapBuilder.<String, Object>put(
					"error",
					" No nodes matched the conditions and default task was " +
						"not found"
				).build(),
				null);
		}

		long currentTimeMillis = System.currentTimeMillis();

		TaskResponse taskResponse = task.execute(debug, input);

		return new TaskResponseImpl(
			_getDebugInfo(debug, currentTimeMillis, task, taskResponse),
			taskResponse.getOutput());
	}

	@Override
	public void test() throws TaskTestException {

		// TODO Auto-generated method stub

	}

	@Override
	public void validateConfigurationJSON()
		throws TaskDefinitionConfigurationJSONException {

		// TODO Auto-generated method stub

	}

	private Map<String, Object> _getDebugInfo(
		boolean debug, long startTime, Task task, TaskResponse taskResponse) {

		if (!debug) {
			return null;
		}

		Map<String, Object> taskDebugInfo = taskResponse.getDebugInfo();

		taskDebugInfo.put(
			"executionTime", (System.currentTimeMillis() - startTime) + "ms");

		return HashMapBuilder.<String, Object>put(
			StringBundler.concat(
				getName(), ".", task.getName(), "#", task.hashCode()),
			taskDebugInfo
		).build();
	}

	private Task _getTask(String value) {
		JSONArray nodesJSONArray = attributesJSONObject.getJSONArray("nodes");

		Task task = null;

		for (int i = 0; i < nodesJSONArray.length(); i++) {
			JSONObject nodeJSONObject = nodesJSONArray.getJSONObject(i);

			if (TaskConditionUtil.validateCondition(
					nodeJSONObject.getJSONObject("condition"), value)) {

				task = _taskBuilder.build(nodeJSONObject.getJSONObject("task"));

				break;
			}
		}

		if (task == null) {
			JSONObject defaultTaskJSONObject =
				attributesJSONObject.getJSONObject("default_task");

			if (defaultTaskJSONObject != null) {
				task = _taskBuilder.build(defaultTaskJSONObject);
			}
		}

		return task;
	}

	private final TaskBuilder _taskBuilder;

}