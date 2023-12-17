/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal;

import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnection;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionFixture;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionManager;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.snapshot.OpenSearchSnapshotClient;

/**
 * @author André de Oliveira
 */
public class OpenSearchSearchEngineReconnectTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() throws Exception {
		OpenSearchConnectionFixture openSearchConnectionFixture =
			OpenSearchConnectionFixture.builder(
			).clusterName(
				OpenSearchSearchEngineReconnectTest.class.getSimpleName()
			).build();

		OpenSearchSearchEngineFixture openSearchSearchEngineFixture =
			new OpenSearchSearchEngineFixture(openSearchConnectionFixture);

		openSearchSearchEngineFixture.setUp();

		_openSearchConnectionFixture = openSearchConnectionFixture;

		_openSearchSearchEngineFixture = openSearchSearchEngineFixture;
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		_openSearchSearchEngineFixture.tearDown();
	}

	public OpenSearchSnapshotClient getSnapshotClient() {
		OpenSearchClient openSearchClient =
			_openSearchConnectionFixture.getOpenSearchClient();

		return openSearchClient.snapshot();
	}

	@Test
	public void testInitializeAfterReconnect() {
		OpenSearchSearchEngine openSearchSearchEngine =
			_openSearchSearchEngineFixture.getOpenSearchSearchEngine();

		long companyId = RandomTestUtil.randomLong();

		openSearchSearchEngine.initialize(companyId);

		_reconnect(
			_openSearchSearchEngineFixture.getOpenSearchConnectionManager());

		openSearchSearchEngine.initialize(companyId);
	}

	private void _reconnect(
		OpenSearchConnectionManager openSearchConnectionManager) {

		OpenSearchConnection openSearchConnection =
			openSearchConnectionManager.getOpenSearchConnection();

		openSearchConnection.close();

		openSearchConnection.connect();
	}

	private static OpenSearchConnectionFixture _openSearchConnectionFixture;
	private static OpenSearchSearchEngineFixture _openSearchSearchEngineFixture;

}