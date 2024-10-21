/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.response;

import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Petteri Karttunen
 */
@ProviderType
public interface GenerativeAIResponseBuilder {

	public GenerativeAIResponse build();

	public GenerativeAIResponseBuilder debugInfo(
		Map<String, Map<String, Object>> debugInfo);

	public GenerativeAIResponseBuilder output(Map<String, Object> output);

	public GenerativeAIResponseBuilder took(String took);

}