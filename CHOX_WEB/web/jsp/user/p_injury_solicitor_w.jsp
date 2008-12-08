<%@ taglib uri="/struts-tags" prefix="s" %>


<form id="formupdateSolicitor" action="user/updateSolicitor.action" class="entity-form">
    <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
     <input type="hidden" name="injuryId" value='<s:property value="injuryId"/>'>
     <input type="hidden" name="incidentId" value='<s:property value="incidentId"/>'>
    <fieldset class="x-fieldset">
        <legend>Injury Solicitor</legend>
        <div style="display:none" class="form-container">
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Name</label>
            <input type="text" class="chox-ttxt" id="ISOLName" name="name" value="<s:property value="name" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Address 1</label>
            <input type="text" class="chox-ttxt" id="ISOLAddress1" name="address1" value="<s:property value="address1" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Address 2</label>
            <input type="text" class="chox-ttxt" id="ISOLAddress2" name="address2" value="<s:property value="address2" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Address 3</label>
            <input type="text" class="chox-ttxt" id="ISOLAddress3" name="address3" value="<s:property value="address3" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Address 4</label>
            <input type="text" class="chox-ttxt" id="ISOLAddress4" name="address4" value="<s:property value="address4" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Address 5</label>
            <input type="text" class="chox-ttxt" id="ISOLAddress5" name="address5" value="<s:property value="address5" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Postcode</label>
            <input type="text" class="chox-ttxt" id="ISOLPostcode" name="postcode" value="<s:property value="postcode" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Telephone Day</label>
            <input type="text" class="chox-ttxt" id="ISOLTelephone" name="telephone" value="<s:property value="telephone" />" /></div>
            <div class="chox-form-button">
                <input type="submit" value="Save Changes" />
            </div>
            <div class="chox-form-submit-result">&nbsp;</div>                                            
        </div>
    </fieldset>
</form>