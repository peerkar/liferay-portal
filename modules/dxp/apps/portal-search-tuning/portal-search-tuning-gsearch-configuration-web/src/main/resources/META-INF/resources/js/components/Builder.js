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
import ClayLayout from '@clayui/layout';
import {PropTypes} from 'prop-types';
import React, {useState} from 'react';

import Fragment from './Fragment';

function Builder({deleteFragment, selectedFragments, updateFragment}) {
	const [collapseAll, setCollapseAll] = useState(false);

	return (
		<ClayLayout.ContainerFluid className="builder" size="md">
			<ClayLayout.Row
				className="bold configuration-header"
				justify="between"
			>
				<ClayLayout.Col size={4}>
					{Liferay.Language.get('builder')}
				</ClayLayout.Col>
				<ClayLayout.Col size={3}>
					<ClayButton
						aria-label={Liferay.Language.get('collapse-all')}
						displayType="unstyled"
						onClick={() => setCollapseAll(!collapseAll)}
					>
						{collapseAll
							? Liferay.Language.get('expand-all')
							: Liferay.Language.get('collapse-all')}
					</ClayButton>
				</ClayLayout.Col>
			</ClayLayout.Row>

			{selectedFragments.map((item, index) => {
				return (
					<Fragment
						collapseAll={collapseAll}
						deleteFragment={deleteFragment}
						description={item.description}
						disabled={item.id === 0}
						icon={item.icon}
						id={item.id}
						jsonString={item.jsonString}
						key={item.id}
						title={item.title}
						updateJson={(jsonString) =>
							updateFragment(index, {...item, jsonString})
						}
					/>
				);
			})}
		</ClayLayout.ContainerFluid>
	);
}

Builder.propTypes = {
	deleteFragment: PropTypes.func,
	selectedFragments: PropTypes.arrayOf(PropTypes.object),
	updateFragment: PropTypes.func,
};

export default React.memo(Builder);
