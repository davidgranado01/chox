<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ include file="paymentDetails.jspf" %>

<script type="text/javascript">
var penaltyChargeApplied = new Boolean(<s:property value="penaltyChargeApplied"/>);
var hireGrossPaid = <s:property value="hireGrossPaid"/>;
var repairGrossPaid = <s:property value="repairGrossPaid"/>;
var engineerFeeGrossPaid = <s:property value="engineerFeeGrossPaid"/>;
var totalLossFeeGrossPaid = <s:property value="totalLossFeeGrossPaid"/>;
var storageRecoveryGrossPaid = <s:property value="storageRecoveryGrossPaid"/>;
var hirePenaltyChargePaid = <s:property value="hirePenaltyChargePaid"/>;
var repairPenaltyChargePaid = <s:property value="repairPenaltyChargePaid"/>;
var outstandingPayment = <s:property value="projectedFinalPayment"/>;
var totalToPay = <s:property value="totalToPay"/>;
var paymentDetailsCHODiscount = <s:property value="paymentDetailsCHODiscount"/>;
var paymentDetailsInsurerDiscount = <s:property value="paymentDetailsInsurerDiscount"/>;
var paymentDetailsClaimHandInvAmt = <s:property value="paymentDetailsClaimHandInvAmt"/>;
var paymentDetailsDeductionClaimHandFee = <s:property value="paymentDetailsDeductionClaimHandFee"/>;
var interimPaymentAmount = <s:property value="interimPaymentMade"/>;

    function doUpdateManualInvoice(action){
    
        $('form#logInvoicePayment input[id="name"]').val(action)

        Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
//        $("form#logInvoicePayment").submit();
        choxJqueryHttpSubmit($("form#logInvoicePayment"));
    
    }

    function confirmPaymentLog(action){
    
        $('form#logInvoicePayment input[id="name"]').val(action)
        return confirmPaymentLogAction();
    
    }

    function switchToClaimsHandler(){
    
        $('form#logInvoicePayment input[id="name"]').val('switchFromPaymentsTeam');
        Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
        choxJqueryHttpSubmit($("form#logInvoicePayment"));
    
    }

    function callInterimPayment(){
    	 var target = "#moreActionPanel";
         var url = "/prv/p/makeInterimPayment.action";
         var param = {"id":<s:property value="id" />};
         ajax.loadHtml2(url,param,function(data){
             $(target).html(data);
         }); 
    }
</script>
<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form id="logInvoicePayment" action="<%=request.getContextPath()%>/prv/processClaim.action" method="POST">
        <fieldset class="x-fieldset"><legend>Invoice Ready For Payment - Action Required</legend>
            <s:hidden id="claimId" name="id" />
            <s:if test="isInsurerManual">
                <s:hidden id="name" name="name" />
            </s:if>
            <div>
                <div class="status-info">
                  <s:if test="isInsurerManual">
                    Once the payment has been made please click on the ‘Insurer Invoice Paid’ button.
                  </s:if>
                  <s:else>
                    If the claim is being paid in full then please click on the ‘Invoice Payment Logged’ button, 
                    this button should only be used if this is intended to be a final payment.  However if an interim payment 
                    is being made please click on the ‘Make Interim Payment’ button.’
                    <s:if test="invoiceWithPaymentsTeam">
                        Alternatively, if you are not in a position to make a payment and need to return the claim to
                        the claims handler please click on 'Switch To Claims Handler'.
                    </s:if>
                  </s:else>
                </div>
                <div class="status-info-submit">
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
                                <s:if test="isInsurerManual">
                                    <input type="button" id="UMIPFormId" value="Insurer Invoice Paid" onclick="doUpdateManualInvoice('updateManualInvoicePaid');" />
                                </s:if>
                                <s:else>
                                    <input type="button" id="LIPInvoicePaymentLoggedButtonId"value="Invoice Payment Logged" onclick="confirmPaymentLogAction();"/>
                                    <input type="button" id="interimPaydButtonId"value="Make Interim Payment" onclick="callInterimPayment();"/>
                                </s:else>
                                <s:if test="invoiceWithPaymentsTeam">
                                    <input type="button" id="switchClaimsHandlerButtonId"value="Switch To Claims Handler" onclick="switchToClaimsHandler();"/>
                                </s:if>
                              </td>
                        </tr>
                    </table>
                </div>
            </div>
        </fieldset>
    </form>
</div>