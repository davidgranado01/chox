<!doctype html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
    <head>
        <title>Unexpected Error</title>
    </head>

    <body>
        <h2>An unexpected error has occurred</h2>
        <p>
            Please report this error to the appropriate technical support personnel.<br/>
            Thank you for your cooperation.
        </p>
        <hr/>
        <h3>Error Message (if available)</h3>
        <s:actionerror/>
        <p>
            <s:property value="%{exception.message}"/>
        </p>
        <hr/>
<%--
        <h3>Technical Details</h3>
        <s:property value="%{exceptionStack}"/>
--%>
    </body>
</html>
