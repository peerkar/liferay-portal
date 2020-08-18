/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.source.formatter.checks;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.tools.ToolsUtil;
import com.liferay.source.formatter.checks.util.JSPSourceUtil;

import java.io.IOException;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hugo Huijser
 */
public class JSPTaglibVariableCheck extends BaseJSPTermsCheck {

	@Override
	protected String doProcess(
			String fileName, String absolutePath, String content)
		throws IOException {

		return _formatTaglibVariable(fileName, content);
	}

	private String _formatTaglibVariable(String fileName, String content)
		throws IOException {

		Matcher matcher = _taglibVariablePattern.matcher(content);

		while (matcher.find()) {
			String nextTag = matcher.group(5);
			String taglibValue = matcher.group(3);
			String variableName = matcher.group(2);

			if (!taglibValue.contains("\n") &&
				(taglibValue.contains("\\\"") ||
				 (taglibValue.contains(StringPool.APOSTROPHE) &&
				  taglibValue.contains(StringPool.QUOTE)))) {

				if (!variableName.startsWith("taglib") &&
					(_getVariableCount(content, variableName) == 2) &&
					nextTag.contains("=\"<%= " + variableName + " %>\"")) {

					addMessage(
						fileName,
						"Variable '" + variableName +
							"' should start with 'taglib'",
						getLineNumber(content, matcher.start(1)));
				}

				continue;
			}

			if (nextTag.contains("=\"<%= " + variableName + " %>\"")) {
				populateContentsMap(fileName, content);

				String newContent = StringUtil.replaceFirst(
					content, "<%= " + variableName + " %>\"",
					"<%= " + taglibValue + " %>\"", matcher.start(5));

				Set<String> checkedFileNames = new HashSet<>();
				Set<String> includeFileNames = new HashSet<>();

				if (hasUnusedJSPTerm(
						fileName, newContent, "\\W" + variableName + "\\W",
						"variable", checkedFileNames, includeFileNames,
						getContentsMap())) {

					if (!taglibValue.contains("\n")) {
						return StringUtil.replaceFirst(
							newContent, matcher.group(1), StringPool.BLANK,
							matcher.start());
					}

					addMessage(
						fileName,
						StringBundler.concat(
							"No need to declare variable '", variableName,
							"', inline inside the tag."),
						getLineNumber(content, matcher.start(2)));
				}
			}
		}

		return content;
	}

	private int _getVariableCount(String content, String variableName) {
		int count = 0;

		Pattern pattern = Pattern.compile("\\W" + variableName + "\\W");

		Matcher matcher = pattern.matcher(content);

		while (matcher.find()) {
			int x = matcher.start() + 1;

			if (JSPSourceUtil.isJavaSource(content, x)) {
				if (!ToolsUtil.isInsideQuotes(content, x)) {
					count++;
				}

				continue;
			}

			if (JSPSourceUtil.isJavaSource(content, x, true)) {
				count++;
			}
		}

		return count;
	}

	private static final Pattern _taglibVariablePattern = Pattern.compile(
		"\n(\t*[\\w<>\\[\\],\\? ]+ (\\w+) = ([^{]((?!;\n).)*);)\n\\s*%>\\s+" +
			"(<[\\S\\s]*?>)(\n|\\Z)",
		Pattern.DOTALL);

}