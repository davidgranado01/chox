<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">


   Ext.onReady(function(){
<s:if test="insurerLouDates">
        openTab(7);
</s:if>
<s:else>
        openTab(6);
</s:else>
    });
        
    function doFormSubmit(action){
        var message = 'Are you sure about this?';
        if (action==='contestRejectedClaim'){
<s:if test="isFixedFeeClaim">
            message="Are you sure you want to 'Send Claim Back To Insurer'?";
</s:if>
<s:elseif test="isInsurerManual">
            message="Are you sure this claim has been Resubmitted?";
</s:elseif>
<s:else>
            message="Are you sure you want to 'Contest This Claim'?";
</s:else>
        } else if (action==='sendClaimGTA'){
            message="Are you sure you want to 'Send Claim Down GTA Route'?";
        } else if (action==='acceptRejectedClaim'){
<s:if test="isInsurerManual">
            message="Are you sure the Rejection Decision was Accepted?";
</s:if>
<s:else>
            message="Are you sure you want to 'Accept Rejection Decision'?";
</s:else>
        }

        Ext.MessageBox.confirm('Confirm', message,
                        function(btn) {
                            if (btn==='yes') {
                                actionPanel.registerAction(action);
                                Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
                                choxJqueryHttpSubmit($("form#contestOrAcceptRejectedClaim"));
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
        <s:hidden id="name" name="name" />
        <fieldset class="x-fieldset">
            <legend>Rejected Claim - Action Required</legend>
            <div>
                <div class="status-info">
                    <s:if test="isFixedFeeClaim">
                        <s:if test="claimRejectedMaxAllowed">
                            Please review the Insurer's notes for the reason for the rejection. This is the <s:property value="rejectionCount" /> time
                            the claim has been rejected, as per the agreement this claim can no longer continue down the
                            Fixed Fee process route. If the rejection is valid and is agreed then click on the 'Accept
                            Rejection Decision' button, this will move the status of the claim to 'ClaimRejectionAccepted'
                            and the claim will be closed. If the rejection is not valid, the ‘Send Claim Down GTA Route’ 
                            button should be clicked, this will submit the claim to the Insurer as a new GTA claim within CHOX
                        </s:if>
                        <s:else>
                            Please review the Insurer’s notes for the reason for the rejection. 
                            If more details are required then add the details to the claim and 
                            click on the ‘Send Claim Back To Insurer’ button. If the rejection 
                            is valid and is agreed then click on the ‘Accept Rejection Decision’ 
                            button, this will move the status of the claim to ‘ClaimRejectionAccepted’ 
                            and the claim will be closed. If the rejection is not valid, the 
                            ‘Send Claim Down GTA Route’ button should be clicked, this will submit the 
                            claim to the Insurer as a new GTA claim within CHOX.
                        </s:else>
                    </s:if>
                    <s:elseif test="IsInsurerManual">
                            This claim has been marked as rejected to the CHO. If the CHO has responded with further information
                            and you can now acknowledge the claim select 'Claim Re-Submitted' below. If the CHO has now withdrawn
                            this claim select 'Close Claim'.
                    </s:elseif>
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
                                    <s:if test="!claimRejectedMaxAllowed">
                                        <input type="button" id="COARCContestFixedFeeClaimButtonId" value="Send Claim Back To Insurer" onclick="event.preventDefault(); doFormSubmit('contestRejectedClaim')" />
                                    </s:if>
                                    <input type="button" id="COARCSendClaimToGTAButtonId" value="Send Claim Down GTA Route" onclick="event.preventDefault(); doFormSubmit('sendClaimGTA');" />
                                    <input type="button" id="COARCAcceptRejectionDecisionButtonId" value="Accept Rejection Decision" onclick="event.preventDefault(); doFormSubmit('acceptRejectedClaim');"  />
                                </s:if>
                                <s:elseif test="isInsurerManual">
                                    <input type="button" id="COARCContestThisClaimButtonId" value="Claim Re-Submitted" onclick="event.preventDefault(); doFormSubmit('contestRejectedClaim');" />
                                    <input type="button" id="COARCAcceptRejectionDecisionButtonId" value="Rejection Decision Accepted" onclick="event.preventDefault(); doFormSubmit('acceptRejectedClaim');"  />
                                </s:elseif>
                                <s:else>    
                                    <input type="button" id="COARCContestThisClaimButtonId" value="Contest This Claim" onclick="event.preventDefault(); doFormSubmit('contestRejectedClaim');" />
                                    <input type="button" id="COARCAcceptRejectionDecisionButtonId" value="Accept Rejection Decision" onclick="event.preventDefault(); doFormSubmit('acceptRejectedClaim');"  />
                                </s:else>    
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="action-error-msg" id="ActionPanelMessageBox"></div>
            </div>
        </fieldset>
        <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>-->
    </form>
</div>
    