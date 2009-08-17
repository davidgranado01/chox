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
        <link href="<%= request.getContextPath()%>/styles/chox.css" rel="stylesheet" type="text/css" media="all"/>
        <link href="<%= request.getContextPath()%>/styles/main.css" rel="stylesheet" type="text/css" media="all"/>
        <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery-1.2.6.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.form.js"></script>    
        <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.blockUI.js"></script>
        <script src="<%= request.getContextPath()%>/scripts/general.js" type="text/javascript"></script>
        <title>IDAS-CHOX</title > 
    </head>
    <body>
        <div class="outer">
            <div class="inner">
                <div id="chox-menu">
                    <table cellpadding="0" cellspacing="0" border="0" width="100%">
                        <tr valign="middle">
                            <td>
                                <img src="<%= request.getContextPath()%>/images/chox_logo_small.jpg" style="display: inline; float: left" />
                            </td>
                            <td width="100%" align="right">
<ul id="top-menu">
    <li><a href="<s:url action="inbox"/>">&nbsp;Home&nbsp;</a></li>
    <li><a href="<s:url action="openUserAccount" />">|&nbsp;Settings&nbsp;</a></li>
    <s:if test="isCHO"><li><a href='<s:url action="uploadClaims"/>'>|&nbsp;XML Uploads&nbsp;</a></li></s:if>
    <li><s:if test="isCHO"><a href="javascript:openFile('<%= request.getContextPath()%>','ChoHelp');">|&nbsp;Help&nbsp;</a></s:if><s:else><a href="javascript:openFile('<%= request.getContextPath()%>','InsHelp');">|&nbsp;Help&nbsp;</a></s:else></li>
    <li><a href="#" onmouseover="mopen('m2')" onmouseout="mclosetime()">|&nbsp;Support&nbsp;</a>
        <div id="m2" onmouseover="mcancelclosetime()" onmouseout="mclosetime()">
        <a href="javascript:openFile('<%= request.getContextPath()%>','Support');">Support Procedure</a>
        <a href="<s:url action="onlineSupport"/>">Online Support Form</a>
        </div></li>
    <li><a href="javascript:onOpenAbout();">|&nbsp;About CHOX&nbsp;</a></li>
    <li><a href="<%=request.getContextPath()%>/j_acegi_logout" >|&nbsp;<b><s:property value="CurrentUserDesc" /></b> ( Log Off )</a></li>
</ul>
<div style="clear:both"></div>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="UploadStatusContainer">
                    <s:if test="results.size()==0">
                        <div class="UploadStatusMessage">
                            <div class="status-info">
                                Couldn't find any claims in the input file! Please try again.  
                            </div>
                        </div>
                    </s:if>
                    <s:else>
                        

<div class="UploadStatusMessage">
                                
                                <div class="status-info">
                                    
                                    Please carefully review the infomation provided below, as it contains important information regarding the claims you have uploaded.
                                    <br/>If the XML file that you have supplied contains errors, please correct any errors in accordance with the information specified in the "Further Information" column on the table below
                                    <p style="text-align:center">
                                        <s:url id="goBackToInbox" action="inbox" />
                                    <s:url id="reUpload" action="uploadClaims" /></p>
                                    <s:a href="%{goBackToInbox}" >Proceed to CHOX Inbox</s:a>
                                    <br /> <br />
                                    <s:a href="%{reUpload}" >Re-Upload XML file</s:a>
                                    
                                    
                                </div>
                                
</div>
                            
<div class="bordereauResultHolder">
    
    <table class="BordereauResultTable" cellpadding="0" cellspacing="0" border="0">
        <tr>
            <td width="15%" class="titleLabel">Created Date</td><td width="35%"><s:property value="bordereauResult.CreatedDate" /></td>
            <td width="15%" class="titleLabel">Created By</td><td width="35%"><s:property value="bordereauResult.CreatedBy.DisplayName" /></td>
        </tr>
        <tr>
            <td width="15%" class="titleLabel">Status</td><td width="35%"><s:property value="bordereauStatus" /></td>
            <td width="15%" class="titleLabel">Total Claims</td><td width="35%"><s:property value="totalClaim" /></td>
        </tr>
        <tr>
            <td width="15%" class="titleLabel">Description</td>
            <td colspan="3"><s:property value="bordereauStatusDesc" /></td>
        </tr>
        <tr>
            <td width="15%" class="titleLabel">Messages</td>
            <td colspan="3">
                <ul class="bordereauErrorMessage">
                <s:iterator id="bordereauMsg" value="bordereauResult.Message" status="stat">
                    <li><s:property value="#bordereauMsg" /></li>
                </s:iterator>
                </ul>
            </td>
        </tr>                                
    </table>
    
</div>
<div class="bordereauResultHolder">
    
                            <table class="UploadStatusTable" cellpadding="0" cellspacing="0" border="0">
                                
                                <tr>
                                    <th width="30px"></th>
                                    <th width="1%" nowrap="true">Supplier Reference&nbsp;&nbsp;</th>
                                    <th>Upload Status</th>
                                    <th>Claim Status</th>
                                    <th>Uploaded?</th>
                                </tr>
                                
                                <s:iterator id="next" value="bordereauResult.ClaimResult" status="stat">

<s:if test="%{#next.ClaimParseStatus.toString()=='newInvoice'}">
    <tr class="<s:property value="#next.claim.status"/>" valign="top">
</s:if>
<s:elseif test="%{#next.ClaimParseStatus.toString()=='newClaim'}">
    <tr class="<s:property value="#next.claim.status"/>" valign="top">
</s:elseif>
<s:elseif test="%{#next.ClaimParseStatus=='ClaimNotEditable'}">
    <tr class="<s:property value="#next.claim.status"/>" valign="top">
</s:elseif>                                    
<s:elseif test="%{#next.ClaimParseStatus.toString()=='existClaim'}">
    <tr class="<s:property value="#next.claim.status"/>" valign="top">
</s:elseif>
<s:elseif test="%{#next.ClaimParseStatus.toString()=='existInvoice'}">
    <tr class="ErrorRow" valign="top">
</s:elseif>
<s:elseif test="%{#next.ClaimParseStatus.toString()=='invalidSchema'}">
    <tr class="ErrorRow" valign="top">
</s:elseif>                                    
<s:else>
    <tr valign="top">
</s:else>

        <td><s:property value="{#stat.index + 1}" /></td>
        <td><s:property value="#next.claim.choReference" /><span>&nbsp;</span></td>
        <td><s:property value="#next.UploadedStatus" /><span>&nbsp;</span></td>
        <td><s:property value="#next.claim.status" /><span>&nbsp;</span></td>
        <td><s:property value="#next.ProcessStatus" /><span>&nbsp;</span></td>

    </tr>

<s:if test="#next.Message.size() > 0">
    <tr>
        <td colspan="5">
            <ul>
            <s:iterator id="remark" value="#next.Message">
            <s:if test="#remark.length() > 0">
                <li><s:property value="#remark" /></li>
            </s:if>
            </s:iterator>
            </ul>
        </td>
    </tr>
</s:if>
                                    
                                </s:iterator>
                            </table>
</div>                            
                            
                        </s:else>
                    </div>
                </div>

<div class="footerText">
©2009 Sherwood Compliance Services Ltd | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','Copyright');" class="footerText">Copyright</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','PrivacyPolicy');" class="footerText">Privacy Policy</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','TermsOfService');" class="footerText">Terms of Service</a></div>

            </div>
    </body>
</html>
