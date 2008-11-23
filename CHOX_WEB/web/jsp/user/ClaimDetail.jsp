<%-- 
    Document   : claimDetail
    Created on : 23-Nov-2008, 12:04:54
    Author     : Emmanuel
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h2>Claim Detail For <s:property value="id" /></h2>
        
        <s:property value="model.choReference" />
    </body>
</html>
