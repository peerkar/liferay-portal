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

package com.liferay.portal.search.tuning.gsearch.ipstack.internal.dataprovider;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.geolocation.GeoLocationPoint;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.tuning.gsearch.cache.JsonDataProviderCache;
import com.liferay.portal.search.tuning.gsearch.ipstack.internal.configuration.IPStackConfiguration;
import com.liferay.portal.search.tuning.gsearch.message.Message;
import com.liferay.portal.search.tuning.gsearch.message.Severity;
import com.liferay.portal.search.tuning.gsearch.parameter.SearchParameterData;
import com.liferay.portal.search.tuning.gsearch.spi.dataprovider.GeoLocationDataProvider;

import java.io.IOException;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	configurationPid = "com.liferay.portal.search.tuning.gsearch.ipstack.internal.configuration.IpStackDataProviderConfiguration",
	immediate = true, property = "name=ipstack",
	service = GeoLocationDataProvider.class
)
public class IPStackDataProvider implements GeoLocationDataProvider {

	public JSONObject getGeoLocationData(
		SearchParameterData searchParameterData, String ipAddress) {

		if (_ipStackConfiguration.isEnabled()) {
			return null;
		}

		String testIPAddress = StringUtil.trim(
			_ipStackConfiguration.testIpAddress());

		if (!Validator.isBlank(testIPAddress)) {
			ipAddress = testIPAddress;

			if (_log.isDebugEnabled()) {
				_log.debug("Using test IP address " + testIPAddress);
			}
		}

		if (!_validateConfiguration(searchParameterData) ||
			!_validateIPAddresss(searchParameterData, ipAddress)) {

			return null;
		}

		JSONObject ipStackDataJsonObject = _jsonDataProviderCache.get(
			ipAddress);

		if (ipStackDataJsonObject != null) {
			return ipStackDataJsonObject;
		}

		ipStackDataJsonObject = _getLocationData(
			searchParameterData, ipAddress);

		if (!_validateData(searchParameterData, ipStackDataJsonObject)) {
			return null;
		}

		_jsonDataProviderCache.put(
			ipAddress, ipStackDataJsonObject,
			_ipStackConfiguration.cacheTimeout());

		if (_log.isDebugEnabled()) {
			_log.debug("IPStack data: " + ipStackDataJsonObject);
		}

		return ipStackDataJsonObject;
	}

	@Override
	public GeoLocationPoint getGeoLocationPoint(
		SearchParameterData searchParameterData, String ipAddress) {

		JSONObject ipStackDataJsonObject = getGeoLocationData(
			searchParameterData, ipAddress);

		if (ipStackDataJsonObject != null) {
			double latitude = ipStackDataJsonObject.getDouble("latitude");
			double longitude = ipStackDataJsonObject.getDouble("longitude");

			return new GeoLocationPoint(latitude, longitude);
		}

		return null;
	}

	public boolean isEnabled() {
		return _ipStackConfiguration.isEnabled();
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_ipStackConfiguration = ConfigurableUtil.createConfigurable(
			IPStackConfiguration.class, properties);
	}

	private String _buildURL(String apiKey, String apiURL, String ipAddress) {
		StringBundler sb = new StringBundler(5);

		sb.append(apiURL);
		sb.append("/");
		sb.append(ipAddress);
		sb.append("?access_key=");
		sb.append(apiKey);

		return sb.toString();
	}

	private JSONObject _getLocationData(
		SearchParameterData searchParameterData, String ipAddress) {

		String apiKey = _ipStackConfiguration.apiKey();
		String apiURL = _ipStackConfiguration.apiURL();

		String url = _buildURL(apiKey, apiURL, ipAddress);

		JSONObject ipStackDataJsonObject = null;

		try {
			String rawData = _http.URLtoString(url);

			if (rawData == null) {
				searchParameterData.addMessage(
					new Message(
						Severity.ERROR, "ipstack",
						"ipstack.error.empty-response"));

				return null;
			}

			ipStackDataJsonObject = JSONFactoryUtil.createJSONObject(rawData);
		}
		catch (IOException ioException) {
			searchParameterData.addMessage(
				new Message(
					Severity.ERROR, "ipstack", "ipstack.error.network-error",
					ioException.getMessage(), ioException, null, null, null));
			_log.error(ioException.getMessage(), ioException);
		}
		catch (JSONException jsonException) {
			searchParameterData.addMessage(
				new Message(
					Severity.ERROR, "ipstack",
					"openweathermap.error.invalid-ipstack-response-format",
					jsonException.getMessage(), jsonException, null, null,
					null));
			_log.error(jsonException.getMessage(), jsonException);
		}

		return ipStackDataJsonObject;
	}

	private boolean _validateConfiguration(
		SearchParameterData searchParameterData) {

		String apiKey = _ipStackConfiguration.apiKey();
		String apiURL = _ipStackConfiguration.apiURL();

		boolean valid = true;

		if (Validator.isBlank(apiKey)) {
			searchParameterData.addMessage(
				new Message(
					Severity.ERROR, "ipstack", "ipstack.error.api-key-empty"));
			valid = false;
		}
		else if (Validator.isBlank(apiURL)) {
			searchParameterData.addMessage(
				new Message(
					Severity.ERROR, "ipstack", "ipstack.error.api-url-empty"));
			valid = false;
		}

		return valid;
	}

	private boolean _validateData(
		SearchParameterData searchParameterData, JSONObject jsonObject) {

		if ((jsonObject == null) || (jsonObject.get("latitude") != null) ||
			(jsonObject.get("longitude") != null)) {

			searchParameterData.addMessage(
				new Message(
					Severity.ERROR, "ipstack",
					"ipstack.error.invalid-response-data", jsonObject, null,
					null));

			return false;
		}

		return true;
	}

	private boolean _validateIPAddresss(
		SearchParameterData searchParameterData, String ipAddress) {

		ipAddress = StringUtil.trim(ipAddress);

		if (Validator.isBlank(ipAddress)) {
			searchParameterData.addMessage(
				new Message(
					Severity.ERROR, "ipstack",
					"ipstack.error.empty-ip-address"));

			return false;
		}

		Inet4Address address;

		try {
			address = (Inet4Address)InetAddress.getByName(ipAddress);
		}
		catch (UnknownHostException unknownHostException) {
			searchParameterData.addMessage(
				new Message(
					Severity.ERROR, "ipstack", "ipstack.error.invalid-ip",
					unknownHostException.getMessage(), unknownHostException,
					null, null, null));

			_log.error(unknownHostException.getMessage(), unknownHostException);

			return false;
		}
		catch (SecurityException securityException) {
			searchParameterData.addMessage(
				new Message(
					Severity.ERROR, "ipstack",
					"ipstack.error.security-exception",
					securityException.getMessage(), securityException, null,
					null, null));
			_log.error(securityException.getMessage(), securityException);

			return false;
		}

		if (address.isSiteLocalAddress() || address.isAnyLocalAddress() ||
			address.isLinkLocalAddress() || address.isLoopbackAddress() ||
			address.isMulticastAddress()) {

			searchParameterData.addMessage(
				new Message(
					Severity.ERROR, "ipstack",
					"ipstack.error.unable-to-provide-geolocation-data-for-private-ip-address",
					null, null, ipAddress));

			return false;
		}

		return true;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		IPStackDataProvider.class);

	@Reference
	private Http _http;

	private volatile IPStackConfiguration _ipStackConfiguration;

	@Reference
	private JsonDataProviderCache _jsonDataProviderCache;

}