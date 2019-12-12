<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

    <head>
        <title>CHOX Welcome Page</title>
        <link href="<%= request.getContextPath()%>/css/login.css" rel="stylesheet" type="text/css" media="all"/>

        <script type="text/javascript">
            var newwindow;
            function openFile(url,name)
            {
                newwindow=window.open(url,name);
                if (window.focus) {newwindow.focus();}
            }

        </script>

    </head>
    <body class="modal login">
        <div class="outer">
            <!--img alt="Logo" src="<%= request.getContextPath()%>/images/Audatex-Logo.png" style="display: inline; float: center; width: 250px; height: 74px"/-->
            <img alt="Logo" src="<%= request.getContextPath()%>/images/SoleraAudatex_logo.png" style="display: inline; float: center; width: 400px; height: 74px"/>
        </div>

        <div class="modal-container">
            <div class="app-title"><img alt="Logo" src="<%= request.getContextPath()%>/images/chox_logo_small.jpg" style="display: inline; float: center; width: 77px; height: 22px"/></div>

            <div class="login-inner">
                <div class="content" id="loginPanel">
                    <form autocomplete="off" action="<%=request.getContextPath()%>/j_spring_security_check" method="POST" >
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <div class="login_form">
                            <p class="password_entry">
                                <label for="name">
                                    Username:</label>
                                <input type="text" id="loginUserNameId" name="username" style="width:150px" />
                            </p>
                            <p class="password_entry">
                                <label for="password">
                                    Password:</label>
                                <input type="password"  id="loginPasswordId" name="password" style="width:150px"/>
                            </p>
                            <div class="checkbox_and_submit">
                                
                                <p>
                                    <input type="submit" id="loginSubmitButtonId" value="Sign In" /><!--<a href="requestForgetPassword.action" class="forget-password-link">Forget Password</a>!-->
                                </p>
                                
                                <s:if test="#parameters.error[0] == 'true'">
                                    <p><span id="login-error">Incorrect Username and/or Password </span></p>
                                </s:if>
                                <s:elseif test="#parameters.iperror[0] == 'true'">
                                    <p><span id="login-error">Due to your current IP address, you are not authorised to access CHOX.<br/> Please contact Audatex Support on 03333 404327.</span></p>
                                </s:elseif>
                                <s:elseif test="#parameters.blocked[0] == 'true'">
                                    <p><span id="login-error"><%= request.getParameter("message") %></span></p>
                                </s:elseif>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <br/>
        <div class="below">
            <span class="note" style="font-size:11px">
                <a href="javascript:openFile('<%= request.getContextPath()%>/terms_of_service.html','TermOfService');">Terms of Service</a></span>
        </div>
        <div class="footerText">This is a Audatex (UK) Limited proprietary system. No use is allowed without appropriate authorisation.<br/>
Unauthorised use of this system will constitute a breach of Audatex (UK) Limited policy and<br/>
prosecution under pertinent legislation will apply. This system uses cookies, signing into this system will<br/>
be taken as consent to use cookies, for details on how cookies are used see ‘Terms of Service’ above.<br/>
This system has been penetration certified by Digital Assurance. View certificate <a  href="javascript:openFile('<%= request.getContextPath()%>/download/CHOXCertificate.pdf');">here</a>.
<br/><br/>
               By using the Audatex product you understand and acknowledge that you will not enter any Personal Data, the term being<br/>
               as defined under Regulation (EU) 2016/679 (or any superseding legislation in the UK from time to time) ("GDPR"),<br/>
               including but not limited to the types and categories of Personal Data listed, defined,<br/>
               or referenced to in Articles 8 - 10 of the GDPR (collectively "High Risk Personal Data"),<br/>
               into any of the free text fields, nor will you incorporate any Personal Data or High Risk Personal Data<br/>
               into any of the images or attachments that you might upload into the Audatex system from time to time.
<br/><br/>
               The entering of Personal Data into free text fields or the incorporation of Personal Data<br/>
               into images or attachments would result in the breach of your contract and/or agreement with us<br/>
               and/or the breach of relevant data protection laws and subject you to legal liability.<br/>
               For more information, please refer to the agreement that you have with us and relevant<br/>
               data protection legislations or contact our Service Desk at [servicedesk@audatex.co.uk].
<br/><br/>
               We process your personal data in accordance with our privacy policy.
<br/><br/>
               By using our service you agree that you have read and understood our privacy policy set out on our website.
</div>
   </body>
</html>
