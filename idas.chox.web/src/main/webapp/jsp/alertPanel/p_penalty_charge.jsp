<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
                    number:true,
                    min:0
                }
            },
            messages: {
                penaltyChargeAmount:{
                    required:"You must supply a value for 'Penalty Charge Amount'",
                    number:"You must supply a numeric value for 'Penalty Charge Amount'",
                    min:"'Penalty Charge Amount' must be larger than 0"
                }
            }
        });
    });

    function updateTotalToPay(inputValue)
    {
        var newPenaltyCharge;
        var totalAmountToPayBeforeNewPenaltyCharge
        var totalAmountToPayAfterNewPenaltyCharge;

        if(!isNaN(inputValue)){
            newPenaltyCharge = parseFloat(inputValue) == NaN ? 0 : parseFloat(inputValue);
            if(isNaN(newPenaltyCharge)){
                newPenaltyCharge = 0;
            }
        }
        else
        {
            newPenaltyCharge = 0;
        }

        if (newPenaltyCharge == 0) {
            $("form#applyPenaltyCharge #penaltyPercentageId").rules("remove");
        }
        else {
            $("form#applyPenaltyCharge #penaltyPercentageId").rules("add", {required: true, messages: {required: "You must supply a value for 'Penalty Percentage'"}});
        }

        totalAmountToPayBeforeNewPenaltyCharge = parseFloat($("#hvTotalAmountToPayBeforeNewPenaltyCharge").val());
        totalAmountToPayAfterNewPenaltyCharge = newPenaltyCharge + totalAmountToPayBeforeNewPenaltyCharge;
        $("#totalAmountToPayAfterNewPenaltyChargeLabel").text('£' + Math.round(totalAmountToPayAfterNewPenaltyCharge*100)/100);
        
        
        var percentageAccepted = parseFloat($("#percentageLiabilityAcceptedForPenalty").val());
        if (! isNaN(percentageAccepted)){
            $("#splitLiabilityToPayAfterPenaltyFormattedLabel").text('£' + Math.round(totalAmountToPayAfterNewPenaltyCharge*percentageAccepted)/100);
        }
        


    }

</script>

<form action="<%= request.getContextPath()%>/prv/doApplyPenaltyCharge.action" method="post" id="applyPenaltyCharge" name="applyPenaltyCharge">

<s:hidden name="id" />
<s:hidden id="hvTotalAmountToPayBeforeNewPenaltyCharge" name="totalAmountToPayBeforeNewPenaltyCharge" />

<fieldset class="x-fieldset"><legend>Apply Penalty Charge</legend>

    <div class="status-warning">
        Payment for this invoice is overdue. The number of days since
        <s:if test="isBasedOnLiabilityAgreedDate">
            liability agreed
        </s:if>

        <s:else>
         the invoice was created
        </s:else>
          is <s:property value="invoiceIntroducedDays" />. A penalty charge may be applicable to this invoice.
    </div>

    <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td width="30px"><label>Full Total Requested</label></td>
        <td width="70%"><s:property value="totalAmountToPayBeforeNewPenaltyChargeFormatted" /></td>
    </tr>
    <s:if test="isBasedOnLiabilityAgreedDate">
    <tr>
        <td width="30px"><label>Total To Pay Amount (Split/PWP) </label></td>
        <td width="70%"><s:property value="splitLiabilityToPayBeforePenaltyFormatted" /></td>
    </tr>
    </s:if>
    <tr>
        <td><label>Penalty Amount</label></td>
        <td>£&nbsp;<input type="text" autocomplete="off" class="chox-ttxt" id="tPenaltyChargeAmount" name="penaltyChargeAmount" value="<s:property value="penaltyChargeAmount" />"/></td>
    </tr>
    <tr>
        <td><label>Penalty Percentage</label></td>
                          <td align="left" width="20%">
                                    <div id="PenaltyPercentageDiv">
                                        <s:select
                                            name="penaltyPercentage"
                                            id="penaltyPercentageId"
                                            list="#{'7.5%':'7.5%', '15.0%':'15.0%', 'Commercial':'Commercial'}"
                                            headerKey=""
                                            headerValue="Please Select"
                                            emptyOption="false">
                                        </s:select>
                                    </div>
                          </td>
    </tr>
    <tr>
        <td><label>Full Total Requested After Penalty Charge</label></td>
        <td><label id="totalAmountToPayAfterNewPenaltyChargeLabel"><s:property value="totalAmountToPayAfterNewPenaltyChargeFormatted" />&nbsp;&nbsp;</label></td>
    </tr>
    <s:if test="isBasedOnLiabilityAgreedDate">
    <input type="hidden" id="percentageLiabilityAcceptedForPenalty" name="percentageLiabilityAcceptedForPenalty" value="<s:property value="percentageLiabilityAcceptedForPenalty"/>" />
    <tr>
        <td><label>Total To Pay Amount After Penalty Charge (Split/PWP) </label></td>
        <td><label id="splitLiabilityToPayAfterPenaltyFormattedLabel"><s:property value="splitLiabilityToPayAfterPenaltyFormatted" />&nbsp;&nbsp;</label></td>
        
    
    </tr>
    </s:if>
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




