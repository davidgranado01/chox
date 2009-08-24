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
                    },
                    labourRate :{
                        number:true
                    },
                    labourHour :{
                        number:true
                    },
                    labourCost :{
                        number:true
                    },
                    nonProvisionReason :{
                        required: isNonProvisionReasonRequired
                    },
                    date_compare_field:{
                        required: isDateCorrect
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
                    },    
                    labourRate :{
                        number:"You must supply a numeric value for 'Labour Rate'"
                    },
                    labourHour :{
                        number:"You must supply a numeric value for 'Labour Hours'"
                    }, 
                    labourCost :{ 
                        number:"You must supply a numeric value for 'Total Labour Cost'"
                    },
                    nonProvisionReason :{
                        required:"You must select 'Labour Information Non-Provision Reason' if 'Labour Rate', 'Labour Hours' or 'Total Labour Cost' cannot be provided"
                    },
                    date_compare_field:{
                        required:"The 'Repair Completion Date' must be after the 'Repair Book In Date'"
                    }
                },
                submitHandler: function(form) {
                    $(form).ajaxSubmit(globalEntityFormOptions);
                }
            });
        }); 
        
        function isDateCorrect(){
            
            var bFlag = true;
            
            var repairBookInDt = $("#repairBookInDatePH :input").val();
            var repairCompletionDt = $("#repairCompletionDatePH :input").val();
            
            if(repairBookInDt!="" && repairCompletionDt!=""){
            
                bFlag = (repairBookInDt <= repairCompletionDt);
                
            }   
            
            return !bFlag;
        }        
        
        function isNonProvisionReasonRequired(){
            
            /*
            var sLabourRate = $("#labourCost").val();
            var sLabourHour = $("#labourHour").val();
            var sLabourCost = $("#labourCost").val();

            if(sLabourRate.length<=0 && sLabourHour.length<=0 && sLabourCost.length<=0){
                return true;
            }
            */
           
           $(".chox-form-submit-result").html("");
           
            return false;
        }
        
    </script>
    
    <fieldset class="x-fieldset">
        <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
        <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
        <input type="hidden" name="date_compare_field" value=''>
        
        <legend>Hire Monitoring</legend>
        <div style="display:none" class="form-container">            
            <div class="chox-form-item">
                <label class="chox-form-std-label2">
            Original ECD</label><label class="std-data-ro"><s:property value="customer.InitialECDDesc" /></label></div>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label2">Name Of Repairer</label>
            <input type="text" class="chox-ttxt" name="nameOfRepairer" value="<s:property value="nameOfRepairer" />"/></div>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label2">Repair Book In Date</label>
                <span id="repairBookInDatePH"></span>
            </div>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label2">
                Inspection Booked Date</label>
                <span id="inspectionBookedDatePH"></span>
            </div>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label2">
                Inspection Date</label>
                <span id="inspectionDatePH"></span>
            </div>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label2">
                Is Total Loss?</label>
            <s:checkbox name="isTotalLostCheck" /></div>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label2">
                Repair Completion Date</label>
            <span id="repairCompletionDatePH"></span></div>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label2">
                Name of IME</label>
            <input type="text" class="chox-ttxt" name="nameOfIme" value="<s:property value="nameOfIme" />"/></div>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label2">
                Labour Rate (Per Hour)</label>
            <input type="text" class="chox-ttxt" name="labourRate" id="labourRate" value="<s:property value="labourRate" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label2">
                Labour Hours</label>
            <input type="text" class="chox-ttxt" name="labourHour" id="labourHour" value="<s:property value="labourHour" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label2">
                Total Labour Cost</label>
            <input type="text" class="chox-ttxt" name="labourCost" id="labourCost" value="<s:property value="labourCost" />"/></div>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label2">Labour Information <br/>Non-Provision Reason</label>
                <s:select name="nonProvisionReason" 
                list="nonProvisionReasons" 
                headerKey="" listKey="text" 
                listValue="value"
                headerValue="--- Please Select ---" 
                emptyOption="false" cssStyle="width:250px"></s:select>
            </div>
                
            <div class="chox-form-button">
                <input type="submit" value="Save Changes" />
            </div>
            
            <div id="HMmessageBox" style="text-align:center" class="action_msg"></div>            
            <div class="chox-form-submit-result">&nbsp;</div>   
            
            
        </div>
    </fieldset>
</form>