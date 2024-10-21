/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Petteri Karttunen
 */
@ExtendedObjectClassDefinition(
	category = "generative-ai",
	scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
	description = "generative-ai-task-configuration-description",
	id = "com.liferay.generative.ai.task.configuration.GenerativeAITaskConfiguration",
	localization = "content/Language",
	name = "generative-ai-task-configuration-name"
)
public interface GenerativeAITaskConfiguration {

	@Meta.AD(deflt = "604800", name = "task-cache-timeout", required = false)
	public int taskCacheTimeout();

}