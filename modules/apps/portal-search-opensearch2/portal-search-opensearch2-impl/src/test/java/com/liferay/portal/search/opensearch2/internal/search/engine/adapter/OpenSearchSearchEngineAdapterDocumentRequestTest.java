/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.document.BulkDocumentItemResponse;
import com.liferay.portal.search.engine.adapter.document.BulkDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.BulkDocumentResponse;
import com.liferay.portal.search.engine.adapter.document.DeleteByQueryDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.DeleteByQueryDocumentResponse;
import com.liferay.portal.search.engine.adapter.document.DeleteDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.DeleteDocumentResponse;
import com.liferay.portal.search.engine.adapter.document.DocumentRequestExecutor;
import com.liferay.portal.search.engine.adapter.document.IndexDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.IndexDocumentResponse;
import com.liferay.portal.search.engine.adapter.document.UpdateByQueryDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.UpdateByQueryDocumentResponse;
import com.liferay.portal.search.engine.adapter.document.UpdateDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.UpdateDocumentResponse;
import com.liferay.portal.search.internal.script.ScriptsImpl;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionManager;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.search.opensearch2.internal.document.OpenSearchDocumentFactory;
import com.liferay.portal.search.opensearch2.internal.document.OpenSearchDocumentFactoryImpl;
import com.liferay.portal.search.opensearch2.internal.search.engine.adapter.document.DocumentRequestExecutorFixture;
import com.liferay.portal.search.opensearch2.internal.util.MappingsUtil;
import com.liferay.portal.search.script.Script;
import com.liferay.portal.search.script.Scripts;
import com.liferay.portal.search.test.util.indexing.DocumentFixture;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.io.IOException;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import org.opensearch.client.json.JsonData;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.Refresh;
import org.opensearch.client.opensearch._types.Result;
import org.opensearch.client.opensearch._types.mapping.TypeMapping;
import org.opensearch.client.opensearch.core.GetRequest;
import org.opensearch.client.opensearch.core.GetResponse;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.indices.CreateIndexRequest;
import org.opensearch.client.opensearch.indices.DeleteIndexRequest;
import org.opensearch.client.opensearch.indices.OpenSearchIndicesClient;

/**
 * @author Dylan Rebelak
 */
public class OpenSearchSearchEngineAdapterDocumentRequestTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() throws Exception {
		_openSearchFixture = new OpenSearchFixture(
			OpenSearchSearchEngineAdapterDocumentRequestTest.class);

		_openSearchFixture.setUp();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		_openSearchFixture.tearDown();
	}

	@Before
	public void setUp() throws Exception {
		_searchEngineAdapter = createSearchEngineAdapter(_openSearchFixture);

		_openSearchClient = _openSearchFixture.getOpenSearchClient();

		_openSearchIndicesClient = _openSearchClient.indices();

		_documentFixture.setUp();

		_createIndex();
	}

	@After
	public void tearDown() throws Exception {
		_deleteIndex();

		_documentFixture.tearDown();
	}

	@Test
	public void testExecuteBulkDocumentRequest() {
		Document document1 = new DocumentImpl();

		document1.addKeyword(Field.UID, "1");
		document1.addKeyword(_FIELD_NAME, Boolean.TRUE.toString());

		IndexDocumentRequest indexDocumentRequest1 = new IndexDocumentRequest(
			_INDEX_NAME, document1);

		indexDocumentRequest1.setType(_MAPPING_NAME);

		BulkDocumentRequest bulkDocumentRequest1 = new BulkDocumentRequest();

		bulkDocumentRequest1.addBulkableDocumentRequest(indexDocumentRequest1);

		Document document2 = new DocumentImpl();

		document2.addKeyword(Field.UID, "2");
		document2.addKeyword(_FIELD_NAME, Boolean.FALSE.toString());

		IndexDocumentRequest indexDocumentRequest2 = new IndexDocumentRequest(
			_INDEX_NAME, document2);

		indexDocumentRequest2.setType(_MAPPING_NAME);

		bulkDocumentRequest1.addBulkableDocumentRequest(indexDocumentRequest2);

		BulkDocumentResponse bulkDocumentResponse1 =
			_searchEngineAdapter.execute(bulkDocumentRequest1);

		Assert.assertFalse(bulkDocumentResponse1.hasErrors());

		List<BulkDocumentItemResponse> bulkDocumentItemResponses1 =
			bulkDocumentResponse1.getBulkDocumentItemResponses();

		Assert.assertEquals(
			bulkDocumentItemResponses1.toString(), 2,
			bulkDocumentItemResponses1.size());

		BulkDocumentItemResponse bulkDocumentItemResponse1 =
			bulkDocumentItemResponses1.get(0);

		Assert.assertEquals("1", bulkDocumentItemResponse1.getId());

		BulkDocumentItemResponse bulkDocumentItemResponse2 =
			bulkDocumentItemResponses1.get(1);

		Assert.assertEquals("2", bulkDocumentItemResponse2.getId());

		DeleteDocumentRequest deleteDocumentRequest = new DeleteDocumentRequest(
			_INDEX_NAME, "1");

		deleteDocumentRequest.setType(_MAPPING_NAME);

		BulkDocumentRequest bulkDocumentRequest2 = new BulkDocumentRequest();

		bulkDocumentRequest2.addBulkableDocumentRequest(deleteDocumentRequest);

		Document document2Update = new DocumentImpl();

		document2Update.addKeyword(Field.UID, "2");
		document2Update.addKeyword(_FIELD_NAME, Boolean.TRUE.toString());

		UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest(
			_INDEX_NAME, "2", document2Update);

		updateDocumentRequest.setType(_MAPPING_NAME);

		bulkDocumentRequest2.addBulkableDocumentRequest(updateDocumentRequest);

		BulkDocumentResponse bulkDocumentResponse2 =
			_searchEngineAdapter.execute(bulkDocumentRequest2);

		Assert.assertFalse(bulkDocumentResponse2.hasErrors());

		List<BulkDocumentItemResponse> bulkDocumentItemResponses2 =
			bulkDocumentResponse2.getBulkDocumentItemResponses();

		Assert.assertEquals(
			bulkDocumentItemResponses2.toString(), 2,
			bulkDocumentItemResponses2.size());

		BulkDocumentItemResponse bulkDocumentItemResponse3 =
			bulkDocumentItemResponses2.get(0);

		Assert.assertEquals("1", bulkDocumentItemResponse3.getId());

		BulkDocumentItemResponse bulkDocumentItemResponse4 =
			bulkDocumentItemResponses2.get(1);

		Assert.assertEquals("2", bulkDocumentItemResponse4.getId());

		GetResponse<JsonData> getResponse1 = _getDocument("1");

		Assert.assertFalse(getResponse1.found());

		GetResponse<JsonData> getResponse2 = _getDocument("2");

		Assert.assertTrue(getResponse2.found());

		Map<String, JsonData> map2 = getResponse2.fields();

		Assert.assertEquals(Boolean.TRUE.toString(), map2.get(_FIELD_NAME));
	}

	@Test
	public void testExecuteBulkDocumentRequestNoUid() {
		Document document1 = new DocumentImpl();

		document1.addKeyword(_FIELD_NAME, Boolean.TRUE.toString());

		IndexDocumentRequest indexDocumentRequest1 = new IndexDocumentRequest(
			_INDEX_NAME, document1);

		indexDocumentRequest1.setType(_MAPPING_NAME);

		BulkDocumentRequest bulkDocumentRequest1 = new BulkDocumentRequest();

		bulkDocumentRequest1.addBulkableDocumentRequest(indexDocumentRequest1);

		Document document2 = new DocumentImpl();

		document2.addKeyword(_FIELD_NAME, Boolean.FALSE.toString());

		IndexDocumentRequest indexDocumentRequest2 = new IndexDocumentRequest(
			_INDEX_NAME, document2);

		indexDocumentRequest2.setType(_MAPPING_NAME);

		bulkDocumentRequest1.addBulkableDocumentRequest(indexDocumentRequest2);

		BulkDocumentResponse bulkDocumentResponse1 =
			_searchEngineAdapter.execute(bulkDocumentRequest1);

		Assert.assertFalse(bulkDocumentResponse1.hasErrors());

		List<BulkDocumentItemResponse> bulkDocumentItemResponses1 =
			bulkDocumentResponse1.getBulkDocumentItemResponses();

		Assert.assertEquals(
			bulkDocumentItemResponses1.toString(), 2,
			bulkDocumentItemResponses1.size());

		BulkDocumentItemResponse bulkDocumentItemResponse1 =
			bulkDocumentItemResponses1.get(0);

		Assert.assertFalse(
			Validator.isBlank(bulkDocumentItemResponse1.getId()));

		BulkDocumentItemResponse bulkDocumentItemResponse2 =
			bulkDocumentItemResponses1.get(1);

		Assert.assertFalse(
			Validator.isBlank(bulkDocumentItemResponse2.getId()));

		DeleteDocumentRequest deleteDocumentRequest = new DeleteDocumentRequest(
			_INDEX_NAME, bulkDocumentItemResponse1.getId());

		deleteDocumentRequest.setType(_MAPPING_NAME);

		BulkDocumentRequest bulkDocumentRequest2 = new BulkDocumentRequest();

		bulkDocumentRequest2.addBulkableDocumentRequest(deleteDocumentRequest);

		Document document2Update = new DocumentImpl();

		document2Update.addKeyword(
			Field.UID, bulkDocumentItemResponse2.getId());
		document2Update.addKeyword(_FIELD_NAME, Boolean.TRUE.toString());

		UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest(
			_INDEX_NAME, bulkDocumentItemResponse2.getId(), document2Update);

		updateDocumentRequest.setType(_MAPPING_NAME);

		bulkDocumentRequest2.addBulkableDocumentRequest(updateDocumentRequest);

		BulkDocumentResponse bulkDocumentResponse2 =
			_searchEngineAdapter.execute(bulkDocumentRequest2);

		Assert.assertFalse(bulkDocumentResponse2.hasErrors());

		List<BulkDocumentItemResponse> bulkDocumentItemResponses2 =
			bulkDocumentResponse2.getBulkDocumentItemResponses();

		Assert.assertEquals(
			bulkDocumentItemResponses2.toString(), 2,
			bulkDocumentItemResponses2.size());

		BulkDocumentItemResponse bulkDocumentItemResponse3 =
			bulkDocumentItemResponses2.get(0);

		Assert.assertEquals(
			bulkDocumentItemResponse1.getId(),
			bulkDocumentItemResponse3.getId());

		BulkDocumentItemResponse bulkDocumentItemResponse4 =
			bulkDocumentItemResponses2.get(1);

		Assert.assertEquals(
			bulkDocumentItemResponse2.getId(),
			bulkDocumentItemResponse4.getId());

		GetResponse<JsonData> getResponse1 = _getDocument(
			bulkDocumentItemResponse1.getId());

		Assert.assertFalse(getResponse1.found());

		GetResponse<JsonData> getResponse2 = _getDocument(
			bulkDocumentItemResponse2.getId());

		Assert.assertTrue(getResponse2.found());

		Map<String, JsonData> map2 = getResponse2.fields();

		Assert.assertEquals(Boolean.TRUE.toString(), map2.get(_FIELD_NAME));
	}

	@Test
	public void testExecuteDeleteByQueryDocumentRequest() {
		String documentSource1 = "{\"" + _FIELD_NAME + "\":\"true\"}";
		String documentSource2 = "{\"" + _FIELD_NAME + "\":\"false\"}";

		_indexDocument(documentSource1, "1");
		_indexDocument(documentSource2, "2");

		BooleanQuery query = new BooleanQueryImpl();

		query.addExactTerm(_FIELD_NAME, true);

		DeleteByQueryDocumentRequest deleteByQueryDocumentRequest =
			new DeleteByQueryDocumentRequest(query, new String[] {_INDEX_NAME});

		DeleteByQueryDocumentResponse deleteByQueryDocumentResponse =
			_searchEngineAdapter.execute(deleteByQueryDocumentRequest);

		Assert.assertEquals(1, deleteByQueryDocumentResponse.getDeleted());
	}

	@Test
	public void testExecuteDeleteDocumentRequest() {
		String documentSource = "{\"" + _FIELD_NAME + "\":\"true\"}";
		String id = "1";

		_indexDocument(documentSource, id);

		GetResponse getResponse1 = _getDocument(id);

		Assert.assertTrue(getResponse1.found());

		DeleteDocumentRequest deleteDocumentRequest = new DeleteDocumentRequest(
			_INDEX_NAME, id);

		deleteDocumentRequest.setType(_MAPPING_NAME);

		DeleteDocumentResponse deleteDocumentResponse =
			_searchEngineAdapter.execute(deleteDocumentRequest);

		Assert.assertEquals(
			Result.Deleted.jsonValue(),
			deleteDocumentResponse.getStatusString());

		GetResponse<JsonData> getResponse2 = _getDocument(id);

		Assert.assertFalse(getResponse2.found());
	}

	@Test
	public void testExecuteIndexDocumentRequestNoUid() {
		IndexDocumentResponse indexDocumentResponse = _indexDocumentWithAdapter(
			null, new DocumentImpl());

		Assert.assertEquals(
			Result.Created.jsonValue(),
			indexDocumentResponse.getStatusString());

		Assert.assertNotNull(indexDocumentResponse.getUid());
	}

	@Test
	public void testExecuteIndexDocumentRequestNoUidWithUpdate() {
		Document document = new DocumentImpl();

		IndexDocumentResponse indexDocumentResponse = _indexDocumentWithAdapter(
			null, document);

		document.addKeyword(_FIELD_NAME, true);

		_updateDocumentWithAdapter(indexDocumentResponse.getUid(), document);

		GetResponse<JsonData> getResponse = _getDocument(
			indexDocumentResponse.getUid());

		Map<String, JsonData> fields = getResponse.fields();

		Assert.assertEquals(Boolean.TRUE.toString(), fields.get(_FIELD_NAME));
	}

	@Test
	public void testExecuteIndexDocumentRequestUidInDocument() {
		Document document = new DocumentImpl();

		document.addKeyword(Field.UID, "1");

		IndexDocumentResponse indexDocumentResponse = _indexDocumentWithAdapter(
			null, document);

		Assert.assertEquals(
			Result.Created.jsonValue(),
			indexDocumentResponse.getStatusString());

		Assert.assertEquals("1", indexDocumentResponse.getUid());
	}

	@Test
	public void testExecuteIndexDocumentRequestUidInRequest() {
		IndexDocumentResponse indexDocumentResponse = _indexDocumentWithAdapter(
			"1", new DocumentImpl());

		Assert.assertEquals(
			Result.Created.jsonValue(),
			indexDocumentResponse.getStatusString());

		Assert.assertEquals("1", indexDocumentResponse.getUid());
	}

	@Test
	public void testExecuteUpdateByQueryDocumentRequest() {
		String documentSource = "{\"" + _FIELD_NAME + "\":\"true\"}";

		_indexDocument(documentSource, "1");

		BooleanQuery query = new BooleanQueryImpl();

		query.addExactTerm(_FIELD_NAME, true);

		UpdateByQueryDocumentRequest updateByQueryDocumentRequest =
			new UpdateByQueryDocumentRequest(
				query, null, new String[] {_INDEX_NAME});

		UpdateByQueryDocumentResponse updateByQueryDocumentResponse =
			_searchEngineAdapter.execute(updateByQueryDocumentRequest);

		Assert.assertEquals(1, updateByQueryDocumentResponse.getUpdated());
	}

	@Test
	public void testExecuteUpdateDocumentRequest() {
		String documentSource = "{\"" + _FIELD_NAME + "\":\"true\"}";
		String id = "1";

		_indexDocument(documentSource, id);

		GetResponse<JsonData> getResponse1 = _getDocument(id);

		Map<String, JsonData> fields1 = getResponse1.fields();

		Assert.assertEquals(Boolean.TRUE.toString(), fields1.get(_FIELD_NAME));

		Document document = new DocumentImpl();

		document.addKeyword(Field.UID, id);
		document.addKeyword(_FIELD_NAME, false);

		UpdateDocumentResponse updateDocumentResponse =
			_updateDocumentWithAdapter(id, document);

		Assert.assertEquals(
			Result.Updated.jsonValue(),
			updateDocumentResponse.getStatusString());

		GetResponse<JsonData> getResponse2 = _getDocument(id);

		Map<String, JsonData> fields2 = getResponse2.fields();

		Assert.assertEquals(Boolean.FALSE.toString(), fields2.get(_FIELD_NAME));
	}

	@Test
	public void testExecuteUpdateDocumentRequestNoDocumentUid() {
		String documentSource = "{\"" + _FIELD_NAME + "\":\"true\"}";
		String id = "1";

		_indexDocument(documentSource, id);

		GetResponse<JsonData> getResponse1 = _getDocument(id);

		Map<String, JsonData> map1 = getResponse1.fields();

		Assert.assertEquals(Boolean.TRUE.toString(), map1.get(_FIELD_NAME));

		Document document = new DocumentImpl();

		document.addKeyword(_FIELD_NAME, false);

		UpdateDocumentResponse updateDocumentResponse =
			_updateDocumentWithAdapter(id, document);

		Assert.assertEquals(
			Result.Updated.jsonValue(),
			updateDocumentResponse.getStatusString());

		GetResponse<JsonData> getResponse2 = _getDocument(id);

		Map<String, JsonData> fields2 = getResponse2.fields();

		Assert.assertEquals(Boolean.FALSE.toString(), fields2.get(_FIELD_NAME));
	}

	@Test
	public void testExecuteUpdateDocumentRequestNoRequestId() {
		String documentSource = "{\"" + _FIELD_NAME + "\":\"true\"}";
		String id = "1";

		_indexDocument(documentSource, id);

		GetResponse<JsonData> getResponse1 = _getDocument(id);

		Map<String, JsonData> fields1 = getResponse1.fields();

		Assert.assertEquals(Boolean.TRUE.toString(), fields1.get(_FIELD_NAME));

		Document document = new DocumentImpl();

		document.addKeyword(Field.UID, id);
		document.addKeyword(_FIELD_NAME, false);

		UpdateDocumentResponse updateDocumentResponse =
			_updateDocumentWithAdapter(null, document);

		Assert.assertEquals(
			Result.Updated.jsonValue(),
			updateDocumentResponse.getStatusString());

		GetResponse<JsonData> getResponse2 = _getDocument(id);

		Map<String, JsonData> fields2 = getResponse2.fields();

		Assert.assertEquals(Boolean.FALSE.toString(), fields2.get(_FIELD_NAME));
	}

	@Test
	public void testExecuteUpdateDocumentRequestScript() {
		String documentSource = "{\"" + _FIELD_NAME + "\":\"true\"}";
		String id = "1";

		_indexDocument(documentSource, id);

		GetResponse<JsonData> getResponse1 = _getDocument(id);

		Map<String, JsonData> fields1 = getResponse1.fields();

		Assert.assertEquals(Boolean.TRUE.toString(), fields1.get(_FIELD_NAME));

		UpdateDocumentResponse updateDocumentResponse =
			_updateDocumentWithAdapter(
				id,
				_scripts.script(
					StringBundler.concat(
						"ctx._source.", _FIELD_NAME, "=\"false\" ")),
				false);

		Assert.assertEquals(
			Result.Updated.jsonValue(),
			updateDocumentResponse.getStatusString());

		GetResponse<JsonData> getResponse2 = _getDocument(id);

		Map<String, JsonData> fields2 = getResponse2.fields();

		Assert.assertEquals(Boolean.FALSE.toString(), fields2.get(_FIELD_NAME));
	}

	@Test
	public void testExecuteUpdateDocumentRequestScriptedUpsert() {
		String id = "1";

		_updateDocumentWithAdapter(
			id,
			_scripts.script(
				StringBundler.concat(
					"ctx._source.", _FIELD_NAME, "=\"true\" ")),
			true);

		GetResponse<JsonData> getResponse = _getDocument(id);

		Map<String, JsonData> fields = getResponse.fields();

		Assert.assertEquals(Boolean.TRUE.toString(), fields.get(_FIELD_NAME));
	}

	protected static SearchEngineAdapter createSearchEngineAdapter(
		OpenSearchConnectionManager openSearchConnectionManager) {

		SearchEngineAdapter searchEngineAdapter =
			new OpenSearchSearchEngineAdapterImpl();

		ReflectionTestUtil.setFieldValue(
			searchEngineAdapter, "_documentRequestExecutor",
			_createDocumentRequestExecutor(
				openSearchConnectionManager,
				new OpenSearchDocumentFactoryImpl()));

		return searchEngineAdapter;
	}

	private static DocumentRequestExecutor _createDocumentRequestExecutor(
		OpenSearchConnectionManager openSearchConnectionManager,
		OpenSearchDocumentFactory openSearchDocumentFactory) {

		DocumentRequestExecutorFixture documentRequestExecutorFixture =
			new DocumentRequestExecutorFixture() {
				{
					setOpenSearchConnectionManager(openSearchConnectionManager);
					setOpenSearchDocumentFactory(openSearchDocumentFactory);
				}
			};

		documentRequestExecutorFixture.setUp();

		return documentRequestExecutorFixture.getDocumentRequestExecutor();
	}

	private void _createIndex() throws JSONException {
		CreateIndexRequest.Builder createIndexRequestBuilder =
			new CreateIndexRequest.Builder();

		createIndexRequestBuilder.index(_INDEX_NAME);

		JSONObject mappingsJSONObject = JSONFactoryUtil.createJSONObject(
			_MAPPING_SOURCE);

		createIndexRequestBuilder.mappings(
			TypeMapping.of(
				typeMapping -> typeMapping.properties(
					MappingsUtil.getPropertiesMap(mappingsJSONObject))));

		try {
			_openSearchIndicesClient.create(createIndexRequestBuilder.build());
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private void _deleteIndex() {
		try {
			_openSearchIndicesClient.delete(
				DeleteIndexRequest.of(
					deleteIndexRequest -> deleteIndexRequest.index(
						_INDEX_NAME)));
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private GetResponse<JsonData> _getDocument(String id) {
		try {
			return _openSearchClient.get(
				GetRequest.of(
					getRequest -> getRequest.id(
						id
					).index(
						_INDEX_NAME
					)),
				JsonData.class);
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private void _indexDocument(String documentSource, String id) {
		IndexRequest.Builder<JsonData> indexRequestBuilder =
			new IndexRequest.Builder<>();

		indexRequestBuilder.id(id);
		indexRequestBuilder.index(_INDEX_NAME);
		indexRequestBuilder.refresh(Refresh.True);
		indexRequestBuilder.document(JsonData.of(documentSource));

		try {
			_openSearchClient.index(indexRequestBuilder.build());
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private IndexDocumentResponse _indexDocumentWithAdapter(
		String uid, Document document) {

		IndexDocumentRequest indexDocumentRequest = new IndexDocumentRequest(
			_INDEX_NAME, uid, document);

		indexDocumentRequest.setType(_MAPPING_NAME);

		return _searchEngineAdapter.execute(indexDocumentRequest);
	}

	private UpdateDocumentResponse _updateDocumentWithAdapter(
		String uid, Document document) {

		UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest(
			_INDEX_NAME, uid, document);

		updateDocumentRequest.setType(_MAPPING_NAME);

		return _searchEngineAdapter.execute(updateDocumentRequest);
	}

	private UpdateDocumentResponse _updateDocumentWithAdapter(
		String uid, Script script, boolean scriptedUpsert) {

		UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest(
			_INDEX_NAME, uid, script);

		updateDocumentRequest.setScriptedUpsert(scriptedUpsert);
		updateDocumentRequest.setType(_MAPPING_NAME);

		return _searchEngineAdapter.execute(updateDocumentRequest);
	}

	private static final String _FIELD_NAME = "matchDocument";

	private static final String _INDEX_NAME = "test_request_index";

	private static final String _MAPPING_NAME = "testDocumentMapping";

	private static final String _MAPPING_SOURCE =
		"{\"properties\":{\"matchDocument\":{\"type\":\"boolean\"}}}";

	private static OpenSearchFixture _openSearchFixture;
	private static final Scripts _scripts = new ScriptsImpl();

	private final DocumentFixture _documentFixture = new DocumentFixture();
	private OpenSearchClient _openSearchClient;
	private OpenSearchIndicesClient _openSearchIndicesClient;
	private SearchEngineAdapter _searchEngineAdapter;

}