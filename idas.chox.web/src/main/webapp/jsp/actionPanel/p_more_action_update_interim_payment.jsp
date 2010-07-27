<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    $(function(){
        if (<s:property value="interimPaymentReceived || false" />) {
            $('#interimPayment').attr("disabled", true);
            $('#submitInterimPaymentReceived').attr("disabled", true);
        }
        else if (!<s:property value="interimPaymentReceived || false" /> && <s:property value="interimPayment != null" />) {
            $('#interimPayment').attr("disabled", false);
            $('#submitInterimPaymentReceived').attr("disabled", false);
        }
        else {
            $('#interimPayment').attr("disabled", true);
            $('#submitInterimPaymentReceived').attr("disabled", true);
        }
    });
</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/updateInterimPayment.action" method="post" id="formUpdateInterimPayment" name="formUpdateInterimPayment">
        <fieldset class="x-fieldset">
            <legend>Update Interim Payment</legend>
            <s:hidden id="claimId" name="id" />
            <div>
                <div class="status-control-set">
                    <table class="status-table">
                        <tr>
                            <td>
                                <label>Interim Payment Amount</label></td><td nowrap>
                                £&nbsp;<input type="text" class="chox-ttxt" disabled="true" id="interimPayment" name="interimPayment" value="<s:property value="interimPayment" />"/>
                                <input type="submit" value="Interim Payment Received" id="submitInterimPaymentReceived"/>
                            </td>
                            <td></td><td></td>
                        </tr>
                        <s:if test="interimPaymentReceived">
                            <tr><td colspan="3"><label>This interim payment has already been received</label></td></tr>
                        </s:if>
                        <s:elseif test="!interimPaymentReceived && interimPayment">
                        </s:elseif>
                        <s:else>
                            <tr><td colspan="3"><label>No interim paymwent has been made.</label></td></tr>
                        </s:else>
                    </table>
                </div>
                <div class="action-error-msg" id="ACKmUpdateInterimPaymentMessageBox"></div>
            </div>
        </fieldset>
        <s:token/>
    </form>
</div>
