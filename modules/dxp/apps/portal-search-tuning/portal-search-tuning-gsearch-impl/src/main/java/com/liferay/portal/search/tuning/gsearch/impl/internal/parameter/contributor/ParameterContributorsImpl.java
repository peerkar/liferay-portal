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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.tuning.gsearch.parameter.Parameter;
import com.liferay.portal.search.tuning.gsearch.parameter.ParameterDefinition;
import com.liferay.portal.search.tuning.gsearch.parameter.SearchParameterData;
import com.liferay.portal.search.tuning.gsearch.spi.parameter.ParameterContributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * @author Petteri Karttunen
 */
@Component(immediate = true, service = ParameterContributors.class)
public class ParameterContributorsImpl implements ParameterContributors {

	public void contribute(
		HttpServletRequest httpServletRequest,
		SearchParameterData searchParameterData) {

		for (Map.Entry<String, ParameterContributor> entry :
				_parameterContributors.entrySet()) {

			entry.getValue(
			).contribute(
				httpServletRequest, searchParameterData
			);
		}

		_logParameters(searchParameterData);
	}

	public void contribute(
			HttpServletRequest httpServletRequest,
			SearchParameterData searchParameterData,
			String parameterContributorName)
		throws IllegalArgumentException {

		ParameterContributor parameterContributor = _getProviderByName(
			parameterContributorName);

		parameterContributor.contribute(
			httpServletRequest, searchParameterData);
	}

	public void contribute(
		SearchContext searchContext, SearchParameterData searchParameterData) {

		for (Map.Entry<String, ParameterContributor> entry :
				_parameterContributors.entrySet()) {

			entry.getValue(
			).contribute(
				searchContext, searchParameterData
			);
		}

		_logParameters(searchParameterData);
	}

	public void contribute(
			SearchContext searchContext,
			SearchParameterData searchParameterData,
			String parameterContributorName)
		throws IllegalArgumentException {

		ParameterContributor parameterContributor = _getProviderByName(
			parameterContributorName);

		parameterContributor.contribute(searchContext, searchParameterData);
	}

	public ParameterDefinition[] getParameterDefinitions() {
		List<ParameterDefinition> parameterDefinitionList = new ArrayList<>();

		for (Map.Entry<String, ParameterContributor> entry :
				_parameterContributors.entrySet()) {

			parameterDefinitionList.addAll(
				entry.getValue(
				).getParameterDefinitions());
		}

		return parameterDefinitionList.toArray(new ParameterDefinition[0]);
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC
	)
	protected void registerContextParameterProvider(
		ParameterContributor parameterContributor,
		Map<String, Object> properties) {

		String name = (String)properties.get("name");
		if (Validator.isBlank(name)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to add facet handler " +
						parameterContributor.getClass(
						).getName() + ". Name property empty.");
			}

			return;
		}
		
		_parameterContributors.put(name, parameterContributor);
	}

	protected void unregisterContextParameterProvider(
		ParameterContributor parameterContributor,
		Map<String, Object> properties) {

		String name = (String)properties.get("name");

		_parameterContributors.remove(name);
	}

	private ParameterContributor _getProviderByName(String name)
		throws IllegalArgumentException {

		ParameterContributor parameterContributor = _parameterContributors.get(
			name);

		if (parameterContributor == null) {
			throw new IllegalArgumentException(
				"No registered context parameter provider " + name);
		}

		return parameterContributor;
	}

	private void _logParameters(SearchParameterData searchParameterData) {
		if (_log.isDebugEnabled()) {
			_log.debug("Search configuration variables after contributions:");

			if (searchParameterData.hasParameters()) {
				for (Parameter parameter :
						searchParameterData.getParameters()) {

					_log.debug(
						parameter.getConfigurationVariable() + ":" +
							parameter.getValue());
				}
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ParameterContributorsImpl.class);

	private volatile Map<String, ParameterContributor> _parameterContributors = new HashMap<String, ParameterContributor>();

}