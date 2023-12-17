/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter.cluster;

import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.search.engine.adapter.cluster.HealthClusterRequest;
import com.liferay.portal.search.engine.adapter.cluster.HealthClusterResponse;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionManager;
import com.liferay.portal.search.opensearch2.internal.util.JsonpUtil;

import java.io.IOException;

import java.util.Arrays;

import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.Time;
import org.opensearch.client.opensearch._types.TimeUnit;
import org.opensearch.client.opensearch.cluster.HealthRequest;
import org.opensearch.client.opensearch.cluster.HealthResponse;
import org.opensearch.client.opensearch.cluster.OpenSearchClusterClient;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Dylan Rebelak
 * @author Petteri Karttunen
 */
@Component(service = HealthClusterRequestExecutor.class)
public class HealthClusterRequestExecutorImpl
	implements HealthClusterRequestExecutor {

	@Override
	public HealthClusterResponse execute(
		HealthClusterRequest healthClusterRequest) {

		HealthResponse healthResponse = _getHealthResponse(
			createHealthRequest(healthClusterRequest), healthClusterRequest);

		return new HealthClusterResponse(
			_clusterHealthStatusTranslator.translate(healthResponse.status()),
			JsonpUtil.toString(healthResponse));
	}

	protected HealthRequest createHealthRequest(
		HealthClusterRequest healthClusterRequest) {

		HealthRequest.Builder healthRequestBuilder =
			new HealthRequest.Builder();

		if (ArrayUtil.isNotEmpty(healthClusterRequest.getIndexNames())) {
			healthRequestBuilder.index(
				Arrays.asList(healthClusterRequest.getIndexNames()));
		}

		if (healthClusterRequest.getTimeout() > 0) {
			Time time = Time.of(
				openSearchTime -> openSearchTime.time(
					healthClusterRequest.getTimeout() +
						TimeUnit.Milliseconds.jsonValue()));

			healthRequestBuilder.masterTimeout(time);
			healthRequestBuilder.timeout(time);
		}

		if (healthClusterRequest.getWaitForClusterHealthStatus() != null) {
			healthRequestBuilder.waitForStatus(
				_clusterHealthStatusTranslator.translate(
					healthClusterRequest.getWaitForClusterHealthStatus()));
		}

		return healthRequestBuilder.build();
	}

	private HealthResponse _getHealthResponse(
		HealthRequest clusterHealthRequest,
		HealthClusterRequest healthClusterRequest) {

		OpenSearchClient openSearchClient =
			_openSearchConnectionManager.getOpenSearchClient(
				healthClusterRequest.getConnectionId(),
				healthClusterRequest.isPreferLocalCluster());

		OpenSearchClusterClient openSearchClusterClient =
			openSearchClient.cluster();

		try {
			return openSearchClusterClient.health(clusterHealthRequest);
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	@Reference
	private ClusterHealthStatusTranslator _clusterHealthStatusTranslator;

	@Reference
	private OpenSearchConnectionManager _openSearchConnectionManager;

}