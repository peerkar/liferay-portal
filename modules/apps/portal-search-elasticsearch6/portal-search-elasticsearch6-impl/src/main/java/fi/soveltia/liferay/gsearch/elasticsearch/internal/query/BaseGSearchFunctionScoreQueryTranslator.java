
package fi.soveltia.liferay.gsearch.elasticsearch.internal.query;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.search.MultiValueMode;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.search.query.FunctionScoreQuery;

import fi.soveltia.liferay.gsearch.query.GSearchFunctionScoreQuery;

/**
 * Function score query translator base class.
 * 
 * @author Petteri Karttunen
 */
public abstract class BaseGSearchFunctionScoreQueryTranslator {

	public QueryBuilder translateFunctionScoreQuery(
			GSearchFunctionScoreQuery query, ScoreFunctionBuilder functionBuilder) {

		FunctionScoreQueryBuilder functionScoreQueryBuilder =
			QueryBuilders.functionScoreQuery(functionBuilder);

		functionScoreQueryBuilder.boost(query.getBoost());

		if (query.getBoostMode() != null) {
			functionScoreQueryBuilder.boostMode(
				CombineFunction.valueOf(query.getBoostMode()));
		}
		
		if (query.getScoreMode() != null) {
			functionScoreQueryBuilder.scoreMode(
					translateScoreMode(query.getScoreMode()));
		}

		if (query.getMaxBoost() != null) {
			functionScoreQueryBuilder.maxBoost(query.getMaxBoost());
		}

		if (query.getMinScore() != null) {
			functionScoreQueryBuilder.setMinScore(query.getMinScore());
		}

		return functionScoreQueryBuilder;
	}

	protected
	org.elasticsearch.common.lucene.search.function.FunctionScoreQuery.
		ScoreMode translateScoreMode(FunctionScoreQuery.ScoreMode scoreMode) {

	if (scoreMode == FunctionScoreQuery.ScoreMode.AVG) {
		return org.elasticsearch.common.lucene.search.function.
			FunctionScoreQuery.ScoreMode.AVG;
	}
	else if (scoreMode == FunctionScoreQuery.ScoreMode.FIRST) {
		return org.elasticsearch.common.lucene.search.function.
			FunctionScoreQuery.ScoreMode.FIRST;
	}
	else if (scoreMode == FunctionScoreQuery.ScoreMode.MAX) {
		return org.elasticsearch.common.lucene.search.function.
			FunctionScoreQuery.ScoreMode.MAX;
	}
	else if (scoreMode == FunctionScoreQuery.ScoreMode.MIN) {
		return org.elasticsearch.common.lucene.search.function.
			FunctionScoreQuery.ScoreMode.MIN;
	}
	else if (scoreMode == FunctionScoreQuery.ScoreMode.MULTIPLY) {
		return org.elasticsearch.common.lucene.search.function.
			FunctionScoreQuery.ScoreMode.MULTIPLY;
	}
	else if (scoreMode == FunctionScoreQuery.ScoreMode.SUM) {
		return org.elasticsearch.common.lucene.search.function.
			FunctionScoreQuery.ScoreMode.SUM;
	}
	else {
		throw new IllegalArgumentException(
			"Invalid FunctionScoreQuery.ScoreMode: " + scoreMode);
	}
}
}
