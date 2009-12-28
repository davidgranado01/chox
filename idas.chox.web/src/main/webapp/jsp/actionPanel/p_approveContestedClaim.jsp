<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">


    $(document).ready(function(){
        doFormValidation();
    });

    function doRejectClaim(){
        
        isFormClaimNumberInvalid("formApproveContestedAcknowledgeAction");
        registeAction('reject');
        
        if(doFormValidation().form()){
            
            if(confirm('Are you sure you want to reject this claim?')){
                
                // var sClaimNumber = $("#claimNumber").val();
                var sClaimNumber = $("form#formApproveContestedAcknowledgeAction input[name$='claimNumber']").val();
                
                if(sClaimNumber.length > 0)
                {
                    var sClaimId = $("#claimId").val();
                    var form = $("#formApproveContestedAcknowledgeAction");
                    checkAndConfirmClaimNumberDuplication(sClaimNumber, sClaimId, form);
                }
                else
                {
                    $("#formApproveContestedAcknowledgeAction").submit();
                }
            }
            
        }                
    }
    
    function isClaimNumberMandatory(){
        var sActionName = $("#actionName").val();
        if(sActionName=="reject" || sActionName=="referFNOL" || sActionName=="pending"){
            return false;
        }
        return true;
    }
    
    function liabilityMinNumber(){
        var sActionName = $("#actionName").val();
        var iMinliability = 0.01;
        if(sActionName=="reject" || sActionName=="referFNOL" || sActionName=="pending"){
            iMinliability = 0;
        }
        return iMinliability;
    }
    
    function liabilityMinNumberMsg(){
        var sActionName = $("#actionName").val();
        var iMinliabilityMsg = "'Percentage Liability Accepted' must be more than 0";
        if(sActionName=="reject" || sActionName=="referFNOL" || sActionName=="pending"){
            iMinliabilityMsg = "'Percentage Liability Accepted' must be more than or equal to 0";
        }
        return iMinliabilityMsg;   
    }
    
    function doFormValidation(){

            
        var validateFlag = $("#formApproveContestedAcknowledgeAction").validate(
        {
            errorLabelContainer: "#ActionPanelMessageBox",                
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
                },                
                actionName:{required:true},
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
                actionName:{required:"You must select action"},
                isClaimNumberValidFlag:{min:"Invalid Character used in Claim Number"} ,
                reasonOfRejectionId:{
                    required:"You must choose a 'Reason For Rejection'"
                }       
            }
            
        });
        
        return validateFlag;
    }
    
    function doSubmit(a){
        
        registeAction(a);
        isFormClaimNumberInvalid("formApproveContestedAcknowledgeAction");
        
        // $("#reasonOfRejectionId").val("");
        
        if(doFormValidation().form()){
            var sClaimNumber = $("form#formApproveContestedAcknowledgeAction input[name$='claimNumber']").val();
            var sClaimId = $("#claimId").val();
            var form = $("#formApproveContestedAcknowledgeAction");
            checkAndConfirmClaimNumberDuplication(sClaimNumber, sClaimId, form);
        }
    }
    
</script>

<form action="<%=request.getContextPath()%>/prv/approveContestedClaim.action" method="post" id="formApproveContestedAcknowledgeAction" name="formApproveContestedAcknowledgeAction">

    <fieldset class="x-fieldset">
        <legend>Contested Claim - Action Required</legend>
        <s:hidden id="claimId" name="id" />
        <s:hidden id="actionName" name="actionName" />
        <s:hidden id="isClaimNumberValidFlag" name="isClaimNumberValidFlag" value="1"/>
        <div>
            <div class="status-info">    
                Please enter details of the claim and decide whether to acknowledge the claim, refer the claim to an engineer, refer the claim to an FNOL handler, reject the claim or set the claim to pending. You can enter private notes in the 'Claim Review Notes' box and add public notes in the 'Notes' tab in order to communicate detailed comments you may have for the CHO.                
            </div>
            <div class="status-control-set">
                <table>
                    <tr> 
                        <td>                    
                            <div class="status-control-set">
                                <table class="status-table">
                                    <tr>
                                        <td>
                                            <label>
                                        Indemnity (Decimal)<span class="mandatory">*</span></label></td><td>
                                            <input type="text" class="chox-ttxt" name="indemnityAmount" value="<s:property value="indemnityAmount" />"/>
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
                                            <input type="text" class="chox-ttxt" name="percentageLiabilityAccepted" value="<s:property value="percentageLiabilityAccepted" />"/>
                                        </td>                                        
                                        
                                    </tr>
                                    <tr valign="top">
                                        <td>
                                            <label>
                                        Claim Review Notes</label></td><td colspan="3">
                                            <textarea class="chox-canote" cols="20" rows="5" name="engineerClaimReviewNotes"><s:property value="engineerClaimReviewNotes" /></textarea>
                                        </td>                                      
                                        
                                    </tr>  
                                    <tr valign="top">
                                        <td>
                                            <label>Reason for Rejection</label>
                                        </td>
                                        <td colspan="3">    
                                            <div id="ReasonOfRejectionDiv">
                                                <s:select name="reasonOfRejectionId" id="reasonOfRejectionId"
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
                                        <td colspan="4" class="choice" nowrap="true">  
                                            <input type="button" value="Reject" onclick="javascript: doRejectClaim();" />
                                            <input type="button" value="Acknowledge" onclick="javascript: doSubmit('accept')"  />
                                            <input type="button" value="Refer To Engineer" onclick="javascript: doSubmit('refer');"  /> 
                                            <input type="button" value="Refer to FNOL" onclick="javascript: doSubmit('referFNOL');" /> 
                                            <input type="button" value="Claim Pending" onclick="javascript: doSubmit('pending');" /> 
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div class="errorBox" id="ActionPanelMessageBox"></div>
                        </td>
                    </tr>
                </table>
            </div>
        </div> 
    </fieldset>
</form>
