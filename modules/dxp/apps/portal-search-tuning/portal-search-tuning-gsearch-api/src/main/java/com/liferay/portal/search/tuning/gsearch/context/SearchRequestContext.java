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

package com.liferay.portal.search.tuning.gsearch.context;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.search.tuning.gsearch.message.Message;
import com.liferay.portal.search.tuning.gsearch.parameter.SearchParameterData;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * @author Petteri Karttunen
 */
public interface SearchRequestContext {

	public void addMessage(Message message);

	public Optional<JSONArray> getAggregationConfiguration();

	public JSONArray getClauseConfiguration();

	public Long getCompanyId();

	public Optional<List<String>> getExcludeQueryContributors();

	public Optional<List<String>> getExcludeQueryPostProcessors();

	public Optional<String[]> getFetchSourceExcludes();

	public Optional<String[]> getFetchSourceIncludes();

	public int getFrom();

	public Optional<JSONObject> getHighlightConfiguration();

	public String[] getIndexNames();

	public Optional<String> getInitialKeywords();

	public Optional<JSONObject> getKeywordIndexingConfiguration();

	public String getKeywords();

	public Optional<JSONObject> getKeywordSuggesterConfiguration();

	public Locale getLocale();

	public List<Message> getMessages();

	public String getRawKeywords();

	public Long getSearchConfigurationId();

	public SearchParameterData getSearchParameterData();

	public Integer getSize();

	public Optional<JSONArray> getSortConfiguration();

	public Optional<JSONObject> getSpellCheckerConfiguration();

	public Long getUserId();

	public boolean hasErrors();

	public boolean isExplain();

	public boolean isFetchSource();

	public boolean isIncludeResponseString();

}