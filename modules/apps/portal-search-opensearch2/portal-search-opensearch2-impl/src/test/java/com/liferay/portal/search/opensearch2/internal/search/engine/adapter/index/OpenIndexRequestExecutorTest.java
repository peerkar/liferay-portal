/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter.index;

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.search.engine.adapter.index.IndicesOptions;
import com.liferay.portal.search.engine.adapter.index.OpenIndexRequest;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import org.opensearch.client.opensearch._types.Time;
import org.opensearch.client.opensearch._types.TimeUnit;
import org.opensearch.client.opensearch._types.WaitForActiveShards;
import org.opensearch.client.opensearch.indices.OpenRequest;

/**
 * @author Michael C. Han
 */
public class OpenIndexRequestExecutorTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		_openSearchFixture = new OpenSearchFixture(
			CreateIndexRequestExecutorTest.class.getSimpleName());

		_openSearchFixture.setUp();
	}

	@After
	public void tearDown() throws Exception {
		_openSearchFixture.tearDown();
	}

	@Test
	public void testIndexRequestTranslation() {
		OpenIndexRequest openIndexRequest = new OpenIndexRequest(_INDEX_NAME);

		IndicesOptions indicesOptions = new IndicesOptions();

		indicesOptions.setIgnoreUnavailable(true);

		openIndexRequest.setIndicesOptions(indicesOptions);
		openIndexRequest.setTimeout(100);
		openIndexRequest.setWaitForActiveShards(200);

		OpenIndexRequestExecutorImpl openIndexRequestExecutorImpl =
			new OpenIndexRequestExecutorImpl();

		ReflectionTestUtil.setFieldValue(
			openIndexRequestExecutorImpl, "_openSearchConnectionManager",
			_openSearchFixture);

		OpenRequest openRequest =
			openIndexRequestExecutorImpl.createOpenRequest(openIndexRequest);

		Assert.assertArrayEquals(
			openIndexRequest.getIndexNames(),
			ArrayUtil.toStringArray(openRequest.index()));

		Time masterTimeout = openRequest.masterTimeout();

		Assert.assertEquals(
			Time.of(
				time -> time.time(
					openIndexRequest.getTimeout() +
						TimeUnit.Milliseconds.jsonValue())),
			masterTimeout.time());

		Time timeout = openRequest.timeout();

		Assert.assertEquals(
			Time.of(
				time -> time.time(
					openIndexRequest.getTimeout() +
						TimeUnit.Milliseconds.jsonValue())),
			timeout.time());

		WaitForActiveShards waitForActiveShards =
			openRequest.waitForActiveShards();

		Integer count = waitForActiveShards.count();

		Assert.assertEquals(
			openIndexRequest.getWaitForActiveShards(), count.intValue());
	}

	private static final String _INDEX_NAME = "test_request_index";

	private OpenSearchFixture _openSearchFixture;

}