/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.background.task;

import com.liferay.portal.search.opensearch2.internal.OpenSearchSearchEngineFixture;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionFixture;
import com.liferay.portal.search.opensearch2.internal.index.FieldMappingAssert;
import com.liferay.portal.search.test.util.background.task.BaseReindexSingleIndexerBackgroundTaskExecutorTestCase;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.ClassRule;
import org.junit.Rule;

import org.opensearch.client.opensearch.OpenSearchClient;

/**
 * @author Adam Brandizzi
 */
public class ReindexSingleIndexerBackgroundTaskExecutorTest
	extends BaseReindexSingleIndexerBackgroundTaskExecutorTestCase {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	public ReindexSingleIndexerBackgroundTaskExecutorTest() {
		OpenSearchConnectionFixture openSearchConnectionFixture =
			OpenSearchConnectionFixture.builder(
			).clusterName(
				ReindexSingleIndexerBackgroundTaskExecutorTest.class.
					getSimpleName()
			).build();

		OpenSearchSearchEngineFixture openSearchSearchEngineFixture =
			new OpenSearchSearchEngineFixture(openSearchConnectionFixture);

		_openSearchConnectionFixture = openSearchConnectionFixture;

		_openSearchSearchEngineFixture = openSearchSearchEngineFixture;
	}

	@Override
	protected void assertFieldType(String fieldName, String fieldType)
		throws Exception {

		OpenSearchClient openSearchClient =
			_openSearchConnectionFixture.getOpenSearchClient();

		FieldMappingAssert.assertType(
			fieldType, fieldName, getIndexName(), openSearchClient.indices());
	}

	@Override
	protected OpenSearchSearchEngineFixture getSearchEngineFixture() {
		return _openSearchSearchEngineFixture;
	}

	private final OpenSearchConnectionFixture _openSearchConnectionFixture;
	private final OpenSearchSearchEngineFixture _openSearchSearchEngineFixture;

}