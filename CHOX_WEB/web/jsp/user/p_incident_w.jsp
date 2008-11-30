
<%@ taglib uri="/struts-tags" prefix="s" %>


<form id="formUpdateIncident" action="user/updateIncident.action" class="entity-form">
    <input type="hidden" name="objectId" value='<s:property value="id"/>'>
    <fieldset class="x-fieldset">
        <legend>Incident Details</legend>
        <div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Date / Time</label>
            <input type="text" class="chox-ttxt" id="IDDateTime" name="date" value='<s:property value="date" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Location</label>
            <input type="text" class="chox-ttxt" id="IDLocation" name="location" value='<s:property value="location" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Police Involved?</label>
                <s:checkbox name="isPoliceInvolved" />
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Description</label>
                <textarea class="chox-tta" id="IDDescription" cols="20" rows="5" name="incidentDescription"></textarea>
            </div>
            <div class="chox-form-button">
                <input type="submit" value="Save Changes" /><div class="chox-update-result"></div>
            </div>                        
        </div>
    </fieldset>
</form>    