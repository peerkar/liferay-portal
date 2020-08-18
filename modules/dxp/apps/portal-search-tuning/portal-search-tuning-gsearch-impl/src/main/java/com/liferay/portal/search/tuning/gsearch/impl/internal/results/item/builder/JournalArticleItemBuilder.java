
package com.liferay.portal.search.tuning.gsearch.impl.internal.results.item.builder;

import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.webserver.WebServerServletTokenUtil;
import com.liferay.portal.search.document.Document;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.results.ResultAttributes;
import com.liferay.portal.search.tuning.gsearch.spi.results.item.ResultItemBuilder;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * JournalArticle item type result builder.
 *
 * @author Petteri Karttunen
 */
@Component(
	immediate = true,
	property = "model.class.name=com.liferay.journal.model.JournalArticle",
	service = ResultItemBuilder.class
)
public class JournalArticleItemBuilder
	extends BaseResultItemBuilder implements ResultItemBuilder {

	@Override
	public String getThumbnail(
			SearchRequestContext queryContext,
			ResultAttributes resultAttributes, Document document)
		throws Exception {

		long smallImageId = _getJournalArticle(
			document
		).getSmallImageId();

		StringBundler sb = new StringBundler(4);

		sb.append("/image/journal/article?img_id=");
		sb.append(String.valueOf(smallImageId));
		sb.append("&t=");
		sb.append(WebServerServletTokenUtil.getToken(smallImageId));

		return sb.toString();
	}

	private JournalArticle _getJournalArticle(Document document)
		throws PortalException {

		return _journalArticleService.getLatestArticle(
			document.getLong(Field.ENTRY_CLASS_PK));
	}

	@Reference
	private JournalArticleService _journalArticleService;

}