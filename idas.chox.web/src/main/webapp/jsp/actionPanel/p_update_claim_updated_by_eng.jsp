<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(function(){

        $("form#formUpdateByEngAcknowledgeAction").validate(
        {
            errorLabelContainer: "#updateByEngMessageBox",
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

    function doClaimUpdatedByEngFormSubmit(action){

        var formName = "formUpdateByEngAcknowledgeAction";
        var form = $("form#"+formName+"");
        actionPanel.registerAction(action);
        doClaimUpdatedByEngFormValidationSetup(formName, action);

        if(form.valid()){

            if(action=='rejectClaim' && Ext.MessageBox.confirm('Confirm', 'Are you sure you want to reject this claim?',function(btn){if(btn=='yes'){
                    var claimNumber = $("form#"+formName+" input[name$='claimNumber']").val();
                    var claimId = $("form#"+formName+" #claimId").val();

                    if(claimNumber && claimNumber.length > 0){
                        checkClaimNumberDuplicationAndSubmit(claimNumber, claimId, form);
                    }else{
                        form.submit();
                    }
                }else{return false;}})){
                return;
            }
            else{
               
                var claimNumber = $("form#"+formName+" input[name$='claimNumber']").val();
                var claimId = $("form#"+formName+" #claimId").val();

                if(claimNumber && claimNumber.length > 0){
                    checkClaimNumberDuplicationAndSubmit(claimNumber, claimId, form);
                }else{
                    form.submit();
                }
            }
        }
    }

    function doClaimUpdatedByEngFormValidationSetup(formName, action){

        // REMOVE ADDED VALIDATION
        $("form#"+formName+" #claimNumber").rules("remove");
        $("form#"+formName+" #reasonOfRejectionId").rules("remove");
        $("form#"+formName+" #percentageLiabilityAccepted").rules("remove", "min");

        // ADD NEW VALIDATION PER SUBMIT TYPE
        if(action=='rejectClaim'){

            $("form#"+formName+" #reasonOfRejectionId").rules("add", {
                required: true,
                messages: {required: "You must choose a 'Reason For Rejection'"}
            });

            addValidationRulePercentageLiabilityAccepted(formName, 0);
        }else if(action=='acknowledgeClaim'){
            $("form#"+formName+" #reasonOfRejectionId").val("");
            addValidationRuleClaimNumber(formName);
            addValidationRulePercentageLiabilityAccepted(formName, 0.01);
        }else if(action=='referEng'){
            $("form#"+formName+" #reasonOfRejectionId").val("");
            addValidationRuleClaimNumber(formName);
            addValidationRulePercentageLiabilityAccepted(formName, 0.01);
        }else if(action=='pending'){
            $("form#"+formName+" #reasonOfRejectionId").val("");
            addValidationRulePercentageLiabilityAccepted(formName, 0);
        }
    }

    function addValidationRuleClaimNumber(formName){
        $("form#"+formName+" #claimNumber").rules("add", {
            required: true,
            messages: {required: "You must supply a value for 'Claim Number'"}
        });
    }

    function addValidationRulePercentageLiabilityAccepted(formName, minValue){
        $("form#"+formName+" #percentageLiabilityAccepted").rules("add", {
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
    <form id="formUpdateByEngAcknowledgeAction" name="formUpdateByEngAcknowledgeAction" action="<%=request.getContextPath()%>/prv/processClaim.action" method="POST">
        <fieldset class="x-fieldset">
            <legend>Claim Acknowledgement - Action Required</legend>
            <div>
                <s:hidden id="claimId" name="id" />
                <s:hidden id="name" name="name" />
                <div>
                    <div class="status-info">
                        <s:if test="insurerIsEngineersEnabled">
                            Please review the Engineer's notes, if applicable enter details of the claim and decide whether to acknowledge the claim, refer the claim to an engineer or set the claim to pending. You can enter public notes in the 'Claim Review Notes' box in order to communicate detailed comments you may have for the CHO.
                        </s:if>
                        <s:else>
                            Please review the Engineer's notes, if applicable enter details of the claim and decide whether to acknowledge the claim or set the claim to pending. You can enter public notes in the 'Claim Review Notes' box in order to communicate detailed comments you may have for the CHO.
                        </s:else>
                        <s:if test="rejectButtonEnabled">
                            Alternatively, if you would like to reject the claim back to the CHO, then select a 'Reason for Rejection'.
                        </s:if>
                        <s:else>
                            This claim cannot be rejected as the Subscriber notification 5 day SLA has passed.
                        </s:else>
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
                                    <s:checkbox id="UCUBEisInvoiceReviewRequiredId" name="isInvoiceReviewRequired" />
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
                                    <div id="ReasonOfRejectionDiv">
                                        <s:if test="rejectButtonEnabled">
                                            <s:select
                                                name="reasonOfRejectionId"
                                                id="reasonOfRejectionId"
                                                list="reasonOfClaimRejections"
                                                listKey="id"
                                                listValue="name"
                                                headerKey=""
                                                headerValue="N/A"
                                                emptyOption="false"></s:select>
                                        </s:if>
                                        <s:select
                                            name="reasonOfRejectionId"
                                            id="reasonOfRejectionId"
                                            list="reasonOfClaimRejections"
                                            listKey="id"
                                            listValue="name"
                                            headerKey=""
                                            disabled="true"
                                            headerValue="N/A"
                                            emptyOption="false"></s:select>
                                        <s:else>

                                        </s:else>
                                    </div>
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
                                        <input type="button" id="UCUBERejectButtonId" value="Reject" onclick="javascript: return doClaimUpdatedByEngFormSubmit('rejectClaim');" />
                                    </s:if>
                                    <s:else>
                                        <input type="button" id="UCUBERejectButtonId" value="Reject" disabled="disabled" />
                                    </s:else>
                                    <input type="button" id="UCUBEAcknowledgeButtonId"value="Acknowledge" onclick="javascript: return doClaimUpdatedByEngFormSubmit('acknowledgeClaim')"  />
                                    <s:if test="insurerIsEngineersEnabled">
                                        <input type="button" id="UCUBEReferToEngineerButtonId"value="Refer To Engineer" onclick="javascript: return doClaimUpdatedByEngFormSubmit('referEng');" />
                                    </s:if>
                                    <input type="button" id="UCUBEClaimPendingButtonId"value="Claim Pending" onclick="javascript: return doClaimUpdatedByEngFormSubmit('pending');" />
                                </td>
                            </tr>
                        </table>
                        <div id="updateByEngMessageBox" class="action-error-msg"></div>
                    </div>
                </div>
            </div>
        </fieldset>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
    </form>
</div>