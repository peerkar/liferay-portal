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
 * Provides the local service utility for SearchConfiguration. This utility wraps
 * <code>com.liferay.portal.search.tuning.gsearch.configuration.service.impl.SearchConfigurationLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see SearchConfigurationLocalService
 * @generated
 */
public class SearchConfigurationLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.portal.search.tuning.gsearch.configuration.service.impl.SearchConfigurationLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.portal.search.tuning.gsearch.configuration.model.
		SearchConfiguration addSearchConfiguration(
				long userId, long groupId,
				java.util.Map<java.util.Locale, String> titleMap,
				java.util.Map<java.util.Locale, String> descriptionMap,
				String configuration, int type,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addSearchConfiguration(
			userId, groupId, titleMap, descriptionMap, configuration, type,
			serviceContext);
	}

	/**
	 * Adds the search configuration to the database. Also notifies the appropriate model listeners.
	 *
	 * @param searchConfiguration the search configuration
	 * @return the search configuration that was added
	 */
	public static com.liferay.portal.search.tuning.gsearch.configuration.model.
		SearchConfiguration addSearchConfiguration(
			com.liferay.portal.search.tuning.gsearch.configuration.model.
				SearchConfiguration searchConfiguration) {

		return getService().addSearchConfiguration(searchConfiguration);
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			createPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().createPersistedModel(primaryKeyObj);
	}

	/**
	 * Creates a new search configuration with the primary key. Does not add the search configuration to the database.
	 *
	 * @param searchConfigurationId the primary key for the new search configuration
	 * @return the new search configuration
	 */
	public static com.liferay.portal.search.tuning.gsearch.configuration.model.
		SearchConfiguration createSearchConfiguration(
			long searchConfigurationId) {

		return getService().createSearchConfiguration(searchConfigurationId);
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			deletePersistedModel(
				com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deletePersistedModel(persistedModel);
	}

	/**
	 * Deletes the search configuration with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param searchConfigurationId the primary key of the search configuration
	 * @return the search configuration that was removed
	 * @throws PortalException if a search configuration with the primary key could not be found
	 */
	public static com.liferay.portal.search.tuning.gsearch.configuration.model.
		SearchConfiguration deleteSearchConfiguration(
				long searchConfigurationId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteSearchConfiguration(searchConfigurationId);
	}

	/**
	 * Deletes the search configuration from the database. Also notifies the appropriate model listeners.
	 *
	 * @param searchConfiguration the search configuration
	 * @return the search configuration that was removed
	 * @throws PortalException
	 */
	public static com.liferay.portal.search.tuning.gsearch.configuration.model.
		SearchConfiguration deleteSearchConfiguration(
				com.liferay.portal.search.tuning.gsearch.configuration.model.
					SearchConfiguration searchConfiguration)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteSearchConfiguration(searchConfiguration);
	}

	public static <T> T dslQuery(
		com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {

		return getService().dslQuery(dslQuery);
	}

	public static com.liferay.portal.kernel.dao.orm.DynamicQuery
		dynamicQuery() {

		return getService().dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.search.tuning.gsearch.configuration.model.impl.SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.search.tuning.gsearch.configuration.model.impl.SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return getService().dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static com.liferay.portal.search.tuning.gsearch.configuration.model.
		SearchConfiguration fetchSearchConfiguration(
			long searchConfigurationId) {

		return getService().fetchSearchConfiguration(searchConfigurationId);
	}

	/**
	 * Returns the search configuration matching the UUID and group.
	 *
	 * @param uuid the search configuration's UUID
	 * @param groupId the primary key of the group
	 * @return the matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public static com.liferay.portal.search.tuning.gsearch.configuration.model.
		SearchConfiguration fetchSearchConfigurationByUuidAndGroupId(
			String uuid, long groupId) {

		return getService().fetchSearchConfigurationByUuidAndGroupId(
			uuid, groupId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	public static com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery
		getExportActionableDynamicQuery(
			com.liferay.exportimport.kernel.lar.PortletDataContext
				portletDataContext) {

		return getService().getExportActionableDynamicQuery(portletDataContext);
	}

	public static java.util.List
		<com.liferay.portal.search.tuning.gsearch.configuration.model.
			SearchConfiguration> getGroupSearchConfigurations(
				long groupId, int type, int start, int end) {

		return getService().getGroupSearchConfigurations(
			groupId, type, start, end);
	}

	public static java.util.List
		<com.liferay.portal.search.tuning.gsearch.configuration.model.
			SearchConfiguration> getGroupSearchConfigurations(
				long groupId, int status, int type, int start, int end) {

		return getService().getGroupSearchConfigurations(
			groupId, status, type, start, end);
	}

	public static java.util.List
		<com.liferay.portal.search.tuning.gsearch.configuration.model.
			SearchConfiguration> getGroupSearchConfigurations(
				long groupId, int status, int type, int start, int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.portal.search.tuning.gsearch.configuration.
						model.SearchConfiguration> orderByComparator) {

		return getService().getGroupSearchConfigurations(
			groupId, status, type, start, end, orderByComparator);
	}

	public static java.util.List
		<com.liferay.portal.search.tuning.gsearch.configuration.model.
			SearchConfiguration> getGroupSearchConfigurations(
				long groupId, int type, int start, int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.portal.search.tuning.gsearch.configuration.
						model.SearchConfiguration> orderByComparator) {

		return getService().getGroupSearchConfigurations(
			groupId, type, start, end, orderByComparator);
	}

	public static int getGroupSearchConfigurationsCount(
		long groupId, int type) {

		return getService().getGroupSearchConfigurationsCount(groupId, type);
	}

	public static int getGroupSearchConfigurationsCount(
		long groupId, int status, int type) {

		return getService().getGroupSearchConfigurationsCount(
			groupId, status, type);
	}

	public static
		com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
			getIndexableActionableDynamicQuery() {

		return getService().getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			getPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	/**
	 * Returns the search configuration with the primary key.
	 *
	 * @param searchConfigurationId the primary key of the search configuration
	 * @return the search configuration
	 * @throws PortalException if a search configuration with the primary key could not be found
	 */
	public static com.liferay.portal.search.tuning.gsearch.configuration.model.
		SearchConfiguration getSearchConfiguration(long searchConfigurationId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getSearchConfiguration(searchConfigurationId);
	}

	/**
	 * Returns the search configuration matching the UUID and group.
	 *
	 * @param uuid the search configuration's UUID
	 * @param groupId the primary key of the group
	 * @return the matching search configuration
	 * @throws PortalException if a matching search configuration could not be found
	 */
	public static com.liferay.portal.search.tuning.gsearch.configuration.model.
		SearchConfiguration getSearchConfigurationByUuidAndGroupId(
				String uuid, long groupId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getSearchConfigurationByUuidAndGroupId(
			uuid, groupId);
	}

	/**
	 * Returns a range of all the search configurations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.search.tuning.gsearch.configuration.model.impl.SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of search configurations
	 */
	public static java.util.List
		<com.liferay.portal.search.tuning.gsearch.configuration.model.
			SearchConfiguration> getSearchConfigurations(int start, int end) {

		return getService().getSearchConfigurations(start, end);
	}

	/**
	 * Returns all the search configurations matching the UUID and company.
	 *
	 * @param uuid the UUID of the search configurations
	 * @param companyId the primary key of the company
	 * @return the matching search configurations, or an empty list if no matches were found
	 */
	public static java.util.List
		<com.liferay.portal.search.tuning.gsearch.configuration.model.
			SearchConfiguration> getSearchConfigurationsByUuidAndCompanyId(
				String uuid, long companyId) {

		return getService().getSearchConfigurationsByUuidAndCompanyId(
			uuid, companyId);
	}

	/**
	 * Returns a range of search configurations matching the UUID and company.
	 *
	 * @param uuid the UUID of the search configurations
	 * @param companyId the primary key of the company
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the range of matching search configurations, or an empty list if no matches were found
	 */
	public static java.util.List
		<com.liferay.portal.search.tuning.gsearch.configuration.model.
			SearchConfiguration> getSearchConfigurationsByUuidAndCompanyId(
				String uuid, long companyId, int start, int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.portal.search.tuning.gsearch.configuration.
						model.SearchConfiguration> orderByComparator) {

		return getService().getSearchConfigurationsByUuidAndCompanyId(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the number of search configurations.
	 *
	 * @return the number of search configurations
	 */
	public static int getSearchConfigurationsCount() {
		return getService().getSearchConfigurationsCount();
	}

	public static com.liferay.portal.search.tuning.gsearch.configuration.model.
		SearchConfiguration updateSearchConfiguration(
				long userId, long searchConfigurationId,
				java.util.Map<java.util.Locale, String> titleMap,
				java.util.Map<java.util.Locale, String> descriptionMap,
				String configuration,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateSearchConfiguration(
			userId, searchConfigurationId, titleMap, descriptionMap,
			configuration, serviceContext);
	}

	/**
	 * Updates the search configuration in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param searchConfiguration the search configuration
	 * @return the search configuration that was updated
	 */
	public static com.liferay.portal.search.tuning.gsearch.configuration.model.
		SearchConfiguration updateSearchConfiguration(
			com.liferay.portal.search.tuning.gsearch.configuration.model.
				SearchConfiguration searchConfiguration) {

		return getService().updateSearchConfiguration(searchConfiguration);
	}

	public static com.liferay.portal.search.tuning.gsearch.configuration.model.
		SearchConfiguration updateStatus(
				long userId, long searchConfigurationId, int status)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateStatus(userId, searchConfigurationId, status);
	}

	public static SearchConfigurationLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<SearchConfigurationLocalService, SearchConfigurationLocalService>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			SearchConfigurationLocalService.class);

		ServiceTracker
			<SearchConfigurationLocalService, SearchConfigurationLocalService>
				serviceTracker =
					new ServiceTracker
						<SearchConfigurationLocalService,
						 SearchConfigurationLocalService>(
							 bundle.getBundleContext(),
							 SearchConfigurationLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}