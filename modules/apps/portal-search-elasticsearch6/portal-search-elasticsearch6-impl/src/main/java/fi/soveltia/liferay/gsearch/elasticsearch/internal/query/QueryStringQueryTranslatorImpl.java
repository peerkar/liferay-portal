package fi.soveltia.liferay.gsearch.elasticsearch.internal.query;

import java.util.Map.Entry;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.osgi.service.component.annotations.Component;

import fi.soveltia.liferay.gsearch.elasticsearch.query.QueryStringQueryTranslator;
import fi.soveltia.liferay.gsearch.query.Operator;
import fi.soveltia.liferay.gsearch.query.QueryStringQuery;

/**
 * Custom QueryStringQuery type translator service implementation.
 * 
 * @author Petteri Karttunen
 *
 */
@Component(
	immediate = true, 
	service = QueryStringQueryTranslator.class
)
public class QueryStringQueryTranslatorImpl implements QueryStringQueryTranslator {

	@Override
	public QueryBuilder translate(QueryStringQuery qq) {

		QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(qq.getQuery());

		if (qq.getFieldBoosts() != null) {
			for (Entry<String, Float> entry : qq.getFieldBoosts().entrySet()) {
				queryStringQueryBuilder.field(entry.getKey(), entry.getValue());
			}
		} else if (qq.getFields() != null) {
			for (String field : qq.getFields()) {
				queryStringQueryBuilder.field(field);
			}
		}

		queryStringQueryBuilder.boost(qq.getBoost());

		if (qq.isAllowLeadingWildcard() != null) {
			queryStringQueryBuilder.allowLeadingWildcard(qq.isAllowLeadingWildcard());
		}

		if (qq.isAnalyzeWildcard() != null) {
			queryStringQueryBuilder.analyzeWildcard(qq.isAnalyzeWildcard());
		}

		if (qq.isAutoGeneratePhraseQueries() != null) {
			queryStringQueryBuilder.analyzeWildcard(qq.isAutoGeneratePhraseQueries());
		}

		if (qq.isDisMax() != null) {
			queryStringQueryBuilder.useDisMax(qq.isDisMax());
		}

		if (qq.isEnablePositionIncrements() != null) {
			queryStringQueryBuilder.enablePositionIncrements(qq.isEnablePositionIncrements());
		}

		if (qq.isEscape() != null) {
			queryStringQueryBuilder.escape(qq.isEscape());
		}

		if (qq.isLenient() != null) {
			queryStringQueryBuilder.lenient(qq.isLenient());
		}

		// https://github.com/elastic/elasticsearch/pull/10086
		//		if (qq.isLowercaseExpandedTerms() != null) {
		//			queryStringQueryBuilder.lowercaseExpandedTerms(qq.isLowercaseExpandedTerms());
		//		}

		if (qq.getAnalyzer() != null) {
			queryStringQueryBuilder.analyzer(qq.getAnalyzer());
		}

		if (qq.getDefaultField() != null) {
			queryStringQueryBuilder.defaultField(qq.getDefaultField());
		}

		if (qq.getDefaultOperator() != null) {

			if (qq.getDefaultOperator().equals(Operator.OR)) {
				queryStringQueryBuilder
						.defaultOperator(org.elasticsearch.index.query.Operator.OR);
			} else {
				queryStringQueryBuilder
						.defaultOperator(org.elasticsearch.index.query.Operator.AND);
			}
		}

		if (qq.getFuzziness() != null) {
			if (qq.getFuzziness() != null) {
				queryStringQueryBuilder.fuzziness(Fuzziness.build(qq.getFuzziness()));
			}
		}

		if (qq.getFuzzyMaxExpansions() != null) {
			queryStringQueryBuilder.fuzzyMaxExpansions(qq.getFuzzyMaxExpansions());
		}

		if (qq.getFuzzyPrefixLength() != null) {
			queryStringQueryBuilder.fuzzyPrefixLength(qq.getFuzzyPrefixLength());
		}

		if (qq.getFuzzyRewrite() != null) {
			queryStringQueryBuilder.fuzzyRewrite(qq.getFuzzyRewrite());
		}

		// https://github.com/elastic/elasticsearch/pull/10086

		//		if (qq.getLocale() != null) {
		//			queryStringQueryBuilder.locale(qq.getLocale());
		//		}

		if (qq.getMaxDeterminizedStates() != null) {
			queryStringQueryBuilder.maxDeterminizedStates(qq.getMaxDeterminizedStates());
		}

		if (qq.getMinimumShouldMatch() != null) {
			queryStringQueryBuilder.minimumShouldMatch(qq.getMinimumShouldMatch());
		}

		if (qq.getPhraseSlop() != null) {
			queryStringQueryBuilder.phraseSlop(qq.getPhraseSlop());
		}

		if (qq.getQueryName() != null) {
			queryStringQueryBuilder.queryName(qq.getQueryName());
		}

		if (qq.getQuoteAnalyzer() != null) {
			queryStringQueryBuilder.quoteAnalyzer(qq.getQuoteAnalyzer());
		}

		if (qq.getQuoteFieldSuffix() != null) {
			queryStringQueryBuilder.quoteFieldSuffix(qq.getQuoteFieldSuffix());
		}

		if (qq.getRewrite() != null) {
			queryStringQueryBuilder.rewrite(qq.getRewrite());
		}

		if (qq.getTieBreaker() != null) {
			queryStringQueryBuilder.tieBreaker(qq.getTieBreaker());
		}

		if (qq.getTimeZone() != null) {
			queryStringQueryBuilder.timeZone(qq.getTimeZone());
		}

		return queryStringQueryBuilder;
	}
}
