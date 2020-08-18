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

package com.liferay.portal.search.tuning.gsearch.searchrequest;

import com.liferay.portal.search.aggregation.Aggregation;
import com.liferay.portal.search.query.BooleanQuery;
import com.liferay.portal.search.query.Queries;
import com.liferay.portal.search.rescore.Rescore;
import com.liferay.portal.search.sort.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Petteri Karttunen
 */
public class SearchRequestData {

	public SearchRequestData(Queries queries) {
		_aggregations = new ArrayList<>();
		_postFilterQuery = queries.booleanQuery();
		_query = queries.booleanQuery();
		_rescores = new ArrayList<>();
		_sorts = new ArrayList<>();
	}

	public List<Aggregation> getAggregations() {
		return _aggregations;
	}

	public BooleanQuery getPostFilterQuery() {
		return _postFilterQuery;
	}

	public BooleanQuery getQuery() {
		return _query;
	}

	public List<Rescore> getRescores() {
		return _rescores;
	}

	public List<Sort> getSorts() {
		return _sorts;
	}

	private final List<Aggregation> _aggregations;
	private final BooleanQuery _postFilterQuery;
	private final BooleanQuery _query;
	private final List<Rescore> _rescores;
	private List<Sort> _sorts;

}