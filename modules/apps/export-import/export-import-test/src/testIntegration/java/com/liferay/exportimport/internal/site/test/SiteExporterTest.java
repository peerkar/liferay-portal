/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.internal.site.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.exportimport.kernel.configuration.ExportImportConfigurationSettingsMapFactoryUtil;
import com.liferay.exportimport.kernel.configuration.constants.ExportImportConfigurationConstants;
import com.liferay.exportimport.kernel.lar.ExportImportHelper;
import com.liferay.exportimport.kernel.lar.LARSite;
import com.liferay.exportimport.kernel.lar.ManifestSummary;
import com.liferay.exportimport.kernel.lar.PortletDataHandlerKeys;
import com.liferay.exportimport.kernel.service.ExportImportConfigurationLocalServiceUtil;
import com.liferay.exportimport.kernel.service.ExportImportLocalServiceUtil;
import com.liferay.exportimport.site.ExportImportSiteProvider;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutServiceUtil;
import com.liferay.portal.kernel.test.TestInfo;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.zip.ZipReader;
import com.liferay.portal.kernel.zip.ZipReaderFactory;
import com.liferay.portal.test.rule.FeatureFlag;
import com.liferay.portal.test.rule.FeatureFlags;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Petteri Karttunen
 */
@FeatureFlags(
	featureFlags = {
		@FeatureFlag(value = "LPD-57655"), @FeatureFlag(value = "LPD-85946")
	}
)
@RunWith(Arquillian.class)
public class SiteExporterTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_companyGroup = GroupLocalServiceUtil.getCompanyGroup(
			TestPropsValues.getCompanyId());

		_group = GroupTestUtil.addGroup();
		_otherGroup = GroupTestUtil.addGroup();
	}

	@Test
	@TestInfo("LPD-85946")
	public void testExportCarriesNoSitesWhenNoneAreSelected() throws Exception {
		File file = _exportCompanyLayouts();

		try (ZipReader zipReader = _zipReaderFactory.getZipReader(file)) {
			Assert.assertFalse(
				_getManifest(
					zipReader
				).contains(
					"<sites>"
				));
		}
		finally {
			FileUtil.delete(file);
		}
	}

	@Test
	@TestInfo("LPD-85946")
	public void testExportCarriesTheSelectedSite() throws Exception {
		File file = _exportCompanyLayouts(_group);

		try (ZipReader zipReader = _zipReaderFactory.getZipReader(file)) {
			String manifest = _getManifest(zipReader);

			Assert.assertTrue(manifest.contains("<sites>"));
			Assert.assertTrue(
				manifest.contains(
					"external-reference-code=\"" +
						_group.getExternalReferenceCode() + "\""));

			// A site of its own carries a manifest of its own, because it has
			// its own set of portlets and its own model counts

			Assert.assertNotNull(
				zipReader.getEntryAsInputStream(_getSiteManifestPath(_group)));
		}
		finally {
			FileUtil.delete(file);
		}
	}

	@Test
	@TestInfo("LPD-85946")
	public void testExportedSiteIsReadBackFromTheManifest() throws Exception {
		File file = _exportCompanyLayouts(_group);

		try {
			ManifestSummary manifestSummary = _getManifestSummary(file);

			List<LARSite> larSites = manifestSummary.getSites();

			Assert.assertEquals(larSites.toString(), 1, larSites.size());

			LARSite larSite = larSites.get(0);

			Assert.assertEquals(
				_group.getExternalReferenceCode(),
				larSite.getExternalReferenceCode());
			Assert.assertEquals(
				_group.getFriendlyURL(), larSite.getFriendlyURL());
			Assert.assertEquals(_group.getGroupId(), larSite.getGroupId());
			Assert.assertEquals(
				_exportImportSiteProvider.getDisplayName(
					_group, LocaleUtil.getSiteDefault()),
				larSite.getName());
			Assert.assertFalse(larSite.isGlobal());

			// The path describes the instance the LAR is leaving, and every
			// path starts at the Global site

			Assert.assertTrue(
				larSite.getHierarchy(),
				larSite.getHierarchy(
				).endsWith(
					_exportImportSiteProvider.getDisplayName(
						_group, LocaleUtil.getSiteDefault())
				));
		}
		finally {
			FileUtil.delete(file);
		}
	}

	@Test
	@TestInfo("LPD-85946")
	public void testExportedSitesAreCountedAsWholeUnits() throws Exception {
		File file = _exportCompanyLayouts(_group, _otherGroup);

		try {
			ManifestSummary manifestSummary = _getManifestSummary(file);

			List<LARSite> larSites = manifestSummary.getSites();

			Assert.assertEquals(larSites.toString(), 2, larSites.size());
		}
		finally {
			FileUtil.delete(file);
		}
	}

	@Test
	@TestInfo("LPD-85946")
	public void testExportLeavesOutTheUnselectedSite() throws Exception {
		File file = _exportCompanyLayouts(_group);

		try (ZipReader zipReader = _zipReaderFactory.getZipReader(file)) {
			String manifest = _getManifest(zipReader);

			Assert.assertFalse(
				manifest.contains(
					"external-reference-code=\"" +
						_otherGroup.getExternalReferenceCode() + "\""));

			Assert.assertNull(
				zipReader.getEntryAsInputStream(
					_getSiteManifestPath(_otherGroup)));
		}
		finally {
			FileUtil.delete(file);
		}
	}

	private File _exportCompanyLayouts(Group... groups) throws Exception {
		return ExportImportLocalServiceUtil.exportLayoutsAsFile(
			ExportImportConfigurationLocalServiceUtil.
				addDraftExportImportConfiguration(
					TestPropsValues.getUserId(),
					ExportImportConfigurationConstants.TYPE_EXPORT_LAYOUT,
					ExportImportConfigurationSettingsMapFactoryUtil.
						buildExportLayoutSettingsMap(
							TestPropsValues.getUser(),
							_companyGroup.getGroupId(), false, null,
							_getExportParameterMap(groups))));
	}

	/**
	 * Returns the parameters an export of the given sites runs under.
	 *
	 * <p>
	 * A selection of no sites is left out of the map rather than put in it as an
	 * empty array, which is what a request carrying no sites does. The settings
	 * of an export import configuration travel as JSON, and an empty array comes
	 * back out of it as an <code>Object[]</code> that the export then fails to
	 * cast.
	 * </p>
	 */
	private Map<String, String[]> _getExportParameterMap(Group... groups) {
		Map<String, String[]> parameterMap = _getParameterMap();

		if (groups.length == 0) {
			return parameterMap;
		}

		String[] siteExternalReferenceCodes = new String[groups.length];

		for (int i = 0; i < groups.length; i++) {
			siteExternalReferenceCodes[i] =
				groups[i].getExternalReferenceCode();
		}

		parameterMap.put(
			PortletDataHandlerKeys.SITE_EXTERNAL_REFERENCE_CODES,
			siteExternalReferenceCodes);

		return parameterMap;
	}

	private String _getManifest(ZipReader zipReader) throws Exception {
		try (InputStream inputStream = zipReader.getEntryAsInputStream(
				"/manifest.xml")) {

			Assert.assertNotNull(inputStream);

			return new String(inputStream.readAllBytes());
		}
	}

	private ManifestSummary _getManifestSummary(File file) throws Exception {
		FileEntry fileEntry = null;

		try (InputStream inputStream = new FileInputStream(file)) {
			fileEntry = LayoutServiceUtil.addTempFileEntry(
				_companyGroup.getGroupId(), SiteExporterTest.class.getName(),
				RandomTestUtil.randomString() + ".lar", inputStream,
				ContentTypes.APPLICATION_ZIP);
		}

		try {
			return _exportImportHelper.getManifestSummary(
				TestPropsValues.getUserId(), _companyGroup.getGroupId(),
				new HashMap<>(), fileEntry);
		}
		finally {
			LayoutServiceUtil.deleteTempFileEntry(
				_companyGroup.getGroupId(), SiteExporterTest.class.getName(),
				fileEntry.getFileName());
		}
	}

	private Map<String, String[]> _getParameterMap() {
		return HashMapBuilder.put(
			PortletDataHandlerKeys.PORTLET_CONFIGURATION,
			new String[] {Boolean.TRUE.toString()}
		).put(
			PortletDataHandlerKeys.PORTLET_DATA,
			new String[] {Boolean.TRUE.toString()}
		).put(
			PortletDataHandlerKeys.PORTLET_DATA_ALL,
			new String[] {Boolean.TRUE.toString()}
		).build();
	}

	private String _getSiteManifestPath(Group group) {
		return StringBundler.concat(
			"/group/", group.getGroupId(), "/manifest.xml");
	}

	private Group _companyGroup;

	@Inject
	private ExportImportHelper _exportImportHelper;

	@Inject
	private ExportImportSiteProvider _exportImportSiteProvider;

	@DeleteAfterTestRun
	private Group _group;

	@DeleteAfterTestRun
	private Group _otherGroup;

	@Inject
	private ZipReaderFactory _zipReaderFactory;

}