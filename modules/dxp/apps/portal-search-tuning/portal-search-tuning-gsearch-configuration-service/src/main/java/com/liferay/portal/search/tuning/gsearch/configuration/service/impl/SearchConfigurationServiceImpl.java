/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.portal.search.tuning.gsearch.configuration.service.impl;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.SearchConfigurationActionKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.SearchConfigurationConstants;
import com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration;
import com.liferay.portal.search.tuning.gsearch.configuration.service.SearchConfigurationLocalService;
import com.liferay.portal.search.tuning.gsearch.configuration.service.base.SearchConfigurationServiceBaseImpl;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * The implementation of the search configuration remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.portal.search.tuning.gsearch.configuration.service.SearchConfigurationService</code> interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SearchConfigurationServiceBaseImpl
 */
@Component(
	property = {
		"json.web.service.context.name=search",
		"json.web.service.context.path=SearchConfiguration"
	},
	service = AopService.class
)
public class SearchConfigurationServiceImpl
	extends SearchConfigurationServiceBaseImpl {

	public SearchConfiguration addCompanySearchConfiguration(
			Map<Locale, String> titleMap, Map<Locale, String> descriptionMap,
			String configuration, int type, ServiceContext serviceContext)
		throws PortalException {

		String actionKey =
			SearchConfigurationActionKeys.
				getActionKeyForSearchConfigurationType(
					type, ActionKeys.ADD_ENTRY);

		long groupId = _getCompanyGroupId(serviceContext);

		_portletResourcePermission.check(
			getPermissionChecker(), groupId, actionKey);

		return searchConfigurationLocalService.addSearchConfiguration(
			getUserId(), groupId, titleMap, descriptionMap, configuration, type,
			serviceContext);
	}

	public SearchConfiguration addGroupSearchConfiguration(
			Map<Locale, String> titleMap, Map<Locale, String> descriptionMap,
			String configuration, int type, ServiceContext serviceContext)
		throws PortalException {

		String actionKey =
			SearchConfigurationActionKeys.
				getActionKeyForSearchConfigurationType(
					type, ActionKeys.ADD_ENTRY);

		long groupId = serviceContext.getScopeGroupId();

		_portletResourcePermission.check(
			getPermissionChecker(), groupId, actionKey);

		return searchConfigurationLocalService.addSearchConfiguration(
			getUserId(), groupId, titleMap, descriptionMap, configuration, type,
			serviceContext);
	}
	
	public SearchConfiguration deleteSearchConfiguration(
			long searchConfigurationId)
		throws PortalException {

		SearchConfiguration searchConfiguration =
			_searchConfigurationLocalService.getSearchConfiguration(
				searchConfigurationId);

		String actionKey =
			SearchConfigurationActionKeys.
				getActionKeyForSearchConfigurationType(
					searchConfiguration.getType(), ActionKeys.DELETE);

		_searchConfigurationModelResourcePermission.check(
			getPermissionChecker(), searchConfigurationId, actionKey);

		return searchConfigurationLocalService.deleteSearchConfiguration(
			searchConfigurationId);
	}

	public List<SearchConfiguration> getGroupSearchConfigurations(
		long companyId, int type, int start, int end) {

		return getGroupSearchConfigurations(
			companyId, WorkflowConstants.STATUS_APPROVED, type, start, end);
	}

	public List<SearchConfiguration> getGroupSearchConfigurations(
		long companyId, int status, int type, int start, int end) {

		if (status == WorkflowConstants.STATUS_ANY) {
			return searchConfigurationPersistence.filterFindByG_T(
				companyId, type, start, end);
		}

		return searchConfigurationPersistence.filterFindByG_S_T(
			companyId, status, type, start, end);
	}

	public List<SearchConfiguration> getGroupSearchConfigurations(
		long companyId, int status, int type, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		if (status == WorkflowConstants.STATUS_ANY) {
			return searchConfigurationPersistence.findByG_T(
				companyId, type, start, end, orderByComparator);
		}

		return searchConfigurationPersistence.filterFindByG_S_T(
			companyId, status, type, start, end, orderByComparator);
	}

	public List<SearchConfiguration> getGroupSearchConfigurations(
		long companyId, int type, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getGroupSearchConfigurations(
			companyId, WorkflowConstants.STATUS_APPROVED, type, start, end,
			orderByComparator);
	}

	public int getGroupSearchConfigurationsCount(long companyId, int type) {
		return getGroupSearchConfigurationsCount(
			companyId, WorkflowConstants.STATUS_APPROVED, type);
	}

	public int getGroupSearchConfigurationsCount(
		long companyId, int status, int type) {

		if (status == WorkflowConstants.STATUS_ANY) {
			return searchConfigurationPersistence.countByG_T(companyId, type);
		}

		return searchConfigurationPersistence.countByG_S_T(
			companyId, status, type);
	}

	public SearchConfiguration getSearchConfiguration(
			long searchConfigurationId)
		throws PortalException {

		SearchConfiguration searchConfiguration =
			_searchConfigurationLocalService.getSearchConfiguration(
				searchConfigurationId);

		String actionKey =
			SearchConfigurationActionKeys.
				getActionKeyForSearchConfigurationType(
					searchConfiguration.getType(), ActionKeys.VIEW);

		_searchConfigurationModelResourcePermission.check(
			getPermissionChecker(), searchConfiguration, actionKey);

		return searchConfiguration;
	}

	public SearchConfiguration updateSearchConfiguration(
			long searchConfigurationId, Map<Locale, String> titleMap,
			Map<Locale, String> descriptionMap, String configuration,
			ServiceContext serviceContext)
		throws PortalException {

		SearchConfiguration searchConfiguration =
			_searchConfigurationLocalService.getSearchConfiguration(
				searchConfigurationId);

		String actionKey =
			SearchConfigurationActionKeys.
				getActionKeyForSearchConfigurationType(
					searchConfiguration.getType(), ActionKeys.UPDATE);

		_searchConfigurationModelResourcePermission.check(
			getPermissionChecker(), searchConfigurationId, actionKey);

		return _searchConfigurationLocalService.updateSearchConfiguration(
			getUserId(), searchConfigurationId, titleMap, descriptionMap,
			configuration, serviceContext);
	}

	private long _getCompanyGroupId(ServiceContext serviceContext)
		throws PortalException {

		Company company = _companyLocalService.getCompany(
			serviceContext.getCompanyId());

		return company.getGroupId();
	}

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(resource.name=" + SearchConfigurationConstants.RESOURCE_NAME + ")"
	)
	private volatile PortletResourcePermission _portletResourcePermission;

	@Reference
	private SearchConfigurationLocalService _searchConfigurationLocalService;

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(model.class.name=com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration)"
	)
	private volatile ModelResourcePermission<SearchConfiguration>
		_searchConfigurationModelResourcePermission;

	@Reference
	private UserLocalService _userLocalService;

}