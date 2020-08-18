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

<liferay-ui:error key="errorDetails">
	<liferay-ui:message arguments='<%= SessionErrors.get(liferayPortletRequest, "errorDetails") %>' key="error.search-configuration-service-error" />
</liferay-ui:error>

<%
final String tabs = ParamUtil.getString(request, "tabs", "configurations");
PortletURL configurationsURL = renderResponse.createRenderURL();
configurationsURL.setParameter(SearchConfigurationWebKeys.SEARCH_CONFIGURATION_TYPE, String.valueOf(SearchConfigurationTypes.CONFIGURATION));
PortletURL templatesURL = renderResponse.createRenderURL();
templatesURL.setParameter(SearchConfigurationWebKeys.SEARCH_CONFIGURATION_TYPE, String.valueOf(SearchConfigurationTypes.TEMPLATE));
PortletURL snippetsURL = renderResponse.createRenderURL();
snippetsURL.setParameter(SearchConfigurationWebKeys.SEARCH_CONFIGURATION_TYPE, String.valueOf(SearchConfigurationTypes.SNIPPET));
%>

<clay:navigation-bar
	inverted="<%= false %>"
	navigationItems='<%=
		new JSPNavigationItemList(pageContext) {
			{
				add(
					navigationItem -> {
						navigationItem.setActive(tabs.equals("configurations"));
						navigationItem.setHref(configurationsURL, "tabs", "configurations");
						navigationItem.setLabel(LanguageUtil.get(request, "search-configurations"));
						navigationItem.putData(
								SearchConfigurationWebKeys.SEARCH_CONFIGURATION_TYPE,
								String.valueOf(SearchConfigurationTypes.CONFIGURATION));
					});
				add(
					navigationItem -> {
						navigationItem.setActive(tabs.equals("templates"));
						navigationItem.setHref(templatesURL, "tabs", "templates");
						navigationItem.setLabel(LanguageUtil.get(request, "configuration-templates"));
					});
				add(
					navigationItem -> {
						navigationItem.setActive(tabs.equals("snippets"));
						navigationItem.setHref(snippetsURL, "tabs", "snippets");
						navigationItem.setLabel(LanguageUtil.get(request, "configuration-snippets"));
						navigationItem.putData(
								SearchConfigurationWebKeys.SEARCH_CONFIGURATION_TYPE,
								String.valueOf(SearchConfigurationTypes.SNIPPET));
					});
			}
		}
	%>'
/>

<liferay-util:include page="/view_search_configurations.jsp" servletContext="<%= application %>" />