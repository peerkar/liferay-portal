/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.query;

import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.search.SearchSearchRequest;
import com.liferay.portal.search.opensearch2.internal.LiferayOpenSearchIndexingFixtureFactory;
import com.liferay.portal.search.test.util.indexing.IndexingFixture;
import com.liferay.portal.search.test.util.query.BaseMoreLikeThisQueryTestCase;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Collections;

import org.hamcrest.CoreMatchers;

import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.opensearch.client.transport.httpclient5.ResponseException;

/**
 * @author Wade Cao
 */
public class MoreLikeThisQueryTest extends BaseMoreLikeThisQueryTestCase {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Ignore
	@Override
	@Test
	public void testMoreLikeThisWithoutFields() throws Exception {
	}

	@Test
	public void testMoreLikeThisWithoutFieldsOpenSearch7() throws Throwable {
		SearchSearchRequest searchSearchRequest = createSearchSearchRequest();

		searchSearchRequest.setQuery(
			queries.moreLikeThis(
				Collections.emptyList(), RandomTestUtil.randomString()));

		SearchEngineAdapter searchEngineAdapter = getSearchEngineAdapter();

		expectedException.expect(ResponseException.class);
		expectedException.expectMessage(
			CoreMatchers.containsString(
				"[more_like_this] query cannot infer the field"));

		searchEngineAdapter.execute(searchSearchRequest);
	}

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Override
	protected IndexingFixture createIndexingFixture() throws Exception {
		return LiferayOpenSearchIndexingFixtureFactory.getInstance();
	}

}