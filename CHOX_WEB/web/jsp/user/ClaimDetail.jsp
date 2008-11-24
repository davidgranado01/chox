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
        <s:form action="UpdateClaimDetail"  >   
            <table>
                <tr>
                    <td width="200px">System Id</td><td><s:textfield name="id" readonly="true"/></td>
                </tr>
                <tr>
                    <td>CHO Ref</td><td><s:property value="model.choReference" /></td>
                </tr>
                <tr>
                    <td>Claim Status</td>
                    <td>
                        
                        <s:select name="status" value="model.status" list="statuses" listKey="value" listValue="text" emptyOption="false"></s:select>
                        <s:submit value="Update" />
                        
                    </td>
                </tr>
            </table>
            <br>
            <span style="color:green;">
                <s:actionmessage />
                <s:actionerror />
                <b><s:property value="actionMessage" /></b>
            </span>
        </s:form>
        
    </body>
</html>
