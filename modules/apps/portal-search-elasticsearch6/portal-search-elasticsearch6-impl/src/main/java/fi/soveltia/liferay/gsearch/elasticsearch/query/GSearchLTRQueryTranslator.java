
package fi.soveltia.liferay.gsearch.elasticsearch.query;

import org.elasticsearch.index.query.QueryBuilder;

import fi.soveltia.liferay.gsearch.query.GSearchLTRQuery;

/**
 * Liferay GSearch LTR query translator.
 * 
 * @author Petteri Karttunen
 */
public interface GSearchLTRQueryTranslator {

	public QueryBuilder translate(GSearchLTRQuery query);

}
