/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.internal.site;

import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Reads the site selection out of an export or import parameter map, and builds
 * the parameter map each per-site pass runs under.
 *
 * <p>
 * The company level pass carries {@link
 * PortletDataHandlerKeys#SITE_EXTERNAL_REFERENCE_CODES}, the sites the user
 * selected. Each per-site pass carries {@link
 * PortletDataHandlerKeys#SITE_EXTERNAL_REFERENCE_CODE} instead, the one site it
 * is processing. Neither parameter is ever mutated once its context exists: a
 * per-site pass gets a parameter map of its own, derived here.
 * </p>
 *
 * @author Petteri Karttunen
 */
public class SiteExportImportParameterUtil {

	public static String[] getSelectedSiteExternalReferenceCodes(
		Map<String, String[]> parameterMap) {

		if (parameterMap == null) {
			return new String[0];
		}

		String[] siteExternalReferenceCodes = parameterMap.get(
			PortletDataHandlerKeys.SITE_EXTERNAL_REFERENCE_CODES);

		if (siteExternalReferenceCodes == null) {
			return new String[0];
		}

		Set<String> uniqueSiteExternalReferenceCodes = new LinkedHashSet<>();

		for (String siteExternalReferenceCode : siteExternalReferenceCodes) {
			if (Validator.isNotNull(siteExternalReferenceCode)) {
				uniqueSiteExternalReferenceCodes.add(
					siteExternalReferenceCode.trim());
			}
		}

		return uniqueSiteExternalReferenceCodes.toArray(new String[0]);
	}

	/**
	 * Returns the external reference codes of the sites the user selected,
	 * without blanks or duplicates.
	 */
	public static String[] getSelectedSiteExternalReferenceCodes(
		PortletDataContext portletDataContext) {

		return getSelectedSiteExternalReferenceCodes(
			portletDataContext.getParameterMap());
	}

	public static String getSiteExternalReferenceCode(
		Map<String, String[]> parameterMap) {

		if (parameterMap == null) {
			return null;
		}

		String siteExternalReferenceCode = MapUtil.getString(
			parameterMap, _CURRENT_SITE_EXTERNAL_REFERENCE_CODE);

		if (Validator.isNull(siteExternalReferenceCode)) {
			return null;
		}

		return siteExternalReferenceCode;
	}

	/**
	 * Returns the external reference code of the site the given pass is
	 * processing, or <code>null</code> when it is the company level pass that
	 * owns the LAR.
	 */
	public static String getSiteExternalReferenceCode(
		PortletDataContext portletDataContext) {

		return getSiteExternalReferenceCode(
			portletDataContext.getParameterMap());
	}

	public static boolean isEnabled(long companyId) {
		return FeatureFlagManagerUtil.isEnabled(companyId, "LPD-85946");
	}

	public static boolean isSitePass(Map<String, String[]> parameterMap) {
		if (getSiteExternalReferenceCode(parameterMap) != null) {
			return true;
		}

		return false;
	}

	public static boolean isSitePass(PortletDataContext portletDataContext) {
		return isSitePass(portletDataContext.getParameterMap());
	}

	/**
	 * Returns the parameter map a single site is exported under.
	 *
	 * <p>
	 * A site is exported as a whole unit, so its pages, its page set settings
	 * and all of its portlet data go in regardless of what the user picked for
	 * the company level entities. What the user picked still applies to
	 * everything this method leaves alone, such as comments and ratings.
	 * </p>
	 */
	public static Map<String, String[]> toSiteExportParameterMap(
		Map<String, String[]> parameterMap, String siteExternalReferenceCode) {

		Map<String, String[]> siteParameterMap = _copy(
			parameterMap, siteExternalReferenceCode);

		// The pages of the site and everything on them. A whole site means all
		// of its content, so the controls a data handler offers default to on.
		// Without this the handlers are asked for their data and answer with
		// nothing, because the caller may well have turned the default off for
		// the company level entities it was choosing between.

		_put(siteParameterMap, PortletDataHandlerKeys.PORTLET_DATA, true);
		_put(siteParameterMap, PortletDataHandlerKeys.PORTLET_DATA_ALL, true);
		_put(
			siteParameterMap,
			PortletDataHandlerKeys.PORTLET_DATA_CONTROL_DEFAULT, true);
		_put(
			siteParameterMap, PortletDataHandlerKeys.LAYOUT_SET_SETTINGS, true);
		_put(siteParameterMap, PortletDataHandlerKeys.LOGO, true);
		_put(siteParameterMap, PortletDataHandlerKeys.THEME_REFERENCE, true);
		_put(
			siteParameterMap, PortletDataHandlerKeys.PORTLET_CONFIGURATION,
			true);
		_put(
			siteParameterMap, PortletDataHandlerKeys.PORTLET_CONFIGURATION_ALL,
			true);
		_put(siteParameterMap, PortletDataHandlerKeys.PORTLET_SETUP_ALL, true);
		_put(
			siteParameterMap,
			PortletDataHandlerKeys.PORTLET_ARCHIVED_SETUPS_ALL, true);

		return siteParameterMap;
	}

	/**
	 * Returns the parameter map a single site is imported under.
	 *
	 * <p>
	 * Sites are always mirrored, whatever update strategy the user picked for
	 * the company level entities.
	 * </p>
	 */
	public static Map<String, String[]> toSiteImportParameterMap(
		Map<String, String[]> parameterMap, String siteExternalReferenceCode) {

		Map<String, String[]> siteParameterMap = toSiteExportParameterMap(
			parameterMap, siteExternalReferenceCode);

		siteParameterMap.put(
			PortletDataHandlerKeys.DATA_STRATEGY,
			new String[] {PortletDataHandlerKeys.DATA_STRATEGY_MIRROR});
		siteParameterMap.put(
			PortletDataHandlerKeys.LAYOUTS_IMPORT_MODE,
			new String[] {
				PortletDataHandlerKeys.LAYOUTS_IMPORT_MODE_MERGE_BY_LAYOUT_UUID
			});

		// Pages the LAR does not carry are left alone. Removing pages from a
		// site the user did not ask to have emptied is not something an import
		// should decide on its own.

		_put(
			siteParameterMap, PortletDataHandlerKeys.DELETE_MISSING_LAYOUTS,
			false);
		_put(
			siteParameterMap, PortletDataHandlerKeys.DELETE_PORTLET_DATA,
			false);

		return siteParameterMap;
	}

	private static Map<String, String[]> _copy(
		Map<String, String[]> parameterMap, String siteExternalReferenceCode) {

		Map<String, String[]> siteParameterMap = new HashMap<>();

		if (parameterMap != null) {
			siteParameterMap.putAll(parameterMap);
		}

		// The selection belongs to the company level pass. Dropping it here is
		// what keeps a per-site pass from starting per-site passes of its own,
		// and what keeps the site settings from being exported once per site.

		siteParameterMap.remove(
			PortletDataHandlerKeys.SITE_EXTERNAL_REFERENCE_CODES);

		siteParameterMap.put(
			_CURRENT_SITE_EXTERNAL_REFERENCE_CODE,
			new String[] {siteExternalReferenceCode});

		// Permissions and deletions of elements inside a site are out of scope

		_put(siteParameterMap, PortletDataHandlerKeys.PERMISSIONS, false);
		_put(siteParameterMap, PortletDataHandlerKeys.DELETIONS, false);

		// Private pages are out of scope, so a site is always its public page
		// set

		_put(
			siteParameterMap, PortletDataHandlerKeys.LAYOUT_SET_PRIVATE_LAYOUT,
			false);

		// Site templates are out of scope

		_put(
			siteParameterMap,
			PortletDataHandlerKeys.LAYOUT_SET_PROTOTYPE_SETTINGS, false);

		return siteParameterMap;
	}

	private static void _put(
		Map<String, String[]> parameterMap, String key, boolean value) {

		parameterMap.put(key, new String[] {String.valueOf(value)});
	}

	/**
	 * Marks a parameter map as belonging to a per-site pass, and names the site
	 * that pass is processing.
	 *
	 * <p>
	 * Written once, when the per-site map is derived, and never mutated
	 * afterwards. Nothing outside this class reads it: whether a pass is a
	 * per-site pass is answered by {@link #isSitePass}, and which site it is
	 * processing by {@link #getSiteExternalReferenceCode}.
	 * </p>
	 */
	private static final String _CURRENT_SITE_EXTERNAL_REFERENCE_CODE =
		"CURRENT_SITE_EXTERNAL_REFERENCE_CODE";

}