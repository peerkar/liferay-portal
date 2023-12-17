/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.connection;

import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.io.IOException;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.OpenSearchException;
import org.opensearch.client.opensearch.nodes.NodesInfoResponse;
import org.opensearch.client.opensearch.nodes.OpenSearchNodesClient;
import org.opensearch.client.opensearch.nodes.info.NodeInfo;
import org.opensearch.client.opensearch.nodes.info.NodeInfoSettings;
import org.opensearch.client.opensearch.nodes.info.NodeInfoSettingsHttp;

/**
 * @author André de Oliveira
 * @author Petteri Karttunen
 */
public class OpenSearchConnectionTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() {
		OpenSearchConnection.Builder openSearchConnectionBuilder =
			new OpenSearchConnection.Builder();

		openSearchConnectionBuilder.networkHostAddresses(
			new String[] {"http://localhost:9200"});

		_postCloseRunnable = Mockito.mock(Runnable.class);

		openSearchConnectionBuilder.postCloseRunnable(_postCloseRunnable);

		_preConnectOpenSearchConnectionConsumer = Mockito.mock(Consumer.class);

		openSearchConnectionBuilder.preConnectOpenSearchConnectionConsumer(
			_preConnectOpenSearchConnectionConsumer);

		_openSearchConnection = openSearchConnectionBuilder.build();
	}

	@Test
	public void testConnectAndClose() throws Exception {
		Assert.assertFalse(_openSearchConnection.isConnected());

		_openSearchConnection.connect();

		Assert.assertTrue(_openSearchConnection.isConnected());

		Mockito.verify(
			_preConnectOpenSearchConnectionConsumer
		).accept(
			Mockito.any()
		);

		_assertNetworkHostAddress("localhost", 9200);

		_openSearchConnection.close();

		Assert.assertFalse(_openSearchConnection.isConnected());

		Mockito.verify(
			_postCloseRunnable
		).run();
	}

	private void _assertNetworkHostAddress(String hostString, int port)
		throws IOException, OpenSearchException {

		OpenSearchClient openSearchClient =
			_openSearchConnection.getOpenSearchClient();

		OpenSearchNodesClient openSearchNodesClient = openSearchClient.nodes();

		NodesInfoResponse nodesInfoResponse = openSearchNodesClient.info();

		Map<String, NodeInfo> nodes = nodesInfoResponse.nodes();

		Set<String> keySet = nodes.keySet();

		Assert.assertEquals(String.join(",", keySet), 1, nodes.size());

		Iterator<String> iterator = keySet.iterator();

		String firstKey = iterator.next();

		NodeInfo nodeInfo = nodes.get(firstKey);

		NodeInfoSettings nodeInfoSettings = nodeInfo.settings();

		NodeInfoSettingsHttp nodeInfoSettingsHttp = nodeInfoSettings.http();

		Assert.assertEquals(hostString, nodeInfo.host());
		Assert.assertEquals(port, nodeInfoSettingsHttp.port());
	}

	private OpenSearchConnection _openSearchConnection;
	private Runnable _postCloseRunnable;
	private Consumer<OpenSearchConnection>
		_preConnectOpenSearchConnectionConsumer;

}