/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.task.openai;

import com.liferay.generative.ai.task.configuration.GenerativeAITaskConfigurationProvider;
import com.liferay.generative.ai.task.exception.TaskDefinitionConfigurationJSONException;
import com.liferay.generative.ai.task.exception.TaskTestException;
import com.liferay.generative.ai.task.internal.task.BaseTask;
import com.liferay.generative.ai.task.internal.task.tools.AIToolsProvider;
import com.liferay.generative.ai.task.internal.util.SetterUtil;
import com.liferay.generative.ai.task.task.Task;
import com.liferay.generative.ai.task.task.TaskResponse;
import com.liferay.portal.kernel.json.JSONObject;

import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openai.OpenAiImageModel;

import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public class OpenAIImageModelTask extends BaseTask implements Task {

	public OpenAIImageModelTask(
		JSONObject definitionJSONObject,
		GenerativeAITaskConfigurationProvider generativeAIConfigurationProvider,
		AIToolsProvider toolsProvider) {

		super(
			toolsProvider, definitionJSONObject,
			generativeAIConfigurationProvider, "openai_image_model");
	}

	@Override
	public TaskResponse execute(boolean debug, Map<String, Object> input) {
		return toImageTaskResponse(debug, _getImageModel(), input);
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

	@Override
	protected String toStringValue(Object value) {
		return null;
	}

	private ImageModel _getImageModel() {
		OpenAiImageModel.OpenAiImageModelBuilder builder =
			OpenAiImageModel.builder();

		SetterUtil.setNotBlankString(
			builder::apiKey, attributesJSONObject.getString("api_key"));
		SetterUtil.setNotNullInteger(
			builder::maxRetries, attributesJSONObject.getInt("max_retries"));
		SetterUtil.setNotBlankString(
			builder::modelName, attributesJSONObject.getString("model_name"));
		SetterUtil.setNotBlankString(
			builder::quality, attributesJSONObject.getString("quality"));
		SetterUtil.setNotBlankString(
			builder::responseFormat,
			attributesJSONObject.getString("response_format"));
		SetterUtil.setNotBlankString(
			builder::size, attributesJSONObject.getString("size"));
		SetterUtil.setNotBlankString(
			builder::style, attributesJSONObject.getString("style"));
		SetterUtil.setNotBlankString(
			builder::user, attributesJSONObject.getString("user"));

		if (configurationJSONObject.getBoolean("debug")) {
			builder.logRequests(true);
			builder.logResponses(true);
		}

		return builder.build();
	}

}