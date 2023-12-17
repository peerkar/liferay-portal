/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.groupby;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.search.groupby.GroupByRequest;
import com.liferay.portal.search.opensearch2.internal.highlight.HighlightTranslator;
import com.liferay.portal.search.opensearch2.internal.legacy.sort.SortTranslator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.opensearch.client.opensearch._types.SortOrder;
import org.opensearch.client.opensearch._types.aggregations.Aggregation;
import org.opensearch.client.opensearch._types.aggregations.AggregationBuilders;
import org.opensearch.client.opensearch._types.aggregations.BucketSortAggregation;
import org.opensearch.client.opensearch._types.aggregations.TermsAggregation;
import org.opensearch.client.opensearch._types.aggregations.TopHitsAggregation;
import org.opensearch.client.opensearch.core.SearchRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 * @author Tibor Lipusz
 * @author Petteri Karttunen
 */
@Component(service = GroupByTranslator.class)
public class GroupByTranslatorImpl implements GroupByTranslator {

	@Override
	public void translate(
		GroupByRequest groupByRequest, boolean highlightEnabled,
		String[] highlightFieldNames, int highlightFragmentSize,
		boolean highlightRequireFieldMatch, int highlightSnippetSize,
		Locale locale, SearchRequest.Builder searchRequestBuilder,
		String[] selectedFieldNames) {

		TermsAggregation.Builder termsAggregationBuilder =
			AggregationBuilders.terms();

		termsAggregationBuilder = termsAggregationBuilder.field(
			groupByRequest.getField());

		int termsSize = GetterUtil.getInteger(groupByRequest.getTermsSize());

		if (termsSize > 0) {
			termsAggregationBuilder.size(termsSize);
		}

		_addTermsSorts(groupByRequest, termsAggregationBuilder);

		Aggregation.Builder aggregationBuilder = new Aggregation.Builder();

		Aggregation.Builder.ContainerBuilder containerBuilder =
			aggregationBuilder.terms(termsAggregationBuilder.build());

		int termsStart = GetterUtil.getInteger(groupByRequest.getTermsStart());

		if ((termsSize > 0) || (termsStart > 0)) {
			containerBuilder.aggregations(
				BUCKET_SORT_AGGREGATION_NAME,
				_getBucketSortPipelineAggregation(termsStart, termsSize));
		}

		containerBuilder.aggregations(
			TOP_HITS_AGGREGATION_NAME,
			_getTopHitsAggregation(
				groupByRequest, selectedFieldNames, highlightFieldNames,
				highlightEnabled, highlightRequireFieldMatch,
				highlightFragmentSize, highlightSnippetSize));

		searchRequestBuilder.aggregations(
			GROUP_BY_AGGREGATION_PREFIX + groupByRequest.getField(),
			containerBuilder.build());
	}

	private void _addTermsSorts(
		GroupByRequest groupByRequest,
		TermsAggregation.Builder termsAggregationBuilder) {

		Sort[] sorts = groupByRequest.getTermsSorts();

		if (ArrayUtil.isEmpty(sorts)) {
			return;
		}

		Set<String> sortFieldNames = new HashSet<>();

		Map<String, SortOrder> sortOrders = new HashMap<>();

		for (Sort sort : sorts) {
			if (sort == null) {
				continue;
			}

			String sortFieldName = sort.getFieldName();

			if (sortFieldNames.contains(sortFieldName)) {
				continue;
			}

			sortFieldNames.add(sortFieldName);
			sortOrders.put(sortFieldName, _translateOrder(sort));
		}

		if (!sortOrders.isEmpty()) {
			termsAggregationBuilder.order(sortOrders);
		}
	}

	private Aggregation _getBucketSortPipelineAggregation(int start, int size) {
		BucketSortAggregation.Builder bucketSortAggregationBuilder =
			AggregationBuilders.bucketSort();

		if (start > 0) {
			bucketSortAggregationBuilder.from(start);
		}

		if (size > 0) {
			bucketSortAggregationBuilder.size(size);
		}

		return new Aggregation(bucketSortAggregationBuilder.build());
	}

	private Aggregation _getTopHitsAggregation(
		GroupByRequest groupByRequest, String[] selectedFieldNames,
		String[] highlightFieldNames, boolean highlightEnabled,
		boolean highlightRequireFieldMatch, int highlightFragmentSize,
		int highlightSnippetSize) {

		TopHitsAggregation.Builder topHitsAggregationBuilder =
			AggregationBuilders.topHits();

		int docsStart = GetterUtil.getInteger(groupByRequest.getDocsStart());

		if (docsStart > 0) {
			topHitsAggregationBuilder.from(docsStart);
		}

		int docsSize = GetterUtil.getInteger(groupByRequest.getDocsSize());

		if (docsSize > 0) {
			topHitsAggregationBuilder.size(docsSize);
		}

		if (!ArrayUtil.isEmpty(groupByRequest.getDocsSorts())) {
			topHitsAggregationBuilder.sort(
				_sortTranslator.translateSorts(groupByRequest.getDocsSorts()));
		}

		if (highlightEnabled) {
			topHitsAggregationBuilder.highlight(
				_highlightTranslator.translate(
					highlightFieldNames, highlightFragmentSize,
					highlightRequireFieldMatch, false, highlightSnippetSize));
		}

		if (ArrayUtil.isEmpty(selectedFieldNames)) {
			topHitsAggregationBuilder.storedFields(StringPool.STAR);
		}
		else {
			topHitsAggregationBuilder.storedFields(
				Arrays.asList(selectedFieldNames));
		}

		return new Aggregation(topHitsAggregationBuilder.build());
	}

	private SortOrder _translateOrder(Sort sort) {
		if (sort.isReverse()) {
			return SortOrder.Desc;
		}

		return SortOrder.Asc;
	}

	private final HighlightTranslator _highlightTranslator =
		new HighlightTranslator();

	@Reference
	private SortTranslator _sortTranslator;

}