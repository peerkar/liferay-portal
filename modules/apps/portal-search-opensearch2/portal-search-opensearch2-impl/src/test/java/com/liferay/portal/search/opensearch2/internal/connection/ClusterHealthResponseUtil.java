/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.connection;

import java.io.IOException;

import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.Time;
import org.opensearch.client.opensearch._types.TimeUnit;
import org.opensearch.client.opensearch._types.WaitForActiveShards;
import org.opensearch.client.opensearch.cluster.HealthRequest;
import org.opensearch.client.opensearch.cluster.HealthResponse;
import org.opensearch.client.opensearch.cluster.OpenSearchClusterClient;

/**
 * @author André de Oliveira
 */
public class ClusterHealthResponseUtil {

	public static HealthResponse getClusterHealthResponse(
		OpenSearchConnectionManager openSearchConnectionManager,
		HealthExpectations healthExpectations) {

		OpenSearchClient openSearchClient =
			openSearchConnectionManager.getOpenSearchClient();

		OpenSearchClusterClient openSearchClusterClient =
			openSearchClient.cluster();

		HealthRequest.Builder healthRequestBuilder =
			new HealthRequest.Builder();

		healthRequestBuilder.masterTimeout(
			Time.of(time -> time.time("10" + TimeUnit.Minutes)));
		healthRequestBuilder.timeout(
			Time.of(time -> time.time("10" + TimeUnit.Minutes)));
		healthRequestBuilder.waitForActiveShards(
			WaitForActiveShards.of(
				waitForActiveShards -> waitForActiveShards.count(
					healthExpectations.getActiveShards())));
		healthRequestBuilder.waitForNodes(
			String.valueOf(healthExpectations.getNumberOfNodes()));
		healthRequestBuilder.waitForNoRelocatingShards(true);
		healthRequestBuilder.waitForStatus(
			healthExpectations.getHealthStatus());

		try {
			return openSearchClusterClient.health(healthRequestBuilder.build());
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

}