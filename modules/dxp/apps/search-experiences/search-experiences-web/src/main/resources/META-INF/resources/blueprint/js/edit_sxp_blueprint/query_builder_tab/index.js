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
import ClayIcon from '@clayui/icon';
import ClayLayout from '@clayui/layout';
import ClayPanel from '@clayui/panel';
import {ClayTooltipProvider} from '@clayui/tooltip';
import {PropTypes} from 'prop-types';
import React, {useState} from 'react';

import JSONElement from '../../shared/JSONElement';
import Element from '../../shared/element/index';
import {ELEMENT_PREFIX} from '../../utils/constants';
import SelectAssetTypes from './SelectAssetTypes';

function QueryBuilderTab({
	entityJSON,
	errors = [],
	frameworkConfig,
	indexFields,
	isSubmitting,
	onBlur,
	onChange,
	onDeleteElement,
	onFrameworkConfigChange,
	onToggleSidebar,
	searchableAssetTypes,
	selectedElements,
	setFieldTouched,
	setFieldValue,
	touched = [],
}) {
	const [collapseAll, setCollapseAll] = useState(false);

	const _renderSelectedElements = () => (
		<>
			{selectedElements.map((element, index) => {
				return element.uiConfigurationJSON ? (
					<Element
						collapseAll={collapseAll}
						elementTemplateJSON={element.elementTemplateJSON}
						entityJSON={entityJSON}
						error={errors[index]}
						id={element.id}
						index={index}
						indexFields={indexFields}
						isSubmitting={isSubmitting}
						key={element.id}
						onBlur={onBlur}
						onChange={onChange}
						onDeleteElement={onDeleteElement}
						prefixedId={`${ELEMENT_PREFIX.QUERY}-${index}`}
						setFieldTouched={setFieldTouched}
						setFieldValue={setFieldValue}
						touched={touched[index]}
						uiConfigurationJSON={element.uiConfigurationJSON}
						uiConfigurationValues={element.uiConfigurationValues}
					/>
				) : (
					<JSONElement
						collapseAll={collapseAll}
						elementTemplateJSON={element.elementTemplateJSON}
						error={errors[index]}
						id={element.id}
						index={index}
						isSubmitting={isSubmitting}
						key={element.id}
						onDeleteElement={onDeleteElement}
						prefixedId={`${ELEMENT_PREFIX.QUERY}-${index}`}
						setFieldTouched={setFieldTouched}
						setFieldValue={setFieldValue}
						touched={touched[index]}
						uiConfigurationValues={element.uiConfigurationValues}
					/>
				);
			})}
		</>
	);

	return (
		<ClayLayout.ContainerFluid
			className="builder query-builder-tab"
			size="xl"
		>
			<div className="builder-content-shift">
				<ClayLayout.Row
					className="configuration-header"
					justify="between"
				>
					<ClayLayout.Col size={6}>
						{Liferay.Language.get('query-builder')}
					</ClayLayout.Col>

					<ClayLayout.Col size={6}>
						<div className="builder-actions">
							<ClayButton
								aria-label={Liferay.Language.get(
									'collapse-all'
								)}
								className="collapse-button"
								displayType="unstyled"
								onClick={() => setCollapseAll(!collapseAll)}
							>
								{collapseAll
									? Liferay.Language.get('expand-all')
									: Liferay.Language.get('collapse-all')}
							</ClayButton>

							<ClayTooltipProvider>
								<ClayButton
									aria-label={Liferay.Language.get(
										'add-query-element'
									)}
									displayType="primary"
									monospaced
									onClick={onToggleSidebar}
									small
									title={Liferay.Language.get(
										'add-query-element'
									)}
								>
									<ClayIcon symbol="plus" />
								</ClayButton>
							</ClayTooltipProvider>
						</div>
					</ClayLayout.Col>
				</ClayLayout.Row>

				{selectedElements.length === 0 ? (
					<div className="sheet">
						<div className="selected-elements-empty-text">
							{Liferay.Language.get(
								'add-elements-to-optimize-search-results-for-your-use-cases'
							)}
						</div>
					</div>
				) : (
					_renderSelectedElements()
				)}

				<ClayLayout.Row
					className="configuration-header configuration-header-settings"
					justify="between"
				>
					<ClayLayout.Col size={12}>
						{Liferay.Language.get('query-settings')}
					</ClayLayout.Col>
				</ClayLayout.Row>

				<div className="settings-content-container sheet">
					<ClayPanel.Group flush>
						<ClayPanel
							className="searchable-asset-types"
							collapsable
							displayTitle={Liferay.Language.get(
								'searchable-asset-types'
							)}
							displayType="unstyled"
							showCollapseIcon
						>
							<ClayPanel.Body>
								<div className="sheet-text">
									{Liferay.Language.get(
										'select-the-searchable-asset-types-description'
									)}
								</div>

								<SelectAssetTypes
									onFrameworkConfigChange={
										onFrameworkConfigChange
									}
									searchableAssetTypes={searchableAssetTypes}
									selectedAssetTypes={
										frameworkConfig.searchable_asset_types
									}
								/>
							</ClayPanel.Body>
						</ClayPanel>
					</ClayPanel.Group>
				</div>
			</div>
		</ClayLayout.ContainerFluid>
	);
}

QueryBuilderTab.propTypes = {
	entityJSON: PropTypes.object,
	errors: PropTypes.arrayOf(PropTypes.object),
	frameworkConfig: PropTypes.object,
	indexFields: PropTypes.arrayOf(PropTypes.object),
	isSubmitting: PropTypes.bool,
	onBlur: PropTypes.func,
	onChange: PropTypes.func,
	onDeleteElement: PropTypes.func,
	onFrameworkConfigChange: PropTypes.func,
	onToggleSidebar: PropTypes.func,
	searchableAssetTypes: PropTypes.arrayOf(PropTypes.object),
	selectedElements: PropTypes.arrayOf(PropTypes.object),
	setFieldTouched: PropTypes.func,
	setFieldValue: PropTypes.func,
	touched: PropTypes.arrayOf(PropTypes.object),
};

export default React.memo(QueryBuilderTab);
