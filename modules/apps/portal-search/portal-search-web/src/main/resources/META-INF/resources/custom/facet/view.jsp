<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/ddm" prefix="liferay-ddm" %><%@
taglib uri="http://liferay.com/tld/frontend" prefix="liferay-frontend" %>

<%@ page import="com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil" %><%@
page import="com.liferay.portal.kernel.util.HashMapBuilder" %><%@
page import="com.liferay.portal.kernel.util.HtmlUtil" %><%@
page import="com.liferay.portal.kernel.util.WebKeys" %><%@
page import="com.liferay.portal.search.web.internal.custom.facet.configuration.CustomFacetPortletInstanceConfiguration" %><%@
page import="com.liferay.portal.search.web.internal.custom.facet.display.context.CustomFacetCalendarDisplayContext" %><%@
page import="com.liferay.portal.search.web.internal.custom.facet.display.context.CustomFacetDisplayContext" %><%@
page import="com.liferay.portal.search.web.internal.custom.facet.portlet.CustomFacetPortlet" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.BucketDisplayContext" %>

<portlet:defineObjects />

<%
CustomFacetDisplayContext customFacetDisplayContext = (CustomFacetDisplayContext)java.util.Objects.requireNonNull(request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT));

CustomFacetPortletInstanceConfiguration customFacetPortletInstanceConfiguration = customFacetDisplayContext.getCustomFacetPortletInstanceConfiguration();

BucketDisplayContext customRangeBucketDisplayContext = customFacetDisplayContext.getCustomRangeBucketDisplayContext();
CustomFacetCalendarDisplayContext customFacetCalendarDisplayContext = customFacetDisplayContext.getCustomFacetCalendarDisplayContext();

String aggregationType = customFacetDisplayContext.getAggregationType();
%>

<c:choose>
	<c:when test="<%= customFacetDisplayContext.isRenderNothing() %>">
		<aui:input name="<%= HtmlUtil.escapeAttribute(customFacetDisplayContext.getParameterName()) %>" type="hidden" value="<%= customFacetDisplayContext.getParameterValue() %>" />
	</c:when>
	<c:otherwise>
		<aui:form action="#" autocomplete="off" method="get" name="fm">
			<aui:input cssClass="aggregation-type" name="aggregation-type" type="hidden" value="<%= customFacetDisplayContext.getAggregationType() %>" />
			<aui:input cssClass="facet-parameter-name" name="facet-parameter-name" type="hidden" value="<%= HtmlUtil.escapeAttribute(customFacetDisplayContext.getParameterName()) %>" />
			<aui:input name="start-parameter-name" type="hidden" value="<%= customFacetDisplayContext.getPaginationStartParameterName() %>" />

			<liferay-ddm:template-renderer
				className="<%= CustomFacetPortlet.class.getName() %>"
				contextObjects='<%=
					HashMapBuilder.<String, Object>put(
						"customFacetCalendarDisplayContext", customFacetCalendarDisplayContext
					).put(
						"customFacetDisplayContext", customFacetDisplayContext
					).put(
						"customRangeBucketDisplayContext", customRangeBucketDisplayContext
					).put(
						"namespace", liferayPortletResponse.getNamespace()
					).build()
				%>'
				displayStyle="<%= customFacetPortletInstanceConfiguration.displayStyle() %>"
				displayStyleGroupId="<%= customFacetDisplayContext.getDisplayStyleGroupId() %>"
				entries="<%= customFacetDisplayContext.getBucketDisplayContexts() %>"
			/>
		</aui:form>
	</c:otherwise>
</c:choose>

<liferay-frontend:component
	context='<%=
		HashMapBuilder.<String, Object>put(
			"namespace", liferayPortletResponse.getNamespace()
		).build()
	%>'
	module="{FacetUtil} from portal-search-web"
/>

<c:if test='<%= FeatureFlagManagerUtil.isEnabled("LPS-153839") && customFacetDisplayContext.isShowInputRange() && (aggregationType.equals("dateRange") || aggregationType.equals("range")) %>'>
	<aui:script use="liferay-search-custom-range-facet">
		new Liferay.Search.CustomRangeFacet({
			aggregationType: '<%= customFacetDisplayContext.getAggregationType() %>',
			form: A.one('#<portlet:namespace />fm'),
			fromInputName: '<portlet:namespace />fromInput',
			namespace: '<portlet:namespace />',
			parameterName:
				'<%= HtmlUtil.escapeAttribute(customFacetDisplayContext.getParameterName()) %>',
			searchCustomRangeButton: A.one(
				'#<portlet:namespace />searchCustomRangeButton'
			),
			searchCustomRangeToggleName:
				'<portlet:namespace /><%= customRangeBucketDisplayContext.getBucketText() %>',
			toInputName: '<portlet:namespace />toInput',
		});
	</aui:script>
</c:if>