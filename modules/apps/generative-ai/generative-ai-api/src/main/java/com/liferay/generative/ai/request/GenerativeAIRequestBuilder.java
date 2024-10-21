/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.request;

import com.liferay.generative.ai.task.task.Task;

import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Petteri Karttunen
 */
@ProviderType
public interface GenerativeAIRequestBuilder {

	public GenerativeAIRequest build();

	public GenerativeAIRequestBuilder debug(boolean debug);

	public GenerativeAIRequestBuilder input(Map<String, Object> input);

	public GenerativeAIRequestBuilder task(Task task);

}