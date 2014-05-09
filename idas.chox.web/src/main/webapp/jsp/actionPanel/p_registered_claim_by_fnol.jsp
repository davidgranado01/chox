<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    Ext.onReady(function() {
        var form = $("form#formRegisterFNOL");
        choxJqueryHttpSubmit(form, doFnolFormSubmit);
    });
    
    function doFnolFormSubmit(){

        $("form#formRegisterFNOL #registerByFnolMessageBox").html("");

        var claimNumber = $("form#formRegisterFNOL #claimNumber").val();
        var reasonForRejection = $("form#formRegisterFNOL #reasonForRejection").val();

        if((claimNumber.length+reasonForRejection.length)<1){
            $("form#formRegisterFNOL #registerByFnolMessageBox").html("Both of the above fields cannot be blank. If there is no claim number for this claim, please enter the reason why in the 'FNOL Review Note' field");
            return false;
        }
        Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
//        return true;
    }


</script>
<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form id="formRegisterFNOL" name="formRegisterFNOL" action="<%=request.getContextPath()%>/prv/processClaim.action" method="POST" class="XXentity-form" >
        <div class="form-container">
            <fieldset class="x-fieldset">
                <legend>First Notification of Loss - Action Required</legend>
                <div>

                    <s:hidden id="id" name="id" />
                    <s:hidden id="name" name="name" value="registerFNOL" />

                    <div>
                        <div class="status-info">
                            Please review the claim details using the 'Claim Details' tab, if and when the claim has been registered please enter the assigned Claim Number and click on the 'Return Claim' button to return the claim. If the claim has not been registered, please enter the reason why in the 'FNOL Review Note' field/box before clicking on the 'Return Claim' button.
                        </div>
                        <div class="status-control-set">
                            <table class="status-table">
                                <tr>
                                    <td><label>Claim Number</label></td>
                                    <td><input type="text" class="chox-ttxt" name="claimNumber" id="claimNumber" value="<s:property value="claimNumber" />" maxlength=100/></td>
                                </tr>
                                <tr valign="top">
                                    <td><label>FNOL Review Note (If applicable)</label></td>
                                    <td><textarea class="chox-canote" cols="80" rows="5" name="reasonForRejection" id="reasonForRejection"><s:property value="reasonForRejection" /></textarea></td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div class="no-format">
                                            <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" class="choice" nowrap>
                                        <input type="submit" id="RCBFReturnClaimButtonId"value="Return Claim" />
                                    </td>
                                </tr>
                            </table>
                            <div class="action-error-msg" id="registerByFnolMessageBox"></div>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>
        <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>-->
    </form>
</div>