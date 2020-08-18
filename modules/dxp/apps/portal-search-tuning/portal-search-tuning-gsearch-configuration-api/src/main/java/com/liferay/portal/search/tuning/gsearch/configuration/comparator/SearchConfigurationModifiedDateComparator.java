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

import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration;

/**
 * @author Petteri Karttunen
 */
public class SearchConfigurationModifiedDateComparator
	extends OrderByComparator<SearchConfiguration> {

	public static final String ORDER_BY_ASC =
		"SearchConfiguration.modifiedDate ASC, " +
			"SearchConfiguration.searchConfigurationId ASC";

	public static final String[] ORDER_BY_CONDITION_FIELDS = {"modifiedDate"};

	public static final String ORDER_BY_DESC =
		"SearchConfiguration.modifiedDate DESC, " +
			"SearchConfiguration.searchConfigurationId DESC";

	public static final String[] ORDER_BY_FIELDS = {
		"modifiedDate", "searchConfigurationId"
	};

	public SearchConfigurationModifiedDateComparator() {
		this(false);
	}

	public SearchConfigurationModifiedDateComparator(boolean ascending) {
		_ascending = ascending;
	}

	@Override
	public int compare(
		SearchConfiguration searchConfiguration1,
		SearchConfiguration searchConfiguration2) {

		int value = DateUtil.compareTo(
			searchConfiguration1.getModifiedDate(),
			searchConfiguration2.getModifiedDate());

		if (value == 0) {
			if (searchConfiguration1.getSearchConfigurationId() <
					searchConfiguration2.getSearchConfigurationId()) {

				value = -1;
			}
			else if (searchConfiguration1.getSearchConfigurationId() >
						searchConfiguration2.getSearchConfigurationId()) {

				value = 1;
			}
		}

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
	public String[] getOrderByConditionFields() {
		return ORDER_BY_CONDITION_FIELDS;
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

}