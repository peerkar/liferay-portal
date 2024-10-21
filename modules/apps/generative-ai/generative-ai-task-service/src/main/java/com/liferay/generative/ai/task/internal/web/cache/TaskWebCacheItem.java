/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.web.cache;

import com.liferay.generative.ai.task.configuration.GenerativeAITaskConfiguration;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.webcache.WebCacheItem;
import com.liferay.portal.kernel.webcache.WebCachePoolUtil;

import java.util.function.BiFunction;

/**
 * @author Petteri Karttunen
 */
public class TaskWebCacheItem implements WebCacheItem {

	public static Object get(
		String chatReference,
		GenerativeAITaskConfiguration generativeAITaskConfiguration,
		String input, BiFunction<String, String, Object> biFunction,
		String taskName) {

		try {
			return WebCachePoolUtil.get(
				StringBundler.concat(
					TaskWebCacheItem.class.getName(), StringPool.POUND, input,
					StringPool.POUND, taskName, StringPool.POUND,
					chatReference),
				new TaskWebCacheItem(
					biFunction, chatReference, generativeAITaskConfiguration,
					input));
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}

			return null;
		}
	}

	public TaskWebCacheItem(
		BiFunction<String, String, Object> biFunction, String chatReference,
		GenerativeAITaskConfiguration generativeAITaskConfiguration,
		String input) {

		_biFunction = biFunction;
		_chatReference = chatReference;
		_generativeAITaskConfiguration = generativeAITaskConfiguration;
		_input = input;
	}

	@Override
	public Object convert(String key) {
		try {
			return _biFunction.apply(_chatReference, _input);
		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	@Override
	public long getRefreshTime() {
		return _generativeAITaskConfiguration.taskCacheTimeout();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		TaskWebCacheItem.class);

	private final BiFunction<String, String, Object> _biFunction;
	private final String _chatReference;
	private final GenerativeAITaskConfiguration _generativeAITaskConfiguration;
	private final String _input;

}