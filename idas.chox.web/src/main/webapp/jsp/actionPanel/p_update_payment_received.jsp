<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    function doUpdatePaymentReceived(action) {
        $("#formUpdatePaymentReceivedName").val(action);
    }
</script> 

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="formUpdatePaymentReceived" name="formUpdatePaymentReceived">
        <fieldset class="x-fieldset">
            <legend>Update Payment Logged</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="formUpdatePaymentReceivedName" name="name"/>
             <s:hidden id="pLogged" name="paymentLogged" />
            <div class="status-control-set">
                <s:if test="paymentLoggedOverDays && showPayNotReceivedButton">
                    <div class="status-info">
                        Please click on the 'Payment Received' button when the payment has been received from the Insurer.
                        If the payment has not been received then click on the 'Payment Not Received' button which will push the claim
                        back to the Insurer for review.
                    </div>
                </s:if>
                <s:else>
                    <div class="status-info">
                        Please click on the 'Payment Received' button below when the payment has been received from the Insurer.
                    </div>
                </s:else>

                <table class="status-table">
                    <tr>
                        <td colspan="3">
                            <div class="no-format"><span>Please specify how you wish to proceed &nbsp;&nbsp;</span></div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3">
                            <input type="submit" id="UPRPaymentReceivedButtonId" value="Payment Received" onclick="doUpdatePaymentReceived('invoicePaymentReceived');" />
                            <s:if test="paymentLoggedOverDays && showPayNotReceivedButton">
                                <input type="submit" id="UPRPaymentNOTReceivedButtonId" value="Payment Not Received" onclick="doUpdatePaymentReceived('revertClaim');" />
                            </s:if>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="action-error-msg" id="updatePaymentReceivedMessageBox"></div>
        </fieldset>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
    </form>
</div>