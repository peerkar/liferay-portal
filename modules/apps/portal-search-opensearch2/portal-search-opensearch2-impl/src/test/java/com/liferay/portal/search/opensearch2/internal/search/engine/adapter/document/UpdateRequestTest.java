/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter.document;

import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
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
import org.opensearch.client.opensearch.core.GetRequest;
import org.opensearch.client.opensearch.core.GetResponse;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.core.IndexResponse;
import org.opensearch.client.opensearch.core.UpdateRequest;
import org.opensearch.client.opensearch.indices.CreateIndexRequest;
import org.opensearch.client.opensearch.indices.DeleteIndexRequest;
import org.opensearch.client.opensearch.indices.OpenSearchIndicesClient;

/**
 * @author Adam Brandizzi
 * @author Petteri Karttunen
 */
public class UpdateRequestTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() throws Exception {
		_openSearchFixture = new OpenSearchFixture();

		_openSearchFixture.setUp();

		_openSearchClient = _openSearchFixture.getOpenSearchClient();

		_openSearchIndicesClient = _openSearchClient.indices();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		_openSearchFixture.tearDown();
	}

	@Before
	public void setUp() throws IOException {
		_openSearchIndicesClient.create(
			CreateIndexRequest.of(
				createIndexReques -> createIndexReques.index(_INDEX_NAME)));
	}

	@After
	public void tearDown() throws IOException {
		_openSearchIndicesClient.delete(
			DeleteIndexRequest.of(
				deleteIndexRequest -> deleteIndexRequest.index(_INDEX_NAME)));
	}

	@Test
	public void testUnsetValueWithArrayWithNull() throws IOException {
		String id = _indexAndGetId();

		_updateField(id, "field2", new Object[] {null});

		Map<String, JsonData> fields = _getFields(id);

		Assert.assertEquals("an example", fields.get("field1"));

		@SuppressWarnings("unchecked")
		List<Object> list = (List<Object>)fields.get("field2");

		Assert.assertEquals(list.toString(), 1, list.size());
		Assert.assertNull(list.get(0));
	}

	@Test
	public void testUnsetValueWithEmptyArray() throws IOException {
		String id = _indexAndGetId();

		_updateField(id, "field2", new Object[0]);

		Map<String, JsonData> fields = _getFields(id);

		Assert.assertEquals("an example", fields.get("field1"));

		@SuppressWarnings("unchecked")
		List<Object> list = (List<Object>)fields.get("field2");

		Assert.assertTrue(list.toString(), list.isEmpty());
	}

	@Test
	public void testUnsetValueWithNull() throws IOException {
		String id = _indexAndGetId();

		_updateField(id, "field2", null);

		Map<String, JsonData> fields = _getFields(id);

		Assert.assertEquals("an example", fields.get("field1"));
		Assert.assertNull(fields.get("field2"));
	}

	@Test
	public void testUpdateRequestWithMap() throws IOException {
		String id = _indexAndGetId();

		_updateField(id, "field2", "UPDATED FIELD");

		Map<String, JsonData> fields = _getFields(id);

		Assert.assertEquals("an example", fields.get("field1"));
		Assert.assertEquals("UPDATED FIELD", fields.get("field2"));
	}

	private Map<String, JsonData> _getFields(String id) throws IOException {
		GetResponse<JsonData> getResponse = _openSearchClient.get(
			GetRequest.of(
				getRequest -> getRequest.index(
					_INDEX_NAME
				).id(
					id
				)),
			JsonData.class);

		return getResponse.fields();
	}

	private String _indexAndGetId() throws IOException {
		IndexRequest.Builder<JsonData> indexRequestBuilder =
			new IndexRequest.Builder<>();

		indexRequestBuilder.document(
			JsonData.of(
				HashMapBuilder.put(
					"field1", "an example"
				).put(
					"field2", "some test"
				).build()));
		indexRequestBuilder.index(_INDEX_NAME);

		IndexResponse indexResponse = _openSearchClient.index(
			indexRequestBuilder.build());

		return indexResponse.id();
	}

	private void _updateField(String id, String fieldName, Object fieldValue)
		throws IOException {

		UpdateRequest.Builder<JsonData, JsonData> updateRequestBuilder =
			new UpdateRequest.Builder<>();

		updateRequestBuilder.id(id);
		updateRequestBuilder.index(_INDEX_NAME);

		updateRequestBuilder.doc(
			JsonData.of(
				HashMapBuilder.put(
					fieldName, fieldValue
				).build()));

		_openSearchClient.update(updateRequestBuilder.build(), JsonData.class);
	}

	private static final String _INDEX_NAME = "test_request_index";

	private static OpenSearchClient _openSearchClient;
	private static OpenSearchFixture _openSearchFixture;
	private static OpenSearchIndicesClient _openSearchIndicesClient;

}