/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.site;

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
		int childSiteCount, String externalReferenceCode, long groupId,
		String path, String name, String parentExternalReferenceCode,
		boolean global) {

		_childSiteCount = childSiteCount;
		_externalReferenceCode = externalReferenceCode;
		_groupId = groupId;
		_path = path;
		_name = name;
		_parentExternalReferenceCode = parentExternalReferenceCode;
		_global = global;
	}

	/**
	 * Returns how many sites sat directly below this one in the instance the LAR
	 * came from. The child sites travel as sites of their own, so this is a
	 * count and not a path.
	 */
	public int getChildSiteCount() {
		return _childSiteCount;
	}

	public String getExternalReferenceCode() {
		return _externalReferenceCode;
	}

	public long getGroupId() {
		return _groupId;
	}

	public String getName() {
		return _name;
	}

	/**
	 * Returns the external reference code of the site this one sat below, or
	 * <code>null</code> when it sat at the top level.
	 *
	 * <p>
	 * The code identifies the parent across instances, so an import can tell
	 * whether the parent is one the file carries, one the instance already has,
	 * or one that exists nowhere. A site whose parent exists nowhere is imported
	 * at the top level, because there is nothing for it to sit below.
	 * </p>
	 */
	public String getParentExternalReferenceCode() {
		return _parentExternalReferenceCode;
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
	public String getPath() {
		return _path;
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
	private final boolean _global;
	private final long _groupId;
	private final String _name;
	private final String _parentExternalReferenceCode;
	private final String _path;

}