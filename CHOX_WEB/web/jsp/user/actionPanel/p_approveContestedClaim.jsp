<%-- 
    Document   : p_approveContestedClaim
    Created on : 01-Dec-2008, 02:28:00
    Author     : Emmanuel
--%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">
    
    $(document).ready(function(){
            
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
                    required:true
                },                
                actionName:{required:true}
                
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
                actionName:{required:"You must select action"}
            }
            
        });
    });
    
</script>

<form onsubmit="return true;" action="user/approveContestedClaim.action" method="post" 
      id="approveContestedClaim" name="approveContestedClaim">
    <fieldset class="x-fieldset">
        <legend>Contested Claim - Action Required</legend>
        <s:hidden name="id" />
        <s:hidden id="actionName" name="actionName" />
        <div>
            <div class="status-info">
                Please review the CHO's notes against the reasoning for contesting the claim rejection and make a decision on whether to acknowledge the claim, reject the claim or refer the claim to an Engineer.
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
                                            <label>
                                        Claim Number<span class="mandatory">*</span></label></td><td>
<s:if test="claimNumber==''">                                                                                         
    <input type="text" class="chox-ttxt" name="claimNumber" value="<s:property value="claimNumber" />"/>    
</s:if>                                            
<s:else>
    <s:property value="claimNumber" />
    <input type="hidden" class="chox-ttxt" name="claimNumber" value="<s:property value="claimNumber" />"/>
</s:else>
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
                    <tr>
                        <td>
                            <div class="no-format">
                                <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>                                
                            </div>
                        </td>
                    </tr>                      
                                    <tr>
                                        <td colspan="2" class="choice">  
                                            <input type="submit" value="Reject" onclick="registeAction('reject');return confirm('Are you sure you want to reject this claim?')" />
                                            <input type="submit" value="Acknowledge" onclick="registeAction('accept')"  />
                                            <input type="submit" value="Refer To Engineer" onclick="registeAction('refer');"  /> 
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
