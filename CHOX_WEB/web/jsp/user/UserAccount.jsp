<%-- 
    Document   : UserAccount
    Created on : 09-Feb-2009, 17:26:27
    Author     : Emmanuel
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<head>
    <title>IDAS-CHOX</title>
    
    
    <link href="<%= request.getContextPath()%>/styles/chox.css" rel="stylesheet" type="text/css" media="all"/>        
    <link href="<%= request.getContextPath()%>/css/ext-all.css" rel="stylesheet" type="text/css" media="all"/>
    
    <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery-1.2.6.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.form.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/ext-jquery-adapter.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.blockUI.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.timer.js"></script>  
    
    <script src="<%= request.getContextPath()%>/scripts/ext-base.js" type="text/javascript"></script>
    <script src="<%= request.getContextPath()%>/scripts/ext-all.js" type="text/javascript"></script> 
    <script src="<%= request.getContextPath()%>/scripts/Application.js" type="text/javascript"></script> 
    <script src="<%= request.getContextPath()%>/scripts/general.js" type="text/javascript"></script> 
    
</head>

<body>
    
    <div class="outer">
        
        <div class="inner">
            
            
            <div class="outer" id="outerDiv">
                
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
                                            <a href="javascript:openFile('<%= request.getContextPath()%>','ChoHelp');">Help</a>
                                        </s:if>
                                        <s:else>
                                            <a href="javascript:openFile('<%= request.getContextPath()%>','InsHelp');">Help</a>
                                        </s:else>&nbsp;|&nbsp;
                                        <a href="javascript:openFile('<%= request.getContextPath()%>','Support');">Support</a>&nbsp;|&nbsp; 
                                        <a href="javascript:onOpenAbout();">About CHOX</a>&nbsp;|&nbsp;
                                        <b><s:property value="CurrentUserDesc" /></b>&nbsp;&nbsp;<a href="<%=request.getContextPath()%>/j_acegi_logout">( Log Off )</a>&nbsp;
                                        <a href="#">( User Account )</a>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    
                </div>
            </div>
            
            <div class="chox-claim-header x-panel-bwrap chox-form-container">   
                
                <form id="formUpdateHireVehicle" action="user/updateVehicleHire.action" class="XXentity-form">
                    <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
                    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
                    <fieldset class="x-fieldset">
                        <legend>User Account Details</legend>
                        <div class="form-container">            
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">First Name</label>
                            <label class="std-data-ro"><s:property value="webUser.firstName" /></label></div>
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">Last Name</label>
                            <label class="std-data-ro"><s:property value="webUser.lastName" /></label></div>
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">Organisation</label>
                            <label class="std-data-ro"><s:property value="webUser.organisationName" /></label></div>
                        </div>
                    </fieldset>
                </form> 
                
            </div>
            
        </div>
    </div>
    
    <div class="footerText">
    ©2009 Sherwood Compliance Services Ltd | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','Copyright');" class="footerText">Copyright</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','PrivacyPolicy');" class="footerText">Privacy Policy</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','TermsOfService');" class="footerText">Terms of Service</a></div>
</body>







