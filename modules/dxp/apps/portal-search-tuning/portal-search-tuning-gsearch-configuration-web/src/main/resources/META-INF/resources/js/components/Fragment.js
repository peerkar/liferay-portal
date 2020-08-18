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
import ClayDropDown from '@clayui/drop-down';
import ClayIcon from '@clayui/icon';
import ClayList from '@clayui/list';
import ClaySticker from '@clayui/sticker';
import {PropTypes} from 'prop-types';
import React, {useContext, useEffect, useMemo, useState} from 'react';

import ThemeContext from '../ThemeContext';
import CodeMirrorEditor from './CodeMirrorEditor';

function Fragment({
	collapseAll,
	deleteFragment,
	description,
	disabled = false,
	icon,
	id,
	jsonString,
	title,
	updateJson,
}) {
	const {locale} = useContext(ThemeContext);

	const [active, setActive] = useState(false);
	const [collapse, setCollapse] = useState(collapseAll);

	useEffect(() => {
		setCollapse(collapseAll);
	}, [collapseAll]);

	function handleChange(value) {
		updateJson(value);
	}

	return (
		<div className="configuration-fragment sheet">
			{useMemo(() => {
				return (
					<ClayList>
						<ClayList.Item flex>
							<ClayList.ItemField>
								<ClaySticker
									className="icon"
									displayType="secondary"
								>
									<ClayIcon symbol={icon} />
								</ClaySticker>
							</ClayList.ItemField>

							<ClayList.ItemField expand>
								<ClayList.ItemTitle>
									{title[locale]}
								</ClayList.ItemTitle>

								<ClayList.ItemText subtext={true}>
									{description[locale]}
								</ClayList.ItemText>
							</ClayList.ItemField>

							<ClayDropDown
								active={active}
								alignmentPosition={3}
								onActiveChange={setActive}
								trigger={
									<ClayList.ItemField>
										<ClayButton
											aria-label={Liferay.Language.get(
												'dropdown'
											)}
											className="component-action"
											displayType="unstyled"
										>
											<ClayIcon symbol="ellipsis-v" />
										</ClayButton>
									</ClayList.ItemField>
								}
							>
								<ClayDropDown.ItemList>
									<ClayDropDown.Item
										disabled={disabled}
										onClick={() => deleteFragment(id)}
									>
										{Liferay.Language.get('delete')}
									</ClayDropDown.Item>
								</ClayDropDown.ItemList>
							</ClayDropDown>
							<ClayList.ItemField>
								<ClayButton
									aria-label={
										!collapse
											? Liferay.Language.get('collapse')
											: Liferay.Language.get('expand')
									}
									className="component-action"
									displayType="unstyled"
									onClick={() => {
										setCollapse(!collapse);
									}}
								>
									<ClayIcon
										symbol={
											!collapse
												? 'angle-down'
												: 'angle-right'
										}
									/>
								</ClayButton>
							</ClayList.ItemField>
						</ClayList.Item>
					</ClayList>
				);
			}, [
				active,
				collapse,
				deleteFragment,
				description,
				disabled,
				icon,
				id,
				locale,
				title,
			])}

			{!collapse && (
				<div className="configuration-editor">
					<CodeMirrorEditor
						onChange={handleChange}
						value={jsonString}
					/>
				</div>
			)}
		</div>
	);
}

Fragment.propTypes = {
	collapseAll: PropTypes.bool,
	deleteFragment: PropTypes.func,
	description: PropTypes.object,
	disabled: PropTypes.bool,
	icon: PropTypes.string,
	id: PropTypes.number,
	jsonString: PropTypes.string,
	title: PropTypes.object,
	updateJson: PropTypes.func,
};

export default React.memo(Fragment);
