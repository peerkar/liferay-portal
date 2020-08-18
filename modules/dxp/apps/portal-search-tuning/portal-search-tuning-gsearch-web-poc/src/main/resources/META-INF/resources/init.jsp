<%@page import="javax.portlet.PortletPreferences"%>
<%@page import="com.liferay.portal.kernel.json.JSONObject"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/frontend" prefix="liferay-frontend" %><%@
taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="com.liferay.portal.kernel.language.LanguageUtil" %>

<liferay-theme:defineObjects />

<portlet:defineObjects />

<%

	PortletPreferences preferences = renderRequest.getPreferences();
	String suggestMode = preferences.getValue("suggestMode", "contents");

	String suggestionsURL = null;

	JSONObject uiConfiguration = (JSONObject)renderRequest.getAttribute("configuration");
	
	if (uiConfiguration != null && uiConfiguration.has("urlConfiguration")) {
		JSONObject urlConfig = uiConfiguration.getJSONObject("urlConfiguration");

		if (suggestMode.equals("contents")) {
			suggestionsURL = urlConfig.getString("searchResultsURL");
		} else {
			suggestionsURL = urlConfig.getString("suggestionsURL");
		}
	}

	Boolean appendRedirect = true; 
	Integer queryMinLength = 3;
	Integer requestDelay =  500; 
	Integer requestTimeout = 10000; 
	String searchPageURL = "/search";
%>

