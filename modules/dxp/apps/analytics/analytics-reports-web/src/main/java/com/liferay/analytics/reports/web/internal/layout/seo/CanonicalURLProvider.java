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

package com.liferay.analytics.reports.web.internal.layout.seo;

import com.liferay.layout.seo.kernel.LayoutSEOLink;
import com.liferay.layout.seo.kernel.LayoutSEOLinkManager;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Cristina González
 */
public class CanonicalURLProvider {

	public CanonicalURLProvider(
		HttpServletRequest httpServletRequest, Language language,
		LayoutSEOLinkManager layoutSEOLinkManager, Portal portal) {

		_httpServletRequest = httpServletRequest;
		_language = language;
		_layoutSEOLinkManager = layoutSEOLinkManager;
		_portal = portal;

		_themeDisplay = (ThemeDisplay)httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public String getCanonicalURL() throws PortalException {
		return _getCanonicalURL(
			_portal.getCanonicalURL(
				_portal.getCurrentCompleteURL(_httpServletRequest),
				_themeDisplay, _themeDisplay.getLayout(), false, false));
	}

	private Map<Locale, String> _getAlternateURLS(String canonicalURL)
		throws PortalException {

		Set<Locale> locales = _language.getAvailableLocales(
			_themeDisplay.getSiteGroupId());

		if (locales.size() > 1) {
			return _portal.getAlternateURLs(
				canonicalURL, _themeDisplay, _themeDisplay.getLayout());
		}

		return Collections.emptyMap();
	}

	private String _getCanonicalURL(String canonicalURL)
		throws PortalException {

		LayoutSEOLink layoutSEOLink =
			_layoutSEOLinkManager.getCanonicalLayoutSEOLink(
				_themeDisplay.getLayout(), _themeDisplay.getLocale(),
				canonicalURL, _getAlternateURLS(canonicalURL));

		return layoutSEOLink.getHref();
	}

	private final HttpServletRequest _httpServletRequest;
	private final Language _language;
	private final LayoutSEOLinkManager _layoutSEOLinkManager;
	private final Portal _portal;
	private final ThemeDisplay _themeDisplay;

}