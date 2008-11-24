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
        <div style="height:96%; overflow:auto; border:solid 1px black;">
            <s:if test="results.size()==0">
                <s:text name="text.noClaims" />
            </s:if>
            <s:else>
                <table class="UploadStatusTable">
                    <tr>
                        <th width="40px"></th>
                        <th width="40px">Supplier Reference</th>
                        <th>Upload Status</th>
                         <th>Claim Status</th>
                        <th>Remark</th>
                    </tr>
                    <s:iterator id="next" value="results" status="stat">
                        <s:if test="%{#next.UploadStatusCode=='CLAIMUPLOADFAILED'}">
                            <tr class="ErrorRow">
                        </s:if>
                        <s:elseif test="%{#next.UploadStatusCode=='INVOICEUPLOADFAILED'}">
                            <tr class="ErrorRow">
                        </s:elseif>
                        <s:elseif test="%{#next.UploadStatusCode=='INVOICEUPLOADEDSUCCESSFUL'}">
                            <tr class="<s:property value="#next.claim.status"/>">
                        </s:elseif>
                        <s:else>
                            <tr>
                        </s:else>               
                            <td><s:property value="{#stat.index + 1}" /></td>
                            <td><s:property value="#next.claim.choReference" /></td> 
                            <td><s:property value="#next.uploadStatus" /></td>  
                            <td><s:property value="#next.claim.status" /></td>
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
        <br />
        <s:url id="goBackToInbox" action="inbox" />
        <s:url id="reUpload" action="uploadClaims" />
        <s:a href="%{goBackToInbox}" >&#60;&#60; Go back to Inbox</s:a>
        <br /> <br /> 
        <s:a href="%{reUpload}" >&#60;&#60; Re-Upload</s:a>
        
    </body>
</html>
