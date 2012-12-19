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
var nonce = '<%= session.getAttribute("SessionNonce")%>';
    
    function callInterimPayment(){
    	 var target = "#moreActionPanel";
         var url = "<%= request.getContextPath()%>/prv/p/makeInterimPayment.action";
         var param = {"id":<s:property value="id" />};
         ajax.loadHtml2(url,param,function(data){
             $(target).html(data);
         }); 
    }
</script>
<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form id="logInvoicePayment" action="post" >
        <fieldset class="x-fieldset"><legend>Invoice ready for payment - Action Required</legend>
            <s:hidden id="claimId" name="id" />
            <div>
                <div class="status-info">
                    If the claim is being paid in full then please click on the ‘Invoice Payment Logged’ button, 
                    this button should only be used if this is intended to be a final payment.  However if an interim payment 
                    is being made please click on the ‘Make Interim Payment’ button.’
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
                            <td><input type="button" id="LIPInvoicePaymentLoggedButtonId"value="Invoice Payment Logged" onclick="confirmPaymentLogAction();"/>
                            <input type="button" id="interimPaydButtonId"value="Make Interim Payment" onclick="callInterimPayment();"/></td>
                        </tr>
                    </table>
                </div>
            </div>
        </fieldset>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
    </form>
</div>