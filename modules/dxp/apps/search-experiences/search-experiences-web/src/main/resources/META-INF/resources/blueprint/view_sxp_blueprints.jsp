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
	ViewSXPBlueprintsDisplayContext viewBlueprintsDisplayContext = (ViewSXPBlueprintsDisplayContext)request.getAttribute(SXPBlueprintWebKeys.VIEW_SXP_BLUEPRINTS_DISPLAY_CONTEXT);
%>

<portlet:actionURL name="<%=SXPBlueprintMVCCommandNames.DELETE_BLUEPRINT%>" var="deleteBlueprintURL">
	<portlet:param name="redirect" value="<%=currentURL%>" />
</portlet:actionURL>

<clay:management-toolbar
	additionalProps='<%=HashMapBuilder.<String, Object>put(
			"deleteBlueprintURL", deleteBlueprintURL
		).build()%>'
	managementToolbarDisplayContext="<%=(ViewSXPBlueprintsManagementToolbarDisplayContext)request.getAttribute(SXPBlueprintWebKeys.VIEW_SXP_BLUEPRINTS_MANAGEMENT_TOOLBAR_DISPLAY_CONTEXT)%>"
	propsTransformer="js/view_blueprints/BlueprintEntriesManagementToolbarPropsTransformer"
	searchContainerId="blueprintEntries"
	supportsBulkActions="<%= true %>"
/>

<clay:container-fluid>
	<aui:form method="post" name="fm">
		<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />

		<liferay-ui:search-container
			cssClass="blueprints-search-container"
			id="blueprintEntries"
			searchContainer="<%= viewBlueprintsDisplayContext.getSearchContainer() %>"
		>
			<liferay-ui:search-container-row
				className="com.liferay.search.experiences.blueprints.model.Blueprint"
				keyProperty="blueprintId"
				modelVar="entry"
			>
				<%@ include file="/blueprint_entry_search_columns.jspf" %>
			</liferay-ui:search-container-row>

			<liferay-ui:search-iterator
				displayStyle="<%= viewBlueprintsDisplayContext.getDisplayStyle() %>"
				markupView="lexicon"
			/>
		</liferay-ui:search-container>
	</aui:form>
</clay:container-fluid>