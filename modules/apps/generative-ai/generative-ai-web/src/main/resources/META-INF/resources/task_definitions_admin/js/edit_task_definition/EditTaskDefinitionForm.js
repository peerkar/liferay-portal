/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import ClayButton from '@clayui/button';
import ClayToolbar from '@clayui/toolbar';
import getCN from 'classnames';
import {setNestedObjectValues, useFormik} from 'formik';
import {fetch, navigate} from 'frontend-js-web';
import {PropTypes} from 'prop-types';
import React, {
	useCallback,
	useContext,
	useEffect,
	useRef,
	useState,
} from 'react';

import useShouldConfirmBeforeNavigate from '../hooks/useShouldConfirmBeforeNavigate';
import PageToolbar from '../shared/PageToolbar';
import SubmitWarningModal from '../shared/SubmitWarningModal';
import ThemeContext from '../shared/ThemeContext';
import {DEFAULT_INDEX_CONFIGURATION} from '../utils/constants';
import {DEFAULT_ERROR} from '../utils/errorMessages';
import fetchData, {DEFAULT_HEADERS} from '../utils/fetch/fetch_data';
import isDefined from '../utils/functions/is_defined';
import formatLocaleWithUnderscores from '../utils/language/format_locale_with_underscores';
import renameKeys from '../utils/language/rename_keys';
import {
	SIDEBAR_STATE,
	setStorageAddSXPElementSidebar,
} from '../utils/sessionStorage';
import transformToSearchPreviewHits from '../utils/sxp_element/transform_to_search_preview_hits';
import {TEST_IDS} from '../utils/testIds';
import {
	openErrorToast,
	openSuccessToast,
	setInitialSuccessToast,
} from '../utils/toasts';
import {INPUT_TYPES} from '../utils/types/inputTypes';
import {SIDEBAR_TYPES} from '../utils/types/sidebarTypes';
import validateBoost from '../utils/validation/validate_boost';
import validateJSON from '../utils/validation/validate_json';
import validateNumberRange from '../utils/validation/validate_number_range';
import validateRequired from '../utils/validation/validate_required';
import ConfigurationTab from './configuration_tab/index';
import PreviewSidebar from './preview_sidebar/index';

// Tabs in display order

/* eslint-disable sort-keys */
const TABS = {
	configuration: Liferay.Language.get('configuration'),
};

/* eslint-enable sort-keys */

function EditTaskDefinitionForm({
	entityJSON,
	initialConfiguration = {},
	initialDescription = '',
	initialDescriptionI18n = {},
	initialExternalReferenceCode,
	initialTitle = '',
	initialTitleI18n = {},
	taskDefinitionId,
}) {
	const {isCompanyAdmin, locale, redirectURL} = useContext(ThemeContext);

	const formRef = useRef();

	const controllerRef = useRef();

	const [errors, setErrors] = useState([]);
	const [isTitleAndDescriptionEdited, setIsTitleAndDescriptionEdited] =
		useState(false);
	const [previewInfo, setPreviewInfo] = useState(() => ({
		loading: false,
		results: {},
	}));
	const [openSidebar, setOpenSidebar] = useState(
		SIDEBAR_TYPES.ADD_SXP_ELEMENT
	);
	const [showSubmitWarningModal, setShowSubmitWarningModal] = useState(false);
	const [tab, setTab] = useState('configuration');

	/**
	 * This method must go before the useFormik hook.
	 */
	const _handleFormikSubmit = async (values) => {
		let configuration;

		try {
			configuration = _getConfiguration(values);
		}
		catch (error) {
			openErrorToast({
				message: Liferay.Language.get(
					'the-configuration-has-missing-or-invalid-values'
				),
			});

			if (process.env.NODE_ENV === 'development') {
				console.error(error);
			}

			return;
		}

		try {

			// If the warning modal is already open, assume the form was submitted
			// using the "Continue To Save" action and should skip the schema
			// validation step.

			if (!showSubmitWarningModal) {
				const validateErrors = {errors: []};

				if (validateErrors.errors?.length) {
					setErrors(validateErrors.errors);
					setShowSubmitWarningModal(true);

					return;
				}
			}

			const responseContent = await fetch(
				`/o/generative-ai/v1.0/task-definitions/${taskDefinitionId}`,
				{
					body: JSON.stringify({
						configuration,
						description_i18n: renameKeys(
							formik.values.description_i18n,
							formatLocaleWithUnderscores
						),
						externalReferenceCode:
							formik.values.externalReferenceCode,
						title_i18n: renameKeys(
							formik.values.title_i18n,
							formatLocaleWithUnderscores
						),
					}),
					headers: DEFAULT_HEADERS,
					method: 'PUT',
				}
			).then((response) => {
				if (!response.ok) {
					setShowSubmitWarningModal(false);

					throw DEFAULT_ERROR;
				}

				return response.json();
			});

			if (
				Object.prototype.hasOwnProperty.call(responseContent, 'errors')
			) {
				responseContent.errors.forEach((message) =>
					openErrorToast({message})
				);
			}
			else {
				openSuccessToast();
			}
		}
		catch (error) {
			openErrorToast();

			if (process.env.NODE_ENV === 'development') {
				console.error(error);
			}
		}
	};

	/**
	 * This method must go before the useFormik hook.
	 */
	const _handleFormikValidate = (values) => {
		const errors = {};

		['taskConfig'].map((configName) => {
			const configError = validateJSON(
				values[configName],
				INPUT_TYPES.JSON
			);

			if (configError) {
				errors[configName] = configError;
			}
		});

		return errors;
	};

	const formik = useFormik({
		initialValues: {
			taskConfig: JSON.stringify(initialConfiguration, null, '\t'),
			description_i18n: initialDescriptionI18n,
			externalReferenceCode: initialExternalReferenceCode,
			title_i18n: initialTitleI18n,
		},
		onSubmit: _handleFormikSubmit,
		validate: _handleFormikValidate,
	});

	// useShouldConfirmBeforeNavigate(formik.dirty && !formik.isSubmitting);

	/**
	 * Formats the form values for the "configuration" parameter to send to
	 * the server. Sets defaults so the JSON.parse calls don't break.
	 * @param {Object} values Form values
	 * @return {Object}
	 */
	const _getConfiguration = ({taskConfig}) => {
		const configuration = taskConfig ? JSON.parse(taskConfig) : {};

		return configuration;
	};

	const _handleExternalReferenceCodeChange = (externalReferenceCode) => {
		formik.setFieldValue('externalReferenceCode', externalReferenceCode);
	};

	/**
	 * Used by the preview sidebar to cancel any unexpectedly slow search.
	 */
	const _handleFetchPreviewCancel = () => {
		controllerRef.current.abort();
	};

		/**
	 * Used by the preview sidebar to perform searches.
	 * @param {string} query The keyword search query
	 * @param {number} delta The number of results to return
	 * @param {number} page The page to return
	 * @param {Array} attributes The search context attributes
	 */
		const _handleFetchPreviewSearch = async (
			query,
			delta,
			page,
			attributes
		) => {
			controllerRef.current = new AbortController();
	
			setPreviewInfo((previewInfo) => ({
				...previewInfo,
				loading: true,
			}));
	
			let configuration;
			let elementInstances;
	
			try {
				configuration = _getConfiguration(formik.values);
				elementInstances = _getElementInstances(formik.values);
	
				// Touch inputs with errors to show validation errors.
	
				const errors = await formik.validateForm();
	
				formik.setTouched(setNestedObjectValues(errors, true));
	
				// Don't perform a search if there are missing required fields.
	
				if (!formik.isValid) {
					throw Liferay.Language.get(
						'the-configuration-has-missing-or-invalid-values'
					);
				}
			}
			catch (error) {
	
				// Add a delay so the loading indicator is visible before showing
				// the error message. This provides feedback that a new search has
				// been made.
	
				setTimeout(() => {
					setPreviewInfo({
						loading: false,
						results: {
							errors: [
								{
									msg: Liferay.Language.get(
										'the-configuration-has-missing-or-invalid-values'
									),
								},
							],
						},
					});
				}, 100);
	
				if (process.env.NODE_ENV === 'development') {
					console.error(error);
				}
	
				return;
			}
	
			const parseResponseContent = (responseContent) => {
				const exceptionKey = 'java.lang.RuntimeException';
	
				if (
					responseContent.searchHits?.totalHits > 0 ||
					!responseContent.responseString?.startsWith(exceptionKey)
				) {
					return responseContent;
				}
	
				let exceptionClass;
	
				const exceptionKeyIndex = responseContent.responseString.indexOf(
					':',
					exceptionKey.length + 1
				);
	
				if (exceptionKeyIndex !== -1) {
					exceptionClass = responseContent.responseString.substring(
						exceptionKey.length + 1,
						exceptionKeyIndex
					);
				}
	
				let msg;
	
				const errorObjectIndex =
					responseContent.responseString.indexOf('{"error":{');
	
				if (errorObjectIndex > 0) {
					const errorJSONObject = JSON.parse(
						responseContent.responseString.substring(errorObjectIndex)
					);
	
					msg = errorJSONObject.error.root_cause[0]?.reason;
				}
	
				return getResultsError({
					exceptionClass,
					exceptionTrace: responseContent.responseString,
					msg,
				});
			};
	
			return fetchPreviewSearch(
				{
					page,
					pageSize: delta,
					query,
				},
				{
					body: JSON.stringify({
						configuration: {
							...configuration,
							generalConfiguration: {
								...configuration?.generalConfiguration,
								emptySearchEnabled: true,
								explain: true,
								includeResponseString: true,
								languageId: Liferay.ThemeDisplay.getLanguageId(),
							},
							searchContextAttributes:
								transformToSearchContextAttributes(attributes),
						},
						elementInstances,
					}),
					signal: controllerRef.current.signal,
				}
			)
				.then((response) => {
					return response.json().then((data) => ({
						ok: response.ok,
						responseContent: data,
					}));
				})
				.then(({ok, responseContent}) => {
					setPreviewInfo({
						loading: false,
						results: parseResponseContent(
							ok
								? responseContent
								: getResultsError({
										msg: responseContent?.title,
									})
						),
					});
				})
				.catch((error) => {
					setPreviewInfo({
						loading: false,
						results:
							error.name === 'AbortError'
								? previewInfo.results
								: getResultsError({}),
					});
				});
		};

		const _handleFocusSXPElement = (prefixedId) => {
			const sxpElement = document.getElementById(prefixedId);
	
			if (sxpElement) {
				window.scrollTo({
					behavior: 'smooth',
					top:
						sxpElement.getBoundingClientRect().top +
						window.pageYOffset -
						55 - // Control menu height
						104 - // Page toolbar height
						20, // Additional padding
				});
	
				sxpElement.classList.remove('focus');
	
				void sxpElement.offsetWidth; // Triggers reflow to restart animation
	
				sxpElement.classList.add('focus');
			}
		};

	const _handleTitleAndDescriptionChange = ({
		description_i18n,
		title_i18n,
	}) => {
		formik.setFieldValue('description_i18n', description_i18n);
		formik.setFieldValue('title_i18n', title_i18n);

		setIsTitleAndDescriptionEdited(true);
	};
	
	const _handleSidebarClose = () => {
		setOpenSidebar('');
	};

	const _handleSubmit = (event) => {
		event.preventDefault();

		formik.handleSubmit();

		if (!formik.isValid) {
			openErrorToast({
				message: Liferay.Language.get(
					'unable-to-save-due-to-invalid-or-missing-configuration-values'
				),
			});
		}
	};

	const _handleToggleSidebar = (type) => () => {
		if (type === SIDEBAR_TYPES.PREVIEW) {
			setStorageAddSXPElementSidebar(SIDEBAR_STATE.CLOSED);
		}

		setOpenSidebar(openSidebar === type ? '' : type);
	};

	const _renderTabContent = () => {
		switch (tab) {
			default:
				return (
					<ConfigurationTab
						errors={formik.errors}
						externalReferenceCode={formik.values.externalReferenceCode}
						setFieldTouched={formik.setFieldTouched}
						setFieldValue={formik.setFieldValue}
						serializedTaskConfig={formik.values.taskConfig}
						touched={formik.touched}
					/>
				);
		}
	};

	return (
		<form ref={formRef}>
			<SubmitWarningModal
				errors={errors}
				isSubmitting={formik.isSubmitting}
				message={Liferay.Language.get(
					'the-task-definition-configuration-has-errors-that-may-cause-unexpected-results.-use-the-preview-panel-to-review-these-errors'
				)}
				onClose={() => setShowSubmitWarningModal(false)}
				onSubmit={_handleSubmit}
				visible={showSubmitWarningModal}
			/>

			<PageToolbar
				description={initialDescription}
				descriptionI18n={formik.values.description_i18n}
				entityId={taskDefinitionId}
				externalReferenceCode={formik.values.externalReferenceCode}
				isSubmitting={formik.isSubmitting}
				onCancel={redirectURL}
				onExternalReferenceCodeChange={
					_handleExternalReferenceCodeChange
				}
				onSubmit={_handleSubmit}
				onTitleAndDescriptionChange={_handleTitleAndDescriptionChange}
				tab={tab}
				tabs={TABS}
				title={initialTitle}
				titleAndDescriptionEdited={isTitleAndDescriptionEdited}
				titleI18n={formik.values.title_i18n}
			>
				<ClayToolbar.Item>
					<ClayButton
						borderless
						className={getCN({
							active: openSidebar === SIDEBAR_TYPES.PREVIEW,
						})}
						data-testid={TEST_IDS.PREVIEW_SIDEBAR_BUTTON}
						displayType="secondary"
						onClick={_handleToggleSidebar(SIDEBAR_TYPES.PREVIEW)}
						sm
					>
						Preview Chat
					</ClayButton>
				</ClayToolbar.Item>
			</PageToolbar>

			<PreviewSidebar
				errors={previewInfo.results.errors}
				formikValues={formik.values}
				hits={transformToSearchPreviewHits(previewInfo.results)}
				isSubmitting={formik.isSubmitting}
				loading={previewInfo.loading}
				onClose={_handleSidebarClose}
				onFetchCancel={_handleFetchPreviewCancel}
				onFetchResults={_handleFetchPreviewSearch}
				onFocusSXPElement={_handleFocusSXPElement}
				openSidebar={openSidebar}
				requestString={previewInfo.results.requestString}
				responseString={previewInfo.results.responseString}
				totalHits={previewInfo.results.searchHits?.totalHits}
				visible={openSidebar === SIDEBAR_TYPES.PREVIEW}
			/>

			<div
				className={getCN({
					'open-preview': openSidebar === SIDEBAR_TYPES.PREVIEW,
				})}
			>
				{_renderTabContent()}
			</div>
		</form>
	);
}

EditTaskDefinitionForm.propTypes = {
	entityJSON: PropTypes.object,
	initialConfiguration: PropTypes.object,
	initialDescription: PropTypes.string,
	initialDescriptionI18n: PropTypes.object,
	initialTitle: PropTypes.string,
	initialTitleI18n: PropTypes.object,
	taskDefinitionExternalReferenceCode: PropTypes.string,
	taskDefinitionId: PropTypes.string,
};

export default React.memo(EditTaskDefinitionForm);
