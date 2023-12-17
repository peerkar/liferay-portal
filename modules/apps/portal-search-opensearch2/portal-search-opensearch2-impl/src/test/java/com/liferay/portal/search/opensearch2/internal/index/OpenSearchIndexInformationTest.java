/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.index;

import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.search.index.IndexNameBuilder;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionManager;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchFixture;
import com.liferay.portal.search.opensearch2.internal.util.ResourceUtil;
import com.liferay.portal.search.test.util.AssertUtils;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * @author Adam Brandizzi
 */
public class OpenSearchIndexInformationTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() throws Exception {
		_openSearchFixture = new OpenSearchFixture();

		_openSearchFixture.setUp();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		_openSearchFixture.tearDown();
	}

	@Before
	public void setUp() throws Exception {
		_companyIndexFactoryFixture = _createCompanyIndexFactoryFixture(
			_openSearchFixture);

		_openSearchIndexInformation = _createOpenSearchIndexInformation(
			_openSearchFixture);
	}

	@After
	public void tearDown() {
		_companyIndexFactoryFixture.tearDown();
	}

	@Test
	public void testGetCompanyIndexName() throws Exception {
		_companyIndexFactoryFixture.createIndices();

		long companyId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			_getIndexNameBuilder(companyId),
			_openSearchIndexInformation.getCompanyIndexName(companyId));
	}

	@Test
	public void testGetFieldMappings() throws Exception {
		_companyIndexFactoryFixture.createIndices();

		AssertUtils.assertEquals(
			"", _loadJSONObject(testName.getMethodName()),
			_jsonFactory.createJSONObject(
				_openSearchIndexInformation.getFieldMappings(
					_companyIndexFactoryFixture.getIndexName())));
	}

	@Test
	public void testGetIndexNames() throws Exception {
		_companyIndexFactoryFixture.createIndices();

		AssertUtils.assertEquals(
			"", Arrays.asList(_companyIndexFactoryFixture.getIndexName()),
			Arrays.asList(_openSearchIndexInformation.getIndexNames()));
	}

	@Rule
	public TestName testName = new TestName();

	private CompanyIndexFactoryFixture _createCompanyIndexFactoryFixture(
		OpenSearchConnectionManager openSearchConnectionManager) {

		return new CompanyIndexFactoryFixture(
			openSearchConnectionManager, testName.getMethodName());
	}

	private OpenSearchIndexInformation _createOpenSearchIndexInformation(
		OpenSearchConnectionManager openSearchConnectionManager) {

		OpenSearchIndexInformation openSearchIndexInformation =
			new OpenSearchIndexInformation();

		ReflectionTestUtil.setFieldValue(
			openSearchIndexInformation, "_openSearchConnectionManager",
			openSearchConnectionManager);
		ReflectionTestUtil.setFieldValue(
			openSearchIndexInformation, "_indexNameBuilder",
			(IndexNameBuilder)companyId -> _getIndexNameBuilder(companyId));

		return openSearchIndexInformation;
	}

	private String _getIndexNameBuilder(long companyId) {
		return "test-" + companyId;
	}

	private JSONObject _loadJSONObject(String suffix) throws Exception {
		String json = ResourceUtil.getResourceAsString(
			getClass(), "OpenSearchIndexInformationTest-" + suffix + ".json");

		return _jsonFactory.createJSONObject(json);
	}

	private static OpenSearchFixture _openSearchFixture;

	private CompanyIndexFactoryFixture _companyIndexFactoryFixture;
	private final JSONFactory _jsonFactory = new JSONFactoryImpl();
	private OpenSearchIndexInformation _openSearchIndexInformation;

}