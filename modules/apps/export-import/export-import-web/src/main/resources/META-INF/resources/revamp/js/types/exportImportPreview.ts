/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {PreviewPortletDataHandlerSection} from './portletDataHandler';

export interface PreviewSite {
	childSiteCount?: number;

	/**
	 * The name the site goes by, which is not always the name it is stored under.
	 */
	descriptiveName?: string;

	/**
	 * Whether the instance being imported into already has a site of the same
	 * external reference code. Absent for a site being exported, where it says
	 * nothing.
	 */
	existsInInstance?: boolean;
	externalReferenceCode: string;

	/**
	 * Whether this is the site an instance keeps for content shared across its
	 * sites.
	 */
	global?: boolean;

	/**
	 * Where the site sits, ready to be shown, as in "Global / My Site / Child".
	 */
	path?: string;
}

export interface ExportPreview {
	additionCount: number;
	deletionCount: number;
	previewPortletDataHandlerSections: PreviewPortletDataHandlerSection[];
}

export interface ImportPreview {
	additionCount: number;
	author: string;
	deletionCount: number;
	exportDate: string;
	fileName: string;
	fileSize: number;
	previewPortletDataHandlerSections: PreviewPortletDataHandlerSection[];
	previewSites?: PreviewSite[];
}
