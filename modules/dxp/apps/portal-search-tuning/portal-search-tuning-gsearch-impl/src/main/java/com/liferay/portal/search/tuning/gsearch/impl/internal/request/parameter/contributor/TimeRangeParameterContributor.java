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
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.RequestParameterConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.impl.util.GSearchUtil;
import com.liferay.portal.search.tuning.gsearch.parameter.DateParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.SearchParameterData;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, property = "type=time_range",
	service = RequestParameterContributor.class
)
public class TimeRangeParameterContributor
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

		Optional<String> valueOptional = GSearchUtil.toStringOptional(
			valueString);

		if (!valueOptional.isPresent()) {
			return;
		}

		String value = valueOptional.get();

		Date timeFrom = null;

		Calendar calendar = Calendar.getInstance();

		if ("last-day".equals(value)) {
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			timeFrom = calendar.getTime();
		}
		else if ("last-hour".equals(value)) {
			calendar.add(Calendar.HOUR_OF_DAY, -1);
			timeFrom = calendar.getTime();
		}
		else if ("last-month".equals(value)) {
			calendar.add(Calendar.MONTH, -1);
			timeFrom = calendar.getTime();
		}
		else if ("last-week".equals(value)) {
			calendar.add(Calendar.WEEK_OF_MONTH, -1);
			timeFrom = calendar.getTime();
		}
		else if ("last-year".equals(value)) {
			calendar.add(Calendar.YEAR, -1);
			timeFrom = calendar.getTime();
		}

		String parameterRole = configurationJsonObject.getString(
			RequestParameterConfigurationKeys.ROLE.getJsonKey());

		searchParameterData.addParameter(
			new DateParameter(
				parameterName, parameterRole,
				"${request." + parameterName + "}", timeFrom));
	}

}