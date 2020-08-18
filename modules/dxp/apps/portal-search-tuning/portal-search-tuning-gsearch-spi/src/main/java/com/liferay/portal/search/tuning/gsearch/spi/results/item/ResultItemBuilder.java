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

package com.liferay.portal.search.tuning.gsearch.spi.results.item;

import com.liferay.portal.search.document.Document;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.results.ResultAttributes;

import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public interface ResultItemBuilder {

	public String getDate(
			SearchRequestContext searchRequestContext,
			ResultAttributes resultAttributes, Document document)
		throws Exception;

	public String getDescription(
			SearchRequestContext searchRequestContext,
			ResultAttributes resultAttributes, Document document)
		throws Exception;

	public Map<String, String> getMetadata(
			SearchRequestContext queryContext,
			ResultAttributes resultAttributes, Document document)
		throws Exception;

	public String getThumbnail(
			SearchRequestContext searchRequestContext,
			ResultAttributes resultAttributes, Document document)
		throws Exception;

	public String getTitle(
			SearchRequestContext searchRequestContext,
			ResultAttributes resultAttributes, Document document)
		throws Exception;

	public String getType(Document document) throws Exception;

}