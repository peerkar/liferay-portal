/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.internal.site;

import com.liferay.exportimport.site.ExportImportSiteProvider;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.GroupService;
import com.liferay.portal.kernel.util.LinkedHashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.comparator.GroupNameComparator;
import com.liferay.staging.StagingGroupHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(service = ExportImportSiteProvider.class)
public class ExportImportSiteProviderImpl implements ExportImportSiteProvider {

	@Override
	public int getChildSiteCount(Group group) {
		return _groupLocalService.getGroupsCount(
			group.getCompanyId(), group.getGroupId(), true);
	}

	@Override
	public String getDisplayName(Group group, Locale locale) {
		if (group == null) {
			return StringPool.BLANK;
		}

		try {
			return group.getDescriptiveName(locale);
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}

			return group.getName(locale);
		}
	}

	@Override
	public String getHierarchy(Group group, Locale locale) {
		if (group == null) {
			return StringPool.BLANK;
		}

		Group companyGroup = _groupLocalService.fetchCompanyGroup(
			group.getCompanyId());

		String globalName = StringPool.BLANK;

		if (companyGroup != null) {
			globalName = getDisplayName(companyGroup, locale);
		}

		// The Global site is the head of every path, so it is the whole path
		// when it is the site being described

		if ((companyGroup != null) &&
			(companyGroup.getGroupId() == group.getGroupId())) {

			return globalName;
		}

		List<String> names = new ArrayList<>();

		if (Validator.isNotNull(globalName)) {
			names.add(globalName);
		}

		// The ancestors come back nearest first, and the path reads from the
		// top down

		List<Group> ancestors = group.getAncestors();

		for (int i = ancestors.size() - 1; i >= 0; i--) {
			names.add(getDisplayName(ancestors.get(i), locale));
		}

		names.add(getDisplayName(group, locale));

		return StringUtil.merge(names, _HIERARCHY_SEPARATOR);
	}

	@Override
	public List<Group> getSupportedSites(
			long companyId, String search, int start, int end,
			OrderByComparator<Group> orderByComparator)
		throws PortalException {

		return ListUtil.subList(
			_getSupportedSites(companyId, search, orderByComparator), start,
			end);
	}

	@Override
	public int getSupportedSitesCount(long companyId, String search)
		throws PortalException {

		List<Group> groups = _getSupportedSites(
			companyId, search, new GroupNameComparator());

		return groups.size();
	}

	@Override
	public boolean isSupported(Group group) {
		if (group == null) {
			return false;
		}

		// A site, and nothing that merely looks like one. The Global site is a
		// site like any other here, even though its class name is Company. The
		// group that is excluded is the one the instance level export itself
		// runs under, which is a different group altogether.

		if (!group.isSite() || group.isDepot() || group.isLayoutPrototype() ||
			group.isLayoutSetPrototype() ||
			_stagingGroupHelper.isCompanyGroup(group)) {

			return false;
		}

		// CMS spaces are out of scope

		if (group.isCMS()) {
			return false;
		}

		// Staged sites are out of scope, and so are the staging groups that
		// belong to them

		if (group.isStaged() || group.isStagingGroup() || !group.isActive()) {
			return false;
		}

		return true;
	}

	private List<Group> _getSupportedSites(
			long companyId, String search,
			OrderByComparator<Group> orderByComparator)
		throws PortalException {

		List<Group> groups = _groupService.search(
			companyId,
			new long[] {
				_portal.getClassNameId(Company.class.getName()),
				_portal.getClassNameId(Group.class.getName())
			},
			search, null,
			LinkedHashMapBuilder.<String, Object>put(
				"active", Boolean.TRUE
			).put(
				"site", Boolean.TRUE
			).build(),
			true, QueryUtil.ALL_POS, QueryUtil.ALL_POS, orderByComparator);

		// The unsupported sites go before the range is applied, so that a page
		// of results is a page of sites the caller can act on

		return ListUtil.filter(groups, this::isSupported);
	}

	private static final String _HIERARCHY_SEPARATOR = " / ";

	private static final Log _log = LogFactoryUtil.getLog(
		ExportImportSiteProviderImpl.class);

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private GroupService _groupService;

	@Reference
	private Portal _portal;

	@Reference
	private StagingGroupHelper _stagingGroupHelper;

}