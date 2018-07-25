<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html>

    <body>

        <div class='main' id='main'>
            <h1>Welcome to CHOX</h1>  

            <authz:authorize ifNotGranted="ROLE_USER">
                <s:url id="login" action="login" namespace="/" includeParams="none"/>
                <s:a href="%{login}">Log in</s:a>
            </authz:authorize>
            <authz:authorize ifAllGranted="ROLE_USER">
                <ul>
                    <li>Upload Your Claims? <a href="<s:url action="uploadClaims" namespace="user" includeParams="none"/>">click here</a></li>
                    <li>Goto My Inbox <a href="<s:url action="inbox" namespace="user" includeParams="none"/>">click here</a></li>
                    <li><a href="<%=request.getContextPath()%>/logout">Log Off</a></li>
                </ul>   
            </authz:authorize>    

        </div>        

    </body>
</html>