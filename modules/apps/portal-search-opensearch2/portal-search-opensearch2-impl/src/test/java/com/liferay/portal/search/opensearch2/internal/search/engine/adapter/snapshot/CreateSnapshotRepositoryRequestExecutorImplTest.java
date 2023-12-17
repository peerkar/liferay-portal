/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter.snapshot;

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.search.engine.adapter.snapshot.CreateSnapshotRepositoryRequest;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.search.opensearch2.internal.search.engine.adapter.index.AnalyzeIndexRequestExecutorTest;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import org.opensearch.client.opensearch.snapshot.CreateRepositoryRequest;
import org.opensearch.client.opensearch.snapshot.RepositorySettings;

/**
 * @author Michael C. Han
 */
public class CreateSnapshotRepositoryRequestExecutorImplTest {

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
	public void testCreatePutRepositoryRequest() {
		CreateSnapshotRepositoryRequest createSnapshotRepositoryRequest =
			new CreateSnapshotRepositoryRequest("name", "location");

		createSnapshotRepositoryRequest.setCompress(true);
		createSnapshotRepositoryRequest.setType("type");
		createSnapshotRepositoryRequest.setVerify(true);

		CreateSnapshotRepositoryRequestExecutorImpl
			createSnapshotRepositoryRequestExecutorImpl =
				new CreateSnapshotRepositoryRequestExecutorImpl();

		ReflectionTestUtil.setFieldValue(
			createSnapshotRepositoryRequestExecutorImpl,
			"_openSearchConnectionManager", _openSearchFixture);

		CreateRepositoryRequest createRepositoryRequest =
			createSnapshotRepositoryRequestExecutorImpl.
				createCreateRepositoryRequest(createSnapshotRepositoryRequest);

		RepositorySettings repositorySettings =
			createRepositoryRequest.settings();

		Assert.assertEquals(
			String.valueOf(createSnapshotRepositoryRequest.isCompress()),
			repositorySettings.compress());
		Assert.assertEquals(
			String.valueOf(createSnapshotRepositoryRequest.getLocation()),
			repositorySettings.location());

		Assert.assertEquals(
			createSnapshotRepositoryRequest.getName(),
			createRepositoryRequest.name());
		Assert.assertEquals(
			createSnapshotRepositoryRequest.getType(),
			createRepositoryRequest.type());
		Assert.assertEquals(
			createSnapshotRepositoryRequest.isVerify(),
			createRepositoryRequest.verify());
	}

	private OpenSearchFixture _openSearchFixture;

}