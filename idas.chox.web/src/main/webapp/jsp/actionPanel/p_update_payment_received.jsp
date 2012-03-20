<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    function doUpdatePaymentReceived(action) {
    	var interimPayment = <s:property value="partialInterimPayment" />;
    	$("#formUpdatePaymentReceivedName").val(action);
    	if (action=='fullInvoicePaymentReceived') {
            if (interimPayment != undefined &&  interimPayment > 0){
            	Ext.MessageBox.confirm('Confirm', 'Please note that there is an interim payment on this claim which has not yet been marked as received, marking the claim as ‘Full Payment Received’ will also mark the interim payment as received.' 
            			,function(btn){if(btn=='yes'){$("form#formUpdatePaymentReceived").submit();}else{return false;}});
            }else{
            	$("form#formUpdatePaymentReceived").submit();
            }
        } else if (action == 'invoicePaymentReceived') {
           	Ext.MessageBox.confirm('Confirm', 'Insurer has made a payment of £<s:property value="partialInterimPayment" /> against an amount outstanding of £<s:property value="interimPayment" />. Please confirm the amount you have received:' 
           			,function(btn){if(btn=='yes'){$("form#formUpdatePaymentReceived").submit();}else{return false;}});
        } else {
        	$("form#formUpdatePaymentReceived").submit();
        }
        
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
                       Please click on the 'Full Payment Received' button when full payment for the invoice has been received from the Insurer.<br/><br/>
					   Please click on the ‘Payment Received But Not Full Amount’ button if the Insurer has made a payment but there is a balance outstanding 
					   on the invoice, this will return the claim to the Insurer for review and the amount received recorded on the invoice.<br/><br/>
					   If the payment has not been received then clicking on the 'Payment Not Received' button will return the claim to the Insurer for review.  
					   Note that this button will only be visible after 9 days.
                    </div>
                </s:if>
                <s:else>
                    <div class="status-info">
                       Please click on the 'Full Payment Received' button when full payment for the invoice has been received from the Insurer.<br/><br/>
					   Please click on the ‘Payment Received But Not Full Amount’ button if the Insurer has made a payment but there is a balance outstanding 
					   on the invoice, this will return the claim to the Insurer for review and the amount received recorded on the invoice.
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
                        	<input type="button" id="FullPaymentReceivedButtonId" value="Full Payment Received" onclick="doUpdatePaymentReceived('fullInvoicePaymentReceived');" />
                            <input type="button" id="UPRPaymentReceivedButtonId" value="Payment Received" onclick="doUpdatePaymentReceived('invoicePaymentReceived');" />
                            <s:if test="paymentLoggedOverDays && showPayNotReceivedButton">
                                <input type="button" id="UPRPaymentNOTReceivedButtonId" value="Payment Not Received" onclick="doUpdatePaymentReceived('revertClaim');" />
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