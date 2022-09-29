/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.search.experiences.ingest.web.internal.util;

import com.liferay.portal.kernel.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Petteri Karttunen
 */
public class CSVUtil {

	public static List<Long> csvToLongList(String csv) {
		String[] arr = StringUtil.split(csv, ",");

		List<Long> values = new ArrayList<>();

		for (String s : arr) {
			values.add(Long.valueOf(StringUtil.trim(s)));
		}

		return values;
	}

	public static List<String> csvtoStringList(String csv) {
		String[] arr = StringUtil.split(csv, ",");

		List<String> values = new ArrayList<>();

		for (String s : arr) {
			values.add(StringUtil.trim(s));
		}

		return values;
	}

}