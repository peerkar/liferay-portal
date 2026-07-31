/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {PreviewPortletDataHandlerSection} from './portletDataHandler';

export const PREVIEW_SITE_TYPES = {
	GLOBAL: 'GLOBAL',
	SITE: 'SITE',
} as const;

export type PreviewSiteType =
	(typeof PREVIEW_SITE_TYPES)[keyof typeof PREVIEW_SITE_TYPES];

export interface PreviewSite {
	childSiteCount?: number;
	existing?: boolean;
	externalReferenceCode: string;
	friendlyUrlPath?: string;

	/**
	 * Where the site sits, ready to be shown, as in "Global / My Site / Child".
	 */
	hierarchy?: string;
	name?: string;
	type?: PreviewSiteType;
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
