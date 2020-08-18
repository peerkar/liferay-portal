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
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.message.Message;
import com.liferay.portal.search.tuning.gsearch.message.Severity;
import com.liferay.portal.search.tuning.gsearch.parameter.SearchParameterData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * @author Petteri Karttunen
 */
public class SearchRequestContextImpl implements SearchRequestContext {

	public SearchRequestContextImpl(
		JSONArray aggregationConfigurationJsonArray,
		JSONArray clauseConfigurationJsonArray, Long companyId,
		List<String> excludeQueryContributors,
		List<String> excludeQueryPostProcessors, boolean explain,
		boolean fetchSource, String[] fetchSourceExcludes,
		String[] fetchSourceIncludes, int from,
		JSONObject highlightConfigurationJsonObject,
		boolean includeResponseString, String[] indexNames,
		String initialKeywords,
		JSONObject keywordIndexingConfigurationJsonObject, String keywords,
		JSONObject keywordSuggestionsConfigurationJsonObject, Locale locale,
		String rawKeywords, long searchConfigurationId,
		SearchParameterData searchParameterData, Integer size,
		JSONArray sortConfigurationJsonArray,
		JSONObject spellCheckerConfigurationJsonObject, Long userId) {

		_aggregationConfigurationJsonArray = aggregationConfigurationJsonArray;
		_clauseConfigurationJsonArray = clauseConfigurationJsonArray;
		_companyId = companyId;
		_excludeQueryContributors = excludeQueryContributors;
		_excludeQueryPostProcessors = excludeQueryPostProcessors;
		_explain = explain;
		_fetchSource = fetchSource;
		_fetchSourceExcludes = fetchSourceExcludes;
		_fetchSourceIncludes = fetchSourceIncludes;
		_from = from;
		_highlightConfigurationJsonObject = highlightConfigurationJsonObject;
		_includeResponseString = includeResponseString;
		_indexNames = indexNames;
		_initialKeywords = initialKeywords;
		_keywordIndexingConfigurationJsonObject =
			keywordIndexingConfigurationJsonObject;
		_keywords = keywords;
		_keywordSuggesterConfigurationJsonObject =
			keywordSuggestionsConfigurationJsonObject;
		_locale = locale;
		_rawKeywords = rawKeywords;
		_searchConfigurationId = searchConfigurationId;
		_searchParameterData = searchParameterData;
		_size = size;
		_sortConfigurationJsonArray = sortConfigurationJsonArray;
		_spellCheckerConfigurationJsonObject =
				spellCheckerConfigurationJsonObject;
		_userId = userId;
	}

	@Override
	public void addMessage(Message message) {
		_messages.add(message);
	}

	@Override
	public Optional<JSONArray> getAggregationConfiguration() {
		if ((_aggregationConfigurationJsonArray == null) ||
			(_aggregationConfigurationJsonArray.length() == 0)) {

			return Optional.empty();

		}
		return Optional.of(_aggregationConfigurationJsonArray);
	}

	@Override
	public JSONArray getClauseConfiguration() {
		return _clauseConfigurationJsonArray;
	}

	@Override
	public Long getCompanyId() {
		return _companyId;
	}

	@Override
	public Optional<List<String>> getExcludeQueryContributors() {
		if ((_excludeQueryContributors != null) &&
			(_excludeQueryContributors.size() > 0)) {

			return Optional.of(_excludeQueryContributors);
		}

		return Optional.empty();
	}

	@Override
	public Optional<List<String>> getExcludeQueryPostProcessors() {
		if ((_excludeQueryPostProcessors != null) &&
			(_excludeQueryPostProcessors.size() > 0)) {

			return Optional.of(_excludeQueryPostProcessors);
		}

		return Optional.empty();
	}

	@Override
	public Optional<String[]> getFetchSourceExcludes() {
		if ((_fetchSourceExcludes != null) &&
			(_fetchSourceExcludes.length > 0)) {

			return Optional.of(_fetchSourceExcludes);
		}

		return Optional.empty();
	}

	@Override
	public Optional<String[]> getFetchSourceIncludes() {
		if ((_fetchSourceIncludes != null) &&
			(_fetchSourceIncludes.length > 0)) {

			return Optional.of(_fetchSourceIncludes);
		}

		return Optional.empty();
	}

	@Override
	public int getFrom() {
		return _from;
	}

	@Override
	public Optional<JSONObject> getHighlightConfiguration() {
		if ((_highlightConfigurationJsonObject == null) ||
			(_highlightConfigurationJsonObject.length() == 0)) {

			return Optional.empty();
		}
		return Optional.of(_highlightConfigurationJsonObject);

	}

	@Override
	public String[] getIndexNames() {
		return _indexNames;
	}

	@Override
	public Optional<String> getInitialKeywords() {
		if (!Validator.isBlank(_initialKeywords)) {
			return Optional.of(_initialKeywords);
		}

		return Optional.empty();
	}

	@Override
	public Optional<JSONObject> getKeywordIndexingConfiguration() {
		if ((_keywordIndexingConfigurationJsonObject == null) ||
			(_keywordIndexingConfigurationJsonObject.length() == 0)) {

			return Optional.empty();
		}
		return Optional.of(_keywordIndexingConfigurationJsonObject);
	}

	@Override
	public String getKeywords() {
		return _keywords;
	}

	@Override
	public Optional<JSONObject> getKeywordSuggesterConfiguration() {
		if ((_keywordSuggesterConfigurationJsonObject == null) ||
			(_keywordSuggesterConfigurationJsonObject.length() == 0)) {

			return Optional.empty();
		}
		return Optional.of(_keywordSuggesterConfigurationJsonObject);

	}

	@Override
	public Locale getLocale() {
		return _locale;
	}

	@Override
	public List<Message> getMessages() {
		return _messages;
	}

	@Override
	public String getRawKeywords() {
		return _rawKeywords;
	}

	@Override
	public Long getSearchConfigurationId() {
		return _searchConfigurationId;
	}

	@Override
	public SearchParameterData getSearchParameterData() {
		return _searchParameterData;
	}

	@Override
	public Integer getSize() {
		return _size;
	}

	@Override
	public Optional<JSONArray> getSortConfiguration() {
		if ((_sortConfigurationJsonArray == null) ||
			(_sortConfigurationJsonArray.length() == 0)) {

			return Optional.empty();
		}
		return Optional.of(_sortConfigurationJsonArray);

	}

	@Override
	public Optional<JSONObject> getSpellCheckerConfiguration() {
		if ((_spellCheckerConfigurationJsonObject == null) ||
			(_spellCheckerConfigurationJsonObject.length() == 0)) {

			return Optional.empty();
		}
		return Optional.of(_spellCheckerConfigurationJsonObject);
	}

	@Override
	public Long getUserId() {
		return _userId;
	}

	@Override
	public boolean hasErrors() {
		return _messages.stream(
		).anyMatch(
			m -> m.getSeverity(
			).equals(
				Severity.ERROR
			)
		);
	}

	@Override
	public boolean isExplain() {
		return _explain;
	}

	@Override
	public boolean isFetchSource() {
		return _fetchSource;
	}

	@Override
	public boolean isIncludeResponseString() {
		return _includeResponseString;
	}

	@Override
	public String toString() {
		return "SearchRequestContextImpl [_aggregationConfigurationJsonArray=" +
			_aggregationConfigurationJsonArray +
				", _clauseConfigurationJsonArray=" +
					_clauseConfigurationJsonArray + ", _companyId=" +
						_companyId + ", _excludeQueryContributors=" +
							_excludeQueryContributors +
								", _excludeQueryPostProcessors=" +
									_excludeQueryPostProcessors +
										", _messages=" + _messages +
											", _explain=" + _explain +
												", _fetchSource=" +
													_fetchSource +
														", _fetchSourceExcludes=" +
															Arrays.toString(
																_fetchSourceExcludes) +
																	", _fetchSourceIncludes=" +
																		Arrays.
																			toString(
																				_fetchSourceIncludes) +
																					", _from=" +
																						_from +
																							", _highlightConfigurationJsonObject=" +
																								_highlightConfigurationJsonObject +
																									", _includeResponseString=" +
																										_includeResponseString +
																											", _indexNames=" +
																												Arrays.
																													toString(
																														_indexNames) +
																															", _initialKeywords=" +
																																_initialKeywords +
																																	", _keywordIndexingConfigurationJsonObject=" +
																																		_keywordIndexingConfigurationJsonObject +
																																			", _keywordSuggestionsConfigurationJsonObject=" +
																																				_keywordSuggesterConfigurationJsonObject +
																																					", _keywords=" +
																																						_keywords +
																																							", _locale=" +
																																								_locale +
																																									", _rawKeywords=" +
																																										_rawKeywords +
																																											", _searchConfigurationId=" +
																																												_searchConfigurationId +
																																													", _searchParameterData=" +
																																														_searchParameterData +
																																															", _size=" +
																																																_size +
																																																	", _sortConfigurationJsonArray=" +
																																																		_sortConfigurationJsonArray +
																																																			", _spellCheckerConfigurationJsonArray=" +
																																																				_spellCheckerConfigurationJsonObject +
																																																					", _userId=" +
																																																						_userId +
																																																							"]";
	}

	private final JSONArray _aggregationConfigurationJsonArray;
	private final JSONArray _clauseConfigurationJsonArray;
	private final Long _companyId;
	private final List<String> _excludeQueryContributors;
	private final List<String> _excludeQueryPostProcessors;
	private final boolean _explain;
	private final boolean _fetchSource;
	private final String[] _fetchSourceExcludes;
	private final String[] _fetchSourceIncludes;
	private final int _from;
	private final JSONObject _highlightConfigurationJsonObject;
	private final boolean _includeResponseString;
	private final String[] _indexNames;
	private final String _initialKeywords;
	private final JSONObject _keywordIndexingConfigurationJsonObject;
	private final String _keywords;
	private final JSONObject _keywordSuggesterConfigurationJsonObject;
	private final Locale _locale;
	private final List<Message> _messages = new ArrayList<>();
	private final String _rawKeywords;
	private final long _searchConfigurationId;
	private final SearchParameterData _searchParameterData;
	private final Integer _size;
	private final JSONArray _sortConfigurationJsonArray;
	private final JSONObject _spellCheckerConfigurationJsonObject;
	private final Long _userId;

}