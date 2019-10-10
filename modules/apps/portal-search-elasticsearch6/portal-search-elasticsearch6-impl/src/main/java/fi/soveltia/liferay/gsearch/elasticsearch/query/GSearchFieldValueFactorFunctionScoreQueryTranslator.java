
package fi.soveltia.liferay.gsearch.elasticsearch.query;

import org.elasticsearch.index.query.QueryBuilder;

import fi.soveltia.liferay.gsearch.query.GSearchFieldValueFactorFunctionScoreQuery;

/**
 * Field value factor function score query translator.
 * 
 * @author Petteri Karttunen
 */
public interface GSearchFieldValueFactorFunctionScoreQueryTranslator {

	public QueryBuilder translate(
			GSearchFieldValueFactorFunctionScoreQuery query);

}
