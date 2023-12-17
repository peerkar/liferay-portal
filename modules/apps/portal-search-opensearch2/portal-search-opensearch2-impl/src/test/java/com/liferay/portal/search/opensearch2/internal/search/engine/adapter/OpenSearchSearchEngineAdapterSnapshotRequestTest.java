/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter;

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.snapshot.CreateSnapshotRepositoryRequest;
import com.liferay.portal.search.engine.adapter.snapshot.CreateSnapshotRepositoryResponse;
import com.liferay.portal.search.engine.adapter.snapshot.CreateSnapshotRequest;
import com.liferay.portal.search.engine.adapter.snapshot.CreateSnapshotResponse;
import com.liferay.portal.search.engine.adapter.snapshot.DeleteSnapshotRequest;
import com.liferay.portal.search.engine.adapter.snapshot.DeleteSnapshotResponse;
import com.liferay.portal.search.engine.adapter.snapshot.GetSnapshotRepositoriesRequest;
import com.liferay.portal.search.engine.adapter.snapshot.GetSnapshotRepositoriesResponse;
import com.liferay.portal.search.engine.adapter.snapshot.GetSnapshotsRequest;
import com.liferay.portal.search.engine.adapter.snapshot.RestoreSnapshotRequest;
import com.liferay.portal.search.engine.adapter.snapshot.SnapshotDetails;
import com.liferay.portal.search.engine.adapter.snapshot.SnapshotRepositoryDetails;
import com.liferay.portal.search.engine.adapter.snapshot.SnapshotRequestExecutor;
import com.liferay.portal.search.engine.adapter.snapshot.SnapshotState;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionManager;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.search.opensearch2.internal.search.engine.adapter.snapshot.SnapshotRequestExecutorFixture;
import com.liferay.portal.search.test.util.IdempotentRetryAssert;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.io.IOException;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
import org.opensearch.client.opensearch.indices.ExistsRequest;
import org.opensearch.client.opensearch.indices.OpenSearchIndicesClient;
import org.opensearch.client.opensearch.snapshot.CreateRepositoryRequest;
import org.opensearch.client.opensearch.snapshot.DeleteRepositoryRequest;
import org.opensearch.client.opensearch.snapshot.GetRepositoryRequest;
import org.opensearch.client.opensearch.snapshot.GetRepositoryResponse;
import org.opensearch.client.opensearch.snapshot.GetSnapshotRequest;
import org.opensearch.client.opensearch.snapshot.GetSnapshotResponse;
import org.opensearch.client.opensearch.snapshot.OpenSearchSnapshotClient;
import org.opensearch.client.opensearch.snapshot.Repository;
import org.opensearch.client.opensearch.snapshot.RepositorySettings;
import org.opensearch.client.opensearch.snapshot.SnapshotInfo;
import org.opensearch.client.transport.endpoints.BooleanResponse;

/**
 * @author Michael C. Han
 * @author Petteri Karttunen
 */
public class OpenSearchSearchEngineAdapterSnapshotRequestTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() throws Exception {
		_openSearchFixture = new OpenSearchFixture(
			OpenSearchSearchEngineAdapterSnapshotRequestTest.class);

		_openSearchFixture.setUp();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		_openSearchFixture.tearDown();
	}

	@Before
	public void setUp() throws Exception {
		_searchEngineAdapter = createSearchEngineAdapter(_openSearchFixture);

		OpenSearchClient openSearchClient =
			_openSearchFixture.getOpenSearchClient();

		_openSearchIndicesClient = openSearchClient.indices();

		_openSearchSnapshotClient = openSearchClient.snapshot();

		_createIndex();
		_createRepository(_TEST_REPOSITORY_NAME, _TEST_REPOSITORY_NAME);
	}

	@After
	public void tearDown() throws Exception {
		_deleteIndex();
		_deleteRepository(_TEST_REPOSITORY_NAME);
	}

	@Test
	public void testCreateSnapshot() {
		CreateSnapshotRequest createSnapshotRequest = new CreateSnapshotRequest(
			_TEST_REPOSITORY_NAME, "test_create_snapshot");

		createSnapshotRequest.setIndexNames(_INDEX_NAME);

		CreateSnapshotResponse createSnapshotResponse =
			_searchEngineAdapter.execute(createSnapshotRequest);

		SnapshotDetails snapshotDetails =
			createSnapshotResponse.getSnapshotDetails();

		Assert.assertArrayEquals(
			createSnapshotRequest.getIndexNames(),
			snapshotDetails.getIndexNames());

		Assert.assertEquals(
			SnapshotState.SUCCESS, snapshotDetails.getSnapshotState());

		Assert.assertTrue(snapshotDetails.getSuccessfulShards() > 0);

		List<SnapshotInfo> snapshotInfos = _getSnapshotInfo(
			"test_create_snapshot");

		Assert.assertEquals("Expected 1 SnapshotInfo", 1, snapshotInfos.size());

		SnapshotInfo snapshotInfo = snapshotInfos.get(0);

		List<String> indices = snapshotInfo.indices();

		Assert.assertArrayEquals(
			createSnapshotRequest.getIndexNames(), indices.toArray());

		Assert.assertEquals(
			"test_create_snapshot", createSnapshotRequest.getSnapshotName());
		Assert.assertEquals(
			createSnapshotRequest.getRepositoryName(), _TEST_REPOSITORY_NAME);

		_deleteSnapshot(_TEST_REPOSITORY_NAME, "test_create_snapshot");
	}

	@Test
	public void testCreateSnapshotRepository() {
		CreateSnapshotRepositoryRequest createSnapshotRepositoryRequest =
			new CreateSnapshotRepositoryRequest(
				"testCreateSnapshotRepository", "testCreateSnapshotRepository");

		CreateSnapshotRepositoryResponse createSnapshotRepositoryResponse =
			_searchEngineAdapter.execute(createSnapshotRepositoryRequest);

		Assert.assertTrue(createSnapshotRepositoryResponse.isAcknowledged());

		GetRepositoryResponse getRepositoryResponse =
			_getGetRepositoriesResponse(
				new String[] {"testCreateSnapshotRepository"});

		Map<String, Repository> repositories = getRepositoryResponse.result();

		Assert.assertEquals(
			"Expected 1 RepositoryMetadata", 1, repositories.size());

		Set<String> repositoryKeys = repositories.keySet();

		Iterator<String> iterator = repositoryKeys.iterator();

		Assert.assertEquals("testCreateSnapshotRepository", iterator.next());

		Repository repository = repositories.get(0);

		Assert.assertEquals(
			SnapshotRepositoryDetails.FS_REPOSITORY_TYPE, repository.type());

		_deleteRepository("testCreateSnapshotRepository");
	}

	@Test
	public void testDeleteSnapshot() throws Exception {
		_createSnapshot(
			_TEST_REPOSITORY_NAME, "test_delete_snapshot", true, _INDEX_NAME);

		IdempotentRetryAssert.retryAssert(
			10, TimeUnit.SECONDS,
			() -> {
				List<SnapshotInfo> snapshotInfos = _getSnapshotInfo(
					"test_delete_snapshot");

				Assert.assertEquals(
					"Expected 1 SnapshotInfo", 1, snapshotInfos.size());

				DeleteSnapshotRequest deleteSnapshotRequest =
					new DeleteSnapshotRequest(
						_TEST_REPOSITORY_NAME, "test_delete_snapshot");

				DeleteSnapshotResponse deleteSnapshotResponse =
					_searchEngineAdapter.execute(deleteSnapshotRequest);

				Assert.assertTrue(deleteSnapshotResponse.isAcknowledged());

				snapshotInfos = _getSnapshotInfo("test_delete_snapshot");

				Assert.assertTrue(snapshotInfos.isEmpty());

				return null;
			});
	}

	@Test
	public void testGetSnapshotRepositories() {
		GetSnapshotRepositoriesRequest getSnapshotRepositoriesRequest =
			new GetSnapshotRepositoriesRequest(_TEST_REPOSITORY_NAME);

		GetSnapshotRepositoriesResponse getSnapshotRepositoriesResponse =
			_searchEngineAdapter.execute(getSnapshotRepositoriesRequest);

		List<SnapshotRepositoryDetails> snapshotRepositoryDetailsList =
			getSnapshotRepositoriesResponse.getSnapshotRepositoryDetails();

		Assert.assertEquals(
			"Expected 1 SnapshotRepositoryDetails", 1,
			snapshotRepositoryDetailsList.size());

		SnapshotRepositoryDetails snapshotRepositoryDetails =
			snapshotRepositoryDetailsList.get(0);

		Assert.assertEquals(
			_TEST_REPOSITORY_NAME, snapshotRepositoryDetails.getName());
		Assert.assertEquals(
			SnapshotRepositoryDetails.FS_REPOSITORY_TYPE,
			snapshotRepositoryDetails.getType());
	}

	@Test
	public void testGetSnapshots() {
		_createSnapshot(
			_TEST_REPOSITORY_NAME, "test_get_snapshots", true, _INDEX_NAME);

		GetSnapshotsRequest getSnapshotsRequest = new GetSnapshotsRequest(
			_TEST_REPOSITORY_NAME);

		getSnapshotsRequest.setSnapshotNames("test_get_snapshots");

		com.liferay.portal.search.engine.adapter.snapshot.GetSnapshotsResponse
			getSnapshotsResponse = _searchEngineAdapter.execute(
				getSnapshotsRequest);

		List<SnapshotDetails> snapshotDetailsList =
			getSnapshotsResponse.getSnapshotDetails();

		Assert.assertEquals(
			"Expected 1 SnapshotDetails", 1, snapshotDetailsList.size());

		SnapshotDetails snapshotDetails = snapshotDetailsList.get(0);

		Assert.assertArrayEquals(
			new String[] {_INDEX_NAME}, snapshotDetails.getIndexNames());
		Assert.assertEquals(
			SnapshotState.SUCCESS, snapshotDetails.getSnapshotState());

		_deleteSnapshot(_TEST_REPOSITORY_NAME, "test_get_snapshots");
	}

	@Test
	public void testRestoreSnapshot() {
		_createSnapshot(
			_TEST_REPOSITORY_NAME, "test_restore_snapshot", true, _INDEX_NAME);

		_deleteIndex();

		RestoreSnapshotRequest restoreSnapshotRequest =
			new RestoreSnapshotRequest(
				_TEST_REPOSITORY_NAME, "test_restore_snapshot");

		restoreSnapshotRequest.setIndexNames(_INDEX_NAME);

		_searchEngineAdapter.execute(restoreSnapshotRequest);

		Assert.assertTrue("Indices not restored", _indicesExists(_INDEX_NAME));

		_deleteSnapshot(_TEST_REPOSITORY_NAME, "test_restore_snapshot");
	}

	protected static SearchEngineAdapter createSearchEngineAdapter(
		OpenSearchConnectionManager openSearchConnectionManager) {

		SearchEngineAdapter searchEngineAdapter =
			new OpenSearchSearchEngineAdapterImpl();

		ReflectionTestUtil.setFieldValue(
			searchEngineAdapter, "_snapshotRequestExecutor",
			_createSnapshotRequestExecutor(openSearchConnectionManager));

		return searchEngineAdapter;
	}

	private static SnapshotRequestExecutor _createSnapshotRequestExecutor(
		OpenSearchConnectionManager openSearchConnectionManager) {

		SnapshotRequestExecutorFixture snapshotRequestExecutorFixture =
			new SnapshotRequestExecutorFixture() {
				{
					setOpenSearchConnectionManager(openSearchConnectionManager);
				}
			};

		snapshotRequestExecutorFixture.setUp();

		return snapshotRequestExecutorFixture.getSnapshotRequestExecutor();
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

	private void _createRepository(String name, String location) {
		CreateRepositoryRequest.Builder createRepositoryRequestBuilder =
			new CreateRepositoryRequest.Builder();

		createRepositoryRequestBuilder.name(name);
		createRepositoryRequestBuilder.settings(
			RepositorySettings.of(
				repositorySettings -> repositorySettings.location(location)));
		createRepositoryRequestBuilder.type(
			SnapshotRepositoryDetails.FS_REPOSITORY_TYPE);

		try {
			_openSearchSnapshotClient.createRepository(
				createRepositoryRequestBuilder.build());
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private void _createSnapshot(
		String repositoryName, String snapshotName, boolean waitForCompletion,
		String... indexNames) {

		org.opensearch.client.opensearch.snapshot.CreateSnapshotRequest.Builder
			createSnapshotRequestBuilder =
				new org.opensearch.client.opensearch.snapshot.
					CreateSnapshotRequest.Builder();

		createSnapshotRequestBuilder.indices(ListUtil.fromArray(indexNames));
		createSnapshotRequestBuilder.repository(repositoryName);
		createSnapshotRequestBuilder.snapshot(snapshotName);
		createSnapshotRequestBuilder.waitForCompletion(waitForCompletion);

		try {
			_openSearchSnapshotClient.create(
				createSnapshotRequestBuilder.build());
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

	private void _deleteRepository(String name) {
		try {
			_openSearchSnapshotClient.deleteRepository(
				DeleteRepositoryRequest.of(
					deleteRepositoryRequest -> deleteRepositoryRequest.name(
						name)));
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private void _deleteSnapshot(String repository, String snapshot) {
		try {
			_openSearchSnapshotClient.delete(
				org.opensearch.client.opensearch.snapshot.DeleteSnapshotRequest.
					of(
						deleteSnapshotRequest ->
							deleteSnapshotRequest.repository(
								repository
							).snapshot(
								snapshot
							)));
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private GetRepositoryResponse _getGetRepositoriesResponse(
		String[] repositories) {

		try {
			return _openSearchSnapshotClient.getRepository(
				GetRepositoryRequest.of(
					getRepositoryRequest -> getRepositoryRequest.name(
						ListUtil.fromArray(repositories))));
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private List<SnapshotInfo> _getSnapshotInfo(String snapshotName) {
		GetSnapshotRequest.Builder getSnapshopRequestBuilder =
			new GetSnapshotRequest.Builder();

		getSnapshopRequestBuilder.ignoreUnavailable(true);
		getSnapshopRequestBuilder.repository(_TEST_REPOSITORY_NAME);
		getSnapshopRequestBuilder.snapshot(snapshotName);

		try {
			GetSnapshotResponse getSnapshotsResponse =
				_openSearchSnapshotClient.get(
					getSnapshopRequestBuilder.build());

			return getSnapshotsResponse.snapshots();
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private boolean _indicesExists(String indexName) {
		try {
			BooleanResponse booleanResponse = _openSearchIndicesClient.exists(
				ExistsRequest.of(
					existRequest -> existRequest.index(indexName)));

			return booleanResponse.value();
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private static final String _INDEX_NAME = "test_request_index";

	private static final String _TEST_REPOSITORY_NAME =
		"testRepositoryOperations";

	private static OpenSearchFixture _openSearchFixture;

	private OpenSearchIndicesClient _openSearchIndicesClient;
	private OpenSearchSnapshotClient _openSearchSnapshotClient;
	private SearchEngineAdapter _searchEngineAdapter;

}