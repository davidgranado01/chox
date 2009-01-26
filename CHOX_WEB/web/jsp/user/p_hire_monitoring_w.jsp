<%@ taglib uri="/struts-tags" prefix="s" %>
<form id="formUpdateHireMonitoringDetail" action="user/updateHireMonitorDetail.action" class="XXentity-form">
    
    
    <script language="JavaScript">
        

        $(document).ready(function(){
            
            
            var repairBookInDatePicker = new Ext.form.DateField({
                name: 'repairBookInDate',
                width: 100,
                allowBlank: true,
                format: 'd/m/Y',
                showWeekNumber: true,
                validationEvent : false,
                value: '<s:date format="dd/MM/yyyy" name="repairBookInDate" />',
                renderTo:'repairBookInDatePH'
            });  
            
            var inspectionBookedDateDatePicker = new Ext.form.DateField({
                name: 'inspectionBookedDate',
                width: 100,
                allowBlank: true,
                format: 'd/m/Y',
                showWeekNumber: true,
                validationEvent : false,
                value: '<s:date format="dd/MM/yyyy" name="inspectionBookedDate" />',
                renderTo:'inspectionBookedDatePH'
            });  
            
            var inspectionDateDatePicker = new Ext.form.DateField({
                name: 'inspectionDate',
                width: 100,
                allowBlank: true,
                format: 'd/m/Y',
                showWeekNumber: true,
                validationEvent : false,
                value: '<s:date format="dd/MM/yyyy" name="inspectionDate" />',
                renderTo:'inspectionDatePH'
            });              
                 
            var repairCompletionDateDatePicker = new Ext.form.DateField({
                name: 'repairCompletionDate',
                width: 100,
                allowBlank: true,
                format: 'd/m/Y',
                showWeekNumber: true,
                validationEvent : false,
                value: '<s:date format="dd/MM/yyyy" name="repairCompletionDate" />',
                renderTo:'repairCompletionDatePH'
            });                    
            
            
            //repairBookInDatePicker.render('repairBookInDatePlaceHolder');
       
            $("#formUpdateHireMonitoringDetail").validate(
            {
               errorLabelContainer: "#HMmessageBox",                
               rules: {
                 repairBookInDate:{
                     date:true
                 },
                 inspectionBookedDate:{
                     date:true
                 },
                 inspectionDate:{
                     date:true
                 },
                 repairCompletionDate:{
                     date:true
                 }                 
               },
               messages: {
                 nameOfRepairer:{
                     required:"You must supply a date for 'Name Of Repairer'"
                 },
                 repairBookInDate: {
                   date:"Invalid date format for 'Repair Book In Date'"
                 }, 
                 inspectionBookedDate: {
                   date:"Invalid date format for 'Inspection Booked Date'"
                 },
                 inspectionDate: {
                   date:"Invalid date format for 'Inspection Date'"
                 },  
                 repairCompletionDate: {
                   date:"Invalid date format for 'Repair Completion Date'"
                 }                   
               },
                submitHandler: function(form) {
                    $(form).ajaxSubmit(globalEntityFormOptions);
                }
            });
        }); 
        
        </script>
    
    <fieldset class="x-fieldset">
        <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
        <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
        <legend>Hire Monitoring</legend>
        <div style="display:none" class="form-container">            
           <div class="chox-form-item">
                <label class="chox-form-std-label">
                Original ECD</label><label class="std-data-ro"><s:property value="customer.InitialECDDesc" /></label></div>

           <div class="chox-form-item">
                <label class="chox-form-std-label">Name Of Repairer</label>
           <input type="text" class="chox-ttxt" name="nameOfRepairer" value="<s:property value="nameOfRepairer" />"/></div>
           
           <div class="chox-form-item">
                <label class="chox-form-std-label">Repair Book In Date</label>
                <span id="repairBookInDatePH"></span>
            </div>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Inspection Booked Date</label>
            <span id="inspectionBookedDatePH"></span>
            </div>
           
           <div class="chox-form-item">
                <label class="chox-form-std-label">
                Inspection Date</label>
            <span id="inspectionDatePH"></span>
            </div>
           
           <div class="chox-form-item">
                <label class="chox-form-std-label">
                Is Total Loss?</label>
            <s:checkbox name="isTotalLostCheck" /></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Repair Completion Date</label>
            <span id="repairCompletionDatePH"></span></div>
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Name of IME</label>
            <input type="text" class="chox-ttxt" name="nameOfIme" value="<s:property value="nameOfIme" />"/></div>
            
            
            <div class="chox-form-button">
                <input type="submit" value="Save Changes" />
            </div>
            
            <div id="HMmessageBox" style="text-align:center"></div>            
            <div class="chox-form-submit-result">&nbsp;</div>   
            
            
        </div>
    </fieldset>
</form>