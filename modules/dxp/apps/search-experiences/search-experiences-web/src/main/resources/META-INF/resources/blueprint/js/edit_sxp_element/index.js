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

import ClayAlert from '@clayui/alert';
import ClayButton from '@clayui/button';
import ClayEmptyState from '@clayui/empty-state';
import ClayIcon from '@clayui/icon';
import ClayLayout from '@clayui/layout';
import ClayLink from '@clayui/link';
import ClaySticker from '@clayui/sticker';
import ClayToolbar from '@clayui/toolbar';
import {ClayTooltipProvider} from '@clayui/tooltip';
import getCN from 'classnames';
import {fetch, navigate} from 'frontend-js-web';
import {PropTypes} from 'prop-types';
import React, {
	useCallback,
	useContext,
	useEffect,
	useRef,
	useState,
} from 'react';

import CodeMirrorEditor from '../shared/CodeMirrorEditor';
import ErrorBoundary from '../shared/ErrorBoundary';
import PreviewModal from '../shared/PreviewModal';
import SearchInput from '../shared/SearchInput';
import SubmitWarningModal from '../shared/SubmitWarningModal';
import ThemeContext from '../shared/ThemeContext';
import Element from '../shared/element/index';
import {CONFIG_PREFIX} from '../utils/constants';
import {renameKeys, sub} from '../utils/language';
import {openErrorToast} from '../utils/toasts';
import {getUIConfigurationValues} from '../utils/utils';
import SidebarPanel from './SidebarPanel';

function EditElementForm({
	elementId,
	initialConfigurationString,
	initialDescription,
	initialTitle,
	predefinedVariables,
	readOnly,
	redirectURL,
	submitFormURL,
	type,
	validateElementURL,
}) {
	const {defaultLocale, namespace} = useContext(ThemeContext);

	const initialConfiguration = JSON.parse(initialConfigurationString);

	const form = useRef();
	const elementTemplateJSONRef = useRef();

	const [errors, setErrors] = useState([]);
	const [isSubmitting, setIsSubmitting] = useState(false);
	const [showSidebar, setShowSidebar] = useState(false);
	const [showSubmitWarningModal, setShowSubmitWarningModal] = useState(false);
	const [expandAllVariables, setExpandAllVariables] = useState(false);

	const [variables, setVariables] = useState(predefinedVariables);

	initialConfiguration.elementTemplateJSON.title = renameKeys(
		initialTitle,
		(str) => str.replace('-', '_')
	);
	initialConfiguration.elementTemplateJSON.description = renameKeys(
		initialDescription,
		(str) => str.replace('-', '_')
	);

	const [elementTemplateJSON, setElementTemplateJSON] = useState(
		JSON.stringify(initialConfiguration.elementTemplateJSON, null, '\t')
	);
	const [uiConfigurationJSON, setUIConfigurationJSON] = useState(
		JSON.stringify(initialConfiguration.uiConfigurationJSON, null, '\t')
	);

	useEffect(() => {

		// Workaround to force a re-render so `elementTemplateJSONRef` will be
		// defined when calling `_handleVariableClick`

		if (!readOnly) {
			setShowSidebar(true);
		}
	}, [readOnly]);

	function _appendEntryLocale(entry, name, formData) {
		if (typeof entry == 'object') {
			return Object.keys(entry).map((key) => {
				formData.append(
					`${namespace}${name}_${key.replace('-', '_')}`,
					entry[key]
				);
			});
		}
		else if (typeof entry == 'string') {
			formData.append(
				`${namespace}${name}_${defaultLocale.replace('-', '_')}`,
				entry
			);
		}
	}

	const _handleSearchFilter = useCallback(
		(value) => {
			const filteredParameterDefinitions = predefinedVariables.map(
				(category) => ({
					...category,
					parameterDefinitions: category.parameterDefinitions.filter(
						({description = ''}) =>
							description
								.toLowerCase()
								.includes(value.toLowerCase())
					),
				})
			);

			const filteredCategories = filteredParameterDefinitions.filter(
				({parameterDefinitions}) => parameterDefinitions.length
			);

			setVariables(filteredCategories);
			setExpandAllVariables(!!value);
		},
		[predefinedVariables]
	);

	const _handleSubmit = async (event) => {
		event.preventDefault();

		setIsSubmitting(true);

		const formData = new FormData(form.current);

		try {
			const parseElementTemplateJSON = _validateJSON(
				elementTemplateJSON,
				Liferay.Language.get('element-template-json')
			);
			const parseUIConfigurationJSON = _validateJSON(
				uiConfigurationJSON,
				Liferay.Language.get('ui-configuration-json')
			);

			_validateConfigKeys(elementTemplateJSON, parseUIConfigurationJSON);

			if (!parseElementTemplateJSON.title) {
				throw Liferay.Language.get('error.title-empty');
			}

			if (
				typeof parseElementTemplateJSON.title === 'object' &&
				!parseElementTemplateJSON.title[defaultLocale]
			) {
				throw Liferay.Language.get('error.default-locale-title-empty');
			}

			_appendEntryLocale(
				parseElementTemplateJSON.title,
				'title',
				formData
			);
			_appendEntryLocale(
				parseElementTemplateJSON.description,
				'description',
				formData
			);

			formData.append(
				`${namespace}configuration`,
				JSON.stringify({
					elementTemplateJSON: parseElementTemplateJSON,
					uiConfigurationJSON: parseUIConfigurationJSON,
				})
			);
		}
		catch (error) {
			openErrorToast({
				message: error,
			});

			setIsSubmitting(false);

			return;
		}

		formData.append(`${namespace}elementId`, elementId);
		formData.append(`${namespace}redirect`, redirectURL);
		formData.append(`${namespace}elementType`, type);

		try {

			// If the warning modal is already open, assume the form was submitted
			// using the "Continue To Save" action and should skip the schema
			// validation step.

			if (!showSubmitWarningModal) {
				const validateErrors = await fetch(validateElementURL, {
					body: formData,
					method: 'POST',
				}).then((response) => response.json());

				if (validateErrors.errors?.length) {
					setErrors(validateErrors.errors);
					setShowSubmitWarningModal(true);
					setIsSubmitting(false);

					return;
				}
			}

			const responseContent = await fetch(submitFormURL, {
				body: formData,
				method: 'POST',
			}).then((response) => response.json());

			if (
				Object.prototype.hasOwnProperty.call(responseContent, 'errors')
			) {
				responseContent.errors.forEach((message) =>
					openErrorToast({message})
				);

				setIsSubmitting(false);
			}
			else {
				navigate(redirectURL);
			}
		}
		catch (error) {
			openErrorToast();

			if (process.env.NODE_ENV === 'development') {
				console.error(error);
			}
		}

		return fetch(submitFormURL, {
			body: formData,
			method: 'POST',
		})
			.then((response) => response.json())
			.then((responseContent) => {
				if (
					Object.prototype.hasOwnProperty.call(
						responseContent,
						'errors'
					)
				) {
					responseContent.errors.forEach((message) =>
						openErrorToast({message})
					);

					setIsSubmitting(false);
				}
				else {
					navigate(redirectURL);
				}
			})
			.catch(() => {
				openErrorToast();

				setIsSubmitting(false);
			});
	};

	function _handleVariableClick(variable) {
		const doc = elementTemplateJSONRef.current.getDoc();
		const cursor = doc.getCursor();

		doc.replaceRange(variable, cursor);
	}

	function _renderPreviewBody() {
		let previewElementTemplateJSON = {};
		let previewUIConfigurationJSON = {};

		try {
			previewElementTemplateJSON = JSON.parse(elementTemplateJSON);
			previewUIConfigurationJSON = JSON.parse(uiConfigurationJSON);
		}
		catch (error) {
			return (
				<ClayEmptyState
					description={Liferay.Language.get(
						'json-may-be-incorrect-and-we-were-unable-to-load-a-preview-of-the-configuration'
					)}
					imgSrc="/o/admin-theme/images/states/empty_state.gif"
					title={Liferay.Language.get('unable-to-load-preview')}
				/>
			);
		}

		return (
			<div className="portlet-blueprints-admin">
				<ErrorBoundary>
					<Element
						collapseAll={false}
						elementTemplateJSON={previewElementTemplateJSON}
						uiConfigurationJSON={previewUIConfigurationJSON}
						uiConfigurationValues={getUIConfigurationValues(
							previewUIConfigurationJSON
						)}
					/>
				</ErrorBoundary>
			</div>
		);
	}

	const _validateConfigKeys = (
		elementTemplateJSON,
		parseUIConfigurationJSON
	) => {
		const regex = new RegExp(`\\$\\{${CONFIG_PREFIX}.([\\w\\d_]+)\\}`, 'g');

		const elementKeys = [...elementTemplateJSON.matchAll(regex)].map(
			(item) => item[1]
		);

		const uiConfigKeys = parseUIConfigurationJSON.fieldSets
			? parseUIConfigurationJSON.fieldSets.reduce((acc, curr) => {

					// Find names within each fields array

					const configKeys = curr.fields
						? curr.fields.map((item) => item.name)
						: [];

					return [...acc, ...configKeys];
			  }, [])
			: [];

		const missingKeys = elementKeys.filter(
			(item) => !uiConfigKeys.includes(item)
		);

		if (missingKeys.length > 0) {
			throw sub(
				Liferay.Language.get(
					'the-following-configuration-key-is-missing-x'
				),
				[missingKeys.join(', ')]
			);
		}
	};

	const _validateJSON = (text, name) => {
		try {
			return JSON.parse(text);
		}
		catch {
			throw sub(Liferay.Language.get('x-is-invalid'), [name]);
		}
	};

	return (
		<>
			<form ref={form}>
				<SubmitWarningModal
					errors={errors}
					isSubmitting={isSubmitting}
					message={Liferay.Language.get(
						'the-element-configuration-has-errors-that-may-cause-unexpected-results'
					)}
					onClose={() => setShowSubmitWarningModal(false)}
					onSubmit={_handleSubmit}
					visible={showSubmitWarningModal}
				/>

				<div className="page-toolbar-root">
					<ClayToolbar light>
						<ClayLayout.ContainerFluid>
							<ClayToolbar.Nav>
								<ClayToolbar.Item className="text-left" expand>
									<div>
										<div className="entry-title text-truncate">
											{
												initialTitle[
													defaultLocale.replace(
														'_',
														'-'
													)
												]
											}
										</div>

										<div className="entry-description text-truncate">
											{initialDescription[
												defaultLocale.replace('_', '-')
											] || (
												<span className="entry-description-blank">
													{Liferay.Language.get(
														'no-description'
													)}
												</span>
											)}
										</div>
									</div>
								</ClayToolbar.Item>

								{readOnly && (
									<ClayToolbar.Item>
										<ClayAlert
											className="m-0"
											displayType="info"
											title={Liferay.Language.get(
												'read-only'
											)}
											variant="feedback"
										/>
									</ClayToolbar.Item>
								)}

								<ClayToolbar.Item>
									<PreviewModal
										body={_renderPreviewBody()}
										size="lg"
										title={Liferay.Language.get(
											'preview-configuration'
										)}
									>
										<ClayButton
											borderless
											displayType="secondary"
											small
										>
											{Liferay.Language.get('preview')}
										</ClayButton>
									</PreviewModal>
								</ClayToolbar.Item>

								<ClayToolbar.Item>
									<div className="tbar-divider" />
								</ClayToolbar.Item>

								{readOnly ? (
									<ClayToolbar.Item>
										<ClayLink
											displayType="secondary"
											href={redirectURL}
											outline="secondary"
										>
											{Liferay.Language.get('close')}
										</ClayLink>
									</ClayToolbar.Item>
								) : (
									<>
										<ClayToolbar.Item>
											<ClayLink
												displayType="secondary"
												href={redirectURL}
												outline="secondary"
											>
												{Liferay.Language.get('cancel')}
											</ClayLink>
										</ClayToolbar.Item>

										<ClayToolbar.Item>
											<ClayButton
												disabled={isSubmitting}
												onClick={_handleSubmit}
												small
												type="submit"
											>
												{Liferay.Language.get('save')}
											</ClayButton>
										</ClayToolbar.Item>
									</>
								)}
							</ClayToolbar.Nav>
						</ClayLayout.ContainerFluid>
					</ClayToolbar>
				</div>
			</form>

			<div className="element-row">
				<ClayLayout.Row>
					<ClayLayout.Col size={8}>
						<div className="element-section">
							<div className="element-header">
								{!readOnly && (
									<ClayButton
										borderless
										className={showSidebar && 'active'}
										disabled={false}
										displayType="secondary"
										monospaced
										onClick={() =>
											setShowSidebar(!showSidebar)
										}
										small
										title={Liferay.Language.get(
											'predefined-variables'
										)}
										type="submit"
									>
										<ClayIcon symbol="list-ul" />
									</ClayButton>
								)}

								<div className="expand-header">
									<div className="header-label">
										<label>
											{Liferay.Language.get(
												'element-template-json'
											)}

											<ClayTooltipProvider>
												<ClaySticker
													displayType="unstyled"
													size="sm"
													title={Liferay.Language.get(
														'element-template-json-help'
													)}
												>
													<ClayIcon
														data-tooltip-align="top"
														symbol="info-circle"
													/>
												</ClaySticker>
											</ClayTooltipProvider>
										</label>
									</div>
								</div>
							</div>

							<ClayLayout.Row>
								<ClayLayout.Col
									className={getCN('json-section', {
										hide: !showSidebar,
									})}
									size={3}
								>
									<div className="sidebar sidebar-light">
										<div className="sidebar-header">
											<span className="text-truncate-inline">
												<span className="text-truncate">
													{Liferay.Language.get(
														'predefined-variables'
													)}
												</span>
											</span>
										</div>

										<div className="container-fluid sidebar-input">
											<SearchInput
												onChange={_handleSearchFilter}
											/>
										</div>

										<div className="container-fluid">
											<dl className="sidebar-dl">
												{variables.length ? (
													variables
														.filter(
															(item) =>
																item
																	.parameterDefinitions
																	.length
														)
														.map((item) => (
															<SidebarPanel
																categoryName={
																	item.categoryName
																}
																expand={
																	expandAllVariables
																}
																item={item}
																key={
																	item.categoryName
																}
																onVariableClick={
																	_handleVariableClick
																}
																parameterDefinitions={
																	item.parameterDefinitions
																}
															/>
														))
												) : (
													<div className="empty-list-message">
														<ClayEmptyState
															description=""
															title={Liferay.Language.get(
																'no-predefined-variables-found'
															)}
														/>
													</div>
												)}
											</dl>
										</div>
									</div>
								</ClayLayout.Col>

								<ClayLayout.Col
									className="json-section"
									size={showSidebar ? 9 : 12}
								>
									<CodeMirrorEditor
										onChange={(value) =>
											setElementTemplateJSON(value)
										}
										readOnly={readOnly}
										ref={elementTemplateJSONRef}
										value={elementTemplateJSON}
									/>
								</ClayLayout.Col>
							</ClayLayout.Row>
						</div>
					</ClayLayout.Col>

					<ClayLayout.Col size={4}>
						<div className="element-section">
							<div className="element-header">
								<div className="expand-header">
									<div className="header-label">
										<label>
											{Liferay.Language.get(
												'ui-configuration-json'
											)}
											<ClayTooltipProvider>
												<ClaySticker
													displayType="unstyled"
													size="sm"
													title={Liferay.Language.get(
														'ui-configuration-json-help'
													)}
												>
													<ClayIcon
														data-tooltip-align="top"
														symbol="info-circle"
													/>
												</ClaySticker>
											</ClayTooltipProvider>
										</label>
									</div>
								</div>
							</div>

							<div className="json-section">
								<CodeMirrorEditor
									onChange={(value) =>
										setUIConfigurationJSON(value)
									}
									readOnly={readOnly}
									value={uiConfigurationJSON}
								/>
							</div>
						</div>
					</ClayLayout.Col>
				</ClayLayout.Row>
			</div>
		</>
	);
}

EditElementForm.propTypes = {
	elementId: PropTypes.string,
	initialConfigurationString: PropTypes.string,
	initialDescription: PropTypes.object,
	initialTitle: PropTypes.object,
	predefinedVariables: PropTypes.arrayOf(PropTypes.object),
	redirectURL: PropTypes.string,
	submitFormURL: PropTypes.string,
	type: PropTypes.number,
};

export default function ({context, props}) {
	return (
		<ThemeContext.Provider value={context}>
			<div className="edit-element-root">
				<ErrorBoundary>
					<EditElementForm {...props} />
				</ErrorBoundary>
			</div>
		</ThemeContext.Provider>
	);
}
