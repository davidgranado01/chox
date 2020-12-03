<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset">
    <legend>Hire Monitoring</legend>
    <div style="display:none" class="form-container" id="insurerHireMonitoringRId">
        <table class="chox-table-form">
            <tr>
                <td><label class="std-label-ro">Date TP Reported Incident to TPI</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="TPReportedIncidentToTPIDate" /> </label></td>
            </tr>

            <tr>
                <td><label class="std-label-ro">Inspection Booked Date</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="inspectionBookedDate" /> </label></td>
            </tr>

            <tr>
                <td><label class="std-label-ro">Inspection Date</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="inspectionDate" /></label></td>
            </tr>

            <tr>
                <td><label class="std-label-ro">Date Repair Authorised or TL Identified</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairAuthorisedDate" /></label></td>
            </tr>

            <tr>
                <td><label class="std-label-ro">Date TL Report Sent To Us</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="TLReportSentToUsDate" /></label></td>
            </tr>

            <tr>
                <td><label class="std-label-ro">Repair Book In Date</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairBookInDate" /></label></td>
            </tr>

            <tr>
                <td><label class="std-label-ro">Date Parts Received</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="partsReceivedDate" /></label></td>
            </tr>

            <tr>
                <td><label class="std-label-ro">Date Repair Commenced</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairCommencedDate" /></label></td>
            </tr>

            <tr>
                <td><label class="std-label-ro">Repair Completion Date</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairCompletionDate" /></label></td>
            </tr>

            <tr>
                <td><label class="std-label-ro">Is Total Loss?</label></td>
                <td>&nbsp;</td>
                <td><label id="hireMonitoringTotalLossId" class="std-data-ro"><s:property value="isTotalLossDesc" /></label></td>
            </tr>

            <tr>
                <td><label class="std-label-ro">Date Engineers Report Sent</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="engineersReportSentDate" /></label></td>
            </tr>

            <tr>
                <td><label class="std-label-ro">Date Total Loss Offer Made</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferMadeDate" /></label></td>
            </tr>

            <tr>
                <td><label class="std-label-ro">Date Total Loss Offer Accepted</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferAcceptedDate" /></label></td>
            </tr>

            <tr>
                <td><label class="std-label-ro">Date Total Loss Payment Issued</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferPaymentIssuedDate" /></label></td>
            </tr>

             <tr>
                <td><label class="std-label-ro">Payment Type</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="paymentType" /></label></td>
            </tr>

            <tr>
                <td><label class="std-label-ro">Date Total Loss Payment Received</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferPaymentReceivedDate" /></label></td>
            </tr>
            
            <tr>
                <td><label class="std-label-ro">Labour Rate (Per Hour)</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro">£<s:property value="labourRate" /></label></td>
            </tr>
            
            <tr>
                <td><label class="std-label-ro">Labour Hours</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="labourHour" /></label></td>
            </tr>
            
            <tr>
                <td><label class="std-label-ro">Total Labour Cost</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro">£<s:property value="labourCost" /></label></td>
            </tr>

            <tr>
                <td><label class="std-label-ro">Is the Claimant Impecunious?</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="claimantImpecuniousDesc" /></label></td>
            </tr>
            <tr>
                <td><label class="std-label-ro">Who managed the repair?</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="whoManagedRepair" /></label></td>
            </tr>
        </table>
    </div>
</fieldset>
