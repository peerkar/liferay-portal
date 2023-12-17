/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter.document;

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.search.engine.adapter.document.DocumentRequestExecutor;
import com.liferay.portal.search.internal.document.DocumentBuilderFactoryImpl;
import com.liferay.portal.search.internal.geolocation.GeoBuildersImpl;
import com.liferay.portal.search.internal.script.ScriptsImpl;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionManager;
import com.liferay.portal.search.opensearch2.internal.document.OpenSearchDocumentFactory;
import com.liferay.portal.search.opensearch2.internal.query.OpenSearchQueryTranslatorFixture;
import com.liferay.portal.search.script.Scripts;

/**
 * @author Dylan Rebelak
 */
public class DocumentRequestExecutorFixture {

	public DocumentRequestExecutor getDocumentRequestExecutor() {
		return _documentRequestExecutor;
	}

	public void setUp() {
		_documentRequestExecutor = _createDocumentRequestExecutor(
			_openSearchConnectionManager, _openSearchDocumentFactory);
	}

	protected void setOpenSearchConnectionManager(
		OpenSearchConnectionManager openSearchConnectionManager) {

		_openSearchConnectionManager = openSearchConnectionManager;
	}

	protected void setOpenSearchDocumentFactory(
		OpenSearchDocumentFactory openSearchDocumentFactory) {

		_openSearchDocumentFactory = openSearchDocumentFactory;
	}

	private OpenSearchBulkableDocumentRequestTranslator
		_createBulkableDocumentRequestTranslator(
			OpenSearchDocumentFactory openSearchDocumentFactory) {

		OpenSearchBulkableDocumentRequestTranslator
			openSearchBulkableDocumentRequestTranslator =
				new OpenSearchBulkableDocumentRequestTranslatorImpl();

		ReflectionTestUtil.setFieldValue(
			openSearchBulkableDocumentRequestTranslator,
			"_openSearchDocumentFactory", openSearchDocumentFactory);

		return openSearchBulkableDocumentRequestTranslator;
	}

	private BulkDocumentRequestExecutor _createBulkDocumentRequestExecutor(
		OpenSearchConnectionManager openSearchConnectionManager,
		OpenSearchBulkableDocumentRequestTranslator
			openSearchBulkableDocumentRequestTranslator) {

		BulkDocumentRequestExecutor bulkDocumentRequestExecutor =
			new BulkDocumentRequestExecutorImpl();

		ReflectionTestUtil.setFieldValue(
			bulkDocumentRequestExecutor,
			"_openSearchBulkableDocumentRequestTranslator",
			openSearchBulkableDocumentRequestTranslator);
		ReflectionTestUtil.setFieldValue(
			bulkDocumentRequestExecutor, "_openSearchConnectionManager",
			openSearchConnectionManager);

		return bulkDocumentRequestExecutor;
	}

	private DeleteByQueryDocumentRequestExecutor
		_createDeleteByQueryDocumentRequestExecutor(
			OpenSearchConnectionManager openSearchConnectionManager) {

		DeleteByQueryDocumentRequestExecutor
			deleteByQueryDocumentRequestExecutor =
				new DeleteByQueryDocumentRequestExecutorImpl();

		com.liferay.portal.search.opensearch2.internal.legacy.query.
			OpenSearchQueryTranslatorFixture
				legacyOpenSearchQueryTranslatorFixture =
					new com.liferay.portal.search.opensearch2.internal.legacy.
						query.OpenSearchQueryTranslatorFixture();

		OpenSearchQueryTranslatorFixture openSearchQueryTranslatorFixture =
			new OpenSearchQueryTranslatorFixture();

		ReflectionTestUtil.setFieldValue(
			deleteByQueryDocumentRequestExecutor,
			"_openSearchConnectionManager", openSearchConnectionManager);
		ReflectionTestUtil.setFieldValue(
			deleteByQueryDocumentRequestExecutor, "_legacyQueryTranslator",
			legacyOpenSearchQueryTranslatorFixture.
				getOpenSearchQueryTranslator());
		ReflectionTestUtil.setFieldValue(
			deleteByQueryDocumentRequestExecutor, "_queryTranslator",
			openSearchQueryTranslatorFixture.getOpenSearchQueryTranslator());

		return deleteByQueryDocumentRequestExecutor;
	}

	private DeleteDocumentRequestExecutor _createDeleteDocumentRequestExecutor(
		OpenSearchConnectionManager openSearchConnectionManager,
		OpenSearchBulkableDocumentRequestTranslator
			openSearchBulkableDocumentRequestTranslator) {

		DeleteDocumentRequestExecutor deleteDocumentRequestExecutor =
			new DeleteDocumentRequestExecutorImpl();

		ReflectionTestUtil.setFieldValue(
			deleteDocumentRequestExecutor,
			"_openSearchBulkableDocumentRequestTranslator",
			openSearchBulkableDocumentRequestTranslator);
		ReflectionTestUtil.setFieldValue(
			deleteDocumentRequestExecutor, "_openSearchConnectionManager",
			openSearchConnectionManager);

		return deleteDocumentRequestExecutor;
	}

	private DocumentRequestExecutor _createDocumentRequestExecutor(
		OpenSearchConnectionManager openSearchConnectionManager,
		OpenSearchDocumentFactory openSearchDocumentFactory) {

		OpenSearchBulkableDocumentRequestTranslator
			openSearchBulkableDocumentRequestTranslator =
				_createBulkableDocumentRequestTranslator(
					openSearchDocumentFactory);

		DocumentRequestExecutor documentRequestExecutor =
			new OpenSearchDocumentRequestExecutor();

		ReflectionTestUtil.setFieldValue(
			documentRequestExecutor, "_bulkDocumentRequestExecutor",
			_createBulkDocumentRequestExecutor(
				openSearchConnectionManager,
				openSearchBulkableDocumentRequestTranslator));
		ReflectionTestUtil.setFieldValue(
			documentRequestExecutor, "_deleteByQueryDocumentRequestExecutor",
			_createDeleteByQueryDocumentRequestExecutor(
				openSearchConnectionManager));
		ReflectionTestUtil.setFieldValue(
			documentRequestExecutor, "_deleteDocumentRequestExecutor",
			_createDeleteDocumentRequestExecutor(
				openSearchConnectionManager,
				openSearchBulkableDocumentRequestTranslator));
		ReflectionTestUtil.setFieldValue(
			documentRequestExecutor, "_getDocumentRequestExecutor",
			_createGetDocumentRequestExecutor(
				openSearchConnectionManager,
				openSearchBulkableDocumentRequestTranslator));
		ReflectionTestUtil.setFieldValue(
			documentRequestExecutor, "_indexDocumentRequestExecutor",
			_createIndexDocumentRequestExecutor(
				openSearchConnectionManager,
				openSearchBulkableDocumentRequestTranslator));
		ReflectionTestUtil.setFieldValue(
			documentRequestExecutor, "_updateByQueryDocumentRequestExecutor",
			_createUpdateByQueryDocumentRequestExecutor(
				openSearchConnectionManager));
		ReflectionTestUtil.setFieldValue(
			documentRequestExecutor, "_updateDocumentRequestExecutor",
			_createUpdateDocumentRequestExecutor(
				openSearchConnectionManager,
				openSearchBulkableDocumentRequestTranslator));

		return documentRequestExecutor;
	}

	private GetDocumentRequestExecutor _createGetDocumentRequestExecutor(
		OpenSearchConnectionManager openSearchConnectionManager,
		OpenSearchBulkableDocumentRequestTranslator
			openSearchBulkableDocumentRequestTranslator) {

		GetDocumentRequestExecutor getDocumentRequestExecutor =
			new GetDocumentRequestExecutorImpl();

		ReflectionTestUtil.setFieldValue(
			getDocumentRequestExecutor,
			"_openSearchBulkableDocumentRequestTranslator",
			openSearchBulkableDocumentRequestTranslator);
		ReflectionTestUtil.setFieldValue(
			getDocumentRequestExecutor, "_openSearchConnectionManager",
			openSearchConnectionManager);
		ReflectionTestUtil.setFieldValue(
			getDocumentRequestExecutor, "_documentBuilderFactory",
			new DocumentBuilderFactoryImpl());
		ReflectionTestUtil.setFieldValue(
			getDocumentRequestExecutor, "_geoBuilders", new GeoBuildersImpl());

		return getDocumentRequestExecutor;
	}

	private IndexDocumentRequestExecutor _createIndexDocumentRequestExecutor(
		OpenSearchConnectionManager openSearchConnectionManager,
		OpenSearchBulkableDocumentRequestTranslator
			openSearchBulkableDocumentRequestTranslator) {

		IndexDocumentRequestExecutor indexDocumentRequestExecutor =
			new IndexDocumentRequestExecutorImpl();

		ReflectionTestUtil.setFieldValue(
			indexDocumentRequestExecutor,
			"_openSearchBulkableDocumentRequestTranslator",
			openSearchBulkableDocumentRequestTranslator);
		ReflectionTestUtil.setFieldValue(
			indexDocumentRequestExecutor, "_openSearchConnectionManager",
			openSearchConnectionManager);

		return indexDocumentRequestExecutor;
	}

	private UpdateByQueryDocumentRequestExecutor
		_createUpdateByQueryDocumentRequestExecutor(
			OpenSearchConnectionManager openSearchConnectionManager) {

		UpdateByQueryDocumentRequestExecutor
			updateByQueryDocumentRequestExecutor =
				new UpdateByQueryDocumentRequestExecutorImpl();

		com.liferay.portal.search.opensearch2.internal.legacy.query.
			OpenSearchQueryTranslatorFixture
				lecacyOpenSearchQueryTranslatorFixture =
					new com.liferay.portal.search.opensearch2.internal.legacy.
						query.OpenSearchQueryTranslatorFixture();

		OpenSearchQueryTranslatorFixture openSearchQueryTranslatorFixture =
			new OpenSearchQueryTranslatorFixture();

		ReflectionTestUtil.setFieldValue(
			updateByQueryDocumentRequestExecutor,
			"_openSearchConnectionManager", openSearchConnectionManager);
		ReflectionTestUtil.setFieldValue(
			updateByQueryDocumentRequestExecutor, "_legacyQueryTranslator",
			lecacyOpenSearchQueryTranslatorFixture.
				getOpenSearchQueryTranslator());
		ReflectionTestUtil.setFieldValue(
			updateByQueryDocumentRequestExecutor, "_queryTranslator",
			openSearchQueryTranslatorFixture.getOpenSearchQueryTranslator());
		ReflectionTestUtil.setFieldValue(
			updateByQueryDocumentRequestExecutor, "_scripts", _scripts);

		return updateByQueryDocumentRequestExecutor;
	}

	private UpdateDocumentRequestExecutor _createUpdateDocumentRequestExecutor(
		OpenSearchConnectionManager openSearchConnectionManager,
		OpenSearchBulkableDocumentRequestTranslator
			openSearchBulkableDocumentRequestTranslator) {

		UpdateDocumentRequestExecutor updateDocumentRequestExecutor =
			new UpdateDocumentRequestExecutorImpl();

		ReflectionTestUtil.setFieldValue(
			updateDocumentRequestExecutor,
			"_openSearchBulkableDocumentRequestTranslator",
			openSearchBulkableDocumentRequestTranslator);
		ReflectionTestUtil.setFieldValue(
			updateDocumentRequestExecutor, "_openSearchConnectionManager",
			openSearchConnectionManager);

		return updateDocumentRequestExecutor;
	}

	private static final Scripts _scripts = new ScriptsImpl();

	private DocumentRequestExecutor _documentRequestExecutor;
	private OpenSearchConnectionManager _openSearchConnectionManager;
	private OpenSearchDocumentFactory _openSearchDocumentFactory;

}