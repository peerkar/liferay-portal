/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.task;

import com.liferay.generative.ai.task.configuration.GenerativeAITaskConfigurationProvider;
import com.liferay.generative.ai.task.exception.TaskDefinitionConfigurationJSONException;
import com.liferay.generative.ai.task.exception.TaskTestException;
import com.liferay.generative.ai.task.task.Task;
import com.liferay.generative.ai.task.task.TaskBuilder;
import com.liferay.generative.ai.task.task.TaskResponse;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.MapUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public class ChainTask extends BaseTask implements Task {

	public ChainTask(
		JSONObject configurationJSONObject,
		GenerativeAITaskConfigurationProvider generativeAIConfigurationProvider,
		TaskBuilder taskBuilder) {

		super(
			null, configurationJSONObject, generativeAIConfigurationProvider,
			"chain");

		JSONObject attributesJSONObject = configurationJSONObject.getJSONObject(
			"attributes");

		JSONArray jsonArray = attributesJSONObject.getJSONArray("tasks");

		for (int i = 0; i < jsonArray.length(); i++) {
			_tasks.add(taskBuilder.build(jsonArray.getJSONObject(i)));
		}
	}

	@Override
	public TaskResponse execute(boolean debug, Map<String, Object> input) {
		Map<String, Object> taskDebugInfos = new HashMap<>();
		Map<String, Object> taskOutputs = new HashMap<>();

		TaskResponse taskResponse = null;

		for (Task task : _tasks) {
			long currentTimeMillis = System.currentTimeMillis();

			taskResponse = task.execute(debug, input);

			if (debug) {
				Map<String, Object> taskDebugInfo = taskResponse.getDebugInfo();

				taskDebugInfo.put(
					"executionTime", getExecutionTime(currentTimeMillis));

				taskDebugInfos.put(
					StringBundler.concat(
						getName(), ".", task.getName(), "#", task.hashCode()),
					taskDebugInfo);
			}

			MapUtil.isNotEmptyForEach(
				taskResponse.getOutput(),
				(key, value) -> taskOutputs.put(key, value));
		}

		if (taskResponse != null) {
			return new TaskResponseImpl(
				_getDebugInfo(debug, taskDebugInfos), taskOutputs);
		}

		return toTaskResponse(_getDebugInfo(debug, taskDebugInfos), null);
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

	protected String toStringValue(Object value) {
		return null;
	}

	private Map<String, Object> _getDebugInfo(
		boolean debug, Map<String, Object> taskDebugInfos) {

		if (!debug) {
			return null;
		}

		return HashMapBuilder.<String, Object>put(
			"numberOfTasks", _tasks.size()
		).put(
			"tasks", taskDebugInfos
		).build();
	}

	private final List<Task> _tasks = new ArrayList<>();

}