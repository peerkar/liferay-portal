/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.task.tools;

import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.constants.ObjectRelationshipConstants;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.model.ObjectField;
import com.liferay.object.model.ObjectFieldSetting;
import com.liferay.object.model.ObjectRelationship;
import com.liferay.object.service.ObjectDefinitionLocalServiceUtil;
import com.liferay.object.service.ObjectEntryLocalServiceUtil;
import com.liferay.object.service.ObjectFieldLocalServiceUtil;
import com.liferay.object.service.ObjectRelationshipLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.vulcan.util.LocalizedMapUtil;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import java.io.Serializable;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Demo class for object operations

/**
 * @author Fabian Bouché
 */
public class ObjectsAITools implements AITools {

	public ObjectsAITools(JSONObject configurationJSONObject) {
		_configurationJSONObject = configurationJSONObject;
	}

	@Override
	public JSONObject getConfigurationJSONObject() {
		return _configurationJSONObject;
	}

	private static final Log _log = LogFactoryUtil.getLog(ObjectsAITools.class);

	@Tool(
		"Creates an empty Liferay Object definition draft (hint: use this tool before starting to add fields to the object definition). Returns the Object Definition Id associated to the newly created Object Definition. Returns -1 if it failed to create the object definition."
	)
	long createObjectDefinitionDraft(
		@P(
			"The name of the Object Definition to be created, it uses only letters, PascalCase"
		)
		String name,
		@P(
			"The label of the Object Definition to be created, human readable, it should be singular"
		)
		String label,
		@P("The plural of the label of the Object Definition to be created")
			String pluralLabel,
		@P(
			"The External Reference Code of the Object Definition to create, it uses only letters, SCREAMING_SNAKE_CASE"
		)
		String objectDefinitionERC) {

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		long userId = PrincipalThreadLocal.getUserId();
		long objectFolderId = 0;
		boolean enableComments = false;
		boolean enableIndexSearch = true;
		boolean enableLocalization = false;
		boolean enableObjectEntryDraft = false;
		Map<java.util.Locale, String> labelMap = new HashMap<>();

		labelMap.put(serviceContext.getLocale(), label);
		String panelAppOrder = null;
		String panelCategoryKey = null;
		Map<java.util.Locale, String> pluralLabelMap = new HashMap<>();

		pluralLabelMap.put(serviceContext.getLocale(), pluralLabel);
		boolean portlet = true;
		String scope = ObjectDefinitionConstants.SCOPE_COMPANY;
		String storageType = ObjectDefinitionConstants.STORAGE_TYPE_DEFAULT;
		List<ObjectField> objectFields = Collections.emptyList();

		try {
			ObjectDefinition objectDefinition =
				ObjectDefinitionLocalServiceUtil.addCustomObjectDefinition(
					userId, objectFolderId, enableComments, enableIndexSearch,
					enableLocalization, enableObjectEntryDraft, labelMap, name,
					panelAppOrder, panelCategoryKey, pluralLabelMap, portlet,
					scope, storageType, objectFields);

			ObjectDefinitionLocalServiceUtil.updateExternalReferenceCode(
				objectDefinition.getObjectDefinitionId(), objectDefinitionERC);

			return objectDefinition.getObjectDefinitionId();
		}
		catch (PortalException portalException) {
			_log.error("Failed to create object definition", portalException);
		}

		return -1;
	}

	@Tool(
		"Use this tool only if you know the Object Definition's ID. Adds a date field to a Liferay Object definition. Returns the Object Field Id or -1 if it fails to create the object field."
	)
	long addDateFieldToObjectDefinition(
		@P(
			"The Object Definition ID of the Object Definition, use the getObjectDefinitions tool if you don't know it"
		)
		long objectDefinitionId,
		@P(
			"The key of the Field to be created, it uses only letters, camelCase"
		)
		String fieldName,
		@P("The label of the Field to be created, human readable") String
			fieldLabel,
		@P(
			"The External Reference Code of the Field to be created, it uses only letters, SCREAMING_SNAKE_CASE"
		)
		String fieldERC,
		@P("Should the field be required or not") boolean required) {

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		long userId = PrincipalThreadLocal.getUserId();
		long listTypeDefinitionId = 0;
		String businessType = ObjectFieldConstants.BUSINESS_TYPE_DATE;
		String dbType = ObjectFieldConstants.DB_TYPE_DATE;
		boolean indexed = true;
		boolean indexedAsKeyword = false;
		String indexedLanguageId = null;
		Map<java.util.Locale, String> labelMap =
			LocalizedMapUtil.getLocalizedMap(fieldLabel);
		boolean localized = false;
		String readOnly = ObjectFieldConstants.READ_ONLY_FALSE;
		String readOnlyConditionExpression = null;
		boolean state = false;
		List<ObjectFieldSetting> objectFieldSettings = Collections.emptyList();

		try {
			ObjectField objectField =
				ObjectFieldLocalServiceUtil.addCustomObjectField(
					fieldERC, userId, listTypeDefinitionId, objectDefinitionId,
					businessType, dbType, indexed, indexedAsKeyword,
					indexedLanguageId, labelMap, localized, fieldName, readOnly,
					readOnlyConditionExpression, required, state,
					objectFieldSettings);

			return objectField.getObjectFieldId();
		}
		catch (PortalException portalException) {
			_log.error("Failed to create object field", portalException);
		}

		return -1;
	}

	@Tool(
		"Use this tool only if you know the Object Definition's ID. Adds a text field to a Liferay Object definition. Returns the Object Field Id or -1 if it fails to create the object field."
	)
	long addTextFieldToObjectDefinition(
		@P(
			"The Object Definition ID of the Object Definition, use the getObjectDefinitions tool if you don't know it"
		)
		long objectDefinitionId,
		@P(
			"The key of the Field to be created, it uses only letters, camelCase"
		)
		String fieldName,
		@P("The label of the Field to be created, human readable") String
			fieldLabel,
		@P(
			"The External Reference Code of the Field to be created, it uses only letters, SCREAMING_SNAKE_CASE"
		)
		String fieldERC,
		@P("Should the field be required or not") boolean required) {

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		long userId = PrincipalThreadLocal.getUserId();
		long listTypeDefinitionId = 0;
		String businessType = ObjectFieldConstants.BUSINESS_TYPE_TEXT;
		String dbType = ObjectFieldConstants.DB_TYPE_STRING;
		boolean indexed = true;
		boolean indexedAsKeyword = false;
		String indexedLanguageId = serviceContext.getLanguageId();
		Map<java.util.Locale, String> labelMap =
			LocalizedMapUtil.getLocalizedMap(fieldLabel);
		boolean localized = false;
		String readOnly = ObjectFieldConstants.READ_ONLY_FALSE;
		String readOnlyConditionExpression = null;
		boolean state = false;
		List<ObjectFieldSetting> objectFieldSettings = Collections.emptyList();

		try {
			ObjectField objectField =
				ObjectFieldLocalServiceUtil.addCustomObjectField(
					fieldERC, userId, listTypeDefinitionId, objectDefinitionId,
					businessType, dbType, indexed, indexedAsKeyword,
					indexedLanguageId, labelMap, localized, fieldName, readOnly,
					readOnlyConditionExpression, required, state,
					objectFieldSettings);

			return objectField.getObjectFieldId();
		}
		catch (PortalException portalException) {
			_log.error("Failed to create object field", portalException);
		}

		return -1;
	}

	@Tool(
		"Use this tool only if you know the Object Definition's ID. Adds an integer field to a Liferay Object definition. Returns the Object Field Id or -1 if it fails to create the object field."
	)
	long addIntegerFieldToObjectDefinition(
		@P(
			"The Object Definition ID of the Object Definition, use the getObjectDefinitions tool if you don't know it"
		)
		long objectDefinitionId,
		@P(
			"The key of the Field to be created, it uses only letters, camelCase"
		)
		String fieldName,
		@P("The label of the Field to be created, human readable") String
			fieldLabel,
		@P(
			"The External Reference Code of the Field to be created, it uses only letters, SCREAMING_SNAKE_CASE"
		)
		String fieldERC,
		@P("Should the field be required or not") boolean required) {

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		long userId = PrincipalThreadLocal.getUserId();
		long listTypeDefinitionId = 0;
		String businessType = ObjectFieldConstants.BUSINESS_TYPE_INTEGER;
		String dbType = ObjectFieldConstants.DB_TYPE_INTEGER;
		boolean indexed = true;
		boolean indexedAsKeyword = false;
		String indexedLanguageId = null;
		Map<java.util.Locale, String> labelMap =
			LocalizedMapUtil.getLocalizedMap(fieldLabel);
		boolean localized = false;
		String readOnly = ObjectFieldConstants.READ_ONLY_FALSE;
		String readOnlyConditionExpression = null;
		boolean state = false;
		List<ObjectFieldSetting> objectFieldSettings = Collections.emptyList();

		try {
			ObjectField objectField =
				ObjectFieldLocalServiceUtil.addCustomObjectField(
					fieldERC, userId, listTypeDefinitionId, objectDefinitionId,
					businessType, dbType, indexed, indexedAsKeyword,
					indexedLanguageId, labelMap, localized, fieldName, readOnly,
					readOnlyConditionExpression, required, state,
					objectFieldSettings);

			return objectField.getObjectFieldId();
		}
		catch (PortalException portalException) {
			_log.error("Failed to create object field", portalException);
		}

		return -1;
	}

	@Tool(
		"Use this tool only if you know the Object Definition's ID. Adds an picklist field to a Liferay Object definition (enumeration of values). Returns the Object Field Id or -1 if it fails to create the object field."
	)
	long addPicklistFieldToObjectDefinition(
		@P(
			"The Object Definition ID of the Object Definition, use the getObjectDefinitions tool if you don't know it"
		)
		long objectDefinitionId,
		@P(
			"The key of the Field to be created, it uses only letters, camelCase"
		)
		String fieldName,
		@P("The label of the Field to be created, human readable") String
			fieldLabel,
		@P(
			"The External Reference Code of the Field to be created, it uses only letters, SCREAMING_SNAKE_CASE"
		)
		String fieldERC,
		@P("The Picklist ID of the Picklist enumerating the authorized values")
			long picklistId,
		@P("Should the field be required or not") boolean required) {

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		long userId = PrincipalThreadLocal.getUserId();
		long listTypeDefinitionId = picklistId;
		String businessType = ObjectFieldConstants.BUSINESS_TYPE_PICKLIST;
		String dbType = ObjectFieldConstants.DB_TYPE_STRING;
		boolean indexed = true;
		boolean indexedAsKeyword = true;
		String indexedLanguageId = null;
		Map<java.util.Locale, String> labelMap =
			LocalizedMapUtil.getLocalizedMap(fieldLabel);
		boolean localized = false;
		String readOnly = ObjectFieldConstants.READ_ONLY_FALSE;
		String readOnlyConditionExpression = null;
		boolean state = false;
		List<ObjectFieldSetting> objectFieldSettings = Collections.emptyList();

		try {
			ObjectField objectField =
				ObjectFieldLocalServiceUtil.addCustomObjectField(
					fieldERC, userId, listTypeDefinitionId, objectDefinitionId,
					businessType, dbType, indexed, indexedAsKeyword,
					indexedLanguageId, labelMap, localized, fieldName, readOnly,
					readOnlyConditionExpression, required, state,
					objectFieldSettings);

			return objectField.getObjectFieldId();
		}
		catch (PortalException portalException) {
			_log.error("Failed to create object field", portalException);
		}

		return -1;
	}

	@Tool(
		"Returns the list of Liferay Object definitions (Object definition Id: Label - Status)"
	)
	List<String> getObjectDefinitions() {
		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		return ObjectDefinitionLocalServiceUtil.getObjectDefinitions(
			-1, -1
		).stream(
		).map(
			objectDefinition ->
				objectDefinition.getLabel(serviceContext.getLocale()) + ": " +
					objectDefinition.getObjectDefinitionId() + " - " +
						(objectDefinition.getStatus() == 0 ? "published" :
							"draft")
		).collect(
			Collectors.toList()
		);
	}

	@Tool(
		"Use this tool only if you know the Object Definition's ID. Returns the list of Object Fields for a given Object Definition (Field key: Label; Type)"
	)
	List<String> getObjectFields(
		@P(
			"The Object Definition ID, use the getObjectDefinitions tool if you don't know it"
		)
		long objectDefinitionId) {

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		return ObjectFieldLocalServiceUtil.getObjectFields(
			objectDefinitionId
		).stream(
		).map(
			objectField ->
				objectField.getName() + ": " +
					objectField.getLabel(serviceContext.getLocale()) + "; " +
						objectField.getBusinessType()
		).collect(
			Collectors.toList()
		);
	}

	@Tool(
		"Use this tool only if you know the Object Definition's ID. Publish the Object Definition. This is required before users can start to interact with it."
	)
	void publishObjectDefinition(
			@P(
				"The Object Definition ID, use the getObjectDefinitions tool if you don't know it"
			)
			long objectDefinitionId)
		throws PortalException {

		long userId = PrincipalThreadLocal.getUserId();

		ObjectDefinitionLocalServiceUtil.publishCustomObjectDefinition(
			userId, objectDefinitionId);
	}

	@Tool(
		"Adds a new Object Entry (a record) to an existing Object Definition. Each record you pass should include all the required fields. Returns the Object Entry ID or -1 if it failed."
	)
	long addObjectEntry(
		@P(
			"The Object Definition ID of the Object Definition we should add an Object Entry to, use the getObjectDefinitions tool if you don't know it"
		)
		long objectDefinitionId,
		@P(
			"A map of values tuples (pass all the values as String), with one tuple for each Object Field you'd like to insert, eg: [{projectName: 'Appolo X'}, {projectDate: '1967-07-31'}, {projectDescription: 'A secret attempt to go to Mars'}], always using yyyy-MM-dd format for dates"
		)
		Map
			<String, String> stringValues) {

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		long userId = PrincipalThreadLocal.getUserId();

		Map<String, Serializable> values = new HashMap<>();

		try {
			for (Map.Entry<String, String> entry : stringValues.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				ObjectField field = ObjectFieldLocalServiceUtil.getObjectField(
					objectDefinitionId, key);

				if (ObjectFieldConstants.BUSINESS_TYPE_INTEGER.equals(
						field.getBusinessType())) {

					values.put(key, Integer.valueOf(value));
				}
				else if (ObjectFieldConstants.BUSINESS_TYPE_TEXT.equals(
							field.getBusinessType())) {

					values.put(key, value);
				}
				else if (ObjectFieldConstants.BUSINESS_TYPE_PICKLIST.equals(
							field.getBusinessType())) {

					values.put(key, value);
				}
				else if (ObjectFieldConstants.BUSINESS_TYPE_DATE.equals(
							field.getBusinessType())) {

					try {
						Date dateValue = DateUtil.parseDate(
							"yyyy-MM-dd", value, serviceContext.getLocale());

						values.put(key, dateValue);
					}
					catch (java.text.ParseException parseException) {
						_log.error(
							"Failed to parse " + value + " " + key,
							parseException);

						return -1;
					}
				}
			}

			ObjectEntry objectEntry =
				ObjectEntryLocalServiceUtil.addObjectEntry(
					userId, 0, objectDefinitionId, values, serviceContext);

			return objectEntry.getObjectEntryId();
		}
		catch (PortalException portalException) {
			_log.error("Failed to create object entry", portalException);
		}

		return -1;
	}

	@Tool(
		"Use this tool only if you know the Object Definitions' IDs. Create a one to many relationship between two object definitions. One Object Entry A is bound to many Object Entries B. Use this tool if you need a one to one relationship, using Object Definition A as the most important / central object in the relationship."
	)
	long createOneToManyObjectDefinitionRelationship(
			@P(
				"The Object Definition ID of object definition A, use the getObjectDefinitions tool if you don't know it"
			)
			long objectDefinitionAId,
			@P(
				"The Object Definition ID of object definition B, use the getObjectDefinitions tool if you don't know it"
			)
			long objectDefinitionBId,
			@P(
				"The key for the relationship to be created, it uses only letters, camelCase"
			)
			String relationshipName,
			@P("A label to designate the relationship") String label,
			@P(
				"The External Reference Code of the Relationship to be created, it uses only letters, SCREAMING_SNAKE_CASE"
			)
			String relationshipERC)
		throws PortalException {

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		long userId = PrincipalThreadLocal.getUserId();

		long parameterObjectFieldId = 0;
		String deletionType =
			ObjectRelationshipConstants.DELETION_TYPE_DISASSOCIATE;
		Map<java.util.Locale, String> labelMap =
			LocalizedMapUtil.getLocalizedMap(label);
		String name = relationshipName;
		boolean system = false;
		String type = ObjectRelationshipConstants.TYPE_ONE_TO_MANY;
		ObjectField objectField = null;

		try {
			ObjectRelationship relationship =
				ObjectRelationshipLocalServiceUtil.addObjectRelationship(
					relationshipERC, userId, objectDefinitionAId,
					objectDefinitionBId, parameterObjectFieldId, deletionType,
					labelMap, name, system, type, objectField);

			return relationship.getObjectRelationshipId();
		}
		catch (PortalException portalException) {
			_log.error("Failed to create object relationship", portalException);
		}

		return -1;
	}

	@Tool(
		"Use this tool only if you know the Object Definitions' IDs. Create a many to many relationship between two object definitions. Many Object Entries A are bound to many Object Entries B."
	)
	long createManyToManyObjectDefinitionRelationship(
			@P(
				"The Object Definition ID of object definition A, use the getObjectDefinitions tool if you don't know it"
			)
			long objectDefinitionAId,
			@P(
				"The Object Definition ID of object definition B, use the getObjectDefinitions tool if you don't know it"
			)
			long objectDefinitionBId,
			@P(
				"The key for the relationship to be created, it uses only letters, camelCase"
			)
			String relationshipName,
			@P("A label to designate the relationship") String label,
			@P(
				"The External Reference Code of the Relationship to be created, it uses only letters, SCREAMING_SNAKE_CASE"
			)
			String relationshipERC)
		throws PortalException {

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		long userId = PrincipalThreadLocal.getUserId();

		long parameterObjectFieldId = 0;
		String deletionType =
			ObjectRelationshipConstants.DELETION_TYPE_DISASSOCIATE;
		Map<java.util.Locale, String> labelMap =
			LocalizedMapUtil.getLocalizedMap(label);
		String name = relationshipName;
		boolean system = false;
		String type = ObjectRelationshipConstants.TYPE_MANY_TO_MANY;
		ObjectField objectField = null;

		try {
			ObjectRelationship relationship =
				ObjectRelationshipLocalServiceUtil.addObjectRelationship(
					relationshipERC, userId, objectDefinitionAId,
					objectDefinitionBId, parameterObjectFieldId, deletionType,
					labelMap, name, system, type, objectField);

			return relationship.getObjectRelationshipId();
		}
		catch (PortalException portalException) {
			_log.error("Failed to create object relationship", portalException);
		}

		return -1;
	}

	private final JSONObject _configurationJSONObject;

}