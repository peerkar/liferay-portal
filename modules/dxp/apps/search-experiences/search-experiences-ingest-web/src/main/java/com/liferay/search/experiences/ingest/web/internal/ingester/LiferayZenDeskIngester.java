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
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.search.experiences.ingest.web.internal.importer.JournalArticleImporterImpl;
import com.liferay.search.experiences.ingest.web.internal.util.CSVUtil;
import com.liferay.search.experiences.ingest.web.internal.util.TagUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author André de Oliveira
 * @author Petteri Karttunen
 * @author Gustavo Lima
 */
@Component(
	enabled = false, immediate = true, property = "type=liferay_zendesk",
	service = Ingester.class
)
public class LiferayZenDeskIngester implements Ingester {

	@Override
	public Map<String, List<String>> ingest(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		return _ingest(actionRequest);
	}

	private String _getApiUrl(int page) {
		return StringBundler.concat(_API_URL, "?page=", page, "&per_page=30");
	}

	private String[] _getAssetTagNames(JSONObject jsonObject) {
		List<String> assetTagNames = new ArrayList<>();

		assetTagNames.add("Liferay Help Center");

		JSONArray jsonArray = jsonObject.getJSONArray("label_names");

		for (int i = 0; i < jsonArray.length(); i++) {
			String tag = jsonArray.getString(i);

			if (TagUtil.isValidTag(tag)) {
				assetTagNames.add(TagUtil.cleanTag(tag));
			}
		}

		return assetTagNames.toArray(new String[0]);
	}

	private String _getContent(JSONObject jsonObject) {
		return jsonObject.getString("body");
	}

	private Map<String, List<String>> _ingest(ActionRequest actionRequest) {
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		int numOfPages = ParamUtil.getInteger(actionRequest, "numOfPages", 1);

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

		try {
			for (int i = 0; i < numOfPages; i++) {
				JSONObject jsonObject = _jsonFactory.createJSONObject(
					_http.URLtoString(_getApiUrl(i + 1)));

				JSONArray jsonArray = jsonObject.getJSONArray("articles");

				for (int j = 0; j < jsonArray.length(); j++) {
					JSONObject resultJSONObject = jsonArray.getJSONObject(j);

					journalArticleImporterImpl.addJournalArticle(
						_getAssetTagNames(resultJSONObject),
						_getContent(resultJSONObject),
						resultJSONObject.getString("title"));
				}
			}
		}
		catch (IOException | JSONException exception) {
			_log.error(exception);
		}

		return journalArticleImporterImpl.getIngestResults();
	}

	private static final String _API_URL =
		"https://liferay-support.zendesk.com/api/v2/help_center/en-us" +
			"/articles.json";

	private static final Log _log = LogFactoryUtil.getLog(
		LiferayZenDeskIngester.class);

	@Reference
	private Http _http;

	@Reference
	private JournalArticleLocalService _journalArticleLocalService;

	@Reference
	private JSONFactory _jsonFactory;
}