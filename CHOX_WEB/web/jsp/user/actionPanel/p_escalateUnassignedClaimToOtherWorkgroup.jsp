<%@ taglib uri="/struts-tags" prefix="s" %>

<form onsubmit="return true;" action="user/UpdateClaimWorkgroupAssignment.action" method="post" id="formEscalateClaimAction" name="formEscalateClaimAction">
    <fieldset class="x-fieldset">
        <legend>Remove Workgroup - Action Required</legend>
        <div>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="actionName" name="actionName" value="assigned" />
            <div>
                <div class="status-info">
                If you have been incorrectly assigned this claim in your workgroup, please click on the 'Remove Workgroup Mapping' button to send it back to a Claims Router, who will then select the correct workgroup for this claim.
                </div>
                <div class="status-control-set">
                    <table class="status-table" width="100%">
                    <tr>
                        <td colspan="2" class="choice" nowrap>
                            <input id="assignOnly" type="submit" value="Remove Workgroup Mapping"/>
                        </td>
                    </tr>

                    </table>
                    <div class="errorBox" id="EscalateClaimMessageBox"></div>
                </div>
            </div>

        </div>
    </fieldset>
</form>