/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.internal.response;

import com.liferay.generative.ai.response.GenerativeAIResponseBuilder;
import com.liferay.generative.ai.response.GenerativeAIResponseBuilderFactory;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(service = GenerativeAIResponseBuilderFactory.class)
public class GenerativeAIResponseBuilderFactoryImpl
	implements GenerativeAIResponseBuilderFactory {

	@Override
	public GenerativeAIResponseBuilder builder() {
		return new GenerativeAIResponseBuilderImpl();
	}

}