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

package com.liferay.portal.search.tuning.gsearch.impl.internal.suggester;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.suggest.Suggester;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.search.SuggestSearchRequest;
import com.liferay.portal.search.engine.adapter.search.SuggestSearchResponse;
import com.liferay.portal.search.engine.adapter.search.SuggestSearchResult;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.CommonConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.message.Message;
import com.liferay.portal.search.tuning.gsearch.message.Severity;
import com.liferay.portal.search.tuning.gsearch.spi.suggester.SuggesterBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, service = SuggesterHelper.class)
public class SuggesterHelper {
	
	public List<Suggester> getSuggesters(
		SearchRequestContext searchRequestContext,
		JSONArray suggesterConfigurationJSONArray) {

		List<Suggester> suggesters = new ArrayList<>();

		for (int i = 0; i < suggesterConfigurationJSONArray.length(); i++) {
			JSONObject suggesterJsonObject =
				suggesterConfigurationJSONArray.getJSONObject(i);

			String type = null;

			try {
				boolean enabled = suggesterJsonObject.getBoolean(
					CommonConfigurationKeys.ENABLED.getJsonKey(), false);

				if (!enabled) {
					continue;
				}

				JSONObject suggesterConfigurationJsonObject =
					suggesterJsonObject.getJSONObject(
						CommonConfigurationKeys.CONFIGURATION.getJsonKey());

				type = suggesterJsonObject.getString(
					CommonConfigurationKeys.TYPE.getJsonKey());
				
				SuggesterBuilder suggesterBuilder =
					_suggesterBuilderFactory.getBuilder(type);

				Optional<Suggester> suggesterOptional = suggesterBuilder.build(
					searchRequestContext, suggesterConfigurationJsonObject);

				if (suggesterOptional.isPresent()) {
					suggesters.add(suggesterOptional.get());
				}
			}
			catch (IllegalArgumentException illegalArgumentException) {
				searchRequestContext.addMessage(
					new Message(
						Severity.ERROR, "core",
						"core.error.unknown-suggester-type",
						illegalArgumentException.getMessage(),
						illegalArgumentException, suggesterJsonObject,
						CommonConfigurationKeys.TYPE.getJsonKey(), type));

				if (_log.isWarnEnabled()) {
					_log.warn(
						illegalArgumentException.getMessage(),
						illegalArgumentException);
				}
			}
			catch (Exception exception) {
				searchRequestContext.addMessage(
					new Message(
						Severity.ERROR, "core",
						"core.error.unknown-error-in-suggester-configuration",
						exception.getMessage(), exception, suggesterJsonObject,
						null, null));

				_log.error(exception.getMessage(), exception);
			}
		}

		return suggesters;
	}

	public JSONArray getSuggestions(
		SearchRequestContext searchRequestContext, List<Suggester> suggesters) {

		List<String> suggestions = new ArrayList<>();

		try {
			SuggestSearchResponse response = _executeSuggestSearchRequest(
				searchRequestContext, suggesters);

			Collection<SuggestSearchResult> suggesterResults =
				response.getSuggestSearchResults();

			if (suggesterResults != null) {
				for (SuggestSearchResult suggesterResult : suggesterResults) {
					for (SuggestSearchResult.Entry entry :
							suggesterResult.getEntries()) {

						for (SuggestSearchResult.Entry.Option option :
								entry.getOptions()) {

							if (!suggestions.contains(option.getText())) {
								if (_log.isDebugEnabled()) {
									_log.debug(
										"Adding suggestion text " +
											option.getText());
								}

								suggestions.add(option.getText());
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		if (suggestions.size() == 1) {
			suggestions.set(0, searchRequestContext.getKeywords());
		}

		return JSONFactoryUtil.createJSONArray(suggestions);
	}

	private SuggestSearchResponse _executeSuggestSearchRequest(
		SearchRequestContext searchRequestContext, List<Suggester> suggesters) {

		long companyId = searchRequestContext.getCompanyId();

		// TODO: LPS-118888

		String indexName = "TODO";

		SuggestSearchRequest suggestSearchRequest = new SuggestSearchRequest(
			indexName);

		for (Suggester suggester : suggesters) {
			suggestSearchRequest.addSuggester(suggester);
		}

		return _searchEngineAdapter.execute(suggestSearchRequest);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SuggesterHelper.class);

	@Reference
	private SearchEngineAdapter _searchEngineAdapter;

	@Reference
	private SuggesterBuilderFactory _suggesterBuilderFactory;

}