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
					contextCompany.getCompanyId(), search, _isAscending(sorts),
					pagination.getStartPosition(), pagination.getEndPosition()),
				group -> new PreviewSite() {
					{
						setChildSiteCount(
							() -> _exportImportSiteProvider.getChildSiteCount(
								group));
						setExternalReferenceCode(
							group::getExternalReferenceCode);
						setFriendlyUrlPath(group::getFriendlyURL);
						setHierarchy(
							() -> _exportImportSiteProvider.getHierarchy(
								group,
								contextAcceptLanguage.getPreferredLocale()));
						setName(group::getNameCurrentValue);

						// The only supported site whose class name is Company
						// is the site an instance keeps for content shared
						// across its sites

						setType(
							() -> {
								if (group.isCompany()) {
									return PreviewSite.Type.GLOBAL;
								}

								return PreviewSite.Type.SITE;
							});
					}
				}),
			pagination,
			_exportImportSiteProvider.getSupportedSitesCount(
				contextCompany.getCompanyId(), search));
	}

	/**
	 * The sites can only be ordered by name, so a sort names the direction and
	 * nothing else. Anything the caller did not ask for reads as ascending,
	 * which is the order the sites are listed in to begin with.
	 */
	private boolean _isAscending(Sort[] sorts) {
		if (ArrayUtil.isEmpty(sorts)) {
			return true;
		}

		Sort sort = sorts[0];

		return !sort.isReverse();
	}

	private static final EntityModel _entityModel =
		new PreviewSiteEntityModel();

	@Reference
	private ExportImportSiteProvider _exportImportSiteProvider;

	@Reference
	private StagingGroupHelper _stagingGroupHelper;

}