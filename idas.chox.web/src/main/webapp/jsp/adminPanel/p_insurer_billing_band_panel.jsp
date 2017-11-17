<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var insBill_gridviewJsonReader;
    var insBill_gridviewDataStore;
    var insBill_gridviewGrid;
    var insBill_gridviewData;
    var selectedInsurerId = -1;
    var selectedInsurerTriggerId = -1;
    var insurerCombo;
    var insurerTriggersCombo;
    
    Ext.onReady(function(){
        var insurersJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields: [
                {name:'text'},
                {name:'value'}
            ]
        });

        var insurers = Ext.util.JSON.decode('<s:property value="insurersJsonString" escapeHtml="false"/>');
        var insurerStore = new Ext.data.Store({
                data : insurers,
                reader : insurersJsonReader
        });
        
        insurerCombo = new Ext.form.ComboBox({
                store: insurerStore,
                width: 145,
                renderTo: 'billingInsurerDivId',
                valueField: 'text',
                id: 'billingInsurerId',
                hiddenName: 'billingInsurerId',
                displayField:'value',
                typeAhead: true,
                mode: 'local',
                triggerAction: 'all',
                emptyText: '--- Please Select ---',
                forceSelection: true,
                listWidth: 145,
                selectOnFocus: true,
                listeners: {
                    select: function () {
                        if(this.getRawValue() === "") {
                            this.clearValue();
                            this.reset();
                            selectedInsurerId = -1;
                        }else {
                            selectedInsurerId = this.value;
                        }
                    },
                    specialkey:function (el, e) {
                            if(e.keyCode === e.ENTER) {
                                e.preventDefault();
                            }
                    }
                }
            });
            
        var triggersData = [
            ['Accepted Claims', 'AwaitingCarHireInfo'],
            ['Invoice Payment Logged', 'InvoicePaymentLogged'],
            ['Payment Received', 'PaymentReceived'],
            ['Manual Invoice Paid', 'ManualInvoicePaid']
        ];

        var triggersStore = new Ext.data.SimpleStore({
            id: 0,
            fields: ['trigger', 'triggerStatus'],
            data: triggersData
        });

        insurerTriggersCombo = new Ext.form.ComboBox({
                store: triggersStore,
                width: 145,
                renderTo: 'insurerBillingTriggerDivId',
                valueField: 'triggerStatus',
                id: 'insurerTriggerPoint',
                hiddenName: 'triggerPoint',
                displayField:'trigger',
                typeAhead: true,
                mode: 'local',
                triggerAction: 'all',
                emptyText: '--- Please Select ---',
                forceSelection: true,
                listWidth: 145,
                selectOnFocus: true,
                listeners: {
                    select: function () {
                        if(this.getRawValue() === "") {
                            this.clearValue();
                            this.reset();
                            selectedInsurerTriggerId = -1;
                        }else {
                            selectedInsurerTriggerId = this.value;
                        }
                    },
                    specialkey:function (el, e) {
                            if(e.keyCode === e.ENTER) {
                                e.preventDefault();
                            }
                    }
                }
        });

        insBill_gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields: [
                {name:'id'},
                {name:'orgName'},
                {name:'bandName'},
                {name:'costPerClaim'},
                {name:'excludeSupplementary'},
                {name:'trigger'}
            ]
        });

        insBill_gridviewData = new choxDataStore({
            url: '/prv/p/getInsurerBillingBands.action',
            reader:insBill_gridviewJsonReader
        });

        insBill_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:insBill_recordOnclick},
            store: insBill_gridviewData,
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            loadMask : true,
            viewConfig:{forceFit:true},
            columns: [
                {header: "Insurer Name", width: 150, dataIndex: 'orgName', sortable: true, resizable: true},
                {header: "Billing Band Name", width: 180, dataIndex: 'bandName', sortable: true, resizable: true},
                {header: "Cost Per Claim", width: 80, dataIndex: 'costPerClaim', sortable: true, resizable: true},
                {header: "Exclude Supplementary Claims?", width: 150, dataIndex: 'excludeSupplementary', sortable: true, resizable: true},
                {header: "Trigger", width: 120, dataIndex: 'trigger', sortable: true, resizable: true},
                {header: "Action", width: 70, dataIndex: 'id', sortable: false, resizable: true, editable : false, renderer:function(value,p,r){ return "<a href='#' class='high-light-item'>Delete</a>";}}
            ],
            renderTo:'insurerBilling_grid',
            height:300,
            width: 760
        });

        insBill_loadGridViewList();
        
        $.validator.addMethod("insurerSelection",
            function(value) {
                if(value === "") {
                    return false;
                }
                return true;
            }
        );

        var form = $("form#insurerBillingBandForm");

        form.validate({
            errorLabelContainer: "#CDInsurerBillingMessageBox",
            rules: {
                insurerBillingBandName : {required:true},
                insurerCostPerClaim : {required:true,number:true, min:0, max:999.99},
            },
            messages: {
                insurerBillingBandName : { required : "Please enter a 'Billing Band Name'" },
                insurerCostPerClaim : { required : "Please enter a 'Cost Per Claim'", number : "The 'Cost Per Claim' must be numeric", min : "The 'Cost Per Claim' cannot be negative",
                        max : "The 'Cost Per Claim' must be less than £1000"},
            }
        });
        
        ui.ajaxForm(form, insBill_onSubmitResponseReceived, 'json');

    });

    function insBill_loadGridViewList(){
        insBill_gridviewData.load({});
    }

    function insBill_recordOnclick(grid, rowIndex, columnIndex, e){
        var gridView = insBill_gridviewGrid.getStore().getAt(rowIndex);
        if(columnIndex===5){
            Ext.MessageBox.confirm('Confirm', 'Are you sure you want to delete this Insurer Billing Band?',function(btn){
                if(btn==='yes'){
                    var billingBandId = gridView.get("id");
                    var url = "/prv/p/deleteInsurerBillingBand.action";
                    var param = {"billingBandId":billingBandId};
                    ajax.loadHtml2(url, param, insBill_onSubmitResponseReceived);
                }
            });
        }
    }


    function insBill_onSubmitResponseReceived(responseText, statusText)  {
        var response = eval('(' + responseText.trim() + ')');
        var outputDiv = $('div#CDInsurerBillingMessageBox');

        if(response)
        {
            if(response.isValid){
                clearInsurerBillingBandFormValues();
                if(response.resultType && response.resultType === 'Message')
                {
                    Ext.MessageBox.show({
                        title: '',
                        msg: response.result,
                        width:300,
                        buttons: Ext.MessageBox.OK
                    });
                    insBill_loadGridViewList();
                }
                else
                {
                    insBill_loadGridViewList();
                }
            }
            else
            {
               $.each(response.errors, function() {
                    
                    Ext.Msg.show({
                        title: 'Error',
                        msg:this.toString(),
                        icon:Ext.Msg.ERROR,
                        buttons:Ext.Msg.OK,
                        width : 400
                    });
                });
                insBill_loadGridViewList();
            }
        }
        else
        {
            triggerCss(outputDiv, true);
            outputDiv.append("Unknown Error Encountered, please try again.");
            outputDiv.addClass("submit-error");
        }

    }
    
    function clearInsurerBillingBandFormValues() {
        insurerCombo.reset();
        insurerTriggersCombo.reset();
        $("#insurerBillingBandNameId").val('');
        $("#insurerCostPerClaimId").val('');
        $("#insurerExcludeSupplementaryId").attr('checked', false);
    }

    function validateComboBox(){
    	var mesBox = $("#CDInsurerBillingMessageBox");
    	if ($("#billingInsurerId").val() === "--- Please Select ---" || $("#insurerTriggerPoint").val() === "--- Please Select ---") {
    		mesBox.empty();
    		if($("#billingInsurerId").val() === "--- Please Select ---") {
                    mesBox.append("Please select an 'Insurer Name'\n<br/>").show();
                }
    		if($("#insurerTriggerPoint").val() === "--- Please Select ---") {
                    mesBox.append("Please select a 'Trigger'").show();
                }
    		return false;
    	} else {
    		mesBox.text("").show();
    		return true;
    	}
    		
    }

    function doInsurerBillingBandSubmit(){
        var ins = $("[name='billingInsurerId']");
    	var mesBox = $("#CDInsurerBillingMessageBox");
    	if (ins.val() === ""){
    		ins.val(-1);
        }
    	
    	if (validateComboBox()) {
                choxJqueryHttpSubmit($("form#insurerBillingBandForm"));
        }
        return false;
    }
</script>

<div id="insurerBillingBandTab">

    <div class="sub-admin-tab-css">
        <!--div><label class="chox-claim-header-text">Billing Band Setup</label></div-->
        <br><br>
        <div class="status-info">
            Create and manage the Billing Bands for each Insurer by allocating a price and the billing trigger points.
            Use the 'Insurer Billing Band Mapping' page to determine which CHOs sit within each billing band.
        </div>
        <div class="grid-view-header">
        <form id="insurerBillingBandForm" name="insurerBillingBandForm" class="XXentity-form" action="<%= request.getContextPath()%>/prv/p/addNewInsurerBillingBand.action" method="post">
            <div class="chox-form-item">
                <label class="chox-form-std-label">Insurer Name<span class="mandatory">*</span></label>
                <div id="billingInsurerDivId"></div>
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Billing Band Name<span class="mandatory">*</span></label>
                <input name="insurerBillingBandName" id="insurerBillingBandNameId" type="text">
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Cost Per Claim (£)<span class="mandatory">*</span></label>
                <input name="insurerCostPerClaim" id="insurerCostPerClaimId" type="text" onkeyup="extractNumber(this,2,true);">
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Exclude Supplementary Claims?</label>
                <s:checkbox name="insurerExcludeSupplementary" id="insurerExcludeSupplementaryId"/>
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Trigger<span class="mandatory">*</span></label>
                <div id="insurerBillingTriggerDivId"></div>
            </div>
            <div class="chox-form-button">
                <input type="submit" value="Add New Billing Band" onclick="event.preventDefault(); doInsurerBillingBandSubmit();"/>
            </div>
        </form>
        </div>
        <div id="CDInsurerBillingMessageBox" class="action-error-msg"></div>
        <div id="insurerBilling_grid"></div>

    </div>

</div>
