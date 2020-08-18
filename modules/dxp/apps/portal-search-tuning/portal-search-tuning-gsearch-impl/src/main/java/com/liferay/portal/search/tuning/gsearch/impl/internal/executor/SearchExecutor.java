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

package com.liferay.portal.search.tuning.gsearch.impl.internal.executor;

import com.liferay.portal.search.engine.adapter.search.SearchSearchResponse;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.searchrequest.SearchRequestData;

/**
 * @author Petteri Karttunen
 */
public interface SearchExecutor {

	public SearchSearchResponse execute(
		SearchRequestContext searchRequestContext,
		SearchRequestData searchRequestData);

}