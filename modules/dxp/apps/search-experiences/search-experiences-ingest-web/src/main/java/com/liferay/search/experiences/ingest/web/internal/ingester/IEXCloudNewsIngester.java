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

import com.liferay.journal.model.JournalArticle;
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
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.search.experiences.ingest.web.internal.importer.JournalArticleImporterImpl;
import com.liferay.search.experiences.ingest.web.internal.util.CSVUtil;

import java.io.IOException;

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
	enabled = false, immediate = true, property = "type=iexcloud_news",
	service = Ingester.class
)
public class IEXCloudNewsIngester implements Ingester {

	@Override
	public Map<String, List<String>> ingest(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		return _ingest(actionRequest);
	}

	private String _getAPIUrl(ActionRequest actionRequest) {
		String iexCloudNewsRange = ParamUtil.getString(
			actionRequest, "iexCloudNewsRange", "1d");

		String iexCloudNewsSymbol = ParamUtil.getString(
			actionRequest, "iexCloudNewsSymbol", "AAPL");

		String iexCloudNewsToken = ParamUtil.getString(
			actionRequest, "iexCloudNewsToken");

		String iexCloudNewsWorkspaceName = ParamUtil.getString(
			actionRequest, "iexCloudNewsWorkspaceName");

		if (Validator.isBlank(iexCloudNewsRange) ||
			Validator.isBlank(iexCloudNewsSymbol) ||
			Validator.isBlank(iexCloudNewsToken) ||
			Validator.isBlank(iexCloudNewsWorkspaceName)) {

			return null;
		}

		int iexCloudNewsLimit = ParamUtil.getInteger(
			actionRequest, "iexCloudNewsLimit", 10);

		StringBundler sb = new StringBundler(10);

		sb.append("https://");
		sb.append(iexCloudNewsWorkspaceName);
		sb.append(".iex.cloud/v1/time-series/news/");
		sb.append(iexCloudNewsSymbol);
		sb.append("?token=");
		sb.append(iexCloudNewsToken);
		sb.append("&range=");
		sb.append(iexCloudNewsRange);
		sb.append("&limit=");
		sb.append(iexCloudNewsLimit);

		return sb.toString();
	}

	private String[] _getAssetTagNames(JSONObject jsonObject) {
		List<String> assetTagNames = new ArrayList<>();

		assetTagNames.add(jsonObject.getString("provider"));
		assetTagNames.add(jsonObject.getString("source"));
		assetTagNames.add(jsonObject.getString("symbol"));

		return assetTagNames.toArray(new String[0]);
	}

	private Map<String, List<String>> _ingest(ActionRequest actionRequest) {
		String apiUrl = _getAPIUrl(actionRequest);

		if (Validator.isBlank(apiUrl)) {
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

		try {
			JSONArray jsonArray = _jsonFactory.createJSONArray(
				_http.URLtoString(apiUrl));

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject resultJSONObject = jsonArray.getJSONObject(i);

				JournalArticle journalArticle =
					journalArticleImporterImpl.addJournalArticle(
						_getAssetTagNames(resultJSONObject),
						resultJSONObject.getString("summary"),
						resultJSONObject.getString("headline"));

				journalArticleImporterImpl.updateJournalArticle(journalArticle);
			}
		}
		catch (IOException | JSONException exception) {
			_log.error(exception);
		}

		return journalArticleImporterImpl.getIngestResults();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		IEXCloudNewsIngester.class);

	@Reference
	private Http _http;

	@Reference
	private JournalArticleLocalService _journalArticleLocalService;

	@Reference
	private JSONFactory _jsonFactory;

}