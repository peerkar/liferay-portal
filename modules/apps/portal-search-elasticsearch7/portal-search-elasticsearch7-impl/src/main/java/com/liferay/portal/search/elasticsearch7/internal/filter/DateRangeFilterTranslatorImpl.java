/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.elasticsearch7.internal.filter;

import com.liferay.portal.search.elasticsearch7.internal.filter.util.RangeUtil;
import com.liferay.portal.search.filter.DateRangeFilter;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

import org.osgi.service.component.annotations.Component;

/**
 * @author Eric Yan
 * @author André de Oliveira
 */
@Component(service = DateRangeFilterTranslator.class)
public class DateRangeFilterTranslatorImpl
	implements DateRangeFilterTranslator {

	@Override
	public QueryBuilder translate(DateRangeFilter dateRangeFilter) {
		RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(
			dateRangeFilter.getFieldName());

		RangeUtil.addRange(
			dateRangeFilter.getFrom(), rangeQueryBuilder,
			dateRangeFilter.getTo());

		if (dateRangeFilter.getFormat() != null) {
			rangeQueryBuilder.format(dateRangeFilter.getFormat());
		}

		rangeQueryBuilder.includeLower(dateRangeFilter.isIncludeLower());
		rangeQueryBuilder.includeUpper(dateRangeFilter.isIncludeUpper());

		if (dateRangeFilter.getTimeZoneId() != null) {
			rangeQueryBuilder.timeZone(dateRangeFilter.getTimeZoneId());
		}

		return rangeQueryBuilder;
	}

}