/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter.document;

import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.search.engine.adapter.document.BulkDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.DeleteDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.IndexDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.UpdateDocumentRequest;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.search.opensearch2.internal.document.OpenSearchDocumentFactoryImpl;
import com.liferay.portal.search.test.util.indexing.DocumentFixture;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import org.opensearch.client.opensearch.core.BulkRequest;
import org.opensearch.client.opensearch.core.bulk.BulkOperation;

/**
 * @author Michael C. Han
 */
public class BulkDocumentRequestExecutorTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		OpenSearchFixture openSearchFixture = new OpenSearchFixture(getClass());

		OpenSearchBulkableDocumentRequestTranslator
			openSearchBulkableDocumentRequestTranslator =
				new OpenSearchBulkableDocumentRequestTranslatorImpl();

		ReflectionTestUtil.setFieldValue(
			openSearchBulkableDocumentRequestTranslator,
			"_openSearchDocumentFactory", new OpenSearchDocumentFactoryImpl());

		_bulkDocumentRequestExecutorImpl =
			new BulkDocumentRequestExecutorImpl();

		ReflectionTestUtil.setFieldValue(
			_bulkDocumentRequestExecutorImpl,
			"_openSearchBulkableDocumentRequestTranslator",
			openSearchBulkableDocumentRequestTranslator);
		ReflectionTestUtil.setFieldValue(
			_bulkDocumentRequestExecutorImpl, "_openSearchConnectionManager",
			openSearchFixture);

		_openSearchFixture = openSearchFixture;

		_documentFixture.setUp();
		_openSearchFixture.setUp();
	}

	@After
	public void tearDown() throws Exception {
		_documentFixture.tearDown();

		_openSearchFixture.tearDown();
	}

	@Test
	public void testBulkDocumentRequestTranslation() {
		String uid = "1";

		Document document = new DocumentImpl();

		document.addKeyword(Field.TYPE, _MAPPING_NAME);
		document.addKeyword(Field.UID, uid);
		document.addKeyword("staging", "true");

		IndexDocumentRequest indexDocumentRequest = new IndexDocumentRequest(
			_INDEX_NAME, document);

		BulkDocumentRequest bulkDocumentRequest = new BulkDocumentRequest();

		bulkDocumentRequest.addBulkableDocumentRequest(indexDocumentRequest);

		DeleteDocumentRequest deleteDocumentRequest = new DeleteDocumentRequest(
			_INDEX_NAME, uid);

		bulkDocumentRequest.addBulkableDocumentRequest(deleteDocumentRequest);

		Document updatedDocument = new DocumentImpl();

		updatedDocument.addKeyword(Field.UID, uid);
		updatedDocument.addKeyword("staging", "false");

		UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest(
			_INDEX_NAME, uid, updatedDocument);

		bulkDocumentRequest.addBulkableDocumentRequest(updateDocumentRequest);

		BulkRequest bulkRequest =
			_bulkDocumentRequestExecutorImpl.createBulkRequest(
				bulkDocumentRequest);

		List<BulkOperation> bulkOperations = bulkRequest.operations();

		Assert.assertEquals(3, bulkOperations.size());
	}

	private static final String _INDEX_NAME = "test_request_index";

	private static final String _MAPPING_NAME = "testMapping";

	private BulkDocumentRequestExecutorImpl _bulkDocumentRequestExecutorImpl;
	private final DocumentFixture _documentFixture = new DocumentFixture();
	private OpenSearchFixture _openSearchFixture;

}