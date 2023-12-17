/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter;

import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.cluster.ClusterHealthStatus;
import com.liferay.portal.search.engine.adapter.cluster.ClusterRequestExecutor;
import com.liferay.portal.search.engine.adapter.cluster.HealthClusterRequest;
import com.liferay.portal.search.engine.adapter.cluster.HealthClusterResponse;
import com.liferay.portal.search.engine.adapter.cluster.StateClusterRequest;
import com.liferay.portal.search.engine.adapter.cluster.StateClusterResponse;
import com.liferay.portal.search.engine.adapter.cluster.StatsClusterRequest;
import com.liferay.portal.search.engine.adapter.cluster.StatsClusterResponse;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionManager;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.search.opensearch2.internal.search.engine.adapter.cluster.ClusterRequestExecutorFixture;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.indices.CreateIndexRequest;
import org.opensearch.client.opensearch.indices.DeleteIndexRequest;
import org.opensearch.client.opensearch.indices.OpenSearchIndicesClient;

/**
 * @author Dylan Rebelak
 */
public class OpenSearchSearchEngineAdapterClusterRequestTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() throws Exception {
		_openSearchFixture = new OpenSearchFixture();

		_openSearchFixture.setUp();

		_searchEngineAdapter = createSearchEngineAdapter(_openSearchFixture);

		OpenSearchClient openSearchClient =
			_openSearchFixture.getOpenSearchClient();

		_openSearchIndicesClient = openSearchClient.indices();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		_openSearchFixture.tearDown();
	}

	@Before
	public void setUp() {
		_createIndex();
	}

	@After
	public void tearDown() {
		_deleteIndex();
	}

	@Test
	public void testExecuteHealthClusterRequest() {
		HealthClusterRequest healthClusterRequest = new HealthClusterRequest(
			new String[] {_INDEX_NAME});

		HealthClusterResponse healthClusterResponse =
			_searchEngineAdapter.execute(healthClusterRequest);

		_assertHealthy(healthClusterResponse.getClusterHealthStatus());

		JSONObject jsonObject = createJSONObject(
			healthClusterResponse.getHealthStatusMessage());

		_assertClusterName(jsonObject);

		Assert.assertEquals(
			_ELASTICSEARCH_DEFAULT_NUMBER_OF_SHARDS,
			jsonObject.getString("active_shards"));
	}

	@Test
	public void testExecuteStateClusterRequest() {
		StateClusterRequest stateClusterRequest = new StateClusterRequest(
			new String[] {_INDEX_NAME});

		StateClusterResponse stateClusterResponse =
			_searchEngineAdapter.execute(stateClusterRequest);

		_assertNodesContainLocalhost(stateClusterResponse.getStateMessage());
	}

	@Test
	public void testExecuteStatsClusterRequest() {
		_testExecuteStatsClusterRequest(null);
	}

	@Test
	public void testExecuteStatsClusterRequestWithNodeId() {
		_testExecuteStatsClusterRequest(new String[] {"liferay"});
	}

	protected static SearchEngineAdapter createSearchEngineAdapter(
		OpenSearchConnectionManager openSearchConnectionManager) {

		SearchEngineAdapter searchEngineAdapter =
			new OpenSearchSearchEngineAdapterImpl();

		ReflectionTestUtil.setFieldValue(
			searchEngineAdapter, "_clusterRequestExecutor",
			_createClusterRequestExecutor(openSearchConnectionManager));

		return searchEngineAdapter;
	}

	protected JSONObject createJSONObject(String message) {
		try {
			return JSONFactoryUtil.createJSONObject(message);
		}
		catch (JSONException jsonException) {
			throw new RuntimeException(jsonException);
		}
	}

	private static ClusterRequestExecutor _createClusterRequestExecutor(
		OpenSearchConnectionManager openSearchConnectionManager) {

		ClusterRequestExecutorFixture clusterRequestExecutorFixture =
			new ClusterRequestExecutorFixture() {
				{
					setOpenSearchConnectionManager(openSearchConnectionManager);
				}
			};

		clusterRequestExecutorFixture.setUp();

		return clusterRequestExecutorFixture.getClusterRequestExecutor();
	}

	private void _assertClusterName(JSONObject jsonObject) {
		Assert.assertEquals(
			_CLUSTER_NAME, jsonObject.getString("cluster_name"));
	}

	private void _assertHealthy(ClusterHealthStatus clusterHealthStatus) {
		Assert.assertTrue(
			clusterHealthStatus.equals(ClusterHealthStatus.GREEN) ||
			clusterHealthStatus.equals(ClusterHealthStatus.YELLOW));
	}

	private void _assertNodesContainLocalhost(String message) {
		JSONObject jsonObject = createJSONObject(message);

		String nodesString = jsonObject.getString("nodes");

		Assert.assertTrue(nodesString.contains("127.0.0.1"));
	}

	private void _assertOneIndex(String message) {
		JSONObject jsonObject = createJSONObject(message);

		JSONObject indicesJSONObject = jsonObject.getJSONObject("indices");

		Assert.assertEquals("1", indicesJSONObject.getString("count"));
	}

	private void _createIndex() {
		try {
			_openSearchIndicesClient.create(
				CreateIndexRequest.of(
					createIndexRequest -> createIndexRequest.index(
						_INDEX_NAME)));
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private void _deleteIndex() {
		try {
			_openSearchIndicesClient.delete(
				DeleteIndexRequest.of(
					deleteIndexRequest -> deleteIndexRequest.index(
						_INDEX_NAME)));
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private void _testExecuteStatsClusterRequest(String[] nodeIds) {
		StatsClusterRequest statsClusterRequest = new StatsClusterRequest(
			nodeIds);

		StatsClusterResponse statsClusterResponse =
			_searchEngineAdapter.execute(statsClusterRequest);

		_assertHealthy(statsClusterResponse.getClusterHealthStatus());

		_assertOneIndex(statsClusterResponse.getStatsMessage());
	}

	private static final String _CLUSTER_NAME =
		OpenSearchSearchEngineAdapterClusterRequestTest.class.getSimpleName();

	private static final String _ELASTICSEARCH_DEFAULT_NUMBER_OF_SHARDS = "1";

	private static final String _INDEX_NAME = "test_request_index";

	private static OpenSearchFixture _openSearchFixture;
	private static OpenSearchIndicesClient _openSearchIndicesClient;
	private static SearchEngineAdapter _searchEngineAdapter;

}