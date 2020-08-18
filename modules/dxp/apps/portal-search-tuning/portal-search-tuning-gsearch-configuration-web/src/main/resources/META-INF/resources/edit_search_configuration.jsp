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

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/frontend" prefix="liferay-frontend" %><%@
taglib uri="http://liferay.com/tld/react" prefix="react" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>

<%@ page import="com.liferay.portal.search.tuning.gsearch.configuration.web.internal.constants.SearchConfigurationWebKeys" %><%@
page import="com.liferay.portal.search.tuning.gsearch.configuration.web.internal.display.context.EditSearchConfigurationDisplayContext" %>

<liferay-frontend:defineObjects />

<liferay-theme:defineObjects />

<portlet:defineObjects />

<%
EditSearchConfigurationDisplayContext editSearchConfigurationDisplayContext = (EditSearchConfigurationDisplayContext)request.getAttribute(SearchConfigurationWebKeys.EDIT_SEARCH_CONFIGURATION_DISPLAY_CONTEXT);

renderResponse.setTitle(editSearchConfigurationDisplayContext.getPageTitle());
%>

<div>
	<react:component
		data="<%= editSearchConfigurationDisplayContext.getData() %>"
		module="js/ConfigurationSetApp"
	/>
</div>