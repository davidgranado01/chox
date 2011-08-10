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

  <table cellpadding="0" cellspacing="0" border="0" width="100%">
    <tr valign="top">
        <td class="chox-form-left-col">
            <div>
                <div  id="hideAndShow" />
                <div id="formUpdateInvoiceForm"  class="XXentity-form">
                    <fieldset class="x-fieldset">
                        <legend>Invoice Detail</legend>
                        <div style="display:none" class="form-container" id="invoiceDetailRId">
                            <table class="chox-table-form">
                                <tr>
                                    <div  id="NoteMessage"></div>

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

                                        <s:if test="hireRateChargedPerDay!=hireRateChargedPerDay_original&&(hireRateChargedPerDay_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll" id="tooltip">(<s:property value="hireRateChargedPerDay_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Hire Net</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="hireNet" /></label></td>
                                    <td>
                                        <s:if test="hireNet!=hireNet_original&&(hireNet_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll" >(<s:property value="hireNet_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Hire VAT</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="hireVat" /></label></td>
                                    <td>
                                        <s:if test="hireVat!=hireVat_original&&(hireVat_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="hireVat_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Hire Gross</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="hireGross" /></label></td>
                                    <td>
                                        <s:if test="hireGross!=hireGross_original&&(hireGross_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="hireGross_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Repair Net</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="repairNet" /></label></td>
                                    <td>
                                        <s:if test="repairNet!=repairNet_original&&(repairNet_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="repairNet_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Repair VAT</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="repairVat" /></label></td>
                                    <td>
                                        <s:if test="repairVat!=repairVat_original&&(repairVat_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="repairVat_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Repair Gross</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="repairGross" /></label></td>
                                    <td>
                                        <s:if test="repairGross!=repairGross_original&&(repairGross_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="repairGross_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Engineer Fee Net</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="engineerFeeNet" /></label></td>
                                    <td>
                                        <s:if test="engineerFeeNet!=engineerFeeNet_original&&(engineerFeeNet_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="engineerFeeNet_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Engineer Fee VAT</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="engineerFeeVat" /></label></td>
                                    <td>
                                        <s:if test="engineerFeeVat!=engineerFeeVat_original&&(engineerFeeVat_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="engineerFeeVat_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Engineer Fee Gross</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="engineerFeeGross" /></label></td>
                                    <td>
                                        <s:if test="engineerFeeGross!=engineerFeeGross_original&&(engineerFeeGross_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="engineerFeeGross_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Total Loss Fee Net</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="totalLossFeeNet" /></label></td>
                                    <td>
                                        <s:if test="totalLossFeeNet!=totalLossFeeNet_original&&(totalLossFeeNet_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="totalLossFeeNet_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Total Loss Fee VAT</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="totalLossFeeVat" /></label></td>
                                    <td>
                                        <s:if test="totalLossFeeVat!=totalLossFeeVat_original&&(totalLossFeeVat_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="totalLossFeeVat_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Total Loss Fee Gross</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="totalLossFeeGross" /></label></td>
                                    <td>
                                        <s:if test="totalLossFeeGross!=totalLossFeeGross_original&&(totalLossFeeGross_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="totalLossFeeGross_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Storage Recovery Net</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="storageRecoveryNet" /></label></td>
                                    <td>
                                        <s:if test="storageRecoveryNet!=storageRecoveryNet_original&&(storageRecoveryNet_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="storageRecoveryNet_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Storage Recovery VAT</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="storageRecoveryVat" /></label></td>
                                    <td>
                                        <s:if test="storageRecoveryVat!=storageRecoveryVat_original&&(storageRecoveryVat_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="storageRecoveryVat_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Storage Recovery Gross</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="storageRecoveryGross" /></label></td>
                                    <td>
                                        <s:if test="storageRecoveryGross!=storageRecoveryGross_original&&(storageRecoveryGross_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="storageRecoveryGross_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Total Net</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="totalNet" /></label></td>
                                    <td>
                                        <s:if test="totalNet!=totalNet_original&&(totalNet_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="totalNet_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Total VAT</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="totalVat" /></label></td>
                                    <td>
                                        <s:if test="totalVat!=totalVat_original&&(totalVat_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="totalVat_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Total Gross</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="totalGross" /></label></td>
                                    <td>
                                        <s:if test="totalGross!=totalGross_original&&(totalGross_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="totalGross_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Claims Handling Invoice Amount</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="claimsHandlingInvoiceAmount" /></label></td>
                                    <td>
                                        <s:if test="claimsHandlingInvoiceAmount!=claimsHandlingInvoiceAmount_original&&(claimsHandlingInvoiceAmount_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="claimsHandlingInvoiceAmount_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Deduction For Claims Handling Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="deductionForClaimsHandlingFee" /></label></td>
                                    <td>
                                        <s:if test="deductionForClaimsHandlingFee!=deductionForClaimsHandlingFee_original&&(deductionForClaimsHandlingFee_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="deductionForClaimsHandlingFee_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Discount</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="discount" /></label></td>
                                    <td>
                                        <s:if test="discount!=discount_original&&(discount_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="discount_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Hire Penalty Percentage</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="hirePenaltyPercentage" /></label></td>

                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Hire Penalty Charge</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="hirePenaltyCharge" /></label></td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Repair Penalty Percentage</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="repairPenaltyPercentage" /></label></td>
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
                                    <td><label class="std-label-ro">Full Total Requested</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="fullTotalToPay" /></label></td>
                                    <td>
                                        <s:if test="fullTotalToPay!=fullTotalToPay_original&&(fullTotalToPay_original!=null)">
                                            <label class="chox-ttnum-smalll">(<s:property value="fullTotalToPay_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro-big">Total To Pay</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="totalToPay" /></label></td>
                                    <td>
                                        <s:if test="totalToPay!=totalToPay_original&&(totalToPay_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">(<s:property value="totalToPay_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <s:if test="interimPaymentReceivedFullAndFinal">
                                    <tr>
                                        <td><label class="std-label-ro">Interim Payment</label></td>
                                        <td>&nbsp;</td>
                                        <td><label class="std-data-ro-red-invrecalc">£<s:property value="interimPayment"/> (Payment Received, Accepted Full & Final)</label></td>
                                    </tr>
                                </s:if>
                                <s:elseif test="interimPaymentReceived">
                                    <tr>
                                        <td><label class="std-label-ro">Interim Payment</label></td>
                                        <td>&nbsp;</td>
                                        <td><label class="std-data-ro-red-invrecalc">£<s:property value="interimPayment"/> (Payment Received)</label></td>
                                    </tr>
                                    <tr>
                                        <td colspan="3"><label class="std-label-ro-small">Note that the interim payment has NOT been deducted from the 'Total To Pay'</label></td>
                                    </tr>
                                </s:elseif>
                                <s:elseif test="!interimPaymentReceived && interimPayment">
                                    <tr>
                                        <td><label class="std-label-ro">Interim Payment</label></td>
                                        <td>&nbsp;</td>
                                        <td><label class="std-data-ro-red-invrecalc">£<s:property value="interimPayment" /> (Not Yet Received)</label></td>
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
                                            <label class="chox-ttnum-smalll">(<s:date format="dd/MM/yyyy" name="dateInvoiced_original" />)</label>
                                        </s:if>

                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">Invoice Uploaded Date</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:date name="invoiceCreatedDate" format="dd MMM yyyy" /></label>
                                        <s:if test="invoicedDays > 0">
                                            <label class="std-data-ro"> (<s:property value="invoicedDays" /> days)</label>
                                        </s:if></td>
                                </tr>
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

                                        <s:if test="VehicleClassName!=VehicleClassName_original&&(VehicleClassName_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="VehicleClassName_original" />)</label>
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
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:date format="dd/MM/yyyy HH:mm" name="rentalStart_original" /><span id="rentalStart_originalPH"></span>)</label>
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
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:date format="dd/MM/yyyy HH:mm" name="rentalEnd_original" /><span id="rentalEnd_originalPH"></span>)</label>
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



                                        <s:if test="days!=days_original&&(days_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="days_original" />)</label>
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
                        <legend>Extras</legend>
                        <div style="display:none" class="form-container"  id="extrasRId">
                            <table class="chox-table-form">
                                <tr>
                                    <td><label class="std-label-ro">
                                            Miscellaneous Costs</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="miscellaneousFee" /></label></td>
                                    <td>
                                        <s:if test="miscellaneousFee!=miscellaneousFee_original&&(miscellaneousFee_original!=null)">
                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="miscellaneousFee_original" />)</label>&nbsp;
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
                                        <s:if test="automaticFee!=automaticFee_original&&(automaticFee_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="automaticFee_original" />)</label>
                                        </s:if>


                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Automatic Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="automaticQty" /></label></td>
                                    <td>
                                        <s:if test="automaticQty!=automaticQty_original&&(automaticQty_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="automaticQty_original" />)</label>
                                        </s:if>


                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Additional Driver Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="additionalDriverFee" /></label></td>
                                    <td>
                                        <s:if test="additionalDriverFee!=additionalDriverFee_original&&(additionalDriverFee_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="additionalDriverFee_original" />)</label>
                                        </s:if>


                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Additional Driver Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="additionalDriverQty" /></label></td>
                                    <td>
                                        <s:if test="additionalDriverQty!=additionalDriverQty_original&&(additionalDriverQty_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="additionalDriverQty_original" />)</label>
                                        </s:if>


                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Sat Nav Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="satNavFee" /></label></td>
                                    <td>
                                        <s:if test="satNavFee!=satNavFee_original&&(satNavFee_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="satNavFee_original" />)</label>
                                        </s:if>


                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Sat Nav Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="satNavQty" /></label></td>
                                    <td>
                                        <s:if test="satNavQty!=satNavQty_original&&(satNavQty_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="satNavQty_original" />)</label>
                                        </s:if>


                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Estate Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="estateFee" /></label></td>
                                    <td>
                                        <s:if test="estateFee!=estateFee_original&&(estateFee_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="estateFee_original" />)</label>
                                        </s:if>


                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Estate Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="estateQty" /></label></td>
                                    <td>
                                        <s:if test="estateQty!=estateQty_original&&(estateQty_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="estateQty_original" />)</label>
                                        </s:if>


                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Baby Seat Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="babySeatFee" /></label></td>
                                    <td>
                                        <s:if test="babySeatFee!=babySeatFee_original&&(babySeatFee_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="babySeatFee_original" />)</label>
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
                                        <s:if test="babySeatQty!=babySeatQty_original&&(babySeatQty_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="babySeatQty_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Tow Bars Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="towBarsFee" /></label></td>
                                    <td>
                                        <s:if test="towBarsFee!=towBarsFee_original&&(towBarsFee_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="towBarsFee_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Tow Bars Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="towBarsQty" /></label></td>
                                    <td>
                                        <s:if test="towBarsQty!=towBarsQty_original&&(towBarsQty_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="towBarsQty_original" />)</label>
                                        </s:if>


                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Non-standard Risk Ins. Premium Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="nonStandardInsurancePremiumFee" /></label></td>
                                    <td>
                                        <s:if test="nonStandardInsurancePremiumFee!=nonStandardInsurancePremiumFee_original&&(nonStandardInsurancePremiumFee_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="nonStandardInsurancePremiumFee_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Non-standard Risk Ins. Premium Qty</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="nonStandardInsurancePremiumQty" /></label></td>
                                    <td>
                                        <s:if test="nonStandardInsurancePremiumQty!=nonStandardInsurancePremiumQty_original&&(nonStandardInsurancePremiumQty_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="nonStandardInsurancePremiumQty_original" />)</label>
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
                                        <s:if test="adminFee!=adminFee_original&&(adminFee_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="adminFee_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Admin Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="adminQty" /></label></td>
                                    <td>
                                        <s:if test="adminQty!=adminQty_original&&(adminQty_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="adminQty_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Roof Rack Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="roofRackFee" /></label></td>
                                    <td>
                                        <s:if test="roofRackFee!=roofRackFee_original&&(roofRackFee_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="roofRackFee_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Roof Rack Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="roofRackQty" /></label></td>
                                    <td>
                                        <s:if test="roofRackQty!=roofRackQty_original&&(roofRackQty_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="roofRackQty_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Dual Control Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="dualControlFee" /></label></td>
                                    <td>
                                        <s:if test="dualControlFee!=dualControlFee_original&&(dualControlFee_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="dualControlFee_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Dual Control Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="dualControlQty" /></label></td>
                                    <td>
                                        <s:if test="dualControlQty!=dualControlQty_original&&(dualControlQty_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="dualControlQty_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Delivery Collection Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="deliveryCollectionFee" /></label></td>
                                    <td>
                                        <s:if test="deliveryCollectionFee!=deliveryCollectionFee_original&&(deliveryCollectionFee_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="deliveryCollectionFee_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Delivery Collection Fee Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="deliveryCollectionQty" /></label></td>
                                    <td>
                                        <s:if test="deliveryCollectionQty!=deliveryCollectionQty_original&&(deliveryCollectionQty_original!=null)">
                                            <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                            <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="deliveryCollectionQty_original" />)</label>
                                        </s:if>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </fieldset>
                </div>
            </div>
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
        </td>
    </tr>
</table>

