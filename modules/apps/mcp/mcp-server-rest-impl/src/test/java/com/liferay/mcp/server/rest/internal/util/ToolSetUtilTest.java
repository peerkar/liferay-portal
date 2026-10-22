/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.mcp.server.rest.internal.util;

import com.liferay.mcp.server.rest.internal.search.exception.ToolSearchUnavailableException;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.module.service.Snapshot;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.search.capabilities.SearchCapabilities;
import com.liferay.portal.test.rule.LiferayUnitTestRule;
import com.liferay.portal.vulcan.application.HeadlessApplicationProvider;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

/**
 * @author Petteri Karttunen
 */
public class ToolSetUtilTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() {
		PortalUtil portalUtil = new PortalUtil();

		portalUtil.setPortal(Mockito.mock(Portal.class));

		HeadlessApplicationProvider headlessApplicationProvider = Mockito.mock(
			HeadlessApplicationProvider.class);

		List<HeadlessApplicationProvider.Application> applications =
			Arrays.asList(
				_mockApplication("/c/petstores", "v1.0"),
				_mockApplication("/headless-admin-user", "v1.0"),
				_mockApplication("/headless-admin-user-workflow", "v1.0"),
				_mockApplication(
					"/headless-commerce-admin-pricing", "v1.0", "v2.0",
					"v10.0"));

		Mockito.when(
			headlessApplicationProvider.getApplications()
		).thenReturn(
			applications
		);

		Snapshot<HeadlessApplicationProvider>
			headlessApplicationProviderSnapshot = Mockito.mock(Snapshot.class);

		Mockito.when(
			headlessApplicationProviderSnapshot.get()
		).thenReturn(
			headlessApplicationProvider
		);

		ReflectionTestUtil.setFieldValue(
			ToolSetUtil.class, "_headlessApplicationProviderSnapshot",
			headlessApplicationProviderSnapshot);
	}

	@Test
	public void testGetLatestOpenAPIDocuments() {
		Map<String, HeadlessApplicationProvider.OpenAPIDocument>
			openAPIDocuments = ToolSetUtil.getLatestOpenAPIDocuments();

		Assert.assertEquals(
			Arrays.asList(
				"c-petstores-v1.0", "headless-admin-user-v1.0",
				"headless-admin-user-workflow-v1.0",
				"headless-commerce-admin-pricing-v10.0"),
			new ArrayList<>(openAPIDocuments.keySet()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetToolSearchResultsPageWhenSearchIsBlank() {
		_mockSearchCapabilities(true);

		try (MockedStatic<FeatureFlagManagerUtil> mockedStatic =
				Mockito.mockStatic(FeatureFlagManagerUtil.class)) {

			ToolSetUtil.getToolSearchResultsPage(
				null, _httpServletRequest, false, null, StringPool.BLANK);
		}
	}

	@Test
	public void testGetToolSearchResultsPageWhenToolSearchIsUnsupported() {
		_mockSearchCapabilities(false);

		try (MockedStatic<FeatureFlagManagerUtil> mockedStatic =
				Mockito.mockStatic(FeatureFlagManagerUtil.class)) {

			ToolSetUtil.getToolSearchResultsPage(
				null, _httpServletRequest, false, null,
				RandomTestUtil.randomString());

			Assert.fail();
		}
		catch (ToolSearchUnavailableException toolSearchUnavailableException) {
			String message = toolSearchUnavailableException.getMessage();

			Assert.assertTrue(
				message, message.contains("Elasticsearch or OpenSearch"));
		}
	}

	@Test
	public void testGetToolSetName() {
		Assert.assertEquals(
			"c-petstores-v1.0", ToolSetUtil.getToolSetName("/c/petstores"));
		Assert.assertEquals(
			"headless-admin-user-v1.0",
			ToolSetUtil.getToolSetName(
				"/headless-admin-user/v1.0/user-accounts"));
	}

	@Test
	public void testGetToolSetNameWhenBasePathIsPrefixOfAnotherBasePath() {
		Assert.assertEquals(
			"headless-admin-user-workflow-v1.0",
			ToolSetUtil.getToolSetName(
				"/headless-admin-user-workflow/v1.0/tasks"));
	}

	@Test
	public void testGetToolSetNameWhenNoApplicationMatches() {
		Assert.assertNull(ToolSetUtil.getToolSetName(null));
		Assert.assertNull(ToolSetUtil.getToolSetName("/c/petstore"));
		Assert.assertNull(ToolSetUtil.getToolSetName("/headless-admin"));
	}

	@Test
	public void testGetToolSetNameWhenRESTContextPathVersionIsSuperseded() {
		Assert.assertEquals(
			"headless-commerce-admin-pricing-v10.0",
			ToolSetUtil.getToolSetName(
				"/headless-commerce-admin-pricing/v1.0/discounts"));
	}

	@Test
	public void testInvokeToolWhenToolIsNotInProfile() throws Exception {
		try {
			ToolSetUtil.invokeTool(
				SetUtil.fromArray(
					"headless-delivery-v1.0#getSiteBlogPostingsPage"),
				null, _httpServletRequest,
				HashMapBuilder.<String, Object>put(
					"toolName", "postSiteDocument"
				).put(
					"toolSetName", "headless-delivery-v1.0"
				).build(),
				null, "postToolSetToolSetNameToolInvoke",
				ToolSetUtil.TOOL_SET_NAME);

			Assert.fail();
		}
		catch (IllegalArgumentException illegalArgumentException) {
			Assert.assertEquals(
				StringBundler.concat(
					"Tool \"postSiteDocument\" of tool set ",
					"\"headless-delivery-v1.0\" is not in this MCP server ",
					"profile"),
				illegalArgumentException.getMessage());
		}
	}

	private HeadlessApplicationProvider.Application _mockApplication(
		String basePath, String... versions) {

		HeadlessApplicationProvider.Application application = Mockito.mock(
			HeadlessApplicationProvider.Application.class);

		Mockito.when(
			application.getBasePath()
		).thenReturn(
			basePath
		);

		List<HeadlessApplicationProvider.OpenAPIDocument> openAPIDocuments =
			new ArrayList<>();

		for (String version : versions) {
			HeadlessApplicationProvider.OpenAPIDocument openAPIDocument =
				Mockito.mock(HeadlessApplicationProvider.OpenAPIDocument.class);

			Mockito.when(
				openAPIDocument.getApplication()
			).thenReturn(
				application
			);

			Mockito.when(
				openAPIDocument.getVersion()
			).thenReturn(
				version
			);

			openAPIDocuments.add(openAPIDocument);
		}

		Mockito.when(
			application.getOpenAPIDocuments()
		).thenReturn(
			openAPIDocuments
		);

		return application;
	}

	private void _mockSearchCapabilities(boolean mcpToolSearchSupported) {
		SearchCapabilities searchCapabilities = Mockito.mock(
			SearchCapabilities.class);

		Mockito.when(
			searchCapabilities.isMCPToolSearchSupported()
		).thenReturn(
			mcpToolSearchSupported
		);

		Snapshot<SearchCapabilities> searchCapabilitiesSnapshot = Mockito.mock(
			Snapshot.class);

		Mockito.when(
			searchCapabilitiesSnapshot.get()
		).thenReturn(
			searchCapabilities
		);

		ReflectionTestUtil.setFieldValue(
			ToolSetUtil.class, "_searchCapabilitiesSnapshot",
			searchCapabilitiesSnapshot);
	}

	private final HttpServletRequest _httpServletRequest = Mockito.mock(
		HttpServletRequest.class);

}