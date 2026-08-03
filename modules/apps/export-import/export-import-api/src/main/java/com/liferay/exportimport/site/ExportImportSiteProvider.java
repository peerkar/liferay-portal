/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.site;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;
import java.util.Locale;

/**
 * Answers which sites can be exported and imported as whole units.
 *
 * <p>
 * Any site can be exported on its own, from its own export UI. This is about
 * the narrower case of carrying whole sites inside a company level LAR, which
 * does not support every kind of site. The UI that offers sites to pick and the
 * exporter that writes them both ask here, so that a site the user can pick is
 * always a site the exporter accepts.
 * </p>
 *
 * @author Petteri Karttunen
 */
public interface ExportImportSiteProvider {

	/**
	 * Returns how many sites sit directly below the given site.
	 */
	public int getChildSiteCount(Group group);

	/**
	 * Returns the name of the given site as it should be shown.
	 *
	 * <p>
	 * The name a site is stored under is not always the name it goes by. The
	 * Global site is stored under the ID of its company, and the site an
	 * instance starts life with is stored as <code>Guest</code> while it goes by
	 * a name of its own.
	 * </p>
	 */
	public String getDisplayName(Group group, Locale locale);

	/**
	 * Returns where the given site sits, as a path ready to be shown, such as
	 * <code>Global / My Site / Child</code>.
	 *
	 * <p>
	 * Every path starts at the Global site, which is what the sites of an
	 * instance have in common even though it is not their parent.
	 * </p>
	 */
	public String getHierarchy(Group group, Locale locale);

	/**
	 * Returns the sites that can be exported and imported as whole units,
	 * ordered by name, narrowed to those matching <code>search</code>.
	 *
	 * <p>
	 * The unsupported sites are left out before the range is applied, so the
	 * range counts only sites the caller can act on.
	 * </p>
	 */
	public List<Group> getSupportedSites(
			long companyId, String search, int start, int end,
			OrderByComparator<Group> orderByComparator)
		throws PortalException;

	public int getSupportedSitesCount(long companyId, String search)
		throws PortalException;

	/**
	 * Returns <code>true</code> when the given site can be exported and
	 * imported as a whole unit.
	 */
	public boolean isSupported(Group group);

}