/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter.index;

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.search.engine.adapter.index.GetMappingIndexRequest;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import org.opensearch.client.opensearch.indices.GetMappingRequest;

/**
 * @author Dylan Rebelak
 */
public class GetMappingIndexRequestExecutorTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		_openSearchFixture = new OpenSearchFixture();

		_openSearchFixture.setUp();
	}

	@After
	public void tearDown() throws Exception {
		_openSearchFixture.tearDown();
	}

	@Test
	public void testIndexRequestTranslation() {
		GetMappingIndexRequest getMappingIndexRequest =
			new GetMappingIndexRequest(
				new String[] {_INDEX_NAME}, _MAPPING_NAME);

		GetMappingIndexRequestExecutorImpl getMappingIndexRequestExecutorImpl =
			new GetMappingIndexRequestExecutorImpl();

		ReflectionTestUtil.setFieldValue(
			getMappingIndexRequestExecutorImpl, "_openSearchConnectionManager",
			_openSearchFixture);

		GetMappingRequest getMappingsRequest =
			getMappingIndexRequestExecutorImpl.createGetMappingRequest(
				getMappingIndexRequest);

		Assert.assertArrayEquals(
			new String[] {_INDEX_NAME},
			ArrayUtil.toStringArray(getMappingsRequest.index()));
	}

	private static final String _INDEX_NAME = "test_request_index";

	private static final String _MAPPING_NAME = "testMapping";

	private OpenSearchFixture _openSearchFixture;

}