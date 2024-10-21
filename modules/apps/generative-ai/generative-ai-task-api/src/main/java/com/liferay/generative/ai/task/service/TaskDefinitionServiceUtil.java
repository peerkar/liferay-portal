/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.service;

import com.liferay.generative.ai.task.model.TaskDefinition;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.service.Snapshot;

import java.util.Map;

/**
 * Provides the remote service utility for TaskDefinition. This utility wraps
 * <code>com.liferay.generative.ai.task.service.impl.TaskDefinitionServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see TaskDefinitionService
 * @generated
 */
public class TaskDefinitionServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.generative.ai.task.service.impl.TaskDefinitionServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static TaskDefinition addTaskDefinition(
			String configurationJSON,
			Map<java.util.Locale, String> descriptionMap,
			String externalReferenceCode, boolean readOnly,
			String schemaVersion,
			com.liferay.portal.kernel.service.ServiceContext serviceContext,
			Map<java.util.Locale, String> titleMap)
		throws PortalException {

		return getService().addTaskDefinition(
			configurationJSON, descriptionMap, externalReferenceCode, readOnly,
			schemaVersion, serviceContext, titleMap);
	}

	public static TaskDefinition deleteTaskDefinition(long taskDefinitionId)
		throws PortalException {

		return getService().deleteTaskDefinition(taskDefinitionId);
	}

	public static TaskDefinition fetchTaskDefinition(long taskDefinitionId)
		throws PortalException {

		return getService().fetchTaskDefinition(taskDefinitionId);
	}

	public static TaskDefinition fetchTaskDefinitionByExternalReferenceCode(
			String externalReferenceCode, long companyId)
		throws PortalException {

		return getService().fetchTaskDefinitionByExternalReferenceCode(
			externalReferenceCode, companyId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static TaskDefinition getTaskDefinition(long taskDefinitionId)
		throws PortalException {

		return getService().getTaskDefinition(taskDefinitionId);
	}

	public static TaskDefinition getTaskDefinitionByExternalReferenceCode(
			long companyId, String externalReferenceCode)
		throws PortalException {

		return getService().getTaskDefinitionByExternalReferenceCode(
			companyId, externalReferenceCode);
	}

	public static TaskDefinition updateTaskDefinition(
			String configurationJSON,
			Map<java.util.Locale, String> descriptionMap,
			String externalReferenceCode, long taskDefinitionId,
			String schemaVersion,
			com.liferay.portal.kernel.service.ServiceContext serviceContext,
			Map<java.util.Locale, String> titleMap)
		throws PortalException {

		return getService().updateTaskDefinition(
			configurationJSON, descriptionMap, externalReferenceCode,
			taskDefinitionId, schemaVersion, serviceContext, titleMap);
	}

	public static TaskDefinitionService getService() {
		return _serviceSnapshot.get();
	}

	private static final Snapshot<TaskDefinitionService> _serviceSnapshot =
		new Snapshot<>(
			TaskDefinitionServiceUtil.class, TaskDefinitionService.class);

}