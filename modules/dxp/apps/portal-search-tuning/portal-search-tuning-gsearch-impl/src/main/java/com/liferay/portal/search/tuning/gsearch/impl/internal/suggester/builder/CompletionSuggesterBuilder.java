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

package com.liferay.portal.search.tuning.gsearch.impl.internal.suggester.builder;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.search.suggest.CompletionSuggester;
import com.liferay.portal.kernel.search.suggest.Suggester;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.suggester.CommonSuggesterConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.spi.suggester.SuggesterBuilder;

import java.util.Optional;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, property = "type=completion",
	service = SuggesterBuilder.class
)
public class CompletionSuggesterBuilder
	extends BaseSuggesterBuilder implements SuggesterBuilder {

	@Override
	public Optional<Suggester> build(
		SearchRequestContext searchRequestContext,
		JSONObject configurationJsonObject) {

		if (!validateSuggesterConfiguration(
				searchRequestContext, configurationJsonObject)) {

			return Optional.empty();
		}

		String field = configurationJsonObject.getString(
			CommonSuggesterConfigurationKeys.FIELD.getJsonKey());

		String text = configurationJsonObject.getString(
			CommonSuggesterConfigurationKeys.TEXT.getJsonKey(),
			searchRequestContext.getKeywords());

		text = text.toLowerCase();

		CompletionSuggester completionSuggester = new CompletionSuggester(
			createSuggesterName("completion"), field, text);

		if (!configurationJsonObject.isNull(
					CommonSuggesterConfigurationKeys.ANALYZER.getJsonKey())) {

			completionSuggester.setAnalyzer(
				configurationJsonObject.getString(
					CommonSuggesterConfigurationKeys.ANALYZER.getJsonKey()));
		}

		if (!configurationJsonObject.isNull(
					CommonSuggesterConfigurationKeys.SHARD_SIZE.
						getJsonKey())) {

			completionSuggester.setShardSize(
				configurationJsonObject.getInt(
					CommonSuggesterConfigurationKeys.SHARD_SIZE.getJsonKey()));
		}

		if (!configurationJsonObject.isNull(
					CommonSuggesterConfigurationKeys.SIZE.getJsonKey())) {

			completionSuggester.setSize(
				configurationJsonObject.getInt(
					CommonSuggesterConfigurationKeys.SIZE.getJsonKey()));
		}

		return Optional.of(completionSuggester);
	}

}