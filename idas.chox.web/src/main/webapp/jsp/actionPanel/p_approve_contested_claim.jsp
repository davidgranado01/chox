<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(function(){

        $("form#formProcessRejectedClaim").validate(
        {
            errorLabelContainer: "#formProcessRejectedClaimMessageBox",
            rules: {
                indemnityAmount:{
                    required:true,
                    number:true
                },
                percentageLiabilityAccepted:{
                    required:true,
                    number:true,
                    max: 100.00
                }
            },
            messages: {
                indemnityAmount: {
                    required:"You must supply a value for 'Indemnity'",
                    number:"You must supply a numeric value for 'Indemnity'"
                },
                percentageLiabilityAccepted: {
                    required:"You must supply a value for 'Percentage Liability Accepted'",
                    number:"You must supply a numeric value for 'Percentage Liability Accepted'",
                    max:"'Percentage Liability Accepted' cannot be more than 100"
                }
            }
        });
        
        var rejectionDescField = new Ext.form.TextArea({
            name             : 'rejectionDescription',
            id               : 'rejecDescId',
            width            :  350,
            height           :  80,
            allowBlank       :  false,
            renderTo         : 'rejectionDescId',
            disabled         : '<s:property value="rejectButtonEnabled"/>' == 'false'
        });
    });

    function doProcessRejectedClaimFormSubmit(action){

        actionPanel.registerAction(action);
        doProcessRejectedClaimFormValidationSubmit(action);

        if($("form#formProcessRejectedClaim").valid()){

            if(action=='rejectClaim' && !Ext.MessageBox.confirm('Confirm', 'Are you sure you want to reject this claim?',function(btn){if(btn=='yes'){
                    var claimNumber = $("form#formProcessRejectedClaim input[name$='claimNumber']").val();
                    var claimId = $("form#formProcessRejectedClaim #claimId").val();
                    var form = $("form#formProcessRejectedClaim");

                    if(claimNumber && claimNumber.length > 0){
                        checkClaimNumberDuplicationAndSubmit(claimNumber, claimId, form);
                    }else{
                        form.submit();
                    }
                }else{return false;}})){
                return;
            }
            else{
               
                var claimNumber = $("form#formProcessRejectedClaim input[name$='claimNumber']").val();
                var claimId = $("form#formProcessRejectedClaim #claimId").val();
                var form = $("form#formProcessRejectedClaim");

                if(claimNumber && claimNumber.length > 0){
                    checkClaimNumberDuplicationAndSubmit(claimNumber, claimId, form);
                }else{
                    form.submit();
                }
            
            }
        }
    }

    function doProcessRejectedClaimFormValidationSubmit(action){

        // REMOVE ADDED VALIDATION
        $("form#formProcessRejectedClaim #claimNumber").rules("remove");
        $("form#formProcessRejectedClaim #reasonOfRejectionId").rules("remove");
        $("form#formProcessRejectedClaim #percentageLiabilityAccepted").rules("remove", "min");
        $("form#formProcessRejectedClaim #rejecDescId").rules("remove");
        
        // ADD NEW VALIDATION PER SUBMIT TYPE
        if(action=='rejectClaim'){

            $("form#formProcessRejectedClaim #reasonOfRejectionId").rules("add", {
                required: true,
                messages: {required: "You must choose a 'Reason For Rejection'"}
            });
            
            $("form#formProcessRejectedClaim #rejecDescId").rules("add", {
                required: true,
                messages: {required: "You must enter 'Supporting Rejection Notes'"}
            });

            addValidationRulePercentageLiabilityAccepted(0);

        }else if(action=='acknowledgeClaim'){

            $("form#formProcessRejectedClaim #reasonOfRejectionId").val("");
            addValidationRuleClaimNumber();
            addValidationRulePercentageLiabilityAccepted(0.01);

        }else if(action=='referEng'){

            $("form#formProcessRejectedClaim #reasonOfRejectionId").val("");
            addValidationRuleClaimNumber();
            addValidationRulePercentageLiabilityAccepted(0.01);

        }else if(action=='referFNOL'){

            $("form#formProcessRejectedClaim #reasonOfRejectionId").val("");
            addValidationRulePercentageLiabilityAccepted(0);

        }else if(action=='pending'){

            $("form#formProcessRejectedClaim #reasonOfRejectionId").val("");
            addValidationRulePercentageLiabilityAccepted(0);

        }

    }

    function addValidationRuleClaimNumber(){
        $("form#formProcessRejectedClaim #claimNumber").rules("add", {
            required: true,
            messages: {required: "You must supply a value for 'Claim Number'"}
        });
    }

    function addValidationRulePercentageLiabilityAccepted(minValue){
        $("form#formProcessRejectedClaim #percentageLiabilityAccepted").rules("add", {
            min: minValue,
            messages: {min: "'Percentage Liability Accepted' must be more than or equal to "+minValue}
        });
    }

    function checkClaimNumberDuplicationAndSubmit(claimNumber, claimId, form)
    {
        var url = "<%=request.getContextPath()%>/prv/p/checkIsClaimNumberDuplicated.action";
        var param = {
            claimNumber: claimNumber,
            claimId: claimId
        };

        ajax.loadJson2(url, param, function(data){
            if(data.result && data.resultType=='YesNo'){
                Ext.MessageBox.confirm('Confirm', data.result,function(btn){
                    if(btn=='yes')
                    {
                        form.submit();
                    }
                }); 
            }
            else form.submit();
        });
    }
    
    var reasonOfRejectionDescReader = new Ext.data.JsonReader({
        fields:[{name:'id'},{name:'description'}]
    });
    
    var reasonOfRejectionDescStore = new Ext.data.Store({
        data : Ext.util.JSON.decode('<s:property value="jsonReasonOfClaimRejectionDesc" escape="false"/>'),
        reader : reasonOfRejectionDescReader
    });
    
    function refreshDesc(id){
        reasonOfRejectionDescStore.each(function(rec) {
            if(id == rec.json.text){
                Ext.getCmp('rejecDescId').setValue(rec.json.value);
            }
        });
        if(id == -1 || id == '')
            Ext.getCmp('rejecDescId').setValue("");
    }

</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="formProcessRejectedClaim" name="formProcessRejectedClaim">

        <fieldset class="x-fieldset">
            <legend>Contested Claim - Action Required</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="name" name="name" />
            <div>
                <div class="status-info">
                    <s:if test="insurerIsEngineersEnabled && insurerIsFnolEnabled">
                        Please enter details of the claim and decide whether to acknowledge the claim, refer the claim to an engineer, refer the claim to an FNOL handler or set the claim to pending. You can enter public notes in the 'Claim Review Notes' box in order to communicate detailed comments you may have for the CHO.
                    </s:if>
                    <s:elseif test="!insurerIsEngineersEnabled && !insurerIsFnolEnabled">
                        Please enter details of the claim and decide whether to acknowledge the claim or set the claim to pending. You can enter public notes in the 'Claim Review Notes' box in order to communicate detailed comments you may have for the CHO.
                    </s:elseif>
                    <s:elseif test="!insurerIsEngineersEnabled && insurerIsFnolEnabled">
                        Please enter details of the claim and decide whether to acknowledge the claim, refer the claim to an FNOL handler or set the claim to pending. You can enter public notes in the 'Claim Review Notes' box in order to communicate detailed comments you may have for the CHO.
                    </s:elseif>
                    <s:elseif test="insurerIsEngineersEnabled && !insurerIsFnolEnabled">
                        Please enter details of the claim and decide whether to acknowledge the claim, refer the claim to an engineer or set the claim to pending. You can enter public notes in the 'Claim Review Notes' box in order to communicate detailed comments you may have for the CHO.
                    </s:elseif>
                    <s:if test="rejectButtonEnabled">
                        Alternatively, if you would like to reject the claim back to the CHO, then select a 'Reason For Rejection'.
                    </s:if>
                    <s:elseif test="isSubscriberClaim">
                        This claim cannot be rejected as the Subscriber notification 5 day SLA has passed.
                    </s:elseif>
                    <s:elseif test="isFixedFeeClaim">
                        This claim cannot be rejected as the Fixed Fee notification 14 day SLA has passed.
                    </s:elseif>
                </div>
                <div class="status-control-set">
                    <div class="status-control-set">
                        <table class="status-table">
                            <tr>
                                <td>
                                    <label>
                                        Indemnity (Decimal)<span class="mandatory">*</span></label></td><td>
                                    <input type="text" class="chox-ttxt" name="indemnityAmount" id="indemnityAmount" value="<s:property value="indemnityAmount" />"/>
                                </td>
                                <td>
                                    <label>
                                        Invoice Review Required?</label></td><td>
                                        <s:checkbox name="isInvoiceReviewRequired" />
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>Claim Number</label></td><td>
                                    <input type="text" class="chox-ttxt" id="claimNumber" name="claimNumber" value="<s:property value="claimNumber" />"/>
                                </td>
                                <td>
                                    <label>
                                        Quantum Dispute?</label></td><td>
                                        <s:checkbox name="isQuantumDispute" />
                                </td>
                            </tr>
                            <tr valign="top">
                                <td>
                                    <label>
                                        % Liability Accepted<span class="mandatory">*</span></label></td><td colspan="3">
                                    <input type="text" class="chox-ttxt" name="percentageLiabilityAccepted" id="percentageLiabilityAccepted" value="<s:property value="percentageLiabilityAccepted" />"/>
                                </td>

                            </tr>
                            <tr valign="top">
                                <td>
                                    <label>
                                        Claim Review Notes (Public)</label></td><td colspan="3">
                                    <textarea class="chox-canote" cols="20" rows="5" name="engineerClaimReviewNotes" id="engineerClaimReviewNotes"><s:property value="engineerClaimReviewNotes" /></textarea>
                                </td>

                            </tr>
                            <tr valign="top">
                                <td>
                                    <label>Reason For Rejection</label>
                                </td>
                                <td colspan="3">
                                    <div id="ReasonOfRejectionDiv">
                                        <s:if test="rejectButtonEnabled">
                                            <s:select name="reasonOfRejectionId" id="reasonOfRejectionId"
                                                      list="reasonOfClaimRejections"
                                                      listKey="id"
                                                      listValue="rorName"
                                                      onchange="refreshDesc(this.value)"
                                                      headerKey=""
                                                      headerValue="N/A"
                                                      emptyOption="false"></s:select>
                                        </s:if>
                                        <s:else>
                                            <s:select name="reasonOfRejectionId" id="reasonOfRejectionId"
                                                      list="reasonOfClaimRejections"
                                                      listKey="id"
                                                      listValue="rorName"
                                                      onchange="refreshDesc(this.value)"
                                                      headerKey=""
                                                      disabled="true"
                                                      headerValue="N/A"
                                                      emptyOption="false">
                                            </s:select>
                                        </s:else>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                            <td align="right" valign="top"><label class="std-label-ro">Supporting Rejection Note&nbsp;&nbsp;</label></td>
                                <td>
                                    <div id="rejectionDescId"/>
                                </td>
                           </tr>
                           <tr>
                                <td colspan="4">
                                    <div class="no-format">
                                        <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="4" class="choice" nowrap="true">
                                    <input type="button" id="ACCRejectButtonId" value="Reject" <s:if test="rejectButtonEnabled == false">disabled='true'</s:if> onclick="doProcessRejectedClaimFormSubmit('rejectClaim');" />
                                    <input type="button" id="ACCAcknowledgeButtonId" value="Acknowledge" onclick="doProcessRejectedClaimFormSubmit('acknowledgeClaim')"  />
                                    <s:if test="insurerIsEngineersEnabled">
                                        <input type="button" id="ACCReferToEngineerButtonId"value="Refer To Engineer" onclick="doProcessRejectedClaimFormSubmit('referEng');" />
                                    </s:if>
                                    <s:if test="insurerIsFnolEnabled">
                                        <input type="button" id="ACCReferToFnolButtonId" value="Refer to FNOL" onclick="doProcessRejectedClaimFormSubmit('referFNOL');" />
                                    </s:if>
                                    <input type="button" id="ACCClaimPendingButtonId" value="Claim Pending" onclick="doProcessRejectedClaimFormSubmit('pending');" />
                                </td>
                            </tr>
                        </table>
                        <div id="formProcessRejectedClaimMessageBox" class="action-error-msg"></div>
                    </div>
                </div>
            </div>
        </fieldset>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
    </form>
</div>