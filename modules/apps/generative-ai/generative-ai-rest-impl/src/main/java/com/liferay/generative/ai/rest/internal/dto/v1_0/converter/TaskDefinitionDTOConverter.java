/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.rest.internal.dto.v1_0.converter;

import com.liferay.generative.ai.rest.dto.v1_0.TaskDefinition;
import com.liferay.generative.ai.task.service.TaskDefinitionLocalService;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DTOConverterContext;
import com.liferay.portal.vulcan.util.LocalizedMapUtil;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = "dto.class.name=com.liferay.generative.ai.task.model.TaskDefinition",
	service = DTOConverter.class
)
public class TaskDefinitionDTOConverter
	implements DTOConverter
		<com.liferay.generative.ai.task.model.TaskDefinition, TaskDefinition> {

	@Override
	public String getContentType() {
		return TaskDefinition.class.getSimpleName();
	}

	@Override
	public TaskDefinition toDTO(DTOConverterContext dtoConverterContext)
		throws Exception {

		com.liferay.generative.ai.task.model.TaskDefinition taskDefinition =
			_taskDefinitionLocalService.getTaskDefinition(
				(Long)dtoConverterContext.getId());

		return toDTO(dtoConverterContext, taskDefinition);
	}

	@Override
	public TaskDefinition toDTO(
			DTOConverterContext dtoConverterContext,
			com.liferay.generative.ai.task.model.TaskDefinition taskDefinition)
		throws Exception {

		return new TaskDefinition() {
			{
				setConfiguration(
					_toJSONObject(taskDefinition.getConfigurationJSON()));
				setCreateDate(taskDefinition::getCreateDate);
				setDescription(
					() -> _language.get(
						dtoConverterContext.getLocale(),
						taskDefinition.getDescription(
							dtoConverterContext.getLocale())));
				setDescription_i18n(
					() -> LocalizedMapUtil.getI18nMap(
						dtoConverterContext.isAcceptAllLanguages(),
						taskDefinition.getDescriptionMap()));
				setExternalReferenceCode(
					taskDefinition::getExternalReferenceCode);
				setId(taskDefinition::getTaskDefinitionId);
				setModifiedDate(taskDefinition::getModifiedDate);
				setReadOnly(taskDefinition::getReadOnly);
				setSchemaVersion(taskDefinition::getSchemaVersion);
				setTitle(
					() -> _language.get(
						dtoConverterContext.getLocale(),
						taskDefinition.getTitle(
							dtoConverterContext.getLocale())));
				setTitle_i18n(
					() -> LocalizedMapUtil.getI18nMap(
						dtoConverterContext.isAcceptAllLanguages(),
						taskDefinition.getTitleMap()));
				setUserName(taskDefinition::getUserName);
				setVersion(taskDefinition::getVersion);
			}
		};
	}

	@Override
	public TaskDefinition toDTO(
		com.liferay.generative.ai.task.model.TaskDefinition taskDefinition) {

		return new TaskDefinition() {
			{
				setConfiguration(
					_toJSONObject(taskDefinition.getConfigurationJSON()));
				setCreateDate(taskDefinition::getCreateDate);
				setDescription(taskDefinition::getDescription);
				setDescription_i18n(
					() -> LocalizedMapUtil.getI18nMap(
						true, taskDefinition.getDescriptionMap()));
				setExternalReferenceCode(
					taskDefinition::getExternalReferenceCode);
				setId(taskDefinition::getTaskDefinitionId);
				setModifiedDate(taskDefinition::getModifiedDate);
				setReadOnly(taskDefinition::getReadOnly);
				setSchemaVersion(taskDefinition::getSchemaVersion);
				setTitle(taskDefinition::getTitle);
				setTitle_i18n(
					() -> LocalizedMapUtil.getI18nMap(
						true, taskDefinition.getTitleMap()));
				setUserName(taskDefinition::getUserName);
				setVersion(taskDefinition::getVersion);
			}
		};
	}

	private JSONObject _toJSONObject(String json) {
		try {
			return _jsonFactory.createJSONObject(json);
		}
		catch (Exception exception) {
			_log.error(exception);

			return null;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		TaskDefinitionDTOConverter.class);

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private Language _language;

	@Reference
	private TaskDefinitionLocalService _taskDefinitionLocalService;

}