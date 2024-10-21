/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import ClayForm, { ClayRadio, ClayRadioGroup, ClaySelect } from '@clayui/form';
import ClayButton from '@clayui/button';
import { ClayInput } from '@clayui/form';

import ClayLayout from '@clayui/layout';
import getCN from 'classnames';
import React, {useContext, useEffect, useState } from 'react';

import taskConfigurationSchema from '../../../schemas/task-configuration.schema.json';
import CodeMirrorEditor from '../../shared/CodeMirrorEditor';
import LearnMessage from '../../shared/LearnMessage';
import ThemeContext from '../../shared/ThemeContext';
import { DEFAULT_INDEX_CONFIGURATION } from '../../utils/constants';
import ConfigurationForm from './ConfigurationForm';
import MockedFlow from './ConfigurationFlow/MockedFlow';
import { add } from 'date-fns';

const CONFIGURATION_SCHEMAS = {
	taskConfig: taskConfigurationSchema,
};

const addIdstoSerializedTaskConfig = (serializedTaskConfig) => {
	let taskConfig;
	
	try {
		taskConfig = JSON.parse(serializedTaskConfig);
	} catch (error) {
		console.error('Error parsing serialized task config:', error);
	}

    if (!taskConfig || !Object.keys(taskConfig).length) {
        return taskConfig;
    }

	let idCounter = 1;

	function addIdToObjectsWithNames(obj) {
	  for (let key in obj) {
		if (typeof obj[key] === 'object' && obj[key] !== null) {
		  addIdToObjectsWithNames(obj[key]);
		}
		if (key === 'name') {
		  obj.id = idCounter++;
		}
	  }
	}

	addIdToObjectsWithNames(taskConfig);

	return taskConfig;
};

function ConfigurationTab({
	errors,
	externalReferenceCode,
	setFieldTouched,
	serializedTaskConfig,
	setFieldValue,
	touched
}) {
	const { isCompanyAdmin } = useContext(ThemeContext);
	const [editingMode, setEditingMode] = useState('flow');
	const [taskConfigWithIds, setTaskConfigWithIds] = useState(
		addIdstoSerializedTaskConfig(serializedTaskConfig)
	);

	// useEffect(() => {
	// 	setTaskConfigWithIds(
	// 		addIdstoSerializedTaskConfig(
	// 			serializedTaskConfig
	// 		)
	// 	);
	// }, [serializedTaskConfig]);

	useEffect(() => {
		// console.log("taskConfigWithIds", taskConfigWithIds);

		setFieldValue('taskConfig', JSON.stringify(taskConfigWithIds, null, 2));
	}, [taskConfigWithIds]);

	const _renderJSONEditor = () => (
		<ClayForm.Group>
			<div
				className={getCN({
					'has-error': touched['taskConfig'] && errors['taskConfig'],
				})}
				onBlur={() => setFieldTouched('taskConfig')}
			>
				<CodeMirrorEditor
					autocompleteSchema={CONFIGURATION_SCHEMAS['taskConfig']}
					onChange={(value) => setFieldValue('taskConfig', value)}
					value={serializedTaskConfig}
				/>

				{touched['taskConfig'] && errors['taskConfig'] && (
					<ClayForm.FeedbackGroup>
						<ClayForm.FeedbackItem>
							<ClayForm.FeedbackIndicator symbol="exclamation-full" />

							{errors['taskConfig']}
						</ClayForm.FeedbackItem>
					</ClayForm.FeedbackGroup>
				)}
			</div>
		</ClayForm.Group>
	);

	const _renderFlowEditor = () => { 
		return (
			<div className='flow-editor-container'>
				<MockedFlow
					editingMode={editingMode}
					externalReferenceCode={externalReferenceCode}
					taskConfigWithIds={taskConfigWithIds}
					setTaskConfigWithIds={setTaskConfigWithIds}
					setFieldTouched={setFieldTouched}
					setFieldValue={setFieldValue}
				/>
			</div>
		)
	};

	const _handleSwitchEditMode = () =>{
		setEditingMode((currentMode) =>
			currentMode === 'json' ? 'flow' : 'json'
		)
	};

	return (
		<ClayLayout.ContainerFluid className="layout-section-main" size="xl">
			<div className="layout-section-main-shift">
				<div className="configuration-sheet sheet">
					<div className='sheet-header'>
						<label className="sheet-title">
							{Liferay.Language.get('task-configuration')}
						</label>

						<ClayButton
							borderless
							displayType="secondary"
							onClick={_handleSwitchEditMode}
							xs
						>
							{editingMode === 'json'
								? "Edit with Flow"
								: "Edit with JSON"}
						</ClayButton>
					</div>

					{editingMode === 'json'
						? _renderJSONEditor()
						: _renderFlowEditor()
					}

				</div>
			</div>
		</ClayLayout.ContainerFluid>
	);
}

export default React.memo(ConfigurationTab);
