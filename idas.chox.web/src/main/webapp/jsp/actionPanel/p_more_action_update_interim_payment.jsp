<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(function(){
        <s:if test="interimPaymentReceived || false" >
            $('#submitInterimPaymentReceived').attr("disabled", true);
        </s:if>
        <s:elseif test="(interimPaymentReceived!=true || false) && interimPayment != null" >
            $('#submitInterimPaymentReceived').attr("disabled", false);
        </s:elseif>
        <s:else >
            $('#submitInterimPaymentReceived').attr("disabled", true);
        </s:else>
    });
    
    function updateInterimPaymentAction(action){
    	if($("form#formUpdateInterimPayment").valid()){
            $("#updateInterimPaymentFormNameId").val(action);
        }
    }

</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">

<form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="formUpdateInterimPayment" name="formUpdateInterimPayment">
        <fieldset class="x-fieldset">
            <legend>Update Interim Payment</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="updateInterimPaymentFormNameId" name="name"/>
            <div>
                <div class="status-control-set">
                    <table class="status-table">
                        <tr>
                            <td nowrap>
                                <label >Interim Payment Amount</label></td><td nowrap>
                                £&nbsp;<input type="text" class="chox-ttxt" id="partialInterimPayment" name="partialInterimPayment" value="<s:property value="partialInterimPayment" />"/>

                                <s:if test="!interimPaymentReceived">
                                    <input type="submit" value="Interim Payment Received" id="submitInterimPaymentReceived" onclick="return updateInterimPaymentAction('updateInterimPaymentReceived');"/>
                                </s:if>
                                	<input type="submit" value="Interim Payment Accepted Full & Final" id="InterimPaymentReceivedfullandfinal" onclick="return updateInterimPaymentAction('updateInterimPaymentFullAndFinal');"/>
                            </td>

                            <td></td><td></td>
                        </tr>

						</div>
			             	<div class="status-info">
			                   <s:if test="interimPaymentReceivedAmount != null && interimPaymentReceivedAmount > 0">
			                   		A total interim amount of £<s:property value="interimPayment" /> has been made (<s:property value="interimPaymentReceivedAmount" /> so far received). Please enter the additional amount received: 
			                   </s:if>
			                   <s:else>
			                   	    A total interim amount of £<s:property value="interimPayment" /> has been made. Please enter the amount received: 
			                   </s:else>
			            	</div>
			            <div>

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
                 <br>
                <div class="action-error-msg" id="ACKmUpdateInterimPaymentMessageBox"></div>
            </div>
        </fieldset>
       <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
    </form>
</div>
