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
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.tuning.gsearch.parameter.LongParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.ParameterDefinition;
import com.liferay.portal.search.tuning.gsearch.parameter.SearchParameterData;
import com.liferay.portal.search.tuning.gsearch.parameter.StringParameter;
import com.liferay.portal.search.tuning.gsearch.spi.parameter.ParameterContributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, property = "name=context",
	service = ParameterContributor.class
)
public class ContextParameterContributor implements ParameterContributor {

	@Override
	public void contribute(
		HttpServletRequest httpServletRequest,
		SearchParameterData searchParameterData) {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		Layout layout = themeDisplay.getLayout();

		String layoutNameCurrentValue = layout.getNameCurrentValue();

		_contribute(
			searchParameterData, themeDisplay.getCompanyId(),
			themeDisplay.getScopeGroupId(), themeDisplay.getLocale(),
			layoutNameCurrentValue);
	}

	@Override
	public void contribute(
		SearchContext searchContext, SearchParameterData searchParameterData) {

		Long scopeGroupId = (Long)searchContext.getAttribute("scopeGroupId");
		String layoutNameCurrentValue = (String)searchContext.getAttribute(
			"layoutNameCurrentValue");

		_contribute(
			searchParameterData, searchContext.getCompanyId(), scopeGroupId,
			searchContext.getLocale(), layoutNameCurrentValue);
	}

	public List<ParameterDefinition> getParameterDefinitions() {
		List<ParameterDefinition> parameterDefinitions = new ArrayList<>();

		parameterDefinitions.add(
			new ParameterDefinition(
				"${context.company_id}", LongParameter.class.getName(),
				"parameter.context.company-id"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${context.scope_group_id}", LongParameter.class.getName(),
				"parameter.context.scope-group-id"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${context.layout_locale_name}",
				StringParameter.class.getName(),
				"parameter.context.layout-locale-name"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${context.language_id}", StringParameter.class.getName(),
				"parameter.context.language-id"));

		return parameterDefinitions;
	}

	private void _contribute(
		SearchParameterData searchParameterData, long companyId,
		Long scopeGroupId, Locale locale, String layoutNameCurrentValue) {

		searchParameterData.addParameter(
			new LongParameter(
				"context.company_id", null, "${context.company_id}",
				companyId));

		if (scopeGroupId != null) {
			searchParameterData.addParameter(
				new LongParameter(
					"context.scope_group_id", null, "${context.scope_group_id}",
					scopeGroupId));
		}

		if (!Validator.isBlank(layoutNameCurrentValue)) {
			searchParameterData.addParameter(
				new StringParameter(
					"context.layout_locale_name", null,
					"${context.layout_locale_name}", layoutNameCurrentValue));
		}

		searchParameterData.addParameter(
			new StringParameter(
				"context.language_id", null, "${context.language_id}",
				_language.getLanguageId(locale)));
	}

	@Reference
	private Language _language;

}