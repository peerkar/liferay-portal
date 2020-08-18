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

package com.liferay.portal.search.tuning.gsearch.impl.internal.results.item.builder;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.document.Document;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.impl.util.GSearchUtil;
import com.liferay.portal.search.tuning.gsearch.results.ResultAttributes;
import com.liferay.portal.search.tuning.gsearch.spi.results.item.ResultItemBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public abstract class BaseResultItemBuilder implements ResultItemBuilder {

	public static final DateFormat INDEX_DATE_FORMAT = new SimpleDateFormat(
		"yyyyMMddHHmmss");

	@Override
	public String getDate(
			SearchRequestContext searchRequestContext,
			ResultAttributes resultAttributes, Document document)
		throws Exception {

		String dateString = "";

		Locale locale = searchRequestContext.getLocale();

		try {
			String modified = document.getDate(Field.MODIFIED_DATE);

			if (!Validator.isBlank(modified)) {
				Date lastModified = INDEX_DATE_FORMAT.parse(modified);

				DateFormat dateFormat = DateFormat.getDateInstance(
					DateFormat.SHORT, locale);

				dateString = dateFormat.format(lastModified);
			}
		}
		catch (Exception e) {
			_log.error(
				"Error in parsing date for:" +
					document.getString(
						_buildLocalizedFieldName(Field.TITLE, locale)),
				e);
		}

		return dateString;
	}

	@Override
	public String getDescription(
			SearchRequestContext searchRequestContext,
			ResultAttributes resultAttributes, Document document)
		throws Exception {

		int descriptionMaxLength = resultAttributes.getDescriptionMaxLength();

		Locale locale = searchRequestContext.getLocale();

		String description = getStringFieldContent(
			document, Field.CONTENT, locale);

		return GSearchUtil.stripHTML(description, descriptionMaxLength);
	}

	@Override
	public Map<String, String> getMetadata(
			SearchRequestContext searchRequestContext,
			ResultAttributes resultAttributes, Document document)
		throws Exception {

		return null;
	}

	@Override
	public String getThumbnail(
			SearchRequestContext searchRequestContext,
			ResultAttributes resultAttributes, Document document)
		throws Exception {

		return null;
	}

	@Override
	public String getTitle(
			SearchRequestContext searchRequestContext,
			ResultAttributes resultAttributes, Document document)
		throws Exception {

		Locale locale = searchRequestContext.getLocale();

		String title = getStringFieldContent(document, Field.TITLE, locale);

		return GSearchUtil.stripHTML(title, -1);
	}

	@Override
	public String getType(Document document) {
		return document.getString(Field.ENTRY_CLASS_NAME);
	}

	protected String buildLocalizedSnippetFieldName(
		String field, Locale locale) {

		StringBundler sb = new StringBundler(5);

		sb.append(Field.SNIPPET);
		sb.append(StringPool.UNDERLINE);
		sb.append(field);
		sb.append(StringPool.UNDERLINE);
		sb.append(locale.toString());

		return sb.toString();
	}

	protected Indexer<Object> getIndexer(String className) {
		return IndexerRegistryUtil.getIndexer(className);
	}

	protected String getStringFieldContent(
		Document document, String field, Locale locale) {

		String fieldName = null;
		String value = null;

		if (Validator.isNull(value)) {
			fieldName = _buildLocalizedFieldName(field, locale);
			value = document.getString(fieldName);
		}

		if (Validator.isNull(value)) {
			fieldName = _buildLocalizedFieldName2(field, locale);
			value = document.getString(fieldName);
		}

		// Try once more on non localized legacy field as
		// some assets like Wiki still might have it.

		if (Validator.isNull(value)) {
			value = document.getString(value);
		}

		return value;
	}

	private String _buildLocalizedFieldName(String field, Locale locale) {
		StringBundler sb = new StringBundler(3);

		sb.append(field);
		sb.append(StringPool.UNDERLINE);
		sb.append(locale.toString());

		return sb.toString();
	}

	private String _buildLocalizedFieldName2(String field, Locale locale) {
		StringBundler sb = new StringBundler(5);

		sb.append("localized");
		sb.append(StringPool.UNDERLINE);
		sb.append(field);
		sb.append(StringPool.UNDERLINE);
		sb.append(locale.toString());

		return sb.toString();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BaseResultItemBuilder.class);

}