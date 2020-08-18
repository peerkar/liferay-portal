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

package com.liferay.portal.search.tuning.gsearch.web.internal.portlet.action;

import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.PortletRequestModel;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.search.tuning.gsearch.web.internal.constants.GSearchWebPortletKeys;
import com.liferay.portal.search.tuning.gsearch.web.internal.constants.JSONResponseKeys;
import com.liferay.portal.search.tuning.gsearch.web.internal.constants.ResourceRequestKeys;
import com.liferay.portal.search.tuning.gsearch.web.internal.portlet.preferences.GSearchWebPortletPreferences;
import com.liferay.portal.search.tuning.gsearch.web.internal.portlet.preferences.GSearchWebPortletPreferencesImpl;
import com.liferay.portal.search.tuning.gsearch.web.internal.util.GSearchLocalizationHelper;

import java.util.Locale;
import java.util.Optional;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, 
	property = {
		"javax.portlet.name=" + GSearchWebPortletKeys.GSEARCH_WEB,
		"mvc.command.name=" + ResourceRequestKeys.GET_HELP_TEXT
	}, 
	service = MVCResourceCommand.class
)
public class GetHelpTextMVCResourceCommand extends BaseMVCResourceCommand {

	@Override
	protected void doServeResource(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		JSONObject responseJsonObject = JSONFactoryUtil.createJSONObject();
		
		ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		String helpText = null;

		try {
			
			helpText = _getHelpText(resourceRequest, resourceResponse);
					
		} catch (PortalException portalException) {
			_log.error(portalException.getMessage(), portalException);
			responseJsonObject.put(JSONResponseKeys.ERROR,
					portalException.getMessage());
		}
		
		if (Validator.isBlank(helpText)) {
			helpText = _gSearchLocalizationHelper.get(themeDisplay.getLocale(), "default-helptext");
		}
		
		responseJsonObject.put(
				JSONResponseKeys.HELP_TEXT, helpText);

		JSONPortletResponseUtil.writeJSON(
			resourceRequest, resourceResponse, responseJsonObject);
	}

	private String _getHelpText(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse)
	 	throws PortalException {

		ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		GSearchWebPortletPreferences gSearchWebPortletPreferences =
				new GSearchWebPortletPreferencesImpl(resourceRequest.getPreferences());

		Optional<String> valueOptional = gSearchWebPortletPreferences.getHelpTextArticleOptional();
		
		if (!valueOptional.isPresent()) {
			return null;
		}
		
		String value = valueOptional.get();
		
		String[]arr = value.split(":");
		
		if (arr.length != 2) {
			return null;
		}
		
		long groupId = Long.valueOf(arr[0]);
		String articleId = arr[1];
		
		JournalArticle journalArticle =
			_journalArticleService.getLatestArticle(
				groupId, articleId, WorkflowConstants.STATUS_APPROVED);

		return _journalArticleService.getArticleContent(
			groupId, articleId, journalArticle.getVersion(),
			themeDisplay.getLanguageId(),
			new PortletRequestModel(resourceRequest, resourceResponse),
			themeDisplay);
	}

	private static final Log _log =
					LogFactoryUtil.getLog(GetHelpTextMVCResourceCommand.class);

	@Reference
	private GSearchLocalizationHelper _gSearchLocalizationHelper;

	@Reference
	private JournalArticleService _journalArticleService;
}
