<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

	$(function(){
        $("form#formUpdateInterimPayment").validate(
        {
            errorLabelContainer: "#ACKmUpdateInterimPaymentMessageBox",
            rules: {
            	partialInterimPayment:{
            		max: <s:property value="interimPaymentMade" />,
                    required:true,
                    number: true,
                    min: 0
                }
            },
            messages: {
            	partialInterimPayment: {
            		number: "Invalid 'Interim Payment Received' Format",
                    required:"You Must Supply A Value for the 'Interim Payment Received'",
                    max: "The 'Interim Payment Received' Cannot Be Bigger Than The Total Interim Amount Paid",
                    min: "The 'Interim Payment Received' Must Be 0 or Bigger"
                }
            }
        });
    });
     
    function updateInterimPaymentAction(action){
    	if(action == 'updateInterimPaymentFullAndFinal'){
    		$("#updateInterimPaymentFormNameId").val(action);
                Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
        	$('#formUpdateInterimPayment').submit();
    	}
    	else if($("form#formUpdateInterimPayment").valid()){
            $("#updateInterimPaymentFormNameId").val(action);
            Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
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
			                   		  A total interim amount of £<s:property value="interimPaymentMade" /> has been paid (£<s:property value="interimPaymentReceived" /> so far received).
                                      Enter the new total amount received and click the 'Interim Payment Received' button. Alternatively, clicking the 'Interim Payment Accepted Full & Final' button will accept the total interim amount paid as a final payment and move the claim to the 'PaymentReceived' status. 
			                        </s:if>
			                        <s:else>
			                   	      A total interim amount of £<s:property value="interimPaymentMade" /> has been paid.
                                      Enter the total amount received and click the 'Interim Payment Received' button. Alternatively, clicking the 'Interim Payment Accepted Full & Final' button will accept the total interim amount paid as a final payment and move the claim to the 'PaymentReceived' status.
			                        </s:else>
			            	    </div>
                            </td>
                        </tr>
                        <tr>
                            <td nowrap>
                                <label>Interim Payment Received</label>
                            </td>
                            <td nowrap>
                                    <label>£&nbsp;</label><input type="text" class="chox-ttxt" id="partialInterimPayment" name="partialInterimPayment" value="<s:property value="interimPaymentMade" />"/>
                                    <input type="button" value="Interim Payment Received" id="submitInterimPaymentReceived" onclick="return updateInterimPaymentAction('updateInterimPaymentReceived');"/>
                                	<input type="button" value="Interim Payment Accepted Full & Final" id="InterimPaymentReceivedfullandfinal" onclick="return updateInterimPaymentAction('updateInterimPaymentFullAndFinal');"/>
                            </td>
                            <td></td><td></td>
                        </tr>
                    </table>
                </div>
                 <br>
                <div class="action-error-msg" id="ACKmUpdateInterimPaymentMessageBox"></div>
            </div>
        </fieldset>
       <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
    </form>
</div>
