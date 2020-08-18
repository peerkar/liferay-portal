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

package com.liferay.portal.search.tuning.gsearch.impl.internal.results;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserConstants;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.document.Document;
import com.liferay.portal.search.document.Field;
import com.liferay.portal.search.engine.adapter.search.SearchSearchResponse;
import com.liferay.portal.search.highlight.HighlightField;
import com.liferay.portal.search.hits.SearchHit;
import com.liferay.portal.search.hits.SearchHits;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.FacetConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.constants.JSONResponseKeys;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.impl.internal.aggregations.facet.FacetHandlerFactory;
import com.liferay.portal.search.tuning.gsearch.impl.util.GSearchUtilOldTobeRemoved;
import com.liferay.portal.search.tuning.gsearch.results.ResultAttributes;
import com.liferay.portal.search.tuning.gsearch.spi.aggregation.facet.FacetHandler;
import com.liferay.portal.search.tuning.gsearch.spi.results.item.ResultItemBuilder;
import com.liferay.portal.search.tuning.gsearch.spi.results.item.ResultItemContributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Petteri Karttunen
 *
 * This class is TODO
 */
@Component(immediate = true, service = ResultsBuilder.class)
public class ResultsBuilderImpl implements ResultsBuilder {

	@Override
	public JSONObject build(
		SearchRequestContext searchRequestContext,
		SearchSearchResponse searchResponse,
		ResultAttributes resultAttributes) {

		JSONObject resultsObject = JSONFactoryUtil.createJSONObject();

		long startTime = System.currentTimeMillis();

		resultsObject.put(
			JSONResponseKeys.ITEMS,
			createItemsArray(
				searchRequestContext, searchResponse, resultAttributes));

		try {
			resultsObject.put(
				JSONResponseKeys.META,
				createMetaObject(searchRequestContext, searchResponse));
		}
		catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		try {
			resultsObject.put(
				JSONResponseKeys.PAGINATION,
				createPagingObject(searchRequestContext, searchResponse));
		}
		catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		JSONArray querySuggestions = createQuerySuggestionsObject(
			searchResponse);

		if (querySuggestions != null) {
			resultsObject.put(
				JSONResponseKeys.QUERY_SUGGESTIONS, querySuggestions);
		}

		try {
			resultsObject.put(
				JSONResponseKeys.FACETS,
				createFacetsArray(searchResponse, searchRequestContext));
		}
		catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Results processing took: " +
					(System.currentTimeMillis() - startTime));
		}

		return resultsObject;
	}

	protected void addResultItemContributor(
		ResultItemContributor resultItemProcessor) {

		if (_resultItemProcessors == null) {
			_resultItemProcessors = new ArrayList<>();
		}

		_resultItemProcessors.add(resultItemProcessor);
	}

	protected JSONArray createFacetsArray(
			SearchSearchResponse searchResponse,
			SearchRequestContext searchRequestContext)
		throws Exception {

		Optional<JSONArray> aggregationsConfigurationJsonArrayOptional =
			searchRequestContext.getAggregationConfiguration();

		Map<String, AggregationResult> aggregations =
			searchResponse.getAggregationResultsMap();

		JSONArray resultArray = JSONFactoryUtil.createJSONArray();

		if ((aggregations == null) ||
			!aggregationsConfigurationJsonArrayOptional.isPresent()) {

			return resultArray;
		}

		JSONArray configuration =
			aggregationsConfigurationJsonArrayOptional.get();

		// Keep the order of configuration.

		for (int i = 0; i < configuration.length(); i++) {
			JSONObject aggregationJsonObject = configuration.getJSONObject(i);

			JSONObject facetConfigurationJsonObject =
				configuration.getJSONObject(i);

			if (facetConfigurationJsonObject == null) {
				continue;
			}

			String facetFieldName = facetConfigurationJsonObject.getString(
				FacetConfigurationKeys.INDEX_FIELD.getJsonKey());

			String facetHandlerName = facetConfigurationJsonObject.getString(
				FacetConfigurationKeys.HANDLER.getJsonKey());

			for (Map.Entry<String, AggregationResult> entry :
					aggregations.entrySet()) {

				String fieldName = entry.getKey();

				if (!fieldName.equalsIgnoreCase(facetFieldName)) {
					continue;
				}

				FacetHandler facetHandler = _facetHandlerFactory.getHandler(
					facetHandlerName);

				Optional<JSONObject> resultObject =
					facetHandler.getResultsObject(
						searchRequestContext, entry.getValue(),
						facetConfigurationJsonObject);

				if (resultObject.isPresent()) {
					resultArray.put(resultObject);
				}
			}
		}

		return resultArray;
	}

	protected JSONArray createItemsArray(
		SearchRequestContext searchRequestContext,
		SearchSearchResponse searchResponse,
		ResultAttributes resultAttributes) {

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		SearchHits searchHits = searchResponse.getSearchHits();

		List<SearchHit> items = searchHits.getSearchHits();

		if ((items == null) || (items.size() == 0)) {
			return jsonArray;
		}

		for (SearchHit item : items) {
			Document document = item.getDocument();

			try {
				if (_log.isDebugEnabled()) {
					_log.debug(
						"##############################################");

					_log.debug("Score: " + item.getScore());

					for (Map.Entry<String, Field> e :
							document.getFields().entrySet()) {

						_log.debug(
							e.getKey() + ":" +
								e.getValue(
								).getValue());
					}
				}

				JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

				ResultItemBuilder resultItemBuilder =
					_resultsBuilderFactory.getBuilder(
						document.getString("entryClassName"));

				jsonObject.put(
					"title",
					resultItemBuilder.getTitle(
						searchRequestContext, resultAttributes, document));

				jsonObject.put(
					"date",
					resultItemBuilder.getDate(
						searchRequestContext, resultAttributes, document));

				jsonObject.put(
					"description",
					resultItemBuilder.getDescription(
						searchRequestContext, resultAttributes, document));

				jsonObject.put(
					"type",
					resultItemBuilder.getType(
						document
					).toLowerCase());

				jsonObject.put(
					"metadata",
					resultItemBuilder.getMetadata(
						searchRequestContext, resultAttributes, document));

				executeResultItemContributors(
					searchRequestContext, resultAttributes, document,
					resultItemBuilder, jsonObject);

				if (resultAttributes.isIncludeThumbnail()) {
					jsonObject.put(
						"imageSrc",
						resultItemBuilder.getThumbnail(
							searchRequestContext, resultAttributes, document));
				}

				if (resultAttributes.isIncludeUserPortrait()) {
					_includeUserPortrait(
						searchRequestContext, document, jsonObject);
				}

				if (resultAttributes.isIncludeRawDocument()) {
					JSONObject doc = JSONFactoryUtil.createJSONObject();

					for (Map.Entry<String, Field> e :
							document.getFields().entrySet()) {

						doc.put(
							e.getKey(),
							e.getValue(
							).getValue());
					}

					jsonObject.put("document", doc);
				}

				// ADDITIONALFIELDS

				// Include explain

				if (searchRequestContext.isExplain()) {
					jsonObject.put("explain", item.getExplanation());
				}

				// Set higlight fields

				setHightlightFields(
					item, jsonObject,
					resultAttributes.getDescriptionMaxLength());

				// Put single item to result array

				jsonArray.put(jsonObject);
			}
			catch (Exception e) {
				_log.error(e.getMessage(), e);
			}
		}

		return jsonArray;
	}

	protected JSONObject createMetaObject(
			SearchRequestContext searchRequestContext,
			SearchSearchResponse searchResponse)
		throws Exception {

		// 7.2 new SearchHits.getSearchTime() still doesn't work in FP1.
		// Using the hits object.

		Hits hits = searchResponse.getHits();

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		// If this parameter is populated, there was an alternate search.

		String initialKeywords = searchRequestContext.getInitialKeywords(
		).orElse(
			null
		);

		if (initialKeywords != null) {
			jsonObject.put("initialKeywords", initialKeywords);
		}

		jsonObject.put("queryTerms", searchRequestContext.getRawKeywords());

		jsonObject.put(
			"executionTime", String.format("%.3f", hits.getSearchTime()));

		jsonObject.put(
			"start",
			_getStart(searchRequestContext, searchResponse.getSearchHits()));

		jsonObject.put("totalHits", hits.getLength());

		return jsonObject;
	}

	protected JSONObject createPagingObject(
			SearchRequestContext searchRequestContext,
			SearchSearchResponse searchResponse)
		throws Exception {

		SearchHits searchHits = searchResponse.getSearchHits();

		JSONObject pagingObject = JSONFactoryUtil.createJSONObject();

		// ... long ...

		int totalHits = Math.toIntExact(searchHits.getTotalHits());

		if (totalHits == 0) {
			return pagingObject;
		}

		int pageSize = searchRequestContext.getSize();
		int start = _getStart(searchRequestContext, searchHits);
		int pageCount = _getPageCount(searchRequestContext, totalHits);

		// Page number to start from.

		int currentPage = (int)Math.floor((start + 1) / pageSize) + 1;
		pagingObject.put("defaultActivePage", currentPage);
		pagingObject.put("totalPages", pageCount);

		return pagingObject;
	}

	protected JSONArray createQuerySuggestionsObject(
		SearchSearchResponse searchResponse) {

		Hits hits = searchResponse.getHits();

		String[] querySuggestions = hits.getQuerySuggestions();

		if ((querySuggestions != null) && (querySuggestions.length > 0)) {
			return JSONFactoryUtil.createJSONArray(querySuggestions);
		}

		return null;
	}

	protected void executeResultItemContributors(
		SearchRequestContext searchRequestContext,
		ResultAttributes resultAttributes, Document document,
		ResultItemBuilder resultItemBuilder, JSONObject resultItem) {

		if (_log.isDebugEnabled()) {
			_log.debug("Executing result item processors.");
		}

		if (_resultItemProcessors == null) {
			return;
		}

		for (ResultItemContributor r : _resultItemProcessors) {
			try {
				r.contribute(
					searchRequestContext, resultAttributes, resultItemBuilder,
					document, resultItem);
			}
			catch (Exception e) {
				_log.error(e.getMessage(), e);
			}
		}
	}

	protected void removeResultItemContributor(
		ResultItemContributor resultItemProcessor) {

		_resultItemProcessors.remove(resultItemProcessor);
	}

	protected void setHightlightFields(
			SearchHit item, JSONObject resultObject, int maxLength)
		throws Exception {

		if (item.getHighlightFieldsMap() == null) {
			return;
		}

		for (Map.Entry<String, HighlightField> entry :
				item.getHighlightFieldsMap().entrySet()) {

			StringBundler sb = new StringBundler();

			int i = 0;

			for (String s : entry.getValue().getFragments()) {
				if (i > 0) {
					sb.append("...");
				}

				sb.append(s);
				i++;
			}

			// Use the field stem as the key.

			String key = entry.getKey();

			if (key.contains("_")) {
				key = key.substring(0, key.indexOf("_"));
			}

			String cleanedText = GSearchUtilOldTobeRemoved.stripHTML(
				sb.toString(), maxLength);

			resultObject.put(key + "_highlight", cleanedText);
		}
	}

	private int _getPageCount(
		SearchRequestContext searchRequestContext, int totalHits) {

		return (int)Math.ceil(totalHits * 1.0 / searchRequestContext.getSize());
	}

	private int _getStart(
			SearchRequestContext searchRequestContext, SearchHits searchHits)
		throws Exception {

		int pageSize = searchRequestContext.getSize();

		// Total hits in SearchHits API is long.
		// We don't probably have more than 2,147,483,647 documents here but
		// if we do, throw an exception.

		int totalHits = Math.toIntExact(searchHits.getTotalHits());

		int start = searchRequestContext.getFrom();

		if (totalHits < start) {
			start =
				(_getPageCount(searchRequestContext, totalHits) - 1) * pageSize;

			if (start < 0) {
				start = 0;
			}
		}

		return start;
	}

	// TODO

	private void _includeResultlFields(
		Document document, JSONObject resultItem) {

		Map<String, Class<?>> additionalResultFields = new HashMap<>();

		if (additionalResultFields != null) {

			// Loop for additional result fields. These have to be 1-1 index fields.

			for (Map.Entry<String, Class<?>> entry :
					additionalResultFields.entrySet()) {

				if (entry.getValue().isAssignableFrom(String[].class)) {
					List<Object> values = document.getValues(entry.getKey());

					if ((values != null) && (values.size() > 0)) {
						resultItem.put(entry.getKey(), values);
					}
				}
				else {
					String value = document.getString(entry.getKey());

					if (Validator.isNotNull(value)) {
						resultItem.put(entry.getKey(), value);
					}
				}
			}
		}
	}

	private void _includeUserPortrait(
		SearchRequestContext searchRequestContext, Document document,
		JSONObject resultItem) {

		try {
			long userId = document.getLong(
				com.liferay.portal.kernel.search.Field.USER_ID);

			User user = _userLocalService.getUser(userId);

			if (user.getPortraitId() != 0) {
				String userPortraitUrl = null;

				userPortraitUrl = UserConstants.getPortraitURL(
					"/image", user.isMale(), user.getPortraitId(),
					user.getUserUuid());

				if (userPortraitUrl != null) {
					resultItem.put("userPortraitUrl", userPortraitUrl);
				}
			}

			resultItem.put(
				"userInitials",
				user.getFirstName(
				).substring(
					0, 1
				) +
					user.getLastName(
					).substring(
						0, 1
					));

			resultItem.put("userName", user.getFullName());
		}
		catch (PortalException pe) {
			_log.warn(pe.getMessage());

			String name = document.getString(
				com.liferay.portal.kernel.search.Field.USER_NAME);

			String[] nameParts = name.split(" ");

			resultItem.put(
				"userInitials",
				nameParts[0].substring(
					0, 1
				).toUpperCase() +
					nameParts[0].substring(
						0, 1
					).toUpperCase());
		}
	}

	private static final Logger _log = LoggerFactory.getLogger(
		ResultsBuilderImpl.class);

	@Reference
	private FacetHandlerFactory _facetHandlerFactory;

	@Reference(
		bind = "addResultItemContributor",
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC, service = ResultItemContributor.class,
		unbind = "removeResultItemContributor"
	)
	private volatile List<ResultItemContributor> _resultItemProcessors;

	@Reference
	private ResultItemBuilderFactory _resultsBuilderFactory;

	@Reference
	private UserLocalService _userLocalService;

}