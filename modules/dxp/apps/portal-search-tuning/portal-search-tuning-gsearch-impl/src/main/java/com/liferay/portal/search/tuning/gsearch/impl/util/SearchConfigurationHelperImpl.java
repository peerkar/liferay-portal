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

package com.liferay.portal.search.tuning.gsearch.impl.util;

import com.liferay.portal.search.tuning.gsearch.impl.internal.aggregations.AggregationBuilderFactory;
import com.liferay.portal.search.tuning.gsearch.impl.internal.aggregations.facet.FacetHandlerFactory;
import com.liferay.portal.search.tuning.gsearch.impl.internal.clause.ClauseBuilderFactory;
import com.liferay.portal.search.tuning.gsearch.impl.internal.clause.condition.ClauseConditionHandlerFactory;
import com.liferay.portal.search.tuning.gsearch.impl.internal.parameter.contributor.ParameterContributors;
import com.liferay.portal.search.tuning.gsearch.parameter.ParameterDefinition;
import com.liferay.portal.search.tuning.gsearch.util.SearchConfigurationHelper;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(immediate = true, service = SearchConfigurationHelper.class)
public class SearchConfigurationHelperImpl
	implements SearchConfigurationHelper {

	@Override
	public String[] getAvailableAggregationBuilderTypes() {
		return _aggregationBuilderFactory.getBuilderTypes();
	}

	@Override
	public String[] getAvailableClauseBuilderTypes() {
		return _clauseBuilderFactory.getBuilderTypes();
	}

	@Override
	public String[] getAvailableClauseConditionHandlerNames() {
		return _clauseConditionHandlerFactory.getHandlerNames();
	}

	@Override
	public String[] getAvailableFacetHandlerNames() {
		return _facetHandlerFactory.getHandlerNames();
	}

	@Override
	public ParameterDefinition[] getContributedParameterDefinitions() {
		return _parameterContributors.getParameterDefinitions();
	}

	@Reference
	private AggregationBuilderFactory _aggregationBuilderFactory;

	@Reference
	private ClauseBuilderFactory _clauseBuilderFactory;

	@Reference
	private ClauseConditionHandlerFactory _clauseConditionHandlerFactory;

	@Reference
	private FacetHandlerFactory _facetHandlerFactory;

	@Reference
	private ParameterContributors _parameterContributors;

}