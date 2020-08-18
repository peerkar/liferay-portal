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

package com.liferay.portal.search.tuning.gsearch.impl.internal.parameter.contributor;

import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.tuning.gsearch.parameter.DateParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.IntegerParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.ParameterDefinition;
import com.liferay.portal.search.tuning.gsearch.parameter.SearchParameterData;
import com.liferay.portal.search.tuning.gsearch.parameter.StringParameter;
import com.liferay.portal.search.tuning.gsearch.spi.parameter.ParameterContributor;

import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, property = "name=time",
	service = ParameterContributor.class
)
public class TimeParameterContributor implements ParameterContributor {

	@Override
	public void contribute(
		HttpServletRequest httpServletRequest,
		SearchParameterData searchParameterData) {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		_provide(searchParameterData, themeDisplay.getTimeZone());
	}

	@Override
	public void contribute(
		SearchContext searchContext, SearchParameterData searchParameterData) {

		_provide(searchParameterData, searchContext.getTimeZone());
	}

	public List<ParameterDefinition> getParameterDefinitions() {
		List<ParameterDefinition> parameterDefinitions = new ArrayList<>();

		parameterDefinitions.add(
			new ParameterDefinition(
				"${time.current_date}", DateParameter.class.getName(),
				"parameter.time.current-date"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${time.current_day_of_month}",
				IntegerParameter.class.getName(),
				"parameter.time.current-day-of-month"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${time.current_day_of_week}", IntegerParameter.class.getName(),
				"parameter.time.current-day-of-week"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${time.current_day_of_year}", IntegerParameter.class.getName(),
				"parameter.time.current-day-of-year"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${time.current_hour}", IntegerParameter.class.getName(),
				"parameter.time.current-hour"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${time.current_year}", IntegerParameter.class.getName(),
				"parameter.time.current-year"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${time.time_of_day}", StringParameter.class.getName(),
				"parameter.time.time-of-day"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${time.timezone_locale_name}", StringParameter.class.getName(),
				"parameter.time.timezone-locale-name"));

		return parameterDefinitions;
	}

	private String _getTimeOfTheDay(LocalTime localTime) {
		if (_isTimebetween(localTime, _MORNING, _AFTER_NOON)) {
			return "morning";
		}
		else if (_isTimebetween(localTime, _AFTER_NOON, _EVENING)) {
			return "afternoon";
		}
		else if (_isTimebetween(localTime, _EVENING, _NIGHT)) {
			return "evening";
		}
		else {
			return "night";
		}
	}

	private boolean _isTimebetween(
		LocalTime time, LocalTime start, LocalTime end) {

		if (!time.isBefore(start) && time.isBefore(end)) {
			return true;
		}

		return false;
	}

	private void _provide(
		SearchParameterData searchParameterData, TimeZone timeZone) {

		LocalDateTime localDateTime = LocalDateTime.now(timeZone.toZoneId());

		Date now = Date.from(
			localDateTime.atZone(
				timeZone.toZoneId()
			).toInstant());

		searchParameterData.addParameter(
			new DateParameter(
				"time.current_date", null, "${time.current_date}", now));
		searchParameterData.addParameter(
			new IntegerParameter(
				"time.current_day_of_month", null,
				"${time.current_day_of_month}", localDateTime.getDayOfMonth()));
		searchParameterData.addParameter(
			new IntegerParameter(
				"time.current_day_of_week", null, "${time.current_day_of_week}",
				localDateTime.getDayOfWeek(
				).getValue()));
		searchParameterData.addParameter(
			new IntegerParameter(
				"time.current_day_of_year", null, "${time.current_day_of_year}",
				localDateTime.getDayOfYear()));
		searchParameterData.addParameter(
			new IntegerParameter(
				"time.current_hour", null, "${time.current_hour}",
				localDateTime.getHour()));
		searchParameterData.addParameter(
			new IntegerParameter(
				"time.current_year", null, "${time.current_year}",
				localDateTime.getYear()));
		searchParameterData.addParameter(
			new StringParameter(
				"time.time_of_day", null, "${time.time_of_day}",
				_getTimeOfTheDay(localDateTime.toLocalTime())));
		searchParameterData.addParameter(
			new StringParameter(
				"time.timezone", null, "${time.timezone}",
				timeZone.getDisplayName()));
	}

	private static final LocalTime _AFTER_NOON = LocalTime.of(12, 0, 0);

	private static final LocalTime _EVENING = LocalTime.of(16, 0, 0);

	private static final LocalTime _MORNING = LocalTime.of(0, 0, 0);

	private static final LocalTime _NIGHT = LocalTime.of(21, 0, 0);

	@Reference
	private Language _language;

	@Reference
	private UserLocalService _userLocalService;

}