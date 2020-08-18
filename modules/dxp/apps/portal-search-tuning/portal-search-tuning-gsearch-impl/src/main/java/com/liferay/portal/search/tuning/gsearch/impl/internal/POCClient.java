package com.liferay.portal.search.tuning.gsearch.impl.internal;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.search.engine.adapter.search.SearchSearchResponse;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.exception.SearchRequestDataException;
import com.liferay.portal.search.tuning.gsearch.results.ResultAttributes;
import com.liferay.portal.search.tuning.gsearch.searchrequest.SearchRequestData;
import com.liferay.portal.search.tuning.gsearch.util.SearchClientHelper;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Here are POC utilities and a few examples of how to consume the framework.
 *
 * @author Petteri Karttunen
 */

@Component
public class POCClient {

	public JSONObject test(
		HttpServletRequest httpServletRequest, long searchConfigurationId) {

		ResultAttributes resultAttributes = new ResultAttributes();

		try {
			return _searchClientHelper.search(
				httpServletRequest, resultAttributes, searchConfigurationId);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		catch (SearchRequestDataException e) {
			e.printStackTrace();
		}
		catch (PortalException e) {
			e.printStackTrace();
		}

		return JSONFactoryUtil.createJSONObject();
	}

	public JSONObject testWithSearchContext(
		SearchContext searchContext, long searchConfigurationId) {

		ResultAttributes resultAttributes = new ResultAttributes();

		try {
			SearchRequestContext searchRequestContext =
				_searchClientHelper.getSearchRequestContext(
					searchContext, searchConfigurationId);

			SearchRequestData searchRequestData =
				_searchClientHelper.getSearchRequestData(searchRequestContext);

			SearchSearchResponse searchResponse =
				_searchClientHelper.getSearchResponse(
					searchRequestContext, searchRequestData);

			return _searchClientHelper.getSearchResults(
				searchRequestContext, searchResponse, resultAttributes);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		catch (SearchRequestDataException e) {
			e.printStackTrace();
		}
		catch (PortalException e) {
			e.printStackTrace();
		}

		return JSONFactoryUtil.createJSONObject();
	}
	
	

	@Reference
	private SearchClientHelper _searchClientHelper;

}