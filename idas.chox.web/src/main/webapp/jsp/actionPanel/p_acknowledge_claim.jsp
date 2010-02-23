<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(function(){
        createHelpNote();

        $("form#formAcknowledgeAction").validate(
        {
            
            errorLabelContainer: "#ACKmessageBox",
            rules: {
                claimNumber:{
                    required:true
                },
                percentageLiabilityAccepted:{
                    required:true,
                    number:true,
                    max: 100.00
                }
            },
            messages: {
                claimNumber:{
                    required:"You must supply a value for 'Claim Number'"
                },
                percentageLiabilityAccepted: {
                    required:"You must supply a value for 'Percentage Liability Accepted'",
                    number:"You must supply a numeric value for 'Percentage Liability Accepted'",
                    max:"'Percentage Liability Accepted' cannot be more than 100"
                }
            }
        });
        
        var liabilityAgreedDatePicker = ui.dateField('liabilityAgreedDate','<s:date format="dd/MM/yyyy" name="liabilityAgreedDate" />','liabilityAgreedDateDiv');



    });

    function doAcknowledgeFormSubmit(action){

        actionPanel.registerAction(action);
        doFormValidationSetup(action);

        if($("#formAcknowledgeAction").valid()){

            if(action=='rejectClaim' && !confirm('Are you sure you want to reject this claim?')){
                return;
            }

            var claimNumber = $("form#formAcknowledgeAction input[name$='claimNumber']").val();
            var claimId = $("form#formAcknowledgeAction #claimId").val();
            var form = $("form#formAcknowledgeAction");

            if(claimNumber && claimNumber.length > 0){
                checkClaimNumberDuplicationAndSubmit(claimNumber, claimId, form);
            }else{
                form.submit();
            }
        }
    }

    function doFormValidationSetup(action){

        // REMOVE ADDED VALIDATION
        $("form#formAcknowledgeAction #claimNumber").rules("remove");
        $("form#formAcknowledgeAction #reasonOfRejectionId").rules("remove");
        $("form#formAcknowledgeAction #percentageLiabilityAccepted").rules("remove", "min");

        // ADD NEW VALIDATION PER SUBMIT TYPE
        if(action=='rejectClaim'){

            $("form#formAcknowledgeAction #reasonOfRejectionId").rules("add", {
                required: true,
                messages: {required: "You must choose a 'Reason For Rejection'"}
            });

            addValidationRulePercentageLiabilityAccepted(0);

        }else if(action=='acknowledgeClaim'){

            $("form#formAcknowledgeAction #reasonOfRejectionId").val("");
            addValidationRuleClaimNumber();
            if ( ! isLiabilityDisputed()){
                addValidationRulePercentageLiabilityAccepted(0.01);
            }
            console.log('ack setup');

        }else if(action=='referEng'){

            $("form#formAcknowledgeAction #reasonOfRejectionId").val("");
            addValidationRuleClaimNumber();
            addValidationRulePercentageLiabilityAccepted(0.01);

        }else if(action=='referFNOL'){

            $("form#formAcknowledgeAction #reasonOfRejectionId").val("");
            addValidationRulePercentageLiabilityAccepted(0);

        }else if(action=='pending'){

            $("form#formAcknowledgeAction #reasonOfRejectionId").val("");
            addValidationRulePercentageLiabilityAccepted(0);

        }

    }

    function addValidationRuleClaimNumber(){
        $("form#formAcknowledgeAction #claimNumber").rules("add", {
            required: true,
            messages: {required: "You must supply a value for 'Claim Number'"}
        });
    }

    function addValidationRulePercentageLiabilityAccepted(minValue){
        $("form#formAcknowledgeAction #percentageLiabilityAccepted").rules("add", {
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

        ajax.loadJson(url, param, function(data){
            
            if(data.result && data.resultType=='YesNo'){
                if(confirm(data.result))
                {
                    form.submit();
                }
            }
            else form.submit();
        });

    }

    function isLiabilityDisputed(){
        var liabilityStatus = $("#liabilityStatus").val();
        if ( liabilityStatus == 2 || liabilityStatus == 3 || liabilityStatus ==4 ){
            return true;
        }
        return false;
    }

    function onLiabilityStatusSelectionChange(){
        var liabilityStatus = $("#liabilityStatus").val();
        console.log(liabilityStatus);
        if ( liabilityStatus == 1 || liabilityStatus == 5 || liabilityStatus == 6){
            $("#percentageLiabilityAccepted").val(100.00);
            $("#percentageLiabilityCho").val(0.00);
            Ext.getCmp('liabilityAgreedDate').setValue(new Date());
        }else{
            Ext.getCmp('liabilityAgreedDate').setValue("");
        }

    }
    
    /*
    function showLiabilityStatusDropDown() {
        var target = "#liabilityStatusDropDownDiv";
        var url = "<%= request.getContextPath()%>/prv/p/liabilityStatusDropDownAction.action";
        ajax.loadHtml(url,function(data){
            $(target).html(data);
        });
    }
    */
    function createHelpNote(){
        var note = $('#liabilityStatusHelpNotes').html();        
        new Ext.ToolTip({
                target: 'liabilityStatusHelp',
                html: note,
                title: 'Liability Status',
                autoHide: false,
                closable: true,
                draggable:true
            });

        Ext.QuickTips.init();
    }

</script>
<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="formAcknowledgeAction" name="formAcknowledgeAction">

        <fieldset class="x-fieldset">
            <legend>Claim Acknowledgement - Action Required</legend>
            <div>
                <s:hidden id="claimId" name="id" />
                <s:hidden id="name" name="name" />
                <input name="currentVersion" type="hidden" value="<s:property value="version" />" />
                <s:hidden id="isClaimNumberValidFlag" name="isClaimNumberValidFlag" value="1"/>
                <div>
                    <div class="status-info">
                        Please enter details of the claim and decide whether to acknowledge the claim, refer the claim to an engineer, refer the claim to an FNOL handler, reject the claim or set the claim to pending. You can enter private notes in the 'Claim Review Notes' box and add public notes in the 'Notes' tab in order to communicate detailed comments you may have for the CHO.
                    </div>
                    <div class="status-control-set">
                        <table class="status-table">

                            <tr>
                                <td>
                                    <label>Claim Number</label>
                                </td>
                                <td>
                                    <input type="text" class="chox-ttxt" id="claimNumber" name="claimNumber" value="<s:property value="claimNumber" />"/>
                                </td>
                                <td colspan="2">
                                    <label></label>
                                </td>

                            </tr>

                            <tr>
                                <td width="20%">
                                    <label>Liability Status
                                        <span class="mandatory">*</span> 
                                    </label>
                                    <img src="../images/help.png" id="liabilityStatusHelp" alt=""/>
                                </td>
                                <!--
                                <td><div id="liabilityStatusDropDownDiv" ></div></td>
                                -->
                                <td>
                                    <s:select
                                        id="liabilityStatus"
                                        name="liabilityStatus"
                                        list="liabilityStatusDropDownMap"
                                        emptyOption="false"
                                        onchange="javascript:onLiabilityStatusSelectionChange()"
                                        tooltip="Update Liability">
                                    </s:select>
                                </td>
                                <td colspan="2">
                                    <label></label>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <label>
                                        Liability Percentage Agreed(Insurer)</label>

                                </td>
                                <td>
                                        <input type="text" class="chox-ttxt" name="percentageLiabilityAccepted" id="percentageLiabilityAccepted" value="<s:property value="percentageLiabilityAccepted" />"/>
                                </td>
                                <td>
                                    <label>
                                        Liability Percentage Agreed(CHO)</label>
                                </td>
                                <td>
                                        <input type="text" class="chox-ttxt" name="percentageLiabilityCho" id="percentageLiabilityCho" value="<s:property value="percentageLiabilityCho" />"/>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <label>Date Liability Agreed</label>
                                </td>
                                <td><div id="liabilityAgreedDateDiv"></div></td>
                                <td colspan="2">
                                    <label></label>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <label>
                                        Indemnity Value</label>
                                </td>
                                <td>
                                    <input type="text" class="chox-ttxt" name="indemnityAmount" value="<s:property value="indemnityAmount" />"/>
                                </td>
                                <td colspan="2">
                                    <label></label>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>
                                        Invoice Review Required?</label>
                                </td>
                                <td>
                                    <s:checkbox name="isInvoiceReviewRequired" />
                                </td>
                                <td colspan="2"></td>
                            </tr>
                            <tr valign="top">
                                <td>
                                    <label>Claim Review Notes</label>
                                </td>
                                <td colspan="3">
                                    <textarea class="chox-canote" cols="80" rows="5" name="engineerClaimReviewNotes"><s:property value="engineerClaimReviewNotes" /></textarea>
                                </td>
                            </tr>
                            <tr valign="top">
                                <td>
                                    <label>Reason for Rejection</label>
                                </td>
                                <td colspan="3">
                                    <div id="ReasonOfRejectionDiv">
                                        <s:select
                                            name="reasonOfRejectionId"
                                            id="reasonOfRejectionId"
                                            list="reasonOfClaimRejections"
                                            listKey="id"
                                            listValue="name"
                                            headerKey=""
                                            headerValue="N/A"
                                            emptyOption="false"></s:select>
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
                                    <input type="button" value="Reject" onclick="doAcknowledgeFormSubmit('rejectClaim');" />
                                    <input type="button" value="Acknowledge" onclick="doAcknowledgeFormSubmit('acknowledgeClaim')"  />
                                    <input type="button" value="Refer To Engineer" onclick="doAcknowledgeFormSubmit('referEng');" />
                                    <input type="button" value="Refer to FNOL" onclick="doAcknowledgeFormSubmit('referFNOL');" />
                                    <input type="button" value="Claim Pending" onclick="doAcknowledgeFormSubmit('pending');" />
                                </td>
                            </tr>
                        </table>
                        <div id="ACKmessageBox" class="action-error-msg"></div>
                        <div id="liabilityStatusHelpNotes" style="display: none">
                            <table cellpadding='0' cellspacing='0' border='0' class='remark-table' >
                                <tr>
                                    <th width='28%'><b>Status</b></th><th width='70%'><b>Description</b></th>
                                </tr>
                                <tr>
                                    <td>Full Liability Accepted</td>
                                    <td>Indicates that the Third party Insurer is accepting 100% liability for the claim.</td>
                                </tr>
                                <tr>
                                    <td>Liability Disputed </td>
                                    <td>Indicates that liability is in dispute with the CHO and negotiations are taking place.</td>
                                </tr>
                                <tr>
                                    <td>Liability Unknown</td>
                                    <td>Indicates that the Third Party Insurer has insufficient information available to make a comment on liability, for example it is a new claim, there has been no contact from/with Policyholder, or waiting to obtain Policyholder accident report form</td>
                                </tr>
                                <tr>
                                    <td>Liability Repudiated</td>
                                    <td>Indicates that the Third Party Insurer is denying all (zero) liability for the claim.</td>
                                </tr>
                                <tr>
                                    <td>Liability Split</td>
                                    <td>Indicates that liability has been agreed on a split bases with the CHO accepting partial liability.</td>
                                </tr>
                                <tr>
                                    <td>Proceed Without Prejudice</td>
                                    <td>Indicates indemnity is not granted but a decision has been made to make a payment anyway</td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </fieldset>
    </form>
</div>
                             
