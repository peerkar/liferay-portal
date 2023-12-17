/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter.document;

import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.search.engine.adapter.document.DeleteDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.GetDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.IndexDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.UpdateDocumentRequest;
import com.liferay.portal.search.opensearch2.internal.script.ScriptTranslator;

import java.util.Collections;

import org.opensearch.client.json.JsonData;
import org.opensearch.client.opensearch._types.Refresh;
import org.opensearch.client.opensearch.core.DeleteRequest;
import org.opensearch.client.opensearch.core.GetRequest;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.core.UpdateRequest;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = "search.engine.impl=OpenSearch",
	service = OpenSearchDocumentRequestTranslator.class
)
public class OpenSearchDocumentRequestTranslatorImpl
	extends BaseDocumentRequestTranslator
	implements OpenSearchDocumentRequestTranslator {

	@Override
	public DeleteRequest translate(
		DeleteDocumentRequest deleteDocumentRequest) {

		DeleteRequest.Builder deleteRequestBuilder =
			new DeleteRequest.Builder();

		deleteRequestBuilder.id(deleteDocumentRequest.getUid());
		deleteRequestBuilder.index(deleteDocumentRequest.getIndexName());

		if (deleteDocumentRequest.isRefresh()) {
			deleteRequestBuilder.refresh(Refresh.True);
		}

		return deleteRequestBuilder.build();
	}

	@Override
	public GetRequest translate(GetDocumentRequest getDocumentRequest) {
		GetRequest.Builder getRequestBuilder = new GetRequest.Builder();

		getRequestBuilder.id(getDocumentRequest.getId());
		getRequestBuilder.index(getDocumentRequest.getIndexName());
		getRequestBuilder.refresh(getDocumentRequest.isRefresh());
		getRequestBuilder.source(
			source -> source.fetch(getDocumentRequest.isFetchSource()));
		getRequestBuilder.sourceExcludes(
			ListUtil.fromArray(getDocumentRequest.getFetchSourceExcludes()));
		getRequestBuilder.sourceIncludes(
			ListUtil.fromArray(getDocumentRequest.getFetchSourceIncludes()));
		getRequestBuilder.storedFields(
			ListUtil.fromArray(getDocumentRequest.getStoredFields()));

		return getRequestBuilder.build();
	}

	@Override
	public IndexRequest<JsonData> translate(
		IndexDocumentRequest indexDocumentRequest) {

		IndexRequest.Builder indexRequestBuilder = new IndexRequest.Builder();

		indexRequestBuilder.document(
			getDocument(
				indexDocumentRequest.getDocument(),
				indexDocumentRequest.getDocument71()));
		indexRequestBuilder.id(getUid(indexDocumentRequest));
		indexRequestBuilder.index(indexDocumentRequest.getIndexName());

		if (indexDocumentRequest.isRefresh()) {
			indexRequestBuilder.refresh(Refresh.True);
		}

		return indexRequestBuilder.build();
	}

	@Override
	public UpdateRequest translate(
		UpdateDocumentRequest updateDocumentRequest) {

		UpdateRequest.Builder updateRequestBuilder =
			new UpdateRequest.Builder();

		updateRequestBuilder.id(getUid(updateDocumentRequest));
		updateRequestBuilder.index(updateDocumentRequest.getIndexName());

		if (updateDocumentRequest.isRefresh()) {
			updateRequestBuilder.refresh(Refresh.True);
		}

		if (updateDocumentRequest.isUpsert()) {
			updateRequestBuilder.docAsUpsert(true);
		}

		if (updateDocumentRequest.isScriptedUpsert()) {
			updateRequestBuilder.scriptedUpsert(true);
			updateRequestBuilder.upsert(Collections.emptyMap());
		}

		if (updateDocumentRequest.getScript() != null) {
			updateRequestBuilder.script(
				_scriptTranslator.translate(updateDocumentRequest.getScript()));
		}
		else {
			updateRequestBuilder.doc(
				getDocument(
					updateDocumentRequest.getDocument(),
					updateDocumentRequest.getDocument71()));
		}

		return updateRequestBuilder.build();
	}

	private final ScriptTranslator _scriptTranslator = new ScriptTranslator();

}