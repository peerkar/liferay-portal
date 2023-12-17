/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter.cluster;

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.search.engine.adapter.cluster.ClusterHealthStatus;
import com.liferay.portal.search.engine.adapter.cluster.HealthClusterRequest;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import org.opensearch.client.opensearch._types.Time;
import org.opensearch.client.opensearch._types.TimeUnit;
import org.opensearch.client.opensearch.cluster.HealthRequest;

/**
 * @author Dylan Rebelak
 */
public class HealthClusterRequestExecutorTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		_openSearchFixture = new OpenSearchFixture(
			HealthClusterRequestExecutorTest.class.getSimpleName());

		_openSearchFixture.setUp();
	}

	@After
	public void tearDown() throws Exception {
		_openSearchFixture.tearDown();
	}

	@Test
	public void testClusterRequestTranslation() {
		HealthClusterRequest healthClusterRequest = new HealthClusterRequest(
			_INDEX_NAME);

		healthClusterRequest.setTimeout(1000);
		healthClusterRequest.setWaitForClusterHealthStatus(
			ClusterHealthStatus.GREEN);

		HealthClusterRequestExecutorImpl healthClusterRequestExecutorImpl =
			new HealthClusterRequestExecutorImpl();

		ReflectionTestUtil.setFieldValue(
			healthClusterRequestExecutorImpl, "_clusterHealthStatusTranslator",
			new ClusterHealthStatusTranslatorImpl());
		ReflectionTestUtil.setFieldValue(
			healthClusterRequestExecutorImpl, "_openSearchConnectionManager",
			_openSearchFixture);

		HealthRequest healthRequest =
			healthClusterRequestExecutorImpl.createHealthRequest(
				healthClusterRequest);

		Assert.assertArrayEquals(
			new String[] {_INDEX_NAME},
			ArrayUtil.toStringArray(healthRequest.index()));

		ClusterHealthStatusTranslator clusterHealthStatusTranslator =
			new ClusterHealthStatusTranslatorImpl();

		Assert.assertEquals(
			healthClusterRequest.getWaitForClusterHealthStatus(),
			clusterHealthStatusTranslator.translate(
				healthRequest.waitForStatus()));

		Time expectedTimeout = Time.of(
			time -> time.time("1000" + TimeUnit.Milliseconds));

		Time timeout = healthRequest.timeout();

		Assert.assertEquals(expectedTimeout, timeout.time());

		Assert.assertEquals(expectedTimeout, healthRequest.masterTimeout());
	}

	private static final String _INDEX_NAME = "test_request_index";

	private OpenSearchFixture _openSearchFixture;

}