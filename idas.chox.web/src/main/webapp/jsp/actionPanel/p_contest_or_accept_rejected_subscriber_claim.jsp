<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">

   // $(document).ready(function() {
   Ext.onReady(function(){
        openTab(6);
        });
        
    function doFormSubmit(action){
        var message = 'Are you sure about this?';
        if (action == 'contestRejectedSubscriberClaim') {
            message = "Are you sure you want to ‘Send Claim Back To Insurer'?";
        } else if (action == 'acceptSubscriberChallenge') {
            message = "Are you sure you ‘Agree With The Subscriber Challenge’ and want to move the claim to ‘AwaitingInvoiceData’ ready for invoicing?";
        } else if (action == 'sendClaimGTA') {
            message = "Are you sure you want to ‘Send Claim Down GTA Route'?";
        }
        Ext.MessageBox.confirm('Confirm', message,
                        function(btn) {
                            if (btn=='yes') {
                                actionPanel.registerAction(action);
                                $("form#contestOrAcceptRejectedSubscriberClaim").submit();
                            } else {
                                return false;
                            }
                        }
        );
                        
        return false;
    }

</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/processClaim.action"
          method="post"
          id="contestOrAcceptRejectedSubscriberClaim" name="contestOrAcceptRejectedSubscriberClaim">
        <s:hidden id="claimId" name="id" />
        <s:hidden id="name" name="name" />
        <fieldset class="x-fieldset">
            <legend>Rejected Subscriber Claim - Action Required</legend>
            <div>
                <div class="status-info">
                    Please review the Insurer's notes for the reason for the rejection.
                    If more/clarity details are required then add the details to the claim and click on the 'Send Claim Back To Insurer' button.
                    If the rejection is valid and is agreed then click on the 'Agree With Subscriber Challenge' button,
                    this will move the status of the claim to 'AwaitingInvoiceData' so the invoice can be loaded.
                    If the rejection is not valid, clicking on the 'Send Claim Down GTA Route' button should be clicked,
                    this will submit the claim to the Insurer as a new GTA claim within CHOX.
                </div>
                <div class="status-control-set">
                    <table>
                        <tr>
                            <td>
                                <div class="no-format">
                                    <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="submit" id="COARCContestThisSubscriberClaimButtonId" value="Send Claim Back To Insurer"  onclick="return doFormSubmit('contestRejectedSubscriberClaim');" />
                                <input type="submit" id="COARCAcceptRejectionDecisionButtonId" value="Agree With Subscriber Challenge" onclick="return doFormSubmit('acceptSubscriberChallenge')"  />
                                <input type="submit" id="COARCAcceptRejectionDecisionButtonId" value="Send Claim Down GTA Route" onclick="return doFormSubmit('sendClaimGTA')"  />
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="action-error-msg" id="ActionPanelMessageBox"></div>
            </div>
        </fieldset>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
    </form>
</div>