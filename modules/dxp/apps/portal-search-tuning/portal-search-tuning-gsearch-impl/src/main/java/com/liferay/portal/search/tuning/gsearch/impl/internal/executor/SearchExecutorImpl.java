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

package com.liferay.portal.search.tuning.gsearch.impl.internal.executor;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.aggregation.Aggregation;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.search.SearchSearchRequest;
import com.liferay.portal.search.engine.adapter.search.SearchSearchResponse;
import com.liferay.portal.search.sort.Sort;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.HighlightConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.impl.util.GSearchJsonUtil;
import com.liferay.portal.search.tuning.gsearch.searchrequest.SearchRequestData;
import com.liferay.portal.search.tuning.gsearch.spi.query.postprocessor.QueryPostProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * @author Petteri Karttunen
 */
@Component(immediate = true, service = SearchExecutor.class)
public class SearchExecutorImpl implements SearchExecutor {

	public SearchSearchResponse execute(
		SearchRequestContext searchRequestContext,
		SearchRequestData searchRequestData) {

		SearchSearchRequest searchRequest = new SearchSearchRequest();

		searchRequest.setIndexNames(searchRequestContext.getIndexNames());

		if (!searchRequestData.getAggregations().isEmpty()) {
			_setAggregations(
				searchRequest, searchRequestData.getAggregations());
		}

		// Sorts cannot be used with rescorer (See Elasticsearch documentation)

		if (!searchRequestData.getRescores().isEmpty()) {
			searchRequest.setRescores(searchRequestData.getRescores());
		}
		else if (!searchRequestData.getSorts().isEmpty()) {
			searchRequest.addSorts(
				searchRequestData.getSorts(
				).stream(
				).toArray(
					Sort[]::new
				));
		}

		if (searchRequestData.getPostFilterQuery().hasClauses()) {
			searchRequest.setPostFilterQuery(
				searchRequestData.getPostFilterQuery());
		}

		if (searchRequestData.getQuery().hasClauses()) {
			searchRequest.setQuery(searchRequestData.getQuery());
		}

		searchRequest.setSize(searchRequestContext.getSize());
		searchRequest.setStart(searchRequestContext.getFrom());

		_setHighlightSettings(searchRequestContext, searchRequest);

		searchRequest.setLocale(searchRequestContext.getLocale());
		searchRequest.setExplain(searchRequestContext.isExplain());

		SearchSearchResponse searchResponse = _searchEngineAdapter.execute(
			searchRequest);

		if (_log.isDebugEnabled()) {
			if (searchResponse != null) {
				_log.debug(
					"Request string: " +
						searchResponse.getSearchRequestString());
				_log.debug("Hits: " + searchResponse.getCount());
				_log.debug("Time:" + searchResponse.getExecutionTime());
			}
		}

		_executeQueryPostProcessors(searchRequestContext, searchResponse);

		return searchResponse;
	}

	
	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC
	)
	protected void registerQueryPostProcessor(
		QueryPostProcessor queryPostProcessor, Map<String, Object> properties) {

		String name = (String)properties.get("name");

		if (Validator.isBlank(name)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to add query post processor " +
						queryPostProcessor.getClass(
						).getName() + ". Name property empty.");
			}
		}

		_queryPostProcessors.put(name, queryPostProcessor);
	}

	protected void unregisterQueryPostProcessor(
		QueryPostProcessor queryPostProcessor, Map<String, Object> properties) {

		String name = (String)properties.get("name");

		if (Validator.isBlank(name)) {
			return;
		}

		_queryPostProcessors.remove(name);
	}

	private void _executeQueryPostProcessors(
		SearchRequestContext searchRequestContext,
		SearchSearchResponse searchResponse) {

		if (_log.isDebugEnabled()) {
			_log.debug("Executing query post processors.");
		}

		if (_queryPostProcessors == null) {
			return;
		}

		Optional<List<String>> excludedQueryPostProcessorsOptional =
			searchRequestContext.getExcludeQueryPostProcessors();

		if (excludedQueryPostProcessorsOptional.filter(
				list -> list.contains("*")).isPresent()) {

			return;
		}

		for (Map.Entry<String, QueryPostProcessor> entry :
				_queryPostProcessors.entrySet()) {

			if (excludedQueryPostProcessorsOptional.filter(
					list -> list.contains(entry.getKey())).isPresent()) {

				if (_log.isDebugEnabled()) {
					_log.debug(
						entry.getValue(
						).getClass(
						).getName() + " is excluded.");
				}

				continue;
			}

			entry.getValue(
			).process(
				searchRequestContext, searchResponse
			);
		}
	}

	private void _setAggregations(
		SearchSearchRequest searchRequest, List<Aggregation> aggregations) {

		for (Aggregation aggregation : aggregations) {
			searchRequest.addAggregation(aggregation);
		}
	}

	private void _setHighlightSettings(
		SearchRequestContext searchRequestContext,
		SearchSearchRequest searchRequest) {

		Optional<JSONObject> highlightConfigurationJsonObjectOptional =
			searchRequestContext.getHighlightConfiguration();

		if (!highlightConfigurationJsonObjectOptional.isPresent()) {
			return;
		}

		JSONObject highlightConfigurationJsonObject =
			highlightConfigurationJsonObjectOptional.get();

		if (Validator.isNotNull(
				highlightConfigurationJsonObject.get(
					HighlightConfigurationKeys.ENABLED.getJsonKey()))) {

			searchRequest.setHighlightEnabled(
				highlightConfigurationJsonObject.getBoolean(
					HighlightConfigurationKeys.ENABLED.getJsonKey()));
		}

		if (Validator.isNotNull(
				highlightConfigurationJsonObject.get(
					HighlightConfigurationKeys.FRAGMENT_SIZE.getJsonKey()))) {

			searchRequest.setHighlightFragmentSize(
				highlightConfigurationJsonObject.getInt(
					HighlightConfigurationKeys.FRAGMENT_SIZE.getJsonKey()));
		}

		if (Validator.isNotNull(
				highlightConfigurationJsonObject.get(
					HighlightConfigurationKeys.SNIPPET_SIZE.getJsonKey()))) {

			searchRequest.setHighlightSnippetSize(
				highlightConfigurationJsonObject.getInt(
					HighlightConfigurationKeys.SNIPPET_SIZE.getJsonKey()));
		}

		if (Validator.isNotNull(
				highlightConfigurationJsonObject.get(
					HighlightConfigurationKeys.REQUIRE_FIELD_MATCH.
						getJsonKey()))) {

			searchRequest.setHighlightRequireFieldMatch(
				highlightConfigurationJsonObject.getBoolean(
					HighlightConfigurationKeys.REQUIRE_FIELD_MATCH.
						getJsonKey()));
		}

		if (Validator.isNotNull(
				highlightConfigurationJsonObject.get(
					HighlightConfigurationKeys.FIELD_NAMES.getJsonKey()))) {

			JSONArray fieldNamesJsonArray =
				highlightConfigurationJsonObject.getJSONArray(
					HighlightConfigurationKeys.FIELD_NAMES.getJsonKey());

			String[] fieldNames = GSearchJsonUtil.jsonArrayToStringArray(
				fieldNamesJsonArray);

			searchRequest.setHighlightFieldNames(fieldNames);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SearchExecutorImpl.class);

	private volatile Map<String, QueryPostProcessor> _queryPostProcessors = new HashMap<String, QueryPostProcessor>();

	@Reference
	private SearchEngineAdapter _searchEngineAdapter;

}