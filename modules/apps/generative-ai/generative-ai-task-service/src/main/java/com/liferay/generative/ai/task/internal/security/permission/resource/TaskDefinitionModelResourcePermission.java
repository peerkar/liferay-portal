/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.security.permission.resource;

import com.liferay.generative.ai.task.constants.TaskDefinitionConstants;
import com.liferay.generative.ai.task.model.TaskDefinition;
import com.liferay.generative.ai.task.service.TaskDefinitionLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = "model.class.name=com.liferay.generative.ai.task.model.TaskDefinition",
	service = ModelResourcePermission.class
)
public class TaskDefinitionModelResourcePermission
	implements ModelResourcePermission<TaskDefinition> {

	@Override
	public void check(
			PermissionChecker permissionChecker, long taskDefinitionId,
			String actionId)
		throws PortalException {

		if (!contains(permissionChecker, taskDefinitionId, actionId)) {
			throw new PrincipalException.MustHavePermission(
				permissionChecker, TaskDefinition.class.getName(),
				taskDefinitionId, actionId);
		}
	}

	@Override
	public void check(
			PermissionChecker permissionChecker, TaskDefinition taskDefinition,
			String actionId)
		throws PortalException {

		if (!contains(permissionChecker, taskDefinition, actionId)) {
			throw new PrincipalException.MustHavePermission(
				permissionChecker, TaskDefinition.class.getName(),
				taskDefinition.getPrimaryKey(), actionId);
		}
	}

	@Override
	public boolean contains(
			PermissionChecker permissionChecker, long taskDefinitionId,
			String actionId)
		throws PortalException {

		return contains(
			permissionChecker,
			_taskDefinitionLocalService.getTaskDefinition(taskDefinitionId),
			actionId);
	}

	@Override
	public boolean contains(
			PermissionChecker permissionChecker, TaskDefinition taskDefinition,
			String actionId)
		throws PortalException {

		if (permissionChecker.hasOwnerPermission(
				permissionChecker.getCompanyId(),
				TaskDefinition.class.getName(),
				taskDefinition.getTaskDefinitionId(),
				taskDefinition.getUserId(), actionId) ||
			(permissionChecker.getUserId() == taskDefinition.getUserId()) ||
			permissionChecker.hasPermission(
				null, TaskDefinition.class.getName(),
				taskDefinition.getPrimaryKey(), actionId)) {

			return true;
		}

		return false;
	}

	@Override
	public String getModelName() {
		return TaskDefinition.class.getName();
	}

	@Override
	public PortletResourcePermission getPortletResourcePermission() {
		return _portletResourcePermission;
	}

	@Reference(
		target = "(resource.name=" + TaskDefinitionConstants.RESOURCE_NAME + ")"
	)
	private PortletResourcePermission _portletResourcePermission;

	@Reference
	private TaskDefinitionLocalService _taskDefinitionLocalService;

}