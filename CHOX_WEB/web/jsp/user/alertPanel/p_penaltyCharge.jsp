<%-- 
    Document   : p_penaltyCharge
    Created on : 08-Jan-2009, 14:09:03
    Author     : Emmanuel
--%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">
    
    $(document).ready(function(){
        doFormValidation();
        
        $("#tPenaltyChargeAmount").keyup(function (e) {
            updateTotalToPay($("#tPenaltyChargeAmount").val());
        });
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
    


    
    function updateTotalToPay(inputValue)
    {       
        var newPenaltyCharge;
        var totalAmountToPayBeforeNewPenaltyCharge
        var totalAmountToPayAfterNewPenaltyCharge;

        if(!isNaN(inputValue)){
            newPenaltyCharge = parseFloat(inputValue) == NaN ? 0 : parseFloat(inputValue);
        }
        else
        {
            newPenaltyCharge = 0;
        }       
        totalAmountToPayBeforeNewPenaltyCharge = parseFloat($("#hvTotalAmountToPayBeforeNewPenaltyCharge").val());                  
        totalAmountToPayAfterNewPenaltyCharge = newPenaltyCharge + totalAmountToPayBeforeNewPenaltyCharge;            
        $("#totalAmountToPayAfterNewPenaltyChargeLabel").text('£' + Math.round(totalAmountToPayAfterNewPenaltyCharge*100)/100);
    }

</script>

<form onsubmit="return true;" action="user/doApplyPenaltyCharge.action" method="post" id="applyPenaltyCharge" name="applyPenaltyCharge">
    
    <s:hidden name="id" />    
    <s:hidden id="hvTotalAmountToPayBeforeNewPenaltyCharge" name="totalAmountToPayBeforeNewPenaltyCharge" />
    
    <s:if test="isShowPenaltyChargeAlert">
        <div class="warning">
            Payment for this invoice is overdue. The number of days since the invoice was created is <s:property value="invoiceIntroducedDays" />. A penalty charge may be applicable to this invoice.
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
                        <label class="chox-claim-header-text"><s:property value="totalAmountToPayBeforeNewPenaltyChargeFormatted" /></label>                            
                    </td>
                </tr>
                <tr>
                    <td>
                        <label class="chox-claim-header-label">Penalty Amount&nbsp;:&nbsp;£</label>
                        <input type="text" class="chox-ttxt" id="tPenaltyChargeAmount" name="penaltyChargeAmount" value="<s:property value="penaltyChargeAmount" />"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label class="chox-claim-header-label">Total Amount to Pay After Penalty Charge</label>
                        <label id="totalAmountToPayAfterNewPenaltyChargeLabel" class="chox-claim-header-text"><s:property value="totalAmountToPayAfterNewPenaltyChargeFormatted" />&nbsp;&nbsp;</label>
                    </td>
                </tr>
                <td>
                <tr>                        
                    <td>
                        <input type="submit" value="Apply" />
                        <s:if test="isShowPenaltyChargeAlert">
                            &nbsp;<s:checkbox name="isRemovePenaltyAlert" label="Remove From Penalty Charge Queue"/>
                            <label>Remove From Queue</label>
                        </s:if>
                    </td>
                </tr>
            </table>         
            <div class="errorBox" id="ACKmessageBox"></div>
        </div>
    </fieldset> 
    
    
    
</form>    




