<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">

   // $(document).ready(function() {
   Ext.onReady(function(){
        openTab(6);
        });
        
    function doFormSubmit(action){
        var message = 'Are you sure about this?';
        if (action == 'contestRejectedClaim') {
            message = "Are you sure you want to 'Send Claim Back To Insurer'?";
        } else if (action == 'sendClaimGTA') {
            message = "Are you sure you want to 'Send Claim Down GTA Route'?";
        }
        Ext.MessageBox.confirm('Confirm', message,
                        function(btn) {
                            if (btn=='yes') {
                                actionPanel.registerAction(action);
                                $("form#contestOrAcceptRejectedClaim").submit();
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
          id="contestOrAcceptRejectedClaim" name="contestOrAcceptRejectedClaim">
        <s:hidden id="claimId" name="id" />
        <s:hidden id="name" name="name" />
        <fieldset class="x-fieldset">
            <legend>Rejected Claim - Action Required</legend>
            <div>
                <div class="status-info">
                    <s:if test="isFixedFeeClaim">
                        <s:if test="subscriberClaimRejectedMoreThanOnce">
                            Please review the Insurer's notes for the reason for the rejection. This is the second time
                            the claim has been rejected, as per the agreement this claim can no longer continue down the
                            Fixed Fee process route. If the rejection is valid and is agreed then click on the 'Accept
                            Rejection Decision' button, this will move the status of the claim to 'ClaimRejectionAccepted'
                            and the claim will be closed. If the rejection is not valid, clicking on the 'Send Claim Down
                            GTA Route' button should be clicked, this will submit the claim to the Insurer as a new GTA
                            claim within CHOX.
                        </s:if>
                        <s:else>
                            Please review the Insurer's notes for the reason for the rejection. If more/clarity details are required then
                            add the details to the claim and click on the 'Send Claim Back To Insurer' button. 
                            If the rejection is valid and is agreed then click on the 'Accept Rejection Decision' button, this will move
                            the status of the claim to 'ClaimRejectionAccepted' and the claim will be closed.
                            If the rejection is not valid, clicking on the 'Send Claim Down GTA Route' button should be clicked,
                            this will submit the claim to the Insurer as a new GTA claim within CHOX.
                        </s:else>
                    </s:if>
                    <s:else>
                        Please review the Insurer's notes against rejection reasoning and decide whether to accept or reject the Insurer's rejection decision.
                        Please include supporting notes on the decision made using the 'Notes' tab.
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
                                <s:if test="isFixedFeeClaim">
                                    <s:if test="!subscriberClaimRejectedMoreThanOnce">
                                        <input type="submit" id="COARCContestThisSubscriberClaimButtonId" value="Send Claim Back To Insurer"  onclick="return doFormSubmit('contestRejectedClaim');" />
                                    </s:if>
                                    <input type="submit" id="COARCAcceptRejectionDecisionButtonId" value="Send Claim Down GTA Route" onclick="return doFormSubmit('sendClaimGTA');" />
                                </s:if>
                                <s:else>    
                                    <input type="submit" id="COARCContestThisClaimButtonId" value="Contest This Claim"  onclick="actionPanel.registerAction('contestRejectedClaim');" />
                                </s:else>    
                                <input type="submit" id="COARCAcceptRejectionDecisionButtonId" value="Accept Rejection Decision" onclick="actionPanel.registerAction('acceptRejectedClaim')"  />
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