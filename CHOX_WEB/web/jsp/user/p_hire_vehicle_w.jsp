<%@ taglib uri="/struts-tags" prefix="s" %>
<form id="formUpdateIncident" action="user/updateVehicleHire.action" class="entity-form">
    <input type="hidden" name="objectId" value='<s:property value="id"/>'>
    <fieldset class="x-fieldset">
        <legend>Hire Vehicle Details</legend>
        <div style="display:none" class="form-container">
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Manufacturer</label>
            <input type="text" class="chox-ttxt" id="HVDManufacturer" name="date" value='<s:property value="date" />' /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Model</label>
            <input type="text" class="chox-ttxt" id="HVDModel" name="date" value='<s:property value="date" />' /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Registration</label>
            <input type="text" class="chox-ttxt" id="HVDRegistration"  name="date" value='<s:property value="date" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Replacement Vehicle Class</label>
            <input type="text" class="chox-ttxt" id="HVDReplacementVehicleClass"  name="date" value='<s:property value="date" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Hire Start</label>
            <input type="text" class="chox-ttxt" id="HVDHireStart" name="date" value='<s:property value="date" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Hire End</label>
            <input type="text" class="chox-ttxt" id="HVDHireEnd" name="date" value='<s:property value="date" />' /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Reason For Collection</label>
            <input type="text" class="chox-ttxt" id="HVDReasonForCollection" name="date" value='<s:property value="date" />' /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                No. Days Hire</label>
            <input type="text" class="chox-ttnum" id="HVDNNumberOfDaysHire" name="date" value='<s:property value="date" />' /></div>
            <div class="chox-form-button">
                <input type="submit" value="Save Changes" />
            </div>
            <div class="chox-form-submit-result">&nbsp;</div>                                              
        </div>
    </fieldset>
</form> 