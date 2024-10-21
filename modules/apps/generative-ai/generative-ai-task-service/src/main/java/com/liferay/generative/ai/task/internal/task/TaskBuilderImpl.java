/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.task;

import com.liferay.generative.ai.task.configuration.GenerativeAITaskConfigurationProvider;
import com.liferay.generative.ai.task.internal.task.google.GeminiChatModelTask;
import com.liferay.generative.ai.task.internal.task.google.GoogleImagenTask;
import com.liferay.generative.ai.task.internal.task.openai.OpenAIImageModelTask;
import com.liferay.generative.ai.task.internal.task.tools.AIToolsProvider;
import com.liferay.generative.ai.task.task.Task;
import com.liferay.generative.ai.task.task.TaskBuilder;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.searcher.SearchRequestBuilderFactory;
import com.liferay.portal.search.searcher.Searcher;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(service = TaskBuilder.class)
public class TaskBuilderImpl implements TaskBuilder {

	@Override
	public Task build(JSONObject configurationJSONObject) {
		return _createTask(configurationJSONObject);
	}

	private Task _createTask(JSONObject configurationJSONObject) {
		String name = configurationJSONObject.getString("name");

		if (Validator.isBlank(name)) {
			throw new IllegalArgumentException("Name is required");
		}

		if (name.equals("chain")) {
			return new ChainTask(
				configurationJSONObject, _generativeAIConfigurationProvider,
				this);
		}
		else if (name.equals("gemini_chat_model")) {
			return new GeminiChatModelTask(
				configurationJSONObject, _generativeAIConfigurationProvider,
				_toolsProvider);
		}
		else if (name.equals("google_imagen")) {
			return new GoogleImagenTask(
				configurationJSONObject, _generativeAIConfigurationProvider);
		}
		else if (name.equals("local_document_retrieval")) {
			return new LocalDocumentRetrievalTask(
				configurationJSONObject, _generativeAIConfigurationProvider,
				_searcher, _searchRequestBuilderFactory);
		}
		else if (name.equals("openai_image_model")) {
			return new OpenAIImageModelTask(
				configurationJSONObject, _generativeAIConfigurationProvider,
				_toolsProvider);
		}
		else if (name.equals("task_context_parameter_agent")) {
			return new TaskContextParameterAgent(
				configurationJSONObject, _generativeAIConfigurationProvider,
				this);
		}
		else if (name.equals("text_input_agent")) {
			return new TextInputAgentTask(
				configurationJSONObject, _generativeAIConfigurationProvider,
				this);
		}
		else if (name.equals("webhook")) {
			return new WebHookTask(
				configurationJSONObject, _generativeAIConfigurationProvider,
				_http);
		}

		throw new IllegalArgumentException("Unknown task name " + name);
	}

	@Reference
	private GenerativeAITaskConfigurationProvider
		_generativeAIConfigurationProvider;

	@Reference
	private Http _http;

	@Reference
	private Searcher _searcher;

	@Reference
	private SearchRequestBuilderFactory _searchRequestBuilderFactory;

	@Reference
	private AIToolsProvider _toolsProvider;

}