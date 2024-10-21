/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.task.tools;

import com.liferay.blogs.model.BlogsEntry;
import com.liferay.blogs.service.BlogsEntryLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import java.util.Date;

// Demo class for blogs operations

/**
 * @author Petteri Karttunen
 */
public class BlogsAITools implements AITools {

	public BlogsAITools(JSONObject configurationJSONObject) {
		_configurationJSONObject = configurationJSONObject;
	}

	@Override
	public JSONObject getConfigurationJSONObject() {
		return _configurationJSONObject;
	}

	private static final Log _log = LogFactoryUtil.getLog(BlogsAITools.class);

	@Tool("Creates a new blogs entry")
	BlogsEntry createBlogsEntry(
		@P("The content of the blogs entry to be created") String content,
		@P("The title of the blogs entry to be created") String title) {

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		serviceContext.setScopeGroupId(20116L);

		try {
			return BlogsEntryLocalServiceUtil.addEntry(
				PrincipalThreadLocal.getUserId(), title, content, new Date(),
				serviceContext);
		}
		catch (PortalException portalException) {
			_log.error("Failed to create blogs entry", portalException);
		}

		return null;
	}

	private final JSONObject _configurationJSONObject;

}