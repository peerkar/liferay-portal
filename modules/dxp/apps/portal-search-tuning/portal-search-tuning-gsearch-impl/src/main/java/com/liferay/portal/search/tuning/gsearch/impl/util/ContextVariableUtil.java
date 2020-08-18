package com.liferay.portal.search.tuning.gsearch.impl.util;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.impl.internal.parameter.visitor.ToConfigurationParameterStringVisitor;
import com.liferay.portal.search.tuning.gsearch.parameter.Parameter;

public class ContextVariableUtil {

	public static JSONObject parseConfigurationVariables(
			SearchRequestContext searchRequestContext,
			JSONObject queryJsonObject)
		throws Exception {

		if (!searchRequestContext.getSearchParameterData().hasParameters()) {
			return queryJsonObject;
		}

		String queryString = queryJsonObject.toString();

		boolean changed = false;

		ToConfigurationParameterStringVisitor
			toConfigurationParameterStringVisitor =
				new ToConfigurationParameterStringVisitor();

		for (Parameter parameter :
				searchRequestContext.getSearchParameterData().getParameters()) {

			String variable = parameter.getConfigurationVariable();

			if (queryString.contains(variable)) {
				queryString = queryString.replace(
					variable,
					parameter.accept(toConfigurationParameterStringVisitor));
				changed = true;
			}
		}

		if (changed) {
			return JSONFactoryUtil.createJSONObject(queryString);
		}

		return queryJsonObject;
	}

}