<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form onsubmit="return true;" action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="formUpdatePaymentReceived" name="formUpdatePaymentReceived">
        <fieldset class="x-fieldset">
            <legend>Update Payment Logged</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="name" name="name" value="invoicePaymentReceived"/>
            <div class="status-control-set">
                <div class="status-info">
                    Please click on the 'Payment Received' button below when the payment has been received from the Insurer.
                </div>

                <table class="status-table">
                    <tr>
                        <td colspan="3">
                            <div class="no-format"><span>Please specify how you wish to proceed &nbsp;&nbsp;</span></div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="submit" value="Payment Received"/>
                        </td>
                        <td></td><td></td>
                    </tr>
                </table>
            </div>
            <div class="action-error-msg" id="updatePaymentReceivedMessageBox"></div>
        </fieldset>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
    </form>
</div>