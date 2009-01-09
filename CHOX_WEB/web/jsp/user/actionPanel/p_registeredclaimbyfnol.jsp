<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">

    function doRejectClaim(){
        
        registeAction('rejectFNOL');
        if(doFormValidation().form()){
            if(!confirm('Are you sure you want to reject this claim?')){
                return false;
            }
        }else{
            return false;
        }
    }
    
    function doNoteValidation(){
        var noteText = $("#actionName").val();
        
        if(noteText=="rejectFNOL"){
            return true;
        }

        return false;
    }
    
    $(document).ready(function(){
        doFormValidation();
    });
    
    function doFormValidation(){
        
        return $("#formRegisterFNOL").validate(
        {
            errorLabelContainer: "#errorMessageBox",                
            rules: {
                actionName:{
                    required:true
                },
                reasonForRejection:{
                    required:doNoteValidation
                }
            },
            messages: {
                actionName:{
                    required:"You must choose 'Register FNOL' or 'Reject"
                },
                reasonForRejection:{
                    required:"You must supply a value for 'Reason for Rejection"
                }                 
            }
        });
    }
    
</script>

<form action="user/registerFNOL.action" method="post" id="formRegisterFNOL" name="formRegisterFNOL">
    <fieldset class="x-fieldset">
        <legend>First Notification of Loss - Action Required</legend>
        <div>
            <s:hidden name="id" />
            <s:hidden id="actionName" name="actionName" />
            <div>
                <div class="status-info">
                    Please review the claim details using the 'Claim Details' tab, if and when the claim has been registered please enter the assigned Claim Number and click on the 'Return to Claims Handler' button to return the claim to the Claims Handler for review. If the claim has not been registered, please enter the reason why in the 'Reason for Rejection' field before clicking on the 'Return to Claims Handler' button.
                </div>
                <div class="status-control-set">
                    <table class="status-table">
                        <tr>
                            <td><label>Claim Number</label></td>
                            <td><input type="text" class="chox-ttxt" name="claimNumber" value="<s:property value="claimNumber" />" maxlength=100/></td>
                        </tr>
                        <tr valign="top">
                            <td><label>Reason for Rejection (If applicable)</label></td>
                            <td><textarea class="chox-canote" cols="80" rows="5" name="reasonForRejection" id="reasonForRejection"><s:property value="reasonForRejection" /></textarea></td>
                        </tr>                        
                        <tr>
                            <td colspan="2">
                                <div class="no-format">
                                    <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" class="choice" nowrap>                     
                                <input type="submit" value="Return to Claims Handler" onclick="registeAction('registerFNOL')"  />
                            </td>
                        </tr>
                    </table>
                    <div class="errorBox" id="errorMessageBox"></div>
                </div>
            </div>
        </div>
    </fieldset>
</form>
