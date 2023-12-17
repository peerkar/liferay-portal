/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.connection;

import com.liferay.portal.kernel.module.util.SystemBundleUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.search.opensearch2.internal.configuration.OpenSearchConfigurationWrapper;
import com.liferay.portal.search.opensearch2.internal.configuration.OpenSearchConfigurationWrapperImpl;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

import org.opensearch.client.opensearch.OpenSearchClient;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

/**
 * @author André de Oliveira
 */
public class OpenSearchConnectionManagerTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() {
		BundleContext bundleContext = SystemBundleUtil.getBundleContext();

		_frameworkUtilMockedStatic.when(
			() -> FrameworkUtil.getBundle(Mockito.any())
		).thenReturn(
			bundleContext.getBundle()
		);
	}

	@AfterClass
	public static void tearDownClass() {
		_frameworkUtilMockedStatic.close();
	}

	@Before
	public void setUp() {
		_resetAndSetUpMocks();

		_openSearchConnectionManager = _createOpenSearchConnectionManager(
			_sidecarOpenSearchConnection, _remoteOpenSearchConnection1,
			_remoteOpenSearchConnection2, _remoteOpenSearchConnection3);
	}

	@Test
	public void testActivateRemoteModeDisabled() {
		OpenSearchConnectionManager openSearchConnectionManager = Mockito.spy(
			_openSearchConnectionManager);

		openSearchConnectionManager.activate();

		Mockito.verify(
			openSearchConnectionManager, Mockito.never()
		).addOpenSearchConnection(
			Mockito.any()
		);

		Mockito.verify(
			openSearchConnectionManager
		).removeOpenSearchConnection(
			Mockito.any()
		);
	}

	@Test
	public void testActivateRemoteModeEnabledWithConnectionId() {
		Mockito.when(
			_operationModeResolver.isProductionModeEnabled()
		).thenReturn(
			true
		);

		Mockito.when(
			_openSearchConfigurationWrapper.remoteClusterConnectionId()
		).thenReturn(
			"test"
		);

		OpenSearchConnectionManager openSearchConnectionManager = Mockito.spy(
			_openSearchConnectionManager);

		openSearchConnectionManager.activate();

		Mockito.verify(
			openSearchConnectionManager, Mockito.never()
		).addOpenSearchConnection(
			Mockito.any()
		);

		Mockito.verify(
			openSearchConnectionManager, Mockito.never()
		).removeOpenSearchConnection(
			Mockito.any()
		);
	}

	@Test
	public void testActivateRemoteModeEnabledWithoutConnectionId() {
		Mockito.when(
			_operationModeResolver.isProductionModeEnabled()
		).thenReturn(
			true
		);

		Mockito.when(
			_openSearchConfigurationWrapper.remoteClusterConnectionId()
		).thenReturn(
			null
		);

		Mockito.when(
			_openSearchConfigurationWrapper.networkHostAddresses()
		).thenReturn(
			new String[] {"http://localhost:9200"}
		);

		OpenSearchConnectionManager openSearchConnectionManager = Mockito.spy(
			_openSearchConnectionManager);

		openSearchConnectionManager.activate();

		Mockito.verify(
			openSearchConnectionManager
		).addOpenSearchConnection(
			Mockito.any()
		);

		Mockito.verify(
			openSearchConnectionManager, Mockito.never()
		).removeOpenSearchConnection(
			Mockito.any()
		);
	}

	@Test
	public void testAddConnectionNoConnectionIdAndIsActive() {
		OpenSearchConnection openSearchConnection = Mockito.mock(
			OpenSearchConnection.class);

		Mockito.when(
			openSearchConnection.getConnectionId()
		).thenReturn(
			null
		);
		Mockito.when(
			openSearchConnection.isActive()
		).thenReturn(
			true
		);

		_openSearchConnectionManager.addOpenSearchConnection(
			openSearchConnection);

		Mockito.verify(
			openSearchConnection, Mockito.never()
		).isActive();

		Mockito.verify(
			openSearchConnection, Mockito.never()
		).connect();
	}

	@Test
	public void testAddConnectionNoConnectionIdAndIsNotActive() {
		OpenSearchConnection openSearchConnection = Mockito.mock(
			OpenSearchConnection.class);

		Mockito.when(
			openSearchConnection.getConnectionId()
		).thenReturn(
			null
		);
		Mockito.when(
			openSearchConnection.isActive()
		).thenReturn(
			false
		);

		_openSearchConnectionManager.addOpenSearchConnection(
			openSearchConnection);

		Mockito.verify(
			openSearchConnection, Mockito.never()
		).isActive();

		Mockito.verify(
			openSearchConnection, Mockito.never()
		).connect();
	}

	@Test
	public void testAddConnectionWithConnectionIdAndIsActive() {
		OpenSearchConnection openSearchConnection = Mockito.mock(
			OpenSearchConnection.class);

		Mockito.when(
			openSearchConnection.getConnectionId()
		).thenReturn(
			"test"
		);
		Mockito.when(
			openSearchConnection.isActive()
		).thenReturn(
			true
		);

		_openSearchConnectionManager.addOpenSearchConnection(
			openSearchConnection);

		Mockito.verify(
			openSearchConnection
		).isActive();

		Mockito.verify(
			openSearchConnection
		).connect();
	}

	@Test
	public void testAddConnectionWithConnectionIdAndIsNotActive() {
		OpenSearchConnection openSearchConnection = Mockito.mock(
			OpenSearchConnection.class);

		Mockito.when(
			openSearchConnection.getConnectionId()
		).thenReturn(
			"test"
		);
		Mockito.when(
			openSearchConnection.isActive()
		).thenReturn(
			false
		);

		_openSearchConnectionManager.addOpenSearchConnection(
			openSearchConnection);

		Mockito.verify(
			openSearchConnection
		).isActive();

		Mockito.verify(
			openSearchConnection, Mockito.never()
		).connect();
	}

	@Test
	public void testGetExplicitOpenSearchClientWhenRestClientNull() {
		try {
			_openSearchConnectionManager.getOpenSearchClient(
				_REMOTE_3_CONNECTION_ID);

			Assert.fail();
		}
		catch (OpenSearchConnectionNotInitializedException
					openSearchConnectionNotInitializedException) {

			String message =
				openSearchConnectionNotInitializedException.getMessage();

			Assert.assertTrue(
				message.contains("REST high level client not found"));
		}
	}

	@Test
	public void testGetExplicitOpenSearchClientWithRemoteModeDisabled() {
		Assert.assertEquals(
			_remoteOpenSearchConnection1.getOpenSearchClient(),
			_openSearchConnectionManager.getOpenSearchClient(
				_REMOTE_1_CONNECTION_ID));

		Assert.assertEquals(
			_remoteOpenSearchConnection2.getOpenSearchClient(),
			_openSearchConnectionManager.getOpenSearchClient(
				_REMOTE_2_CONNECTION_ID));
	}

	@Test
	public void testGetExplicitOpenSearchClientWithRemoteModeDisabledAndConnectionDoesNotExist() {
		try {
			_openSearchConnectionManager.getOpenSearchClient("none");

			Assert.fail();
		}
		catch (OpenSearchConnectionNotInitializedException
					openSearchConnectionNotInitializedException) {

			String message =
				openSearchConnectionNotInitializedException.getMessage();

			Assert.assertTrue(
				message.contains("OpenSearch connection not found"));
		}
	}

	@Test
	public void testGetExplicitOpenSearchClientWithRemoteModeDisabledAndDifferentConnectionId() {
		_setRemoteConnectionId(_REMOTE_1_CONNECTION_ID);

		Assert.assertEquals(
			_remoteOpenSearchConnection2.getOpenSearchClient(),
			_openSearchConnectionManager.getOpenSearchClient(
				_REMOTE_2_CONNECTION_ID));
	}

	@Test
	public void testGetExplicitOpenSearchClientWithRemoteModeDisabledAndIdNull() {
		Assert.assertEquals(
			_sidecarOpenSearchConnection.getOpenSearchClient(),
			_openSearchConnectionManager.getOpenSearchClient(null));
	}

	@Test
	public void testGetExplicitOpenSearchClientWithRemoteModeEnabled() {
		_enableRemoteMode();

		Assert.assertEquals(
			_remoteOpenSearchConnection1.getOpenSearchClient(),
			_openSearchConnectionManager.getOpenSearchClient(
				_REMOTE_1_CONNECTION_ID));

		Assert.assertEquals(
			_remoteOpenSearchConnection2.getOpenSearchClient(),
			_openSearchConnectionManager.getOpenSearchClient(
				_REMOTE_2_CONNECTION_ID));
	}

	@Test
	public void testGetExplicitOpenSearchClientWithRemoteModeEnabledAndConnectionDoesNotExist() {
		_enableRemoteMode();

		try {
			_openSearchConnectionManager.getOpenSearchClient("none");

			Assert.fail();
		}
		catch (OpenSearchConnectionNotInitializedException
					openSearchConnectionNotInitializedException) {

			String message =
				openSearchConnectionNotInitializedException.getMessage();

			Assert.assertTrue(
				message.contains("OpenSearch connection not found"));
		}
	}

	@Test
	public void testGetExplicitOpenSearchClientWithRemoteModeEnabledAndDifferentConnectionId() {
		_enableRemoteMode();
		_setRemoteConnectionId(_REMOTE_1_CONNECTION_ID);

		Assert.assertEquals(
			_remoteOpenSearchConnection2.getOpenSearchClient(),
			_openSearchConnectionManager.getOpenSearchClient(
				_REMOTE_2_CONNECTION_ID));
	}

	@Test
	public void testGetExplicitOpenSearchClientWithRemoteModeEnabledAndIdNull() {
		_enableRemoteMode();

		Assert.assertEquals(
			_defaultRemoteOpenSearchConnection.getOpenSearchClient(),
			_openSearchConnectionManager.getOpenSearchClient(null));
	}

	@Test
	public void testGetExplicitOpenSearchConnectionWhenConnectionDoesNotExist() {
		Assert.assertEquals(
			null, _openSearchConnectionManager.getOpenSearchConnection("none"));
	}

	@Test
	public void testGetExplicitOpenSearchConnectionWhenConnectionIdNull() {
		try {
			_openSearchConnectionManager.getOpenSearchConnection(null);

			Assert.fail();
		}
		catch (NullPointerException nullPointerException) {
		}
	}

	@Test
	public void testGetExplicitOpenSearchConnectionWithDifferentConnectionId() {
		_setRemoteConnectionId(_REMOTE_1_CONNECTION_ID);

		Assert.assertEquals(
			_remoteOpenSearchConnection2,
			_openSearchConnectionManager.getOpenSearchConnection(
				_REMOTE_2_CONNECTION_ID));
	}

	@Test
	public void testGetExplicitOpenSearchConnectionWithRemoteModeDisabled() {
		Assert.assertEquals(
			_remoteOpenSearchConnection1,
			_openSearchConnectionManager.getOpenSearchConnection(
				_REMOTE_1_CONNECTION_ID));

		Assert.assertEquals(
			_remoteOpenSearchConnection2,
			_openSearchConnectionManager.getOpenSearchConnection(
				_REMOTE_2_CONNECTION_ID));
	}

	@Test
	public void testGetExplicitOpenSearchConnectionWithRemoteModeEnabled() {
		_enableRemoteMode();

		Assert.assertEquals(
			_remoteOpenSearchConnection1,
			_openSearchConnectionManager.getOpenSearchConnection(
				_REMOTE_1_CONNECTION_ID));

		Assert.assertEquals(
			_remoteOpenSearchConnection2,
			_openSearchConnectionManager.getOpenSearchConnection(
				_REMOTE_2_CONNECTION_ID));
	}

	@Test
	public void testGetExplicitOpenSearchConnectionWithRemoteModeEnabledAndDifferentConnectionId() {
		_enableRemoteMode();
		_setRemoteConnectionId(_REMOTE_1_CONNECTION_ID);

		Assert.assertEquals(
			_remoteOpenSearchConnection2,
			_openSearchConnectionManager.getOpenSearchConnection(
				_REMOTE_2_CONNECTION_ID));
	}

	@Test
	public void testGetOpenSearchClientWithRemoteModeDisabled() {
		Assert.assertEquals(
			_sidecarOpenSearchConnection.getOpenSearchClient(),
			_openSearchConnectionManager.getOpenSearchClient());
	}

	@Test
	public void testGetOpenSearchClientWithRemoteModeDisabledAndConnectionId() {
		_setRemoteConnectionId(_REMOTE_1_CONNECTION_ID);

		Assert.assertEquals(
			_sidecarOpenSearchConnection.getOpenSearchClient(),
			_openSearchConnectionManager.getOpenSearchClient());

		_setRemoteConnectionId(_REMOTE_2_CONNECTION_ID);

		Assert.assertEquals(
			_sidecarOpenSearchConnection.getOpenSearchClient(),
			_openSearchConnectionManager.getOpenSearchClient());
	}

	@Test
	public void testGetOpenSearchClientWithRemoteModeEnabled() {
		_enableRemoteMode();

		Assert.assertEquals(
			_defaultRemoteOpenSearchConnection.getOpenSearchClient(),
			_openSearchConnectionManager.getOpenSearchClient());
	}

	@Test
	public void testGetOpenSearchClientWithRemoteModeEnabledAndConnectionId() {
		_enableRemoteMode();
		_setRemoteConnectionId(_REMOTE_1_CONNECTION_ID);

		Assert.assertEquals(
			_remoteOpenSearchConnection1.getOpenSearchClient(),
			_openSearchConnectionManager.getOpenSearchClient());

		_setRemoteConnectionId(_REMOTE_2_CONNECTION_ID);

		Assert.assertEquals(
			_remoteOpenSearchConnection2.getOpenSearchClient(),
			_openSearchConnectionManager.getOpenSearchClient());
	}

	@Test
	public void testGetOpenSearchConnectionWithRemoteModeDisabled() {
		Assert.assertEquals(
			_sidecarOpenSearchConnection,
			_openSearchConnectionManager.getOpenSearchConnection());
	}

	@Test
	public void testGetOpenSearchConnectionWithRemoteModeEnabled() {
		_enableRemoteMode();

		Assert.assertEquals(
			_defaultRemoteOpenSearchConnection,
			_openSearchConnectionManager.getOpenSearchConnection());
	}

	@Test
	public void testGetOpenSearchConnectionWithRemoteModeEnabledAndConnectionId() {
		_enableRemoteMode();
		_setRemoteConnectionId(_REMOTE_1_CONNECTION_ID);

		Assert.assertEquals(
			_remoteOpenSearchConnection1,
			_openSearchConnectionManager.getOpenSearchConnection());

		_setRemoteConnectionId(_REMOTE_2_CONNECTION_ID);

		Assert.assertEquals(
			_remoteOpenSearchConnection2,
			_openSearchConnectionManager.getOpenSearchConnection());
	}

	@Test
	public void testRemoveConnectionThatDoesNotExistWithConnectionId() {
		OpenSearchConnection openSearchConnection = Mockito.mock(
			OpenSearchConnection.class);

		Mockito.when(
			openSearchConnection.getConnectionId()
		).thenReturn(
			"test"
		);

		_openSearchConnectionManager.removeOpenSearchConnection(
			openSearchConnection.getConnectionId());

		Mockito.verify(
			openSearchConnection, Mockito.never()
		).close();
	}

	@Test
	public void testRemoveConnectionThatExistsWithConnectionId() {
		_openSearchConnectionManager.removeOpenSearchConnection(
			_remoteOpenSearchConnection1.getConnectionId());

		Mockito.verify(
			_remoteOpenSearchConnection1
		).close();
	}

	@Test
	public void testRemoveConnectionWithNullConnectionId() {
		_openSearchConnectionManager.removeOpenSearchConnection(null);
	}

	private OpenSearchConnectionManager _createOpenSearchConnectionManager(
		OpenSearchConnection remoteOpenSearchConnection1,
		OpenSearchConnection remoteOpenSearchConnection2,
		OpenSearchConnection remoteOpenSearchConnection3,
		OpenSearchConnection sidecarOpenSearchConnection) {

		OpenSearchConnectionManager openSearchConnectionManager =
			new OpenSearchConnectionManager() {
				{
					http = _http;
					openSearchConfigurationWrapper =
						_openSearchConfigurationWrapper;
					operationModeResolver = _operationModeResolver;
				}
			};

		openSearchConnectionManager.addOpenSearchConnection(
			remoteOpenSearchConnection1);
		openSearchConnectionManager.addOpenSearchConnection(
			remoteOpenSearchConnection2);
		openSearchConnectionManager.addOpenSearchConnection(
			remoteOpenSearchConnection3);
		openSearchConnectionManager.addOpenSearchConnection(
			sidecarOpenSearchConnection);

		openSearchConnectionManager.activate();

		return openSearchConnectionManager;
	}

	private void _enableRemoteMode() {
		Mockito.when(
			_operationModeResolver.isProductionModeEnabled()
		).thenReturn(
			true
		);

		Mockito.when(
			_operationModeResolver.isDevelopmentModeEnabled()
		).thenReturn(
			false
		);

		_openSearchConnectionManager.addOpenSearchConnection(
			_defaultRemoteOpenSearchConnection);
	}

	private void _resetAndSetUpMocks() {
		Mockito.reset(
			_defaultRemoteOpenSearchConnection, _openSearchConfigurationWrapper,
			_remoteOpenSearchConnection1, _remoteOpenSearchConnection2,
			_remoteOpenSearchConnection3, _sidecarOpenSearchConnection);

		_setUpDefaultConnection();
		_setUpRemoteConnection1();
		_setUpRemoteConnection2();
		_setUpRemoteConnection3();
	}

	private void _setRemoteConnectionId(String connectionId) {
		Mockito.when(
			_openSearchConfigurationWrapper.remoteClusterConnectionId()
		).thenReturn(
			connectionId
		);
	}

	private void _setUpDefaultConnection() {
		Mockito.when(
			_defaultRemoteOpenSearchConnection.getConnectionId()
		).thenReturn(
			"__REMOTE__"
		);
		Mockito.when(
			_defaultRemoteOpenSearchConnection.getOpenSearchClient()
		).thenReturn(
			Mockito.mock(OpenSearchClient.class)
		);
		Mockito.when(
			_defaultRemoteOpenSearchConnection.isActive()
		).thenReturn(
			true
		);
	}

	private void _setUpRemoteConnection1() {
		Mockito.when(
			_remoteOpenSearchConnection1.getConnectionId()
		).thenReturn(
			_REMOTE_1_CONNECTION_ID
		);
		Mockito.when(
			_remoteOpenSearchConnection1.getOpenSearchClient()
		).thenReturn(
			Mockito.mock(OpenSearchClient.class)
		);
		Mockito.when(
			_remoteOpenSearchConnection1.isActive()
		).thenReturn(
			true
		);
	}

	private void _setUpRemoteConnection2() {
		Mockito.when(
			_remoteOpenSearchConnection2.getConnectionId()
		).thenReturn(
			_REMOTE_2_CONNECTION_ID
		);
		Mockito.when(
			_remoteOpenSearchConnection2.getOpenSearchClient()
		).thenReturn(
			Mockito.mock(OpenSearchClient.class)
		);
		Mockito.when(
			_remoteOpenSearchConnection2.isActive()
		).thenReturn(
			true
		);
	}

	private void _setUpRemoteConnection3() {
		Mockito.when(
			_remoteOpenSearchConnection3.getConnectionId()
		).thenReturn(
			_REMOTE_3_CONNECTION_ID
		);
		Mockito.when(
			_remoteOpenSearchConnection3.getOpenSearchClient()
		).thenReturn(
			null
		);
		Mockito.when(
			_remoteOpenSearchConnection3.isActive()
		).thenReturn(
			false
		);
	}

	private static final String _REMOTE_1_CONNECTION_ID = "remote 1";

	private static final String _REMOTE_2_CONNECTION_ID = "remote 2";

	private static final String _REMOTE_3_CONNECTION_ID = "remote 3";

	private static final MockedStatic<FrameworkUtil>
		_frameworkUtilMockedStatic = Mockito.mockStatic(FrameworkUtil.class);

	private final OpenSearchConnection _defaultRemoteOpenSearchConnection =
		Mockito.mock(OpenSearchConnection.class);
	private final Http _http = Mockito.mock(Http.class);
	private final OpenSearchConfigurationWrapper
		_openSearchConfigurationWrapper = Mockito.mock(
			OpenSearchConfigurationWrapperImpl.class);
	private OpenSearchConnectionManager _openSearchConnectionManager;
	private final OpenSearchConnection _remoteOpenSearchConnection1 =
		Mockito.mock(OpenSearchConnection.class);
	private final OpenSearchConnection _remoteOpenSearchConnection2 =
		Mockito.mock(OpenSearchConnection.class);
	private final OpenSearchConnection _remoteOpenSearchConnection3 =
		Mockito.mock(OpenSearchConnection.class);
	private final OpenSearchConnection _sidecarOpenSearchConnection =
		Mockito.mock(OpenSearchConnection.class);

}