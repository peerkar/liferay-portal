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

import Fragment from '../../../src/main/resources/META-INF/resources/js/components/Fragment';
import {QUERY_FRAGMENTS} from './../mocks/data';

import '@testing-library/jest-dom/extend-expect';

jest.mock(
	'../../../src/main/resources/META-INF/resources/js/components/CodeMirrorEditor',
	() => ({onChange, value}) => (
		<textarea aria-label="text-area" onChange={onChange} value={value} />
	)
);

const deleteFragment = jest.fn();
const updateJson = jest.fn();

function renderFragment(props) {
	return render(
		<Fragment
			collapseAll={false}
			deleteFragment={deleteFragment}
			description={QUERY_FRAGMENTS[0].description}
			disabled={false}
			icon={QUERY_FRAGMENTS[0].icon}
			id={QUERY_FRAGMENTS[0].id}
			jsonString={QUERY_FRAGMENTS[0].jsonString}
			title={QUERY_FRAGMENTS[0].title}
			updateJson={updateJson}
			{...props}
		/>
	);
}

describe('Fragment', () => {
	it('renders the fragment', () => {
		const {container} = renderFragment();

		expect(container).not.toBeNull();
	});

	it('displays the title', () => {
		const {getByText} = renderFragment();

		getByText(QUERY_FRAGMENTS[0].title['en_US']);
	});

	it('displays the description', () => {
		const {getByText} = renderFragment();

		getByText(QUERY_FRAGMENTS[0].description['en_US']);
	});

	it('displays the matching icon', () => {
		const {container} = renderFragment();

		expect(
			container.querySelector(`.lexicon-icon-${QUERY_FRAGMENTS[0].icon}`)
		).toBeInTheDocument();
	});

	it('can collapse the query fragments', () => {
		const {container, getByLabelText} = renderFragment();

		fireEvent.click(getByLabelText('collapse'));

		expect(container.querySelector('.configuration-editor')).toBeNull();
	});

	it('calls updateJson when typing in the editor', () => {
		const {getByLabelText} = renderFragment();

		fireEvent.change(getByLabelText('text-area'), {
			target: {value: 'test'},
		});

		expect(updateJson).toHaveBeenCalled();
	});

	it('calls deleteFragment when clicking on delete from dropdown', () => {
		const {getByLabelText, getByText} = renderFragment();

		fireEvent.click(getByLabelText('dropdown'));

		fireEvent.click(getByText('delete'));

		expect(deleteFragment).toHaveBeenCalled();
	});
});
