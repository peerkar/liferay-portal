/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.mcp.server.rest.internal.search.index.util;

import com.liferay.mcp.server.rest.internal.search.constants.MCPSearchToolVocabulary;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * Generates phrases a tool is found by.
 *
 * @author Petteri Karttunen
 */
public class MCPToolExpansionUtil {

	public static String[] getExpansion(
		boolean batch, String marker, String method, String path, String tags,
		String toolName) {

		String entity = StringUtil.toLowerCase(tags.trim());

		if (Validator.isNull(entity) || Objects.equals(method, "head") ||
			Objects.equals(method, "options")) {

			return new String[0];
		}

		boolean collection = toolName.endsWith("Page");

		if (marker != null) {
			return _getActionExpansion(entity, marker, path, toolName);
		}

		String[] verbs = MCPSearchToolVocabulary.VERBS_DELETE;

		if (Objects.equals(method, "get")) {
			verbs = collection ? MCPSearchToolVocabulary.VERBS_COLLECTION :
				MCPSearchToolVocabulary.VERBS_SINGLE;
		}
		else if (Objects.equals(method, "patch")) {
			verbs = MCPSearchToolVocabulary.VERBS_UPDATE;
		}
		else if (Objects.equals(method, "post")) {
			verbs = MCPSearchToolVocabulary.VERBS_CREATE;
		}
		else if (Objects.equals(method, "put")) {
			verbs = MCPSearchToolVocabulary.VERBS_REPLACE;
		}

		String remainder = toolName.replaceFirst(
			"^(delete|get|head|options|patch|post|put)", StringPool.BLANK);

		String scope = StringPool.BLANK;

		for (Map.Entry<String, String> entry :
				MCPSearchToolVocabulary.expansionTargetScopes.entrySet()) {

			String prefix = entry.getKey();

			if (!remainder.startsWith(prefix)) {
				continue;
			}

			String rest = remainder.substring(prefix.length());

			if (rest.isEmpty() || !Character.isUpperCase(rest.charAt(0))) {
				continue;
			}

			remainder = rest;
			scope = entry.getValue();

			break;
		}

		if (batch) {
			remainder = StringUtil.removeLast(remainder, "Batch");
		}

		NounPhrase nounPhrase = _getNounPhrase(entity, remainder);

		String head = nounPhrase._head;
		String prefix = nounPhrase._prefix;
		String suffix = nounPhrase._suffix;

		if (batch || collection) {
			head = MCPToolWordUtil.toPlural(head);
		}

		String parameterSuffix = _getParameterSuffix(
			path, StringBundler.concat(prefix, " ", head, " ", suffix), scope);

		verbs = _getApplicableVerbs(entity, verbs);

		List<String> expansions = new ArrayList<>(verbs.length);

		for (String verb : verbs) {
			StringBundler sb = new StringBundler(12);

			if (batch) {
				sb.append("batch ");
			}

			sb.append(verb);
			sb.append(StringPool.SPACE);

			if (Validator.isNotNull(prefix)) {
				sb.append(prefix);
				sb.append(StringPool.SPACE);
			}

			sb.append(head);

			if (Validator.isNotNull(scope)) {
				sb.append(StringPool.SPACE);
				sb.append(scope);
			}

			if (Validator.isNotNull(suffix)) {
				sb.append(StringPool.SPACE);
				sb.append(suffix);
			}

			if (Validator.isNotNull(parameterSuffix)) {
				sb.append(StringPool.SPACE);
				sb.append(parameterSuffix);
			}

			expansions.add(sb.toString());
		}

		return expansions.toArray(new String[0]);
	}

	private static String[] _getActionExpansion(
		String entity, String marker, String path, String toolName) {

		String remainder = StringUtil.removeLast(
			toolName.replaceFirst(
				"^(delete|get|head|options|patch|post|put)", StringPool.BLANK),
			marker);

		NounPhrase nounPhrase = _getNounPhrase(entity, remainder);

		String head = nounPhrase._head;
		String prefix = nounPhrase._prefix;
		String suffix = nounPhrase._suffix;

		String parameterSuffix = _getParameterSuffix(
			path, StringBundler.concat(prefix, " ", head, " ", suffix),
			StringPool.BLANK);

		List<String> expansions = new ArrayList<>();

		for (String verb : MCPSearchToolVocabulary.actionVerbs.get(marker)) {
			StringBundler sb = new StringBundler(9);

			sb.append(verb);
			sb.append(StringPool.SPACE);

			if (Validator.isNotNull(prefix)) {
				sb.append(prefix);
				sb.append(StringPool.SPACE);
			}

			sb.append(head);

			if (Validator.isNotNull(suffix)) {
				sb.append(StringPool.SPACE);
				sb.append(suffix);
			}

			if (Validator.isNotNull(parameterSuffix)) {
				sb.append(StringPool.SPACE);
				sb.append(parameterSuffix);
			}

			expansions.add(sb.toString());
		}

		return expansions.toArray(new String[0]);
	}

	private static String[] _getApplicableVerbs(String entity, String[] verbs) {
		List<String> applicableVerbs = new ArrayList<>(verbs.length);

		for (String verb : verbs) {
			String[] keywords = MCPSearchToolVocabulary.verbEntities.get(verb);

			if (keywords == null) {
				applicableVerbs.add(verb);

				continue;
			}

			for (String keyword : keywords) {
				if (entity.contains(keyword)) {
					applicableVerbs.add(verb);

					break;
				}
			}
		}

		return applicableVerbs.toArray(new String[0]);
	}

	private static NounPhrase _getNounPhrase(String entity, String remainder) {
		String humanizedRemainder = StringUtil.toLowerCase(
			MCPToolWordUtil.humanize(StringUtil.removeLast(remainder, "Page")));

		Set<String> entityWords = new HashSet<>();

		for (String word : StringUtil.split(entity, CharPool.SPACE)) {
			entityWords.add(MCPToolWordUtil.toSingular(word));
		}

		StringBundler afterSB = new StringBundler();
		StringBundler beforeSB = new StringBundler();
		boolean seen = false;

		for (String word :
				StringUtil.split(humanizedRemainder, CharPool.SPACE)) {

			if (entityWords.contains(MCPToolWordUtil.toSingular(word))) {
				seen = true;

				continue;
			}

			if (!seen) {
				beforeSB.append(word);
				beforeSB.append(StringPool.SPACE);
			}
			else if (!MCPSearchToolVocabulary.possessives.contains(word)) {
				afterSB.append(word);
				afterSB.append(StringPool.SPACE);
			}
		}

		String before = StringUtil.trim(beforeSB.toString());

		if (StringUtil.startsWith(before, "by ")) {
			return new NounPhrase(entity, StringPool.BLANK, before);
		}

		String after = StringUtil.trim(afterSB.toString());

		if (Validator.isNull(after)) {
			return new NounPhrase(entity, before, StringPool.BLANK);
		}

		if (StringUtil.startsWith(after, "by ")) {
			return new NounPhrase(entity, before, after);
		}

		return new NounPhrase(
			after, StringUtil.trim(before + StringPool.SPACE + entity),
			StringPool.BLANK);
	}

	private static String _getParameterSuffix(
		String path, String qualifier, String scope) {

		Matcher matcher = MCPSearchToolVocabulary.pathParameterPattern.matcher(
			path);

		String parameter = null;

		while (matcher.find()) {
			parameter = matcher.group(1);
		}

		if (parameter == null) {
			return StringPool.BLANK;
		}

		String words = StringUtil.toLowerCase(
			MCPToolWordUtil.humanize(parameter));

		if (qualifier.contains(words)) {
			return StringPool.BLANK;
		}

		for (String word : StringUtil.split(words, CharPool.SPACE)) {
			if (scope.contains(word)) {
				return StringPool.BLANK;
			}
		}

		return "by " + words;
	}

	private static class NounPhrase {

		private NounPhrase(String head, String prefix, String suffix) {
			_head = head;
			_prefix = prefix;
			_suffix = suffix;
		}

		private final String _head;
		private final String _prefix;
		private final String _suffix;

	}

}