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

package com.liferay.search.experiences.internal.blueprint.condition.util;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.search.experiences.blueprint.clause.ConditionHandler;
import com.liferay.search.experiences.blueprint.parameter.SXPParameterData;
import com.liferay.search.experiences.internal.blueprint.condition.ConditionHandlerFactory;
import com.liferay.search.experiences.internal.problem.ProblemUtil;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(immediate = true, service = ConditionsProcessor.class)
public class ConditionsProcessor {

	public boolean processConditions(
		JSONObject jsonObject, SXPParameterData sxpParameterData,
		String groupCondition) {

		if ((jsonObject == null) || (jsonObject.length() == 0)) {
			return true;
		}

		Set<String> keySet = jsonObject.keySet();

		boolean childrenValid = _processDirectChildren(
			groupCondition, jsonObject, keySet, sxpParameterData);

		if (!childrenValid) {
			return false;
		}

		if (keySet.contains("any_of")) {
			Stream<String> stream = keySet.stream();

			boolean valid = stream.filter(
				key -> key.equals("any_of")
			).anyMatch(
				key -> processConditions(
					jsonObject.getJSONObject(key), sxpParameterData, "any_of")
			);

			if (!valid) {
				return false;
			}
		}

		if (keySet.contains("all_of")) {
			Stream<String> stream = keySet.stream();

			boolean valid = stream.filter(
				key -> key.equals("all_of")
			).allMatch(
				key -> processConditions(
					jsonObject.getJSONObject(key), sxpParameterData, "all_of")
			);

			if (!valid) {
				return false;
			}
		}

		return true;
	}

	private boolean _processCondition(
		String handler, JSONObject jsonObject,
		SXPParameterData sxpParameterData) {

		try {
			ConditionHandler conditionHandler =
				_conditionHandlerFactory.getHandler(handler);

			return conditionHandler.isTrue(jsonObject, sxpParameterData);
		}
		catch (Exception exception) {
			_log.error(exception);

			ProblemUtil.addUnknownError(
				getClass().getName(), jsonObject, null, null, exception);

			return false;
		}
	}

	private boolean _processDirectChildren(
		String groupCondition, JSONObject jsonObject, Set<String> keySet,
		SXPParameterData sxpParameterData) {

		Stream<String> stream1 = keySet.stream();

		List<String> conditions = stream1.filter(
			key -> !key.equals("all_of") && !key.equals("any_of")
		).collect(
			Collectors.toList()
		);

		if (conditions.isEmpty()) {
			return true;
		}

		Stream<String> stream2 = conditions.stream();

		if ((groupCondition != null) && groupCondition.equals("any_of")) {
			return stream2.anyMatch(
				key -> _processCondition(
					key, jsonObject.getJSONObject(key), sxpParameterData));
		}

		return stream2.allMatch(
			key -> _processCondition(
				key, jsonObject.getJSONObject(key), sxpParameterData));
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ConditionsProcessor.class);

	@Reference
	private ConditionHandlerFactory _conditionHandlerFactory;

}