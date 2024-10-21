/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.web.internal.task.definition.admin.portlet.action;

import com.liferay.generative.ai.task.constants.TaskDefinitionPortletKeys;
import com.liferay.generative.ai.web.internal.display.context.EditTaskDefinitionDisplayContext;
import com.liferay.item.selector.ItemSelector;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = {
		"javax.portlet.name=" + TaskDefinitionPortletKeys.TASK_DEFINITIONS_ADMIN,
		"mvc.command.name=/task_definitions_admin/edit_task_definition"
	},
	service = MVCRenderCommand.class
)
public class EditTaskDefinitionMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		renderRequest.setAttribute(
			EditTaskDefinitionDisplayContext.class.getName(),
			new EditTaskDefinitionDisplayContext(
				_itemSelector, renderRequest, renderResponse));

		return "/task_definitions_admin/edit_task_definition.jsp";
	}

	@Reference
	private ItemSelector _itemSelector;

}