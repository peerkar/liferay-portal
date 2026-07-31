/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import ClayButton from '@clayui/button';
import ClayModal, {useModal} from '@clayui/modal';
import {FrontendDataSet, IView} from '@liferay/frontend-data-set-web';
import React, {useState} from 'react';

import {PreviewSite} from '../../../types/exportImportPreview';
import SiteNameRenderer from './SiteNameRenderer';
import SiteStatusRenderer from './SiteStatusRenderer';
import SiteTypeRenderer from './SiteTypeRenderer';

/**
 * The page sizes every list in the portal offers, from
 * "search.container.page.delta.values".
 */
const DELTAS = [{label: 20}, {label: 40}, {label: 60}];

const FDS_ID = 'exportImportSiteSelector';

const SITE_NAME_RENDERER = 'siteName';

const SITE_STATUS_RENDERER = 'siteStatus';

const SITE_TYPE_RENDERER = 'siteType';

/**
 * Every column that shows something other than the value itself renders it
 * here, so that the rows stay the sites as they arrive and a column reads the
 * same whether the sites came from the API or from a list the dialog was
 * handed.
 */
const CUSTOM_DATA_RENDERERS = {
	[SITE_NAME_RENDERER]: SiteNameRenderer,
	[SITE_STATUS_RENDERER]: SiteStatusRenderer,
	[SITE_TYPE_RENDERER]: SiteTypeRenderer,
};

/**
 * The name is the only field the sites endpoint orders on, so the dropdown
 * offers the direction and nothing else to choose between.
 *
 * <p>
 * The data set turns this into a "sort=name:asc" parameter, so it only has an
 * effect on the side that reads the sites from the API. Sorting a list handed
 * to the data set through its <code>items</code> prop is not something it
 * supports, which is why the import side goes without.
 * </p>
 */
const SORTS = [
	{
		active: true,
		default: true,
		direction: 'asc' as const,
		key: 'name',
		label: Liferay.Language.get('title'),
	},
];

function getView(showExisting: boolean): IView {
	return {
		contentRenderer: 'table',
		default: true,
		label: Liferay.Language.get('table'),
		name: 'table',
		schema: {
			fields: [
				{
					contentRenderer: SITE_NAME_RENDERER,
					expand: true,
					fieldName: 'name',
					label: Liferay.Language.get('title'),
				},
				{
					fieldName: 'childSiteCount',
					label: Liferay.Language.get('child-sites'),
				},
				{
					contentRenderer: SITE_TYPE_RENDERER,
					fieldName: 'type',
					label: Liferay.Language.get('type'),
				},
				...(showExisting
					? [
							{
								contentRenderer: SITE_STATUS_RENDERER,
								fieldName: 'existing',
								label: Liferay.Language.get('status'),
							},
						]
					: []),
			],
		},
	};
}

/**
 * Gives the row the identity the data set expects, which the sites the API
 * returns do not carry either.
 */
function toRow(previewSite: PreviewSite) {
	return {
		...previewSite,
		id: previewSite.externalReferenceCode,
	};
}

/**
 * Lists the sites that can be picked, and hands back the ones the user chose
 * only when they confirm, so that closing the dialog leaves the selection as it
 * was.
 *
 * <p>
 * Pass <code>apiURL</code> to page and search through the sites of the
 * instance, or <code>previewSites</code> to work from a list already in hand,
 * which is what the import side has once a file is read.
 * </p>
 */
export default function SiteSelectorModal({
	apiURL,
	onClose,
	onSubmit,
	previewSites,
	selectedExternalReferenceCodes,
	showExisting = false,
}: {
	apiURL?: string;
	onClose: () => void;
	onSubmit: (previewSites: PreviewSite[]) => void;
	previewSites?: PreviewSite[];
	selectedExternalReferenceCodes: string[];
	showExisting?: boolean;
}) {
	const {observer, onClose: closeModal} = useModal({onClose});

	const items = previewSites?.map(toRow);

	// The sites are read a page at a time when they come from the API, so the
	// ones picked earlier are not necessarily in hand. The data set matches a
	// selection on the external reference code alone, so a stand-in carrying
	// only that is enough to show them as picked.

	const [selectedItems, setSelectedItems] = useState<any[]>(() =>
		selectedExternalReferenceCodes.map(
			(externalReferenceCode) =>
				items?.find(
					(item) =>
						item.externalReferenceCode === externalReferenceCode
				) ?? {externalReferenceCode}
		)
	);

	return (
		<ClayModal observer={observer} size="lg">
			<ClayModal.Header>
				{Liferay.Language.get('select-sites')}
			</ClayModal.Header>

			<ClayModal.Body>
				<FrontendDataSet
					apiURL={apiURL}
					customDataRenderers={CUSTOM_DATA_RENDERERS}
					id={FDS_ID}
					itemsActions={[]}
					onItemsPropSearch={(item, query) =>
						String(item.name ?? '')
							.toLowerCase()
							.includes(query.toLowerCase())
					}
					onSelectedItemsChange={setSelectedItems}
					pagination={{deltas: DELTAS, initialDelta: DELTAS[0].label}}
					selectedItems={selectedItems}
					selectedItemsKey="externalReferenceCode"
					selectionType="multiple"
					showManagementBar
					showPagination
					showSearch
					showSelectAll
					style="stacked"
					views={[getView(showExisting)]}
					{...(items ? {items} : {sorts: SORTS})}
				/>
			</ClayModal.Body>

			<ClayModal.Footer
				last={
					<ClayButton.Group spaced>
						<ClayButton
							displayType="secondary"
							onClick={closeModal}
						>
							{Liferay.Language.get('cancel')}
						</ClayButton>

						<ClayButton
							onClick={() => {
								onSubmit(selectedItems);

								closeModal();
							}}
						>
							{Liferay.Language.get('select')}
						</ClayButton>
					</ClayButton.Group>
				}
			/>
		</ClayModal>
	);
}
