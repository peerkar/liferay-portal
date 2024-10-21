/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.service.impl;

import com.liferay.generative.ai.task.constants.TaskDefinitionActionKeys;
import com.liferay.generative.ai.task.constants.TaskDefinitionConstants;
import com.liferay.generative.ai.task.exception.TaskDefinitionReadOnlyException;
import com.liferay.generative.ai.task.model.TaskDefinition;
import com.liferay.generative.ai.task.service.base.TaskDefinitionServiceBaseImpl;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.service.ServiceContext;

import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = {
		"json.web.service.context.name=generativeai",
		"json.web.service.context.path=TaskDefinition"
	},
	service = AopService.class
)
public class TaskDefinitionServiceImpl extends TaskDefinitionServiceBaseImpl {

	@Override
	public TaskDefinition addTaskDefinition(
			String configurationJSON, Map<Locale, String> descriptionMap,
			String externalReferenceCode, boolean readOnly,
			String schemaVersion, ServiceContext serviceContext,
			Map<Locale, String> titleMap)
		throws PortalException {

		_portletResourcePermission.check(
			getPermissionChecker(), null,
			TaskDefinitionActionKeys.ADD_TASK_DEFINITION);

		return taskDefinitionLocalService.addTaskDefinition(
			configurationJSON, descriptionMap, externalReferenceCode, readOnly,
			schemaVersion, serviceContext, titleMap, getUserId());
	}

	@Override
	public TaskDefinition deleteTaskDefinition(long taskDefinitionId)
		throws PortalException {

		_taskDefinitionModelResourcePermission.check(
			getPermissionChecker(), taskDefinitionId, ActionKeys.DELETE);

		TaskDefinition taskDefinition =
			taskDefinitionPersistence.findByPrimaryKey(taskDefinitionId);

		if (taskDefinition.isReadOnly()) {
			throw new TaskDefinitionReadOnlyException(
				StringBundler.concat(
					"Task definition  ", taskDefinitionId, " is read-only"));
		}

		return taskDefinitionLocalService.deleteTaskDefinition(
			taskDefinitionId);
	}

	@Override
	public TaskDefinition fetchTaskDefinition(long taskDefinitionId)
		throws PortalException {

		TaskDefinition taskDefinition =
			taskDefinitionLocalService.fetchTaskDefinition(taskDefinitionId);

		if (taskDefinition != null) {
			_taskDefinitionModelResourcePermission.check(
				getPermissionChecker(), taskDefinition, ActionKeys.VIEW);
		}

		return taskDefinition;
	}

	@Override
	public TaskDefinition fetchTaskDefinitionByExternalReferenceCode(
			String externalReferenceCode, long companyId)
		throws PortalException {

		TaskDefinition taskDefinition =
			taskDefinitionLocalService.
				fetchTaskDefinitionByExternalReferenceCode(
					externalReferenceCode, companyId);

		if (taskDefinition != null) {
			_taskDefinitionModelResourcePermission.check(
				getPermissionChecker(), taskDefinition, ActionKeys.VIEW);
		}

		return taskDefinition;
	}

	@Override
	public TaskDefinition getTaskDefinition(long taskDefinitionId)
		throws PortalException {

		TaskDefinition taskDefinition =
			taskDefinitionLocalService.getTaskDefinition(taskDefinitionId);

		_taskDefinitionModelResourcePermission.check(
			getPermissionChecker(), taskDefinition,
			TaskDefinitionActionKeys.APPLY_TASK_DEFINITION);

		return taskDefinition;
	}

	@Override
	public TaskDefinition getTaskDefinitionByExternalReferenceCode(
			long companyId, String externalReferenceCode)
		throws PortalException {

		TaskDefinition taskDefinition =
			taskDefinitionLocalService.getTaskDefinitionByExternalReferenceCode(
				externalReferenceCode, companyId);

		_taskDefinitionModelResourcePermission.check(
			getPermissionChecker(), taskDefinition,
			TaskDefinitionActionKeys.APPLY_TASK_DEFINITION);

		return taskDefinition;
	}

	@Override
	public TaskDefinition updateTaskDefinition(
			String configurationJSON, Map<Locale, String> descriptionMap,
			String externalReferenceCode, long taskDefinitionId,
			String schemaVersion, ServiceContext serviceContext,
			Map<Locale, String> titleMap)
		throws PortalException {

		_taskDefinitionModelResourcePermission.check(
			getPermissionChecker(), taskDefinitionId, ActionKeys.UPDATE);

		TaskDefinition taskDefinition =
			taskDefinitionPersistence.findByPrimaryKey(taskDefinitionId);

		if (taskDefinition.isReadOnly()) {
			throw new TaskDefinitionReadOnlyException(
				StringBundler.concat(
					"Task definition ", taskDefinitionId, " is read-only"));
		}

		return taskDefinitionLocalService.updateTaskDefinition(
			configurationJSON, descriptionMap, externalReferenceCode,
			taskDefinitionId, schemaVersion, serviceContext, titleMap);
	}

	@Reference(
		target = "(resource.name=" + TaskDefinitionConstants.RESOURCE_NAME + ")"
	)
	private volatile PortletResourcePermission _portletResourcePermission;

	@Reference(
		target = "(model.class.name=com.liferay.generative.ai.task.model.TaskDefinition)"
	)
	private volatile ModelResourcePermission<TaskDefinition>
		_taskDefinitionModelResourcePermission;

}