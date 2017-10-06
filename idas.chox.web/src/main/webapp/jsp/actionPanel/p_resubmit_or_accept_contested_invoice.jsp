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
        
        
        $("form#resubmitOrAcceptContestedInvoice").validate(
        {
            errorLabelContainer: "#ActionPanelMessageBox",
            rules: {
                supportingLiabilityNotes :{
                    required:true
                }
            },
            messages: {
                supportingLiabilityNotes :{
                    required:"You Must Enter Details In The 'Supporting Notes (Public)' Field."
                }
            }
        });
        
        
    });

    function resubmitOrAcceptContestedInvoiceSubmit(action){
        if($("form#resubmitOrAcceptContestedInvoice").valid()){
            $("#resubmitOrAcceptContestedInvoiceFormNameId").val(action);
            Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
            choxJqueryHttpSubmit($("form#resubmitOrAcceptContestedInvoice"));
        }
        return false;
    }
    
</script>
<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form id="resubmitOrAcceptContestedInvoice" name="resubmitOrAcceptContestedInvoice"
          action="<%=request.getContextPath()%>/prv/processClaim.action"
          method="post">
        <fieldset class="x-fieldset">
            <legend>Contested Invoice - Action Required</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="resubmitOrAcceptContestedInvoiceFormNameId" name="name"/>
            <div>
                <div class="status-info">
                    Please review the 'Notes' tab for details regarding the rejection reasoning made by the Insurer. 
                    Decide whether to resubmit the invoice, close the invoice or move the claim to a pending status 
                    if the claim is being taken to litigation (once/if litigation has been resolved the claim can be moved back into this status). 
                    Resubmission will require a modification to the invoice details and/or the attachment of a payment pack or other supporting documentation. 
                    Please include supporting notes on the decision made using the 'Supporting Notes' text box below.
                </div>
                <div>
                    <table>
                        <tr>
                            <td>
                                <label>Supporting Notes (Public)</label>
                            </td>
                            <td>
                                <textarea class="chox-canote" cols="80" rows="3" name="supportingLiabilityNotes" id="supportingLiabilityNotesId"><s:property value="supportingLiabilityNotes" /></textarea>
                            </td>
                        </tr>
                        <tr>
                            <td>
                               &nbsp; 
                            </td>
                        </tr>
                        <tr>
                            <td colspan="4">
                                <div class="no-format">
                                    <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="4" class="choice" nowrap>
                                <input type="submit" id="ROACIRejectDecisionandResubmitButtonId"value="Resubmit Invoice"  onclick="return resubmitOrAcceptContestedInvoiceSubmit('contestRejectedInvoice');" />
                                <input type="submit" id="ROACIAcceptRejectionDecisionButtonId"value="Close Invoice" onclick="return resubmitOrAcceptContestedInvoiceSubmit('acceptRejectedInvoice');"  />
                                <input type="submit" id="ROACIAwaitingLitigationOutcomeButtonId"value="Move Claim To Litigation Status"  onclick="return resubmitOrAcceptContestedInvoiceSubmit('awaitingLitigationOutcome');" />
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="action-error-msg" id="ActionPanelMessageBox"></div>
            </div>
        </fieldset>
    </form>
</div>