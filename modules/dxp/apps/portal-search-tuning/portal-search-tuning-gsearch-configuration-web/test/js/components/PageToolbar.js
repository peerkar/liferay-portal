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

import {fireEvent, render} from '@testing-library/react';
import React from 'react';

import PageToolbar from '../../../src/main/resources/META-INF/resources/js/components/PageToolbar';
import {AVAILABLE_LOCALES} from './../mocks/data';

import '@testing-library/jest-dom/extend-expect';

const onSubmit = jest.fn();

function renderPageToolbar(props) {
	return render(
		<PageToolbar
			availableLocales={AVAILABLE_LOCALES}
			initialTitle={{}}
			onCancel="/link"
			onSubmit={onSubmit}
			{...props}
		/>
	);
}

describe('PageToolbar', () => {
	it('renders the page toolbar form', () => {
		const {container} = renderPageToolbar();

		expect(container).not.toBeNull();
	});

	it('renders the title', () => {
		const initialTitle = {
			'en-US': 'Apple',
			'es-ES': 'Manzana',
		};

		const {getByText} = renderPageToolbar({
			initialTitle,
		});

		getByText(initialTitle['en-US']);
	});

	it('updates the input title', () => {
		const {getByPlaceholderText, getByText} = renderPageToolbar();

		fireEvent.change(getByPlaceholderText('untitled'), {
			target: {value: 'Test Title'},
		});

		getByText('Test Title');
	});

	it('offers link to cancel', () => {
		const {getByText} = renderPageToolbar();

		expect(getByText('cancel')).toHaveAttribute('href', '/link');
	});

	it('calls onSubmit when clicking on Save', () => {
		const {getByText} = renderPageToolbar();

		fireEvent.click(getByText('save'));

		expect(onSubmit).toHaveBeenCalled();
	});

	it('disables submit button when submitting', () => {
		const {getByText} = renderPageToolbar({isSubmitting: true});

		expect(getByText('save')).toBeDisabled();
	});
});
