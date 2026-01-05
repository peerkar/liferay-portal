/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

export const config = {
	name: 'site-cms-site.main',
	teardown: 'site-cms-site.teardown',
	testDir: 'tests/setup/site-cms-site/main',
	testMatch: 'setup.spec.ts',
	timeout: 90 * 1000,
};
