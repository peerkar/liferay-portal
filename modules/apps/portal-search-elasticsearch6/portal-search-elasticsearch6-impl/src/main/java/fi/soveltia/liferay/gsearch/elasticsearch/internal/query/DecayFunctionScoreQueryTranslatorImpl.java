package fi.soveltia.liferay.gsearch.elasticsearch.internal.query;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.DecayFunctionBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ExponentialDecayFunctionBuilder;
import org.elasticsearch.index.query.functionscore.GaussDecayFunctionBuilder;
import org.elasticsearch.index.query.functionscore.LinearDecayFunctionBuilder;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.search.MultiValueMode;

import org.osgi.service.component.annotations.Component;

import fi.soveltia.liferay.gsearch.elasticsearch.query.DecayFunctionScoreQueryTranslator;
import fi.soveltia.liferay.gsearch.query.DecayFunctionScoreQuery;

/**
 * Decay Funtion Score query translator impl.
 * 
 * @author Petteri Karttunen
 *
 */
@Component(
	immediate=true,
	service = DecayFunctionScoreQueryTranslator.class
)
public class DecayFunctionScoreQueryTranslatorImpl
	extends BaseFunctionScoreQueryTranslator
	implements DecayFunctionScoreQueryTranslator {

	public QueryBuilder translate(DecayFunctionScoreQuery query) {
		
		DecayFunctionBuilder functionBuilder;

		if (query.getFunctionType().equals("exp")) {

			functionBuilder = new ExponentialDecayFunctionBuilder(query.getFieldName(), query.getOrigin(),
					query.getScale(), query.getOffset(), query.getDecay());
		} else if (query.getFunctionType().equals("linear")) {

			functionBuilder = new LinearDecayFunctionBuilder(query.getFieldName(), query.getOrigin(),
					query.getScale(), query.getOffset(), query.getDecay());
		} else {

			functionBuilder = new GaussDecayFunctionBuilder(query.getFieldName(), query.getOrigin(),
					query.getScale(), query.getOffset(), query.getDecay());
		}
		
		// Multivalue mode
		
		if (query.getMultiValueMode() != null) {
			
			if (query.getMultiValueMode().equals("avg")) {

				functionBuilder.setMultiValueMode(MultiValueMode.AVG);

			} else if (query.getMultiValueMode().equals("max")) {
				
				functionBuilder.setMultiValueMode(MultiValueMode.MAX);

			} else if (query.getMultiValueMode().equals("min")) {

				functionBuilder.setMultiValueMode(MultiValueMode.MIN);

			} else if (query.getMultiValueMode().equals("sum")) {

				functionBuilder.setMultiValueMode(MultiValueMode.SUM);
			}
		}

		return super.translateFunctionScoreQuery(query, functionBuilder);
	}
}
