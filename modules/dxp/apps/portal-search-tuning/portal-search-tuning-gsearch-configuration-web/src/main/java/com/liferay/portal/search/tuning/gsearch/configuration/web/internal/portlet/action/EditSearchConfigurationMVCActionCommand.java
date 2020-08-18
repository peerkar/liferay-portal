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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.SearchConfigurationPortletKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.SearchConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.exception.SearchConfigurationValidationException;
import com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration;
import com.liferay.portal.search.tuning.gsearch.configuration.service.SearchConfigurationService;
import com.liferay.portal.search.tuning.gsearch.configuration.web.internal.constants.SearchConfigurationMVCCommandNames;
import com.liferay.portal.search.tuning.gsearch.configuration.web.internal.constants.SearchConfigurationWebKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.web.internal.handler.SearchConfigurationExceptionRequestHandler;

import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

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
	service = MVCActionCommand.class
)
public class EditSearchConfigurationMVCActionCommand
	extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long searchConfigurationId = ParamUtil.getLong(
			actionRequest, SearchConfigurationWebKeys.SEARCH_CONFIGURATION_ID);

		int type = ParamUtil.getInteger(
			actionRequest,
			SearchConfigurationWebKeys.SEARCH_CONFIGURATION_TYPE);

		Map<Locale, String> titleMap = LocalizationUtil.getLocalizationMap(
			actionRequest, SearchConfigurationWebKeys.TITLE);

		Map<Locale, String> descriptionMap =
			LocalizationUtil.getLocalizationMap(
				actionRequest, SearchConfigurationWebKeys.DESCRIPTION);

		String configuration = _buildConfigurationFromRequest(actionRequest);

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			com.liferay.portal.kernel.service.ServiceContext serviceContext =
				ServiceContextFactory.getInstance(
					SearchConfiguration.class.getName(), actionRequest);

			if (Constants.ADD.equals(cmd)) {
				_searchConfigurationService.addCompanySearchConfiguration(
					titleMap, descriptionMap, configuration, type,
					serviceContext);
			}
			else if (searchConfigurationId > 0) {
				_searchConfigurationService.updateSearchConfiguration(
					searchConfigurationId, titleMap, descriptionMap,
					configuration, serviceContext);
			}

			JSONObject jsonObject = JSONUtil.put("success", titleMap);

			JSONPortletResponseUtil.writeJSON(
				actionRequest, actionResponse, jsonObject);
		}
		catch (PortalException portalException) {
			hideDefaultErrorMessage(actionRequest);

			_searchConfigurationExceptionRequestHandler.handlePortalException(
				actionRequest, actionResponse, portalException);
		}
	}

	private String _buildConfigurationFromRequest(ActionRequest actionRequest)
		throws JSONException {

		String clauseConfigurationString = ParamUtil.getString(
			actionRequest, "clauseConfiguration");

		JSONArray clauseConfigurationStringJSONArray =
			JSONFactoryUtil.createJSONArray(clauseConfigurationString);

		JSONObject configuration = JSONUtil.put(
			SearchConfigurationKeys.CLAUSE_CONFIGURATION.getJsonKey(),
			clauseConfigurationStringJSONArray);

		return configuration.toString();
	}

	@Reference
	private SearchConfigurationExceptionRequestHandler
		_searchConfigurationExceptionRequestHandler;

	@Reference
	private SearchConfigurationService _searchConfigurationService;

}