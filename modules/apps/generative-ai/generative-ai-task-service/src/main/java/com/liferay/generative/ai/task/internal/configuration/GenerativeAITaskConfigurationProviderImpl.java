/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.configuration;

import com.liferay.generative.ai.task.configuration.GenerativeAITaskConfiguration;
import com.liferay.generative.ai.task.configuration.GenerativeAITaskConfigurationProvider;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.configuration.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(service = GenerativeAITaskConfigurationProvider.class)
public class GenerativeAITaskConfigurationProviderImpl
	implements GenerativeAITaskConfigurationProvider {

	@Override
	public GenerativeAITaskConfiguration getCompanyConfiguration(
		long companyId) {

		return _getGenerativeAITaskConfiguration(companyId);
	}

	@Override
	public GenerativeAITaskConfiguration getSystemConfiguration() {
		return _getGenerativeAITaskConfiguration(CompanyConstants.SYSTEM);
	}

	private GenerativeAITaskConfiguration _getGenerativeAITaskConfiguration(
		long companyId) {

		try {
			if (companyId > CompanyConstants.SYSTEM) {
				return _configurationProvider.getCompanyConfiguration(
					GenerativeAITaskConfiguration.class, companyId);
			}

			return _configurationProvider.getSystemConfiguration(
				GenerativeAITaskConfiguration.class);
		}
		catch (ConfigurationException configurationException) {
			return ReflectionUtil.throwException(configurationException);
		}
	}

	@Reference
	private ConfigurationProvider _configurationProvider;

}