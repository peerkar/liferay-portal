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
import com.liferay.portal.kernel.search.suggest.Suggester;
import com.liferay.portal.kernel.search.suggest.TermSuggester;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.suggester.CommonSuggesterConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.suggester.TermSuggesterConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.spi.suggester.SuggesterBuilder;

import java.util.Optional;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, property = "type=term", service = SuggesterBuilder.class
)
public class TermSuggesterBuilder
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

		TermSuggester termSuggester = new TermSuggester(
			createSuggesterName("term"), field, text);

		if (!configurationJsonObject.isNull(
					TermSuggesterConfigurationKeys.ACCURACY.getJsonKey())) {

			termSuggester.setAccuracy(
				GetterUtil.getFloat(
					configurationJsonObject.get(
						TermSuggesterConfigurationKeys.ACCURACY.getJsonKey())));
		}

		if (!configurationJsonObject.isNull(
					CommonSuggesterConfigurationKeys.ANALYZER.getJsonKey())) {

			termSuggester.setAccuracy(
				GetterUtil.getFloat(
					configurationJsonObject.get(
						CommonSuggesterConfigurationKeys.ANALYZER.
							getJsonKey())));
		}

		// TODO: implement the rest.
		// https://www.elastic.co/guide/en/elasticsearch/reference/current/search-suggesters.html#term-suggester

		return Optional.of(termSuggester);
	}

}