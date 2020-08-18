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

package com.liferay.portal.search.tuning.gsearch.configuration.web.internal.portlet.action;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.SearchConfigurationPortletKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.service.SearchConfigurationService;
import com.liferay.portal.search.tuning.gsearch.configuration.web.internal.constants.SearchConfigurationMVCCommandNames;
import com.liferay.portal.search.tuning.gsearch.configuration.web.internal.constants.SearchConfigurationWebKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.web.internal.display.context.EditSearchConfigurationDisplayBuilder;
import com.liferay.portal.search.tuning.gsearch.configuration.web.internal.display.context.EditSearchConfigurationDisplayContext;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + SearchConfigurationPortletKeys.SEARCH_CONFIGURATION_ADMIN,
		"mvc.command.name=" + SearchConfigurationMVCCommandNames.EDIT_SEARCH_CONFIGURATION
	},
	service = MVCRenderCommand.class
)
public class EditSearchConfigurationMVCRenderCommand
	implements MVCRenderCommand {

	@Override
	public String render(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		EditSearchConfigurationDisplayContext
			editSearchConfigurationDisplayContext =
				new EditSearchConfigurationDisplayBuilder(
					portal.getHttpServletRequest(renderRequest), language, _log,
					jsonFactory, renderRequest, renderResponse,
					_searchConfigurationService
				).build();

		renderRequest.setAttribute(
			SearchConfigurationWebKeys.
				EDIT_SEARCH_CONFIGURATION_DISPLAY_CONTEXT,
			editSearchConfigurationDisplayContext);

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		portletDisplay.setShowBackIcon(true);
		portletDisplay.setURLBack(
			editSearchConfigurationDisplayContext.getRedirect());

		return "/edit_search_configuration.jsp";
	}

	@Reference
	protected JSONFactory jsonFactory;

	@Reference
	protected Language language;

	@Reference
	protected Portal portal;

	private static final Log _log = LogFactoryUtil.getLog(
		EditSearchConfigurationMVCRenderCommand.class);

	@Reference
	private SearchConfigurationService _searchConfigurationService;

}