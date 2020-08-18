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
SearchConfigurationEntriesManagementToolbarDisplayContext searchConfigurationEntriesManagementToolbarDisplayContext = (SearchConfigurationEntriesManagementToolbarDisplayContext)request.getAttribute(SearchConfigurationWebKeys.SEARCH_CONFIGURATION_ENTRIES_MANAGEMENT_TOOLBAR_DISPLAY_CONTEXT);

SearchConfigurationEntriesDisplayContext searchConfigurationEntriesDisplayContext = (SearchConfigurationEntriesDisplayContext)request.getAttribute(SearchConfigurationWebKeys.SEARCH_CONFIGURATION_ENTRIES_DISPLAY_CONTEXT);

SearchContainer configurationsSearchContainer = searchConfigurationEntriesDisplayContext.getSearchContainer();

String displayStyle = searchConfigurationEntriesDisplayContext.getDisplayStyle();
%>

<clay:management-toolbar
	displayContext="<%= searchConfigurationEntriesManagementToolbarDisplayContext %>"
	searchContainerId="configurationEntries"
	supportsBulkActions="<%= true %>"
/>

<clay:container-fluid>
	<aui:form method="post" name="fm">
		<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />

		<liferay-ui:search-container
			id="configurationEntries"
			searchContainer="<%= configurationsSearchContainer %>"
		>
			<liferay-ui:search-container-row
				className="com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration"
				keyProperty="searchConfigurationId"
				modelVar="entry"
			>
				<%@ include file="/entry_search_columns.jspf" %>
			</liferay-ui:search-container-row>

			<liferay-ui:search-iterator
				displayStyle="<%= displayStyle %>"
				markupView="lexicon"
			/>
		</liferay-ui:search-container>
	</aui:form>
</clay:container-fluid>

<aui:script sandbox="<%= true %>">
	var submitForm = function (url) {
		var searchContainer = document.getElementById(
			'<portlet:namespace />configurationEntries'
		);

		if (searchContainer) {
			Liferay.Util.postForm(document.<portlet:namespace />fm, {
				data: {
					actionFormInstanceIds: Liferay.Util.listCheckedExcept(
						searchContainer,
						'<portlet:namespace />allRowIds'
					),
				},
				url: url,
			});
		}
	};

	var deleteSearchConfigurationEntries = function () {
		if (
			confirm(
				'<liferay-ui:message key="are-you-sure-you-want-to-delete-configurations" />'
			)
		) {
			<portlet:actionURL name="<%= SearchConfigurationMVCCommandNames.DELETE_SEARCH_CONFIGURATIONS %>" var="deleteSearchConfigurationURL">
				<portlet:param name="redirect" value="<%= currentURL %>" />
			</portlet:actionURL>

			submitForm('<%= deleteSearchConfigurationURL %>');
		}
	};

	var ACTIONS = {
		deleteSearchConfigurationEntries: deleteSearchConfigurationEntries,
	};

	Liferay.componentReady('configurationEntriesManagementToolbar').then(function (
		managementToolbar
	) {
		managementToolbar.on('actionItemClicked', function (event) {
			var itemData = event.data.item.data;

			if (itemData && itemData.action && ACTIONS[itemData.action]) {
				ACTIONS[itemData.action]();
			}
		});
	});
</aui:script>