/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.task;

import com.liferay.generative.ai.task.configuration.GenerativeAITaskConfigurationProvider;
import com.liferay.generative.ai.task.exception.TaskDefinitionConfigurationJSONException;
import com.liferay.generative.ai.task.exception.TaskTestException;
import com.liferay.generative.ai.task.task.Task;
import com.liferay.generative.ai.task.task.TaskResponse;
import com.liferay.generative.ai.task.task.context.TaskContextParameter;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.constants.SearchContextAttributes;
import com.liferay.portal.search.document.Document;
import com.liferay.portal.search.hits.SearchHit;
import com.liferay.portal.search.hits.SearchHits;
import com.liferay.portal.search.searcher.SearchRequest;
import com.liferay.portal.search.searcher.SearchRequestBuilder;
import com.liferay.portal.search.searcher.SearchRequestBuilderFactory;
import com.liferay.portal.search.searcher.SearchResponse;
import com.liferay.portal.search.searcher.Searcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Petteri Karttunen
 */
public class LocalDocumentRetrievalTask extends BaseTask implements Task {

	public LocalDocumentRetrievalTask(
		JSONObject configurationJSONObject,
		GenerativeAITaskConfigurationProvider generativeAIConfigurationProvider,
		Searcher searcher,
		SearchRequestBuilderFactory searchRequestBuilderFactory) {

		super(
			null, configurationJSONObject, generativeAIConfigurationProvider,
			"local_document_retrieval");

		_searcher = searcher;
		_searchRequestBuilderFactory = searchRequestBuilderFactory;
	}

	@Override
	public TaskResponse execute(boolean debug, Map<String, Object> input) {
		SearchResponse searchResponse = _searcher.search(
			_getSearchRequest(
				_createSearchContext(input),
				attributesJSONObject.getInt("top_k", 3)));

		return toTaskResponse(
			_getDebugInfo(debug, searchResponse.getSearchHits()),
			_getTexts(searchResponse.getSearchHits()));
	}

	@Override
	public void test() throws TaskTestException {

		// TODO Auto-generated method stub

	}

	@Override
	public void validateConfigurationJSON()
		throws TaskDefinitionConfigurationJSONException {

		// TODO Auto-generated method stub

	}

	@Override
	protected String toStringValue(Object value) {
		if (value == null) {
			return null;
		}

		List<String> list = (List<String>)value;

		if (ListUtil.isEmpty(list)) {
			return StringPool.BLANK;
		}

		StringBundler sb = new StringBundler();

		for (String s : list) {
			sb.append(s);
			sb.append(" \n");
		}

		return sb.toString();
	}

	private SearchContext _createSearchContext(Map<String, Object> input) {
		SearchContext searchContext = new SearchContext();

		TaskContextParameter ipAddressTaskContextParameter =
			taskContext.getTaskContextParameter("ipAddress");

		if (ipAddressTaskContextParameter != null) {
			searchContext.setAttribute(
				"search.experiences.ip.address",
				ipAddressTaskContextParameter.getStringValue());
		}

		searchContext.setCompanyId(taskContext.getCompanyId());

		//searchContext.setGroupIds(new long[] {groupId});

		searchContext.setKeywords(MapUtil.getString(input, "text"));
		searchContext.setLocale(locale);

		TaskContextParameter timeZoneTaskContextParameter =
			taskContext.getTaskContextParameter("timeZone");

		if (timeZoneTaskContextParameter != null) {
			searchContext.setTimeZone(
				(TimeZone)timeZoneTaskContextParameter.getValue());
		}

		searchContext.setUserId(taskContext.getUserId());

		return searchContext;
	}

	private Map<String, Object> _getDebugInfo(
		boolean debug, SearchHits searchHits) {

		if (!debug) {
			return null;
		}

		return HashMapBuilder.<String, Object>put(
			"totalHits", searchHits.getTotalHits()
		).build();
	}

	private SearchRequest _getSearchRequest(
		SearchContext searchContext1, int size) {

		SearchRequestBuilder searchRequestBuilder =
			_searchRequestBuilderFactory.builder();

		searchRequestBuilder.from(
			0
		).queryString(
			searchContext1.getKeywords()
		).size(
			size
		).withSearchContext(
			searchContext2 -> {
				_setSearchExperiencesSearchContextAttributes(
					searchContext1, searchContext2);

				searchContext2.setAttribute(
					SearchContextAttributes.
						ATTRIBUTE_KEY_CONTRIBUTE_TUNING_RANKINGS,
					Boolean.TRUE);
				searchContext2.setCompanyId(searchContext1.getCompanyId());
				//searchContext2.setGroupIds(searchContext1.getGroupIds());
				searchContext2.setKeywords(searchContext1.getKeywords());
				searchContext2.setLocale(searchContext1.getLocale());
				searchContext2.setTimeZone(searchContext1.getTimeZone());
				searchContext2.setUserId(searchContext1.getUserId());
			}
		);

		return searchRequestBuilder.build();
	}

	private List<String> _getTexts(SearchHits searchHits) {
		if (searchHits.getTotalHits() == 0) {
			return null;
		}

		String resultField = replaceTemplateVariables(
			locale,
			attributesJSONObject.getString(
				"result_field", "content_${language_id}"));

		List<SearchHit> searchHitList = searchHits.getSearchHits();

		List<String> texts = new ArrayList<>();

		ListUtil.isNotEmptyForEach(
			searchHitList,
			searchHit -> {
				Document document = searchHit.getDocument();

				texts.add(document.getString(resultField));
			});

		return texts;
	}

	private void _setSearchExperiencesSearchContextAttributes(
		SearchContext sourceSearchContext, SearchContext targetSearchContext) {

		MapUtil.isNotEmptyForEach(
			sourceSearchContext.getAttributes(),
			targetSearchContext::setAttribute);

		String sxpBlueprintExternalReferenceCode =
			attributesJSONObject.getString(
				"sxp_blueprint_external_reference_code");

		if (!Validator.isBlank(sxpBlueprintExternalReferenceCode)) {
			targetSearchContext.setAttribute(
				"search.experiences.blueprint.external.reference.code",
				sxpBlueprintExternalReferenceCode);
		}

		String sxpBlueprintId = attributesJSONObject.getString(
			"sxpBlueprintId");

		if (!Validator.isBlank(sxpBlueprintId)) {
			targetSearchContext.setAttribute(
				"search.experiences.blueprint.id", sxpBlueprintId);
		}
	}

	private final Searcher _searcher;
	private final SearchRequestBuilderFactory _searchRequestBuilderFactory;

}