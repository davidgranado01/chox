<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    $(function(){

        $("form#formMakeInterimPayment").validate(
        {
            errorLabelContainer: "#ACKmMakeInterimPaymentMessageBox",
            rules: {
            	additionalInterimPayment:{
                    required:true,
                    number:true
                },
                newTotalInterimInput:{
                    required:true,
                    number:true
                }
            },
            messages: {
                additionalInterimInput: {
                    required:"You must supply a value for 'Additional Interim Payment'",
                    number:"Invalid 'Interim Payment' Format"
                },
                newTotalInterimInput: {
                    required:"You must supply a value for 'New Total Interim Payment'",
                    number:"Invalid 'Interim Payment' Format"
                }
            }
        });
        
    });
    
    function setPaymentField(){
    	if($('#interimPayAdd').attr('checked') != undefined){
    		$('#additionalInterim').show();
    		$('#newTotalInterim').hide();
    		$('#newTotalInterimPayment').val('');
    	}else{
    		$('#additionalInterim').hide();
    		$('#newTotalInterim').show();
    		$('#additionalInterimPayment').val('');
    	}
    }
    
    function submitInterim(action){
    	
    	if (action == 'additional'){
            $("form#formMakeInterimPayment #newTotalInterimPayment").rules("remove");
    	} else if (action == 'newTotal'){
            $("form#formMakeInterimPayment #additionalInterimPayment").rules("remove");
    	}
    	if($("#formMakeInterimPayment").valid())
    		$('#formMakeInterimPayment').submit();
    }
    
    $('#additionalInterim').hide();
	$('#newTotalInterim').show();
    
</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/makeInterimPayment.action" method="post" id="formMakeInterimPayment" name="formMakeInterimPayment">
        <fieldset class="x-fieldset">
            <legend>Make Interim Payment</legend>
            <s:hidden id="claimId" name="id" />
            
            <div class="status-info">
            <s:if test="interimPayment != null && interimPayment > 0">
                 An interim payment of £ <s:property value="interimPayment" /> has been made on this claim.<br/>
            </s:if>
            <s:if test="interimPaymentReceivedAmount != null && interimPaymentReceivedAmount > 0">
                  An interim payment of £ <s:property value="interimPaymentReceivedAmount" /> has already been received on this claim.
            </s:if>
            </div>
            <br/>
            
            <s:if test="interimPayment != null && interimPayment > 0">
				<div class="chox-form-item">
						<span class="input-radio"><input type="radio" name="interimPayAdd" id="interimPayAdd" onchange="javascript: setPaymentField()"/> 
						This interim payment is addition to the current interim payment.</span>
						<br/>
						<span class="input-radio"><input type="radio" name="interimPayAdd" id="interimPayAdd" checked="checked" onchange="javascript: setPaymentField()"/> 
						This interim payment is new total interim payment.</span>
				</div>
			</s:if>
				<div>
	                <div class="status-control-set">
	                    <table class="status-table">
	                    	<tr id="newTotalInterim" style="display: none;">
	                            <td><label>New Total Interim Payment Amount<span class="mandatory">*</span></label></td>
	                       			<td nowrap>
		                                £&nbsp;<input type="text" class="chox-ttxt" id="newTotalInterimPayment" name="newTotalInterimPayment" value="<s:property value="newTotalInterimPayment" />" />
		                                <input type="button" onclick="javascript: submitInterim('newTotal')" value="Confirm Interim Payment" id="newTotalInterimButton"/>
		                            </td>
	                            <td></td><td></td>
	                        </tr>
	                        <tr id="additionalInterim">
	                            <td><label>Additional Interim Payment Amount<span class="mandatory">*</span></label></td>
	                       			<td nowrap>
		                                £&nbsp;<input type="text" class="chox-ttxt" id="additionalInterimPayment" name="additionalInterimPayment" value="<s:property value="additionalInterimPayment" />" />
		                                <input type="button" onclick="javascript: submitInterim('additional')" value="Confirm Interim Payment" id="additionalInterimButton"/>
		                            </td>
	                            <td></td><td></td>
	                        </tr>
	                    </table>
	                </div>
	                <div class="action-error-msg" id="ACKmMakeInterimPaymentMessageBox"></div>
	            </div>
            <div class="action-error-msg" id="ACKmMakeInterimPaymentMessageBox"></div>
		</fieldset>
       <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
    </form>
</div>
