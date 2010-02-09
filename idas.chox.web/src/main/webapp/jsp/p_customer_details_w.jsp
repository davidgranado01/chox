<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    $(function(){

        var form = $("#formUpdateCustomerDetails");

        var fsets =  $('legend',form);
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});

        form.validate(
        {
            errorLabelContainer: "#CDmessageBox",
            rules: {
                title:{
                    required:true
                },
                firstName:{
                    required:true
                },
                lastName:{
                    required:true
                },
                address1:{
                    required:true
                },
                postcode:{
                    required:true
                },
                telephoneDay:{
                    required:true
                },
                insurerName:{
                    required:true
                },
                vehicleClassId:{
                    min:1
                },
                vehicleManufacturer:{
                    required:true
                },
                vehicleModel:{
                    required:true
                },
                policyNumber:{
                    required:true
                },
                vehicleRegistration:{
                    required:true
                },
                location:{
                    required:true
                },
                age:{
                    number:true
                },
                email:{
                    email:true
                }
            },
            messages: {
                title: {
                    required:"You must supply a value for 'Title'"
                },
                firstName: {
                    required:"You must supply a value for 'First Name'"
                },
                lastName: {
                    required:"You must supply a value for 'Last Name'"
                },
                address1: {
                    required:"You must supply a value for 'Address 1'"
                },
                postcode: {
                    required:"You must supply a value for 'Postcode'"
                },
                telephoneDay: {
                    required:"You must supply a value for 'Telephone Day'"
                },
                vehicleClassId:{
                    min: "You must select a Vehicle Class"
                },
                insurerName:{
                    required:"You must supply a value for 'Insurer'"
                },
                vehicleManufacturer: {
                    required:"You must supply a value for 'Vehicle Manufacturer'"
                },
                vehicleModel: {
                    required:"You must supply a value for 'Vehicle Model'"
                },
                policyNumber: {
                    required:"You must supply a value for 'Policy Number'"
                },
                vehicleRegistration: {
                    required:"You must supply a value for 'Vehicle Registration'"
                },
                location: {
                    required:"You must supply a value for 'Vehicle Location'"
                }, age:{
                    number:"You must supply a numeric value for 'Age'"
                },
                email :{
                    email:"You must supply a valid email address for 'Email'"
                }
            }
        });
        ui.ajaxForm(form,null,'html');
    });

</script>

<form id="formUpdateCustomerDetails" action="<%=request.getContextPath()%>/prv/p/updateCustomer.action" class="XXentity-form">
    <input name="claimId" type="hidden" value="<s:property value="claimId" />" />
    <input name="currentVersion" type="hidden" value="<s:property value="version" />" />
    <fieldset  style="display:none" class="x-fieldset partial" >
        <legend>Customer Details</legend>
        <div class="form-container">
            <div class="chox-form-item">
                <label class="chox-form-std-label">Title<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" id="CCDTitle" name="title" value="<s:property value="title" />"/>
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    First Name(s)<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" id="CCDFirstName" name="firstName" value="<s:property value="firstName" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Surname<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" id="CCDSurname" name="lastName"  value="<s:property value="lastName" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 1<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" id="CCDAddress1"  name="address1" value="<s:property value="address1" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 2</label>
                <input type="text" class="chox-ttxt" id="CCDAddress2" name="address2" value="<s:property value="address2" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 3</label>
                <input type="text" class="chox-ttxt" id="CCDAddress3" name="address3" value="<s:property value="address3" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 4</label>
                <input type="text" class="chox-ttxt" id="CCDAddress4" name="address4" value="<s:property value="address4" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 5</label>
                <input type="text" class="chox-ttxt" id="CCDAddress5" name="address5" value="<s:property value="address5" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Postcode<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" id="CCDPostcode" name="postcode" value="<s:property value="postcode" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Telephone Day<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" id="CCDTeleponeDay" name="telephoneDay" value="<s:property value="telephoneDay" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Telephone Evening</label>
                <input type="text" class="chox-ttxt" id="CCDTeleponeEvening" name="telephoneEvening"  value="<s:property value="telephoneEvening" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Email</label>
                <input type="text" class="chox-ttxt" id="CCDEmail" name="email" value="<s:property value="email" />" /></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">Age</label>
                <input type="text" class="chox-ttxt" id="CCDAge"  name="age" value="<s:property value="age" />" />
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">Occupation</label>
                <input type="text" class="chox-ttxt" id="CCDOccupation"  name="occupation" value="<s:property value="occupation" />" />
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">Policy Usage</label>
                <input type="text" class="chox-ttxt" id="CCDPolicyUsage"  name="policyUsage" value="<s:property value="policyUsage" />" />
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Insurer<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" id="CCDInsurerName" name="insurerName" value="<s:property value="insurerName" />" /></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Policy Number<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" id="CCDPolicyNumber" name="policyNumber" value="<s:property value="policyNumber" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Claim Number
                </label>
                <input type="text" class="chox-ttxt" id="CCDClaimNumber" name="claimReference" value="<s:property value="claimReference" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Comprehensive</label>
            <s:checkbox name="comprehensive" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Vehicle Manufacturer<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" id="CCDVehicleManufacturer" name="vehicleManufacturer" value="<s:property value="vehicleManufacturer" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Vehicle Model<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" id="CCDVehicleModel" name="vehicleModel" value="<s:property value="vehicleModel" />" />
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Vehicle Class<span class="mandatory">*</span></label>
                    <s:select name="vehicleClassId"
                              list="vehicleClasses"
                              listKey="id"
                              listValue="name"
                              headerKey="-1"
                              headerValue="--SELECT--"
                              emptyOption="false"></s:select>
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Vehicle Registration Number<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" id="CCDVehicleRegistration" name="vehicleRegistration" value="<s:property value="vehicleRegistration" />" />
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">Vehicle Location<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" id="CCDVehicleLocation"  name="location" value="<s:property value="location" />" />
            </div>

            <div class="chox-form-button">
                <input type="submit" value="Save Changes" />
            </div>
            <div id="CDmessageBox" class="action-error-msg"><s:property value="actionError" /></div>
            <div class="chox-form-submit-result"><s:property value="actionResult" /></div>
        </div>
    </fieldset>
</form>
