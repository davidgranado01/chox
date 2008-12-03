<%@ taglib uri="/struts-tags" prefix="s" %>
<form id="formUpdateIncident" action="user/updateVehicleHire.action" class="entity-form">
    <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <fieldset class="x-fieldset">
        <legend>Hire Vehicle Details</legend>
        <div style="display:none" class="form-container">
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Manufacturer</label>
            <input type="text" class="chox-ttxt" id="HVDManufacturer" name="vehicleManufacturer" value='<s:property value="vehicleManufacturer" />' /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Model</label>
            <input type="text" class="chox-ttxt" id="HVDModel" name="vehicleModel" value='<s:property value="vehicleModel" />' /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Registration</label>
            <input type="text" class="chox-ttxt" id="HVDRegistration"  name="vehicleRegistration" value='<s:property value="vehicleRegistration" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Replacement Vehicle Class</label>
            <s:select name="vehicleClassId" list="vehicleClasses" listKey="id" listValue="name" headerKey="-1"
                                                                                           headerValue="--- ALL ---"
                                                                                       emptyOption="false"></s:select>
            
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Hire Start</label>
            <input type="text" class="chox-ttxt" id="HVDHireStart" name="rentalStart" value='<s:property value="rentalStart" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Hire End</label>
            <input type="text" class="chox-ttxt" id="HVDHireEnd" name="rentalEnd" value='<s:property value="rentalEnd" />' /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Reason For Collection</label>
            <input type="text" class="chox-ttxt" id="HVDReasonForCollection" name="collectionReason" value='<s:property value="collectionReason" />' /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                No. Days Hire</label>
            <input type="text" class="chox-ttnum" id="HVDNNumberOfDaysHire" name="days" value='<s:property value="days" />' /></div>
            <div class="chox-form-button">
                <input type="submit" value="Save Changes" />
            </div>
            <div class="chox-form-submit-result">&nbsp;</div>                                              
        </div>
    </fieldset>
</form> 