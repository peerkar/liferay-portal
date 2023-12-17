/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.connection;

import com.liferay.portal.search.opensearch2.internal.connection.helper.IndexCreationHelper;
import com.liferay.portal.search.opensearch2.internal.connection.helper.LiferayIndexCreationHelper;

import java.io.IOException;

import org.mockito.Mockito;

import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.indices.CreateIndexRequest;
import org.opensearch.client.opensearch.indices.DeleteIndexRequest;
import org.opensearch.client.opensearch.indices.IndexSettings;
import org.opensearch.client.opensearch.indices.OpenSearchIndicesClient;

/**
 * @author André de Oliveira
 */
public class IndexCreator {

	public Index createIndex(IndexName indexName) {
		OpenSearchIndicesClient openSearchIndicesClient = _getIndicesClient();

		String name = indexName.getName();

		deleteIndex(openSearchIndicesClient, name);

		CreateIndexRequest.Builder createIndexRequestBuilder =
			new CreateIndexRequest.Builder();

		createIndexRequestBuilder.index(name);

		IndexCreationHelper indexCreationHelper = _getIndexCreationHelper();

		indexCreationHelper.contribute(createIndexRequestBuilder);

		IndexSettings.Builder indexSettingsBuilder =
			new IndexSettings.Builder();

		indexSettingsBuilder.numberOfReplicas("0");
		indexSettingsBuilder.numberOfShards("1");

		indexCreationHelper.contributeIndexSettings(indexSettingsBuilder);

		createIndexRequestBuilder.settings(indexSettingsBuilder.build());

		try {
			openSearchIndicesClient.create(createIndexRequestBuilder.build());
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}

		indexCreationHelper.whenIndexCreated(name);

		return new Index(indexName);
	}

	public void deleteIndex(IndexName indexName) {
		deleteIndex(_getIndicesClient(), indexName.getName());
	}

	protected void deleteIndex(
		OpenSearchIndicesClient openSearchIndicesClient, String name) {

		try {
			openSearchIndicesClient.delete(
				DeleteIndexRequest.of(
					deleteIndexRequest -> deleteIndexRequest.index(name)));
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	protected void setIndexCreationHelper(
		IndexCreationHelper indexCreationHelper) {

		_indexCreationHelper = indexCreationHelper;
	}

	protected void setLiferayMappingsAddedToIndex(
		boolean liferayMappingsAddedToIndex) {

		_liferayMappingsAddedToIndex = liferayMappingsAddedToIndex;
	}

	protected void setOpenSearchConnectionManager(
		OpenSearchConnectionManager openSearchConnectionManager) {

		_openSearchConnectionManager = openSearchConnectionManager;
	}

	private IndexCreationHelper _getIndexCreationHelper() {
		if (!_liferayMappingsAddedToIndex) {
			if (_indexCreationHelper != null) {
				return _indexCreationHelper;
			}

			return Mockito.mock(IndexCreationHelper.class);
		}

		LiferayIndexCreationHelper liferayIndexCreationHelper =
			new LiferayIndexCreationHelper(_openSearchConnectionManager);

		if (_indexCreationHelper == null) {
			return liferayIndexCreationHelper;
		}

		return new IndexCreationHelper() {

			@Override
			public void contribute(
				CreateIndexRequest.Builder createIndexRequestBuilder) {

				_indexCreationHelper.contribute(createIndexRequestBuilder);

				liferayIndexCreationHelper.contribute(
					createIndexRequestBuilder);
			}

			@Override
			public void contributeIndexSettings(
				IndexSettings.Builder indexSettingsBuilder) {

				_indexCreationHelper.contributeIndexSettings(
					indexSettingsBuilder);

				liferayIndexCreationHelper.contributeIndexSettings(
					indexSettingsBuilder);
			}

			@Override
			public void whenIndexCreated(String indexName) {
				_indexCreationHelper.whenIndexCreated(indexName);

				liferayIndexCreationHelper.whenIndexCreated(indexName);
			}

		};
	}

	private final OpenSearchIndicesClient _getIndicesClient() {
		OpenSearchClient openSearchClient =
			_openSearchConnectionManager.getOpenSearchClient();

		return openSearchClient.indices();
	}

	private IndexCreationHelper _indexCreationHelper;
	private boolean _liferayMappingsAddedToIndex;
	private OpenSearchConnectionManager _openSearchConnectionManager;

}