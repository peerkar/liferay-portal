/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.search.elasticsearch6.internal.query;

import com.liferay.portal.search.query.WildcardQuery;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.osgi.service.component.annotations.Component;

/**
 * @author Miguel Angelo Caldas Gallindo
 * @author Petteri Karttunen
 */
@Component(service = WildcardQueryTranslator.class)
public class WildcardQueryTranslatorImpl implements WildcardQueryTranslator {

	@Override
	public QueryBuilder translate(WildcardQuery wildcardQuery) {
		
		String field = wildcardQuery.getField();
		String value = wildcardQuery.getValue();

		WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery(
			field, value);

		if (wildcardQuery.getBoost() !=  null) {
			wildcardQueryBuilder.boost(wildcardQuery.getBoost());
		}

		if (wildcardQuery.getRewrite() != null) {
			wildcardQueryBuilder.rewrite(wildcardQuery.getRewrite());
		}

		return wildcardQueryBuilder;
	}

}