/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.service;

import com.liferay.generative.ai.task.model.TaskDefinition;
import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.service.Snapshot;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;
import java.util.Map;

/**
 * Provides the local service utility for TaskDefinition. This utility wraps
 * <code>com.liferay.generative.ai.task.service.impl.TaskDefinitionLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see TaskDefinitionLocalService
 * @generated
 */
public class TaskDefinitionLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.generative.ai.task.service.impl.TaskDefinitionLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static TaskDefinition addTaskDefinition(
			String configurationJSON,
			Map<java.util.Locale, String> descriptionMap,
			String externalReferenceCode, boolean readOnly,
			String schemaVersion,
			com.liferay.portal.kernel.service.ServiceContext serviceContext,
			Map<java.util.Locale, String> titleMap, long userId)
		throws PortalException {

		return getService().addTaskDefinition(
			configurationJSON, descriptionMap, externalReferenceCode, readOnly,
			schemaVersion, serviceContext, titleMap, userId);
	}

	/**
	 * Adds the task definition to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect TaskDefinitionLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param taskDefinition the task definition
	 * @return the task definition that was added
	 */
	public static TaskDefinition addTaskDefinition(
		TaskDefinition taskDefinition) {

		return getService().addTaskDefinition(taskDefinition);
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel createPersistedModel(
			Serializable primaryKeyObj)
		throws PortalException {

		return getService().createPersistedModel(primaryKeyObj);
	}

	/**
	 * Creates a new task definition with the primary key. Does not add the task definition to the database.
	 *
	 * @param taskDefinitionId the primary key for the new task definition
	 * @return the new task definition
	 */
	public static TaskDefinition createTaskDefinition(long taskDefinitionId) {
		return getService().createTaskDefinition(taskDefinitionId);
	}

	public static void deleteCompanyTaskDefinitions(long companyId)
		throws PortalException {

		getService().deleteCompanyTaskDefinitions(companyId);
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel deletePersistedModel(
			PersistedModel persistedModel)
		throws PortalException {

		return getService().deletePersistedModel(persistedModel);
	}

	/**
	 * Deletes the task definition with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect TaskDefinitionLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param taskDefinitionId the primary key of the task definition
	 * @return the task definition that was removed
	 * @throws PortalException if a task definition with the primary key could not be found
	 */
	public static TaskDefinition deleteTaskDefinition(long taskDefinitionId)
		throws PortalException {

		return getService().deleteTaskDefinition(taskDefinitionId);
	}

	/**
	 * Deletes the task definition from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect TaskDefinitionLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param taskDefinition the task definition
	 * @return the task definition that was removed
	 * @throws PortalException
	 */
	public static TaskDefinition deleteTaskDefinition(
			TaskDefinition taskDefinition)
		throws PortalException {

		return getService().deleteTaskDefinition(taskDefinition);
	}

	public static <T> T dslQuery(DSLQuery dslQuery) {
		return getService().dslQuery(dslQuery);
	}

	public static int dslQueryCount(DSLQuery dslQuery) {
		return getService().dslQueryCount(dslQuery);
	}

	public static DynamicQuery dynamicQuery() {
		return getService().dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	public static <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return getService().dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.generative.ai.task.model.impl.TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	public static <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.generative.ai.task.model.impl.TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	public static <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator) {

		return getService().dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(DynamicQuery dynamicQuery) {
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
		DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static TaskDefinition fetchTaskDefinition(long taskDefinitionId) {
		return getService().fetchTaskDefinition(taskDefinitionId);
	}

	public static TaskDefinition fetchTaskDefinitionByExternalReferenceCode(
		String externalReferenceCode, long companyId) {

		return getService().fetchTaskDefinitionByExternalReferenceCode(
			externalReferenceCode, companyId);
	}

	/**
	 * Returns the task definition with the matching UUID and company.
	 *
	 * @param uuid the task definition's UUID
	 * @param companyId the primary key of the company
	 * @return the matching task definition, or <code>null</code> if a matching task definition could not be found
	 */
	public static TaskDefinition fetchTaskDefinitionByUuidAndCompanyId(
		String uuid, long companyId) {

		return getService().fetchTaskDefinitionByUuidAndCompanyId(
			uuid, companyId);
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
	public static PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	/**
	 * Returns the task definition with the primary key.
	 *
	 * @param taskDefinitionId the primary key of the task definition
	 * @return the task definition
	 * @throws PortalException if a task definition with the primary key could not be found
	 */
	public static TaskDefinition getTaskDefinition(long taskDefinitionId)
		throws PortalException {

		return getService().getTaskDefinition(taskDefinitionId);
	}

	public static TaskDefinition getTaskDefinitionByExternalReferenceCode(
			String externalReferenceCode, long companyId)
		throws PortalException {

		return getService().getTaskDefinitionByExternalReferenceCode(
			externalReferenceCode, companyId);
	}

	/**
	 * Returns the task definition with the matching UUID and company.
	 *
	 * @param uuid the task definition's UUID
	 * @param companyId the primary key of the company
	 * @return the matching task definition
	 * @throws PortalException if a matching task definition could not be found
	 */
	public static TaskDefinition getTaskDefinitionByUuidAndCompanyId(
			String uuid, long companyId)
		throws PortalException {

		return getService().getTaskDefinitionByUuidAndCompanyId(
			uuid, companyId);
	}

	/**
	 * Returns a range of all the task definitions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.generative.ai.task.model.impl.TaskDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of task definitions
	 * @param end the upper bound of the range of task definitions (not inclusive)
	 * @return the range of task definitions
	 */
	public static List<TaskDefinition> getTaskDefinitions(int start, int end) {
		return getService().getTaskDefinitions(start, end);
	}

	public static List<TaskDefinition> getTaskDefinitions(
		long companyId, boolean readOnly) {

		return getService().getTaskDefinitions(companyId, readOnly);
	}

	/**
	 * Returns the number of task definitions.
	 *
	 * @return the number of task definitions
	 */
	public static int getTaskDefinitionsCount() {
		return getService().getTaskDefinitionsCount();
	}

	public static int getTaskDefinitionsCount(
		long companyId, boolean readOnly) {

		return getService().getTaskDefinitionsCount(companyId, readOnly);
	}

	public static TaskDefinition updateStatus(
			long taskDefinitionId,
			com.liferay.portal.kernel.service.ServiceContext serviceContext,
			int status, long userId)
		throws PortalException {

		return getService().updateStatus(
			taskDefinitionId, serviceContext, status, userId);
	}

	public static TaskDefinition updateTaskDefinition(
			String configurationJSON,
			Map<java.util.Locale, String> descriptionMap,
			String externalReferenceCode, long taskDefinitionId,
			String schemaVersion,
			com.liferay.portal.kernel.service.ServiceContext serviceContext,
			Map<java.util.Locale, String> titleMap)
		throws PortalException {

		return getService().updateTaskDefinition(
			configurationJSON, descriptionMap, externalReferenceCode,
			taskDefinitionId, schemaVersion, serviceContext, titleMap);
	}

	/**
	 * Updates the task definition in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect TaskDefinitionLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param taskDefinition the task definition
	 * @return the task definition that was updated
	 */
	public static TaskDefinition updateTaskDefinition(
		TaskDefinition taskDefinition) {

		return getService().updateTaskDefinition(taskDefinition);
	}

	public static TaskDefinitionLocalService getService() {
		return _serviceSnapshot.get();
	}

	private static final Snapshot<TaskDefinitionLocalService> _serviceSnapshot =
		new Snapshot<>(
			TaskDefinitionLocalServiceUtil.class,
			TaskDefinitionLocalService.class);

}