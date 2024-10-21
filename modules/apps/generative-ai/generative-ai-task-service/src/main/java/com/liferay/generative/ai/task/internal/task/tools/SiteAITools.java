/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.task.tools;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.util.LocaleUtil;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

// Demo class for site operations

/**
 * @author Petteri Karttunen
 */
public class SiteAITools implements AITools {

	public SiteAITools(JSONObject configurationJSONObject) {
		_configurationJSONObject = configurationJSONObject;
	}

	@Override
	public JSONObject getConfigurationJSONObject() {
		return _configurationJSONObject;
	}

	private final JSONObject _configurationJSONObject;

	@Tool("Creates a Liferay site for the given name")
	Group createSite(
		@P("The name of the site which should be created") String name) {

		Map<Locale, String> nameMap = new HashMap<>();

		nameMap.put(LocaleUtil.getDefault(), name);

		Map<Locale, String> descriptionMap = new HashMap<>();

		descriptionMap.put(LocaleUtil.getDefault(), "Demo fun");

		try {
			return GroupLocalServiceUtil.addGroup(
				PrincipalThreadLocal.getUserId(),
				GroupConstants.DEFAULT_PARENT_GROUP_ID,
				Group.class.getName(),
				0, // Class PK
				GroupConstants.DEFAULT_LIVE_GROUP_ID,
				nameMap,
				descriptionMap,
				GroupConstants.TYPE_SITE_OPEN,
				true, // Manual membership
				GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION, // Membership restriction
				null, // Friendly URL (optional)
				true, // Site flag
				true, // Active flag
				ServiceContextThreadLocal.getServiceContext());
		}
		catch (PortalException portalException) {
			throw new RuntimeException(portalException);
		}
	}

}