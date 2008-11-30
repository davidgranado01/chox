
<%@ taglib uri="/struts-tags" prefix="s" %>

<form id="formUpdateIncident" action="user/updateThirdParty.action" class="entity-form">
    <input type="hidden" name="objectId" value='<s:property value="id"/>'>
    <fieldset class="x-fieldset">
        <legend>Third-Party Details</legend>
        <div style="display:none" class="form-container">
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Title</label>
            <input type="text" class="chox-ttxt" name="titl" value='<s:property value="name" />'/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                First Name(s)</label>
            <input type="text" class="chox-ttxt" name="name" value='<s:property value="name" />'/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Surname</label>
            <input type="text" class="chox-ttxt" name="name" value='<s:property value="name" />'/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Address 1</label>
            <input type="text" class="chox-ttxt" name="name" value='<s:property value="name" />'/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Address 2</label>
            <input type="text" class="chox-ttxt" name="name" value='<s:property value="name" />'/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Address 3</label>
            <input type="text" class="chox-ttxt" name="name" value='<s:property value="name" />'/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Address 4</label>
            <input type="text" class="chox-ttxt" name="name" value='<s:property value="name" />'/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Address 5</label>
            <input type="text" class="chox-ttxt" name="name" value='<s:property value="name" />'/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Postcode</label>
            <input type="text" class="chox-ttxt" name="name" value='<s:property value="name" />'/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Telephone Day</label>
            <input type="text" class="chox-ttxt" name="name" value='<s:property value="name" />'/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Telepone Evening</label>
            <input type="text" class="chox-ttxt" name="name" value='<s:property value="name" />'/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Email</label>
            <input type="text" class="chox-ttxt" name="name" value='<s:property value="name" />'/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Insurer</label>
            <input type="text" class="chox-ttxt" name="name" value='<s:property value="name" />'/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Policy Number</label>
            <input type="text" class="chox-ttxt" name="name" value='<s:property value="name" />'/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Claim Number
                </label>
            <input type="text" class="chox-ttxt" name="name" value='<s:property value="name" />'/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Vehicle Manufacturer</label>
            <input type="text" class="chox-ttxt" name="name" value='<s:property value="name" />'/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Vehicle Model</label>
            <input type="text" class="chox-ttxt" name="name" value='<s:property value="name" />'/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Vehicle Registration Number</label>
            <input type="text" class="chox-ttxt" name="name" value='<s:property value="name" />'/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Vehicle Class</label>
            <input type="text" class="chox-ttxt" name="name" value='<s:property value="name" />'/></div>   
            <div class="chox-form-button">
                <input type="submit" value="Save Changes" /><div class="chox-form-submit-result"></div>
            </div> 
            
        </div>
    </fieldset>
</form>