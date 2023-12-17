/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter.document;

import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.search.engine.adapter.document.UpdateByQueryDocumentRequest;
import com.liferay.portal.search.internal.script.ScriptsImpl;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.search.opensearch2.internal.query.OpenSearchQueryTranslatorFixture;
import com.liferay.portal.search.opensearch2.internal.script.ScriptTranslator;
import com.liferay.portal.search.opensearch2.internal.util.JsonpUtil;
import com.liferay.portal.search.script.Scripts;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import org.opensearch.client.opensearch.core.UpdateByQueryRequest;

/**
 * @author Dylan Rebelak
 */
public class UpdateByQueryDocumentRequestExecutorTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		_openSearchFixture = new OpenSearchFixture(
			UpdateByQueryDocumentRequestExecutorTest.class.getSimpleName());

		_openSearchFixture.setUp();
	}

	@After
	public void tearDown() throws Exception {
		_openSearchFixture.tearDown();
	}

	@Test
	public void testDocumentRequestTranslationWithNoRefresh() {
		doTestDocumentRequestTranslation(false);
	}

	@Test
	public void testDocumentRequestTranslationWithRefresh() {
		doTestDocumentRequestTranslation(true);
	}

	protected void doTestDocumentRequestTranslation(boolean refresh) {
		BooleanQuery booleanQuery = new BooleanQueryImpl();

		booleanQuery.addExactTerm(_FIELD_NAME, true);

		UpdateByQueryDocumentRequest updateByQueryDocumentRequest =
			new UpdateByQueryDocumentRequest(
				booleanQuery, null, new String[] {_INDEX_NAME});

		updateByQueryDocumentRequest.setRefresh(refresh);

		UpdateByQueryDocumentRequestExecutorImpl
			updateByQueryDocumentRequestExecutorImpl =
				new UpdateByQueryDocumentRequestExecutorImpl();

		com.liferay.portal.search.opensearch2.internal.legacy.query.
			OpenSearchQueryTranslatorFixture
				lecacyOpenSearchQueryTranslatorFixture =
					new com.liferay.portal.search.opensearch2.internal.legacy.
						query.OpenSearchQueryTranslatorFixture();

		ReflectionTestUtil.setFieldValue(
			updateByQueryDocumentRequestExecutorImpl,
			"_openSearchConnectionManager", _openSearchFixture);
		ReflectionTestUtil.setFieldValue(
			updateByQueryDocumentRequestExecutorImpl, "_legacyQueryTranslator",
			lecacyOpenSearchQueryTranslatorFixture.
				getOpenSearchQueryTranslator());

		OpenSearchQueryTranslatorFixture openSearchQueryTranslatorFixture =
			new OpenSearchQueryTranslatorFixture();

		ReflectionTestUtil.setFieldValue(
			updateByQueryDocumentRequestExecutorImpl, "_queryTranslator",
			openSearchQueryTranslatorFixture.getOpenSearchQueryTranslator());
		ReflectionTestUtil.setFieldValue(
			updateByQueryDocumentRequestExecutorImpl, "_scripts", _scripts);
		ReflectionTestUtil.setFieldValue(
			updateByQueryDocumentRequestExecutorImpl, "_scriptTranslator",
			new ScriptTranslator());

		UpdateByQueryRequest updateByQueryRequest =
			updateByQueryDocumentRequestExecutorImpl.createUpdateByQueryRequest(
				updateByQueryDocumentRequest);

		Assert.assertArrayEquals(
			new String[] {_INDEX_NAME},
			ArrayUtil.toStringArray(updateByQueryRequest.index()));

		Assert.assertEquals(
			updateByQueryDocumentRequest.isRefresh(),
			updateByQueryRequest.refresh());

		String queryString = String.valueOf(
			JsonpUtil.toString(updateByQueryRequest));

		Assert.assertTrue(queryString.contains(_FIELD_NAME));
		Assert.assertTrue(queryString.contains("\"value\":\"true\""));
	}

	private static final String _FIELD_NAME = "testField";

	private static final String _INDEX_NAME = "test_request_index";

	private static final Scripts _scripts = new ScriptsImpl();

	private OpenSearchFixture _openSearchFixture;

}