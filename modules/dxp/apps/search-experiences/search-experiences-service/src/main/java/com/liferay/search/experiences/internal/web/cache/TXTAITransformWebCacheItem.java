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

package com.liferay.search.experiences.internal.web.cache;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.servlet.URLEncoder;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.URLCodec;
import com.liferay.portal.kernel.webcache.WebCacheItem;
import com.liferay.portal.kernel.webcache.WebCachePoolUtil;
import com.liferay.search.experiences.blueprint.exception.InvalidWebCacheItemException;
import com.liferay.search.experiences.internal.configuration.TXTAIConfiguration;

import java.io.IOException;

/**
 * @author Petteri Karttunen
 */
public class TXTAITransformWebCacheItem implements WebCacheItem {

	public static JSONArray get(
			String text, TXTAIConfiguration txtAIConfiguration)
		throws Exception {

		if (!txtAIConfiguration.enabled()) {
			return JSONFactoryUtil.createJSONArray();
		}

		return (JSONArray)WebCachePoolUtil.get(
			StringBundler.concat(
				TXTAITransformWebCacheItem.class.getName(), StringPool.POUND,
				txtAIConfiguration.host(), StringPool.POUND, text),
			new TXTAITransformWebCacheItem(text, txtAIConfiguration));
	}

	public TXTAITransformWebCacheItem(
		String text, TXTAIConfiguration txtAIConfiguration) {

		_text = text;
		_txtAIConfiguration = txtAIConfiguration;
	}

	@Override
	public JSONArray convert(String key) {
		try {
			String host = _txtAIConfiguration.host();

			if (!host.endsWith("/")) {
				host += "/";
			}

			String url = StringBundler.concat(host, "transform?text=", URLCodec.encodeURL(_text, true));

			return JSONFactoryUtil.createJSONArray(HttpUtil.URLtoString(url));
		}
		catch (IOException | JSONException exception) {
			throw new InvalidWebCacheItemException(exception);
		}
	}

	@Override
	public long getRefreshTime() {
		if (_txtAIConfiguration.enabled()) {
			return _txtAIConfiguration.cacheTimeout();
		}

		return 0;
	}

	private final String _text;
	private final TXTAIConfiguration _txtAIConfiguration;

}