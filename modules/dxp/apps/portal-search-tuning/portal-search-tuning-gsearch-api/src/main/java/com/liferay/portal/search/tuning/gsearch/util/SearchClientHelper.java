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

package com.liferay.portal.search.tuning.gsearch.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.search.engine.adapter.search.SearchSearchResponse;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.exception.SearchRequestDataException;
import com.liferay.portal.search.tuning.gsearch.results.ResultAttributes;
import com.liferay.portal.search.tuning.gsearch.searchrequest.SearchRequestData;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Petteri Karttunen
 */
public interface SearchClientHelper {

	public SearchRequestContext getSearchRequestContext(
			HttpServletRequest httpServletRequest, long searchConfigurationId)
		throws JSONException, PortalException;

	public SearchRequestContext getSearchRequestContext(
			SearchContext searchContext, long searchConfigurationId)
		throws JSONException, PortalException;

	public SearchRequestData getSearchRequestData(
			SearchRequestContext searchRequestContext)
		throws SearchRequestDataException;

	public SearchSearchResponse getSearchResponse(
		SearchRequestContext searchRequestContext,
		SearchRequestData searchRequestData);

	public JSONObject getSearchResults(
		SearchRequestContext searchRequestContext,
		SearchSearchResponse searchResponse, ResultAttributes resultAttributes);

	public JSONObject search(
			HttpServletRequest httpServletRequest,
			ResultAttributes resultAttributes, long searchConfigurationId)
		throws JSONException, PortalException, SearchRequestDataException;

}