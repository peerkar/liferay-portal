/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.search.experiences.ingest.web.internal.ingester;

import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.document.DocumentBuilderFactory;
import com.liferay.search.experiences.ingest.web.internal.importer.JournalArticleImporter;
import com.liferay.search.experiences.ingest.web.internal.importer.JournalArticleImporterImpl;
import com.liferay.search.experiences.ingest.web.internal.ingester.liferay.download.Downloader;
import com.liferay.search.experiences.ingest.web.internal.ingester.liferay.scrape.Crawler;
import com.liferay.search.experiences.ingest.web.internal.ingester.liferay.scrape.CrawlerImpl;
import com.liferay.search.experiences.ingest.web.internal.ingester.liferay.scrape.Scraper;
import com.liferay.search.experiences.ingest.web.internal.ingester.liferay.scrape.ScraperFactory;
import com.liferay.search.experiences.ingest.web.internal.util.CSVUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.apache.commons.lang.StringUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author André de Oliveira
 * @author Petteri Karttunen
 * @author Gustavo Lima
 */
@Component(
	enabled = false, immediate = true, property = "type=liferay_learn",
	service = Ingester.class
)
public class LiferayLearnIngester implements Ingester {

	@Override
	public Map<String, List<String>> ingest(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		JournalArticleImporter journalArticleImporter =
			new JournalArticleImporterImpl(
				CSVUtil.csvToLongList(
					ParamUtil.getString(
						actionRequest, "groupIds",
						String.valueOf(themeDisplay.getScopeGroupId()))),
				_journalArticleLocalService,
				ParamUtil.getString(actionRequest, "languageId", "en_US"),
				actionRequest,
				CSVUtil.csvToLongList(
					ParamUtil.getString(
						actionRequest, "userIds",
						String.valueOf(themeDisplay.getUserId()))));

		Scraper scraper = _scraperFactory.builder(
		).consumeImmediately(
			false
		).crawlerBuilder(
			_getCrawlerBuilder()
		).journalArticleImporter(
			journalArticleImporter
		).onAddress(
			this::_ingest
		).build();

		scraper.scrape();

		return journalArticleImporter.getIngestResults();
	}

	private Crawler _buildCrawler(Consumer<String> consumer) {
		return CrawlerImpl.builder(
		).base(
			"https://learn.liferay.com/"
		).listLinksDelimiter(
			"<section class=\"col-md-12 justify-content-center products\">",
			"</section>"
		).delimiter(
			"</a>"
		).ignores(
			new ArrayList<>(Arrays.asList("reference/latest/en/index.html"))
		).html(
			_downloader.download("https://learn.liferay.com/index.html")
		).onAddress(
			address -> _crawl1(consumer, address)
		).build();
	}

	private void _crawl1(Consumer<String> consumer, String seed) {
		CrawlerImpl.builder(
		).base(
			StringUtils.substringBefore(seed, "index.html")
		).listLinksDelimiter(
			"<ul>", "</ul>"
		).html(
			_downloader.download(seed)
		).onAddress(
			address -> _crawl2(consumer, address)
		).build(
		).crawl();
	}

	private void _crawl2(Consumer<String> consumer, String seed) {
		CrawlerImpl.builder(
		).base(
			StringUtils.substringBeforeLast(seed, "/") + "/"
		).listLinksDelimiter(
			"<ul>", "</ul>"
		).html(
			_downloader.download(seed)
		).onAddress(
			consumer
		).build(
		).crawl();
	}

	private String[] _getAssetTagNames(String content) {
		return new String[] {_getLiferayVersion(content), "Liferay Learn"};
	}

	private Crawler.Builder _getCrawlerBuilder() {
		return new Crawler.Builder() {

			@Override
			public Crawler build() {
				return _buildCrawler(_consumer);
			}

			@Override
			public Crawler.Builder onAddress(Consumer<String> consumer) {
				_consumer = consumer;

				return this;
			}

			private Consumer<String> _consumer;

		};
	}

	private String _getLiferayVersion(String content) {
		String aux = StringUtils.substringAfter(content, "Liferay DXP 7.");

		if (aux.equals("")) {
			aux = StringUtils.substringAfter(content, "Liferay Portal 6.");

			return "Liferay DXP 6." + aux.charAt(0);
		}

		return "Liferay DXP 7." + aux.charAt(0);
	}

	private String _getTitle(String content) {
		String title = StringUtils.substringBetween(
			content, "<title>", "</title>");

		return StringUtils.substringBeforeLast(title, "&");
	}

	private void _ingest(
		String address, JournalArticleImporter journalArticleImporter) {

		String content = _downloader.download(address);

		if (Validator.isBlank(content)) {
			return;
		}

		journalArticleImporter.addJournalArticle(
			_getAssetTagNames(content), _sanitizeContent(content),
			_getTitle(content));
	}

	private String _sanitizeContent(String content) {
		Document doc = Jsoup.parse(content);

		Elements elements = doc.select("div#docContent");

		return elements.html();
	}

	@Reference
	private DocumentBuilderFactory _documentBuilderFactory;

	@Reference
	private Downloader _downloader;

	@Reference
	private JournalArticleLocalService _journalArticleLocalService;

	@Reference
	private ScraperFactory _scraperFactory;

}