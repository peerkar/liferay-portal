package fi.soveltia.liferay.gsearch.elasticsearch.internal.query;

import com.liferay.portal.kernel.search.generic.StringQuery;
import com.liferay.portal.search.elasticsearch6.internal.query.StringQueryTranslator;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import fi.soveltia.liferay.gsearch.elasticsearch.query.DecayFunctionScoreQueryTranslator;
import fi.soveltia.liferay.gsearch.elasticsearch.query.FieldValueFactorFunctionScoreQueryTranslator;
import fi.soveltia.liferay.gsearch.elasticsearch.query.QueryStringQueryTranslator;
import fi.soveltia.liferay.gsearch.query.DecayFunctionScoreQuery;
import fi.soveltia.liferay.gsearch.query.FieldValueFactorFunctionScoreQuery;
import fi.soveltia.liferay.gsearch.query.QueryStringQuery;

/**
 * Custom StringQuery translator service.
 *
 * This is working now as a router for all Liferay GSearch custom query types. This is
 * not exactly "clean" but this way modifying the standard QueryVisitor class
 * from portal-search has been avoided. 
 * 
 * If it gets a regular StrinQuery object it translates it with the default way.
 * It it gets the custom QueryStringQuery object the translation is made by the
 * custom translator.
 * 
 * @author Petteri Karttunen
 *
 */
@Component(
		immediate=true,
		property = {
			"service.ranking:Integer=100"
		},
		service = StringQueryTranslator.class
	)
public class GSearchStringQueryTranslatorImpl implements StringQueryTranslator {

	@Override
	public QueryBuilder translate(StringQuery stringQuery) {

		try {
			
			// If object is one of the extended types then use custom translator
	
			if (stringQuery instanceof QueryStringQuery) {
	
				return _queryStringQueryTranslator.translate((QueryStringQuery) stringQuery);
	
			} else if (stringQuery instanceof DecayFunctionScoreQuery) {
	
				return _decayFunctionScoreQueryTranslator.translate((DecayFunctionScoreQuery) stringQuery);
				
			} else if (stringQuery instanceof FieldValueFactorFunctionScoreQuery) {
		
				return _fieldValueFactorFunctionScoreQueryTranslator.translate((FieldValueFactorFunctionScoreQuery) stringQuery);
			}
	
			QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(stringQuery.getQuery());
	
			if (!stringQuery.isDefaultBoost()) {
				queryStringQueryBuilder.boost(stringQuery.getBoost());
			}
	
			return queryStringQueryBuilder;

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Reference
	private DecayFunctionScoreQueryTranslator _decayFunctionScoreQueryTranslator;

	@Reference
	private FieldValueFactorFunctionScoreQueryTranslator _fieldValueFactorFunctionScoreQueryTranslator;

	@Reference
	private QueryStringQueryTranslator _queryStringQueryTranslator;

}
