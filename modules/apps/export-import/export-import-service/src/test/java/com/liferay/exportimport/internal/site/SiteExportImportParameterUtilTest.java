/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.internal.site;

import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Map;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

/**
 * @author Petteri Karttunen
 */
public class SiteExportImportParameterUtilTest {

	@ClassRule
	@Rule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Test
	public void testGetSelectedSiteExternalReferenceCodesWhenBlank() {
		Assert.assertArrayEquals(
			new String[0],
			SiteExportImportParameterUtil.getSelectedSiteExternalReferenceCodes(
				HashMapBuilder.put(
					PortletDataHandlerKeys.SITE_EXTERNAL_REFERENCE_CODES,
					new String[] {"", null, "   "}
				).build()));
	}

	@Test
	public void testGetSelectedSiteExternalReferenceCodesWhenDuplicated() {

		// The same site picked twice is still one site to export

		Assert.assertArrayEquals(
			new String[] {"erc1", "erc2"},
			SiteExportImportParameterUtil.getSelectedSiteExternalReferenceCodes(
				HashMapBuilder.put(
					PortletDataHandlerKeys.SITE_EXTERNAL_REFERENCE_CODES,
					new String[] {"erc1", "erc2", "erc1"}
				).build()));
	}

	@Test
	public void testGetSelectedSiteExternalReferenceCodesWhenMissing() {
		Assert.assertArrayEquals(
			new String[0],
			SiteExportImportParameterUtil.getSelectedSiteExternalReferenceCodes(
				HashMapBuilder.put(
					PortletDataHandlerKeys.PORTLET_DATA,
					new String[] {Boolean.TRUE.toString()}
				).build()));
	}

	@Test
	public void testGetSelectedSiteExternalReferenceCodesWhenNull() {
		Assert.assertArrayEquals(
			new String[0],
			SiteExportImportParameterUtil.getSelectedSiteExternalReferenceCodes(
				(Map)null));
	}

	@Test
	public void testGetSelectedSiteExternalReferenceCodesWhenPadded() {
		Assert.assertArrayEquals(
			new String[] {"erc1"},
			SiteExportImportParameterUtil.getSelectedSiteExternalReferenceCodes(
				HashMapBuilder.put(
					PortletDataHandlerKeys.SITE_EXTERNAL_REFERENCE_CODES,
					new String[] {"  erc1  "}
				).build()));
	}

	@Test
	public void testGetSiteExternalReferenceCodeWhenBlank() {
		Assert.assertNull(
			SiteExportImportParameterUtil.getSiteExternalReferenceCode(
				SiteExportImportParameterUtil.toSiteExportParameterMap(
					null, "")));
	}

	@Test
	public void testGetSiteExternalReferenceCodeWhenNull() {
		Assert.assertNull(
			SiteExportImportParameterUtil.getSiteExternalReferenceCode(
				(Map)null));
	}

	@Test
	public void testGetSiteExternalReferenceCodeWhenSet() {
		Assert.assertEquals(
			"erc1",
			SiteExportImportParameterUtil.getSiteExternalReferenceCode(
				SiteExportImportParameterUtil.toSiteExportParameterMap(
					null, "erc1")));
	}

	@Test
	public void testIsEnabledFollowsTheFeatureFlag() {
		long companyId = RandomTestUtil.randomLong();

		try (MockedStatic<FeatureFlagManagerUtil>
				featureFlagManagerUtilMockedStatic = Mockito.mockStatic(
					FeatureFlagManagerUtil.class)) {

			featureFlagManagerUtilMockedStatic.when(
				() -> FeatureFlagManagerUtil.isEnabled(companyId, "LPD-85946")
			).thenReturn(
				true
			);

			Assert.assertTrue(
				SiteExportImportParameterUtil.isEnabled(companyId));
		}
	}

	@Test
	public void testIsSitePassWhenCompanyLevel() {
		Assert.assertFalse(
			SiteExportImportParameterUtil.isSitePass(
				HashMapBuilder.put(
					PortletDataHandlerKeys.SITE_EXTERNAL_REFERENCE_CODES,
					new String[] {"erc1"}
				).build()));
	}

	@Test
	public void testIsSitePassWhenSiteLevel() {
		PortletDataContext portletDataContext = Mockito.mock(
			PortletDataContext.class);

		Mockito.when(
			portletDataContext.getParameterMap()
		).thenReturn(
			SiteExportImportParameterUtil.toSiteExportParameterMap(null, "erc1")
		);

		Assert.assertTrue(
			SiteExportImportParameterUtil.isSitePass(portletDataContext));
	}

	@Test
	public void testToSiteExportParameterMapCarriesTheWholeSite() {

		// A handler asked for its data answers with nothing when the controls
		// it offers default to off, which is what the company level pass may
		// well have been left with

		Map<String, String[]> siteParameterMap =
			SiteExportImportParameterUtil.toSiteExportParameterMap(
				HashMapBuilder.put(
					PortletDataHandlerKeys.PORTLET_DATA,
					new String[] {Boolean.FALSE.toString()}
				).put(
					PortletDataHandlerKeys.PORTLET_DATA_ALL,
					new String[] {Boolean.FALSE.toString()}
				).put(
					PortletDataHandlerKeys.PORTLET_DATA_CONTROL_DEFAULT,
					new String[] {Boolean.FALSE.toString()}
				).build(),
				"erc1");

		Assert.assertTrue(
			MapUtil.getBoolean(
				siteParameterMap, PortletDataHandlerKeys.PORTLET_DATA));
		Assert.assertTrue(
			MapUtil.getBoolean(
				siteParameterMap, PortletDataHandlerKeys.PORTLET_DATA_ALL));
		Assert.assertTrue(
			MapUtil.getBoolean(
				siteParameterMap,
				PortletDataHandlerKeys.PORTLET_DATA_CONTROL_DEFAULT));
		Assert.assertTrue(
			MapUtil.getBoolean(
				siteParameterMap, PortletDataHandlerKeys.LAYOUT_SET_SETTINGS));
	}

	@Test
	public void testToSiteExportParameterMapDropsTheSelection() {

		// Dropping the selection is what keeps a per-site pass from starting
		// per-site passes of its own

		Map<String, String[]> siteParameterMap =
			SiteExportImportParameterUtil.toSiteExportParameterMap(
				HashMapBuilder.put(
					PortletDataHandlerKeys.SITE_EXTERNAL_REFERENCE_CODES,
					new String[] {"erc1", "erc2"}
				).build(),
				"erc1");

		Assert.assertArrayEquals(
			new String[0],
			SiteExportImportParameterUtil.getSelectedSiteExternalReferenceCodes(
				siteParameterMap));
		Assert.assertEquals(
			"erc1",
			SiteExportImportParameterUtil.getSiteExternalReferenceCode(
				siteParameterMap));
		Assert.assertTrue(
			SiteExportImportParameterUtil.isSitePass(siteParameterMap));
	}

	@Test
	public void testToSiteExportParameterMapKeepsWhatItDoesNotDecide() {
		Map<String, String[]> siteParameterMap =
			SiteExportImportParameterUtil.toSiteExportParameterMap(
				HashMapBuilder.put(
					PortletDataHandlerKeys.COMMENTS,
					new String[] {Boolean.TRUE.toString()}
				).put(
					PortletDataHandlerKeys.RATINGS,
					new String[] {Boolean.TRUE.toString()}
				).build(),
				"erc1");

		Assert.assertTrue(
			MapUtil.getBoolean(
				siteParameterMap, PortletDataHandlerKeys.COMMENTS));
		Assert.assertTrue(
			MapUtil.getBoolean(
				siteParameterMap, PortletDataHandlerKeys.RATINGS));
	}

	@Test
	public void testToSiteExportParameterMapLeavesOutOfScopeOff() {
		Map<String, String[]> siteParameterMap =
			SiteExportImportParameterUtil.toSiteExportParameterMap(
				HashMapBuilder.put(
					PortletDataHandlerKeys.DELETIONS,
					new String[] {Boolean.TRUE.toString()}
				).put(
					PortletDataHandlerKeys.LAYOUT_SET_PROTOTYPE_SETTINGS,
					new String[] {Boolean.TRUE.toString()}
				).put(
					PortletDataHandlerKeys.PERMISSIONS,
					new String[] {Boolean.TRUE.toString()}
				).build(),
				"erc1");

		Assert.assertFalse(
			MapUtil.getBoolean(
				siteParameterMap, PortletDataHandlerKeys.DELETIONS));
		Assert.assertFalse(
			MapUtil.getBoolean(
				siteParameterMap,
				PortletDataHandlerKeys.LAYOUT_SET_PRIVATE_LAYOUT));
		Assert.assertFalse(
			MapUtil.getBoolean(
				siteParameterMap,
				PortletDataHandlerKeys.LAYOUT_SET_PROTOTYPE_SETTINGS));
		Assert.assertFalse(
			MapUtil.getBoolean(
				siteParameterMap, PortletDataHandlerKeys.PERMISSIONS));
	}

	@Test
	public void testToSiteImportParameterMapMirrors() {
		Map<String, String[]> siteParameterMap =
			SiteExportImportParameterUtil.toSiteImportParameterMap(
				HashMapBuilder.put(
					PortletDataHandlerKeys.DATA_STRATEGY,
					new String[] {
						PortletDataHandlerKeys.DATA_STRATEGY_COPY_AS_NEW
					}
				).build(),
				"erc1");

		Assert.assertEquals(
			PortletDataHandlerKeys.DATA_STRATEGY_MIRROR,
			MapUtil.getString(
				siteParameterMap, PortletDataHandlerKeys.DATA_STRATEGY));
		Assert.assertEquals(
			PortletDataHandlerKeys.LAYOUTS_IMPORT_MODE_MERGE_BY_LAYOUT_UUID,
			MapUtil.getString(
				siteParameterMap, PortletDataHandlerKeys.LAYOUTS_IMPORT_MODE));
	}

	@Test
	public void testToSiteImportParameterMapRemovesNothing() {

		// Emptying a site the user did not ask to have emptied is not something
		// an import should decide on its own

		Map<String, String[]> siteParameterMap =
			SiteExportImportParameterUtil.toSiteImportParameterMap(
				HashMapBuilder.put(
					PortletDataHandlerKeys.DELETE_MISSING_LAYOUTS,
					new String[] {Boolean.TRUE.toString()}
				).put(
					PortletDataHandlerKeys.DELETE_PORTLET_DATA,
					new String[] {Boolean.TRUE.toString()}
				).build(),
				"erc1");

		Assert.assertFalse(
			MapUtil.getBoolean(
				siteParameterMap,
				PortletDataHandlerKeys.DELETE_MISSING_LAYOUTS));
		Assert.assertFalse(
			MapUtil.getBoolean(
				siteParameterMap, PortletDataHandlerKeys.DELETE_PORTLET_DATA));
	}

}