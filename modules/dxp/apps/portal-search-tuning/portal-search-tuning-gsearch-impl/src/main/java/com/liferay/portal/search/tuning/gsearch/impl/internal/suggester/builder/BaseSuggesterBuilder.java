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

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.suggest.Suggester;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.suggester.CommonSuggesterConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.message.Message;
import com.liferay.portal.search.tuning.gsearch.message.Severity;
import com.liferay.portal.search.tuning.gsearch.spi.suggester.SuggesterBuilder;

import java.util.Optional;
import java.util.Random;

/**
 * @author Petteri Karttunen
 */
public abstract class BaseSuggesterBuilder implements SuggesterBuilder {

	@Override
	public abstract Optional<Suggester> build(
		SearchRequestContext searchRequestContext, JSONObject configurationJsonObject);

	protected String createSuggesterName(String type) {
		StringBundler sb = new StringBundler(3);

		sb.append(type);
		sb.append("-suggester-");
		sb.append(new Random().nextInt(1000));

		return sb.toString();
	}

	protected boolean validateSuggesterConfiguration(
		SearchRequestContext searchRequestContext,
		JSONObject configurationJsonObject) {

		boolean isValid = true;

		if (Validator.isBlank(
				configurationJsonObject.getString(
					CommonSuggesterConfigurationKeys.FIELD.getJsonKey()))) {

			searchRequestContext.addMessage(
				new Message(
					Severity.ERROR, "core", "core.error.undefined-suggester-field",
					null, null, configurationJsonObject,
					CommonSuggesterConfigurationKeys.FIELD.getJsonKey(), null));

			isValid = false;
			
			if (_log.isDebugEnabled()) {
				
				StringBundler sb = new StringBundler(3);
				sb.append("Suggester field undefined in [ ");
				sb.append(configurationJsonObject);
				sb.append(" ]");
				
				_log.debug(sb.toString());
			}
		}

		if (Validator.isBlank(
				configurationJsonObject.getString(
					CommonSuggesterConfigurationKeys.TEXT.getJsonKey(),
					searchRequestContext.getKeywords()))) {

			searchRequestContext.addMessage(
				new Message(
					Severity.ERROR, "core", "core.error.undefined-suggester-text",
					null, null, configurationJsonObject,
					CommonSuggesterConfigurationKeys.TEXT.getJsonKey(), null));

			isValid = false;
			
			if (_log.isDebugEnabled()) {
				
				StringBundler sb = new StringBundler(3);
				sb.append("Suggester text undefined in [ ");
				sb.append(configurationJsonObject);
				sb.append(" ]");
				
				_log.debug(sb.toString());
			}
		}

		return isValid;
	}

	private static final Log _log =
			LogFactoryUtil.getLog(BaseSuggesterBuilder.class);
	
}