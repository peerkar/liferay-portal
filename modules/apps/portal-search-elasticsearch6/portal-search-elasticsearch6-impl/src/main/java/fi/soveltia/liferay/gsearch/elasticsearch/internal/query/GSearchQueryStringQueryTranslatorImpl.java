package fi.soveltia.liferay.gsearch.elasticsearch.internal.query;

import java.util.Map.Entry;

import com.liferay.portal.search.query.Operator;
import com.liferay.portal.search.query.StringQuery;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;

import org.osgi.service.component.annotations.Component;

import fi.soveltia.liferay.gsearch.query.GSearchQueryStringQuery;

import fi.soveltia.liferay.gsearch.elasticsearch.query.GSearchQueryStringQueryTranslator;

/**
 * Liferay GSearch QueryString query translator impl.
 * 
 * @author Petteri Karttunen
 *
 */
@Component(
	immediate = true, 
	property = {
			"service.ranking:Integer=100"
	},
	service = GSearchQueryStringQueryTranslator.class
)
public class GSearchQueryStringQueryTranslatorImpl implements GSearchQueryStringQueryTranslator {

	@Override
	public QueryBuilder translate(GSearchQueryStringQuery stringQuery) {
	
		QueryStringQueryBuilder queryStringQueryBuilder = 
				QueryBuilders.queryStringQuery(stringQuery.getQuery());

		if (stringQuery.getFieldBoosts() != null) {
			for (Entry<String, Float> entry : stringQuery.getFieldBoosts().entrySet()) {
				queryStringQueryBuilder.field(entry.getKey(), entry.getValue());
			}
		} else if (stringQuery.getFields() != null) {
			for (String field : stringQuery.getFields()) {
				queryStringQueryBuilder.field(field);
			}
		}

		queryStringQueryBuilder.boost(stringQuery.getBoost());

		if (stringQuery.getAllowLeadingWildcard() != null) {
			queryStringQueryBuilder.allowLeadingWildcard(stringQuery.getAllowLeadingWildcard());
		}

		if (stringQuery.getAnalyzeWildcard() != null) {
			queryStringQueryBuilder.analyzeWildcard(stringQuery.getAnalyzeWildcard());
		}

		if (stringQuery.getAutoGenerateSynonymsPhraseQuery() != null) {
			queryStringQueryBuilder.analyzeWildcard(stringQuery.getAutoGenerateSynonymsPhraseQuery());
		}

		if (stringQuery.isDisMax() != null) {
			queryStringQueryBuilder.useDisMax(stringQuery.isDisMax());
		}

		if (stringQuery.getEnablePositionIncrements() != null) {
			queryStringQueryBuilder.enablePositionIncrements(stringQuery.getEnablePositionIncrements());
		}

		if (stringQuery.isEscape() != null) {
			queryStringQueryBuilder.escape(stringQuery.isEscape());
		}

		if (stringQuery.getLenient() != null) {
			queryStringQueryBuilder.lenient(stringQuery.getLenient());
		}

		// https://github.com/elastic/elasticsearch/pull/10086
		//		if (stringQuery.isLowercaseExpandedTerms() != null) {
		//			queryStringQueryBuilder.lowercaseExpandedTerms(stringQuery.isLowercaseExpandedTerms());
		//		}

		if (stringQuery.getAnalyzer() != null) {
			queryStringQueryBuilder.analyzer(stringQuery.getAnalyzer());
		}

		if (stringQuery.getDefaultField() != null) {
			queryStringQueryBuilder.defaultField(stringQuery.getDefaultField());
		}

		if (stringQuery.getDefaultOperator() != null) {

			if (stringQuery.getDefaultOperator().equals(Operator.OR)) {
				queryStringQueryBuilder
						.defaultOperator(org.elasticsearch.index.query.Operator.OR);
			} else {
				queryStringQueryBuilder
						.defaultOperator(org.elasticsearch.index.query.Operator.AND);
			}
		}

		if (stringQuery.getFuzziness() != null) {
			if (stringQuery.getFuzziness() != null) {
				queryStringQueryBuilder.fuzziness(Fuzziness.build(stringQuery.getFuzziness()));
			}
		}

		if (stringQuery.getFuzzyMaxExpansions() != null) {
			queryStringQueryBuilder.fuzzyMaxExpansions(stringQuery.getFuzzyMaxExpansions());
		}

		if (stringQuery.getFuzzyPrefixLength() != null) {
			queryStringQueryBuilder.fuzzyPrefixLength(stringQuery.getFuzzyPrefixLength());
		}

		if (stringQuery.getFuzzyRewrite() != null) {
			queryStringQueryBuilder.fuzzyRewrite(stringQuery.getFuzzyRewrite());
		}

		// https://github.com/elastic/elasticsearch/pull/10086

		//		if (stringQuery.getLocale() != null) {
		//			queryStringQueryBuilder.locale(stringQuery.getLocale());
		//		}

		if (stringQuery.getMaxDeterminedStates() != null) {
			queryStringQueryBuilder.maxDeterminizedStates(stringQuery.getMaxDeterminedStates());
		}

		if (stringQuery.getMinimumShouldMatch() != null) {
			queryStringQueryBuilder.minimumShouldMatch(stringQuery.getMinimumShouldMatch());
		}

		if (stringQuery.getPhraseSlop() != null) {
			queryStringQueryBuilder.phraseSlop(stringQuery.getPhraseSlop());
		}

		if (stringQuery.getQueryName() != null) {
			queryStringQueryBuilder.queryName(stringQuery.getQueryName());
		}

		if (stringQuery.getQuoteAnalyzer() != null) {
			queryStringQueryBuilder.quoteAnalyzer(stringQuery.getQuoteAnalyzer());
		}

		if (stringQuery.getQuoteFieldSuffix() != null) {
			queryStringQueryBuilder.quoteFieldSuffix(stringQuery.getQuoteFieldSuffix());
		}

		if (stringQuery.getRewrite() != null) {
			queryStringQueryBuilder.rewrite(stringQuery.getRewrite());
		}

		if (stringQuery.getTieBreaker() != null) {
			queryStringQueryBuilder.tieBreaker(stringQuery.getTieBreaker());
		}

		if (stringQuery.getTimeZone() != null) {
			queryStringQueryBuilder.timeZone(stringQuery.getTimeZone());
		}

		return queryStringQueryBuilder;
	}
}
