/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal;

import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionFixture;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.io.IOException;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.snapshot.CreateSnapshotRequest;
import org.opensearch.client.opensearch.snapshot.DeleteSnapshotRequest;
import org.opensearch.client.opensearch.snapshot.GetSnapshotRequest;
import org.opensearch.client.opensearch.snapshot.GetSnapshotResponse;
import org.opensearch.client.opensearch.snapshot.OpenSearchSnapshotClient;
import org.opensearch.client.opensearch.snapshot.get.SnapshotResponseItem;

/**
 * @author André de Oliveira
 */
public class OpenSearchSearchEngineBackupTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() throws Exception {
		OpenSearchConnectionFixture openSearchConnectionFixture =
			OpenSearchConnectionFixture.builder(
			).clusterName(
				OpenSearchSearchEngineBackupTest.class.getSimpleName()
			).build();

		OpenSearchSearchEngineFixture openSearchSearchEngineFixture =
			new OpenSearchSearchEngineFixture(openSearchConnectionFixture);

		openSearchSearchEngineFixture.setUp();

		_openSearchConnectionFixture = openSearchConnectionFixture;

		_openSearchSearchEngineFixture = openSearchSearchEngineFixture;
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		_openSearchSearchEngineFixture.tearDown();
	}

	@Test
	public void testBackup() throws SearchException {
		OpenSearchSearchEngine openSearchSearchEngine =
			_openSearchSearchEngineFixture.getOpenSearchSearchEngine();

		long companyId = RandomTestUtil.randomLong();

		openSearchSearchEngine.initialize(companyId);

		openSearchSearchEngine.backup(companyId, "backup_test");

		GetSnapshotResponse getSnapshotsResponse = _getGetSnapshotResponse(
			"liferay_backup", new String[] {"backup_test"}, true);

		List<SnapshotResponseItem> snapshotResponseItems =
			getSnapshotsResponse.responses();

		Assert.assertTrue(snapshotResponseItems.size() == 1);

		_deleteSnapshot("liferay_backup", "backup_test");
	}

	@Test
	public void testRestore() throws SearchException {
		OpenSearchSearchEngine openSearchSearchEngine =
			_openSearchSearchEngineFixture.getOpenSearchSearchEngine();

		long companyId = RandomTestUtil.randomLong();

		openSearchSearchEngine.initialize(companyId);

		openSearchSearchEngine.createBackupRepository();

		_createSnapshot(
			"liferay_backup", "restore_test", true, String.valueOf(companyId));

		openSearchSearchEngine.restore(companyId, "restore_test");

		_deleteSnapshot("liferay_backup", "restore_test");
	}

	protected OpenSearchSnapshotClient getOpenSearchSnapshotClient() {
		OpenSearchClient openSearchClient =
			_openSearchConnectionFixture.getOpenSearchClient();

		return openSearchClient.snapshot();
	}

	private void _createSnapshot(
		String repositoryName, String snapshotName, boolean waitForCompletion,
		String... indexNames) {

		CreateSnapshotRequest.Builder createSnapshotRequestBuilder =
			new CreateSnapshotRequest.Builder();

		createSnapshotRequestBuilder.indices(ListUtil.fromArray(indexNames));
		createSnapshotRequestBuilder.repository(repositoryName);
		createSnapshotRequestBuilder.snapshot(snapshotName);
		createSnapshotRequestBuilder.waitForCompletion(waitForCompletion);

		OpenSearchSnapshotClient snapshotClient = getOpenSearchSnapshotClient();

		try {
			snapshotClient.create(createSnapshotRequestBuilder.build());
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private void _deleteSnapshot(String repository, String snapshot) {
		DeleteSnapshotRequest.Builder deleteSnapshotRequestBuilder =
			new DeleteSnapshotRequest.Builder();

		deleteSnapshotRequestBuilder.repository(repository);
		deleteSnapshotRequestBuilder.snapshot(snapshot);

		OpenSearchSnapshotClient openSearchSnapshotClient =
			getOpenSearchSnapshotClient();

		try {
			openSearchSnapshotClient.delete(
				deleteSnapshotRequestBuilder.build());
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private GetSnapshotResponse _getGetSnapshotResponse(
		String repository, String[] snapshots, boolean ignoreUnavailable) {

		GetSnapshotRequest.Builder getSnapshotRequestBuilder =
			new GetSnapshotRequest.Builder();

		getSnapshotRequestBuilder.ignoreUnavailable(ignoreUnavailable);
		getSnapshotRequestBuilder.repository(repository);
		getSnapshotRequestBuilder.snapshot(ListUtil.fromArray(snapshots));

		OpenSearchSnapshotClient openSearchSnapshotClient =
			getOpenSearchSnapshotClient();

		try {
			return openSearchSnapshotClient.get(
				getSnapshotRequestBuilder.build());
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private static OpenSearchConnectionFixture _openSearchConnectionFixture;
	private static OpenSearchSearchEngineFixture _openSearchSearchEngineFixture;

}