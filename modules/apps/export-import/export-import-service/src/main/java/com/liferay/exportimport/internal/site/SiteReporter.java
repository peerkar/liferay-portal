/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.internal.site;

import com.liferay.exportimport.kernel.lar.ExportImportThreadLocal;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.report.constants.ExportImportReportEntryConstants;
import com.liferay.exportimport.report.service.ExportImportReportEntryLocalService;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.util.GetterUtil;

/**
 * Records what became of the sites a LAR carries, on the process that carried
 * them.
 *
 * <p>
 * A site that cannot be imported is the kind of failure a user has to be told
 * about, and the log is not where they look. Everything here lands on the import
 * process, next to the entries the batch engine writes for the items it could
 * not import.
 * </p>
 *
 * @author Petteri Karttunen
 */
public class SiteReporter {

	public SiteReporter(
		ClassNameLocalService classNameLocalService,
		ExportImportReportEntryLocalService
			exportImportReportEntryLocalService) {

		_classNameLocalService = classNameLocalService;
		_exportImportReportEntryLocalService =
			exportImportReportEntryLocalService;
	}

	/**
	 * Reports a site the user selected that the LAR does not carry. The
	 * selection and the file disagree, which means the user is not getting what
	 * they asked for.
	 */
	public void reportMissingSite(
		PortletDataContext portletDataContext, String externalReferenceCode) {

		_addReportEntry(
			portletDataContext, externalReferenceCode,
			ExportImportReportEntryConstants.TYPE_ERROR,
			"The file does not carry a site with external reference code " +
				externalReferenceCode);
	}

	/**
	 * Reports a site the user selected that no site settings brought into
	 * existence, which leaves nothing to import the site's content into.
	 */
	public void reportUncreatedSite(
		PortletDataContext portletDataContext, String externalReferenceCode) {

		_addReportEntry(
			portletDataContext, externalReferenceCode,
			ExportImportReportEntryConstants.TYPE_ERROR,
			StringBundler.concat(
				"Unable to import the site with external reference code ",
				externalReferenceCode, " because no site of that external ",
				"reference code exists and the site settings the file carries ",
				"created none"));
	}

	/**
	 * Reports a site the user selected that this instance cannot take as a whole
	 * unit, such as a staged site.
	 */
	public void reportUnsupportedSite(
		PortletDataContext portletDataContext, Group group) {

		_addReportEntry(
			portletDataContext, group.getExternalReferenceCode(),
			ExportImportReportEntryConstants.TYPE_ERROR,
			"The site " + group.getGroupId() +
				" cannot be imported as a whole unit");
	}

	private void _addReportEntry(
		PortletDataContext portletDataContext, String externalReferenceCode,
		int type, String message) {

		// The sites travel in a company level LAR, so the entries belong to the
		// company rather than to a site of its own

		_exportImportReportEntryLocalService.getOrAddExportImportReportEntry(
			0, portletDataContext.getCompanyId(),
			GetterUtil.getString(externalReferenceCode),
			_classNameLocalService.getClassNameId(Group.class.getName()), 0,
			GetterUtil.getLong(
				ExportImportThreadLocal.getExportImportConfigurationId()),
			type, message, null, "sites");
	}

	private final ClassNameLocalService _classNameLocalService;
	private final ExportImportReportEntryLocalService
		_exportImportReportEntryLocalService;

}