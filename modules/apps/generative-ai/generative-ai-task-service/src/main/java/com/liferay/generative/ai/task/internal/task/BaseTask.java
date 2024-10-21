/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.task;

import com.liferay.generative.ai.task.configuration.GenerativeAITaskConfigurationProvider;
import com.liferay.generative.ai.task.internal.task.tools.AIToolsProvider;
import com.liferay.generative.ai.task.task.Task;
import com.liferay.generative.ai.task.task.TaskResponse;
import com.liferay.generative.ai.task.task.context.TaskContext;
import com.liferay.generative.ai.task.task.context.TaskContextParameter;
import com.liferay.generative.ai.task.task.context.TaskContextThreadLocal;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.service.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * @author Petteri Karttunen
 */
public abstract class BaseTask implements Task {

	public BaseTask(
		AIToolsProvider aiToolsProvider, JSONObject configurationJSONObject,
		GenerativeAITaskConfigurationProvider generativeAIConfigurationProvider,
		String name) {

		this.aiToolsProvider = aiToolsProvider;
		this.configurationJSONObject = configurationJSONObject;
		this.generativeAIConfigurationProvider =
			generativeAIConfigurationProvider;
		this.name = name;

		attributesJSONObject = configurationJSONObject.getJSONObject(
			"attributes");
		debug = configurationJSONObject.getBoolean("debug");
		locale = (Locale)configurationJSONObject.get("locale");
		taskContext = TaskContextThreadLocal.getTaskContext();
	}

	@Override
	public String getName() {
		return name;
	}

	protected Prompt applyPromptTemplateVariables(
		Map<String, Object> input, PromptTemplate promptTemplate) {

		Map<String, Object> promptTemplateVariables = new HashMap<>();

		MapUtil.isNotEmptyForEach(
			taskContext.getTaskContextParameters(),
			(key, value) -> promptTemplateVariables.put(
				key, value.getStringValue()));

		MapUtil.isNotEmptyForEach(
			input,
			(key, value) -> {
				if (key.equals("history") && (value != null)) {
					promptTemplateVariables.put(
						key, chatHistoryToString(value));
				}
				else {
					promptTemplateVariables.put(key, value);
				}
			});

		_ensurePromptTemplateVariables(promptTemplate, promptTemplateVariables);

		return promptTemplate.apply(promptTemplateVariables);
	}

	protected String chatHistoryToString(Object value) {
		List<Map<String, String>> messages = (List<Map<String, String>>)value;

		StringBundler sb = new StringBundler();

		for (Map<String, String> message : messages) {
			sb.append(message.get("role"));
			sb.append(message.get(": "));
			sb.append(message.get("text"));
			sb.append("\n");
		}

		return sb.toString();
	}

	protected Map<String, Object> getDebugInfo(
		boolean fromCache, boolean debug, Result<?> result) {

		if (!debug) {
			return null;
		}

		if (result == null) {
			return new HashMap<>();
		}

		TokenUsage tokenUsage = result.tokenUsage();

		return HashMapBuilder.<String, Object>put(
			"fromCache", fromCache
		).put(
			"inputTokenCount", tokenUsage.inputTokenCount()
		).put(
			"outputTokenCount", tokenUsage.outputTokenCount()
		).put(
			"totalTokenCount", tokenUsage.totalTokenCount()
		).build();
	}

	protected String getExecutionTime(long startTime) {
		return (System.currentTimeMillis() - startTime) + "ms";
	}

	protected Map<String, Object> getMultiImageTaskDebugInfo(
		boolean debug, String executionTime, Response<List<Image>> response) {

		if (!debug) {
			return null;
		}

		Image image = response.content(
		).get(
			0
		);

		return HashMapBuilder.<String, Object>put(
			"executionTime", executionTime
		).put(
			"finishReason", response.finishReason()
		).put(
			"mimeTypes", image.mimeType()
		).put(
			"revisedPrompt", image.revisedPrompt()
		).put(
			"tokenUsage", response.tokenUsage()
		).build();
	}

	protected PromptTemplate getPromptTemplate(String promptField) {
		String promptTemplateString = attributesJSONObject.getString(
			promptField);

		if (Validator.isBlank(promptTemplateString)) {
			return null;
		}

		return PromptTemplate.from(promptTemplateString);
	}

	protected Map<String, Object> getSingleImageTaskDebugInfo(
		boolean debug, String executionTime, Response<Image> response) {

		if (!debug) {
			return null;
		}

		Image image = response.content();

		return HashMapBuilder.<String, Object>put(
			"executionTime", executionTime
		).put(
			"finishReason", response.finishReason()
		).put(
			"mimeTypes", image.mimeType()
		).put(
			"revisedPrompt", image.revisedPrompt()
		).put(
			"tokenUsage", response.tokenUsage()
		).build();
	}

	protected String getSystemMessage(Map<String, Object> input) {
		PromptTemplate promptTemplate = getPromptTemplate("system_message");

		if (promptTemplate == null) {
			return StringPool.BLANK;
		}

		Prompt prompt = applyPromptTemplateVariables(input, promptTemplate);

		return prompt.text();
	}

	protected List<Object> getTools() {
		JSONArray toolsJSONArray = attributesJSONObject.getJSONArray("tools");

		if (toolsJSONArray == null) {
			return Collections.emptyList();
		}

		List<Object> tools = new ArrayList<>();

		for (int i = 0; i < toolsJSONArray.length(); i++) {
			Object object = toolsJSONArray.get(i);

			Object tool = null;

			if (object instanceof JSONObject) {
				JSONObject toolsJSONObject = (JSONObject)object;

				tool = aiToolsProvider.getTool(
					toolsJSONObject.getJSONObject("configuration"),
					(String)object);
			}
			else if (object instanceof String) {
				tool = aiToolsProvider.getTool(null, (String)object);
			}

			if (tool != null) {
				tools.add(tool);
			}
		}

		return tools;
	}

	protected String getUserMessage(Map<String, Object> input) {
		PromptTemplate promptTemplate = getPromptTemplate("prompt_template");

		if (promptTemplate == null) {
			return MapUtil.getString(input, "text");
		}

		Prompt prompt = applyPromptTemplateVariables(input, promptTemplate);

		return prompt.text();
	}

	protected String replaceTemplateVariables(Locale locale, String s) {
		return StringUtil.replace(
			s, "${language_id}", LocaleUtil.toLanguageId(locale));
	}

	protected Map<String, Object> toImageEntry(Image image) {
		if (image.url() != null) {
			return HashMapBuilder.<String, Object>put(
				"url", image.url()
			).build();
		}

		return HashMapBuilder.<String, Object>put(
			"base64Data", image.base64Data()
		).build();
	}

	protected TaskResponse toImageTaskResponse(
		boolean debug, ImageModel imageModel, Map<String, Object> input) {

		int numberOfImages = attributesJSONObject.getInt("number_of_images", 1);

		if (numberOfImages > 1) {
			return toMultiImageTaskResponse(
				debug, imageModel, input, numberOfImages);
		}

		return toSingleImageTaskResponse(debug, imageModel, input);
	}

	protected TaskResponse toMultiImageTaskResponse(
		boolean debug, ImageModel imageModel, Map<String, Object> input,
		int numberOfImages) {

		long currentTimeMillis = System.currentTimeMillis();

		Response<List<Image>> response = imageModel.generate(
			getUserMessage(input), numberOfImages);

		List<Image> images = response.content();

		List<Map<String, Object>> output = new ArrayList<>();

		for (Image image : images) {
			output.add(toImageEntry(image));
		}

		return toTaskResponse(
			getMultiImageTaskDebugInfo(
				debug, getExecutionTime(currentTimeMillis), response),
			output);
	}

	protected TaskResponse toSingleImageTaskResponse(
		boolean debug, ImageModel imageModel, Map<String, Object> input) {

		long currentTimeMillis = System.currentTimeMillis();

		Response<Image> response = imageModel.generate(getUserMessage(input));

		Image image = response.content();

		return toTaskResponse(
			getSingleImageTaskDebugInfo(
				debug, getExecutionTime(currentTimeMillis), response),
			toImageEntry(image));
	}

	protected String toStringValue(Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof String) {
			return (String)value;
		}

		return String.valueOf(value);
	}

	protected TaskResponse toTaskResponse(
		Map<String, Object> debugInfo, Object value) {

		if (value == null) {
			return new TaskResponseImpl(debugInfo, null);
		}

		String contextOutputVariableName = attributesJSONObject.getString(
			"context_output_parameter_name");

		if (!Validator.isBlank(contextOutputVariableName)) {
			taskContext.addTaskContextParameter(
				contextOutputVariableName,
				new TaskContextParameter(toStringValue(value), value));

			debugInfo.put("context_output_parameter_value", value);

			return new TaskResponseImpl(debugInfo, null);
		}

		return new TaskResponseImpl(
			debugInfo,
			HashMapBuilder.put(
				attributesJSONObject.getString("output_parameter_name", "text"),
				value
			).build());
	}

	protected TaskResponse toTaskResponse(
		Map<String, Object> debugInfo, Result<?> result) {

		if (result == null) {
			return new TaskResponseImpl(debugInfo, null);
		}

		return toTaskResponse(debugInfo, result.content());
	}

	protected final AIToolsProvider aiToolsProvider;
	protected final JSONObject attributesJSONObject;
	protected final JSONObject configurationJSONObject;
	protected final boolean debug;
	protected final GenerativeAITaskConfigurationProvider
		generativeAIConfigurationProvider;
	protected final Locale locale;
	protected final String name;
	protected final TaskContext taskContext;

	private void _ensurePromptTemplateVariables(
		PromptTemplate promptTemplate,
		Map<String, Object> promptTemplateVariables) {

		String[] values = StringUtils.substringsBetween(
			promptTemplate.template(), "{{", "}}");

		if (ArrayUtil.isEmpty(values)) {
			return;
		}

		for (String value : values) {
			if (!promptTemplateVariables.containsKey(value)) {
				promptTemplateVariables.put(value, StringPool.BLANK);
			}
		}
	}

}