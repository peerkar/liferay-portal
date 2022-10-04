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
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.search.experiences.ingest.web.internal.importer.JournalArticleImporter;
import com.liferay.search.experiences.ingest.web.internal.importer.JournalArticleImporterImpl;
import com.liferay.search.experiences.ingest.web.internal.util.CSVUtil;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
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

		_crawlHomePage(
			ParamUtil.getInteger(actionRequest, "liferayLearnLevelsToCrawl", 2),
			journalArticleImporter);

		return journalArticleImporter.getIngestResults();
	}

	private void _crawlHomePage(
		int levelsToCrawl, JournalArticleImporter journalArticleImporter) {

		Document document = _getDocument(_ROOT_URL);

		if (document == null) {
			_log.error("Unable to retrieve homepage");

			return;
		}

		for (Element link : document.select(".products a[href]")) {
			if (_isValidLink(link.attr("href"))) {
				_crawlTopic(
					journalArticleImporter, levelsToCrawl, link.absUrl("href"),
					new ArrayList<String>());
			}
		}
	}

	private void _crawlTopic(
		JournalArticleImporter journalArticleImporter, int levelsToCrawl,
		String url, List<String> visitedPages) {

		visitedPages.add(url);

		Document document = _getDocument(url);

		if (document == null) {
			return;
		}

		for (Element link :
				document.select(".doc-nav .toctree-l1 a.reference.internal")) {

			if (_isValidLink(link.attr("href"))) {
				_crawlTopicChildren(
					journalArticleImporter, 2, levelsToCrawl,
					link.absUrl("href"), visitedPages);
			}
		}
	}

	private void _crawlTopicChildren(
		JournalArticleImporter journalArticleImporter, int level,
		int levelsToCrawl, String url, List<String> visitedPages) {

		if (visitedPages.contains(url) || (level > levelsToCrawl)) {
			return;
		}

		visitedPages.add(url);

		Document document = _getDocument(url);

		if (document == null) {
			return;
		}

		journalArticleImporter.addJournalArticle(
			_getAssetTagNames(), _getContent(document), _getTitle(document));

		for (Element link :
				document.select(
					".doc-nav .toctree-l" + level + " a.reference.internal")) {

			String href = link.attr("href");

			if (_isValidLink(href)) {
				_crawlTopicChildren(
					journalArticleImporter, level + 1, levelsToCrawl,
					link.absUrl("href"), visitedPages);
			}
		}
	}

	private String[] _getAssetTagNames() {
		return new String[] {"Liferay Learn"};
	}

	private String _getContent(Document document) {
		Elements elements = document.select(
			"#docContent .article-body .section " +
				">*:not(.toctree-wrapper):not(.toctree-wrapper + ul)");

		return elements.html();
	}

	private Document _getDocument(String url) {
		try {
			Connection connection = Jsoup.connect(url);

			Document document = connection.get();

			Connection.Response response = connection.response();

			if (response.statusCode() == 200) {
				return document;
			}
		}
		catch (IOException ioException) {
			_log.error(ioException);
		}

		return null;
	}

	private String _getTitle(Document document) {
		Elements elements = document.select("#breadcrumbCurrentArticle");

		return elements.html();
	}

	private boolean _isValidLink(String link) {
		if (!_excludedLinks.contains(link) &&
			!_excludedLinkPrefixes.contains(link)) {

			return true;
		}

		return false;
	}

	private static final String _ROOT_URL =
		"https://learn.liferay.com/index.html";

	private static final Log _log = LogFactoryUtil.getLog(
		LiferayLearnIngester.class);

	private static final List<String> _excludedLinkPrefixes =
		new ArrayList<String>() {
			{
				add("../");
				add("http:");
				add("https:");
			}
		};
	private static final List<String> _excludedLinks = new ArrayList<String>() {
		{
			add("reference/latest/en/index.html");
			add("#");
		}
	};

	@Reference
	private JournalArticleLocalService _journalArticleLocalService;

}