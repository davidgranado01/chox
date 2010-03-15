<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset">
    <legend>Injury Solicitor</legend>
    <div style="display:none" class="form-container">
        <div class="chox-form-item">
            <label class="std-label-ro">
                Name</label>
            <label class="std-data-ro"><s:property value="solicitor.name" /></label></div>

        <div class="chox-form-item">
            <label class="std-label-ro">
                Address 1</label>
            <label class="std-data-ro"><s:property value="solicitor.address1" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Address 2</label>
            <label class="std-data-ro"><s:property value="solicitor.address2" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Address 3</label>
            <label class="std-data-ro"><s:property value="solicitor.address3" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Address 4</label>
            <label class="std-data-ro"><s:property value="solicitor.address4" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Address 5</label>
            <label class="std-data-ro"><s:property value="solicitor.address5" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Postcode</label>
            <label class="std-data-ro"><s:property value="solicitor.postcode" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Telephone Day</label>
            <label class="std-data-ro"><s:property value="solicitor.telephone" /></label></div>
    </div>
</fieldset>