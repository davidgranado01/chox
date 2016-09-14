<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    // $(function(){
    Ext.onReady(function(){

        var repairBookInDatePicker = ui.unvalidatedDateField('repairBookInDate','<s:date format="dd/MM/yyyy" name="repairBookInDate" />','insurerRepairBookInDatePH');
        var repairAuthorisedDatePicker = ui.unvalidatedDateField('repairAuthorisedDate','<s:date format="dd/MM/yyyy" name="repairAuthorisedDate" />','insurerRepairAuthorisedDatePH');
        var repairCommencedDatePicker = ui.unvalidatedDateField('repairCommencedDate','<s:date format="dd/MM/yyyy" name="repairCommencedDate" />','insurerRepairCommencedDatePH');
        var inspectionBookedDateDatePicker = ui.unvalidatedDateField('inspectionBookedDate','<s:date format="dd/MM/yyyy" name="inspectionBookedDate" />','insurerInspectionBookedDatePH');
        var inspectionDateDatePicker = ui.unvalidatedDateField('inspectionDate','<s:date format="dd/MM/yyyy" name="inspectionDate" />','insurerInspectionDatePH');
        var repairCompletionDateDatePicker = ui.unvalidatedDateField('repairCompletionDate','<s:date format="dd/MM/yyyy" name="repairCompletionDate" />','insurerRepairCompletionDatePH');
        var totalLossOfferMadeDatePicker = ui.unvalidatedDateField('totalLossOfferMadeDate','<s:date format="dd/MM/yyyy" name="totalLossOfferMadeDate" />','insurerTotalLossOfferMadeDatePH');
        var totalLossOfferAcceptedDatePicker = ui.unvalidatedDateField('totalLossOfferAcceptedDate','<s:date format="dd/MM/yyyy" name="totalLossOfferAcceptedDate" />','insurerTotalLossOfferAcceptedDatePH');
        var totalLossOfferCheckIssuedDatePicker = ui.unvalidatedDateField('totalLossOfferCheckIssuedDate','<s:date format="dd/MM/yyyy" name="totalLossOfferCheckIssuedDate" />','insurerTotalLossOfferCheckIssuedDatePH');
        var totalLossOfferCheckReceivedDatePicker = ui.unvalidatedDateField('totalLossOfferCheckReceivedDate','<s:date format="dd/MM/yyyy" name="totalLossOfferCheckReceivedDate" />','insurerTotalLossOfferCheckReceivedDatePH');

        
        var form = $("form#formUpdateInsurerHireMonitoringDetail");

        var fsets =  $('legend',form);
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});
        
        form.validate(
        {
            errorLabelContainer: "#insurerHMmessageBox",
            rules: {
                repairBookInDate:{dateITA:true},
                repairAuthorisedDate:{dateITA:true},
                repairCommencedDate:{dateITA:true},
                inspectionBookedDate:{dateITA:true},
                inspectionDate:{dateITA:true},
                repairCompletionDate:{dateITA:true},
                totalLossOfferMadeDate:{dateITA:true},
                totalLossOfferAcceptedDate:{dateITA:true},
                totalLossOfferCheckIssuedDate:{dateITA:true},
                totalLossOfferCheckReceivedDate:{dateITA:true},
                labourRate :{number:true, min : 0}, //,max: 100000
                labourHour :{number:true, min : 0}, //,max: 100000
                labourCost :{number:true, min : 0}, //,max: 100000
                date_compare_field:{required: isDateCorrect}
            },
            messages: {
                repairBookInDate: {dateITA:"Invalid date format for 'Repair Book In Date'"},
                repairAuthorisedDate: {dateITA:"Invalid date format for 'Date Repair Authorised'"},
                repairCommencedDate: {dateITA:"Invalid date format for 'Date Repair Commenced'"},
                inspectionBookedDate: {dateITA:"Invalid date format for 'Inspection Booked Date'"},
                inspectionDate: {dateITA:"Invalid date format for 'Inspection Date'"},
                repairCompletionDate: {dateITA:"Invalid date format for 'Repair Completion Date'"},
                totalLossOfferMadeDate: {dateITA:"Invalid date format for 'Date Total Loss Offer Made'"},
                totalLossOfferAcceptedDate: {dateITA:"Invalid date format for 'Date Total Loss Offer Accepted'"},
                totalLossOfferCheckIssuedDate: {dateITA:"Invalid date format for 'Date Total Loss Cheque Issued'"},
                totalLossOfferCheckReceivedDate: {dateITA:"Invalid date format for 'Date Total Loss Cheque Received'"},
                labourRate :{number:"You must supply a numeric value for 'Labour Rate'", min : "Labour Rate must not be negative."},
                labourHour :{number:"You must supply a numeric value for 'Labour Hours'", min : "Labour Hours must not be negative."},
                labourCost :{number:"You must supply a numeric value for 'Total Labour Cost'", min : "Labour Cost must not be negative."},
                date_compare_field:{required:"The 'Repair Completion Date' must be after the 'Repair Book In Date'"}
            }
        });

        ui.ajaxForm(form,onInsurerHireMonitoringSubmitResponseReceived,'html');
        $("#insurerHMsuccessBox").fadeOut(10000);

        $('select[name="claimantImpecunious"]').val('<s:property value='claimantImpecunious'/>');
        $('select[name="whoManagedRepair"]').val('<s:property value='whoManagedRepair'/>');


    });
        
    function isDateCorrect(){
            
        var bFlag = true;
        var repairBookInDt = $("#insurerRepairBookInDatePH :input").val();
        var repairCompletionDt = $("#insurerRepairCompletionDatePH :input").val();
            
        if(repairBookInDt!=="" && repairCompletionDt!==""){
            var dRepairBookInDt = getDate(repairBookInDt);
            var dRepairCompletionDt = getDate(repairCompletionDt);
            bFlag = (dRepairBookInDt <= dRepairCompletionDt);
        }
            
        return bFlag;
    }
                
    function onInsurerHireMonitoringSubmitResponseReceived(responseText, statusText){
    }

    function insurerHireMonitoringSubmit() {
        if(isDateCorrect()){
                choxJqueryHttpSubmit($("form#formUpdateInsurerHireMonitoringDetail"));
        }
        else {
        	$("#insurerHMmessageBox").empty();
        	$("#insurerHMmessageBox").append("The 'Repair Completion Date' must be after the 'Repair Book In Date'.\n<br/>").show();
        }
    }
    

</script>

<form id="formUpdateInsurerHireMonitoringDetail" name="formUpdateInsurerHireMonitoringDetail"
      action="<%=request.getContextPath()%>/prv/p/updateInsurerHireMonitorDetail.action" method="POST">

    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <input type="hidden" name="date_compare_field" id="date_compare_field" value=''>

    <fieldset class="x-fieldset partial">
        <legend>Hire Monitoring</legend>
        <div class="form-container" id="insurerHireMonitoringWId">

                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Inspection Booked Date</label>
                    <span id="insurerInspectionBookedDatePH"></span>
                </div>

                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Inspection Date</label>
                    <span id="insurerInspectionDatePH"></span>
                </div>

                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Repair Authorised</label>
                    <span id="insurerRepairAuthorisedDatePH"></span>
                </div>

                <div class="chox-form-item" >
                    <label class="chox-form-std-label2">Repair Book In Date</label>
                    <span id="insurerRepairBookInDatePH"></span>
                </div>

                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Repair Commenced</label>
                    <span id="insurerRepairCommencedDatePH"></span>
                </div>

                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Repair Completion Date</label>
                    <span id="insurerRepairCompletionDatePH"></span>
                </div>

                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Total Loss Offer Made</label>
                    <span id="insurerTotalLossOfferMadeDatePH"></span>
                </div>

                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Total Loss Offer Accepted</label>
                    <span id="insurerTotalLossOfferAcceptedDatePH"></span>
                </div>

                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Total Loss Cheque Issued</label>
                    <span id="insurerTotalLossOfferCheckIssuedDatePH"></span>
                </div>

                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Total Loss Cheque Received</label>
                    <span id="insurerTotalLossOfferCheckReceivedDatePH"></span>
                </div>


            <div class="chox-form-item">
                <label class="chox-form-std-label2">Labour Rate (Per Hour)</label>
                <input type="text" class="chox-ttxt" id="insurerHireMonitoringlabourRateId" name="labourRate" value="<s:property value="labourRate" />"/>
            </div>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label2">Labour Hours</label>
                <input type="text" class="chox-ttxt" id="insurerHireMonitoringlabourHourId" name="labourHour" value="<s:property value="labourHour" />"/>
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label2">Total Labour Cost</label>
                <input type="text" class="chox-ttxt" id="insurerHireMonitoringlabourCostId" name="labourCost" value="<s:property value="labourCost" />"/>
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label2">Is the Claimant Impecunious?</label>
                <select name="claimantImpecunious" id="claimantImpecuniousId">
                    <option value="">Unknown</option>
                    <option value="true">Yes</option>
                    <option value="false">No</option>
                </select>
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label2">Who managed the repair?</label>
                <select name="whoManagedRepair" id="whoManagedRepairId">
                    <option value="">Unknown</option>
                    <option value="TPI Total Loss">TPI Total Loss</option>
                    <option value="TPI Repair">TPI Repair</option>
                    <option value="You Managed The Repair">You Managed The Repair</option>
                    <option value="You Managed The Total Loss">You Managed The Total Loss</option>
                    <option value="CHO Total Loss">CHO Total Loss</option>
                    <option value="CHO Repair">CHO Repair</option>
                </select>
            </div>

            <div class="chox-form-item-button">
                <input type="button" id="insurerHireMonitoringIdSubmitButtonId" value="Save Changes" onclick="return insurerHireMonitoringSubmit()"/>
           </div>
            <div id="insurerHMmessageBox" style="text-align:center" class="action-error-msg">
                <s:property value="actionError" />
            </div>
            <div id="insurerHMsuccessBox" class="chox-form-submit-result">
                <s:property value="actionResult" />
            </div>

        </div>

    </fieldset>
</form>