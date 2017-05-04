<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var choBillingBand_a_gridviewJsonReader;
    var choBillingBand_a_gridviewDataStore;
    var choBillingBand_a_gridviewGrid;
    var choBillingBand_a_gridviewData;

    var choBillingBand_s_gridviewJsonReader;
    var choBillingBand_s_gridviewDataStore;
    var choBillingBand_s_gridviewGrid;
    var choBillingBand_s_gridviewData;
    
    var billingBandStore;
    var billingBandCombo;
    var choCombo;
    
    var selectedChoId = -1;
    var selectedBandId = -1;
    
    Ext.onReady(function(){

        var choJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields: [
                {name:'text'},
                {name:'value'}
            ]
        });

        var chos = Ext.util.JSON.decode('<s:property value="chosJsonString" escapeHtml="false"/>');
        var choStore = new Ext.data.Store({
                data : chos,
                reader : choJsonReader
        });
        
        choCombo = new Ext.form.ComboBox({
                store: choStore,
                width: 145,
                renderTo: 'choDropDownDiv',
                valueField: 'text',
                id: 'selectedChoId',
                hiddenName: 'selectedChoId',
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
                            selectedBandId = -1;
                            
                        }else {
                            selectedChoId = this.value;
                            selectedBandId = -1;
                        }
                        onChoBillingBandMappingPageRefresh();
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
            url: '/prv/p/loadChoBillingBands.action',
            reader:billingBandJsonReader
        });
        
        billingBandCombo = new Ext.form.ComboBox({
                store: billingBandStore,
                width: 145,
                renderTo: 'choBillingBandDropDownDiv',
                valueField: 'id',
                id: 'selectedChoBandId',
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
                        onChoBillingBandMappingPageRefresh();
                    }
                }
            });

    
        choBillingBand_a_gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields: [
                {name:'id'},
                {name:'insurerId'},
                {name:'insurerName'},
                {name:'claimType'},
                {name:'claimTypeDesc'}
            ]
        });

        choBillingBand_s_gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields: [
                {name:'id'},
                {name:'insurerId'},
                {name:'insurerName'},
                {name:'claimType'},
                {name:'claimTypeDesc'}
            ]
        });

        choBillingBand_a_gridviewData = new choxDataStore({
            url: '/prv/p/getAvailableChoBillingBandInsurer.action',
            reader:choBillingBand_a_gridviewJsonReader
        });

        choBillingBand_s_gridviewData = new choxDataStore({
            url: '/prv/p/getSelectedChoBillingBandInsurer.action',
            reader:choBillingBand_s_gridviewJsonReader
        });

        choBillingBand_a_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:choBillingBand_recordOnclickAdd },
            store: choBillingBand_a_gridviewData,
            renderTo:'cho_breband_a_gridviewGrid',
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Name", width: 250, dataIndex: 'insurerName', sortable: true, resizable: true},
                {header: "Claim Type", width: 130, dataIndex: 'claimTypeDesc', sortable: true, resizable: true},
                {header: "", width: 50, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>Add</a>";}}
            ],
            height:430,
            width: 360
        });

        choBillingBand_s_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:choBillingBand_recordOnclickRemove },
            store: choBillingBand_s_gridviewData,
            renderTo:'cho_breband_s_gridviewGrid',
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Name", width: 220, dataIndex: 'insurerName', sortable: true, resizable: true},
                {header: "Claim Type", width: 130, dataIndex: 'claimTypeDesc', sortable: true, resizable: true},
                {header: "", width: 80, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>Remove</a>";}}
            ],
            height:430,
            width: 360
        });

        onChoBillingBandMappingPageRefresh();
    });

    function onChoBillingBandMappingPageRefresh(){
        if (selectedChoId === -1) {
            // No insurer selected - clear band dropdown?
            billingBandCombo.reset();
            choBillingBand_a_gridviewData.removeAll(true);
            choBillingBand_s_gridviewData.removeAll(true);
            choBillingBand_a_gridviewGrid.view.refresh();
            choBillingBand_s_gridviewGrid.view.refresh();
        } else if (selectedBandId === -1) { // we have an inurer selected but no band
            loadChoBillingBandDropDown();
            billingBandCombo.reset();
            choBillingBand_a_gridviewData.removeAll(true);
            choBillingBand_s_gridviewData.removeAll(true);
            choBillingBand_a_gridviewGrid.view.refresh();
            choBillingBand_s_gridviewGrid.view.refresh();
        } else { // bith insurer and band selected
            choBillingBandMapping_loadGridViewList();
        }
    }

    function choBillingBandMapping_loadGridViewList(){
        choBillingBand_a_gridviewData.load({params:{chorganisationId:selectedChoId,billingBandId:selectedBandId}});
        choBillingBand_s_gridviewData.load({params:{chorganisationId:selectedChoId,billingBandId:selectedBandId}});
    }

    function choBillingBand_recordOnclickAdd(grid, rowIndex, columnIndex, e){
        if(columnIndex===2){
            var gridView = choBillingBand_a_gridviewGrid.getStore().getAt(rowIndex);
            var insurerId = gridView.get("insurerId");
            var claimType = gridView.get("claimType");
            var url = "/prv/p/doAddChoBillingBandInsurerMapping.action";
            var param = {chorganisationId:selectedChoId, billingBandId:selectedBandId, insurerId:insurerId, claimTypeId:claimType};
            ajax.loadHtml2(url, param, afterChoBillingBandMappingSubmit);
        }
    }

    function choBillingBand_recordOnclickRemove(grid, rowIndex, columnIndex, e){

        if(columnIndex===2){
            var gridView = choBillingBand_s_gridviewGrid.getStore().getAt(rowIndex);
            var billingBandMappingId = gridView.get("id");
            var url = "/prv/p/doRemoveChoBillingBandInsurerMapping.action";
            var param = {billingBandMappingId:billingBandMappingId};
            ajax.loadHtml2(url, param, afterChoBillingBandMappingSubmit);
        }

    }

    function loadChoBillingBandDropDown() {
        billingBandStore.load({params:{chorganisationId:selectedChoId}});
    }
        
    function afterChoBillingBandMappingSubmit(responseText, statusText) {
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
        choBillingBandMapping_loadGridViewList();
    }

</script>

<div id="choBillingBandMappingTab">

<div class="sub-admin-tab-css">

    <div class="status-info">
        Assign Insurers and a relevant Claim Type to a billing band for a selected CHO.
    </div>

    <div class="grid-view-header">
        <table width="100%">
            <tr>
                <td style="width: 300px"></td>
                <td style="width: 100px" valign="top" align="right">
                    <label class="std-label-ro" >CHO Name<span class="mandatory">*</span></label>
                </td>
                <td>
                    <div class="label-block">
                        <div id="choDropDownDiv" class="label-block"/>
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
                        <div id="choBillingBandDropDownDiv" class="label-block"/>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="3">
                    <div class="label-block">
                        <div id="CDChoBillingBandMappingMessageBox" class="action-error-msg"/>
                    </div>
                </td>
            </tr>
        </table>
    </div>

    <table width="100%">
        <tr>
            <td valign="top">
                <label class="gird-view-label">Selected Insurers and Claim Type</label>
                <div id="cho_breband_s_gridviewGrid"></div>
            </td>
            <td valign="top">
                <label class="gird-view-label">Available Insurers and Claim Type</label>
                <div id="cho_breband_a_gridviewGrid"></div>
            </td>
        </tr>
    </table>


</div>

</div>
