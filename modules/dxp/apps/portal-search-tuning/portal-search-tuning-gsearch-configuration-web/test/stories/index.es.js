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

import {ClayIconSpriteContext} from '@clayui/icon';
import {
	STORYBOOK_CONSTANTS,
	StorybookAddonActions,
	StorybookAddonKnobs,
	StorybookReact,
} from 'liferay-npm-scripts/src/storybook';
import React from 'react';

import '../../src/main/resources/META-INF/resources/css/main.scss';

import ClayLayout from '@clayui/layout';

import Builder from '../../src/main/resources/META-INF/resources/js/components/Builder';
import ConfigurationSetForm from '../../src/main/resources/META-INF/resources/js/components/ConfigurationSetForm';
import Fragment from '../../src/main/resources/META-INF/resources/js/components/Fragment';
import PageToolbar from '../../src/main/resources/META-INF/resources/js/components/PageToolbar';
import Sidebar from '../../src/main/resources/META-INF/resources/js/components/Sidebar';
import {AVAILABLE_LOCALES, QUERY_FRAGMENTS} from './../js/mocks/data';

const {addDecorator, storiesOf} = StorybookReact;
const {action} = StorybookAddonActions;
const {withKnobs} = StorybookAddonKnobs;

addDecorator(withKnobs);

addDecorator((storyFn) => {
	const context = {
		namespace:
			'_com_liferay_portal_search_tuning_gsearch_configuration_web_internal_portlet_SearchConfigurationAdminPortlet_',
		spritemap: STORYBOOK_CONSTANTS.SPRITEMAP_PATH,
	};

	return (
		<ClayIconSpriteContext.Provider value={context.spritemap}>
			<div className="configuration-set-root">{storyFn()}</div>
		</ClayIconSpriteContext.Provider>
	);
});

const withBuilderClass = (storyFn) => (
	<div className="builder">{storyFn()}</div>
);

const withContainer = (storyFn) => (
	<ClayLayout.ContainerFluid size="md">{storyFn()}</ClayLayout.ContainerFluid>
);

storiesOf('Pages|ConfigurationSetForm', module).add('default', () => (
	<ConfigurationSetForm
		availableLocales={AVAILABLE_LOCALES}
		configurationId="0"
		configurationType={0}
		initialTitle={{
			'en-US': 'Test Title',
		}}
		redirectURL=""
		submitFormURL=""
	/>
));

storiesOf('Components|PageToolbar', module).add('PageToolbar', () => (
	<PageToolbar
		availableLocales={AVAILABLE_LOCALES}
		initialTitle={{
			'en-US': 'Test Title',
		}}
		onCancel=""
		onPublish={action('onPublish')}
	/>
));

storiesOf('Components|Sidebar', module).add('Sidebar', () => (
	<Sidebar
		addFragment={action('addFragment')}
		queryFragments={QUERY_FRAGMENTS}
	/>
));

storiesOf('Components|Builder', module)
	.addDecorator(withContainer)
	.add('Builder', () => (
		<Builder
			deleteFragment={action('buildFragment')}
			selectedFragments={QUERY_FRAGMENTS}
			updateFragments={action('updateFragments')}
		/>
	));

storiesOf('Components|Fragment', module)
	.addDecorator(withBuilderClass)
	.addDecorator(withContainer)
	.add('Fragment', () => (
		<Fragment
			deleteFragment={action('deleteFragment')}
			description={QUERY_FRAGMENTS[0].description}
			disabled={false}
			icon={QUERY_FRAGMENTS[0].icon}
			jsonString={QUERY_FRAGMENTS[0].jsonString}
			title={QUERY_FRAGMENTS[0].title}
		/>
	));
