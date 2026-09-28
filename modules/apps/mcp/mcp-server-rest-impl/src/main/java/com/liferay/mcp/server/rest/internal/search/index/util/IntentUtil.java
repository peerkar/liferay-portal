/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.mcp.server.rest.internal.search.index.util;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LinkedHashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Petteri Karttunen
 */
public class IntentUtil {

	public static final String INTENT_CREATE = "create";

	public static final String INTENT_LIST = "list";

	public static final String INTENT_READ = "read";

	public static String[] getIntentVerbs(String intent) {
		return _intentVerbs.getOrDefault(intent, new String[0]);
	}

	public static Set<String> getOperationActionSuffixes() {
		return _operationActionSuffixIntents.keySet();
	}

	public static String getOperationIntent(
		boolean collection, String method, String operationActionSuffix) {

		if (operationActionSuffix != null) {
			return _operationActionSuffixIntents.get(operationActionSuffix);
		}

		if (Objects.equals(method, "get")) {
			if (collection) {
				return INTENT_LIST;
			}

			return INTENT_READ;
		}

		return _methodIntents.getOrDefault(method, StringPool.BLANK);
	}

	public static List<String> getOtherIntents(List<String> searchIntents) {
		return ListUtil.filter(
			_allIntents, intent -> !searchIntents.contains(intent));
	}

	public static List<String> getSearchIntents(String search) {
		String[] words = StringUtil.split(
			StringUtil.toLowerCase(search), CharPool.SPACE);

		for (int i = 1; i < words.length; i++) {
			List<String> intents = _phraseIntents.get(
				words[i - 1] + StringPool.SPACE + words[i]);

			if (intents != null) {
				return intents;
			}
		}

		for (String word : words) {
			List<String> intents = _wordIntents.get(word);

			if (intents != null) {
				return intents;
			}
		}

		return Collections.emptyList();
	}

	private static final String _INTENT_COPY = "copy";

	private static final String _INTENT_DELETE = "delete";

	private static final String _INTENT_DEPLOY = "deploy";

	private static final String _INTENT_MOVE = "move";

	private static final String _INTENT_PUBLISH = "publish";

	private static final String _INTENT_REPLACE = "replace";

	private static final String _INTENT_RESTORE = "restore";

	private static final String _INTENT_SUBSCRIBE = "subscribe";

	private static final String _INTENT_TRANSLATE = "translate";

	private static final String _INTENT_UNDEPLOY = "undeploy";

	private static final String _INTENT_UNPUBLISH = "unpublish";

	private static final String _INTENT_UNSUBSCRIBE = "unsubscribe";

	private static final String _INTENT_UPDATE = "update";

	private static final String _INTENT_VALIDATE = "validate";

	private static final List<String> _allIntents = Arrays.asList(
		_INTENT_COPY, INTENT_CREATE, _INTENT_DELETE, _INTENT_DEPLOY,
		INTENT_LIST, _INTENT_MOVE, _INTENT_PUBLISH, INTENT_READ,
		_INTENT_REPLACE, _INTENT_RESTORE, _INTENT_SUBSCRIBE, _INTENT_TRANSLATE,
		_INTENT_UNDEPLOY, _INTENT_UNPUBLISH, _INTENT_UNSUBSCRIBE,
		_INTENT_UPDATE, _INTENT_VALIDATE);
	private static final Map<String, String[]> _intentVerbs =
		HashMapBuilder.put(
			_INTENT_COPY, new String[] {"clone", "copy", "duplicate"}
		).put(
			_INTENT_DELETE, new String[] {"delete", "get rid of", "remove"}
		).put(
			_INTENT_DEPLOY, new String[] {"activate", "deploy"}
		).put(
			_INTENT_MOVE, new String[] {"move", "relocate", "transfer"}
		).put(
			_INTENT_PUBLISH, new String[] {"activate", "make live", "publish"}
		).put(
			_INTENT_REPLACE, new String[] {"overwrite", "replace", "set"}
		).put(
			_INTENT_RESTORE, new String[] {"recover", "restore", "undelete"}
		).put(
			_INTENT_SUBSCRIBE, new String[] {"follow", "subscribe", "watch"}
		).put(
			_INTENT_TRANSLATE, new String[] {"localize", "translate"}
		).put(
			_INTENT_UNDEPLOY, new String[] {"deactivate", "undeploy"}
		).put(
			_INTENT_UNPUBLISH, new String[] {"expire", "retire", "unpublish"}
		).put(
			_INTENT_UNSUBSCRIBE,
			new String[] {"unfollow", "unsubscribe", "unwatch"}
		).put(
			_INTENT_UPDATE,
			new String[] {"change", "edit", "modify", "rename", "update"}
		).put(
			_INTENT_VALIDATE, new String[] {"check", "validate", "verify"}
		).put(
			INTENT_CREATE,
			new String[] {
				"add", "create", "make", "start", "submit", "upload", "write"
			}
		).put(
			INTENT_LIST,
			new String[] {"browse", "find", "get all", "list", "see", "show"}
		).put(
			INTENT_READ,
			new String[] {"fetch", "get", "look up", "open", "read", "view"}
		).build();
	private static final Map<String, String> _methodIntents =
		HashMapBuilder.put(
			"delete", _INTENT_DELETE
		).put(
			"patch", _INTENT_UPDATE
		).put(
			"post", INTENT_CREATE
		).put(
			"put", _INTENT_REPLACE
		).build();
	private static final Map<String, String> _operationActionSuffixIntents =
		LinkedHashMapBuilder.put(
			"TranslationLanguage", _INTENT_TRANSLATE
		).put(
			"CopyReplace", _INTENT_COPY
		).put(
			"MoveReplace", _INTENT_MOVE
		).put(
			"Unsubscribe", _INTENT_UNSUBSCRIBE
		).put(
			"Translation", _INTENT_TRANSLATE
		).put(
			"Subscribe", _INTENT_SUBSCRIBE
		).put(
			"Undeploy", _INTENT_UNDEPLOY
		).put(
			"Validate", _INTENT_VALIDATE
		).put(
			"Restore", _INTENT_RESTORE
		).put(
			"Publish", _INTENT_PUBLISH
		).put(
			"Expire", _INTENT_UNPUBLISH
		).put(
			"Deploy", _INTENT_DEPLOY
		).put(
			"Copy", _INTENT_COPY
		).put(
			"Move", _INTENT_MOVE
		).build();
	private static final Map<String, List<String>> _phraseIntents =
		HashMapBuilder.<String, List<String>>put(
			"get rid", Arrays.asList(_INTENT_DELETE)
		).put(
			"look up", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"set up", Arrays.asList(INTENT_CREATE)
		).put(
			"sign up", Arrays.asList(INTENT_CREATE)
		).build();
	private static final Map<String, List<String>> _wordIntents =
		HashMapBuilder.<String, List<String>>put(
			"add", Arrays.asList(INTENT_CREATE)
		).put(
			"assign", Arrays.asList(INTENT_CREATE)
		).put(
			"associate", Arrays.asList(INTENT_CREATE)
		).put(
			"attach", Arrays.asList(INTENT_CREATE)
		).put(
			"browse", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"change", Arrays.asList(_INTENT_REPLACE, _INTENT_UPDATE)
		).put(
			"check", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"clone", Arrays.asList(_INTENT_COPY)
		).put(
			"copy", Arrays.asList(_INTENT_COPY)
		).put(
			"create", Arrays.asList(INTENT_CREATE)
		).put(
			"delete", Arrays.asList(_INTENT_DELETE)
		).put(
			"deploy", Arrays.asList(_INTENT_DEPLOY)
		).put(
			"destroy", Arrays.asList(_INTENT_DELETE)
		).put(
			"detach", Arrays.asList(_INTENT_DELETE)
		).put(
			"disassociate", Arrays.asList(_INTENT_DELETE)
		).put(
			"duplicate", Arrays.asList(_INTENT_COPY)
		).put(
			"edit", Arrays.asList(_INTENT_REPLACE, _INTENT_UPDATE)
		).put(
			"expire", Arrays.asList(_INTENT_UNPUBLISH)
		).put(
			"fetch", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"find", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"follow", Arrays.asList(_INTENT_SUBSCRIBE)
		).put(
			"get", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"link", Arrays.asList(INTENT_CREATE)
		).put(
			"list", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"localize", Arrays.asList(_INTENT_TRANSLATE)
		).put(
			"make", Arrays.asList(INTENT_CREATE)
		).put(
			"modify", Arrays.asList(_INTENT_REPLACE, _INTENT_UPDATE)
		).put(
			"move", Arrays.asList(_INTENT_MOVE)
		).put(
			"new", Arrays.asList(INTENT_CREATE)
		).put(
			"open", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"overwrite", Arrays.asList(_INTENT_REPLACE)
		).put(
			"patch", Arrays.asList(_INTENT_UPDATE)
		).put(
			"place", Arrays.asList(INTENT_CREATE)
		).put(
			"post", Arrays.asList(INTENT_CREATE)
		).put(
			"publish", Arrays.asList(_INTENT_PUBLISH)
		).put(
			"put", Arrays.asList(_INTENT_REPLACE)
		).put(
			"read", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"recover", Arrays.asList(_INTENT_RESTORE)
		).put(
			"relate", Arrays.asList(INTENT_CREATE)
		).put(
			"relocate", Arrays.asList(_INTENT_MOVE)
		).put(
			"remove", Arrays.asList(_INTENT_DELETE)
		).put(
			"rename", Arrays.asList(_INTENT_REPLACE, _INTENT_UPDATE)
		).put(
			"replace", Arrays.asList(_INTENT_REPLACE)
		).put(
			"restore", Arrays.asList(_INTENT_RESTORE)
		).put(
			"retrieve", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"see", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"set", Arrays.asList(_INTENT_REPLACE, _INTENT_UPDATE)
		).put(
			"show", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"start", Arrays.asList(INTENT_CREATE)
		).put(
			"submit", Arrays.asList(INTENT_CREATE)
		).put(
			"subscribe", Arrays.asList(_INTENT_SUBSCRIBE)
		).put(
			"translate", Arrays.asList(_INTENT_TRANSLATE)
		).put(
			"unassign", Arrays.asList(_INTENT_DELETE)
		).put(
			"undeploy", Arrays.asList(_INTENT_UNDEPLOY)
		).put(
			"unfollow", Arrays.asList(_INTENT_UNSUBSCRIBE)
		).put(
			"unlink", Arrays.asList(_INTENT_DELETE)
		).put(
			"unpublish", Arrays.asList(_INTENT_UNPUBLISH)
		).put(
			"unsubscribe", Arrays.asList(_INTENT_UNSUBSCRIBE)
		).put(
			"update", Arrays.asList(_INTENT_REPLACE, _INTENT_UPDATE)
		).put(
			"upload", Arrays.asList(INTENT_CREATE)
		).put(
			"upsert", Arrays.asList(_INTENT_REPLACE)
		).put(
			"validate", Arrays.asList(_INTENT_VALIDATE)
		).put(
			"verify", Arrays.asList(_INTENT_VALIDATE)
		).put(
			"view", Arrays.asList(INTENT_LIST, INTENT_READ)
		).put(
			"write", Arrays.asList(INTENT_CREATE)
		).build();

}