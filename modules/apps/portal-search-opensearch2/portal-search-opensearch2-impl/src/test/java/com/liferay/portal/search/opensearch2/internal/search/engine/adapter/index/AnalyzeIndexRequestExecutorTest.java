/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter.index;

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.search.engine.adapter.index.AnalyzeIndexRequest;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import org.opensearch.client.opensearch.indices.AnalyzeRequest;

/**
 * @author Michael C. Han
 */
public class AnalyzeIndexRequestExecutorTest {

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
	public void testIndexRequestTranslation() {
		AnalyzeIndexRequest analyzeIndexRequest = new AnalyzeIndexRequest();

		analyzeIndexRequest.setIndexName(_INDEX_NAME);

		AnalyzeIndexRequestExecutorImpl analyzeIndexRequestExecutorImpl =
			new AnalyzeIndexRequestExecutorImpl();

		ReflectionTestUtil.setFieldValue(
			analyzeIndexRequestExecutorImpl, "_openSearchConnectionManager",
			_openSearchFixture);

		AnalyzeRequest analyzeRequest =
			analyzeIndexRequestExecutorImpl.createAnalyzeRequest(
				analyzeIndexRequest);

		Assert.assertEquals(_INDEX_NAME, analyzeRequest.index());
	}

	private static final String _INDEX_NAME = "test_request_index";

	private OpenSearchFixture _openSearchFixture;

}