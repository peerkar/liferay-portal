
package fi.soveltia.liferay.gsearch.elasticsearch.internal;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.IndexWriter;
import com.liferay.portal.kernel.search.ParseException;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.search.filter.TermFilter;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.search.generic.MatchAllQuery;
import com.liferay.portal.kernel.search.suggest.SpellCheckIndexWriter;
import com.liferay.portal.kernel.search.suggest.SuggestionConstants;
import com.liferay.portal.kernel.util.Localization;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.PortalRunMode;
import com.liferay.portal.search.elasticsearch6.internal.util.DocumentTypes;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.document.BulkDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.DeleteByQueryDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.IndexDocumentRequest;
import com.liferay.portal.search.suggest.BaseGenericSpellCheckIndexWriter;

import com.liferay.portal.search.index.IndexNameBuilder;

import java.util.Collection;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import fi.soveltia.liferay.gsearch.elasticsearch.internal.index.GSearchIndexUtil;

/**
 * GSearch suggestion index writer.
 * 
 * @author Petteri Karttunen
 * @author Michael C. Han
 */
@Component(
	immediate = true, 
	property = "search.engine.impl=GSearch",
	service = SpellCheckIndexWriter.class
)
public class GSearchQuerySuggestionIndexWriter extends BaseGenericSpellCheckIndexWriter {

	@Override
	public void clearQuerySuggestionDictionaryIndexes(SearchContext searchContext) 
			throws SearchException {

		try {
			deleteDocuments(searchContext, SuggestionConstants.TYPE_QUERY_SUGGESTION);
		} catch (Exception e) {
			throw new SearchException("Unable to clear query suggestions", e);
		}
	}

	@Override
	public void clearSpellCheckerDictionaryIndexes(SearchContext searchContext) throws SearchException {

		try {
			deleteDocuments(searchContext, SuggestionConstants.TYPE_SPELL_CHECKER);
		} catch (Exception e) {
			throw new SearchException("Unable to to clear spell checks", e);
		}
	}

	@Override
	protected void addDocument(String documentType, SearchContext searchContext, Document document)
			throws SearchException {

		String indexName = GSearchIndexUtil.getQuerySuggestionIndexName(searchContext.getCompanyId());
		
		IndexDocumentRequest indexDocumentRequest = new IndexDocumentRequest(
			indexName, document);

		indexDocumentRequest.setType(GSearchIndexUtil.getQuerySuggestionType());

		searchEngineAdapter.execute(indexDocumentRequest);
	}


	@Override
	protected void addDocuments(
		String documentType, SearchContext searchContext,
		Collection<Document> documents) {

		String indexName = GSearchIndexUtil.getQuerySuggestionIndexName(searchContext.getCompanyId());

		BulkDocumentRequest bulkDocumentRequest = new BulkDocumentRequest();

		documents.forEach(
			document -> {
				IndexDocumentRequest indexDocumentRequest =
					new IndexDocumentRequest(indexName, document);

				indexDocumentRequest.setType(GSearchIndexUtil.getQuerySuggestionType());

				bulkDocumentRequest.addBulkableDocumentRequest(
					indexDocumentRequest);

			});

		searchEngineAdapter.execute(bulkDocumentRequest);
	}
	
	@Override
	protected Document createDocument(long companyId, long groupId, String languageId, String keywords, float weight,
			String keywordFieldName, String typeFieldValue, int maxNGramLength) {

		Document document = createDocument();

		Localization localization = getLocalization();

		String localizedName = localization.getLocalizedName(keywordFieldName, languageId);

		document.addKeyword(localizedName, keywords);

		document.addKeyword(Field.COMPANY_ID, companyId);
		document.addKeyword(Field.GROUP_ID, groupId);
		document.addKeyword(Field.LANGUAGE_ID, languageId);
		document.addKeyword(Field.PRIORITY, String.valueOf(weight));
		document.addKeyword(Field.TYPE, typeFieldValue);
		document.addKeyword(Field.UID, getUID(companyId, keywordFieldName, languageId, keywords));

		return document;
	}

	protected void deleteDocuments(
		SearchContext searchContext, String typeFieldValue) {

		try {
			String indexName = GSearchIndexUtil.getQuerySuggestionIndexName(searchContext.getCompanyId());

			Filter termFilter = new TermFilter(Field.TYPE, typeFieldValue);

			BooleanFilter booleanFilter = new BooleanFilter();

			booleanFilter.add(termFilter, BooleanClauseOccur.MUST);

			MatchAllQuery matchAllQuery = new MatchAllQuery();

			BooleanQuery booleanQuery = new BooleanQueryImpl();

			booleanQuery.setPreBooleanFilter(booleanFilter);

			booleanQuery.add(matchAllQuery, BooleanClauseOccur.MUST);

			DeleteByQueryDocumentRequest deleteByQueryDocumentRequest =
				new DeleteByQueryDocumentRequest(matchAllQuery, indexName);

			if (PortalRunMode.isTestMode() ||
				searchContext.isCommitImmediately()) {

				deleteByQueryDocumentRequest.setRefresh(true);
			}

			searchEngineAdapter.execute(deleteByQueryDocumentRequest);
		}
		catch (ParseException pe) {
			throw new SystemException(pe);
		}
	}
	
	protected Localization getLocalization() {

		// See LPS-72507 and LPS-76500

		if (localization != null) {
			return localization;
		}

		return LocalizationUtil.getLocalization();
	}
	
	public static final String TYPE_QUERY_SUGGESTION = "querySuggestion";
	
	@Reference(unbind = "-")
	protected IndexNameBuilder indexNameBuilder;

	protected Localization localization;

	@Reference(target = "(search.engine.impl=Elasticsearch)")
	protected SearchEngineAdapter searchEngineAdapter;
}