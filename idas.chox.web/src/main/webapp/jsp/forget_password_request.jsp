<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<head>
    <title>CHOX Welcome Page</title>

    <script type="text/javascript">

         var elementToBlock;
            
        Ext.onReady(function() {

            var form = $("#forgetPasswordRequestForm");
            form.validate(
            {
                errorLabelContainer: "#ACKmessageBox",
                rules: {
                    userName:{required:true} ,
                    email:{required:true, email: true}
                },
                messages: {
                    userName:{required:"You must supply a value for 'User Name'"},
                    email:{required:"You must choose 'Email'", email: "Incorrect email format"}
                }
            });
            ui.ajaxForm(form);

        });

        var newwindow;
        function openFile(url,name)
        {
            newwindow=window.open(url,name);
            if (window.focus) {newwindow.focus();}
        }

        function doCancel(){
            document.location="<%= request.getContextPath()%>/login.action";
        }
            
    </script>

</head>

<div class="forget-password-container">

    <div class="status-info">
        Please review the 'History' tab for details on why the claim has been rejected.
        Please decide on whether to progress the claim for payment or reject the claim.
        Please enter any relevant details/comments on the 'Notes' tab regarding the decision made.
    </div>

    <div class="inner" style="padding-top:10px;">
        <div id="login">

            <form autocomplete="off" id="forgetPasswordRequestForm" name="forgetPasswordRequestForm" class="XXentity-form" action="<%= request.getContextPath()%>/requestToResetPassword.action" method="POST" >
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
                            <input type="submit"id="forgetPasswordSubmitButtonId"value="Submit"/><input type="button" id="forgetPasswordCancelButtonId" value="Cancel" onclick="javascript:doCancel();" />
                        </p>
                    </div>
                </div>
                <div id="ACKmessageBox" style="display:block;"></div>

            </form>

        </div>
    </div>

</div>

