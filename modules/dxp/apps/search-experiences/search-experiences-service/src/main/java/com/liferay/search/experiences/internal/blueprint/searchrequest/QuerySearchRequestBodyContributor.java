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

package com.liferay.search.experiences.internal.blueprint.searchrequest;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.search.filter.ComplexQueryPartBuilderFactory;
import com.liferay.portal.search.query.Queries;
import com.liferay.portal.search.query.Query;
import com.liferay.portal.search.rescore.RescoreBuilder;
import com.liferay.portal.search.rescore.RescoreBuilderFactory;
import com.liferay.portal.search.searcher.SearchRequestBuilder;
import com.liferay.search.experiences.blueprint.constants.ClauseContext;
import com.liferay.search.experiences.blueprint.constants.Occur;
import com.liferay.search.experiences.blueprint.parameter.SXPParameterData;
import com.liferay.search.experiences.blueprint.query.QueryContributor;
import com.liferay.search.experiences.blueprint.searchrequest.SearchRequestBodyContributor;
import com.liferay.search.experiences.blueprint.util.SXPBlueprintConfigurationsJSONHelper;
import com.liferay.search.experiences.internal.blueprint.clause.util.ClauseHelper;
import com.liferay.search.experiences.internal.blueprint.condition.util.ConditionsProcessor;
import com.liferay.search.experiences.internal.problem.ProblemUtil;
import com.liferay.search.experiences.model.SXPBlueprint;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, property = "name=query",
	service = SearchRequestBodyContributor.class
)
public class QuerySearchRequestBodyContributor
	implements SearchRequestBodyContributor {

	@Override
	public void contribute(
		SearchRequestBuilder searchRequestBuilder, SXPBlueprint sxpBlueprint,
		SXPParameterData sxpParameterData) {

		Optional<JSONArray> optional =
			_sxpBlueprintConfigurationsJSONHelper.getQueryConfigurationOptional(
				sxpBlueprint);

		optional.ifPresent(
			jsonArray -> _contribute(
				jsonArray, searchRequestBuilder, sxpParameterData));

		_executeQueryContributors(
			searchRequestBuilder, sxpBlueprint, sxpParameterData);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_queryContributorServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, QueryContributor.class, "name");
	}

	@Deactivate
	protected void deactivate() {
		_queryContributorServiceTrackerMap.close();
	}

	private void _addPostFilterClause(
		Occur occur, Query query, SearchRequestBuilder searchRequestBuilder) {

		searchRequestBuilder.addPostFilterQueryPart(
			_complexQueryPartBuilderFactory.builder(
			).query(
				query
			).occur(
				_getOccurString(occur)
			).build());
	}

	private void _addQueryClause(
		Occur occur, Query query, SearchRequestBuilder searchRequestBuilder) {

		searchRequestBuilder.addComplexQueryPart(
			_complexQueryPartBuilderFactory.builder(
			).query(
				query
			).occur(
				_getOccurString(occur)
			).build());
	}

	private void _addRescoreClause(
		JSONObject jsonObject, Query query,
		SearchRequestBuilder searchRequestBuilder) {

		RescoreBuilder rescoreBuilder = _rescoreBuilderFactory.builder(query);

		if (jsonObject.has("window_size")) {
			rescoreBuilder.windowSize(jsonObject.getInt("window_size", 100));
		}

		if (jsonObject.has("query_weight")) {
			rescoreBuilder.queryWeight(
				GetterUtil.getFloat(
					jsonObject.getString("window_size", "1.0")));
		}

		if (jsonObject.has("rescore_query_weight")) {
			rescoreBuilder.queryWeight(
				GetterUtil.getFloat(
					jsonObject.getString("rescore_query_weight", "1.0")));
		}

		searchRequestBuilder.addRescore(rescoreBuilder.build());
	}

	private void _addRescoreClause(
		Query query, QueryContributor queryContributor,
		SearchRequestBuilder searchRequestBuilder) {

		RescoreBuilder rescoreBuilder = _rescoreBuilderFactory.builder(query);

		if (queryContributor.getAttributes() != null) {
			Map<String, Object> attributes = queryContributor.getAttributes();

			if (attributes.containsKey("window_size")) {
				rescoreBuilder.windowSize(
					GetterUtil.getInteger(attributes.get("window_size")));
			}

			if (attributes.containsKey("query_weight")) {
				rescoreBuilder.queryWeight(
					GetterUtil.getFloat(attributes.get("query_weight")));
			}

			if (attributes.containsKey("rescore_query_weight")) {
				rescoreBuilder.rescoreQueryWeight(
					GetterUtil.getFloat(
						attributes.get("rescore_query_weight")));
			}
		}

		searchRequestBuilder.addRescore(rescoreBuilder.build());
	}

	private void _contribute(
		JSONArray jsonArray, SearchRequestBuilder searchRequestBuilder,
		SXPParameterData sxpParameterData) {

		for (int i = 0; i < jsonArray.length(); i++) {
			_contribute(
				"queryElement-" + i, jsonArray.getJSONObject(i),
				searchRequestBuilder, sxpParameterData);
		}
	}

	private void _contribute(
		String elementId, JSONObject jsonObject,
		SearchRequestBuilder searchRequestBuilder,
		SXPParameterData sxpParameterData) {

		problemsHolderBuilder.setElementId(elementId);

		if (jsonObject.getBoolean("enabled", true) &&
			_isConditionsTrue(
				jsonObject, sxpParameterData, problemsHolderBuilder)) {

			_processClauses(
				jsonObject.getJSONArray("clauses"), searchRequestBuilder,
				sxpParameterData);
		}

		problemsHolderBuilder.unsetElementId();
	}

	private void _executeQueryContributors(
		SearchRequestBuilder searchRequestBuilder, SXPBlueprint sxpBlueprint,
		SXPParameterData sxpParameterData) {

		if (_log.isDebugEnabled()) {
			_log.debug("Processing query contributors");
		}

		Set<String> keySet = _queryContributorServiceTrackerMap.keySet();

		if (keySet.isEmpty()) {
			return;
		}

		for (String name : keySet) {
			QueryContributor queryContributor =
				_queryContributorServiceTrackerMap.getService(name);

			try {
				Optional<Query> optional = queryContributor.build(
					sxpBlueprint, sxpParameterData);

				if (!optional.isPresent()) {
					return;
				}

				ClauseContext clauseContext =
					queryContributor.getClauseContext();

				if (clauseContext.equals(ClauseContext.POST_FILTER)) {
					_addPostFilterClause(
						queryContributor.getOccur(), optional.get(),
						searchRequestBuilder);
				}
				else if (clauseContext.equals(ClauseContext.QUERY)) {
					_addQueryClause(
						queryContributor.getOccur(), optional.get(),
						searchRequestBuilder);
				}
				else if (clauseContext.equals(ClauseContext.RESCORE)) {
					_addRescoreClause(
						optional.get(), queryContributor, searchRequestBuilder);
				}
			}
			catch (Exception exception) {
				_log.error(exception);

				ProblemUtil.addUnknownError(getClass().getName(), exception);
			}
		}
	}

	private ClauseContext _getClauseContext(JSONObject jsonObject) {
		String context = jsonObject.getString("context");

		return ClauseContext.valueOf(StringUtil.toUpperCase(context));
	}

	private Occur _getOccur(JSONObject jsonObject) {
		String occur = jsonObject.getString("occur", "must");

		return Occur.valueOf(StringUtil.toUpperCase(occur));
	}

	private String _getOccurString(Occur occur) {
		if (occur.equals(Occur.FILTER)) {
			return "filter";
		}
		else if (occur.equals(Occur.MUST)) {
			return "must";
		}
		else if (occur.equals(Occur.MUST_NOT)) {
			return "must_not";
		}
		else if (occur.equals(Occur.SHOULD)) {
			return "should";
		}

		return null;
	}

	private boolean _isConditionsTrue(
		JSONObject jsonObject, SXPParameterData sxpParameterData) {

		JSONObject conditionsJSONObject = jsonObject.getJSONObject(
			"conditions");

		if (conditionsJSONObject == null) {
			return true;
		}

		return _conditionsProcessor.processConditions(
			conditionsJSONObject, sxpParameterData, null);
	}

	private void _processClause(
		JSONObject jsonObject, SearchRequestBuilder searchRequestBuilder,
		SXPParameterData sxpParameterData) {

		Optional<Query> optional = _clauseHelper.getQueryOptional(
			jsonObject.getJSONObject("query"), sxpParameterData);

		optional.ifPresent(
			query -> {
				ClauseContext clauseContext = _getClauseContext(jsonObject);

				Occur occur = _getOccur(jsonObject);

				if (clauseContext.equals(ClauseContext.POST_FILTER)) {
					_addPostFilterClause(occur, query, searchRequestBuilder);
				}
				else if (clauseContext.equals(ClauseContext.QUERY)) {
					_addQueryClause(occur, query, searchRequestBuilder);
				}
				else if (clauseContext.equals(ClauseContext.RESCORE)) {
					_addRescoreClause(jsonObject, query, searchRequestBuilder);
				}
			});
	}

	private void _processClauses(
		JSONArray jsonArray, SearchRequestBuilder searchRequestBuilder,
		SXPParameterData sxpParameterData) {

		for (int j = 0; j < jsonArray.length(); j++) {
			_processClause(
				jsonArray.getJSONObject(j), searchRequestBuilder,
				sxpParameterData);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		QuerySearchRequestBodyContributor.class);

	@Reference
	private ClauseHelper _clauseHelper;

	@Reference
	private ComplexQueryPartBuilderFactory _complexQueryPartBuilderFactory;

	@Reference
	private ConditionsProcessor _conditionsProcessor;

	@Reference
	private Queries _queries;

	private ServiceTrackerMap<String, QueryContributor>
		_queryContributorServiceTrackerMap;

	@Reference
	private RescoreBuilderFactory _rescoreBuilderFactory;

	@Reference
	private SXPBlueprintConfigurationsJSONHelper
		_sxpBlueprintConfigurationsJSONHelper;

}