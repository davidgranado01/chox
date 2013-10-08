<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    $(function(){
        var form = $("#formUpdateThirdParty");

        var fsets =  $('legend',form);
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});

        var vcTPJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'text'},
                    {name:'value'}
                ]
        });

        var vcTPStore = new Ext.data.Store({
                proxy : new Ext.data.HttpProxy
                ({url : "<%= request.getContextPath()%>/prv/p/getAvailableVehicleClasses.action"}),
                reader : vcTPJsonReader
                ,listeners: {load: function() {
                    vcTPCombo.setValue('<s:property value="vehicleClass.id"/>');    
                }}
        });

        var vcTPCombo = new Ext.form.ComboBox({
                store: vcTPStore,
                renderTo: 'vcTPSelectionHolder',
                valueField: 'text',
                id: 'vcTPComboId',
                hiddenName: 'vehicleClassId',
                displayField:'value',
                typeAhead: true,
                autoWidth: true,
                listWidth: 100,
                width: 100,
                mode: 'local',
                triggerAction: 'all',
                forceSelection : true,
                emptyText: '--- SELECT ---'
        });
        vcTPStore.load();
        
        form.validate(
        {
            errorLabelContainer: "#thirdPartyMsgBox",
            rules: {
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
        $("#thirdPartySuccessBox").fadeOut(10000);
    }); 
</script>

<form id="formUpdateThirdParty" action="<%=request.getContextPath()%>/prv/p/updateThirdParty.action" class="XXentity-form">
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <fieldset class="x-fieldset partial">
        <legend>Third-Party Details</legend>
        <div class="form-container" id="thirdPartyDetailsWId">
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Title</label>
                <input type="text" class="chox-ttxt" id="thirdPartyTitleFieldId" name="title" value="<s:property value="title" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    First Name(s)</label>
                <input type="text" class="chox-ttxt" id="thirdPartyFirstNameFieldId" name="firstName" value="<s:property value="firstName" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Surname</label>
                <input type="text" class="chox-ttxt" id="thirdPartyLastNameFieldId" name="lastName" value="<s:property value="lastName" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 1</label>
                <input type="text" class="chox-ttxt" id="thirdPartyAddress1FieldId" name="address1" value="<s:property value="address1" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 2</label>
                <input type="text" class="chox-ttxt" id="thirdPartyAddress2FieldId" name="address2" value="<s:property value="address2" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 3</label>
                <input type="text" class="chox-ttxt" id="thirdPartyAddress3FieldId" name="address3" value="<s:property value="address3" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 4</label>
                <input type="text" class="chox-ttxt" id="thirdPartyAddress4FieldId" name="address4" value="<s:property value="address4" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 5</label>
                <input type="text" class="chox-ttxt" id="thirdPartyAddress5FieldId" name="address5" value="<s:property value="address5" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Postcode</label>
                <input type="text" class="chox-ttxt" id="thirdPartyPostCodeFieldId" name="postcode" value="<s:property value="postcode" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Telephone Day</label>
                <input type="text" class="chox-ttxt" id="thirdPartyTelephoneDayFieldId" name="telephoneDay" value="<s:property value="telephoneDay" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Telephone Evening</label>
                <input type="text" class="chox-ttxt" id="thirdPartyTelephoneEveningFieldId" name="telephoneEvening" value="<s:property value="telephoneEvening" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Email</label>
                <input type="text" class="chox-ttxt" id="thirdPartyEmailFieldId" name="email" value="<s:property value="email" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Insurer Brand</label>
                <input type="text" class="chox-ttxt" id="thirdPartyInsurerBandFieldId" name="insurerBrand" value="<s:property value="insurerBrand" />"/></div>

            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Policy Number<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" id="thirdPartyPolicyNumberFieldId" name="policyNumber" value="<s:property value="policyNumber" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Vehicle Manufacturer</label>
                <input type="text" class="chox-ttxt" id="thirdPartyVehicleManufacturerFieldId" name="vehicleManufacturer" value="<s:property value="vehicleManufacturer" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Vehicle Model</label>
                <input type="text" class="chox-ttxt" id="thirdPartyVehicleModelFieldId" name="vehicleModel" value="<s:property value="vehicleModel" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Vehicle Registration Number<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" id="thirdPartyVehicleRegistrationFieldId" name="vehicleRegistration" value="<s:property value="vehicleRegistration" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Vehicle Class</label>
                    <div id="vcTPSelectionHolder"></div>
            </div>
            <div class="chox-form-button"><input type="submit" id="thirdPartySubmitButtonId" value="Save Changes" /></div>
            <div id="thirdPartyMsgBox" class="action-error-msg"><s:property value="actionError" /></div>
            <div id="thirdPartySuccessBox" class="chox-form-submit-result"><s:property value="actionResult" /></div>
        </div>
    </fieldset>
    <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
    <!--s:token/-->
</form>