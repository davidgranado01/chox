<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">

   Ext.onReady(function(){
//    $(function(){
        openTab(6);
        });
        
    function doSubscriberFormSubmit(action){
        var message='Are you sure about this?';
        if (action=='contestRejectedClaim'){
            message="Are you sure you want to 'Send Claim Back To Insurer'?";
        } else if (action=='acceptSubscriberChallenge'){
    <s:if test="subscriberClaimRejected">
            message="Are you sure you 'Agree With The Subscriber Challenge' and want to close the claim?";
    </s:if>
    <s:else>
            message="Are you sure you 'Agree With The Subscriber Challenge' and want to move the claim to 'AwaitingInvoiceData' ready for invoicing?";
    </s:else>
        } else if (action=='sendClaimGTA'){
            message="Are you sure you want to 'Send Claim Down GTA Route'?";
        }

        Ext.MessageBox.confirm('Confirm', message,
                        function(btn) {
                            if (btn=='yes') {
                                actionPanel.registerAction(action);
                                $("form#contestOrAcceptRejectedSubscriberClaim").submit();
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
        <s:hidden id="name" name="name" />
        <fieldset class="x-fieldset">
            <legend>Rejected Subscriber Claim - Action Required</legend>
            <div>
                <div class="status-info">
                  <s:if test="subscriberClaimRejectedMoreThanOnce">
                     <s:if test="subscriberClaimRejected">
                        Please review the Insurer's notes for the reason for the rejection.
                        This is the second time the claim has been rejected, as per the agreement
                        this claim can no longer continue down the Subscriber process route.
                        If the rejection is valid and is agreed then click on the 'Agree
                        With Subscriber Challenge' button, this will move the status of the
                        claim to 'ClaimRejectionAccepted' and closed as an invoice cannot be
                        submitted for this claim due to the Insurer's rejection reason. If
                        the rejection is not valid, clicking on the 'Send Claim Down GTA Route'
                        button should be clicked, this will submit the claim to the Insurer
                        as a new GTA claim within CHOX.
                    </s:if>
                    <s:else>
                        Please review the Insurer's notes for the reason for the rejection.
                        This is the second time the claim has been rejected, as per the agreement
                        this claim can no longer continue down the Subscriber process route.
                        If the rejection is valid and is agreed then click on the 'Agree
                        With Subscriber Challenge' button, this will move the status of the
                        claim to 'AwaitingInvoiceData' so the invoice can be loaded. If the
                        rejection is not valid, clicking on the 'Send Claim Down GTA Route'
                        button should be clicked, this will submit the claim to the Insurer
                        as a new GTA claim within CHOX.
                    </s:else>
                  </s:if>
                  <s:else>
                    <s:if test="subscriberClaimRejected">
                        Please review the Insurer's notes for the reason for the rejection.
                        If more/clarity details are required then add the details to the claim and click on the 'Send Claim Back To Insurer' button.
                        If the rejection is valid and is agreed then click on the 'Agree With Subscriber Challenge' button,
                        this will move the status of the claim to 'ClaimRejectionAccepted' and closed as an invoice cannot be
                        submitted for this claim due to the Insurer's rejection reason.
                        If the rejection is not valid, clicking on the 'Send Claim Down GTA Route' button should be clicked,
                        this will submit the claim to the Insurer as a new GTA claim within CHOX.
                    </s:if>
                    <s:else>
                        Please review the Insurer’s notes for the reason for the rejection.
                        If more details are required then add the details to the claim and click on the ‘Send Claim Back To Insurer’ button.
                        If the rejection is valid and is agreed then click on the ‘Agree With Subscriber Challenge’ button,
                        this will move the status of the claim to ‘AwaitingInvoiceData’ so the invoice can be loaded.
                        If the rejection is not valid, the ‘Send Claim Down GTA Route’ button should be clicked,
                        this will submit the claim to the Insurer as a new GTA claim within CHOX.
                    </s:else>
                  </s:else>
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
                              <s:if test="!subscriberClaimRejectedMoreThanOnce">
                                <input type="button" id="COARSCContestThisClaimButtonId" value="Send Claim Back To Insurer"  onclick="return doSubscriberFormSubmit('contestRejectedClaim')" />
                              </s:if>
                                <input type="button" id="COARSCAgreeDecisionButtonId" value="Agree With Subscriber Challenge" onclick="return doSubscriberFormSubmit('acceptSubscriberChallenge')"  />
                                <input type="button" id="COARSCSendToGTADecisionButtonId" value="Send Claim Down GTA Route" onclick="return doSubscriberFormSubmit('sendClaimGTA')"  />
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
