<%@page session="false" %>
<%@page import="clime.messadmin.utils.JMX"%>
<%@taglib prefix="core" uri="http://messadmin.sf.net/core" %>
<%@taglib prefix="format" uri="http://messadmin.sf.net/fmt" %>
<format:setBundle basename="clime.messadmin.providers.userdata.ThreadsDumper"/>

<%
	long[] deadlockedThreadsIDs = JMX.findDeadlockedThreadsIDs();
	if (deadlockedThreadsIDs != null && deadlockedThreadsIDs.length > 0) {
%>
	<div style="font-size: larger; font-weight: bolder; color: red;">
		<format:message key="stuckThreads.warning"/>
		<ul>
		<% for (int i = 0; i < deadlockedThreadsIDs.length; ++i) { %>
			<li><%= deadlockedThreadsIDs[i] %></li>
		<% } %>
		</ul>
	</div>
<% } %>