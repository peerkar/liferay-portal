/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.legacy.hits;

import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.opensearch.client.json.JsonData;
import org.opensearch.client.opensearch.core.search.Hit;

/**
 * @author Joshua Cords
 */
public class HitDocumentTranslatorTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Test
	public void testDocumentWithIgnoredField() {
		Hit.Builder<JsonData> hitBuilder = new Hit.Builder<>();

		hitBuilder.fields("ignore", JsonData.of("value"));
		hitBuilder.ignored("ignore");

		HitDocumentTranslator hitDocumentTranslator =
			new HitDocumentTranslatorImpl();

		Document document = hitDocumentTranslator.translate(hitBuilder.build());

		Assert.assertEquals(null, document.get("ignore"));
	}

}