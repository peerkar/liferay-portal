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

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.search.experiences.ingest.web.internal.importer.JournalArticleImporterImpl;
import com.liferay.search.experiences.ingest.web.internal.util.CSVUtil;
import com.liferay.search.experiences.ingest.web.internal.util.ExpandoUtil;
import com.liferay.search.experiences.ingest.web.internal.util.TagUtil;

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
	enabled = false, immediate = true, property = "type=google_places",
	service = Ingester.class
)
public class GooglePlacesIngester implements Ingester {

	@Override
	public Map<String, List<String>> ingest(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		try {
			ExpandoUtil.createGeoLocationExpandoAttribute(
				_LOCATION_EXPANDO_FIELD, JournalArticle.class, actionRequest);
		}
		catch (PortalException portalException) {
			_log.error(portalException);

			return Collections.emptyMap();
		}

		return _ingest(actionRequest);
	}

	private void _addLocationExpandoAttribute(
		JournalArticle journalArticle, String lat, String lng) {

		JSONObject jsonObject = JSONUtil.put(
			"latitude", GetterUtil.getDouble(lat)
		).put(
			"longitude", GetterUtil.getDouble(lng)
		);

		ExpandoBridge expandoBridge = journalArticle.getExpandoBridge();

		expandoBridge.setAttribute(_LOCATION_EXPANDO_FIELD, jsonObject, false);
	}

	private String _getAPIUrl(ActionRequest actionRequest) {
		String googlePlacesApiKey = ParamUtil.getString(
			actionRequest, "googlePlacesApiKey");

		String googlePlacesLatitude = ParamUtil.getString(
			actionRequest, "googlePlacesLatitude");

		String googlePlacesLongitude = ParamUtil.getString(
			actionRequest, "googlePlacesLongitude");

		String googlePlacesRadius = ParamUtil.getString(
			actionRequest, "googlePlacesRadius");

		String googlePlacesType = ParamUtil.getString(
			actionRequest, "googlePlacesType");

		if (Validator.isBlank(googlePlacesApiKey) ||
			Validator.isBlank(googlePlacesLatitude) ||
			Validator.isBlank(googlePlacesLongitude) ||
			Validator.isBlank(googlePlacesRadius) ||
			Validator.isBlank(googlePlacesType)) {

			return null;
		}

		String googlePlacesKeywords = ParamUtil.getString(
			actionRequest, "googlePlacesKeywords");

		StringBundler sb = new StringBundler(13);

		sb.append(_API_BASE_URL);
		sb.append("?location=");
		sb.append(googlePlacesLatitude);
		sb.append(",");
		sb.append(googlePlacesLongitude);
		sb.append("&radius=");
		sb.append(googlePlacesRadius);
		sb.append("&type=");
		sb.append(googlePlacesType);

		if (!Validator.isBlank(googlePlacesKeywords)) {
			sb.append("&keyword=");
			sb.append(googlePlacesKeywords);
		}

		sb.append("&key=");
		sb.append(googlePlacesApiKey);

		return sb.toString();
	}

	private String[] _getAssetTagNames(JSONObject jsonObject) {
		JSONArray jsonArray = jsonObject.getJSONArray("types");

		if (jsonArray.length() == 0) {
			return new String[0];
		}

		List<String> assetTagNames = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			String type = jsonArray.getString(i);

			if (!TagUtil.isValidTag(type)) {
				assetTagNames.add(TagUtil.cleanTag(type));
			}
		}

		return assetTagNames.toArray(new String[0]);
	}

	private String _getContent(JSONObject jsonObject) {
		StringBundler sb = new StringBundler(1);

		sb.append(jsonObject.getString("vicinity"));

		return sb.toString();
	}

	private JSONObject _getLocationJSONObject(JSONObject jsonObject) {
		JSONObject geometryJSONObject = jsonObject.getJSONObject("geometry");

		return geometryJSONObject.getJSONObject("location");
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
			JSONObject jsonObject = _jsonFactory.createJSONObject(
				_http.URLtoString(apiUrl));

			JSONArray jsonArray = jsonObject.getJSONArray("results");

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject resultJSONObject = jsonArray.getJSONObject(i);

				JSONObject locationJSONObject = _getLocationJSONObject(
					resultJSONObject);

				JournalArticle journalArticle =
					journalArticleImporterImpl.addJournalArticle(
						_getAssetTagNames(resultJSONObject),
						_getContent(resultJSONObject),
						resultJSONObject.getString("name"));

				_addLocationExpandoAttribute(
					journalArticle, locationJSONObject.getString("lat"),
					locationJSONObject.getString("lng"));

				journalArticleImporterImpl.updateJournalArticle(journalArticle);
			}
		}
		catch (IOException | JSONException exception) {
			_log.error(exception);
		}

		return journalArticleImporterImpl.getIngestResults();
	}

	private static final String _API_BASE_URL =
		"https://maps.googleapis.com/maps/api/place/nearbysearch/json";

	private static final String _LOCATION_EXPANDO_FIELD = "location";

	private static final Log _log = LogFactoryUtil.getLog(
		GooglePlacesIngester.class);

	@Reference
	private Http _http;

	@Reference
	private JournalArticleLocalService _journalArticleLocalService;

	@Reference
	private JSONFactory _jsonFactory;

}