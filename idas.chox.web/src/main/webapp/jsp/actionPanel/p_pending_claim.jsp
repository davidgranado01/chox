<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!--
 Page to be removed
-->
<script type="text/javascript">

    $(function(){

        $("form#formClaimPendingAcknowledgeAction").validate(
        {
            errorLabelContainer: "#formClaimPendingAcknowledgeMessageBox",
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
    });

    function doClaimPendingFormSubmit(action){
        actionPanel.registerAction(action);
        doClaimPendingValidationSetup(action);

        if($("#formClaimPendingAcknowledgeAction").valid()){

            if(action=='rejectClaim' && !Ext.MessageBox.confirm('Confirm', 'Are you sure you want to reject this claim?',function(btn){if(btn=='yes'){
                    var claimNumber = $("form#formClaimPendingAcknowledgeAction input[name$='claimNumber']").val();
                    var claimId = $("form#formClaimPendingAcknowledgeAction #claimId").val();
                    var form = $("form#formClaimPendingAcknowledgeAction");

                    if(claimNumber && claimNumber.length > 0){
                        checkClaimNumberDuplicationAndSubmit(claimNumber, claimId, form);
                    }else{
                        form.submit();
                    }
                }else{return false;}})){
                return;
            }
            else{
               
                var claimNumber = $("form#formClaimPendingAcknowledgeAction input[name$='claimNumber']").val();
                var claimId = $("form#formClaimPendingAcknowledgeAction #claimId").val();
                var form = $("form#formClaimPendingAcknowledgeAction");

                if(claimNumber && claimNumber.length > 0){
                    checkClaimNumberDuplicationAndSubmit(claimNumber, claimId, form);
                }else{
                    form.submit();
                }
            }
        }
    }

    function doClaimPendingValidationSetup(action){

        // REMOVE ADDED VALIDATION
        $("form#formClaimPendingAcknowledgeAction #claimNumber").rules("remove");
        $("form#formClaimPendingAcknowledgeAction #reasonOfRejectionId").rules("remove");
        $("form#formClaimPendingAcknowledgeAction #percentageLiabilityAccepted").rules("remove", "min");

        // ADD NEW VALIDATION PER SUBMIT TYPE
        if(action=='rejectClaim'){

            $("form#formClaimPendingAcknowledgeAction #reasonOfRejectionId").rules("add", {
                required: true,
                messages: {required: "You must choose a 'Reason For Rejection'"}
            });

            addValidationRulePercentageLiabilityAccepted(0);

        }else if(action=='acknowledgeClaim'){

            $("form#formClaimPendingAcknowledgeAction #reasonOfRejectionId").val("");
            addValidationRuleClaimNumber();
            addValidationRulePercentageLiabilityAccepted(0.01);

        }else if(action=='referEng'){

            $("form#formClaimPendingAcknowledgeAction #reasonOfRejectionId").val("");
            addValidationRuleClaimNumber();
            addValidationRulePercentageLiabilityAccepted(0.01);

        }

    }

    function addValidationRuleClaimNumber(){
        $("form#formClaimPendingAcknowledgeAction #claimNumber").rules("add", {
            required: true,
            messages: {required: "You must supply a value for 'Claim Number'"}
        });
    }

    function addValidationRulePercentageLiabilityAccepted(minValue){
        $("form#formClaimPendingAcknowledgeAction #percentageLiabilityAccepted").rules("add", {
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

</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form id="formClaimPendingAcknowledgeAction" name="formClaimPendingAcknowledgeAction" action="<%=request.getContextPath()%>/prv/processClaim.action" method="POST">
        <fieldset class="x-fieldset">
            <legend>Claim Pending - Action Required</legend>
            <div>
                <s:hidden id="claimId" name="id" />
                <s:hidden id="name" name="name" />
                <div>
                    <div class="status-info">
                        <s:if test="insurerIsEngineersEnabled">
                            Please enter details of the claim and decide whether to acknowledge, refer to an engineer or reject the claim. You can enter public notes in the 'Claim Review Notes' box in order to communicate detailed comments you may have for the CHO.
                        </s:if>
                        <s:else>
                            Please enter details of the claim and decide whether to acknowledge the claim or reject the claim. You can enter public notes in the ‘Claim Review Notes’ box in order to communicate detailed comments you may have for the CHO.
                        </s:else>
                    </div>
                    <div class="status-info">
                        This claim has been pending for <s:property value="daysInStatus" /> day(s).
                    </div>
                    <div class="status-control-set">
                        <table class="status-table">
                            <tr>
                                <td width="20%">
                                    <label>
                                        Indemnity (Decimal)<span class="mandatory">*</span></label>
                                </td><td>
                                    <input type="text" class="chox-ttxt" name="indemnityAmount" id="indemnityAmount" value="<s:property value="indemnityAmount" />"/>
                                </td>
                                <td>
                                    <label>
                                        Invoice Review Required?</label>
                                </td><td>
                                    <s:checkbox id="PCisInvoiceReviewRequiredId"name="isInvoiceReviewRequired" />
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>Claim Number</label></td><td>
                                    <input type="text" class="chox-ttxt" id="claimNumber" name="claimNumber" value="<s:property value="claimNumber" />"/>
                                </td>
                                <td>
                                    <label>Quantum Dispute?</label></td><td>
                                    <s:checkbox name="isQuantumDispute" />
                                </td>
                            </tr>
                            <tr valign="top">
                                <td>
                                    <label>% Liability Accepted<span class="mandatory">*</span></label>
                                </td>
                                <td colspan="3">
                                    <input type="text" class="chox-ttxt" name="percentageLiabilityAccepted" id="percentageLiabilityAccepted" value="<s:property value="percentageLiabilityAccepted" />"/>
                                </td>
                            </tr>
                            <tr valign="top">
                                <td>
                                    <label>Claim Review Notes (Public)</label>
                                </td>
                                <td colspan="3">
                                    <textarea class="chox-canote" cols="80" rows="5" name="engineerClaimReviewNotes" id="engineerClaimReviewNotes"><s:property value="engineerClaimReviewNotes" /></textarea>
                                </td>
                            </tr>
                            <tr valign="top">
                                <td>
                                    <label>Reason for Rejection</label>
                                </td>
                                <td colspan="3">
                                    <s:if test="rejectButtonEnabled">
                                        <div id="ReasonOfRejectionDiv">
                                            <s:select
                                                name="reasonOfRejectionId"
                                                id="reasonOfRejectionId"
                                                list="reasonOfClaimRejections"
                                                listKey="id"
                                                listValue="name"
                                                headerKey=""
                                                headerValue="N/A"
                                                emptyOption="false">
                                            </s:select>
                                        </div>
                                    </s:if>
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
                                <td colspan="4" class="choice" nowrap>
                                    <s:if test="rejectButtonEnabled">
                                        <input type="button" id="PCRejectButtonId"value="Reject" onclick="doClaimPendingFormSubmit('rejectClaim');" />
                                    </s:if>
                                    <input type="button" id="PCAcknowledgeButtonId"value="Acknowledge" onclick="doClaimPendingFormSubmit('acknowledgeClaim')"  />
                                    <s:if test="insurerIsEngineersEnabled">
                                        <input type="button" id="PCReferToEngineerButtonId"value="Refer To Engineer" onclick="doClaimPendingFormSubmit('referEng');" />
                                    </s:if>
                                </td>
                            </tr>
                        </table>
                        <div class="action-error-msg" id="formClaimPendingAcknowledgeMessageBox"></div>
                    </div>
                </div>
            </div>
        </fieldset>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
    </form>
</div>