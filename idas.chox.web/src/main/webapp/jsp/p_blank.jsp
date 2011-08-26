<%@ page contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
            String statusMsg = request.getParameter("statusMsg");
            if (statusMsg == null) {
                statusMsg = "";
            }
            pageContext.setAttribute("statusMsg",statusMsg);

%>
<h1><c:out value="${statusMsg}"/></h1>