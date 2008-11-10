<%-- 
    Document   : UploadStatus
    Created on : 09-Nov-2008, 23:40:54
    Author     : Emmanuel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/struts-tags" prefix="s" %>

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
                <table class="statusTable">
                    <tr>
                        <td>Id</td><td>Supplier Reference</td>
                    </tr>
                <s:iterator id="next" value="results" status="stat">
                    <tr>
                        <td><s:property value="#stat.index" /></td>
                        <td><s:property value="#next.SupplierReference" /></td> 
                        <td><s:property value="#next.Status" /></td>  
                        <td>
                                <ul>
                                    <s:iterator id="remark" value="#next.DataValidationRemarkInList">                                        
                                        <s:if test="#remark.length() > 0">
                                            <li><s:property value="#remark" /></li>
                                        </s:if>
                                    </s:iterator>
                                </ul>                              
               
                                <ul>
                                    <s:iterator id="remark" value="#next.SchemaValidationRemarkInList">
                                        <s:if test="#remark.length() > 0">
                                            <li><s:property value="#remark" /></li>
                                        </s:if>
                                    </s:iterator>
                                </ul> 
                        </td>
                    </tr>                   
                </s:iterator>
                </table>
            </s:else>       
        </div>      
    </body>
</html>
