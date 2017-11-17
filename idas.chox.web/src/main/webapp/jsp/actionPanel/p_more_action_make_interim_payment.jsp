<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var interimPayMade = false;
    var interimPayment;
    var currentRecAmount = <s:property value="interimPaymentReceived" />;
    var totalToPay = <s:property value="totalToPay" />;;
	<s:if test="interimPaymentMade > 0">
		var interimPayMade = true;
                interimPayment = <s:property value="interimPaymentMade" />;
	</s:if>
	
    Ext.onReady(function() {
    	var supportingNotesField = new Ext.form.TextArea({
            name             : 'supportingInterimNotes',
            id               : 'supportingNotesId',
            width            :  350,
            height           :  40,
            allowBlank       :  false,
            renderTo         : 'interimPaymentNotesDivId'
        });

    	$('#additionalInterimPaymentInfo').text('<s:property value="interimPaymentMade" />');
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
                },
                supportingInterimNotes:{
                    required:true
                }
            },
            messages: {
                additionalInterimPayment: {
                    required:"You must supply a value for 'Additional Interim Payment'",
                    number:"Invalid 'Interim Payment' Format"
                },
                newTotalInterimInput: {
                    required:"You must supply a value for 'New Total Interim Payment'",
                    number:"Invalid 'Interim Payment' Format"
                },
                supportingInterimNotes: {
                    required:"You must supply 'Supporting Interim Payment Notes'"
                }
            },
        });
        
    });
    
    function setPaymentField(){
    	$('#ACKmMakeInterimPaymentMessageBox').empty();
    	$('#supportingNotesId').empty();
    	if($('#interimPayAdd').attr('checked') !== undefined){
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
        return true;
    }
    
    function setAdditionalInterimInfo(){
    	var aip = parseInt($('#additionalInterimPayment').val());
    	if(!isNaN(aip))
    		$('#additionalInterimPaymentInfo').text(<s:property value="interimPaymentMade" /> + aip);
    	else
    		$('#additionalInterimPaymentInfo').text('<s:property value="interimPaymentMade" />');
        return true;
    }
    
    function submitInterim(action){
    	if (action === 'additional'){
            $("form#formMakeInterimPayment #newTotalInterimPayment").rules("remove");
            $("form#formMakeInterimPayment #additionalInterimPayment").rules("add", {
                required: true,
                min: 0.01,
                max : (totalToPay - interimPayment),
                number:true,
                messages: {
                    required:"You Must Enter An 'Additional Interim Payment Amount'",
                    min:"The 'Additional Interim Payment Amount' Must Be Larger Than 0",
                    max : "The 'Additional Interim Payment Amount' Must Be Less Than or Equal To 'Total To Pay'",
                    number:"The 'Additional Interim Payment Amount' Must Be A Monetary Value"}
            });
    	} else if (action === 'newTotal' && interimPayMade){
            $("form#formMakeInterimPayment #additionalInterimPayment").rules("remove");
            $("form#formMakeInterimPayment #newTotalInterimPayment").rules("add", {
                required: true,
                min: currentRecAmount,
                max : totalToPay,
            	number:true,
                messages: {
                    required:"You Must Enter A 'New Total Interim Payment Amount'",
                    min:"The 'New Total Interim Payment Amount' Must Be Greater Than Or Equal To The Current Interim Amount Received",
                    max : "The 'New Total Interim Payment Amount' Must Be Less Than or Equal To 'Total To Pay'",
                    number:"The 'New Total Interim Payment Amount' Must Be A Monetary Value"}
            });
    	} else if (action === 'newTotal' && !interimPayMade) {
            $("form#formMakeInterimPayment #additionalInterimPayment").rules("remove");
            $("form#formMakeInterimPayment #newTotalInterimPayment").rules("add", {
                required: true,
                min: 0.01,
                max : totalToPay,
            	number:true,
                messages: { required:"You Must Enter An 'Interim Payment Amount'",
                	min:"The 'Interim Payment Amount' Must Be Larger Than 0",
                        max : "The 'Interim Payment Amount' Must Be Less Than or Equal To 'Total To Pay'",
                	number:"The 'Interim Payment Amount' Must Be A Monetary Value"}
            });

        }
    	if($("form#formMakeInterimPayment").valid()) {
            Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
            choxJqueryHttpSubmit($("form#formMakeInterimPayment"));
        }
        
        return false;
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
                <span class="input-radio"><input type="radio" name="interimPayAdd" id="interimPayAdd" checked="checked" onClick="return setPaymentField();"/> 
                    This interim payment is an addition to the current interim payment.</span>
                <br/>
                <span class="input-radio"><input type="radio" name="interimPayAdd" id="interimPayAdd" onClick="return setPaymentField();"/> 
                    This interim payment is a new total interim payment.</span>
            </div>
            <div>
                <div class="status-control-set">
                    <table class="status-table">
                        <tr id="supportingInterimPaymentNote">
                            <td nowrap="nowrap"><label>Supporting Interim Payment Notes (Public)<span class="mandatory">*</span></label></td>
                            <td>
                                <div id="interimPaymentNotesDivId"/>
                            </td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr id="additionalInterim">
                            <td><label>Additional Interim Payment Amount<span class="mandatory">*</span></label></td>
                            <td nowrap="nowrap">
                                £&nbsp;<input type="text" class="chox-ttxt" id="additionalInterimPayment" name="additionalInterimPayment" onkeyup="return setAdditionalInterimInfo();" value="<s:property value="additionalInterimPayment" />" />
                                <input type="button" onclick="event.preventDefault(); submitInterim('additional');" value="Confirm Interim Payment" id="additionalInterimButton"/>
                            </td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr id="additionalInterimInfo">
                            <td><label>New Total Interim Payment Amount</label></td>
                            <td nowrap="nowrap">
                                £&nbsp;<label id="additionalInterimPaymentInfo"></label>
                            </td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr id="newTotalInterim" style="display: none;">
                            <td><label id="newPayLabel">New Total Interim Payment Amount<span class="mandatory">*</span></label></td>
                            <td nowrap="nowrap">
                                £&nbsp;<input type="text" class="chox-ttxt" id="newTotalInterimPayment" name="newTotalInterimPayment" value="<s:property value="newTotalInterimPayment" />" />
                                <input type="button" onclick="event.preventDefault(); submitInterim('newTotal');" value="Confirm Interim Payment" id="newTotalInterimButton"/>
                            </td>
                            <td></td><td></td>
                        </tr>
                    </table>
                </div>
            </div>
            <div class="action-error-msg" id="ACKmMakeInterimPaymentMessageBox"></div>
        </fieldset>
    </form>
</div>
