/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter.index;

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.search.engine.adapter.index.CloseIndexRequest;
import com.liferay.portal.search.engine.adapter.index.IndicesOptions;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import org.opensearch.client.opensearch._types.Time;
import org.opensearch.client.opensearch._types.TimeUnit;

/**
 * @author Michael C. Han
 */
public class CloseIndexRequestExecutorTest {

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
		CloseIndexRequest closeIndexRequest = new CloseIndexRequest(
			_INDEX_NAME);

		IndicesOptions indicesOptions = new IndicesOptions();

		indicesOptions.setIgnoreUnavailable(true);

		closeIndexRequest.setIndicesOptions(indicesOptions);

		closeIndexRequest.setTimeout(100);

		CloseIndexRequestExecutorImpl closeIndexRequestExecutorImpl =
			new CloseIndexRequestExecutorImpl();

		ReflectionTestUtil.setFieldValue(
			closeIndexRequestExecutorImpl, "_openSearchConnectionManager",
			_openSearchFixture);

		org.opensearch.client.opensearch.indices.CloseIndexRequest
			openSearchCloseIndexRequest =
				closeIndexRequestExecutorImpl.createCloseIndexRequest(
					closeIndexRequest);

		Assert.assertArrayEquals(
			closeIndexRequest.getIndexNames(),
			ArrayUtil.toStringArray(openSearchCloseIndexRequest.index()));

		Assert.assertEquals(
			Time.of(
				time -> time.time(
					closeIndexRequest.getTimeout() +
						TimeUnit.Milliseconds.jsonValue())),
			openSearchCloseIndexRequest.masterTimeout());

		Assert.assertEquals(
			Time.of(
				time -> time.time(
					closeIndexRequest.getTimeout() +
						TimeUnit.Milliseconds.jsonValue())),
			openSearchCloseIndexRequest.timeout());
	}

	private static final String _INDEX_NAME = "test_request_index";

	private OpenSearchFixture _openSearchFixture;

}