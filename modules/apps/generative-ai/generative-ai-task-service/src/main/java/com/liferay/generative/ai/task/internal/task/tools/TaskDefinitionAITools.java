/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.task.tools;

// Demo class for task definition operations

import com.liferay.generative.ai.task.model.TaskDefinition;
import com.liferay.generative.ai.task.service.TaskDefinitionLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleThreadLocal;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

/**
 * @author Louis-Guillaume Durand
 */
public class TaskDefinitionAITools implements AITools {

	public TaskDefinitionAITools(JSONObject configurationJSONObject) {
		_configurationJSONObject = configurationJSONObject;
	}

	@Override
	public JSONObject getConfigurationJSONObject() {
		return _configurationJSONObject;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		TaskDefinitionAITools.class);

	@Tool("The 'AI Task Definition Tool' to create a new AI task definition")
	TaskDefinition createTaskDefinition(
		@P("The configuration of the AI task definition as a JSON string")
			String configurationJSON,
		@P("The title of the AI task definition to be created") String title,
		@P("The description of the AI task definition to be created") String
			description,
		@P("The unique code name of the AI task definition to be created")
			String externalReferenceCode) {

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		configurationJSON = configurationJSON.replace(
			"[user_input]", "{{text}}");

		try {
			return TaskDefinitionLocalServiceUtil.addTaskDefinition(
				configurationJSON,
				HashMapBuilder.put(
					LocaleThreadLocal.getDefaultLocale(), description
				).build(),
				externalReferenceCode, false, "1.0", serviceContext,
				HashMapBuilder.put(
					LocaleThreadLocal.getDefaultLocale(), title
				).build(),
				PrincipalThreadLocal.getUserId());
		}
		catch (PortalException portalException) {
			_log.error("Failed to create a Task Definition", portalException);
		}

		return null;
	}

	private final JSONObject _configurationJSONObject;

}