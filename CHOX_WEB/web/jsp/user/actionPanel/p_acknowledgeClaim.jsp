<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">

                function doRejectClaim(){
                    
                    isClaimNumberInvalid();
                    registeAction('reject');
                    
                    if(doFormValidation().form()){
                        if(!confirm('Are you sure you want to reject this claim?')){
                            return false;
                        }
                    }else{
                        return false;
                    }
                    
                    return true;
                }
                
                function isClaimNumberMandatory(){
                    var sActionName = $("#actionName").val();
                    
                    if(sActionName=="reject" || sActionName=="referFNOL"){
                        return false;
                    }
                    return true;
                }
                
                $(document).ready(function(){
                    
                    doFormValidation();
                    
                });
                
                function isRejected(){
                    var sActionName = $("#actionName").val();
                    if(sActionName=="reject"){
                        return true;
                    }
                    return false;
                }
                
                function liabilityMinNumber(){
                    var sActionName = $("#actionName").val();
                    var iMinliability = 0.01;
                    if(sActionName=="reject" || sActionName=="referFNOL"){
                        iMinliability = 0;
                    }
                    return iMinliability;
                }
                
                function liabilityMinNumberMsg(){
                    var sActionName = $("#actionName").val();
                    var iMinliabilityMsg = "'Percentage Liability Accepted' must be more than 0";
                    if(sActionName=="reject" || sActionName=="referFNOL"){
                        iMinliabilityMsg = "'Percentage Liability Accepted' must be more than or equal to 0";
                    }
                    return iMinliabilityMsg;   
                }
                
                function isClaimNumberInvalid(){
                    
                    $("#isClaimNumberValidFlag").val("1");
                    
                    var sClaimNumber = $("#claimNumber").val();
                    
                    if(isSpecialCharacterExist(sClaimNumber)){
                        
                        $("#isClaimNumberValidFlag").val("0");
                    }
                    
                }
    
                function isSpecialCharacterExist(strClaimNumber){
                    
                    if(strClaimNumber.length>0){
                        
                        var iChars = "!£$%^&*+=-_{[}]#~'@;:/?.>,<"+'"';
                        
                        for (var i = 0; i < strClaimNumber.length; i++) {
                            if (iChars.indexOf(strClaimNumber.charAt(i)) != -1) {
                                return true;
                            }
                        }             
                    }
                    return false;
                }
                
                function doFormValidation(){
                    
                    var validateFlag = $("#formAcknowledgeAction").validate(
                    {
                        errorLabelContainer: "#ACKmessageBox",  
                        rules: {
                            indemnityAmount:{
                                required:true,
                                number:true
                            },
                            percentageLiabilityAccepted:{
                                required:true,
                                number:true,
                                max: 100.00,
                                min:liabilityMinNumber
                            },
                            claimNumber:{
                                required:isClaimNumberMandatory
                            } ,
                            actionName:{
                                required:true
                            },
                            isClaimNumberValidFlag:{
                                min:1
                            },
                            reasonOfRejectionId:{
                                required:isRejected
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
                                max:"'Percentage Liability Accepted' cannot be more than 100",
                                min:liabilityMinNumberMsg
                            },
                            claimNumber: {
                                required:"You must supply a value for 'Claim Number'"
                            },
                            actionName:{
                                required:"You must choose 'Reject this claim' or 'Request Invoice Data"
                            },
                            isClaimNumberValidFlag:{
                                min:"Invalid Character used in Claim Number"
                            } ,
                            reasonOfRejectionId:{
                                required:"You must choose a 'Reason For Rejection'"
                            }               
                        }
                    });
                    
                    return validateFlag;
                }
                
                function doSubmit(a){
                    registeAction(a);
                    isClaimNumberInvalid();
                    
                    $("#reasonOfRejectionId").val("");
                    
                    if(doFormValidation().form()){
                        checkAndConfirClaimNumberDuplication();                       
                    }                    
                }
                
                function checkAndConfirClaimNumberDuplication()
                {
                    var result = true;
                    var sClaimNumber = $("#claimNumber").val();
                    var sClaimId = $("#claimId").val();
                    if(sClaimNumber && sClaimNumber != null)
                    {
                        
                        $.get("checkIsClaimNumberDuplicated.action", { claimNumber: sClaimNumber, claimId: sClaimId },
                        function(data){
                            if(data.trim()== "yes"){
                                if(confirm("The claim number you have supplied is already associated with another claim(s). Do you wish to continue?"))
                                {
                                    $("#formAcknowledgeAction").submit();
                                }
                            }
                            else{
                                $("#formAcknowledgeAction").submit();
                            }                            
                        });
                    
                }   
                    return result;
                }
    
</script>

<form action="user/acknowledge.action" method="post" id="formAcknowledgeAction" name="formAcknowledgeAction">
    <fieldset class="x-fieldset">
        <legend>Claim Acknowledgement - Action Required</legend>
        <div>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="actionName" name="actionName" />
            <s:hidden id="isClaimNumberValidFlag" name="isClaimNumberValidFlag" value="1"/>
            <div>
                <div class="status-info">
                    Please enter details of the claim and decide whether to acknowledge, refer to an engineer, refer to FNOL or reject the claim. You can enter private notes in the 'Claim Review Notes' box and add public notes in the 'Notes' tab in order to communicate detailed comments you may have for the CHO.
                </div>
                <div class="status-control-set">
                    <table class="status-table">
<tr>
    <td width="20%">
        <label>
        Indemnity (Decimal)<span class="mandatory">*</span></label>
    </td><td>
        <input type="text" class="chox-ttxt" name="indemnityAmount" value="<s:property value="indemnityAmount" />"/>
    </td>
    <td>
        <label>
        Invoice Review Required?</label>
    </td><td>
        <s:checkbox name="isInvoiceReviewRequired" />
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
        <input type="text" class="chox-ttxt" name="percentageLiabilityAccepted" value="<s:property value="percentageLiabilityAccepted" />"/>
    </td>
</tr>
<tr valign="top">
    <td>
        <label>Claim Review Notes</label>
    </td>
    <td colspan="3">
        <textarea class="chox-canote" cols="80" rows="5" name="engineerClaimReviewNotes"><s:property value="engineerClaimReviewNotes" /></textarea>
    </td>
</tr>

<div id="dReasonOfRejection">

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

</div>

<tr>
    <td colspan="4">
        <div class="no-format">
            <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
        </div>
    </td>
</tr>                   
                        <tr>
                            <td colspan="4" class="choice" nowrap>
                                <input type="button" value="Reject" onclick="javascript: doRejectClaim();" />
                                <input type="button" value="Acknowledge" onclick="javascript: doSubmit('accept')"  />   
                                <input type="button" value="Refer To Engineer" onclick="javascript: doSubmit('refer');" /> 
                                <input type="button" value="Refer to FNOL" onclick="javascript: doSubmit('referFNOL');" /> 
                                <input type="button" value="Claim Pending" onclick="javascript: doSubmit('pending');" /> 
                            </td>
                        </tr>
                    </table>
                    
                    <div class="errorBox" id="ACKmessageBox"></div>
                    
                </div>
            </div>
        </div>
    </fieldset>
</form>
