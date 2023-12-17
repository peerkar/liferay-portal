/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;
import com.liferay.portal.kernel.search.SearchEngine;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.index.IndexNameBuilder;
import com.liferay.portal.search.opensearch2.configuration.OpenSearchConfiguration;
import com.liferay.portal.search.opensearch2.internal.configuration.OpenSearchConfigurationWrapper;
import com.liferay.portal.search.opensearch2.internal.configuration.OpenSearchConfigurationWrapperImpl;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnection;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionFixture;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionManager;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionManagerImpl;
import com.liferay.portal.search.opensearch2.internal.index.CompanyIdIndexNameBuilder;
import com.liferay.portal.search.opensearch2.internal.index.CompanyIndexFactory;
import com.liferay.portal.search.opensearch2.internal.index.IndexConfigurationDynamicUpdatesExecutor;
import com.liferay.portal.search.opensearch2.internal.index.IndexHelper;
import com.liferay.portal.search.opensearch2.internal.index.IndexHelperImpl;
import com.liferay.portal.search.opensearch2.internal.search.engine.adapter.OpenSearchEngineAdapterFixture;
import com.liferay.portal.search.test.util.search.engine.SearchEngineFixture;

import java.util.Map;
import java.util.Objects;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

/**
 * @author Adam Brandizzi
 */
public class OpenSearchSearchEngineFixture implements SearchEngineFixture {

	public OpenSearchSearchEngineFixture(
		OpenSearchConnectionFixture openSearchConnectionFixture) {

		_openSearchConnectionFixture = openSearchConnectionFixture;
	}

	@Override
	public IndexNameBuilder getIndexNameBuilder() {
		return _indexNameBuilder;
	}

	public OpenSearchConnectionManager getOpenSearchConnectionManager() {
		return _openSearchConnectionManager;
	}

	public OpenSearchSearchEngine getOpenSearchSearchEngine() {
		return _openSearchSearchEngine;
	}

	@Override
	public SearchEngine getSearchEngine() {
		return getOpenSearchSearchEngine();
	}

	@Override
	public void setUp() throws Exception {
		OpenSearchConnectionFixture openSearchConnectionFixture =
			Objects.requireNonNull(_openSearchConnectionFixture);

		CompanyIdIndexNameBuilder indexNameBuilder = _createIndexNameBuilder();

		_frameworkUtilMockedStatic = _createFrameworkUtil();
		_indexNameBuilder = indexNameBuilder;

		OpenSearchConnectionManager openSearchConnectionManager =
			_createOpenSearchConnectionManager(openSearchConnectionFixture);

		_openSearchConnectionManager = openSearchConnectionManager;
		_openSearchSearchEngine = _createOpenSearchSearchEngine(
			openSearchConnectionManager,
			Mockito.mock(IndexConfigurationDynamicUpdatesExecutor.class),
			indexNameBuilder,
			openSearchConnectionFixture.getOpenSearchConfigurationProperties());
	}

	@Override
	public void tearDown() throws Exception {
		_openSearchConnectionFixture.destroyNode();

		_openSearchEngineAdapterFixture.tearDown();

		if (_companyIndexFactory != null) {
			ReflectionTestUtil.invoke(
				_companyIndexFactory, "deactivate", new Class<?>[0]);

			_companyIndexFactory = null;
		}

		if (_companyIndexFactoryHelper != null) {
			ReflectionTestUtil.invoke(
				_companyIndexFactoryHelper, "deactivate", new Class<?>[0]);

			_companyIndexFactoryHelper = null;
		}

		if (_frameworkUtilMockedStatic != null) {
			_frameworkUtilMockedStatic.close();

			_frameworkUtilMockedStatic = null;
		}
	}

	protected static OpenSearchConfigurationWrapper
		createOpenSearchConfigurationWrapper(Map<String, Object> properties) {

		return new OpenSearchConfigurationWrapperImpl() {
			{
				setOpenSearchConfiguration(
					ConfigurableUtil.createConfigurable(
						OpenSearchConfiguration.class, properties));
			}
		};
	}

	private CompanyIndexFactory _createCompanyIndexFactory(
		IndexNameBuilder indexNameBuilder, Map<String, Object> properites) {

		_companyIndexFactory = new CompanyIndexFactory();

		_companyIndexFactoryHelper = new IndexHelperImpl();

		ReflectionTestUtil.setFieldValue(
			_companyIndexFactoryHelper, "_openSearchConfigurationWrapper",
			createOpenSearchConfigurationWrapper(properites));
		ReflectionTestUtil.setFieldValue(
			_companyIndexFactoryHelper, "_indexNameBuilder", indexNameBuilder);
		ReflectionTestUtil.setFieldValue(
			_companyIndexFactoryHelper, "_jsonFactory", new JSONFactoryImpl());

		ReflectionTestUtil.invoke(
			_companyIndexFactoryHelper, "activate",
			new Class<?>[] {BundleContext.class},
			SystemBundleUtil.getBundleContext());

		ReflectionTestUtil.setFieldValue(
			_companyIndexFactory, "_companyIndexFactoryHelper",
			_companyIndexFactoryHelper);

		ReflectionTestUtil.setFieldValue(
			_companyIndexFactory, "_openSearchConfigurationWrapper",
			createOpenSearchConfigurationWrapper(properites));

		ReflectionTestUtil.invoke(
			_companyIndexFactory, "activate",
			new Class<?>[] {BundleContext.class},
			SystemBundleUtil.getBundleContext());

		return _companyIndexFactory;
	}

	private MockedStatic<FrameworkUtil> _createFrameworkUtil() {
		MockedStatic<FrameworkUtil> frameworkUtilMockedStatic =
			Mockito.mockStatic(FrameworkUtil.class);

		BundleContext bundleContext = SystemBundleUtil.getBundleContext();

		frameworkUtilMockedStatic.when(
			() -> FrameworkUtil.getBundle(Mockito.any())
		).thenReturn(
			bundleContext.getBundle()
		);

		return frameworkUtilMockedStatic;
	}

	private CompanyIdIndexNameBuilder _createIndexNameBuilder() {
		return new CompanyIdIndexNameBuilder() {
			{
				setIndexNamePrefix(null);
			}
		};
	}

	private OpenSearchConnectionManager _createOpenSearchConnectionManager(
		OpenSearchConnectionFixture openSearchConnectionFixture) {

		return new OpenSearchConnectionManagerImpl() {
			{
				openSearchConfigurationWrapper =
					createOpenSearchConfigurationWrapper(
						openSearchConnectionFixture.
							getOpenSearchConfigurationProperties());

				OpenSearchConnection openSearchConnection =
					openSearchConnectionFixture.createOpenSearchConnection();

				addOpenSearchConnection(openSearchConnection);

				getOpenSearchConnection(openSearchConnection.getConnectionId());
			}
		};
	}

	private OpenSearchSearchEngine _createOpenSearchSearchEngine(
		OpenSearchConnectionManager openSearchConnectionManager,
		IndexConfigurationDynamicUpdatesExecutor
			indexConfigurationDynamicUpdatesExecutor,
		IndexNameBuilder indexNameBuilder, Map<String, Object> properites) {

		OpenSearchSearchEngine openSearchSearchEngine =
			new OpenSearchSearchEngine();

		ReflectionTestUtil.setFieldValue(
			openSearchSearchEngine, "_openSearchConnectionManager",
			openSearchConnectionManager);
		ReflectionTestUtil.setFieldValue(
			openSearchSearchEngine, "_indexConfigurationDynamicUpdatesExecutor",
			indexConfigurationDynamicUpdatesExecutor);
		ReflectionTestUtil.setFieldValue(
			openSearchSearchEngine, "_indexFactory",
			_createCompanyIndexFactory(indexNameBuilder, properites));
		ReflectionTestUtil.setFieldValue(
			openSearchSearchEngine, "_indexNameBuilder",
			(IndexNameBuilder)String::valueOf);
		ReflectionTestUtil.setFieldValue(
			openSearchSearchEngine, "_searchEngineAdapter",
			_createSearchEngineAdapter(openSearchConnectionManager));

		return openSearchSearchEngine;
	}

	private SearchEngineAdapter _createSearchEngineAdapter(
		OpenSearchConnectionManager openSearchConnectionManager) {

		_openSearchEngineAdapterFixture = new OpenSearchEngineAdapterFixture() {
			{
				setOpenSearchConnectionManager(openSearchConnectionManager);
			}
		};

		_openSearchEngineAdapterFixture.setUp();

		return _openSearchEngineAdapterFixture.getSearchEngineAdapter();
	}

	private CompanyIndexFactory _companyIndexFactory;
	private IndexHelper _companyIndexFactoryHelper;
	private MockedStatic<FrameworkUtil> _frameworkUtilMockedStatic;
	private IndexNameBuilder _indexNameBuilder;
	private final OpenSearchConnectionFixture _openSearchConnectionFixture;
	private OpenSearchConnectionManager _openSearchConnectionManager;
	private OpenSearchEngineAdapterFixture _openSearchEngineAdapterFixture;
	private OpenSearchSearchEngine _openSearchSearchEngine;

}