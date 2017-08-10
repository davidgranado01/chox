<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var vcHMCombo;
    
    Ext.onReady(function() {

        var rentalStartDatePicker = new Ext.form.DateField({
            name: 'rentalStart',
            id: 'insurerRentalStartId',
            width: 100,
            allowBlank: true,
            format: 'd/m/Y',
            showWeekNumber: true,
            validationEvent : false,
            value: '<s:date format="dd/MM/yyyy" name="rentalStart" />',
            renderTo: 'insurerRentalMonitoringStartPH'
        });
        
        var rentalStartTimPicker = new Ext.form.TimeField({
            name: 'rentalStartTime',
            id : 'insurerRentalStartTimePickerHMVId',
            width: 100,
            allowBlank: true,
            validationEvent : false,
            increment: 15,
            format:'H:i',
            value: '<s:property value="rentalStartTime" />',
            renderTo:'insurerRentalMonitoringStartTimePH'
        });
        
        var vcHMJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'text'},
                    {name:'value'}
                ]
        });

        var vcHMStore = new choxDataStore({
                url : "/prv/p/getAvailableVehicleClasses.action",
                reader : vcHMJsonReader,
                listeners: {load: function() {
                    vcHMCombo.setValue('<s:property value="vehicleClass.id"/>');    
                }}
        });

        vcHMCombo = new Ext.form.ComboBox({
                store: vcHMStore,
                renderTo: 'vcInsurerHMSelectionHolder',
                valueField: 'text',
                id: 'vcInsurerHMComboId',
                hiddenName: 'vehicleClassMonitoringId',
                displayField:'value',
                typeAhead: true,
                autoWidth: true,
                listWidth: 100,
                width: 100,
                mode: 'local',
                triggerAction: 'all',
                forceSelection : true,
                emptyText: '--- SELECT ---'
        });
        vcHMStore.load();    
        
        $.validator.addMethod('time', function (value) {
            if (value === '') return true;
            else return (/^(\d{2}:\d{2})$/).test(value);
        });
        
        var form = $("#formUpdateInsurerHireMonitoringVehicle");

        var fsets =  $('legend',form);
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});

        form.validate(
        {
            errorLabelContainer: "#insurerHVDmessageBox",
            rules: {
                rentalStart:{dateITA:true, required : true},
                rentalStartTime:{time:true}
            },
            messages: {
                rentalStart: {dateITA:"Invalid date format for 'Hire Start (Date)'", required : "You must select a 'Hire Start (Date)'"},
                rentalStartTime: {time:"Invalid date format for 'Hire Start (Time)'"}
            }
        });
        ui.ajaxForm(form,null,'html');
        $("#insurerHVDSuccessBox").fadeOut(10000);
    });

</script>

<form id="formUpdateInsurerHireMonitoringVehicle" action="<%=request.getContextPath()%>/prv/p/updateInsurerMonitoringVehicleHire.action" class="XXentity-form">
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <fieldset class="x-fieldset partial">
        <legend>Hire Vehicle Details</legend>
        <div class="form-container" id="insurerHireMonitoringVehicleDetailWId">
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Replacement Vehicle Class</label>
                <div id="vcInsurerHMSelectionHolder"></div>
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Hire Start (Date)<span class="mandatory">*</span></label>
                <span id="insurerRentalMonitoringStartPH"></span></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Hire Start (Time)</label>
                <span id="insurerRentalMonitoringStartTimePH"></span></div>
            <div class="chox-form-button">
                <input type="submit" id="insurerHireMonitoringVehicleSubmitButtonId" value="Save Changes" />
            </div>
            <div id="insurerHVDmessageBox" style="text-align:center" class="action-error-msg"><s:property value="actionError" /></div>
            <div id="insurerHVDSuccessBox" class="chox-form-submit-result"><s:property value="actionResult" /></div>
        </div>
    </fieldset>
</form>