/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.elasticsearch7.internal.filter.util;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

import org.elasticsearch.index.query.RangeQueryBuilder;

/**
 * @author Petteri Karttunen
 */
public class RangeUtil {

	public static void addRange(
		String from, RangeQueryBuilder rangeQueryBuilder, String to) {

		if (StringUtil.equals(from, StringPool.STAR)) {
			rangeQueryBuilder.from(null);
		}
		else {
			rangeQueryBuilder.from(from);
		}

		if (StringUtil.equals(to, StringPool.STAR)) {
			rangeQueryBuilder.to(null);
		}
		else {
			rangeQueryBuilder.to(to);
		}
	}

}