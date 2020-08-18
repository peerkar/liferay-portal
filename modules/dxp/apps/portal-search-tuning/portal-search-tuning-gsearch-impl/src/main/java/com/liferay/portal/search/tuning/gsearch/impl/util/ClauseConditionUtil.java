package com.liferay.portal.search.tuning.gsearch.impl.util;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.ClauseConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.exception.ParameterEvaluationException;
import com.liferay.portal.search.tuning.gsearch.message.Message;
import com.liferay.portal.search.tuning.gsearch.message.Severity;

public class ClauseConditionUtil {

	public static JSONArray getClauseConditionArrayValue(
			JSONObject conditionJsonObject)
		throws ParameterEvaluationException {

		Object object = conditionJsonObject.get(
			ClauseConfigurationKeys.MATCH_VALUE.getJsonKey());

		if (!(object instanceof JSONArray)) {
			throw new ParameterEvaluationException(
				new Message(
					Severity.ERROR, "core",
					"core.error.expected-clause-condition-array-match-value",
					null, null, conditionJsonObject,
					ClauseConfigurationKeys.MATCH_VALUE.getJsonKey(),
					object.toString()));
		}

		return (JSONArray)object;
	}

}