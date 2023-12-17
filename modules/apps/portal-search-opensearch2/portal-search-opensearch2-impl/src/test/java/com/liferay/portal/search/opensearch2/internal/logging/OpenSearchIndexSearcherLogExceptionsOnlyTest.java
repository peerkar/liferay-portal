/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.logging;

import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.search.opensearch2.internal.LiferayOpenSearchIndexingFixtureFactory;
import com.liferay.portal.search.opensearch2.internal.OpenSearchIndexSearcher;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionFixture;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.search.test.util.indexing.BaseIndexingTestCase;
import com.liferay.portal.search.test.util.indexing.IndexingFixture;
import com.liferay.portal.search.test.util.logging.ExpectedLog;
import com.liferay.portal.search.test.util.logging.ExpectedLogMethodTestRule;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Collections;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Bryan Engler
 */
public class OpenSearchIndexSearcherLogExceptionsOnlyTest
	extends BaseIndexingTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			ExpectedLogMethodTestRule.INSTANCE, LiferayUnitTestRule.INSTANCE);

	@ExpectedLog(
		expectedClass = OpenSearchIndexSearcher.class,
		expectedLevel = ExpectedLog.Level.WARNING,
		expectedLog = "all shards failed"
	)
	@Test
	public void testExceptionOnlyLoggedWhenQueryMalformedSearch() {
		search(createSearchContext(), getMalformedQuery());
	}

	@ExpectedLog(
		expectedClass = OpenSearchIndexSearcher.class,
		expectedLevel = ExpectedLog.Level.WARNING,
		expectedLog = "all shards failed"
	)
	@Test
	public void testExceptionOnlyLoggedWhenQueryMalformedSearchCount() {
		searchCount(createSearchContext(), getMalformedQuery());
	}

	@Override
	protected IndexingFixture createIndexingFixture() {
		return LiferayOpenSearchIndexingFixtureFactory.builder(
		).openSearchFixture(
			new OpenSearchFixture(createOpenSearchConnectionFixture())
		).build();
	}

	protected OpenSearchConnectionFixture createOpenSearchConnectionFixture() {
		return OpenSearchConnectionFixture.builder(
		).clusterName(
			OpenSearchIndexWriterLogExceptionsOnlyTest.class.getSimpleName()
		).elasticsearchConfigurationProperties(
			Collections.singletonMap("logExceptionsOnly", true)
		).build();
	}

	protected Query getMalformedQuery() {
		BooleanQueryImpl booleanQueryImpl = new BooleanQueryImpl();

		booleanQueryImpl.add(
			new TermQueryImpl(Field.EXPIRATION_DATE, "text"),
			BooleanClauseOccur.MUST);

		return booleanQueryImpl;
	}

}