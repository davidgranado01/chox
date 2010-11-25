<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
  <table cellpadding="0" cellspacing="0" border="0" width="100%">
    <tr valign="top">
        <td class="chox-form-left-col">
            <div>
                <div id="formUpdateInvoiceForm"  class="XXentity-form">
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
                </div>
            </div>
            <div>
                <div id="formUpdateHireVehicle" class="XXentity-form">
                    <fieldset class="x-fieldset">
                        <legend>Hire Vehicle Details</legend>
                        <div style="display:none" class="form-container" id="hireVehicleDetailRId">
                            <table class="chox-table-form">
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
                                    <td><label class="std-data-ro"><s:property value="vehicleClass.name" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Hire Start</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy HH:mm" name="rentalStart" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Hire End</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy HH:mm" name="rentalEnd" /></label></td></tr>
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
                                            CDW Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="cdwFee" /></label></td>
                                </tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            CDW Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="cdwQty" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Automatic Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="automaticFee" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Automatic Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="automaticQty" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Additional Driver Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="additionalDriverFee" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Additional Driver Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="additionalDriverQty" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Sat Nav Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="satNavFee" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Sat Nav Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="satNavQty" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Estate Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="estateFee" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Estate Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="estateQty" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Baby Seat Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="babySeatFee" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Baby Seat Quantity
                                        </label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="babySeatQty" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Tow Bars Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="towBarsFee" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Tow Bars Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="towBarsQty" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Non-standard Risk Ins. Premium Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="nonStandardInsurancePremiumFee" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Non-standard Risk Ins. Premium Qty</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="nonStandardInsurancePremiumQty" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Cover Note Required For<br/>Customer's Own Insurance Policy?</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="coverNoteRequiredDesc" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Admin Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="adminFee" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Admin Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="adminQty" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Roof Rack Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="roofRackFee" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Roof Rack Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="roofRackQty" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Dual Control Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="dualControlFee" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Dual Control Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="dualControlQty" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Delivery Collection Fee</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro">£<s:property value="deliveryCollectionFee" /></label></td></tr>
                                <tr>
                                    <td><label class="std-label-ro">
                                            Delivery Collection Fee Quantity</label></td>
                                    <td>&nbsp;</td>
                                    <td><label class="std-data-ro"><s:property value="deliveryCollectionQty" /></label></td></tr>
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

