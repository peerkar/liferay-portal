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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="com.liferay.portal.kernel.util.HtmlUtil" %><%@
page import="com.liferay.portal.kernel.util.PrefsParamUtil" %><%@
page import="com.liferay.search.experiences.blueprints.exception.NoSuchBlueprintException" %><%@
page import="com.liferay.search.experiences.blueprints.model.Blueprint" %><%@
page import="com.liferay.search.experiences.blueprints.service.BlueprintLocalServiceUtil" %>

<liferay-theme:defineObjects />

<portlet:defineObjects />

<%
long blueprintId = PrefsParamUtil.getLong(portletPreferences, request, "blueprintId");

Blueprint blueprint = null;

try {
	blueprint = BlueprintLocalServiceUtil.getBlueprint(blueprintId);
}
catch (NoSuchBlueprintException noSuchBlueprintException) {
}
%>

<div class="alert alert-info text-center">
	<aui:a href="javascript:;" onClick="<%= portletDisplay.getURLConfigurationJS() %>">
		<liferay-ui:message key="blueprints-options-help" />

		<c:if test="<%= blueprint != null %>">
			<liferay-ui:message arguments="<%= HtmlUtil.escape(blueprint.getTitle(locale)) %>" key="blueprint-x" />
		</c:if>
	</aui:a>
</div>