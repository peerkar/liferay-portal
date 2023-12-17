/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.search.IndexSearcher;
import com.liferay.portal.kernel.search.IndexWriter;
import com.liferay.portal.kernel.search.suggest.QuerySuggester;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.Localization;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.index.IndexNameBuilder;
import com.liferay.portal.search.internal.legacy.searcher.SearchRequestBuilderFactoryImpl;
import com.liferay.portal.search.internal.legacy.searcher.SearchResponseBuilderFactoryImpl;
import com.liferay.portal.search.opensearch2.configuration.OpenSearchConfiguration;
import com.liferay.portal.search.opensearch2.internal.configuration.OpenSearchConfigurationWrapper;
import com.liferay.portal.search.opensearch2.internal.configuration.OpenSearchConfigurationWrapperImpl;
import com.liferay.portal.search.opensearch2.internal.connection.IndexCreator;
import com.liferay.portal.search.opensearch2.internal.connection.IndexName;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionManager;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.search.opensearch2.internal.connection.helper.IndexCreationHelper;
import com.liferay.portal.search.opensearch2.internal.facet.FacetProcessor;
import com.liferay.portal.search.opensearch2.internal.search.engine.adapter.OpenSearchEngineAdapterFixture;
import com.liferay.portal.search.test.util.indexing.IndexingFixture;
import com.liferay.portal.util.DigesterImpl;
import com.liferay.portal.util.LocalizationImpl;

import java.util.Map;

import org.mockito.Mockito;

import org.opensearch.client.opensearch.core.SearchRequest;

/**
 * @author André de Oliveira
 */
public class OpenSearchIndexingFixture implements IndexingFixture {

	public OpenSearchIndexingFixture() {
		_companyId = RandomTestUtil.randomLong();
	}

	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public IndexSearcher getIndexSearcher() {
		return _indexSearcher;
	}

	@Override
	public IndexWriter getIndexWriter() {
		return _indexWriter;
	}

	public OpenSearchConnectionManager getOpenSearchConnectionManager() {
		return _openSearchFixture;
	}

	@Override
	public SearchEngineAdapter getSearchEngineAdapter() {
		return _searchEngineAdapter;
	}

	@Override
	public boolean isSearchEngineAvailable() {
		return true;
	}

	public void setIndexCreationHelper(
		IndexCreationHelper indexCreationHelper) {

		_indexCreationHelper = indexCreationHelper;
	}

	@Override
	public void setUp() throws Exception {
		_openSearchFixture.setUp();

		OpenSearchEngineAdapterFixture openSearchEngineAdapterFixture =
			_createOpenSearchEngineAdapterFixture(
				_openSearchFixture, _facetProcessor);

		openSearchEngineAdapterFixture.setUp();

		SearchEngineAdapter searchEngineAdapter =
			openSearchEngineAdapterFixture.getSearchEngineAdapter();

		IndexNameBuilder indexNameBuilder = String::valueOf;

		Localization localization = new LocalizationImpl();

		OpenSearchIndexSearcher openSearchIndexSearcher = _createIndexSearcher(
			_openSearchFixture, searchEngineAdapter, indexNameBuilder,
			localization);

		IndexWriter indexWriter = _createIndexWriter(
			indexNameBuilder, localization, _openSearchFixture,
			searchEngineAdapter);

		_indexSearcher = openSearchIndexSearcher;
		_indexWriter = indexWriter;
		_searchEngineAdapter = searchEngineAdapter;

		_createIndex(indexNameBuilder);
	}

	@Override
	public void tearDown() throws Exception {
		_openSearchFixture.tearDown();
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

	protected void setFacetProcessor(
		FacetProcessor<SearchRequest.Builder> facetProcessor) {

		_facetProcessor = facetProcessor;
	}

	protected void setLiferayMappingsAddedToIndex(
		boolean liferayMappingsAddedToIndex) {

		_liferayMappingsAddedToIndex = liferayMappingsAddedToIndex;
	}

	protected void setOpenSearchFixture(OpenSearchFixture openSearchFixture) {
		_openSearchFixture = openSearchFixture;
	}

	private void _createIndex(IndexNameBuilder indexNameBuilder) {
		IndexCreator indexCreator = new IndexCreator() {
			{
				setIndexCreationHelper(_indexCreationHelper);
				setLiferayMappingsAddedToIndex(_liferayMappingsAddedToIndex);
				setOpenSearchConnectionManager(_openSearchFixture);
			}
		};

		indexCreator.createIndex(
			new IndexName(indexNameBuilder.getIndexName(_companyId)));
	}

	private OpenSearchIndexSearcher _createIndexSearcher(
		OpenSearchFixture openSearchFixture,
		SearchEngineAdapter searchEngineAdapter,
		IndexNameBuilder indexNameBuilder, Localization localization) {

		OpenSearchIndexSearcher openSearchIndexSearcher =
			new OpenSearchIndexSearcher();

		ReflectionTestUtil.setFieldValue(
			openSearchIndexSearcher, "_indexNameBuilder", indexNameBuilder);
		ReflectionTestUtil.setFieldValue(
			openSearchIndexSearcher, "_openSearchConfigurationWrapper",
			createOpenSearchConfigurationWrapper(
				openSearchFixture.getOpenSearchConfigurationProperties()));
		ReflectionTestUtil.setFieldValue(
			openSearchIndexSearcher, "_props", _createProps());
		ReflectionTestUtil.setFieldValue(
			openSearchIndexSearcher, "_querySuggester",
			_createOpenSearchQuerySuggester(
				indexNameBuilder, localization, searchEngineAdapter));
		ReflectionTestUtil.setFieldValue(
			openSearchIndexSearcher, "_searchEngineAdapter",
			searchEngineAdapter);
		ReflectionTestUtil.setFieldValue(
			openSearchIndexSearcher, "_searchRequestBuilderFactory",
			new SearchRequestBuilderFactoryImpl());
		ReflectionTestUtil.setFieldValue(
			openSearchIndexSearcher, "_searchResponseBuilderFactory",
			new SearchResponseBuilderFactoryImpl());

		return openSearchIndexSearcher;
	}

	private IndexWriter _createIndexWriter(
		IndexNameBuilder indexNameBuilder, Localization localization,
		OpenSearchFixture openSearchFixture,
		SearchEngineAdapter searchEngineAdapter) {

		OpenSearchIndexWriter openSearchIndexWriter =
			new OpenSearchIndexWriter();

		ReflectionTestUtil.setFieldValue(
			openSearchIndexWriter, "_indexNameBuilder", indexNameBuilder);
		ReflectionTestUtil.setFieldValue(
			openSearchIndexWriter, "_openSearchConfigurationWrapper",
			createOpenSearchConfigurationWrapper(
				openSearchFixture.getOpenSearchConfigurationProperties()));
		ReflectionTestUtil.setFieldValue(
			openSearchIndexWriter, "_searchEngineAdapter", searchEngineAdapter);
		ReflectionTestUtil.setFieldValue(
			openSearchIndexWriter, "_spellCheckIndexWriter",
			_createOpenSearchSpellCheckIndexWriter(
				indexNameBuilder, localization, searchEngineAdapter));

		return openSearchIndexWriter;
	}

	private OpenSearchEngineAdapterFixture
		_createOpenSearchEngineAdapterFixture(
			OpenSearchConnectionManager openSearchConnectionManager,
			FacetProcessor<SearchRequest.Builder> facetProcessor) {

		return new OpenSearchEngineAdapterFixture() {
			{
				setFacetProcessor(facetProcessor);
				setOpenSearchConnectionManager(openSearchConnectionManager);
			}
		};
	}

	private QuerySuggester _createOpenSearchQuerySuggester(
		IndexNameBuilder indexNameBuilder, Localization localization,
		SearchEngineAdapter searchEngineAdapter) {

		OpenSearchQuerySuggester openSearchQuerySuggester =
			new OpenSearchQuerySuggester() {
				{
					setLocalization(localization);
				}
			};

		ReflectionTestUtil.setFieldValue(
			openSearchQuerySuggester, "_indexNameBuilder", indexNameBuilder);
		ReflectionTestUtil.setFieldValue(
			openSearchQuerySuggester, "_searchEngineAdapter",
			searchEngineAdapter);

		return openSearchQuerySuggester;
	}

	private OpenSearchSpellCheckIndexWriter
		_createOpenSearchSpellCheckIndexWriter(
			IndexNameBuilder indexNameBuilder, Localization localization,
			SearchEngineAdapter searchEngineAdapter) {

		OpenSearchSpellCheckIndexWriter openSearchSpellCheckIndexWriter =
			new OpenSearchSpellCheckIndexWriter() {
				{
					digester = new DigesterImpl();

					setLocalization(localization);
				}
			};

		ReflectionTestUtil.setFieldValue(
			openSearchSpellCheckIndexWriter, "_indexNameBuilder",
			indexNameBuilder);
		ReflectionTestUtil.setFieldValue(
			openSearchSpellCheckIndexWriter, "_searchEngineAdapter",
			searchEngineAdapter);

		return openSearchSpellCheckIndexWriter;
	}

	private Props _createProps() {
		Props props = Mockito.mock(Props.class);

		Mockito.doReturn(
			"20"
		).when(
			props
		).get(
			PropsKeys.INDEX_SEARCH_LIMIT
		);

		return props;
	}

	private final long _companyId;
	private FacetProcessor<SearchRequest.Builder> _facetProcessor;
	private IndexCreationHelper _indexCreationHelper;
	private IndexSearcher _indexSearcher;
	private IndexWriter _indexWriter;
	private boolean _liferayMappingsAddedToIndex;
	private OpenSearchFixture _openSearchFixture;
	private SearchEngineAdapter _searchEngineAdapter;

}