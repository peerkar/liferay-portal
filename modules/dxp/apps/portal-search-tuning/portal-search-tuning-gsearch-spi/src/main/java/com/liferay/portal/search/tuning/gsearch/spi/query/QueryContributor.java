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

package com.liferay.portal.search.tuning.gsearch.spi.query;

import com.liferay.portal.search.query.Query;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.values.Occur;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.exception.SearchRequestDataException;

import java.util.Optional;

/**
 * @author Petteri Karttunen
 */
public interface QueryContributor {

	public Optional<Query> build(SearchRequestContext searchRequestContext)
		throws SearchRequestDataException;

	public Occur getOccur();

}