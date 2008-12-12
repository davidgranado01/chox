<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">
          
    $(document).ready(function(){
            
            
        $("#formAcknowledgeAction").validate(
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
                    max: 100.00
                },
                claimNumber:{
                    required:true
                } ,

                actionName:{
                    required:true
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
                actionName:{
                    required:"You must choose 'Reject this claim' or 'Request Invoice Data"
                }                 
            }
        });
    }); 
    


</script>




<form action="user/acknowledge.action" method="post" id="formAcknowledgeAction"
      name="formAcknowledgeAction">
    <fieldset class="x-fieldset">
        <legend>Claim Acknowledgement - Action Required</legend>
        <div>
            <s:hidden name="id" />
            <s:hidden id="actionName" name="actionName" />
            <div>
                <div class="status-info">
                    Please enter details of the claim review and decide whether to acknowledge or reject
                    the claim. Please use the 'Notes' tab in order to communicate detailed comments
                    you may have for the CHO.
                </div>
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
                            % Liability Accepted<span class="mandatory">*</span></label></td><td>
                                <input type="text" class="chox-ttxt" name="percentageLiabilityAccepted" value="<s:property value="percentageLiabilityAccepted" />"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label>
                            Claim Number<span class="mandatory">*</span></label></td><td>
                                <input type="text" class="chox-ttxt" name="claimNumber" value="<s:property value="claimNumber" />"/>
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
                                Claim Review Notes</label></td><td>
                                <textarea class="chox-tta" cols="20" rows="5" name="engineerClaimReviewNotes"><s:property value="engineerClaimReviewNotes" /></textarea>
                            </td>
                            <td>
                                <label>
                            Invoice Review Required?</label></td><td>
                                <s:checkbox name="isInvoiceReviewRequired" />
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" class="choice">
                                
                                <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
                                
                            </td>
                        </tr>                        
                        <tr>
                            <td colspan="2" class="choice">                     
                                <input class="cancel" type="submit" value="Reject"  onclick="registeAction('reject');return confirm('Are you sure you want to reject this claim?')" />
                                <input type="submit" value="Acknowledge" onclick="registeAction('accept')"  />   
                                <input type="submit" value="Refer To Engineer" onclick="registeAction('refer');"  /> 
                            </td>
                        </tr>
                        
                    </table>
                    
                    <div class="errorBox" id="ACKmessageBox"></div>
                </div>
            </div>
        </div>
    </fieldset>
</form>
