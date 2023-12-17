/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter.index;

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.search.engine.adapter.index.GetIndexIndexRequest;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import org.opensearch.client.opensearch.indices.GetIndexRequest;

/**
 * @author Michael C. Han
 */
public class GetIndexIndexRequestExecutorTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		_openSearchFixture = new OpenSearchFixture(
			GetIndexIndexRequestExecutorTest.class.getSimpleName());

		_openSearchFixture.setUp();
	}

	@After
	public void tearDown() throws Exception {
		_openSearchFixture.tearDown();
	}

	@Test
	public void testIndexRequestTranslation() {
		GetIndexIndexRequest getIndexIndexRequest = new GetIndexIndexRequest(
			_INDEX_NAME);

		GetIndexIndexRequestExecutorImpl getIndexIndexRequestExecutorImpl =
			new GetIndexIndexRequestExecutorImpl();

		ReflectionTestUtil.setFieldValue(
			getIndexIndexRequestExecutorImpl, "_openSearchConnectionManager",
			_openSearchFixture);

		GetIndexRequest getIndexRequest =
			getIndexIndexRequestExecutorImpl.createGetIndexRequest(
				getIndexIndexRequest);

		List<String> indices = getIndexRequest.index();

		Assert.assertEquals(String.join(", ", indices), 1, indices.size());
		Assert.assertEquals(_INDEX_NAME, indices.get(0));
	}

	private static final String _INDEX_NAME = "test_request_index";

	private OpenSearchFixture _openSearchFixture;

}