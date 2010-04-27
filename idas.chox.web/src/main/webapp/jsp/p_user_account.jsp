<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    $(function(){

        $.validator.addMethod(
        "regex",
        function(value, element, regexp) {
            var check = false;
            var re = new RegExp(regexp);
            return this.optional(element) || re.test(value);
        }, "Please check your input.");

        var form = $("form#formChangePassword");
        form.validate(
        {
            errorLabelContainer: "#userChangePasswordMessageBox",
            rules: {
                newPassword: {required:true, regex: "^.*(?=.{6,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*$"},
                confirmNewPassword: {equalTo: "#newPassword"}
            }
            ,
            messages: {
                newPassword: {required:"You must supply a value for 'New Password'", regex: "Incorrect Password Format"},
                confirmNewPassword: {equalTo:"Your passwords do not match"}
            }
        });

        ui.ajaxForm(form);
        
    });
/*
    function onSubmitResponseReceived(responseText, statusText)  {
        responseText = responseText.trim();
            
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

*/
</script>


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
        <form autocomplete="off" onsubmit="return true;" id="formChangePassword" action="<%= request.getContextPath()%>/prv/p/changePassword.action" class="XXentity-form" method="post">
            <s:token />
            <div class="status-info">
                N.B. Passwords are case sensitive, must be at least 6 characters, must contain at least one lower case letter, one upper case letter, and one number.<br/>
Please note that password changes are not forced on a periodic basis, it is the user’s responsibility to ensure passwords remain up to date and secure.
            </div>

            <div class="form-container" style="padding-top:10px;">
                <table>
                    <tr>
                        <td align="right"><label class="chox-form-std-label" align="left" >
                           Current password <span class="mandatory">*</span></label></td>
                        <td><input type="password" class="chox-txt" name="oldPassword" id="oldPassword" size="20" maxlength="20" /></td>
                    </tr>
                    <tr>
                        <td align="right"><label class="chox-form-std-label" align="left" >
                            Choose a new password <span class="mandatory">*</span></label></td>
                        <td><input type="password" class="chox-txt" name="newPassword" id="newPassword" size="20" maxlength="20" /></td>
                    </tr>
                    <tr>
                        <td align="right"><label class="chox-form-std-label" align="left">
                            Re-enter new password<span class="mandatory">*</span></label></td>
                        <td><input type="password" class="chox-txt" name="confirmNewPassword" id="confirmNewPassword" size="20" maxlength="20" /></td>
                    </tr>
                    <tr>
                        <td colspan="2" align="center"><input type="submit" value="Save"/></td>
                    </tr>
                </table>
                <div id="submitResult" class="chox-form-submit-result"></div>
                <div class="action-error-msg" id="userChangePasswordMessageBox"></div>
            </div>
        </form>
    </fieldset>
</div>