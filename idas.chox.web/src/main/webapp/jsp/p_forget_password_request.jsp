<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

    <head>
        <title>IDAS CHOX Welcome Page</title>
        <link href="<%= request.getContextPath()%>/css/login.css" rel="stylesheet" type="text/css" media="all"/>
        <script src="<%= request.getContextPath()%>/scripts/jquery/jquery-1.2.6.js" type="text/javascript" ></script>
        <script src="<%= request.getContextPath()%>/scripts/jquery/jquery.form.js" type="text/javascript" ></script>
        <script src="<%= request.getContextPath()%>/scripts/jquery/jquery.validate.min.js" type="text/javascript"></script>

        <script type="text/javascript">

            
            $(document).ready(function(){

                $("#forgetPasswordRequestForm").validate(
                {
                    rules: {
                        userName:{required:true} ,
                        email:{required:true, email: true}
                    },
                    messages: {
                        userName:{required:"You must supply a value for 'User Name'"},
                        email:{required:"You must choose 'Email'", email: "Incorrect email format"}
                    }
                });
            });

            var newwindow;
            function openFile(url,name)
            {
                newwindow=window.open(url,name);
                if (window.focus) {newwindow.focus()}
            }

            function doCancel(){
                document.location="<%= request.getContextPath()%>/login.action";
            }
            
        </script>

    </head>

    <body class="modal">

        <div class="outer">
            <img alt="Logo" src="<%= request.getContextPath()%>/images/logo_login.jpg" alt=""/>
        </div>

        <div class="forget-password-container">

            <div class="status-info">
                Please review the 'History' tab for details on why the claim has been rejected.
                Please decide on whether to progress the claim for payment or reject the claim.
                Please enter any relevant details/comments on the 'Notes' tab regarding the decision made.
            </div>

            <div class="inner" style="padding-top:10px;">
                <div id="login">

                    <form onsubmit="return true;" id="forgetPasswordRequestForm" name="forgetPasswordRequestForm" class="XXentity-form" action="<%= request.getContextPath()%>/requestToResetPassword.action" method="POST" >
                        <div class="login_form">
                            <div>
                                <label class="forget-password-label">Username:</label>
                                <input type="text" name="userName" id="userName" style="width:250px" class="forget-password-input"/>
                            </div>
                            <div>
                                <label class="forget-password-label">Email:</label>
                                <input type="text" name="email" id="email" style="width:250px"/>
                            </div>

                            <div class="checkbox_and_submit">
                            <p>
                                <input type="submit" value="Submit"/><input type="button" value="Cancel" onclick="javascript:doCancel();" />
                            </p>
                            </div>
                        </div>
                        <div id="ACKmessageBox" style="display:block;"></div>
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
