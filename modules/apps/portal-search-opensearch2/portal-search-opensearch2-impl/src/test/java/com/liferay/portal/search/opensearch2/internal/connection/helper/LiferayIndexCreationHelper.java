/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.connection.helper;

import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.search.opensearch2.internal.configuration.OpenSearchConfigurationWrapperImpl;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionManager;
import com.liferay.portal.search.opensearch2.internal.index.MappingsFactory;

import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.indices.CreateIndexRequest;
import org.opensearch.client.opensearch.indices.IndexSettings;

/**
 * @author André de Oliveira
 */
public class LiferayIndexCreationHelper implements IndexCreationHelper {

	public LiferayIndexCreationHelper(
		OpenSearchConnectionManager openSearchConnectionManager) {

		_openSearchConnectionManager = openSearchConnectionManager;
	}

	@Override
	public void contribute(CreateIndexRequest createIndexRequest) {

		//		SettingsFactory settingsFactory = new SettingsFactory(jsonFactory, openSearchConfigurationWrapper);

		MappingsFactory mappingsFactory = _getMappingsFactory();

		//mappingsFactory.(createIndexRequest);
	}

	@Override
	public void contributeIndexSettings(
		IndexSettings.Builder indexSettingsBuilder) {

		MappingsFactory mappingsFactory = _getMappingsFactory();

		mappingsFactory.createRequiredDefaultAnalyzers(indexSettingsBuilder);
	}

	@Override
	public void whenIndexCreated(String indexName) {
		MappingsFactory mappingsFactory = _getMappingsFactory();

		mappingsFactory.addOptionalDefaultMappings(indexName);
	}

	private MappingsFactory _getMappingsFactory() {
		OpenSearchClient openSearchClient =
			_openSearchConnectionManager.getOpenSearchClient();

		return new MappingsFactory(
			new JSONFactoryImpl(), openSearchClient.indices(),
			new OpenSearchConfigurationWrapperImpl());
	}

	private final OpenSearchConnectionManager _openSearchConnectionManager;

}