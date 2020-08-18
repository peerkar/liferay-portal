/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.portal.search.tuning.gsearch.configuration.web.internal.security.permission.resource;

import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.SearchConfigurationActionKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.SearchConfigurationConstants;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(immediate = true, service = {})
public class SearchConfigurationPermission {

	public static boolean contains(
		PermissionChecker permissionChecker, long groupId, int type,
		String actionId) {

		String typedActionKey =
			SearchConfigurationActionKeys.
				getActionKeyForSearchConfigurationType(type, actionId);

		return _portletResourcePermission.contains(
			permissionChecker, groupId, typedActionKey);
	}

	@Reference(
		target = "(resource.name=" + SearchConfigurationConstants.RESOURCE_NAME + ")",
		unbind = "-"
	)
	protected void setPortletResourcePermission(
		PortletResourcePermission portletResourcePermission) {

		_portletResourcePermission = portletResourcePermission;
	}

	private static PortletResourcePermission _portletResourcePermission;

}