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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.SearchConfigurationActionKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(immediate = true, service = SearchConfigurationEntryPermission.class)
public class SearchConfigurationEntryPermission {

	public static boolean contains(
			PermissionChecker permissionChecker, long entryId,
			int searchConfigurationType, String actionId)
		throws PortalException {

		String actionKey =
			SearchConfigurationActionKeys.
				getActionKeyForSearchConfigurationType(
					searchConfigurationType, actionId);

		return _searchConfigurationEntryModelResourcePermission.contains(
			permissionChecker, entryId, actionKey);
	}

	public static boolean contains(
			PermissionChecker permissionChecker, SearchConfiguration entry,
			String actionId)
		throws PortalException {

		String actionKey =
			SearchConfigurationActionKeys.
				getActionKeyForSearchConfigurationType(
					entry.getType(), actionId);

		return _searchConfigurationEntryModelResourcePermission.contains(
			permissionChecker, entry, actionKey);
	}

	@Reference(
		target = "(model.class.name=com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration)",
		unbind = "-"
	)
	protected void setEntryModelPermission(
		ModelResourcePermission<SearchConfiguration> modelResourcePermission) {

		_searchConfigurationEntryModelResourcePermission =
			modelResourcePermission;
	}

	private static ModelResourcePermission<SearchConfiguration>
		_searchConfigurationEntryModelResourcePermission;

}