/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.web.internal.application.list;

import com.liferay.application.list.BasePanelCategory;
import com.liferay.application.list.PanelCategory;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.generative.ai.constants.GenerativeAIPanelCategoryKeys;
import com.liferay.portal.kernel.language.Language;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = {
		"panel.category.key=" + PanelCategoryKeys.APPLICATIONS_MENU_APPLICATIONS,
		"panel.category.order:Integer=500"
	},
	service = PanelCategory.class
)
public class GenerativeAIPanelCategory extends BasePanelCategory {

	@Override
	public String getKey() {
		return GenerativeAIPanelCategoryKeys.CONTROL_PANEL_GENERATIVE_AI;
	}

	@Override
	public String getLabel(Locale locale) {
		return _language.get(locale, "generative-ai");
	}

	@Reference
	private Language _language;

}