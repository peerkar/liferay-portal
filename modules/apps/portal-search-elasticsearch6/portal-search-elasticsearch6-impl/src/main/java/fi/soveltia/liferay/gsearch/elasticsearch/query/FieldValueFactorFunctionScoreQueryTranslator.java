
package fi.soveltia.liferay.gsearch.elasticsearch.query;

import org.elasticsearch.index.query.QueryBuilder;

import fi.soveltia.liferay.gsearch.query.FieldValueFactorFunctionScoreQuery;

/**
 * Field value factor function score query translator service.
 * 
 * @author Petteri Karttunen
 */
public interface FieldValueFactorFunctionScoreQueryTranslator {

	public QueryBuilder translate(FieldValueFactorFunctionScoreQuery query);

}
