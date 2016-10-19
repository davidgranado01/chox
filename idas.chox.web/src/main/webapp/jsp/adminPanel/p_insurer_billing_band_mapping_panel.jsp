<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var insBillingBand_a_gridviewJsonReader;
    var insBillingBand_a_gridviewDataStore;
    var insBillingBand_a_gridviewGrid;
    var insBillingBand_a_gridviewData;

    var insBillingBand_s_gridviewJsonReader;
    var insBillingBand_s_gridviewDataStore;
    var insBillingBand_s_gridviewGrid;
    var insBillingBand_s_gridviewData;
    
    var billingBandStore;
    var billingBandCombo;
    var insurerCombo;
    
    var selectedInsurerId = -1;
    var selectedBandId = -1;
    
    Ext.onReady(function(){

        var insurersJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields: [
                {name:'text'},
                {name:'value'}
            ]
        });

        var insurers = Ext.util.JSON.decode('<s:property value="insurersJsonString" escape="false"/>');
        var insurerStore = new Ext.data.Store({
                data : insurers,
                reader : insurersJsonReader
        });
        
        insurerCombo = new Ext.form.ComboBox({
                store: insurerStore,
                width: 145,
                renderTo: 'insDropDownDiv',
                valueField: 'text',
                id: 'selectedInsurerId',
                hiddenName: 'selectedInsurerId',
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
                            selectedBandId = -1;
                            
                        }else {
                            selectedInsurerId = this.value;
                            selectedBandId = -1;
                        }
                        onInsurerBillingBandMappingPageRefresh();
                    }
                }
            });

        var billingBandJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields: [
                {name:'id'},
                {name:'orgName'},
                {name:'bandName'},
                {name:'trigger'},
                {name:'costPerClaim'},
                {name:'excludeSupplementary'}
            ]
        });

        billingBandStore  = new choxDataStore({
            url: '/prv/p/loadInsurerBillingBands.action',
            reader:billingBandJsonReader
        });
        
        billingBandCombo = new Ext.form.ComboBox({
                store: billingBandStore,
                width: 145,
                renderTo: 'insBillingBandDropDownDiv',
                valueField: 'id',
                id: 'selectedBandId',
                hiddenName: 'selectedBandId',
                displayField:'bandName',
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
                            selectedBandId = -1;                            
                        }else {
                            selectedBandId = this.value;
                        }
                        onInsurerBillingBandMappingPageRefresh();
                    }
                }
            });

    
        insBillingBand_a_gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields: [
                {name:'id'},
                {name:'chorganisationId'},
                {name:'chorganisationName'},
                {name:'claimType'},
                {name:'claimTypeDesc'}
            ]
        });

        insBillingBand_s_gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields: [
                {name:'id'},
                {name:'chorganisationId'},
                {name:'chorganisationName'},
                {name:'claimType'},
                {name:'claimTypeDesc'}
            ]
        });

        insBillingBand_a_gridviewData = new choxDataStore({
            url: '/prv/p/getAvailableInsurerBillingBandChorganisation.action',
            reader:insBillingBand_a_gridviewJsonReader
        });

        insBillingBand_s_gridviewData = new choxDataStore({
            url: '/prv/p/getSelectedInsurerBillingBandChorganisation.action',
            reader:insBillingBand_s_gridviewJsonReader
        });

        insBillingBand_a_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:insBillingBand_recordOnclickAdd },
            store: insBillingBand_a_gridviewData,
            renderTo:'ins_breband_a_gridviewGrid',
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Name", width: 250, dataIndex: 'chorganisationName', sortable: true, resizable: true},
                {header: "Claim Type", width: 130, dataIndex: 'claimTypeDesc', sortable: true, resizable: true},
                {header: "", width: 50, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>Add</a>";}}
            ],
            height:430,
            width: 360
        });

        insBillingBand_s_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:insBillingBand_recordOnclickRemove },
            store: insBillingBand_s_gridviewData,
            renderTo:'ins_breband_s_gridviewGrid',
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Name", width: 220, dataIndex: 'chorganisationName', sortable: true, resizable: true},
                {header: "Claim Type", width: 130, dataIndex: 'claimTypeDesc', sortable: true, resizable: true},
                {header: "", width: 80, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>Remove</a>";}}
            ],
            height:430,
            width: 360
        });

        onInsurerBillingBandMappingPageRefresh();
    });

    function onInsurerBillingBandMappingPageRefresh(){
        if (selectedInsurerId === -1) {
            // No insurer selected - clear band dropdown?
            billingBandCombo.reset();
            insBillingBand_a_gridviewData.removeAll(true);
            insBillingBand_s_gridviewData.removeAll(true);
            insBillingBand_a_gridviewGrid.view.refresh();
            insBillingBand_s_gridviewGrid.view.refresh();
        } else if (selectedBandId === -1) { // we have an inurer selected but no band
            loadInsurerBillingBandDropDown();
            billingBandCombo.reset();
            insBillingBand_a_gridviewData.removeAll(true);
            insBillingBand_s_gridviewData.removeAll(true);
            insBillingBand_a_gridviewGrid.view.refresh();
            insBillingBand_s_gridviewGrid.view.refresh();
        } else { // bith insurer and band selected
            insBillingBandMapping_loadGridViewList();
        }
    }

    function insBillingBandMapping_loadGridViewList(){
        insBillingBand_a_gridviewData.load({params:{insurerId:selectedInsurerId,billingBandId:selectedBandId}});
        insBillingBand_s_gridviewData.load({params:{insurerId:selectedInsurerId,billingBandId:selectedBandId}});
    }

    function insBillingBand_recordOnclickAdd(grid, rowIndex, columnIndex, e){
        if(columnIndex===2){
            var gridView = insBillingBand_a_gridviewGrid.getStore().getAt(rowIndex);
            var chorganisationId = gridView.get("chorganisationId");
            var claimType = gridView.get("claimType");
            var url = "/prv/p/doAddInsurerBillingBandChorganisationMapping.action";
            var param = {insurerId:selectedInsurerId, billingBandId:selectedBandId, chorganisationId:chorganisationId, claimTypeId:claimType};
            ajax.loadHtml2(url, param, afterBillingBandMappingSubmit);
        }
    }

    function insBillingBand_recordOnclickRemove(grid, rowIndex, columnIndex, e){

        if(columnIndex===2){
            var gridView = insBillingBand_s_gridviewGrid.getStore().getAt(rowIndex);
            var billingBandMappingId = gridView.get("id");
            var url = "/prv/p/doRemoveInsurerBillingBandChorganisationMapping.action";
            var param = {billingBandMappingId:billingBandMappingId};
            ajax.loadHtml2(url, param, afterBillingBandMappingSubmit);
        }

    }

    function loadInsurerBillingBandDropDown() {
        billingBandStore.load({params:{insurerId:selectedInsurerId}});
    }
        
    function afterBillingBandMappingSubmit(responseText, statusText) {
       var response = eval('(' + responseText.trim() + ')');
       
       if(response)
        {
            if(!response.isValid){
               $.each(response.errors, function() {
                    Ext.MessageBox.show({
                        title: '',
                        msg: this.toString(),
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }); 
            } 
            
        }
        insBillingBandMapping_loadGridViewList();
    }

</script>

<div id="insurerBillingBandMappingTab">

<div class="sub-admin-tab-css">

    <div class="status-info">
        Assign CHOs and a relevant Claim Type to a billing band for a selected Insurer.
    </div>

    <div class="grid-view-header">
        <table width="100%">
            <tr>
                <td style="width: 300px"></td>
                <td style="width: 100px" valign="top" align="right">
                    <label class="std-label-ro" >Insurer Name<span class="mandatory">*</span></label>
                </td>
                <td>
                    <div class="label-block">
                        <div id="insDropDownDiv" class="label-block"/>
                    </div>
                </td>
            </tr>
            <tr>
                <td></td>
                <td valign="top" align="right">
                    <label class="std-label-ro">Billing Band Name<span class="mandatory">*</span></label>
                </td>
                <td>
                    <div class="label-block">
                        <div id="insBillingBandDropDownDiv" class="label-block"/>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="3">
                    <div class="label-block">
                        <div id="CDInsBillingBandMappingMessageBox" class="action-error-msg"/>
                    </div>
                </td>
            </tr>
        </table>
    </div>

    <table width="100%">
        <tr>
            <td valign="top">
                <label class="gird-view-label">Selected Credit Hire Organisations and Claim Type</label>
                <div id="ins_breband_s_gridviewGrid"></div>
            </td>
            <td valign="top">
                <label class="gird-view-label">Available Credit Hire Organisations and Claim Type</label>
                <div id="ins_breband_a_gridviewGrid"></div>
            </td>
        </tr>
    </table>


</div>

</div>
