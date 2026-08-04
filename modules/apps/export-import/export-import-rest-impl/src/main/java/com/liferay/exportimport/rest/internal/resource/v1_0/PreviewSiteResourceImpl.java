/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.rest.internal.resource.v1_0;

import com.liferay.exportimport.rest.dto.v1_0.PreviewSite;
import com.liferay.exportimport.rest.internal.odata.entity.v1_0.PreviewSiteEntityModel;
import com.liferay.exportimport.rest.internal.util.PermissionUtil;
import com.liferay.exportimport.rest.resource.v1_0.PreviewSiteResource;
import com.liferay.exportimport.site.ExportImportSiteProvider;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.staging.StagingGroupHelper;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MultivaluedMap;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Petteri Karttunen
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/preview-site.properties",
	scope = ServiceScope.PROTOTYPE, service = PreviewSiteResource.class
)
public class PreviewSiteResourceImpl extends BasePreviewSiteResourceImpl {

	@Override
	public EntityModel getEntityModel(MultivaluedMap multivaluedMap) {
		return _entityModel;
	}

	@Override
	public Page<PreviewSite> getExportPreviewSitesPage(
			String search, Pagination pagination, Sort[] sorts)
		throws Exception {

		Group companyGroup = _stagingGroupHelper.fetchCompanyGroup(
			contextCompany.getCompanyId());

		if (companyGroup == null) {
			throw new NotFoundException();
		}

		PermissionUtil.checkExportPermission(
			contextCompany.getCompanyId(), companyGroup.getGroupId());

		if (!FeatureFlagManagerUtil.isEnabled(
				contextCompany.getCompanyId(), "LPD-85946")) {

			return Page.of(Collections.emptyList());
		}

		return Page.of(
			transform(
				_exportImportSiteProvider.getSupportedSites(
					contextCompany.getCompanyId(), search,
					pagination.getStartPosition(), pagination.getEndPosition(),
					_getComparator(sorts)),
				group -> new PreviewSite() {
					{
						setChildSiteCount(
							() -> _exportImportSiteProvider.getChildSiteCount(
								group));
						setDescriptiveName(
							() -> _exportImportSiteProvider.getDisplayName(
								group,
								contextAcceptLanguage.getPreferredLocale()));
						setExternalReferenceCode(
							group::getExternalReferenceCode);
						setGlobal(group::isCompany);
						setPath(
							() -> _exportImportSiteProvider.getPath(
								group,
								contextAcceptLanguage.getPreferredLocale()));
					}
				}),
			pagination,
			_exportImportSiteProvider.getSupportedSitesCount(
				contextCompany.getCompanyId(), search));
	}

	/**
	 * The sites can only be ordered by the name they go by, so a sort names the
	 * direction and nothing else. Anything the caller did not ask for reads as
	 * ascending, which is the order the sites are listed in to begin with.
	 */
	private Comparator<Group> _getComparator(Sort[] sorts) {
		Locale locale = contextAcceptLanguage.getPreferredLocale();

		// The name a site goes by can reach the database, and a comparator is
		// asked for its key on every comparison rather than once per site, so
		// each name is worked out once and kept

		Map<Long, String> displayNames = new HashMap<>();

		Comparator<Group> comparator = Comparator.comparing(
			group -> displayNames.computeIfAbsent(
				group.getGroupId(),
				groupId -> _exportImportSiteProvider.getDisplayName(
					group, locale)),
			String.CASE_INSENSITIVE_ORDER);

		if (ArrayUtil.isEmpty(sorts)) {
			return comparator;
		}

		Sort sort = sorts[0];

		if (sort.isReverse()) {
			return comparator.reversed();
		}

		return comparator;
	}

	private static final EntityModel _entityModel =
		new PreviewSiteEntityModel();

	@Reference
	private ExportImportSiteProvider _exportImportSiteProvider;

	@Reference
	private StagingGroupHelper _stagingGroupHelper;

}