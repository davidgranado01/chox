<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
    var noteMessageDiv=null;
    function showNoteMessage(){
        if(!noteMessageDiv){
            noteMessageDiv = Ext.get('NoteMessage');
            noteMessageDiv.addClass('status-info-recalculation');
            // class="status-info-recalculation"
            noteMessageDiv.createChild('<span class="std-label-ro-small1-bold">N.B. </span>Figures in brackets indicate changes have been made<br/>to the invoice field(s) in question and the figures enclosed <br/>are the original values that were loaded into the system.');
        }
    }

</script>
<!--<div class="x-panel-bwrap chox-form-container">
<label id="expandAllInvoiceId" onclick="expandInvoiceDetails(true);" title="Expand All" style="cursor:pointer;font: 11px tahoma,arial,verdana,sans-serif;">+Expand All</label>-->
  <table cellpadding="0" cellspacing="0" border="0" width="100%">
    <tr valign="top">
        <td class="chox-form-left-col">
            <div>
                <div  id="hideAndShow" ></div>
                <div id="formUpdateInvoiceForm"  class="XXentity-form">
                    <fieldset class="x-fieldset">
                        <legend>Invoice Detail</legend>
                        <div style="display:none" class="form-container" id="invoiceDetailRId">
                            <div id="NoteMessage"/></div>
                            <table class="chox-table-form">
                                <tr>
                                        
                                    <!--  <div class="status-info-recalculation"><span class="std-label-ro-small1-bold">N.B.</span>Figures in brackets indicate changes have been made <br/>to the invoice field(s) in question and the figures enclosed <br/>are the original values that were loaded into the system.
                                      </div> -->
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Supplier Claims Handling #</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="handlingInvoiceNo" /></label></td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Supplier Claim Invoice #</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="claimInvoiceNo" /></label></td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Hire Rate Charged Per Day</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="hireRateChargedPerDay" /></label></td>
                                    <td>

                                        <s:if test="hireRateChargedPerDay!=hireRateChargedPerDayOriginal&&(hireRateChargedPerDayOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll" id="tooltip">(<s:property value="hireRateChargedPerDayOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Hire Net</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="hireNet" /></label></td>
                                    <td>
                                        <s:if test="hireNet!=hireNetOriginal&&(hireNetOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll" >(<s:property value="hireNetOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Hire VAT</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="hireVat" /></label></td>
                                    <td>
                                        <s:if test="hireVat!=hireVatOriginal&&(hireVatOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="hireVatOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Hire Gross</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="hireGross" /></label></td>
                                    <td>
                                        <s:if test="hireGross!=hireGrossOriginal&&(hireGrossOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="hireGrossOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Repair Net</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="repairNet" /></label></td>
                                    <td>
                                        <s:if test="repairNet!=repairNetOriginal&&(repairNetOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="repairNetOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Repair VAT</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="repairVat" /></label></td>
                                    <td>
                                        <s:if test="repairVat!=repairVatOriginal&&(repairVatOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="repairVatOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Repair Gross</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="repairGross" /></label></td>
                                    <td>
                                        <s:if test="repairGross!=repairGrossOriginal&&(repairGrossOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="repairGrossOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Engineer Fee Net</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="engineerFeeNet" /></label></td>
                                    <td>
                                        <s:if test="engineerFeeNet!=engineerFeeNetOriginal&&(engineerFeeNetOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="engineerFeeNetOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Engineer Fee VAT</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="engineerFeeVat" /></label></td>
                                    <td>
                                        <s:if test="engineerFeeVat!=engineerFeeVatOriginal&&(engineerFeeVatOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="engineerFeeVatOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Engineer Fee Gross</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="engineerFeeGross" /></label></td>
                                    <td>
                                        <s:if test="engineerFeeGross!=engineerFeeGrossOriginal&&(engineerFeeGrossOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="engineerFeeGrossOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Total Loss Fee Net</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="totalLossFeeNet" /></label></td>
                                    <td>
                                        <s:if test="totalLossFeeNet!=totalLossFeeNetOriginal&&(totalLossFeeNetOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="totalLossFeeNetOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Total Loss Fee VAT</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="totalLossFeeVat" /></label></td>
                                    <td>
                                        <s:if test="totalLossFeeVat!=totalLossFeeVatOriginal&&(totalLossFeeVatOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="totalLossFeeVatOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Total Loss Fee Gross</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="totalLossFeeGross" /></label></td>
                                    <td>
                                        <s:if test="totalLossFeeGross!=totalLossFeeGrossOriginal&&(totalLossFeeGrossOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="totalLossFeeGrossOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Storage Recovery Net</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="storageRecoveryNet" /></label></td>
                                    <td>
                                        <s:if test="storageRecoveryNet!=storageRecoveryNetOriginal&&(storageRecoveryNetOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="storageRecoveryNetOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Storage Recovery VAT</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="storageRecoveryVat" /></label></td>
                                    <td>
                                        <s:if test="storageRecoveryVat!=storageRecoveryVatOriginal&&(storageRecoveryVatOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="storageRecoveryVatOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Storage Recovery Gross</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="storageRecoveryGross" /></label></td>
                                    <td>
                                        <s:if test="storageRecoveryGross!=storageRecoveryGrossOriginal&&(storageRecoveryGrossOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="storageRecoveryGrossOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Total Net</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="totalNet" /></label></td>
                                    <td>
                                        <s:if test="totalNet!=totalNetOriginal&&(totalNetOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="totalNetOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Total VAT</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="totalVat" /></label></td>
                                    <td>
                                        <s:if test="totalVat!=totalVatOriginal&&(totalVatOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="totalVatOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Total Gross</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="totalGross" /></label></td>
                                    <td>
                                        <s:if test="totalGross!=totalGrossOriginal&&(totalGrossOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="totalGrossOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Claims Handling Invoice Amount</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="claimsHandlingInvoiceAmount" /></label></td>
                                    <td>
                                        <s:if test="claimsHandlingInvoiceAmount!=claimsHandlingInvoiceAmountOriginal&&(claimsHandlingInvoiceAmountOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="claimsHandlingInvoiceAmountOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Deduction For Claims Handling Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="deductionForClaimsHandlingFee" /></label></td>
                                    <td>
                                        <s:if test="deductionForClaimsHandlingFee!=deductionForClaimsHandlingFeeOriginal&&(deductionForClaimsHandlingFeeOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="deductionForClaimsHandlingFeeOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">CHO Discount</label></td>
                                    <td>&nbsp;</td>
                                    <s:if test="discount<0">
                                        <td><label class="std-data-ro" style="color: red; font-weight:bold;" >£<s:property value="discount" /></label></td>
                                    </s:if>
                                    <s:else>
                                        <td><label class="std-data-ro" >£<s:property value="discount" /></label></td>
                                    </s:else>
                                    
                                    <td>
                                        <s:if test="discount!=discountOriginal&&(discountOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="discountOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <td><label class="std-label-ro">Hire Penalty Percentage</label></td>
                                    <td>&nbsp;</td>
                                    <td>
                                        <label class="std-data-ro">
                                            <s:property value="hirePenaltyPercentage" />
                                            <s:if test="appliedHirePenaltyPercentageDifferent && (isInsurer || isChoxAdmin)">
                                                &nbsp;[actual: <s:property value="hirePenaltyPercentageApplied" />]
                                            </s:if>
                                        </label>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Hire Penalty Charge</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="hirePenaltyCharge" /></label></td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Repair Penalty Percentage</label></td>
                                    <td>&nbsp;</td>
                                    <td>
                                        <label class="std-data-ro">
                                            <s:property value="repairPenaltyPercentage" />
                                            <s:if test="appliedRepairPenaltyPercentageDifferent && (isInsurer || isChoxAdmin)">
                                              &nbsp;[actual: <s:property value="repairPenaltyPercentageApplied" />]  
                                            </s:if>
                                        </label>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Repair Penalty Charge</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="repairPenaltyCharge" /></label></td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Total Penalty Charge</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="totalPenaltyCharge" /></label></td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Insurer Discount</label></td>
                                    <td>&nbsp;</td>
                                    <s:if test="insurerDiscount<0">
                                        <td><label class="std-data-ro" style="color: red; font-weight:bold;">£<s:property value="insurerDiscount" /></label></td>
                                    </s:if>
                                    <s:else>
                                        <td><label class="std-data-ro">£<s:property value="insurerDiscount" /></label></td>
                                    </s:else>
                                    
                                    <td>
                                        <s:if test="insurerDiscount!=insurerDiscountOriginal&&(insurerDiscountOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="insurerDiscountOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Full Total Requested</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="fullTotalToPay" /></label></td>
                                    <td>
                                        <s:if test="fullTotalToPay!=fullTotalToPayOriginal&&(fullTotalToPayOriginal!=null)">
                                            <label class="chox-ttnum-smalll">(<s:property value="fullTotalToPayOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro-big">Total To Pay</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="totalToPay" /></label></td>
                                    <td>
                                        <s:if test="totalToPay!=totalToPayOriginal&&(totalToPayOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="totalToPayOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <s:if test="interimPaymentReceivedFullAndFinal">
                                    <tr>
                                        <td><label class="std-label-ro">Interim Payment</label></td>
                                        <td>&nbsp;</td>
                                        <td colspan="2"><label class="std-data-ro-red-invrecalc">£<s:property value="interimPaymentMade"/> (Received as Full & Final)</label></td>
                                    </tr>
                                    <tr>
                                        <td colspan="3"><label class="std-label-ro-small">Note that the interim payment has NOT been deducted from the 'Total To Pay'</label></td>
                                    </tr>
                                </s:if>
                                <s:elseif test="outstandingInterimPayment > 0 && interimPaymentReceived > 0">
                                    <tr>
                                        <td><label class="std-label-ro">Interim Payment</label></td>
                                        <td>&nbsp;</td>
                                        <td colspan="2"><label class="std-data-ro-red-invrecalc">£<s:property value="interimPaymentMade"/></label>&nbsp;<label class="std-data-ro-red-invrecalc">(Only £<s:property value="interimPaymentReceived" /> Received)</label></td>
                                    </tr>
                                    <tr>
                                        <td colspan="3"><label class="std-label-ro-small">Note that the interim payment has NOT been deducted from the 'Total To Pay'</label></td>
                                    </tr>
                                </s:elseif>
                                <s:elseif test="interimPaymentReceived > 0">
                                    <tr>
                                        <td><label class="std-label-ro">Interim Payment</label></td>
                                        <td>&nbsp;</td>
                                        <td colspan="2"><label class="std-data-ro-red-invrecalc">£<s:property value="interimPaymentMade"/></label>&nbsp;<label class="std-data-ro-red-invrecalc">(Received)</label></td>
                                    </tr>
                                    <tr>
                                        <td colspan="3"><label class="std-label-ro-small">Note that the interim payment has NOT been deducted from the 'Total To Pay'</label></td>
                                    </tr>
                                </s:elseif>
                                <s:elseif test="interimPaymentMade > 0">
                                    <tr>
                                        <td><label class="std-label-ro">Interim Payment</label></td>
                                        <td>&nbsp;</td>
                                        <td colspan="2"><label class="std-data-ro-red-invrecalc">£<s:property value="interimPaymentMade" /></label>&nbsp;<label class="std-data-ro-red-invrecalc">(Not Yet Received)</label></td>
                                    </tr>
                                    <tr>
                                        <td colspan="3"><label class="std-label-ro-small">Note that the interim payment has NOT been deducted from the 'Total To Pay'</label></td>
                                    </tr>
                                </s:elseif>
                                <s:else>
                                    <tr>
                                        <td><label class="std-label-ro">Interim Payment</label></td>
                                        <td>&nbsp;</td>
                                        <td><label class="std-data-ro">N/A</label></td>
                                    </tr>
                                </s:else>
                                <tr>
                                    <td><label class="std-label-ro">Excess Amount Collected From Policyholder</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="excessAmountCollected" /></label></td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">VAT Amount Collected From Policyholder</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="vatAmountCollected" /></label></td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Date Invoiced</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:date name="dateInvoiced" format="dd MMM yyyy" /></label></td>
                                    <td>
                                        <s:if test="CanShowOriginalInvoicedDate">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:date format="dd/MM/yyyy" name="dateInvoicedOriginal" />)</label>
                                        </s:if>

                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Invoice Uploaded Date</label></td>
                                    <td>&nbsp;</td>
                                    <td>
                                        <label class="std-data-ro"><s:date name="invoiceCreatedDate" format="dd MMM yyyy" /></label>
                                        <s:if test="invoicedDays > 0 && !penaltyChargeDateModified">
                                            <label class="std-data-ro"> (<s:property value="invoicedDays" /> days)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <s:if test="penaltyChargeDateModified">
                                <tr>
                                    <td><label class="std-label-ro">Penalty Charge Start Date</label></td>
                                    <td>&nbsp;</td>
                                    <td>
                                        <label class="std-data-ro"><s:date name="penaltyChargeDate" format="dd MMM yyyy" /></label>
                                        <s:if test="invoicedDays > 0">
                                            <label class="std-data-ro"> (<s:property value="invoicedDays" /> days)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                </s:if>
                                <tr>
                                    <td><label class="std-label-ro">Time Invoice Has Been<br/>With CHO For Review</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="daysWithCHOForReview" /></label></td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Time Invoice Has Been<br/>With Insurer For Review</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="daysWithInsurerForReview" /></label></td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Time Claim Has Been<br/>Awaiting Liability Resolution</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="daysAwaitingLiabilityResolution" /></label></td>
                                </tr>
                            </table>
                        </div>
                    </fieldset>
                </div>
            </div>
            <div>
                <div id="formUpdateHireVehicle" class="XXentity-form">
                    <fieldset class="x-fieldset">
                        <legend>Hire Vehicle Details</legend>
                        <div style="display:none" class="form-container" id="hireVehicleDetailRId">
                            <table class="chox-table-form">
                                <s:if test="tpiClaim">
                                    <tr>
                                        <td><label class="std-label-ro">
                                                Courtesy Car Provided?</label></td>
                                        <td>&nbsp;</td>
                                        <td><label class="std-data-ro"><s:property value="courtesyCarProvidedDesc" /></label></td>
                                    </tr>

                                </s:if>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Manufacturer</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="vehicleManufacturer" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Model</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="vehicleModel" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Registration</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="vehicleRegistration" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Replacement Vehicle Class</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="vehicleClass.name" /></label></td>
                                    <td>
                                        <s:if test="VehicleClassName!=VehicleClassNameOriginal&&(VehicleClassNameOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="VehicleClassNameOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>

                                <tr>
                                    <td><label class="std-label-ro">
                                            Hire Start</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy HH:mm" name="rentalStart" /></label></td>
                                    <td>
                                        <s:if test="canShowOriginalStartDate">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:date format="dd/MM/yyyy HH:mm" name="rentalStartOriginal" /><span id="rentalStart_originalPH"></span>)</label>
                                        </s:if>


                                    </td>
                                </tr>

                                <tr>
                                    <td><label class="std-label-ro">
                                            Hire End</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy HH:mm" name="rentalEnd" /></label></td>
                                    <td>
                                        <s:if test="canShowOriginalEndDate">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:date format="dd/MM/yyyy HH:mm" name="rentalEndOriginal" /><span id="rentalEnd_originalPH"></span>)</label>
                                        </s:if>


                                    </td>
                                </tr>

                                <tr>
                                    <td><label class="std-label-ro">
                                            Reason For Collection</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="collectionReason" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            No. Days Hire</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="days" /></label></td>

                                    <td>



                                        <s:if test="days!=daysOriginal&&(daysOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="daysOriginal" />)</label>
                                        </s:if>





                                    </td>
                                </tr>
                            </table>
                            <hr width="80%"/>
                            <div>
                                <a  href="http://www.hpicheck.com/" target="_blank"><img align="right" src="<%= request.getContextPath()%>/images/logo-hpi.png" style="display: inline;" alt="HPI" width="80" height="60" border="0"/></a>
                                <label>HPI Check</label>
                                <s:if test="hpiError != null">
                                    <label class="std-label-small">&nbsp;&nbsp;&nbsp;(HPI Check information not available)</label>
                                </s:if>
                                <table class="chox-table-form">
                                    <tr>
                                        <td><label class="std-label-ro">Vehicle Manufacturer</label></td>
                                        <td>&nbsp;</td>
                                        <td><label class="std-data-ro"><s:property value="hpiVehicleManufacturer" /></label></td>
                                    </tr>
                                    <tr>
                                        <td><label class="std-label-ro">Vehicle Model</label></td>
                                        <td>&nbsp;</td>
                                        <td><label class="std-data-ro"><s:property value="hpiVehicleModel" /></label></td>
                                    </tr>
                                    <tr>
                                        <td><label class="std-label-ro">Year of Manufacture</label></td>
                                        <td>&nbsp;</td>
                                        <td><label class="std-data-ro"><s:property value="hpiVehicleYear" /></label></td>
                                    </tr>
                                    <tr>
                                        <td><label class="std-label-ro">Date of Registration</label></td>
                                        <td>&nbsp;</td>
                                        <td><label class="std-data-ro"><s:property value="hpiFirstRegistration" /></label></td>
                                    </tr>
                                    <tr>
                                        <td><label class="std-label-ro">Engine Capacity</label></td>
                                        <td>&nbsp;</td>
                                        <td><label class="std-data-ro"><s:property value="hpiVehicleCapacity" /></label></td>
                                    </tr>
                                    <tr>
                                        <td><label class="std-label-ro">Door Plan</label></td>
                                        <td>&nbsp;</td>
                                        <td><label class="std-data-ro"><s:property value="hpiVehicleDoorplan" /></label></td>
                                    </tr>
                                    <tr>
                                        <td><label class="std-label-ro">Transmission</label></td>
                                        <td>&nbsp;</td>
                                        <td><label class="std-data-ro"><s:property value="hpiVehicleTransmission" /></label></td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </fieldset>
                </div>
            </div>
        </td>
        <td>
            <div>
                <div id="formUpdateExtrasFORM" class="XXentity-form">
                    <fieldset class="x-fieldset">
                        <s:if test="isSubscriberClaim">
                           <legend>Hire Extras</legend> 
                        </s:if>
                        <s:else>
                            <legend>Extras</legend>
                        </s:else>
                        <div style="display:none" class="form-container"  id="extrasRId">
                            <table class="chox-table-form">
<s:if test="isCollaborationProtocolClaim">
                                <tr>
                                    <td>
                                        <label class="std-label-ro">Collaboration Protocol Fee</label>
                                    </td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="collaborationFee" /></label></td>
                                    <td>
                                        <s:if test="collaborationFee!=collaborationFeeOriginal&&(collaborationFeeOriginal!=null)">
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="collaborationFeeOriginal" />)</label>&nbsp;
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <label class="std-label-ro">Collaboration Protocol Quantity</label>
                                    </td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="collaborationQty" /></label></td>
                                    <td>
                                        <s:if test="collaborationQty!=collaborationQtyOriginal&&(collaborationQtyOriginal!=null)">
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="collaborationQtyOriginal" />)</label>&nbsp;
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                        </s:if>
                                    </td>
                                </tr>
</s:if>
                                <tr>
                                    <td>
                                        <s:if test="isSubscriberClaim">
                                            <label class="std-label-ro">Acquisition Fee</label>
                                        </s:if>
                                        <s:else>
                                            <label class="std-label-ro">Miscellaneous Costs</label>
                                        </s:else>
                                    </td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="miscellaneousFee" /></label></td>
                                    <td>
                                        <s:if test="miscellaneousFee!=miscellaneousFeeOriginal&&(miscellaneousFeeOriginal!=null)">
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="miscellaneousFeeOriginal" />)</label>&nbsp;
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Automatic Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="automaticFee" /></label></td>
                                    <td>
                                        <s:if test="automaticFee!=automaticFeeOriginal&&(automaticFeeOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="automaticFeeOriginal" />)</label>
                                        </s:if>


                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Automatic Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="automaticQty" /></label></td>
                                    <td>
                                        <s:if test="automaticQty!=automaticQtyOriginal&&(automaticQtyOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="automaticQtyOriginal" />)</label>
                                        </s:if>


                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Additional Driver Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="additionalDriverFee" /></label></td>
                                    <td>
                                        <s:if test="additionalDriverFee!=additionalDriverFeeOriginal&&(additionalDriverFeeOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="additionalDriverFeeOriginal" />)</label>
                                        </s:if>


                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Additional Driver Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="additionalDriverQty" /></label></td>
                                    <td>
                                        <s:if test="additionalDriverQty!=additionalDriverQtyOriginal&&(additionalDriverQtyOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="additionalDriverQtyOriginal" />)</label>
                                        </s:if>


                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Sat Nav Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="satNavFee" /></label></td>
                                    <td>
                                        <s:if test="satNavFee!=satNavFeeOriginal&&(satNavFeeOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="satNavFeeOriginal" />)</label>
                                        </s:if>


                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Sat Nav Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="satNavQty" /></label></td>
                                    <td>
                                        <s:if test="satNavQty!=satNavQtyOriginal&&(satNavQtyOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="satNavQtyOriginal" />)</label>
                                        </s:if>


                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Estate Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="estateFee" /></label></td>
                                    <td>
                                        <s:if test="estateFee!=estateFeeOriginal&&(estateFeeOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="estateFeeOriginal" />)</label>
                                        </s:if>


                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Estate Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="estateQty" /></label></td>
                                    <td>
                                        <s:if test="estateQty!=estateQtyOriginal&&(estateQtyOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="estateQtyOriginal" />)</label>
                                        </s:if>


                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Baby Seat Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="babySeatFee" /></label></td>
                                    <td>
                                        <s:if test="babySeatFee!=babySeatFeeOriginal&&(babySeatFeeOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="babySeatFeeOriginal" />)</label>
                                        </s:if>


                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Baby Seat Quantity
                                        </label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="babySeatQty" /></label></td>
                                    <td>
                                        <s:if test="babySeatQty!=babySeatQtyOriginal&&(babySeatQtyOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="babySeatQtyOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Tow Bars Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="towBarsFee" /></label></td>
                                    <td>
                                        <s:if test="towBarsFee!=towBarsFeeOriginal&&(towBarsFeeOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="towBarsFeeOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Tow Bars Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="towBarsQty" /></label></td>
                                    <td>
                                        <s:if test="towBarsQty!=towBarsQtyOriginal&&(towBarsQtyOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="towBarsQtyOriginal" />)</label>
                                        </s:if>


                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Non-standard Risk Ins. Premium Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="nonStandardInsurancePremiumFee" /></label></td>
                                    <td>
                                        <s:if test="nonStandardInsurancePremiumFee!=nonStandardInsurancePremiumFeeOriginal&&(nonStandardInsurancePremiumFeeOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="nonStandardInsurancePremiumFeeOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Non-standard Risk Ins. Premium Qty</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="nonStandardInsurancePremiumQty" /></label></td>
                                    <td>
                                        <s:if test="nonStandardInsurancePremiumQty!=nonStandardInsurancePremiumQtyOriginal&&(nonStandardInsurancePremiumQtyOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="nonStandardInsurancePremiumQtyOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Cover Note Required For<br/>Customer's Own Insurance Policy?</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="coverNoteRequiredDesc" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Admin Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="adminFee" /></label></td>
                                    <td>
                                        <s:if test="adminFee!=adminFeeOriginal&&(adminFeeOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="adminFeeOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Admin Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="adminQty" /></label></td>
                                    <td>
                                        <s:if test="adminQty!=adminQtyOriginal&&(adminQtyOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="adminQtyOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Roof Rack Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="roofRackFee" /></label></td>
                                    <td>
                                        <s:if test="roofRackFee!=roofRackFeeOriginal&&(roofRackFeeOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="roofRackFeeOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Roof Rack Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="roofRackQty" /></label></td>
                                    <td>
                                        <s:if test="roofRackQty!=roofRackQtyOriginal&&(roofRackQtyOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="roofRackQtyOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Dual Control Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="dualControlFee" /></label></td>
                                    <td>
                                        <s:if test="dualControlFee!=dualControlFeeOriginal&&(dualControlFeeOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="dualControlFeeOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Dual Control Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="dualControlQty" /></label></td>
                                    <td>
                                        <s:if test="dualControlQty!=dualControlQtyOriginal&&(dualControlQtyOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="dualControlQtyOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Delivery Collection Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="deliveryCollectionFee" /></label></td>
                                    <td>
                                        <s:if test="deliveryCollectionFee!=deliveryCollectionFeeOriginal&&(deliveryCollectionFeeOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="deliveryCollectionFeeOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Delivery Collection Fee Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="deliveryCollectionQty" /></label></td>
                                    <td>
                                        <s:if test="deliveryCollectionQty!=deliveryCollectionQtyOriginal&&(deliveryCollectionQtyOriginal!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="deliveryCollectionQtyOriginal" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </fieldset>
                </div>
            </div>
        <s:if test="isSubscriberClaim">
            <div id="formRepairExtras" class="XXentity-form">
                <fieldset class="x-fieldset">
                    <legend>Repair Extras</legend>
                    <div style="display:none" class="form-container"  id="formRepairExtrasRId">
                        <table class="chox-table-form">
                            <tr>
                                <td><label class="std-label-ro">
                                        Repair Admin Fee</label></td>
                                <td>&nbsp;</td>
                                <td><label class="std-data-ro">£<s:property value="repairAdminFee" /></label></td>
                                <td>
                                    <s:if test="repairAdminFee.compareTo(repairAdminFeeOriginal)!=0&&(repairAdminFeeOriginal!=null)">
                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="repairAdminFeeOriginal" />)</label>
                                    </s:if>
                                </td>
                            </tr>
                            <tr>
                                <td><label class="std-label-ro">
                                        Repair Acquisition Fee</label></td>
                                <td>&nbsp;</td>
                                <td><label class="std-data-ro">£<s:property value="repairAcquisitionFee" /></label></td>
                                <td>
                                    <s:if test="repairAcquisitionFee.compareTo(repairAcquisitionFeeOriginal)!=0&&(repairAcquisitionFeeOriginal!=null)">
                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="repairAcquisitionFeeOriginal" />)</label>
                                    </s:if>
                                </td>
                            </tr>
                        </table>
                    </div>
                </fieldset>
            </div>
        </s:if>
            <div>
                <div id="formEngRptAction" class="XXentity-form">
                    <fieldset class="x-fieldset">
                        <legend>Engineer Report</legend>
                        <div style="display:none" class="form-container"  id="engineerReportRId">
                            <table class="chox-table-form">
                                <tr>
                                    <td><label class="std-label-ro">
                                            Estimated Labour Amount</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="labourAmount" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Estimated Total Repair Amount</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="totalAmount" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Estimated Days Under Repair</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="estimatedDays" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Usable?</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="isUsableDesc" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Name</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="name" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Company</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="company" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Engineer Address 1</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="address1" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Engineer Address 2</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="address2" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Engineer Address 3</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="address3" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Engineer Address 4</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="address4" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Engineer Address 5</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="address5" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Engineer Postcode</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="postcode" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Engineer Telephone</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="telephone" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Engineer Email</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="email" /></label></td></tr>
                            </table>
                        </div>
                    </fieldset>
                </div>
            </div>
            <s:if test="canShowPaymentDetails">               
            <div>
                <div id="formPaymentDetailsAction" class="XXentity-form">
                    <fieldset class="x-fieldset">
                        <legend>Payment Details</legend>
                        <div style="display:none" class="form-container"  id="formPaymentDetailsRId">
                            <table class="chox-table-form" style="width: 100%">
                                <tr>
                                    <td style="width: 30%"><label class="std-label-ro">Hire Gross Paid</label></td>
                                    <td style="width: 10%">&nbsp;</td>
                                    <td style="width: 60%"><label class="std-data-ro">£<s:property value="hireGrossPaid" /></label></td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Repair Gross Paid</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="repairGrossPaid" /></label></td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Engineer Fee Gross Paid</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="engineerFeeGrossPaid" /></label></td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Total Loss Fee Gross Paid</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="totalLossFeeGrossPaid" /></label></td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Storage Recovery Gross Paid</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="storageRecoveryGrossPaid" /></label></td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Hire Penalty Charges Paid</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="hirePenaltyChargePaid" /></label></td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Repair Penalty Charges Paid</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="repairPenaltyChargePaid" /></label></td>
                                </tr>
                                <s:if test="paymentDetailsClaimHandInvAmt > 0">
                                    <tr>
                                        <td><label class="std-label-ro">Claims Handling Invoice Amount</label></td>
                                        <td>&nbsp;</td>
                                        <td><label class="std-data-ro">£<s:property value="paymentDetailsClaimHandInvAmt" /></label></td>
                                    </tr>
                                </s:if>
                                <s:if test="paymentDetailsDeductionClaimHandFee > 0">
                                    <tr>
                                        <td><label class="std-label-ro">Deduction For Claims Handling Fee</label></td>
                                        <td>&nbsp;</td>
                                        <td><label class="std-data-ro">£<s:property value="paymentDetailsDeductionClaimHandFee" /></label></td>
                                    </tr>
                                </s:if>
                                <s:if test="paymentDetailsCHODiscount < 0">
                                    <tr>
                                        <td><label class="std-label-ro">CHO Discount</label></td>
                                        <td>&nbsp;</td>
                                        <td><label class="std-data-ro">£<s:property value="paymentDetailsCHODiscount" /></label></td>
                                    </tr>
                                </s:if>
                                <s:if test="paymentDetailsInsurerDiscount < 0">
                                    <tr>
                                        <td><label class="std-label-ro">Insurer Discount</label></td>
                                        <td>&nbsp;</td>
                                        <td><label class="std-data-ro">£<s:property value="paymentDetailsInsurerDiscount" /></label></td>
                                    </tr>
                                </s:if>
                                <tr>
                                    <td><label class="std-label-ro">Total To Pay</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="totalToPay" /></label></td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Interim Payments Made</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="interimPaymentMade" /></label></td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Final Payment</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="finalPayment" /></label></td>
                                </tr>
                            </table>
                        </div>
                    </fieldset>
                </div>                 
            </div>
           </s:if> 
        </td>
    </tr>
</table>
<!--</div>-->
