
package fi.soveltia.liferay.gsearch.elasticsearch.internal;

import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.IndexWriter;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.suggest.SpellCheckIndexWriter;
import com.liferay.portal.kernel.search.suggest.SuggestionConstants;
import com.liferay.portal.kernel.util.Localization;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.elasticsearch6.internal.document.ElasticsearchUpdateDocumentCommand;
import com.liferay.portal.search.elasticsearch6.internal.index.IndexNameBuilder;
import com.liferay.portal.search.elasticsearch6.internal.util.DocumentTypes;
import com.liferay.portal.search.suggest.BaseGenericSpellCheckIndexWriter;

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

import com.liferay.portal.search.elasticsearch6.internal.SearchHitsProcessor;
import com.liferay.portal.search.elasticsearch6.internal.SearchResponseScroller;
import com.liferay.portal.search.elasticsearch6.internal.DeleteDocumentsSearchHitsProcessor;

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

		_elasticsearchUpdateDocumentCommand.updateDocument(GSearchIndexUtil.getQuerySuggestionType(), searchContext, document, false);
	}

	@Override
	protected void addDocuments(String documentType, SearchContext searchContext, Collection<Document> documents)
			throws SearchException {

		_elasticsearchUpdateDocumentCommand.updateDocuments(GSearchIndexUtil.getQuerySuggestionType(), searchContext, documents, false);
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

	protected void deleteDocuments(SearchContext searchContext, String typeFieldValue) throws Exception {

		if (_searchHitsProcessor == null) {
			throw new IllegalStateException("Module not properly initialized");
		}

		SearchResponseScroller searchResponseScroller = null;

		try {
			Client client = elasticsearchConnectionManager.getClient();

			MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(Field.TYPE, typeFieldValue);

			searchResponseScroller = new SearchResponseScroller(client, searchContext, indexNameBuilder,
					matchQueryBuilder, TimeValue.timeValueSeconds(30), GSearchIndexUtil.getQuerySuggestionType());

			searchResponseScroller.prepare();

			searchResponseScroller.scroll(_searchHitsProcessor);
		} finally {
			if (searchResponseScroller != null) {
				searchResponseScroller.close();
			}
		}
	}

	protected Localization getLocalization() {

		// See LPS-72507 and LPS-76500

		if (localization != null) {
			return localization;
		}

		return LocalizationUtil.getLocalization();
	}
	
	@Reference(
		cardinality = ReferenceCardinality.OPTIONAL, 
		policy = ReferencePolicy.DYNAMIC, 
		policyOption = ReferencePolicyOption.GREEDY, 
		target = "(!(search.engine.impl=*))"
	)
	protected void setIndexWriter(IndexWriter indexWriter) {
		_searchHitsProcessor = new DeleteDocumentsSearchHitsProcessor(indexWriter);
	}
	
	protected void unsetIndexWriter(IndexWriter indexWriter) {
		_searchHitsProcessor = null;
	}

	public static final String TYPE_QUERY_SUGGESTION = "querySuggestion";

	
	@Reference(unbind = "-")
	protected ElasticsearchConnectionManager elasticsearchConnectionManager;

	@Reference
	private GSearchUpdateDocumentCommand _elasticsearchUpdateDocumentCommand;

	@Reference(unbind = "-")
	protected IndexNameBuilder indexNameBuilder;

	protected Localization localization;

	private volatile SearchHitsProcessor _searchHitsProcessor;

}