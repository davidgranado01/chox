
<%@ taglib uri="/struts-tags" prefix="s" %>


    <script language="JavaScript">
        

        $(document).ready(function(){
            
            
        
            var incidentDateDatePicker = new Ext.form.DateField({
                name: 'date',
                width: 100,
                allowBlank: true,
                format: 'd/m/Y',
                showWeekNumber: true,
                validationEvent : false,
                value: '<s:date format="dd/MM/yyyy" name="date" />',
                renderTo:'incidentDatePH'
            });   
            
           var incidentDateTimPicker = new Ext.form.TimeField({
                name: 'time',
                width: 100,
                allowBlank: true,
                validationEvent : false,
                increment: 15,
                format:'H:i',
                value: '<s:property value="time" />',
                renderTo:'incidentTimePH'
            });

       
            $.validator.addMethod('timeFormat', function (value) { 
                return /^(\d{2}:\d{2})$/.test(value); 
            });

            
            $("#formUpdateIncidentForm").validate(
            {
               errorLabelContainer: "#IncidentMessageBox",                
               rules: {
                 incidentDescription:{
                     required:true
                 },
                 location:{
                     required:true
                 },
                 date: {
                     required:true,
                     date:true
                 },
                 time: {
                     required:true,
                     timeFormat:true

                 }
               },
               messages: {  
                 incidentDescription: {
                   required:"You must supply a value for 'Incident Description'"
                 },  
                 location: {
                   required:"You must supply a value for 'Incident Location'"
                 },
                 date:{
                   required:"You must supply a value for 'Date'",
                   date:"Invalid date format for 'Date'"
                 },
                 time:
                 {
                    required:"You must supply a value for 'Time'",
                    timeFormat:"Invalid format for 'Time'"
                 }
                },
                submitHandler: function(form) {
                    $(form).ajaxSubmit(globalEntityFormOptions);
                }
            });
        }); 
        




        
        </script>


<form id="formUpdateIncidentForm" action="<%=request.getContextPath()%>/prv/p/updateIncident.action" class="XXentity-form">
    <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <fieldset class="x-fieldset">
        <legend>Incident Details</legend>
        <div style="display:none" class="form-container">
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Date<span class="mandatory">*</span></label>
                <span id="incidentDatePH"></span>  
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Time<span class="mandatory">*</span></label>
                <span id="incidentTimePH"></span>  
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Location<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttxt" id="IDLocation" name="location" value="<s:property value="location" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Police Involved?</label>
                <s:checkbox name="isPoliceInvolved" />
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Description<span class="mandatory">*</span></label>
                <textarea class="chox-tta" id="IDDescription" cols="20" rows="5" name="incidentDescription"><s:property value="incidentDescription" /></textarea>
            </div>
            <div class="chox-form-button">
                <input type="submit" value="Save Changes" />
            </div>
            <div id="IncidentMessageBox" class="errorBox"></div>
            <div class="chox-form-submit-result"></div>                
        </div>
    </fieldset>
</form>    