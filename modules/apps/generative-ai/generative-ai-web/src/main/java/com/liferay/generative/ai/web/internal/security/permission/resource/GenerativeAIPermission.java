/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.web.internal.security.permission.resource;

import com.liferay.generative.ai.task.constants.TaskDefinitionConstants;
import com.liferay.portal.kernel.module.service.Snapshot;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;

/**
 * @author Petteri Karttunen
 */
public class GenerativeAIPermission {

	public static boolean contains(
		PermissionChecker permissionChecker, long groupId, String actionKey) {

		PortletResourcePermission portletResourcePermission =
			_portletResourcePermissionSnapshot.get();

		return portletResourcePermission.contains(
			permissionChecker, groupId, actionKey);
	}

	private static final Snapshot<PortletResourcePermission>
		_portletResourcePermissionSnapshot = new Snapshot<>(
			GenerativeAIPermission.class, PortletResourcePermission.class,
			"(resource.name=" + TaskDefinitionConstants.RESOURCE_NAME + ")");

}