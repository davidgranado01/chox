<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">
    
    $(document).ready(function(){
  
        doFormValidationClaimNumber();
        
    });
    
    function isUpdateInsurerClaimNumberInvalid(){
        $("#isUpdateInsurerClaimNumberValidFlag").val("1");
        var sClaimNumber = $("#claimNumber").val();
        if(isSpecialCharacterExist(sClaimNumber)){
            
            $("#isUpdateInsurerClaimNumberValidFlag").val("0");
        }
    }
    
    function doFormValidationClaimNumber(){
                
        var validateFlag = $("#formUpdateInsurerClaimNumber").validate(
        {
            errorLabelContainer: "#ACKmUpdateInsurerClaimNumbermessageBox",
            rules: {
                claimNumber:{
                    required:true
                } ,
                isUpdateInsurerClaimNumberValidFlag:{
                    min:1
                }
            },
            messages: {
                claimNumber: {
                    required:"You must supply a value for 'Claim Number'"
                },
                isUpdateInsurerClaimNumberValidFlag:{
                    min:"Invalid Character used in Claim Number"
                }              
            }
        });
        
        return validateFlag;
    }
    
    function doSubmitClaimNumber(a){

        registeAction(a);
        isUpdateInsurerClaimNumberInvalid();

        if(doFormValidationClaimNumber().form()){

            var sClaimNumber = $("#claimNumber").val();
            var sClaimId = $("#claimId").val();
            var form = $("#formUpdateInsurerClaimNumber");
            checkAndConfirmClaimNumberDuplication(sClaimNumber,sClaimId,form);
        }
    }
       
</script>

<form 
    onsubmit="return true;" 
    action="user/updateInsurerClaimNumber.action" 
    method="post" 
    id="formUpdateInsurerClaimNumber" 
    name="formUpdateInsurerClaimNumber">
    <fieldset class="x-fieldset">
        <s:hidden id="claimId" name="id" />
        <s:hidden id="isUpdateInsurerClaimNumberValidFlag" name="isUpdateInsurerClaimNumberValidFlag" value="1"/>
        <legend>Insurer Claim Number</legend>
        <div class="status-control-set">
            <table class="status-table">
                <tr>
                    <td>
                    <label>Claim Number</label></td><td nowrap>
                        <input type="text" class="chox-ttxt" id="claimNumber" name="claimNumber" value="<s:property value="claimNumber" />"/>
                        <input type="button" value="Update Claim Number" onclick="javascript: doSubmitClaimNumber('updateClaimNumber');" /> 
                    </td>
                    <td></td><td></td>
                </tr>
            </table>
        </div>
        <div class="errorBox" id="ACKmUpdateInsurerClaimNumbermessageBox"></div>
    </fieldset>
</form>
