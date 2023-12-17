/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal;

import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.search.opensearch2.internal.facet.FacetProcessor;

import org.opensearch.client.opensearch.core.SearchRequest;

/**
 * @author André de Oliveira
 */
public class OpenSearchIndexingFixtureBuilder {

	public OpenSearchIndexingFixture build() {
		OpenSearchIndexingFixture openSearchIndexingFixture =
			new OpenSearchIndexingFixture();

		openSearchIndexingFixture.setOpenSearchFixture(_getOpenSearchFixture());
		openSearchIndexingFixture.setFacetProcessor(_facetProcessor);
		openSearchIndexingFixture.setLiferayMappingsAddedToIndex(
			_liferayMappingsAddedToIndex);

		return openSearchIndexingFixture;
	}

	public OpenSearchIndexingFixtureBuilder facetProcessor(
		FacetProcessor<SearchRequest.Builder> facetProcessor) {

		_facetProcessor = facetProcessor;

		return this;
	}

	public OpenSearchIndexingFixtureBuilder liferayMappingsAddedToIndex(
		boolean liferayMappingsAddedToIndex) {

		_liferayMappingsAddedToIndex = liferayMappingsAddedToIndex;

		return this;
	}

	public OpenSearchIndexingFixtureBuilder openSearchFixture(
		OpenSearchFixture openSearchFixture) {

		_openSearchFixture = openSearchFixture;

		return this;
	}

	private OpenSearchFixture _getOpenSearchFixture() {
		if (_openSearchFixture != null) {
			return _openSearchFixture;
		}

		return new OpenSearchFixture(RandomTestUtil.randomString());
	}

	private FacetProcessor<SearchRequest.Builder> _facetProcessor;
	private boolean _liferayMappingsAddedToIndex;
	private OpenSearchFixture _openSearchFixture;

}