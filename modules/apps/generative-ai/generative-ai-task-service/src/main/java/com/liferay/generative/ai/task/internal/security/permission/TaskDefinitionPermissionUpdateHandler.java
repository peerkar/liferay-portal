/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.security.permission;

import com.liferay.generative.ai.task.model.TaskDefinition;
import com.liferay.generative.ai.task.service.TaskDefinitionLocalService;
import com.liferay.portal.kernel.security.permission.PermissionUpdateHandler;
import com.liferay.portal.kernel.util.GetterUtil;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = "model.class.name=com.liferay.generative.ai.task.model.TaskDefinition",
	service = PermissionUpdateHandler.class
)
public class TaskDefinitionPermissionUpdateHandler
	implements PermissionUpdateHandler {

	@Override
	public void updatedPermission(String primKey) {
		TaskDefinition taskDefinition =
			_taskDefinitionLocalService.fetchTaskDefinition(
				GetterUtil.getLong(primKey));

		if (taskDefinition == null) {
			return;
		}

		_taskDefinitionLocalService.updateTaskDefinition(taskDefinition);
	}

	@Reference
	private TaskDefinitionLocalService _taskDefinitionLocalService;

}