/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import React from 'react';

/**
 * Whether the site the LAR carries is one the instance already has.
 *
 * <p>
 * The plain boolean the DTO carries would otherwise be shown as a yes or a no,
 * which says nothing about what the import is going to do with the site.
 * </p>
 */
export default function SiteStatusRenderer({value}: {value?: boolean}) {
	if (value) {
		return <>{Liferay.Language.get('existing')}</>;
	}

	return <>{Liferay.Language.get('new')}</>;
}
