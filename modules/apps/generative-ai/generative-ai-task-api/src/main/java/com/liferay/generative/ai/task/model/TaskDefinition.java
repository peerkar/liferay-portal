/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.model;

import com.liferay.portal.kernel.annotation.ImplementationClassName;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.Accessor;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The extended model interface for the TaskDefinition service. Represents a row in the &quot;TaskDefinition&quot; database table, with each column mapped to a property of this class.
 *
 * @author Brian Wing Shun Chan
 * @see TaskDefinitionModel
 * @generated
 */
@ImplementationClassName(
	"com.liferay.generative.ai.task.model.impl.TaskDefinitionImpl"
)
@ProviderType
public interface TaskDefinition extends PersistedModel, TaskDefinitionModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to <code>com.liferay.generative.ai.task.model.impl.TaskDefinitionImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<TaskDefinition, Long>
		TASK_DEFINITION_ID_ACCESSOR = new Accessor<TaskDefinition, Long>() {

			@Override
			public Long get(TaskDefinition taskDefinition) {
				return taskDefinition.getTaskDefinitionId();
			}

			@Override
			public Class<Long> getAttributeClass() {
				return Long.class;
			}

			@Override
			public Class<TaskDefinition> getTypeClass() {
				return TaskDefinition.class;
			}

		};

}