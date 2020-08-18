<%@ include file="/init.jsp" %>

<liferay-ui:error key="error.search-configuration-not-defined" message="Please set search configuration ID." />

<%
if (suggestionsURL == null) {
	return;
}
%>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.devbridge-autocomplete/1.4.11/jquery.autocomplete.min.js"></script>

<c:choose>
	<c:when test='<%= suggestMode.equals("contents") %>'>
		<%@ include file="/suggest_mode/contents.jspf" %>
	</c:when>
	<c:otherwise>
		<%@ include file="/suggest_mode/keywords.jspf" %>
	</c:otherwise>
</c:choose>