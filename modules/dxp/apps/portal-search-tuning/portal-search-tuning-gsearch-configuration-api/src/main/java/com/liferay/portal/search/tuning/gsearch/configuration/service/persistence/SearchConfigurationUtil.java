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

package com.liferay.portal.search.tuning.gsearch.configuration.service.persistence;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The persistence utility for the search configuration service. This utility wraps <code>com.liferay.portal.search.tuning.gsearch.configuration.service.persistence.impl.SearchConfigurationPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SearchConfigurationPersistence
 * @generated
 */
public class SearchConfigurationUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(SearchConfiguration searchConfiguration) {
		getPersistence().clearCache(searchConfiguration);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#fetchByPrimaryKeys(Set)
	 */
	public static Map<Serializable, SearchConfiguration> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<SearchConfiguration> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<SearchConfiguration> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<SearchConfiguration> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static SearchConfiguration update(
		SearchConfiguration searchConfiguration) {

		return getPersistence().update(searchConfiguration);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static SearchConfiguration update(
		SearchConfiguration searchConfiguration,
		ServiceContext serviceContext) {

		return getPersistence().update(searchConfiguration, serviceContext);
	}

	/**
	 * Returns all the search configurations where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching search configurations
	 */
	public static List<SearchConfiguration> findByUuid(String uuid) {
		return getPersistence().findByUuid(uuid);
	}

	/**
	 * Returns a range of all the search configurations where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of matching search configurations
	 */
	public static List<SearchConfiguration> findByUuid(
		String uuid, int start, int end) {

		return getPersistence().findByUuid(uuid, start, end);
	}

	/**
	 * Returns an ordered range of all the search configurations where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching search configurations
	 */
	public static List<SearchConfiguration> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().findByUuid(uuid, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the search configurations where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching search configurations
	 */
	public static List<SearchConfiguration> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByUuid(
			uuid, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first search configuration in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public static SearchConfiguration findByUuid_First(
			String uuid,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the first search configuration in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public static SearchConfiguration fetchByUuid_First(
		String uuid, OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().fetchByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the last search configuration in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public static SearchConfiguration findByUuid_Last(
			String uuid,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the last search configuration in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public static SearchConfiguration fetchByUuid_Last(
		String uuid, OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().fetchByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set where uuid = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	public static SearchConfiguration[] findByUuid_PrevAndNext(
			long searchConfigurationId, String uuid,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByUuid_PrevAndNext(
			searchConfigurationId, uuid, orderByComparator);
	}

	/**
	 * Removes all the search configurations where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public static void removeByUuid(String uuid) {
		getPersistence().removeByUuid(uuid);
	}

	/**
	 * Returns the number of search configurations where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching search configurations
	 */
	public static int countByUuid(String uuid) {
		return getPersistence().countByUuid(uuid);
	}

	/**
	 * Returns the search configuration where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchConfigurationException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public static SearchConfiguration findByUUID_G(String uuid, long groupId)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the search configuration where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public static SearchConfiguration fetchByUUID_G(String uuid, long groupId) {
		return getPersistence().fetchByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the search configuration where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public static SearchConfiguration fetchByUUID_G(
		String uuid, long groupId, boolean useFinderCache) {

		return getPersistence().fetchByUUID_G(uuid, groupId, useFinderCache);
	}

	/**
	 * Removes the search configuration where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the search configuration that was removed
	 */
	public static SearchConfiguration removeByUUID_G(String uuid, long groupId)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().removeByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the number of search configurations where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching search configurations
	 */
	public static int countByUUID_G(String uuid, long groupId) {
		return getPersistence().countByUUID_G(uuid, groupId);
	}

	/**
	 * Returns all the search configurations where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching search configurations
	 */
	public static List<SearchConfiguration> findByUuid_C(
		String uuid, long companyId) {

		return getPersistence().findByUuid_C(uuid, companyId);
	}

	/**
	 * Returns a range of all the search configurations where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of matching search configurations
	 */
	public static List<SearchConfiguration> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return getPersistence().findByUuid_C(uuid, companyId, start, end);
	}

	/**
	 * Returns an ordered range of all the search configurations where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching search configurations
	 */
	public static List<SearchConfiguration> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the search configurations where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching search configurations
	 */
	public static List<SearchConfiguration> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first search configuration in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public static SearchConfiguration findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the first search configuration in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public static SearchConfiguration fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().fetchByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the last search configuration in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public static SearchConfiguration findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByUuid_C_Last(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the last search configuration in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public static SearchConfiguration fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().fetchByUuid_C_Last(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	public static SearchConfiguration[] findByUuid_C_PrevAndNext(
			long searchConfigurationId, String uuid, long companyId,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByUuid_C_PrevAndNext(
			searchConfigurationId, uuid, companyId, orderByComparator);
	}

	/**
	 * Removes all the search configurations where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public static void removeByUuid_C(String uuid, long companyId) {
		getPersistence().removeByUuid_C(uuid, companyId);
	}

	/**
	 * Returns the number of search configurations where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching search configurations
	 */
	public static int countByUuid_C(String uuid, long companyId) {
		return getPersistence().countByUuid_C(uuid, companyId);
	}

	/**
	 * Returns all the search configurations where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the matching search configurations
	 */
	public static List<SearchConfiguration> findByGroupId(long groupId) {
		return getPersistence().findByGroupId(groupId);
	}

	/**
	 * Returns a range of all the search configurations where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of matching search configurations
	 */
	public static List<SearchConfiguration> findByGroupId(
		long groupId, int start, int end) {

		return getPersistence().findByGroupId(groupId, start, end);
	}

	/**
	 * Returns an ordered range of all the search configurations where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching search configurations
	 */
	public static List<SearchConfiguration> findByGroupId(
		long groupId, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().findByGroupId(
			groupId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the search configurations where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching search configurations
	 */
	public static List<SearchConfiguration> findByGroupId(
		long groupId, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByGroupId(
			groupId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public static SearchConfiguration findByGroupId_First(
			long groupId,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByGroupId_First(groupId, orderByComparator);
	}

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public static SearchConfiguration fetchByGroupId_First(
		long groupId,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().fetchByGroupId_First(
			groupId, orderByComparator);
	}

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public static SearchConfiguration findByGroupId_Last(
			long groupId,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByGroupId_Last(groupId, orderByComparator);
	}

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public static SearchConfiguration fetchByGroupId_Last(
		long groupId,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().fetchByGroupId_Last(groupId, orderByComparator);
	}

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set where groupId = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	public static SearchConfiguration[] findByGroupId_PrevAndNext(
			long searchConfigurationId, long groupId,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByGroupId_PrevAndNext(
			searchConfigurationId, groupId, orderByComparator);
	}

	/**
	 * Returns all the search configurations that the user has permission to view where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the matching search configurations that the user has permission to view
	 */
	public static List<SearchConfiguration> filterFindByGroupId(long groupId) {
		return getPersistence().filterFindByGroupId(groupId);
	}

	/**
	 * Returns a range of all the search configurations that the user has permission to view where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of matching search configurations that the user has permission to view
	 */
	public static List<SearchConfiguration> filterFindByGroupId(
		long groupId, int start, int end) {

		return getPersistence().filterFindByGroupId(groupId, start, end);
	}

	/**
	 * Returns an ordered range of all the search configurations that the user has permissions to view where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching search configurations that the user has permission to view
	 */
	public static List<SearchConfiguration> filterFindByGroupId(
		long groupId, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().filterFindByGroupId(
			groupId, start, end, orderByComparator);
	}

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set of search configurations that the user has permission to view where groupId = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	public static SearchConfiguration[] filterFindByGroupId_PrevAndNext(
			long searchConfigurationId, long groupId,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().filterFindByGroupId_PrevAndNext(
			searchConfigurationId, groupId, orderByComparator);
	}

	/**
	 * Removes all the search configurations where groupId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 */
	public static void removeByGroupId(long groupId) {
		getPersistence().removeByGroupId(groupId);
	}

	/**
	 * Returns the number of search configurations where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the number of matching search configurations
	 */
	public static int countByGroupId(long groupId) {
		return getPersistence().countByGroupId(groupId);
	}

	/**
	 * Returns the number of search configurations that the user has permission to view where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the number of matching search configurations that the user has permission to view
	 */
	public static int filterCountByGroupId(long groupId) {
		return getPersistence().filterCountByGroupId(groupId);
	}

	/**
	 * Returns all the search configurations where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching search configurations
	 */
	public static List<SearchConfiguration> findByCompanyId(long companyId) {
		return getPersistence().findByCompanyId(companyId);
	}

	/**
	 * Returns a range of all the search configurations where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of matching search configurations
	 */
	public static List<SearchConfiguration> findByCompanyId(
		long companyId, int start, int end) {

		return getPersistence().findByCompanyId(companyId, start, end);
	}

	/**
	 * Returns an ordered range of all the search configurations where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching search configurations
	 */
	public static List<SearchConfiguration> findByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().findByCompanyId(
			companyId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the search configurations where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching search configurations
	 */
	public static List<SearchConfiguration> findByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByCompanyId(
			companyId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first search configuration in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public static SearchConfiguration findByCompanyId_First(
			long companyId,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByCompanyId_First(
			companyId, orderByComparator);
	}

	/**
	 * Returns the first search configuration in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public static SearchConfiguration fetchByCompanyId_First(
		long companyId,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().fetchByCompanyId_First(
			companyId, orderByComparator);
	}

	/**
	 * Returns the last search configuration in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public static SearchConfiguration findByCompanyId_Last(
			long companyId,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByCompanyId_Last(
			companyId, orderByComparator);
	}

	/**
	 * Returns the last search configuration in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public static SearchConfiguration fetchByCompanyId_Last(
		long companyId,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().fetchByCompanyId_Last(
			companyId, orderByComparator);
	}

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set where companyId = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	public static SearchConfiguration[] findByCompanyId_PrevAndNext(
			long searchConfigurationId, long companyId,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByCompanyId_PrevAndNext(
			searchConfigurationId, companyId, orderByComparator);
	}

	/**
	 * Removes all the search configurations where companyId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 */
	public static void removeByCompanyId(long companyId) {
		getPersistence().removeByCompanyId(companyId);
	}

	/**
	 * Returns the number of search configurations where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching search configurations
	 */
	public static int countByCompanyId(long companyId) {
		return getPersistence().countByCompanyId(companyId);
	}

	/**
	 * Returns all the search configurations where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @return the matching search configurations
	 */
	public static List<SearchConfiguration> findByG_S(
		long groupId, int status) {

		return getPersistence().findByG_S(groupId, status);
	}

	/**
	 * Returns a range of all the search configurations where groupId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of matching search configurations
	 */
	public static List<SearchConfiguration> findByG_S(
		long groupId, int status, int start, int end) {

		return getPersistence().findByG_S(groupId, status, start, end);
	}

	/**
	 * Returns an ordered range of all the search configurations where groupId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching search configurations
	 */
	public static List<SearchConfiguration> findByG_S(
		long groupId, int status, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().findByG_S(
			groupId, status, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the search configurations where groupId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching search configurations
	 */
	public static List<SearchConfiguration> findByG_S(
		long groupId, int status, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByG_S(
			groupId, status, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public static SearchConfiguration findByG_S_First(
			long groupId, int status,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByG_S_First(
			groupId, status, orderByComparator);
	}

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public static SearchConfiguration fetchByG_S_First(
		long groupId, int status,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().fetchByG_S_First(
			groupId, status, orderByComparator);
	}

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public static SearchConfiguration findByG_S_Last(
			long groupId, int status,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByG_S_Last(
			groupId, status, orderByComparator);
	}

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public static SearchConfiguration fetchByG_S_Last(
		long groupId, int status,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().fetchByG_S_Last(
			groupId, status, orderByComparator);
	}

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set where groupId = &#63; and status = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param groupId the group ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	public static SearchConfiguration[] findByG_S_PrevAndNext(
			long searchConfigurationId, long groupId, int status,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByG_S_PrevAndNext(
			searchConfigurationId, groupId, status, orderByComparator);
	}

	/**
	 * Returns all the search configurations that the user has permission to view where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @return the matching search configurations that the user has permission to view
	 */
	public static List<SearchConfiguration> filterFindByG_S(
		long groupId, int status) {

		return getPersistence().filterFindByG_S(groupId, status);
	}

	/**
	 * Returns a range of all the search configurations that the user has permission to view where groupId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of matching search configurations that the user has permission to view
	 */
	public static List<SearchConfiguration> filterFindByG_S(
		long groupId, int status, int start, int end) {

		return getPersistence().filterFindByG_S(groupId, status, start, end);
	}

	/**
	 * Returns an ordered range of all the search configurations that the user has permissions to view where groupId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching search configurations that the user has permission to view
	 */
	public static List<SearchConfiguration> filterFindByG_S(
		long groupId, int status, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().filterFindByG_S(
			groupId, status, start, end, orderByComparator);
	}

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set of search configurations that the user has permission to view where groupId = &#63; and status = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param groupId the group ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	public static SearchConfiguration[] filterFindByG_S_PrevAndNext(
			long searchConfigurationId, long groupId, int status,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().filterFindByG_S_PrevAndNext(
			searchConfigurationId, groupId, status, orderByComparator);
	}

	/**
	 * Removes all the search configurations where groupId = &#63; and status = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 */
	public static void removeByG_S(long groupId, int status) {
		getPersistence().removeByG_S(groupId, status);
	}

	/**
	 * Returns the number of search configurations where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @return the number of matching search configurations
	 */
	public static int countByG_S(long groupId, int status) {
		return getPersistence().countByG_S(groupId, status);
	}

	/**
	 * Returns the number of search configurations that the user has permission to view where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @return the number of matching search configurations that the user has permission to view
	 */
	public static int filterCountByG_S(long groupId, int status) {
		return getPersistence().filterCountByG_S(groupId, status);
	}

	/**
	 * Returns all the search configurations where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @return the matching search configurations
	 */
	public static List<SearchConfiguration> findByG_T(long groupId, int type) {
		return getPersistence().findByG_T(groupId, type);
	}

	/**
	 * Returns a range of all the search configurations where groupId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of matching search configurations
	 */
	public static List<SearchConfiguration> findByG_T(
		long groupId, int type, int start, int end) {

		return getPersistence().findByG_T(groupId, type, start, end);
	}

	/**
	 * Returns an ordered range of all the search configurations where groupId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching search configurations
	 */
	public static List<SearchConfiguration> findByG_T(
		long groupId, int type, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().findByG_T(
			groupId, type, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the search configurations where groupId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching search configurations
	 */
	public static List<SearchConfiguration> findByG_T(
		long groupId, int type, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByG_T(
			groupId, type, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public static SearchConfiguration findByG_T_First(
			long groupId, int type,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByG_T_First(
			groupId, type, orderByComparator);
	}

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public static SearchConfiguration fetchByG_T_First(
		long groupId, int type,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().fetchByG_T_First(
			groupId, type, orderByComparator);
	}

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public static SearchConfiguration findByG_T_Last(
			long groupId, int type,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByG_T_Last(
			groupId, type, orderByComparator);
	}

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public static SearchConfiguration fetchByG_T_Last(
		long groupId, int type,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().fetchByG_T_Last(
			groupId, type, orderByComparator);
	}

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set where groupId = &#63; and type = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param groupId the group ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	public static SearchConfiguration[] findByG_T_PrevAndNext(
			long searchConfigurationId, long groupId, int type,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByG_T_PrevAndNext(
			searchConfigurationId, groupId, type, orderByComparator);
	}

	/**
	 * Returns all the search configurations that the user has permission to view where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @return the matching search configurations that the user has permission to view
	 */
	public static List<SearchConfiguration> filterFindByG_T(
		long groupId, int type) {

		return getPersistence().filterFindByG_T(groupId, type);
	}

	/**
	 * Returns a range of all the search configurations that the user has permission to view where groupId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of matching search configurations that the user has permission to view
	 */
	public static List<SearchConfiguration> filterFindByG_T(
		long groupId, int type, int start, int end) {

		return getPersistence().filterFindByG_T(groupId, type, start, end);
	}

	/**
	 * Returns an ordered range of all the search configurations that the user has permissions to view where groupId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching search configurations that the user has permission to view
	 */
	public static List<SearchConfiguration> filterFindByG_T(
		long groupId, int type, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().filterFindByG_T(
			groupId, type, start, end, orderByComparator);
	}

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set of search configurations that the user has permission to view where groupId = &#63; and type = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param groupId the group ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	public static SearchConfiguration[] filterFindByG_T_PrevAndNext(
			long searchConfigurationId, long groupId, int type,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().filterFindByG_T_PrevAndNext(
			searchConfigurationId, groupId, type, orderByComparator);
	}

	/**
	 * Removes all the search configurations where groupId = &#63; and type = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 */
	public static void removeByG_T(long groupId, int type) {
		getPersistence().removeByG_T(groupId, type);
	}

	/**
	 * Returns the number of search configurations where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @return the number of matching search configurations
	 */
	public static int countByG_T(long groupId, int type) {
		return getPersistence().countByG_T(groupId, type);
	}

	/**
	 * Returns the number of search configurations that the user has permission to view where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @return the number of matching search configurations that the user has permission to view
	 */
	public static int filterCountByG_T(long groupId, int type) {
		return getPersistence().filterCountByG_T(groupId, type);
	}

	/**
	 * Returns all the search configurations where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @return the matching search configurations
	 */
	public static List<SearchConfiguration> findByG_S_T(
		long groupId, int status, int type) {

		return getPersistence().findByG_S_T(groupId, status, type);
	}

	/**
	 * Returns a range of all the search configurations where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of matching search configurations
	 */
	public static List<SearchConfiguration> findByG_S_T(
		long groupId, int status, int type, int start, int end) {

		return getPersistence().findByG_S_T(groupId, status, type, start, end);
	}

	/**
	 * Returns an ordered range of all the search configurations where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching search configurations
	 */
	public static List<SearchConfiguration> findByG_S_T(
		long groupId, int status, int type, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().findByG_S_T(
			groupId, status, type, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the search configurations where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching search configurations
	 */
	public static List<SearchConfiguration> findByG_S_T(
		long groupId, int status, int type, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByG_S_T(
			groupId, status, type, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public static SearchConfiguration findByG_S_T_First(
			long groupId, int status, int type,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByG_S_T_First(
			groupId, status, type, orderByComparator);
	}

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public static SearchConfiguration fetchByG_S_T_First(
		long groupId, int status, int type,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().fetchByG_S_T_First(
			groupId, status, type, orderByComparator);
	}

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public static SearchConfiguration findByG_S_T_Last(
			long groupId, int status, int type,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByG_S_T_Last(
			groupId, status, type, orderByComparator);
	}

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public static SearchConfiguration fetchByG_S_T_Last(
		long groupId, int status, int type,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().fetchByG_S_T_Last(
			groupId, status, type, orderByComparator);
	}

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	public static SearchConfiguration[] findByG_S_T_PrevAndNext(
			long searchConfigurationId, long groupId, int status, int type,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByG_S_T_PrevAndNext(
			searchConfigurationId, groupId, status, type, orderByComparator);
	}

	/**
	 * Returns all the search configurations that the user has permission to view where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @return the matching search configurations that the user has permission to view
	 */
	public static List<SearchConfiguration> filterFindByG_S_T(
		long groupId, int status, int type) {

		return getPersistence().filterFindByG_S_T(groupId, status, type);
	}

	/**
	 * Returns a range of all the search configurations that the user has permission to view where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of matching search configurations that the user has permission to view
	 */
	public static List<SearchConfiguration> filterFindByG_S_T(
		long groupId, int status, int type, int start, int end) {

		return getPersistence().filterFindByG_S_T(
			groupId, status, type, start, end);
	}

	/**
	 * Returns an ordered range of all the search configurations that the user has permissions to view where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching search configurations that the user has permission to view
	 */
	public static List<SearchConfiguration> filterFindByG_S_T(
		long groupId, int status, int type, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().filterFindByG_S_T(
			groupId, status, type, start, end, orderByComparator);
	}

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set of search configurations that the user has permission to view where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	public static SearchConfiguration[] filterFindByG_S_T_PrevAndNext(
			long searchConfigurationId, long groupId, int status, int type,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().filterFindByG_S_T_PrevAndNext(
			searchConfigurationId, groupId, status, type, orderByComparator);
	}

	/**
	 * Removes all the search configurations where groupId = &#63; and status = &#63; and type = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 */
	public static void removeByG_S_T(long groupId, int status, int type) {
		getPersistence().removeByG_S_T(groupId, status, type);
	}

	/**
	 * Returns the number of search configurations where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @return the number of matching search configurations
	 */
	public static int countByG_S_T(long groupId, int status, int type) {
		return getPersistence().countByG_S_T(groupId, status, type);
	}

	/**
	 * Returns the number of search configurations that the user has permission to view where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @return the number of matching search configurations that the user has permission to view
	 */
	public static int filterCountByG_S_T(long groupId, int status, int type) {
		return getPersistence().filterCountByG_S_T(groupId, status, type);
	}

	/**
	 * Caches the search configuration in the entity cache if it is enabled.
	 *
	 * @param searchConfiguration the search configuration
	 */
	public static void cacheResult(SearchConfiguration searchConfiguration) {
		getPersistence().cacheResult(searchConfiguration);
	}

	/**
	 * Caches the search configurations in the entity cache if it is enabled.
	 *
	 * @param searchConfigurations the search configurations
	 */
	public static void cacheResult(
		List<SearchConfiguration> searchConfigurations) {

		getPersistence().cacheResult(searchConfigurations);
	}

	/**
	 * Creates a new search configuration with the primary key. Does not add the search configuration to the database.
	 *
	 * @param searchConfigurationId the primary key for the new search configuration
	 * @return the new search configuration
	 */
	public static SearchConfiguration create(long searchConfigurationId) {
		return getPersistence().create(searchConfigurationId);
	}

	/**
	 * Removes the search configuration with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param searchConfigurationId the primary key of the search configuration
	 * @return the search configuration that was removed
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	public static SearchConfiguration remove(long searchConfigurationId)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().remove(searchConfigurationId);
	}

	public static SearchConfiguration updateImpl(
		SearchConfiguration searchConfiguration) {

		return getPersistence().updateImpl(searchConfiguration);
	}

	/**
	 * Returns the search configuration with the primary key or throws a <code>NoSuchConfigurationException</code> if it could not be found.
	 *
	 * @param searchConfigurationId the primary key of the search configuration
	 * @return the search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	public static SearchConfiguration findByPrimaryKey(
			long searchConfigurationId)
		throws com.liferay.portal.search.tuning.gsearch.configuration.exception.
			NoSuchConfigurationException {

		return getPersistence().findByPrimaryKey(searchConfigurationId);
	}

	/**
	 * Returns the search configuration with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param searchConfigurationId the primary key of the search configuration
	 * @return the search configuration, or <code>null</code> if a search configuration with the primary key could not be found
	 */
	public static SearchConfiguration fetchByPrimaryKey(
		long searchConfigurationId) {

		return getPersistence().fetchByPrimaryKey(searchConfigurationId);
	}

	/**
	 * Returns all the search configurations.
	 *
	 * @return the search configurations
	 */
	public static List<SearchConfiguration> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the search configurations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of search configurations
	 */
	public static List<SearchConfiguration> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the search configurations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of search configurations
	 */
	public static List<SearchConfiguration> findAll(
		int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the search configurations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of search configurations
	 */
	public static List<SearchConfiguration> findAll(
		int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the search configurations from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of search configurations.
	 *
	 * @return the number of search configurations
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static SearchConfigurationPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<SearchConfigurationPersistence, SearchConfigurationPersistence>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			SearchConfigurationPersistence.class);

		ServiceTracker
			<SearchConfigurationPersistence, SearchConfigurationPersistence>
				serviceTracker =
					new ServiceTracker
						<SearchConfigurationPersistence,
						 SearchConfigurationPersistence>(
							 bundle.getBundleContext(),
							 SearchConfigurationPersistence.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}