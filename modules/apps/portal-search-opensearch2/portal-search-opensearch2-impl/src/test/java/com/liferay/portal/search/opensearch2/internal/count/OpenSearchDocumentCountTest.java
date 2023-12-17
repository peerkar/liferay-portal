/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.count;

import com.liferay.portal.search.opensearch2.internal.LiferayOpenSearchIndexingFixtureFactory;
import com.liferay.portal.search.test.util.count.BaseDocumentCountTestCase;
import com.liferay.portal.search.test.util.indexing.IndexingFixture;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.ClassRule;
import org.junit.Rule;

/**
 * @author Wade Cao
 */
public class OpenSearchDocumentCountTest extends BaseDocumentCountTestCase {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Override
	protected IndexingFixture createIndexingFixture() {
		return LiferayOpenSearchIndexingFixtureFactory.getInstance();
	}

	@Override
	protected String getIndexName() {
		return String.valueOf(getCompanyId());
	}

}