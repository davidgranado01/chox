<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset">
    <legend>Hire Vehicle Details</legend>
    <div style="display:none" class="form-container" id="insurerHireMonitoringVehicleDetailRId">
        <table class="chox-table-form">
        <tr>
            <td><label class="std-label-ro">
                Replacement Vehicle Class</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro" id="insurerHireMonitorVehicleClassId"><s:property value="vehicleClass.name" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Hire Start</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro" id="insurerHireMonitorHireStartId"><s:date format="dd/MM/yyyy HH:mm" name="rentalStart" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Hire End</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro" id="insurerHireMonitorHireEndId"><s:date format="dd/MM/yyyy HH:mm" name="rentalEnd" /></label></td></tr>
        </table>
    </div>
</fieldset>
