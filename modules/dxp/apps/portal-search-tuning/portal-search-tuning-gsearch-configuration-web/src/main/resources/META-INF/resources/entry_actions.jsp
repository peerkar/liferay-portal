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
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

SearchConfiguration searchConfiguration = (SearchConfiguration)row.getObject();

long searchConfigurationId = searchConfiguration.getSearchConfigurationId();
int searchConfigurationType = searchConfiguration.getType();
long companyGroupId = themeDisplay.getCompanyGroupId();
%>

<liferay-ui:icon-menu
	direction="left-side"
	icon="<%= StringPool.BLANK %>"
	markupView="lexicon"
	message="<%= StringPool.BLANK %>"
	showWhenSingleIcon="<%= true %>"
>
	<c:if test="<%= SearchConfigurationEntryPermission.contains(permissionChecker, searchConfigurationId, searchConfigurationType, ActionKeys.UPDATE) %>">
		<portlet:renderURL var="editSearchConfigurationURL">
			<portlet:param name="mvcRenderCommandName" value="<%= SearchConfigurationMVCCommandNames.EDIT_SEARCH_CONFIGURATION %>" />
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="<%= SearchConfigurationWebKeys.SEARCH_CONFIGURATION_ID %>" value="<%= String.valueOf(searchConfigurationId) %>" />
		</portlet:renderURL>

		<liferay-ui:icon
			message="edit"
			url="<%= editSearchConfigurationURL %>"
		/>
	</c:if>

	<c:if test="<%= SearchConfigurationPermission.contains(permissionChecker, companyGroupId, searchConfigurationType, ActionKeys.ADD_ENTRY) %>">
		<portlet:actionURL name="<%= SearchConfigurationMVCCommandNames.COPY_SEARCH_CONFIGURATION %>" var="copySearchConfigurationUrl">
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="<%= SearchConfigurationWebKeys.SEARCH_CONFIGURATION_ID %>" value="<%= String.valueOf(searchConfigurationId) %>" />
		</portlet:actionURL>

		<liferay-ui:icon
			message="copy"
			url="<%= copySearchConfigurationUrl %>"
		/>
	</c:if>

	<c:if test="<%= SearchConfigurationPermission.contains(permissionChecker, companyGroupId, searchConfigurationType, ActionKeys.PERMISSIONS) %>">
		<liferay-security:permissionsURL
			modelResource="<%= SearchConfiguration.class.getName() %>"
			modelResourceDescription="<%= searchConfiguration.getTitle(locale) %>"
			resourceGroupId="<%= String.valueOf(searchConfiguration.getGroupId()) %>"
			resourcePrimKey="<%= String.valueOf(searchConfigurationId) %>"
			var="permissionsURL"
			windowState="<%= LiferayWindowState.POP_UP.toString() %>"
		/>

		<liferay-ui:icon
			label="<%= true %>"
			message="permissions"
			method="get"
			url="<%= permissionsURL %>"
			useDialog="<%= true %>"
		/>
	</c:if>

	<portlet:resourceURL id="<%= SearchConfigurationMVCCommandNames.EXPORT_SEARCH_CONFIGURATION %>" var="exportSearchConfigurationUrl">
		<portlet:param name="redirect" value="<%= currentURL %>" />
		<portlet:param name="<%= SearchConfigurationWebKeys.SEARCH_CONFIGURATION_ID %>" value="<%= String.valueOf(searchConfigurationId) %>" />
	</portlet:resourceURL>

	<liferay-ui:icon
		message="export"
		url="<%= exportSearchConfigurationUrl %>"
	/>

	<c:if test="<%= SearchConfigurationEntryPermission.contains(permissionChecker, searchConfigurationId, searchConfigurationType, ActionKeys.DELETE) %>">
		<portlet:actionURL name="<%= SearchConfigurationMVCCommandNames.DELETE_SEARCH_CONFIGURATIONS %>" var="deleteSearchConfigurationURL">
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="<%= SearchConfigurationWebKeys.SEARCH_CONFIGURATION_ID %>" value="<%= String.valueOf(searchConfigurationId) %>" />
		</portlet:actionURL>

		<liferay-ui:icon-delete
			url="<%= deleteSearchConfigurationURL %>"
		/>
	</c:if>
</liferay-ui:icon-menu>