<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(function(){
        createHelpNote();
        console.log($("#liabilityStatus").val());
        $("#percentageLiabilityAccepted").blur(function(){
            var liabilityStatus = $("#liabilityStatus").val();
            var ins = parseFloat($("#percentageLiabilityAccepted").val());
            if (liabilityStatus == 5 && !isNaN(ins)&& ins > 0 && ins <=100 ){
                ins = ins.toFixed(2);
                $("#percentageLiabilityAccepted").val(ins);
                var cho = (100.00-ins);
                cho = cho.toFixed(2);
                $("#percentageLiabilityCho").val(cho);
            }
        });

        $.validator.addMethod(
            "checkTotal",
            function(value, element, para) {
                if (isLiabilityAccepted()){
                    var total = parseFloat($("#fPercentageLiabilityAccepted").val()) + parseFloat($("#fPercentageLiabilityCho").val());
                    if (total > 100) {
                        return false;
                    }
                }
                return true;
            }
        );

        $.validator.addMethod(
            "checkRepudiated",
            function(value, element) {
                if (value != 4){
                    return false;
                }
                return true;
            }
        );
        $.validator.addMethod(
            "checkNotRepudiated",
            function(value, element) {
                if (value == 4){
                    return false;
                }
                return true;
            }
        );
        
        $("form#formAcknowledgeAction").validate(
        {
            
            errorLabelContainer: "#ACKmessageBox",
            rules: {
                claimNumber:{
                    required:true
                },
                percentageLiabilityAccepted:{
                    required:function(element){
                        return isLiabilityAccepted();
                    },
                    number:true,
                    max: 100.00,
                    checkTotal:true
                },
                percentageLiabilityCho:{
                    required:function(element){
                        return isLiabilityAccepted();
                    },
                    number:true,
                    max: 100.00
                },
                liabilityStatus:{
                    range:[1,5],
                    checkRepudiated:"Liability Status should be set to 'Liability Repudiated' to reject claim",
                    checkNotRepudiated:"Liability Status can only be set to 'Liability Repudiated' when rejecting claims"
                }
            },
            messages: {
                claimNumber:{
                    required:"You must supply a value for 'Claim Number'"
                },
                percentageLiabilityAccepted: {
                    required:"You must supply a value for 'Percentage Liability Accepted'",
                    number:"You must supply a numeric value for 'Percentage Liability Agreed'",
                    max:"'Percentage Liability Accepted' cannot be more than 100",
                    checkTotal:"Sum of percantage liability fields must not exceed 100."
                },
                percentageLiabilityCho: {
                    required:"You must supply a value for 'Percentage Liability Accepted'",
                    number:"You must supply a numeric value for 'Percentage Liability Agreed'",
                    max:"'Percentage Liability CHO' cannot be more than 100"
                },
                liabilityStatus:{                    
                    range:"You must select liability status",
                    checkRepudiated:"Liability Status should be set to 'Liability Repudiated' to reject claim",
                    checkNotRepudiated:"Liability Status can only be set to 'Liability Repudiated' when rejecting claims"
                    
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
        $("form#formAcknowledgeAction #liabilityStatus").rules("remove","range");
        $("form#formAcknowledgeAction #claimNumber").rules("remove");
        $("form#formAcknowledgeAction #reasonOfRejectionId").rules("remove");
        $("form#formAcknowledgeAction #percentageLiabilityAccepted").rules("remove", "min");
        //$("form#formAcknowledgeAction #percentageLiabilityAccepted").rules("remove", "required");
        $("form#formAcknowledgeAction #liabilityStatus").rules("remove", "checkRepudiated");
        $("form#formAcknowledgeAction #liabilityStatus").rules("remove", "checkNotRepudiated");

        // ADD NEW VALIDATION PER SUBMIT TYPE
        if(action=='rejectClaim'){
            $("form#formAcknowledgeAction #liabilityStatus").rules("add","checkRepudiated");

            $("form#formAcknowledgeAction #reasonOfRejectionId").rules("add", {
                required: true,
                messages: {required: "You must choose a 'Reason For Rejection'"}
            });

            //addValidationRulePercentageLiabilityAccepted(0);

        }else if(action=='acknowledgeClaim'){
            $("form#formAcknowledgeAction #liabilityStatus").rules("add","range");
            $("form#formAcknowledgeAction #liabilityStatus").rules("add","checkNotRepudiated");
            $("form#formAcknowledgeAction #reasonOfRejectionId").val("");
            addValidationRuleClaimNumber();
            if ( ! isLiabilityDisputed()){
                addValidationRulePercentageLiabilityAccepted(0.01);
            }
 

        }else if(action=='referEng'){
            $("form#formAcknowledgeAction #liabilityStatus").rules("add","checkNotRepudiated");
            $("form#formAcknowledgeAction #reasonOfRejectionId").val("");
            addValidationRuleClaimNumber();
            //addValidationRulePercentageLiabilityAccepted(0.01);

        }else if(action=='referFNOL'){
            $("form#formAcknowledgeAction #liabilityStatus").rules("add","checkNotRepudiated");
            $("form#formAcknowledgeAction #reasonOfRejectionId").val("");
            //addValidationRulePercentageLiabilityAccepted(0);

        }else if(action=='pending'){
            $("form#formAcknowledgeAction #liabilityStatus").rules("add","checkNotRepudiated");
            $("form#formAcknowledgeAction #reasonOfRejectionId").val("");
            //addValidationRulePercentageLiabilityAccepted(0);

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

    function isLiabilityAccepted(){
        var liabilityStatus = $("#liabilityStatus").val();
        if ( liabilityStatus == 1 || liabilityStatus == 5 || liabilityStatus == 6){
            return true;
        }
        return false;
    }
    function isLiabilityDisputed(){
        var liabilityStatus = $("#liabilityStatus").val();
        if ( liabilityStatus == 2 || liabilityStatus == 3 || liabilityStatus ==4 ){
            return true;
        }
        return false;
    }

    function isLiabilityRepudiated(){
        var liabilityStatus = $("#liabilityStatus").val();
        if ( liabilityStatus == 4){
            return true;
        }
        return false;
    }

    function onLiabilityStatusSelectionChange(){
        
        var liabilityStatus = $("#liabilityStatus").val();

        if ( isLiabilityAccepted()){
            if ( liabilityStatus != 5){
                $("#percentageLiabilityAccepted").val((100.00).toFixed(2));
                $("#percentageLiabilityCho").val((0.00).toFixed(2));
            }
            Ext.getCmp('liabilityAgreedDate').setValue(new Date());
        }else{
            $("#percentageLiabilityAccepted").val("");
            $("#percentageLiabilityCho").val("");

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
                        Please enter details of the claim and decide whether to acknowledge, refer to an engineer or reject the claim. You can enter private notes in the 'Claim Review Notes' box and add public notes in the 'Notes' tab in order to communicate detailed comments you may have for the CHO.
                    </div>
                    <div class="status-info">
                        This claim has been pending for <s:property value="daysInStatus" /> day(s).
                    </div>
                    <div class="status-control-set">
                        <table class="status-table">

                            <tr>
                                <td>
                                    <label>Claim Number <span class="mandatory">*</span></label>
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
                                </td>
                            </tr>
                        </table>
                        <div id="ACKmessageBox" class="action-error-msg"></div>

                    </div>
                </div>
            </div>
        </fieldset>
         <div id="liabilityStatusHelpNotes" style="display: none">
            <table  cellpadding='0' cellspacing='0' border='0' class='remark-table' >
                <tr>
                    <th width='28%'><b>Status</b></th><th width='70%'><b>Description</b></th>
                </tr>
                <tr valign="top">
                    <td valign="top">Full Liability Accepted</td>
                    <td>Indicates that the Third party Insurer is accepting 100% liability for the claim.<br/></td>
                </tr>
                <tr valign="top">
                    <td>Liability Disputed </td>
                    <td>Indicates that liability is in dispute with the CHO and negotiations are taking place.<br/></td>
                </tr>
                <tr valign="top">
                    <td>Liability Unknown</td>
                    <td>Indicates that the Third Party Insurer has insufficient information available to make a comment on liability, for example it is a new claim, there has been no contact from/with Policyholder, or waiting to obtain Policyholder accident report form<br/></td>
                </tr>
                <tr valign="top">
                    <td>Liability Repudiated</td>
                    <td>Indicates that the Third Party Insurer is denying all (zero) liability for the claim.<br/></td>
                </tr>
                <tr valign="top">
                    <td>Liability Split</td>
                    <td>Indicates that liability has been agreed on a split bases with the CHO accepting partial liability.<br/></td>
                </tr>
                <tr valign="top">
                    <td>Proceed Without Prejudice</td>
                    <td>Indicates indemnity is not granted but a decision has been made to make a payment anyway<br/></td>
                </tr>
            </table>
        </div>
    </form>
</div>
                             
