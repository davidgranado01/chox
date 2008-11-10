<%-- 
    Document   : UploadClaims
    Created on : 09-Nov-2008, 23:28:37
    Author     : Emmanuel
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<head >
    <title>Upload Claims</title >
</head>
<body >
    <s:text>uploadclaims.title</s:text>
    <s:form action ="UploadClaims" method ="POST" enctype="multipart/form-data">
        <s:file name ="upload" label ="Claim XML File" size="55"/>     
        <s:submit />
    </s:form>
</body>
</html> 