/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.internal.response;

import com.liferay.generative.ai.response.GenerativeAIResponse;

import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public class GenerativeAIResponseImpl implements GenerativeAIResponse {

	public GenerativeAIResponseImpl(
		Map<String, Map<String, Object>> debugInfo, Map<String, Object> output,
		String took) {

		_debugInfo = debugInfo;
		_output = output;
		_took = took;
	}

	@Override
	public Map<String, Map<String, Object>> getDebugInfo() {
		return _debugInfo;
	}

	@Override
	public Map<String, Object> getOutput() {
		return _output;
	}

	@Override
	public String getTook() {
		return _took;
	}

	private final Map<String, Map<String, Object>> _debugInfo;
	private final Map<String, Object> _output;
	private final String _took;

}