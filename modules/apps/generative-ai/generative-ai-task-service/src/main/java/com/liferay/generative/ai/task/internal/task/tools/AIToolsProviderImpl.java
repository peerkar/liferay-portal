/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.task.tools;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.Validator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(service = AIToolsProvider.class)
public class AIToolsProviderImpl implements AIToolsProvider {

	@Override
	public Object getTool(JSONObject configurationJSONObject, String key) {
		if (Validator.isBlank(key)) {
			return null;
		}

		if (key.equals("blogs")) {
			return new BlogsAITools(configurationJSONObject);
		}
		else if (key.equals("objects")) {
			return new ObjectsAITools(configurationJSONObject);
		}
		else if (key.equals("picklists")) {
			return new PickListsAITools(configurationJSONObject);
		}
		else if (key.equals("site")) {
			return new SiteAITools(configurationJSONObject);
		}
		else if (key.equals("task_definition")) {
			return new ObjectsAITools(configurationJSONObject);
		}
		else if (key.equals("user")) {
			return new UserAITools(configurationJSONObject);
		}
		else if (key.equals("webhook")) {
			return new WebHookAITools(configurationJSONObject, _http);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Tools not found: " + key);
		}

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AIToolsProviderImpl.class);

	@Reference
	private Http _http;

}