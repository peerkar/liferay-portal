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

Element element = (SXPElement)row.getObject();

long elementId = element.getElementId();

long companyGroupId = themeDisplay.getCompanyGroupId();
%>

<liferay-ui:icon-menu
	direction="left-side"
	icon="<%= StringPool.BLANK %>"
	markupView="lexicon"
	message="<%= StringPool.BLANK %>"
	showWhenSingleIcon="<%= true %>"
>
	<c:if test="<%= SXPElementEntryPermission.contains(permissionChecker, element, ActionKeys.UPDATE) %>">
		<portlet:renderURL var="editEntryURL">
			<portlet:param name="mvcRenderCommandName" value="<%=SXPBlueprintMVCCommandNames.EDIT_ELEMENT%>" />
			<portlet:param name="redirect" value="<%=currentURL%>" />
			<portlet:param name="<%=SXPBlueprintWebKeys.ELEMENT_ID%>" value="<%=String.valueOf(elementId)%>" />
		</portlet:renderURL>

		<liferay-ui:icon
			message='<%=element.getReadOnly() ? "view" : "edit"%>'
			url="<%=editEntryURL%>"
		/>
	</c:if>

	<c:if test="<%=SXPBlueprintPermission.contains(permissionChecker, companyGroupId, BlueprintsActionKeys.ADD_ELEMENT)%>">
		<portlet:actionURL name="<%=SXPBlueprintMVCCommandNames.COPY_ELEMENT%>" var="copyEntryURL">
			<portlet:param name="redirect" value="<%=currentURL%>" />
			<portlet:param name="<%=SXPBlueprintWebKeys.ELEMENT_ID%>" value="<%=String.valueOf(elementId)%>" />
		</portlet:actionURL>

		<liferay-ui:icon
			message="copy"
			url="<%=copyEntryURL%>"
		/>
	</c:if>

	<portlet:resourceURL id="<%=SXPBlueprintMVCCommandNames.EXPORT_ELEMENT%>" var="exportEntryURL">
		<portlet:param name="redirect" value="<%=currentURL%>" />
		<portlet:param name="<%=SXPBlueprintWebKeys.ELEMENT_ID%>" value="<%=String.valueOf(elementId)%>" />
	</portlet:resourceURL>

	<liferay-ui:icon
		message="export"
		url="<%=exportEntryURL%>"
	/>

	<c:if test="<%=SXPBlueprintPermission.contains(permissionChecker, companyGroupId, ActionKeys.PERMISSIONS)%>">
		<liferay-security:permissionsURL
			modelResource="<%=SXPElement.class.getName()%>"
			modelResourceDescription="<%=element.getTitle(locale)%>"
			resourceGroupId="<%=String.valueOf(element.getGroupId())%>"
			resourcePrimKey="<%=String.valueOf(elementId)%>"
			var="permissionsURL"
			windowState="<%=LiferayWindowState.POP_UP.toString()%>"
		/>

		<liferay-ui:icon
			label="<%=true%>"
			message="permissions"
			method="get"
			url="<%=permissionsURL%>"
			useDialog="<%=true%>"
		/>
	</c:if>

	<c:if test="<%=SXPElementEntryPermission.contains(permissionChecker, element, ActionKeys.UPDATE)%>">
		<portlet:actionURL name="<%=SXPBlueprintMVCCommandNames.EDIT_ELEMENT%>" var="hideEntryURL">
			<portlet:param name="redirect" value="<%=currentURL%>" />
			<portlet:param name="<%=SXPBlueprintWebKeys.ELEMENT_ID%>" value="<%=String.valueOf(elementId)%>" />
			<portlet:param name="<%=Constants.CMD%>" value="<%=SXPBlueprintWebKeys.HIDE%>" />
			<portlet:param name="hidden" value="<%=String.valueOf(!element.getHidden())%>" />
		</portlet:actionURL>

		<liferay-ui:icon
			message='<%=element.getHidden() ? "show" : "hide"%>'
			url="<%=hideEntryURL%>"
		/>
	</c:if>

	<c:if test="<%=SXPElementEntryPermission.contains(permissionChecker, element, ActionKeys.DELETE) && !element.getReadOnly()%>">
		<portlet:actionURL name="<%=SXPBlueprintMVCCommandNames.DELETE_ELEMENT%>" var="deleteEntryURL">
			<portlet:param name="redirect" value="<%=currentURL%>" />
			<portlet:param name="<%=SXPBlueprintWebKeys.ELEMENT_ID%>" value="<%= String.valueOf(elementId) %>" />
		</portlet:actionURL>

		<liferay-ui:icon-delete
			url="<%= deleteEntryURL %>"
		/>
	</c:if>
</liferay-ui:icon-menu>