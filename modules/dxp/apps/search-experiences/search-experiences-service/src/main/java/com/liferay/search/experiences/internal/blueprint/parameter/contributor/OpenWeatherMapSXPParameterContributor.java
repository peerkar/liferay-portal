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

package com.liferay.search.experiences.internal.blueprint.parameter.contributor;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.search.geolocation.GeoLocationPoint;
import com.liferay.portal.search.searcher.SearchRequestBuilder;
import com.liferay.search.experiences.blueprint.constants.SearchContextAttributeKeys;
import com.liferay.search.experiences.blueprint.parameter.DoubleSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.IntegerSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.SXPParameterContributionDefinition;
import com.liferay.search.experiences.blueprint.parameter.SXPParameterContributor;
import com.liferay.search.experiences.blueprint.parameter.SXPParameterDataBuilder;
import com.liferay.search.experiences.blueprint.parameter.StringSXPParameter;
import com.liferay.search.experiences.configuration.OpenWeatherMapConfiguration;
import com.liferay.search.experiences.internal.blueprint.data.provider.GeoLocationDataProvider;
import com.liferay.search.experiences.internal.blueprint.data.provider.OpenWeatherMapDataProvider;
import com.liferay.search.experiences.internal.blueprint.util.SearchContextUtil;
import com.liferay.search.experiences.model.SXPBlueprint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	configurationPid = "com.liferay.search.experiences.configuration.OpenWeatherMapConfiguration",
	immediate = true, property = "name=openweathermap",
	service = SXPParameterContributor.class
)
public class OpenWeatherMapSXPParameterContributor
	implements SXPParameterContributor {

	@Override
	public void contribute(
		SearchRequestBuilder searchRequestBuilder, SXPBlueprint sxpBlueprint,
		SXPParameterDataBuilder sxpParameterDataBuilder) {

		if (!_openWeatherMapConfiguration.isEnabled()) {
			return;
		}

		_contribute(
			SearchContextUtil.getStringAttribute(
				SearchContextAttributeKeys.IP_ADDRESS, searchRequestBuilder),
			sxpParameterDataBuilder);
	}

	@Override
	public String getCategoryNameKey() {
		return "weather";
	}

	@Override
	public List<SXPParameterContributionDefinition>
		getSXPParameterContributionDefinitions() {

		List<SXPParameterContributionDefinition> parameterDefinitions =
			new ArrayList<>();

		if (!_openWeatherMapConfiguration.isEnabled()) {
			return parameterDefinitions;
		}

		parameterDefinitions.add(
			new SXPParameterContributionDefinition(
				IntegerSXPParameter.class.getName(), "weather-id",
				"openweathermap.weather_id"));

		parameterDefinitions.add(
			new SXPParameterContributionDefinition(
				StringSXPParameter.class.getName(), "weather-name}",
				"openweathermap.weather_name"));

		parameterDefinitions.add(
			new SXPParameterContributionDefinition(
				DoubleSXPParameter.class.getName(), "temperature",
				"openweathermap.temperature"));

		return parameterDefinitions;
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_openWeatherMapConfiguration = ConfigurableUtil.createConfigurable(
			OpenWeatherMapConfiguration.class, properties);
	}

	private void _contribute(
		String ipAddress, SXPParameterDataBuilder sxpParameterDataBuilder) {

		Optional<GeoLocationPoint> geoLocationPointOptional =
			_geoLocationDataProvider.getGeoLocationPoint(ipAddress);

		if (!geoLocationPointOptional.isPresent()) {
			return;
		}

		GeoLocationPoint geoLocationPoint = geoLocationPointOptional.get();

		Optional<JSONObject> weatherDataJSONObjectOptional =
			_openWeatherMapDataProvider.getWeatherData(geoLocationPoint);

		if (!weatherDataJSONObjectOptional.isPresent()) {
			return;
		}

		JSONObject weatherDataJSONObject = weatherDataJSONObjectOptional.get();

		JSONArray weatherJSONArray = weatherDataJSONObject.getJSONArray(
			"weather");

		JSONObject weatherJSONObject = weatherJSONArray.getJSONObject(0);

		if (weatherJSONObject == null) {
			return;
		}

		sxpParameterDataBuilder.addSXPParameter(
			new IntegerSXPParameter(
				"openweathermap.weather_id", true,
				weatherJSONObject.getInt("id")));

		sxpParameterDataBuilder.addSXPParameter(
			new StringSXPParameter(
				"openweathermap.weather_name", true,
				weatherJSONObject.getString("main")));

		sxpParameterDataBuilder.addSXPParameter(
			new DoubleSXPParameter(
				"openweathermap.temperature", true,
				weatherJSONObject.getDouble("temp")));
	}

	@Reference
	private GeoLocationDataProvider _geoLocationDataProvider;

	private volatile OpenWeatherMapConfiguration _openWeatherMapConfiguration;

	@Reference
	private OpenWeatherMapDataProvider _openWeatherMapDataProvider;

}