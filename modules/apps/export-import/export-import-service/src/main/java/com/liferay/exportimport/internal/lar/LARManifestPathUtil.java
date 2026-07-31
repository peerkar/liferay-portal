/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.internal.lar;

import com.liferay.exportimport.internal.site.SiteExportImportParameters;
import com.liferay.exportimport.kernel.lar.ExportImportPathUtil;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;

/**
 * Resolves which <code>manifest.xml</code> inside a LAR a given export or
 * import pass reads and writes.
 *
 * <p>
 * A LAR holding whole sites carries one manifest per site, because each site
 * has its own set of portlets and its own model counts. The manifest of the
 * company level pass that owns the LAR stays at the root, and every per-site
 * pass gets one under its own group folder:
 * </p>
 *
 * <p>
 * <pre>
 * <code>
 * /manifest.xml                 the company level pass
 * /group/{groupId}/manifest.xml one per exported site
 * </code>
 * </pre></p>
 *
 * <p>
 * A pass is a per-site pass when its portlet data context carries {@link
 * PortletDataHandlerKeys#SITE_EXTERNAL_REFERENCE_CODE}. That parameter is set
 * once, when the per-site context is created, and is never mutated afterwards.
 * </p>
 *
 * @author Petteri Karttunen
 */
public class LARManifestPathUtil {

	public static final String MANIFEST_XML_FILE_PATH = "/manifest.xml";

	/**
	 * Returns the path of the manifest the given export pass writes to. The
	 * group folder comes from the scope group ID, which is what the export
	 * writers use for every other entry they add.
	 *
	 * @see ExportImportPathUtil#getRootPath(PortletDataContext)
	 */
	public static String getExportManifestXmlFilePath(
		PortletDataContext portletDataContext) {

		if (!_isSitePass(portletDataContext)) {
			return MANIFEST_XML_FILE_PATH;
		}

		return ExportImportPathUtil.getRootPath(portletDataContext) +
			MANIFEST_XML_FILE_PATH;
	}

	/**
	 * Returns the path of the manifest of the site exported from the given
	 * group. Used to read a site's manifest before a per-site import context
	 * exists for it.
	 */
	public static String getImportManifestXmlFilePath(long sourceGroupId) {
		return StringBundler.concat(
			StringPool.FORWARD_SLASH, ExportImportPathUtil.PATH_PREFIX_GROUP,
			StringPool.FORWARD_SLASH, sourceGroupId, MANIFEST_XML_FILE_PATH);
	}

	/**
	 * Returns the path of the manifest the given import pass reads from. The
	 * group folder comes from the source group ID, because the folders inside
	 * the LAR are named after the groups of the instance that produced it.
	 *
	 * @see ExportImportPathUtil#getSourceRootPath(PortletDataContext)
	 */
	public static String getImportManifestXmlFilePath(
		PortletDataContext portletDataContext) {

		if (!_isSitePass(portletDataContext)) {
			return MANIFEST_XML_FILE_PATH;
		}

		return ExportImportPathUtil.getSourceRootPath(portletDataContext) +
			MANIFEST_XML_FILE_PATH;
	}

	private static boolean _isSitePass(PortletDataContext portletDataContext) {
		return SiteExportImportParameters.isSitePass(portletDataContext);
	}

}