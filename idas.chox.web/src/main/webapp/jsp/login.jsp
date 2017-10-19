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
            <img alt="Logo" src="<%= request.getContextPath()%>/images/Audatex-Logo.png" style="display: inline; float: center; width: 250px; height: 74px"/>
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
                                <input type="text" id="loginUserNameId" name="j_username" style="width:150px" />
                            </p>
                            <p class="password_entry">
                                <label for="password">
                                    Password:</label>
                                <input type="password"  id="loginPasswordId" name="j_password" style="width:150px"/>
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
This system has been penetration certified by Digital Assurance. View certificate <a  href="javascript:openFile('<%= request.getContextPath()%>/download/CHOXCertificate.pdf');">here</a>.</div>
   </body>
</html>
