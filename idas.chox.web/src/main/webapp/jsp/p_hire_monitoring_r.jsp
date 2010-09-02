<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset">
    <legend>Hire Monitoring</legend>
    <div style="display:none" class="form-container" id="hireMonitoringRId">
        <table class="chox-table-form">
        <tr>
            <td><label class="std-label-ro">Next Review Date</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="nextReviewDate" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Original ECD</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="customer.InitialECDDesc" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Name Of Repairer
            </label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="nameOfRepairer" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Inspection Booked Date</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="inspectionBookedDate" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Inspection Date</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="inspectionDate" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Date Repair Authorised</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairAuthorisedDate" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Repair Book In Date</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairBookInDate" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Date Repair Commenced</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairCommencedDate" /></label></td></tr>

        <tr>
            <td><label class="std-label-ro">
                Is Total Loss?</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="isTotalLossDesc" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Date Total Loss Offer Made</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferMadeDate" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Date Total Loss Offer Accepted</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferAcceptedDate" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Date Total Loss Cheque Issued</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferCheckIssuedDate" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Date Total Loss Cheque Received</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferCheckReceivedDate" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Repair Completion Date</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairCompletionDate" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Name of IME</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="nameOfIme" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Labour Rate (Per Hour)</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="labourRate" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Labour Hours</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="labourHour" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Total Labour Cost</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="labourCost" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">Labour Information Non-Provision Reason</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="nonProvisionReason" /></label></td>
        </tr>
        </table>
    </div>
</fieldset>
