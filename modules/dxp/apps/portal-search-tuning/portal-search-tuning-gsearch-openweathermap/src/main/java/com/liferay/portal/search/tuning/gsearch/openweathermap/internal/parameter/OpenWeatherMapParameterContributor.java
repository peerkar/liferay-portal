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

package com.liferay.portal.search.tuning.gsearch.openweathermap.internal.parameter;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.geolocation.GeoLocationPoint;
import com.liferay.portal.search.tuning.gsearch.constants.SearchContextAttributeKeys;
import com.liferay.portal.search.tuning.gsearch.openweathermap.internal.dataprovider.OpenWeatherMapDataProvider;
import com.liferay.portal.search.tuning.gsearch.parameter.DoubleParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.IntegerParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.ParameterDefinition;
import com.liferay.portal.search.tuning.gsearch.parameter.SearchParameterData;
import com.liferay.portal.search.tuning.gsearch.parameter.StringParameter;
import com.liferay.portal.search.tuning.gsearch.spi.dataprovider.GeoLocationDataProvider;
import com.liferay.portal.search.tuning.gsearch.spi.parameter.ParameterContributor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, property = "name=openweathermap",
	service = ParameterContributor.class
)
public class OpenWeatherMapParameterContributor
	implements ParameterContributor {

	@Override
	public void contribute(
		HttpServletRequest httpServletRequest,
		SearchParameterData searchParameterData) {

		String ipAddress = httpServletRequest.getRemoteAddr();

		_provide(searchParameterData, ipAddress);
	}

	@Override
	public void contribute(
		SearchContext searchContext, SearchParameterData searchParameterData) {

		String ipAddress = (String)searchContext.getAttribute(
			SearchContextAttributeKeys.IP_ADDRESS);

		_provide(searchParameterData, ipAddress);
	}

	@Override
	public List<ParameterDefinition> getParameterDefinitions() {
		List<ParameterDefinition> parameterDefinitions = new ArrayList<>();

		parameterDefinitions.add(
			new ParameterDefinition(
				"${openweathermap.weather_id}",
				IntegerParameter.class.getName(),
				"parameter.openweathermap.weather-id"));

		parameterDefinitions.add(
			new ParameterDefinition(
				"${openweathermap.weather_name}",
				StringParameter.class.getName(),
				"parameter.openweathermap.weather-name"));

		parameterDefinitions.add(
			new ParameterDefinition(
				"${openweathermap.temperature}",
				DoubleParameter.class.getName(),
				"parameter.openweathermap.temperature"));

		return parameterDefinitions;
	}

	private void _provide(
		SearchParameterData searchParameterData, String ipAddress) {

		GeoLocationPoint geoLocationPoint =
			_geoLocationDataProvider.getGeoLocationPoint(
				searchParameterData, ipAddress);

		if (geoLocationPoint == null) {
			return;
		}

		JSONObject weatherDataJsonObject =
			_openWeatherMapDataProvider.getWeatherData(
				searchParameterData, geoLocationPoint);

		if (weatherDataJsonObject == null) {
			return;
		}

		JSONObject weatherJsonObject = weatherDataJsonObject.getJSONArray(
			"weather"
		).getJSONObject(
			0
		);

		if (weatherJsonObject == null) {
			return;
		}

		searchParameterData.addParameter(
			new IntegerParameter(
				"openweathermap.weather_id", null,
				"${openweathermap.weather_id}",
				weatherJsonObject.getInt("id")));

		searchParameterData.addParameter(
			new StringParameter(
				"openweathermap.weather_name", null,
				"${openweathermap.weather_name}",
				weatherJsonObject.getString("main")));

		searchParameterData.addParameter(
			new DoubleParameter(
				"openweathermap.temperature", null,
				"${openweathermap.temperature}",
				weatherJsonObject.getDouble("temp")));
	}

	@Reference
	private GeoLocationDataProvider _geoLocationDataProvider;

	@Reference
	private OpenWeatherMapDataProvider _openWeatherMapDataProvider;

}