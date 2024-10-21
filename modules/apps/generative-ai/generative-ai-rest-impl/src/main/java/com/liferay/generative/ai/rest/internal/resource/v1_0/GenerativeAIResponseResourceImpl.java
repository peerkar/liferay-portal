/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.rest.internal.resource.v1_0;

import com.liferay.generative.ai.request.GenerativeAIRequestBuilder;
import com.liferay.generative.ai.request.GenerativeAIRequestBuilderFactory;
import com.liferay.generative.ai.request.GenerativeAIRequestExecutor;
import com.liferay.generative.ai.rest.dto.v1_0.GenerativeAIRequest;
import com.liferay.generative.ai.rest.dto.v1_0.GenerativeAIResponse;
import com.liferay.generative.ai.rest.resource.v1_0.GenerativeAIResponseResource;
import com.liferay.generative.ai.task.model.TaskDefinition;
import com.liferay.generative.ai.task.service.TaskDefinitionService;
import com.liferay.generative.ai.task.task.TaskBuilder;
import com.liferay.generative.ai.task.task.context.TaskContext;
import com.liferay.generative.ai.task.task.context.TaskContextParameter;
import com.liferay.generative.ai.task.task.context.TaskContextThreadLocal;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Petteri Karttunen
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/generative-ai-response.properties",
	scope = ServiceScope.PROTOTYPE, service = GenerativeAIResponseResource.class
)
public class GenerativeAIResponseResourceImpl
	extends BaseGenerativeAIResponseResourceImpl {

	@Override
	public GenerativeAIResponse postGenerateExternalReferenceCode(
			String externalReferenceCode,
			GenerativeAIRequest generativeAIRequest)
		throws Exception {

		return _toDTO(
			_generativeAIRequestExecutor.execute(
				_createGenerativeAIRequest(
					externalReferenceCode, generativeAIRequest)));
	}

	private void _contributeContextParameters(TaskContext taskContext) {
		taskContext.addTaskContextParameter(
			"ipAddress",
			new TaskContextParameter(
				contextHttpServletRequest.getRemoteAddr()));
		taskContext.addTaskContextParameter(
			"timeZone",
			new TaskContextParameter(
				contextUser.getTimeZone(
				).getDisplayName(),
				contextUser.getTimeZone()));
	}

	private void _contributeUserParameters(TaskContext taskContext) {
		if (contextUser.isGuestUser()) {
			return;
		}

		try {
			taskContext.addTaskContextParameter(
				"userBirthday",
				new TaskContextParameter(
					String.valueOf(contextUser.getBirthday()),
					contextUser.getBirthday()));
			taskContext.addTaskContextParameter(
				"userFirstName",
				new TaskContextParameter(contextUser.getFirstName()));
			taskContext.addTaskContextParameter(
				"userFullName",
				new TaskContextParameter(contextUser.getFullName()));
			taskContext.addTaskContextParameter(
				"userIsFemale",
				new TaskContextParameter(
					String.valueOf(contextUser.isFemale()),
					contextUser.isFemale()));

			boolean genderX = false;

			if (!contextUser.isFemale() && contextUser.isMale()) {
				genderX = true;
			}

			taskContext.addTaskContextParameter(
				"userIsGenderX",
				new TaskContextParameter(String.valueOf(genderX), genderX));
			taskContext.addTaskContextParameter(
				"userIsMale",
				new TaskContextParameter(
					String.valueOf(contextUser.isMale()),
					contextUser.isMale()));
			taskContext.addTaskContextParameter(
				"userJobTitle",
				new TaskContextParameter(contextUser.getJobTitle()));

			Locale locale = contextUser.getLocale();

			taskContext.addTaskContextParameter(
				"userLanguage",
				new TaskContextParameter(locale.getDisplayLanguage()));

			taskContext.addTaskContextParameter(
				"userLastName",
				new TaskContextParameter(contextUser.getLastName()));
		}
		catch (PortalException portalException) {
			throw new RuntimeException(portalException);
		}
	}

	private com.liferay.generative.ai.request.GenerativeAIRequest
			_createGenerativeAIRequest(
				String externalReferenceCode,
				GenerativeAIRequest generativeAIRequest)
		throws Exception {

		GenerativeAIRequestBuilder generativeAIRequestBuilder =
			_generativeAIRequestBuilderFactory.builder();

		TaskDefinition taskDefinition =
			_taskDefinitionService.getTaskDefinitionByExternalReferenceCode(
				contextCompany.getCompanyId(), externalReferenceCode);

		JSONObject configurationJSONObject = _jsonFactory.createJSONObject(
			taskDefinition.getConfigurationJSON());

		generativeAIRequestBuilder.debug(
			configurationJSONObject.getBoolean("debug", false));

		TaskContextThreadLocal.setTaskContext(_createTaskContext(taskDefinition));

		generativeAIRequestBuilder.input(
			(Map<String, Object>)generativeAIRequest.getInput());
		generativeAIRequestBuilder.task(
			_taskBuilder.build(
				configurationJSONObject));

		return generativeAIRequestBuilder.build();
	}

	private TaskContext _createTaskContext(TaskDefinition taskDefinition)
		throws Exception {

		TaskContext taskContext = new TaskContext(
			contextCompany.getCompanyId(),
			contextAcceptLanguage.getPreferredLocale(),
			taskDefinition.getExternalReferenceCode(), contextUser.getUserId());

		_contributeContextParameters(taskContext);
		_contributeUserParameters(taskContext);

		return taskContext;
	}

	private GenerativeAIResponse _toDTO(
		com.liferay.generative.ai.response.GenerativeAIResponse
			generativeAIResponse) {

		return new GenerativeAIResponse() {
			{
				if (generativeAIResponse.getDebugInfo() != null) {
					setDebugInfo(generativeAIResponse::getDebugInfo);
				}

				setOutput(generativeAIResponse::getOutput);
				setTook(generativeAIResponse::getTook);
			}
		};
	}

	@Reference
	private GenerativeAIRequestBuilderFactory
		_generativeAIRequestBuilderFactory;

	@Reference
	private GenerativeAIRequestExecutor _generativeAIRequestExecutor;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private TaskBuilder _taskBuilder;

	@Reference
	private TaskDefinitionService _taskDefinitionService;

}