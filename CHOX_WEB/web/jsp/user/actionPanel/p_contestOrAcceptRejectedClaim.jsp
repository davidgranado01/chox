<%-- 
    Document   :p_contestOrAcceptRejectedClaim
    Created on : Dec 02, 2008, 11:39:12 AM
    Author     : Emmanuel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<script language="JavaScript">
    
    $(document).ready(function(){
            
        $("#contestOrAcceptRejectedClaim").validate(
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

<form onsubmit="return true;" action="user/contestOrAcceptRejectedClaim.action" method="post" id="contestOrAcceptRejectedClaim" name="contestOrAcceptRejectedClaim">
    <fieldset class="x-fieldset">
        <legend>Rejected Claim - Action Required</legend>
        <s:hidden name="id" />
        <s:hidden id="actionName" name="actionName" />
        <div>
            <div class="status-info">
                Please review the Insurer's notes against rejection reasoning and decide whether to accept or reject the Insurer's rejection decision.  
                Please include supporting notes on the decision made using the 'Notes' tab.
            </div>
            <div>
                <table>
                    <tr>
                        <td>
                            <div class="no-format">
                                <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
                            </div>
                        </td>
                    </tr>
                    
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
                                        % Liability Accepted<span class="mandatory">*</span></label></td><td>
                                            <input type="text" class="chox-ttxt" name="percentageLiabilityAccepted" value="<s:property value="percentageLiabilityAccepted" />"/>
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
                                            <input type="submit" value="Contest This Claim" onclick="registeAction('reject');" />
                                            <input type="submit" value="Accept Rejection Decision" class="cancel" onclick="registeAction('accept')"  />   
                                            <!--
                                <input class="cancel" type="submit" value="Reject"  onclick="registeAction('reject');return confirm('Are you sure you want to reject this claim?')" />
                                <input type="submit" value="Acknowledge" onclick="registeAction('accept')"  />   
                                <input type="submit" value="Refer To Engineer" onclick="registeAction('refer');"  /> 
                                            -->
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div class="errorBox" id="ActionPanelMessageBox"></div>
                        </td>
                    </tr> 
                    <!--                    
                    <tr> 
                        <td>
                            <input type="submit" value="Contest This Claim"  onclick="registeAction('reject');" />
                            <input type="submit" value="Accept Rejection Decision" onclick="registeAction('accept')"  />   
                        </td>
                    </tr>
                    !-->                    
                </table>
            </div>
            
        </div> 
    </fieldset>
</form>
