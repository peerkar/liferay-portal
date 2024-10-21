/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.rest.internal.graphql.servlet.v1_0;

import com.liferay.generative.ai.rest.internal.graphql.mutation.v1_0.Mutation;
import com.liferay.generative.ai.rest.internal.graphql.query.v1_0.Query;
import com.liferay.generative.ai.rest.internal.resource.v1_0.GenerativeAIResponseResourceImpl;
import com.liferay.generative.ai.rest.internal.resource.v1_0.TaskDefinitionResourceImpl;
import com.liferay.generative.ai.rest.resource.v1_0.GenerativeAIResponseResource;
import com.liferay.generative.ai.rest.resource.v1_0.TaskDefinitionResource;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.vulcan.graphql.servlet.ServletData;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;

/**
 * @author Petteri Karttunen
 * @generated
 */
@Component(service = ServletData.class)
@Generated("")
public class ServletDataImpl implements ServletData {

	@Activate
	public void activate(BundleContext bundleContext) {
		Mutation.setGenerativeAIResponseResourceComponentServiceObjects(
			_generativeAIResponseResourceComponentServiceObjects);
		Mutation.setTaskDefinitionResourceComponentServiceObjects(
			_taskDefinitionResourceComponentServiceObjects);

		Query.setTaskDefinitionResourceComponentServiceObjects(
			_taskDefinitionResourceComponentServiceObjects);
	}

	public String getApplicationName() {
		return "Liferay.Generative.AI.REST";
	}

	@Override
	public Mutation getMutation() {
		return new Mutation();
	}

	@Override
	public String getPath() {
		return "/generative-ai-graphql/v1_0";
	}

	@Override
	public Query getQuery() {
		return new Query();
	}

	public ObjectValuePair<Class<?>, String> getResourceMethodObjectValuePair(
		String methodName, boolean mutation) {

		if (mutation) {
			return _resourceMethodObjectValuePairs.get(
				"mutation#" + methodName);
		}

		return _resourceMethodObjectValuePairs.get("query#" + methodName);
	}

	private static final Map<String, ObjectValuePair<Class<?>, String>>
		_resourceMethodObjectValuePairs =
			new HashMap<String, ObjectValuePair<Class<?>, String>>() {
				{
					put(
						"mutation#createGenerateExternalReferenceCode",
						new ObjectValuePair<>(
							GenerativeAIResponseResourceImpl.class,
							"postGenerateExternalReferenceCode"));
					put(
						"mutation#createTaskDefinitionsPageExportBatch",
						new ObjectValuePair<>(
							TaskDefinitionResourceImpl.class,
							"postTaskDefinitionsPageExportBatch"));
					put(
						"mutation#createTaskDefinition",
						new ObjectValuePair<>(
							TaskDefinitionResourceImpl.class,
							"postTaskDefinition"));
					put(
						"mutation#createTaskDefinitionBatch",
						new ObjectValuePair<>(
							TaskDefinitionResourceImpl.class,
							"postTaskDefinitionBatch"));
					put(
						"mutation#updateTaskDefinitionByExternalReferenceCode",
						new ObjectValuePair<>(
							TaskDefinitionResourceImpl.class,
							"putTaskDefinitionByExternalReferenceCode"));
					put(
						"mutation#createTaskDefinitionByExternalReferenceCodeClear",
						new ObjectValuePair<>(
							TaskDefinitionResourceImpl.class,
							"postTaskDefinitionByExternalReferenceCodeClear"));
					put(
						"mutation#createTaskDefinitionValidate",
						new ObjectValuePair<>(
							TaskDefinitionResourceImpl.class,
							"postTaskDefinitionValidate"));
					put(
						"mutation#deleteTaskDefinition",
						new ObjectValuePair<>(
							TaskDefinitionResourceImpl.class,
							"deleteTaskDefinition"));
					put(
						"mutation#deleteTaskDefinitionBatch",
						new ObjectValuePair<>(
							TaskDefinitionResourceImpl.class,
							"deleteTaskDefinitionBatch"));
					put(
						"mutation#patchTaskDefinition",
						new ObjectValuePair<>(
							TaskDefinitionResourceImpl.class,
							"patchTaskDefinition"));
					put(
						"mutation#updateTaskDefinition",
						new ObjectValuePair<>(
							TaskDefinitionResourceImpl.class,
							"putTaskDefinition"));
					put(
						"mutation#updateTaskDefinitionBatch",
						new ObjectValuePair<>(
							TaskDefinitionResourceImpl.class,
							"putTaskDefinitionBatch"));
					put(
						"mutation#createTaskDefinitionCopy",
						new ObjectValuePair<>(
							TaskDefinitionResourceImpl.class,
							"postTaskDefinitionCopy"));

					put(
						"query#taskDefinitions",
						new ObjectValuePair<>(
							TaskDefinitionResourceImpl.class,
							"getTaskDefinitionsPage"));
					put(
						"query#taskDefinitionByExternalReferenceCode",
						new ObjectValuePair<>(
							TaskDefinitionResourceImpl.class,
							"getTaskDefinitionByExternalReferenceCode"));
					put(
						"query#taskDefinition",
						new ObjectValuePair<>(
							TaskDefinitionResourceImpl.class,
							"getTaskDefinition"));
					put(
						"query#taskDefinitionExport",
						new ObjectValuePair<>(
							TaskDefinitionResourceImpl.class,
							"getTaskDefinitionExport"));

					put(
						"query#TaskDefinition.export",
						new ObjectValuePair<>(
							TaskDefinitionResourceImpl.class,
							"getTaskDefinitionExport"));
				}
			};

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<GenerativeAIResponseResource>
		_generativeAIResponseResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<TaskDefinitionResource>
		_taskDefinitionResourceComponentServiceObjects;

}