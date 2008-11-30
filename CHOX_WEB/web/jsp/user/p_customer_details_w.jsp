       
<%@ taglib uri="/struts-tags" prefix="s" %>
<form id="formUpdateIncident" action="user/updateCustomer.action" class="entity-form">
    <input type="hidden" name="objectId" value='<s:property value="id"/>'>
   
            <fieldset class="x-fieldset">
                <legend>Customer Details</legend>
                <div style="display:none" class="form-container">
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Title</label>
                    <input type="text" class="chox-ttxt" id="CCDTitle" value='<s:property value="title" />'/></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        First Name(s)</label>
                    <input type="text" class="chox-ttxt" id="CCDFirstName" name="firstName" value='<s:property value="firstName" />'/></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Surname</label>
                    <input type="text" class="chox-ttxt" id="CCDSurname" name="lastName"  value='<s:property value="lastName" />' /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Address 1</label>
                    <input type="text" class="chox-ttxt" id="CCDAddress1"  name="address1" value='<s:property value="address1" />' /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Address 2</label>
                    <input type="text" class="chox-ttxt" id="CCDAddress2" name="address2" value='<s:property value="address2" />' /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Address 3</label>
                    <input type="text" class="chox-ttxt" id="CCDAddress3" name="address3" value='<s:property value="address3" />'/></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Address 4</label>
                    <input type="text" class="chox-ttxt" id="CCDAddress4" name="address4" value='<s:property value="address4" />' /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Address 5</label>
                    <input type="text" class="chox-ttxt" id="CCDAddress5" name="address5" value='<s:property value="address5" />' /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Postcode</label>
                    <input type="text" class="chox-ttxt" id="CCDPostcode" name="postcode" value='<s:property value="postcode" />' /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Telephone Day</label>
                    <input type="text" class="chox-ttxt" id="CCDTeleponeDay" name="telephoneDay" value='<s:property value="telephoneDay" />' /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Telepone Evening</label>
                    <input type="text" class="chox-ttxt" id="CCDTeleponeEvening" name="telephoneEvening"  value='<s:property value="telephoneEvening" />'/></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Email</label>
                    <input type="text" class="chox-ttxt" id="CCDEmail" name="email" value='<s:property value="email" />' /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Insurer</label>
                        NOT IMPLEMENTED</div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Policy Number</label>
                    <input type="text" class="chox-ttxt" id="CCDPolicyNumber" name="policyNumber" value='<s:property value="policyNumber" />' /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                            Claim Number
                        </label>
                    <input type="text" class="chox-ttxt" id="CCDClaimNumber" name="claimReference" value='<s:property value="claimReference" />' /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Comprehensive</label>
                    <s:checkbox name="comprehensive" /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Vehicle Manufacturer</label>
                    <input type="text" class="chox-ttxt" id="CCDVehicleManufacturer" name="vehicleManufacturer" value='<s:property value="vehicleManufacturer" />' /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Vehicle Class</label>
                    NOT IMPLEMENTED</div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Vehicle Location</label>
                    <input type="text" class="chox-ttxt" id="CCDVehicleLocation"  name="location" value='<s:property value="location" />' /></div>
                    <div class="chox-form-button">
                        <input type="submit" value="Save Changes" /><div class="chox-form-submit-result"></div>
                    </div>   
                </div>
            </fieldset>
        </form>
