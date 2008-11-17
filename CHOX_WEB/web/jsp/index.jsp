<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="authz" uri="http://acegisecurity.org/authz" %>

<html>
    
    <body>
        
        <div class='main' id='main'>
            
            <h1>Welcome to CHOX</h1>  
            
            <authz:authorize ifNotGranted="ROLE_USER">
                <s:url id="login" action="login" namespace="/" />
                <s:a href="%{login}">Log in</s:a>
            </authz:authorize>
            <authz:authorize ifAllGranted="ROLE_USER">
                <ul>
                    <li>Upload Your Claims? <a href="<s:url action="UploadClaims" namespace="claim"/>">click here</a></li>
                    <li>Goto My Inbox <a href="<s:url action="Inbox" namespace="claim"/>">click here</a></li>
                    <li><a href="<%=request.getContextPath()%>/j_acegi_logout">
                    <s:text name="link.logoff" /></a></li>
                </ul>   
            </authz:authorize>
            
           
            
            
        </div>        
        
    </body>
</html>