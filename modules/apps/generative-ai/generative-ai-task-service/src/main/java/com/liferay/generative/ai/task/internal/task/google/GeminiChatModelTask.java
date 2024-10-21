/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.task.google;

import com.liferay.generative.ai.task.configuration.GenerativeAITaskConfigurationProvider;
import com.liferay.generative.ai.task.exception.TaskDefinitionConfigurationJSONException;
import com.liferay.generative.ai.task.exception.TaskTestException;
import com.liferay.generative.ai.task.internal.task.BaseTask;
import com.liferay.generative.ai.task.internal.task.tools.AIToolsProvider;
import com.liferay.generative.ai.task.internal.util.SetterUtil;
import com.liferay.generative.ai.task.internal.web.cache.TaskWebCacheItem;
import com.liferay.generative.ai.task.task.Task;
import com.liferay.generative.ai.task.task.TaskResponse;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Validator;

import dev.langchain4j.agent.tool.ToolExecutor;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.Result;

import java.util.List;
import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public class GeminiChatModelTask extends BaseTask implements Task {

	public GeminiChatModelTask(
		JSONObject definitionJSONObject,
		GenerativeAITaskConfigurationProvider generativeAIConfigurationProvider,
		AIToolsProvider toolsProvider) {

		super(
			toolsProvider, definitionJSONObject,
			generativeAIConfigurationProvider, "gemini_chat_model");
	}

	@Override
	@SuppressWarnings("unchecked")
	public TaskResponse execute(boolean debug, Map<String, Object> input) {
		GeminiAssistant geminiAssistant = _getGeminiAssistant(input);

		if (attributesJSONObject.getBoolean("use_cache", false)) {
			Result<String> result = (Result<String>)TaskWebCacheItem.get(
				_getMemoryId(),
				generativeAIConfigurationProvider.getCompanyConfiguration(
					taskContext.getCompanyId()),
				getUserMessage(input), geminiAssistant::chat, getName());

			return toTaskResponse(getDebugInfo(true, debug, result), result);
		}

		Result<String> result = geminiAssistant.chat(
			_getMemoryId(), getUserMessage(input));

		return toTaskResponse(getDebugInfo(false, debug, result), result);
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

	private ChatLanguageModel _getChatLanguageModel() {
		VertexAiGeminiChatModel.VertexAiGeminiChatModelBuilder builder =
			VertexAiGeminiChatModel.builder();

		SetterUtil.setNotBlankString(
			builder::location, attributesJSONObject.getString("location"));
		SetterUtil.setNotNullInteger(
			builder::maxOutputTokens,
			attributesJSONObject.getInt("max_output_tokens"));
		SetterUtil.setNotNullInteger(
			builder::maxRetries, attributesJSONObject.getInt("max_retries"));
		SetterUtil.setNotBlankString(
			builder::modelName, attributesJSONObject.getString("model_name"));
		SetterUtil.setNotBlankString(
			builder::project, attributesJSONObject.getString("project"));
		SetterUtil.setNotNullDoubleAsFloat(
			builder::temperature,
			attributesJSONObject.getDouble("temperature"));
		SetterUtil.setNotNullInteger(
			builder::topK, attributesJSONObject.getInt("top_k"));
		SetterUtil.setNotNullDoubleAsFloat(
			builder::topP, attributesJSONObject.getDouble("top_p"));

		return builder.build();
	}

	private GeminiAssistant _getGeminiAssistant(Map<String, Object> input) {
		AiServices<GeminiAssistant> builder = AiServices.builder(
			GeminiAssistant.class
		).chatLanguageModel(
			_getChatLanguageModel()
		);

		_setChatMemory(builder);

		String systemMessage = getSystemMessage(input);

		if (!Validator.isBlank(systemMessage)) {
			builder.systemMessageProvider(memoryId -> systemMessage);
		}

		List<Object> tools = getTools();

		ListUtil.isNotEmptyForEach(
			tools,
			tool -> {
				if (tool instanceof Map) {
					builder.tools((Map<ToolSpecification, ToolExecutor>)tool);
				}
				else {
					builder.tools(tool);
				}
			});

		return builder.build();
	}

	private String _getMemoryId() {
		return StringBundler.concat(
			taskContext.getUserId(), StringPool.POUND,
			taskContext.getTaskDefinitionExternalReferenceCode());
	}

	private void _setChatMemory(AiServices<GeminiAssistant> builder) {
		if (!attributesJSONObject.getBoolean("use_chat_memory", false)) {
			return;
		}

		builder.chatMemoryProvider(
			memoryId -> GeminiMessageWindowChatMemory.builder(
			).id(
				memoryId
			).maxMessages(
				attributesJSONObject.getInt("memory_max_messages", 10)
			).chatMemoryStore(
				new GeminiMapDBChatMemoryStore()
			).build());
	}

}