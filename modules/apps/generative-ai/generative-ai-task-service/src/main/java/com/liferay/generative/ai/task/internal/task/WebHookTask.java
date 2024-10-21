/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.task;

import com.liferay.generative.ai.task.configuration.GenerativeAITaskConfigurationProvider;
import com.liferay.generative.ai.task.exception.TaskDefinitionConfigurationJSONException;
import com.liferay.generative.ai.task.exception.TaskTestException;
import com.liferay.generative.ai.task.task.Task;
import com.liferay.generative.ai.task.task.TaskResponse;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Http;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Louis-Guillaume Durand
 */
public class WebHookTask extends BaseTask implements Task {

	public WebHookTask(
		JSONObject configurationJSONObject,
		GenerativeAITaskConfigurationProvider generativeAIConfigurationProvider,
		Http http) {

		super(
			null, configurationJSONObject, generativeAIConfigurationProvider,
			"webhook");

		_http = http;
	}

	@Override
	public TaskResponse execute(boolean debug, Map<String, Object> input) {
		Http.Options options = new Http.Options();

		options.setLocation(
			GetterUtil.getString(attributesJSONObject.get("url")));
		options.setMethod(Http.Method.GET);

		try {
			return toTaskResponse(
				_getDebugInfo(debug, null), _http.URLtoString(options));
		}
		catch (Exception exception) {
			return toTaskResponse(_getDebugInfo(debug, exception), null);
		}
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
		boolean debug, Exception exception) {

		if (!debug) {
			return null;
		}

		if (exception == null) {
			return new HashMap<>();
		}

		return HashMapBuilder.<String, Object>put(
			"exception", exception.getLocalizedMessage()
		).build();
	}

	private final Http _http;

}