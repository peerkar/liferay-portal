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

package com.liferay.search.experiences.blueprints.options.web.internal.portlet.shared.search;

import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchContributor;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchSettings;
import com.liferay.search.experiences.blueprints.engine.constants.SearchContextAttributeKeys;
import com.liferay.search.experiences.blueprints.options.web.internal.constants.BlueprintsOptionsPortletKeys;
import com.liferay.search.experiences.blueprints.options.web.internal.portlet.preferences.BlueprintsOptionsPortletPreferences;
import com.liferay.search.experiences.blueprints.options.web.internal.portlet.preferences.BlueprintsOptionsPortletPreferencesImpl;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Kevin Tan
 */
@Component(
	immediate = true,
	property = "javax.portlet.name=" + BlueprintsOptionsPortletKeys.BLUEPRINTS_OPTIONS,
	service = PortletSharedSearchContributor.class
)
public class BlueprintsOptionsPortletSharedSearchContributor
	implements PortletSharedSearchContributor {

	@Override
	public void contribute(
		PortletSharedSearchSettings portletSharedSearchSettings) {

		SearchContext searchContext =
			portletSharedSearchSettings.getSearchContext();

		String blueprintId = (String)searchContext.getAttribute(
			SearchContextAttributeKeys.BLUEPRINT_ID);

		if (Validator.isNull(blueprintId)) {
			BlueprintsOptionsPortletPreferences
				blueprintsOptionsPortletPreferences =
					new BlueprintsOptionsPortletPreferencesImpl(
						portletSharedSearchSettings.
							getPortletPreferencesOptional());

			String blueprintIdPreference =
				blueprintsOptionsPortletPreferences.getBlueprintIdString();

			if (!Objects.equals(blueprintIdPreference, "0")) {
				searchContext.setAttribute(
					SearchContextAttributeKeys.BLUEPRINT_ID,
					blueprintIdPreference);
			}
		}

		HttpServletRequest httpServletRequest = _portal.getHttpServletRequest(
			portletSharedSearchSettings.getRenderRequest());

		searchContext.setAttribute(
			SearchContextAttributeKeys.IP_ADDRESS,
			httpServletRequest.getRemoteAddr());
	}

	@Reference
	private Portal _portal;

}