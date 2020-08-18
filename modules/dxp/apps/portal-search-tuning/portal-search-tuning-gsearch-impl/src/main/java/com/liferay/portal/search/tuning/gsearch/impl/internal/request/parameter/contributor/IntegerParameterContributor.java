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

package com.liferay.portal.search.tuning.gsearch.impl.internal.request.parameter.contributor;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.RequestParameterConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.impl.util.GSearchUtil;
import com.liferay.portal.search.tuning.gsearch.parameter.IntegerParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.SearchParameterData;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, property = "type=integer",
	service = RequestParameterContributor.class
)
public class IntegerParameterContributor
	implements RequestParameterContributor {

	@Override
	public void contribute(
		HttpServletRequest httpServletRequest,
		SearchParameterData searchParameterData,
		JSONObject configurationJsonObject) {

		String parameterName = configurationJsonObject.getString(
			RequestParameterConfigurationKeys.PARAMETER_NAME.getJsonKey());

		String valueString = ParamUtil.getString(
			httpServletRequest, parameterName);

		Optional<Integer> valueOptional = GSearchUtil.stringToIntegerOptional(
			valueString);

		if (!valueOptional.isPresent()) {
			valueOptional = GSearchUtil.stringToIntegerOptional(
				configurationJsonObject.getString(
					RequestParameterConfigurationKeys.DEFAULT.getJsonKey()));
		}

		if (!valueOptional.isPresent()) {
			return;
		}

		int value = valueOptional.get();

		Optional<Integer> minValue = GSearchUtil.stringToIntegerOptional(
			configurationJsonObject.getString(
				RequestParameterConfigurationKeys.MIN_VALUE.getJsonKey()));

		if (minValue.isPresent() &&
			(Integer.compare(value, minValue.get()) < 0)) {

			if (_log.isWarnEnabled()) {
				_log.warn(minValue.get() + " is below the minimum.");
			}

			value = minValue.get();
		}

		Optional<Integer> maxValue = GSearchUtil.stringToIntegerOptional(
			configurationJsonObject.getString(
				RequestParameterConfigurationKeys.MAX_VALUE.getJsonKey()));

		if (maxValue.isPresent() &&
			(Integer.compare(value, maxValue.get()) > 0)) {

			if (_log.isWarnEnabled()) {
				_log.warn(maxValue.get() + " is above the maximum.");
			}

			value = maxValue.get();
		}

		String parameterRole = configurationJsonObject.getString(
			RequestParameterConfigurationKeys.ROLE.getJsonKey());

		searchParameterData.addParameter(
			new IntegerParameter(
				parameterName, parameterRole,
				"${request." + parameterName + "}", value));
	}

	private static final Log _log = LogFactoryUtil.getLog(
		IntegerParameterContributor.class);

}