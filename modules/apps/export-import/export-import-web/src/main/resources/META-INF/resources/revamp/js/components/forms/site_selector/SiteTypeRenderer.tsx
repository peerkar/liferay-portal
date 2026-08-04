/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import React from 'react';

/**
 * The kind of site, translated.
 *
 * <p>
 * The translation happens here rather than on the row, so that it happens to
 * every site the table shows, whether the row came from the API or from a list
 * the dialog was handed.
 * </p>
 */
export default function SiteTypeRenderer({value}: {value?: boolean}) {
	if (value) {
		return <>{Liferay.Language.get('global')}</>;
	}

	return <>{Liferay.Language.get('site')}</>;
}
