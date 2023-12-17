/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.connection.helper;

import org.opensearch.client.opensearch.indices.CreateIndexRequest;
import org.opensearch.client.opensearch.indices.IndexSettings;

/**
 * @author André de Oliveira
 */
public interface IndexCreationHelper {

	public void contribute(
		CreateIndexRequest.Builder createIndexRequestBuilder);

	public void contributeIndexSettings(
		IndexSettings.Builder indexSettingsBuilder);

	public void whenIndexCreated(String indexName);

}