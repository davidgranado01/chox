<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset">
    <legend>Invoice Detail</legend>

    <div style="display:none" class="form-container" id="invoiceDetailRId">
        <table class="chox-table-form">
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
        </tr>
        <tr>
            <td><label class="std-label-ro">Hire Net</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="hireNet" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Hire VAT</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="hireVat" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Hire Gross</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="hireGross" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Repair Net</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="repairNet" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Repair VAT</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="repairVat" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Repair Gross</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="repairGross" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Engineer Fee Net</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="engineerFeeNet" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Engineer Fee VAT</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="engineerFeeVat" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Engineer Fee Gross</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="engineerFeeGross" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Total Loss Fee Net</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="totalLossFeeNet" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Total Loss Fee VAT</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="totalLossFeeVat" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Total Loss Fee Gross</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="totalLossFeeGross" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Storage Recovery Net</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="storageRecoveryNet" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Storage Recovery VAT</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="storageRecoveryVat" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Storage Recovery Gross</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="storageRecoveryGross" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Total Net</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="totalNet" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Total VAT</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="totalVat" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Total Gross</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="totalGross" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Claims Handling Invoice Amount</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="claimsHandlingInvoiceAmount" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Deduction For Claims Handling Fee</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="deductionForClaimsHandlingFee" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Discount</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="discount" /></label></td>
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
        </tr>
        <tr>
            <td><label class="std-label-ro-big">Total To Pay</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="totalToPay" /></label></td>
        </tr>
        <s:if test="interimPaymentReceived">
          <tr>
            <td colspan="3"><label class="std-label-ro-small">Note that the interim payment has NOT been deducted from the 'Total To Pay'</label></td>
          </tr>
          <tr>
            <td><label class="std-label-ro">Interim Payment</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro-red">£<s:property value="interimPayment"/> (Payment Received)</label></td>
          </tr>
        </s:if>
        <s:elseif test="!interimPaymentReceived && interimPayment">
          <tr>
            <td colspan="3"><label class="std-label-ro-small">Note that the interim payment has NOT been deducted from the 'Total To Pay'</label></td>
          </tr>
          <tr>
            <td><label class="std-label-ro">Interim Payment</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro-red">£<s:property value="interimPayment" /> (Not Yet Received)</label></td>
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
        </tr>
        <tr>
            <td><label class="std-label-ro">Invoice Uploaded Date</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:date name="createdDate" format="dd MMM yyyy" /></label>
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
