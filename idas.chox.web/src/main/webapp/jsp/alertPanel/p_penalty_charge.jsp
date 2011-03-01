<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(function(){

        $("#tHirePenaltyChargeAmount").keyup(function (e) {
            updateTotalToPay();
        });

        $("#tRepairPenaltyChargeAmount").keyup(function (e) {
            updateTotalToPay();
        });

        var form = $("form#applyPenaltyCharge");

        form.validate(
        {
            errorLabelContainer: "#PenaltyChargeBox",
            rules: {
                hirePenaltyChargeAmount:{
//                    required:true,
//                    min:0,
                    number:true
                },
                repairPenaltyChargeAmount:{
//                    required:true,
//                    min:0,
                    number:true
                }
            },
            messages: {
                repairPenaltyChargeAmount:{
//                    required:"You must supply a value for 'Repair Penalty Charge Amount'",
//                    min:"'Repair Penalty Charge Amount' must be larger than 0",
                    number:"You must supply a numeric value for 'Repair Penalty Charge Amount'"
                },
                hirePenaltyChargeAmount:{
//                    required:"You must supply a value for 'Hire Penalty Charge Amount'",
//                    min:"'Hire Penalty Charge Amount' must be larger than 0",
                    number:"You must supply a numeric value for 'Hire Penalty Charge Amount'"
                }
            }
        });
    });

    function updateTotalToPay(inputValue)
    {
        var repairAmount = $("#tRepairPenaltyChargeAmount").val();
        var hireAmount = $("#tHirePenaltyChargeAmount").val();
        var repairPenaltyCharge;
        var hirePenaltyCharge;
        var totalPenaltyCharge;
        var totalAmountToPayBeforeNewPenaltyCharge
        var totalAmountToPayAfterNewPenaltyCharge;

        if(!isNaN(repairAmount)){
            repairPenaltyCharge = parseFloat(repairAmount) == NaN ? 0 : parseFloat(repairAmount);
            if(isNaN(repairPenaltyCharge)){
                repairPenaltyCharge = 0;
            }
        }
        else
        {
            repairPenaltyCharge = 0;
        }

        if(!isNaN(hireAmount)){
            hirePenaltyCharge = parseFloat(hireAmount) == NaN ? 0 : parseFloat(hireAmount);
            if(isNaN(hirePenaltyCharge)){
                hirePenaltyCharge = 0;
            }
        }
        else
        {
            hirePenaltyCharge = 0;
        }
        totalPenaltyCharge = repairPenaltyCharge + hirePenaltyCharge;
        if (hirePenaltyCharge == 0) {
            $("form#applyPenaltyCharge #hirePenaltyPercentageId").rules("remove");
            $("form#applyPenaltyCharge #tRepairPenaltyChargeAmount").rules("add", {required: true, messages: {required: "You must supply a value for 'Repair Penalty Charge Amount' or 'Hire Penalty Charge Amount'"}});
            $("form#applyPenaltyCharge #tRepairPenaltyChargeAmount").rules("add", {min: 0, messages: {required: "One of 'Repair Penalty Charge Amount' and 'Hire Penalty Charge Amount' must be larger than 0"}});
        }
        else {
            $("form#applyPenaltyCharge #hirePenaltyPercentageId").rules("add", {required: true, messages: {required: "You must supply a value for 'Hire Penalty Percentage'"}});
            $("form#applyPenaltyCharge #tRepairPenaltyChargeAmount").rules("remove");
        }

        if (repairPenaltyCharge == 0) {
            $("form#applyPenaltyCharge #repairPenaltyPercentageId").rules("remove");
        }
        else {
            $("form#applyPenaltyCharge #repairPenaltyPercentageId").rules("add", {required: true, messages: {required: "You must supply a value for 'Repair Penalty Percentage'"}});
       }

        totalAmountToPayBeforeNewPenaltyCharge = parseFloat($("#hvTotalAmountToPayBeforeNewPenaltyCharge").val());
        totalAmountToPayAfterNewPenaltyCharge = totalPenaltyCharge + totalAmountToPayBeforeNewPenaltyCharge;
        $("#totalAmountToPayAfterNewPenaltyChargeLabel").text('£' + Math.round(totalAmountToPayAfterNewPenaltyCharge*100)/100);
        
        
        var percentageAccepted = parseFloat($("#percentageLiabilityAcceptedForPenalty").val());
        if (! isNaN(percentageAccepted)){
            $("#splitLiabilityToPayAfterPenaltyFormattedLabel").text('£' + Math.round(totalAmountToPayAfterNewPenaltyCharge*percentageAccepted)/100);
        }
        


    }

    function updateHirePenaltyPercentage() {
        // Currently not used: we need the percentage of the hire cost not the total
        var originalHireAmount = $("#tHirePenaltyChargeAmount").val();
        var percentage = parseFloat($('#hirePenaltyPercentageId').val())/100.0;
        var fullTotalRequested = parseFloat($('#hvTotalAmountToPayBeforeNewPenaltyCharge').val());
        var hirePenaltyCharge = parseFloat((fullTotalRequested*percentage).toFixed(2));
        var repairPenaltyCharge = parseFloat($("#tRepairPenaltyChargeAmount").val());
        $('#tHirePenaltyChargeAmount').val(hirePenaltyCharge);
        var newTotaltoPay = fullTotalRequested + hirePenaltyCharge + repairPenaltyCharge;
        $("#totalAmountToPayAfterNewPenaltyChargeLabel").text('£' + newTotaltoPay);
    }
    function updateRepairPenaltyPercentage() {
        // Currently not used: we need the percentage of the repair cost not the total
        var originalRepairAmount = $("#tRepairPenaltyChargeAmount").val();
        var percentage = parseFloat($('#repairPenaltyPercentageId').val())/100.0;
        var fullTotalRequested = parseFloat($('#hvTotalAmountToPayBeforeNewPenaltyCharge').val());
        var repairPenaltyCharge = parseFloat((fullTotalRequested*percentage).toFixed(2));
        var hirePenaltyCharge = parseFloat($("#tHirePenaltyChargeAmount").val());
        $('#tRepairPenaltyChargeAmount').val(repairPenaltyCharge);
        var newTotaltoPay = fullTotalRequested + hirePenaltyCharge + repairPenaltyCharge;
        $("#totalAmountToPayAfterNewPenaltyChargeLabel").text('£' + newTotaltoPay);
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

    <table border="0" cellspacing="0" cellpadding="0" style="width:90%">
    <tr>
        <td align="left" style="width:40%"><label>Full Total Requested &nbsp;</label></td>
        <td align="left" style="width:30%"><s:property value="totalAmountToPayBeforeNewPenaltyChargeFormatted" /></td>
        <td style="width:10%"></td>
        <td style="width:20%"></td>
     </tr>
     
    <s:if test="interimPaymentReceived ">
    <tr>
        <td align="left" style="width:40%"><label>Interim Payment &nbsp;</label></td>
        <td align="left" style="width:30%"><label class="std-data-ro-red">£<s:property value="interimPayment" /> (Payment Received)</label></td>
        <td style="width:10%"></td>
        <td style="width:20%"></td>
    </tr>
    </s:if>
    <s:elseif test="!interimPaymentReceived && interimPayment">
     <tr>
         <td align="left" style="width:40%"><label>Interim Payment &nbsp;</label></td>
         <td align="left" style="width:30%"><label class="std-data-ro-red">£<s:property value="interimPayment" /> (Payment Not Received)</label></td>
        <td style="width:10%"></td>
        <td style="width:20%"></td>
     </tr>
     </s:elseif>
    <s:else>
     <tr>
        <td align="left" style="width:40%"><label>Interim Payment &nbsp;</label></td>
        <td align="left" style="width:30%">N/A</td>
        <td style="width:10%"></td>
        <td style="width:20%"></td>
     </tr>
     </s:else>

     

    <s:if test="isBasedOnLiabilityAgreedDate">
    <tr>
        <td align="left" style="white-space:nowrap"><label>Total To Pay Amount (Split/PWP) &nbsp;</label></td>
        <td colspan="3" align="left"><s:property value="splitLiabilityToPayBeforePenaltyFormatted" /></td>
    </tr>
    </s:if>
    <tr>
        <td align="left"><label>Hire Penalty Amount</label></td>
        <td align="left">£&nbsp;<input type="text" class="chox-ttxt" id="tHirePenaltyChargeAmount" name="hirePenaltyChargeAmount" value="<s:property value="hirePenaltyChargeAmount" />" onkeyup="extractNumber(this,2,true);"/></td>
        <td align="left"><label>Repair Penalty Amount</label></td>
        <td align="left" nowrap >£<input type="text" class="chox-ttxt" id="tRepairPenaltyChargeAmount" name="repairPenaltyChargeAmount" value="<s:property value="repairPenaltyChargeAmount" />" onkeyup="extractNumber(this,2,true);"/></td>
    </tr>
    <tr>
        <td align="left"><label>Hire Penalty Percentage &nbsp;</label></td>
                          <td align="left">
                                    <div id="hirePenaltyPercentageDiv">
                                        <s:select
                                            name="hirePenaltyPercentage"
                                            id="hirePenaltyPercentageId"
                                            list="#{'7.5%':'7.5%', '15.0%':'15.0%', 'Commercial':'Commercial'}"
                                            headerKey=""
                                            headerValue="Please Select"
                                            emptyOption="false">
                                        </s:select>
                                    </div>
                          </td>
        <td align="left"><label>Repair Penalty Percentage &nbsp;</label></td>
                          <td align="left">
                                    <div id="repairPenaltyPercentageDiv">
                                        <s:select
                                            name="repairPenaltyPercentage"
                                            id="repairPenaltyPercentageId"
                                            list="#{'2.5%':'2.5%', '5.0%':'5.0%'}"
                                            headerKey=""
                                            headerValue="Please Select"
                                            emptyOption="false">
                                        </s:select>
                                    </div>
                          </td>
    </tr>
    <tr>
        <td align="left"><label>Full Total Requested After Penalty Charge &nbsp;</label></td>
        <td colspan="3" align="left"><label id="totalAmountToPayAfterNewPenaltyChargeLabel" style="font-weight:bold"><s:property value="totalAmountToPayAfterNewPenaltyChargeFormatted" />&nbsp;&nbsp;</label></td>
    </tr>
    <s:if test="isBasedOnLiabilityAgreedDate">
    <input type="hidden" id="percentageLiabilityAcceptedForPenalty" name="percentageLiabilityAcceptedForPenalty" value="<s:property value="percentageLiabilityAcceptedForPenalty"/>" />
    <tr>
        <td align="left"><label>Total To Pay Amount After Penalty Charge (Split/PWP) &nbsp;</label></td>
        <td colspan="3" align="left"><label id="splitLiabilityToPayAfterPenaltyFormattedLabel" style="font-weight:bold"><s:property value="splitLiabilityToPayAfterPenaltyFormatted" />&nbsp;&nbsp;</label></td>
        
    
    </tr>
    </s:if>
    <tr>
        <td colspan="4" align="left">
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
    <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
    <!--s:token/-->

</form>




