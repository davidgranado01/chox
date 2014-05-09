<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    Ext.onReady(function() {

        var form = $("#formUpdateCustomerDetails");

        var fsets =  $('legend',form);
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});

        var vcCustomerJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'text'},
                    {name:'value'}
                ]
        });

        var vcCustomerStore = new choxDataStore({
                url : "/prv/p/getAvailableVehicleClasses.action",
                reader : vcCustomerJsonReader
                ,listeners: {load: function() {
                    vcCustomerCombo.setValue('<s:property value="vehicleClass.id"/>');    
                }}
        });

        var vcCustomerCombo = new Ext.form.ComboBox({
                store: vcCustomerStore,
                renderTo: 'vcCustomerSelectionHolder',
                valueField: 'text',
                id: 'vcCustomerComboId',
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
        vcCustomerStore.load();    
        
        $.validator.addMethod("vcCustomerSelectionRule",
            function(value) {
                if(value === "" || value < 1) {
                    return false;
                }
                return true;
            }
        );
            
        form.validate(
        {
            ignore: [],
            errorLabelContainer: "#CDmessageBox",
            rules: {
<s:if test="!isInsurer">
                title:{
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
                insurerName:{
                    required:true
                },
                policyNumber:{
                    required:true
                },
                location:{
                    required:true
                },
</s:if>
                vehicleClassId:{
                    vcCustomerSelectionRule : true
                },
                vehicleManufacturer:{
                    required:true
                },
                vehicleModel:{
                    required:true
                },
                vehicleRegistration:{
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
<s:if test="!isInsurer">
                title: {
                    required:"You must supply a value for 'Title'"
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
                insurerName:{
                    required:"You must supply a value for 'Insurer'"
                },
                policyNumber: {
                    required:"You must supply a value for 'Policy Number'"
                },
                location: {
                    required:"You must supply a value for 'Vehicle Location'"
                },
</s:if>
                vehicleClassId:{
                    vcCustomerSelectionRule: "You must select a Vehicle Class"
                },
                vehicleManufacturer: {
                    required:"You must supply a value for 'Vehicle Manufacturer'"
                },
                vehicleModel: {
                    required:"You must supply a value for 'Vehicle Model'"
                },
                vehicleRegistration: {
                    required:"You must supply a value for 'Vehicle Registration'"
                },
                age:{
                    number:"You must supply a numeric value for 'Age'"
                },
                email :{
                    email:"You must supply a valid email address for 'Email'"
                }
            }
            
        });
        ui.ajaxForm(form,null,'html');
        $("#CDSuccessBox").fadeOut(10000);
        
        $('#CDSuccessBox').change(function() {
        	  alert('Handler for .change() called.');
        });
    });
    
</script>

<form id="formUpdateCustomerDetails" action="<%=request.getContextPath()%>/prv/p/updateCustomer.action" class="XXentity-form">
    <input name="claimId" type="hidden" value="<s:property value="claimId" />" />
    <fieldset class="x-fieldset partial" >
        <legend>Customer Details</legend>
        <div class="form-container" id="customerDetailsWId">
            <div class="chox-form-item">
                <label class="chox-form-std-label">Title<s:if test="!isInsurer"><span class="mandatory">*</span></s:if></label>
                <input type="text" class="chox-ttxt" id="CCDTitle" name="title" value="<s:property value="title" />"/>
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    First Name(s)</label>
                <input type="text" class="chox-ttxt" id="CCDFirstName" name="firstName" value="<s:property value="firstName" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Surname<s:if test="!isInsurer"><span class="mandatory">*</span></s:if></label>
                <input type="text" class="chox-ttxt" id="CCDSurname" name="lastName"  value="<s:property value="lastName" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 1<s:if test="!isInsurer"><span class="mandatory">*</span></s:if></label>
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
                    Postcode<s:if test="!isInsurer"><span class="mandatory">*</span></s:if></label>
                <input type="text" class="chox-ttxt" id="CCDPostcode" name="postcode" value="<s:property value="postcode" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Telephone Day</label>
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
                    Insurer<s:if test="!isInsurer"><span class="mandatory">*</span></s:if></label>
                <input type="text" class="chox-ttxt" id="CCDInsurerName" name="insurerName" value="<s:property value="insurerName" />" /></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Policy Number<s:if test="!isInsurer"><span class="mandatory">*</span></s:if></label>
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
                    <div id="vcCustomerSelectionHolder"></div>
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Vehicle Registration Number<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" id="CCDVehicleRegistration" name="vehicleRegistration" value="<s:property value="vehicleRegistration" />" />
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Vehicle Year of Manufacture</label>
                <input type="text" class="chox-ttxt" id="CCDVehicleYear" name="vehicleYear" value="<s:property value="vehicleYear" />" />
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">Vehicle Location<s:if test="!isInsurer"><span class="mandatory">*</span></s:if></label>
                <input type="text" class="chox-ttxt" id="CCDVehicleLocation"  name="location" value="<s:property value="location" />" />
            </div>

            <div class="chox-form-button">
                <input type="submit" id="customerDetailsSubmitButtonId" value="Save Changes" />
            </div>
            <div id="CDmessageBox" class="action-error-msg"><s:property value="actionError" /></div>
            <div id="CDSuccessBox" class="chox-form-submit-result"><s:property value="actionResult" /></div>
        <hr width="80%"/>
        <div>
            <a  href="http://www.hpicheck.com/" target="_blank"><img align="right" src="<%= request.getContextPath()%>/images/logo-hpi.png" style="display: inline;" alt="HPI" width="80" height="60" border="0"/></a>
            <label>HPI Check</label>
            <s:if test="hpiError != null">
                <label class="std-label-small">&nbsp;&nbsp;&nbsp;(HPI Check information not available)</label>
            </s:if>
            <table class="chox-table-form">
              <tr>
                <td><label class="std-label-ro">Vehicle Manufacturer</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="hpiVehicleManufacturer" /></label></td>
              </tr>
              <tr>
                <td><label class="std-label-ro">Vehicle Model</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="hpiVehicleModel" /></label></td>
              </tr>
              <tr>
                <td><label class="std-label-ro">Year of Manufacture</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="hpiVehicleYear" /></label></td>
              </tr>
              <tr>
                <td><label class="std-label-ro">Date of Registration</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="hpiFirstRegistration" /></label></td>
              </tr>
              <tr>
                <td><label class="std-label-ro">Engine Capacity</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="hpiVehicleCapacity" /></label></td>
              </tr>
              <tr>
                <td><label class="std-label-ro">Door Plan</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="hpiVehicleDoorplan" /></label></td>
              </tr>
              <tr>
                <td><label class="std-label-ro">Transmission</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="hpiVehicleTransmission" /></label></td>
              </tr>
            </table>
        </div>
        </div>
    </fieldset>
    <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>-->
    <!--s:token/-->
</form>
