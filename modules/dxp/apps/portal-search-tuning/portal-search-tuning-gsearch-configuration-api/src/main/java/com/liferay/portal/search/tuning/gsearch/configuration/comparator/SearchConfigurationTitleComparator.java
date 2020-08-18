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

package com.liferay.portal.search.tuning.gsearch.configuration.comparator;

import com.liferay.portal.kernel.util.CollatorUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration;

import java.text.Collator;

import java.util.Locale;

/**
 * @author Petteri Karttunen
 */
public class SearchConfigurationTitleComparator
	extends OrderByComparator<SearchConfiguration> {

	public static final String ORDER_BY_ASC = "SearchConfiguration.title ASC";

	public static final String ORDER_BY_DESC = "SearchConfiguration.title DESC";

	public static final String[] ORDER_BY_FIELDS = {"title"};

	public SearchConfigurationTitleComparator() {
		this(false);
	}

	public SearchConfigurationTitleComparator(boolean ascending) {
		_ascending = ascending;
	}

	public SearchConfigurationTitleComparator(
		boolean ascending, Locale locale) {

		_ascending = ascending;
		_locale = locale;
	}

	@Override
	public int compare(
		SearchConfiguration searchConfiguration1,
		SearchConfiguration searchConfiguration2) {

		Collator collator = CollatorUtil.getInstance(_locale);

		String title1 = StringUtil.toLowerCase(
			searchConfiguration1.getTitle(_locale));
		String title2 = StringUtil.toLowerCase(
			searchConfiguration2.getTitle(_locale));

		int value = collator.compare(title1, title2);

		if (_ascending) {
			return value;
		}

		return -value;
	}

	@Override
	public String getOrderBy() {
		if (_ascending) {
			return ORDER_BY_ASC;
		}

		return ORDER_BY_DESC;
	}

	@Override
	public String[] getOrderByFields() {
		return ORDER_BY_FIELDS;
	}

	@Override
	public boolean isAscending() {
		return _ascending;
	}

	private final boolean _ascending;
	private Locale _locale;

}