<%@ page import="org.acegisecurity.AuthenticationException" %>
<%@ page import="org.acegisecurity.ui.AbstractProcessingFilter" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    
    <head>
        <title>IDAS CHOX Welcome Page</title>
        <link href="<%= request.getContextPath()%>/styles/login.css" rel="stylesheet" type="text/css" media="all"/> 
        
        <script>
            var newwindow;
            function openFile(url,name)
            {
                    newwindow=window.open(url,name);
                    if (window.focus) {newwindow.focus()}
            }

        </script>
        
    </head>
    <body class="modal login">
    <div class="outer">
        <img alt="Logo" src="<%= request.getContextPath() %>/images/logo_login.jpg">
    </div>
    <div class="modal_container">
        <div class="app_title"><img alt="Logo" src="<%= request.getContextPath() %>/images/chox_logo_small.jpg"></div>
        <div class="inner">
            <div class="content" id="loginPanel">
               <form action="<%=request.getContextPath()%>/j_acegi_security_check" method="POST" >
                    <div class="login_form">
                        <p class="password_entry">
                            <label for="name">
                                Username:</label>
                            <input type="text" name="j_username" />
                        </p>
                        <p class="password_entry">
                            <label for="password">
                                Password:</label>
                           <input type="password"  name="j_password" />
                            <!--<span class="note">(<a href="#">Forgotten Username/Password</a>)</span>-->
                        </p>
                        <!--
                        <p class="open_id_entry" style="display: none;">
                            <label for="name">
                                <img alt="Openid-icon" src="https://asset1.highrisehq.com/images/openid-icon.gif?1227775600"
                                    style="margin-bottom: 4px;" align="absmiddle" height="16" width="16">
                                OpenID:
                            </label>
                            
                            <input class="identity_url" id="openid_url" name="openid_url" type="text">
                            <span class="note">(<a href="#">Can't login?</a>) </span> 
                        </p>
                        -->
                        <div class="checkbox_and_submit">
                            <!--
                            <p>
                                <label>
                                    <input id="save_login" name="save_login" value="1" type="checkbox">
                                    Remember me on this computer</label></p>
                            -->
                            <p>
                                <input type="submit" value="Sign In" />        
                                    <s:if test="#parameters.size()>0">

<span id="login-error">Incorrect Username and/or Password </span>

<!--
<span id="login-error">Error:&nbsp;<s:property value="#session['ACEGI_SECURITY_LAST_EXCEPTION'].message" /></span>
-->

                                    </s:if>
                            </p>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="below">
        <span class="note" style="font-size:11px">
        <a href="javascript:openFile('<%= request.getContextPath()%>/jsp/terms_of_service.jsp','TermOfService');">Terms of Service</a></span>
    </div>
    <div class="footerText">This is a Sherwood Compliance Services Ltd proprietary system. No use is allowed without appropriate authorisation.<br/> Unauthorised use of this system will constitute a breach of Sherwood Compliance Services Ltd policy and prosecution under pertinent legislation will apply.</div>
</body>
</html>
