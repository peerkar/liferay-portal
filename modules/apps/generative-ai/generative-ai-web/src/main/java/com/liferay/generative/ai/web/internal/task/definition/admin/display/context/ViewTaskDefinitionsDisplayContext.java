/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.web.internal.task.definition.admin.display.context;

import com.liferay.frontend.data.set.model.FDSActionDropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.generative.ai.task.constants.TaskDefinitionActionKeys;
import com.liferay.generative.ai.task.model.TaskDefinition;
import com.liferay.generative.ai.web.internal.display.context.helper.GenerativeAIRequestHelper;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.util.Constants;

import java.util.Arrays;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Petteri Karttunen
 */
public class ViewTaskDefinitionsDisplayContext {

	public ViewTaskDefinitionsDisplayContext(
		HttpServletRequest httpServletRequest,
		ModelResourcePermission<TaskDefinition>
			TaskDefinitionModelResourcePermission) {

		_taskDefinitionModelResourcePermission =
			TaskDefinitionModelResourcePermission;

		_generativeAIRequestHelper = new GenerativeAIRequestHelper(
			httpServletRequest);
	}

	public String getAPIURL() {
		return "/o/generative-ai/v1.0/task-definitions";
	}

	public List<DropdownItem> getBulkActionDropdownItems() throws Exception {
		return Arrays.asList(
			new FDSActionDropdownItem(
				PortletURLBuilder.createActionURL(
					_generativeAIRequestHelper.getLiferayPortletResponse()
				).setActionName(
					"/task_definitions_admin/edit_task_definition"
				).setCMD(
					Constants.DELETE
				).buildString(),
				"trash", "delete",
				LanguageUtil.get(
					_generativeAIRequestHelper.getRequest(), "delete"),
				"delete", "delete", null));
	}

	public CreationMenu getCreationMenu() throws Exception {
		CreationMenu creationMenu = new CreationMenu();

		if (!_hasAddTaskDefinitionPermission()) {
			return creationMenu;
		}

		creationMenu.addDropdownItem(
			dropdownItem -> {
				dropdownItem.setHref("addTaskDefinition");
				dropdownItem.setLabel(
					LanguageUtil.get(
						_generativeAIRequestHelper.getRequest(),
						"add-task-definition"));
				dropdownItem.setTarget("event");
			});

		return creationMenu;
	}

	public List<FDSActionDropdownItem> getFDSActionDropdownItems()
		throws Exception {

		return Arrays.asList(
			new FDSActionDropdownItem(
				PortletURLBuilder.create(
					getPortletURL()
				).setMVCRenderCommandName(
					"/task_definitions_admin/edit_task_definition"
				).setParameter(
					"taskDefinitionId", "{id}"
				).buildString(),
				"pencil", "edit",
				LanguageUtil.get(
					_generativeAIRequestHelper.getRequest(), "edit"),
				"get", "get", null),
			new FDSActionDropdownItem(
				getAPIURL() + "/{id}/copy", "copy", "copy",
				LanguageUtil.get(
					_generativeAIRequestHelper.getRequest(), "copy"),
				"post", "create", "async"),
			new FDSActionDropdownItem(
				"#", "export", "export",
				LanguageUtil.get(
					_generativeAIRequestHelper.getRequest(), "export"),
				null, "get", null),
			new FDSActionDropdownItem(
				LanguageUtil.get(
					_generativeAIRequestHelper.getRequest(),
					"are-you-sure-you-want-to-delete-this-entry"),
				getAPIURL() + "/{id}", "trash", "delete",
				LanguageUtil.get(
					_generativeAIRequestHelper.getRequest(), "delete"),
				"delete", "delete", "async"));
	}

	public PortletURL getPortletURL() throws PortletException {
		return PortletURLUtil.clone(
			PortletURLUtil.getCurrent(
				_generativeAIRequestHelper.getLiferayPortletRequest(),
				_generativeAIRequestHelper.getLiferayPortletResponse()),
			_generativeAIRequestHelper.getLiferayPortletResponse());
	}

	private boolean _hasAddTaskDefinitionPermission() {
		PortletResourcePermission portletResourcePermission =
			_taskDefinitionModelResourcePermission.
				getPortletResourcePermission();

		return portletResourcePermission.contains(
			_generativeAIRequestHelper.getPermissionChecker(), null,
			TaskDefinitionActionKeys.ADD_TASK_DEFINITION);
	}

	private final GenerativeAIRequestHelper _generativeAIRequestHelper;
	private final ModelResourcePermission<TaskDefinition>
		_taskDefinitionModelResourcePermission;

}