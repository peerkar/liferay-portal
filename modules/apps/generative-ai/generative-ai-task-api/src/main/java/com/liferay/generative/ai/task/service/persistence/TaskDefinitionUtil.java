/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.service.persistence;

import com.liferay.generative.ai.task.model.TaskDefinition;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence utility for the task definition service. This utility wraps <code>com.liferay.generative.ai.task.service.persistence.impl.TaskDefinitionPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see TaskDefinitionPersistence
 * @generated
 */
public class TaskDefinitionUtil {

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
	public static void clearCache(TaskDefinition taskDefinition) {
		getPersistence().clearCache(taskDefinition);
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
	public static Map<Serializable, TaskDefinition> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<TaskDefinition> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<TaskDefinition> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<TaskDefinition> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<TaskDefinition> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static TaskDefinition update(TaskDefinition taskDefinition) {
		return getPersistence().update(taskDefinition);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static TaskDefinition update(
		TaskDefinition taskDefinition, ServiceContext serviceContext) {

		return getPersistence().update(taskDefinition, serviceContext);
	}

	/**
	 * Returns all the task definitions where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching task definitions
	 */
	public static List<TaskDefinition> findByUuid(String uuid) {
		return getPersistence().findByUuid(uuid);
	}

	/**
	 * Returns a range of all the task definitions where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @return the range of matching task definitions
	 */
	public static List<TaskDefinition> findByUuid(
		String uuid, int start, int end) {

		return getPersistence().findByUuid(uuid, start, end);
	}

	/**
	 * Returns an ordered range of all the task definitions where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching task definitions
	 */
	public static List<TaskDefinition> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<TaskDefinition> orderByComparator) {

		return getPersistence().findByUuid(uuid, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the task definitions where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching task definitions
	 */
	public static List<TaskDefinition> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<TaskDefinition> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByUuid(
			uuid, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first task definition in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching task definition
	 * @throws NoSuchTaskDefinitionException if a matching task definition could not be found
	 */
	public static TaskDefinition findByUuid_First(
			String uuid, OrderByComparator<TaskDefinition> orderByComparator)
		throws com.liferay.generative.ai.task.exception.
			NoSuchTaskDefinitionException {

		return getPersistence().findByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the first task definition in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching task definition, or <code>null</code> if a matching task definition could not be found
	 */
	public static TaskDefinition fetchByUuid_First(
		String uuid, OrderByComparator<TaskDefinition> orderByComparator) {

		return getPersistence().fetchByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the last task definition in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching task definition
	 * @throws NoSuchTaskDefinitionException if a matching task definition could not be found
	 */
	public static TaskDefinition findByUuid_Last(
			String uuid, OrderByComparator<TaskDefinition> orderByComparator)
		throws com.liferay.generative.ai.task.exception.
			NoSuchTaskDefinitionException {

		return getPersistence().findByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the last task definition in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching task definition, or <code>null</code> if a matching task definition could not be found
	 */
	public static TaskDefinition fetchByUuid_Last(
		String uuid, OrderByComparator<TaskDefinition> orderByComparator) {

		return getPersistence().fetchByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the task definitions before and after the current task definition in the ordered set where uuid = &#63;.
	 *
	 * @param taskDefinitionId the primary key of the current task definition
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next task definition
	 * @throws NoSuchTaskDefinitionException if a task definition with the primary key could not be found
	 */
	public static TaskDefinition[] findByUuid_PrevAndNext(
			long taskDefinitionId, String uuid,
			OrderByComparator<TaskDefinition> orderByComparator)
		throws com.liferay.generative.ai.task.exception.
			NoSuchTaskDefinitionException {

		return getPersistence().findByUuid_PrevAndNext(
			taskDefinitionId, uuid, orderByComparator);
	}

	/**
	 * Returns all the task definitions that the user has permission to view where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching task definitions that the user has permission to view
	 */
	public static List<TaskDefinition> filterFindByUuid(String uuid) {
		return getPersistence().filterFindByUuid(uuid);
	}

	/**
	 * Returns a range of all the task definitions that the user has permission to view where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @return the range of matching task definitions that the user has permission to view
	 */
	public static List<TaskDefinition> filterFindByUuid(
		String uuid, int start, int end) {

		return getPersistence().filterFindByUuid(uuid, start, end);
	}

	/**
	 * Returns an ordered range of all the task definitions that the user has permissions to view where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching task definitions that the user has permission to view
	 */
	public static List<TaskDefinition> filterFindByUuid(
		String uuid, int start, int end,
		OrderByComparator<TaskDefinition> orderByComparator) {

		return getPersistence().filterFindByUuid(
			uuid, start, end, orderByComparator);
	}

	/**
	 * Returns the task definitions before and after the current task definition in the ordered set of task definitions that the user has permission to view where uuid = &#63;.
	 *
	 * @param taskDefinitionId the primary key of the current task definition
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next task definition
	 * @throws NoSuchTaskDefinitionException if a task definition with the primary key could not be found
	 */
	public static TaskDefinition[] filterFindByUuid_PrevAndNext(
			long taskDefinitionId, String uuid,
			OrderByComparator<TaskDefinition> orderByComparator)
		throws com.liferay.generative.ai.task.exception.
			NoSuchTaskDefinitionException {

		return getPersistence().filterFindByUuid_PrevAndNext(
			taskDefinitionId, uuid, orderByComparator);
	}

	/**
	 * Removes all the task definitions where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public static void removeByUuid(String uuid) {
		getPersistence().removeByUuid(uuid);
	}

	/**
	 * Returns the number of task definitions where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching task definitions
	 */
	public static int countByUuid(String uuid) {
		return getPersistence().countByUuid(uuid);
	}

	/**
	 * Returns the number of task definitions that the user has permission to view where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching task definitions that the user has permission to view
	 */
	public static int filterCountByUuid(String uuid) {
		return getPersistence().filterCountByUuid(uuid);
	}

	/**
	 * Returns all the task definitions where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching task definitions
	 */
	public static List<TaskDefinition> findByUuid_C(
		String uuid, long companyId) {

		return getPersistence().findByUuid_C(uuid, companyId);
	}

	/**
	 * Returns a range of all the task definitions where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @return the range of matching task definitions
	 */
	public static List<TaskDefinition> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return getPersistence().findByUuid_C(uuid, companyId, start, end);
	}

	/**
	 * Returns an ordered range of all the task definitions where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching task definitions
	 */
	public static List<TaskDefinition> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<TaskDefinition> orderByComparator) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the task definitions where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching task definitions
	 */
	public static List<TaskDefinition> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<TaskDefinition> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first task definition in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching task definition
	 * @throws NoSuchTaskDefinitionException if a matching task definition could not be found
	 */
	public static TaskDefinition findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<TaskDefinition> orderByComparator)
		throws com.liferay.generative.ai.task.exception.
			NoSuchTaskDefinitionException {

		return getPersistence().findByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the first task definition in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching task definition, or <code>null</code> if a matching task definition could not be found
	 */
	public static TaskDefinition fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<TaskDefinition> orderByComparator) {

		return getPersistence().fetchByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the last task definition in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching task definition
	 * @throws NoSuchTaskDefinitionException if a matching task definition could not be found
	 */
	public static TaskDefinition findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<TaskDefinition> orderByComparator)
		throws com.liferay.generative.ai.task.exception.
			NoSuchTaskDefinitionException {

		return getPersistence().findByUuid_C_Last(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the last task definition in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching task definition, or <code>null</code> if a matching task definition could not be found
	 */
	public static TaskDefinition fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<TaskDefinition> orderByComparator) {

		return getPersistence().fetchByUuid_C_Last(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the task definitions before and after the current task definition in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param taskDefinitionId the primary key of the current task definition
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next task definition
	 * @throws NoSuchTaskDefinitionException if a task definition with the primary key could not be found
	 */
	public static TaskDefinition[] findByUuid_C_PrevAndNext(
			long taskDefinitionId, String uuid, long companyId,
			OrderByComparator<TaskDefinition> orderByComparator)
		throws com.liferay.generative.ai.task.exception.
			NoSuchTaskDefinitionException {

		return getPersistence().findByUuid_C_PrevAndNext(
			taskDefinitionId, uuid, companyId, orderByComparator);
	}

	/**
	 * Returns all the task definitions that the user has permission to view where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching task definitions that the user has permission to view
	 */
	public static List<TaskDefinition> filterFindByUuid_C(
		String uuid, long companyId) {

		return getPersistence().filterFindByUuid_C(uuid, companyId);
	}

	/**
	 * Returns a range of all the task definitions that the user has permission to view where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @return the range of matching task definitions that the user has permission to view
	 */
	public static List<TaskDefinition> filterFindByUuid_C(
		String uuid, long companyId, int start, int end) {

		return getPersistence().filterFindByUuid_C(uuid, companyId, start, end);
	}

	/**
	 * Returns an ordered range of all the task definitions that the user has permissions to view where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching task definitions that the user has permission to view
	 */
	public static List<TaskDefinition> filterFindByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<TaskDefinition> orderByComparator) {

		return getPersistence().filterFindByUuid_C(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the task definitions before and after the current task definition in the ordered set of task definitions that the user has permission to view where uuid = &#63; and companyId = &#63;.
	 *
	 * @param taskDefinitionId the primary key of the current task definition
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next task definition
	 * @throws NoSuchTaskDefinitionException if a task definition with the primary key could not be found
	 */
	public static TaskDefinition[] filterFindByUuid_C_PrevAndNext(
			long taskDefinitionId, String uuid, long companyId,
			OrderByComparator<TaskDefinition> orderByComparator)
		throws com.liferay.generative.ai.task.exception.
			NoSuchTaskDefinitionException {

		return getPersistence().filterFindByUuid_C_PrevAndNext(
			taskDefinitionId, uuid, companyId, orderByComparator);
	}

	/**
	 * Removes all the task definitions where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public static void removeByUuid_C(String uuid, long companyId) {
		getPersistence().removeByUuid_C(uuid, companyId);
	}

	/**
	 * Returns the number of task definitions where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching task definitions
	 */
	public static int countByUuid_C(String uuid, long companyId) {
		return getPersistence().countByUuid_C(uuid, companyId);
	}

	/**
	 * Returns the number of task definitions that the user has permission to view where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching task definitions that the user has permission to view
	 */
	public static int filterCountByUuid_C(String uuid, long companyId) {
		return getPersistence().filterCountByUuid_C(uuid, companyId);
	}

	/**
	 * Returns all the task definitions where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching task definitions
	 */
	public static List<TaskDefinition> findByCompanyId(long companyId) {
		return getPersistence().findByCompanyId(companyId);
	}

	/**
	 * Returns a range of all the task definitions where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @return the range of matching task definitions
	 */
	public static List<TaskDefinition> findByCompanyId(
		long companyId, int start, int end) {

		return getPersistence().findByCompanyId(companyId, start, end);
	}

	/**
	 * Returns an ordered range of all the task definitions where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching task definitions
	 */
	public static List<TaskDefinition> findByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<TaskDefinition> orderByComparator) {

		return getPersistence().findByCompanyId(
			companyId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the task definitions where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching task definitions
	 */
	public static List<TaskDefinition> findByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<TaskDefinition> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByCompanyId(
			companyId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first task definition in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching task definition
	 * @throws NoSuchTaskDefinitionException if a matching task definition could not be found
	 */
	public static TaskDefinition findByCompanyId_First(
			long companyId, OrderByComparator<TaskDefinition> orderByComparator)
		throws com.liferay.generative.ai.task.exception.
			NoSuchTaskDefinitionException {

		return getPersistence().findByCompanyId_First(
			companyId, orderByComparator);
	}

	/**
	 * Returns the first task definition in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching task definition, or <code>null</code> if a matching task definition could not be found
	 */
	public static TaskDefinition fetchByCompanyId_First(
		long companyId, OrderByComparator<TaskDefinition> orderByComparator) {

		return getPersistence().fetchByCompanyId_First(
			companyId, orderByComparator);
	}

	/**
	 * Returns the last task definition in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching task definition
	 * @throws NoSuchTaskDefinitionException if a matching task definition could not be found
	 */
	public static TaskDefinition findByCompanyId_Last(
			long companyId, OrderByComparator<TaskDefinition> orderByComparator)
		throws com.liferay.generative.ai.task.exception.
			NoSuchTaskDefinitionException {

		return getPersistence().findByCompanyId_Last(
			companyId, orderByComparator);
	}

	/**
	 * Returns the last task definition in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching task definition, or <code>null</code> if a matching task definition could not be found
	 */
	public static TaskDefinition fetchByCompanyId_Last(
		long companyId, OrderByComparator<TaskDefinition> orderByComparator) {

		return getPersistence().fetchByCompanyId_Last(
			companyId, orderByComparator);
	}

	/**
	 * Returns the task definitions before and after the current task definition in the ordered set where companyId = &#63;.
	 *
	 * @param taskDefinitionId the primary key of the current task definition
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next task definition
	 * @throws NoSuchTaskDefinitionException if a task definition with the primary key could not be found
	 */
	public static TaskDefinition[] findByCompanyId_PrevAndNext(
			long taskDefinitionId, long companyId,
			OrderByComparator<TaskDefinition> orderByComparator)
		throws com.liferay.generative.ai.task.exception.
			NoSuchTaskDefinitionException {

		return getPersistence().findByCompanyId_PrevAndNext(
			taskDefinitionId, companyId, orderByComparator);
	}

	/**
	 * Returns all the task definitions that the user has permission to view where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching task definitions that the user has permission to view
	 */
	public static List<TaskDefinition> filterFindByCompanyId(long companyId) {
		return getPersistence().filterFindByCompanyId(companyId);
	}

	/**
	 * Returns a range of all the task definitions that the user has permission to view where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @return the range of matching task definitions that the user has permission to view
	 */
	public static List<TaskDefinition> filterFindByCompanyId(
		long companyId, int start, int end) {

		return getPersistence().filterFindByCompanyId(companyId, start, end);
	}

	/**
	 * Returns an ordered range of all the task definitions that the user has permissions to view where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching task definitions that the user has permission to view
	 */
	public static List<TaskDefinition> filterFindByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<TaskDefinition> orderByComparator) {

		return getPersistence().filterFindByCompanyId(
			companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the task definitions before and after the current task definition in the ordered set of task definitions that the user has permission to view where companyId = &#63;.
	 *
	 * @param taskDefinitionId the primary key of the current task definition
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next task definition
	 * @throws NoSuchTaskDefinitionException if a task definition with the primary key could not be found
	 */
	public static TaskDefinition[] filterFindByCompanyId_PrevAndNext(
			long taskDefinitionId, long companyId,
			OrderByComparator<TaskDefinition> orderByComparator)
		throws com.liferay.generative.ai.task.exception.
			NoSuchTaskDefinitionException {

		return getPersistence().filterFindByCompanyId_PrevAndNext(
			taskDefinitionId, companyId, orderByComparator);
	}

	/**
	 * Removes all the task definitions where companyId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 */
	public static void removeByCompanyId(long companyId) {
		getPersistence().removeByCompanyId(companyId);
	}

	/**
	 * Returns the number of task definitions where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching task definitions
	 */
	public static int countByCompanyId(long companyId) {
		return getPersistence().countByCompanyId(companyId);
	}

	/**
	 * Returns the number of task definitions that the user has permission to view where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching task definitions that the user has permission to view
	 */
	public static int filterCountByCompanyId(long companyId) {
		return getPersistence().filterCountByCompanyId(companyId);
	}

	/**
	 * Returns all the task definitions where companyId = &#63; and readOnly = &#63;.
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @return the matching task definitions
	 */
	public static List<TaskDefinition> findByC_R(
		long companyId, boolean readOnly) {

		return getPersistence().findByC_R(companyId, readOnly);
	}

	/**
	 * Returns a range of all the task definitions where companyId = &#63; and readOnly = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @return the range of matching task definitions
	 */
	public static List<TaskDefinition> findByC_R(
		long companyId, boolean readOnly, int start, int end) {

		return getPersistence().findByC_R(companyId, readOnly, start, end);
	}

	/**
	 * Returns an ordered range of all the task definitions where companyId = &#63; and readOnly = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching task definitions
	 */
	public static List<TaskDefinition> findByC_R(
		long companyId, boolean readOnly, int start, int end,
		OrderByComparator<TaskDefinition> orderByComparator) {

		return getPersistence().findByC_R(
			companyId, readOnly, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the task definitions where companyId = &#63; and readOnly = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching task definitions
	 */
	public static List<TaskDefinition> findByC_R(
		long companyId, boolean readOnly, int start, int end,
		OrderByComparator<TaskDefinition> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByC_R(
			companyId, readOnly, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first task definition in the ordered set where companyId = &#63; and readOnly = &#63;.
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching task definition
	 * @throws NoSuchTaskDefinitionException if a matching task definition could not be found
	 */
	public static TaskDefinition findByC_R_First(
			long companyId, boolean readOnly,
			OrderByComparator<TaskDefinition> orderByComparator)
		throws com.liferay.generative.ai.task.exception.
			NoSuchTaskDefinitionException {

		return getPersistence().findByC_R_First(
			companyId, readOnly, orderByComparator);
	}

	/**
	 * Returns the first task definition in the ordered set where companyId = &#63; and readOnly = &#63;.
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching task definition, or <code>null</code> if a matching task definition could not be found
	 */
	public static TaskDefinition fetchByC_R_First(
		long companyId, boolean readOnly,
		OrderByComparator<TaskDefinition> orderByComparator) {

		return getPersistence().fetchByC_R_First(
			companyId, readOnly, orderByComparator);
	}

	/**
	 * Returns the last task definition in the ordered set where companyId = &#63; and readOnly = &#63;.
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching task definition
	 * @throws NoSuchTaskDefinitionException if a matching task definition could not be found
	 */
	public static TaskDefinition findByC_R_Last(
			long companyId, boolean readOnly,
			OrderByComparator<TaskDefinition> orderByComparator)
		throws com.liferay.generative.ai.task.exception.
			NoSuchTaskDefinitionException {

		return getPersistence().findByC_R_Last(
			companyId, readOnly, orderByComparator);
	}

	/**
	 * Returns the last task definition in the ordered set where companyId = &#63; and readOnly = &#63;.
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching task definition, or <code>null</code> if a matching task definition could not be found
	 */
	public static TaskDefinition fetchByC_R_Last(
		long companyId, boolean readOnly,
		OrderByComparator<TaskDefinition> orderByComparator) {

		return getPersistence().fetchByC_R_Last(
			companyId, readOnly, orderByComparator);
	}

	/**
	 * Returns the task definitions before and after the current task definition in the ordered set where companyId = &#63; and readOnly = &#63;.
	 *
	 * @param taskDefinitionId the primary key of the current task definition
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next task definition
	 * @throws NoSuchTaskDefinitionException if a task definition with the primary key could not be found
	 */
	public static TaskDefinition[] findByC_R_PrevAndNext(
			long taskDefinitionId, long companyId, boolean readOnly,
			OrderByComparator<TaskDefinition> orderByComparator)
		throws com.liferay.generative.ai.task.exception.
			NoSuchTaskDefinitionException {

		return getPersistence().findByC_R_PrevAndNext(
			taskDefinitionId, companyId, readOnly, orderByComparator);
	}

	/**
	 * Returns all the task definitions that the user has permission to view where companyId = &#63; and readOnly = &#63;.
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @return the matching task definitions that the user has permission to view
	 */
	public static List<TaskDefinition> filterFindByC_R(
		long companyId, boolean readOnly) {

		return getPersistence().filterFindByC_R(companyId, readOnly);
	}

	/**
	 * Returns a range of all the task definitions that the user has permission to view where companyId = &#63; and readOnly = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @return the range of matching task definitions that the user has permission to view
	 */
	public static List<TaskDefinition> filterFindByC_R(
		long companyId, boolean readOnly, int start, int end) {

		return getPersistence().filterFindByC_R(
			companyId, readOnly, start, end);
	}

	/**
	 * Returns an ordered range of all the task definitions that the user has permissions to view where companyId = &#63; and readOnly = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching task definitions that the user has permission to view
	 */
	public static List<TaskDefinition> filterFindByC_R(
		long companyId, boolean readOnly, int start, int end,
		OrderByComparator<TaskDefinition> orderByComparator) {

		return getPersistence().filterFindByC_R(
			companyId, readOnly, start, end, orderByComparator);
	}

	/**
	 * Returns the task definitions before and after the current task definition in the ordered set of task definitions that the user has permission to view where companyId = &#63; and readOnly = &#63;.
	 *
	 * @param taskDefinitionId the primary key of the current task definition
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next task definition
	 * @throws NoSuchTaskDefinitionException if a task definition with the primary key could not be found
	 */
	public static TaskDefinition[] filterFindByC_R_PrevAndNext(
			long taskDefinitionId, long companyId, boolean readOnly,
			OrderByComparator<TaskDefinition> orderByComparator)
		throws com.liferay.generative.ai.task.exception.
			NoSuchTaskDefinitionException {

		return getPersistence().filterFindByC_R_PrevAndNext(
			taskDefinitionId, companyId, readOnly, orderByComparator);
	}

	/**
	 * Removes all the task definitions where companyId = &#63; and readOnly = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 */
	public static void removeByC_R(long companyId, boolean readOnly) {
		getPersistence().removeByC_R(companyId, readOnly);
	}

	/**
	 * Returns the number of task definitions where companyId = &#63; and readOnly = &#63;.
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @return the number of matching task definitions
	 */
	public static int countByC_R(long companyId, boolean readOnly) {
		return getPersistence().countByC_R(companyId, readOnly);
	}

	/**
	 * Returns the number of task definitions that the user has permission to view where companyId = &#63; and readOnly = &#63;.
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @return the number of matching task definitions that the user has permission to view
	 */
	public static int filterCountByC_R(long companyId, boolean readOnly) {
		return getPersistence().filterCountByC_R(companyId, readOnly);
	}

	/**
	 * Returns the task definition where externalReferenceCode = &#63; and companyId = &#63; or throws a <code>NoSuchTaskDefinitionException</code> if it could not be found.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param companyId the company ID
	 * @return the matching task definition
	 * @throws NoSuchTaskDefinitionException if a matching task definition could not be found
	 */
	public static TaskDefinition findByERC_C(
			String externalReferenceCode, long companyId)
		throws com.liferay.generative.ai.task.exception.
			NoSuchTaskDefinitionException {

		return getPersistence().findByERC_C(externalReferenceCode, companyId);
	}

	/**
	 * Returns the task definition where externalReferenceCode = &#63; and companyId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param companyId the company ID
	 * @return the matching task definition, or <code>null</code> if a matching task definition could not be found
	 */
	public static TaskDefinition fetchByERC_C(
		String externalReferenceCode, long companyId) {

		return getPersistence().fetchByERC_C(externalReferenceCode, companyId);
	}

	/**
	 * Returns the task definition where externalReferenceCode = &#63; and companyId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param companyId the company ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching task definition, or <code>null</code> if a matching task definition could not be found
	 */
	public static TaskDefinition fetchByERC_C(
		String externalReferenceCode, long companyId, boolean useFinderCache) {

		return getPersistence().fetchByERC_C(
			externalReferenceCode, companyId, useFinderCache);
	}

	/**
	 * Removes the task definition where externalReferenceCode = &#63; and companyId = &#63; from the database.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param companyId the company ID
	 * @return the task definition that was removed
	 */
	public static TaskDefinition removeByERC_C(
			String externalReferenceCode, long companyId)
		throws com.liferay.generative.ai.task.exception.
			NoSuchTaskDefinitionException {

		return getPersistence().removeByERC_C(externalReferenceCode, companyId);
	}

	/**
	 * Returns the number of task definitions where externalReferenceCode = &#63; and companyId = &#63;.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param companyId the company ID
	 * @return the number of matching task definitions
	 */
	public static int countByERC_C(
		String externalReferenceCode, long companyId) {

		return getPersistence().countByERC_C(externalReferenceCode, companyId);
	}

	/**
	 * Caches the task definition in the entity cache if it is enabled.
	 *
	 * @param taskDefinition the task definition
	 */
	public static void cacheResult(TaskDefinition taskDefinition) {
		getPersistence().cacheResult(taskDefinition);
	}

	/**
	 * Caches the task definitions in the entity cache if it is enabled.
	 *
	 * @param taskDefinitions the task definitions
	 */
	public static void cacheResult(List<TaskDefinition> taskDefinitions) {
		getPersistence().cacheResult(taskDefinitions);
	}

	/**
	 * Creates a new task definition with the primary key. Does not add the task definition to the database.
	 *
	 * @param taskDefinitionId the primary key for the new task definition
	 * @return the new task definition
	 */
	public static TaskDefinition create(long taskDefinitionId) {
		return getPersistence().create(taskDefinitionId);
	}

	/**
	 * Removes the task definition with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param taskDefinitionId the primary key of the task definition
	 * @return the task definition that was removed
	 * @throws NoSuchTaskDefinitionException if a task definition with the primary key could not be found
	 */
	public static TaskDefinition remove(long taskDefinitionId)
		throws com.liferay.generative.ai.task.exception.
			NoSuchTaskDefinitionException {

		return getPersistence().remove(taskDefinitionId);
	}

	public static TaskDefinition updateImpl(TaskDefinition taskDefinition) {
		return getPersistence().updateImpl(taskDefinition);
	}

	/**
	 * Returns the task definition with the primary key or throws a <code>NoSuchTaskDefinitionException</code> if it could not be found.
	 *
	 * @param taskDefinitionId the primary key of the task definition
	 * @return the task definition
	 * @throws NoSuchTaskDefinitionException if a task definition with the primary key could not be found
	 */
	public static TaskDefinition findByPrimaryKey(long taskDefinitionId)
		throws com.liferay.generative.ai.task.exception.
			NoSuchTaskDefinitionException {

		return getPersistence().findByPrimaryKey(taskDefinitionId);
	}

	/**
	 * Returns the task definition with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param taskDefinitionId the primary key of the task definition
	 * @return the task definition, or <code>null</code> if a task definition with the primary key could not be found
	 */
	public static TaskDefinition fetchByPrimaryKey(long taskDefinitionId) {
		return getPersistence().fetchByPrimaryKey(taskDefinitionId);
	}

	/**
	 * Returns all the task definitions.
	 *
	 * @return the task definitions
	 */
	public static List<TaskDefinition> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the task definitions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @return the range of task definitions
	 */
	public static List<TaskDefinition> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the task definitions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of task definitions
	 */
	public static List<TaskDefinition> findAll(
		int start, int end,
		OrderByComparator<TaskDefinition> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the task definitions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of task definitions
	 */
	public static List<TaskDefinition> findAll(
		int start, int end, OrderByComparator<TaskDefinition> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the task definitions from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of task definitions.
	 *
	 * @return the number of task definitions
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static TaskDefinitionPersistence getPersistence() {
		return _persistence;
	}

	public static void setPersistence(TaskDefinitionPersistence persistence) {
		_persistence = persistence;
	}

	private static volatile TaskDefinitionPersistence _persistence;

}