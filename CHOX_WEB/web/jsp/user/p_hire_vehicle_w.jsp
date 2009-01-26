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
            
            
            //repairBookInDatePicker.render('repairBookInDatePlaceHolder');
       
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
                 rentalEnd:{
                     date:true,
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
                   date:"Invalid date format for 'Hire Start'",
                   required:"You must supply a value for 'Hire Start'"
                 }, 
                 rentalEnd: {
                    date:"Invalid date format for 'Hire End'",
                   required:"You must supply a value for 'Hire End'"
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





<form id="formUpdateHireVehicle" action="user/updateVehicleHire.action" class="XXentity-form">
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
                Hire Start<span class="mandatory">*</span></label>
             <span id="rentalStartPH"></span></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Hire End<span class="mandatory">*</span></label>
            <span id="rentalEndPH"></span></div>
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