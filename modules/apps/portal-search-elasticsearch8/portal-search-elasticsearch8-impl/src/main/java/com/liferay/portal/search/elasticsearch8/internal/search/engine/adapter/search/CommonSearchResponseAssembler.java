/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.elasticsearch8.internal.search.engine.adapter.search;

import co.elastic.clients.elasticsearch.core.search.ResponseBody;
import co.elastic.clients.json.JsonData;

import com.liferay.portal.search.engine.adapter.search.BaseSearchRequest;
import com.liferay.portal.search.engine.adapter.search.BaseSearchResponse;

/**
 * @author Michael C. Han
 */
public interface CommonSearchResponseAssembler {

	public void assemble(
		BaseSearchRequest baseSearchRequest,
		BaseSearchResponse baseSearchResponse,
		ResponseBody<JsonData> responseBody, String searchRequestString);

}