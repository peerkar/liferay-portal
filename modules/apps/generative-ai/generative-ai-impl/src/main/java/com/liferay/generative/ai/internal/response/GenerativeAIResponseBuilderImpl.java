/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.internal.response;

import com.liferay.generative.ai.response.GenerativeAIResponse;
import com.liferay.generative.ai.response.GenerativeAIResponseBuilder;

import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(service = GenerativeAIResponseBuilder.class)
public class GenerativeAIResponseBuilderImpl
	implements GenerativeAIResponseBuilder {

	@Override
	public GenerativeAIResponse build() {
		return new GenerativeAIResponseImpl(_debugInfo, _output, _took);
	}

	@Override
	public GenerativeAIResponseBuilder debugInfo(
		Map<String, Map<String, Object>> debugInfo) {

		_debugInfo = debugInfo;

		return this;
	}

	@Override
	public GenerativeAIResponseBuilder output(Map<String, Object> output) {
		_output = output;

		return this;
	}

	@Override
	public GenerativeAIResponseBuilder took(String took) {
		_took = took;

		return this;
	}

	private Map<String, Map<String, Object>> _debugInfo;
	private Map<String, Object> _output;
	private String _took;

}