/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter.index;

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.search.engine.adapter.index.DeleteIndexRequest;
import com.liferay.portal.search.engine.adapter.index.IndicesOptions;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Michael C. Han
 */
public class DeleteIndexRequestExecutorTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		_openSearchFixture = new OpenSearchFixture(
			DeleteIndexRequestExecutorTest.class.getSimpleName());

		_openSearchFixture.setUp();
	}

	@After
	public void tearDown() throws Exception {
		_openSearchFixture.tearDown();
	}

	@Test
	public void testIndexRequestTranslation() {
		DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(
			_INDEX_NAME_1, _INDEX_NAME_2);

		IndicesOptions indicesOptions = new IndicesOptions();

		indicesOptions.setAllowNoIndices(true);
		indicesOptions.setExpandToClosedIndices(false);
		indicesOptions.setExpandToOpenIndices(false);
		indicesOptions.setIgnoreUnavailable(true);

		deleteIndexRequest.setIndicesOptions(indicesOptions);

		DeleteIndexRequestExecutorImpl deleteIndexRequestExecutorImpl =
			new DeleteIndexRequestExecutorImpl();

		ReflectionTestUtil.setFieldValue(
			deleteIndexRequestExecutorImpl, "_openSearchConnectionManager",
			_openSearchFixture);

		org.opensearch.client.opensearch.indices.DeleteIndexRequest
			openSearchDeleteIndexRequest =
				deleteIndexRequestExecutorImpl.createDeleteIndexRequest(
					deleteIndexRequest);

		List<String> indices = openSearchDeleteIndexRequest.index();

		Assert.assertEquals(String.join(", ", indices), 2, indices.size());
		Assert.assertEquals(_INDEX_NAME_1, indices.get(0));
		Assert.assertEquals(_INDEX_NAME_2, indices.get(1));

		Assert.assertEquals(
			indicesOptions.isAllowNoIndices(),
			openSearchDeleteIndexRequest.allowNoIndices());
		Assert.assertEquals(
			indicesOptions.isExpandToClosedIndices(),
			openSearchDeleteIndexRequest.expandWildcards());
		Assert.assertEquals(
			indicesOptions.isIgnoreUnavailable(),
			openSearchDeleteIndexRequest.ignoreUnavailable());
		Assert.assertEquals(
			indicesOptions.isExpandToOpenIndices(),
			openSearchDeleteIndexRequest.expandWildcards());
	}

	private static final String _INDEX_NAME_1 = "test_request_index1";

	private static final String _INDEX_NAME_2 = "test_request_index2";

	private OpenSearchFixture _openSearchFixture;

}