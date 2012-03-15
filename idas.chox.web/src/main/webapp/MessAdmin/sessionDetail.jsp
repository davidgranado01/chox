<?xml version="1.0" encoding="UTF-8"?>
<%@page session="false" contentType="text/html; charset=UTF-8" %>
<%@page import="clime.messadmin.core.Constants" %>
<%@page import="clime.messadmin.admin.AdminActionProvider"%>
<%@page import="clime.messadmin.admin.BaseAdminActionWithContext"%>
<%@page import="clime.messadmin.admin.BaseAdminActionWithContextAndSession"%>
<%@page import="clime.messadmin.admin.actions.ServerInfos"%>
<%@page import="clime.messadmin.admin.actions.WebAppsList"%>
<%@page import="clime.messadmin.admin.actions.WebAppStats"%>
<%@page import="clime.messadmin.admin.actions.SessionsList"%>
<%@page import="clime.messadmin.admin.actions.ReloadSessionDataProvider"%>
<%@page import="clime.messadmin.admin.actions.ReloadDataProviderHelper"%>
<%@page import="clime.messadmin.model.IApplicationInfo" %>
<%@page import="clime.messadmin.model.ApplicationInfo"%>
<%@page import="clime.messadmin.model.ISessionInfo" %>
<%@page import="clime.messadmin.model.DisplayDataHolder"%>
<%@taglib prefix="core" uri="http://messadmin.sf.net/core" %>
<%@taglib prefix="format" uri="http://messadmin.sf.net/fmt" %>
<%--!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"--%>
<%--!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"--%>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%--!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"--%>
<%--!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
 "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"--%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="<%= response.getLocale() %>">
<format:setBundle basename="clime.messadmin.admin.i18n.sessionDetail"/>
<% String context = (String) request.getAttribute("context");
   IApplicationInfo webAppStats = (IApplicationInfo) request.getAttribute("webAppStats");
   ISessionInfo currentSession = (ISessionInfo)request.getAttribute("currentSession");
   String currentSessionId = currentSession.getId(); %>
<%--c:url value="${pageContext.request.servletPath}" var="submitUrl" scope="page"/--%><%-- can use value="${pageContext.request.servletPath}" because this JSP is include()'ed --%>
<%-- or use directly ${pageContext.request.requestURI} --%>
<% String submitUrl = request.getContextPath() + request.getServletPath(); /* Can use +request.getServletPath() because this JSP is include()'ed */ %>
<head>
	<%@ include file="inc/meta.inc" %>
	<title><format:message key="page.title"><format:param><core:out value="<%= currentSessionId %>"/></format:param></format:message></title>
	<%@ include file="inc/css.inc" %>
	<style type="text/css">
	</style>
	<script type="text/javascript">//<![CDATA[
		function reloadPage() {
			//window.location.reload();
			document.getElementById('refreshButton').click();
		}
	//]]>
	</script>
</head>
<body>

<div id="menu">
<jsp:include page="inc/menuTools.jsp"/>
<span>
[
<a href="<%=submitUrl%>?<%=AdminActionProvider.ACTION_PARAMETER_NAME%>=<%=ServerInfos.ID%>"><format:message key="menu.serverInfos"/></a>
|
<a href="<%=submitUrl%>?<%=AdminActionProvider.ACTION_PARAMETER_NAME%>=<%=WebAppsList.ID%>"><format:message key="menu.webAppsList"/></a>
|
<a href="<%=submitUrl%>?<%=AdminActionProvider.ACTION_PARAMETER_NAME%>=<%=WebAppStats.ID%>&amp;<%=BaseAdminActionWithContext.CONTEXT_KEY%>=<%=context%>"><format:message key="menu.webAppStats"><format:param><core:out value="<%=webAppStats.getContextPath()%>"/></format:param></format:message></a>
|
<a href="<%=submitUrl%>?<%=AdminActionProvider.ACTION_PARAMETER_NAME%>=<%=SessionsList.ID%>&amp;<%=BaseAdminActionWithContext.CONTEXT_KEY%>=<%=context%>"><format:message key="menu.sessionsList"><format:param><core:out value="<%=webAppStats.getContextPath()%>"/></format:param></format:message></a>
]
</span>
</div>

<h1><format:message key="page.title2"><format:param><core:out value="<%= currentSessionId %>"/></format:param><format:param><core:out value="<%= webAppStats.getServletContextName() %>"/></format:param></format:message></h1>

<jsp:include page="inc/stuckThreads.jsp"/>

<div><span class="collapsible" id="sessionDetails"></span></div>
<div id="sessionDetails-target" style="<core:if test='<%=currentSession.isSecure()%>'> background-color: #F5F6BE;</core:if>">
<table style="text-align: left;" border="0">
  <tr>
    <th><format:message key="session.id"/></th>
    <td colspan="3"><core:out value="<%= currentSessionId %>"/></td>
  </tr>
  <tr>
    <th nowrap="nowrap"><format:message key="request.last_url"/></th>
    <td colspan="3"><span id="url" class="infoballoonable"><core:out value="<%= currentSession.getLastRequestURL() %>"/></span>
    	<div id="url-infoballoon" class="infoballoon">
    		<strong><format:message key="response.last_status"/></strong> <%= currentSession.getLastResponseStatus() > 0 ? ""+currentSession.getLastResponseStatus() : "" %><br />
    		<strong><format:message key="referer"/></strong> <core:out value="<%= currentSession.getReferer() %>"/><br />
    		<strong><format:message key="user_agent"/></strong> <core:out value="<%= currentSession.getUserAgent() %>"/>
    	</div>
    </td>
  </tr>
</table>
<table style="text-align: left;" border="0">
  <tr>
    <th><core:if test="<%=((ApplicationInfo) webAppStats).isMessAdminFullMode()%>"><format:message key="message_pending"/></core:if></th>
    <td><core:if test="<%=((ApplicationInfo) webAppStats).isMessAdminFullMode()%>">
<core:if test="<%=currentSession.getAttribute(Constants.SESSION_MESSAGE_KEY) != null%>"><format:message key="message_pending.yes"/></core:if>
<core:if test="<%=currentSession.getAttribute(Constants.SESSION_MESSAGE_KEY) == null%>"><format:message key="message_pending.no"/></core:if>
</core:if>
    </td>
    <th><format:message key="remote_host"/></th>
    <td><core:out value="<%= currentSession.getRemoteHost() %>"/></td>
  </tr>
  <tr>
    <th><format:message key="user"/></th>
    <td><span title="<format:message key='user.authentication_scheme'><format:param><core:out value='<%=currentSession.getAuthType()%>' default='(none)'/></format:param></format:message>"><core:out value="<%= currentSession.getGuessedUser() %>"/></span></td>
    <th><format:message key="locale"/></th>
    <td><core:out value="<%= currentSession.getGuessedLocale() %>"/></td>
  </tr>
<core:if test="<%= currentSession.isSecure() %>">
  <tr>
    <th><format:message key="ssl.cipher_suite"/></th>
    <td><core:out value="<%= currentSession.getSslCipherSuite() %>"/></td>
    <th><format:message key="ssl.algorithm_size"/></th>
    <td title="<format:message key='ssl.algorithm_size.TT'/>"><format:formatNumber value="<%= currentSession.getSslAlgorithmSize() %>"/></td>
  </tr>
</core:if>
<core:if test="<%= currentSession.getNErrors() > 0 %>">
  <tr style="background-color: red;">
    <th><format:message key="error.count"/></th>
    <td><format:formatNumber value="<%= currentSession.getNErrors() %>"/></td>
    <th><format:message key="error.last"/></th>
    <td>
    	<span id="lastError" class="infoballoonable"><core:out value="<%=currentSession.getLastError().getThrowable()%>"/></span>
    	<div id="lastError-infoballoon" class="infoballoon">
			<table border="0">
				<tr>
					<th><format:message key="error.last.date"/></th>
					<td><format:formatDate value="<%=currentSession.getLastError().getDate()%>" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				</tr>
				<tr>
					<th><format:message key="error.last.uri"/></th>
					<td><core:out value="<%=currentSession.getLastError().getRequestURI()%>"/></td>
				</tr>
			</table>
    	</div>
    </td>
  </tr>
</core:if>
  <tr>
    <td colspan="4"><hr /></td>
  </tr>
  <tr>
    <th><format:message key="session.creation_time"/></th>
    <td><format:formatDate value="<%= currentSession.getCreationTime() %>" pattern="yyyy-MM-dd HH:mm:ss"/></td>
    <th><format:message key="session.last_accessed_time"/></th>
    <td><format:formatDate value="<%= currentSession.getLastAccessedTime() %>" pattern="yyyy-MM-dd HH:mm:ss"/></td>
  </tr>
  <tr>
    <th><format:message key="session.max_inactive_time"/></th>
    <td><format:formatTimeInterval value="<%= currentSession.getMaxInactiveInterval()*1000 %>"/></td>
    <th><span title="<format:message key='session.ttl.TT'/>"><format:message key="session.ttl"/></span></th>
    <td><format:formatTimeInterval value="<%= currentSession.getTTL() %>"/></td>
  </tr>
  <tr>
    <th><format:message key="server_time.total"/></th>
    <td><span title="<format:message key='server_time.total.TT'><format:param value='<%= currentSession.getTotalUsedTime() % 1000 %>'/></format:message>"><format:formatTimeInterval value="<%= currentSession.getTotalUsedTime() %>"/></span></td>
    <th><format:message key="session.idle_time"/></th>
    <td><format:formatTimeInterval value="<%= currentSession.getIdleTime() %>"/></td>
  </tr>
  <tr>
    <th><format:message key="server_time.last"/></th>
    <td><format:formatNumber value="<%= currentSession.getLastUsedTime() %>" type="number"/> ms</td>
    <th><format:message key="request.time.min_max"/></th>
    <td><span id="servertime" class="infoballoonable">
    	<span title="<format:formatDate value='<%= currentSession.getMinUsedTimeDate() %>' type='both' pattern='yyyy-MM-dd HH:mm:ss'/>"><format:formatNumber value="<%= currentSession.getMinUsedTime() %>" type="number"/></span> /
    	<span title="<format:formatDate value='<%= currentSession.getMaxUsedTimeDate() %>' type='both' pattern='yyyy-MM-dd HH:mm:ss'/>"><format:formatNumber value="<%= currentSession.getMaxUsedTime() %>" type="number"/></span>
    	ms</span>
    	<div id="servertime-infoballoon" class="infoballoon">
			<table border="0">
				<caption></caption>
				<tr>
					<th><format:message key="mean"/></th>
					<td class="number"><format:formatNumber value="<%= currentSession.getMeanUsedTime() %>" type="number"/> ms</td>
				</tr>
				<tr>
					<th><format:message key="stdDev"/></th>
					<td class="number"><format:formatNumber value="<%= currentSession.getStdDevUsedTime() %>" type="number"/> ms</td>
				</tr>
			</table>
    	</div>
    </td>
  </tr>
  <tr>
    <td colspan="4"><hr /></td>
  </tr>
  <tr>
    <th><format:message key="n_hits"/></th>
    <td><format:formatNumber value="<%= currentSession.getHits() %>" type="number"/></td>
    <th><format:message key="session.size"/></th>
    <td><format:formatNumber type="bytes"><core:sizeof id="currentSession"/></format:formatNumber></td>
  </tr>
  <tr>
    <th><format:message key="request.last_size"/></th>
    <td><span id="requestSize1" class="infoballoonable"><format:formatNumber value="<%= currentSession.getRequestLastLength() %>" type="bytes"/></span>
    	<div id="requestSize1-infoballoon" class="infoballoon">
			<table border="0">
				<caption></caption>
				<tr>
					<th><format:message key="min"/></th>
					<td class="number"><format:formatNumber value="<%= currentSession.getRequestMinLength() %>" type="bytes"/></td>
				</tr>
				<tr>
					<th><format:message key="max"/></th>
					<td class="number"><format:formatNumber value="<%= currentSession.getRequestMaxLength() %>" type="bytes"/></td>
				</tr>
			</table>
    	</div>
    </td>
    <th><format:message key="request.total_size"/></th>
    <td><span id="requestSize2" class="infoballoonable"><format:formatNumber value="<%= currentSession.getRequestTotalLength() %>" type="bytes"/></span>
    	<div id="requestSize2-infoballoon" class="infoballoon">
			<table border="0">
				<caption></caption>
				<tr>
					<th><format:message key="mean"/></th>
					<td class="number"><format:formatNumber value="<%= currentSession.getRequestMeanLength() %>" type="bytes"/></td>
				</tr>
				<tr>
					<th><format:message key="stdDev"/></th>
					<td class="number"><format:formatNumber value="<%= currentSession.getRequestStdDevLength() %>" type="bytes"/></td>
				</tr>
			</table>
    	</div>
    </td>
  </tr>
<core:if test="<%=((ApplicationInfo) webAppStats).isMessAdminFullMode()%>">
  <tr>
    <th><format:message key="response.last_size"/></th>
    <td><span id="responseSize1" class="infoballoonable"><format:formatNumber value="<%= currentSession.getResponseLastLength() %>" type="bytes"/></span>
    	<div id="responseSize1-infoballoon" class="infoballoon">
			<table border="0">
				<caption></caption>
				<tr>
					<th><format:message key="min"/></th>
					<td class="number"><format:formatNumber value="<%= currentSession.getResponseMinLength() %>" type="bytes"/></td>
				</tr>
				<tr>
					<th><format:message key="max"/></th>
					<td class="number"><format:formatNumber value="<%= currentSession.getResponseMaxLength() %>" type="bytes"/></td>
				</tr>
			</table>
    	</div>
    </td>
    <th><format:message key="response.total_size"/></th>
    <td><span id="responseSize2" class="infoballoonable"><format:formatNumber value="<%= currentSession.getResponseTotalLength() %>" type="bytes"/></span>
    	<div id="responseSize2-infoballoon" class="infoballoon">
			<table border="0">
				<caption></caption>
				<tr>
					<th><format:message key="mean"/></th>
					<td class="number"><format:formatNumber value="<%= currentSession.getResponseMeanLength() %>" type="bytes"/></td>
				</tr>
				<tr>
					<th><format:message key="stdDev"/></th>
					<td class="number"><format:formatNumber value="<%= currentSession.getResponseStdDevLength() %>" type="bytes"/></td>
				</tr>
			</table>
    	</div>
    </td>
  </tr>
</core:if>
</table>
</div>

<div class="noprint" style="text-align: center;">
<p>
<form action="<%= submitUrl %>" method="get">
	<input type="hidden" name="action" value="sessionDetail" />
	<input type="hidden" name="context" value="<%= context %>" />
	<input type="hidden" name="sessionId" value="<core:out value='<%= currentSessionId %>'/>" />
	<input type="number" name="autorefresh" id="autorefresh" min="5" title="<format:message key='autorefresh.TT'/>" value="<core:out value='<%=request.getAttribute("autorefresh")%>'/>" size="3" maxlength="3" onchange="setAutorefresh(this); return false;" />
	<input type="submit" name="refresh" id="refreshButton" value="<format:message key='refresh' />" title="<format:message key='refresh.TT'/>" />
</form>
</p>
</div>

<div class="error"><core:out value='<%= request.getAttribute("error") %>'/></div>
<div class="message"><core:out value='<%= request.getAttribute("message") %>'/></div>

<div id="extraSessionAttributes">
<% int index = 0; %>
<core:forEach items="<%= currentSession.getSessionSpecificData() %>" var="sessionSpecificData" varStatus="status">
<%	DisplayDataHolder sessionSpecificData = (DisplayDataHolder) pageContext.getAttribute("sessionSpecificData");
	String dataTitle = sessionSpecificData.getTitle();
	String dataXHTML = sessionSpecificData.getXHTMLData();
	String refreshTitleURL = submitUrl + '?' + AdminActionProvider.ACTION_PARAMETER_NAME + '=' + ReloadSessionDataProvider.ID + '&' + ReloadDataProviderHelper.PARAM_PROVIDER + '=' + sessionSpecificData.getHTMLId() + '&' + ReloadDataProviderHelper.PARAM_SCOPE + '=' + ReloadDataProviderHelper.SCOPE_TITLE + '&' + BaseAdminActionWithContext.CONTEXT_KEY + '=' + context + '&' + BaseAdminActionWithContextAndSession.SESSION_KEY + '=' + currentSessionId;
	String refreshDataURL = submitUrl + '?' + AdminActionProvider.ACTION_PARAMETER_NAME + '=' + ReloadSessionDataProvider.ID + '&' + ReloadDataProviderHelper.PARAM_PROVIDER + '=' + sessionSpecificData.getHTMLId() + '&' + ReloadDataProviderHelper.PARAM_SCOPE + '=' + ReloadDataProviderHelper.SCOPE_CONTENT + '&' + BaseAdminActionWithContext.CONTEXT_KEY + '=' + context + '&' + BaseAdminActionWithContextAndSession.SESSION_KEY + '=' + currentSessionId;
	if (dataTitle != null && dataXHTML != null) {
		++index; %>
	<fieldset><%--
Refresh
0x21BA	8634	ANTICLOCKWISE OPEN CIRCLE ARROW	↺
0x21BB	8635	CLOCKWISE OPEN CIRCLE ARROW	↻
0x21B9	8633	LEFTWARDS ARROW TO BAR OVER RIGHTWARDS ARROW TO BAR	↹
0x21C4	8644	RIGHTWARDS ARROW OVER LEFTWARDS ARROW	⇄
0x21C6	8646	LEFTWARDS ARROW OVER RIGHTWARDS ARROW	⇆
--%>
		<legend><a href="#" title="<format:message key='refresh'/>" style="text-decoration: none;" onclick="javascript:/*jah('<%=refreshTitleURL%>','extraSessionAttributes-<%=index%>');*/jah('<%=refreshDataURL%>','<%=sessionSpecificData.getHTMLId()%>');return false;">&#8635;</a><span class="collapsible" id="extraSessionAttributes-<%=index%>"><%= dataTitle %></span></legend>
		<div id="extraSessionAttributes-<%=index%>-target"><div id="<%=sessionSpecificData.getHTMLId()%>"><%= dataXHTML %></div></div>
	</fieldset>
<% } %>
</core:forEach>
</div>

<jsp:include page="inc/footer.jsp"/>

<%@ include file="inc/js.inc" %>
	<script type="text/javascript" defer="defer">//<![CDATA[
		addWindowOnLoadHandler(function() {
			setAutorefresh(document.getElementById('autorefresh'));
		});
	//]]>
	</script>
</body>
</html>