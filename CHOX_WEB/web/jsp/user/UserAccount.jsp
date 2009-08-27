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

            $.validator.addMethod(
            "regex",
            function(value, element, regexp) {
                var check = false;
                var re = new RegExp(regexp);
                return this.optional(element) || re.test(value);
            },
            "Please check your input."
        );

            $("#formChangePassword").validate(
            {
                errorLabelContainer: "#errorMessageBox",
                rules: {
                    newPassword: {required:true, regex: "^.*(?=.{6,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*$"},
                    confirmNewPassword: {
                        equalTo: "#newPassword"
                    }
                }
                ,
                messages: {

                    newPassword: {
                        required:"You must supply a value for 'New Password'", regex: "Incorrect Password Format"
                    },
                    confirmNewPassword: {
                        equalTo:"Your passwords do not match"
                    }
                },
                submitHandler: function(form) {
                    $(form).ajaxSubmit(op);
                }

            });
        });

        function onBeforeSubmit(formData, jqForm, options) {

        }

        function onSubmitResponseReceived(responseText, statusText)  {
            responseText = responseText.trim();
            // $('input[@name=newPassword]').val("");
            // $('input[@name=confirmNewPassword]').val("");
                <s:if test="isShowMessage">
                      window.location= "user/inbox.action";
                </s:if>
                <s:else>
                        var response = eval('(' + responseText.trim() + ')');
                        if(response.isValid)
                        {
                            $("#submitResult").text(response.result);
                        }
                        else
                        {
                            $("#submitResult").text(formErrors(data.errors));
                        }

                </s:else>

                    }

                    function onSubmitError(XMLHttpRequest, textStatus, errorThrown) {
                        responseText = responseText.trim();
                        $('input[@name=newPassword]').val("");
                        $('input[@name=confirmNewPassword]').val("");
                        $("#errorMessageBox").text(responseText);
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
                <s:if test="isShowMessage">
                    <div class="status-info"><s:property value="message" /></div>
                </s:if>
                
                <fieldset class="x-fieldset">
                    <legend>User Details</legend>
                    <div class="form-container">
                        <div class="chox-form-item">
                            <label class="chox-form-std-label">First Name</label>
                        <label class="std-data-ro"><s:property value="webUser.firstName" /></label></div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label">Last Name</label>
                        <label class="std-data-ro"><s:property value="webUser.lastName" /></label></div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label">Email Address</label>
                        <label class="std-data-ro"><s:property value="webUser.email" /></label></div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label">Organisation</label>
                        <label class="std-data-ro"><s:property value="webUser.organisationName" /></label></div>
                    </div>
                </fieldset>

                <fieldset class="x-fieldset">
                    <legend>Change Password</legend>
                    <form onsubmit="return true;" id="formChangePassword" action="user/changePassword.action" class="XXentity-form" method="post">
                        
                        <div class="status-info">
                            N.B. Passwords are case sensitive. Must be at least 6 characters.<br/>
                            Must contain at least one lower case letter, one upper case letter, and one number. 
                        </div>
    
                        <div class="form-container" style="padding-top:10px;">
                            
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">
                                Choose a new password <span class="mandatory">*</span></label>
                            <input type="password" class="chox-ttxt" label="Enter Password" name="newPassword" id="newPassword" size="10" maxlength="10" />
                            </div>
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">
                                Re-enter new password<span class="mandatory">*</span></label>
                            <input type="password" class="chox-ttxt" label="Enter Password" name="confirmNewPassword" id="confirmNewPassword" size="10" maxlength="10" />
                            </div>
                            <div class="chox-form-button">
                                <label class="chox-form-std-label">&nbsp;</label>
                                <input type="submit" value="Save"/>
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







