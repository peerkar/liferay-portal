/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.task.tools;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserConstants;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.service.UserServiceUtil;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.StringUtil;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

// Demo tool for user operations

/**
 * @author Petteri Karttunen
 */
public class UserAITools implements AITools {

	public UserAITools(JSONObject configurationJSONObject) {
		_configurationJSONObject = configurationJSONObject;
	}

	@Tool("Creates a new user")
	void createUser(
		@P("The first name of the user") String firstName,
		@P("The last name of the user") String lastName) {

		try {
			String emailAddress =
				firstName + StringUtil.randomString() + "@example.com";

			String password = "test";

			User user = UserLocalServiceUtil.addUser(
				PrincipalThreadLocal.getUserId(),
				CompanyThreadLocal.getCompanyId(),
				false, // autoPassword
				password,
				password,
				true, // auto screenName
				null, // Screen name
				emailAddress,
				LocaleThreadLocal.getDefaultLocale(),
				firstName, // First name
				"",
				lastName, // Last name
				-1, // prefixListTypeId
				-1, // suffixListTypeId
				true,
				1, // int birthdayMonth
				1, // int birthdayDay
				1985, // int birthdayYear
				"Tester",
				UserConstants.TYPE_REGULAR,
				new long[0], // long[] groupIds
				new long[0], // long[] organizationIds
				new long[0], // long[] roleIds
				new long[0], // long[] userGroupIds
				false, //
				ServiceContextThreadLocal.getServiceContext()
			);
		}
		catch (PortalException portalException) {
			throw new RuntimeException(portalException);
		}
	}

	@Tool("Updates my first name")
	void updateUserFirstName(
		@P("The new first name of the user") String firstName) {

		try {
			User user = UserServiceUtil.getCurrentUser();

			user.setFirstName(firstName);

			UserLocalServiceUtil.updateUser(user);
		}
		catch (PortalException portalException) {
			throw new RuntimeException(portalException);
		}
	}

	@Tool("Updates my last name")
	void updateUserLastName(
		@P("The new last name of the user") String lastName) {

		try {
			User user = UserServiceUtil.getCurrentUser();

			user.setLastName(lastName);

			UserLocalServiceUtil.updateUser(user);
		}
		catch (PortalException portalException) {
			throw new RuntimeException(portalException);
		}
	}

	@Override
	public JSONObject getConfigurationJSONObject() {
		return _configurationJSONObject;
	}

	private final JSONObject _configurationJSONObject;

}