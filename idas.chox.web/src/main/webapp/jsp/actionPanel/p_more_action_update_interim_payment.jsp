<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var randomNumberGenerator=11;
    $(function(){
        if (<s:property value="interimPaymentReceived || false" />) {
            $('#submitInterimPaymentReceived').attr("disabled", true);
        }
        else if (!<s:property value="interimPaymentReceived || false" /> && <s:property value="interimPayment != null" />) {
            $('#submitInterimPaymentReceived').attr("disabled", false);
        }
        else {
            $('#submitInterimPaymentReceived').attr("disabled", true);
        }


    });

    function InterimpaymentReceived(){

        randomNumberGenerator=10;
        return randomNumberGenerator;
    }
    function InterimPaymentFullAndFinal(){

        randomNumberGenerator=20;
        return randomNumberGenerator;
    }
</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/updateInterimPayment.action" method="post" id="formUpdateInterimPayment" name="formUpdateInterimPayment">
        <input type="hidden" id="submitAction" name="actionSelected" value=""/>
        <fieldset class="x-fieldset">
            <legend>Update Interim Payment</legend>
            <s:hidden id="claimId" name="id" />
            <div>
                <div class="status-control-set">
                    <table class="status-table">
                        <tr>
                            <td nowrap>
                                <label >Interim Payment Amount</label></td><td nowrap>
                                £&nbsp;<input type="text" class="chox-ttxt" disabled="true" id="interimPayment" name="interimPayment" value="<s:property value="interimPayment" />"/>

                                <s:if test="!interimPaymentReceived">
                                    <input type="submit" value="Interim Payment Received" id="submitInterimPaymentReceived" onclick="submitAction.value = InterimpaymentReceived()"/>
                                </s:if>
                                <input type="submit" value="Interim Payment Accepted Full & Final" id="InterimPaymentReceivedfullandfinal" onclick="submitAction.value = InterimPaymentFullAndFinal()"/>
                            </td>

                            <td></td><td></td>
                        </tr>

                        <s:if test="InterimPaymentReceivedFullAndFinal">
                            <tr><td colspan="3"><label>This interim payment has already been Received Full & Final </label></td></tr>
                        </s:if>
                        <s:elseif test="interimPaymentReceived">
                            <tr><td></td><td colspan="3"><label>This interim payment has already been received.</label></td><td></td></tr>
                        </s:elseif>
                        <s:elseif test="!interimPaymentReceived && interimPayment">
                        </s:elseif>
                        <s:else>
                             <tr><td></td><td colspan="3"><label>No interim payment has been made.</label></td><td></td></tr>
                        </s:else>
                    </table>
                </div>
                <div class="action-error-msg" id="ACKmUpdateInterimPaymentMessageBox"></div>
            </div>
        </fieldset>
       <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
    </form>
</div>
