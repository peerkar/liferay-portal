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

import Sidebar from '../../../src/main/resources/META-INF/resources/js/components/Sidebar';
import {QUERY_FRAGMENTS} from './../mocks/data';

import '@testing-library/jest-dom/extend-expect';

function renderSidebar(props) {
	return render(
		<Sidebar
			addFragment={jest.fn()}
			queryFragments={QUERY_FRAGMENTS}
			{...props}
		/>
	);
}

describe('Sidebar', () => {
	it('renders the sidebar', () => {
		const {container} = renderSidebar();

		expect(container).not.toBeNull();
	});

	it('renders the titles for the possible query fragments', () => {
		const {getByText} = renderSidebar();

		QUERY_FRAGMENTS.map((fragment) => getByText(fragment.title['en_US']));
	});

	it('renders the descriptions for the possible query fragments', () => {
		const {getByText} = renderSidebar();

		QUERY_FRAGMENTS.map((fragment) =>
			getByText(fragment.description['en_US'])
		);
	});

	it('renders the matching icons for the possible query fragments', () => {
		const {container} = renderSidebar();

		QUERY_FRAGMENTS.map((fragment) =>
			expect(
				container.querySelector(`.lexicon-icon-${fragment.icon}`)
			).toBeInTheDocument()
		);
	});

	it('renders the add button when mouseOver item', () => {
		const {container, getByLabelText, queryByLabelText} = renderSidebar();

		expect(queryByLabelText('add')).toBeNull();

		fireEvent.mouseOver(container.querySelectorAll('.list-group-title')[1]);

		getByLabelText('add');
	});
});
