/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.mcp.server.rest.internal.search.constants;

import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LinkedHashMapBuilder;
import com.liferay.portal.kernel.util.SetUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author Petteri Karttunen
 */
public class MCPSearchToolVocabulary {

	public static final String ENTITY_DEFINITION = "object definition";

	public static final String ENTITY_ENTRY = "object entry";

	public static final String INTENT_COPY = "copy";

	public static final String INTENT_CREATE = "create";

	public static final String INTENT_DELETE = "delete";

	public static final String INTENT_DEPLOY = "deploy";

	public static final String INTENT_LIST = "list";

	public static final String INTENT_MOVE = "move";

	public static final String INTENT_PUBLISH = "publish";

	public static final String INTENT_READ = "read";

	public static final String INTENT_REPLACE = "replace";

	public static final String INTENT_RESTORE = "restore";

	public static final String INTENT_SUBSCRIBE = "subscribe";

	public static final String INTENT_TRANSLATE = "translate";

	public static final String INTENT_UNDEPLOY = "undeploy";

	public static final String INTENT_UNPUBLISH = "unpublish";

	public static final String INTENT_UNSUBSCRIBE = "unsubscribe";

	public static final String INTENT_UPDATE = "update";

	public static final String INTENT_VALIDATE = "validate";

	public static final String[] INTENTS = {
		INTENT_COPY, INTENT_CREATE, INTENT_DELETE, INTENT_DEPLOY, INTENT_LIST,
		INTENT_MOVE, INTENT_PUBLISH, INTENT_READ, INTENT_REPLACE,
		INTENT_RESTORE, INTENT_SUBSCRIBE, INTENT_TRANSLATE, INTENT_UNDEPLOY,
		INTENT_UNPUBLISH, INTENT_UNSUBSCRIBE, INTENT_UPDATE, INTENT_VALIDATE
	};

	public static final String MODIFIER_APPROVED = "approved";

	public static final String MODIFIER_BATCH = "batch";

	public static final String MODIFIER_COPY = "copy";

	public static final String MODIFIER_EXPIRE = "expire";

	public static final String MODIFIER_HISTORY = "history";

	public static final String MODIFIER_KEYED = "keyed";

	public static final String MODIFIER_MOVE = "move";

	public static final String MODIFIER_NESTED = "nested";

	public static final String MODIFIER_OPENAPI = "openapi";

	public static final String MODIFIER_PERMISSIONS = "permissions";

	public static final String MODIFIER_PREVIEW = "preview";

	public static final String MODIFIER_RATING = "rating";

	public static final String MODIFIER_RESTORE = "restore";

	public static final String MODIFIER_SUBSCRIPTION = "subscription";

	public static final String MODIFIER_TRANSLATION = "translation";

	public static final String MODIFIER_TRAVERSAL = "traversal";

	public static final String MODIFIER_VALIDATE = "validate";

	public static final String OPENAPI_TOOL_SET_NAME = "openapi";

	public static final String[] RARELY_WANTED_MODIFIERS = {
		MODIFIER_HISTORY, MODIFIER_KEYED, MODIFIER_NESTED, MODIFIER_OPENAPI,
		MODIFIER_PERMISSIONS, MODIFIER_PREVIEW, MODIFIER_RATING,
		MODIFIER_SUBSCRIPTION
	};

	public static final String[] REFERENCE_SUFFIXES = {
		"ExternalReferenceCode", "Key", "Id"
	};

	public static final String[] RESHAPING_MODIFIERS = {
		MODIFIER_APPROVED, MODIFIER_COPY, MODIFIER_EXPIRE, MODIFIER_MOVE,
		MODIFIER_RESTORE, MODIFIER_TRANSLATION, MODIFIER_VALIDATE
	};

	public static final String[] SUCCESS_STATUSES = {"200", "201", "default"};

	public static final String[] VERBS_COLLECTION = {
		"list", "show", "browse", "find", "get all", "see"
	};

	public static final String[] VERBS_CREATE = {
		"create", "add", "make", "start", "upload", "write"
	};

	public static final String[] VERBS_DELETE = {
		"delete", "remove", "get rid of"
	};

	public static final String[] VERBS_REPLACE = {
		"replace", "set", "overwrite"
	};

	public static final String[] VERBS_SINGLE = {
		"get", "view", "open", "read", "fetch", "look up"
	};

	public static final String[] VERBS_UPDATE = {
		"update", "edit", "change", "rename", "modify"
	};

	public static final Map<String, String> actionIntents = HashMapBuilder.put(
		"Copy", INTENT_COPY
	).put(
		"CopyReplace", INTENT_COPY
	).put(
		"Deploy", INTENT_DEPLOY
	).put(
		"Expire", INTENT_UNPUBLISH
	).put(
		"Move", INTENT_MOVE
	).put(
		"MoveReplace", INTENT_MOVE
	).put(
		"Publish", INTENT_PUBLISH
	).put(
		"Restore", INTENT_RESTORE
	).put(
		"Subscribe", INTENT_SUBSCRIBE
	).put(
		"Translation", INTENT_TRANSLATE
	).put(
		"TranslationLanguage", INTENT_TRANSLATE
	).put(
		"Undeploy", INTENT_UNDEPLOY
	).put(
		"Unsubscribe", INTENT_UNSUBSCRIBE
	).put(
		"Validate", INTENT_VALIDATE
	).build();
	public static final Map<String, String[]> actionVerbs =
		LinkedHashMapBuilder.put(
			"TranslationLanguage", new String[] {"translate", "localize"}
		).put(
			"CopyReplace", new String[] {"copy", "duplicate", "clone"}
		).put(
			"MoveReplace", new String[] {"move", "relocate", "transfer"}
		).put(
			"Unsubscribe", new String[] {"unsubscribe", "unfollow", "unwatch"}
		).put(
			"Translation", new String[] {"translate", "localize"}
		).put(
			"Subscribe", new String[] {"subscribe", "follow", "watch"}
		).put(
			"Undeploy", new String[] {"undeploy", "deactivate"}
		).put(
			"Validate", new String[] {"validate", "check", "verify"}
		).put(
			"Restore", new String[] {"restore", "recover", "undelete"}
		).put(
			"Publish", new String[] {"publish", "activate", "make live"}
		).put(
			"Expire", new String[] {"expire", "unpublish", "retire"}
		).put(
			"Deploy", new String[] {"deploy", "activate"}
		).put(
			"Copy", new String[] {"copy", "duplicate", "clone"}
		).put(
			"Move", new String[] {"move", "relocate", "transfer"}
		).build();
	public static final Set<String> associationVerbs = SetUtil.fromArray(
		"add", "added", "adds", "put", "remove", "removed", "removes");
	public static final Set<String> associationWords = SetUtil.fromArray(
		"assign", "assigned", "assigning", "associate", "associated",
		"association", "attach", "attached", "detach", "disassociate", "link",
		"linked", "relate", "related", "unassign", "unlink");
	public static final Set<String> bulkWords = SetUtil.fromArray(
		"batch", "batches", "bulk", "dozen", "hundred", "many", "ten",
		"thousand", "twelve", "twenty");
	public static final Pattern entityPattern = Pattern.compile(
		"\\b(entity|entities)\\b", Pattern.CASE_INSENSITIVE);
	public static final Map<String, String> expansionTargetScopes =
		LinkedHashMapBuilder.put(
			"AssetLibrary", "in an asset library"
		).put(
			"DocumentFolder", "in a folder"
		).put(
			"KnowledgeBaseFolder", "in a folder"
		).put(
			"MessageBoardSection", "in a section"
		).put(
			"MessageBoardThread", "in a thread"
		).put(
			"ObjectDefinition", "on a custom object"
		).put(
			"StructuredContentFolder", "in a folder"
		).put(
			"TaxonomyVocabulary", "in a vocabulary"
		).put(
			"WikiNode", "in a wiki"
		).put(
			"Organization", "in an organization"
		).put(
			"Account", "on an account"
		).put(
			"Site", "in a site"
		).build();
	public static final Set<String> genericNouns = SetUtil.fromArray(
		"data", "detail", "details", "entries", "entry", "info", "information",
		"item", "items", "object", "objects", "record", "records", "row",
		"rows", "value", "values");
	public static final Set<String> headNounBoundaries = SetUtil.fromArray(
		"and", "as", "at", "belonging", "by", "for", "from", "in", "inside",
		"into", "of", "on", "onto", "that", "to", "under", "using", "via",
		"whose", "with", "within");
	public static final Map<String, List<String>> intentPhrases =
		HashMapBuilder.<String, List<String>>put(
			"get rid", Arrays.asList(INTENT_DELETE)
		).put(
			"look up", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"set up", Arrays.asList(INTENT_CREATE)
		).put(
			"sign up", Arrays.asList(INTENT_CREATE)
		).build();
	public static final Map<String, List<String>> intentsByWord =
		HashMapBuilder.<String, List<String>>put(
			"add", Arrays.asList(INTENT_CREATE)
		).put(
			"attach", Arrays.asList(INTENT_CREATE)
		).put(
			"browse", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"change", Arrays.asList(INTENT_UPDATE, INTENT_REPLACE)
		).put(
			"check", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"clone", Arrays.asList(INTENT_COPY)
		).put(
			"copy", Arrays.asList(INTENT_COPY)
		).put(
			"create", Arrays.asList(INTENT_CREATE)
		).put(
			"delete", Arrays.asList(INTENT_DELETE)
		).put(
			"deploy", Arrays.asList(INTENT_DEPLOY)
		).put(
			"destroy", Arrays.asList(INTENT_DELETE)
		).put(
			"duplicate", Arrays.asList(INTENT_COPY)
		).put(
			"edit", Arrays.asList(INTENT_UPDATE, INTENT_REPLACE)
		).put(
			"expire", Arrays.asList(INTENT_UNPUBLISH)
		).put(
			"fetch", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"find", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"follow", Arrays.asList(INTENT_SUBSCRIBE)
		).put(
			"get", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"list", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"localize", Arrays.asList(INTENT_TRANSLATE)
		).put(
			"make", Arrays.asList(INTENT_CREATE)
		).put(
			"modify", Arrays.asList(INTENT_UPDATE, INTENT_REPLACE)
		).put(
			"move", Arrays.asList(INTENT_MOVE)
		).put(
			"new", Arrays.asList(INTENT_CREATE)
		).put(
			"open", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"overwrite", Arrays.asList(INTENT_REPLACE)
		).put(
			"patch", Arrays.asList(INTENT_UPDATE)
		).put(
			"place", Arrays.asList(INTENT_CREATE)
		).put(
			"post", Arrays.asList(INTENT_CREATE)
		).put(
			"publish", Arrays.asList(INTENT_PUBLISH)
		).put(
			"put", Arrays.asList(INTENT_REPLACE)
		).put(
			"read", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"recover", Arrays.asList(INTENT_RESTORE)
		).put(
			"relocate", Arrays.asList(INTENT_MOVE)
		).put(
			"remove", Arrays.asList(INTENT_DELETE)
		).put(
			"rename", Arrays.asList(INTENT_UPDATE, INTENT_REPLACE)
		).put(
			"replace", Arrays.asList(INTENT_REPLACE)
		).put(
			"restore", Arrays.asList(INTENT_RESTORE)
		).put(
			"retrieve", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"see", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"set", Arrays.asList(INTENT_UPDATE, INTENT_REPLACE)
		).put(
			"show", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"start", Arrays.asList(INTENT_CREATE)
		).put(
			"submit", Arrays.asList(INTENT_CREATE)
		).put(
			"subscribe", Arrays.asList(INTENT_SUBSCRIBE)
		).put(
			"translate", Arrays.asList(INTENT_TRANSLATE)
		).put(
			"undeploy", Arrays.asList(INTENT_UNDEPLOY)
		).put(
			"unfollow", Arrays.asList(INTENT_UNSUBSCRIBE)
		).put(
			"unpublish", Arrays.asList(INTENT_UNPUBLISH)
		).put(
			"unsubscribe", Arrays.asList(INTENT_UNSUBSCRIBE)
		).put(
			"update", Arrays.asList(INTENT_UPDATE, INTENT_REPLACE)
		).put(
			"upload", Arrays.asList(INTENT_CREATE)
		).put(
			"upsert", Arrays.asList(INTENT_REPLACE)
		).put(
			"validate", Arrays.asList(INTENT_VALIDATE)
		).put(
			"verify", Arrays.asList(INTENT_VALIDATE)
		).put(
			"view", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"write", Arrays.asList(INTENT_CREATE)
		).build();
	public static final Map<String, String> methodIntents = HashMapBuilder.put(
		"delete", INTENT_DELETE
	).put(
		"patch", INTENT_UPDATE
	).put(
		"post", INTENT_CREATE
	).put(
		"put", INTENT_REPLACE
	).build();
	public static final Map<String, String> modifiers = HashMapBuilder.put(
		"approved", MODIFIER_APPROVED
	).put(
		"batch", MODIFIER_BATCH
	).put(
		"by-key", MODIFIER_KEYED
	).put(
		"by-uuid", MODIFIER_KEYED
	).put(
		"copy", MODIFIER_COPY
	).put(
		"expire", MODIFIER_EXPIRE
	).put(
		"export-batch", MODIFIER_BATCH
	).put(
		"export-preview", MODIFIER_PREVIEW
	).put(
		"friendly-url-history", MODIFIER_HISTORY
	).put(
		"import-preview", MODIFIER_PREVIEW
	).put(
		"move", MODIFIER_MOVE
	).put(
		"my-rating", MODIFIER_RATING
	).put(
		"openapi", MODIFIER_OPENAPI
	).put(
		"permissions", MODIFIER_PERMISSIONS
	).put(
		"preview", MODIFIER_PREVIEW
	).put(
		"rated-by-me", MODIFIER_RATING
	).put(
		"restore", MODIFIER_RESTORE
	).put(
		"subscribe", MODIFIER_SUBSCRIPTION
	).put(
		"translation", MODIFIER_TRANSLATION
	).put(
		"translations", MODIFIER_TRANSLATION
	).put(
		"unsubscribe", MODIFIER_SUBSCRIPTION
	).put(
		"validate", MODIFIER_VALIDATE
	).build();
	public static final Pattern pathParameterPattern = Pattern.compile(
		"\\{([^}]+)\\}");
	public static final Set<String> possessives = SetUtil.fromArray("me", "my");
	public static final Pattern referencePattern = Pattern.compile(
		"`([A-Z]\\w+)`");
	public static final Map<String, String[]> verbEntities = HashMapBuilder.put(
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
		new String[] {"article", "comment", "message", "note", "post", "text"}
	).build();

}