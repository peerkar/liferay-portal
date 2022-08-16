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

package com.liferay.search.experiences.internal.ml.txtai.search.spi.model.index.contributor;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.search.experiences.internal.configuration.TXTAIConfiguration;
import com.liferay.search.experiences.internal.ml.txtai.client.TXTAIClient;
import com.liferay.search.experiences.internal.ml.txtai.client.TXTAIClientImpl;

/**
 * @author Petteri Karttunen
 */
public abstract class BaseTXTAIModelDocumentContributor {

	protected void addTextEmbedding(
		Document document, String languageId, String text,
		TXTAIConfiguration txtAIConfiguration) {

		if (Validator.isBlank(text)) {
			return;
		}

		TXTAIClient txtAIClient = new TXTAIClientImpl(txtAIConfiguration);

		try {
			Double[] textEmbeddings = txtAIClient.getTextEmbedding(text, false);

			if (textEmbeddings.length == 0) {
				return;
			}

			String fieldName = _TEXT_EMBEDDING_FIELD;

			if (!Validator.isBlank(languageId)) {
				fieldName = StringBundler.concat(
					fieldName, StringPool.UNDERLINE, languageId);
			}

			Field embeddingField = new Field(fieldName);

			embeddingField.setValues(ArrayUtil.toStringArray(textEmbeddings));
			embeddingField.setNumeric(true);
			embeddingField.setNumericClass(Double.class);
			embeddingField.setTokenized(false);

			document.add(embeddingField);
		}
		catch (Exception exception) {
			_log.error(exception);
		}
	}

	protected void addTextEmbedding(
		Document document, String text, TXTAIConfiguration txtAIConfiguration) {

		addTextEmbedding(document, StringPool.BLANK, text, txtAIConfiguration);
	}

	protected boolean isAddTextEmbeddings(
		Class<?> clazz, TXTAIConfiguration txtAIConfiguration) {

		if (txtAIConfiguration.enabled() &&
			ArrayUtil.contains(
				txtAIConfiguration.entryClassNames(), clazz.getName(), true)) {

			return true;
		}

		return false;
	}

	private static final String _TEXT_EMBEDDING_FIELD = "text_embedding";

	private static final Log _log = LogFactoryUtil.getLog(
		BaseTXTAIModelDocumentContributor.class);

}