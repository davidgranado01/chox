<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset">
    <legend>Hire Monitoring</legend>
    <div style="display:none" class="form-container">

        <div class="chox-form-item">
            <label class="std-label-ro">Next Review Date</label>
            <label class="std-data-ro"><s:date format="dd/MM/yyyy" name="nextReviewDate" /></label></div>

        <div class="chox-form-item">
            <label class="std-label-ro">
                Original ECD</label>
            <label class="std-data-ro"><s:property value="customer.InitialECDDesc" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Name Of Repairer
            </label>
            <label class="std-data-ro"><s:property value="nameOfRepairer" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Repair Book In Date</label>
            <label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairBookInDate" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Inspection Booked Date</label>
            <label class="std-data-ro"><s:date format="dd/MM/yyyy" name="inspectionBookedDate" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Inspection Date</label>
            <label class="std-data-ro"><s:date format="dd/MM/yyyy" name="inspectionDate" /></label></div>

        <div class="chox-form-item">
            <label class="std-label-ro">
                Is Total Loss?</label>
            <label class="std-data-ro"><s:property value="isTotalLossDesc" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Repair Completion Date</label>
            <label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairCompletionDate" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Name of IME</label>
            <label class="std-data-ro"><s:property value="nameOfIme" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Labour Rate (Per Hour)</label>
            <label class="std-data-ro">£<s:property value="labourRate" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Labour Hours</label>
            <label class="std-data-ro"><s:property value="labourHour" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Total Labour Cost</label>
            <label class="std-data-ro">£<s:property value="labourCost" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">Labour Information Non-Provision Reason</label>
            <label class="std-data-ro"><s:property value="nonProvisionReason" /></label></div>

    </div>
</fieldset>
