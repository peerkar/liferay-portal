/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.rest.internal.graphql.query.v1_0;

import com.liferay.generative.ai.rest.dto.v1_0.TaskDefinition;
import com.liferay.generative.ai.rest.resource.v1_0.TaskDefinitionResource;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLTypeExtension;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.util.Map;
import java.util.function.BiFunction;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.osgi.service.component.ComponentServiceObjects;

/**
 * @author Petteri Karttunen
 * @generated
 */
@Generated("")
public class Query {

	public static void setTaskDefinitionResourceComponentServiceObjects(
		ComponentServiceObjects<TaskDefinitionResource>
			taskDefinitionResourceComponentServiceObjects) {

		_taskDefinitionResourceComponentServiceObjects =
			taskDefinitionResourceComponentServiceObjects;
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {taskDefinitions(filter: ___, page: ___, pageSize: ___, search: ___, sorts: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public TaskDefinitionPage taskDefinitions(
			@GraphQLName("search") String search,
			@GraphQLName("filter") String filterString,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page,
			@GraphQLName("sort") String sortsString)
		throws Exception {

		return _applyComponentServiceObjects(
			_taskDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			taskDefinitionResource -> new TaskDefinitionPage(
				taskDefinitionResource.getTaskDefinitionsPage(
					search,
					_filterBiFunction.apply(
						taskDefinitionResource, filterString),
					Pagination.of(page, pageSize),
					_sortsBiFunction.apply(
						taskDefinitionResource, sortsString))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {taskDefinitionByExternalReferenceCode(externalReferenceCode: ___){actions, configuration, createDate, description, description_i18n, externalReferenceCode, id, modifiedDate, readOnly, schemaVersion, title, title_i18n, userName, version}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public TaskDefinition taskDefinitionByExternalReferenceCode(
			@GraphQLName("externalReferenceCode") String externalReferenceCode)
		throws Exception {

		return _applyComponentServiceObjects(
			_taskDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			taskDefinitionResource ->
				taskDefinitionResource.getTaskDefinitionByExternalReferenceCode(
					externalReferenceCode));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {taskDefinition(taskDefinitionId: ___){actions, configuration, createDate, description, description_i18n, externalReferenceCode, id, modifiedDate, readOnly, schemaVersion, title, title_i18n, userName, version}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public TaskDefinition taskDefinition(
			@GraphQLName("taskDefinitionId") Long taskDefinitionId)
		throws Exception {

		return _applyComponentServiceObjects(
			_taskDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			taskDefinitionResource -> taskDefinitionResource.getTaskDefinition(
				taskDefinitionId));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {taskDefinitionExport(taskDefinitionId: ___){}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public Response taskDefinitionExport(
			@GraphQLName("taskDefinitionId") Long taskDefinitionId)
		throws Exception {

		return _applyComponentServiceObjects(
			_taskDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			taskDefinitionResource ->
				taskDefinitionResource.getTaskDefinitionExport(
					taskDefinitionId));
	}

	@GraphQLTypeExtension(TaskDefinition.class)
	public class GetTaskDefinitionExportTypeExtension {

		public GetTaskDefinitionExportTypeExtension(
			TaskDefinition taskDefinition) {

			_taskDefinition = taskDefinition;
		}

		@GraphQLField
		public Response export() throws Exception {
			return _applyComponentServiceObjects(
				_taskDefinitionResourceComponentServiceObjects,
				Query.this::_populateResourceContext,
				taskDefinitionResource ->
					taskDefinitionResource.getTaskDefinitionExport(
						_taskDefinition.getId()));
		}

		private TaskDefinition _taskDefinition;

	}

	@GraphQLName("TaskDefinitionPage")
	public class TaskDefinitionPage {

		public TaskDefinitionPage(Page taskDefinitionPage) {
			actions = taskDefinitionPage.getActions();

			items = taskDefinitionPage.getItems();
			lastPage = taskDefinitionPage.getLastPage();
			page = taskDefinitionPage.getPage();
			pageSize = taskDefinitionPage.getPageSize();
			totalCount = taskDefinitionPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map<String, String>> actions;

		@GraphQLField
		protected java.util.Collection<TaskDefinition> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	private <T, R, E1 extends Throwable, E2 extends Throwable> R
			_applyComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeFunction<T, R, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			return unsafeFunction.apply(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private void _populateResourceContext(
			TaskDefinitionResource taskDefinitionResource)
		throws Exception {

		taskDefinitionResource.setContextAcceptLanguage(_acceptLanguage);
		taskDefinitionResource.setContextCompany(_company);
		taskDefinitionResource.setContextHttpServletRequest(
			_httpServletRequest);
		taskDefinitionResource.setContextHttpServletResponse(
			_httpServletResponse);
		taskDefinitionResource.setContextUriInfo(_uriInfo);
		taskDefinitionResource.setContextUser(_user);
		taskDefinitionResource.setGroupLocalService(_groupLocalService);
		taskDefinitionResource.setRoleLocalService(_roleLocalService);
	}

	private static ComponentServiceObjects<TaskDefinitionResource>
		_taskDefinitionResourceComponentServiceObjects;

	private AcceptLanguage _acceptLanguage;
	private com.liferay.portal.kernel.model.Company _company;
	private BiFunction<Object, String, Filter> _filterBiFunction;
	private GroupLocalService _groupLocalService;
	private HttpServletRequest _httpServletRequest;
	private HttpServletResponse _httpServletResponse;
	private RoleLocalService _roleLocalService;
	private BiFunction<Object, String, Sort[]> _sortsBiFunction;
	private UriInfo _uriInfo;
	private com.liferay.portal.kernel.model.User _user;

}