/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.web.internal.task.definition.admin.portlet.action;

import com.liferay.generative.ai.task.constants.TaskDefinitionPortletKeys;
import com.liferay.generative.ai.task.exception.TaskDefinitionReadOnlyException;
import com.liferay.generative.ai.task.service.TaskDefinitionService;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

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
	service = MVCActionCommand.class
)
public class EditTaskDefinitionMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.DELETE)) {
				_deleteTaskDefinitions(actionRequest);
			}
		}
		catch (Exception exception) {
			if (exception instanceof TaskDefinitionReadOnlyException) {
				hideDefaultErrorMessage(actionRequest);
			}

			SessionErrors.add(actionRequest, exception.getClass(), exception);

			String redirect = ParamUtil.getString(actionRequest, "redirect");

			sendRedirect(actionRequest, actionResponse, redirect);
		}
	}

	private void _deleteTaskDefinitions(ActionRequest actionRequest)
		throws Exception {

		long[] deleteTaskDefinitionIds = StringUtil.split(
			ParamUtil.getString(actionRequest, "id"), 0L);

		for (long deleteTaskDefinitionId : deleteTaskDefinitionIds) {
			_taskDefinitionService.deleteTaskDefinition(deleteTaskDefinitionId);
		}
	}

	@Reference
	private TaskDefinitionService _taskDefinitionService;

}