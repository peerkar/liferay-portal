
package fi.soveltia.liferay.gsearch.elasticsearch.query;

import org.elasticsearch.index.query.QueryBuilder;

import fi.soveltia.liferay.gsearch.query.QueryStringQuery;

/**
 * Custom QueryStringQuery query type translator service.
 * 
 * @author Petteri Karttunen
 *
 */
public interface QueryStringQueryTranslator {

	public QueryBuilder translate(QueryStringQuery stringQuery);

}
