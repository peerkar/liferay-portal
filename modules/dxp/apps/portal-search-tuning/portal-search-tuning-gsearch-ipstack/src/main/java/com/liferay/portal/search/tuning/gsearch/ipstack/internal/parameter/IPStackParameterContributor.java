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

package com.liferay.portal.search.tuning.gsearch.ipstack.internal.parameter;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.search.tuning.gsearch.constants.SearchContextAttributeKeys;
import com.liferay.portal.search.tuning.gsearch.parameter.DoubleParameter;
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
	immediate = true, property = "name=ipstack",
	service = ParameterContributor.class
)
public class IPStackParameterContributor implements ParameterContributor {

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
				"${ipstack.city}", StringParameter.class.getName(),
				"parameter.ipstack.city"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${ipstack.continent_code}", StringParameter.class.getName(),
				"parameter.ipstack.continent-code"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${ipstack.continent_name}", StringParameter.class.getName(),
				"parameter.ipstack.continent-name"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${ipstack.country_code}", StringParameter.class.getName(),
				"parameter.ipstack.country-code"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${ipstack.country_name}", StringParameter.class.getName(),
				"parameter.ipstack.country-name"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${ipstack.latitude}", DoubleParameter.class.getName(),
				"parameter.ipstack.latitude"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${ipstack.longitude}", DoubleParameter.class.getName(),
				"parameter.ipstack.longitude"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${ipstack.region_code}", StringParameter.class.getName(),
				"parameter.ipstack.region-code"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${ipstack.region_name}", StringParameter.class.getName(),
				"parameter.ipstack.region-name"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${ipstack.zip}", StringParameter.class.getName(),
				"parameter.ipstack.zip"));

		return parameterDefinitions;
	}

	private void _provide(
		SearchParameterData searchParameterData, String ipAddress) {

		JSONObject geoLocationJSONObject =
			_geoLocationDataProvider.getGeoLocationData(
				searchParameterData, ipAddress);

		if (geoLocationJSONObject == null) {
			return;
		}

		searchParameterData.addParameter(
			new StringParameter(
				"ipstack.city", null, "${ipstack.city}",
				geoLocationJSONObject.getString("city")));

		searchParameterData.addParameter(
			new StringParameter(
				"ipstack.continent_code", null, "${ipstack.continent_code}",
				geoLocationJSONObject.getString("continent_code")));

		searchParameterData.addParameter(
			new StringParameter(
				"ipstack.continent_name", null, "${ipstack.continent_name}",
				geoLocationJSONObject.getString("continent_name")));

		searchParameterData.addParameter(
			new StringParameter(
				"ipstack.country_code", null, "${ipstack.country_code}",
				geoLocationJSONObject.getString("country_code")));

		searchParameterData.addParameter(
			new StringParameter(
				"ipstack.country_name", null, "${ipstack.country_name}",
				geoLocationJSONObject.getString("country_name")));

		searchParameterData.addParameter(
			new DoubleParameter(
				"ipstack.latitude", null, "${ipstack.latitude}",
				geoLocationJSONObject.getDouble("latitude")));

		searchParameterData.addParameter(
			new DoubleParameter(
				"ipstack.longitude", null, "${ipstack.latitude}",
				geoLocationJSONObject.getDouble("longitude")));

		searchParameterData.addParameter(
			new StringParameter(
				"ipstack.region_code", null, "${ipstack.region_code}",
				geoLocationJSONObject.getString("region_code")));

		searchParameterData.addParameter(
			new StringParameter(
				"ipstack.region_name", null, "${ipstack.region_name}",
				geoLocationJSONObject.getString("region_name")));

		searchParameterData.addParameter(
			new StringParameter(
				"ipstack.zip", null, "${ipstack.zip}",
				geoLocationJSONObject.getString("zip")));
	}

	@Reference
	private GeoLocationDataProvider _geoLocationDataProvider;

}