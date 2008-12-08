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

        <title>JSP Page</title>
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
                                <div class="top-menu">
                                    <a href="<s:url action="inbox"/>">Home</a>&nbsp;|&nbsp;
                                    <s:if test="isCHO">
                                    <a href='<s:url action="uploadClaims"/>'>XML Uploads</a>&nbsp;|&nbsp;
                                    </s:if>
                                     <s:if test="isCHO">
                                        <a href="javascript:openFile('<%= request.getContextPath()%>/download/iDAS_CHOX_CHO_UG_1.1-1.pdf','Help');">Help</a>
                                    </s:if>
                                    <s:else>
                                        <a href="javascript:openFile('<%= request.getContextPath()%>/download/iDAS_CHOX_IUG_1.0-1.pdf','Help');">Help</a>
                                    </s:else>&nbsp;|&nbsp;
                                    <a href="#">Support</a>&nbsp;|&nbsp;
                                    <a href="#">About Chox</a>&nbsp;|&nbsp;
                                    <a href="<%=request.getContextPath()%>/j_acegi_logout">Log Off</a>
                                </div>
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


                        <table class="UploadStatusTable" cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <th width="40px"></th>
                                <th width="40px">Supplier Reference</th>
                                <th>Upload Status</th>
                                <th>Claim Status</th>
                                <th>Further Information</th>
                            </tr>
                            <s:iterator id="next" value="results" status="stat">
                                <s:if test="%{#next.UploadStatusCode=='CLAIMUPLOADFAILED'}">
                                    <tr class="ErrorRow" valign="top">
                                </s:if>
                                <s:elseif test="%{#next.UploadStatusCode=='INVOICEUPLOADFAILED'}">
                                    <tr class="ErrorRow"   valign="top">
                                </s:elseif>
                                <s:elseif test="%{#next.UploadStatusCode=='INVOICEUPLOADEDSUCCESSFUL'}">
                                    <tr class="<s:property value="#next.claim.status"/>"  valign="top">
                                </s:elseif>
                                <s:else>
                                    <tr  valign="top">
                                    </s:else>
                                    <td><s:property value="{#stat.index + 1}" /></td>
                                    <td><s:property value="#next.claim.choReference" /><span>&nbsp;</span></td>
                                    <td><s:property value="#next.uploadStatus" /></td>
                                    <td><s:property value="#next.claim.status" /><span>&nbsp;</span></td>
                                    <td>
                                        <ul>
                                            <s:iterator id="remark" value="#next.DataValidationRemark">
                                                <s:if test="#remark.length() > 0">
                                                    <li><s:property value="#remark" /></li>
                                                </s:if>
                                            </s:iterator>
                                        </ul>

                                        <ul>
                                            <s:iterator id="remark" value="#next.SchemaValidationRemark">
                                                <s:if test="#remark.length() > 0">
                                                    <li><s:property value="#remark" /></li>
                                                </s:if>
                                            </s:iterator>
                                        </ul>
                                    <span>&nbsp;</span></td>
                                </tr>
                            </s:iterator>
                        </table>
                    </s:else>
                </div>



            </div>

        </div>

    </body>
</html>
