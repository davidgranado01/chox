<%-- 
    Document   : UploadFailed
    Created on : 10-Nov-2008, 01:11:50
    Author     : Emmanuel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Upload Failed</title>
    </head>
    <body>
        <br /> 
        <br /> 
        <h2>No Xml document selected for upload or file type not recognised. Please ensure you are trying to upload an xml file and resubmit
        </h2>
        
        <br /> 
        <s:url id="reUpload" action="uploadClaims" />
        <s:a href="%{reUpload}" >&#60;&#60; Re-Upload</s:a>
    </body>
</html>
