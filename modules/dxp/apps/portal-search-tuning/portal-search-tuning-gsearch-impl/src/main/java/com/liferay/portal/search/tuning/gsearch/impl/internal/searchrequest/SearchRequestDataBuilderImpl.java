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
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.aggregation.Aggregation;
import com.liferay.portal.search.query.BooleanQuery;
import com.liferay.portal.search.query.Queries;
import com.liferay.portal.search.query.Query;
import com.liferay.portal.search.query.TermQuery;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.AggregationConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.ClauseConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.CommonConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.FacetConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.values.ClauseContext;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.values.FilterMode;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.values.Occur;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.values.Operator;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.exception.SearchRequestDataException;
import com.liferay.portal.search.tuning.gsearch.impl.internal.aggregations.AggregationBuilderFactory;
import com.liferay.portal.search.tuning.gsearch.impl.internal.clause.ClauseBuilderFactory;
import com.liferay.portal.search.tuning.gsearch.impl.internal.clause.condition.ClauseConditionHandlerFactory;
import com.liferay.portal.search.tuning.gsearch.impl.util.ContextVariableUtil;
import com.liferay.portal.search.tuning.gsearch.message.Message;
import com.liferay.portal.search.tuning.gsearch.message.Severity;
import com.liferay.portal.search.tuning.gsearch.parameter.Parameter;
import com.liferay.portal.search.tuning.gsearch.searchrequest.SearchRequestData;
import com.liferay.portal.search.tuning.gsearch.spi.aggregation.AggregationBuilder;
import com.liferay.portal.search.tuning.gsearch.spi.clause.ClauseBuilder;
import com.liferay.portal.search.tuning.gsearch.spi.clause.ClauseConditionHandler;
import com.liferay.portal.search.tuning.gsearch.spi.query.QueryContributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * @author Petteri Karttunen
 *
 * This class is TODO
 */
@Component(immediate = true, service = SearchRequestDataBuilder.class)
public class SearchRequestDataBuilderImpl implements SearchRequestDataBuilder {

	@Override
	public SearchRequestData build(SearchRequestContext searchRequestContext)
		throws SearchRequestDataException {

		SearchRequestData searchRequestData = new SearchRequestData(_queries);

		addClauses(searchRequestContext, searchRequestData);

		addAggregations(searchRequestContext, searchRequestData);

		processQueryContributors(searchRequestContext, searchRequestData);

		if (searchRequestContext.hasErrors()) {
			throw new SearchRequestDataException("Couldn't build searchrequest data.",
				searchRequestContext.getMessages());
		}

		return searchRequestData;
	}

	protected void addAggregations(
		SearchRequestContext searchRequestContext,
		SearchRequestData searchRequestData) {

		Optional<JSONArray> aggregationConfigurationJsonArrayOptional =
			searchRequestContext.getAggregationConfiguration();

		if (!aggregationConfigurationJsonArrayOptional.isPresent()) {
			return;
		}

		JSONArray aggregationConfigurationJsonArray =
			aggregationConfigurationJsonArrayOptional.get();

		for (int i = 0; i < aggregationConfigurationJsonArray.length(); i++) {
			JSONObject aggregationJsonObject =
				aggregationConfigurationJsonArray.getJSONObject(i);

			boolean enabled = aggregationJsonObject.getBoolean(
				AggregationConfigurationKeys.ENABLED.getJsonKey(), false);

			if (!enabled) {
				continue;
			}

			String type = aggregationJsonObject.getString(
				CommonConfigurationKeys.TYPE.getJsonKey());

			try {
				JSONObject aggregationConfigurationJsonObject =
					aggregationJsonObject.getJSONObject(
						AggregationConfigurationKeys.BODY.getJsonKey());

				String name = aggregationConfigurationJsonObject.getString(
					AggregationConfigurationKeys.NAME.getJsonKey());

				AggregationBuilder aggregationBuilder =
					_aggregationBuilderFactory.getBuilder(type);

				Optional<Aggregation> aggregationOptional =
					aggregationBuilder.build(
						searchRequestContext,
						aggregationConfigurationJsonObject, name);

				if (aggregationOptional.isPresent()) {
					searchRequestData.getAggregations(
					).add(
						aggregationOptional.get()
					);

					// Facet

					if (aggregationJsonObject.has(
							AggregationConfigurationKeys.FACET.getJsonKey())) {

						addFacetFilters(
							searchRequestContext, searchRequestData,
							aggregationJsonObject);
					}
				}
			}
			catch (IllegalArgumentException iae) {
				searchRequestContext.addMessage(
					new Message(
						Severity.ERROR, "core",
						"core.error.unknown-aggregation-type", iae.getMessage(),
						iae, aggregationJsonObject,
						CommonConfigurationKeys.TYPE.getJsonKey(), type));
			}
			catch (Exception e) {
				searchRequestContext.addMessage(
					new Message(
						Severity.ERROR, "core",
						"core.error.unknown-aggregation-configuration-error",
						e.getMessage(), e, aggregationJsonObject, null, null));

				_log.error(e.getMessage(), e);
			}
		}
	}

	protected void addClauses(
		SearchRequestContext searchRequestContext,
		SearchRequestData searchRequestData) {

		JSONArray clauseConfigurationJSonArray =
			searchRequestContext.getClauseConfiguration();

		for (int i = 0; i < clauseConfigurationJSonArray.length(); i++) {
			JSONObject clauseConfigurationJsonObject =
				clauseConfigurationJSonArray.getJSONObject(i);

			if (!clauseConfigurationJsonObject.getBoolean(
					ClauseConfigurationKeys.ENABLED.getJsonKey(), false)) {

				continue;
			}

			boolean applyClauses = false;

			applyClauses = checkClauseConditions(
				searchRequestContext, clauseConfigurationJsonObject);

			if (applyClauses) {
				JSONArray clauseJsonArray =
					clauseConfigurationJsonObject.getJSONArray(
						ClauseConfigurationKeys.CLAUSES.getJsonKey());

				JSONObject clauseJsonObject = null;
				JSONObject queryJsonObject = null;

				for (int j = 0; j < clauseJsonArray.length(); j++) {
					clauseJsonObject = clauseJsonArray.getJSONObject(j);

					String type = clauseJsonObject.getString(
						ClauseConfigurationKeys.TYPE.getJsonKey());

					try {
						ClauseBuilder clauseBuilder =
							_clauseBuilderFactory.getBuilder(type);

						queryJsonObject =
							ContextVariableUtil.parseConfigurationVariables(
								searchRequestContext,
								clauseJsonObject.getJSONObject(
									ClauseConfigurationKeys.QUERY.
										getJsonKey()));

						Optional<Query> clauseOptional =
							clauseBuilder.buildClause(
								searchRequestContext, queryJsonObject);

						if (clauseOptional.isPresent()) {
							_addClause(
								searchRequestContext, searchRequestData,
								clauseJsonObject, clauseOptional.get());
						}
					}
					catch (IllegalArgumentException iae) {
						searchRequestContext.addMessage(
							new Message(
								Severity.ERROR, "core",
								"core.error.unknown-query-type",
								iae.getMessage(), iae, clauseJsonObject,
								CommonConfigurationKeys.TYPE.getJsonKey(),
								type));

						if (_log.isWarnEnabled()) {
							_log.warn(iae.getMessage(), iae);
						}
					}
					catch (JSONException je) {
						searchRequestContext.addMessage(
							new Message(
								Severity.ERROR, "core",
								"core.error.error-in-parsing-configuration-parameters",
								je.getMessage(), je, queryJsonObject, null,
								null));
						_log.error(je.getMessage(), je);
					}
					catch (Exception e) {
						searchRequestContext.addMessage(
							new Message(
								Severity.ERROR, "core",
								"core.error.unknown-clause-building-error",
								e.getMessage(), e, clauseJsonObject, null,
								null));
						_log.error(e.getMessage(), e);
					}
				}
			}
		}
	}

	protected void addFacetFilters(
		SearchRequestContext searchRequestContext,
		SearchRequestData searchRequestData, JSONObject aggregationJsonObject) {

		BooleanQuery facetPreFilterQuery = _queries.booleanQuery();

		BooleanQuery facetPostFilterQuery = _queries.booleanQuery();

		JSONObject facetConfigurationJsonObject =
			aggregationJsonObject.getJSONObject(
				AggregationConfigurationKeys.FACET.getJsonKey());

		for (Parameter parameter :
				searchRequestContext.getSearchParameterData().getParameters()) {

			String parameterName = facetConfigurationJsonObject.getString(
				CommonConfigurationKeys.PARAMETER_NAME.getJsonKey());

			if (!parameter.getName().equals("facet." + parameterName)) {
				continue;
			}

			try {
				boolean enabled = facetConfigurationJsonObject.getBoolean(
					FacetConfigurationKeys.ENABLED.getJsonKey());

				if (!enabled) {
					continue;
				}

				_addFacetFilterClauses(
					searchRequestContext, facetPreFilterQuery,
					facetPostFilterQuery, facetConfigurationJsonObject,
					parameter.getValue());
			}
			catch (Exception e) {
				searchRequestContext.addMessage(
					new Message(
						Severity.ERROR, "core",
						"core.error.unknown-error-in-creating-facet-filter",
						e.getMessage(), e, facetConfigurationJsonObject, null,
						null));

				_log.error(e.getMessage(), e);
			}

			if (facetPreFilterQuery.hasClauses()) {
				searchRequestData.getQuery(
				).addFilterQueryClauses(
					facetPreFilterQuery
				);
			}

			if (facetPostFilterQuery.hasClauses()) {
				searchRequestData.getPostFilterQuery(
				).addMustQueryClauses(
					facetPostFilterQuery
				);
			}
		}
	}

	protected void addQueryContributor(QueryContributor queryContributor) {
		_queryContributors.add(queryContributor);
	}

	protected boolean checkClauseConditions(
		SearchRequestContext searchRequestContext,
		JSONObject clauseConfigurationJsonObject) {

		JSONArray conditionsJsonArray =
			clauseConfigurationJsonObject.getJSONArray(
				ClauseConfigurationKeys.CONDITIONS.getJsonKey());

		if ((conditionsJsonArray == null) ||
			(conditionsJsonArray.length() == 0)) {

			return true;
		}

		boolean valid = false;

		for (int i = 0; i < conditionsJsonArray.length(); i++) {
			JSONObject conditionJsonObject = conditionsJsonArray.getJSONObject(
				i);

			String handler = conditionJsonObject.getString(
				ClauseConfigurationKeys.HANDLER.getJsonKey());

			try {
				ClauseConditionHandler clauseConditionHandler =
					_clauseConditionHandlerFactory.getHandler(handler);

				String operatorString = conditionJsonObject.getString(
					ClauseConfigurationKeys.OPERATOR.getJsonKey());

				Operator operator = _getOperator(operatorString);

				JSONObject configurationJsonObject =
					conditionJsonObject.getJSONObject(
						ClauseConfigurationKeys.CONFIGURATION.getJsonKey());

				boolean conditionTrue = clauseConditionHandler.isTrue(
					searchRequestContext, configurationJsonObject);

				if (operator.equals(Operator.AND) && !conditionTrue) {
					return false;
				}
				else if (operator.equals(Operator.NOT) && conditionTrue) {
					return false;
				}
				else if (conditionTrue) {
					valid = true;
				}
			}
			catch (IllegalArgumentException iae) {

				// TODO _getOperator too

				searchRequestContext.addMessage(
					new Message(
						Severity.ERROR, "core",
						"core.error.unknown-clause-condition-handler",
						iae.getMessage(), iae, conditionJsonObject,
						ClauseConfigurationKeys.HANDLER.getJsonKey(), handler));

				if (_log.isWarnEnabled()) {
					_log.warn(iae.getMessage(), iae);
				}
			}
			catch (Exception e) {
				searchRequestContext.addMessage(
					new Message(
						Severity.ERROR, "core",
						"core.error.unknown-clause-condition-error",
						e.getMessage(), e, conditionJsonObject, null, null));

				_log.error(e.getMessage(), e);
			}
		}

		return valid;
	}

	protected void processQueryContributors(
		SearchRequestContext searchRequestContext,
		SearchRequestData searchRequestData) {

		if (_log.isDebugEnabled()) {
			_log.debug("Processing query contributors.");
		}

		if (_queryContributors.isEmpty()) {
			return;
		}

		BooleanQuery booleanQuery = searchRequestData.getQuery();

		for (QueryContributor queryContributor : _queryContributors) {
			if (_isQueryContributorExcluded(
					searchRequestContext,
					queryContributor.getClass().getName())) {

				continue;
			}

			try {
				Optional<Query> contributorQueryOptional =
					queryContributor.build(searchRequestContext);

				if (contributorQueryOptional.isPresent()) {
					Query contributorQuery = contributorQueryOptional.get();

					Occur occur = queryContributor.getOccur();

					if (Occur.MUST.equals(occur)) {
						booleanQuery.addMustQueryClauses(contributorQuery);
					}
					else if (Occur.MUST_NOT.equals(occur)) {
						booleanQuery.addMustNotQueryClauses(contributorQuery);
					}
					else if (Occur.SHOULD.equals(occur)) {
						booleanQuery.addShouldQueryClauses(contributorQuery);
					}
				}
			}
			catch (SearchRequestDataException e) {
				_log.error(e.getMessage(), e);
			}
		}
	}

	protected void removeQueryContributor(QueryContributor queryContributor) {
		_queryContributors.remove(queryContributor);
	}

	private void _addClause(
		SearchRequestContext searchRequestContext,
		SearchRequestData searchRequestData, JSONObject queryJsonObject,
		Query clause) {

		ClauseContext clauseContext;
		String clauseContextString = queryJsonObject.getString(
			ClauseConfigurationKeys.CONTEXT.getJsonKey());

		try {
			clauseContext = _getClauseContext(clauseContextString);
		}
		catch (IllegalArgumentException iae) {
			searchRequestContext.addMessage(
				new Message(
					Severity.ERROR, "core", "core.error.unknown-clause-context",
					iae.getMessage(), iae, queryJsonObject,
					ClauseConfigurationKeys.CONTEXT.getJsonKey(),
					clauseContextString));

			if (_log.isWarnEnabled()) {
				_log.warn(iae.getMessage(), iae);
			}

			return;
		}

		Occur occur;
		String occurString = queryJsonObject.getString(
			ClauseConfigurationKeys.OCCUR.getJsonKey());

		try {
			occur = _getItemOccur(occurString);
		}
		catch (IllegalArgumentException iae) {
			searchRequestContext.addMessage(
				new Message(
					Severity.ERROR, "core", "core.error.unknown-occur-value",
					iae.getMessage(), iae, queryJsonObject,
					ClauseConfigurationKeys.OCCUR.getJsonKey(), occurString));

			return;
		}

		if (ClauseContext.POST_FILTER.equals(clauseContext)) {
			if (Occur.MUST.equals(occur)) {
				searchRequestData.getPostFilterQuery(
				).addMustQueryClauses(
					clause
				);
			}
			else if (Occur.MUST_NOT.equals(occur)) {
				searchRequestData.getPostFilterQuery(
				).addMustNotQueryClauses(
					clause
				);
			}
			else {
				searchRequestData.getPostFilterQuery(
				).addShouldQueryClauses(
					clause
				);
			}
		}
		else if (ClauseContext.PRE_FILTER.equals(clauseContext)) {
			searchRequestData.getQuery(
			).addFilterQueryClauses(clause);
		}
		else if (ClauseContext.QUERY.equals(clauseContext)) {
			if (Occur.MUST.equals(occur)) {
				searchRequestData.getQuery(
				).addMustQueryClauses(
					clause
				);
			}
			else if (Occur.MUST_NOT.equals(occur)) {
				searchRequestData.getQuery(
				).addMustNotQueryClauses(
					clause
				);
			}
			else {
				searchRequestData.getQuery(
				).addShouldQueryClauses(
					clause
				);
			}
		}
		else {

			// TODO RESCORES

		}
	}

	private void _addFacetFilterClauses(
		SearchRequestContext searchRequestContext,
		BooleanQuery facetPreFilterQuery, BooleanQuery facetPostFilterQuery,
		JSONObject facetConfigurationJsonObject, Object value) {

		BooleanQuery query = _queries.booleanQuery();

		String indexField = facetConfigurationJsonObject.getString(
			FacetConfigurationKeys.INDEX_FIELD.getJsonKey());

		if (Validator.isBlank(indexField)) {
			indexField = facetConfigurationJsonObject.getString(
				CommonConfigurationKeys.PARAMETER_NAME.getJsonKey());
		}

		String filterModeString = facetConfigurationJsonObject.getString(
			FacetConfigurationKeys.FILTER_MODE.getJsonKey(),
			FilterMode.PRE.getjsonValue());
		FilterMode filterMode;

		try {
			filterMode = _getFilterMode(filterModeString);
		}
		catch (IllegalArgumentException iae) {
			searchRequestContext.addMessage(
				new Message(
					Severity.ERROR, "core",
					"core.error.unknown-facet-filter-mode", iae.getMessage(),
					iae, facetConfigurationJsonObject,
					FacetConfigurationKeys.FILTER_MODE.getJsonKey(),
					filterModeString));

			return;
		}

		String multiValueOperatorString =
			facetConfigurationJsonObject.getString(
				FacetConfigurationKeys.MULTI_VALUE_OPERATOR.getJsonKey(),
				Operator.OR.getjsonValue());
		Operator multiValueOperator;

		try {
			multiValueOperator = _getOperator(multiValueOperatorString);
		}
		catch (IllegalArgumentException iae) {
			searchRequestContext.addMessage(
				new Message(
					Severity.ERROR, "core",
					"core.error.unknown-facet-multi-value-operator",
					iae.getMessage(), iae, facetConfigurationJsonObject,
					FacetConfigurationKeys.MULTI_VALUE_OPERATOR.getJsonKey(),
					multiValueOperatorString));

			return;
		}

		// TODO

		if (value instanceof String) {
			query.addMustQueryClauses(_queries.term(indexField, value));
		}
		else {
			String[] values = (String[])value;

			for (String val : values) {
				TermQuery condition = _queries.term(indexField, val);

				if (values.length > 1) {
					if (multiValueOperator.equals(Operator.AND)) {
						query.addMustQueryClauses(condition);
					}
					else {
						query.addShouldQueryClauses(condition);
					}
				}
				else {
					query.addMustQueryClauses(condition);
				}
			}
		}

		if (query.hasClauses()) {
			if (FilterMode.PRE.equals(filterMode)) {
				facetPreFilterQuery.addMustQueryClauses(query);
			}
			else {
				facetPostFilterQuery.addShouldQueryClauses(query);
			}
		}
	}

	private ClauseContext _getClauseContext(String context)
		throws IllegalArgumentException {

		context = StringUtil.toUpperCase(context);

		return ClauseContext.valueOf(context);
	}

	private FilterMode _getFilterMode(String filter)
		throws IllegalArgumentException {

		filter = StringUtil.toUpperCase(filter);

		return FilterMode.valueOf(filter);
	}

	private Occur _getItemOccur(String occur) throws IllegalArgumentException {
		occur = StringUtil.toUpperCase(occur);

		return Occur.valueOf(occur);
	}

	private Operator _getOperator(String operator)
		throws IllegalArgumentException {

		operator = StringUtil.toUpperCase(operator);

		return Operator.valueOf(operator);
	}

	private boolean _isQueryContributorExcluded(
		SearchRequestContext searchRequestContext, String name) {

		Optional<List<String>> excludedQueryContributorsOptional =
			searchRequestContext.getExcludeQueryContributors();

		if (!excludedQueryContributorsOptional.isPresent()) {
			return false;
		}

		if (excludedQueryContributorsOptional.filter(
				list -> list.contains(name) || list.contains("*")).
					isPresent()) {

			return true;
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SearchRequestDataBuilderImpl.class);

	@Reference
	private AggregationBuilderFactory _aggregationBuilderFactory;

	@Reference
	private ClauseBuilderFactory _clauseBuilderFactory;

	@Reference
	private ClauseConditionHandlerFactory _clauseConditionHandlerFactory;

	@Reference
	private Queries _queries;

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC, service = QueryContributor.class,
		unbind = "removeQueryContributor"
	)
	private volatile List<QueryContributor> _queryContributors =
		new ArrayList<>();

}