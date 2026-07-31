/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.internal.site;

import com.liferay.exportimport.kernel.lar.ExportImportHelper;
import com.liferay.exportimport.kernel.lar.ExportImportThreadLocal;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.PortletDataContextFactory;
import com.liferay.exportimport.site.ExportImportSiteProvider;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.xml.Element;

/**
 * Exports whole sites into the LAR owned by a company level export.
 *
 * <p>
 * Each selected site is exported by running the layout export a second time
 * against a portlet data context of its own, sharing the archive the company
 * level pass is writing to. A context is never reused across sites: as it runs
 * it accumulates primary keys, references, permissions, locks and asset
 * metadata, none of which is meaningful for the next site.
 * </p>
 *
 * @author Petteri Karttunen
 */
public class SiteExporter {

	public SiteExporter(
		ExportImportHelper exportImportHelper,
		ExportImportSiteProvider exportImportSiteProvider,
		GroupLocalService groupLocalService,
		PortletDataContextFactory portletDataContextFactory) {

		_exportImportHelper = exportImportHelper;
		_exportImportSiteProvider = exportImportSiteProvider;
		_groupLocalService = groupLocalService;
		_portletDataContextFactory = portletDataContextFactory;
	}

	/**
	 * Records the sites this LAR carries. On the company level pass this adds
	 * the inventory the import side reads to learn which sites the LAR holds
	 * and under which group folder each one lives. On a per-site pass it stamps
	 * the site's external reference code onto the header, which is how the
	 * import side matches the site to one in the target instance.
	 */
	public void addSiteElements(
		PortletDataContext portletDataContext, Element headerElement) {

		String siteExternalReferenceCode =
			SiteExportImportParameters.getSiteERC(portletDataContext);

		if (siteExternalReferenceCode != null) {
			headerElement.addAttribute(
				"site-external-reference-code", siteExternalReferenceCode);

			return;
		}

		if (!SiteExportImportParameters.isEnabled(
				portletDataContext.getCompanyId())) {

			return;
		}

		String[] siteExternalReferenceCodes =
			SiteExportImportParameters.getSelectedSiteERCs(portletDataContext);

		if (siteExternalReferenceCodes.length == 0) {
			return;
		}

		Element sitesElement = headerElement.addElement("sites");

		for (String curSiteExternalReferenceCode : siteExternalReferenceCodes) {
			Group group = _fetchSite(
				portletDataContext, curSiteExternalReferenceCode);

			if (group == null) {
				continue;
			}

			Element siteElement = sitesElement.addElement("site");

			siteElement.addAttribute(
				"child-site-count",
				String.valueOf(
					_exportImportSiteProvider.getChildSiteCount(group)));
			siteElement.addAttribute(
				"external-reference-code", group.getExternalReferenceCode());
			siteElement.addAttribute("friendly-url", group.getFriendlyURL());

			// The only supported site whose class name is Company is the site
			// an instance keeps for content shared across its sites. The group
			// the instance level export itself runs under is not a site and is
			// already left out.

			siteElement.addAttribute(
				"global", String.valueOf(group.isCompany()));

			siteElement.addAttribute(
				"group-id", String.valueOf(group.getGroupId()));

			// The path describes the instance the LAR is leaving, so it is
			// composed now and travels as composed

			siteElement.addAttribute(
				"hierarchy",
				_exportImportSiteProvider.getHierarchy(
					group, LocaleUtil.getSiteDefault()));

			siteElement.addAttribute("name", group.getNameCurrentValue());
			siteElement.addAttribute("uuid", group.getUuid());
		}
	}

	/**
	 * Exports every selected site into the LAR the given company level context
	 * is writing to, by handing a fresh per-site context to
	 * <code>exportSiteUnsafeConsumer</code> once per site.
	 *
	 * <p>
	 * This is a no-op on a per-site context, which is what keeps the per-site
	 * passes from starting per-site passes of their own.
	 * </p>
	 */
	public void exportSites(
			PortletDataContext portletDataContext,
			UnsafeConsumer<PortletDataContext, Exception>
				exportSiteUnsafeConsumer)
		throws Exception {

		if (SiteExportImportParameters.isSitePass(portletDataContext) ||
			!SiteExportImportParameters.isEnabled(
				portletDataContext.getCompanyId())) {

			return;
		}

		String[] siteExternalReferenceCodes =
			SiteExportImportParameters.getSelectedSiteERCs(portletDataContext);

		for (String siteExternalReferenceCode : siteExternalReferenceCodes) {
			Group group = _fetchSite(
				portletDataContext, siteExternalReferenceCode);

			if (group == null) {
				continue;
			}

			if (_log.isInfoEnabled()) {
				_log.info(
					StringBundler.concat(
						"Exporting site ", siteExternalReferenceCode,
						" from group ", group.getGroupId()));
			}

			exportSiteUnsafeConsumer.accept(
				_createPortletDataContext(portletDataContext, group));
		}
	}

	private PortletDataContext _createPortletDataContext(
			PortletDataContext portletDataContext, Group group)
		throws Exception {

		PortletDataContext sitePortletDataContext =
			_portletDataContextFactory.createExportPortletDataContext(
				portletDataContext.getCompanyId(), group.getGroupId(),
				SiteExportImportParameters.toSiteExportParameterMap(
					portletDataContext.getParameterMap(),
					group.getExternalReferenceCode()),
				portletDataContext.getStartDate(),
				portletDataContext.getEndDate(),
				portletDataContext.getZipWriter());

		sitePortletDataContext.setExportImportProcessId(
			portletDataContext.getExportImportProcessId());

		// Private pages are out of scope, so a site is always its public page
		// set

		sitePortletDataContext.setPrivateLayout(false);
		sitePortletDataContext.setLayoutIds(
			_exportImportHelper.getAllLayoutIds(group.getGroupId(), false));

		return sitePortletDataContext;
	}

	private Group _fetchSite(
		PortletDataContext portletDataContext,
		String siteExternalReferenceCode) {

		Group group = _groupLocalService.fetchGroupByExternalReferenceCode(
			siteExternalReferenceCode, portletDataContext.getCompanyId());

		if (group == null) {
			_log.error(
				"No site exists with external reference code " +
					siteExternalReferenceCode);

			return null;
		}

		if (!_exportImportSiteProvider.isSupported(group) ||
			ExportImportThreadLocal.isStagingInProcess()) {

			_log.error(
				"Site " + group.getGroupId() +
					" cannot be exported as a whole unit");

			return null;
		}

		return group;
	}

	private static final Log _log = LogFactoryUtil.getLog(SiteExporter.class);

	private final ExportImportHelper _exportImportHelper;
	private final ExportImportSiteProvider _exportImportSiteProvider;
	private final GroupLocalService _groupLocalService;
	private final PortletDataContextFactory _portletDataContextFactory;

}