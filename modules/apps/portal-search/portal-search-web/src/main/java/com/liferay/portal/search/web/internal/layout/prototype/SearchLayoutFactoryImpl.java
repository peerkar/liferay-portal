/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.search.web.internal.layout.prototype;

import com.liferay.layout.page.template.importer.LayoutPageTemplatesImporter;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructure;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalService;
import com.liferay.layout.page.template.util.LayoutPrototypeHelperUtil;
import com.liferay.layout.util.LayoutCopyHelper;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.LayoutPrototype;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.LayoutPrototypeLocalService;
import com.liferay.portal.kernel.service.LayoutSetLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.language.LanguageResources;
import com.liferay.portal.search.web.layout.prototype.SearchLayoutPrototypeCustomizer;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Adam Brandizzi
 * @author André de Oliveira
 * @author Lino Alves
 * @author Petteri Karttunen
 */
@Component(immediate = true, service = SearchLayoutFactory.class)
public class SearchLayoutFactoryImpl implements SearchLayoutFactory {

	@Override
	public void createSearchLayout(Group group) {
		if (!_shouldCreateSearchLayout(group)) {
			return;
		}

		ServiceContext serviceContext = new ServiceContext();

		String currentName = PrincipalThreadLocal.getName();
		ServiceContext currentServiceContext =
			ServiceContextThreadLocal.popServiceContext();

		PrincipalThreadLocal.setName(String.valueOf(group.getCreatorUserId()));
		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			Layout layout = _layoutLocalService.addLayout(
				group.getCreatorUserId(), group.getGroupId(), false,
				LayoutConstants.DEFAULT_PARENT_LAYOUT_ID,
				_getSearchTitleLocalizationMap(),
				_getSearchTitleLocalizationMap(),
				_getSearchDescriptionLocalizationMap(), null, null,
				LayoutConstants.TYPE_CONTENT, StringPool.BLANK, true,
				_getFriendlyURLMap(), serviceContext);

			Layout draftLayout = layout.fetchDraftLayout();

			_importPageElement(draftLayout);

			layout = _layoutCopyHelper.copyLayout(draftLayout, layout);

			// TODO

			_layoutLocalService.updateLayout(
				layout.getGroupId(), layout.isPrivateLayout(),
				layout.getLayoutId(), layout.getTypeSettings());

			_layoutLocalService.updateStatus(
				layout.getUserId(), layout.getPlid(),
				WorkflowConstants.STATUS_APPROVED, serviceContext);

			UnicodeProperties unicodeProperties =
				group.getTypeSettingsProperties();

			unicodeProperties.put("searchLayoutCreated", "true");

			group.setTypeSettingsProperties(unicodeProperties);

			groupLocalService.updateGroup(group);

			if (_log.isInfoEnabled()) {
				_log.info("Search Page created");
			}
		}
		catch (RuntimeException runtimeException) {
			throw runtimeException;
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
		finally {
			PrincipalThreadLocal.setName(currentName);
			ServiceContextThreadLocal.pushServiceContext(currentServiceContext);
		}
	}

	@Override
	public void createSearchLayoutPrototype(Company company) {
		long companyId = company.getCompanyId();

		try {
			createSearchLayoutPrototype(
				companyId, userLocalService.getDefaultUserId(companyId),
				_getSearchTitleLocalizationMap(),
				_getSearchDescriptionLocalizationMap());
		}
		catch (RuntimeException runtimeException) {
			throw runtimeException;
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
	}

	protected void createSearchLayoutPrototype(
			long companyId, long defaultUserId, Map<Locale, String> nameMap,
			Map<Locale, String> descriptionMap)
		throws Exception {

		String layoutTemplateId = getLayoutTemplateId();

		List<LayoutPrototype> layoutPrototypes =
			layoutPrototypeLocalService.search(
				companyId, null, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		Layout layout = LayoutPrototypeHelperUtil.addLayoutPrototype(
			layoutPrototypeLocalService, companyId, defaultUserId, nameMap,
			descriptionMap, layoutTemplateId, layoutPrototypes);

		if (layout == null) {
			return;
		}

		customize(layout);

		if (_log.isInfoEnabled()) {
			_log.info("Search Page Template created");
		}
	}

	protected void customize(Layout layout) throws Exception {
		if (searchLayoutPrototypeCustomizer != null) {
			searchLayoutPrototypeCustomizer.customize(layout);
		}
		else {
			_defaultSearchLayoutPrototypeCustomizer.customize(layout);
		}
	}

	protected String getLayoutTemplateId() {
		if (searchLayoutPrototypeCustomizer != null) {
			return searchLayoutPrototypeCustomizer.getLayoutTemplateId();
		}

		return _defaultSearchLayoutPrototypeCustomizer.getLayoutTemplateId();
	}

	@Reference
	protected GroupLocalService groupLocalService;

	@Reference
	protected LayoutLocalService layoutLocalService;

	@Reference
	protected LayoutPrototypeLocalService layoutPrototypeLocalService;

	@Reference(
		cardinality = ReferenceCardinality.OPTIONAL,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	protected volatile SearchLayoutPrototypeCustomizer
		searchLayoutPrototypeCustomizer;

	@Reference
	protected UserLocalService userLocalService;

	private Map<Locale, String> _getFriendlyURLMap() {
		return LocalizationUtil.getLocalizationMap("/search");
	}

	private Map<Locale, String> _getLocalizationMap(String key) {
		return ResourceBundleUtil.getLocalizationMap(
			LanguageResources.PORTAL_RESOURCE_BUNDLE_LOADER, key);
	}

	private Map<Locale, String> _getSearchDescriptionLocalizationMap() {
		return _getLocalizationMap("layout-prototype-search-description");
	}

	private Map<Locale, String> _getSearchTitleLocalizationMap() {
		return _getLocalizationMap("layout-prototype-search-title");
	}

	private boolean _hasSearchLayout(Group group) {
		Layout layout = layoutLocalService.fetchLayoutByFriendlyURL(
			group.getGroupId(), false, "/search");

		if (layout != null) {
			return true;
		}

		return false;
	}

	private void _importPageElement(Layout layout) throws PortalException {
		try {
			LayoutPageTemplateStructure layoutPageTemplateStructure =
				_layoutPageTemplateStructureLocalService.
					fetchLayoutPageTemplateStructure(
						layout.getGroupId(), layout.getPlid(), true);

			LayoutStructure layoutStructure = LayoutStructure.of(
				layoutPageTemplateStructure.getDefaultSegmentsExperienceData());

			Class<?> clazz = getClass();

			String pageElementJSON = StringUtil.read(
				clazz.getClassLoader(), _PATH + "page-definition.json");

			_layoutPageTemplatesImporter.importPageElement(
				layout, layoutStructure, layoutStructure.getMainItemId(),
				pageElementJSON, 0);
		}
		catch (Exception exception) {
			throw new PortalException(exception);
		}
	}

	private boolean _shouldCreateSearchLayout(Group group) {
		if (_hasSearchLayout(group)) {
			return false;
		}

		UnicodeProperties unicodeProperties = group.getTypeSettingsProperties();

		if (unicodeProperties.get("searchLayoutCreated") != null) {
			return false;
		}

		return true;
	}

	private static final String _PATH =
		"com/liferay/portal/search/web/internal/layout/prototype/dependencies/";

	private static final Log _log = LogFactoryUtil.getLog(
		SearchLayoutFactoryImpl.class);

	private final SearchLayoutPrototypeCustomizer
		_defaultSearchLayoutPrototypeCustomizer =
			new DefaultSearchLayoutPrototypeCustomizer();

	@Reference
	private LayoutCopyHelper _layoutCopyHelper;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutPageTemplatesImporter _layoutPageTemplatesImporter;

	@Reference
	private LayoutPageTemplateStructureLocalService
		_layoutPageTemplateStructureLocalService;

	@Reference
	private LayoutSetLocalService _layoutSetLocalService;

}