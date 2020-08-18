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
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.SearchConfigurationPortletKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.exception.NoSuchConfigurationException;
import com.liferay.portal.search.tuning.gsearch.configuration.exception.SearchConfigurationValidationException;
import com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration;
import com.liferay.portal.search.tuning.gsearch.configuration.service.SearchConfigurationService;
import com.liferay.portal.search.tuning.gsearch.configuration.web.internal.constants.SearchConfigurationMVCCommandNames;
import com.liferay.portal.search.tuning.gsearch.configuration.web.internal.constants.SearchConfigurationWebKeys;

import java.io.IOException;

import java.util.HashMap;
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
		"mvc.command.name=" + SearchConfigurationMVCCommandNames.COPY_SEARCH_CONFIGURATION
	},
	service = MVCActionCommand.class
)
public class CopySearchConfigurationMVCActionCommand
	extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long searchConfigurationId = ParamUtil.getLong(
			actionRequest, SearchConfigurationWebKeys.SEARCH_CONFIGURATION_ID);

		try {
			SearchConfiguration sourceSearchConfiguration =
				_searchConfigurationService.getSearchConfiguration(
					searchConfigurationId);

			_createCopy(
				actionRequest, actionResponse, sourceSearchConfiguration);
		}
		catch (NoSuchConfigurationException noSuchConfigurationException) {
			_log.error(
				"Search configuration " + searchConfigurationId + " not found.",
				noSuchConfigurationException);

			SessionErrors.add(
				actionRequest, SearchConfigurationWebKeys.ERROR_DETAILS,
				noSuchConfigurationException);
		}
		catch (PortalException portalException) {
			_log.error(portalException.getMessage(), portalException);

			SessionErrors.add(
				actionRequest, SearchConfigurationWebKeys.ERROR_DETAILS,
				portalException);
		}
	}

	private void _createCopy(
		ActionRequest actionRequest, ActionResponse actionResponse,
		SearchConfiguration sourceSearchConfiguration) {

		try {
			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				SearchConfiguration.class.getName(), actionRequest);

			Map<Locale, String> titleMap = _getTargetTitleMap(
				sourceSearchConfiguration);

			_searchConfigurationService.addCompanySearchConfiguration(
				titleMap, sourceSearchConfiguration.getDescriptionMap(),
				sourceSearchConfiguration.getConfiguration(),
				sourceSearchConfiguration.getType(), serviceContext);

			sendRedirect(actionRequest, actionResponse);
		}
		catch (SearchConfigurationValidationException
					searchConfigurationValidationException) {

			_log.error(
				searchConfigurationValidationException.getMessage(),
				searchConfigurationValidationException);

			searchConfigurationValidationException.getErrors(
			).forEach(
				key -> SessionErrors.add(actionRequest, key)
			);
		}
		catch (PortalException portalException) {
			_log.error(portalException.getMessage(), portalException);

			SessionErrors.add(
				actionRequest, SearchConfigurationWebKeys.ERROR_DETAILS,
				portalException);
		}
		catch (IOException ioException) {
			SessionErrors.add(
				actionRequest, SearchConfigurationWebKeys.ERROR_DETAILS,
				ioException);

			_log.error(ioException.getMessage(), ioException);
		}
	}

	private Map<Locale, String> _getTargetTitleMap(
		SearchConfiguration searchConfiguration) {

		Map<Locale, String> sourceTitleMap = searchConfiguration.getTitleMap();
		Map<Locale, String> targetTitleMap = new HashMap<>();

		for (Map.Entry<Locale, String> entry : sourceTitleMap.entrySet()) {
			targetTitleMap.put(
				entry.getKey(),
				entry.getValue() + " (" +
					_language.get(entry.getKey(), "copy") + ")");
		}

		return targetTitleMap;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CopySearchConfigurationMVCActionCommand.class);

	@Reference
	private Language _language;

	@Reference
	private SearchConfigurationService _searchConfigurationService;

}