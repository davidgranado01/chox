<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var rentalStartTimPicker = -1;

    $(function(){

        ui.dateField('rentalStart', '<s:date format="dd/MM/yyyy" name="rentalStart" />' ,'rentalStartPH');
        ui.dateField('rentalEnd', '<s:date format="dd/MM/yyyy" name="rentalEnd" />' ,'rentalEndPH');

        rentalStartTimPicker = new Ext.form.TimeField({
            name: 'rentalStartTime',
            width: 100,
            allowBlank: true,
            validationEvent : false,
            increment: 15,
            format:'H:i',
            value: '<s:property value="rentalStartTime" />',
            renderTo:'rentalStartTimePH'
        });
        
        var rentalEndTimPicker = new Ext.form.TimeField({
            name: 'rentalEndTime',
            width: 100,
            allowBlank: true,
            validationEvent : false,
            increment: 15,
            format:'H:i',
            value: '<s:property value="rentalEndTime" />',
            renderTo:'rentalEndTimePH'
        });
            
        $.validator.addMethod('time', function (value) {
            return /^(\d{2}:\d{2})$/.test(value);
        });

        var form = $("#formUpdateHireVehicle");

        var fsets =  $('legend',form);
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});

        form.validate(
        {
            errorLabelContainer: "#HVDmessageBox",
            rules: {
                vehicleManufacturer:{required:true},
                vehicleModel:{required:true},
                vehicleRegistration:{required:true},    
                rentalStart:{date:true,required:true},
                rentalStartTime:{time:true,required:true},  
                rentalEnd:{date:true,required:true},
                rentalEndTime:{time:true,required:true},  
                vehicleClassId : { min:1 },
                days : { min:0, digits:true }
            },
            messages: {
                vehicleManufacturer:{required:"You must supply a value for 'Vehicle Manufacturer"},
                vehicleModel:{required:"You must supply a value for 'Vehicle Model'"},
                vehicleRegistration:{required:"You must supply a value for 'Vehicle Registration'"},                   
                rentalStart: {date:"Invalid date format for 'Hire Start (Date)'", required:"You must supply a value for 'Hire Start (Date)'"},
                rentalEnd: {date:"Invalid date format for 'Hire End (Date)'", required:"You must supply a value for 'Hire End (Date)'"},
                rentalStartTime: {time:"Invalid date format for 'Hire Start (Time)'", required:"You must supply a value for 'Hire Start (Time)'"},
                rentalEndTime: {time:"Invalid date format for 'Hire End (Time)'", required:"You must supply a value for 'Hire End (Time)'"},
                vehicleClassId:{min: "You must select a Vehicle Class"},
                days:{required:"You must supply a value for 'No. Days Hire'", min: "You must supply a value for 'No. Days Hire' that is greater than 0", digits: "You must supply a numeric value for 'No. Days Hire'"}
            }
        });

        ui.ajaxForm(form,updateHireMonitoringPanel,'html');
        
    }); 

    function updateHireMonitoringPanel() {
        var vehicleClassId = $('#vehicleClassComboId :selected').text();
        document.getElementById("hireMonitorVehicleClassId").innerHTML = vehicleClassId;

        var time = $('#rentalStart').val() + ' ' + rentalStartTimPicker.getValue();
        document.getElementById("hireMonitorHireStartId").innerHTML = time;
    }

</script>

<form id="formUpdateHireVehicle" action="<%=request.getContextPath()%>/prv/p/updateVehicleHire.action" class="XXentity-form">
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <input name="currentVersion" type="hidden" value="<s:property value="version" />" />
    <fieldset class="x-fieldset partial">
        <legend>Hire Vehicle Details</legend>
        <div class="form-container" id="hireVehicleDetailWId">
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Manufacturer<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" id="HVDManufacturer" name="vehicleManufacturer" value="<s:property value="vehicleManufacturer" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Model<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" id="HVDModel" name="vehicleModel" value="<s:property value="vehicleModel" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Registration<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" id="HVDRegistration"  name="vehicleRegistration" value="<s:property value="vehicleRegistration" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Replacement Vehicle Class<span class="mandatory">*</span></label>
                    <s:select id="vehicleClassComboId" name="vehicleClassId" list="vehicleClasses" listKey="id" listValue="name" headerKey="-1" headerValue="--- SELECT ---" emptyOption="false"></s:select>

            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Hire Start (Date)<span class="mandatory">*</span></label>
                <span id="rentalStartPH"></span></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Hire Start (Time)<span class="mandatory">*</span></label>
                <span id="rentalStartTimePH"></span></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Hire End (Date)<span class="mandatory">*</span></label>
                <span id="rentalEndPH"></span></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Hire End (Time)<span class="mandatory">*</span></label>
                <span id="rentalEndTimePH"></span></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Reason For Collection</label>
                <input type="text" class="chox-ttxt" id="HVDReasonForCollection" name="collectionReason" value="<s:property value="collectionReason" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    No. Days Hire<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" id="HVDNNumberOfDaysHire" name="days" value="<s:property value="days" />" /></div>
            <div class="chox-form-button">
                <input type="submit" value="Save Changes" />
            </div>

            <div id="HVDmessageBox" class="action-error-msg"><s:property value="actionError" /></div>
            <div class="chox-form-submit-result"><s:property value="actionResult" /></div>
        </div>
    </fieldset>
</form> 