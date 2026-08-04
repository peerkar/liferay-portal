/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.site;

import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.repository.model.FileEntry;

import java.util.List;

/**
 * Reads the sites a LAR carries as whole units out of the manifest at its root.
 *
 * <p>
 * The inventory is read where it is needed rather than kept on the manifest
 * summary, because the sites a LAR carries are an inventory of scopes rather
 * than a count of models, and because the summary is portal API while carrying
 * whole sites is not.
 * </p>
 *
 * @author Petteri Karttunen
 */
public interface LARSiteReader {

	/**
	 * Returns the sites the given LAR carries, in export order, or an empty list
	 * when it carries none. Reads a file that has been uploaded but is not being
	 * imported yet, which is what a preview works from.
	 */
	public List<LARSite> getLARSites(FileEntry fileEntry) throws Exception;

	/**
	 * Returns the sites the LAR of the given pass carries, in export order, or an
	 * empty list when it carries none.
	 */
	public List<LARSite> getLARSites(PortletDataContext portletDataContext)
		throws Exception;

}