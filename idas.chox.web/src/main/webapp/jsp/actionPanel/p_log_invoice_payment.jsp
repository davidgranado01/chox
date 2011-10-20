<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script src="<%= request.getContextPath()%>/scripts/StatusBar.js" type="text/javascript"></script>
<script src="<%= request.getContextPath()%>/scripts/ValidationStatus.js" type="text/javascript"></script>
<script src="<%= request.getContextPath()%>/scripts/paymentDetails.js" type="text/javascript"></script>
<script type="text/javascript">
    var paymentDetailsConfirmationEnabled = <s:property value="paymentDetailsConfirmationEnabled"/>;
    var panaltyChargeApplied = <s:property value="PenaltyChargeApplied"/>;
    var hireGrossPaid = <s:property value="hireGrossPaid"/>;
    var repairGrossPaid = <s:property value="repairGrossPaid"/>;
    var engineerFeeGrossPaid = <s:property value="engineerFeeGrossPaid"/>;
    var totalLossFeeGrossPaid = <s:property value="totalLossFeeGrossPaid"/>;
    var storageRecoveryGrossPaid = <s:property value="storageRecoveryGrossPaid"/>;
    var hirePenaltyChargePaid = <s:property value="hirePenaltyChargePaid"/>;
    var repairPenaltyChargePaid = <s:property value="repairPenaltyChargePaid"/>;
    var totalPaid = <s:property value="totalPaid"/>;
    var paymentDetailsCHODiscount = <s:property value="paymentDetailsCHODiscount"/>;
    var paymentDetailsInsurerDiscount = <s:property value="paymentDetailsInsurerDiscount"/>;
    var paymentDetailsClaimHandInvAmt = <s:property value="paymentDetailsClaimHandInvAmt"/>;
    var paymentDetailsDeductionClaimHandFee = <s:property value="paymentDetailsDeductionClaimHandFee"/>;
    var interimPaymentAmount = <s:property value="interimPaymentAmount"/>;
    var InterimPaymentAmountReceived = <s:property value="InterimPaymentAmountReceived"/>;
    var nonce = '<%= session.getAttribute("SessionNonce")%>';
</script>
<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form id="logInvoicePayment" action="post" >
        <fieldset class="x-fieldset"><legend>Invoice ready for payment - Action Required</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="name" name="name" value="invoicePaymentLogged"/>
            <div>
                <div class="status-info">
                    Please update the claim by recording that a payment has been logged against this claim.
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
                            <td><input type="button" id="LIPInvoicePaymentLoggedButtonId"value="Invoice Payment Logged" onclick="confirmPaymentlogAction();"/></td>
                        </tr>
                    </table>
                </div>
            </div>
        </fieldset>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
    </form>
</div>