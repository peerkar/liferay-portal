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

package com.liferay.search.experiences.ingest.web.internal.portlet.action;

import com.liferay.exportimport.kernel.lar.ExportImportThreadLocal;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.search.experiences.ingest.web.internal.constants.IngestPortletKeys;
import com.liferay.search.experiences.ingest.web.internal.constants.MVCActionCommandNames;
import com.liferay.search.experiences.ingest.web.internal.ingester.Ingester;
import com.liferay.search.experiences.ingest.web.internal.ingester.IngesterFactory;

import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	enabled = false, immediate = true,
	property = {
		"javax.portlet.name=" + IngestPortletKeys.INGEST,
		"mvc.command.name=" + MVCActionCommandNames.INGEST
	},
	service = MVCActionCommand.class
)
public class IngestMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		Ingester dataIngestor = _ingesterFactory.getIngester(
			ParamUtil.getString(actionRequest, "type"));

		ExportImportThreadLocal.setPortletImportInProcess(true);

		long timeMillis = System.currentTimeMillis();

		Map<String, List<String>> results = dataIngestor.ingest(
			actionRequest, actionResponse);

		if (_log.isInfoEnabled()) {
			_log.info("Finished ingestion in " + (timeMillis / 1000) + " s");
		}

		ExportImportThreadLocal.setPortletImportInProcess(false);

		_setResultsInfo(actionRequest, results);
	}

	private void _setResultsInfo(
		ActionRequest actionRequest, Map<String, List<String>> results) {

		if (results.isEmpty()) {
			SessionErrors.add(actionRequest, "noItemsWereIngested");
		}
		else {
			List<String> failedItems = results.get("failedItems");

			actionRequest.setAttribute("failedItems", failedItems);

			actionRequest.setAttribute("failedItemsCount", failedItems.size());

			List<String> ingestedItems = results.get("ingestedItems");

			actionRequest.setAttribute("ingestedItems", ingestedItems);

			actionRequest.setAttribute(
				"ingestedItemsCount", ingestedItems.size());
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		IngestMVCActionCommand.class);

	@Reference
	private IngesterFactory _ingesterFactory;

}