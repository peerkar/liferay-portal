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

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the remote service utility for SearchConfiguration. This utility wraps
 * <code>com.liferay.portal.search.tuning.gsearch.configuration.service.impl.SearchConfigurationServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see SearchConfigurationService
 * @generated
 */
public class SearchConfigurationServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.portal.search.tuning.gsearch.configuration.service.impl.SearchConfigurationServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.portal.search.tuning.gsearch.configuration.model.
		SearchConfiguration addCompanySearchConfiguration(
				java.util.Map<java.util.Locale, String> titleMap,
				java.util.Map<java.util.Locale, String> descriptionMap,
				String configuration, int type,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addCompanySearchConfiguration(
			titleMap, descriptionMap, configuration, type, serviceContext);
	}

	public static com.liferay.portal.search.tuning.gsearch.configuration.model.
		SearchConfiguration addGroupSearchConfiguration(
				java.util.Map<java.util.Locale, String> titleMap,
				java.util.Map<java.util.Locale, String> descriptionMap,
				String configuration, int type,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addGroupSearchConfiguration(
			titleMap, descriptionMap, configuration, type, serviceContext);
	}

	public static com.liferay.portal.search.tuning.gsearch.configuration.model.
		SearchConfiguration deleteSearchConfiguration(
				long searchConfigurationId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteSearchConfiguration(searchConfigurationId);
	}

	public static java.util.List
		<com.liferay.portal.search.tuning.gsearch.configuration.model.
			SearchConfiguration> getGroupSearchConfigurations(
				long companyId, int type, int start, int end) {

		return getService().getGroupSearchConfigurations(
			companyId, type, start, end);
	}

	public static java.util.List
		<com.liferay.portal.search.tuning.gsearch.configuration.model.
			SearchConfiguration> getGroupSearchConfigurations(
				long companyId, int status, int type, int start, int end) {

		return getService().getGroupSearchConfigurations(
			companyId, status, type, start, end);
	}

	public static java.util.List
		<com.liferay.portal.search.tuning.gsearch.configuration.model.
			SearchConfiguration> getGroupSearchConfigurations(
				long companyId, int status, int type, int start, int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.portal.search.tuning.gsearch.configuration.
						model.SearchConfiguration> orderByComparator) {

		return getService().getGroupSearchConfigurations(
			companyId, status, type, start, end, orderByComparator);
	}

	public static java.util.List
		<com.liferay.portal.search.tuning.gsearch.configuration.model.
			SearchConfiguration> getGroupSearchConfigurations(
				long companyId, int type, int start, int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.portal.search.tuning.gsearch.configuration.
						model.SearchConfiguration> orderByComparator) {

		return getService().getGroupSearchConfigurations(
			companyId, type, start, end, orderByComparator);
	}

	public static int getGroupSearchConfigurationsCount(
		long companyId, int type) {

		return getService().getGroupSearchConfigurationsCount(companyId, type);
	}

	public static int getGroupSearchConfigurationsCount(
		long companyId, int status, int type) {

		return getService().getGroupSearchConfigurationsCount(
			companyId, status, type);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static com.liferay.portal.search.tuning.gsearch.configuration.model.
		SearchConfiguration getSearchConfiguration(long searchConfigurationId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getSearchConfiguration(searchConfigurationId);
	}

	public static com.liferay.portal.search.tuning.gsearch.configuration.model.
		SearchConfiguration updateSearchConfiguration(
				long searchConfigurationId,
				java.util.Map<java.util.Locale, String> titleMap,
				java.util.Map<java.util.Locale, String> descriptionMap,
				String configuration,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateSearchConfiguration(
			searchConfigurationId, titleMap, descriptionMap, configuration,
			serviceContext);
	}

	public static SearchConfigurationService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<SearchConfigurationService, SearchConfigurationService>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			SearchConfigurationService.class);

		ServiceTracker<SearchConfigurationService, SearchConfigurationService>
			serviceTracker =
				new ServiceTracker
					<SearchConfigurationService, SearchConfigurationService>(
						bundle.getBundleContext(),
						SearchConfigurationService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}