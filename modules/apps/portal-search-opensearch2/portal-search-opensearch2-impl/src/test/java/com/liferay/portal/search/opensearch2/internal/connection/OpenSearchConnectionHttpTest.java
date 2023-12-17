/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.connection;

import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.io.InputStream;

import java.net.URL;

import java.util.List;

import org.apache.http.HttpHost;

import org.hamcrest.CoreMatchers;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import org.opensearch.client.Node;
import org.opensearch.client.RestClient;
import org.opensearch.client.opensearch.OpenSearchClient;

/**
 * @author André de Oliveira
 */
public class OpenSearchConnectionHttpTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() {
		String clusterName = RandomTestUtil.randomString();

		OpenSearchConnectionFixture openSearchConnectionFixture =
			OpenSearchConnectionFixture.builder(
			).clusterName(
				OpenSearchConnectionHttpTest.class.getSimpleName()
			).elasticsearchConfigurationProperties(
				HashMapBuilder.<String, Object>put(
					"clusterName", clusterName
				).put(
					"networkHost", "_site_"
				).build()
			).build();

		openSearchConnectionFixture.createNode();

		_clusterName = clusterName;
		_openSearchConnectionFixture = openSearchConnectionFixture;
	}

	@AfterClass
	public static void tearDownClass() {
		_openSearchConnectionFixture.destroyNode();
	}

	@Test
	public void testHttpLocallyAvailableRegardlessOfNetworkHost()
		throws Exception {

		String status = toString(new URL("http://localhost:" + getHttpPort()));

		Assert.assertThat(
			status,
			CoreMatchers.containsString(
				"\"cluster_name\" : \"" + _clusterName));
	}

	protected int getHttpPort() {
		OpenSearchClient openSearchClient =
			_openSearchConnectionFixture.getOpenSearchClient();

		RestClient restClient = openSearchClient.getLowLevelClient();

		List<Node> nodes = restClient.getNodes();

		Node node = nodes.get(0);

		HttpHost httpHost = node.getHost();

		return httpHost.getPort();
	}

	protected String toString(URL url) throws Exception {
		try (InputStream inputStream = url.openStream()) {
			return StringUtil.read(inputStream);
		}
	}

	private static String _clusterName;
	private static OpenSearchConnectionFixture _openSearchConnectionFixture;

}