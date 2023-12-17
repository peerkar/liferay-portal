/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.index;

import com.liferay.portal.search.test.util.IdempotentRetryAssert;

import java.io.IOException;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;

import org.opensearch.client.opensearch._types.mapping.FieldMapping;
import org.opensearch.client.opensearch._types.mapping.Property;
import org.opensearch.client.opensearch.indices.GetFieldMappingRequest;
import org.opensearch.client.opensearch.indices.GetFieldMappingResponse;
import org.opensearch.client.opensearch.indices.OpenSearchIndicesClient;
import org.opensearch.client.opensearch.indices.get_field_mapping.TypeFieldMappings;

/**
 * @author Artur Aquino
 * @author André de Oliveira
 */
public class FieldMappingAssert {

	public static void assertAnalyzer(
			String expectedValue, String field, String index,
			OpenSearchIndicesClient openSearchIndicesClient)
		throws Exception {

		assertFieldMappingMetadata(
			expectedValue, "analyzer", field, index, openSearchIndicesClient);
	}

	public static void assertFieldMappingMetadata(
			String expectedValue, String key, String field, String index,
			OpenSearchIndicesClient openSearchIndicesClient)
		throws Exception {

		IdempotentRetryAssert.retryAssert(
			10, TimeUnit.SECONDS,
			() -> _assertFieldMappingMetadata(
				expectedValue, key, field, index, openSearchIndicesClient));
	}

	public static void assertType(
			String expectedValue, String field, String index,
			OpenSearchIndicesClient openSearchIndicesClient)
		throws Exception {

		assertFieldMappingMetadata(
			expectedValue, "type", field, index, openSearchIndicesClient);
	}

	private static void _assertFieldMappingMetadata(
		String expectedValue, String key, String field, String index,
		OpenSearchIndicesClient openSearchIndicesClient) {

		TypeFieldMappings typeFieldMappings = _getTypeFieldMappings(
			field, index, openSearchIndicesClient);

		String value = _getFieldMappingPropertyValue(
			typeFieldMappings, field, key);

		Assert.assertEquals(expectedValue, value);
	}

	private static String _getFieldMappingPropertyValue(
		TypeFieldMappings typeFieldMappings, String field, String key) {

		Map<String, FieldMapping> fieldMappings = typeFieldMappings.mappings();

		FieldMapping fieldMapping = fieldMappings.get(field);

		Map<String, Property> properties = fieldMapping.mapping();

		Property property = properties.get(key);

		return property.toString();
	}

	private static TypeFieldMappings _getTypeFieldMappings(
		String field, String index,
		OpenSearchIndicesClient openSearchIndicesClient) {

		try {
			GetFieldMappingResponse getFieldMappingResponse =
				openSearchIndicesClient.getFieldMapping(
					GetFieldMappingRequest.of(
						getFieldMappingRequest -> getFieldMappingRequest.fields(
							field
						).index(
							index
						)));

			return getFieldMappingResponse.get(index);
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

}