/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.task.tools;

import com.liferay.list.type.model.ListTypeDefinition;
import com.liferay.list.type.model.ListTypeEntry;
import com.liferay.list.type.service.ListTypeDefinitionLocalServiceUtil;
import com.liferay.list.type.service.ListTypeEntryLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.vulcan.util.LocalizedMapUtil;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Demo class for picklists operations

/**
 * @author Fabian Bouché
 */
public class PickListsAITools implements AITools {

	public PickListsAITools(JSONObject configurationJSONObject) {
		_configurationJSONObject = configurationJSONObject;
	}

	@Override
	public JSONObject getConfigurationJSONObject() {
		return _configurationJSONObject;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PickListsAITools.class);

	@Tool(
		"Creates an empty Liferay Picklist (hint: use this tool before starting to add entries to the Picklist). Returns the Picklist Id associated to the newly created Picklist. Returns -1 if it failed to create the object definition."
	)
	long createPicklist(
		@P(
			"The name of the Picklist to be created, it uses only letters, PascalCase"
		)
		String name,
		@P(
			"The External Reference Code of the Picklist to create, it uses only letters, SCREAMING_SNAKE_CASE"
		)
		String picklistERC) {

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		long userId = PrincipalThreadLocal.getUserId();

		Map<java.util.Locale, String> nameMap =
			LocalizedMapUtil.getLocalizedMap(name);
		boolean system = false;
		List<ListTypeEntry> listTypeEntries = Collections.emptyList();

		try {
			ListTypeDefinition listTypeDefinition =
				ListTypeDefinitionLocalServiceUtil.addListTypeDefinition(
					picklistERC, userId, nameMap, system, listTypeEntries);

			return listTypeDefinition.getListTypeDefinitionId();
		}
		catch (PortalException portalException) {
			_log.error("Failed to create pick list", portalException);
		}

		return -1;
	}

	@Tool(
		"Use this tool only if you know the Picklist's ID. Adds an entry a Liferay Picklist. Returns the Picklist Entry Id or -1 if it fails to create the Picklist entry."
	)
	long addPicklistEntryToPicklistDefinition(
		@P(
			"The Picklist ID of the Picklist, use the getPicklists tool if you don't know it"
		)
		long picklistId,
		@P(
			"The value of the Picklist entry to be created, it uses only letters, camelCase"
		)
		String picklistEntryName,
		@P("The label of the Picklist entry to be created, human readable")
			String picklistEntryLabel,
		@P(
			"The External Reference Code of the Picklist entry to be created, it uses only letters, SCREAMING_SNAKE_CASE"
		)
		String picklistEntryERC) {

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		long userId = PrincipalThreadLocal.getUserId();
		Map<java.util.Locale, String> nameMap =
			LocalizedMapUtil.getLocalizedMap(picklistEntryLabel);

		try {
			ListTypeEntry listTypeEntry =
				ListTypeEntryLocalServiceUtil.addListTypeEntry(
					picklistEntryERC, userId, picklistId, picklistEntryName,
					nameMap);

			return listTypeEntry.getListTypeEntryId();
		}
		catch (PortalException portalException) {
			_log.error("Failed to create picklist entry", portalException);
		}

		return -1;
	}

	@Tool("Returns the list of Liferay Picklists (Picklist Id: Label)")
	List<String> getPicklists() {
		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		return ListTypeDefinitionLocalServiceUtil.getListTypeDefinitions(
			-1, -1
		).stream(
		).filter(
			listTypeDefinition -> !listTypeDefinition.isSystem()
		).map(
			listTypeDefinition ->
				listTypeDefinition.getListTypeDefinitionId() + ": " +
					listTypeDefinition.getName(serviceContext.getLocale())
		).collect(
			Collectors.toList()
		);
	}

	private final JSONObject _configurationJSONObject;

}