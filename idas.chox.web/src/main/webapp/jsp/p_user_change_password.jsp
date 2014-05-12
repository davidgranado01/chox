<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var csrfParameterName = '${_csrf.parameterName}';
    var csrfTokenValue = '${_csrf.token}';
    var csrfParam = {
        '${_csrf.parameterName}' : '${_csrf.token}'
    };
    
    Ext.onReady(function() {

        var passForm = $("form#formChangePassword");
        
        $.validator.addMethod(
        "regex",
        function(value, element, regexp) {
            var check = false;
            var re = new RegExp(regexp);
            return this.optional(element) || re.test(value);
        }, "Please check your input.");

        var passwordRegex = "^.*(?=.{" + '<s:property value="minPasswordLength" />' + ",})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*$";
        passForm.validate(
        {
            errorLabelContainer: "#EXTmessageBox",
            rules: {
                oldPassword: {required:true},
                newPassword: {required:true, regex: passwordRegex},
                confirmNewPassword: {equalTo: "#newPassword"}
            }
            ,
            messages: {
                oldPassword: {required:"You must supply a value for 'Current Password'"},
                newPassword: {required:"You must supply a value for 'New Password'", regex: "Incorrect Password Format"},
                confirmNewPassword: {equalTo:"Your passwords do not match"}
            }
        });

        $('#responseMessageBox').fadeOut(10000);

    });
    
    function changePassword(){
        $('#responseMessageBox').html('');
        $('#responseErrorBox').html('');
        var form = $("form#formChangePassword");
        if(form.valid()){
            var queryString = {};
            $.each(form.serializeArray(), function() {queryString[this.name] = this.value;});
            choxExtAjaxRequest({
                url: '/prv/p/changePassword.action',
                params : queryString,
                callback : doChangePasswordSucceed
            });
    
        }
    }

    function doChangePasswordSucceed(options,success,resp){
        var response = Ext.util.JSON.decode(resp.responseText);
        if(response && response.isValid)
        {
            if(response.resultType && response.resultType == 'Message'){
                var target = "#updatePasswordId";
                var url = "/prv/p/getUserChangePassword.action";
                var param = {"actionResult":response.result};
                ajax.loadHtml2(url,param,function(data){
                    $(target).html(data);
                });
            }
            if(!$('#telephoneId').length){ // No contact telephone change, so me must be here due to password expired
                Ext.MessageBox.alert('Status', 'Your Password has been changed.', confirmOk);
            }
        }
        else {
            if(!$('#telephoneId').length){ // No contact telephone change, so me must be here due to password expired
                Ext.MessageBox.alert('Error', 'Error updating password: '+ response.errors + '\n Please try again.', confirmError);
            } else {
                var target = "#updatePasswordId";
                var url = "/prv/p/getUserChangePassword.action";
                var param = {"actionError":response.errors[0]};
                ajax.loadHtml2(url,param,function(data){
                    $(target).html(data);
                });
            }
        }
    }

    function confirmOk(btn){
        Ext.get('userDetailsScreenId').mask("Loading inbox...");
        loadInbox(true);
    }
    function confirmError(btn){
        var isExpired = <s:property value="AuthenticatedUser.isExpired"/>;
        if (isExpired)
            window.location = "<%= request.getContextPath()%>/prv/openUserAccountRedirect.action";
        else
            window.location = "<%= request.getContextPath()%>/prv/openUserAccountSettings.action";
    };

</script>
<form autocomplete="off" id="formChangePassword" action="<%= request.getContextPath()%>/prv/p/changePassword.action" class="XXentity-form" method="post">
    <div class="status-info">
        N.B. Passwords are case sensitive, must be at least <s:property value="minPasswordLength" /> characters, must contain at least one lower case letter, one upper case letter, and one number.
    </div>

    <div class="form-container" style="padding-top:10px;">
        <table>
            <tr>
                <td align="right"><label class="chox-form-std-label">
                        Current Password <span class="mandatory">*</span></label></td>
                <td><input type="password" class="chox-txt" name="oldPassword" id="oldPassword" size="20" maxlength="20" /></td>
            </tr>
            <tr>
                <td align="right"><label class="chox-form-std-label">
                        Choose a New Password <span class="mandatory">*</span></label></td>
                <td><input type="password" class="chox-txt" name="newPassword" id="newPassword" size="20" maxlength="20" /></td>
            </tr>
            <tr>
                <td align="right"><label class="chox-form-std-label">
                        Re-enter New Password<span class="mandatory">*</span></label></td>
                <td><input type="password" class="chox-txt" name="confirmNewPassword" id="confirmNewPassword" size="20" maxlength="20" /></td>
            </tr>
            <tr>
                <td colspan="2" align="center"><input type="button" id="userChangePasswordSubmitButtonId" value="Save" onclick="javascript:changePassword();"/></td>
            </tr>
        </table>
        <div id="EXTmessageBox" class="action-error-msg"></div>
        <div class="action-error-msg" id="responseErrorBox"><s:property value="actionError" /></div>
        <div class="chox-form-submit-result" id="responseMessageBox"><s:property value="actionResult" /></div>
    </div>
    <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>-->
</form>
