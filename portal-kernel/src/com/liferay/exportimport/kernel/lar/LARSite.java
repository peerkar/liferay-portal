/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.kernel.lar;

import java.io.Serializable;

/**
 * A site a LAR carries as a whole unit, as recorded in the manifest at the root
 * of the LAR.
 *
 * <p>
 * The group ID is the one the site had in the instance that produced the LAR. It
 * names the folder the site's content and its own manifest live under, and it is
 * meaningless in any other instance. The external reference code is what
 * identifies the site across instances.
 * </p>
 *
 * @author Petteri Karttunen
 */
public class LARSite implements Serializable {

	public LARSite(
		int childSiteCount, String externalReferenceCode, String friendlyURL,
		long groupId, String hierarchy, String name, boolean global) {

		_childSiteCount = childSiteCount;
		_externalReferenceCode = externalReferenceCode;
		_friendlyURL = friendlyURL;
		_groupId = groupId;
		_hierarchy = hierarchy;
		_name = name;
		_global = global;
	}

	/**
	 * Returns how many sites sat directly below this one in the instance the LAR
	 * came from. The child sites travel as sites of their own, so this is a
	 * count and not a hierarchy.
	 */
	public int getChildSiteCount() {
		return _childSiteCount;
	}

	public String getExternalReferenceCode() {
		return _externalReferenceCode;
	}

	public String getFriendlyURL() {
		return _friendlyURL;
	}

	public long getGroupId() {
		return _groupId;
	}

	/**
	 * Returns where this site sat in the instance the LAR came from, as a path
	 * ready to be shown, such as <code>Global / My Site / Child</code>.
	 *
	 * <p>
	 * The path is composed when the LAR is written and travels as written,
	 * because it describes the instance the LAR came from rather than the one it
	 * is being read in. Nothing splits it back into its parts.
	 * </p>
	 */
	public String getHierarchy() {
		return _hierarchy;
	}

	public String getName() {
		return _name;
	}

	/**
	 * Returns <code>true</code> when this is the site an instance keeps for
	 * content shared across its sites.
	 */
	public boolean isGlobal() {
		return _global;
	}

	private final int _childSiteCount;
	private final String _externalReferenceCode;
	private final String _friendlyURL;
	private final boolean _global;
	private final long _groupId;
	private final String _hierarchy;
	private final String _name;

}