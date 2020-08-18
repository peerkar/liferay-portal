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

package com.liferay.portal.search.tuning.gsearch.configuration.constants;

import com.liferay.portal.kernel.security.permission.ActionKeys;

/**
 * @author Petteri Karttunen
 */
public class SearchConfigurationActionKeys {

	public static final String ADD_CONFIGURATION = "ADD_CONFIGURATION";

	public static final String ADD_SNIPPET = "ADD_SNIPPET";

	public static final String ADD_TEMPLATE = "ADD_TEMPLATE";

	public static final String DELETE_CONFIGURATION = "DELETE_CONFIGURATION";

	public static final String DELETE_SNIPPET = "DELETE_SNIPPET";

	public static final String DELETE_TEMPLATE = "DELETE_TEMPLATE";

	public static final String UPDATE_CONFIGURATION = "UPDATE_CONFIGURATION";

	public static final String UPDATE_SNIPPET = "UPDATE_SNIPPET";

	public static final String UPDATE_TEMPLATE = "UPDATE_TEMPLATE";

	public static final String VIEW_CONFIGURATION = "VIEW_CONFIGURATION";

	public static final String VIEW_SNIPPET = "VIEW_SNIPPET";

	public static final String VIEW_TEMPLATE = "VIEW_TEMPLATE";

	public static String getActionKeyForSearchConfigurationType(
		int type, String action) {

		if (type == SearchConfigurationTypes.CONFIGURATION) {
			if (ActionKeys.VIEW.equals(action)) {
				return SearchConfigurationActionKeys.VIEW_CONFIGURATION;
			}
			else if (ActionKeys.ADD_ENTRY.equals(action)) {
				return SearchConfigurationActionKeys.ADD_CONFIGURATION;
			}
			else if (ActionKeys.UPDATE.equals(action)) {
				return SearchConfigurationActionKeys.UPDATE_CONFIGURATION;
			}
			else if (ActionKeys.DELETE.equals(action)) {
				return SearchConfigurationActionKeys.DELETE_CONFIGURATION;
			}
			else {
				return action;
			}
		}
		else if (type == SearchConfigurationTypes.SNIPPET) {
			if (ActionKeys.VIEW.equals(action)) {
				return SearchConfigurationActionKeys.VIEW_SNIPPET;
			}
			else if (ActionKeys.ADD_ENTRY.equals(action)) {
				return SearchConfigurationActionKeys.ADD_SNIPPET;
			}
			else if (ActionKeys.UPDATE.equals(action)) {
				return SearchConfigurationActionKeys.UPDATE_SNIPPET;
			}
			else if (ActionKeys.DELETE.equals(action)) {
				return SearchConfigurationActionKeys.DELETE_SNIPPET;
			}
			else {
				return action;
			}
		}
		else if (type == SearchConfigurationTypes.TEMPLATE) {
			if (ActionKeys.VIEW.equals(action)) {
				return SearchConfigurationActionKeys.VIEW_TEMPLATE;
			}
			else if (ActionKeys.ADD_ENTRY.equals(action)) {
				return SearchConfigurationActionKeys.ADD_TEMPLATE;
			}
			else if (ActionKeys.UPDATE.equals(action)) {
				return SearchConfigurationActionKeys.UPDATE_TEMPLATE;
			}
			else if (ActionKeys.DELETE.equals(action)) {
				return SearchConfigurationActionKeys.DELETE_TEMPLATE;
			}
			else {
				return action;
			}
		}

		throw new RuntimeException(
			"Search Configuration type " + type + " for action " + action +
				" not recognized.");
	}

}