<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
String type = ParamUtil.getString(request, "type", "google_places");
%>

<liferay-ui:error key="noItemsWereIngested" message="no-items-were-ingested-check-input" />

<%@ include file="/results.jspf" %>

<portlet:actionURL name="<%= MVCActionCommandNames.INGEST %>" var="ingestActionURL" />

<div class="container-md">
	<h1><liferay-ui:message key="ingest-title" /></h1>

	<aui:form action="<%= ingestActionURL %>" name="form">
		<aui:select label="ingest-type" name="type">
			<aui:option label="google-places" value="google_places" />
			<aui:option label="wikipedia-articles" value="wikipedia" />
			<aui:option label="liferay-learn" value="liferay_learn" />
			<aui:option label="liferay-help-center" value="liferay_help_center" />
		</aui:select>

		<div class="ingestion-type google_places <%= type.equals("google_places") ? "" : "hide" %>">
			<%@ include file="/ingester/google_places.jspf" %>
		</div>

		<div class="ingestion-type liferay_learn <%= type.equals("liferay_learn") ? "" : "hide" %>">
			<%@ include file="/ingester/liferay_learn.jspf" %>
		</div>

		<div class="ingestion-type liferay_help_center <%= type.equals("liferay_help_center") ? "" : "hide" %>">
			<%@ include file="/ingester/liferay_help_center.jspf" %>
		</div>

		<div class="ingestion-type wikipedia <%= type.equals("wikipedia") ? "" : "hide" %>">
			<%@ include file="/ingester/wikipedia.jspf" %>
		</div>

		<aui:fieldset label="target-parameters">
			<aui:input label="target-user-ids" name="userIds" value="<%= themeDisplay.getUserId() %>">
				<aui:validator name="required" />
			</aui:input>

			<aui:input label="target-group-ids" name="groupIds" required="<%= true %>" value="<%= themeDisplay.getScopeGroupId() %>">
				<aui:validator name="required" />
			</aui:input>

			<aui:input label="target-language-id" name="languageId" required="<%= true %>" value="<%= themeDisplay.getLanguageId() %>">
				<aui:validator name="required" />
			</aui:input>
		</aui:fieldset>

		<aui:button-row>
			<aui:button cssClass="btn btn-primary" type="submit" value="ingest" />
		</aui:button-row>
	</aui:form>
</div>

<script type="text/javascript">

	Liferay.Portlet.ready(function() {

		let A = AUI();

		let typeElement = A.one('#<portlet:namespace />type');

		typeElement.on('change', function () {

			A.all('.ingestion-type').addClass('hide');

			A.one('.' + this.val()).removeClass('hide');
		});
	});
</script>