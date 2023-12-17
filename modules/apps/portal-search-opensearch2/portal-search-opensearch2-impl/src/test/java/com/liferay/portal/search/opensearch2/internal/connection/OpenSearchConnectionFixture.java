/* *
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 * /

package com.liferay.portal.search.opensearch2.internal.connection;

/ **
 * @author André de Oliveira
 * /
public class OpenSearchConnectionFixture implements OpenSearchConnection {

	public static Builder builder() {
		return new Builder();
	}

	public void createNode() {
		createOpenSearchConnection();

		_elasticsearchConnection.connect();
	}

	public OpenSearchConnection createOpenSearchConnection() {
		PropsUtil.setProps(new PropsImpl());

		OpenSearchConfigurationWrapper elasticsearchConfigurationWrapper =
			new OpenSearchConfigurationWrapper() {
				{
					setOpenSearchConfiguration(
						ConfigurableUtil.createConfigurable(
							OpenSearchConfiguration.class,
							_elasticsearchConfigurationProperties));
				}
			};

		OpenSearchConnection.Builder openSearchConnectionBuilder =
			new OpenSearchConnection.Builder();

		openSearchConnectionBuilder.active(
			true
		).connectionId(
			RandomTestUtil.randomString()
		);

		_elasticsearchConnection = openSearchConnectionBuilder.build();

		return _elasticsearchConnection;
	}

	public void destroyNode() {
		if (_elasticsearchConnection != null) {
			_elasticsearchConnection.close();
		}

		_deleteTmpDir();
	}

	@Override
	public OpenSearchClient getOpenSearchClient() {
		return _elasticsearchConnection.getOpenSearchClient();
	}

	@Override
	public OpenSearchClient getOpenSearchClient(String connectionId) {
		return getOpenSearchClient();
	}

	@Override
	public OpenSearchClient getOpenSearchClient(
		String connectionId, boolean preferLocalCluster) {

		return getOpenSearchClient();
	}

	public Map<String, Object> getOpenSearchConfigurationProperties() {
		return _elasticsearchConfigurationProperties;
	}

	public OpenSearchConnection getOpenSearchConnection() {
		return _elasticsearchConnection;
	}

	public static class Builder {

		public OpenSearchConnectionFixture build() {
			OpenSearchConnectionFixture elasticsearchConnectionFixture =
				new OpenSearchConnectionFixture();

			elasticsearchConnectionFixture.
				_elasticsearchConfigurationProperties =
					createOpenSearchConfigurationProperties(
						_elasticsearchConfigurationProperties, _clusterName);
			elasticsearchConnectionFixture._workPath = _TMP_PATH.resolve(
				_clusterName);

			return elasticsearchConnectionFixture;
		}

		public OpenSearchConnectionFixture.Builder clusterName(
			String clusterName) {

			_clusterName = clusterName;

			return this;
		}

		public Builder elasticsearchConfigurationProperties(
			Map<String, Object> elasticsearchConfigurationProperties) {

			if (elasticsearchConfigurationProperties == null) {
				elasticsearchConfigurationProperties =
					Collections.<String, Object>emptyMap();
			}

			_elasticsearchConfigurationProperties =
				elasticsearchConfigurationProperties;

			return this;
		}

		protected static Map<String, Object>
			createOpenSearchConfigurationProperties(
				Map<String, Object> elasticsearchConfigurationProperties,
				String clusterName) {

			return HashMapBuilder.<String, Object>put(
				"clusterName", clusterName
			).put(
				"configurationPid", OpenSearchConfiguration.class.getName()
			).put(
				"httpCORSAllowOrigin", "*"
			).put(
				"logExceptionsOnly", false
			).put(
				"sidecarHttpPort", HttpPortRange.AUTO
			).put(
				"sidecarJVMOptions", "-Xmx256m"
			).putAll(
				elasticsearchConfigurationProperties
			).build();
		}

		private String _clusterName;
		private Map<String, Object> _elasticsearchConfigurationProperties =
			Collections.<String, Object>emptyMap();

	}

	private OpenSearchInstancePaths _createOpenSearchInstancePaths() {
		OpenSearchInstancePaths elasticsearchInstancePaths = Mockito.mock(
			OpenSearchInstancePaths.class);

		Mockito.doReturn(
			_TMP_PATH.resolve("sidecar-elasticsearch")
		).when(
			elasticsearchInstancePaths
		).getHomePath();

		Mockito.doReturn(
			_workPath
		).when(
			elasticsearchInstancePaths
		).getWorkPath();

		return elasticsearchInstancePaths;
	}

	private void _deleteTmpDir() {
		PathUtil.deleteDir(_workPath);
	}

	private static final Path _TMP_PATH = Paths.get("tmp");

	private Map<String, Object> _elasticsearchConfigurationProperties =
		Collections.<String, Object>emptyMap();
	private OpenSearchConnection _elasticsearchConnection;
	private Path _workPath;

}
*/