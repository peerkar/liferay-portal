package fi.soveltia.liferay.gsearch.elasticsearch.query;

import org.elasticsearch.index.query.QueryBuilder;

import fi.soveltia.liferay.gsearch.query.GSearchQueryStringQuery;

/**
 * Liferay GSearch QueryStringQuery translator.
 * 
 * @author Petteri Karttunen
 */
public interface GSearchQueryStringQueryTranslator {

	public QueryBuilder translate(GSearchQueryStringQuery stringQuery);

}