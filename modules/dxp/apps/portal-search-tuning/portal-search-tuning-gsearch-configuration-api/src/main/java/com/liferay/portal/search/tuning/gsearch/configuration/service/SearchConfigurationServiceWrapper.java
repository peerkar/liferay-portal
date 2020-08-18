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

package com.liferay.portal.search.tuning.gsearch.configuration.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link SearchConfigurationService}.
 *
 * @author Brian Wing Shun Chan
 * @see SearchConfigurationService
 * @generated
 */
public class SearchConfigurationServiceWrapper
	implements SearchConfigurationService,
			   ServiceWrapper<SearchConfigurationService> {

	public SearchConfigurationServiceWrapper(
		SearchConfigurationService searchConfigurationService) {

		_searchConfigurationService = searchConfigurationService;
	}

	@Override
	public com.liferay.portal.search.tuning.gsearch.configuration.model.
		SearchConfiguration addCompanySearchConfiguration(
				java.util.Map<java.util.Locale, String> titleMap,
				java.util.Map<java.util.Locale, String> descriptionMap,
				String configuration, int type,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
			throws com.liferay.portal.kernel.exception.PortalException {

		return _searchConfigurationService.addCompanySearchConfiguration(
			titleMap, descriptionMap, configuration, type, serviceContext);
	}

	@Override
	public com.liferay.portal.search.tuning.gsearch.configuration.model.
		SearchConfiguration addGroupSearchConfiguration(
				java.util.Map<java.util.Locale, String> titleMap,
				java.util.Map<java.util.Locale, String> descriptionMap,
				String configuration, int type,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
			throws com.liferay.portal.kernel.exception.PortalException {

		return _searchConfigurationService.addGroupSearchConfiguration(
			titleMap, descriptionMap, configuration, type, serviceContext);
	}

	@Override
	public com.liferay.portal.search.tuning.gsearch.configuration.model.
		SearchConfiguration deleteSearchConfiguration(
				long searchConfigurationId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return _searchConfigurationService.deleteSearchConfiguration(
			searchConfigurationId);
	}

	@Override
	public java.util.List
		<com.liferay.portal.search.tuning.gsearch.configuration.model.
			SearchConfiguration> getGroupSearchConfigurations(
				long companyId, int type, int start, int end) {

		return _searchConfigurationService.getGroupSearchConfigurations(
			companyId, type, start, end);
	}

	@Override
	public java.util.List
		<com.liferay.portal.search.tuning.gsearch.configuration.model.
			SearchConfiguration> getGroupSearchConfigurations(
				long companyId, int status, int type, int start, int end) {

		return _searchConfigurationService.getGroupSearchConfigurations(
			companyId, status, type, start, end);
	}

	@Override
	public java.util.List
		<com.liferay.portal.search.tuning.gsearch.configuration.model.
			SearchConfiguration> getGroupSearchConfigurations(
				long companyId, int status, int type, int start, int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.portal.search.tuning.gsearch.configuration.
						model.SearchConfiguration> orderByComparator) {

		return _searchConfigurationService.getGroupSearchConfigurations(
			companyId, status, type, start, end, orderByComparator);
	}

	@Override
	public java.util.List
		<com.liferay.portal.search.tuning.gsearch.configuration.model.
			SearchConfiguration> getGroupSearchConfigurations(
				long companyId, int type, int start, int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.portal.search.tuning.gsearch.configuration.
						model.SearchConfiguration> orderByComparator) {

		return _searchConfigurationService.getGroupSearchConfigurations(
			companyId, type, start, end, orderByComparator);
	}

	@Override
	public int getGroupSearchConfigurationsCount(long companyId, int type) {
		return _searchConfigurationService.getGroupSearchConfigurationsCount(
			companyId, type);
	}

	@Override
	public int getGroupSearchConfigurationsCount(
		long companyId, int status, int type) {

		return _searchConfigurationService.getGroupSearchConfigurationsCount(
			companyId, status, type);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _searchConfigurationService.getOSGiServiceIdentifier();
	}

	@Override
	public com.liferay.portal.search.tuning.gsearch.configuration.model.
		SearchConfiguration getSearchConfiguration(long searchConfigurationId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return _searchConfigurationService.getSearchConfiguration(
			searchConfigurationId);
	}

	@Override
	public com.liferay.portal.search.tuning.gsearch.configuration.model.
		SearchConfiguration updateSearchConfiguration(
				long searchConfigurationId,
				java.util.Map<java.util.Locale, String> titleMap,
				java.util.Map<java.util.Locale, String> descriptionMap,
				String configuration,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
			throws com.liferay.portal.kernel.exception.PortalException {

		return _searchConfigurationService.updateSearchConfiguration(
			searchConfigurationId, titleMap, descriptionMap, configuration,
			serviceContext);
	}

	@Override
	public SearchConfigurationService getWrappedService() {
		return _searchConfigurationService;
	}

	@Override
	public void setWrappedService(
		SearchConfigurationService searchConfigurationService) {

		_searchConfigurationService = searchConfigurationService;
	}

	private SearchConfigurationService _searchConfigurationService;

}