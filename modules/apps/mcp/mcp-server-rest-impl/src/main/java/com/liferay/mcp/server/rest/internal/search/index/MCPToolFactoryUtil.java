/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.mcp.server.rest.internal.search.index;

import com.liferay.mcp.server.rest.internal.search.index.constants.MCPToolConstants;
import com.liferay.mcp.server.rest.internal.search.index.util.IntentUtil;
import com.liferay.mcp.server.rest.internal.search.index.util.WordUtil;
import com.liferay.mcp.server.rest.internal.util.OpenAPIUtil;
import com.liferay.mcp.server.rest.internal.util.ToolSetUtil;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.vulcan.application.HeadlessApplicationProvider;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Petteri Karttunen
 */
public class MCPToolFactoryUtil {

	public static List<MCPTool> getMCPTools(
		HttpServletRequest httpServletRequest,
		HeadlessApplicationProvider.OpenAPIDocument openAPIDocument,
		String toolSetName) {

		List<MCPTool> mcpTools = new ArrayList<>();

		JSONObject openAPIJSONObject = ToolSetUtil.getOpenAPIJSONObject(
			httpServletRequest, toolSetName);

		JSONObject pathsJSONObject = openAPIJSONObject.getJSONObject("paths");

		if (pathsJSONObject == null) {
			return mcpTools;
		}

		HeadlessApplicationProvider.Application application =
			openAPIDocument.getApplication();

		for (String path : pathsJSONObject.keySet()) {
			JSONObject pathItemJSONObject = pathsJSONObject.getJSONObject(path);

			mcpTools.addAll(
				TransformUtil.transformToList(
					OpenAPIUtil.METHODS,
					method -> _getMCPTool(
						application, method, openAPIJSONObject, path,
						pathItemJSONObject, toolSetName)));
		}

		return mcpTools;
	}

	private static String[] _getApplicableVerbs(
		String entityWords, String[] verbs) {

		return TransformUtil.transform(
			verbs,
			verb -> {
				String[] verbEntityWords = _verbEntityWords.get(verb);

				if (verbEntityWords == null) {
					return verb;
				}

				for (String verbEntityWord : verbEntityWords) {
					if (StringUtil.contains(
							entityWords, verbEntityWord, StringPool.SPACE)) {

						return verb;
					}
				}

				return null;
			},
			String.class);
	}

	private static String _getDescription(JSONObject operationJSONObject) {
		String description = operationJSONObject.getString("description");
		String summary = operationJSONObject.getString("summary");

		if (!Validator.isBlank(description) && !Validator.isBlank(summary)) {
			if (StringUtil.endsWith(summary, CharPool.PERIOD)) {
				return summary + StringPool.SPACE + description;
			}

			return summary + ". " + description;
		}

		if (!Validator.isBlank(description)) {
			return description;
		}

		if (!Validator.isBlank(summary)) {
			return summary;
		}

		return StringPool.BLANK;
	}

	private static String _getEntityName(JSONObject operationJSONObject) {
		JSONArray tagsJSONArray = operationJSONObject.getJSONArray("tags");

		if (JSONUtil.isEmpty(tagsJSONArray)) {
			return StringPool.BLANK;
		}

		return tagsJSONArray.getString(0);
	}

	private static String[] _getExpansions(
		boolean batch, boolean collection, String entityWords, String intent,
		String operationActionSuffix, String path, String toolName) {

		entityWords = StringUtil.trim(entityWords);

		if (Validator.isNull(entityWords) || Validator.isNull(intent)) {
			return new String[0];
		}

		String toolNameRemainder = _removeMethodPrefix(toolName);

		boolean plural = batch || collection;

		if (collection) {
			toolNameRemainder = StringUtil.removeLast(
				toolNameRemainder, "Page");
		}

		if (operationActionSuffix != null) {
			toolNameRemainder = StringUtil.removeLast(
				toolNameRemainder, operationActionSuffix);

			plural = _isCollectionAction(path);
		}

		String parentEntityName = _getParentEntityName(
			entityWords, path, toolNameRemainder);

		if (parentEntityName != null) {
			toolNameRemainder = toolNameRemainder.substring(
				parentEntityName.length());
		}

		if (batch) {
			toolNameRemainder = StringUtil.removeLast(
				toolNameRemainder, "Batch");
		}

		NounPhrase nounPhrase = _getNounPhrase(
			entityWords, plural, toolNameRemainder);

		String parentPhrase = _getParentPhrase(parentEntityName);

		return TransformUtil.transform(
			_getApplicableVerbs(entityWords, IntentUtil.getIntentVerbs(intent)),
			verb -> _toExpansion(
				batch, nounPhrase, _getLastParameterPhrase(
					nounPhrase, parentPhrase, path), parentPhrase, verb),
			String.class);
	}

	private static String _getLastParameterPhrase(
		NounPhrase nounPhrase, String parentPhrase, String path) {

		String pathParameter = OpenAPIUtil.getLastPathParameter(path);

		if (pathParameter == null) {
			return StringPool.BLANK;
		}

		String pathParameterWords = WordUtil.toWords(pathParameter);

		if (StringUtil.contains(
			nounPhrase.toString(), pathParameterWords, StringPool.SPACE)) {

			return StringPool.BLANK;
		}

		for (String word :
				StringUtil.split(pathParameterWords, CharPool.SPACE)) {

			if (StringUtil.contains(parentPhrase, word, StringPool.SPACE)) {
				return StringPool.BLANK;
			}
		}

		return "by " + pathParameterWords;
	}

	private static MCPTool _getMCPTool(
		HeadlessApplicationProvider.Application application, String method,
		JSONObject openAPIJSONObject, String path,
		JSONObject pathItemJSONObject, String toolSetName) {

		JSONObject operationJSONObject = pathItemJSONObject.getJSONObject(
			method);

		if (operationJSONObject == null) {
			return null;
		}

		String toolName = operationJSONObject.getString("operationId");

		if (Validator.isBlank(toolName)) {
			return null;
		}

		String operationActionSuffix = _getOperationActionSuffix(
			method, path, toolName);

		String entityName = _getEntityName(operationJSONObject);

		String entityWords = WordUtil.toWords(entityName);

		String operationVariant = _getOperationVariant(entityName, path);

		if (Objects.equals(
				operationVariant,
				MCPToolConstants.OPERATION_VARIANT_TRAVERSAL)) {

			entityName = _getTraversalEntityName(entityName, path, toolName);
		}

		boolean batch = Objects.equals(
			operationVariant, MCPToolConstants.OPERATION_VARIANT_BATCH);

		boolean collection = OpenAPIUtil.isCollectionSchema(
			openAPIJSONObject,
			OpenAPIUtil.getResponseSchemaJSONObject(operationJSONObject));

		String intent = IntentUtil.getOperationIntent(
			collection, method, operationActionSuffix);

		return new MCPTool(
			collection, operationJSONObject.getBoolean("deprecated"),
			StringUtil.trim(_getDescription(operationJSONObject)), entityName,
			_getExpansions(
				batch, collection, entityWords, intent, operationActionSuffix, path,
				toolName), intent, method, operationVariant, _getParameterNames(
			operationJSONObject, pathItemJSONObject), Portal.PATH_MODULE + application.getBasePath() + path,
			_getRequiredReferences(
				openAPIJSONObject, operationJSONObject, path),
			_getSchemaPropertyNames(
			openAPIJSONObject, operationJSONObject), toolName, toolSetName);
	}

	private static NounPhrase _getNounPhrase(
		String entityWords, boolean plural, String toolNameRemainder) {

		NounPhrase nounPhrase = _getNounPhrase(entityWords, toolNameRemainder);

		if (!plural) {
			return nounPhrase;
		}

		return new NounPhrase(
			WordUtil.toPlural(nounPhrase._noun), nounPhrase._wordsBefore,
			nounPhrase._wordsAfter);
	}

	private static NounPhrase _getNounPhrase(
		String entityWords, String toolNameRemainder) {

		String[] words = StringUtil.split(
			WordUtil.toWords(toolNameRemainder), CharPool.SPACE);

		Set<String> singularEntityWords = new HashSet<>();

		for (String word : StringUtil.split(entityWords, CharPool.SPACE)) {
			singularEntityWords.add(WordUtil.toSingular(word));
		}

		int firstEntityWordIndex = words.length;
		int lastEntityWordIndex = words.length;

		for (int i = 0; i < words.length; i++) {
			if (!singularEntityWords.contains(WordUtil.toSingular(words[i]))) {
				continue;
			}

			if (firstEntityWordIndex == words.length) {
				firstEntityWordIndex = i;
			}

			lastEntityWordIndex = i;
		}

		List<String> wordsAfterEntity = new ArrayList<>();
		List<String> wordsBeforeEntity = new ArrayList<>();
		List<String> wordsBetweenEntityWords = new ArrayList<>();

		for (int i = 0; i < words.length; i++) {
			if (i < firstEntityWordIndex) {
				wordsBeforeEntity.add(words[i]);
			}
			else if (i > lastEntityWordIndex) {
				wordsAfterEntity.add(words[i]);
			}
			else if (!singularEntityWords.contains(
						WordUtil.toSingular(words[i]))) {

				wordsBetweenEntityWords.add(words[i]);
			}
		}

		String wordsBefore = StringUtil.merge(
			wordsBeforeEntity, StringPool.SPACE);

		if (StringUtil.startsWith(wordsBefore, "by ")) {
			return new NounPhrase(entityWords, StringPool.BLANK, wordsBefore);
		}

		if (firstEntityWordIndex == words.length) {
			return _getToolNameNounPhrase(wordsBeforeEntity);
		}

		String wordsAfter = StringUtil.merge(
			wordsAfterEntity, StringPool.SPACE);

		String wordsBetween = StringUtil.merge(
			wordsBetweenEntityWords, StringPool.SPACE);

		if (Validator.isNull(wordsAfter) &&
			StringUtil.startsWith(wordsBetween, "by ")) {

			wordsAfter = wordsBetween;
		}

		if (Validator.isNull(wordsAfter)) {
			return new NounPhrase(entityWords, wordsBefore, StringPool.BLANK);
		}

		if (StringUtil.startsWith(wordsAfter, "by ") ||
			!WordUtil.isPlural(wordsAfter)) {

			return new NounPhrase(entityWords, wordsBefore, wordsAfter);
		}

		return new NounPhrase(
			wordsAfter,
			StringUtil.trim(wordsBefore + StringPool.SPACE + entityWords),
			StringPool.BLANK);
	}

	private static String _getOperationActionSuffix(
		String method, String path, String toolName) {

		if (Objects.equals(method, "get") || Objects.equals(method, "head") ||
			Objects.equals(method, "options")) {

			return null;
		}

		for (String operationActionSuffix :
				IntentUtil.getOperationActionSuffixes()) {

			if (toolName.endsWith(operationActionSuffix) &&
				_hasOperationActionSuffixSegment(operationActionSuffix, path)) {

				return operationActionSuffix;
			}
		}

		return null;
	}

	private static String _getOperationVariant(String entityName, String path) {
		String operationVariant = _getPathSegmentOperationVariant(path);

		if (Validator.isNotNull(operationVariant)) {
			return operationVariant;
		}

		if (_isTraversal(entityName, path)) {
			return MCPToolConstants.OPERATION_VARIANT_TRAVERSAL;
		}

		return StringPool.BLANK;
	}

	private static List<String> _getParameterNames(
		JSONArray parametersJSONArray) {

		List<String> parameterNames = new ArrayList<>();

		if (parametersJSONArray == null) {
			return parameterNames;
		}

		for (int i = 0; i < parametersJSONArray.length(); i++) {
			JSONObject parameterJSONObject = parametersJSONArray.getJSONObject(
				i);

			if (parameterJSONObject == null) {
				continue;
			}

			String name = parameterJSONObject.getString("name");

			if (Validator.isNotNull(name)) {
				parameterNames.add(name);
			}
		}

		return parameterNames;
	}

	private static String[] _getParameterNames(
		JSONObject operationJSONObject, JSONObject pathItemJSONObject) {

		Set<String> parameters = new LinkedHashSet<>(
			_getParameterNames(operationJSONObject.getJSONArray("parameters")));

		parameters.addAll(
			_getParameterNames(pathItemJSONObject.getJSONArray("parameters")));

		return parameters.toArray(new String[0]);
	}

	private static String _getParentCollectionSegment(String path) {
		String[] segments = StringUtil.split(path, CharPool.SLASH);

		for (int i = 1; i < (segments.length - 1); i++) {
			if (!OpenAPIUtil.isPathParameter(segments[i])) {
				continue;
			}

			String segment = segments[i - 1];

			if (StringUtil.startsWith(segment, "by-") && (i > 1)) {
				segment = segments[i - 2];
			}

			if (Validator.isNull(segment) ||
				OpenAPIUtil.isPathParameter(segment) ||
				StringUtil.startsWith(segment, "by-")) {

				return null;
			}

			return segment;
		}

		return null;
	}

	private static String _getParentEntityName(
		String entityWords, String path, String toolNameRemainder) {

		String parentCollectionSegment = _getParentCollectionSegment(path);

		if (parentCollectionSegment == null) {
			return null;
		}

		String parentEntityName = _toEntityName(parentCollectionSegment);

		if (!toolNameRemainder.startsWith(parentEntityName)) {
			return null;
		}

		int length = parentEntityName.length();

		if ((toolNameRemainder.length() <= length) ||
			!Character.isUpperCase(toolNameRemainder.charAt(length)) ||
			StringUtil.contains(
				entityWords, WordUtil.toWords(parentEntityName),
				StringPool.SPACE)) {

			return null;
		}

		return parentEntityName;
	}

	private static String _getParentPhrase(String parentEntityName) {
		if (parentEntityName == null) {
			return StringPool.BLANK;
		}

		String parentEntityWords = WordUtil.toWords(parentEntityName);

		String article = "a";

		if (StringUtil.startsWith(parentEntityWords, "a") ||
			StringUtil.startsWith(parentEntityWords, "e") ||
			StringUtil.startsWith(parentEntityWords, "i") ||
			StringUtil.startsWith(parentEntityWords, "o") ||
			StringUtil.startsWith(parentEntityWords, "u")) {

			article = "an";
		}

		return StringBundler.concat(
			"in ", article, StringPool.SPACE, parentEntityWords);
	}

	private static String _getPathSegmentOperationVariant(String path) {
		String[] segments = StringUtil.split(path, CharPool.SLASH);

		for (int i = segments.length - 1; i >= 0; i--) {
			String segment = StringUtil.toLowerCase(segments[i]);

			if (Validator.isNull(segment) ||
				OpenAPIUtil.isPathParameter(segment)) {

				continue;
			}

			int index = segment.indexOf(CharPool.PERIOD);

			if (index > 0) {
				segment = segment.substring(0, index);
			}

			String operationVariant =
				MCPToolConstants.pathSegmentOperationVariants.get(
					StringUtil.removeLast(segment, "-replace"));

			if (operationVariant != null) {
				return operationVariant;
			}
		}

		int byExternalReferenceCodeSegmentsCount = 0;

		for (String segment : segments) {
			if (StringUtil.startsWith(
					StringUtil.toLowerCase(segment), "by-external")) {

				byExternalReferenceCodeSegmentsCount++;
			}
		}

		if (byExternalReferenceCodeSegmentsCount > 1) {
			return MCPToolConstants.OPERATION_VARIANT_NESTED;
		}

		return StringPool.BLANK;
	}

	private static Map<String, JSONObject> _getProperties(
		JSONObject openAPIJSONObject, JSONObject schemaJSONObject) {

		Map<String, JSONObject> properties = new LinkedHashMap<>();

		Map<String, JSONObject> schemaProperties = OpenAPIUtil.getProperties(
			openAPIJSONObject, schemaJSONObject);

		for (Map.Entry<String, JSONObject> entry :
				schemaProperties.entrySet()) {

			String propertyName = entry.getKey();

			if (propertyName.startsWith("x-") ||
				_isNestedSchema(entry.getValue())) {

				continue;
			}

			properties.put(propertyName, entry.getValue());
		}

		return properties;
	}

	private static String _getReferenceCollectionSegment(String propertyName) {
		String referencedEntityName = null;

		for (String suffix : _REFERENCE_PROPERTY_NAME_SUFFIXES) {
			if (propertyName.endsWith(suffix) &&
				(propertyName.length() > suffix.length())) {

				referencedEntityName = propertyName.substring(
					0, propertyName.length() - suffix.length());

				break;
			}
		}

		if (referencedEntityName == null) {
			return null;
		}

		return StringUtil.replace(
			WordUtil.toPlural(StringUtil.trim(WordUtil.toWords(referencedEntityName))),
			CharPool.SPACE, CharPool.DASH);
	}

	private static String[] _getRequiredReferences(
		JSONObject openAPIJSONObject, JSONObject operationJSONObject,
		String path) {

		JSONObject bodySchemaJSONObject =
			OpenAPIUtil.getRequestBodySchemaJSONObject(operationJSONObject);

		if (bodySchemaJSONObject == null) {
			return new String[0];
		}

		Map<String, JSONObject> properties = _getProperties(
			openAPIJSONObject, bodySchemaJSONObject);

		return TransformUtil.transformToArray(
			OpenAPIUtil.getRequiredPropertyNames(
				openAPIJSONObject, bodySchemaJSONObject),
			propertyName -> {
				if (path.contains("{" + propertyName + "}") ||
					!properties.containsKey(propertyName)) {

					return null;
				}

				String collectionSegment = _getReferenceCollectionSegment(
					propertyName);

				if (collectionSegment == null) {
					return null;
				}

				return propertyName + StringPool.POUND + collectionSegment;
			},
			String.class);
	}

	private static String[] _getSchemaPropertyNames(
		JSONObject openAPIJSONObject, JSONObject operationJSONObject) {

		Map<String, JSONObject> properties = _getProperties(
			openAPIJSONObject,
			OpenAPIUtil.getRequestBodySchemaJSONObject(operationJSONObject));

		Set<String> propertyNames = new LinkedHashSet<>(properties.keySet());

		properties = _getProperties(
			openAPIJSONObject,
			OpenAPIUtil.getResponseSchemaJSONObject(operationJSONObject));

		propertyNames.addAll(properties.keySet());

		return propertyNames.toArray(new String[0]);
	}

	private static NounPhrase _getToolNameNounPhrase(List<String> words) {
		int index = words.indexOf("by");

		if (index < 0) {
			return new NounPhrase(
				StringUtil.merge(words, StringPool.SPACE), StringPool.BLANK,
				StringPool.BLANK);
		}

		return new NounPhrase(
			StringUtil.merge(words.subList(0, index), StringPool.SPACE),
			StringPool.BLANK,
			StringUtil.merge(
				words.subList(index, words.size()), StringPool.SPACE));
	}

	private static String _getTraversalEntityName(
		String entityName, String path, String toolName) {

		if (!StringUtil.contains(
				_toSingularWords(WordUtil.toWords(toolName)),
				_toSingularWords(WordUtil.toWords(entityName)),
				StringPool.SPACE)) {

			return entityName;
		}

		String[] segments = StringUtil.split(path, CharPool.SLASH);

		for (int i = segments.length - 1; i >= 0; i--) {
			String segment = segments[i];

			if (OpenAPIUtil.isPathParameter(segment) ||
				StringUtil.startsWith(segment, "by-") ||
				!_isCollectionSegment(segment)) {

				continue;
			}

			return _toEntityName(segment);
		}

		return entityName;
	}

	private static boolean _hasOperationActionSuffixSegment(
		String operationActionSuffix, String path) {

		String normalizedOperationActionSuffix = WordUtil.normalize(
			operationActionSuffix);

		for (String segment : StringUtil.split(path, CharPool.SLASH)) {
			if (Validator.isNull(segment) ||
				OpenAPIUtil.isPathParameter(segment)) {

				continue;
			}

			int index = segment.indexOf(CharPool.PERIOD);

			if (index > 0) {
				segment = segment.substring(0, index);
			}

			String normalizedSegment = WordUtil.normalize(segment);

			if (normalizedSegment.equals(normalizedOperationActionSuffix) ||
				normalizedSegment.endsWith(normalizedOperationActionSuffix) ||
				normalizedOperationActionSuffix.startsWith(
					WordUtil.toSingular(normalizedSegment))) {

				return true;
			}
		}

		return false;
	}

	private static boolean _isCollectionAction(String path) {
		String[] segments = StringUtil.split(path, CharPool.SLASH);

		if (segments.length < 2) {
			return false;
		}

		return _isCollectionSegment(segments[segments.length - 2]);
	}

	private static boolean _isCollectionSegment(String segment) {
		if (StringUtil.startsWith(segment, "has-") ||
			StringUtil.startsWith(segment, "is-")) {

			return false;
		}

		return WordUtil.isPlural(segment);
	}

	private static boolean _isEntitySegment(String entityName, String segment) {
		String entityWords = _toSingularWords(WordUtil.toWords(entityName));
		String segmentWords = _toSingularWords(
			WordUtil.toWords(_toEntityName(segment)));

		return StringUtil.endsWith(
			StringPool.SPACE + entityWords, StringPool.SPACE + segmentWords);
	}

	private static boolean _isNestedSchema(JSONObject propertyJSONObject) {
		if (propertyJSONObject == null) {
			return false;
		}

		if (Validator.isNotNull(propertyJSONObject.getString("$ref"))) {
			return true;
		}

		JSONObject itemsJSONObject = propertyJSONObject.getJSONObject("items");

		if ((itemsJSONObject != null) &&
			Validator.isNotNull(itemsJSONObject.getString("$ref"))) {

			return true;
		}

		return false;
	}

	private static boolean _isTraversal(String entityName, String path) {
		if (Validator.isNull(entityName)) {
			return false;
		}

		List<String> segmentsAfterParameter = new ArrayList<>();

		boolean parameterSeen = false;

		for (String segment : StringUtil.split(path, CharPool.SLASH)) {
			if (Validator.isNull(segment)) {
				continue;
			}

			if (OpenAPIUtil.isPathParameter(segment)) {
				parameterSeen = true;

				continue;
			}

			if (parameterSeen) {
				segmentsAfterParameter.add(segment);
			}
		}

		if (segmentsAfterParameter.isEmpty()) {
			return false;
		}

		String segment = segmentsAfterParameter.get(
			segmentsAfterParameter.size() - 1);

		if (_isEntitySegment(entityName, segment)) {
			return false;
		}

		if (_isCollectionSegment(segment)) {
			return true;
		}

		if (segmentsAfterParameter.size() < 2) {
			return false;
		}

		segment = segmentsAfterParameter.get(segmentsAfterParameter.size() - 2);

		if (_isCollectionSegment(segment) &&
			!_isEntitySegment(entityName, segment)) {

			return true;
		}

		return false;
	}

	private static String _removeMethodPrefix(String toolName) {
		Matcher matcher = _methodPrefixPattern.matcher(toolName);

		return matcher.replaceFirst(StringPool.BLANK);
	}

	private static String _toEntityName(String segment) {
		StringBundler sb = new StringBundler();

		for (String word :
				StringUtil.split(WordUtil.toSingular(segment), CharPool.DASH)) {

			sb.append(StringUtil.upperCaseFirstLetter(word));
		}

		return sb.toString();
	}

	private static String _toExpansion(
		boolean batch, NounPhrase nounPhrase, String parameterPhrase,
		String parentPhrase, String verb) {

		StringBundler sb = new StringBundler(12);

		if (batch) {
			sb.append("batch ");
		}

		sb.append(verb);
		sb.append(StringPool.SPACE);

		if (Validator.isNotNull(nounPhrase._wordsBefore)) {
			sb.append(nounPhrase._wordsBefore);
			sb.append(StringPool.SPACE);
		}

		sb.append(nounPhrase._noun);

		if (Validator.isNotNull(parentPhrase)) {
			sb.append(StringPool.SPACE);
			sb.append(parentPhrase);
		}

		if (Validator.isNotNull(nounPhrase._wordsAfter)) {
			sb.append(StringPool.SPACE);
			sb.append(nounPhrase._wordsAfter);
		}

		if (Validator.isNotNull(parameterPhrase)) {
			sb.append(StringPool.SPACE);
			sb.append(parameterPhrase);
		}

		return sb.toString();
	}

	private static String _toSingularWords(String words) {
		StringBundler sb = new StringBundler();

		for (String word : StringUtil.split(words, CharPool.SPACE)) {
			sb.append(WordUtil.toSingular(word));
			sb.append(StringPool.SPACE);
		}

		return StringUtil.trim(sb.toString());
	}

	private static final String[] _REFERENCE_PROPERTY_NAME_SUFFIXES = {
		"ExternalReferenceCode", "ExternalReferenceCodes", "Id", "Ids", "Key",
		"Keys"
	};

	private static final Pattern _methodPrefixPattern = Pattern.compile(
		StringBundler.concat(
			"^(", StringUtil.merge(OpenAPIUtil.METHODS, StringPool.PIPE), ")"));
	private static final Map<String, String[]> _verbEntityWords =
		HashMapBuilder.put(
			"start",
			new String[] {
				"conversation", "discussion", "instance", "process", "task",
				"thread"
			}
		).put(
			"submit", new String[] {"form", "request", "task", "workflow"}
		).put(
			"upload",
			new String[] {
				"attachment", "document", "file", "image", "logo", "media",
				"picture", "thumbnail", "video"
			}
		).put(
			"write",
			new String[] {
				"article", "comment", "message", "note", "post", "posting",
				"text"
			}
		).build();

	private static class NounPhrase {

		@Override
		public String toString() {
			return StringUtil.trim(
				StringBundler.concat(
					_wordsBefore, StringPool.SPACE, _noun, StringPool.SPACE,
					_wordsAfter));
		}

		private NounPhrase(String noun, String wordsBefore, String wordsAfter) {
			_noun = noun;
			_wordsBefore = wordsBefore;
			_wordsAfter = wordsAfter;
		}

		private final String _noun;
		private final String _wordsAfter;
		private final String _wordsBefore;

	}

}