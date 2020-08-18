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

package com.liferay.portal.search.tuning.gsearch.impl.internal.aggregations.facet.handler;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.aggregation.bucket.Bucket;
import com.liferay.portal.search.aggregation.bucket.TermsAggregationResult;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.FacetConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.constants.JSONResponseKeys;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.message.Message;
import com.liferay.portal.search.tuning.gsearch.message.Severity;
import com.liferay.portal.search.tuning.gsearch.spi.aggregation.facet.FacetHandler;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, property = "name=asset_categories_by_vocabulary",
	service = FacetHandler.class
)
public class AssetCategoriesByVocabularyFacetHandler
	extends BaseFacetHandler implements FacetHandler {

	@Override
	public Optional<JSONObject> getResultsObject(
		SearchRequestContext searchRequestContext,
		AggregationResult aggregationResult,
		JSONObject configurationJsonObject) {

		TermsAggregationResult termsAggregationResult =
			(TermsAggregationResult)aggregationResult;

		JSONObject handlerParametersJsonObject =
			configurationJsonObject.getJSONObject(
				FacetConfigurationKeys.HANDLER_PARAMETERS.getJsonKey());

		if (handlerParametersJsonObject == null) {
			searchRequestContext.addMessage(
				new Message(
					Severity.ERROR, "core",
					"core.error.facet-handler-parameters-missing", null, null,
					handlerParametersJsonObject,
					FacetConfigurationKeys.HANDLER_PARAMETERS.getJsonKey(),
					null));

			return Optional.empty();
		}

		Long vocabularyId = handlerParametersJsonObject.getLong(
			"vocabulary_id", -1);

		if (vocabularyId < 0) {
			searchRequestContext.addMessage(
				new Message(
					Severity.ERROR, "core",
					"core.error.facet-asset-vocabulary-id-parameter-missing",
					null, null, handlerParametersJsonObject, "vocabulary_id",
					null));

			return Optional.empty();
		}

		Locale locale = searchRequestContext.getLocale();

		JSONArray termsArray = null;

		try {
			termsArray = _getCategoriesArray(
				termsAggregationResult, vocabularyId, locale);
		}
		catch (PortalException pe) {
			searchRequestContext.addMessage(
				new Message(
					Severity.ERROR, "core",
					"core.error.asset-vocabulary-not-found", pe.getMessage(),
					pe, handlerParametersJsonObject, "vocabulary_id",
					String.valueOf(vocabularyId)));

			if (_log.isWarnEnabled()) {
				_log.warn("Asset vocabulary " + vocabularyId + " not found.");
			}

			return Optional.empty();
		}

		return createResultObject(termsArray, configurationJsonObject);
	}

	private JSONArray _getCategoriesArray(
			TermsAggregationResult termsAggregationResult, long vocabularyId,
			Locale locale)
		throws PortalException {

		AssetVocabulary assetVocabulary =
			_assetVocabularyLocalService.getAssetVocabulary(vocabularyId);

		List<AssetCategory> assetCategories = assetVocabulary.getCategories();

		JSONArray termsArray = JSONFactoryUtil.createJSONArray();

		for (AssetCategory assetCategory : assetCategories) {
			for (Bucket bucket : termsAggregationResult.getBuckets()) {
				if (Long.valueOf(bucket.getKey()) ==
						assetCategory.getCategoryId()) {

					JSONObject item = JSONFactoryUtil.createJSONObject();

					item.put(JSONResponseKeys.FREQUENCY, bucket.getDocCount());
					item.put(
						JSONResponseKeys.NAME, assetCategory.getTitle(locale));
					item.put(JSONResponseKeys.VALUE, bucket.getKey());
					termsArray.put(item);
				}
			}
		}

		return termsArray;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AssetCategoriesByVocabularyFacetHandler.class);

	@Reference
	private AssetVocabularyLocalService _assetVocabularyLocalService;

}