/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.internal.custom.facet.display.context;

import com.liferay.portal.search.web.internal.custom.facet.configuration.CustomFacetPortletInstanceConfiguration;
import com.liferay.portal.search.web.internal.facet.display.context.BucketDisplayContext;
import com.liferay.portal.search.web.internal.facet.display.context.FacetDisplayContext;

import java.util.List;

/**
 * @author Wade Cao
 * @author Petteri Karttunen
 */
public class CustomFacetDisplayContext implements FacetDisplayContext {

	public CustomFacetDisplayContext(
		String aggregationType,
		List<BucketDisplayContext> bucketDisplayContexts,
		CustomFacetCalendarDisplayContext customFacetCalendarDisplayContext,
		CustomFacetPortletInstanceConfiguration
			customFacetPortletInstanceConfiguration,
		BucketDisplayContext customRangeBucketDisplayContext,
		String displayCaption, long displayStyleGroupId,
		boolean nothingSelected, String paginationStartParameterName,
		String parameterName, String parameterValue,
		List<String> parameterValues, boolean renderNothing) {

		_aggregationType = aggregationType;
		_bucketDisplayContexts = bucketDisplayContexts;
		_customFacetCalendarDisplayContext = customFacetCalendarDisplayContext;
		_customFacetPortletInstanceConfiguration =
			customFacetPortletInstanceConfiguration;
		_customRangeBucketDisplayContext = customRangeBucketDisplayContext;
		_displayCaption = displayCaption;
		_displayStyleGroupId = displayStyleGroupId;
		_nothingSelected = nothingSelected;
		_paginationStartParameterName = paginationStartParameterName;
		_parameterName = parameterName;
		_parameterValue = parameterValue;
		_parameterValues = parameterValues;
		_renderNothing = renderNothing;
	}

	public String getAggregationType() {
		return _aggregationType;
	}

	@Override
	public List<BucketDisplayContext> getBucketDisplayContexts() {
		return _bucketDisplayContexts;
	}

	public CustomFacetCalendarDisplayContext
		getCustomFacetCalendarDisplayContext() {

		return _customFacetCalendarDisplayContext;
	}

	public CustomFacetPortletInstanceConfiguration
		getCustomFacetPortletInstanceConfiguration() {

		return _customFacetPortletInstanceConfiguration;
	}

	public BucketDisplayContext getCustomRangeBucketDisplayContext() {
		return _customRangeBucketDisplayContext;
	}

	public String getDisplayCaption() {
		return _displayCaption;
	}

	@Override
	public long getDisplayStyleGroupId() {
		return _displayStyleGroupId;
	}

	@Override
	public String getPaginationStartParameterName() {
		return _paginationStartParameterName;
	}

	@Override
	public String getParameterName() {
		return _parameterName;
	}

	@Override
	public String getParameterValue() {
		return _parameterValue;
	}

	@Override
	public List<String> getParameterValues() {
		return _parameterValues;
	}

	@Override
	public boolean isNothingSelected() {
		return _nothingSelected;
	}

	@Override
	public boolean isRenderNothing() {
		return _renderNothing;
	}

	private final String _aggregationType;
	private final List<BucketDisplayContext> _bucketDisplayContexts;
	private final CustomFacetCalendarDisplayContext
		_customFacetCalendarDisplayContext;
	private final CustomFacetPortletInstanceConfiguration
		_customFacetPortletInstanceConfiguration;
	private final BucketDisplayContext _customRangeBucketDisplayContext;
	private final String _displayCaption;
	private final long _displayStyleGroupId;
	private final boolean _nothingSelected;
	private final String _paginationStartParameterName;
	private final String _parameterName;
	private final String _parameterValue;
	private final List<String> _parameterValues;
	private final boolean _renderNothing;

}