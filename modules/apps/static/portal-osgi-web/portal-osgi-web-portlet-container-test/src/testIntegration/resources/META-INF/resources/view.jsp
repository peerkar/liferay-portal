<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/ddm" prefix="liferay-ddm" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>

<%@ page import="com.liferay.petra.string.StringPool" %><%@
page import="com.liferay.portal.kernel.util.GetterUtil" %><%@
page import="com.liferay.portal.kernel.util.HashMapBuilder" %><%@
page import="com.liferay.portal.osgi.web.portlet.container.test.TestPortlet" %>

<%@ page import="java.util.Collections" %>

<liferay-theme:defineObjects />

<portlet:defineObjects />

<liferay-ddm:template-renderer
	className="<%= TestPortlet.class.getName() %>"
	contextObjects='<%=
		HashMapBuilder.<String, Object>put(
			"testRuntimePortletId", "testRuntimePortletId"
		).build()
	%>'
	displayStyle='<%= GetterUtil.getString(portletPreferences.getValue("displayStyle", StringPool.BLANK)) %>'
	displayStyleGroupId='<%= GetterUtil.getLong(portletPreferences.getValue("displayStyleGroupId", null), scopeGroupId) %>'
	entries="<%= Collections.emptyList() %>"
>
	This is the default content in case of failure.
</liferay-ddm:template-renderer>