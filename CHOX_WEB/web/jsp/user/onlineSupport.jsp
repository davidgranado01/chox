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
    <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.blockUI.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/ext-jquery-adapter.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.metadata.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.validate.min.js"></script> 
    
    <script src="<%= request.getContextPath()%>/scripts/ext-base.js" type="text/javascript"></script>
    <script src="<%= request.getContextPath()%>/scripts/ext-all.js" type="text/javascript"></script> 
    <script src="<%= request.getContextPath()%>/scripts/Application.js" type="text/javascript"></script> 
    <script src="<%= request.getContextPath()%>/scripts/general.js" type="text/javascript"></script> 
    
    <script>
        
        $(document).ready(function () {
            
            var op = { 
                beforeSubmit:  onBeforeSubmit,  // pre-submit callback 
                success:       onSubmitResponseReceived,  // post-submit callback 
                timeout: 3000,
                error: onSubmitError
            };

            $("#supportMessageForm").validate(
            {
                errorLabelContainer: "#errorMessageBox",                
                rules: {
                    iSubject:{
                        required:true
                    },
                    iMessage:{
                        required:true
                    }
                },
                messages: {
                    iSubject:{
                        required:"You must supply a value for 'Subject'"
                    },
                    iMessage:{
                        required:"You must supply a value for 'Message'"
                    }
                },
                submitHandler: function(form) {
                    $(form).ajaxSubmit(op);
                }   

            });
        });
        
        function onBeforeSubmit(formData, jqForm, options) { 
            $.blockUI();
        
        
        }
        
        function onSubmitResponseReceived(responseText, statusText)  {   
            $.unblockUI();            
            responseText = responseText.trim();
            $('#iSupplierReference').val("");
            $('#iSubject').val("");
            $('#iMessage').val("");
            $("#submitResult").text(responseText);
        }   

        function onSubmitError(XMLHttpRequest, textStatus, errorThrown) {
            $.unblockUI();
            $("#submitResult").text("Unexpected error encountered, Please try again!");          
        }
        
        
    </script>
    
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
                    
                </div>
            </div>
            
            <div class="chox-claim-header x-panel-bwrap chox-form-container">   
                
                <fieldset class="x-fieldset">
                    <legend>Online Support Form</legend>
                    <form onsubmit="return true;" action="user/submitSupportMessage.action" class="XXentity-form" method="post" id="supportMessageForm">
                        <div class="form-container">                                             
                            
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">Supplier Reference</label>
                                <input type="text" class="chox-ttxt" name="iSupplierReference" id="iSupplierReference" size="10" maxlength="10" /></div>
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">Subject<span class="mandatory">*</span></label>
                                <input type="text" class="chox-textarea" name="iSubject" id="iSubject" size="20" maxlength="100" /> 
                            </div>
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">Message<span class="mandatory">*</span></label>
                                <textarea class="chox-canote" cols="20" rows="5" name="iMessage" id="iMessage"></textarea>
                            </div>
                            <div class="chox-form-button">
                                <input type="submit" value="Submit"/>
                            </div>
                            
                            <div id="submitResult" class="chox-form-submit-result"></div>
                            
                            <div class="errorBox" id="errorMessageBox"></div>
                        </div>
                    </form>
                </fieldset>  
                
            </div>
        </div>
        
    </div>
    
    <div class="footerText">
    ©2009 Sherwood Compliance Services Ltd | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','Copyright');" class="footerText">Copyright</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','PrivacyPolicy');" class="footerText">Privacy Policy</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','TermsOfService');" class="footerText">Terms of Service</a></div>
</body>







