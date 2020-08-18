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

package com.liferay.portal.search.tuning.gsearch.configuration.web.internal.handler;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.exception.SearchConfigurationValidationException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	immediate = true, service = SearchConfigurationExceptionRequestHandler.class
)
public class SearchConfigurationExceptionRequestHandler {

	public void handlePortalException(
			ActionRequest actionRequest, ActionResponse actionResponse,
			PortalException portalException)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		JSONArray jsonArray = _jsonFactory.createJSONArray();

		if (portalException instanceof SearchConfigurationValidationException) {
			SearchConfigurationValidationException
				searchConfigurationValidationException =
					(SearchConfigurationValidationException)portalException;

			_log.error(
				searchConfigurationValidationException.getMessage(),
				searchConfigurationValidationException);

			searchConfigurationValidationException.getErrors(
			).forEach(
				key -> {
					String errorMessage = "an-unexpected-error-occurred";

					if (key.equals("titleEmpty")) {
						errorMessage = "error.title-empty";
					}
					else if (key.equals("defaultLocaleTitleEmpty")) {
						errorMessage = "error.default-locale-title-empty";
					}

					jsonArray.put(
						_language.get(themeDisplay.getRequest(), errorMessage));
				}
			);
		}
		else {
			_log.error(portalException.getMessage(), portalException);

			jsonArray.put(
				_language.get(
					themeDisplay.getRequest(), "an-unexpected-error-occurred"));
		}

		JSONObject jsonObject = JSONUtil.put("errors", jsonArray);

		JSONPortletResponseUtil.writeJSON(
			actionRequest, actionResponse, jsonObject);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SearchConfigurationExceptionRequestHandler.class);

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private Language _language;

}