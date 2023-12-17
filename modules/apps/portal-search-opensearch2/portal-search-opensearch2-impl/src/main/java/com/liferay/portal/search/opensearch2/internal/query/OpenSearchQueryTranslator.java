/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.query;

import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.search.geolocation.Coordinate;
import com.liferay.portal.search.geolocation.GeoDistance;
import com.liferay.portal.search.geolocation.Shape;
import com.liferay.portal.search.opensearch2.internal.geolocation.GeoTranslator;
import com.liferay.portal.search.opensearch2.internal.query.function.score.OpenSearchScoreFunctionTranslator;
import com.liferay.portal.search.opensearch2.internal.script.ScriptTranslator;
import com.liferay.portal.search.opensearch2.internal.util.ConversionUtil;
import com.liferay.portal.search.opensearch2.internal.util.QueryUtil;
import com.liferay.portal.search.opensearch2.internal.util.SetterUtil;
import com.liferay.portal.search.query.BooleanQuery;
import com.liferay.portal.search.query.BoostingQuery;
import com.liferay.portal.search.query.CommonTermsQuery;
import com.liferay.portal.search.query.ConstantScoreQuery;
import com.liferay.portal.search.query.DateRangeTermQuery;
import com.liferay.portal.search.query.DisMaxQuery;
import com.liferay.portal.search.query.ExistsQuery;
import com.liferay.portal.search.query.FunctionScoreQuery;
import com.liferay.portal.search.query.FuzzyQuery;
import com.liferay.portal.search.query.GeoBoundingBoxQuery;
import com.liferay.portal.search.query.GeoDistanceQuery;
import com.liferay.portal.search.query.GeoDistanceRangeQuery;
import com.liferay.portal.search.query.GeoPolygonQuery;
import com.liferay.portal.search.query.GeoShapeQuery;
import com.liferay.portal.search.query.IdsQuery;
import com.liferay.portal.search.query.MatchAllQuery;
import com.liferay.portal.search.query.MatchPhrasePrefixQuery;
import com.liferay.portal.search.query.MatchPhraseQuery;
import com.liferay.portal.search.query.MatchQuery;
import com.liferay.portal.search.query.MoreLikeThisQuery;
import com.liferay.portal.search.query.MultiMatchQuery;
import com.liferay.portal.search.query.NestedQuery;
import com.liferay.portal.search.query.Operator;
import com.liferay.portal.search.query.PercolateQuery;
import com.liferay.portal.search.query.PrefixQuery;
import com.liferay.portal.search.query.Query;
import com.liferay.portal.search.query.QueryTranslator;
import com.liferay.portal.search.query.QueryVisitor;
import com.liferay.portal.search.query.RangeTermQuery;
import com.liferay.portal.search.query.RegexQuery;
import com.liferay.portal.search.query.ScriptQuery;
import com.liferay.portal.search.query.SimpleStringQuery;
import com.liferay.portal.search.query.StringQuery;
import com.liferay.portal.search.query.TermQuery;
import com.liferay.portal.search.query.TermsQuery;
import com.liferay.portal.search.query.TermsSetQuery;
import com.liferay.portal.search.query.WildcardQuery;
import com.liferay.portal.search.query.WrapperQuery;
import com.liferay.portal.search.query.function.CombineFunction;
import com.liferay.portal.search.query.geolocation.ShapeRelation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.function.Consumer;

import org.opensearch.client.json.JsonData;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.GeoLocation;
import org.opensearch.client.opensearch._types.GeoShapeRelation;
import org.opensearch.client.opensearch._types.query_dsl.BoolQuery;
import org.opensearch.client.opensearch._types.query_dsl.ChildScoreMode;
import org.opensearch.client.opensearch._types.query_dsl.FieldLookup;
import org.opensearch.client.opensearch._types.query_dsl.FunctionBoostMode;
import org.opensearch.client.opensearch._types.query_dsl.FunctionScore;
import org.opensearch.client.opensearch._types.query_dsl.FunctionScoreMode;
import org.opensearch.client.opensearch._types.query_dsl.GeoPolygonPoints;
import org.opensearch.client.opensearch._types.query_dsl.GeoShapeFieldQuery;
import org.opensearch.client.opensearch._types.query_dsl.Like;
import org.opensearch.client.opensearch._types.query_dsl.LikeDocument;
import org.opensearch.client.opensearch._types.query_dsl.QueryBuilders;
import org.opensearch.client.opensearch._types.query_dsl.QueryStringQuery;
import org.opensearch.client.opensearch._types.query_dsl.QueryVariant;
import org.opensearch.client.opensearch._types.query_dsl.RangeQuery;
import org.opensearch.client.opensearch._types.query_dsl.RangeRelation;
import org.opensearch.client.opensearch._types.query_dsl.RegexpQuery;
import org.opensearch.client.opensearch._types.query_dsl.SimpleQueryStringQuery;
import org.opensearch.client.opensearch._types.query_dsl.TextQueryType;
import org.opensearch.client.opensearch._types.query_dsl.ZeroTermsQuery;

import org.osgi.service.component.annotations.Component;

/**
 * @author Michael C. Han
 * @author Petteri Karttunen
 */
@Component(
	property = "search.engine.impl=OpenSearch", service = QueryTranslator.class
)
public class OpenSearchQueryTranslator
	implements QueryTranslator<QueryVariant>, QueryVisitor<QueryVariant> {

	@Override
	public QueryVariant translate(Query query) {
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
	public QueryVariant visit(BooleanQuery booleanQuery) {
		BoolQuery.Builder boolQueryBuilder = QueryBuilders.bool();

		boolQueryBuilder.queryName(booleanQuery.getQueryName());

		SetterUtil.setNotNullFloat(
			boolQueryBuilder::boost, booleanQuery.getBoost());

		_processBooleanQueryClauses(
			booleanQuery.getMustQueryClauses(), boolQueryBuilder::must);

		_processBooleanQueryClauses(
			booleanQuery.getMustNotQueryClauses(), boolQueryBuilder::mustNot);

		_processBooleanQueryClauses(
			booleanQuery.getShouldQueryClauses(), boolQueryBuilder::should);

		_processBooleanQueryClauses(
			booleanQuery.getFilterQueryClauses(), boolQueryBuilder::filter);

		if (booleanQuery.getMinimumShouldMatch() != null) {
			boolQueryBuilder.minimumShouldMatch(
				String.valueOf(booleanQuery.getMinimumShouldMatch()));
		}

		return boolQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(BoostingQuery boostingQuery) {
		org.opensearch.client.opensearch._types.query_dsl.BoostingQuery.Builder
			boostingQueryBuilder = QueryBuilders.boosting();

		SetterUtil.setNotNullFloat(
			boostingQueryBuilder::boost, boostingQuery.getBoost());

		Query negativeQuery = boostingQuery.getNegativeQuery();

		boostingQueryBuilder.negative(
			new org.opensearch.client.opensearch._types.query_dsl.Query(
				negativeQuery.accept(this)));

		Query positiveQuery = boostingQuery.getPositiveQuery();

		boostingQueryBuilder.positive(
			new org.opensearch.client.opensearch._types.query_dsl.Query(
				positiveQuery.accept(this)));

		SetterUtil.setNotNullFloatAsDouble(
			boostingQueryBuilder::negativeBoost,
			boostingQuery.getNegativeBoost());

		return boostingQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(CommonTermsQuery commonTermsQuery) {
		org.opensearch.client.opensearch._types.query_dsl.CommonTermsQuery.
			Builder commonTermsQueryBuilder =
				new org.opensearch.client.opensearch._types.query_dsl.
					CommonTermsQuery.Builder();

		commonTermsQueryBuilder.field(commonTermsQuery.getField());
		commonTermsQueryBuilder.query(commonTermsQuery.getText());

		SetterUtil.setNotBlankString(
			commonTermsQueryBuilder::analyzer, commonTermsQuery.getAnalyzer());
		SetterUtil.setNotNullFloat(
			commonTermsQueryBuilder::boost, commonTermsQuery.getBoost());
		SetterUtil.setNotNullFloatAsDouble(
			commonTermsQueryBuilder::cutoffFrequency,
			commonTermsQuery.getCutoffFrequency());

		if (commonTermsQuery.getHighFreqOperator() != null) {
			commonTermsQueryBuilder.highFreqOperator(
				_translateOperator(commonTermsQuery.getHighFreqOperator()));
		}

		if (commonTermsQuery.getLowFreqOperator() != null) {
			commonTermsQueryBuilder.lowFreqOperator(
				_translateOperator(commonTermsQuery.getLowFreqOperator()));
		}

		return commonTermsQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(ConstantScoreQuery constantScoreQuery) {
		org.opensearch.client.opensearch._types.query_dsl.ConstantScoreQuery.
			Builder constantScoreQueryBuilder =
				new org.opensearch.client.opensearch._types.query_dsl.
					ConstantScoreQuery.Builder();

		SetterUtil.setNotNullFloat(
			constantScoreQueryBuilder::boost, constantScoreQuery.getBoost());

		Query query = constantScoreQuery.getQuery();

		constantScoreQueryBuilder.filter(
			new org.opensearch.client.opensearch._types.query_dsl.Query(
				query.accept(this)));

		return constantScoreQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(DateRangeTermQuery dateRangeTermQuery) {
		RangeQuery.Builder rangeQueryBuilder = new RangeQuery.Builder();

		SetterUtil.setNotNullFloat(
			rangeQueryBuilder::boost, dateRangeTermQuery.getBoost());

		rangeQueryBuilder.field(dateRangeTermQuery.getField());

		QueryUtil.setRanges(
			dateRangeTermQuery.isIncludesLower(),
			dateRangeTermQuery.isIncludesUpper(),
			dateRangeTermQuery.getLowerBound(), rangeQueryBuilder,
			dateRangeTermQuery.getUpperBound());

		SetterUtil.setNotBlankString(
			rangeQueryBuilder::format, dateRangeTermQuery.getDateFormat());

		TimeZone timeZone = dateRangeTermQuery.getTimeZone();

		if (timeZone != null) {
			rangeQueryBuilder.timeZone(timeZone.getID());
		}

		return rangeQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(DisMaxQuery disMaxQuery) {
		org.opensearch.client.opensearch._types.query_dsl.DisMaxQuery.Builder
			disMaxQueryBuilder = QueryBuilders.disMax();

		SetterUtil.setNotNullFloat(
			disMaxQueryBuilder::boost, disMaxQuery.getBoost());

		for (Query query : disMaxQuery.getQueries()) {
			disMaxQueryBuilder.queries(
				new org.opensearch.client.opensearch._types.query_dsl.Query(
					query.accept(this)));
		}

		SetterUtil.setNotNullFloatAsDouble(
			disMaxQueryBuilder::tieBreaker, disMaxQuery.getTieBreaker());

		return disMaxQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(ExistsQuery existsQuery) {
		org.opensearch.client.opensearch._types.query_dsl.ExistsQuery.Builder
			existsQueryBuilder = QueryBuilders.exists();

		existsQueryBuilder.field(existsQuery.getField());

		SetterUtil.setNotNullFloat(
			existsQueryBuilder::boost, existsQuery.getBoost());

		return existsQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(FunctionScoreQuery functionScoreQuery) {
		org.opensearch.client.opensearch._types.query_dsl.FunctionScoreQuery.
			Builder functionScoreQueryBuilder = QueryBuilders.functionScore();

		functionScoreQueryBuilder.query(
			new org.opensearch.client.opensearch._types.query_dsl.Query(
				translate(functionScoreQuery.getQuery())));

		ListUtil.isNotEmptyForEach(
			functionScoreQuery.getFilterQueryScoreFunctionHolders(),
			filterQueryScoreFunctionHolder -> {
				OpenSearchScoreFunctionTranslator scoreFunctionTranslator =
					new OpenSearchScoreFunctionTranslator(
						filterQueryScoreFunctionHolder, this);

				FunctionScore functionScore =
					scoreFunctionTranslator.translate();

				if (functionScore != null) {
					functionScoreQueryBuilder.functions(functionScore);
				}
			});

		SetterUtil.setNotNullFloat(
			functionScoreQueryBuilder::boost, functionScoreQuery.getBoost());

		if (functionScoreQuery.getCombineFunction() != null) {
			functionScoreQueryBuilder.boostMode(
				_translateCombineFunction(
					functionScoreQuery.getCombineFunction()));
		}

		SetterUtil.setNotNullFloatAsDouble(
			functionScoreQueryBuilder::maxBoost,
			functionScoreQuery.getMaxBoost());
		SetterUtil.setNotNullFloatAsDouble(
			functionScoreQueryBuilder::minScore,
			functionScoreQuery.getMinScore());

		if (functionScoreQuery.getScoreMode() != null) {
			functionScoreQueryBuilder.scoreMode(
				_translateFunctionScoreQueryScoreMore(
					functionScoreQuery.getScoreMode()));
		}

		return functionScoreQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(FuzzyQuery fuzzyQuery) {
		org.opensearch.client.opensearch._types.query_dsl.FuzzyQuery.Builder
			fuzzyQueryBuilder = QueryBuilders.fuzzy();

		fuzzyQueryBuilder.field(fuzzyQuery.getField());
		fuzzyQueryBuilder.value(FieldValue.of(fuzzyQuery.getValue()));

		SetterUtil.setNotNullFloat(
			fuzzyQueryBuilder::boost, fuzzyQuery.getBoost());
		SetterUtil.setNotNullValueAsString(
			fuzzyQueryBuilder::fuzziness, fuzzyQuery.getFuzziness());
		SetterUtil.setNotNullInteger(
			fuzzyQueryBuilder::maxExpansions, fuzzyQuery.getMaxExpansions());
		SetterUtil.setNotNullInteger(
			fuzzyQueryBuilder::prefixLength, fuzzyQuery.getPrefixLength());
		SetterUtil.setNotBlankString(
			fuzzyQueryBuilder::rewrite, fuzzyQuery.getRewrite());
		SetterUtil.setNotNullBoolean(
			fuzzyQueryBuilder::transpositions, fuzzyQuery.getTranspositions());

		return fuzzyQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(GeoBoundingBoxQuery geoBoundingBoxQuery) {
		org.opensearch.client.opensearch._types.query_dsl.GeoBoundingBoxQuery.
			Builder geoBoundingBoxQueryBuilder = QueryBuilders.geoBoundingBox();

		geoBoundingBoxQueryBuilder.field(geoBoundingBoxQuery.getField());

		SetterUtil.setNotNullFloat(
			geoBoundingBoxQueryBuilder::boost, geoBoundingBoxQuery.getBoost());

		geoBoundingBoxQueryBuilder.boundingBox(
			_geoTranslator.toGeoBounds(
				geoBoundingBoxQuery.getTopLeftGeoLocationPoint(),
				geoBoundingBoxQuery.getBottomRightGeoLocationPoint()));

		if (geoBoundingBoxQuery.getGeoExecType() != null) {
			geoBoundingBoxQueryBuilder.type(
				_geoTranslator.translateGeoExecType(
					geoBoundingBoxQuery.getGeoExecType()));
		}

		if (geoBoundingBoxQuery.getGeoValidationMethod() != null) {
			geoBoundingBoxQueryBuilder.validationMethod(
				_geoTranslator.translateGeoValidationMethod(
					geoBoundingBoxQuery.getGeoValidationMethod()));
		}

		SetterUtil.setNotNullBoolean(
			geoBoundingBoxQueryBuilder::ignoreUnmapped,
			geoBoundingBoxQuery.getIgnoreUnmapped());

		return geoBoundingBoxQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(GeoDistanceQuery geoDistanceQuery) {
		org.opensearch.client.opensearch._types.query_dsl.GeoDistanceQuery.
			Builder geoDistanceQueryBuilder = QueryBuilders.geoDistance();

		geoDistanceQueryBuilder.field(geoDistanceQuery.getField());
		geoDistanceQueryBuilder.location(
			_geoTranslator.translateGeoLocationPoint(
				geoDistanceQuery.getPinGeoLocationPoint()));

		SetterUtil.setNotNullFloat(
			geoDistanceQueryBuilder::boost, geoDistanceQuery.getBoost());
		SetterUtil.setNotBlankString(
			geoDistanceQueryBuilder::queryName,
			geoDistanceQuery.getQueryName());

		if (geoDistanceQuery.getGeoDistance() != null) {
			geoDistanceQueryBuilder.distance(
				_geoTranslator.toStringWithUnit(
					geoDistanceQuery.getGeoDistance()));
		}

		SetterUtil.setNotBlankString(
			geoDistanceQueryBuilder::queryName,
			geoDistanceQuery.getQueryName());

		if (geoDistanceQuery.getGeoValidationMethod() != null) {
			geoDistanceQueryBuilder.validationMethod(
				_geoTranslator.translateGeoValidationMethod(
					geoDistanceQuery.getGeoValidationMethod()));
		}

		return geoDistanceQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(GeoDistanceRangeQuery geoDistanceRangeQuery) {
		RangeQuery.Builder rangeQueryBuilder = new RangeQuery.Builder();

		GeoDistance geoDistanceLowerBound =
			geoDistanceRangeQuery.getLowerBoundGeoDistance();

		GeoDistance geoDistanceUpperBound =
			geoDistanceRangeQuery.getUpperBoundGeoDistance();

		QueryUtil.setRanges(
			geoDistanceRangeQuery.isIncludesLower(),
			geoDistanceRangeQuery.isIncludesUpper(),
			geoDistanceLowerBound.toString(), rangeQueryBuilder,
			geoDistanceUpperBound.toString());

		SetterUtil.setNotNullFloat(
			rangeQueryBuilder::boost, geoDistanceRangeQuery.getBoost());

		if (geoDistanceRangeQuery.getShapeRelation() != null) {
			ShapeRelation shapeRelation =
				geoDistanceRangeQuery.getShapeRelation();

			String shapeRelationName = shapeRelation.name();

			rangeQueryBuilder.relation(
				RangeRelation.valueOf(
					StringUtil.toLowerCase(shapeRelationName)));
		}

		return rangeQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(GeoPolygonQuery geoPolygonQuery) {
		org.opensearch.client.opensearch._types.query_dsl.GeoPolygonQuery.
			Builder geoPolygonQueryBuilder = QueryBuilders.geoPolygon();

		geoPolygonQueryBuilder.field(geoPolygonQuery.getField());

		List<GeoLocation> geoLocations = TransformUtil.transform(
			geoPolygonQuery.getGeoLocationPoints(),
			_geoTranslator::translateGeoLocationPoint);

		geoPolygonQueryBuilder.polygon(
			GeoPolygonPoints.of(
				geoPolygonPoints -> geoPolygonPoints.points(geoLocations)));

		SetterUtil.setNotNullFloat(
			geoPolygonQueryBuilder::boost, geoPolygonQuery.getBoost());

		if (geoPolygonQuery.getGeoValidationMethod() != null) {
			geoPolygonQueryBuilder.validationMethod(
				_geoTranslator.translateGeoValidationMethod(
					geoPolygonQuery.getGeoValidationMethod()));
		}

		SetterUtil.setNotNullBoolean(
			geoPolygonQueryBuilder::ignoreUnmapped,
			geoPolygonQuery.getIgnoreUnmapped());

		return geoPolygonQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(GeoShapeQuery geoShapeQuery) {
		org.opensearch.client.opensearch._types.query_dsl.GeoShapeQuery.Builder
			geoShapeQueryBuilder = QueryBuilders.geoShape();

		geoShapeQueryBuilder.field(geoShapeQuery.getField());

		SetterUtil.setNotNullFloat(
			geoShapeQueryBuilder::boost, geoShapeQuery.getBoost());
		SetterUtil.setNotNullBoolean(
			geoShapeQueryBuilder::ignoreUnmapped,
			geoShapeQuery.getIgnoreUnmapped());

		GeoShapeFieldQuery.Builder geoShapeFieldQueryBuilder =
			new GeoShapeFieldQuery.Builder();

		if (geoShapeQuery.getIndexedShapeId() != null) {
			FieldLookup.Builder fieldLookupBuilder = new FieldLookup.Builder();

			SetterUtil.setNotBlankString(
				fieldLookupBuilder::id, geoShapeQuery.getIndexedShapeId());
			SetterUtil.setNotBlankString(
				fieldLookupBuilder::index,
				geoShapeQuery.getIndexedShapeIndex());
			SetterUtil.setNotBlankString(
				fieldLookupBuilder::path, geoShapeQuery.getIndexedShapePath());
			SetterUtil.setNotBlankString(
				fieldLookupBuilder::routing,
				geoShapeQuery.getIndexedShapeRouting());

			geoShapeFieldQueryBuilder.indexedShape(fieldLookupBuilder.build());
		}
		else {
			Shape shape = geoShapeQuery.getShape();

			List<Coordinate> coordinates = shape.getCoordinates();

			geoShapeFieldQueryBuilder.shape(
				JsonData.of(
					JSONUtil.put(
						"coordinates", JSONUtil.putAll(coordinates.toArray())
					).put(
						"type", "point"
					)));
		}

		if (geoShapeQuery.getShapeRelation() != null) {
			geoShapeFieldQueryBuilder.relation(
				_translateShapeRelation(geoShapeQuery.getShapeRelation()));
		}

		geoShapeQueryBuilder.shape(geoShapeFieldQueryBuilder.build());

		SetterUtil.setNotNullBoolean(
			geoShapeQueryBuilder::ignoreUnmapped,
			geoShapeQuery.getIgnoreUnmapped());

		return geoShapeQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(IdsQuery idsQuery) {
		org.opensearch.client.opensearch._types.query_dsl.IdsQuery.Builder
			idsQueryBuilder = QueryBuilders.ids();

		idsQueryBuilder.queryName(idsQuery.getQueryName());
		idsQueryBuilder.values(ListUtil.fromCollection(idsQuery.getIds()));

		SetterUtil.setNotNullFloat(idsQueryBuilder::boost, idsQuery.getBoost());

		return idsQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(MatchAllQuery matchAllQuery) {
		org.opensearch.client.opensearch._types.query_dsl.MatchAllQuery.Builder
			matchAllQueryBuilder = QueryBuilders.matchAll();

		SetterUtil.setNotNullFloat(
			matchAllQueryBuilder::boost, matchAllQuery.getBoost());

		return matchAllQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(MatchPhrasePrefixQuery matchPhrasePrefixQuery) {
		org.opensearch.client.opensearch._types.query_dsl.
			MatchPhrasePrefixQuery.Builder matchPhrasePrefixQueryBuilder =
				QueryBuilders.matchPhrasePrefix();

		matchPhrasePrefixQueryBuilder.field(matchPhrasePrefixQuery.getField());
		matchPhrasePrefixQueryBuilder.query(
			String.valueOf(matchPhrasePrefixQuery.getValue()));

		SetterUtil.setNotBlankString(
			matchPhrasePrefixQueryBuilder::analyzer,
			matchPhrasePrefixQuery.getAnalyzer());
		SetterUtil.setNotNullFloat(
			matchPhrasePrefixQueryBuilder::boost,
			matchPhrasePrefixQuery.getBoost());
		SetterUtil.setNotNullInteger(
			matchPhrasePrefixQueryBuilder::slop,
			matchPhrasePrefixQuery.getSlop());
		SetterUtil.setNotNullInteger(
			matchPhrasePrefixQueryBuilder::maxExpansions,
			matchPhrasePrefixQuery.getMaxExpansions());

		return matchPhrasePrefixQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(MatchPhraseQuery matchPhraseQuery) {
		org.opensearch.client.opensearch._types.query_dsl.MatchPhraseQuery.
			Builder matchPhraseQueryBuilder = QueryBuilders.matchPhrase();

		matchPhraseQueryBuilder.field(matchPhraseQuery.getField());
		matchPhraseQueryBuilder.query(
			String.valueOf(matchPhraseQuery.getValue()));

		SetterUtil.setNotBlankString(
			matchPhraseQueryBuilder::analyzer, matchPhraseQuery.getAnalyzer());
		SetterUtil.setNotNullFloat(
			matchPhraseQueryBuilder::boost, matchPhraseQuery.getBoost());
		SetterUtil.setNotNullInteger(
			matchPhraseQueryBuilder::slop, matchPhraseQuery.getSlop());

		return matchPhraseQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(MatchQuery matchQuery) {
		String field = matchQuery.getField();
		MatchQuery.Type type = matchQuery.getType();
		Object value = matchQuery.getValue();

		if (value instanceof String) {
			String stringValue = (String)value;

			if (stringValue.startsWith(StringPool.QUOTE) &&
				stringValue.endsWith(StringPool.QUOTE)) {

				type = MatchQuery.Type.PHRASE;

				stringValue = StringUtil.unquote(stringValue);

				if (stringValue.endsWith(StringPool.STAR)) {
					type = MatchQuery.Type.PHRASE_PREFIX;
				}
			}

			if (type == MatchQuery.Type.PHRASE) {
				return _translateMatchPhraseQuery(
					field, matchQuery, stringValue);
			}
			else if (type == MatchQuery.Type.PHRASE_PREFIX) {
				return _translateMatchPhrasePrefixQuery(
					field, matchQuery, stringValue);
			}
		}

		if ((type == null) || (type == MatchQuery.Type.BOOLEAN)) {
			return _translateMatchQuery(field, matchQuery, value);
		}

		throw new IllegalArgumentException("Invalid match query type " + type);
	}

	@Override
	public QueryVariant visit(MoreLikeThisQuery moreLikeThisQuery) {
		org.opensearch.client.opensearch._types.query_dsl.MoreLikeThisQuery.
			Builder moreLikeThisQueryBuilder = QueryBuilders.moreLikeThis();

		List<Like> likes = _translateDocumentIdentifiers(
			moreLikeThisQuery.getDocumentIdentifiers());

		ListUtil.isNotEmptyForEach(
			moreLikeThisQuery.getLikeTexts(),
			likeText -> likes.add(Like.of(like -> like.text(likeText))));

		moreLikeThisQueryBuilder.like(likes);

		if (ListUtil.isNotEmpty(moreLikeThisQuery.getFields())) {
			moreLikeThisQueryBuilder.fields(moreLikeThisQuery.getFields());
		}

		SetterUtil.setNotBlankString(
			moreLikeThisQueryBuilder::analyzer,
			moreLikeThisQuery.getAnalyzer());
		SetterUtil.setNotNullFloat(
			moreLikeThisQueryBuilder::boost, moreLikeThisQuery.getBoost());
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
		SetterUtil.setNotBlankString(
			moreLikeThisQueryBuilder::minimumShouldMatch,
			moreLikeThisQuery.getMinShouldMatch());
		SetterUtil.setNotNullInteger(
			moreLikeThisQueryBuilder::minTermFreq,
			moreLikeThisQuery.getMinTermFrequency());
		SetterUtil.setNotNullInteger(
			moreLikeThisQueryBuilder::minWordLength,
			moreLikeThisQuery.getMinWordLength());

		if (SetUtil.isNotEmpty(moreLikeThisQuery.getStopWords())) {
			moreLikeThisQueryBuilder.stopWords(
				ListUtil.fromCollection(moreLikeThisQuery.getStopWords()));
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
	public QueryVariant visit(MultiMatchQuery multiMatchQuery) {
		org.opensearch.client.opensearch._types.query_dsl.MultiMatchQuery.
			Builder multiMatchQueryBuilder = QueryBuilders.multiMatch();

		multiMatchQueryBuilder.query(
			String.valueOf(multiMatchQuery.getValue()));

		SetterUtil.setNotBlankString(
			multiMatchQueryBuilder::analyzer, multiMatchQuery.getAnalyzer());
		SetterUtil.setNotNullFloat(
			multiMatchQueryBuilder::boost, multiMatchQuery.getBoost());
		SetterUtil.setNotNullFloatAsDouble(
			multiMatchQueryBuilder::cutoffFrequency,
			multiMatchQuery.getCutOffFrequency());

		if (!multiMatchQuery.isFieldBoostsEmpty()) {
			multiMatchQueryBuilder.fields(
				QueryUtil.fieldsBoostsToFieldsWithBoosts(
					multiMatchQuery.getFieldsBoosts()));
		}

		SetterUtil.setNotBlankString(
			multiMatchQueryBuilder::fuzziness, multiMatchQuery.getFuzziness());

		if (multiMatchQuery.getFuzzyRewriteMethod() != null) {
			multiMatchQueryBuilder.fuzzyRewrite(
				translateMatchQueryRewriteMethod(
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
				_translateOperator(multiMatchQuery.getOperator()));
		}

		SetterUtil.setNotNullInteger(
			multiMatchQueryBuilder::prefixLength,
			multiMatchQuery.getPrefixLength());
		SetterUtil.setNotNullInteger(
			multiMatchQueryBuilder::slop, multiMatchQuery.getSlop());

		if (multiMatchQuery.getType() != null) {
			multiMatchQueryBuilder.type(
				translateMultiMatchQueryType(multiMatchQuery.getType()));
		}

		if (multiMatchQuery.getZeroTermsQuery() != null) {
			multiMatchQueryBuilder.zeroTermsQuery(
				translateMatchQueryZeroTermsQuery(
					multiMatchQuery.getZeroTermsQuery()));
		}

		SetterUtil.setNotNullBoolean(
			multiMatchQueryBuilder::lenient, multiMatchQuery.isLenient());

		return multiMatchQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(NestedQuery nestedQuery) {
		org.opensearch.client.opensearch._types.query_dsl.NestedQuery.Builder
			nestedQueryBuilder = QueryBuilders.nested();

		nestedQueryBuilder.path(nestedQuery.getPath());

		Query query = nestedQuery.getQuery();

		nestedQueryBuilder.query(
			new org.opensearch.client.opensearch._types.query_dsl.Query(
				query.accept(this)));

		nestedQueryBuilder.scoreMode(ChildScoreMode.Sum);

		SetterUtil.setNotNullFloat(
			nestedQueryBuilder::boost, nestedQuery.getBoost());

		if (nestedQuery.getQueryName() != null) {
			nestedQueryBuilder.queryName(nestedQuery.getQueryName());
		}

		return nestedQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(PercolateQuery percolateQuery) {
		org.opensearch.client.opensearch._types.query_dsl.PercolateQuery.Builder
			percolateQueryBuilder = QueryBuilders.percolate();

		SetterUtil.setNotNullFloat(
			percolateQueryBuilder::boost, percolateQuery.getBoost());

		percolateQueryBuilder.field(percolateQuery.getField());

		ListUtil.isNotEmptyForEach(
			percolateQuery.getDocumentJSONs(),
			documentJSON -> percolateQueryBuilder.document(
				JsonData.of(documentJSON)));

		return percolateQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(PrefixQuery prefixQuery) {
		org.opensearch.client.opensearch._types.query_dsl.PrefixQuery.Builder
			prefixQueryBuilder = QueryBuilders.prefix();

		prefixQueryBuilder.field(prefixQuery.getField());
		prefixQueryBuilder.value(prefixQuery.getPrefix());

		SetterUtil.setNotNullFloat(
			prefixQueryBuilder::boost, prefixQuery.getBoost());
		SetterUtil.setNotBlankString(
			prefixQueryBuilder::rewrite, prefixQuery.getRewrite());

		return prefixQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(RangeTermQuery rangeTermQuery) {
		RangeQuery.Builder rangeQueryBuilder = QueryBuilders.range();

		rangeQueryBuilder.field(rangeTermQuery.getField());

		QueryUtil.setRanges(
			rangeTermQuery.isIncludesLower(), rangeTermQuery.isIncludesUpper(),
			rangeTermQuery.getLowerBound(), rangeQueryBuilder,
			rangeTermQuery.getUpperBound());

		SetterUtil.setNotNullFloat(
			rangeQueryBuilder::boost, rangeTermQuery.getBoost());

		return rangeQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(RegexQuery regexQuery) {
		RegexpQuery.Builder regexpQueryBuilder = QueryBuilders.regexp();

		regexpQueryBuilder.field(regexQuery.getField());

		SetterUtil.setNotNullFloat(
			regexpQueryBuilder::boost, regexQuery.getBoost());
		SetterUtil.setNotNullInteger(
			regexpQueryBuilder::maxDeterminizedStates,
			regexQuery.getMaxDeterminedStates());
		SetterUtil.setNotNullValueAsString(
			regexpQueryBuilder::flags, regexQuery.getRegexFlags());
		SetterUtil.setNotBlankString(
			regexpQueryBuilder::rewrite, regexQuery.getRewrite());

		return regexpQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(ScriptQuery scriptQuery) {
		org.opensearch.client.opensearch._types.query_dsl.ScriptQuery.Builder
			scriptQueryBuilder = QueryBuilders.script();

		scriptQueryBuilder.script(
			_scriptTranslator.translate(scriptQuery.getScript()));

		SetterUtil.setNotNullFloat(
			scriptQueryBuilder::boost, scriptQuery.getBoost());

		return scriptQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(SimpleStringQuery simpleStringQuery) {
		SimpleQueryStringQuery.Builder simpleQueryStringBuilder =
			QueryBuilders.simpleQueryString();

		simpleQueryStringBuilder.query(simpleStringQuery.getQuery());

		SetterUtil.setNotNullFloat(
			simpleQueryStringBuilder::boost, simpleStringQuery.getBoost());

		if (MapUtil.isNotEmpty(simpleStringQuery.getFieldBoostMap())) {
			simpleQueryStringBuilder.fields(
				QueryUtil.fieldsBoostsToFieldsWithBoosts(
					simpleStringQuery.getFieldBoostMap()));
		}

		SetterUtil.setNotBlankString(
			simpleQueryStringBuilder::analyzer,
			simpleStringQuery.getAnalyzer());
		SetterUtil.setNotNullBoolean(
			simpleQueryStringBuilder::analyzeWildcard,
			simpleStringQuery.getAnalyzeWildcard());
		SetterUtil.setNotNullBoolean(
			simpleQueryStringBuilder::autoGenerateSynonymsPhraseQuery,
			simpleStringQuery.getAutoGenerateSynonymsPhraseQuery());

		if (simpleStringQuery.getDefaultOperator() != null) {
			simpleQueryStringBuilder.defaultOperator(
				_translateOperator(simpleStringQuery.getDefaultOperator()));
		}

		SetterUtil.setNotNullInteger(
			simpleQueryStringBuilder::fuzzyMaxExpansions,
			simpleStringQuery.getFuzzyMaxExpansions());
		SetterUtil.setNotNullInteger(
			simpleQueryStringBuilder::fuzzyPrefixLength,
			simpleStringQuery.getFuzzyPrefixLength());
		SetterUtil.setNotNullBoolean(
			simpleQueryStringBuilder::fuzzyTranspositions,
			simpleStringQuery.getFuzzyTranspositions());
		SetterUtil.setNotNullBoolean(
			simpleQueryStringBuilder::lenient, simpleStringQuery.getLenient());
		SetterUtil.setNotBlankString(
			simpleQueryStringBuilder::quoteFieldSuffix,
			simpleStringQuery.getQuoteFieldSuffix());

		return simpleQueryStringBuilder.build();
	}

	@Override
	public QueryVariant visit(StringQuery stringQuery) {
		QueryStringQuery.Builder queryStringQueryBuilder =
			QueryBuilders.queryString();

		queryStringQueryBuilder.query(stringQuery.getQuery());

		SetterUtil.setNotNullFloat(
			queryStringQueryBuilder::boost, stringQuery.getBoost());

		if (MapUtil.isNotEmpty(stringQuery.getFieldsBoosts())) {
			queryStringQueryBuilder.fields(
				QueryUtil.fieldsBoostsToFieldsWithBoosts(
					stringQuery.getFieldsBoosts()));
		}

		SetterUtil.setNotNullBoolean(
			queryStringQueryBuilder::allowLeadingWildcard,
			stringQuery.getAllowLeadingWildcard());
		SetterUtil.setNotBlankString(
			queryStringQueryBuilder::analyzer, stringQuery.getAnalyzer());
		SetterUtil.setNotNullBoolean(
			queryStringQueryBuilder::analyzeWildcard,
			stringQuery.getAnalyzeWildcard());
		SetterUtil.setNotNullBoolean(
			queryStringQueryBuilder::autoGenerateSynonymsPhraseQuery,
			stringQuery.getAutoGenerateSynonymsPhraseQuery());
		SetterUtil.setNotBlankString(
			queryStringQueryBuilder::defaultField,
			stringQuery.getDefaultField());

		if (stringQuery.getDefaultOperator() != null) {
			queryStringQueryBuilder.defaultOperator(
				_translateOperator(stringQuery.getDefaultOperator()));
		}

		SetterUtil.setNotNullBoolean(
			queryStringQueryBuilder::enablePositionIncrements,
			stringQuery.getEnablePositionIncrements());
		SetterUtil.setNotNullBoolean(
			queryStringQueryBuilder::escape, stringQuery.getEscape());
		SetterUtil.setNotNullValueAsString(
			queryStringQueryBuilder::fuzziness, stringQuery.getFuzziness());
		SetterUtil.setNotNullInteger(
			queryStringQueryBuilder::fuzzyMaxExpansions,
			stringQuery.getFuzzyMaxExpansions());
		SetterUtil.setNotNullInteger(
			queryStringQueryBuilder::fuzzyPrefixLength,
			stringQuery.getFuzzyPrefixLength());
		SetterUtil.setNotBlankString(
			queryStringQueryBuilder::fuzzyRewrite,
			stringQuery.getFuzzyRewrite());
		SetterUtil.setNotNullBoolean(
			queryStringQueryBuilder::fuzzyTranspositions,
			stringQuery.getFuzzyTranspositions());
		SetterUtil.setNotNullBoolean(
			queryStringQueryBuilder::lenient, stringQuery.getLenient());
		SetterUtil.setNotNullInteger(
			queryStringQueryBuilder::maxDeterminizedStates,
			stringQuery.getMaxDeterminedStates());
		SetterUtil.setNotBlankString(
			queryStringQueryBuilder::minimumShouldMatch,
			stringQuery.getMinimumShouldMatch());
		SetterUtil.setNotNullIntegerAsDouble(
			queryStringQueryBuilder::phraseSlop, stringQuery.getPhraseSlop());
		SetterUtil.setNotBlankString(
			queryStringQueryBuilder::quoteAnalyzer,
			stringQuery.getQuoteAnalyzer());
		SetterUtil.setNotBlankString(
			queryStringQueryBuilder::quoteFieldSuffix,
			stringQuery.getQuoteFieldSuffix());
		SetterUtil.setNotBlankString(
			queryStringQueryBuilder::rewrite, stringQuery.getRewrite());
		SetterUtil.setNotNullFloatAsDouble(
			queryStringQueryBuilder::tieBreaker, stringQuery.getTieBreaker());
		SetterUtil.setNotBlankString(
			queryStringQueryBuilder::timeZone, stringQuery.getTimeZone());

		return queryStringQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(TermQuery termQuery) {
		org.opensearch.client.opensearch._types.query_dsl.TermQuery.Builder
			termQueryBuilder = QueryBuilders.term();

		termQueryBuilder.field(termQuery.getField());
		termQueryBuilder.value(
			ConversionUtil.toFieldValue(termQuery.getValue()));

		SetterUtil.setNotNullFloat(
			termQueryBuilder::boost, termQuery.getBoost());

		return termQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(TermsQuery termsQuery) {
		org.opensearch.client.opensearch._types.query_dsl.TermsQuery.Builder
			termsQueryBuilder = QueryBuilders.terms();

		SetterUtil.setNotNullFloat(
			termsQueryBuilder::boost, termsQuery.getBoost());

		termsQueryBuilder.field(termsQuery.getField());

		List<FieldValue> fieldValues = new ArrayList<>();

		ListUtil.isNotEmptyForEach(
			Arrays.asList(termsQuery.getValues()),
			value -> fieldValues.add(FieldValue.of(value)));

		termsQueryBuilder.terms(
			termsQueryField -> termsQueryField.value(fieldValues));

		return termsQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(TermsSetQuery termsSetQuery) {
		org.opensearch.client.opensearch._types.query_dsl.TermsSetQuery.Builder
			termsSetQueryBuilder = QueryBuilders.termsSet();

		termsSetQueryBuilder.field(termsSetQuery.getFieldName());

		ListUtil.isNotEmptyForEach(
			termsSetQuery.getValues(),
			value -> termsSetQueryBuilder.terms(
				Objects.toString(value, StringPool.BLANK)));

		SetterUtil.setNotNullFloat(
			termsSetQueryBuilder::boost, termsSetQuery.getBoost());
		SetterUtil.setNotBlankString(
			termsSetQueryBuilder::minimumShouldMatchField,
			termsSetQuery.getMinimumShouldMatchField());
		SetterUtil.setNotNullScript(
			termsSetQueryBuilder::minimumShouldMatchScript,
			termsSetQuery.getMinimumShouldMatchScript());

		return termsSetQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(WildcardQuery wildcardQuery) {
		org.opensearch.client.opensearch._types.query_dsl.WildcardQuery.Builder
			wildcardQueryBuilder = QueryBuilders.wildcard();

		wildcardQueryBuilder.field(wildcardQuery.getField());
		wildcardQueryBuilder.value(wildcardQuery.getValue());

		SetterUtil.setNotNullFloat(
			wildcardQueryBuilder::boost, wildcardQuery.getBoost());
		SetterUtil.setNotBlankString(
			wildcardQueryBuilder::rewrite, wildcardQuery.getRewrite());

		return wildcardQueryBuilder.build();
	}

	@Override
	public QueryVariant visit(WrapperQuery wrapperQuery) {
		org.opensearch.client.opensearch._types.query_dsl.WrapperQuery.Builder
			wrapperQueryBuilder = QueryBuilders.wrapper();

		wrapperQueryBuilder.query(Base64.encode(wrapperQuery.getSource()));

		SetterUtil.setNotNullFloat(
			wrapperQueryBuilder::boost, wrapperQuery.getBoost());

		return wrapperQueryBuilder.build();
	}

	protected String translateMatchQueryRewriteMethod(
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

	protected ZeroTermsQuery translateMatchQueryZeroTermsQuery(
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

	protected TextQueryType translateMultiMatchQueryType(
		MultiMatchQuery.Type type) {

		if (type == MultiMatchQuery.Type.BEST_FIELDS) {
			return TextQueryType.BestFields;
		}
		else if (type == MultiMatchQuery.Type.BOOL_PREFIX) {
			return TextQueryType.BoolPrefix;
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

	private void _processBooleanQueryClauses(
		List<Query> queryClauses,
		Consumer<org.opensearch.client.opensearch._types.query_dsl.Query>
			consumer) {

		for (Query query : queryClauses) {
			consumer.accept(
				new org.opensearch.client.opensearch._types.query_dsl.Query(
					query.accept(this)));
		}
	}

	private FunctionBoostMode _translateCombineFunction(
		CombineFunction combineFunction) {

		if (combineFunction == CombineFunction.AVG) {
			return FunctionBoostMode.Avg;
		}
		else if (combineFunction == CombineFunction.MAX) {
			return FunctionBoostMode.Max;
		}
		else if (combineFunction == CombineFunction.MIN) {
			return FunctionBoostMode.Min;
		}
		else if (combineFunction == CombineFunction.MULTIPLY) {
			return FunctionBoostMode.Multiply;
		}
		else if (combineFunction == CombineFunction.REPLACE) {
			return FunctionBoostMode.Replace;
		}
		else if (combineFunction == CombineFunction.SUM) {
			return FunctionBoostMode.Sum;
		}

		throw new IllegalArgumentException(
			"Invalid combine function " + combineFunction);
	}

	private List<Like> _translateDocumentIdentifiers(
		Set<MoreLikeThisQuery.DocumentIdentifier> documentIdentifiers) {

		List<Like> likes = new ArrayList<>();

		if (SetUtil.isEmpty(documentIdentifiers)) {
			return likes;
		}

		documentIdentifiers.forEach(
			documentIdentifier -> {
				LikeDocument.Builder likeDocumentBuilder =
					new LikeDocument.Builder();

				likeDocumentBuilder.id(documentIdentifier.getId());
				likeDocumentBuilder.index(documentIdentifier.getIndex());

				likes.add(
					Like.of(l -> l.document(likeDocumentBuilder.build())));
			});

		return likes;
	}

	private FunctionScoreMode _translateFunctionScoreQueryScoreMore(
		FunctionScoreQuery.ScoreMode scoreMode) {

		if (scoreMode == FunctionScoreQuery.ScoreMode.AVG) {
			return FunctionScoreMode.Avg;
		}
		else if (scoreMode == FunctionScoreQuery.ScoreMode.FIRST) {
			return FunctionScoreMode.First;
		}
		else if (scoreMode == FunctionScoreQuery.ScoreMode.MAX) {
			return FunctionScoreMode.Max;
		}
		else if (scoreMode == FunctionScoreQuery.ScoreMode.MIN) {
			return FunctionScoreMode.Min;
		}
		else if (scoreMode == FunctionScoreQuery.ScoreMode.MULTIPLY) {
			return FunctionScoreMode.Multiply;
		}
		else if (scoreMode == FunctionScoreQuery.ScoreMode.SUM) {
			return FunctionScoreMode.Sum;
		}

		throw new IllegalArgumentException(
			"Invalid function score query score mode " + scoreMode);
	}

	private QueryVariant _translateMatchPhrasePrefixQuery(
		String field, MatchQuery matchQuery, String value) {

		org.opensearch.client.opensearch._types.query_dsl.
			MatchPhrasePrefixQuery.Builder matchPhrasePrefixQueryBuilder =
				QueryBuilders.matchPhrasePrefix();

		matchPhrasePrefixQueryBuilder.field(matchQuery.getField());
		matchPhrasePrefixQueryBuilder.query(value);

		SetterUtil.setNotBlankString(
			matchPhrasePrefixQueryBuilder::analyzer, matchQuery.getAnalyzer());
		SetterUtil.setNotNullFloat(
			matchPhrasePrefixQueryBuilder::boost, matchQuery.getBoost());
		SetterUtil.setNotNullInteger(
			matchPhrasePrefixQueryBuilder::maxExpansions,
			matchQuery.getMaxExpansions());
		SetterUtil.setNotNullInteger(
			matchPhrasePrefixQueryBuilder::slop, matchQuery.getSlop());

		return matchPhrasePrefixQueryBuilder.build();
	}

	private QueryVariant _translateMatchPhraseQuery(
		String field, MatchQuery matchQuery, String value) {

		org.opensearch.client.opensearch._types.query_dsl.MatchPhraseQuery.
			Builder matchPhraseQueryBuilder = QueryBuilders.matchPhrase();

		matchPhraseQueryBuilder.field(matchQuery.getField());
		matchPhraseQueryBuilder.query(value);

		SetterUtil.setNotBlankString(
			matchPhraseQueryBuilder::analyzer, matchQuery.getAnalyzer());
		SetterUtil.setNotNullFloat(
			matchPhraseQueryBuilder::boost, matchQuery.getBoost());
		SetterUtil.setNotNullInteger(
			matchPhraseQueryBuilder::slop, matchQuery.getSlop());

		return matchPhraseQueryBuilder.build();
	}

	private QueryVariant _translateMatchQuery(
		String field, MatchQuery matchQuery, Object value) {

		org.opensearch.client.opensearch._types.query_dsl.MatchQuery.Builder
			matchQueryBuilder = QueryBuilders.match();

		matchQueryBuilder.field(matchQuery.getField());
		matchQueryBuilder.query(ConversionUtil.toFieldValue(value));

		SetterUtil.setNotBlankString(
			matchQueryBuilder::analyzer, matchQuery.getAnalyzer());
		SetterUtil.setNotNullFloat(
			matchQueryBuilder::boost, matchQuery.getBoost());
		SetterUtil.setNotNullFloatAsDouble(
			matchQueryBuilder::cutoffFrequency,
			matchQuery.getCutOffFrequency());
		SetterUtil.setNotNullValueAsString(
			matchQueryBuilder::fuzziness, matchQuery.getFuzziness());

		if (matchQuery.getFuzzyRewriteMethod() != null) {
			matchQueryBuilder.fuzzyRewrite(
				translateMatchQueryRewriteMethod(
					matchQuery.getFuzzyRewriteMethod()));
		}

		SetterUtil.setNotNullInteger(
			matchQueryBuilder::maxExpansions, matchQuery.getMaxExpansions());
		SetterUtil.setNotBlankString(
			matchQueryBuilder::minimumShouldMatch,
			matchQuery.getMinShouldMatch());

		if (matchQuery.getOperator() != null) {
			matchQueryBuilder.operator(
				_translateOperator(matchQuery.getOperator()));
		}

		SetterUtil.setNotNullInteger(
			matchQueryBuilder::prefixLength, matchQuery.getPrefixLength());

		if (matchQuery.getZeroTermsQuery() != null) {
			matchQueryBuilder.zeroTermsQuery(
				translateMatchQueryZeroTermsQuery(
					matchQuery.getZeroTermsQuery()));
		}

		SetterUtil.setNotNullBoolean(
			matchQueryBuilder::fuzzyTranspositions,
			matchQuery.isFuzzyTranspositions());
		SetterUtil.setNotNullBoolean(
			matchQueryBuilder::lenient, matchQuery.isLenient());

		return matchQueryBuilder.build();
	}

	private org.opensearch.client.opensearch._types.query_dsl.Operator
		_translateOperator(Operator operator) {

		if (operator == Operator.AND) {
			return org.opensearch.client.opensearch._types.query_dsl.Operator.
				And;
		}
		else if (operator == Operator.OR) {
			return org.opensearch.client.opensearch._types.query_dsl.Operator.
				Or;
		}

		throw new IllegalArgumentException("Invalid operator " + operator);
	}

	private GeoShapeRelation _translateShapeRelation(
		ShapeRelation shapeRelation) {

		if (shapeRelation == ShapeRelation.CONTAINS) {
			return GeoShapeRelation.Contains;
		}

		if (shapeRelation == ShapeRelation.DISJOINT) {
			return GeoShapeRelation.Disjoint;
		}

		if (shapeRelation == ShapeRelation.INTERSECTS) {
			return GeoShapeRelation.Intersects;
		}

		if (shapeRelation == ShapeRelation.WITHIN) {
			return GeoShapeRelation.Within;
		}

		throw new IllegalArgumentException(
			"Invalid shape relation " + shapeRelation);
	}

	private final GeoTranslator _geoTranslator = new GeoTranslator();
	private final ScriptTranslator _scriptTranslator = new ScriptTranslator();

}