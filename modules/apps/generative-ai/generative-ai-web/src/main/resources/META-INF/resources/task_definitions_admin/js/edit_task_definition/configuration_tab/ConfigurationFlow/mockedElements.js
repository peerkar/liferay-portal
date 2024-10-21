/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import { NAME_LABELS } from '../../../utils/constants';

const MOCKED_ELEMENTS = {
    "chuck-vs-aristoteles": [
        { id: '1', type: 'input', data: { label: NAME_LABELS['chain'] }, position: { x: 0, y: 0 }, className: 'mocked-elements' },
        { id: 'e1-2', source: '1', target: '2' },
        { id: '2', type: 'default', data: { label: NAME_LABELS['gemini_chat_model'] }, position: { x: 0, y: 100 }, className: 'mocked-elements' },
        { id: 'e2-3', source: '2', target: '3' },
        { id: '3', type: 'default', data: { label: NAME_LABELS['task_context_parameter_agent'] }, position: { x: 0, y: 200 }, className: 'mocked-elements' },
        { id: 'e3-4', source: '3', target: '4' },
        { id: 'e3-5', source: '3', target: '5' },
        { id: '4', type: 'output', data: { label: NAME_LABELS['gemini_chat_model'] }, position: { x: -150, y: 300 }, className: 'mocked-elements' },
        { id: '5', type: 'output', data: { label: NAME_LABELS['gemini_chat_model'] }, position: { x: +150, y: 300 }, className: 'mocked-elements' },
    ]
}

const getMockedElements = (externalReferenceCode) => MOCKED_ELEMENTS[externalReferenceCode];

export default getMockedElements;
