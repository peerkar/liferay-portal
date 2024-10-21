/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.task.tools;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Http;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import java.io.IOException;

/**
 * @author Louis-Guillaume Durand
 */
public class WebHookAITools implements AITools {

	public WebHookAITools(JSONObject configurationJSONObject, Http http) {
		_configurationJSONObject = configurationJSONObject;
		_http = http;
	}

	@Override
	public JSONObject getConfigurationJSONObject() {
		return _configurationJSONObject;
	}

	@Tool("Send JSON data to external tool using webhook URL")
	public void sendJSON(
			@P("The json data to send") String json,
			@P("The URL to use to send the data") String url)
		throws IOException {

		Http.Options options = new Http.Options();

		options.setBody(json, "application/json", "utf-8");
		options.setHeaders(
			HashMapBuilder.put(
				"Content-Type", "application/json"
			).build());

		options.setLocation(url);
		options.setMethod(Http.Method.POST);

		_http.URLtoString(options);
	}

	private final JSONObject _configurationJSONObject;
	private final Http _http;

}