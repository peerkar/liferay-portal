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

package com.liferay.portal.search.tuning.rankings.web.internal.index;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.document.Document;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.document.GetDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.GetDocumentResponse;
import com.liferay.portal.search.engine.adapter.index.IndicesExistsIndexRequest;
import com.liferay.portal.search.engine.adapter.index.IndicesExistsIndexResponse;
import com.liferay.portal.search.engine.adapter.search.SearchSearchRequest;
import com.liferay.portal.search.engine.adapter.search.SearchSearchResponse;
import com.liferay.portal.search.hits.SearchHit;
import com.liferay.portal.search.hits.SearchHits;
import com.liferay.portal.search.query.BooleanQuery;
import com.liferay.portal.search.query.Queries;
import com.liferay.portal.search.tuning.rankings.web.internal.index.name.RankingIndexName;

import java.util.List;
import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Bryan Engler
 */
@Component(service = RankingIndexReader.class)
public class RankingIndexReaderImpl implements RankingIndexReader {

	@Override
	public Optional<Ranking> fetchOptional(
		long[] groupIds, String queryString, RankingIndexName rankingIndexName,
		long sxpBlueprintId) {

		if (Validator.isBlank(queryString)) {
			return Optional.empty();
		}

		SearchSearchRequest searchSearchRequest = new SearchSearchRequest();

		searchSearchRequest.setIndexNames(rankingIndexName.getIndexName());
		searchSearchRequest.setQuery(_getQueryStringQuery(queryString));
		searchSearchRequest.setSize(3);

		SearchSearchResponse searchSearchResponse =
			_searchEngineAdapter.execute(searchSearchRequest);

		return _getRankingOptional(
			groupIds, rankingIndexName, searchSearchResponse, sxpBlueprintId);
	}

	@Override
	public Optional<Ranking> fetchOptional(
		String id, RankingIndexName rankingIndexName) {

		return Optional.ofNullable(
			_getDocument(rankingIndexName, id)
		).map(
			document -> translate(document, id)
		);
	}

	@Override
	public boolean isExists(RankingIndexName rankingIndexName) {
		IndicesExistsIndexRequest indicesExistsIndexRequest =
			new IndicesExistsIndexRequest(rankingIndexName.getIndexName());

		IndicesExistsIndexResponse indicesExistsIndexResponse =
			_searchEngineAdapter.execute(indicesExistsIndexRequest);

		return indicesExistsIndexResponse.isExists();
	}

	@Reference(unbind = "-")
	protected void setQueries(Queries queries) {
		_queries = queries;
	}

	@Reference(unbind = "-")
	protected void setSearchEngineAdapter(
		SearchEngineAdapter searchEngineAdapter) {

		_searchEngineAdapter = searchEngineAdapter;
	}

	protected Ranking translate(Document document, String id) {
		return _documentToRankingTranslator.translate(document, id);
	}

	private Document _getDocument(
		RankingIndexName rankingIndexName, String id) {

		GetDocumentRequest getDocumentRequest = new GetDocumentRequest(
			rankingIndexName.getIndexName(), id);

		getDocumentRequest.setFetchSource(true);
		getDocumentRequest.setFetchSourceInclude(StringPool.STAR);
		getDocumentRequest.setPreferLocalCluster(false);

		GetDocumentResponse getDocumentResponse = _searchEngineAdapter.execute(
			getDocumentRequest);

		if (getDocumentResponse.isExists()) {
			return getDocumentResponse.getDocument();
		}

		return null;
	}

	private BooleanQuery _getQueryStringQuery(String queryString) {
		BooleanQuery booleanQuery = _queries.booleanQuery();

		booleanQuery.addFilterQueryClauses(
			_queries.term(RankingFields.QUERY_STRINGS_KEYWORD, queryString));
		booleanQuery.addMustNotQueryClauses(
			_queries.term(RankingFields.INACTIVE, true));

		return booleanQuery;
	}

	private Optional<Ranking> _getRankingOptional(
		long[] groupIds, RankingIndexName rankingIndexName,
		SearchSearchResponse searchSearchResponse, long sxpBlueprintId) {

		if (searchSearchResponse.getCount() == 0) {
			return Optional.empty();
		}

		SearchHits searchHits = searchSearchResponse.getSearchHits();

		List<SearchHit> searchHitsList = searchHits.getSearchHits();

		List<Long> groupIdsList = ListUtil.fromArray(groupIds);

		String id = null;

		for (SearchHit searchHit : searchHitsList) {
			Document document = searchHit.getDocument();

			List<Long> rankingGroupIds = document.getLongs(
				RankingFields.GROUP_IDS);

			long rankingSXPBlueprintId = document.getLong(
				RankingFields.SXP_BLUEPRINT_ID);

			if ((sxpBlueprintId != 0) &&
				(sxpBlueprintId == rankingSXPBlueprintId)) {

				return fetchOptional(searchHit.getId(), rankingIndexName);
			}
			else if (!groupIdsList.isEmpty() && !rankingGroupIds.isEmpty()) {
				for (Long groupId : groupIds) {
					if (rankingGroupIds.contains(groupId)) {
						id = searchHit.getId();

						break;
					}
				}
			}
			else if (id == null) {
				id = searchHit.getId();
			}
		}

		return fetchOptional(id, rankingIndexName);
	}

	@Reference
	private DocumentToRankingTranslator _documentToRankingTranslator;

	private Queries _queries;
	private SearchEngineAdapter _searchEngineAdapter;

}