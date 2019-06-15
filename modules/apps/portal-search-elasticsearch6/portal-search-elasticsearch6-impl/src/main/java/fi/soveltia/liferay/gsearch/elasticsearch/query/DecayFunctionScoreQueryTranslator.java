
package fi.soveltia.liferay.gsearch.elasticsearch.query;

import org.elasticsearch.index.query.QueryBuilder;

import fi.soveltia.liferay.gsearch.query.DecayFunctionScoreQuery;

/**
 * Decay function score query translator service.
 * 
 * @author Petteri Karttunen
 */
public interface DecayFunctionScoreQueryTranslator {

	public QueryBuilder translate(DecayFunctionScoreQuery query);

}
