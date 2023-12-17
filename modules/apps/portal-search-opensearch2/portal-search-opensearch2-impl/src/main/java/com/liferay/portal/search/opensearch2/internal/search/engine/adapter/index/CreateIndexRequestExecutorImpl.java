/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter.index;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.engine.adapter.index.CreateIndexRequest;
import com.liferay.portal.search.engine.adapter.index.CreateIndexResponse;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionManager;
import com.liferay.portal.search.opensearch2.internal.util.JsonpUtil;

import jakarta.json.spi.JsonProvider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;

import org.opensearch.client.json.JsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.mapping.TypeMapping;
import org.opensearch.client.opensearch.indices.IndexSettings;
import org.opensearch.client.opensearch.indices.OpenSearchIndicesClient;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 * @author Joshua Cords
 * @author Tibor Lipusz
 * @author Petteri Karttunen
 */
@Component(service = CreateIndexRequestExecutor.class)
public class CreateIndexRequestExecutorImpl
	implements CreateIndexRequestExecutor {

	@Override
	public CreateIndexResponse execute(CreateIndexRequest createIndexRequest) {
		org.opensearch.client.opensearch.indices.CreateIndexResponse
			createIndexResponse = _getCreateIndexResponse(
				createIndexRequest,
				createCreateIndexRequest(createIndexRequest));

		JsonpUtil.logInfoResponse(_log, createIndexResponse);

		return new CreateIndexResponse(
			createIndexResponse.acknowledged(), createIndexResponse.index());
	}

	protected org.opensearch.client.opensearch.indices.CreateIndexRequest
		createCreateIndexRequest(CreateIndexRequest createIndexRequest) {

		org.opensearch.client.opensearch.indices.CreateIndexRequest.Builder
			createIndexRequestBuilder =
				new org.opensearch.client.opensearch.indices.CreateIndexRequest.
					Builder();

		createIndexRequestBuilder.index(createIndexRequest.getIndexName());

		_setMappings(createIndexRequest, createIndexRequestBuilder);
		_setSettings(createIndexRequest, createIndexRequestBuilder);

		return createIndexRequestBuilder.build();
	}

	private org.opensearch.client.opensearch.indices.CreateIndexResponse
		_getCreateIndexResponse(
			CreateIndexRequest createIndexRequest,
			org.opensearch.client.opensearch.indices.CreateIndexRequest
				openSearchCreateIndexRequest) {

		OpenSearchClient openSearchClient =
			_openSearchConnectionManager.getOpenSearchClient(
				createIndexRequest.getConnectionId(),
				createIndexRequest.isPreferLocalCluster());

		OpenSearchIndicesClient openSearchIndicesClient =
			openSearchClient.indices();

		try {
			return openSearchIndicesClient.create(openSearchCreateIndexRequest);
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private void _setMappings(
		CreateIndexRequest createIndexRequest,
		org.opensearch.client.opensearch.indices.CreateIndexRequest.Builder
			createIndexRequestBuilder) {

		String mappings = createIndexRequest.getMappings();

		if (Validator.isBlank(mappings)) {
			return;
		}

		JsonpMapper jsonpMapper = _openSearchConnectionManager.getJsonpMapper(
			createIndexRequest.getConnectionId());

		try (InputStream inputStream = new ByteArrayInputStream(
				mappings.getBytes(StandardCharsets.UTF_8))) {

			JsonProvider jsonProvider = jsonpMapper.jsonProvider();

			createIndexRequestBuilder.mappings(
				TypeMapping._DESERIALIZER.deserialize(
					jsonProvider.createParser(inputStream), jsonpMapper));
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private void _setSettings(
		CreateIndexRequest createIndexRequest,
		org.opensearch.client.opensearch.indices.CreateIndexRequest.Builder
			createIndexRequestBuilder) {

		String settings = createIndexRequest.getSettings();

		if (Validator.isBlank(settings)) {
			return;
		}

		JsonpMapper jsonpMapper = _openSearchConnectionManager.getJsonpMapper(
			createIndexRequest.getConnectionId());

		try (InputStream inputStream = new ByteArrayInputStream(
				settings.getBytes(StandardCharsets.UTF_8))) {

			JsonProvider jsonProvider = jsonpMapper.jsonProvider();

			createIndexRequestBuilder.settings(
				IndexSettings._DESERIALIZER.deserialize(
					jsonProvider.createParser(inputStream), jsonpMapper));
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CreateIndexRequestExecutorImpl.class);

	@Reference
	private OpenSearchConnectionManager _openSearchConnectionManager;

}