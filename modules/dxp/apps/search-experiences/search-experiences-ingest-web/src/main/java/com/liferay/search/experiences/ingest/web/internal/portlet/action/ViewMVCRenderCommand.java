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

package com.liferay.search.experiences.ingest.web.internal.portlet.action;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.util.Http;
import com.liferay.search.experiences.ingest.web.internal.constants.IngestPortletKeys;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	enabled = false, immediate = true,
	property = {
		"javax.portlet.name=" + IngestPortletKeys.INGEST, "mvc.command.name=/"
	},
	service = MVCRenderCommand.class
)
public class ViewMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		renderRequest.setAttribute(
			"liferayHelpCenterNumberOfArticles",
			_getLiferayHelpCenterArticleCount());

		return "/view.jsp";
	}

	private int _getLiferayHelpCenterArticleCount() {
		try {
			JSONObject jsonObject = _jsonFactory.createJSONObject(
				_http.URLtoString(
					"https://liferay-support.zendesk.com/api/v2/help_center" +
						"/en-us/articles.json?page=1&per_page=1"));

			return jsonObject.getInt("count");
		}
		catch (Exception exception) {
			_log.error(exception);
		}

		return 0;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ViewMVCRenderCommand.class);

	@Reference
	private Http _http;

	@Reference
	private JSONFactory _jsonFactory;

}