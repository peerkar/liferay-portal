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

package com.liferay.search.experiences.internal.blueprint.keyword;

import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.searcher.SearchRequestBuilder;
import com.liferay.search.experiences.blueprint.keyword.KeywordProcessor;

import java.util.stream.IntStream;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, property = "name=sanitize",
	service = KeywordProcessor.class
)
public class SanitizeKeywordProcessor implements KeywordProcessor {

	@Override
	public String process(
		String keywords, SearchRequestBuilder searchRequestBuilder) {

		return _clean(keywords);
	}

	private String _clean(String keywords) {
		keywords = StringUtil.trim(keywords);

		if (Validator.isBlank(keywords)) {
			return keywords;
		}

		// Remove quotes if there's an uneven count of them.

		IntStream intStream = keywords.chars();

		long quoteCount = intStream.filter(
			ch -> ch == '"'
		).count();

		if ((quoteCount % 2) != 0) {
			keywords = keywords.replaceAll("\"", "");
		}

		// Encode slashes.

		keywords = keywords.replaceAll("/", "&#8725;");
		keywords = keywords.replaceAll("\\\\", "&#92;");

		keywords = keywords.replaceAll("\"", "\\\\\"");

		keywords = keywords.replaceAll("\\[", "&#91;");
		keywords = keywords.replaceAll("\\]", "&#93;");

		return keywords;
	}

}