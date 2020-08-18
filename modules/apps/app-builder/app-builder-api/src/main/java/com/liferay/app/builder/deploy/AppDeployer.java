/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.app.builder.deploy;

import com.liferay.app.builder.model.AppBuilderApp;
import com.liferay.app.builder.service.AppBuilderAppLocalService;

import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Jeyvison Nascimento
 */
@ProviderType
public interface AppDeployer {

	public void deploy(long appId) throws Exception;

	public default boolean undeploy(
			AppBuilderAppLocalService appBuilderAppLocalService, long appId,
			Map<Long, ServiceRegistration<?>[]> serviceRegistrationsMap)
		throws Exception {

		ServiceRegistration<?>[] serviceRegistrations =
			serviceRegistrationsMap.remove(appId);

		if (serviceRegistrations == null) {
			return false;
		}

		for (ServiceRegistration<?> serviceRegistration :
				serviceRegistrations) {

			serviceRegistration.unregister();
		}

		AppBuilderApp appBuilderApp =
			appBuilderAppLocalService.getAppBuilderApp(appId);

		appBuilderApp.setActive(false);

		appBuilderAppLocalService.updateAppBuilderApp(appBuilderApp);

		return true;
	}

	public void undeploy(long appId) throws Exception;

}