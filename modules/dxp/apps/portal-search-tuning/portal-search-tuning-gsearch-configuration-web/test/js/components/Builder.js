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

import Builder from '../../../src/main/resources/META-INF/resources/js/components/Builder';
import {QUERY_FRAGMENTS} from './../mocks/data';

import '@testing-library/jest-dom/extend-expect';

jest.mock(
	'../../../src/main/resources/META-INF/resources/js/components/CodeMirrorEditor',
	() => ({onChange, value}) => (
		<textarea aria-label="text-area" onChange={onChange} value={value} />
	)
);

const updateFragment = jest.fn();

function renderBuilder(props) {
	return render(
		<Builder
			deleteFragment={jest.fn()}
			selectedFragments={QUERY_FRAGMENTS}
			updateFragment={updateFragment}
			{...props}
		/>
	);
}

describe('Builder', () => {
	it('renders the builder', () => {
		const {container} = renderBuilder();

		expect(container).not.toBeNull();
	});

	it('renders the titles for the selected query fragments', () => {
		const {getByText} = renderBuilder();

		QUERY_FRAGMENTS.map((fragment) => getByText(fragment.title['en_US']));
	});

	it('renders the description for the selected query fragments', () => {
		const {getByText} = renderBuilder();

		QUERY_FRAGMENTS.map((fragment) =>
			getByText(fragment.description['en_US'])
		);
	});

	it('renders the matching icons for the possible query fragments', () => {
		const {container} = renderBuilder();

		QUERY_FRAGMENTS.map((fragment) =>
			expect(
				container.querySelector(`.lexicon-icon-${fragment.icon}`)
			).toBeInTheDocument()
		);
	});

	it('can collapse all the query fragments', () => {
		const {container, getByText} = renderBuilder();

		fireEvent.click(getByText('collapse-all'));

		expect(container.querySelectorAll('.configuration-editor').length).toBe(
			0
		);
	});

	it('can expand all the query fragments', () => {
		const {container, getByText} = renderBuilder();

		fireEvent.click(getByText('collapse-all'));

		fireEvent.click(getByText('expand-all'));

		expect(container.querySelectorAll('.configuration-editor').length).toBe(
			3
		);
	});
});
