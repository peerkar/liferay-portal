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

package com.liferay.portal.search.tuning.gsearch.impl.internal.clause.builder;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.query.Operator;
import com.liferay.portal.search.query.Queries;
import com.liferay.portal.search.query.Query;
import com.liferay.portal.search.query.StringQuery;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.query.SimpleQueryStringConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.spi.clause.ClauseBuilder;

import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, property = "type=simple_query_string",
	service = ClauseBuilder.class
)
public class SimpleQueryStringClauseBuilder implements ClauseBuilder {

	@Override
	public Optional<Query> buildClause(
		SearchRequestContext queryContext, JSONObject configurationJsonObject) {

		String keywords = configurationJsonObject.getString(
			SimpleQueryStringConfigurationKeys.QUERY.getJsonKey());

		if (Validator.isBlank(keywords)) {
			keywords = queryContext.getKeywords();
		}

		StringQuery simpleQueryString = _queries.string(keywords);

		if (configurationJsonObject.has(
				SimpleQueryStringConfigurationKeys.ANALYZE_WILDCARD.
					getJsonKey())) {

			simpleQueryString.setAnalyzeWildcard(
				configurationJsonObject.getBoolean(
					SimpleQueryStringConfigurationKeys.ANALYZE_WILDCARD.
						getJsonKey()));
		}

		if (configurationJsonObject.has(
				SimpleQueryStringConfigurationKeys.ANALYZER.getJsonKey())) {

			simpleQueryString.setAnalyzer(
				SimpleQueryStringConfigurationKeys.ANALYZER.getJsonKey());
		}

		if (configurationJsonObject.has(
				SimpleQueryStringConfigurationKeys.
					AUTO_GENERATE_SYNONYMS_PHRASE_QUERY.getJsonKey())) {

			simpleQueryString.setAutoGenerateSynonymsPhraseQuery(
				configurationJsonObject.getBoolean(
					SimpleQueryStringConfigurationKeys.
						AUTO_GENERATE_SYNONYMS_PHRASE_QUERY.getJsonKey()));
		}

		if (configurationJsonObject.has(
				SimpleQueryStringConfigurationKeys.DEFAULT_OPERATOR.
					getJsonKey())) {

			String operator = configurationJsonObject.getString(
				SimpleQueryStringConfigurationKeys.DEFAULT_OPERATOR.
					getJsonKey(),
				Operator.AND.name());

			if (operator.equalsIgnoreCase(Operator.AND.name())) {
				simpleQueryString.setDefaultOperator(Operator.AND);
			}
			else {
				simpleQueryString.setDefaultOperator(Operator.OR);
			}
		}

		if (configurationJsonObject.has(SimpleQueryStringConfigurationKeys.FIELDS.getJsonKey())) {
		
			JSONArray fields = configurationJsonObject.getJSONArray(
				SimpleQueryStringConfigurationKeys.FIELDS.getJsonKey());
			
			for (int i = 0; i < fields.length(); i++) {
				JSONObject field = fields.getJSONObject(i);
	
				String fieldName = field.getString(
					SimpleQueryStringConfigurationKeys.FIELD.getJsonKey());
	
				float boost = GetterUtil.getFloat(
					field.getString(
						SimpleQueryStringConfigurationKeys.BOOST.getJsonKey()),
					1.0F);
	
				simpleQueryString.addField(fieldName, boost);
			}
		}

		if (configurationJsonObject.has(
				SimpleQueryStringConfigurationKeys.FUZZY_MAX_EXPANSIONS.
					getJsonKey())) {

			simpleQueryString.setFuzzyMaxExpansions(
				configurationJsonObject.getInt(
					SimpleQueryStringConfigurationKeys.FUZZY_MAX_EXPANSIONS.
						getJsonKey()));
		}

		if (configurationJsonObject.has(
				SimpleQueryStringConfigurationKeys.FUZZY_PREFIX_LENGTH.
					getJsonKey())) {

			simpleQueryString.setFuzzyPrefixLength(
				configurationJsonObject.getInt(
					SimpleQueryStringConfigurationKeys.FUZZY_PREFIX_LENGTH.
						getJsonKey()));
		}

		if (configurationJsonObject.has(
				SimpleQueryStringConfigurationKeys.FUZZY_TRANSPOSITIONS.
					getJsonKey())) {

			simpleQueryString.setLenient(
				configurationJsonObject.getBoolean(
					SimpleQueryStringConfigurationKeys.FUZZY_TRANSPOSITIONS.
						getJsonKey()));
		}

		if (configurationJsonObject.has(
				SimpleQueryStringConfigurationKeys.LENIENT.getJsonKey())) {

			simpleQueryString.setLenient(
				configurationJsonObject.getBoolean(
					SimpleQueryStringConfigurationKeys.LENIENT.getJsonKey()));
		}

		if (configurationJsonObject.has(
				SimpleQueryStringConfigurationKeys.QUOTE_FIELD_SUFFIX.
					getJsonKey())) {

			simpleQueryString.setQuoteFieldSuffix(
				configurationJsonObject.getString(
					SimpleQueryStringConfigurationKeys.QUOTE_FIELD_SUFFIX.
						getJsonKey()));
		}

		return Optional.of(simpleQueryString);
	}

	@Reference
	private Queries _queries;

}