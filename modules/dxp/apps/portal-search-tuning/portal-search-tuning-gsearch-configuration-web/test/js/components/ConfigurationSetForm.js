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

import {fireEvent, render, within} from '@testing-library/react';
import React from 'react';

import ConfigurationSetForm from '../../../src/main/resources/META-INF/resources/js/components/ConfigurationSetForm';
import {AVAILABLE_LOCALES} from './../mocks/data';

import '@testing-library/jest-dom/extend-expect';

jest.mock(
	'../../../src/main/resources/META-INF/resources/js/components/CodeMirrorEditor',
	() => ({onChange, value}) => (
		<textarea aria-label="text-area" onChange={onChange} value={value} />
	)
);

function renderConfigurationSetForm(props) {
	return render(
		<ConfigurationSetForm
			availableLocales={AVAILABLE_LOCALES}
			configurationId="0"
			configurationType={0}
			initialTitle={{
				'en-US': 'Test Title',
			}}
			redirectURL=""
			submitFormURL=""
			{...props}
		/>
	);
}

describe('ConfigurationSetForm', () => {
	it('renders the configuration set form', () => {
		const {container} = renderConfigurationSetForm();

		expect(container).not.toBeNull();
	});

	it('renders the default query fragment', () => {
		const {container} = renderConfigurationSetForm();

		const {getByText} = within(container.querySelector('.builder'));

		getByText('Matches any keyword');
	});

	it('disables removal of default query fragment from sidebar', () => {
		const {getByLabelText, getByText} = renderConfigurationSetForm();

		fireEvent.click(getByLabelText('dropdown'));

		expect(getByText('delete')).toBeDisabled();
	});

	it('adds additional query fragment from sidebar', () => {
		const {container, getByLabelText} = renderConfigurationSetForm();

		fireEvent.mouseOver(container.querySelectorAll('.list-group-title')[1]);

		fireEvent.click(getByLabelText('add'));

		const {getByText} = within(container.querySelector('.builder'));

		getByText('Freshness');
	});

	it('enables removal of additional query fragments', () => {
		const {
			container,
			getAllByLabelText,
			getAllByText,
			getByLabelText,
		} = renderConfigurationSetForm();

		fireEvent.mouseOver(container.querySelectorAll('.list-group-title')[1]);

		fireEvent.click(getByLabelText('add'));

		fireEvent.click(getAllByLabelText('dropdown')[0]);

		fireEvent.click(getAllByText('delete')[1]);

		const {queryByText} = within(container.querySelector('.builder'));

		expect(queryByText('Freshness')).toBeNull();
	});
});
