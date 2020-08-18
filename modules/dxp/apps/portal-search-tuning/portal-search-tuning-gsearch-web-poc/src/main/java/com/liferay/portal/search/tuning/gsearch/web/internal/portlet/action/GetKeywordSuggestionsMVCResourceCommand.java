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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.search.tuning.gsearch.configuration.service.SearchConfigurationService;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.suggester.KeywordSuggester;
import com.liferay.portal.search.tuning.gsearch.util.SearchClientHelper;
import com.liferay.portal.search.tuning.gsearch.web.internal.constants.GSearchWebPortletKeys;
import com.liferay.portal.search.tuning.gsearch.web.internal.constants.ResourceRequestKeys;
import com.liferay.portal.search.tuning.gsearch.web.internal.constants.JSONResponseKeys;
import com.liferay.portal.search.tuning.gsearch.web.internal.portlet.preferences.GSearchWebPortletPreferences;
import com.liferay.portal.search.tuning.gsearch.web.internal.portlet.preferences.GSearchWebPortletPreferencesImpl;
import com.liferay.portal.search.tuning.gsearch.web.internal.util.GSearchLocalizationHelper;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, 
	property = {
		"javax.portlet.name=" + GSearchWebPortletKeys.GSEARCH_WEB,
		"mvc.command.name=" + ResourceRequestKeys.GET_SUGGESTIONS
	}, 
	service = MVCResourceCommand.class
)
public class GetKeywordSuggestionsMVCResourceCommand extends BaseMVCResourceCommand {

	@Override
	protected void doServeResource(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		JSONObject responseJsonObject= JSONFactoryUtil.createJSONObject();

		GSearchWebPortletPreferences gSearchWebPortletPreferences =
				new GSearchWebPortletPreferencesImpl(resourceRequest.getPreferences());
		
		long searchConfigurationId = gSearchWebPortletPreferences.getSearchConfigurationId();

		try {
			HttpServletRequest httpServletRequest =
				_portal.getHttpServletRequest(resourceRequest);

			SearchRequestContext searchRequestContext = 
					_searchClientHelper.getSearchRequestContext(httpServletRequest, searchConfigurationId);
			
			JSONArray suggestionsJsonArray = 
					_keywordSuggester.getSuggestions(searchRequestContext);
			
			responseJsonObject.put(JSONResponseKeys.KEYWORD_SUGGESTIONS, suggestionsJsonArray);
						
		} catch (PortalException portalException) {
			
			_log.error(portalException.getMessage(), portalException);

			responseJsonObject.put(JSONResponseKeys.ERROR, portalException.getMessage());

		}

		JSONPortletResponseUtil.writeJSON(
			resourceRequest, resourceResponse, responseJsonObject);
	}

	private static final Log _log =
		LogFactoryUtil.getLog(GetKeywordSuggestionsMVCResourceCommand.class);

	@Reference
	private KeywordSuggester _keywordSuggester;

	@Reference
	private GSearchLocalizationHelper _gSearchLocalizationHelper;
	
	@Reference
	private Portal _portal;

	@Reference 
	private SearchClientHelper _searchClientHelper;
	
	@Reference
	private SearchConfigurationService _searchConfigurationService;
}
