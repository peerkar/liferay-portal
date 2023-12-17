/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter.snapshot;

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.search.engine.adapter.snapshot.RestoreSnapshotRequest;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.search.opensearch2.internal.search.engine.adapter.index.AnalyzeIndexRequestExecutorTest;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import org.opensearch.client.opensearch.snapshot.RestoreRequest;

/**
 * @author Michael C. Han
 */
public class RestoreSnapshotRequestExecutorImplTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		_openSearchFixture = new OpenSearchFixture(
			AnalyzeIndexRequestExecutorTest.class.getSimpleName());

		_openSearchFixture.setUp();
	}

	@After
	public void tearDown() throws Exception {
		_openSearchFixture.tearDown();
	}

	@Test
	public void testCreateRestoreSnapshotRequest() {
		RestoreSnapshotRequest restoreSnapshotRequest =
			new RestoreSnapshotRequest("repositoryName", "snapshotName");

		restoreSnapshotRequest.setIncludeAliases(true);
		restoreSnapshotRequest.setPartialRestore(true);
		restoreSnapshotRequest.setRestoreGlobalState(true);
		restoreSnapshotRequest.setWaitForCompletion(true);
		restoreSnapshotRequest.setIndexNames("index1", "index2");

		RestoreSnapshotRequestExecutorImpl restoreSnapshotRequestExecutorImpl =
			new RestoreSnapshotRequestExecutorImpl();

		ReflectionTestUtil.setFieldValue(
			restoreSnapshotRequestExecutorImpl, "_openSearchConnectionManager",
			_openSearchFixture);

		RestoreRequest restoreRequest =
			restoreSnapshotRequestExecutorImpl.createRestoreRequest(
				restoreSnapshotRequest);

		Assert.assertArrayEquals(
			restoreSnapshotRequest.getIndexNames(),
			ArrayUtil.toStringArray(restoreRequest.indices()));
		Assert.assertEquals(
			restoreSnapshotRequest.isIncludeAliases(),
			restoreRequest.includeAliases());
		Assert.assertEquals(
			restoreSnapshotRequest.isPartialRestore(),
			restoreRequest.partial());
		Assert.assertEquals(
			restoreSnapshotRequest.getRepositoryName(),
			restoreRequest.repository());
		Assert.assertEquals(
			restoreSnapshotRequest.isRestoreGlobalState(),
			restoreRequest.includeGlobalState());
		Assert.assertEquals(
			restoreSnapshotRequest.getSnapshotName(),
			restoreRequest.snapshot());
		Assert.assertEquals(
			restoreSnapshotRequest.isWaitForCompletion(),
			restoreRequest.waitForCompletion());
	}

	private OpenSearchFixture _openSearchFixture;

}