<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var choBill_gridviewJsonReader;
    var choBill_gridviewDataStore;
    var choBill_gridviewGrid;
    var choBill_gridviewData;
    var selectedChoId = -1;
    var selectedTriggerId = -1;
    var choCombo;
    var triggersCombo;
    
    Ext.onReady(function(){
        var chosJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields: [
                {name:'text'},
                {name:'value'}
            ]
        });

        var chos = Ext.util.JSON.decode('<s:property value="chosJsonString" escape="false"/>');
        var choStore = new Ext.data.Store({
                data : chos,
                reader : chosJsonReader
        });
        
        choCombo = new Ext.form.ComboBox({
                store: choStore,
                width: 145,
                renderTo: 'billingChoDivId',
                valueField: 'text',
                id: 'billingChoId',
                hiddenName: 'billingChoId',
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
                            selectedChoId = -1;
                        }else {
                            selectedChoId = this.value;
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
            ['Payment Received', 'PaymentReceived'],
            ['Invoice Payment Logged', 'InvoicePaymentLogged'],
            ['Manual Invoice Paid', 'ManualInvoicePaid']
        ];

        var triggersStore = new Ext.data.SimpleStore({
            id: 0,
            fields: ['trigger', 'triggerStatus'],
            data: triggersData
        });

        triggersCombo = new Ext.form.ComboBox({
                store: triggersStore,
                width: 145,
                renderTo: 'choBillingTriggerDivId',
                valueField: 'triggerStatus',
                id: 'choTriggerPointId',
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
                            selectedTriggerId = -1;
                        }else {
                            selectedTriggerId = this.value;
                        }
                    },
                    specialkey:function (el, e) {
                            if(e.keyCode === e.ENTER) {
                                e.preventDefault();
                            }
                    }
                }
        });

        choBill_gridviewJsonReader = new Ext.data.JsonReader({
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

        choBill_gridviewData = new choxDataStore({
            url: '/prv/p/getChoBillingBands.action',
            reader:choBill_gridviewJsonReader
        });

        choBill_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:choBill_recordOnclick},
            store: choBill_gridviewData,
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            loadMask : true,
            viewConfig:{forceFit:true},
            columns: [
                {header: "CHO Name", width: 150, dataIndex: 'orgName', sortable: true, resizable: true},
                {header: "Billing Band Name", width: 180, dataIndex: 'bandName', sortable: true, resizable: true},
                {header: "Cost Per Claim", width: 80, dataIndex: 'costPerClaim', sortable: true, resizable: true},
                {header: "Exclude Supplementary Claims?", width: 150, dataIndex: 'excludeSupplementary', sortable: true, resizable: true},
                {header: "Trigger", width: 120, dataIndex: 'trigger', sortable: true, resizable: true},
                {header: "Action", width: 70, dataIndex: 'id', sortable: false, resizable: true, editable : false, renderer:function(value,p,r){ return "<a href='#' class='high-light-item'>Delete</a>";}}
            ],
            renderTo:'choBilling_grid',
            height:300,
            width: 760
        });

        choBill_loadGridViewList();
        
        $.validator.addMethod("choSelection",
            function(value) {
                if(value === "") {
                    return false;
                }
                return true;
            }
        );

        var form = $("form#choBillingBandForm");

        form.validate({
            errorLabelContainer: "#CDChoBillingMessageBox",
            rules: {
                choBillingBandName : {required:true},
                choCostPerClaim : {required:true,number:true, min:0, max:999.99},
            },
            messages: {
                choBillingBandName : { required : "Please enter a 'Billing Band Name'" },
                choCostPerClaim : { required : "Please enter a 'Cost Per Claim'", number : "The 'Cost Per Claim' must be numeric", min : "The 'Cost Per Claim' cannot be negative",
                        max : "The 'Cost Per Claim' must be less than £1000"},
            }
        });
        
        ui.ajaxForm(form, choBill_onSubmitResponseReceived, 'json');

    });

    function choBill_loadGridViewList(){
        choBill_gridviewData.load({});
    }

    function choBill_recordOnclick(grid, rowIndex, columnIndex, e){
        var gridView = choBill_gridviewGrid.getStore().getAt(rowIndex);
        if(columnIndex===5){
            Ext.MessageBox.confirm('Confirm', 'Are you sure you want to delete this CHO Billing Band?',function(btn){
                if(btn==='yes'){
                    var billingBandId = gridView.get("id");
                    var url = "/prv/p/deleteChoBillingBand.action";
                    var param = {"billingBandId":billingBandId};
                    ajax.loadHtml2(url, param, choBill_onSubmitResponseReceived);
                }
            });
        }
    }


    function choBill_onSubmitResponseReceived(responseText, statusText)  {
        var response = eval('(' + responseText.trim() + ')');
        var outputDiv = $('div#CDChoBillingMessageBox');

        if(response)
        {
            if(response.isValid){
                clearChoBillingBandFormValues();
                if(response.resultType && response.resultType === 'Message')
                {
                    Ext.MessageBox.show({
                        title: '',
                        msg: response.result,
                        width:300,
                        buttons: Ext.MessageBox.OK
                    });
                    choBill_loadGridViewList();
                }
                else
                {
                    choBill_loadGridViewList();
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
                choBill_loadGridViewList();
            }
        }
        else
        {
            triggerCss(outputDiv, true);
            outputDiv.append("Unknown Error Encountered, please try again.");
            outputDiv.addClass("submit-error");
        }

    }
    
    function clearChoBillingBandFormValues() {
        choCombo.reset();
        triggersCombo.reset();
        $("#choBillingBandNameId").val('');
        $("#choCostPerClaimId").val('');
        $("#choExcludeSupplementaryId").attr('checked', false);
    }

    function validateChoBillingComboBox(){
    	var mesBox = $("#CDChoBillingMessageBox");
    	if ($("#billingChoId").val() === "--- Please Select ---" || $("#choTriggerPointId").val() === "--- Please Select ---") {
    		mesBox.empty();
    		if($("#billingChoId").val() === "--- Please Select ---") {
                    mesBox.append("Please select an 'CHO Name'\n<br/>").show();
                }
    		if($("#choTriggerPointId").val() === "--- Please Select ---") {
                    mesBox.append("Please select a 'Trigger'").show();
                }
    		return false;
    	} else {
    		mesBox.text("").show();
    		return true;
    	}
    		
    }

    function doChoBillingBandSubmit(){
        var cho = $("[name='billingChoId']");
    	var mesBox = $("#CDChoBillingMessageBox");
    	if (cho.val() === ""){
    		cho.val(-1);
        }
    	
    	if (validateChoBillingComboBox()) {
                choxJqueryHttpSubmit($("form#choBillingBandForm"));
        }
        return false;
    }
</script>

<div id="choBillingBandTab">

    <div class="sub-admin-tab-css">
        <!--div><label class="chox-claim-header-text">Billing Band Setup</label></div-->
        <br><br>
        <div class="status-info">
            Create and manage the Billing Bands for each CHO by allocating a price and the billing trigger points.
            Use the 'CHO Billing Band Mapping' page to determine which Insurers sit within each billing band.
        </div>
        <div class="grid-view-header">
        <form id="choBillingBandForm" name="choBillingBandForm" class="XXentity-form" action="<%= request.getContextPath()%>/prv/p/addNewChoBillingBand.action" method="post">
            <div class="chox-form-item">
                <label class="chox-form-std-label">CHO Name<span class="mandatory">*</span></label>
                <div id="billingChoDivId"></div>
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Billing Band Name<span class="mandatory">*</span></label>
                <input name="choBillingBandName" id="choBillingBandNameId" type="text">
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Cost Per Claim (£)<span class="mandatory">*</span></label>
                <input name="choCostPerClaim" id="choCostPerClaimId" type="text" onkeyup="extractNumber(this,2,true);">
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Exclude Supplementary Claims?</label>
                <s:checkbox name="choExcludeSupplementary" id="choExcludeSupplementaryId"/>
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Trigger<span class="mandatory">*</span></label>
                <div id="choBillingTriggerDivId"></div>
            </div>
            <div class="chox-form-button">
                <input type="submit" value="Add New Billing Band" onclick="return doChoBillingBandSubmit();"/>
            </div>
        </form>
        </div>
        <div id="CDChoBillingMessageBox" class="action-error-msg"></div>
        <div id="choBilling_grid"></div>

    </div>

</div>
