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
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.RequestParameterConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.message.Message;
import com.liferay.portal.search.tuning.gsearch.message.Severity;
import com.liferay.portal.search.tuning.gsearch.parameter.DateParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.SearchParameterData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, property = "type=date",
	service = RequestParameterContributor.class
)
public class DateParameterContributor implements RequestParameterContributor {

	@Override
	public void contribute(
		HttpServletRequest httpServletRequest,
		SearchParameterData searchParameterData,
		JSONObject configurationJsonObject) {

		if (!_validateConfiguration(
				searchParameterData, configurationJsonObject)) {

			return;
		}

		String parameterName = configurationJsonObject.getString(
			RequestParameterConfigurationKeys.PARAMETER_NAME.getJsonKey());

		String dateString = ParamUtil.getString(
			httpServletRequest, parameterName);

		if (Validator.isBlank(dateString)) {
			return;
		}

		Date date = _getDate(
			httpServletRequest, configurationJsonObject, dateString);

		if (date != null) {
			String parameterRole = configurationJsonObject.getString(
				RequestParameterConfigurationKeys.ROLE.getJsonKey());

			searchParameterData.addParameter(
				new DateParameter(
					parameterName, parameterRole,
					"${request." + parameterName + "}", date));
		}
	}

	private Date _getDate(
		HttpServletRequest httpServletRequest,
		JSONObject configurationJsonObject, String dateString) {

		String dateFormat = configurationJsonObject.getString(
			RequestParameterConfigurationKeys.DATE_FORMAT.getJsonKey());

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		TimeZone timeZone = themeDisplay.getTimeZone();

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
			dateFormat);

		if (Validator.isBlank(dateString)) {
			return null;
		}

		try {
			LocalDate localDate = LocalDate.parse(
				dateString, dateTimeFormatter);

			Date value = GregorianCalendar.from(
				localDate.atStartOfDay(timeZone.toZoneId())
			).getTime();

			return value;
		}
		catch (Exception e) {
			_log.error(
				String.format(
					"Cannot parse date from string '%s'", dateString));
		}

		return null;
	}

	private boolean _validateConfiguration(
		SearchParameterData searchParameterData,
		JSONObject configurationJsonObject) {

		boolean valid = true;

		if (Validator.isNull(
				configurationJsonObject.getString(
					RequestParameterConfigurationKeys.DATE_FORMAT.
						getJsonKey()))) {

			searchParameterData.addMessage(
				new Message(
					Severity.ERROR, "core", "core.error.undefined-date-format",
					null, null, configurationJsonObject,
					RequestParameterConfigurationKeys.DATE_FORMAT.getJsonKey(),
					null));
			valid = false;
		}

		return valid;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DateParameterContributor.class);

}