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

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.SearchConfigurationPortletKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.SearchConfigurationTypes;
import com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration;
import com.liferay.portal.search.tuning.gsearch.configuration.web.internal.constants.SearchConfigurationMVCCommandNames;
import com.liferay.portal.search.tuning.gsearch.configuration.web.internal.constants.SearchConfigurationWebKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.web.internal.display.context.SearchConfigurationEntriesDisplayContext;
import com.liferay.portal.search.tuning.gsearch.configuration.web.internal.display.context.SearchConfigurationEntriesManagementToolbarDisplayContext;

import javax.portlet.PortletException;
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
		"mvc.command.name=" + SearchConfigurationMVCCommandNames.VIEW_SEARCH_CONFIGURATIONS,
		"mvc.command.name=/"
	},
	service = MVCRenderCommand.class
)
public class ViewSearchConfigurationsMVCRenderCommand
	implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		int searchConfigurationType = ParamUtil.getInteger(
			renderRequest, SearchConfigurationWebKeys.SEARCH_CONFIGURATION_TYPE,
			SearchConfigurationTypes.CONFIGURATION);

		SearchConfigurationEntriesDisplayContext
			searchConfigurationEntriesDisplayContext =
				new SearchConfigurationEntriesDisplayContext(
					_portal.getLiferayPortletRequest(renderRequest),
					_portal.getLiferayPortletResponse(renderResponse),
					searchConfigurationType);

		renderRequest.setAttribute(
			SearchConfigurationWebKeys.
				SEARCH_CONFIGURATION_ENTRIES_DISPLAY_CONTEXT,
			searchConfigurationEntriesDisplayContext);

		try {
			SearchConfigurationEntriesManagementToolbarDisplayContext
				searchConfigurationsManagementToolbarDisplayContext =
					_getSearchConfigurationsManagementToolbar(
						renderRequest, renderResponse,
						searchConfigurationEntriesDisplayContext.
							getSearchContainer(),
						searchConfigurationEntriesDisplayContext.
							getDisplayStyle(),
						searchConfigurationType);

			renderRequest.setAttribute(
				SearchConfigurationWebKeys.
					SEARCH_CONFIGURATION_ENTRIES_MANAGEMENT_TOOLBAR_DISPLAY_CONTEXT,
				searchConfigurationsManagementToolbarDisplayContext);
		}
		catch (PortalException portalException) {
			_log.error(portalException.getMessage(), portalException);

			SessionErrors.add(renderRequest, "errorDetails", portalException);
		}

		return "/view.jsp";
	}

	private SearchConfigurationEntriesManagementToolbarDisplayContext
		_getSearchConfigurationsManagementToolbar(
			RenderRequest renderRequest, RenderResponse renderResponse,
			SearchContainer<SearchConfiguration> searchContainer,
			String displayStyle, int searchConfigurationType) {

		return new SearchConfigurationEntriesManagementToolbarDisplayContext(
			_portal.getHttpServletRequest(renderRequest),
			_portal.getLiferayPortletRequest(renderRequest),
			_portal.getLiferayPortletResponse(renderResponse), searchContainer,
			displayStyle, searchConfigurationType);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ViewSearchConfigurationsMVCRenderCommand.class);

	@Reference
	private Portal _portal;

}