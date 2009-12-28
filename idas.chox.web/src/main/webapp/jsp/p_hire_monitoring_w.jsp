<%@ taglib uri="/struts-tags" prefix="s" %>


<script type="text/javascript">
        
    $(function(){

        var repairBookInDatePicker = ui.dateField('repairBookInDate','<s:date format="dd/MM/yyyy" name="repairBookInDate" />','repairBookInDatePH');
        var inspectionBookedDateDatePicker = ui.dateField('inspectionBookedDate','<s:date format="dd/MM/yyyy" name="inspectionBookedDate" />','inspectionBookedDatePH');
        var inspectionDateDatePicker = ui.dateField('inspectionDate','<s:date format="dd/MM/yyyy" name="inspectionDate" />','inspectionDatePH');
        var nextReviewDatePicker = ui.dateField('nextReviewDate','<s:date format="dd/MM/yyyy" name="nextReviewDate" />','nextReviewDatePH');
        var repairCompletionDateDatePicker = ui.dateField('repairCompletionDate','<s:date format="dd/MM/yyyy" name="repairCompletionDate" />','repairCompletionDatePH');

        var form = $("form#formUpdateHireMonitoringDetail");
        
        form.validate(
        {
            errorLabelContainer: "#HMmessageBox",
            rules: {
                repairBookInDate:{date:true},
                inspectionBookedDate:{date:true},
                inspectionDate:{date:true},
                repairCompletionDate:{date:true},
                nextReviewDate:{date:true},
                labourRate :{number:true},
                labourHour :{digits:true},
                labourCost :{number:true},
                nonProvisionReason :{required: isNonProvisionReasonRequired},
                date_compare_field:{required: isDateCorrect}
            },
            messages: {
                nameOfRepairer:{required:"You must supply a date for 'Name Of Repairer'"},
                repairBookInDate: {date:"Invalid date format for 'Repair Book In Date'"},
                inspectionBookedDate: {date:"Invalid date format for 'Inspection Booked Date'"},
                inspectionDate: {date:"Invalid date format for 'Inspection Date'"},
                nextReviewDate: {date:"Invalid date format for 'Next Review Date'"},
                repairCompletionDate: {date:"Invalid date format for 'Repair Completion Date'"},
                labourRate :{number:"You must supply a numeric value for 'Labour Rate'"},
                labourHour :{digits:"You must supply a digit value for 'Labour Hours'"},
                labourCost :{number:"You must supply a numeric value for 'Total Labour Cost'"},
                nonProvisionReason :{required:"You must select 'Labour Information Non-Provision Reason' if 'Labour Rate', 'Labour Hours' or 'Total Labour Cost' cannot be provided"},
                date_compare_field:{required:"The 'Repair Completion Date' must be after the 'Repair Book In Date'"}
            }
        });

        ui.ajaxForm(form,onHireMonitoringSubmitResponseReceived);

        var repairBookDt = $("#repairBookInDate").val();
        $("#notificationRepairBookInDate").val(repairBookDt);

    });
        
    function isDateCorrect(){
            
        var bFlag = true;
        var repairBookInDt = $("#repairBookInDatePH :input").val();
        var repairCompletionDt = $("#repairCompletionDatePH :input").val();
            
        if(repairBookInDt!="" && repairCompletionDt!=""){
            var dRepairBookInDt = getDate(repairBookInDt);
            var dRepairCompletionDt = getDate(repairCompletionDt);
            bFlag = (dRepairBookInDt <= dRepairCompletionDt);
        }
            
        return !bFlag;
    }
        
    function isNonProvisionReasonRequired(){
        $(".chox-form-submit-result").html("");
        return false;
    }
        
    function onHireMonitoringSubmitResponseReceived(responseText, statusText){
        var repairBookDt = $("#repairBookInDate").val();
        $("#notificationRepairBookInDate").val(repairBookDt);            
    }



</script>

<form id="formUpdateHireMonitoringDetail" name="formUpdateHireMonitoringDetail"
      action="<%=request.getContextPath()%>/prv/p/updateHireMonitorDetail.action" method="POST">

    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <input type="hidden" name="date_compare_field" value=''>
    <s:hidden value="notificationRepairBookInDate" id="notificationRepairBookInDate" name="notificationRepairBookInDate"/>

    <fieldset class="x-fieldset">

        <legend>Hire Monitoring</legend>

        <div style="display:none" class="form-container">

            <div class="chox-form-item">
                <label class="chox-form-std-label2">
                    Next Review Date</label>
                <span id="nextReviewDatePH"></span>
            </div>

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
                <input type="text" class="chox-ttxt" name="labourCost" id="labourCost" value="<s:property value="labourCost" />"/>
            </div>

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
                <input type="submit" value="Save Changes" /><s:checkbox name="isUpdateInsurer" /><label>Update Insurer</label>
            </div>

            <div id="HMmessageBox" style="text-align:center" class="action-error-msg"></div>
            <div class="chox-form-submit-result">&nbsp;</div>   

        </div>

    </fieldset>
</form>