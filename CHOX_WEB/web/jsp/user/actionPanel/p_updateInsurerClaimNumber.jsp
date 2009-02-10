<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">
    
    $(document).ready(function(){
  
        doFormValidationClaimNumber();
        
    });
    
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
    
    function doFormValidationClaimNumber(){
                
        var validateFlag = $("#formUpdateInsurerClaimNumber").validate(
        {
            errorLabelContainer: "#ACKmessageBox",  
            rules: {
                claimNumber:{
                    required:true
                } ,
                isClaimNumberValidFlag:{
                    min:1
                }
            },
            messages: {
                claimNumber: {
                    required:"You must supply a value for 'Claim Number'"
                },
                isClaimNumberValidFlag:{
                    min:"Invalid Character used in Claim Number"
                }              
            }
        });
        
        return validateFlag;
    }
    
     
    function doSubmitClaimNumber(a){
                    registeAction(a);
                    isClaimNumberInvalid();
                    
                    if(doFormValidationClaimNumber().form()){
                        checkAndConfirClaimNumberDuplication();                       
                    }                    
                }
                
        function checkAndConfirClaimNumberDuplication()
        {
            var sClaimNumber = $("#claimNumber").val();
            var sClaimId = $("#claimId").val();
            if(sClaimNumber && sClaimNumber != null)
            {
                
                $.get("checkIsClaimNumberDuplicated.action", { claimNumber: sClaimNumber, claimId: sClaimId },
                function(data){
                    if(data.trim()== "yes"){
                        if(confirm("The claim number you have supplied is already associated with another claim(s). Do you wish to continue?"))
                        {
                            $("#formUpdateInsurerClaimNumber").submit();
                        }
                    }
                    else{
                        $("#formUpdateInsurerClaimNumber").submit();
                    }                            
                });
                
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
            <s:hidden id="actionName" name="actionName" />
            <s:hidden id="isClaimNumberValidFlag" name="isClaimNumberValidFlag" value="1"/>        
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
                <div class="errorBox" id="ACKmessageBox"></div>
    </fieldset>
</form>
