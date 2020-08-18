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

package com.liferay.portal.search.tuning.gsearch.impl.internal.query.postprocessor;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.engine.adapter.search.SearchSearchResponse;
import com.liferay.portal.search.hits.SearchHits;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.KeywordIndexingConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.impl.util.GSearchJsonUtil;
import com.liferay.portal.search.tuning.gsearch.spi.query.postprocessor.QueryPostProcessor;

import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, service = QueryPostProcessor.class)
public class KeywordIndexerPostProcessor implements QueryPostProcessor {

	@Override
	public boolean process(
		SearchRequestContext searchRequestContext,
		SearchSearchResponse searchResponse) {

		Optional<JSONObject> configurationJsonObjectOptional =
			searchRequestContext.getKeywordIndexingConfiguration();

		if (!configurationJsonObjectOptional.isPresent()) {
			return true;
		}

		JSONObject configurationJsonObject =
			configurationJsonObjectOptional.get();

		boolean enabled = configurationJsonObject.getBoolean(
			KeywordIndexingConfigurationKeys.ENABLED.getJsonKey());

		if (!enabled) {
			return true;
		}

		SearchHits searchHits = searchResponse.getSearchHits();

		String keywords = searchRequestContext.getKeywords();

		int hitsThreshold = configurationJsonObject.getInt(
			KeywordIndexingConfigurationKeys.HITS_THRESHOLD.getJsonKey(), 2);

		if (!Validator.isBlank(keywords) &&
			(searchHits.getTotalHits() >= hitsThreshold)) {

			keywords = _filterKeywords(configurationJsonObject, keywords);

			if (keywords.length() == 0) {
				return true;
			}

			_addDocument(searchRequestContext, keywords);
		}

		return true;
	}

	private void _addDocument(
		SearchRequestContext searchRequestContext, String keywords) {

		// TODO

		/*
		 Requirements:
		 	- There should be an own index for storing and querying stored keywords
		 	- Indexed keywords should (in the future) also be contextually tagged: "persons", "places", "times" etc.
		 	- Reindexing a keyword should increase its priority / "popularity"
		 */
	}

	private String _filterKeywords(
		JSONObject configurationJsonObject, String keywords) {

		JSONArray excludedWordsJsonArray = configurationJsonObject.getJSONArray(
			KeywordIndexingConfigurationKeys.BLACKLIST.getJsonKey());

		if ((excludedWordsJsonArray == null) ||
			(excludedWordsJsonArray.length() == 0)) {

			return keywords;
		}

		String[] excludedWords = GSearchJsonUtil.jsonArrayToStringArray(
			excludedWordsJsonArray);

		String splitter = configurationJsonObject.getString(
			KeywordIndexingConfigurationKeys.BLACKLIST_SPLITTER.getJsonKey(),
			" ");

		try {
			String[] keywordArray = keywords.split(splitter);

			for (String keyword : keywordArray) {
				for (String exclude : excludedWords) {
					if (exclude.endsWith("*")) {
						exclude = exclude.substring(0, exclude.length() - 1);

						if (keyword.startsWith(exclude)) {
							if (_log.isDebugEnabled()) {
								_log.debug(
									"Excluding keyword by stem: " + keyword);
							}

							keywords = keywords.replace(keyword, "");
						}
					}
					else if (keyword.equals(exclude)) {
						if (_log.isDebugEnabled()) {
							_log.debug("Excluding keyword: " + keyword);
						}

						keywords = keywords.replace(keyword, "");
					}
				}
			}
		}
		catch (Exception exception) {
			_log.error(exception.getMessage(), exception);
		}

		return keywords;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		KeywordIndexerPostProcessor.class);

	@Reference
	private IndexWriterHelper _indexWriterHelper;

}