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

import ClayButton from '@clayui/button';
import ClayLink from '@clayui/link';
import ClayLocalizedInput from '@clayui/localized-input';
import ClayManagementToolbar from '@clayui/management-toolbar';
import PropTypes from 'prop-types';
import React, {useContext, useState} from 'react';

import ThemeContext from '../ThemeContext';

export default function PageToolbar({
	availableLocales,
	initialTitle,
	isSubmitting,
	onCancel,
	onSubmit,
}) {
	const {namespace} = useContext(ThemeContext);

	const [selectedLocale, setSelectedLocale] = useState(availableLocales[0]);
	const [title, setTitle] = useState(initialTitle);

	const _renderLocalizedInputs = (inputId, translations) => {
		return Object.keys(translations).map((key) => (
			<input
				key={key}
				name={`${inputId}_${key.replace('-', '_')}`}
				type="hidden"
				value={translations[key]}
			/>
		));
	};

	const titleInputId = `${namespace}title`;

	return (
		<ClayManagementToolbar
			aria-label={Liferay.Language.get('save')}
			className="page-toolbar-root"
		>
			<ClayManagementToolbar.ItemList expand>
				<ClayManagementToolbar.Item className="localized-title">
					<ClayLocalizedInput
						aria-label={Liferay.Language.get('title')}
						className="form-control form-control-inline input-group-inset"
						id={titleInputId}
						label=""
						locales={availableLocales}
						onSelectedLocaleChange={setSelectedLocale}
						onTranslationsChange={setTitle}
						placeholder={Liferay.Language.get('untitled')}
						selectedLocale={selectedLocale}
						translations={title}
					/>

					{_renderLocalizedInputs(titleInputId, title)}
				</ClayManagementToolbar.Item>
			</ClayManagementToolbar.ItemList>

			<ClayManagementToolbar.ItemList>
				<ClayManagementToolbar.Item>
					<ClayLink
						displayType="secondary"
						href={onCancel}
						outline="secondary"
					>
						{Liferay.Language.get('cancel')}
					</ClayLink>
				</ClayManagementToolbar.Item>

				<ClayManagementToolbar.Item>
					<ClayButton
						disabled={isSubmitting}
						onClick={onSubmit}
						small
						type="submit"
					>
						{Liferay.Language.get('save')}
					</ClayButton>
				</ClayManagementToolbar.Item>
			</ClayManagementToolbar.ItemList>
		</ClayManagementToolbar>
	);
}

PageToolbar.propTypes = {
	availableLocales: PropTypes.arrayOf(PropTypes.object),
	initialTitle: PropTypes.object,
	isSubmitting: PropTypes.bool,
	onCancel: PropTypes.string.isRequired,
	onSubmit: PropTypes.func.isRequired,
};
