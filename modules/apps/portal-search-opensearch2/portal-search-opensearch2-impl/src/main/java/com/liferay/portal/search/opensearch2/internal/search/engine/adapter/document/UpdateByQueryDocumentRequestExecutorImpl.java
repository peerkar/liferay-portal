/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.search.engine.adapter.document;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.search.engine.adapter.document.UpdateByQueryDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.UpdateByQueryDocumentResponse;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionManager;
import com.liferay.portal.search.opensearch2.internal.script.ScriptTranslator;
import com.liferay.portal.search.query.QueryTranslator;
import com.liferay.portal.search.script.ScriptBuilder;
import com.liferay.portal.search.script.ScriptType;
import com.liferay.portal.search.script.Scripts;

import java.io.IOException;

import java.util.Arrays;
import java.util.Map;

import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.query_dsl.Query;
import org.opensearch.client.opensearch._types.query_dsl.QueryVariant;
import org.opensearch.client.opensearch.core.UpdateByQueryRequest;
import org.opensearch.client.opensearch.core.UpdateByQueryResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Dylan Rebelak
 * @author Petteri Karttunen
 */
@Component(service = UpdateByQueryDocumentRequestExecutor.class)
public class UpdateByQueryDocumentRequestExecutorImpl
	implements UpdateByQueryDocumentRequestExecutor {

	@Override
	public UpdateByQueryDocumentResponse execute(
		UpdateByQueryDocumentRequest updateByQueryDocumentRequest) {

		UpdateByQueryResponse updateByQueryResponse = _getUpdateByQueryResponse(
			updateByQueryDocumentRequest,
			createUpdateByQueryRequest(updateByQueryDocumentRequest));

		return new UpdateByQueryDocumentResponse(
			updateByQueryResponse.total(), updateByQueryResponse.took());
	}

	protected UpdateByQueryRequest createUpdateByQueryRequest(
		UpdateByQueryDocumentRequest updateByQueryDocumentRequest) {

		UpdateByQueryRequest.Builder updateByQueryRequestBuilder =
			new UpdateByQueryRequest.Builder();

		updateByQueryRequestBuilder.index(
			Arrays.asList(updateByQueryDocumentRequest.getIndexNames()));

		if (updateByQueryDocumentRequest.getPortalSearchQuery() != null) {
			updateByQueryRequestBuilder.query(
				new Query(
					_queryTranslator.translate(
						updateByQueryDocumentRequest.getPortalSearchQuery())));
		}
		else {
			updateByQueryRequestBuilder.query(
				_legacyQueryTranslator.translate(
					updateByQueryDocumentRequest.getQuery(), null));
		}

		if (updateByQueryDocumentRequest.isRefresh()) {
			updateByQueryRequestBuilder.refresh(true);
		}

		if (updateByQueryDocumentRequest.getScript() != null) {
			updateByQueryRequestBuilder.script(
				_scriptTranslator.translate(
					updateByQueryDocumentRequest.getScript()));
		}
		else if (updateByQueryDocumentRequest.getScriptJSONObject() != null) {
			ScriptBuilder scriptBuilder = _scripts.builder();

			JSONObject scriptJSONObject =
				updateByQueryDocumentRequest.getScriptJSONObject();

			if (scriptJSONObject.has("idOrCode")) {
				scriptBuilder.idOrCode(scriptJSONObject.getString("idOrCode"));
			}

			if (scriptJSONObject.has("language")) {
				scriptBuilder.language(scriptJSONObject.getString("language"));
			}

			if (scriptJSONObject.has("optionsMap")) {
				scriptBuilder.options(
					(Map<String, String>)scriptJSONObject.get("optionsMap"));
			}

			if (scriptJSONObject.has("parametersMap")) {
				scriptBuilder.parameters(
					(Map<String, Object>)scriptJSONObject.get("parametersMap"));
			}

			if (scriptJSONObject.has("scriptType")) {
				scriptBuilder.scriptType(
					(ScriptType)scriptJSONObject.get("scriptType"));
			}

			updateByQueryRequestBuilder.script(
				_scriptTranslator.translate(scriptBuilder.build()));
		}

		return updateByQueryRequestBuilder.build();
	}

	private UpdateByQueryResponse _getUpdateByQueryResponse(
		UpdateByQueryDocumentRequest updateByQueryDocumentRequest,
		UpdateByQueryRequest updateByQueryRequest) {

		OpenSearchClient openSearchClient =
			_openSearchConnectionManager.getOpenSearchClient(
				updateByQueryDocumentRequest.getConnectionId(),
				updateByQueryDocumentRequest.isPreferLocalCluster());

		try {
			return openSearchClient.updateByQuery(updateByQueryRequest);
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	@Reference(target = "(search.engine.impl=OpenSearch)")
	private com.liferay.portal.kernel.search.query.QueryTranslator<Query>
		_legacyQueryTranslator;

	@Reference
	private OpenSearchConnectionManager _openSearchConnectionManager;

	@Reference(target = "(search.engine.impl=OpenSearch)")
	private QueryTranslator<QueryVariant> _queryTranslator;

	@Reference
	private Scripts _scripts;

	private final ScriptTranslator _scriptTranslator = new ScriptTranslator();

}