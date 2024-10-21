/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.internal.request;

import com.liferay.generative.ai.request.GenerativeAIRequestBuilder;
import com.liferay.generative.ai.request.GenerativeAIRequestBuilderFactory;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(service = GenerativeAIRequestBuilderFactory.class)
public class GenerativeAIRequestBuilderFactoryImpl
	implements GenerativeAIRequestBuilderFactory {

	@Override
	public GenerativeAIRequestBuilder builder() {
		return new GenerativeAIRequestBuilderImpl();
	}

}