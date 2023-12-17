/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.index.AnalysisIndexResponseToken;
import com.liferay.portal.search.engine.adapter.index.AnalyzeIndexRequest;
import com.liferay.portal.search.engine.adapter.index.AnalyzeIndexResponse;
import com.liferay.portal.search.engine.adapter.index.CloseIndexRequest;
import com.liferay.portal.search.engine.adapter.index.CloseIndexResponse;
import com.liferay.portal.search.engine.adapter.index.CreateIndexRequest;
import com.liferay.portal.search.engine.adapter.index.CreateIndexResponse;
import com.liferay.portal.search.engine.adapter.index.DeleteIndexRequest;
import com.liferay.portal.search.engine.adapter.index.DeleteIndexResponse;
import com.liferay.portal.search.engine.adapter.index.FlushIndexRequest;
import com.liferay.portal.search.engine.adapter.index.FlushIndexResponse;
import com.liferay.portal.search.engine.adapter.index.GetFieldMappingIndexRequest;
import com.liferay.portal.search.engine.adapter.index.GetFieldMappingIndexResponse;
import com.liferay.portal.search.engine.adapter.index.GetIndexIndexRequest;
import com.liferay.portal.search.engine.adapter.index.GetIndexIndexResponse;
import com.liferay.portal.search.engine.adapter.index.GetMappingIndexRequest;
import com.liferay.portal.search.engine.adapter.index.GetMappingIndexResponse;
import com.liferay.portal.search.engine.adapter.index.IndexRequestExecutor;
import com.liferay.portal.search.engine.adapter.index.IndicesExistsIndexRequest;
import com.liferay.portal.search.engine.adapter.index.IndicesExistsIndexResponse;
import com.liferay.portal.search.engine.adapter.index.IndicesOptions;
import com.liferay.portal.search.engine.adapter.index.OpenIndexRequest;
import com.liferay.portal.search.engine.adapter.index.OpenIndexResponse;
import com.liferay.portal.search.engine.adapter.index.PutMappingIndexRequest;
import com.liferay.portal.search.engine.adapter.index.PutMappingIndexResponse;
import com.liferay.portal.search.engine.adapter.index.RefreshIndexRequest;
import com.liferay.portal.search.engine.adapter.index.RefreshIndexResponse;
import com.liferay.portal.search.engine.adapter.index.UpdateIndexSettingsIndexRequest;
import com.liferay.portal.search.engine.adapter.index.UpdateIndexSettingsIndexResponse;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionManager;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.search.opensearch2.internal.search.engine.adapter.index.IndexRequestExecutorFixture;
import com.liferay.portal.search.opensearch2.internal.util.JsonpUtil;
import com.liferay.portal.search.opensearch2.internal.util.MappingsUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import jakarta.json.spi.JsonProvider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

import org.opensearch.client.json.JsonpMapper;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.Time;
import org.opensearch.client.opensearch.cat.IndicesRequest;
import org.opensearch.client.opensearch.cat.IndicesResponse;
import org.opensearch.client.opensearch.cat.OpenSearchCatClient;
import org.opensearch.client.opensearch.cat.indices.IndicesRecord;
import org.opensearch.client.opensearch.indices.ExistsRequest;
import org.opensearch.client.opensearch.indices.GetIndicesSettingsRequest;
import org.opensearch.client.opensearch.indices.GetIndicesSettingsResponse;
import org.opensearch.client.opensearch.indices.GetMappingRequest;
import org.opensearch.client.opensearch.indices.GetMappingResponse;
import org.opensearch.client.opensearch.indices.IndexSettings;
import org.opensearch.client.opensearch.indices.IndexState;
import org.opensearch.client.opensearch.indices.OpenRequest;
import org.opensearch.client.opensearch.indices.OpenSearchIndicesClient;
import org.opensearch.client.opensearch.indices.PutIndicesSettingsRequest;
import org.opensearch.client.opensearch.indices.PutMappingRequest;
import org.opensearch.client.opensearch.indices.get_mapping.IndexMappingRecord;
import org.opensearch.client.transport.endpoints.BooleanResponse;

/**
 * @author Dylan Rebelak
 * @author Petteri Karttunen
 */
public class OpenSearchSearchEngineAdapterIndexRequestTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() throws Exception {
		_openSearchFixture = new OpenSearchFixture();

		_openSearchFixture.setUp();

		_searchEngineAdapter = createSearchEngineAdapter(_openSearchFixture);

		OpenSearchClient openSearchClient =
			_openSearchFixture.getOpenSearchClient();

		_openSearchIndicesClient = openSearchClient.indices();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		_openSearchFixture.tearDown();
	}

	@Before
	public void setUp() {
		_createIndex(_INDEX_NAME);
	}

	@After
	public void tearDown() {
		_deleteIndex(_INDEX_NAME);
	}

	@Test
	public void testExecuteAnalyzeIndexRequestWithAnalyzer() {
		AnalyzeIndexRequest analyzeIndexRequest = new AnalyzeIndexRequest();

		analyzeIndexRequest.setAnalyzer("stop");

		_assertExecuteAnalyzeIndexRequest(
			analyzeIndexRequest,
			"quick,brown,foxes,jumped,over,lazy,dog,s,bone");
	}

	@Test
	public void testExecuteAnalyzeIndexRequestWithCharFilters() {
		_putSettings(
			StringBundler.concat(
				"{\n", "    \"settings\": {\n", "        \"analysis\": {\n",
				"            \"char_filter\": {\n",
				"                \"custom_cf\": {\n",
				"                    \"type\": \"mapping\",\n",
				"                    \"mappings\": [\n",
				"                        \"- => +\",\n",
				"                        \"2 => 3\"\n",
				"                    ]\n", "                }\n",
				"            }\n", "        }\n", "    }\n", "}"));

		AnalyzeIndexRequest analyzeIndexRequest = new AnalyzeIndexRequest();

		analyzeIndexRequest.setCharFilters(Collections.singleton("custom_cf"));

		_assertExecuteAnalyzeIndexRequest(
			analyzeIndexRequest,
			"The 3 QUICK Brown+Foxes jumped over the lazy dog's bone.");
	}

	@Test
	public void testExecuteAnalyzeIndexRequestWithExplainAndAnalyzer() {
		AnalyzeIndexRequest analyzeIndexRequest = new AnalyzeIndexRequest();

		analyzeIndexRequest.setAnalyzer("stop");
		analyzeIndexRequest.setExplain(true);
		analyzeIndexRequest.setIndexName(_INDEX_NAME);
		analyzeIndexRequest.setTexts(
			"The 2 QUICK Brown-Foxes jumped over the lazy dog's bone.");

		AnalyzeIndexResponse analyzeIndexResponse =
			_searchEngineAdapter.execute(analyzeIndexRequest);

		AnalyzeIndexResponse.DetailsAnalyzer detailsAnalyzer =
			analyzeIndexResponse.getDetailsAnalyzer();

		Assert.assertEquals("stop", detailsAnalyzer.getAnalyzerName());

		_assertAnalysisIndexResponseTokens(
			detailsAnalyzer.getAnalysisIndexResponseTokens(),
			"quick,brown,foxes,jumped,over,lazy,dog,s,bone");
	}

	@Test
	public void testExecuteAnalyzeIndexRequestWithExplainAndWithoutAnalyzer() {
		AnalyzeIndexRequest analyzeIndexRequest = new AnalyzeIndexRequest();

		analyzeIndexRequest.setExplain(true);
		analyzeIndexRequest.setIndexName(_INDEX_NAME);
		analyzeIndexRequest.setTexts(
			"The 2 QUICK Brown-Foxes jumped over the lazy dog's bone.");
		analyzeIndexRequest.setTokenFilters(Collections.singleton("uppercase"));

		AnalyzeIndexResponse analyzeIndexResponse =
			_searchEngineAdapter.execute(analyzeIndexRequest);

		Assert.assertNull(analyzeIndexResponse.getDetailsAnalyzer());

		List<AnalyzeIndexResponse.DetailsTokenFilter> detailsTokenFilters =
			analyzeIndexResponse.getDetailsTokenFilters();

		Assert.assertEquals(
			detailsTokenFilters.toString(), 1, detailsTokenFilters.size());

		AnalyzeIndexResponse.DetailsTokenFilter detailsTokenFilter =
			detailsTokenFilters.get(0);

		Assert.assertEquals(
			"uppercase", detailsTokenFilter.getTokenFilterName());

		_assertAnalysisIndexResponseTokens(
			detailsTokenFilter.getAnalysisIndexResponseTokens(),
			"THE 2 QUICK BROWN-FOXES JUMPED OVER THE LAZY DOG'S BONE.");
	}

	@Test
	public void testExecuteAnalyzeIndexRequestWithFieldName() throws Exception {
		String mappingsSource =
			"{\"properties\":{\"keywordTestField\":{\"type\":\"keyword\"}}}";

		_putMapping(mappingsSource);

		AnalyzeIndexRequest analyzeIndexRequest = new AnalyzeIndexRequest();

		analyzeIndexRequest.setFieldName("keywordTestField");

		_assertExecuteAnalyzeIndexRequest(
			analyzeIndexRequest,
			"The 2 QUICK Brown-Foxes jumped over the lazy dog's bone.");
	}

	@Ignore
	@Test
	public void testExecuteAnalyzeIndexRequestWithNormalizer() {
		_putSettings(
			StringBundler.concat(
				"{\n", "    \"settings\": {\n", "        \"analysis\": {\n",
				"            \"normalizer\": {\n",
				"                \"custom_normalizer\": {\n",
				"                    \"type\": \"custom\",\n",
				"                    \"filter\": [\"uppercase\"]\n",
				"                }\n", "            }\n", "        }\n",
				"    }\n", "}"));

		AnalyzeIndexRequest analyzeIndexRequest = new AnalyzeIndexRequest();

		analyzeIndexRequest.setNormalizer("custom_normalizer");

		// OpenSearch bug? Calls AnalyzeRequest.withNormalizer()
		// Response contains tokens:
		// "the,2,quick,brown,foxes,jumped,over,the,lazy,dog's,bone"

		_assertExecuteAnalyzeIndexRequest(
			analyzeIndexRequest,
			"THE 2 QUICK BROWN-FOXES JUMPED OVER THE LAZY DOG'S BONE.");
	}

	@Test
	public void testExecuteAnalyzeIndexRequestWithTokenFilters() {
		AnalyzeIndexRequest analyzeIndexRequest = new AnalyzeIndexRequest();

		analyzeIndexRequest.setTokenFilters(Collections.singleton("uppercase"));

		_assertExecuteAnalyzeIndexRequest(
			analyzeIndexRequest,
			"THE 2 QUICK BROWN-FOXES JUMPED OVER THE LAZY DOG'S BONE.");
	}

	@Test
	public void testExecuteAnalyzeIndexRequestWithTokenizer() {
		AnalyzeIndexRequest analyzeIndexRequest = new AnalyzeIndexRequest();

		analyzeIndexRequest.setTokenizer("letter");

		_assertExecuteAnalyzeIndexRequest(
			analyzeIndexRequest,
			"The,QUICK,Brown,Foxes,jumped,over,the,lazy,dog,s,bone");
	}

	@Test
	public void testExecuteCloseIndexRequest() {
		CloseIndexRequest closeIndexRequest = new CloseIndexRequest(
			_INDEX_NAME);

		IndicesOptions indicesOptions = new IndicesOptions();

		indicesOptions.setIgnoreUnavailable(true);

		closeIndexRequest.setIndicesOptions(indicesOptions);

		CloseIndexResponse closeIndexResponse = _searchEngineAdapter.execute(
			closeIndexRequest);

		Assert.assertTrue(
			"Close request not acknowledged",
			closeIndexResponse.isAcknowledged());

		_assertIndexStatus(_INDEX_NAME, "close");
	}

	@Test
	public void testExecuteCreateIndexRequest() {
		CreateIndexRequest createIndexRequest = new CreateIndexRequest(
			"test_index_2");

		createIndexRequest.setSource(
			StringBundler.concat(
				"{\n", "    \"settings\": {\n",
				"        \"number_of_shards\": 1\n", "    },\n",
				"    \"mappings\": {\n", "        \"type1\": {\n",
				"            \"properties\": {\n",
				"                \"field1\": {\n",
				"                    \"type\": \"text\"\n",
				"                }\n", "            }\n", "        }\n",
				"    }\n", "}"));

		CreateIndexResponse createIndexResponse = _searchEngineAdapter.execute(
			createIndexRequest);

		Assert.assertTrue(createIndexResponse.isAcknowledged());

		Assert.assertEquals("test_index_2", createIndexResponse.getIndexName());

		Assert.assertTrue(_indiciesExists("test_index_2"));

		_deleteIndex("test_index_2");
	}

	@Test
	public void testExecuteDeleteIndexRequest() {
		_createIndex("test_index_2");

		DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(
			"test_index_2");

		DeleteIndexResponse deleteIndexResponse = _searchEngineAdapter.execute(
			deleteIndexRequest);

		Assert.assertTrue(deleteIndexResponse.isAcknowledged());

		Assert.assertFalse(_indiciesExists("test_index_2"));
	}

	@Test
	public void testExecuteFlushIndexRequest() {
		FlushIndexRequest flushIndexRequest = new FlushIndexRequest(
			_INDEX_NAME);

		FlushIndexResponse flushIndexResponse = _searchEngineAdapter.execute(
			flushIndexRequest);

		Assert.assertEquals(0, flushIndexResponse.getFailedShards());
	}

	@Test
	public void testExecuteGetFieldMappingIndexRequest() throws Exception {
		String mappingSource =
			"{\"properties\":{\"testField\":{\"type\":\"keyword\"}, " +
				"\"otherTestField\":{\"type\":\"keyword\"}}}";

		_putMapping(mappingSource);

		String[] fields = {"otherTestField"};

		GetFieldMappingIndexRequest getFieldMappingIndexRequest =
			new GetFieldMappingIndexRequest(new String[] {_INDEX_NAME}, fields);

		GetFieldMappingIndexResponse getFieldMappingIndexResponse =
			_searchEngineAdapter.execute(getFieldMappingIndexRequest);

		Map<String, String> fieldMappings =
			getFieldMappingIndexResponse.getFieldMappings();

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			fieldMappings.get(_INDEX_NAME));

		String fieldMapping = jsonObject.getString("otherTestField");

		Assert.assertTrue(
			fieldMapping,
			fieldMapping.equals("{\"otherTestField\":{\"type\":\"keyword\"}}"));
	}

	@Test
	public void testExecuteGetIndexIndexRequest() throws Exception {
		String mappingSource =
			"{\"properties\":{\"testField\":{\"type\":\"keyword\"}}}";

		_putMapping(mappingSource);

		GetIndexIndexRequest getIndexIndexRequest = new GetIndexIndexRequest(
			_INDEX_NAME);

		GetIndexIndexResponse getIndexIndexResponse =
			_searchEngineAdapter.execute(getIndexIndexRequest);

		String[] indexNames = getIndexIndexResponse.getIndexNames();

		Assert.assertEquals(Arrays.toString(indexNames), 1, indexNames.length);
		Assert.assertEquals(_INDEX_NAME, indexNames[0]);

		String indexMappings = String.valueOf(
			getIndexIndexResponse.getMappings());

		Assert.assertTrue(indexMappings, indexMappings.contains(mappingSource));
	}

	@Test
	public void testExecuteGetMappingIndexRequest() throws Exception {
		String mappingSource =
			"{\"properties\":{\"testField\":{\"type\":\"keyword\"}}}";

		_putMapping(mappingSource);

		GetMappingIndexRequest getMappingIndexRequest =
			new GetMappingIndexRequest(new String[] {_INDEX_NAME});

		GetMappingIndexResponse getMappingIndexResponse =
			_searchEngineAdapter.execute(getMappingIndexRequest);

		String string = String.valueOf(
			getMappingIndexResponse.getIndexMappings());

		Assert.assertTrue(string.contains(mappingSource));
	}

	@Test
	public void testExecuteIndicesExistsIndexRequest() {
		IndicesExistsIndexRequest indicesExistsIndexRequest1 =
			new IndicesExistsIndexRequest(_INDEX_NAME);

		IndicesExistsIndexResponse indicesExistsIndexResponse1 =
			_searchEngineAdapter.execute(indicesExistsIndexRequest1);

		Assert.assertTrue(indicesExistsIndexResponse1.isExists());

		IndicesExistsIndexRequest indicesExistsIndexRequest2 =
			new IndicesExistsIndexRequest("test_index_2");

		IndicesExistsIndexResponse indicesExistsIndexResponse2 =
			_searchEngineAdapter.execute(indicesExistsIndexRequest2);

		Assert.assertFalse(indicesExistsIndexResponse2.isExists());
	}

	@Test
	public void testExecuteOpenIndexRequest() {
		_closeIndex(_INDEX_NAME);

		_assertIndexStatus(_INDEX_NAME, "close");

		OpenIndexRequest openIndexRequest = new OpenIndexRequest(_INDEX_NAME);

		IndicesOptions indicesOptions = new IndicesOptions();

		indicesOptions.setIgnoreUnavailable(true);

		openIndexRequest.setIndicesOptions(indicesOptions);

		OpenIndexResponse openIndexResponse = _searchEngineAdapter.execute(
			openIndexRequest);

		Assert.assertTrue(
			"Open request not acknowledged",
			openIndexResponse.isAcknowledged());

		_assertIndexStatus(_INDEX_NAME, "open");
	}

	@Test
	public void testExecutePutMappingIndexRequest() {
		String mappingSource =
			"{\"properties\":{\"testField\":{\"type\":\"keyword\"}}}";

		PutMappingIndexRequest putMappingIndexRequest =
			new PutMappingIndexRequest(
				new String[] {_INDEX_NAME}, mappingSource);

		PutMappingIndexResponse putMappingIndexResponse =
			_searchEngineAdapter.execute(putMappingIndexRequest);

		Assert.assertTrue(putMappingIndexResponse.isAcknowledged());

		GetMappingResponse getMappingResponse = _getGetMappingResponse(
			_INDEX_NAME);

		Map<String, IndexMappingRecord> indexMappingsRecords =
			getMappingResponse.result();

		IndexMappingRecord indexMappingRecord = indexMappingsRecords.get(
			_INDEX_NAME);

		String mappingMetadataSource = JsonpUtil.toString(
			indexMappingRecord.mappings());

		Assert.assertTrue(mappingMetadataSource.contains(mappingSource));
	}

	@Test
	public void testExecuteRefreshIndexRequest() {
		RefreshIndexRequest refreshIndexRequest = new RefreshIndexRequest(
			_INDEX_NAME);

		RefreshIndexResponse refreshIndexResponse =
			_searchEngineAdapter.execute(refreshIndexRequest);

		Assert.assertEquals(0, refreshIndexResponse.getFailedShards());
	}

	@Test
	public void testExecuteUpdateIndexSettingsIndexRequest() {
		_createIndex("test_index_2");

		UpdateIndexSettingsIndexRequest updateIndexSettingsIndexRequest =
			new UpdateIndexSettingsIndexRequest("test_index_2");

		updateIndexSettingsIndexRequest.setSettings(
			StringBundler.concat(
				"{\n", "    \"index\": {\n",
				"        \"refresh_interval\": \"2s\"\n", "    }\n", "}"));

		UpdateIndexSettingsIndexResponse indexSettingsIndexResponse =
			_searchEngineAdapter.execute(updateIndexSettingsIndexRequest);

		Assert.assertTrue(indexSettingsIndexResponse.isAcknowledged());

		GetIndicesSettingsResponse getIndicesSettingsResponse =
			_getGetIndicesSettingsResponse("test_index_2");

		Map<String, IndexState> indexStates =
			getIndicesSettingsResponse.result();

		IndexState indexState = indexStates.get("test_index_2");

		IndexSettings indexSettings = indexState.settings();

		Time refreshInterval = indexSettings.refreshInterval();

		Assert.assertEquals("2s", refreshInterval.time());

		_deleteIndex("test_index_2");
	}

	protected static SearchEngineAdapter createSearchEngineAdapter(
		OpenSearchConnectionManager openSearchConnectionManager) {

		SearchEngineAdapter searchEngineAdapter =
			new OpenSearchSearchEngineAdapterImpl();

		ReflectionTestUtil.setFieldValue(
			searchEngineAdapter, "_indexRequestExecutor",
			_createIndexRequestExecutor(openSearchConnectionManager));

		return searchEngineAdapter;
	}

	private static IndexRequestExecutor _createIndexRequestExecutor(
		OpenSearchConnectionManager openSearchConnectionManager) {

		IndexRequestExecutorFixture indexRequestExecutorFixture =
			new IndexRequestExecutorFixture() {
				{
					setOpenSearchConnectionManager(openSearchConnectionManager);
				}
			};

		indexRequestExecutorFixture.setUp();

		return indexRequestExecutorFixture.getIndexRequestExecutor();
	}

	private void _assertAnalysisIndexResponseTokens(
		List<AnalysisIndexResponseToken> analysisIndexResponseTokens,
		String expectedTokens) {

		List<String> actualTokensList = new ArrayList<>();

		for (AnalysisIndexResponseToken analysisIndexResponseToken :
				analysisIndexResponseTokens) {

			actualTokensList.add(analysisIndexResponseToken.getTerm());
		}

		String actualTokens = StringUtil.merge(
			actualTokensList, StringPool.COMMA);

		Assert.assertEquals(expectedTokens, expectedTokens, actualTokens);
	}

	private void _assertExecuteAnalyzeIndexRequest(
		AnalyzeIndexRequest analyzeIndexRequest, String expectedTokens) {

		analyzeIndexRequest.setIndexName(_INDEX_NAME);
		analyzeIndexRequest.setTexts(
			"The 2 QUICK Brown-Foxes jumped over the lazy dog's bone.");

		AnalyzeIndexResponse analyzeIndexResponse =
			_searchEngineAdapter.execute(analyzeIndexRequest);

		_assertAnalysisIndexResponseTokens(
			analyzeIndexResponse.getAnalysisIndexResponseTokens(),
			expectedTokens);
	}

	private void _assertIndexStatus(String indexName, String status) {
		OpenSearchClient openSearchClient =
			_openSearchFixture.getOpenSearchClient();

		try {
			OpenSearchCatClient openSearchCatClient = openSearchClient.cat();

			IndicesResponse indicesResponse = openSearchCatClient.indices(
				IndicesRequest.of(
					indicesRequest -> indicesRequest.index(indexName)));

			List<IndicesRecord> indicesRecords = indicesResponse.valueBody();

			IndicesRecord indicesRecord = indicesRecords.get(0);

			Assert.assertEquals(status, indicesRecord.status());
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
	}

	private void _closeIndex(String indexName) {
		try {
			_openSearchIndicesClient.close(
				org.opensearch.client.opensearch.indices.CloseIndexRequest.of(
					closeIndexRequest -> closeIndexRequest.index(indexName)));
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private void _createIndex(String indexName) {
		try {
			_openSearchIndicesClient.create(
				org.opensearch.client.opensearch.indices.CreateIndexRequest.of(
					createIndexRequest -> createIndexRequest.index(indexName)));
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private org.opensearch.client.opensearch.indices.DeleteIndexResponse
		_deleteIndex(String indexName) {

		try {
			return _openSearchIndicesClient.delete(
				org.opensearch.client.opensearch.indices.DeleteIndexRequest.of(
					deleteIndexRequest -> deleteIndexRequest.index(indexName)));
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private GetIndicesSettingsResponse _getGetIndicesSettingsResponse(
		String indexName) {

		try {
			return _openSearchIndicesClient.getSettings(
				GetIndicesSettingsRequest.of(
					getIndicesSettingsRequest ->
						getIndicesSettingsRequest.index(indexName)));
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private GetMappingResponse _getGetMappingResponse(String indexName) {
		try {
			return _openSearchIndicesClient.getMapping(
				GetMappingRequest.of(
					getMappingRequest -> getMappingRequest.index(indexName)));
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private boolean _indiciesExists(String indexName) {
		try {
			BooleanResponse booleanResponse = _openSearchIndicesClient.exists(
				ExistsRequest.of(
					existRequest -> existRequest.index(indexName)));

			return booleanResponse.value();
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private void _openIndex(String indexName) {
		try {
			_openSearchIndicesClient.open(
				OpenRequest.of(openRequest -> openRequest.index(indexName)));
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private void _putMapping(String mappingsSource) throws JSONException {
		PutMappingRequest.Builder putMappingRequestBuilder =
			new PutMappingRequest.Builder();

		putMappingRequestBuilder.index(_INDEX_NAME);
		putMappingRequestBuilder.properties(
			MappingsUtil.getPropertiesMap(
				JSONFactoryUtil.createJSONObject(mappingsSource)));

		try {
			_openSearchIndicesClient.putMapping(
				putMappingRequestBuilder.build());
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private void _putSettings(String settingsSource) {
		_closeIndex(_INDEX_NAME);

		PutIndicesSettingsRequest.Builder putIndicesSettingsRequestBuilder =
			new PutIndicesSettingsRequest.Builder();

		putIndicesSettingsRequestBuilder.index(_INDEX_NAME);

		JsonpMapper jsonpMapper = new JacksonJsonpMapper();

		JsonProvider jsonProvider = jsonpMapper.jsonProvider();

		try (InputStream inputStream = new ByteArrayInputStream(
				settingsSource.getBytes(StandardCharsets.UTF_8))) {

			putIndicesSettingsRequestBuilder.settings(
				IndexSettings._DESERIALIZER.deserialize(
					jsonProvider.createParser(inputStream), jsonpMapper));
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}

		try {
			_openSearchIndicesClient.putSettings(
				putIndicesSettingsRequestBuilder.build());
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}

		_openIndex(_INDEX_NAME);
	}

	private static final String _INDEX_NAME = "test_request_index";

	private static OpenSearchFixture _openSearchFixture;
	private static OpenSearchIndicesClient _openSearchIndicesClient;
	private static SearchEngineAdapter _searchEngineAdapter;

}