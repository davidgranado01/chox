<%@ taglib uri="/struts-tags" prefix="s" %>


<script language="JavaScript">
        

    $(document).ready(function(){
            
            
        var rentalStartDatePicker = new Ext.form.DateField({
            name: 'rentalStart',
            width: 100,
            allowBlank: true,
            format: 'd/m/Y',
            showWeekNumber: true,
            validationEvent : false,
            value: '<s:date format="dd/MM/yyyy" name="rentalStart" />',
            renderTo:'rentalStartPH'
        });  
            
        var rentalStartTimPicker = new Ext.form.TimeField({
            name: 'rentalStartTime',
            width: 100,
            allowBlank: true,
            validationEvent : false,
            increment: 15,
            format:'H:i',
            value: '<s:property value="rentalStartTime" />',
            renderTo:'rentalStartTimePH'
        });
            
        var rentalEndDatePicker = new Ext.form.DateField({
            name: 'rentalEnd',
            width: 100,
            allowBlank: true,
            format: 'd/m/Y',
            showWeekNumber: true,
            validationEvent : false,
            value: '<s:date format="dd/MM/yyyy" name="rentalEnd" />',
            renderTo:'rentalEndPH'
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
            
            
        //repairBookInDatePicker.render('repairBookInDatePlaceHolder');
         $.validator.addMethod('time', function (value) { 
                return /^(\d{2}:\d{2})$/.test(value); 
            });
            
        $("#formUpdateHireVehicle").validate(
        {
            errorLabelContainer: "#HVDmessageBox",  
            
            rules: {
                   
                vehicleManufacturer:{
                    required:true
                },
                vehicleModel:{
                    required:true
                },
                vehicleRegistration:{
                    required:true
                },    
                rentalStart:{
                    date:true,
                    required:true
                },
                rentalStartTime:{
                    time:true,
                    required:true  
                 
                },  
                rentalStart:{
                    required:true
                },
                rentalEndTime:{
                    time:true,
                    required:true                     
                },  
                vehicleClassId:{
                    min:1
                },
                days:{
                    min:0,
                    digits:true
                }
                 
            },
            messages: {
                   
                vehicleManufacturer:{
                    required:"You must supply a value for 'Vehicle Manufacturer"
                },
                vehicleModel:{
                    required:"You must supply a value for 'Vehicle Model'"
                },
                vehicleRegistration:{
                    required:"You must supply a value for 'Vehicle Registration'"
                },                   
                rentalStart: {
                    date:"Invalid date format for 'Hire Start (Date)'",
                    required:"You must supply a value for 'Hire Start (Date)'"
                }, 
                rentalEnd: {
                    date:"Invalid date format for 'Hire End (Date)'",                   
                    required:"You must supply a value for 'Hire End (Date)'"
                },
                rentalStartTime: {
                    time:"Invalid date format for 'Hire Start (Time)'",
                    required:"You must supply a value for 'Hire Start (Time)'"
                }, 
                rentalEndTime: {
                    time:"Invalid date format for 'Hire End (Time)'",
                    required:"You must supply a value for 'Hire End (Time)'"
                },
                vehicleClassId:{
                    min: "You must select a Vehicle Class"
                },
                days:{
                    required:"You must supply a value for 'No. Days Hire'",
                    min: "You must supply a value for 'No. Days Hire' that is greater than 0",
                    digits: "You must supply a numeric value for 'No. Days Hire'" 
                }
                 
            },
            submitHandler: function(form) {
                $(form).ajaxSubmit(globalEntityFormOptions);
            }
        });
    }); 
        
</script>

<form id="formUpdateHireVehicle" action="<%=request.getContextPath()%>/prv/p/updateVehicleHire.action" class="XXentity-form">
    <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <fieldset class="x-fieldset">
        <legend>Hire Vehicle Details</legend>
        <div style="display:none" class="form-container">            
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
                <s:select name="vehicleClassId" list="vehicleClasses" listKey="id" listValue="name" headerKey="-1" headerValue="--- SELECT ---" emptyOption="false"></s:select> 
                
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
            <div class="chox-form-submit-result">&nbsp;</div>   
            <div id="HVDmessageBox" style="text-align:center"></div>  
        </div>
    </fieldset>
</form> 