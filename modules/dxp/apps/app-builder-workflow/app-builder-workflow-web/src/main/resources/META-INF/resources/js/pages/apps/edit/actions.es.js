/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import {getItem} from 'app-builder-web/js/utils/client.es';
import {getTranslatedValue} from 'app-builder-web/js/utils/utils.es';

import {getFormViewFields, validateSelectedFormViews} from './utils.es';

const PARAMS = {keywords: '', page: -1, pageSize: -1, sort: ''};

export function getAssigneeRoles() {
	return getItem('/o/headless-admin-user/v1.0/roles').then(getItems);
}

export function getDataObjects() {
	return Promise.all([
		getItem(
			'/o/data-engine/v2.0/data-definitions/by-content-type/app-builder',
			PARAMS
		).then(getItems),
		getItem(
			'/o/data-engine/v2.0/data-definitions/by-content-type/native-object',
			PARAMS
		).then(getItems),
	]).then(([customObjects, nativeObjects]) => {
		return [
			...customObjects.map((item) => ({
				...item,
				type: 'custom',
			})),
			...nativeObjects.map((item) => ({
				...item,
				type: 'native',
			})),
		];
	});
}

export function getFormViews(dataDefinitionId) {
	return getItem(
		`/o/data-engine/v2.0/data-definitions/${dataDefinitionId}/data-layouts`,
		PARAMS
	).then(getItems);
}

function getItems({items}) {
	return items;
}

export function getTableViews(dataDefinitionId) {
	return getItem(
		`/o/data-engine/v2.0/data-definitions/${dataDefinitionId}/data-list-views`,
		PARAMS
	).then(getItems);
}

export function populateConfigData([
	app,
	appWorkflow,
	assigneeRoles,
	dataObjects,
	formViews,
	tableViews,
]) {
	const parseItem = (item = {}) => {
		return {
			...item,
			name: getTranslatedValue(item, 'name'),
		};
	};

	appWorkflow.appWorkflowTasks.forEach((task) => {
		task.appWorkflowDataLayoutLinks = task.appWorkflowDataLayoutLinks.map(
			(item) => {
				const {name, ...formView} = parseItem(
					formViews.find(({id}) => id === item.dataLayoutId)
				);

				return {...item, fields: getFormViewFields(formView), name};
			}
		);

		task.appWorkflowTransitions.sort(
			(actionA, actionB) => actionB.primary - actionA.primary
		);

		task.errors = {
			formViews: validateSelectedFormViews(
				task.appWorkflowDataLayoutLinks
			),
		};
	});

	const {appWorkflowStates = [], appWorkflowTasks = []} = appWorkflow;
	const initialState = appWorkflowStates.find(({initial}) => initial);
	const finalState = appWorkflowStates.find(({initial}) => !initial);

	const config = {
		currentStep: initialState,
		dataObject: parseItem(
			dataObjects.find(({id}) => id === app.dataDefinitionId)
		),
		formView: parseItem(formViews.find(({id}) => id === app.dataLayoutId)),
		listItems: {
			assigneeRoles,
			dataObjects,
			fetching: false,
			formViews,
			tableViews,
		},
		steps: [initialState, ...appWorkflowTasks, finalState],
		tableView: parseItem(
			tableViews.find(({id}) => id === app.dataListViewId)
		),
	};

	return [app, config];
}
