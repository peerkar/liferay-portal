/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.web.internal.task.definition.admin.application.list;

import com.liferay.application.list.BasePanelApp;
import com.liferay.application.list.PanelApp;
import com.liferay.generative.ai.constants.GenerativeAIPanelCategoryKeys;
import com.liferay.generative.ai.task.constants.TaskDefinitionPortletKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.security.permission.PermissionChecker;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = {
		"panel.app.order:Integer=100",
		"panel.category.key=" + GenerativeAIPanelCategoryKeys.CONTROL_PANEL_GENERATIVE_AI
	},
	service = PanelApp.class
)
public class TaskDefinitionAdminPanelApp extends BasePanelApp {

	@Override
	public Portlet getPortlet() {
		return _portlet;
	}

	@Override
	public String getPortletId() {
		return TaskDefinitionPortletKeys.TASK_DEFINITIONS_ADMIN;
	}

	@Override
	public boolean isShow(PermissionChecker permissionChecker, Group group)
		throws PortalException {

		return super.isShow(permissionChecker, group);
	}

	@Reference(
		target = "(javax.portlet.name=" + TaskDefinitionPortletKeys.TASK_DEFINITIONS_ADMIN + ")"
	)
	private Portlet _portlet;

}