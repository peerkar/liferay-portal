
package fi.soveltia.liferay.gsearch.elasticsearch.internal;

import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;

import java.util.Collection;

/**
 * @author Petteri Karttunen
 * @author Michael C. Han
 */
public interface GSearchUpdateDocumentCommand {

	public String updateDocument(
			String documentType, SearchContext searchContext, Document document,
			boolean deleteFirst)
		throws SearchException;

	public void updateDocuments(
			String documentType, SearchContext searchContext,
			Collection<Document> documents, boolean deleteFirst)
		throws SearchException;

}