/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.legacy.query;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.service.Snapshot;
import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.QueryTerm;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.TermQuery;
import com.liferay.portal.kernel.search.TermRangeQuery;
import com.liferay.portal.kernel.search.WildcardQuery;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.FilterTranslator;
import com.liferay.portal.kernel.search.generic.DisMaxQuery;
import com.liferay.portal.kernel.search.generic.FuzzyQuery;
import com.liferay.portal.kernel.search.generic.MatchAllQuery;
import com.liferay.portal.kernel.search.generic.MatchQuery;
import com.liferay.portal.kernel.search.generic.MoreLikeThisQuery;
import com.liferay.portal.kernel.search.generic.MultiMatchQuery;
import com.liferay.portal.kernel.search.generic.NestedQuery;
import com.liferay.portal.kernel.search.generic.StringQuery;
import com.liferay.portal.kernel.search.query.QueryTranslator;
import com.liferay.portal.kernel.search.query.QueryVisitor;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.index.IndexNameBuilder;
import com.liferay.portal.search.opensearch2.internal.util.QueryUtil;
import com.liferay.portal.search.opensearch2.internal.util.SetterUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.opensearch.client.json.JsonData;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.query_dsl.BoolQuery;
import org.opensearch.client.opensearch._types.query_dsl.ChildScoreMode;
import org.opensearch.client.opensearch._types.query_dsl.Like;
import org.opensearch.client.opensearch._types.query_dsl.LikeDocument;
import org.opensearch.client.opensearch._types.query_dsl.MatchPhrasePrefixQuery;
import org.opensearch.client.opensearch._types.query_dsl.MatchPhraseQuery;
import org.opensearch.client.opensearch._types.query_dsl.Operator;
import org.opensearch.client.opensearch._types.query_dsl.Query;
import org.opensearch.client.opensearch._types.query_dsl.QueryBuilders;
import org.opensearch.client.opensearch._types.query_dsl.QueryStringQuery;
import org.opensearch.client.opensearch._types.query_dsl.QueryVariant;
import org.opensearch.client.opensearch._types.query_dsl.RangeQuery;
import org.opensearch.client.opensearch._types.query_dsl.TextQueryType;
import org.opensearch.client.opensearch._types.query_dsl.ZeroTermsQuery;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author André de Oliveira
 * @author Miguel Angelo Caldas Gallindo
 * @author Petteri Karttunen
 */
@Component(
	property = "search.engine.impl=OpenSearch", service = QueryTranslator.class
)
public class OpenSearchQueryTranslator
	implements QueryTranslator<QueryVariant>, QueryVisitor<QueryVariant> {

	@Override
	public QueryVariant translate(
		com.liferay.portal.kernel.search.Query query,
		SearchContext searchContext) {

		QueryVariant queryVariant = query.accept(this);

		if (queryVariant != null) {
			return queryVariant;
		}

		return QueryBuilders.queryString(
		).query(
			query.toString()
		).build();
	}

	@Override
	public QueryVariant visitQuery(BooleanQuery booleanQuery) {
		BoolQuery.Builder boolQueryBuilder = QueryBuilders.bool();

		List<BooleanClause<com.liferay.portal.kernel.search.Query>> clauses =
			booleanQuery.clauses();

		for (BooleanClause<com.liferay.portal.kernel.search.Query> clause :
				clauses) {

			_addClause(boolQueryBuilder, clause);
		}

		if (!booleanQuery.isDefaultBoost()) {
			boolQueryBuilder.boost(booleanQuery.getBoost());
		}

		BooleanFilter booleanFilter = booleanQuery.getPreBooleanFilter();

		if (booleanFilter == null) {
			return boolQueryBuilder.build();
		}

		// LPS-86537 The following conversion is present for backwards
		// compatibility with how Liferay's Indexer frameworks handles queries.
		// Ideally, we do not wrap the BooleanQuery with another BooleanQuery.

		BoolQuery.Builder wrapperBoolQueryBuilder = QueryBuilders.bool();

		if (!clauses.isEmpty()) {
			wrapperBoolQueryBuilder.must(new Query(boolQueryBuilder.build()));
		}

		FilterTranslator<QueryVariant> filterTranslator =
			_filterTranslatorSnapshot.get();

		if (filterTranslator == null) {
			_log.error(
				"Unable to translate boolean filter " + booleanFilter +
					" because filter translator is null");

			return boolQueryBuilder.build();
		}

		Query filterQuery = new Query(
			filterTranslator.translate(booleanFilter, null));

		wrapperBoolQueryBuilder.filter(filterQuery);

		return wrapperBoolQueryBuilder.build();
	}

	@Override
	public QueryVariant visitQuery(DisMaxQuery disMaxQuery) {
		org.opensearch.client.opensearch._types.query_dsl.DisMaxQuery.Builder
			disMaxQueryBuilder = QueryBuilders.disMax();

		if (!disMaxQuery.isDefaultBoost()) {
			disMaxQueryBuilder.boost(disMaxQuery.getBoost());
		}

		for (com.liferay.portal.kernel.search.Query query :
				disMaxQuery.getQueries()) {

			disMaxQueryBuilder.queries(new Query(query.accept(this)));
		}

		SetterUtil.setNotNullFloatAsDouble(
			disMaxQueryBuilder::tieBreaker, disMaxQuery.getTieBreaker());

		return disMaxQueryBuilder.build();
	}

	@Override
	public QueryVariant visitQuery(FuzzyQuery fuzzyQuery) {
		org.opensearch.client.opensearch._types.query_dsl.FuzzyQuery.Builder
			fuzzyQueryBuilder = QueryBuilders.fuzzy();

		fuzzyQueryBuilder.field(fuzzyQuery.getField());
		fuzzyQueryBuilder.value(FieldValue.of(fuzzyQuery.getValue()));

		if (!fuzzyQuery.isDefaultBoost()) {
			fuzzyQueryBuilder.boost(fuzzyQuery.getBoost());
		}

		SetterUtil.setNotNullValueAsString(
			fuzzyQueryBuilder::fuzziness, fuzzyQuery.getFuzziness());
		SetterUtil.setNotNullInteger(
			fuzzyQueryBuilder::maxExpansions, fuzzyQuery.getMaxExpansions());
		SetterUtil.setNotNullInteger(
			fuzzyQueryBuilder::prefixLength, fuzzyQuery.getPrefixLength());

		return fuzzyQueryBuilder.build();
	}

	@Override
	public QueryVariant visitQuery(MatchAllQuery matchAllQuery) {
		org.opensearch.client.opensearch._types.query_dsl.MatchAllQuery.Builder
			matchAllQueryBuilder = QueryBuilders.matchAll();

		if (!matchAllQuery.isDefaultBoost()) {
			matchAllQueryBuilder.boost(matchAllQuery.getBoost());
		}

		return matchAllQueryBuilder.build();
	}

	@Override
	public QueryVariant visitQuery(MatchQuery matchQuery) {
		String field = matchQuery.getField();

		MatchQuery.Type type = matchQuery.getType();

		String value = matchQuery.getValue();

		if (value.startsWith(StringPool.QUOTE) &&
			value.endsWith(StringPool.QUOTE)) {

			type = MatchQuery.Type.PHRASE;

			value = StringUtil.unquote(value);

			if (value.endsWith(StringPool.STAR)) {
				type = MatchQuery.Type.PHRASE_PREFIX;
			}
		}

		if ((type == null) || (type == MatchQuery.Type.BOOLEAN)) {
			return _translateMatchQuery(field, matchQuery, value);
		}
		else if (type == MatchQuery.Type.PHRASE) {
			return _translateMatchPhraseQuery(field, matchQuery, value);
		}
		else if (type == MatchQuery.Type.PHRASE_PREFIX) {
			return _translateMatchPhrasePrefixQuery(field, matchQuery, value);
		}

		throw new IllegalArgumentException("Invalid match query type " + type);
	}

	@Override
	public QueryVariant visitQuery(MoreLikeThisQuery moreLikeThisQuery) {
		org.opensearch.client.opensearch._types.query_dsl.MoreLikeThisQuery.
			Builder moreLikeThisQueryBuilder = QueryBuilders.moreLikeThis();

		moreLikeThisQueryBuilder.fields(moreLikeThisQuery.getFields());
		moreLikeThisQueryBuilder.like(_getLikes(moreLikeThisQuery));

		SetterUtil.setNotBlankString(
			moreLikeThisQueryBuilder::analyzer,
			moreLikeThisQuery.getAnalyzer());

		if (!moreLikeThisQuery.isDefaultBoost()) {
			moreLikeThisQueryBuilder.boost(moreLikeThisQuery.getBoost());
		}

		SetterUtil.setNotNullInteger(
			moreLikeThisQueryBuilder::maxDocFreq,
			moreLikeThisQuery.getMaxDocFrequency());
		SetterUtil.setNotNullInteger(
			moreLikeThisQueryBuilder::maxQueryTerms,
			moreLikeThisQuery.getMaxQueryTerms());
		SetterUtil.setNotNullInteger(
			moreLikeThisQueryBuilder::maxWordLength,
			moreLikeThisQuery.getMaxWordLength());
		SetterUtil.setNotNullInteger(
			moreLikeThisQueryBuilder::minDocFreq,
			moreLikeThisQuery.getMinDocFrequency());
		SetterUtil.setNotNullInteger(
			moreLikeThisQueryBuilder::minTermFreq,
			moreLikeThisQuery.getMinTermFrequency());
		SetterUtil.setNotBlankString(
			moreLikeThisQueryBuilder::minimumShouldMatch,
			moreLikeThisQuery.getMinShouldMatch());
		SetterUtil.setNotNullInteger(
			moreLikeThisQueryBuilder::minWordLength,
			moreLikeThisQuery.getMinWordLength());

		Collection<String> stopWords = moreLikeThisQuery.getStopWords();

		if (!stopWords.isEmpty()) {
			moreLikeThisQueryBuilder.stopWords(
				ListUtil.fromCollection(stopWords));
		}

		SetterUtil.setNotNullFloatAsDouble(
			moreLikeThisQueryBuilder::boostTerms,
			moreLikeThisQuery.getTermBoost());
		SetterUtil.setNotNullBoolean(
			moreLikeThisQueryBuilder::include,
			moreLikeThisQuery.isIncludeInput());

		return moreLikeThisQueryBuilder.build();
	}

	@Override
	public QueryVariant visitQuery(MultiMatchQuery multiMatchQuery) {
		org.opensearch.client.opensearch._types.query_dsl.MultiMatchQuery.
			Builder multiMatchQueryBuilder = QueryBuilders.multiMatch();

		multiMatchQueryBuilder.fields(
			QueryUtil.fieldsBoostsToFieldsWithBoosts(
				multiMatchQuery.getFieldsBoosts()));
		multiMatchQueryBuilder.query(multiMatchQuery.getValue());

		SetterUtil.setNotBlankString(
			multiMatchQueryBuilder::analyzer, multiMatchQuery.getAnalyzer());
		SetterUtil.setNotNullFloatAsDouble(
			multiMatchQueryBuilder::cutoffFrequency,
			multiMatchQuery.getCutOffFrequency());
		SetterUtil.setNotBlankString(
			multiMatchQueryBuilder::fuzziness, multiMatchQuery.getFuzziness());

		if (multiMatchQuery.getFuzzyRewriteMethod() != null) {
			multiMatchQueryBuilder.fuzzyRewrite(
				_translateMatchQueryRewriteMethod(
					multiMatchQuery.getFuzzyRewriteMethod()));
		}

		SetterUtil.setNotNullInteger(
			multiMatchQueryBuilder::maxExpansions,
			multiMatchQuery.getMaxExpansions());
		SetterUtil.setNotBlankString(
			multiMatchQueryBuilder::minimumShouldMatch,
			multiMatchQuery.getMinShouldMatch());

		if (multiMatchQuery.getOperator() != null) {
			multiMatchQueryBuilder.operator(
				_translateMatchQueryOperator(multiMatchQuery.getOperator()));
		}

		SetterUtil.setNotNullInteger(
			multiMatchQueryBuilder::prefixLength,
			multiMatchQuery.getPrefixLength());
		SetterUtil.setNotNullInteger(
			multiMatchQueryBuilder::slop, multiMatchQuery.getSlop());

		if (multiMatchQuery.getType() != null) {
			multiMatchQueryBuilder.type(
				_translateMultiMatchQueryType(multiMatchQuery.getType()));
		}

		if (multiMatchQuery.getZeroTermsQuery() != null) {
			multiMatchQueryBuilder.zeroTermsQuery(
				_translateMatchQueryZeroTermsQuery(
					multiMatchQuery.getZeroTermsQuery()));
		}

		if (!multiMatchQuery.isDefaultBoost()) {
			multiMatchQueryBuilder.boost(multiMatchQuery.getBoost());
		}

		SetterUtil.setNotNullBoolean(
			multiMatchQueryBuilder::lenient, multiMatchQuery.isLenient());

		return multiMatchQueryBuilder.build();
	}

	@Override
	public QueryVariant visitQuery(NestedQuery nestedQuery) {
		org.opensearch.client.opensearch._types.query_dsl.NestedQuery.Builder
			nestedQueryBuilder = QueryBuilders.nested();

		com.liferay.portal.kernel.search.Query query = nestedQuery.getQuery();

		nestedQueryBuilder.path(nestedQuery.getPath());
		nestedQueryBuilder.query(new Query(query.accept(this)));
		nestedQueryBuilder.scoreMode(ChildScoreMode.Sum);

		if (!nestedQuery.isDefaultBoost()) {
			nestedQueryBuilder.boost(nestedQuery.getBoost());
		}

		return nestedQueryBuilder.build();
	}

	@Override
	public QueryVariant visitQuery(StringQuery stringQuery) {
		QueryStringQuery.Builder queryStringQueryBuilder =
			QueryBuilders.queryString();

		queryStringQueryBuilder.query(stringQuery.getQuery());

		if (!stringQuery.isDefaultBoost()) {
			queryStringQueryBuilder.boost(stringQuery.getBoost());
		}

		return queryStringQueryBuilder.build();
	}

	@Override
	public QueryVariant visitQuery(TermQuery termQuery) {
		org.opensearch.client.opensearch._types.query_dsl.TermQuery.Builder
			termQueryBuilder = QueryBuilders.term();

		QueryTerm queryTerm = termQuery.getQueryTerm();

		termQueryBuilder.field(queryTerm.getField());
		termQueryBuilder.value(FieldValue.of(queryTerm.getValue()));

		if (!termQuery.isDefaultBoost()) {
			termQueryBuilder.boost(termQuery.getBoost());
		}

		return termQueryBuilder.build();
	}

	@Override
	public QueryVariant visitQuery(TermRangeQuery termRangeQuery) {
		RangeQuery.Builder rangeQueryBuilder = QueryBuilders.range();

		rangeQueryBuilder.field(termRangeQuery.getField());
		rangeQueryBuilder.from(JsonData.of(termRangeQuery.getLowerTerm()));

		if (!termRangeQuery.isDefaultBoost()) {
			rangeQueryBuilder.boost(termRangeQuery.getBoost());
		}

		QueryUtil.setRanges(
			termRangeQuery.includesLower(), termRangeQuery.includesUpper(),
			termRangeQuery.getLowerTerm(), rangeQueryBuilder,
			termRangeQuery.getUpperTerm());

		return rangeQueryBuilder.build();
	}

	@Override
	public QueryVariant visitQuery(WildcardQuery wildcardQuery) {
		org.opensearch.client.opensearch._types.query_dsl.WildcardQuery.Builder
			wildcardQueryBuilder = QueryBuilders.wildcard();

		QueryTerm queryTerm = wildcardQuery.getQueryTerm();

		wildcardQueryBuilder.field(queryTerm.getField());
		wildcardQueryBuilder.value(queryTerm.getValue());

		if (!wildcardQuery.isDefaultBoost()) {
			wildcardQueryBuilder.boost(wildcardQuery.getBoost());
		}

		return wildcardQueryBuilder.build();
	}

	private void _addClause(
		BoolQuery.Builder boolQueryBuilder,
		BooleanClause<com.liferay.portal.kernel.search.Query> clause) {

		BooleanClauseOccur booleanClauseOccur = clause.getBooleanClauseOccur();

		com.liferay.portal.kernel.search.Query query = clause.getClause();

		Query translatedQuery = new Query(query.accept(this));

		if (booleanClauseOccur.equals(BooleanClauseOccur.MUST)) {
			boolQueryBuilder.must(translatedQuery);

			return;
		}

		if (booleanClauseOccur.equals(BooleanClauseOccur.MUST_NOT)) {
			boolQueryBuilder.mustNot(translatedQuery);

			return;
		}

		if (booleanClauseOccur.equals(BooleanClauseOccur.SHOULD)) {
			boolQueryBuilder.should(translatedQuery);

			return;
		}

		throw new IllegalArgumentException(
			"Invalid Boolean clause occur " + booleanClauseOccur);
	}

	private List<Like> _getLikes(MoreLikeThisQuery moreLikeThisQuery) {
		List<Like> likes = new ArrayList<>();

		if (moreLikeThisQuery.getDocumentUIDs() == null) {
			return likes;
		}

		String indexName = _indexNameBuilder.getIndexName(
			moreLikeThisQuery.getCompanyId());

		for (String documentUID : moreLikeThisQuery.getDocumentUIDs()) {
			likes.add(
				Like.of(
					l -> l.document(
						LikeDocument.of(
							ld -> ld.id(
								documentUID
							).index(
								indexName
							)))));
		}

		if (Validator.isNotNull(moreLikeThisQuery.getLikeText())) {
			likes.add(Like.of(l -> l.text(moreLikeThisQuery.getLikeText())));
		}

		return likes;
	}

	private QueryVariant _translateMatchPhrasePrefixQuery(
		String field, MatchQuery matchQuery, String value) {

		MatchPhrasePrefixQuery.Builder matchPhrasePrefixQueryBuilder =
			QueryBuilders.matchPhrasePrefix();

		matchPhrasePrefixQueryBuilder.field(field);
		matchPhrasePrefixQueryBuilder.query(value);

		SetterUtil.setNotBlankString(
			matchPhrasePrefixQueryBuilder::analyzer, matchQuery.getAnalyzer());

		if (!matchQuery.isDefaultBoost()) {
			matchPhrasePrefixQueryBuilder.boost(matchQuery.getBoost());
		}

		SetterUtil.setNotNullInteger(
			matchPhrasePrefixQueryBuilder::maxExpansions,
			matchQuery.getMaxExpansions());
		SetterUtil.setNotNullInteger(
			matchPhrasePrefixQueryBuilder::slop, matchQuery.getSlop());

		return matchPhrasePrefixQueryBuilder.build();
	}

	private QueryVariant _translateMatchPhraseQuery(
		String field, MatchQuery matchQuery, String value) {

		MatchPhraseQuery.Builder matchPhraseQueryBuilder =
			QueryBuilders.matchPhrase();

		matchPhraseQueryBuilder.field(field);
		matchPhraseQueryBuilder.query(value);

		SetterUtil.setNotBlankString(
			matchPhraseQueryBuilder::analyzer, matchQuery.getAnalyzer());

		if (!matchQuery.isDefaultBoost()) {
			matchPhraseQueryBuilder.boost(matchQuery.getBoost());
		}

		SetterUtil.setNotNullInteger(
			matchPhraseQueryBuilder::slop, matchQuery.getSlop());

		return matchPhraseQueryBuilder.build();
	}

	private QueryVariant _translateMatchQuery(
		String field, MatchQuery matchQuery, String value) {

		org.opensearch.client.opensearch._types.query_dsl.MatchQuery.Builder
			matchQueryBuilder = QueryBuilders.match();

		matchQueryBuilder.field(field);
		matchQueryBuilder.query(FieldValue.of(value));

		SetterUtil.setNotBlankString(
			matchQueryBuilder::analyzer, matchQuery.getAnalyzer());

		if (!matchQuery.isDefaultBoost()) {
			matchQueryBuilder.boost(matchQuery.getBoost());
		}

		SetterUtil.setNotNullFloatAsDouble(
			matchQueryBuilder::cutoffFrequency,
			matchQuery.getCutOffFrequency());
		SetterUtil.setNotNullValueAsString(
			matchQueryBuilder::fuzziness, matchQuery.getFuzziness());

		if (matchQuery.getFuzzyRewriteMethod() != null) {
			matchQueryBuilder.fuzzyRewrite(
				_translateMatchQueryRewriteMethod(
					matchQuery.getFuzzyRewriteMethod()));
		}

		SetterUtil.setNotNullInteger(
			matchQueryBuilder::maxExpansions, matchQuery.getMaxExpansions());
		SetterUtil.setNotBlankString(
			matchQueryBuilder::minimumShouldMatch,
			matchQuery.getMinShouldMatch());

		if (matchQuery.getOperator() != null) {
			matchQueryBuilder.operator(
				_translateMatchQueryOperator(matchQuery.getOperator()));
		}

		SetterUtil.setNotNullInteger(
			matchQueryBuilder::prefixLength, matchQuery.getPrefixLength());

		if (matchQuery.getZeroTermsQuery() != null) {
			matchQueryBuilder.zeroTermsQuery(
				_translateMatchQueryZeroTermsQuery(
					matchQuery.getZeroTermsQuery()));
		}

		SetterUtil.setNotNullBoolean(
			matchQueryBuilder::fuzzyTranspositions,
			matchQuery.isFuzzyTranspositions());
		SetterUtil.setNotNullBoolean(
			matchQueryBuilder::lenient, matchQuery.isLenient());

		return matchQueryBuilder.build();
	}

	private Operator _translateMatchQueryOperator(
		MatchQuery.Operator matchQueryOperator) {

		if (matchQueryOperator == MatchQuery.Operator.AND) {
			return Operator.And;
		}
		else if (matchQueryOperator == MatchQuery.Operator.OR) {
			return Operator.Or;
		}

		throw new IllegalArgumentException(
			"Invalid operator " + matchQueryOperator);
	}

	private String _translateMatchQueryRewriteMethod(
		MatchQuery.RewriteMethod rewriteMethod) {

		if (rewriteMethod == MatchQuery.RewriteMethod.CONSTANT_SCORE_AUTO) {
			return "constant_score_auto";
		}
		else if (rewriteMethod ==
					MatchQuery.RewriteMethod.CONSTANT_SCORE_BOOLEAN) {

			return "constant_score_boolean";
		}
		else if (rewriteMethod ==
					MatchQuery.RewriteMethod.CONSTANT_SCORE_FILTER) {

			return "constant_score_filter";
		}
		else if (rewriteMethod == MatchQuery.RewriteMethod.SCORING_BOOLEAN) {
			return "scoring_boolean";
		}
		else if (rewriteMethod == MatchQuery.RewriteMethod.TOP_TERMS_N) {
			return "top_terms_N";
		}
		else if (rewriteMethod == MatchQuery.RewriteMethod.TOP_TERMS_BOOST_N) {
			return "top_terms_boost_N";
		}

		throw new IllegalArgumentException(
			"Invalid rewrite method " + rewriteMethod);
	}

	private ZeroTermsQuery _translateMatchQueryZeroTermsQuery(
		MatchQuery.ZeroTermsQuery zeroTermsQuery) {

		if (zeroTermsQuery == MatchQuery.ZeroTermsQuery.ALL) {
			return ZeroTermsQuery.All;
		}
		else if (zeroTermsQuery == MatchQuery.ZeroTermsQuery.NONE) {
			return ZeroTermsQuery.None;
		}

		throw new IllegalArgumentException(
			"Invalid zero terms query " + zeroTermsQuery);
	}

	private TextQueryType _translateMultiMatchQueryType(
		MultiMatchQuery.Type type) {

		if (type == MultiMatchQuery.Type.BEST_FIELDS) {
			return TextQueryType.BestFields;
		}
		else if (type == MultiMatchQuery.Type.CROSS_FIELDS) {
			return TextQueryType.CrossFields;
		}
		else if (type == MultiMatchQuery.Type.MOST_FIELDS) {
			return TextQueryType.MostFields;
		}
		else if (type == MultiMatchQuery.Type.PHRASE) {
			return TextQueryType.Phrase;
		}
		else if (type == MultiMatchQuery.Type.PHRASE_PREFIX) {
			return TextQueryType.PhrasePrefix;
		}

		throw new IllegalArgumentException(
			"Invalid multi match query type " + type);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OpenSearchQueryTranslator.class);

	private static final Snapshot<FilterTranslator<QueryVariant>>
		_filterTranslatorSnapshot = new Snapshot<>(
			OpenSearchQueryTranslator.class,
			Snapshot.cast(FilterTranslator.class),
			"(search.engine.impl=OpenSearch)", true);

	@Reference
	private IndexNameBuilder _indexNameBuilder;

}