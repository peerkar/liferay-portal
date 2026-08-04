/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import React from 'react';

import {PreviewSite} from '../../../types/exportImportPreview';

/**
 * The title cell of the sites list, naming the site and showing where it sits
 * below the name.
 *
 * <p>
 * Sites of every level are listed side by side rather than nested, so the path
 * is what tells a parent apart from a child, and two sites of the same name
 * apart from each other.
 * </p>
 */
export default function SiteNameRenderer({
	itemData,
	value,
}: {
	itemData: PreviewSite;
	value: string;
}) {
	return (
		<>
			<span className="d-block">{value}</span>

			{itemData.path && (
				<span className="d-block small text-secondary">
					{itemData.path}
				</span>
			)}
		</>
	);
}
