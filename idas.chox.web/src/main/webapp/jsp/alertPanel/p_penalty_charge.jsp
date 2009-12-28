<%-- 
    Document   : p_penaltyCharge
    Created on : 08-Jan-2009, 14:09:03
    Author     : Emmanuel
--%>

<%@ taglib uri="/struts-tags" prefix="s" %>


<script type="text/javascript">

    $(function(){

        $("#tPenaltyChargeAmount").keyup(function (e) {
            updateTotalToPay($("#tPenaltyChargeAmount").val());
        });
        
        var form = $("form#applyPenaltyCharge");

        form.validate(
        {
            errorLabelContainer: "#PenaltyChargeBox",
            rules: {
                penaltyChargeAmount:{
                    required:true,
                    number:true
                }
            },
            messages: {
                penaltyChargeAmount:{
                    required:"You must supply a value for 'Penalty Charge Amount'",
                    number:"You must supply a numeric value for 'Penalty Charge Amount'"
                }
            }
        });

        //ui.ajaxForm(form);

    });


    /*
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
                    required:"You must supply a value for 'Penalty Charge Amount'",
                    number:"You must supply a numeric value for 'Penalty Charge Amount'"
                }              
            }
        });
        
        return validateFlag;
    }
    */
   
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

<form action="<%= request.getContextPath()%>/prv/doApplyPenaltyCharge.action" method="post" id="applyPenaltyCharge" name="applyPenaltyCharge">

<s:hidden name="id" />
<s:hidden id="hvTotalAmountToPayBeforeNewPenaltyCharge" name="totalAmountToPayBeforeNewPenaltyCharge" />

<fieldset class="x-fieldset"><legend>Apply Penalty Charge</legend>
    
    <div class="status-warning">
        Payment for this invoice is overdue. The number of days since the invoice was created is <s:property value="invoiceIntroducedDays" />. A penalty charge may be applicable to this invoice.
    </div> 

    <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td width="30px"><label>Total Amount to Pay </label></td>
        <td width="70%"><s:property value="totalAmountToPayBeforeNewPenaltyChargeFormatted" /></td>
    </tr>
    <tr>
        <td><label>Penalty Amount</label></td>
        <td>£&nbsp;<input type="text" class="chox-ttxt" id="tPenaltyChargeAmount" name="penaltyChargeAmount" value="<s:property value="penaltyChargeAmount" />"/></td>
    </tr>
    <tr>
        <td><label>Total Amount to Pay After Penalty Charge</label></td>
        <td><label id="totalAmountToPayAfterNewPenaltyChargeLabel"><s:property value="totalAmountToPayAfterNewPenaltyChargeFormatted" />&nbsp;&nbsp;</td>
    </tr>
    <tr>
        <td colspan="2">
            <input type="submit" value="Apply" />
            <s:if test="isShowPenaltyChargeAlert">
            &nbsp;<s:checkbox name="isRemovePenaltyAlert" label="Remove From Penalty Charge Queue"/>
            <label>Remove From Queue</label>
            </s:if>
        </td>
    </tr>
    </table>
    <div class="chox-form-submit-result">&nbsp;</div>
    <div id="PenaltyChargeBox" class="action-error-msg"></div>
</fieldset> 



</form>    




