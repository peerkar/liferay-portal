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

package com.liferay.search.experiences.blueprint.query;

import com.liferay.portal.search.query.Query;
import com.liferay.search.experiences.blueprint.constants.ClauseContext;
import com.liferay.search.experiences.blueprint.constants.Occur;
import com.liferay.search.experiences.blueprint.parameter.SXPParameterData;
import com.liferay.search.experiences.model.SXPBlueprint;

import java.util.Map;
import java.util.Optional;

/**
 * @author Petteri Karttunen
 */
public interface QueryContributor {

	public Optional<Query> build(
		SXPBlueprint sxpBlueprint, SXPParameterData sxpParameterData);

	public Map<String, Object> getAttributes();

	public ClauseContext getClauseContext();

	public Occur getOccur();

}