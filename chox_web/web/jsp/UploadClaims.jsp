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
    <s:form action ="UploadClaims" method ="POST" enctype="multipart/form-data">
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