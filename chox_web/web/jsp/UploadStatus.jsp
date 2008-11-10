<%-- 
    Document   : UploadStatus
    Created on : 09-Nov-2008, 23:40:54
    Author     : Emmanuel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h2>Your Claims Uploaded</h2>
        <div>
            <s:if test="results.size()==0">
                <s:text name="text.noClaims" />
            </s:if>
            <s:else>
                <s:iterator id="next" value="Results">
                    <h3><s:property value="#next.TpClaimReference" /></h3>
                </s:iterator>
            </s:else>       
        </div>      
    </body>
</html>
