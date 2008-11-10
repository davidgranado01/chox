<%-- 
    Document   : inbox
    Created on : 10-Nov-2008, 10:02:30
    Author     : Emmanuel
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/struts-tags" prefix="s" %>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My Inbox</title>
    </head>
    <body>
        
        <div>
                <s:action name="ListAllClaims" executeResult="true" />                
        </div>      
        
    </body>
</html>
