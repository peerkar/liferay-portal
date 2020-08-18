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

package com.liferay.portal.search.tuning.gsearch.impl.internal.searchrequest;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.parameter.SearchParameterData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Petteri Karttunen
 */
public class SearchRequestContextBuilder {

	public SearchRequestContextBuilder aggregationConfiguration(
		JSONArray aggregationConfigurationJsonArray) {

		_aggregationConfigurationJsonArray = aggregationConfigurationJsonArray;

		return this;
	}

	public SearchRequestContext build() {
		SearchRequestContext queryContext = new SearchRequestContextImpl(
			_aggregationConfigurationJsonArray, _clauseConfigurationJsonArray,
			_companyId, _excludeQueryContributors, _excludeQueryPostProcessors,
			_explain, _fetchSource, _fetchSourceExcludes, _fetchSourceIncludes,
			_from, _highlightConfigurationJsonObject, _includeResponseString,
			_indexNames, _initialKeywords,
			_keywordIndexingConfigurationJsonObject, _keywords,
			_keywordSuggesterConfigurationJsonObject, _locale, _rawKeywords,
			_searchConfigurationId, _searchParameterData, _size,
			_sortConfigurationJsonArray, _spellCheckerConfigurationJsonObject,
			_userId);

		_validateQueryContext(queryContext);

		return queryContext;
	}

	public SearchRequestContextBuilder clauseConfiguration(
		JSONArray clauseConfigurationJsonArray) {

		_clauseConfigurationJsonArray = clauseConfigurationJsonArray;

		return this;
	}

	public SearchRequestContextBuilder companyId(long companyId) {
		_companyId = companyId;

		return this;
	}

	public SearchRequestContextBuilder excludeQueryContributors(
		List<String> excludeQueryContributors) {

		_excludeQueryContributors = excludeQueryContributors;

		return this;
	}

	public SearchRequestContextBuilder excludeQueryPostProcessors(
		List<String> excludeQueryPostProcessors) {

		_excludeQueryPostProcessors = excludeQueryPostProcessors;

		return this;
	}

	public SearchRequestContextBuilder explain(boolean explain) {
		_explain = explain;

		return this;
	}

	public SearchRequestContextBuilder fetchSource(boolean fetchSource) {
		_fetchSource = fetchSource;

		return this;
	}

	public SearchRequestContextBuilder fetchSourceExcludes(
		String[] fetchSourceIncludes) {

		_fetchSourceIncludes = fetchSourceIncludes;

		return this;
	}

	public SearchRequestContextBuilder fetchSourceIncludes(
		String[] fetchSourceIncludes) {

		_fetchSourceIncludes = fetchSourceIncludes;

		return this;
	}

	public SearchRequestContextBuilder from(int from) {
		_from = from;

		return this;
	}

	public SearchRequestContextBuilder highlightConfiguration(
		JSONObject highlightConfigurationJsonObject) {

		_highlightConfigurationJsonObject = highlightConfigurationJsonObject;

		return this;
	}

	public SearchRequestContextBuilder includeResponseString(
		boolean includeResponseString) {

		_includeResponseString = includeResponseString;

		return this;
	}

	public SearchRequestContextBuilder indexNames(String[] indexNames) {
		_indexNames = indexNames;

		return this;
	}

	public SearchRequestContextBuilder initialKeywords(String initialKeywords) {
		_initialKeywords = initialKeywords;

		return this;
	}

	public SearchRequestContextBuilder keywordIndexingConfiguration(
		JSONObject keywordIndexingConfigurationJsonObject) {

		_keywordIndexingConfigurationJsonObject =
			keywordIndexingConfigurationJsonObject;

		return this;
	}

	public SearchRequestContextBuilder keywords(String keywords) {
		_keywords = keywords;

		return this;
	}

	public SearchRequestContextBuilder keywordSuggesterConfiguration(
		JSONObject keywordSuggestionsConfigurationJsonObject) {

		_keywordSuggesterConfigurationJsonObject =
			keywordSuggestionsConfigurationJsonObject;

		return this;
	}

	public SearchRequestContextBuilder locale(Locale locale) {
		_locale = locale;

		return this;
	}

	public SearchRequestContextBuilder rawKeywords(String rawKeywords) {
		_rawKeywords = rawKeywords;

		return this;
	}

	public SearchRequestContextBuilder searchConfigurationId(
		long searchConfigurationId) {

		_searchConfigurationId = searchConfigurationId;

		return this;
	}

	public SearchRequestContextBuilder searchParameterData(
		SearchParameterData searchParameterData) {

		_searchParameterData = searchParameterData;

		return this;
	}

	public SearchRequestContextBuilder size(int size) {
		_size = size;

		return this;
	}

	public SearchRequestContextBuilder sortConfiguration(
		JSONArray sortConfigurationJsonArray) {

		_sortConfigurationJsonArray = sortConfigurationJsonArray;

		return this;
	}

	public SearchRequestContextBuilder spellCheckerConfiguration(
		JSONObject spellCheckerConfigurationJsonObject) {

		_spellCheckerConfigurationJsonObject =
			spellCheckerConfigurationJsonObject;

		return this;
	}

	public SearchRequestContextBuilder userId(long userId) {
		_userId = userId;

		return this;
	}

	private void _validateQueryContext(SearchRequestContext queryContext) {
		if ((queryContext.getCompanyId() == null) ||
			(queryContext.getClauseConfiguration() == null) ||
			(queryContext.getClauseConfiguration().length() == 0) ||
			(queryContext.getIndexNames() == null) ||
			(queryContext.getLocale() == null) ||
			(queryContext.getSearchConfigurationId() == null) ||
			(queryContext.getSize() == null) ||
			(queryContext.getUserId() == null)) {

			throw new IllegalStateException(
				"Invalid QueryContext state." +
					" clauseConfiguration, indexes, locale, size and userId " +
						" are mandatory [ " + queryContext.toString() + " ]");
		}
	}

	private JSONArray _aggregationConfigurationJsonArray;
	private JSONArray _clauseConfigurationJsonArray;
	private Long _companyId;
	private List<String> _excludeQueryContributors = new ArrayList<>();
	private List<String> _excludeQueryPostProcessors = new ArrayList<>();
	private boolean _explain;
	private boolean _fetchSource;
	private String[] _fetchSourceExcludes;
	private String[] _fetchSourceIncludes;
	private int _from;
	private JSONObject _highlightConfigurationJsonObject;
	private boolean _includeResponseString;
	private String[] _indexNames;
	private String _initialKeywords;
	private JSONObject _keywordIndexingConfigurationJsonObject;
	private String _keywords;
	private JSONObject _keywordSuggesterConfigurationJsonObject;
	private Locale _locale;
	private String _rawKeywords;
	private Long _searchConfigurationId;
	private SearchParameterData _searchParameterData;
	private Integer _size;
	private JSONArray _sortConfigurationJsonArray;
	private JSONObject _spellCheckerConfigurationJsonObject;
	private Long _userId;

}