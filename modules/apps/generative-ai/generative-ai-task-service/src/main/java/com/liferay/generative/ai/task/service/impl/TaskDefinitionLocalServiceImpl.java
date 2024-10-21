/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.service.impl;

import com.liferay.generative.ai.task.exception.TaskDefinitionTitleException;
import com.liferay.generative.ai.task.model.TaskDefinition;
import com.liferay.generative.ai.task.service.base.TaskDefinitionLocalServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.ResourceLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = "model.class.name=com.liferay.generative.ai.task.model.TaskDefinition",
	service = AopService.class
)
public class TaskDefinitionLocalServiceImpl
	extends TaskDefinitionLocalServiceBaseImpl {

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public TaskDefinition addTaskDefinition(
			String configurationJSON, Map<Locale, String> descriptionMap,
			String externalReferenceCode, boolean readOnly,
			String schemaVersion, ServiceContext serviceContext,
			Map<Locale, String> titleMap, long userId)
		throws PortalException {

		_validate(titleMap, serviceContext);

		TaskDefinition taskDefinition = taskDefinitionPersistence.create(
			counterLocalService.increment());

		User user = _userLocalService.getUser(userId);

		taskDefinition.setExternalReferenceCode(externalReferenceCode);
		taskDefinition.setCompanyId(user.getCompanyId());
		taskDefinition.setUserId(user.getUserId());
		taskDefinition.setUserName(user.getFullName());
		taskDefinition.setConfigurationJSON(configurationJSON);
		taskDefinition.setDescriptionMap(descriptionMap);
		taskDefinition.setReadOnly(readOnly);
		taskDefinition.setSchemaVersion(schemaVersion);
		taskDefinition.setTitleMap(titleMap);
		taskDefinition.setVersion(
			String.format(
				"%.1f",
				GetterUtil.getFloat(taskDefinition.getVersion(), 0.9F) + 0.1));
		taskDefinition.setStatus(WorkflowConstants.STATUS_APPROVED);
		taskDefinition.setStatusByUserId(user.getUserId());
		taskDefinition.setStatusDate(serviceContext.getModifiedDate(null));

		taskDefinition = taskDefinitionPersistence.update(taskDefinition);

		_resourceLocalService.addModelResources(taskDefinition, serviceContext);

		return taskDefinition;
	}

	@Override
	public void deleteCompanyTaskDefinitions(long companyId)
		throws PortalException {

		List<TaskDefinition> taskDefinitions =
			taskDefinitionPersistence.findByCompanyId(companyId);

		for (TaskDefinition taskDefinition : taskDefinitions) {
			taskDefinitionLocalService.deleteTaskDefinition(taskDefinition);
		}
	}

	@Indexable(type = IndexableType.DELETE)
	@Override
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public TaskDefinition deleteTaskDefinition(long taskDefinitionId)
		throws PortalException {

		TaskDefinition taskDefinition =
			taskDefinitionPersistence.findByPrimaryKey(taskDefinitionId);

		return deleteTaskDefinition(taskDefinition);
	}

	@Indexable(type = IndexableType.DELETE)
	@Override
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public TaskDefinition deleteTaskDefinition(TaskDefinition taskDefinition)
		throws PortalException {

		taskDefinition = taskDefinitionPersistence.remove(taskDefinition);

		_resourceLocalService.deleteResource(
			taskDefinition, ResourceConstants.SCOPE_INDIVIDUAL);

		return taskDefinition;
	}

	@Override
	public List<TaskDefinition> getTaskDefinitions(
		long companyId, boolean readOnly) {

		return taskDefinitionPersistence.findByC_R(companyId, readOnly);
	}

	@Override
	public int getTaskDefinitionsCount(long companyId, boolean readOnly) {
		return taskDefinitionPersistence.countByC_R(companyId, readOnly);
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public TaskDefinition updateStatus(
			long taskDefinitionId, ServiceContext serviceContext, int status,
			long userId)
		throws PortalException {

		TaskDefinition taskDefinition =
			taskDefinitionPersistence.findByPrimaryKey(taskDefinitionId);

		if (taskDefinition.getStatus() == status) {
			return taskDefinition;
		}

		User user = _userLocalService.getUser(userId);

		taskDefinition.setStatus(status);
		taskDefinition.setStatusByUserId(user.getUserId());
		taskDefinition.setStatusByUserName(user.getFullName());
		taskDefinition.setStatusDate(serviceContext.getModifiedDate(null));

		return taskDefinitionPersistence.update(taskDefinition);
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public TaskDefinition updateTaskDefinition(
			String configurationJSON, Map<Locale, String> descriptionMap,
			String externalReferenceCode, long taskDefinitionId,
			String schemaVersion, ServiceContext serviceContext,
			Map<Locale, String> titleMap)
		throws PortalException {

		_validate(titleMap, serviceContext);

		TaskDefinition taskDefinition =
			taskDefinitionPersistence.findByPrimaryKey(taskDefinitionId);

		taskDefinition.setExternalReferenceCode(externalReferenceCode);
		taskDefinition.setConfigurationJSON(configurationJSON);
		taskDefinition.setDescriptionMap(descriptionMap);
		taskDefinition.setTitleMap(titleMap);
		taskDefinition.setVersion(
			String.format(
				"%.1f",
				GetterUtil.getFloat(taskDefinition.getVersion(), 0.9F) + 0.1));

		return updateTaskDefinition(taskDefinition);
	}

	private void _validate(
			Map<Locale, String> titleMap, ServiceContext serviceContext)
		throws TaskDefinitionTitleException {

		if (!GetterUtil.getBoolean(
				serviceContext.getAttribute(
					TaskDefinitionLocalServiceImpl.class.getName() +
						"#_validate"),
				true)) {

			return;
		}

		if (MapUtil.isEmpty(titleMap)) {
			throw new TaskDefinitionTitleException("Title is empty");
		}
	}

	@Reference
	private ResourceLocalService _resourceLocalService;

	@Reference
	private UserLocalService _userLocalService;

}