/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.logging;

import com.liferay.portal.kernel.search.generic.MatchAllQuery;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.search.CountSearchRequest;
import com.liferay.portal.search.engine.adapter.search.MultisearchSearchRequest;
import com.liferay.portal.search.engine.adapter.search.SearchSearchRequest;
import com.liferay.portal.search.opensearch2.internal.connection.ClusterHealthResponseUtil;
import com.liferay.portal.search.opensearch2.internal.connection.HealthExpectations;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionManager;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.search.opensearch2.internal.search.engine.adapter.OpenSearchEngineAdapterFixture;
import com.liferay.portal.search.opensearch2.internal.search.engine.adapter.search.CountSearchRequestExecutorImpl;
import com.liferay.portal.search.opensearch2.internal.search.engine.adapter.search.MultisearchSearchRequestExecutorImpl;
import com.liferay.portal.search.opensearch2.internal.search.engine.adapter.search.SearchSearchRequestExecutorImpl;
import com.liferay.portal.search.test.util.logging.ExpectedLog;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.opensearch.client.opensearch._types.HealthStatus;

/**
 * @author Bryan Engler
 * @author André de Oliveira
 */
public class OpenSearchSearchEngineAdapterLoggingTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() throws Exception {
		_openSearchFixture = new OpenSearchFixture();

		_openSearchFixture.setUp();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		_openSearchFixture.tearDown();
	}

	@Before
	public void setUp() {
		_openSearchEngineAdapterFixture = new OpenSearchEngineAdapterFixture() {
			{
				setOpenSearchConnectionManager(_openSearchFixture);
			}
		};

		_openSearchEngineAdapterFixture.setUp();

		_waitForOpenSearchToStart(_openSearchFixture);

		_searchEngineAdapter =
			_openSearchEngineAdapterFixture.getSearchEngineAdapter();
	}

	@After
	public void tearDown() {
		_openSearchEngineAdapterFixture.tearDown();
	}

	@ExpectedLog(
		expectedClass = CountSearchRequestExecutorImpl.class,
		expectedLevel = ExpectedLog.Level.FINE,
		expectedLog = "The search engine processed"
	)
	@Test
	public void testCountSearchRequestExecutorLogs() {
		_searchEngineAdapter.execute(
			new CountSearchRequest() {
				{
					setIndexNames("_all");
					setQuery(new MatchAllQuery());
				}
			});
	}

	@ExpectedLog(
		expectedClass = MultisearchSearchRequestExecutorImpl.class,
		expectedLevel = ExpectedLog.Level.FINE,
		expectedLog = "The search engine processed"
	)
	@Test
	public void testMultisearchSearchRequestExecutorLogs() {
		_searchEngineAdapter.execute(
			new MultisearchSearchRequest() {
				{
					addSearchSearchRequest(
						new SearchSearchRequest() {
							{
								setIndexNames("_all");
								setQuery(new MatchAllQuery());
							}
						});
				}
			});
	}

	@ExpectedLog(
		expectedClass = SearchSearchRequestExecutorImpl.class,
		expectedLevel = ExpectedLog.Level.FINE,
		expectedLog = "The search engine processed"
	)
	@Test
	public void testSearchSearchRequestExecutorLogs() {
		_searchEngineAdapter.execute(
			new SearchSearchRequest() {
				{
					setIndexNames("_all");
					setQuery(new MatchAllQuery());
				}
			});
	}

	private void _waitForOpenSearchToStart(
		OpenSearchConnectionManager openSearchConnectionManager) {

		ClusterHealthResponseUtil.getClusterHealthResponse(
			openSearchConnectionManager,
			new HealthExpectations() {
				{
					setActivePrimaryShards(0);
					setActiveShards(0);
					setNumberOfDataNodes(1);
					setNumberOfNodes(1);
					setStatus(HealthStatus.Green);
					setUnassignedShards(0);
				}
			});
	}

	private static OpenSearchFixture _openSearchFixture;

	private OpenSearchEngineAdapterFixture _openSearchEngineAdapterFixture;
	private SearchEngineAdapter _searchEngineAdapter;

}