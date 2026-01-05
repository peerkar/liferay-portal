/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {expect, mergeTests} from '@playwright/test';

import {claySamplePageTest} from './fixtures/claySamplePageTest';
import {TabName} from './pages/ClaySamplePage';

const test = mergeTests(claySamplePageTest);

test.beforeEach(async ({claySamplePage}) => {
	await claySamplePage.selectTab(TabName.FORM_ELEMENTS);
});

test("Asserts Clay's select elements are working correctly", async ({page}) => {
	const getSingleSelect = (name: string) =>
		page.getByRole('combobox', {exact: true, name});

	await test.step('Single select: chooses option and updates value/text', async () => {
		const select = getSingleSelect('Regular Select Element');

		await expect(select).toBeEditable();

		await expect(select).toHaveValue('0');

		await expect(select).toContainText('Sample 0');

		await select.selectOption('Sample 1');

		await expect(select).toHaveValue('1');

		await expect(select).toContainText('Sample 1');
	});

	await test.step('Single select (disabled): is not editable', async () => {
		const select = getSingleSelect('Disabled Regular Select Element');

		await expect(select).toBeDisabled();

		await expect(select).not.toBeEditable();
	});

	const getMultipleSelect = (name: string) =>
		page.getByRole('listbox', {exact: true, name});

	await test.step('Multiple select: supports selecting multiple options', async () => {
		const select = getMultipleSelect('Multiple Select Element');

		await expect(select).toBeEditable();

		await select.selectOption(['Sample 1', 'Sample 2']);

		await expect(select).toHaveValues(['1', '2']);
	});

	await test.step('Multiple select (disabled): is not editable', async () => {
		const select = getMultipleSelect('Disabled Multiple Select Element');

		await expect(select).toBeDisabled();

		await expect(select).not.toBeEditable();
	});
});
