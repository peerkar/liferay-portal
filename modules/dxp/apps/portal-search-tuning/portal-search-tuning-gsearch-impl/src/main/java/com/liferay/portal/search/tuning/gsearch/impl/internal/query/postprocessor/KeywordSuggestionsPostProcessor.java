package com.liferay.portal.search.tuning.gsearch.impl.internal.query.postprocessor;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.search.engine.adapter.search.SearchSearchResponse;
import com.liferay.portal.search.hits.SearchHits;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.KeywordIndexingConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.exception.SearchRequestDataException;
import com.liferay.portal.search.tuning.gsearch.impl.util.GSearchJsonUtil;
import com.liferay.portal.search.tuning.gsearch.searchrequest.SearchRequestData;
import com.liferay.portal.search.tuning.gsearch.spi.query.postprocessor.QueryPostProcessor;
import com.liferay.portal.search.tuning.gsearch.suggester.KeywordSuggester;
import com.liferay.portal.search.tuning.gsearch.util.SearchClientHelper;

import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, service = QueryPostProcessor.class)
public class KeywordSuggestionsPostProcessor implements QueryPostProcessor {

	@Override
	public boolean process(
		SearchRequestContext searchRequestContext,
		SearchSearchResponse searchResponse) {

		Optional<JSONObject> configurationJsonObjectOptional =
			searchRequestContext.getKeywordSuggesterConfiguration();

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

		int hitsThreshold = configurationJsonObject.getInt(
			KeywordIndexingConfigurationKeys.HITS_THRESHOLD.getJsonKey(), 1);

		if (searchHits.getTotalHits() >= hitsThreshold) {
			return true;
		}

		// Get suggestions

		JSONArray querySuggestionsJsonArray =
			_keKeywordSuggester.getSuggestions(searchRequestContext);

		String[] querySuggestions = GSearchJsonUtil.jsonArrayToStringArray(
			querySuggestionsJsonArray);

		querySuggestions = ArrayUtil.remove(
			querySuggestions, searchRequestContext.getKeywords());

		if (_log.isDebugEnabled()) {
			_log.debug("Query suggestions size: " + querySuggestions.length);
		}

		if (ArrayUtil.isNotEmpty(querySuggestions)) {
			if (_log.isDebugEnabled()) {
				_log.debug("Suggestions found.");
			}

			/*
			 * TODO
			 searchRequestContext.setOriginalKeywords(
			 (String)searchRequestContext.getParameter(ParameterNames.RAW_KEYWORDS));
			 if (_log.isDebugEnabled()) {
			 _log.debug("Using querySuggestions[0] for alternative search.");
			 }
			 searchRequestContext.setKeywords(querySuggestions[0]);
			 // Remove the new keywords from query suggestions.
			 if (querySuggestions.length > 0) {
			 querySuggestions = ArrayUtil.remove(
			 querySuggestions, querySuggestions[0]);
			 } else {
			 querySuggestions = new String[0];
			 }
			 SearchSearchResponse newResponse = _executeNewSearch(searchRequestContext);
			 // Copy new values to the response.
			 if (newResponse.getAggregationResultsMap() != null) {
			 for (Entry<String, AggregationResult> entry :
			 newResponse.getAggregationResultsMap().entrySet()) {

			 searchResponse.addAggregationResult(entry.getValue());
			 }
			 }
			 searchResponse.setSearchHits(newResponse.getSearchHits());
			 searchResponse.getHits().copy(newResponse.getHits());
			 */
		}

		searchResponse.getHits(
		).setQuerySuggestions(
			querySuggestions
		);

		return true;
	}

	private SearchSearchResponse _executeNewSearch(
			SearchRequestContext searchRequestContext)
		throws SearchRequestDataException {

		SearchRequestData searchRequestData =
			_searchClientHelper.getSearchRequestData(searchRequestContext);

		return _searchClientHelper.getSearchResponse(
			searchRequestContext, searchRequestData);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		KeywordSuggestionsPostProcessor.class);

	@Reference
	private KeywordSuggester _keKeywordSuggester;

	@Reference
	private SearchClientHelper _searchClientHelper;;

}