<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    $(function(){
        var form = $("form#formChangeContact");
        ui.ajaxForm(form, doChangeTelephoneSucceed, 'html');
        $('#contactTelephoneId').html('<s:property value="authenticatedUser.telephone" />');
        
        $('#responseTelMessageBox').fadeOut(10000);
   });

   function doChangeTelephoneSucceed(responseText, statusText){
        var response = eval('(' + responseText.trim() + ')');
            if(response && response.isValid)
            {

                if(response.resultType && response.resultType == 'Message'){
                    var target = "#updateTelephoneNumberId";
                    var url = "<%= request.getContextPath()%>/prv/p/getUserChangeContact.action";
                    var param = {"actionResult":response.result};
                    ajax.loadHtml2(url,param,function(data){
                        $(target).html(data);
                    });
                }
                <s:if test="redirect" > // We were redirected here
                    Ext.MessageBox.alert('Status', 'Your contact details have been added.', confirmOk);
                </s:if>
//                if (!$('#telephoneId').length) {
//                    window.location = "<%= request.getContextPath()%>/prv/inbox.action?showHistory=1";
//                }
            }
            else {
//            Ext.MessageBox.alert('Error', 'Error updating password: '+ response.errors + '\n Please try again.', confirmError);
                    var target = "#updateTelephoneNumberId";
                    var url = "<%= request.getContextPath()%>/prv/p/getUserChangeContact.action";
                    var param = {"actionError":response.errors[0]};
                    ajax.loadHtml2(url,param,function(data){
                        $(target).html(data);
                    });
        }
    }
    function confirmOk(btn){
                 window.location = "<%= request.getContextPath()%>/prv/inbox.action?showHistory=1&nonce=<%= session.getAttribute("SessionNonce")%>";
    }

</script>
        <form autocomplete="off" id="formChangeContact" action="<%= request.getContextPath()%>/prv/p/changeTelephone.action" class="XXentity-form" method="post">
           <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
            <div class="status-info">
                N.B. When a user is assigned a claim, the contact telephone number specified here will be attached to the claim via a Note.
            </div>

            <div class="form-container" style="padding-top:10px;">
                <table>
                    <tr>
                        <td align="right"><label class="chox-form-std-label">Contact telephone number</label></td>
                        <td><input type="text" class="chox-txt" name="telephone" id="telephoneId" size="16" maxlength="16"/></td>
                    </tr>
                    <tr>
                        <td colspan="2" align="center"><input type="submit" id="userChangeContactSubmitButtonId" value="Save"/></td>
                    </tr>
                </table>
            <div id="EXTmessageBox" class="action-error-msg"><s:property value="actionError" /></div>
            <div class="chox-form-submit-result" id="responseTelMessageBox"><s:property value="actionResult" /></div>
                <div id="submitResult" class="chox-form-submit-result"></div>
                <div class="action-error-msg" id="userChangeTelephoneMessageBox"></div>
            </div>
        </form>
