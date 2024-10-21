/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.service.persistence;

import com.liferay.generative.ai.task.exception.NoSuchTaskDefinitionException;
import com.liferay.generative.ai.task.model.TaskDefinition;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the task definition service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see TaskDefinitionUtil
 * @generated
 */
@ProviderType
public interface TaskDefinitionPersistence
	extends BasePersistence<TaskDefinition> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link TaskDefinitionUtil} to access the task definition persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns all the task definitions where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching task definitions
	 */
	public java.util.List<TaskDefinition> findByUuid(String uuid);

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
	public java.util.List<TaskDefinition> findByUuid(
		String uuid, int start, int end);

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
	public java.util.List<TaskDefinition> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
			orderByComparator);

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
	public java.util.List<TaskDefinition> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first task definition in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching task definition
	 * @throws NoSuchTaskDefinitionException if a matching task definition could not be found
	 */
	public TaskDefinition findByUuid_First(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
				orderByComparator)
		throws NoSuchTaskDefinitionException;

	/**
	 * Returns the first task definition in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching task definition, or <code>null</code> if a matching task definition could not be found
	 */
	public TaskDefinition fetchByUuid_First(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
			orderByComparator);

	/**
	 * Returns the last task definition in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching task definition
	 * @throws NoSuchTaskDefinitionException if a matching task definition could not be found
	 */
	public TaskDefinition findByUuid_Last(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
				orderByComparator)
		throws NoSuchTaskDefinitionException;

	/**
	 * Returns the last task definition in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching task definition, or <code>null</code> if a matching task definition could not be found
	 */
	public TaskDefinition fetchByUuid_Last(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
			orderByComparator);

	/**
	 * Returns the task definitions before and after the current task definition in the ordered set where uuid = &#63;.
	 *
	 * @param taskDefinitionId the primary key of the current task definition
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next task definition
	 * @throws NoSuchTaskDefinitionException if a task definition with the primary key could not be found
	 */
	public TaskDefinition[] findByUuid_PrevAndNext(
			long taskDefinitionId, String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
				orderByComparator)
		throws NoSuchTaskDefinitionException;

	/**
	 * Returns all the task definitions that the user has permission to view where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching task definitions that the user has permission to view
	 */
	public java.util.List<TaskDefinition> filterFindByUuid(String uuid);

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
	public java.util.List<TaskDefinition> filterFindByUuid(
		String uuid, int start, int end);

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
	public java.util.List<TaskDefinition> filterFindByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
			orderByComparator);

	/**
	 * Returns the task definitions before and after the current task definition in the ordered set of task definitions that the user has permission to view where uuid = &#63;.
	 *
	 * @param taskDefinitionId the primary key of the current task definition
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next task definition
	 * @throws NoSuchTaskDefinitionException if a task definition with the primary key could not be found
	 */
	public TaskDefinition[] filterFindByUuid_PrevAndNext(
			long taskDefinitionId, String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
				orderByComparator)
		throws NoSuchTaskDefinitionException;

	/**
	 * Removes all the task definitions where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public void removeByUuid(String uuid);

	/**
	 * Returns the number of task definitions where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching task definitions
	 */
	public int countByUuid(String uuid);

	/**
	 * Returns the number of task definitions that the user has permission to view where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching task definitions that the user has permission to view
	 */
	public int filterCountByUuid(String uuid);

	/**
	 * Returns all the task definitions where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching task definitions
	 */
	public java.util.List<TaskDefinition> findByUuid_C(
		String uuid, long companyId);

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
	public java.util.List<TaskDefinition> findByUuid_C(
		String uuid, long companyId, int start, int end);

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
	public java.util.List<TaskDefinition> findByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
			orderByComparator);

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
	public java.util.List<TaskDefinition> findByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first task definition in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching task definition
	 * @throws NoSuchTaskDefinitionException if a matching task definition could not be found
	 */
	public TaskDefinition findByUuid_C_First(
			String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
				orderByComparator)
		throws NoSuchTaskDefinitionException;

	/**
	 * Returns the first task definition in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching task definition, or <code>null</code> if a matching task definition could not be found
	 */
	public TaskDefinition fetchByUuid_C_First(
		String uuid, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
			orderByComparator);

	/**
	 * Returns the last task definition in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching task definition
	 * @throws NoSuchTaskDefinitionException if a matching task definition could not be found
	 */
	public TaskDefinition findByUuid_C_Last(
			String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
				orderByComparator)
		throws NoSuchTaskDefinitionException;

	/**
	 * Returns the last task definition in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching task definition, or <code>null</code> if a matching task definition could not be found
	 */
	public TaskDefinition fetchByUuid_C_Last(
		String uuid, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
			orderByComparator);

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
	public TaskDefinition[] findByUuid_C_PrevAndNext(
			long taskDefinitionId, String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
				orderByComparator)
		throws NoSuchTaskDefinitionException;

	/**
	 * Returns all the task definitions that the user has permission to view where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching task definitions that the user has permission to view
	 */
	public java.util.List<TaskDefinition> filterFindByUuid_C(
		String uuid, long companyId);

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
	public java.util.List<TaskDefinition> filterFindByUuid_C(
		String uuid, long companyId, int start, int end);

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
	public java.util.List<TaskDefinition> filterFindByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
			orderByComparator);

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
	public TaskDefinition[] filterFindByUuid_C_PrevAndNext(
			long taskDefinitionId, String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
				orderByComparator)
		throws NoSuchTaskDefinitionException;

	/**
	 * Removes all the task definitions where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public void removeByUuid_C(String uuid, long companyId);

	/**
	 * Returns the number of task definitions where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching task definitions
	 */
	public int countByUuid_C(String uuid, long companyId);

	/**
	 * Returns the number of task definitions that the user has permission to view where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching task definitions that the user has permission to view
	 */
	public int filterCountByUuid_C(String uuid, long companyId);

	/**
	 * Returns all the task definitions where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching task definitions
	 */
	public java.util.List<TaskDefinition> findByCompanyId(long companyId);

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
	public java.util.List<TaskDefinition> findByCompanyId(
		long companyId, int start, int end);

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
	public java.util.List<TaskDefinition> findByCompanyId(
		long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
			orderByComparator);

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
	public java.util.List<TaskDefinition> findByCompanyId(
		long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first task definition in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching task definition
	 * @throws NoSuchTaskDefinitionException if a matching task definition could not be found
	 */
	public TaskDefinition findByCompanyId_First(
			long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
				orderByComparator)
		throws NoSuchTaskDefinitionException;

	/**
	 * Returns the first task definition in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching task definition, or <code>null</code> if a matching task definition could not be found
	 */
	public TaskDefinition fetchByCompanyId_First(
		long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
			orderByComparator);

	/**
	 * Returns the last task definition in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching task definition
	 * @throws NoSuchTaskDefinitionException if a matching task definition could not be found
	 */
	public TaskDefinition findByCompanyId_Last(
			long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
				orderByComparator)
		throws NoSuchTaskDefinitionException;

	/**
	 * Returns the last task definition in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching task definition, or <code>null</code> if a matching task definition could not be found
	 */
	public TaskDefinition fetchByCompanyId_Last(
		long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
			orderByComparator);

	/**
	 * Returns the task definitions before and after the current task definition in the ordered set where companyId = &#63;.
	 *
	 * @param taskDefinitionId the primary key of the current task definition
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next task definition
	 * @throws NoSuchTaskDefinitionException if a task definition with the primary key could not be found
	 */
	public TaskDefinition[] findByCompanyId_PrevAndNext(
			long taskDefinitionId, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
				orderByComparator)
		throws NoSuchTaskDefinitionException;

	/**
	 * Returns all the task definitions that the user has permission to view where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching task definitions that the user has permission to view
	 */
	public java.util.List<TaskDefinition> filterFindByCompanyId(long companyId);

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
	public java.util.List<TaskDefinition> filterFindByCompanyId(
		long companyId, int start, int end);

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
	public java.util.List<TaskDefinition> filterFindByCompanyId(
		long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
			orderByComparator);

	/**
	 * Returns the task definitions before and after the current task definition in the ordered set of task definitions that the user has permission to view where companyId = &#63;.
	 *
	 * @param taskDefinitionId the primary key of the current task definition
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next task definition
	 * @throws NoSuchTaskDefinitionException if a task definition with the primary key could not be found
	 */
	public TaskDefinition[] filterFindByCompanyId_PrevAndNext(
			long taskDefinitionId, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
				orderByComparator)
		throws NoSuchTaskDefinitionException;

	/**
	 * Removes all the task definitions where companyId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 */
	public void removeByCompanyId(long companyId);

	/**
	 * Returns the number of task definitions where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching task definitions
	 */
	public int countByCompanyId(long companyId);

	/**
	 * Returns the number of task definitions that the user has permission to view where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching task definitions that the user has permission to view
	 */
	public int filterCountByCompanyId(long companyId);

	/**
	 * Returns all the task definitions where companyId = &#63; and readOnly = &#63;.
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @return the matching task definitions
	 */
	public java.util.List<TaskDefinition> findByC_R(
		long companyId, boolean readOnly);

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
	public java.util.List<TaskDefinition> findByC_R(
		long companyId, boolean readOnly, int start, int end);

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
	public java.util.List<TaskDefinition> findByC_R(
		long companyId, boolean readOnly, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
			orderByComparator);

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
	public java.util.List<TaskDefinition> findByC_R(
		long companyId, boolean readOnly, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first task definition in the ordered set where companyId = &#63; and readOnly = &#63;.
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching task definition
	 * @throws NoSuchTaskDefinitionException if a matching task definition could not be found
	 */
	public TaskDefinition findByC_R_First(
			long companyId, boolean readOnly,
			com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
				orderByComparator)
		throws NoSuchTaskDefinitionException;

	/**
	 * Returns the first task definition in the ordered set where companyId = &#63; and readOnly = &#63;.
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching task definition, or <code>null</code> if a matching task definition could not be found
	 */
	public TaskDefinition fetchByC_R_First(
		long companyId, boolean readOnly,
		com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
			orderByComparator);

	/**
	 * Returns the last task definition in the ordered set where companyId = &#63; and readOnly = &#63;.
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching task definition
	 * @throws NoSuchTaskDefinitionException if a matching task definition could not be found
	 */
	public TaskDefinition findByC_R_Last(
			long companyId, boolean readOnly,
			com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
				orderByComparator)
		throws NoSuchTaskDefinitionException;

	/**
	 * Returns the last task definition in the ordered set where companyId = &#63; and readOnly = &#63;.
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching task definition, or <code>null</code> if a matching task definition could not be found
	 */
	public TaskDefinition fetchByC_R_Last(
		long companyId, boolean readOnly,
		com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
			orderByComparator);

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
	public TaskDefinition[] findByC_R_PrevAndNext(
			long taskDefinitionId, long companyId, boolean readOnly,
			com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
				orderByComparator)
		throws NoSuchTaskDefinitionException;

	/**
	 * Returns all the task definitions that the user has permission to view where companyId = &#63; and readOnly = &#63;.
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @return the matching task definitions that the user has permission to view
	 */
	public java.util.List<TaskDefinition> filterFindByC_R(
		long companyId, boolean readOnly);

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
	public java.util.List<TaskDefinition> filterFindByC_R(
		long companyId, boolean readOnly, int start, int end);

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
	public java.util.List<TaskDefinition> filterFindByC_R(
		long companyId, boolean readOnly, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
			orderByComparator);

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
	public TaskDefinition[] filterFindByC_R_PrevAndNext(
			long taskDefinitionId, long companyId, boolean readOnly,
			com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
				orderByComparator)
		throws NoSuchTaskDefinitionException;

	/**
	 * Removes all the task definitions where companyId = &#63; and readOnly = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 */
	public void removeByC_R(long companyId, boolean readOnly);

	/**
	 * Returns the number of task definitions where companyId = &#63; and readOnly = &#63;.
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @return the number of matching task definitions
	 */
	public int countByC_R(long companyId, boolean readOnly);

	/**
	 * Returns the number of task definitions that the user has permission to view where companyId = &#63; and readOnly = &#63;.
	 *
	 * @param companyId the company ID
	 * @param readOnly the read only
	 * @return the number of matching task definitions that the user has permission to view
	 */
	public int filterCountByC_R(long companyId, boolean readOnly);

	/**
	 * Returns the task definition where externalReferenceCode = &#63; and companyId = &#63; or throws a <code>NoSuchTaskDefinitionException</code> if it could not be found.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param companyId the company ID
	 * @return the matching task definition
	 * @throws NoSuchTaskDefinitionException if a matching task definition could not be found
	 */
	public TaskDefinition findByERC_C(
			String externalReferenceCode, long companyId)
		throws NoSuchTaskDefinitionException;

	/**
	 * Returns the task definition where externalReferenceCode = &#63; and companyId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param companyId the company ID
	 * @return the matching task definition, or <code>null</code> if a matching task definition could not be found
	 */
	public TaskDefinition fetchByERC_C(
		String externalReferenceCode, long companyId);

	/**
	 * Returns the task definition where externalReferenceCode = &#63; and companyId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param companyId the company ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching task definition, or <code>null</code> if a matching task definition could not be found
	 */
	public TaskDefinition fetchByERC_C(
		String externalReferenceCode, long companyId, boolean useFinderCache);

	/**
	 * Removes the task definition where externalReferenceCode = &#63; and companyId = &#63; from the database.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param companyId the company ID
	 * @return the task definition that was removed
	 */
	public TaskDefinition removeByERC_C(
			String externalReferenceCode, long companyId)
		throws NoSuchTaskDefinitionException;

	/**
	 * Returns the number of task definitions where externalReferenceCode = &#63; and companyId = &#63;.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param companyId the company ID
	 * @return the number of matching task definitions
	 */
	public int countByERC_C(String externalReferenceCode, long companyId);

	/**
	 * Caches the task definition in the entity cache if it is enabled.
	 *
	 * @param taskDefinition the task definition
	 */
	public void cacheResult(TaskDefinition taskDefinition);

	/**
	 * Caches the task definitions in the entity cache if it is enabled.
	 *
	 * @param taskDefinitions the task definitions
	 */
	public void cacheResult(java.util.List<TaskDefinition> taskDefinitions);

	/**
	 * Creates a new task definition with the primary key. Does not add the task definition to the database.
	 *
	 * @param taskDefinitionId the primary key for the new task definition
	 * @return the new task definition
	 */
	public TaskDefinition create(long taskDefinitionId);

	/**
	 * Removes the task definition with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param taskDefinitionId the primary key of the task definition
	 * @return the task definition that was removed
	 * @throws NoSuchTaskDefinitionException if a task definition with the primary key could not be found
	 */
	public TaskDefinition remove(long taskDefinitionId)
		throws NoSuchTaskDefinitionException;

	public TaskDefinition updateImpl(TaskDefinition taskDefinition);

	/**
	 * Returns the task definition with the primary key or throws a <code>NoSuchTaskDefinitionException</code> if it could not be found.
	 *
	 * @param taskDefinitionId the primary key of the task definition
	 * @return the task definition
	 * @throws NoSuchTaskDefinitionException if a task definition with the primary key could not be found
	 */
	public TaskDefinition findByPrimaryKey(long taskDefinitionId)
		throws NoSuchTaskDefinitionException;

	/**
	 * Returns the task definition with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param taskDefinitionId the primary key of the task definition
	 * @return the task definition, or <code>null</code> if a task definition with the primary key could not be found
	 */
	public TaskDefinition fetchByPrimaryKey(long taskDefinitionId);

	/**
	 * Returns all the task definitions.
	 *
	 * @return the task definitions
	 */
	public java.util.List<TaskDefinition> findAll();

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
	public java.util.List<TaskDefinition> findAll(int start, int end);

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
	public java.util.List<TaskDefinition> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
			orderByComparator);

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
	public java.util.List<TaskDefinition> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<TaskDefinition>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the task definitions from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of task definitions.
	 *
	 * @return the number of task definitions
	 */
	public int countAll();

}