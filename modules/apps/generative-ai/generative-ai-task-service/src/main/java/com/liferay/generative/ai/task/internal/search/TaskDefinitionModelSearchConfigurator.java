/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.search;

import com.liferay.generative.ai.task.internal.search.spi.model.index.contributor.TaskDefinitionModelIndexerWriterContributor;
import com.liferay.generative.ai.task.internal.search.spi.model.result.contributor.TaskDefinitionModelSummaryContributor;
import com.liferay.generative.ai.task.model.TaskDefinition;
import com.liferay.generative.ai.task.service.TaskDefinitionLocalService;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.util.Localization;
import com.liferay.portal.search.batch.DynamicQueryBatchIndexingActionableFactory;
import com.liferay.portal.search.spi.model.index.contributor.ModelIndexerWriterContributor;
import com.liferay.portal.search.spi.model.registrar.ModelSearchConfigurator;
import com.liferay.portal.search.spi.model.result.contributor.ModelSummaryContributor;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(service = ModelSearchConfigurator.class)
public class TaskDefinitionModelSearchConfigurator
	implements ModelSearchConfigurator<TaskDefinition> {

	@Override
	public String getClassName() {
		return TaskDefinition.class.getName();
	}

	@Override
	public String[] getDefaultSelectedFieldNames() {
		return new String[] {
			Field.COMPANY_ID, Field.ENTRY_CLASS_NAME, Field.ENTRY_CLASS_PK,
			Field.GROUP_ID, Field.MODIFIED_DATE, Field.SCOPE_GROUP_ID,
			Field.STATUS, Field.UID
		};
	}

	@Override
	public String[] getDefaultSelectedLocalizedFieldNames() {
		return new String[] {Field.DESCRIPTION, Field.TITLE};
	}

	@Override
	public ModelIndexerWriterContributor<TaskDefinition>
		getModelIndexerWriterContributor() {

		return _modelIndexWriterContributor;
	}

	@Override
	public ModelSummaryContributor getModelSummaryContributor() {
		return _modelSummaryContributor;
	}

	@Activate
	protected void activate() {
		_modelIndexWriterContributor =
			new TaskDefinitionModelIndexerWriterContributor(
				_dynamicQueryBatchIndexingActionableFactory,
				_taskDefinitionLocalService);
		_modelSummaryContributor = new TaskDefinitionModelSummaryContributor(
			_localization);
	}

	@Reference
	private DynamicQueryBatchIndexingActionableFactory
		_dynamicQueryBatchIndexingActionableFactory;

	@Reference
	private Localization _localization;

	private ModelIndexerWriterContributor<TaskDefinition>
		_modelIndexWriterContributor;
	private ModelSummaryContributor _modelSummaryContributor;

	@Reference
	private TaskDefinitionLocalService _taskDefinitionLocalService;

}