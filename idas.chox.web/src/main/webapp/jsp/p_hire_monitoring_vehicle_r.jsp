<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset">
    <legend>Hire Vehicle Details</legend>
    <div style="display:none" class="form-container" id="hireMonitoringVehicleDetailRId">
        <table class="chox-table-form">
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
        </table>
    </div>
</fieldset>
