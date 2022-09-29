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
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.URLCodec;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.search.experiences.ingest.web.internal.importer.JournalArticleImporter;
import com.liferay.search.experiences.ingest.web.internal.importer.JournalArticleImporterImpl;
import com.liferay.search.experiences.ingest.web.internal.util.CSVUtil;
import com.liferay.search.experiences.ingest.web.internal.util.TagUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	enabled = false, immediate = true, property = "type=wikipedia",
	service = Ingester.class
)
public class WikipediaIngester implements Ingester {

	@Override
	public Map<String, List<String>> ingest(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		List<String> articleList = CSVUtil.csvtoStringList(
			ParamUtil.getString(actionRequest, "wikiArticles"));

		if (articleList.isEmpty()) {
			_log.error("Root article not given");

			return Collections.emptyMap();
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		JournalArticleImporterImpl journalArticleImporterImpl =
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

		_ingest(
			CSVUtil.csvtoStringList(
				ParamUtil.getString(actionRequest, "wikiArticles")),
			0, journalArticleImporterImpl,
			ParamUtil.getInteger(actionRequest, "numberOfArticles", 10),
			ParamUtil.getString(actionRequest, "wikiLanguage", "en"));

		return journalArticleImporterImpl.getIngestResults();
	}

	private String _getAPIUrl(String wikiLanguage, String article) {
		StringBundler sb = new StringBundler(5);

		sb.append("https://");
		sb.append(wikiLanguage);
		sb.append(_API_URL_SUFFIX);
		sb.append(URLCodec.encodeURL(article));
		sb.append("&format=json");

		return sb.toString();
	}

	private List<String> _getArticleLinks(JSONObject jsonObject) {
		List<String> articleLinks = new ArrayList<>();

		JSONArray jsonArray = jsonObject.getJSONArray("links");

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject linkJSONObject = jsonArray.getJSONObject(i);

			int ns = linkJSONObject.getInt("ns");

			if (ns != 0) {
				continue;
			}

			articleLinks.add(linkJSONObject.getString("*"));
		}

		return articleLinks;
	}

	private String[] _getAssetTagNames(JSONObject jsonObject) {
		JSONArray jsonArray = jsonObject.getJSONArray("categories");

		if (jsonArray.length() == 0) {
			return new String[0];
		}

		List<String> assetTagNames = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject categoryJSONObject = jsonArray.getJSONObject(i);

			if (categoryJSONObject.getBoolean("hidden")) {
				continue;
			}

			String tag = categoryJSONObject.getString("*");

			if (TagUtil.isValidTag(tag)) {
				assetTagNames.add(TagUtil.cleanTag(tag));
			}
		}

		return assetTagNames.toArray(new String[0]);
	}

	private String _getContent(JSONObject jsonObject) {
		JSONObject textJSONObject = jsonObject.getJSONObject("text");

		return textJSONObject.getString("*");
	}

	private void _ingest(
		List<String> articleList, int counter,
		JournalArticleImporter journalArticleImporter, int limit,
		String wikiLanguage) {

		List<String> articleLinks = new ArrayList<>();

		for (String article : articleList) {
			if (counter >= limit) {
				return;
			}

			try {
				JSONObject rootJSONObject = _jsonFactory.createJSONObject(
					_http.URLtoString(_getAPIUrl(wikiLanguage, article)));

				JSONObject parseJSONObject = rootJSONObject.getJSONObject(
					"parse");

				String title = parseJSONObject.getString("title");

				List<String> importedTitles =
					journalArticleImporter.getIngestedTitles();

				if (importedTitles.contains(title)) {
					continue;
				}

				journalArticleImporter.addJournalArticle(
					_getAssetTagNames(parseJSONObject),
					_getContent(parseJSONObject), title);

				articleLinks.addAll(_getArticleLinks(parseJSONObject));
			}
			catch (Exception exception) {
				_log.error(exception);
			}

			counter++;
		}

		if (articleLinks.isEmpty()) {
			return;
		}

		_ingest(
			articleLinks, counter, journalArticleImporter, limit, wikiLanguage);
	}

	private static final String _API_URL_SUFFIX =
		".wikipedia.org/w/api.php?action=parse&page=";

	private static final Log _log = LogFactoryUtil.getLog(
		WikipediaIngester.class);

	@Reference
	private Http _http;

	@Reference
	private JournalArticleLocalService _journalArticleLocalService;

	@Reference
	private JSONFactory _jsonFactory;

}