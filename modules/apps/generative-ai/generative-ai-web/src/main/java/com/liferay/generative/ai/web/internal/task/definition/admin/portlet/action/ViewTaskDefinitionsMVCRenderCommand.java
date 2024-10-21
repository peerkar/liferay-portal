/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.web.internal.task.definition.admin.portlet.action;

import com.liferay.generative.ai.task.constants.TaskDefinitionPortletKeys;
import com.liferay.generative.ai.task.model.TaskDefinition;
import com.liferay.generative.ai.web.internal.constants.GenerativeAIWebKeys;
import com.liferay.generative.ai.web.internal.task.definition.admin.display.context.ViewTaskDefinitionsDisplayContext;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.util.Portal;

import javax.portlet.PortletException;
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
		"mvc.command.name=/",
		"mvc.command.name=/task_definitions_admin/view_task_definitions"
	},
	service = MVCRenderCommand.class
)
public class ViewTaskDefinitionsMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		renderRequest.setAttribute(
			GenerativeAIWebKeys.VIEW_TASK_DEFINITIONS_DISPLAY_CONTEXT,
			new ViewTaskDefinitionsDisplayContext(
				_portal.getHttpServletRequest(renderRequest),
				_taskDefinitionModelResourcePermission));

		return "/task_definitions_admin/view.jsp";
	}

	@Reference
	private Portal _portal;

	@Reference(
		target = "(model.class.name=com.liferay.generative.ai.task.model.TaskDefinition)"
	)
	private ModelResourcePermission<TaskDefinition>
		_taskDefinitionModelResourcePermission;

}