/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link TaskDefinitionService}.
 *
 * @author Brian Wing Shun Chan
 * @see TaskDefinitionService
 * @generated
 */
public class TaskDefinitionServiceWrapper
	implements ServiceWrapper<TaskDefinitionService>, TaskDefinitionService {

	public TaskDefinitionServiceWrapper() {
		this(null);
	}

	public TaskDefinitionServiceWrapper(
		TaskDefinitionService taskDefinitionService) {

		_taskDefinitionService = taskDefinitionService;
	}

	@Override
	public com.liferay.generative.ai.task.model.TaskDefinition
			addTaskDefinition(
				String configurationJSON,
				java.util.Map<java.util.Locale, String> descriptionMap,
				String externalReferenceCode, boolean readOnly,
				String schemaVersion,
				com.liferay.portal.kernel.service.ServiceContext serviceContext,
				java.util.Map<java.util.Locale, String> titleMap)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _taskDefinitionService.addTaskDefinition(
			configurationJSON, descriptionMap, externalReferenceCode, readOnly,
			schemaVersion, serviceContext, titleMap);
	}

	@Override
	public com.liferay.generative.ai.task.model.TaskDefinition
			deleteTaskDefinition(long taskDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _taskDefinitionService.deleteTaskDefinition(taskDefinitionId);
	}

	@Override
	public com.liferay.generative.ai.task.model.TaskDefinition
			fetchTaskDefinition(long taskDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _taskDefinitionService.fetchTaskDefinition(taskDefinitionId);
	}

	@Override
	public com.liferay.generative.ai.task.model.TaskDefinition
			fetchTaskDefinitionByExternalReferenceCode(
				String externalReferenceCode, long companyId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _taskDefinitionService.
			fetchTaskDefinitionByExternalReferenceCode(
				externalReferenceCode, companyId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _taskDefinitionService.getOSGiServiceIdentifier();
	}

	@Override
	public com.liferay.generative.ai.task.model.TaskDefinition
			getTaskDefinition(long taskDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _taskDefinitionService.getTaskDefinition(taskDefinitionId);
	}

	@Override
	public com.liferay.generative.ai.task.model.TaskDefinition
			getTaskDefinitionByExternalReferenceCode(
				long companyId, String externalReferenceCode)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _taskDefinitionService.getTaskDefinitionByExternalReferenceCode(
			companyId, externalReferenceCode);
	}

	@Override
	public com.liferay.generative.ai.task.model.TaskDefinition
			updateTaskDefinition(
				String configurationJSON,
				java.util.Map<java.util.Locale, String> descriptionMap,
				String externalReferenceCode, long taskDefinitionId,
				String schemaVersion,
				com.liferay.portal.kernel.service.ServiceContext serviceContext,
				java.util.Map<java.util.Locale, String> titleMap)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _taskDefinitionService.updateTaskDefinition(
			configurationJSON, descriptionMap, externalReferenceCode,
			taskDefinitionId, schemaVersion, serviceContext, titleMap);
	}

	@Override
	public TaskDefinitionService getWrappedService() {
		return _taskDefinitionService;
	}

	@Override
	public void setWrappedService(TaskDefinitionService taskDefinitionService) {
		_taskDefinitionService = taskDefinitionService;
	}

	private TaskDefinitionService _taskDefinitionService;

}