<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset">
    <legend>Hire Vehicle Details</legend>
    <div style="display:none" class="form-container">
        <div class="chox-form-item">
            <label class="std-label-ro">
                Manufacturer</label>
            <label class="std-data-ro"><s:property value="vehicleManufacturer" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Model</label>
            <label class="std-data-ro"><s:property value="vehicleModel" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Registration</label>
            <label class="std-data-ro"><s:property value="vehicleRegistration" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Replacement Vehicle Class</label>
            <label class="std-data-ro"><s:property value="vehicleClass.name" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Hire Start</label>
            <label class="std-data-ro"><s:date format="dd/MM/yyyy kk:mm" name="rentalStart" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Hire End</label>
            <label class="std-data-ro"><s:date format="dd/MM/yyyy kk:mm" name="rentalEnd" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Reason For Collection</label>
            <label class="std-data-ro"><s:property value="collectionReason" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                No. Days Hire</label>
            <label class="std-data-ro"><s:property value="days" /></label>
        </div>               
    </div>
</fieldset>
