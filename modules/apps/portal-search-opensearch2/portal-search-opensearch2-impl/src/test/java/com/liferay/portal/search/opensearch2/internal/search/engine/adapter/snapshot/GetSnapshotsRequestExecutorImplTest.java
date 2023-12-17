/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter.snapshot;

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.search.engine.adapter.snapshot.GetSnapshotsRequest;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.search.opensearch2.internal.search.engine.adapter.index.AnalyzeIndexRequestExecutorTest;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import org.opensearch.client.opensearch.snapshot.GetSnapshotRequest;

/**
 * @author Michael C. Han
 */
public class GetSnapshotsRequestExecutorImplTest {

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
	public void testCreateGetSnapshotsRequest() {
		GetSnapshotsRequest getSnapshotsRequest = new GetSnapshotsRequest(
			"repository1");

		getSnapshotsRequest.setIgnoreUnavailable(true);
		getSnapshotsRequest.setSnapshotNames("snapshot1", "snapshot2");
		getSnapshotsRequest.setVerbose(true);

		GetSnapshotsRequestExecutorImpl getSnapshotsRequestExecutorImpl =
			new GetSnapshotsRequestExecutorImpl();

		ReflectionTestUtil.setFieldValue(
			getSnapshotsRequestExecutorImpl, "_openSearchConnectionManager",
			_openSearchFixture);

		GetSnapshotRequest openSearchGetSnapshotsRequest =
			getSnapshotsRequestExecutorImpl.createGetSnapshotRequest(
				getSnapshotsRequest);

		Assert.assertEquals(
			getSnapshotsRequest.isIgnoreUnavailable(),
			openSearchGetSnapshotsRequest.ignoreUnavailable());
		Assert.assertEquals(
			getSnapshotsRequest.getRepositoryName(),
			openSearchGetSnapshotsRequest.repository());
		Assert.assertArrayEquals(
			getSnapshotsRequest.getSnapshotNames(),
			ArrayUtil.toStringArray(openSearchGetSnapshotsRequest.snapshot()));
		Assert.assertEquals(
			getSnapshotsRequest.isVerbose(),
			openSearchGetSnapshotsRequest.verbose());
	}

	private OpenSearchFixture _openSearchFixture;

}