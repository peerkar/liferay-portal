/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.synonym;

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.index.CreateIndexRequest;
import com.liferay.portal.search.engine.adapter.index.CreateIndexResponse;
import com.liferay.portal.search.engine.adapter.index.DeleteIndexRequest;
import com.liferay.portal.search.engine.adapter.index.DeleteIndexResponse;
import com.liferay.portal.search.engine.adapter.index.IndexRequestExecutor;
import com.liferay.portal.search.opensearch2.internal.connection.IndexName;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionManager;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.search.opensearch2.internal.document.SingleFieldFixture;
import com.liferay.portal.search.opensearch2.internal.query.QueryFactories;
import com.liferay.portal.search.opensearch2.internal.query.SearchAssert;
import com.liferay.portal.search.opensearch2.internal.search.engine.adapter.OpenSearchSearchEngineAdapterImpl;
import com.liferay.portal.search.opensearch2.internal.search.engine.adapter.OpenSearchSearchEngineAdapterIndexRequestTest;
import com.liferay.portal.search.opensearch2.internal.search.engine.adapter.index.IndexRequestExecutorFixture;
import com.liferay.portal.search.opensearch2.internal.util.ResourceUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import org.opensearch.client.opensearch._types.query_dsl.MatchPhraseQuery;
import org.opensearch.client.opensearch._types.query_dsl.Query;

/**
 * @author Adam Brandizzi
 */
public class SynonymFiltersTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() throws Exception {
		_openSearchFixture = new OpenSearchFixture(
			OpenSearchSearchEngineAdapterIndexRequestTest.class.
				getSimpleName());

		_openSearchFixture.setUp();

		_searchEngineAdapter = _createSearchEngineAdapter(_openSearchFixture);

		_singleFieldFixture = new SingleFieldFixture(
			_openSearchFixture.getOpenSearchClient(),
			new IndexName(_INDEX_NAME));

		_singleFieldFixture.setField(_FIELD_NAME);
		_singleFieldFixture.setQueryBuilderFactory(QueryFactories.MATCH);
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		_openSearchFixture.tearDown();
	}

	@After
	public void tearDown() throws Exception {
		_deleteIndex();
	}

	@Test
	public void testSynonymFilterFailsWithSpaceInSynonymSetAndMatchPhraseQuery()
		throws Exception {

		_createIndex("synonym-filter-spaced");

		_singleFieldFixture.indexDocument("git hash");
		_singleFieldFixture.indexDocument("stable");

		_assertMatchPhraseQuerySearch("stable", "git hash");
	}

	@Test
	public void testSynonymFilterIgnoresQuoteInSearchString() throws Exception {
		_createIndex("synonym-filter-unquoted");

		_singleFieldFixture.indexDocument("\"stable\"");
		_singleFieldFixture.indexDocument("upstream");

		_singleFieldFixture.assertSearch(
			"\"stable\"", "\"stable\"", "upstream");
	}

	@Test
	public void testSynonymFilterIgnoresQuoteInSynonymSet() throws Exception {
		_createIndex("synonym-filter-quoted");

		_singleFieldFixture.indexDocument("\"stable\"");
		_singleFieldFixture.indexDocument("upstream");

		_singleFieldFixture.assertSearch("stable", "\"stable\"", "upstream");
	}

	@Test
	public void testSynonymFilterIgnoresSpaceInSearchString() throws Exception {
		_createIndex("synonym-filter-spaced");

		_singleFieldFixture.indexDocument("git hash");
		_singleFieldFixture.indexDocument("stable");

		_singleFieldFixture.assertSearch("git hash", "git hash", "stable");
	}

	@Test
	public void testSynonymFilterIgnoresSpaceInSynonymSet() throws Exception {
		_createIndex("synonym-filter-spaced");

		_singleFieldFixture.indexDocument("git hash");
		_singleFieldFixture.indexDocument("stable");

		_singleFieldFixture.assertSearch("stable", "git hash", "stable");
	}

	@Test
	public void testSynonymGraphFilterIgnoresQuoteInSearchString()
		throws Exception {

		_createIndex("synonym-graph-filter-unquoted");

		_singleFieldFixture.indexDocument("\"stable\"");
		_singleFieldFixture.indexDocument("upstream");

		_singleFieldFixture.assertSearch(
			"\"stable\"", "\"stable\"", "upstream");
	}

	@Test
	public void testSynonymGraphFilterIgnoresQuoteInSynonymSet()
		throws Exception {

		_createIndex("synonym-graph-filter-quoted");

		_singleFieldFixture.indexDocument("\"stable\"");
		_singleFieldFixture.indexDocument("upstream");

		_singleFieldFixture.assertSearch("stable", "\"stable\"", "upstream");
	}

	@Test
	public void testSynonymGraphFilterIgnoresSpaceInSearchString()
		throws Exception {

		_createIndex("synonym-graph-filter-spaced");

		_singleFieldFixture.indexDocument("git hash");
		_singleFieldFixture.indexDocument("stable");

		_singleFieldFixture.assertSearch("git hash", "git hash", "stable");
	}

	@Test
	public void testSynonymGraphFilterIgnoresSpaceInSynonymSet()
		throws Exception {

		_createIndex("synonym-graph-filter-spaced");

		_singleFieldFixture.indexDocument("git hash");
		_singleFieldFixture.indexDocument("stable");

		_singleFieldFixture.assertSearch("stable", "git hash", "stable");
	}

	@Test
	public void testSynonymGraphFilterWorksWithSpaceInSynonymSetAndMatchPhraseQuery()
		throws Exception {

		_createIndex("synonym-graph-filter-spaced");

		_singleFieldFixture.indexDocument("git hash");
		_singleFieldFixture.indexDocument("stable");

		_assertMatchPhraseQuerySearch("stable", "git hash", "stable");
	}

	private static IndexRequestExecutor _createIndexRequestExecutor(
		OpenSearchConnectionManager openSearchConnectionManager) {

		IndexRequestExecutorFixture indexRequestExecutorFixture =
			new IndexRequestExecutorFixture() {
				{
					setOpenSearchConnectionManager(openSearchConnectionManager);
				}
			};

		indexRequestExecutorFixture.setUp();

		return indexRequestExecutorFixture.getIndexRequestExecutor();
	}

	private static SearchEngineAdapter _createSearchEngineAdapter(
		OpenSearchConnectionManager openSearchConnectionManager) {

		SearchEngineAdapter searchEngineAdapter =
			new OpenSearchSearchEngineAdapterImpl();

		ReflectionTestUtil.setFieldValue(
			searchEngineAdapter, "_indexRequestExecutor",
			_createIndexRequestExecutor(openSearchConnectionManager));

		return searchEngineAdapter;
	}

	private void _assertMatchPhraseQuerySearch(
			String text, String... expectedValues)
		throws Exception {

		MatchPhraseQuery.Builder matchPhraseQueryBuilder =
			new MatchPhraseQuery.Builder();

		matchPhraseQueryBuilder.field(_FIELD_NAME);
		matchPhraseQueryBuilder.query(text);

		SearchAssert.assertSearch(
			_openSearchFixture.getOpenSearchClient(), _FIELD_NAME,
			new Query(matchPhraseQueryBuilder.build()), expectedValues);
	}

	private void _createIndex(String suffix) {
		CreateIndexRequest createIndexRequest = new CreateIndexRequest(
			_INDEX_NAME);

		createIndexRequest.setSource(_getSource(suffix));

		CreateIndexResponse createIndexResponse = _searchEngineAdapter.execute(
			createIndexRequest);

		Assert.assertTrue(createIndexResponse.isAcknowledged());
	}

	private void _deleteIndex() {
		DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(
			_INDEX_NAME);

		DeleteIndexResponse deleteIndexResponse = _searchEngineAdapter.execute(
			deleteIndexRequest);

		Assert.assertTrue(deleteIndexResponse.isAcknowledged());
	}

	private String _getSource(String suffix) {
		return ResourceUtil.getResourceAsString(
			getClass(),
			"dependencies/synonym-filters-test-" + suffix + ".json");
	}

	private static final String _FIELD_NAME = "content";

	private static final String _INDEX_NAME = "test_synonyms";

	private static OpenSearchFixture _openSearchFixture;
	private static SearchEngineAdapter _searchEngineAdapter;
	private static SingleFieldFixture _singleFieldFixture;

}