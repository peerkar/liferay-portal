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

package com.liferay.portal.search.tuning.gsearch.impl.internal.spellcheck;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.suggest.Suggester;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.SpellCheckerConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.impl.internal.suggester.SuggesterHelper;
import com.liferay.portal.search.tuning.gsearch.spellcheck.SpellChecker;

import java.util.List;
import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(immediate = true, service = SpellChecker.class)
public class SpellCheckerImpl implements SpellChecker {

	@Override
	public JSONArray getSuggestions(SearchRequestContext searchRequestContext) {
		
		Optional<JSONObject> spellCheckerConfigurationJsonObjectOptional =
				searchRequestContext.getKeywordSuggesterConfiguration();
			
			if (!spellCheckerConfigurationJsonObjectOptional.isPresent()) {
				
				if (_log.isDebugEnabled()) {
					_log.debug("Spell checker configuration not available in search configuration " +
							searchRequestContext.getSearchConfigurationId());
				}
				
				return JSONFactoryUtil.createJSONArray();
			}

			JSONObject spellCheckerConfigurationJsonObject = 
					spellCheckerConfigurationJsonObjectOptional.get();

			if (!spellCheckerConfigurationJsonObject.getBoolean(
					SpellCheckerConfigurationKeys.ENABLED.getJsonKey()) ||
					spellCheckerConfigurationJsonObject.isNull(
							SpellCheckerConfigurationKeys.SUGGESTERS.getJsonKey())) {
				return JSONFactoryUtil.createJSONArray();
			}
			
			JSONArray suggesterConfigurationJsonArray = 
					spellCheckerConfigurationJsonObject.getJSONArray(
							SpellCheckerConfigurationKeys.SUGGESTERS.getJsonKey());
		
		List<Suggester> suggesters = _suggesterHelper.getSuggesters(
			searchRequestContext, suggesterConfigurationJsonArray);

		if (!suggesters.isEmpty()) {
			return _suggesterHelper.getSuggestions(
				searchRequestContext, suggesters);
		}

		return JSONFactoryUtil.createJSONArray();
	}
	
	private static final Log _log = LogFactoryUtil.getLog(
			SpellCheckerImpl.class);

	@Reference
	private SuggesterHelper _suggesterHelper;

}