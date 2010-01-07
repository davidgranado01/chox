<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset">
    <legend>Engineer Report</legend>
    <div style="display:none" class="form-container">
        <div class="chox-form-item">
            <label class="std-label-ro">
                Estimated Labour Amount</label>
            <label class="std-data-ro"><s:property value="labourAmount" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Estimated Total Repair Amount</label>
            <label class="std-data-ro"><s:property value="totalAmount" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Estimated Days Under Repair</label>
            <label class="std-data-ro"><s:property value="days" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Usable?</label>

            <label class="std-data-ro"><s:property value="isUsableDesc" /></label></div>

        <div class="chox-form-item">
            <label class="std-label-ro">
                Name</label>
            <label class="std-data-ro"><s:property value="name" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Company</label>
            <label class="std-data-ro"><s:property value="company" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Engineer Address 1</label>
            <label class="std-data-ro"><s:property value="address1" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Engineer Address 2</label>
            <label class="std-data-ro"><s:property value="address2" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Engineer Address 3</label>
            <label class="std-data-ro"><s:property value="address3" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Engineer Address 4</label>
            <label class="std-data-ro"><s:property value="address4" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Engineer Address 5</label>
            <label class="std-data-ro"><s:property value="address5" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Engineer Postcode</label>
            <label class="std-data-ro"><s:property value="postcode" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Engineer Telephone</label>
            <label class="std-data-ro"><s:property value="telephone" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Engineer Email</label>
            <label class="std-data-ro"><s:property value="email" /></label></div>
    </div>
</fieldset>
