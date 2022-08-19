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

package com.liferay.portal.search.tuning.rankings.web.internal.searcher;

import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.search.searcher.SearchRequest;
import com.liferay.portal.search.searcher.SearchRequestBuilder;
import com.liferay.portal.search.searcher.SearchRequestBuilderFactory;
import com.liferay.portal.search.spi.searcher.SearchRequestContributor;
import com.liferay.portal.search.tuning.rankings.web.internal.index.Ranking;
import com.liferay.portal.search.tuning.rankings.web.internal.index.RankingIndexReader;
import com.liferay.portal.search.tuning.rankings.web.internal.index.name.RankingIndexName;
import com.liferay.portal.search.tuning.rankings.web.internal.index.name.RankingIndexNameBuilder;
import com.liferay.portal.search.tuning.rankings.web.internal.searcher.helper.RankingSearchRequestHelper;

import java.util.Optional;
import java.util.function.Function;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author André de Oliveira
 */
@Component(
	immediate = true,
	property = "search.request.contributor.id=com.liferay.portal.search.ranking",
	service = SearchRequestContributor.class
)
public class RankingSearchRequestContributor
	implements SearchRequestContributor {

	@Override
	public SearchRequest contribute(SearchRequest searchRequest) {
		RankingIndexName rankingIndexName = _getRankingIndexName(searchRequest);

		if (!rankingIndexReader.isExists(rankingIndexName)) {
			return searchRequest;
		}

		SearchContext searchContext = _getSearchContext(searchRequest);

		Optional<Ranking> optional = rankingIndexReader.fetchOptional(
			searchContext.getGroupIds(), searchRequest.getQueryString(),
			rankingIndexName,
			GetterUtil.getLong(
				searchContext.getAttribute("search.experiences.blueprint.id")));

		return optional.map(
			ranking -> contribute(searchRequest, ranking)
		).orElse(
			searchRequest
		);
	}

	protected SearchRequest contribute(
		SearchRequest searchRequest, Ranking ranking) {

		SearchRequestBuilder searchRequestBuilder =
			searchRequestBuilderFactory.builder(searchRequest);

		rankingSearchRequestHelper.contribute(searchRequestBuilder, ranking);

		return searchRequestBuilder.build();
	}

	@Reference
	protected RankingIndexNameBuilder rankingIndexNameBuilder;

	@Reference
	protected RankingIndexReader rankingIndexReader;

	@Reference
	protected RankingSearchRequestHelper rankingSearchRequestHelper;

	@Reference
	protected SearchRequestBuilderFactory searchRequestBuilderFactory;

	private RankingIndexName _getRankingIndexName(SearchRequest searchRequest) {
		SearchRequestBuilder builder = searchRequestBuilderFactory.builder(
			searchRequest);

		long[] companyIds = new long[1];

		builder.withSearchContext(
			searchContext -> companyIds[0] = searchContext.getCompanyId());

		return rankingIndexNameBuilder.getRankingIndexName(companyIds[0]);
	}

	private SearchContext _getSearchContext(SearchRequest searchRequest) {
		SearchRequestBuilder searchRequestBuilder =
			searchRequestBuilderFactory.builder(searchRequest);

		return searchRequestBuilder.withSearchContextGet(Function.identity());
	}

}