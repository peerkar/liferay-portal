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

package com.liferay.portal.search.tuning.gsearch.impl.internal.keywords;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.tuning.gsearch.impl.internal.misspellings.MisspellingService;

import org.apache.commons.lang3.StringUtils;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(immediate = true, service = KeywordsProcessor.class)
public class KeywordsProcessorImpl implements KeywordsProcessor {

	@Override
	public String clean(String keywords) {
		keywords = _cleanInput(keywords);

		return _misspellingService.process(keywords);
	}

	private String _cleanInput(String keywords) {
		keywords = StringUtil.trim(keywords);

		if (Validator.isBlank(keywords)) {
			return keywords;
		}

		// Remove quotes id there's an uneven count of them.

		int quoteCount = StringUtils.countMatches(keywords, "\"");

		if ((quoteCount % 2) != 0) {
			keywords = keywords.replaceAll("\"", "");
		}

		// Encode slashes.

		keywords = keywords.replaceAll("/", "&#8725;");
		keywords = keywords.replaceAll("\\\\", "&#92;");

		return keywords;
	}

	@Reference
	private MisspellingService _misspellingService;

}