/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.elasticsearch7.internal.filter;

import com.liferay.portal.search.elasticsearch7.internal.filter.util.RangeUtil;
import com.liferay.portal.search.filter.RangeFilter;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(service = RangeFilterTranslator.class)
public class RangeFilterTranslatorImpl implements RangeFilterTranslator {

	@Override
	public QueryBuilder translate(RangeFilter rangeFilter) {
		RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(
			rangeFilter.getFieldName());

		RangeUtil.addRange(
			rangeFilter.getFrom(), rangeQueryBuilder, rangeFilter.getTo());

		if (rangeFilter.getFormat() != null) {
			rangeQueryBuilder.format(rangeFilter.getFormat());
		}

		rangeQueryBuilder.includeLower(rangeFilter.isIncludeLower());
		rangeQueryBuilder.includeUpper(rangeFilter.isIncludeUpper());

		if (rangeFilter.getTimeZoneId() != null) {
			rangeQueryBuilder.timeZone(rangeFilter.getTimeZoneId());
		}

		return rangeQueryBuilder;
	}

}