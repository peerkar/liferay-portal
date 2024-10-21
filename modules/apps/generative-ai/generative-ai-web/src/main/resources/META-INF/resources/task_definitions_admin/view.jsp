<%--
/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
ViewTaskDefinitionsDisplayContext viewTaskDefinitionsDisplayContext = (ViewTaskDefinitionsDisplayContext)request.getAttribute(GenerativeAIWebKeys.VIEW_TASK_DEFINITIONS_DISPLAY_CONTEXT);
%>

<aui:form action="" method="post" name="fm">
	<aui:input name="redirect" type="hidden" value="<%= String.valueOf(viewTaskDefinitionsDisplayContext.getPortletURL()) %>" />

	<frontend-data-set:headless-display
		apiURL="<%= viewTaskDefinitionsDisplayContext.getAPIURL() %>"
		bulkActionDropdownItems="<%= viewTaskDefinitionsDisplayContext.getBulkActionDropdownItems() %>"
		creationMenu="<%= viewTaskDefinitionsDisplayContext.getCreationMenu() %>"
		fdsActionDropdownItems="<%= viewTaskDefinitionsDisplayContext.getFDSActionDropdownItems() %>"
		formName="fm"
		id="<%= TaskDefinitionAdminFDSNames.TASK_DEFINITIONS %>"
		propsTransformer="{ViewTaskDefinitionsPropsTransformer} from generative-ai-web"
		selectedItemsKey="id"
		selectionType="multiple"
		style="fluid"
	/>
</aui:form>

<div id="<portlet:namespace />addTaskDefinition">
	<react:component
		module="{AddTaskDefinitionModal} from generative-ai-web"
		props='<%=
			HashMapBuilder.<String, Object>put(
				"contextPath", application.getContextPath()
			).put(
				"defaultLocale", LocaleUtil.toLanguageId(LocaleUtil.getDefault())
			).put(
				"editTaskDefinitionURL",
				PortletURLBuilder.createRenderURL(
					renderResponse
				).setMVCRenderCommandName(
					"/task_definitions_admin/edit_task_definition"
				).buildString()
			).put(
				"portletNamespace", liferayPortletResponse.getNamespace()
			).build()
		%>'
	/>
</div>

<liferay-frontend:component
	module="{openInitialSuccessToastHandler} from generative-ai-web"
/>