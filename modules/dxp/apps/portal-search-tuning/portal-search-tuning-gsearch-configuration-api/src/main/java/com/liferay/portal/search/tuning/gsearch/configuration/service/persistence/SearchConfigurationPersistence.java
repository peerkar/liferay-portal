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

import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.search.tuning.gsearch.configuration.exception.NoSuchConfigurationException;
import com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the search configuration service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SearchConfigurationUtil
 * @generated
 */
@ProviderType
public interface SearchConfigurationPersistence
	extends BasePersistence<SearchConfiguration> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link SearchConfigurationUtil} to access the search configuration persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns all the search configurations where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching search configurations
	 */
	public java.util.List<SearchConfiguration> findByUuid(String uuid);

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
	public java.util.List<SearchConfiguration> findByUuid(
		String uuid, int start, int end);

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
	public java.util.List<SearchConfiguration> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

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
	public java.util.List<SearchConfiguration> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first search configuration in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public SearchConfiguration findByUuid_First(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Returns the first search configuration in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public SearchConfiguration fetchByUuid_First(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

	/**
	 * Returns the last search configuration in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public SearchConfiguration findByUuid_Last(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Returns the last search configuration in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public SearchConfiguration fetchByUuid_Last(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set where uuid = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	public SearchConfiguration[] findByUuid_PrevAndNext(
			long searchConfigurationId, String uuid,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Removes all the search configurations where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public void removeByUuid(String uuid);

	/**
	 * Returns the number of search configurations where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching search configurations
	 */
	public int countByUuid(String uuid);

	/**
	 * Returns the search configuration where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchConfigurationException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public SearchConfiguration findByUUID_G(String uuid, long groupId)
		throws NoSuchConfigurationException;

	/**
	 * Returns the search configuration where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public SearchConfiguration fetchByUUID_G(String uuid, long groupId);

	/**
	 * Returns the search configuration where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public SearchConfiguration fetchByUUID_G(
		String uuid, long groupId, boolean useFinderCache);

	/**
	 * Removes the search configuration where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the search configuration that was removed
	 */
	public SearchConfiguration removeByUUID_G(String uuid, long groupId)
		throws NoSuchConfigurationException;

	/**
	 * Returns the number of search configurations where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching search configurations
	 */
	public int countByUUID_G(String uuid, long groupId);

	/**
	 * Returns all the search configurations where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching search configurations
	 */
	public java.util.List<SearchConfiguration> findByUuid_C(
		String uuid, long companyId);

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
	public java.util.List<SearchConfiguration> findByUuid_C(
		String uuid, long companyId, int start, int end);

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
	public java.util.List<SearchConfiguration> findByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

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
	public java.util.List<SearchConfiguration> findByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first search configuration in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public SearchConfiguration findByUuid_C_First(
			String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Returns the first search configuration in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public SearchConfiguration fetchByUuid_C_First(
		String uuid, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

	/**
	 * Returns the last search configuration in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public SearchConfiguration findByUuid_C_Last(
			String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Returns the last search configuration in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public SearchConfiguration fetchByUuid_C_Last(
		String uuid, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

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
	public SearchConfiguration[] findByUuid_C_PrevAndNext(
			long searchConfigurationId, String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Removes all the search configurations where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public void removeByUuid_C(String uuid, long companyId);

	/**
	 * Returns the number of search configurations where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching search configurations
	 */
	public int countByUuid_C(String uuid, long companyId);

	/**
	 * Returns all the search configurations where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the matching search configurations
	 */
	public java.util.List<SearchConfiguration> findByGroupId(long groupId);

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
	public java.util.List<SearchConfiguration> findByGroupId(
		long groupId, int start, int end);

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
	public java.util.List<SearchConfiguration> findByGroupId(
		long groupId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

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
	public java.util.List<SearchConfiguration> findByGroupId(
		long groupId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public SearchConfiguration findByGroupId_First(
			long groupId,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public SearchConfiguration fetchByGroupId_First(
		long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public SearchConfiguration findByGroupId_Last(
			long groupId,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public SearchConfiguration fetchByGroupId_Last(
		long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set where groupId = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	public SearchConfiguration[] findByGroupId_PrevAndNext(
			long searchConfigurationId, long groupId,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Returns all the search configurations that the user has permission to view where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the matching search configurations that the user has permission to view
	 */
	public java.util.List<SearchConfiguration> filterFindByGroupId(
		long groupId);

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
	public java.util.List<SearchConfiguration> filterFindByGroupId(
		long groupId, int start, int end);

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
	public java.util.List<SearchConfiguration> filterFindByGroupId(
		long groupId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set of search configurations that the user has permission to view where groupId = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	public SearchConfiguration[] filterFindByGroupId_PrevAndNext(
			long searchConfigurationId, long groupId,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Removes all the search configurations where groupId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 */
	public void removeByGroupId(long groupId);

	/**
	 * Returns the number of search configurations where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the number of matching search configurations
	 */
	public int countByGroupId(long groupId);

	/**
	 * Returns the number of search configurations that the user has permission to view where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the number of matching search configurations that the user has permission to view
	 */
	public int filterCountByGroupId(long groupId);

	/**
	 * Returns all the search configurations where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching search configurations
	 */
	public java.util.List<SearchConfiguration> findByCompanyId(long companyId);

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
	public java.util.List<SearchConfiguration> findByCompanyId(
		long companyId, int start, int end);

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
	public java.util.List<SearchConfiguration> findByCompanyId(
		long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

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
	public java.util.List<SearchConfiguration> findByCompanyId(
		long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first search configuration in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public SearchConfiguration findByCompanyId_First(
			long companyId,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Returns the first search configuration in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public SearchConfiguration fetchByCompanyId_First(
		long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

	/**
	 * Returns the last search configuration in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public SearchConfiguration findByCompanyId_Last(
			long companyId,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Returns the last search configuration in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public SearchConfiguration fetchByCompanyId_Last(
		long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set where companyId = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	public SearchConfiguration[] findByCompanyId_PrevAndNext(
			long searchConfigurationId, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Removes all the search configurations where companyId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 */
	public void removeByCompanyId(long companyId);

	/**
	 * Returns the number of search configurations where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching search configurations
	 */
	public int countByCompanyId(long companyId);

	/**
	 * Returns all the search configurations where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @return the matching search configurations
	 */
	public java.util.List<SearchConfiguration> findByG_S(
		long groupId, int status);

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
	public java.util.List<SearchConfiguration> findByG_S(
		long groupId, int status, int start, int end);

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
	public java.util.List<SearchConfiguration> findByG_S(
		long groupId, int status, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

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
	public java.util.List<SearchConfiguration> findByG_S(
		long groupId, int status, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public SearchConfiguration findByG_S_First(
			long groupId, int status,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public SearchConfiguration fetchByG_S_First(
		long groupId, int status,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public SearchConfiguration findByG_S_Last(
			long groupId, int status,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public SearchConfiguration fetchByG_S_Last(
		long groupId, int status,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

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
	public SearchConfiguration[] findByG_S_PrevAndNext(
			long searchConfigurationId, long groupId, int status,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Returns all the search configurations that the user has permission to view where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @return the matching search configurations that the user has permission to view
	 */
	public java.util.List<SearchConfiguration> filterFindByG_S(
		long groupId, int status);

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
	public java.util.List<SearchConfiguration> filterFindByG_S(
		long groupId, int status, int start, int end);

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
	public java.util.List<SearchConfiguration> filterFindByG_S(
		long groupId, int status, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

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
	public SearchConfiguration[] filterFindByG_S_PrevAndNext(
			long searchConfigurationId, long groupId, int status,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Removes all the search configurations where groupId = &#63; and status = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 */
	public void removeByG_S(long groupId, int status);

	/**
	 * Returns the number of search configurations where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @return the number of matching search configurations
	 */
	public int countByG_S(long groupId, int status);

	/**
	 * Returns the number of search configurations that the user has permission to view where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @return the number of matching search configurations that the user has permission to view
	 */
	public int filterCountByG_S(long groupId, int status);

	/**
	 * Returns all the search configurations where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @return the matching search configurations
	 */
	public java.util.List<SearchConfiguration> findByG_T(
		long groupId, int type);

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
	public java.util.List<SearchConfiguration> findByG_T(
		long groupId, int type, int start, int end);

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
	public java.util.List<SearchConfiguration> findByG_T(
		long groupId, int type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

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
	public java.util.List<SearchConfiguration> findByG_T(
		long groupId, int type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public SearchConfiguration findByG_T_First(
			long groupId, int type,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public SearchConfiguration fetchByG_T_First(
		long groupId, int type,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	public SearchConfiguration findByG_T_Last(
			long groupId, int type,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public SearchConfiguration fetchByG_T_Last(
		long groupId, int type,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

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
	public SearchConfiguration[] findByG_T_PrevAndNext(
			long searchConfigurationId, long groupId, int type,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Returns all the search configurations that the user has permission to view where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @return the matching search configurations that the user has permission to view
	 */
	public java.util.List<SearchConfiguration> filterFindByG_T(
		long groupId, int type);

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
	public java.util.List<SearchConfiguration> filterFindByG_T(
		long groupId, int type, int start, int end);

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
	public java.util.List<SearchConfiguration> filterFindByG_T(
		long groupId, int type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

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
	public SearchConfiguration[] filterFindByG_T_PrevAndNext(
			long searchConfigurationId, long groupId, int type,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Removes all the search configurations where groupId = &#63; and type = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 */
	public void removeByG_T(long groupId, int type);

	/**
	 * Returns the number of search configurations where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @return the number of matching search configurations
	 */
	public int countByG_T(long groupId, int type);

	/**
	 * Returns the number of search configurations that the user has permission to view where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @return the number of matching search configurations that the user has permission to view
	 */
	public int filterCountByG_T(long groupId, int type);

	/**
	 * Returns all the search configurations where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @return the matching search configurations
	 */
	public java.util.List<SearchConfiguration> findByG_S_T(
		long groupId, int status, int type);

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
	public java.util.List<SearchConfiguration> findByG_S_T(
		long groupId, int status, int type, int start, int end);

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
	public java.util.List<SearchConfiguration> findByG_S_T(
		long groupId, int status, int type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

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
	public java.util.List<SearchConfiguration> findByG_S_T(
		long groupId, int status, int type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator,
		boolean useFinderCache);

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
	public SearchConfiguration findByG_S_T_First(
			long groupId, int status, int type,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public SearchConfiguration fetchByG_S_T_First(
		long groupId, int status, int type,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

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
	public SearchConfiguration findByG_S_T_Last(
			long groupId, int status, int type,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	public SearchConfiguration fetchByG_S_T_Last(
		long groupId, int status, int type,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

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
	public SearchConfiguration[] findByG_S_T_PrevAndNext(
			long searchConfigurationId, long groupId, int status, int type,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Returns all the search configurations that the user has permission to view where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @return the matching search configurations that the user has permission to view
	 */
	public java.util.List<SearchConfiguration> filterFindByG_S_T(
		long groupId, int status, int type);

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
	public java.util.List<SearchConfiguration> filterFindByG_S_T(
		long groupId, int status, int type, int start, int end);

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
	public java.util.List<SearchConfiguration> filterFindByG_S_T(
		long groupId, int status, int type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

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
	public SearchConfiguration[] filterFindByG_S_T_PrevAndNext(
			long searchConfigurationId, long groupId, int status, int type,
			com.liferay.portal.kernel.util.OrderByComparator
				<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException;

	/**
	 * Removes all the search configurations where groupId = &#63; and status = &#63; and type = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 */
	public void removeByG_S_T(long groupId, int status, int type);

	/**
	 * Returns the number of search configurations where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @return the number of matching search configurations
	 */
	public int countByG_S_T(long groupId, int status, int type);

	/**
	 * Returns the number of search configurations that the user has permission to view where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @return the number of matching search configurations that the user has permission to view
	 */
	public int filterCountByG_S_T(long groupId, int status, int type);

	/**
	 * Caches the search configuration in the entity cache if it is enabled.
	 *
	 * @param searchConfiguration the search configuration
	 */
	public void cacheResult(SearchConfiguration searchConfiguration);

	/**
	 * Caches the search configurations in the entity cache if it is enabled.
	 *
	 * @param searchConfigurations the search configurations
	 */
	public void cacheResult(
		java.util.List<SearchConfiguration> searchConfigurations);

	/**
	 * Creates a new search configuration with the primary key. Does not add the search configuration to the database.
	 *
	 * @param searchConfigurationId the primary key for the new search configuration
	 * @return the new search configuration
	 */
	public SearchConfiguration create(long searchConfigurationId);

	/**
	 * Removes the search configuration with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param searchConfigurationId the primary key of the search configuration
	 * @return the search configuration that was removed
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	public SearchConfiguration remove(long searchConfigurationId)
		throws NoSuchConfigurationException;

	public SearchConfiguration updateImpl(
		SearchConfiguration searchConfiguration);

	/**
	 * Returns the search configuration with the primary key or throws a <code>NoSuchConfigurationException</code> if it could not be found.
	 *
	 * @param searchConfigurationId the primary key of the search configuration
	 * @return the search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	public SearchConfiguration findByPrimaryKey(long searchConfigurationId)
		throws NoSuchConfigurationException;

	/**
	 * Returns the search configuration with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param searchConfigurationId the primary key of the search configuration
	 * @return the search configuration, or <code>null</code> if a search configuration with the primary key could not be found
	 */
	public SearchConfiguration fetchByPrimaryKey(long searchConfigurationId);

	/**
	 * Returns all the search configurations.
	 *
	 * @return the search configurations
	 */
	public java.util.List<SearchConfiguration> findAll();

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
	public java.util.List<SearchConfiguration> findAll(int start, int end);

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
	public java.util.List<SearchConfiguration> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator);

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
	public java.util.List<SearchConfiguration> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<SearchConfiguration>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the search configurations from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of search configurations.
	 *
	 * @return the number of search configurations
	 */
	public int countAll();

}