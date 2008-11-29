<%@ taglib uri="/struts-tags" prefix="s" %>



<fieldset class="x-fieldset">
    <legend><input type="checkbox" class="jq-toggle-fieldset" checked="checked"/>Incident Details</legend>
    <div>
        <div class="chox-form-item">
            <label class="chox-form-std-label">
            Date / Time</label>
        <label class="chox-label-ro"><s:property value="date" /></label></div>
        <div class="chox-form-item">
            <label class="chox-form-std-label">
            Location</label>
        <label class="chox-label-ro"><s:property value="location" /></label></div>
        <div class="chox-form-item">
            <label class="chox-form-std-label">
            Police Involved?</label>
        <label class="chox-label-ro">todo: map prop to Yes / No</label></div>
    <div class="chox-form-item">
        <label class="chox-form-std-label">
        Description</label>
        <textarea class="chox-tta" id="IDDescription" cols="20" rows="5" name="incidentDescription"></textarea>
        <s:property value="incidentDescription" />
    </div>                       
    </div>
</fieldset>

