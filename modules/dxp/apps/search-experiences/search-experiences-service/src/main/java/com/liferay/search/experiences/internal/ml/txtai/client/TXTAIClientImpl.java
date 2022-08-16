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

package com.liferay.search.experiences.internal.ml.txtai.client;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.search.experiences.internal.configuration.TXTAIConfiguration;
import com.liferay.search.experiences.internal.web.cache.TXTAITransformWebCacheItem;

import java.util.List;

/**
 * @author Petteri Karttunen
 */
public class TXTAIClientImpl implements TXTAIClient {

	public TXTAIClientImpl(TXTAIConfiguration txtaiConfiguration) {
		_txtAIConfiguration = txtaiConfiguration;
	}

	public Double[] getTextEmbedding(String text, boolean useCache)
		throws Exception {

		if (Validator.isBlank(text)) {
			return new Double[0];
		}

		JSONArray jsonArray;

		if (useCache) {
			jsonArray = TXTAITransformWebCacheItem.get(
				text, _txtAIConfiguration);
		}
		else {
			jsonArray = _getTextEmbeddingJSONArray(text);
		}

		if (jsonArray.length() == 0) {
			return new Double[0];
		}

		List<Double> list = JSONUtil.toDoubleList(jsonArray);

		return list.toArray(new Double[0]);
	}

	private JSONArray _getTextEmbeddingJSONArray(String text) throws Exception {
		String host = _txtAIConfiguration.host();

		if (!host.endsWith("/")) {
			host += "/";
		}

		String url = StringBundler.concat(host, "transform?text=", text);

		return JSONFactoryUtil.createJSONArray(HttpUtil.URLtoString(url));
	}

	private final TXTAIConfiguration _txtAIConfiguration;

}