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

package com.liferay.search.experiences.internal.blueprint.searchrequest;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.search.highlight.Highlight;
import com.liferay.portal.search.searcher.SearchRequestBuilder;
import com.liferay.search.experiences.blueprint.parameter.SXPParameterData;
import com.liferay.search.experiences.blueprint.searchrequest.SearchRequestBodyContributor;
import com.liferay.search.experiences.blueprint.template.variable.SXPBlueprintTemplateVariableParser;
import com.liferay.search.experiences.blueprint.util.SXPBlueprintConfigurationsJSONHelper;
import com.liferay.search.experiences.internal.blueprint.util.HighlightHelper;
import com.liferay.search.experiences.model.SXPBlueprint;

import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, property = "name=highlight",
	service = SearchRequestBodyContributor.class
)
public class HighlightSearchRequestBodyContributor
	implements SearchRequestBodyContributor {

	@Override
	public void contribute(
		SearchRequestBuilder searchRequestBuilder, SXPBlueprint sxpBlueprint,
		SXPParameterData sxpParameterData) {

		Optional<JSONObject> optional =
			_sxpBlueprintConfigurationsJSONHelper.
				getHighlightConfigurationOptional(sxpBlueprint);

		if (!optional.isPresent()) {
			return;
		}

		_contribute(searchRequestBuilder, optional.get(), sxpParameterData);
	}

	private void _contribute(
		SearchRequestBuilder searchRequestBuilder, JSONObject jsonObject,
		SXPParameterData sxpParameterData) {

		Optional<JSONObject> optional1 =
			_sxpBlueprintTemplateVariableParser.parseObject(
				getClass().getName(), jsonObject, sxpParameterData);

		if (!optional1.isPresent()) {
			return;
		}

		Optional<Highlight> optional2 = _highlightHelper.getHighlight(
			optional1.get(), sxpParameterData);

		if (optional2.isPresent()) {
			searchRequestBuilder.highlight(optional2.get());
		}
	}

	@Reference
	private HighlightHelper _highlightHelper;

	@Reference
	private SXPBlueprintConfigurationsJSONHelper
		_sxpBlueprintConfigurationsJSONHelper;

	@Reference
	private SXPBlueprintTemplateVariableParser
		_sxpBlueprintTemplateVariableParser;

}