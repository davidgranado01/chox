
<%@ taglib uri="/struts-tags" prefix="s" %>


    <script language="JavaScript">
        

        $(document).ready(function(){
            
            
        
            var incidentDateDatePicker = new Ext.form.DateField({
                name: 'date',
                width: 185,
                allowBlank: true,
                format: 'd/m/Y',
                showWeekNumber: true,
                validationEvent : false,
                value: '<s:date format="dd/MM/yyyy" name="date" />',
                renderTo:'incidentDatePH'
            });   

       
            
            
            $("#formUpdateIncidentForm").validate(
            {
               errorLabelContainer: "#INCmessageBox",                
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
                   required:"You must supply a value for 'Date / Time'",
                   date:"Invalid date format for 'Date / Time'"
                 }
               },
                submitHandler: function(form) {
                    $(form).ajaxSubmit(globalEntityFormOptions);
                }
            });
        }); 
        




        
        </script>


<form id="formUpdateIncidentForm" action="user/updateIncident.action" class="XXentity-form">
    <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
    <fieldset class="x-fieldset">
        <legend>Incident Details</legend>
        <div style="display:none" class="form-container">  
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Date / Time</label>
            <span id="incidentDatePH"></span></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Location</label>
            <input type="text" class="chox-ttxt" id="IDLocation" name="location" value='<s:property value="location" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Police Involved?</label>
                <s:checkbox name="isPoliceInvolved" />
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Description</label>
                <textarea class="chox-tta" id="IDDescription" cols="20" rows="5" name="incidentDescription"><s:property value="incidentDescription" /></textarea>
            </div>
            <div class="chox-form-button">
                <input type="submit" value="Save Changes" />
            </div>
            <div id="INCmessageBox" class="errorBox"></div>
            <div class="chox-form-submit-result"></div>                
        </div>
    </fieldset>
</form>    