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

package com.liferay.portal.search.tuning.gsearch.configuration.internal.security.permission;

import com.liferay.portal.kernel.security.permission.PermissionUpdateHandler;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration;
import com.liferay.portal.search.tuning.gsearch.configuration.service.SearchConfigurationLocalService;

import java.util.Date;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = "model.class.name=com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration",
	service = PermissionUpdateHandler.class
)
public class SearchConfigurationPermissionUpdateHandler
	implements PermissionUpdateHandler {

	@Override
	public void updatedPermission(String primKey) {
		SearchConfiguration searchConfiguration =
			_searchConfigurationLocalService.fetchSearchConfiguration(
				GetterUtil.getLong(primKey));

		if (searchConfiguration == null) {
			return;
		}

		searchConfiguration.setModifiedDate(new Date());

		_searchConfigurationLocalService.updateSearchConfiguration(
			searchConfiguration);
	}

	@Reference
	private SearchConfigurationLocalService _searchConfigurationLocalService;

}