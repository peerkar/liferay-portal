
package fi.soveltia.liferay.gsearch.elasticsearch.internal.query;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.search.MultiValueMode;

import org.osgi.service.component.annotations.Component;

import fi.soveltia.liferay.gsearch.elasticsearch.query.DecayFunctionScoreQueryTranslator;
import fi.soveltia.liferay.gsearch.query.FunctionScoreQuery;

/**
 * Function score query translator base class.
 * 
 * @author Petteri Karttunen
 */
public abstract class BaseFunctionScoreQueryTranslator {

	public QueryBuilder translateFunctionScoreQuery(
		FunctionScoreQuery query, ScoreFunctionBuilder functionBuilder) {

		FunctionScoreQueryBuilder functionScoreQueryBuilder =
			QueryBuilders.functionScoreQuery(functionBuilder);

		functionScoreQueryBuilder.boost(query.getBoost());

		if (query.getBoostMode() != null) {
			functionScoreQueryBuilder.boostMode(
				CombineFunction.valueOf(query.getBoostMode()));
		}

		if (query.getScoreMode() != null) {
			functionScoreQueryBuilder.scoreMode(
				org.elasticsearch.common.lucene.search.function.FunctionScoreQuery.ScoreMode.valueOf(query.getScoreMode()));
		}

		if (query.getMaxBoost() != null) {
			functionScoreQueryBuilder.maxBoost(query.getMaxBoost());
		}

		if (query.getMinScore() != null) {
			functionScoreQueryBuilder.setMinScore(query.getMinScore());
		}

		return functionScoreQueryBuilder;
	}
}
