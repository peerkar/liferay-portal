/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.task.google;

import com.liferay.generative.ai.task.configuration.GenerativeAITaskConfigurationProvider;
import com.liferay.generative.ai.task.exception.TaskDefinitionConfigurationJSONException;
import com.liferay.generative.ai.task.exception.TaskTestException;
import com.liferay.generative.ai.task.internal.task.BaseTask;
import com.liferay.generative.ai.task.internal.util.SetterUtil;
import com.liferay.generative.ai.task.task.Task;
import com.liferay.generative.ai.task.task.TaskResponse;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.Validator;

import dev.langchain4j.model.vertexai.VertexAiImageModel;

import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public class GoogleImagenTask extends BaseTask implements Task {

	public GoogleImagenTask(
		JSONObject definitionJSONObject,
		GenerativeAITaskConfigurationProvider
			generativeAIConfigurationProvider) {

		super(
			null, definitionJSONObject, generativeAIConfigurationProvider,
			"google_imagen");
	}

	@Override
	@SuppressWarnings("unchecked")
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

	private VertexAiImageModel _getImageModel() {
		VertexAiImageModel.Builder builder = VertexAiImageModel.builder();

		SetterUtil.setNotBlankString(
			builder::endpoint, attributesJSONObject.getString("endpoint"));
		SetterUtil.setNotNullInteger(
			builder::guidanceScale,
			attributesJSONObject.getInt("guidance_scale"));
		SetterUtil.setNotBlankString(
			builder::language, attributesJSONObject.getString("language"));
		SetterUtil.setNotBlankString(
			builder::location, attributesJSONObject.getString("location"));
		SetterUtil.setNotNullInteger(
			builder::maxRetries, attributesJSONObject.getInt("max_retries"));
		SetterUtil.setNotBlankString(
			builder::modelName, attributesJSONObject.getString("model_name"));
		SetterUtil.setNotBlankString(
			builder::negativePrompt,
			attributesJSONObject.getString("negative_prompt"));
		SetterUtil.setNotBlankString(
			builder::project, attributesJSONObject.getString("project"));
		SetterUtil.setNotBlankString(
			builder::publisher, attributesJSONObject.getString("publisher"));
		SetterUtil.setNotNullInteger(
			builder::sampleImageSize,
			attributesJSONObject.getInt("sample_image_size"));

		VertexAiImageModel.ImageStyle imageStyle = _getImageStyle();

		if (imageStyle != null) {
			builder.sampleImageStyle(imageStyle);
		}

		SetterUtil.setNotNullLong(
			builder::seed, attributesJSONObject.getLong("seed"));

		return builder.build();
	}

	private VertexAiImageModel.ImageStyle _getImageStyle() {
		String imageStyle = attributesJSONObject.getString("image_style");

		if (Validator.isBlank(imageStyle)) {
			return null;
		}

		if (imageStyle.equals("cyberpunk")) {
			return VertexAiImageModel.ImageStyle.CYBERPUNK;
		}
		else if (imageStyle.equals("digital_art")) {
			return VertexAiImageModel.ImageStyle.DIGITAL_ART;
		}
		else if (imageStyle.equals("landscape")) {
			return VertexAiImageModel.ImageStyle.LANDSCAPE;
		}
		else if (imageStyle.equals("photograph")) {
			return VertexAiImageModel.ImageStyle.PHOTOGRAPH;
		}
		else if (imageStyle.equals("pop_art")) {
			return VertexAiImageModel.ImageStyle.POP_ART;
		}
		else if (imageStyle.equals("sketch")) {
			return VertexAiImageModel.ImageStyle.SKETCH;
		}
		else if (imageStyle.equals("watercolor")) {
			return VertexAiImageModel.ImageStyle.WATERCOLOR;
		}

		throw new IllegalArgumentException("Invalid image style " + imageStyle);
	}

}