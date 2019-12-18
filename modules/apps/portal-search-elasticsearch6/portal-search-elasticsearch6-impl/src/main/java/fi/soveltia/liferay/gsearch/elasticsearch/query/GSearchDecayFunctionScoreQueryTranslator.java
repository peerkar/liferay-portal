
package fi.soveltia.liferay.gsearch.elasticsearch.query;

import org.elasticsearch.index.query.QueryBuilder;

import fi.soveltia.liferay.gsearch.query.GSearchDecayFunctionScoreQuery;

/**
 * Decay function score query translator.
 * 
 * @author Petteri Karttunen
 */
public interface GSearchDecayFunctionScoreQueryTranslator {

	public QueryBuilder translate(
			GSearchDecayFunctionScoreQuery query);

}
