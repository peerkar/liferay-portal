/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

 package com.liferay.generative.ai.rest.internal.resource.v1_0;

 import com.liferay.generative.ai.request.GenerativeAIRequestExecutor;
 import com.liferay.generative.ai.rest.dto.v1_0.TaskDefinition;
 import com.liferay.generative.ai.rest.internal.odata.entity.v1_0.TaskDefinitionEntityModel;
 import com.liferay.generative.ai.rest.internal.util.SearchUtil;
 import com.liferay.generative.ai.rest.internal.util.TitleMapUtil;
 import com.liferay.generative.ai.rest.resource.v1_0.TaskDefinitionResource;
 import com.liferay.generative.ai.task.constants.TaskDefinitionActionKeys;
 import com.liferay.generative.ai.task.constants.TaskDefinitionConstants;
 import com.liferay.generative.ai.task.exception.DuplicateTaskDefinitionExternalReferenceCodeException;
 import com.liferay.generative.ai.task.service.TaskDefinitionService;
 import com.liferay.generative.ai.task.task.TaskMemoryCleaner;
 import com.liferay.petra.string.StringBundler;
 import com.liferay.petra.string.StringPool;
 import com.liferay.portal.kernel.json.JSONFactory;
 import com.liferay.portal.kernel.json.JSONObject;
 import com.liferay.portal.kernel.json.JSONUtil;
 import com.liferay.portal.kernel.search.Field;
 import com.liferay.portal.kernel.search.Sort;
 import com.liferay.portal.kernel.search.filter.Filter;
 import com.liferay.portal.kernel.security.permission.ActionKeys;
 import com.liferay.portal.kernel.service.ServiceContextFactory;
 import com.liferay.portal.kernel.util.GetterUtil;
 import com.liferay.portal.kernel.util.HashMapBuilder;
 import com.liferay.portal.kernel.util.Validator;
 import com.liferay.portal.odata.entity.EntityModel;
 import com.liferay.portal.vulcan.dto.converter.DTOConverter;
 import com.liferay.portal.vulcan.dto.converter.DTOConverterRegistry;
 import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
 import com.liferay.portal.vulcan.pagination.Page;
 import com.liferay.portal.vulcan.pagination.Pagination;
 import com.liferay.portal.vulcan.util.LocalizedMapUtil;
 
 import java.util.Collections;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.Objects;
 
 import javax.ws.rs.core.MultivaluedMap;
 import javax.ws.rs.core.Response;
 
 import org.osgi.service.component.annotations.Component;
 import org.osgi.service.component.annotations.Reference;
 import org.osgi.service.component.annotations.ServiceScope;
 
 /**
  * @author Petteri Karttunen
  */
 @Component(
	 properties = "OSGI-INF/liferay/rest/v1_0/task-definition.properties",
	 scope = ServiceScope.PROTOTYPE, service = TaskDefinitionResource.class
 )
 public class TaskDefinitionResourceImpl extends BaseTaskDefinitionResourceImpl {
 
	 @Override
	 public void deleteTaskDefinition(Long taskDefinitionId) throws Exception {
		 _taskDefinitionService.deleteTaskDefinition(taskDefinitionId);
	 }
 
	 @Override
	 public EntityModel getEntityModel(MultivaluedMap multivaluedMap) {
		 return _entityEntityModel;
	 }
 
	 @Override
	 public TaskDefinition getTaskDefinition(Long taskDefinitionId)
		 throws Exception {
 
		 return _taskDefinitionDTOConverter.toDTO(
			 new DefaultDTOConverterContext(
				 contextAcceptLanguage.isAcceptAllLanguages(), new HashMap<>(),
				 _dtoConverterRegistry, contextHttpServletRequest,
				 taskDefinitionId, contextAcceptLanguage.getPreferredLocale(),
				 contextUriInfo, contextUser),
			 _taskDefinitionService.getTaskDefinition(taskDefinitionId));
	 }
 
	 @Override
	 public TaskDefinition getTaskDefinitionByExternalReferenceCode(
			 String externalReferenceCode)
		 throws Exception {
 
		 com.liferay.generative.ai.task.model.TaskDefinition taskDefinition =
			 _taskDefinitionService.getTaskDefinitionByExternalReferenceCode(
				 contextCompany.getCompanyId(), externalReferenceCode);
 
		 return _taskDefinitionDTOConverter.toDTO(
			 new DefaultDTOConverterContext(
				 contextAcceptLanguage.isAcceptAllLanguages(), new HashMap<>(),
				 _dtoConverterRegistry, contextHttpServletRequest,
				 taskDefinition.getTaskDefinitionId(),
				 contextAcceptLanguage.getPreferredLocale(), contextUriInfo,
				 contextUser),
			 taskDefinition);
	 }
 
	 @Override
	 public Response getTaskDefinitionExport(Long taskDefinitionId)
		 throws Exception {
 
		 com.liferay.generative.ai.task.model.TaskDefinition taskDefinition =
			 _taskDefinitionService.getTaskDefinition(taskDefinitionId);
 
		 return Response.ok(
		 ).encoding(
			 "UTF-8"
		 ).entity(
			 JSONUtil.put(
				 "configuration",
				 _jsonFactory.createJSONObject(
					 taskDefinition.getConfigurationJSON())
			 ).put(
				 "description_i18n",
				 _jsonFactory.createJSONObject(
					 _jsonFactory.looseSerialize(
						 taskDefinition.getDescriptionMap()))
			 ).put(
				 "externalReferenceCode",
				 taskDefinition.getExternalReferenceCode()
			 ).put(
				 "schemaVersion", taskDefinition.getSchemaVersion()
			 ).put(
				 "title_i18n",
				 _jsonFactory.createJSONObject(
					 _jsonFactory.looseSerialize(taskDefinition.getTitleMap()))
			 )
		 ).header(
			 "Content-Disposition",
			 StringBundler.concat(
				 "attachment; filename=\"",
				 taskDefinition.getTitle(
					 contextAcceptLanguage.getPreferredLocale(), true),
				 ".json\"")
		 ).build();
	 }
 
	 @Override
	 public Page<TaskDefinition> getTaskDefinitionsPage(
			 String search, Filter filter, Pagination pagination, Sort[] sorts)
		 throws Exception {
 
		 if (sorts == null) {
			 sorts = new Sort[] {
				 new Sort("modified_sortable", Sort.LONG_TYPE, true)
			 };
		 }
 
		 return SearchUtil.search(
			 Collections.emptyMap(),
			 booleanQuery -> SearchUtil.processTaskDefinitionSearchBooleanQuery(
				 contextAcceptLanguage, booleanQuery, search),
			 filter,
			 com.liferay.generative.ai.task.model.TaskDefinition.class.getName(),
			 search, pagination,
			 queryConfig -> queryConfig.setSelectedFieldNames(
				 Field.ENTRY_CLASS_PK),
			 searchContext -> {
				 searchContext.setCompanyId(contextCompany.getCompanyId());
 
				 if (!Validator.isBlank(search)) {
					 searchContext.setKeywords("");
				 }
			 },
			 sorts,
			 document -> {
				 long taskDefinitionId = GetterUtil.getLong(
					 document.get(Field.ENTRY_CLASS_PK));
 
				 TaskDefinition taskDefinition =
					 _taskDefinitionDTOConverter.toDTO(
						 new DefaultDTOConverterContext(
							 contextAcceptLanguage.isAcceptAllLanguages(),
							 new HashMap<>(), _dtoConverterRegistry,
							 contextHttpServletRequest,
							 document.get(Field.ENTRY_CLASS_PK),
							 contextAcceptLanguage.getPreferredLocale(),
							 contextUriInfo, contextUser),
						 _taskDefinitionService.getTaskDefinition(
							 taskDefinitionId));
 
				 String permissionName =
					 com.liferay.generative.ai.task.model.TaskDefinition.class.
						 getName();
 
				 taskDefinition.setActions(
					 () -> HashMapBuilder.put(
						 "create",
						 () -> addAction(
							 TaskDefinitionActionKeys.ADD_TASK_DEFINITION,
							 "postTaskDefinition",
							 TaskDefinitionConstants.RESOURCE_NAME,
							 contextCompany.getCompanyId())
					 ).put(
						 "delete",
						 () -> {
							 if (taskDefinition.getReadOnly()) {
								 return null;
							 }
 
							 return addAction(
								 ActionKeys.DELETE, "deleteTaskDefinition",
								 permissionName, taskDefinitionId);
						 }
					 ).put(
						 "get",
						 () -> addAction(
							 ActionKeys.VIEW, "getTaskDefinition",
							 permissionName, taskDefinitionId)
					 ).put(
						 "update",
						 () -> {
							 if (taskDefinition.getReadOnly()) {
								 return null;
							 }
 
							 return addAction(
								 ActionKeys.UPDATE, "putTaskDefinition",
								 permissionName, taskDefinitionId);
						 }
					 ).build());
 
				 return taskDefinition;
			 });
	 }
 
	 @Override
	 public TaskDefinition postTaskDefinition(TaskDefinition taskDefinition)
		 throws Exception {
 
		 return _taskDefinitionDTOConverter.toDTO(
			 new DefaultDTOConverterContext(
				 contextAcceptLanguage.isAcceptAllLanguages(), new HashMap<>(),
				 _dtoConverterRegistry, contextHttpServletRequest,
				 taskDefinition.getId(),
				 contextAcceptLanguage.getPreferredLocale(), contextUriInfo,
				 contextUser),
			 _taskDefinitionService.addTaskDefinition(
				 _getConfigurationJSON(taskDefinition),
				 LocalizedMapUtil.getLocalizedMap(
					 contextAcceptLanguage.getPreferredLocale(),
					 taskDefinition.getDescription(),
					 taskDefinition.getDescription_i18n()),
				 taskDefinition.getExternalReferenceCode(), false,
				 _getSchemaVersion(),
				 ServiceContextFactory.getInstance(contextHttpServletRequest),
				 LocalizedMapUtil.getLocalizedMap(
					 contextAcceptLanguage.getPreferredLocale(),
					 taskDefinition.getTitle(),
					 taskDefinition.getTitle_i18n())));
	 }
 
	 @Override
	 public TaskDefinition postTaskDefinitionCopy(Long taskDefinitionId)
		 throws Exception {
 
		 com.liferay.generative.ai.task.model.TaskDefinition taskDefinition =
			 _taskDefinitionService.getTaskDefinition(taskDefinitionId);
 
		 return _taskDefinitionDTOConverter.toDTO(
			 new DefaultDTOConverterContext(
				 contextAcceptLanguage.isAcceptAllLanguages(), new HashMap<>(),
				 _dtoConverterRegistry, contextHttpServletRequest,
				 taskDefinition.getTaskDefinitionId(),
				 contextAcceptLanguage.getPreferredLocale(), contextUriInfo,
				 contextUser),
			 _taskDefinitionService.addTaskDefinition(
				 taskDefinition.getConfigurationJSON(),
				 taskDefinition.getDescriptionMap(), null, false,
				 taskDefinition.getSchemaVersion(),
				 ServiceContextFactory.getInstance(contextHttpServletRequest),
				 TitleMapUtil.copy(taskDefinition.getTitleMap())));
	 }
 
	 @Override
	 public TaskDefinition postTaskDefinitionValidate(String json)
		 throws Exception {
 
		 TaskDefinition taskDefinition = TaskDefinition.unsafeToDTO(json);
 
		 _validateTaskDefinitionExternalReferenceCode(taskDefinition);
 
		 return taskDefinition;
	 }
 
	 @Override
	 public TaskDefinition putTaskDefinition(
			 Long taskDefinitionId, TaskDefinition taskDefinition)
		 throws Exception {
 
		 com.liferay.generative.ai.task.model.TaskDefinition
			 serviceBuilderTaskDefinition =
				 _taskDefinitionService.fetchTaskDefinition(taskDefinitionId);
 
		 if (serviceBuilderTaskDefinition == null) {
			 return postTaskDefinition(taskDefinition);
		 }
 
		 if (!serviceBuilderTaskDefinition.isReadOnly()) {
			 return _updateTaskDefinition(taskDefinitionId, taskDefinition);
		 }
 
		 return getTaskDefinition(
			 serviceBuilderTaskDefinition.getTaskDefinitionId());
	 }
 
	 @Override
	 public TaskDefinition putTaskDefinitionByExternalReferenceCode(
			 String externalReferenceCode, TaskDefinition taskDefinition)
		 throws Exception {
 
		 com.liferay.generative.ai.task.model.TaskDefinition
			 serviceBuilderTaskDefinition =
				 _taskDefinitionService.
					 fetchTaskDefinitionByExternalReferenceCode(
						 externalReferenceCode, contextCompany.getCompanyId());
 
		 taskDefinition.setExternalReferenceCode(() -> externalReferenceCode);
 
		 if (serviceBuilderTaskDefinition != null) {
			 return _updateTaskDefinition(
				 serviceBuilderTaskDefinition.getTaskDefinitionId(),
				 taskDefinition);
		 }
 
		 return postTaskDefinition(taskDefinition);
	 }
 
	 @Override
	 public void postTaskDefinitionByExternalReferenceCodeClear(
			 String externalReferenceCode)
		 throws Exception {
 
		 String memoryId = StringBundler.concat(
			 contextUser.getUserId(), StringPool.POUND,
			 externalReferenceCode);
 
		 _taskMemoryCleaner.clear(memoryId);
	 
	 }
 
	 private String _getConfigurationJSON(TaskDefinition taskDefinition) {
		 if (taskDefinition.getConfiguration() == null) {
			 return null;
		 }
 
		 JSONObject configurationJSONObject = _jsonFactory.createJSONObject(
			 (Map<?, ?>)taskDefinition.getConfiguration());
 
		 // TODO: fixme in the frontend (payload should not be in 'taskConfiguration')
 
		 if (!configurationJSONObject.has("taskConfiguration")) {
			 return configurationJSONObject.toString();
		 }
 
		 JSONObject taskConfigurationJSONObject =
			 configurationJSONObject.getJSONObject("taskConfiguration");
 
		 return String.valueOf(taskConfigurationJSONObject);
	 }
 
	 private String _getSchemaVersion() {
		 return "1.0";
	 }
 
	 private TaskDefinition _updateTaskDefinition(
			 Long taskDefinitionId, TaskDefinition taskDefinition)
		 throws Exception {
 
		 return _taskDefinitionDTOConverter.toDTO(
			 new DefaultDTOConverterContext(
				 contextAcceptLanguage.isAcceptAllLanguages(), new HashMap<>(),
				 _dtoConverterRegistry, contextHttpServletRequest,
				 taskDefinition.getId(),
				 contextAcceptLanguage.getPreferredLocale(), contextUriInfo,
				 contextUser),
			 _taskDefinitionService.updateTaskDefinition(
				 _getConfigurationJSON(taskDefinition),
				 LocalizedMapUtil.getLocalizedMap(
					 contextAcceptLanguage.getPreferredLocale(),
					 taskDefinition.getDescription(),
					 taskDefinition.getDescription_i18n()),
				 taskDefinition.getExternalReferenceCode(), taskDefinitionId,
				 _getSchemaVersion(),
				 ServiceContextFactory.getInstance(contextHttpServletRequest),
				 LocalizedMapUtil.getLocalizedMap(
					 contextAcceptLanguage.getPreferredLocale(),
					 taskDefinition.getTitle(),
					 taskDefinition.getTitle_i18n())));
	 }
 
	 private void _validateTaskDefinitionExternalReferenceCode(
			 TaskDefinition taskDefinition)
		 throws Exception {
 
		 if (Validator.isBlank(taskDefinition.getExternalReferenceCode())) {
			 return;
		 }
 
		 com.liferay.generative.ai.task.model.TaskDefinition
			 serviceBuilderTaskDefinition =
				 _taskDefinitionService.
					 fetchTaskDefinitionByExternalReferenceCode(
						 taskDefinition.getExternalReferenceCode(),
						 contextCompany.getCompanyId());
 
		 if ((serviceBuilderTaskDefinition != null) &&
			 !Objects.equals(
				 serviceBuilderTaskDefinition.getTaskDefinitionId(),
				 taskDefinition.getId())) {
 
			 throw new DuplicateTaskDefinitionExternalReferenceCodeException();
		 }
	 }
 
	 @Reference
	 private DTOConverterRegistry _dtoConverterRegistry;
 
	 private final TaskDefinitionEntityModel _entityEntityModel =
		 new TaskDefinitionEntityModel();
 
	 @Reference
	 private JSONFactory _jsonFactory;
 
	 @Reference(
		 target = "(component.name=com.liferay.generative.ai.rest.internal.dto.v1_0.converter.TaskDefinitionDTOConverter)"
	 )
	 private DTOConverter
		 <com.liferay.generative.ai.task.model.TaskDefinition, TaskDefinition>
			 _taskDefinitionDTOConverter;
 
	 @Reference
	 private TaskMemoryCleaner _taskMemoryCleaner;
 
	 @Reference
	 private TaskDefinitionService _taskDefinitionService;
 }