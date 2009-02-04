<%-- 
    Document   : p_approveContestedClaim
    Created on : 01-Dec-2008, 02:28:00
    Author     : Emmanuel
--%>

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
    
    $(document).ready(function(){
        doFormValidation();
    });
    
    function isClaimNumberMandatory(){
        var sActionName = $("#actionName").val();
        if(sActionName=="reject" || sActionName=="referFNOL"){
            return false;
        }
        return true;
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
    
    function isRejected(){
        var sActionName = $("#actionName").val();
        if(sActionName=="reject"){
            return true;
        }
        return false;
    }
    
    function doFormValidation(){

            
        var validateFlag = $("#approveContestedClaim").validate(
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
                    max: 100.00
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
                    max:"'Percentage Liability Accepted' cannot be more than 100"
                },
                claimNumber: {
                    required:"You must supply a value for 'Claim Number'"
                },
                actionName:{required:"You must select action"},
                isClaimNumberValidFlag:{min:"Invalid Character used in Claim Number"} ,
                reasonOfRejectionId:{
                    required:"You must choose 'Reason For Rejection'"
                }       
            }
            
        });
        
        return validateFlag;
    }
    
    function doSubmit(a){
        
        registeAction(a);

        // SET REASON OF REJECTION IS EMPTY
        $("#reasonOfRejectionId").val("");
        
        isClaimNumberInvalid();
        if(!doFormValidation().form()){
            return false;
        }
        return true;
        
    }    
</script>

<form onsubmit="return true;" action="user/approveContestedClaim.action" method="post" 
      id="approveContestedClaim" name="approveContestedClaim">
    <fieldset class="x-fieldset">
        <legend>Contested Claim - Action Required</legend>
        <s:hidden name="id" />
        <s:hidden id="actionName" name="actionName" />
        <s:hidden id="isClaimNumberValidFlag" name="isClaimNumberValidFlag" value="1"/>
        <div>
            <div class="status-info">
                Please review the CHO's notes against the reason for contesting the claim rejection and make a decision on whether to acknowledge the claim, reject the claim, refer the claim to FNOL or refer the claim to an Engineer.
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
                                            <input type="submit" value="Reject" onclick="javascript: return doRejectClaim();" />
                                            <input type="submit" value="Acknowledge" onclick="javascript: return doSubmit('accept')"  />
                                            <input type="submit" value="Refer To Engineer" onclick="javascript: return doSubmit('refer');"  /> 
                                            <input type="submit" value="Refer to FNOL" onclick="javascript: return doSubmit('referFNOL');" /> 
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
