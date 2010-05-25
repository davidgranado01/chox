<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

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
    </div>
</fieldset>
