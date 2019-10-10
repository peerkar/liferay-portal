
package fi.soveltia.liferay.gsearch.elasticsearch.internal.query;

import org.elasticsearch.index.fielddata.IndexNumericFieldData;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FieldValueFactorFunctionBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction;

import org.elasticsearch.search.MultiValueMode;

import org.osgi.service.component.annotations.Component;

import fi.soveltia.liferay.gsearch.elasticsearch.query.GSearchFieldValueFactorFunctionScoreQueryTranslator;
import fi.soveltia.liferay.gsearch.query.GSearchFieldValueFactorFunctionScoreQuery;

/**
 * Liferay GSearch FieldValueFactor funtion score query translator impl.
 * 
 * @author Petteri Karttunen
 */
@Component(
	immediate=true,
	service = GSearchFieldValueFactorFunctionScoreQueryTranslator.class
)
public class GSearchFieldValueFactorFunctionScoreQueryTranslatorImpl
	extends BaseGSearchFunctionScoreQueryTranslator
	implements GSearchFieldValueFactorFunctionScoreQueryTranslator {

	public QueryBuilder translate(GSearchFieldValueFactorFunctionScoreQuery query) {
		
		FieldValueFactorFunctionBuilder functionBuilder = 
				new FieldValueFactorFunctionBuilder(query.getFieldName());

		// Factor.
		
		if (query.getFactor() != null) {
			functionBuilder.factor(query.getFactor());
		}

		// Missing value.
		
		if (query.getMissing() != null) {
			functionBuilder.missing(query.getMissing());
		}

		// Modifier.
		
		if (query.getModifier() != null) {

			FieldValueFactorFunction.Modifier modifier = 
					FieldValueFactorFunction.Modifier.valueOf(query.getModifier().toUpperCase());

			if (modifier != null) {
				functionBuilder.modifier(modifier);
			}	
		}
		
		return super.translateFunctionScoreQuery(query, functionBuilder);
	}
}
