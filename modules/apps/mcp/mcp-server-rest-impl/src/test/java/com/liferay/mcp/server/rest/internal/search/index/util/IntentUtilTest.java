/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.mcp.server.rest.internal.search.index.util;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Petteri Karttunen
 */
public class IntentUtilTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Test
	public void testGetIntentVerbs() {
		Assert.assertArrayEquals(
			new String[] {"clone", "copy", "duplicate"},
			IntentUtil.getIntentVerbs("copy"));
		Assert.assertArrayEquals(
			new String[] {
				"add", "create", "make", "start", "submit", "upload", "write"
			},
			IntentUtil.getIntentVerbs(IntentUtil.INTENT_CREATE));
		Assert.assertArrayEquals(
			new String[0], IntentUtil.getIntentVerbs(StringPool.BLANK));
	}

	@Test
	public void testGetOperationActionSuffixes() {
		Set<String> operationActionSuffixes =
			IntentUtil.getOperationActionSuffixes();

		Iterator<String> iterator = operationActionSuffixes.iterator();

		Assert.assertEquals("TranslationLanguage", iterator.next());

		Assert.assertTrue(operationActionSuffixes.contains("Translation"));
	}

	@Test
	public void testGetOperationIntent() {
		Assert.assertEquals(
			"subscribe",
			IntentUtil.getOperationIntent(false, "put", "Subscribe"));
		Assert.assertEquals(
			IntentUtil.INTENT_LIST,
			IntentUtil.getOperationIntent(true, "get", null));
		Assert.assertEquals(
			IntentUtil.INTENT_READ,
			IntentUtil.getOperationIntent(false, "get", null));
		Assert.assertEquals(
			IntentUtil.INTENT_CREATE,
			IntentUtil.getOperationIntent(false, "post", null));
		Assert.assertEquals(
			IntentUtil.INTENT_CREATE,
			IntentUtil.getOperationIntent(true, "post", null));
		Assert.assertEquals(
			StringPool.BLANK,
			IntentUtil.getOperationIntent(false, "head", null));
	}

	@Test
	public void testGetOtherIntents() {
		List<String> otherIntents = IntentUtil.getOtherIntents(
			Arrays.asList(IntentUtil.INTENT_LIST, IntentUtil.INTENT_READ));

		Assert.assertEquals(otherIntents.toString(), 15, otherIntents.size());
		Assert.assertFalse(otherIntents.contains(IntentUtil.INTENT_LIST));
		Assert.assertTrue(otherIntents.contains(IntentUtil.INTENT_CREATE));
	}

	@Test
	public void testGetSearchIntents() {
		Assert.assertEquals(
			Arrays.asList("delete"),
			IntentUtil.getSearchIntents("delete twenty users"));
		Assert.assertEquals(
			Arrays.asList(IntentUtil.INTENT_LIST, IntentUtil.INTENT_READ),
			IntentUtil.getSearchIntents("Show me the blog postings"));
		Assert.assertTrue(
			IntentUtil.getSearchIntents(
				"hello world"
			).isEmpty());
		Assert.assertTrue(
			IntentUtil.getSearchIntents(
				StringPool.BLANK
			).isEmpty());
	}

	@Test
	public void testGetSearchIntentsWithAPhrase() {
		Assert.assertEquals(
			Arrays.asList("delete"),
			IntentUtil.getSearchIntents("get rid of a user"));
		Assert.assertEquals(
			Arrays.asList(IntentUtil.INTENT_CREATE),
			IntentUtil.getSearchIntents("please sign up a user"));
	}

}