<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
	var interimPayMade = false;
    var currentRecAmount = <s:property value="interimPaymentReceived" />;
	<s:if test="interimPaymentMade > 0">
		var interimPayMade = true;
	</s:if>
	
    $(function(){
    	
    	$('#additionalInterimPaymentInfo').text('<s:property value="interimPaymentMade" />')
    	if(interimPayMade){
    		$('#radioBox').show();
    	} else {
    		$('#radioBox').hide();
    		$('#additionalInterim').hide();
    		$('#additionalInterimInfo').hide();
    		$('#newPayLabel').html("Interim Payment Amount<span class='mandatory'>*</span>");
    		$('#newTotalInterim').show();
    	}
    	
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
    	$('#ACKmMakeInterimPaymentMessageBox').empty();
    	if($('#interimPayAdd').attr('checked') != undefined){
    		$('#additionalInterim').show();
    		$('#additionalInterimInfo').show();
    		$('#newTotalInterim').hide();
    		$('#newTotalInterimPayment').val('');
    	}else{
    		$('#additionalInterim').hide();
    		$('#additionalInterimInfo').hide();
    		$('#newTotalInterim').show();
    		$('#additionalInterimPayment').val('');
    	}
    }
    
    function setAdditionalInterimInfo(){
    	var aip = parseInt($('#additionalInterimPayment').val());
    	if(!isNaN(aip))
    		$('#additionalInterimPaymentInfo').text(<s:property value="interimPaymentMade" /> + aip);
    	else
    		$('#additionalInterimPaymentInfo').text('<s:property value="interimPaymentMade" />');
    }
    
    function submitInterim(action){
    	if (action == 'additional'){
            $("form#formMakeInterimPayment #newTotalInterimPayment").rules("remove");
            $("form#formMakeInterimPayment #additionalInterimPayment").rules("add", {
                min: 1,
                number:true,
                messages: { required:"The Supplied Value must Be bigger Than 0.",
                	min:"You must supply a value bigger than 0 for 'Additional Interim Payment'",
                    number:"Invalid 'Interim Payment' Format"}
            });
    	} else if (action == 'newTotal'){
            $("form#formMakeInterimPayment #additionalInterimPayment").rules("remove");
            $("form#formMakeInterimPayment #newTotalInterimPayment").rules("add", {
                min: currentRecAmount,
            	number:true,
                messages: { required:"The Supplied Value Cannot Be Less Than The Interim Amount Received.",
                	min:"You must supply a value which is bigger than the current interim amount received value",
                	number:"Invalid 'Interim Payment' Format"}
            });
    	}
    	if($("form#formMakeInterimPayment").valid())
    		$('#formMakeInterimPayment').submit();
    }
    
</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="formMakeInterimPayment" name="formMakeInterimPayment">
        <fieldset class="x-fieldset">
            <legend>Make Interim Payment</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="activityNameId" name="name" value="makeInterimPayment"/>
            <s:if test="(interimPaymentMade > 0) || (interimPaymentReceived > 0)">
                <div class="status-info">
                    <s:if test="interimPaymentMade > 0">
                        An interim payment of £<s:property value="interimPaymentMade" /> has already been made on this claim.<br/>
                    </s:if>
                    <s:if test="interimPaymentReceived > 0">
                        An interim payment of £<s:property value="interimPaymentReceived" /> has been received on this claim.
                    </s:if>
                </div>
                <br/>
            </s:if>
            <div class="chox-form-item" id="radioBox">
                <span class="input-radio"><input type="radio" name="interimPayAdd" id="interimPayAdd" checked="checked" onchange="javascript: setPaymentField()"/> 
                    This interim payment is an addition to the current interim payment.</span>
                <br/>
                <span class="input-radio"><input type="radio" name="interimPayAdd" id="interimPayAdd" onchange="javascript: setPaymentField()"/> 
                    This interim payment is a new total interim payment.</span>
            </div>
            <div>
                <div class="status-control-set">
                    <table class="status-table">
                        <tr id="additionalInterim">
                            <td><label>Additional Interim Payment Amount<span class="mandatory">*</span></label></td>
                            <td nowrap>
                                £&nbsp;<input type="text" class="chox-ttxt" id="additionalInterimPayment" name="additionalInterimPayment" onkeyup="javascript: setAdditionalInterimInfo()" value="<s:property value="additionalInterimPayment" />" />
                                <input type="button" onclick="javascript: submitInterim('additional')" value="Confirm Interim Payment" id="additionalInterimButton"/>
                            </td>
                            <td></td><td></td>
                        </tr>
                        <tr id="additionalInterimInfo">
                            <td><label>New Total Interim Payment Amount</label></td>
                            <td nowrap>
                                £&nbsp;<label id="additionalInterimPaymentInfo" name="additionalInterimPaymentInfo" ></label>
                            </td>
                            <td></td><td></td>
                        </tr>
                        <tr id="newTotalInterim" style="display: none;">
                            <td><label id="newPayLabel">New Total Interim Payment Amount</label></td>
                            <td nowrap>
                                £&nbsp;<input type="text" class="chox-ttxt" id="newTotalInterimPayment" name="newTotalInterimPayment" value="<s:property value="newTotalInterimPayment" />" />
                                <input type="button" onclick="javascript: submitInterim('newTotal')" value="Confirm Interim Payment" id="newTotalInterimButton"/>
                            </td>
                            <td></td><td></td>
                        </tr>
                    </table>
                </div>
            </div>
            <div class="action-error-msg" id="ACKmMakeInterimPaymentMessageBox"></div>
        </fieldset>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
    </form>
</div>
