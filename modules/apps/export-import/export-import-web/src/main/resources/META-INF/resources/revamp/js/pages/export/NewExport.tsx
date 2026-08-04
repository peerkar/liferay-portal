/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import ClayAlert from '@clayui/alert';
import ClayButton from '@clayui/button';
import ClayIcon from '@clayui/icon';
import ClayLayout from '@clayui/layout';
import {Form, Formik, FormikValues} from 'formik';
import React, {useCallback, useEffect, useRef, useState} from 'react';

import Footer from '../../components/Footer';
import {
	DateFilterValues,
	NormalizedDateFilter,
	Range,
	normalizeDateFilter,
} from '../../components/date_filter';
import {ContentSelection} from '../../components/forms/content_selector/ContentSelector';
import {
	ExportPreviewParams,
	getExportPreview,
} from '../../services/getExportPreview';
import {postExportProcess} from '../../services/postExportProcess';
import {ExportPreview} from '../../types/exportImportPreview';
import {
	getSelectedDeletionCount,
	getSelectedItemsCount,
	toProcessRequestFlags,
	withSelectedLayoutSetCount,
} from '../../utils/contentSelection';
import {toRequestPortletDataHandlers} from '../../utils/toRequestPortletDataHandlers';
import DataSelection from './components/DataSelection';
import {PageTreeModalConfiguration} from './components/PageTreeModal';
import Setup from './components/Setup';
import SiteSelection from './components/SiteSelection';

export function NewExport({
	backURL,
	commentsAndRatingsEnabled = false,
	exportPreview,
	exportPreviewAPIURL,
	exportPreviewSitesAPIURL,
	exportProcessAPIURL,
	lookAndFeelEnabled = false,
	pageTreeModalConfiguration,
	sitesEnabled = false,
}: {
	backURL: string;
	commentsAndRatingsEnabled?: boolean;
	exportPreview?: ExportPreview;
	exportPreviewAPIURL: string;
	exportPreviewSitesAPIURL?: string;
	exportProcessAPIURL: string;
	lookAndFeelEnabled?: boolean;
	pageTreeModalConfiguration: PageTreeModalConfiguration;
	sitesEnabled?: boolean;
}) {
	const [preview, setPreview] = useState<ExportPreview | undefined>(
		exportPreview
	);
	const [error, setError] = useState<string | null>(null);
	const [loading, setLoading] = useState(!exportPreview);
	const initialPreviewRef = useRef<ExportPreview | undefined>(exportPreview);
	const appliedDateFilterRef = useRef<NormalizedDateFilter>({});

	const getPreview = useCallback(
		(exportPreviewParams: ExportPreviewParams) => {
			setLoading(true);
			setError(null);

			getExportPreview(exportPreviewParams).then(
				(exportPreviewResponse) => {
					if (exportPreviewResponse.error !== null) {
						setError(exportPreviewResponse.error);
					}
					else {
						setPreview(exportPreviewResponse.data);

						if (!initialPreviewRef.current) {
							initialPreviewRef.current =
								exportPreviewResponse.data;
						}
					}

					setLoading(false);
				}
			);
		},
		[]
	);

	useEffect(() => {
		if (exportPreview) {
			return;
		}

		getPreview({url: exportPreviewAPIURL});
	}, [exportPreview, exportPreviewAPIURL, getPreview]);

	if (error) {
		return <ClayAlert displayType="danger">{error}</ClayAlert>;
	}

	const previewPortletDataHandlerSections =
		preview?.previewPortletDataHandlerSections ?? [];

	const handleApplyFilter = (filterValues: DateFilterValues) => {
		appliedDateFilterRef.current = normalizeDateFilter(filterValues);

		if (filterValues.range === Range.All && initialPreviewRef.current) {
			setPreview(initialPreviewRef.current);

			return;
		}

		getPreview({
			query: appliedDateFilterRef.current,
			url: exportPreviewAPIURL,
		});
	};

	return (
		<Formik
			initialValues={{
				contentSelection: undefined,
				dateFilter: {range: Range.All} as DateFilterValues,
				deletions: false,
				name: '',
				permissions: false,
				siteExternalReferenceCodes: [],
			}}
			onSubmit={async (values) => {
				const contentSelection = values.contentSelection as
					| ContentSelection
					| undefined;

				const result = await postExportProcess({
					exportProcessRequest: {
						...appliedDateFilterRef.current,
						...toProcessRequestFlags(contentSelection),
						deletions: !!values.deletions,
						name: values.name,
						permissions: !!values.permissions,
						requestPortletDataHandlers:
							toRequestPortletDataHandlers(
								previewPortletDataHandlerSections,
								values.contentSelection
							),
						siteExternalReferenceCodes:
							values.siteExternalReferenceCodes,
					},
					url: exportProcessAPIURL,
				});

				if (result.error) {
					Liferay.Util.openToast({
						message: result.error,
						type: 'danger',
					});

					return;
				}

				Liferay.Util.navigate(backURL);
			}}
			validate={(values: FormikValues) => {
				const errors: {[key: string]: string} = {};

				if (!values?.name) {
					errors.name = Liferay.Language.get(
						'this-field-is-required'
					);
				}

				if (
					!values?.contentSelection &&
					!values?.siteExternalReferenceCodes?.length
				) {

					// Where sites are on offer the requirement is satisfied by
					// an entity type or by a site, so it belongs to the form
					// and is shown below both selectors. Where they are not,
					// the content selector is the one thing left incomplete,
					// so the error stays on it.

					if (sitesEnabled) {
						errors.selection = Liferay.Language.get(
							'please-select-at-least-one-entity-type-or-site-to-continue'
						);
					}
					else {
						errors.contentSelection = Liferay.Language.get(
							'please-select-at-least-one-entity-type-to-continue'
						);
					}
				}

				return errors;
			}}
			validateOnMount
		>
			{(formik) => {
				const contentSelection = formik.values.contentSelection as
					| ContentSelection
					| undefined;

				// Only set where sites are on offer, so it is not keyed to a
				// field of its own

				const {selection: selectionError} = formik.errors as {
					selection?: string;
				};

				return (
					<Form noValidate>
						<Setup />

						<DataSelection
							commentsAndRatingsEnabled={
								commentsAndRatingsEnabled
							}
							deletionCount={getSelectedDeletionCount(
								preview?.deletionCount,
								previewPortletDataHandlerSections,
								contentSelection
							)}
							itemsCount={getSelectedItemsCount(
								preview?.additionCount,
								previewPortletDataHandlerSections,
								contentSelection
							)}
							loading={loading}
							lookAndFeelEnabled={lookAndFeelEnabled}
							onApplyFilter={handleApplyFilter}
							pageTreeModalConfiguration={
								pageTreeModalConfiguration
							}
							previewPortletDataHandlerSections={withSelectedLayoutSetCount(
								previewPortletDataHandlerSections,
								contentSelection
							)}
						/>

						{sitesEnabled && exportPreviewSitesAPIURL && (
							<ClayLayout.Sheet className="mt-4 option-group">
								<SiteSelection
									exportPreviewSitesAPIURL={
										exportPreviewSitesAPIURL
									}
								/>
							</ClayLayout.Sheet>
						)}

						{formik.touched.contentSelection && selectionError && (
							<ClayAlert
								className="mt-4"
								displayType="danger"
								title={Liferay.Language.get('error-colon')}
							>
								{selectionError}
							</ClayAlert>
						)}

						<Footer
							actionButton={
								<ClayButton
									disabled={
										formik.isSubmitting || !formik.isValid
									}
									type="submit"
								>
									<span className="inline-item inline-item-before">
										<ClayIcon
											className="mr-1"
											symbol="export"
										/>
									</span>

									{Liferay.Language.get('export')}
								</ClayButton>
							}
							backURL={backURL}
						/>
					</Form>
				);
			}}
		</Formik>
	);
}
