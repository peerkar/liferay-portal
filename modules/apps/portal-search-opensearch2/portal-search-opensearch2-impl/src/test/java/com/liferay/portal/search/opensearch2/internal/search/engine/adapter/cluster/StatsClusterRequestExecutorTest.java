/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter.cluster;

import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.search.engine.adapter.cluster.StatsClusterRequest;
import com.liferay.portal.search.engine.adapter.cluster.StatsClusterResponse;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionFixture;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Dylan Rebelak
 */
public class StatsClusterRequestExecutorTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		OpenSearchConnectionFixture openSearchConnectionFixture =
			OpenSearchConnectionFixture.builder(
			).clusterName(
				StatsClusterRequestExecutorTest.class.getSimpleName()
			).build();

		openSearchConnectionFixture.createNode();

		_openSearchConnectionFixture = openSearchConnectionFixture;
	}

	@After
	public void tearDown() throws Exception {
		_openSearchConnectionFixture.destroyNode();
	}

	@Test
	public void testClusterRequestExecution() {
		StatsClusterRequest statsClusterRequest = new StatsClusterRequest(
			new String[] {_NODE_ID});

		StatsClusterRequestExecutorImpl statsClusterRequestExecutorImpl =
			new StatsClusterRequestExecutorImpl();

		ReflectionTestUtil.setFieldValue(
			statsClusterRequestExecutorImpl, "_clusterHealthStatusTranslator",
			new ClusterHealthStatusTranslatorImpl());
		ReflectionTestUtil.setFieldValue(
			statsClusterRequestExecutorImpl, "_openSearchConnectionManager",
			_openSearchConnectionFixture);
		ReflectionTestUtil.setFieldValue(
			statsClusterRequestExecutorImpl, "_jsonFactory",
			new JSONFactoryImpl());

		StatsClusterResponse statsClusterResponse =
			statsClusterRequestExecutorImpl.execute(statsClusterRequest);

		Assert.assertNotNull(statsClusterResponse);

		Assert.assertNotNull(statsClusterResponse.getClusterHealthStatus());
	}

	private static final String _NODE_ID = "liferay";

	private OpenSearchConnectionFixture _openSearchConnectionFixture;

}