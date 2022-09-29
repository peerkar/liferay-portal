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

package com.liferay.search.experiences.ingest.web.internal.util;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.PortletRequest;

/**
 * @author Petteri Karttunen
 */
public class ExpandoUtil {

	public static void createGeoLocationExpandoAttribute(
			String expandoAttributeName, Class<?> clazz,
			PortletRequest portletRequest)
		throws PortalException {

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		ExpandoBridge expandoBridge = ExpandoBridgeFactoryUtil.getExpandoBridge(
			themeDisplay.getCompanyId(), clazz.getName());

		if (!expandoBridge.hasAttribute(expandoAttributeName)) {
			expandoBridge.addAttribute(
				expandoAttributeName, ExpandoColumnConstants.GEOLOCATION,
				JSONUtil.put(
					"latitude", 0D
				).put(
					"longitude", 0D
				),
				false);

			UnicodeProperties unicodeProperties =
				expandoBridge.getAttributeProperties(expandoAttributeName);

			unicodeProperties.setProperty(
				ExpandoColumnConstants.INDEX_TYPE,
				String.valueOf(ExpandoColumnConstants.INDEX_TYPE_KEYWORD));

			unicodeProperties.setProperty(
				ExpandoColumnConstants.PROPERTY_LOCALIZE_FIELD_NAME, "false");

			expandoBridge.setAttributeProperties(
				expandoAttributeName, unicodeProperties);
		}
	}

}