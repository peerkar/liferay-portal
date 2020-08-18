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
import com.liferay.portal.search.tuning.gsearch.impl.util.GSearchJsonUtil;
import com.liferay.portal.search.tuning.gsearch.parameter.IntegerArrayParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.SearchParameterData;

import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, property = "type=integer_array",
	service = RequestParameterContributor.class
)
public class IntegerArrayParameterContributor
	implements RequestParameterContributor {

	@Override
	public void contribute(
		HttpServletRequest httpServletRequest,
		SearchParameterData searchParameterData,
		JSONObject configurationJsonObject) {

		String parameterName = configurationJsonObject.getString(
			RequestParameterConfigurationKeys.PARAMETER_NAME.getJsonKey());

		String[] stringValueArray = ParamUtil.getStringValues(
			httpServletRequest, parameterName);

		if ((stringValueArray == null) || (stringValueArray.length == 0)) {
			if (stringValueArray == null) {
				Optional<String[]> valueOptional =
					GSearchJsonUtil.getStringArrayOptional(
						configurationJsonObject,
						RequestParameterConfigurationKeys.DEFAULT.getJsonKey());

				if (valueOptional.isPresent()) {
					stringValueArray = valueOptional.get();
				}
			}
		}

		try {
			Integer[] integerArray = Arrays.stream(
				stringValueArray
			).map(
				Long::valueOf
			).toArray(
				Integer[]::new
			);

			String parameterRole = configurationJsonObject.getString(
				RequestParameterConfigurationKeys.ROLE.getJsonKey());

			searchParameterData.addParameter(
				new IntegerArrayParameter(
					parameterName, parameterRole,
					"${request." + parameterName + "}", integerArray));
		}
		catch (NumberFormatException numberFormatException) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					numberFormatException.getMessage(), numberFormatException);
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		IntegerArrayParameterContributor.class);

}