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

package com.liferay.portal.search.tuning.gsearch.spi.aggregation.facet;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.parameter.SearchParameterData;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Petteri Karttunen
 */
public interface FacetHandler {

	public void addSearchParameter(
		HttpServletRequest httpServletRequest,
		SearchParameterData searchParameterData,
		JSONObject configurationJsonObject);

	public Optional<JSONObject> getResultsObject(
		SearchRequestContext searchRequestContext,
		AggregationResult aggregationResult,
		JSONObject configurationJsonObject);

}