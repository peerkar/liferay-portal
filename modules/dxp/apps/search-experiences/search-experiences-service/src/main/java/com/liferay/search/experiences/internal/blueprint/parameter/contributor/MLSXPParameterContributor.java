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

import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.search.experiences.blueprint.parameter.SXPParameter;
import com.liferay.search.experiences.blueprint.parameter.contributor.SXPParameterContributorDefinition;
import com.liferay.search.experiences.internal.blueprint.parameter.DoubleArraySXPParameter;
import com.liferay.search.experiences.internal.configuration.TXTAIConfiguration;
import com.liferay.search.experiences.internal.ml.txtai.client.TXTAIClient;
import com.liferay.search.experiences.internal.ml.txtai.client.TXTAIClientImpl;
import com.liferay.search.experiences.rest.dto.v1_0.SXPBlueprint;

import java.beans.ExceptionListener;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author Petteri Karttunen
 */
public class MLSXPParameterContributor implements SXPParameterContributor {

	public MLSXPParameterContributor(
		ConfigurationProvider configurationProvider) {

		_configurationProvider = configurationProvider;
	}

	@Override
	public void contribute(
		ExceptionListener exceptionListener, SearchContext searchContext,
		SXPBlueprint sxpBlueprint, Set<SXPParameter> sxpParameters) {

		TXTAIClient txtAIClient = new TXTAIClientImpl(
			_getTXTAIConfiguration(searchContext.getCompanyId()));

		try {
			Double[] textEmbeddings = txtAIClient.getTextEmbedding(
				searchContext.getKeywords(), true);

			if (ArrayUtil.isEmpty(textEmbeddings)) {
				return;
			}

			sxpParameters.add(
				new DoubleArraySXPParameter(
					"ml.keyword_vectors", true, textEmbeddings));
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}

			exceptionListener.exceptionThrown(exception);
		}
	}

	@Override
	public String getSXPParameterCategoryNameKey() {
		return "ml";
	}

	@Override
	public List<SXPParameterContributorDefinition>
		getSXPParameterContributorDefinitions(long companyId, Locale locale) {

		return Arrays.asList(
			new SXPParameterContributorDefinition(
				DoubleArraySXPParameter.class, "keyword-vectors",
				"ml.keyword_vectors"));
	}

	private TXTAIConfiguration _getTXTAIConfiguration(long companyId) {
		try {
			return _configurationProvider.getCompanyConfiguration(
				TXTAIConfiguration.class, companyId);
		}
		catch (ConfigurationException configurationException) {
			return ReflectionUtil.throwException(configurationException);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MLSXPParameterContributor.class);

	private final ConfigurationProvider _configurationProvider;

}