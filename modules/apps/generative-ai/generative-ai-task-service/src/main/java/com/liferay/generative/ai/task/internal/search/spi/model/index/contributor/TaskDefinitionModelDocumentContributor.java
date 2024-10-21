/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.search.spi.model.index.contributor;

import com.liferay.generative.ai.task.model.TaskDefinition;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Localization;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = "indexer.class.name=com.liferay.generative.ai.model.TaskDefinition",
	service = ModelDocumentContributor.class
)
public class TaskDefinitionModelDocumentContributor
	implements ModelDocumentContributor<TaskDefinition> {

	@Override
	public void contribute(Document document, TaskDefinition taskDefinition) {
		document.addDate(Field.MODIFIED_DATE, taskDefinition.getModifiedDate());
		document.addKeyword(Field.STATUS, taskDefinition.getStatus());

		for (Locale locale :
				_language.getCompanyAvailableLocales(
					taskDefinition.getCompanyId())) {

			String languageId = LocaleUtil.toLanguageId(locale);

			document.addKeyword(
				Field.getSortableFieldName(
					_localization.getLocalizedName(
						Field.DESCRIPTION, languageId)),
				taskDefinition.getDescription(locale), true);
			document.addKeyword(
				Field.getSortableFieldName(
					_localization.getLocalizedName(Field.TITLE, languageId)),
				taskDefinition.getTitle(locale), true);
			document.addText(
				_localization.getLocalizedName(Field.DESCRIPTION, languageId),
				taskDefinition.getDescription(locale));
			document.addText(
				_localization.getLocalizedName(Field.TITLE, languageId),
				taskDefinition.getTitle(locale));
		}
	}

	@Reference
	private Language _language;

	@Reference
	private Localization _localization;

}