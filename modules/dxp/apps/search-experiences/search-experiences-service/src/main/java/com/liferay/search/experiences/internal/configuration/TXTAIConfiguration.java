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

package com.liferay.search.experiences.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Petteri Karttunen
 */
@ExtendedObjectClassDefinition(
	category = "search-experiences",
	scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
	id = "com.liferay.search.experiences.internal.configuration.TXTAIConfiguration",
	localization = "content/Language", name = "txtai-configuration-name"
)
public interface TXTAIConfiguration {

	@Meta.AD(deflt = "false", name = "enabled", required = false)
	public boolean enabled();

	@Meta.AD(deflt = "http://localhost:8000", name = "host", required = false)
	public String host();

	@Meta.AD(deflt = "604800", name = "cache-timeout", required = false)
	public int cacheTimeout();

	@Meta.AD(
		deflt = "com.liferay.journal.model.JournalArticle",
		name = "entry-class-names", required = false
	)
	public String[] entryClassNames();

	@Meta.AD(deflt = "en_US", name = "language-ids", required = false)
	public String[] languageIds();

	@Meta.AD(
		deflt = "sentence-transformers/nli-mpnet-base-v2",
		name = "embeddings-path", required = false
	)
	public String embeddingsPath();

}