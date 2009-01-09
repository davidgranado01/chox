<%-- 
    Document   : p_penaltyCharge
    Created on : 08-Jan-2009, 14:09:03
    Author     : Emmanuel
--%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">
    
    $(document).ready(function(){
        doFormValidation();
    });
    
    function doFormValidation(){
        
        var validateFlag = $("#applyPenaltyCharge").validate(
        {
            errorLabelContainer: "#ACKmessageBox",                
            rules: {
                penaltyChargeAmount:{
                    required:true,
                    number:true
                }
            },
            messages: {
                penaltyChargeAmount:{
                    required:"You must supply a value for 'penaltyChargeAmount'",
                    number:"You must supply a numeric value for 'penaltyChargeAmount'"
                }              
            }
        });
        
        return validateFlag;
    }
    
    function updateTotalToPay()
    {
        
    }
</script>

<form onsubmit="return true;" action="user/doApplyPenaltyCharge.action" method="post" id="applyPenaltyCharge" name="applyPenaltyCharge">
    
    <s:hidden name="id" />
    
    <s:if test="isShowPenaltyChargeAlert">
        <div class="warning">
            Please select the 'Line of Business' in order to route the claim to the relevant handling team.
        </div> 
    </s:if>
    
    <fieldset class="x-fieldset"><legend>Apply Penalty Charge</legend>
    <s:if test="isShowPenaltyChargeAlert">
        <div class="form-container">
    </s:if>
    <s:else>
        <div style="display:none" class="form-container">
    </s:else>
            <table>
                <tr>
                    <td>
                        <label class="chox-claim-header-label">Total Amount to Pay </label>
                        <span class="highlight"><s:property value="totalAmountToPayBeforeNewPenaltyCharge" /></span>                            
                    </td>
                </tr>
                <tr>
                    <td>
                        <label class="chox-claim-header-label">Penalty Amount&nbsp;:&nbsp;£</label>
                        <input type="text" class="chox-ttxt" name="penaltyChargeAmount" onchange="javascript:updateTotalToPay();" value="<s:property value="penaltyChargeAmount" />"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label class="chox-claim-header-label">Total Amount to Pay After Penalty Charge</label>
                        <span class="highlight"><s:property value="totalAmountToPayAfterNewPenaltyCharge" />&nbsp;&nbsp;</span>
                    </td>
                </tr>
                <td>
                <tr>                        
                    <td>
                        <input type="submit" value="Apply" />
                        <s:if test="isShowPenaltyChargeAlert">
                            &nbsp;<s:checkbox name="isRemovePenaltyAlert" label="Remove From Queue"/>
                            <label>Remove From Queue</label>
                        </s:if>
                    </td>
                </tr>
            </table>         
            <div class="errorBox" id="ACKmessageBox"></div>
        </div>
    </fieldset> 
    
    
    
</form>    




