<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    $(function(){
        var form = $("#formUpdateThirdParty");

        var fsets =  $('legend',form);
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});

        form.validate(
        {
            errorLabelContainer: "#thirdPartyMsgBox",
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
                insurerId:{
                    min:1
                },
                policyNumber:{
                    required:true
                },
                vehicleRegistration:{
                    required:true
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
                insurerId:{
                    min: "You must select an Insurer"
                },
                policyNumber: {
                    required:"You must supply a value for 'Policy Number'"
                },
                vehicleRegistration: {
                    required:"You must supply a value for 'Vehicle Registration'"
                },
                email: {
                    email:"You must supply a valid email address for 'Email'"
                }
            }
        });
        ui.ajaxForm(form,null,'html');
    }); 
</script>

<form id="formUpdateThirdParty" action="<%=request.getContextPath()%>/prv/p/updateThirdParty.action" class="XXentity-form">
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <input name="currentVersion" type="hidden" value="<s:property value="version" />" />
    <fieldset class="x-fieldset partial">
        <legend>Third-Party Details</legend>
        <div class="form-container" id="thirdPartyDetailsWId">
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Title<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" name="title" value="<s:property value="title" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    First Name(s)<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" name="firstName" value="<s:property value="firstName" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Surname<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" name="lastName" value="<s:property value="lastName" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 1</label>
                <input type="text" class="chox-ttxt" name="address1" value="<s:property value="address1" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 2</label>
                <input type="text" class="chox-ttxt" name="address2" value="<s:property value="address2" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 3</label>
                <input type="text" class="chox-ttxt" name="address3" value="<s:property value="address3" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 4</label>
                <input type="text" class="chox-ttxt" name="address4" value="<s:property value="address4" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 5</label>
                <input type="text" class="chox-ttxt" name="address5" value="<s:property value="address5" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Postcode</label>
                <input type="text" class="chox-ttxt" name="postcode" value="<s:property value="postcode" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Telephone Day</label>
                <input type="text" class="chox-ttxt" name="telephoneDay" value="<s:property value="telephoneDay" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Telephone Evening</label>
                <input type="text" class="chox-ttxt" name="telephoneEvening" value="<s:property value="telephoneEvening" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Email</label>
                <input type="text" class="chox-ttxt" name="email" value="<s:property value="email" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Insurer Brand</label>
                <input type="text" class="chox-ttxt" name="insurerBrand" value="<s:property value="insurerBrand" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Insurer<span class="mandatory">*</span></label>
                    <s:select name="insurerId"
                              list="insurers"
                              listKey="id"
                              listValue="name"
                              headerKey="-1"
                              headerValue="--SELECT--"
                              emptyOption="false"></s:select></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Policy Number<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" name="policyNumber" value="<s:property value="policyNumber" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Vehicle Manufacturer</label>
                <input type="text" class="chox-ttxt" name="vehicleManufacturer" value="<s:property value="vehicleManufacturer" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Vehicle Model</label>
                <input type="text" class="chox-ttxt" name="vehicleModel" value="<s:property value="vehicleModel" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Vehicle Registration Number<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" name="vehicleRegistration" value="<s:property value="vehicleRegistration" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Vehicle Class</label>
                    <s:select name="vehicleClassId"
                              list="vehicleClasses"
                              listKey="id"
                              listValue="name"
                              headerKey="-1"
                              headerValue="--SELECT--"
                              emptyOption="false"></s:select>
            </div>
            <div class="chox-form-button"><input type="submit" value="Save Changes" /></div>
            <div id="thirdPartyMsgBox" class="action-error-msg"><s:property value="actionError" /></div>
            <div class="chox-form-submit-result"><s:property value="actionResult" /></div>
        </div>
    </fieldset>
</form>