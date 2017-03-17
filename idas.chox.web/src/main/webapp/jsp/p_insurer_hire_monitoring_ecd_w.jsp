<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var insurerEcdDateField;
    var insurerEcdDataStore;
    
    Ext.onReady(function () {

        insurerEcdDateField = new Ext.form.DateField({
            name: 'insurerEcdDate',
            id: 'insurerEcdDate',
            width: 100,
            allowBlank: true,
            format: 'd/m/Y',
            showWeekNumber: true,
            validationEvent: false,
            value: '<s:date format="dd/MM/yyyy" name="date" />',
            renderTo: 'insurerEcdDatePH'
        });

        var form = $("#formAddNewInsurerHMEcd");

        form.validate({
                errorLabelContainer: "#insurerECDMessageBox",
                rules: {
                    insurerReasonOfDelayId: {required: true},
                    insurerEcdDate: {required: true, dateITA: true},
                    insurerSupportingNote: {required: true}
                },
                messages: {
                    insurerReasonOfDelayId: {
                        required: "You must supply a value for 'Reason for Delay'"
                    },
                    insurerEcdDate: {
                        required: "You must supply a value for 'New ECD'",
                        dateITA: "You must supply valid date format for 'New ECD'"
                    },
                    insurerSupportingNote: {
                        required: "You must supply a value for 'Supporting Note'"
                    }
                }
        });
                
        var fsets = $('legend', form);
        fsets.click(function () {$(this).next().toggle();});
        fsets.mouseover(function () {$(this).css("cursor", "pointer");});
        fsets.mouseout(function () {$(this).css("cursor", "normal");});

        var insurerEcdJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                    [
                        {name: 'sequence'},
                        {name: 'ecdDate'},
                        {name: 'createdDate'},
                        {name: 'reason'},
                        {name: 'supportingNote'}]
        });

        insurerEcdDataStore = new choxDataStore({
            url: '/prv/p/getInsurerHireMonitoringEcds.action',
            reader: insurerEcdJsonReader
        });

        var insurerEcdGrid = new Ext.grid.GridPanel({
            listeners: {cellclick: loadInsurerHireMonitor},
            store: insurerEcdDataStore,
            renderTo: 'insurerEcdGridHolder',
            enableHdMenu: false,
            id: 'insurerECDGridId',
            layout: 'fit',
            viewConfig: {forceFit: true},
            columns: [
                {header: "ECD Date", width: 70, dataIndex: 'ecdDate', sortable: false, resizable: true},
                {header: "Created", width: 70, dataIndex: 'createdDate', sortable: false, resizable: true},
                {header: "Reason", width: 90, dataIndex: 'reason', sortable: false, resizable: true},
                {header: "Supporting Note", width: 235, dataIndex: 'supportingNote', sortable: false, resizable: true}
            ],
            width: 475,
            autoHeight: true
        });

        loadInsurerEcds();
    });

    function loadInsurerHireMonitor(grid, rowIndex, columnIndex, e) {
        var hiremonitoringECD = insurerEcdDataStore.getAt(rowIndex);
        var supportingNoteText = "<br/><b>Supporting note</b>: <br/>" + hiremonitoringECD.get("supportingNote");
        var ecdText = "<b>ECD Date</b>: " + hiremonitoringECD.get("ecdDate");
        var reasonText = "<b>Reason</b>: " + hiremonitoringECD.get("reason");
        var title = ecdText;
        var msg = ecdText + "<br/>" + reasonText + "<br/>" + supportingNoteText;
        propmtMsg(title, msg);
    }

    function loadInsurerEcds() {
        insurerEcdDataStore.load({params: {id: <s:property value="claimId" />}});
    }

    function onAfterInsurerEcdSubmit(responseText, statusText) {
        loadInsurerEcds();
        doResetInsurerForm();
      }

    function doResetInsurerForm() {
        document.formAddNewInsurerHMEcd.insurerEcdDate.value = "";
        document.formAddNewInsurerHMEcd.insurerReasonOfDelayId.value = "";
        document.formAddNewInsurerHMEcd.insurerSupportingNote.value = "";
    }

    function doPopulateInsurerNote() {
        var reasonOfDelayId = $('#insurerReasonOfDelayId :selected').val();
        $("#insurerECDSupportingNote").val(getReasonDescription(reasonOfDelayId));
    }

    function getInsurerReasonDescription(id) {

    <s:iterator value="reasonOfDelay">
        if (id == "<s:property value="id"/>") {
            return "<s:property value="description"/>";
        }
    </s:iterator>

    }

    function addNewInsurerHireMonitoringEcd() {

        if ($("#formAddNewInsurerHMEcd").valid()) {
            choxExtAjaxRequest({
                url: '/prv/p/addNewInsurerHireMonitoringEcd.action',
                params: {
                    ecdDate: insurerEcdDateField.getRawValue(),
                    reasonOfDelayId: $('#insurerReasonOfDelayId :selected').val(),
                    supportingNote: $("#insurerECDSupportingNote").val(),
                    name: $("#insurerEcdActivityNameId").val()
                },
                callback: function (options, success, response) {
                    var response = Ext.util.JSON.decode(response.responseText);
                    if (!response.success) {
                        Ext.MessageBox.show({
                            title: 'Error',
                            msg: response.errors,
                            width: 300,
                            closable: false,
                            buttons: Ext.MessageBox.OK,
                            icon: Ext.MessageBox.ERROR
                        });
                    }
                    onAfterInsurerEcdSubmit();
                }
            });

        }
    }
</script>

<form id="formAddNewInsurerHMEcd" name="formAddNewInsurerHMEcd" class="XXentity-form">
    <s:hidden id="insurerEcdActivityNameId" name="name" value="insurerEcdUpdate"/>
    <fieldset class="x-fieldset partial">
        <legend>New/Revised ECD</legend>
        <div class="form-container" id="newInsurerRevisedECDWId">
            <s:if test="isECDFormVisible">
                <div class="chox-form-item">
                    <label class="chox-form-std-label" style="width:150px;">New ECD<span class="mandatory">*</span></label>
                    <span id="insurerEcdDatePH"></span>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label" style="width:150px;">Reason for Delay<span class="mandatory">*</span></label>
                    <s:select 
                        name="insurerReasonOfDelayId" id="insurerReasonOfDelayId" list="reasonOfDelay"
                        listKey="id" listValue="name" headerKey=""
                        headerValue="-- Please Select --"
                        emptyOption="false" onchange="doPopulateInsurerNote();" cssClass="hm-reason-drop-down">
                    </s:select>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label" style="width:150px;">Supporting Note<span class="mandatory">*</span></label>
                    <textarea class="chox-tta" id="insurerECDSupportingNote" cols="30" rows="5" name="insurerSupportingNote"><s:property value="supportingNote" /></textarea>
                </div>
                <div class="chox-form-item-button">
                    <input type="button" id="insurerHireMonitoringEcdSubmitButtonId" value="Save Changes" onclick="return addNewInsurerHireMonitoringEcd()"/>&nbsp;&nbsp;&nbsp;
                </div>
                <div id="insurerECDMessageBox" class="action-error-msg"></div>
            </s:if>
            <s:else>
                <span id="insurerEcdDatePH" style="visibility:hidden;"></span>
                <input type="hidden" name="insurerReasonOfDelayId"/>
                <input id="insurerECDSupportingNote" name="insurerSupportingNote" type="hidden"/>
            </s:else>
            <div id="insurerEcdGridHolder"></div>
        </div>
    </fieldset>
</form>