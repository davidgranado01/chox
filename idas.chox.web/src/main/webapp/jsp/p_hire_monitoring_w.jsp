<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    // $(function(){
    Ext.onReady(function(){

        var repairBookInDatePicker = ui.unvalidatedDateField('repairBookInDate','<s:date format="dd/MM/yyyy" name="repairBookInDate" />','repairBookInDatePH');
        var repairAuthorisedDatePicker = ui.unvalidatedDateField('repairAuthorisedDate','<s:date format="dd/MM/yyyy" name="repairAuthorisedDate" />','repairAuthorisedDatePH');
        var repairCommencedDatePicker = ui.unvalidatedDateField('repairCommencedDate','<s:date format="dd/MM/yyyy" name="repairCommencedDate" />','repairCommencedDatePH');
        var inspectionBookedDateDatePicker = ui.unvalidatedDateField('inspectionBookedDate','<s:date format="dd/MM/yyyy" name="inspectionBookedDate" />','inspectionBookedDatePH');
        var inspectionDateDatePicker = ui.unvalidatedDateField('inspectionDate','<s:date format="dd/MM/yyyy" name="inspectionDate" />','inspectionDatePH');
        var nextReviewDatePicker = ui.unvalidatedDateField('nextReviewDate','<s:date format="dd/MM/yyyy" name="nextReviewDate" />','nextReviewDatePH');
        var repairCompletionDateDatePicker = ui.unvalidatedDateField('repairCompletionDate','<s:date format="dd/MM/yyyy" name="repairCompletionDate" />','repairCompletionDatePH');
        var totalLossOfferMadeDatePicker = ui.unvalidatedDateField('totalLossOfferMadeDate','<s:date format="dd/MM/yyyy" name="totalLossOfferMadeDate" />','totalLossOfferMadeDatePH');
        var totalLossOfferAcceptedDatePicker = ui.unvalidatedDateField('totalLossOfferAcceptedDate','<s:date format="dd/MM/yyyy" name="totalLossOfferAcceptedDate" />','totalLossOfferAcceptedDatePH');
        var totalLossOfferCheckIssuedDatePicker = ui.unvalidatedDateField('totalLossOfferCheckIssuedDate','<s:date format="dd/MM/yyyy" name="totalLossOfferCheckIssuedDate" />','totalLossOfferCheckIssuedDatePH');
        var totalLossOfferCheckReceivedDatePicker = ui.unvalidatedDateField('totalLossOfferCheckReceivedDate','<s:date format="dd/MM/yyyy" name="totalLossOfferCheckReceivedDate" />','totalLossOfferCheckReceivedDatePH');

        
        var form = $("form#formUpdateHireMonitoringDetail");

        var fsets =  $('legend',form);
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});
        
        form.validate(
        {
            errorLabelContainer: "#HMmessageBox",
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
                nextReviewDate:{dateITA:true},
                labourRate :{number:true, min : 0}, //,max: 100000
                labourHour :{number:true, min : 0}, //,max: 100000
                labourCost :{number:true, min : 0}, //,max: 100000
                nonProvisionReason :{required: isNonProvisionReasonRequired},
                date_compare_field:{required: isDateCorrect}
            },
            messages: {
                nameOfRepairer:{required:"You must supply a date for 'Name Of Repairer'"},
                repairBookInDate: {dateITA:"Invalid date format for 'Repair Book In Date'"},
                repairAuthorisedDate: {dateITA:"Invalid date format for 'Date Repair Authorised'"},
                repairCommencedDate: {dateITA:"Invalid date format for 'Date Repair Commenced'"},
                inspectionBookedDate: {dateITA:"Invalid date format for 'Inspection Booked Date'"},
                inspectionDate: {dateITA:"Invalid date format for 'Inspection Date'"},
                nextReviewDate: {dateITA:"Invalid date format for 'Next Review Date'"},
                repairCompletionDate: {dateITA:"Invalid date format for 'Repair Completion Date'"},
                totalLossOfferMadeDate: {dateITA:"Invalid date format for 'Date Total Loss Offer Made'"},
                totalLossOfferAcceptedDate: {dateITA:"Invalid date format for 'Date Total Loss Offer Accepted'"},
                totalLossOfferCheckIssuedDate: {dateITA:"Invalid date format for 'Date Total Loss Cheque Issued'"},
                totalLossOfferCheckReceivedDate: {dateITA:"Invalid date format for 'Date Total Loss Cheque Received'"},
                labourRate :{number:"You must supply a numeric value for 'Labour Rate'", min : "Labour Rate must not be negative."},
                labourHour :{number:"You must supply a numeric value for 'Labour Hours'", min : "Labour Hours must not be negative."},
                labourCost :{number:"You must supply a numeric value for 'Total Labour Cost'", min : "Labour Cost must not be negative."},
                nonProvisionReason :{required:"You must select 'Labour Information Non-Provision Reason' if 'Labour Rate', 'Labour Hours' or 'Total Labour Cost' cannot be provided"},
                date_compare_field:{required:"The 'Repair Completion Date' must be after the 'Repair Book In Date'"}
            }
        });

        ui.ajaxForm(form,onHireMonitoringSubmitResponseReceived,'html');
        $("#HMsuccessBox").fadeOut(10000);

        //Call Information Help ToolTip
        createInfoHelp();


    });
        
    function isDateCorrect(){
            
        var bFlag = true;
        var repairBookInDt = $("#repairBookInDatePH :input").val();
        var repairCompletionDt = $("#repairCompletionDatePH :input").val();
            
        if(repairBookInDt!=="" && repairCompletionDt!==""){
            var dRepairBookInDt = getDate(repairBookInDt);
            var dRepairCompletionDt = getDate(repairCompletionDt);
            bFlag = (dRepairBookInDt <= dRepairCompletionDt);
        }
            
        return bFlag;
    }
        
    function isNonProvisionReasonRequired(){
        $(".chox-form-submit-result").html("");
        return false;
    }
        
    function onHireMonitoringSubmitResponseReceived(responseText, statusText){
//        var repairBookDt = $("#repairBookInDate").val();
//        $("#notificationRepairBookInDate").val(repairBookDt);            
    }

    function createInfoHelp(){
        
        new Ext.ToolTip({
            target: 'inspectionBookedDateId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="inspectionBookedDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        new Ext.ToolTip({
            target: 'inspectionDateId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="inspectionDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        new Ext.ToolTip({
            target: 'dateRepairAuthorisedId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="repairAuthorisedDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        new Ext.ToolTip({
            target: 'repairBookInDateId',
            html: '<s:date  format="EEE d MMM HH:mm:ss yyyy" name="repairBookInDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        new Ext.ToolTip({
            target: 'dateRepairCommencedId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="repairCommencedDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        new Ext.ToolTip({
            target: 'isTotalLossId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="isTotalLostCheckLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        new Ext.ToolTip({
            target: 'dateTotalLossOfferMadeId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="totalLossOfferMadeLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        new Ext.ToolTip({
            target: 'dateTotalLossOfferAcceptedId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="totalLossOfferAcceptedLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        new Ext.ToolTip({
            target: 'dateTotalLossChequeIssuedId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="totalLossCheckIssuedLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        new Ext.ToolTip({
            target: 'dateTotalLossChequeReceivedId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="totalLossCheckReceivedLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        new Ext.ToolTip({
            target: 'repairCompletionDateId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="repairCompletionDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        new Ext.ToolTip({
            target: 'dateRepairOnlyOnHireId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="isRepairOnlyCheckLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        new Ext.ToolTip({
            target: 'dateNonFaultinsurerManagingRepairId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="isNFInsurerManagingRepairLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        new Ext.ToolTip({
            target: 'dateClientVatRegisteredId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="clientVatRegisteredLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        new Ext.ToolTip({
            target: 'dateManagingRepairId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="managingRepairLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });



    }
    function hireMonitoringSubmit() {
        updateCHOManagingRepairInClaimDetailsTab();
        // Update Customer Vehicle Damage Total Loss field
        var customerVehicleTotalLossId = document.getElementById("customerVehicleTotalLossId");
        
        if (customerVehicleTotalLossId) { // In read screen
            var originalValue = $('#customerVehicleTotalLossOriginalId').html();
            if ($('#isTotalLostCheckId').is(':checked') === 1) {
                document.getElementById("customerVehicleTotalLossId").innerHTML = 'Yes';
                if (originalValue.length === 0) {
                    document.getElementById("customerVehicleTotalLossOriginalId").innerHTML = '(No)';
                }
            }
            else {
                document.getElementById("customerVehicleTotalLossId").innerHTML = 'No';
                if (originalValue.length === 0) {
                    document.getElementById("customerVehicleTotalLossOriginalId").innerHTML = '(Yes)';
                }
            }            
        }
        else { // in write screen
            var originalValue = $('#customerVehicleTotalLossOriginalDescId').html();
            if ($('#isTotalLostCheckId').is(':checked') === 1) {
                $("#customerVehicleDamageisTotalLossId").prop('checked', true);
                if (originalValue.length === 0) {
                    document.getElementById("customerVehicleTotalLossOriginalDescId").innerHTML = '(No)';
                }
            }
            else {
                $("#customerVehicleDamageisTotalLossId").prop('checked', false);
                if (originalValue.length === 0) {
                    document.getElementById("customerVehicleTotalLossOriginalDescId").innerHTML = '(Yes)';
                }
            }
            
        }
        
        if(isDateCorrect()){
                choxJqueryHttpSubmit($("form#formUpdateHireMonitoringDetail"));
        }
        else {
        	$("#HMmessageBox").empty();
        	$("#HMmessageBox").append("The 'Repair Completion Date' must be after the 'Repair Book In Date'.\n<br/>").show();
        }
    }
    
    function updateCHOManagingRepairInClaimDetailsTab() {
         // Update Customer Vehicle Damage Total Loss field
        var managingRepairCheckboxReadScreenId = document.getElementById("managingRepairCheckboxReadScreenId");
        
        if (managingRepairCheckboxReadScreenId) { // In read screen
            var originalValue = $('#managingRepairCheckboxReadScreenOriginalId').html();
            if ($('#managingRepairCheckId').is(':checked') === true) {
                document.getElementById("managingRepairCheckboxReadScreenId").innerHTML = 'Yes';
                if (originalValue.length === 0) {
                    document.getElementById("managingRepairCheckboxReadScreenOriginalId").innerHTML = '(No)';
                }
            }
            else {
                document.getElementById("managingRepairCheckboxReadScreenId").innerHTML = 'No';
                if (originalValue.length === 0) {
                    document.getElementById("managingRepairCheckboxReadScreenOriginalId").innerHTML = '(Yes)';
                }
            }            
        }
        else { // in write screen
            if ($('#managingRepairCheckId').is(':checked') === true) {
                $("#managingRepairCheckboxWriteScreenId").prop('checked', true);
            }
            else {
                $("#managingRepairCheckboxWriteScreenId").prop('checked', false);
            }
        }
    }

</script>

<form id="formUpdateHireMonitoringDetail" name="formUpdateHireMonitoringDetail"
      action="<%=request.getContextPath()%>/prv/p/updateHireMonitorDetail.action" method="POST">

    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <input type="hidden" name="date_compare_field" id="date_compare_field" value=''>

    <fieldset class="x-fieldset partial">

        <legend>Hire Monitoring</legend>

        <div class="form-container" id="hireMonitoringWId">

            <div class="chox-form-item">
                <label class="chox-form-std-label2">Next Review Date</label>
                <span id="nextReviewDatePH"></span>
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label2">Original ECD</label><label class="std-data-ro"><s:property value="customer.InitialECDDesc" /></label>&nbsp;</div>

            <div class="chox-form-item">
                <label class="chox-form-std-label2">Name Of Repairer</label>
                <input type="text" id="hireMonitoringnameOfRepairerId" class="chox-ttxt" name="nameOfRepairer" value="<s:property value="nameOfRepairer" />"/></div>


            <s:if test="inspectionBookedDateLastModified!=null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Inspection Booked Date </label>
                    <table><tr> <td><div class="chox-form-std-label-dateId" id="inspectionBookedDatePH"></div></td><td> <img src="../images/sign_info.png" alt="" width="13" height="13" id="inspectionBookedDateId" /> </td></tr></table>
                </div>
            </s:if>
            <s:else>
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Inspection Booked Date <img style="display:none" src="../images/sign_info.png" width="13" alt="" height="13" id="inspectionBookedDateId" /></label>
                    <span id="inspectionBookedDatePH"></span>
                </div>
            </s:else>

            <s:if  test="inspectionDateLastModified != null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Inspection Date </label>
                    <table><tr><td><div class="chox-form-std-label-dateId" id="inspectionDatePH"></div></td><td><img src="../images/sign_info.png" alt="" width="13" height="13" id="inspectionDateId" /></td></tr></table>
                </div>
            </s:if>
            <s:else>
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Inspection Date <img style="display:none" src="../images/sign_info.png" width="13" height="13" alt="" id="inspectionDateId" /> </label>
                    <span id="inspectionDatePH"></span>
                </div>
            </s:else>

            <s:if test="repairAuthorisedDateLastModified != null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Repair Authorised </label>
                    <table><tr><td><div class="chox-form-std-label-dateId" id="repairAuthorisedDatePH"></div></td><td><img src="../images/sign_info.png" alt="" width="13" height="13" id="dateRepairAuthorisedId" /></td></tr></table>
                </div>
            </s:if>
            <s:else>
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Repair Authorised <img  style="display:none" src="../images/sign_info.png" width="13" alt="" height="13" id="dateRepairAuthorisedId" /></label>
                    <span id="repairAuthorisedDatePH"></span>
                </div>
            </s:else>

            <s:if test="repairBookInDateLastModified!=null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Repair Book In Date </label>
                    <table><tr><td><div class="chox-form-std-label-dateId" id="repairBookInDatePH"></div></td><td><img src="../images/sign_info.png" alt="" width="13" height="13" id="repairBookInDateId" /></td></tr></table>
                </div>
            </s:if>
            <s:else>
                <div class="chox-form-item" >
                    <label class="chox-form-std-label2">Repair Book In Date <img style="display: none" src="../images/sign_info.png" width="13" alt="" height="13" id="repairBookInDateId" /></label>
                    <span id="repairBookInDatePH"></span>
                </div>
            </s:else>

            <s:if test="repairCommencedDateLastModified != null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Repair Commenced </label>
                    <table><tr><td><div class="chox-form-std-label-dateId" id="repairCommencedDatePH"></div></td><td><img src="../images/sign_info.png" alt="" width="13" height="13" id="dateRepairCommencedId" /></td></tr></table>
                </div>
            </s:if>
            <s:else>
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Repair Commenced <img style="display: none" src="../images/sign_info.png" width="13" alt="" height="13" id="dateRepairCommencedId" /></label>
                    <span id="repairCommencedDatePH"></span>
                </div>
            </s:else>

            <s:if test="repairCompletionDateLastModified != null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Repair Completion Date </label>
                    <table><tr><td><div class="chox-form-std-label-dateId" id="repairCompletionDatePH"></div></td><td><img src="../images/sign_info.png" alt="" width="13" height="13" id="repairCompletionDateId" /></td></tr></table>
                </div>
            </s:if>
            <s:else>
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Repair Completion Date <img style="display: none" src="../images/sign_info.png" width="13" alt="" height="13" id="repairCompletionDateId" /></label>
                    <span id="repairCompletionDatePH"></span>
                </div>
            </s:else>

            <s:if test="isTotalLostCheckLastModified!=null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Is Total Loss? </label>
                    <table><tr><td><s:checkbox name="isTotalLostCheck" id="isTotalLostCheckId"/></td><td><img src="../images/sign_info.png" width="13" alt="" height="13" id="isTotalLossId" /></td></tr></table>
                </div>
            </s:if>
            <s:else>
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Is Total Loss? <img style="display: none" src="../images/sign_info.png" alt="" width="13" height="13" id="isTotalLossId" /></label>
                    <s:checkbox name="isTotalLostCheck" id="isTotalLostCheckId"/>
                </div>
            </s:else>

            <s:if test="totalLossOfferMadeLastModified!=null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Total Loss Offer Made </label>
                    <table><tr><td><div class="chox-form-std-label-dateId" id="totalLossOfferMadeDatePH"></div></td><td><img src="../images/sign_info.png" alt="" width="13" height="13" id="dateTotalLossOfferMadeId" /></td></tr></table>
                </div>
            </s:if>
            <s:else>
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Total Loss Offer Made <img style="display: none" src="../images/sign_info.png" alt="" width="13" height="13" id="dateTotalLossOfferMadeId" /></label>
                    <span id="totalLossOfferMadeDatePH"></span>
                </div>
            </s:else>

            <s:if test="totalLossOfferAcceptedLastModified!=null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Total Loss Offer Accepted </label>
                    <table><tr><td><div class="chox-form-std-label-dateId" id="totalLossOfferAcceptedDatePH"></div></td><td><img src="../images/sign_info.png" alt="" width="13" height="13" id="dateTotalLossOfferAcceptedId" /></td></tr></table>
                </div>
            </s:if>
            <s:else>
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Total Loss Offer Accepted <img style="display: none" src="../images/sign_info.png" alt="" width="13" height="13" id="dateTotalLossOfferAcceptedId" /></label>
                    <span id="totalLossOfferAcceptedDatePH"></span>
                </div>
            </s:else>

            <s:if test="totalLossCheckIssuedLastModified!=null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Total Loss Cheque Issued </label>
                    <table><tr><td><div class="chox-form-std-label-dateId" id="totalLossOfferCheckIssuedDatePH"></div></td><td><img src="../images/sign_info.png" alt="" width="13" height="13" id="dateTotalLossChequeIssuedId" /></td></tr></table>
                </div>
            </s:if>
            <s:else>
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Total Loss Cheque Issued <img style="display: none" src="../images/sign_info.png" alt="" width="13" height="13" id="dateTotalLossChequeIssuedId" /></label>
                    <span id="totalLossOfferCheckIssuedDatePH"></span>
                </div>
            </s:else>

            <s:if test="totalLossCheckReceivedLastModified != null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Total Loss Cheque Received </label>
                    <table><tr><td><div class="chox-form-std-label-dateId" id="totalLossOfferCheckReceivedDatePH"></div></td><td><img src="../images/sign_info.png" alt="" width="13" height="13" id="dateTotalLossChequeReceivedId" /></td></tr></table>
                </div>
            </s:if>
            <s:else>
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Total Loss Cheque Received <img style="display: none" src="../images/sign_info.png" alt="" width="13" height="13" id="dateTotalLossChequeReceivedId" /></label>
                    <span id="totalLossOfferCheckReceivedDatePH"></span>
                </div>
            </s:else>

            <div class="chox-form-item">
                <label class="chox-form-std-label2">Name of IME</label>
                <input type="text" class="chox-ttxt" id="hireMonitoringnameOfImeId" name="nameOfIme" value="<s:property value="nameOfIme" />"/>
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label2">Labour Rate (Per Hour)</label>
                <input type="text" class="chox-ttxt" id="hireMonitoringlabourRateId" name="labourRate" value="<s:property value="labourRate" />"/>
            </div>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label2">Labour Hours</label>
                <input type="text" class="chox-ttxt" id="hireMonitoringlabourHourId" name="labourHour" value="<s:property value="labourHour" />"/>
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label2">Total Labour Cost</label>
                <input type="text" class="chox-ttxt" id="hireMonitoringlabourCostId" name="labourCost" value="<s:property value="labourCost" />"/>
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label3">Labour Information Non-Provision Reason</label>
                <s:select name="nonProvisionReason"
                          list="nonProvisionReasons"
                          headerKey="" listKey="text"
                          listValue="value"
                          headerValue="-- Please Select --"
                          emptyOption="false" cssStyle="width:230px"></s:select>
            </div>
            <br/>

            <s:if test="isRepairOnlyCheckLastModified != null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label3">Repair Only (No Hire)? </label>
                    <table><tr><td><s:checkbox id="hireMonitoringisRepairOnlyCheckId" name="isRepairOnlyCheck" /></td><td><img src="../images/sign_info.png" alt="" width="13" height="13" id="dateRepairOnlyOnHireId" /></td></tr></table>
                </div>
            </s:if>
            <s:else>
                <div class="chox-form-item">
                    <label class="chox-form-std-label3">Repair Only (No Hire)? <img style="display: none" src="../images/sign_info.png" alt="" width="13" height="13" id="dateRepairOnlyOnHireId" /></label>
                    <s:checkbox id="hireMonitoringisRepairOnlyCheckId" name="isRepairOnlyCheck" />
                </div>
            </s:else>
            <s:if test="managingRepairLastModified != null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label3">CHO Managing Repair? </label>
                    <table><tr><td><s:checkbox name="managingRepair" id="managingRepairCheckId" /></td><td><img src="../images/sign_info.png" alt="" width="13" height="13" id="dateManagingRepairId" /></td></tr></table>
                </div>
            </s:if>
            <s:else>
                <div class="chox-form-item">
                    <label class="chox-form-std-label3">CHO Managing Repair? <img style="display: none" src="../images/sign_info.png" alt="" width="13" height="13" id="dateManagingRepairId" /></label>
                    <s:checkbox name="managingRepair" id="managingRepairCheckId" />
                </div>
            </s:else>
            <s:if test="isNFInsurerManagingRepairLastModified != null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label3">Non-Fault Insurer Managing Repair?</label>
                    <table><tr><td><s:checkbox id="hireMonitoringisNFInsurerManagingRepairId" name="isNFInsurerManagingRepair" /></td><td><img src="../images/sign_info.png" alt="" width="13" height="13" id="dateNonFaultinsurerManagingRepairId" /></td></tr></table>
                </div>
            </s:if>
            <s:else>
                <div class="chox-form-item">
                    <label class="chox-form-std-label3">Non-Fault Insurer Managing Repair?<img style="display: none" src="../images/sign_info.png" alt="" width="13" height="13" id="dateNonFaultinsurerManagingRepairId" /></label>
                    <s:checkbox id="hireMonitoringisNFInsurerManagingRepairId" name="isNFInsurerManagingRepair" />
                </div>
            </s:else>

            <s:if test="clientVatRegisteredLastModified != null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label3">Is The Vehicle Owner VAT Registered?</label>
                    <table><tr><td><s:checkbox id="clientVatRegisteredId" name="clientVatRegistered" /></td><td><img src="../images/sign_info.png" width="13" alt="" height="13" id="dateClientVatRegisteredId" /></td></tr></table>
                </div>
            </s:if>
            <s:else>
                <div class="chox-form-item">
                    <label class="chox-form-std-label3">Is The Vehicle Owner VAT Registered?<img style="display: none" src="../images/sign_info.png" width="13" alt="" height="13" id="dateClientVatRegisteredId" /></label>
                    <s:checkbox   id="clientVatRegisteredId" name="clientVatRegistered" />
                </div>
            </s:else>

            <div class="chox-form-item-button">
            <s:if test="isInsurer">
                <input type="button" id="hireMonitoringIdSubmitButtonId" value="Save Changes" onclick="event.preventDefault(); hireMonitoringSubmit();"/>&nbsp;&nbsp;&nbsp;<s:checkbox disabled='true' name="updateInsurer" /><label>Update Insurer</label>
            </s:if>
            <s:else>
                <input type="button" id="hireMonitoringIdSubmitButtonId" value="Save Changes" onclick="event.preventDefault(); hireMonitoringSubmit();"/><s:if test="allowUpdateInsurer">&nbsp;&nbsp;&nbsp;<s:checkbox name="updateInsurer" /><label>Update Insurer</label></s:if>
            </s:else>
           </div>
            <div id="HMmessageBox" style="text-align:center" class="action-error-msg">
                <s:property value="actionError" />
            </div>
            <div id="HMsuccessBox" class="chox-form-submit-result">
                <s:property value="actionResult" />
            </div>

        </div>

    </fieldset>
</form>