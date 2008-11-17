<%-- 
    Document   : UploadClaims
    Created on : 09-Nov-2008, 23:28:37
    Author     : Emmanuel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
    <head >
        <title>Upload Claims</title >
    </head>
    <body >
        <s:form action ="ProcessClaimsAction" method ="POST" enctype="multipart/form-data">
            <table>
                <tr>
                    <td>
                        <s:file name ="upload" label ="Claim XML File" size="55"/>   
                    </td>
                </tr>
                <tr>
                    <td>
                        <s:select
                            label="Upload Type"
                            name="uploadType"
                            list="#{'c':'Claim Only','a':'All'}"
                            headerKey="-1"
                            headerValue="--- Please Select ---"
                            emptyOption="false"
                            value="c" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <s:submit />
                    </td>
                </tr>
            </table>     
            
        </s:form>
    </body>
</html> 