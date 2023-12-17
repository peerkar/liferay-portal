/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter.snapshot;

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.search.engine.adapter.snapshot.GetSnapshotRepositoriesRequest;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.search.opensearch2.internal.search.engine.adapter.index.AnalyzeIndexRequestExecutorTest;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import org.opensearch.client.opensearch.snapshot.GetRepositoryRequest;

/**
 * @author Michael C. Han
 */
public class GetSnapshotRepositoriesRequestExecutorImplTest {

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
	public void testGetSnapshotRepositoriesRequest() {
		GetSnapshotRepositoriesRequest getSnapshotRepositoriesRequest =
			new GetSnapshotRepositoriesRequest("repository1", "repository2");

		GetSnapshotRepositoriesRequestExecutorImpl
			getSnapshotRepositoriesRequestExecutorImpl =
				new GetSnapshotRepositoriesRequestExecutorImpl();

		ReflectionTestUtil.setFieldValue(
			getSnapshotRepositoriesRequestExecutorImpl,
			"_openSearchConnectionManager", _openSearchFixture);

		GetRepositoryRequest getRepositoriesRequest =
			getSnapshotRepositoriesRequestExecutorImpl.
				createGetRepositoryRequest(getSnapshotRepositoriesRequest);

		Assert.assertArrayEquals(
			getSnapshotRepositoriesRequest.getRepositoryNames(),
			ArrayUtil.toStringArray(getRepositoriesRequest.name()));
	}

	private OpenSearchFixture _openSearchFixture;

}