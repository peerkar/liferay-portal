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
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.search.searcher.SearchRequestBuilder;
import com.liferay.search.experiences.blueprint.constants.SearchContextAttributeKeys;
import com.liferay.search.experiences.blueprint.parameter.DoubleSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.SXPParameterContributionDefinition;
import com.liferay.search.experiences.blueprint.parameter.SXPParameterContributor;
import com.liferay.search.experiences.blueprint.parameter.SXPParameterDataBuilder;
import com.liferay.search.experiences.blueprint.parameter.StringSXPParameter;
import com.liferay.search.experiences.configuration.IPStackConfiguration;
import com.liferay.search.experiences.internal.blueprint.data.provider.GeoLocationDataProvider;
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
	configurationPid = "com.liferay.search.experiences.configuration.IPStackConfiguration",
	immediate = true, property = "name=ipstack",
	service = SXPParameterContributor.class
)
public class IPStackParameterContributor implements SXPParameterContributor {

	@Override
	public void contribute(
		SearchRequestBuilder searchRequestBuilder, SXPBlueprint sxpBlueprint,
		SXPParameterDataBuilder sxpParameterDataBuilder) {

		_contribute(
			SearchContextUtil.getStringAttribute(
				SearchContextAttributeKeys.IP_ADDRESS, searchRequestBuilder),
			sxpParameterDataBuilder);
	}

	@Override
	public String getCategoryNameKey() {
		return "ip";
	}

	@Override
	public List<SXPParameterContributionDefinition>
		getsxpParameterContributionDefinitions() {

		List<SXPParameterContributionDefinition>
			sxpParameterContributionDefinitions = new ArrayList<>();

		if (!_ipStackConfiguration.isEnabled()) {
			return sxpParameterContributionDefinitions;
		}

		sxpParameterContributionDefinitions.add(
			new SXPParameterContributionDefinition(
				StringSXPParameter.class.getName(), "city", "ipstack.city"));
		sxpParameterContributionDefinitions.add(
			new SXPParameterContributionDefinition(
				StringSXPParameter.class.getName(), "continent-code",
				"ipstack.continent_code"));
		sxpParameterContributionDefinitions.add(
			new SXPParameterContributionDefinition(
				StringSXPParameter.class.getName(), "continent-name",
				"ipstack.continent_name"));
		sxpParameterContributionDefinitions.add(
			new SXPParameterContributionDefinition(
				StringSXPParameter.class.getName(), "country-code",
				"ipstack.country_code"));
		sxpParameterContributionDefinitions.add(
			new SXPParameterContributionDefinition(
				StringSXPParameter.class.getName(), "country-name",
				"ipstack.country_name"));
		sxpParameterContributionDefinitions.add(
			new SXPParameterContributionDefinition(
				DoubleSXPParameter.class.getName(), "latitude",
				"ipstack.latitude"));
		sxpParameterContributionDefinitions.add(
			new SXPParameterContributionDefinition(
				DoubleSXPParameter.class.getName(), "longitude",
				"ipstack.longitude"));
		sxpParameterContributionDefinitions.add(
			new SXPParameterContributionDefinition(
				StringSXPParameter.class.getName(), "region-code",
				"ipstack.region_code"));
		sxpParameterContributionDefinitions.add(
			new SXPParameterContributionDefinition(
				StringSXPParameter.class.getName(), "region-name",
				"ipstack.region_name"));
		sxpParameterContributionDefinitions.add(
			new SXPParameterContributionDefinition(
				StringSXPParameter.class.getName(), "zip-code", "ipstack.zip"));

		return sxpParameterContributionDefinitions;
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_ipStackConfiguration = ConfigurableUtil.createConfigurable(
			IPStackConfiguration.class, properties);
	}

	private void _contribute(
		String ipAddress, SXPParameterDataBuilder sxpParameterDataBuilder) {

		Optional<JSONObject> geoLocationJSONObjectOptional =
			_geoLocationDataProvider.getGeoLocationData(ipAddress);

		if (!geoLocationJSONObjectOptional.isPresent()) {
			return;
		}

		JSONObject geoLocationJSONObject = geoLocationJSONObjectOptional.get();

		sxpParameterDataBuilder.addSXPParameter(
			new StringSXPParameter(
				"ipstack.city", true, geoLocationJSONObject.getString("city")));

		sxpParameterDataBuilder.addSXPParameter(
			new StringSXPParameter(
				"ipstack.continent_code", true,
				geoLocationJSONObject.getString("continent_code")));

		sxpParameterDataBuilder.addSXPParameter(
			new StringSXPParameter(
				"ipstack.continent_name", true,
				geoLocationJSONObject.getString("continent_name")));

		sxpParameterDataBuilder.addSXPParameter(
			new StringSXPParameter(
				"ipstack.country_code", true,
				geoLocationJSONObject.getString("country_code")));

		sxpParameterDataBuilder.addSXPParameter(
			new StringSXPParameter(
				"ipstack.country_name", true,
				geoLocationJSONObject.getString("country_name")));

		sxpParameterDataBuilder.addSXPParameter(
			new DoubleSXPParameter(
				"ipstack.latitude", true,
				geoLocationJSONObject.getDouble("latitude")));

		sxpParameterDataBuilder.addSXPParameter(
			new DoubleSXPParameter(
				"ipstack.longitude", true,
				geoLocationJSONObject.getDouble("longitude")));

		sxpParameterDataBuilder.addSXPParameter(
			new StringSXPParameter(
				"ipstack.region_code", true,
				geoLocationJSONObject.getString("region_code")));

		sxpParameterDataBuilder.addSXPParameter(
			new StringSXPParameter(
				"ipstack.region_name", true,
				geoLocationJSONObject.getString("region_name")));

		sxpParameterDataBuilder.addSXPParameter(
			new StringSXPParameter(
				"ipstack.zip", true, geoLocationJSONObject.getString("zip")));
	}

	@Reference
	private GeoLocationDataProvider _geoLocationDataProvider;

	private volatile IPStackConfiguration _ipStackConfiguration;

}