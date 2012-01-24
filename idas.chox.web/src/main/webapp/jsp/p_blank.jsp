<%@ page contentType="text/html; charset=UTF-8" %>

<%
            String statusMsg = request.getParameter("statusMsg");
            if (statusMsg == null) {
                statusMsg = "";
            }
            pageContext.setAttribute("statusMsg",statusMsg);

%>
