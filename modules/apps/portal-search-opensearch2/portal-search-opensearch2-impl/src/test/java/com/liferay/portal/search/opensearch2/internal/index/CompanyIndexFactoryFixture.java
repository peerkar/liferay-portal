/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.index;

import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.search.index.IndexNameBuilder;
import com.liferay.portal.search.opensearch2.internal.configuration.OpenSearchConfigurationWrapper;
import com.liferay.portal.search.opensearch2.internal.configuration.OpenSearchConfigurationWrapperImpl;
import com.liferay.portal.search.opensearch2.internal.connection.IndexName;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionManager;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionNotInitializedException;

import java.util.HashMap;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

import org.opensearch.client.opensearch.OpenSearchClient;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

/**
 * @author Adam Brandizzi
 */
public class CompanyIndexFactoryFixture {

	public CompanyIndexFactoryFixture(
		OpenSearchConnectionManager openSearchConnectionManager,
		String indexName) {

		_indexName = indexName;

		_frameworkUtilMockedStatic = _createFrameworkUtil();

		_openSearchConnectionManager = Mockito.mock(
			OpenSearchConnectionManager.class);

		Mockito.when(
			_openSearchConnectionManager.getOpenSearchClient()
		).thenThrow(
			OpenSearchConnectionNotInitializedException.class
		);
	}

	public void createIndices() {
		CompanyIndexFactory companyIndexFactory = getCompanyIndexFactory();

		OpenSearchClient openSearchClient =
			_openSearchConnectionManager.getOpenSearchClient();

		companyIndexFactory.createIndices(
			openSearchClient.indices(), RandomTestUtil.randomLong());
	}

	public void deleteIndices() {
		CompanyIndexFactory companyIndexFactory = getCompanyIndexFactory();

		OpenSearchClient openSearchClient =
			_openSearchConnectionManager.getOpenSearchClient();

		companyIndexFactory.deleteIndices(
			openSearchClient.indices(), RandomTestUtil.randomLong());
	}

	public CompanyIndexFactory getCompanyIndexFactory() {
		if (_companyIndexFactory != null) {
			return _companyIndexFactory;
		}

		_companyIndexFactory = new CompanyIndexFactory();

		ReflectionTestUtil.setFieldValue(
			_companyIndexFactory, "_companyIndexFactoryHelper",
			getCompanyIndexFactoryHelper());
		ReflectionTestUtil.setFieldValue(
			_companyIndexFactory, "_openSearchConfigurationWrapper",
			createOpenSearchConfigurationWrapper());

		ReflectionTestUtil.invoke(
			_companyIndexFactory, "activate",
			new Class<?>[] {BundleContext.class},
			SystemBundleUtil.getBundleContext());

		return _companyIndexFactory;
	}

	public IndexHelper getCompanyIndexFactoryHelper() {
		if (_companyIndexFactoryHelper != null) {
			return _companyIndexFactoryHelper;
		}

		_companyIndexFactoryHelper = new IndexHelperImpl();

		ReflectionTestUtil.setFieldValue(
			_companyIndexFactoryHelper, "_openSearchConfigurationWrapper",
			createOpenSearchConfigurationWrapper());
		ReflectionTestUtil.setFieldValue(
			_companyIndexFactoryHelper, "_openSearchConnectionManager",
			_openSearchConnectionManager);
		ReflectionTestUtil.setFieldValue(
			_companyIndexFactoryHelper, "_indexNameBuilder",
			new TestIndexNameBuilder());
		ReflectionTestUtil.setFieldValue(
			_companyIndexFactoryHelper, "_jsonFactory", new JSONFactoryImpl());

		ReflectionTestUtil.invoke(
			_companyIndexFactoryHelper, "activate",
			new Class<?>[] {BundleContext.class},
			SystemBundleUtil.getBundleContext());

		return _companyIndexFactoryHelper;
	}

	public String getIndexName() {
		IndexName indexName = new IndexName(_indexName);

		return indexName.getName();
	}

	public void tearDown() {
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

	protected OpenSearchConfigurationWrapper
		createOpenSearchConfigurationWrapper() {

		return new OpenSearchConfigurationWrapperImpl() {
			{
				activate(new HashMap<>());
			}
		};
	}

	protected class TestIndexNameBuilder implements IndexNameBuilder {

		@Override
		public String getIndexName(long companyId) {
			return CompanyIndexFactoryFixture.this.getIndexName();
		}

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

	private CompanyIndexFactory _companyIndexFactory;
	private IndexHelper _companyIndexFactoryHelper;
	private MockedStatic<FrameworkUtil> _frameworkUtilMockedStatic;
	private final String _indexName;
	private final OpenSearchConnectionManager _openSearchConnectionManager;

}