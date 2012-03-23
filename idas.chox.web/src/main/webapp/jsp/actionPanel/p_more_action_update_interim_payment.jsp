<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

	$("form#formUpdateInterimPayment").validate(
        {
            errorLabelContainer: "#ACKmUpdateInterimPaymentMessageBox",
            rules: {
            	partialInterimPayment:{
            		max: <s:property value="outstandingInterimPayment" />,
                    required:true,
                    number: true
                }
            },
            messages: {
            	partialInterimPayment: {
            		number: "Invalid 'Interim Payment' Format",
                    required:"You must supply a value for 'Claim Number'",
                    max:"The supplied value cannot be bigger than the current interim ammount."
                }
            }
     });
     
    function updateInterimPaymentAction(action){
    	if($("form#formUpdateInterimPayment").valid()){
            $("#updateInterimPaymentFormNameId").val(action);
            $('#formUpdateInterimPayment').submit();
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
                            <td colspan="4">
			             	<div class="status-info">
			                   <s:if test="interimPaymentReceived > 0">
			                   		A total interim amount of £<s:property value="interimPaymentMade" /> has been made (<s:property value="interimPaymentReceived" /> so far received). Please enter the additional amount received: 
			                   </s:if>
			                   <s:else>
			                   	    A total interim amount of £<s:property value="interimPaymentMade" /> has been made. Please enter the amount received: 
			                   </s:else>
			            	</div>
                            </td>
                        </tr>
                        <tr>
                            <td nowrap>
                                <label >Interim Payment Amount</label></td><td nowrap>
                                £&nbsp;<input type="text" class="chox-ttxt" id="partialInterimPayment" name="partialInterimPayment" value="<s:property value="outstandingInterimPayment" />"/>

                                <s:if test="interimPaymentReceived == 0">
                                    <input type="button" value="Interim Payment Received" id="submitInterimPaymentReceived" onclick="return updateInterimPaymentAction('updateInterimPaymentReceived');"/>
                                </s:if>
                                	<input type="button" value="Interim Payment Accepted Full & Final" id="InterimPaymentReceivedfullandfinal" onclick="return updateInterimPaymentAction('updateInterimPaymentFullAndFinal');"/>
                            </td>

                            <td></td><td></td>
                        </tr>
                        <s:if test="interimPaymentReceivedFullAndFinal">
                            <tr><td colspan="3"><label>This interim payment has already been Received as Full & Final </label></td></tr>
                        </s:if>
                        <s:elseif test="interimPaymentMade == 0">
                             <tr><td></td><td colspan="3"><label>No interim payment has been made.</label></td><td></td></tr>
                        </s:elseif>
                        <s:elseif test="outstandingInterimPayment == 0">
                            <tr><td></td><td colspan="3"><label>This interim payment has already been received.</label></td><td></td></tr>
                        </s:elseif>
                    </table>
                </div>
                 <br>
                <div class="action-error-msg" id="ACKmUpdateInterimPaymentMessageBox"></div>
            </div>
        </fieldset>
       <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
    </form>
</div>
