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

package fi.soveltia.liferay.gsearch.elasticsearch.internal.query;

import com.liferay.portal.search.query.TermQuery;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.search.elasticsearch6.internal.query.TermQueryTranslator;

/**
 * Liferay GSearch TermQuery translator.
 * 
 * Adds the boost option, missing in default translator.
 * 
 * @author Petteri Karttunen
 * @author André de Oliveira
 * @author Miguel Angelo Caldas Gallindo
 */
@Component(
	property = {
		"service.ranking:Integer=100"
	},
	service = TermQueryTranslator.class
)
public class GSearchTermQueryTranslatorImpl implements TermQueryTranslator {

	@Override
	public QueryBuilder translate(TermQuery termQuery) {
		
		TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(
				termQuery.getField(), termQuery.getValue());

		if (termQuery.getBoost() !=  null) {
			termQueryBuilder.boost(termQuery.getBoost());
		}

		return termQueryBuilder;		
	}
}