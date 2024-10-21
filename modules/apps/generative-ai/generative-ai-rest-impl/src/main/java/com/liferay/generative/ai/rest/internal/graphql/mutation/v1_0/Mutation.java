/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.rest.internal.graphql.mutation.v1_0;

import com.liferay.generative.ai.rest.dto.v1_0.GenerativeAIRequest;
import com.liferay.generative.ai.rest.dto.v1_0.GenerativeAIResponse;
import com.liferay.generative.ai.rest.dto.v1_0.TaskDefinition;
import com.liferay.generative.ai.rest.resource.v1_0.GenerativeAIResponseResource;
import com.liferay.generative.ai.rest.resource.v1_0.TaskDefinitionResource;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.batch.engine.resource.VulcanBatchEngineExportTaskResource;
import com.liferay.portal.vulcan.batch.engine.resource.VulcanBatchEngineImportTaskResource;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;

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
public class Mutation {

	public static void setGenerativeAIResponseResourceComponentServiceObjects(
		ComponentServiceObjects<GenerativeAIResponseResource>
			generativeAIResponseResourceComponentServiceObjects) {

		_generativeAIResponseResourceComponentServiceObjects =
			generativeAIResponseResourceComponentServiceObjects;
	}

	public static void setTaskDefinitionResourceComponentServiceObjects(
		ComponentServiceObjects<TaskDefinitionResource>
			taskDefinitionResourceComponentServiceObjects) {

		_taskDefinitionResourceComponentServiceObjects =
			taskDefinitionResourceComponentServiceObjects;
	}

	@GraphQLField(description = "TBD")
	public GenerativeAIResponse createGenerateExternalReferenceCode(
			@GraphQLName("externalReferenceCode") String externalReferenceCode,
			@GraphQLName("generativeAIRequest") GenerativeAIRequest
				generativeAIRequest)
		throws Exception {

		return _applyComponentServiceObjects(
			_generativeAIResponseResourceComponentServiceObjects,
			this::_populateResourceContext,
			generativeAIResponseResource ->
				generativeAIResponseResource.postGenerateExternalReferenceCode(
					externalReferenceCode, generativeAIRequest));
	}

	@GraphQLField
	public Response createTaskDefinitionsPageExportBatch(
			@GraphQLName("search") String search,
			@GraphQLName("filter") String filterString,
			@GraphQLName("sort") String sortsString,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("contentType") String contentType,
			@GraphQLName("fieldNames") String fieldNames)
		throws Exception {

		return _applyComponentServiceObjects(
			_taskDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			taskDefinitionResource ->
				taskDefinitionResource.postTaskDefinitionsPageExportBatch(
					search,
					_filterBiFunction.apply(
						taskDefinitionResource, filterString),
					_sortsBiFunction.apply(taskDefinitionResource, sortsString),
					callbackURL, contentType, fieldNames));
	}

	@GraphQLField
	public TaskDefinition createTaskDefinition(
			@GraphQLName("taskDefinition") TaskDefinition taskDefinition)
		throws Exception {

		return _applyComponentServiceObjects(
			_taskDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			taskDefinitionResource -> taskDefinitionResource.postTaskDefinition(
				taskDefinition));
	}

	@GraphQLField
	public Response createTaskDefinitionBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_taskDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			taskDefinitionResource ->
				taskDefinitionResource.postTaskDefinitionBatch(
					callbackURL, object));
	}

	@GraphQLField
	public TaskDefinition updateTaskDefinitionByExternalReferenceCode(
			@GraphQLName("externalReferenceCode") String externalReferenceCode,
			@GraphQLName("taskDefinition") TaskDefinition taskDefinition)
		throws Exception {

		return _applyComponentServiceObjects(
			_taskDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			taskDefinitionResource ->
				taskDefinitionResource.putTaskDefinitionByExternalReferenceCode(
					externalReferenceCode, taskDefinition));
	}

	@GraphQLField
	public boolean createTaskDefinitionByExternalReferenceCodeClear(
			@GraphQLName("externalReferenceCode") String externalReferenceCode)
		throws Exception {

		_applyVoidComponentServiceObjects(
			_taskDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			taskDefinitionResource ->
				taskDefinitionResource.
					postTaskDefinitionByExternalReferenceCodeClear(
						externalReferenceCode));

		return true;
	}

	@GraphQLField
	public TaskDefinition createTaskDefinitionValidate(
			@GraphQLName("string") String string)
		throws Exception {

		return _applyComponentServiceObjects(
			_taskDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			taskDefinitionResource ->
				taskDefinitionResource.postTaskDefinitionValidate(string));
	}

	@GraphQLField
	public boolean deleteTaskDefinition(
			@GraphQLName("taskDefinitionId") Long taskDefinitionId)
		throws Exception {

		_applyVoidComponentServiceObjects(
			_taskDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			taskDefinitionResource ->
				taskDefinitionResource.deleteTaskDefinition(taskDefinitionId));

		return true;
	}

	@GraphQLField
	public Response deleteTaskDefinitionBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_taskDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			taskDefinitionResource ->
				taskDefinitionResource.deleteTaskDefinitionBatch(
					callbackURL, object));
	}

	@GraphQLField
	public TaskDefinition patchTaskDefinition(
			@GraphQLName("taskDefinitionId") Long taskDefinitionId,
			@GraphQLName("taskDefinition") TaskDefinition taskDefinition)
		throws Exception {

		return _applyComponentServiceObjects(
			_taskDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			taskDefinitionResource ->
				taskDefinitionResource.patchTaskDefinition(
					taskDefinitionId, taskDefinition));
	}

	@GraphQLField
	public TaskDefinition updateTaskDefinition(
			@GraphQLName("taskDefinitionId") Long taskDefinitionId,
			@GraphQLName("taskDefinition") TaskDefinition taskDefinition)
		throws Exception {

		return _applyComponentServiceObjects(
			_taskDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			taskDefinitionResource -> taskDefinitionResource.putTaskDefinition(
				taskDefinitionId, taskDefinition));
	}

	@GraphQLField
	public Response updateTaskDefinitionBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_taskDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			taskDefinitionResource ->
				taskDefinitionResource.putTaskDefinitionBatch(
					callbackURL, object));
	}

	@GraphQLField
	public TaskDefinition createTaskDefinitionCopy(
			@GraphQLName("taskDefinitionId") Long taskDefinitionId)
		throws Exception {

		return _applyComponentServiceObjects(
			_taskDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			taskDefinitionResource ->
				taskDefinitionResource.postTaskDefinitionCopy(
					taskDefinitionId));
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

	private <T, E1 extends Throwable, E2 extends Throwable> void
			_applyVoidComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeConsumer<T, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			unsafeFunction.accept(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private void _populateResourceContext(
			GenerativeAIResponseResource generativeAIResponseResource)
		throws Exception {

		generativeAIResponseResource.setContextAcceptLanguage(_acceptLanguage);
		generativeAIResponseResource.setContextCompany(_company);
		generativeAIResponseResource.setContextHttpServletRequest(
			_httpServletRequest);
		generativeAIResponseResource.setContextHttpServletResponse(
			_httpServletResponse);
		generativeAIResponseResource.setContextUriInfo(_uriInfo);
		generativeAIResponseResource.setContextUser(_user);
		generativeAIResponseResource.setGroupLocalService(_groupLocalService);
		generativeAIResponseResource.setRoleLocalService(_roleLocalService);
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

		taskDefinitionResource.setVulcanBatchEngineExportTaskResource(
			_vulcanBatchEngineExportTaskResource);

		taskDefinitionResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private static ComponentServiceObjects<GenerativeAIResponseResource>
		_generativeAIResponseResourceComponentServiceObjects;
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
	private VulcanBatchEngineExportTaskResource
		_vulcanBatchEngineExportTaskResource;
	private VulcanBatchEngineImportTaskResource
		_vulcanBatchEngineImportTaskResource;

}