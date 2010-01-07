<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<%
            String statusMsg = request.getParameter("statusMsg");
            if (statusMsg == null) {
                statusMsg = "";
            }

%>
<h1><%= statusMsg%></h1>