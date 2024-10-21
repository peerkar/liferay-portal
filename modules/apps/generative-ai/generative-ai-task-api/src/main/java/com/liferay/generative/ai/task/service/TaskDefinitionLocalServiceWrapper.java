/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.service;

import com.liferay.portal.kernel.service.ServiceWrapper;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

/**
 * Provides a wrapper for {@link TaskDefinitionLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see TaskDefinitionLocalService
 * @generated
 */
public class TaskDefinitionLocalServiceWrapper
	implements ServiceWrapper<TaskDefinitionLocalService>,
			   TaskDefinitionLocalService {

	public TaskDefinitionLocalServiceWrapper() {
		this(null);
	}

	public TaskDefinitionLocalServiceWrapper(
		TaskDefinitionLocalService taskDefinitionLocalService) {

		_taskDefinitionLocalService = taskDefinitionLocalService;
	}

	@Override
	public com.liferay.generative.ai.task.model.TaskDefinition
			addTaskDefinition(
				String configurationJSON,
				java.util.Map<java.util.Locale, String> descriptionMap,
				String externalReferenceCode, boolean readOnly,
				String schemaVersion,
				com.liferay.portal.kernel.service.ServiceContext serviceContext,
				java.util.Map<java.util.Locale, String> titleMap, long userId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _taskDefinitionLocalService.addTaskDefinition(
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
	@Override
	public com.liferay.generative.ai.task.model.TaskDefinition
		addTaskDefinition(
			com.liferay.generative.ai.task.model.TaskDefinition
				taskDefinition) {

		return _taskDefinitionLocalService.addTaskDefinition(taskDefinition);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel createPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _taskDefinitionLocalService.createPersistedModel(primaryKeyObj);
	}

	/**
	 * Creates a new task definition with the primary key. Does not add the task definition to the database.
	 *
	 * @param taskDefinitionId the primary key for the new task definition
	 * @return the new task definition
	 */
	@Override
	public com.liferay.generative.ai.task.model.TaskDefinition
		createTaskDefinition(long taskDefinitionId) {

		return _taskDefinitionLocalService.createTaskDefinition(
			taskDefinitionId);
	}

	@Override
	public void deleteCompanyTaskDefinitions(long companyId)
		throws com.liferay.portal.kernel.exception.PortalException {

		_taskDefinitionLocalService.deleteCompanyTaskDefinitions(companyId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _taskDefinitionLocalService.deletePersistedModel(persistedModel);
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
	@Override
	public com.liferay.generative.ai.task.model.TaskDefinition
			deleteTaskDefinition(long taskDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _taskDefinitionLocalService.deleteTaskDefinition(
			taskDefinitionId);
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
	@Override
	public com.liferay.generative.ai.task.model.TaskDefinition
			deleteTaskDefinition(
				com.liferay.generative.ai.task.model.TaskDefinition
					taskDefinition)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _taskDefinitionLocalService.deleteTaskDefinition(taskDefinition);
	}

	@Override
	public <T> T dslQuery(com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {
		return _taskDefinitionLocalService.dslQuery(dslQuery);
	}

	@Override
	public int dslQueryCount(
		com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {

		return _taskDefinitionLocalService.dslQueryCount(dslQuery);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _taskDefinitionLocalService.dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _taskDefinitionLocalService.dynamicQuery(dynamicQuery);
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
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _taskDefinitionLocalService.dynamicQuery(
			dynamicQuery, start, end);
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
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _taskDefinitionLocalService.dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _taskDefinitionLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return _taskDefinitionLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public com.liferay.generative.ai.task.model.TaskDefinition
		fetchTaskDefinition(long taskDefinitionId) {

		return _taskDefinitionLocalService.fetchTaskDefinition(
			taskDefinitionId);
	}

	@Override
	public com.liferay.generative.ai.task.model.TaskDefinition
		fetchTaskDefinitionByExternalReferenceCode(
			String externalReferenceCode, long companyId) {

		return _taskDefinitionLocalService.
			fetchTaskDefinitionByExternalReferenceCode(
				externalReferenceCode, companyId);
	}

	/**
	 * Returns the task definition with the matching UUID and company.
	 *
	 * @param uuid the task definition's UUID
	 * @param companyId the primary key of the company
	 * @return the matching task definition, or <code>null</code> if a matching task definition could not be found
	 */
	@Override
	public com.liferay.generative.ai.task.model.TaskDefinition
		fetchTaskDefinitionByUuidAndCompanyId(String uuid, long companyId) {

		return _taskDefinitionLocalService.
			fetchTaskDefinitionByUuidAndCompanyId(uuid, companyId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _taskDefinitionLocalService.getActionableDynamicQuery();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery
		getExportActionableDynamicQuery(
			com.liferay.exportimport.kernel.lar.PortletDataContext
				portletDataContext) {

		return _taskDefinitionLocalService.getExportActionableDynamicQuery(
			portletDataContext);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _taskDefinitionLocalService.getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _taskDefinitionLocalService.getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _taskDefinitionLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	 * Returns the task definition with the primary key.
	 *
	 * @param taskDefinitionId the primary key of the task definition
	 * @return the task definition
	 * @throws PortalException if a task definition with the primary key could not be found
	 */
	@Override
	public com.liferay.generative.ai.task.model.TaskDefinition
			getTaskDefinition(long taskDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _taskDefinitionLocalService.getTaskDefinition(taskDefinitionId);
	}

	@Override
	public com.liferay.generative.ai.task.model.TaskDefinition
			getTaskDefinitionByExternalReferenceCode(
				String externalReferenceCode, long companyId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _taskDefinitionLocalService.
			getTaskDefinitionByExternalReferenceCode(
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
	@Override
	public com.liferay.generative.ai.task.model.TaskDefinition
			getTaskDefinitionByUuidAndCompanyId(String uuid, long companyId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _taskDefinitionLocalService.getTaskDefinitionByUuidAndCompanyId(
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
	@Override
	public java.util.List<com.liferay.generative.ai.task.model.TaskDefinition>
		getTaskDefinitions(int start, int end) {

		return _taskDefinitionLocalService.getTaskDefinitions(start, end);
	}

	@Override
	public java.util.List<com.liferay.generative.ai.task.model.TaskDefinition>
		getTaskDefinitions(long companyId, boolean readOnly) {

		return _taskDefinitionLocalService.getTaskDefinitions(
			companyId, readOnly);
	}

	/**
	 * Returns the number of task definitions.
	 *
	 * @return the number of task definitions
	 */
	@Override
	public int getTaskDefinitionsCount() {
		return _taskDefinitionLocalService.getTaskDefinitionsCount();
	}

	@Override
	public int getTaskDefinitionsCount(long companyId, boolean readOnly) {
		return _taskDefinitionLocalService.getTaskDefinitionsCount(
			companyId, readOnly);
	}

	@Override
	public com.liferay.generative.ai.task.model.TaskDefinition updateStatus(
			long taskDefinitionId,
			com.liferay.portal.kernel.service.ServiceContext serviceContext,
			int status, long userId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _taskDefinitionLocalService.updateStatus(
			taskDefinitionId, serviceContext, status, userId);
	}

	@Override
	public com.liferay.generative.ai.task.model.TaskDefinition
			updateTaskDefinition(
				String configurationJSON,
				java.util.Map<java.util.Locale, String> descriptionMap,
				String externalReferenceCode, long taskDefinitionId,
				String schemaVersion,
				com.liferay.portal.kernel.service.ServiceContext serviceContext,
				java.util.Map<java.util.Locale, String> titleMap)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _taskDefinitionLocalService.updateTaskDefinition(
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
	@Override
	public com.liferay.generative.ai.task.model.TaskDefinition
		updateTaskDefinition(
			com.liferay.generative.ai.task.model.TaskDefinition
				taskDefinition) {

		return _taskDefinitionLocalService.updateTaskDefinition(taskDefinition);
	}

	@Override
	public BasePersistence<?> getBasePersistence() {
		return _taskDefinitionLocalService.getBasePersistence();
	}

	@Override
	public TaskDefinitionLocalService getWrappedService() {
		return _taskDefinitionLocalService;
	}

	@Override
	public void setWrappedService(
		TaskDefinitionLocalService taskDefinitionLocalService) {

		_taskDefinitionLocalService = taskDefinitionLocalService;
	}

	private TaskDefinitionLocalService _taskDefinitionLocalService;

}