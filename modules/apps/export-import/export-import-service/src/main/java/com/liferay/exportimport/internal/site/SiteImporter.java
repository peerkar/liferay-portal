/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.internal.site;

import com.liferay.exportimport.internal.lar.LARManifestPathUtil;
import com.liferay.exportimport.kernel.exception.LARFileException;
import com.liferay.exportimport.kernel.lar.ExportImportThreadLocal;
import com.liferay.exportimport.kernel.lar.LARSite;
import com.liferay.exportimport.kernel.lar.ManifestSummary;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.PortletDataContextFactory;
import com.liferay.exportimport.site.ExportImportSiteProvider;
import com.liferay.petra.function.UnsafeBiConsumer;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;

import java.util.List;
import java.util.Objects;

/**
 * Imports the whole sites a LAR carries.
 *
 * <p>
 * The company level pass runs first and brings in the site settings, which is
 * what brings the sites themselves into existence when the target instance does
 * not have them yet. Only then is each site's content imported, one at a time,
 * each against a portlet data context of its own that reads the site's own
 * manifest and maps the site's source group onto the target group that carries
 * the same external reference code.
 * </p>
 *
 * @author Petteri Karttunen
 */
public class SiteImporter {

	public SiteImporter(
		ExportImportSiteProvider exportImportSiteProvider,
		GroupLocalService groupLocalService,
		PortletDataContextFactory portletDataContextFactory,
		SiteReporter siteReporter) {

		_exportImportSiteProvider = exportImportSiteProvider;
		_groupLocalService = groupLocalService;
		_portletDataContextFactory = portletDataContextFactory;
		_siteReporter = siteReporter;
	}

	/**
	 * Imports every site the LAR carries that the user selected, by handing a
	 * fresh per-site context to <code>importSiteUnsafeBiConsumer</code> once per
	 * site.
	 *
	 * <p>
	 * This is a no-op on a per-site context, which is what keeps the per-site
	 * passes from starting per-site passes of their own.
	 * </p>
	 */
	public void importSites(
			PortletDataContext portletDataContext, long userId,
			UnsafeBiConsumer<PortletDataContext, Long, Exception>
				importSiteUnsafeBiConsumer)
		throws Exception {

		if (SiteExportImportParameterUtil.isSitePass(portletDataContext) ||
			!SiteExportImportParameterUtil.isEnabled(
				portletDataContext.getCompanyId())) {

			return;
		}

		List<LARSite> larSites = _getLARSites(portletDataContext);

		if (ListUtil.isEmpty(larSites)) {
			return;
		}

		String[] selectedSiteExternalReferenceCodes =
			SiteExportImportParameterUtil.getSelectedSiteExternalReferenceCodes(
				portletDataContext);

		// A file is imported for everything it carries, and the whole sites it
		// carries are only a part of that, so selecting none of them is an
		// ordinary thing to do rather than something to report

		if (ArrayUtil.isEmpty(selectedSiteExternalReferenceCodes)) {
			return;
		}

		// A selection the file cannot satisfy means the user is not getting the
		// sites they asked for

		for (String selectedSiteExternalReferenceCode :
				selectedSiteExternalReferenceCodes) {

			if (!_containsSite(larSites, selectedSiteExternalReferenceCode)) {
				_siteReporter.reportMissingSite(
					portletDataContext, selectedSiteExternalReferenceCode);
			}
		}

		for (LARSite larSite : larSites) {

			// Sites the LAR carries but the user did not select are skipped

			if (!ArrayUtil.contains(
					selectedSiteExternalReferenceCodes,
					larSite.getExternalReferenceCode())) {

				if (_log.isInfoEnabled()) {
					_log.info(
						"Skipping unselected site " +
							larSite.getExternalReferenceCode());
				}

				continue;
			}

			Group group = _fetchSite(portletDataContext, larSite);

			if (group == null) {
				continue;
			}

			// The company level pass has already run, so every site the file
			// carries exists by now. A parent still missing here is one this
			// instance does not have and the file does not carry, which leaves
			// the site at the top level.

			_reportMissingParentSite(portletDataContext, larSite);

			if (_log.isInfoEnabled()) {
				_log.info(
					StringBundler.concat(
						"Importing site ", larSite.getExternalReferenceCode(),
						" from group ", larSite.getGroupId(), " into group ",
						group.getGroupId()));
			}

			importSiteUnsafeBiConsumer.accept(
				_createPortletDataContext(portletDataContext, larSite, group),
				userId);
		}
	}

	private boolean _containsSite(
		List<LARSite> larSites, String externalReferenceCode) {

		for (LARSite larSite : larSites) {
			if (Objects.equals(
					larSite.getExternalReferenceCode(),
					externalReferenceCode)) {

				return true;
			}
		}

		return false;
	}

	private PortletDataContext _createPortletDataContext(
			PortletDataContext portletDataContext, LARSite larSite, Group group)
		throws Exception {

		PortletDataContext sitePortletDataContext =
			_portletDataContextFactory.createImportPortletDataContext(
				portletDataContext.getCompanyId(), group.getGroupId(),
				SiteExportImportParameterUtil.toSiteImportParameterMap(
					portletDataContext.getParameterMap(),
					larSite.getExternalReferenceCode()),
				portletDataContext.getUserIdStrategy(),
				portletDataContext.getZipReader());

		sitePortletDataContext.setExportImportProcessId(
			portletDataContext.getExportImportProcessId());

		// The factory reads the manifest at the root of the LAR, which belongs
		// to the company level pass. Point the context at the site's own
		// manifest instead.

		Element rootElement = _getRootElement(
			sitePortletDataContext, larSite.getGroupId());

		sitePortletDataContext.setImportDataRootElement(rootElement);

		Element headerElement = rootElement.element("header");

		sitePortletDataContext.setSourceCompanyId(
			GetterUtil.getLong(headerElement.attributeValue("company-id")));
		sitePortletDataContext.setSourceCompanyGroupId(
			GetterUtil.getLong(
				headerElement.attributeValue("company-group-id")));
		sitePortletDataContext.setSourceUserPersonalSiteGroupId(
			GetterUtil.getLong(
				headerElement.attributeValue("user-personal-site-group-id")));

		sitePortletDataContext.setSourceGroupId(larSite.getGroupId());

		Element missingReferencesElement = rootElement.element(
			"missing-references");

		if (missingReferencesElement != null) {
			sitePortletDataContext.setMissingReferencesElement(
				missingReferencesElement);
		}

		// Private pages are out of scope, so a site is always its public page
		// set

		sitePortletDataContext.setPrivateLayout(false);

		return sitePortletDataContext;
	}

	private Group _fetchSite(
		PortletDataContext portletDataContext, LARSite larSite) {

		Group group = _groupLocalService.fetchGroupByExternalReferenceCode(
			larSite.getExternalReferenceCode(),
			portletDataContext.getCompanyId());

		if (group == null) {
			_log.error(
				StringBundler.concat(
					"Unable to import site ",
					larSite.getExternalReferenceCode(),
					" because no site exists with that external reference ",
					"code and none was created by the site settings the LAR ",
					"carries"));

			_siteReporter.reportUncreatedSite(
				portletDataContext, larSite.getExternalReferenceCode());

			return null;
		}

		if (!_exportImportSiteProvider.isSupported(group) ||
			ExportImportThreadLocal.isStagingInProcess()) {

			_log.error(
				"Site " + group.getGroupId() +
					" cannot be imported as a whole unit");

			_siteReporter.reportUnsupportedSite(portletDataContext, group);

			return null;
		}

		return group;
	}

	private List<LARSite> _getLARSites(PortletDataContext portletDataContext) {
		ManifestSummary manifestSummary =
			portletDataContext.getManifestSummary();

		if (manifestSummary == null) {
			return null;
		}

		return manifestSummary.getSites();
	}

	private Element _getRootElement(
			PortletDataContext portletDataContext, long sourceGroupId)
		throws Exception {

		String xml = portletDataContext.getZipEntryAsString(
			LARManifestPathUtil.getImportManifestXmlFilePath(sourceGroupId));

		if (Validator.isNull(xml)) {
			throw new LARFileException(LARFileException.TYPE_MISSING_MANIFEST);
		}

		try {
			Document document = SAXReaderUtil.read(xml);

			return document.getRootElement();
		}
		catch (Exception exception) {
			throw new LARFileException(
				LARFileException.TYPE_INVALID_MANIFEST, exception);
		}
	}

	private void _reportMissingParentSite(
		PortletDataContext portletDataContext, LARSite larSite) {

		String parentExternalReferenceCode =
			larSite.getParentExternalReferenceCode();

		if (Validator.isNull(parentExternalReferenceCode)) {
			return;
		}

		Group parentGroup =
			_groupLocalService.fetchGroupByExternalReferenceCode(
				parentExternalReferenceCode, portletDataContext.getCompanyId());

		if (parentGroup != null) {
			return;
		}

		_siteReporter.reportMissingParentSite(portletDataContext, larSite);
	}

	private static final Log _log = LogFactoryUtil.getLog(SiteImporter.class);

	private final ExportImportSiteProvider _exportImportSiteProvider;
	private final GroupLocalService _groupLocalService;
	private final PortletDataContextFactory _portletDataContextFactory;
	private final SiteReporter _siteReporter;

}