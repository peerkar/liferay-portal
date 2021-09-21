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

package com.liferay.search.experiences.web.internal.blueprint.display.context;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.search.experiences.blueprint.parameter.SXPParameterContributionDefinition;
import com.liferay.search.experiences.blueprint.util.SXPEngineContextHelper;
import com.liferay.search.experiences.model.SXPElement;
import com.liferay.search.experiences.service.SXPElementService;
import com.liferay.search.experiences.web.internal.blueprint.constants.SXPBlueprintMVCCommandNames;
import com.liferay.search.experiences.web.internal.blueprint.util.SXPBlueprintRequestUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceURL;

/**
 * @author Kevin Tan
 * @author Petteri Karttunen
 */
public class EditSXPElementDisplayBuilder extends EditEntryDisplayBuilder {

	public EditSXPElementDisplayBuilder(
		RenderRequest renderRequest, RenderResponse renderResponse,
		SXPEngineContextHelper sxpEngineContextHelper,
		SXPElementService sxpElementService, JSONFactory jsonFactory,
		Language language) {

		super(
			renderRequest, renderResponse, sxpElementService, jsonFactory,
			language);

		_sxpEngineContextHelper = sxpEngineContextHelper;

		_sxpElement = _getSXPElement();

		_sxpElementId = SXPBlueprintRequestUtil.getSXPElementId(renderRequest);
	}

	public EntryDisplayContext build() {
		EntryDisplayContext entryDisplayContext = new EntryDisplayContext();

		entryDisplayContext.setId(_sxpElementId);

		_setType(entryDisplayContext);
		setData(entryDisplayContext, _getProps());
		_setPageTitle(entryDisplayContext);
		setRedirect(entryDisplayContext);

		return entryDisplayContext;
	}

	private SXPElement _getSXPElement() {
		Optional<SXPElement> optional = SXPBlueprintRequestUtil.getSXPElement(
			renderRequest, renderResponse);

		return optional.orElse(null);
	}

	private JSONArray _getPredefinedVariablesJSONArray() {
		JSONArray predefinedVariablesJSONArray = jsonFactory.createJSONArray();

		Map<String, List<SXPParameterContributionDefinition>> sxpParameterContributionDefinitions =
				_sxpEngineContextHelper.getSXPParameterContributionDefinitions();

		for (Map.Entry<String, List<SXPParameterContributionDefinition>> entry :
			sxpParameterContributionDefinitions.entrySet()) {

			JSONObject jsonObject = jsonFactory.createJSONObject();

			jsonObject.put(
				"categoryName",
				language.get(httpServletRequest, entry.getKey()));

			JSONArray parameterDefinitionsJSONArray =
				jsonFactory.createJSONArray();

			for (SXPParameterContributionDefinition sxpParameterContributionDefinition : entry.getValue()) {
				JSONObject parameterDefinitionJSONObject =
					jsonFactory.createJSONObject();

				parameterDefinitionJSONObject.put(
					"className", sxpParameterContributionDefinition.getClassName()
				).put(
					"description",
					language.get(
						httpServletRequest,
						sxpParameterContributionDefinition.getLanguageKey())
				).put(
					"variable", sxpParameterContributionDefinition.getTemplateVariable()
				);

				parameterDefinitionsJSONArray.put(
					parameterDefinitionJSONObject);
			}

			jsonObject.put(
				"parameterDefinitions", parameterDefinitionsJSONArray);

			predefinedVariablesJSONArray.put(jsonObject);
		}

		return predefinedVariablesJSONArray;
	}

	private Map<String, Object> _getProps() {
		Map<String, Object> props = HashMapBuilder.<String, Object>put(
			"sxpElementId", _sxpElementId
		).put(
			"redirectURL", getRedirect()
		).put(
			"submitFormURL",
			getSubmitFormURL(
				SXPBlueprintMVCCommandNames.EDIT_SXP_ELEMENT, _sxpElement != null)
		).put(
			"type", _getType()
		).put(
			"validateElementURL", _getValidateSXPElementURL()
		).build();

		if (_sxpElement != null) {
			props.put(
				"initialConfigurationString", _sxpElement.getElementDefinitionJSON());
			props.put(
				"initialDescription",
				getDescriptionJSONObject(_sxpElement.getDescriptionMap()));
			props.put(
				"initialTitle", getTitleJSONObject(_sxpElement.getTitleMap()));
			props.put(
				"predefinedVariables", _getPredefinedVariablesJSONArray());
			props.put("readOnly", _sxpElement.isReadOnly());
		}

		return props;
	}

	private int _getType() {
		if (_sxpElement != null) {
			return _sxpElement.getType();
		}

		return SXPBlueprintRequestUtil.getSXPElementType(renderRequest);
	}

	private String _getValidateSXPElementURL() {
		ResourceURL resourceURL = renderResponse.createResourceURL();

		resourceURL.setResourceID(
			SXPBlueprintMVCCommandNames.VALIDATE_SXP_ELEMENT);

		return resourceURL.toString();
	}

	private void _setPageTitle(EntryDisplayContext entryDisplayContext) {
		StringBundler sb = new StringBundler(2);

		sb.append((_sxpElement != null) ? "edit-" : "add-");
		sb.append("sxpElement");

		entryDisplayContext.setPageTitle(
			language.get(httpServletRequest, sb.toString()));
	}

	private void _setType(EntryDisplayContext entryDisplayContext) {
		entryDisplayContext.setType(_getType());
	}

	private final SXPEngineContextHelper _sxpEngineContextHelper;
	private final SXPElement _sxpElement;
	private final long _sxpElementId;

}