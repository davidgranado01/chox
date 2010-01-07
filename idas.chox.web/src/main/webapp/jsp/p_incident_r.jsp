<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset">
    <legend>Incident Details</legend>
    <div style="display:none" class="form-container">  
        <div class="chox-form-item">
            <label class="std-label-ro">Date / Time</label>
            <label class="std-data-ro"><s:date name="date" format="dd MMM yyyy kk:mm"  /></label>
        </div>
        <div class="chox-form-item">
            <label class="std-label-ro">Location</label>
            <label class="std-data-ro"><s:property value="location" /></label>
        </div>
        <div class="chox-form-item">
            <label class="std-label-ro">Police Involved?</label>
            <label class="std-data-ro"><s:property value="isPoliceInvolvedDesc" /></label>
        </div>
        <div class="chox-form-item">
            <label class="std-label-ro">Description</label>
            <div class="std-data-ro-big"><s:property value="incidentDescription" /></div>
        </div>         
    </div>
</fieldset>