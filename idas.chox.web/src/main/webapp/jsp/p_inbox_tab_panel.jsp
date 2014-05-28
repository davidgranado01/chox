<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var filterName;
    var title;
    var defaultDropdownValue={'value':-1,'text':'--- ALL ---'};

    Ext.onReady(function(){
    
    <s:if test="isCHO" > 
            document.getElementById('queueOrgFilter').innerHTML = 'Insurer Filter : &nbsp;&nbsp;';

            var insurersJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'text'},
                    {name: 'value'}
                ]
            });

            var myinsurers = Ext.util.JSON.decode('<s:property value="insurersJsonString" escape="false"/>');
            var insurersStore = new Ext.data.Store({
                data : myinsurers,
                reader : insurersJsonReader,
                listeners: {load: function() {this.insert(0, new Ext.data.Record(defaultDropdownValue));}}
            });

            var insurerFilterCombo = new Ext.form.ComboBox({
                store : insurersStore,
                //                    renderTo: 'orgFilterDiv',
                id:'filterOrgId',
                autoHeight: true,
                autoWidth: false,
                width: 180,
                listWidth: 180,
                valueField : 'value',
                displayField :'text',
                typeAhead : true,
                mode : 'local',
                triggerAction : 'all',
                valueNotFoundText : '--- ALL ---',
                selectOnFocus : true,
                listeners: {
                    select: reloadQueues,
                    blur: function () {
                        if(this.getRawValue() == "" ) {
                            this.clearValue();
                            reloadQueues();
                        }
                    }
                }
            });
                
            insurerFilterCombo.render('orgFilterDiv');
            insurerFilterCombo.setValue(<s:property value="filterOrgId" />);              
    </s:if>

    <s:elseif test="isInsurer" > 
            if (document.getElementById('queueOrgFilter')) {
                document.getElementById('queueOrgFilter').innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;CHO Filter : &nbsp;&nbsp;';
            
                var suppliersJsonReader = new Ext.data.JsonReader({
                    totalProperty: 'totalCount',
                    root: 'results',
                    fields:
                        [
                        {name:'text'},
                        {name:'value'}
                    ]
                });

                var mysuppliers = Ext.util.JSON.decode('<s:property value="suppliersJsonString" escape="false"/>');
                var suppliersStore = new Ext.data.Store({
                    data : mysuppliers,
                    reader : suppliersJsonReader,
                    listeners: {load: function() {this.insert(0, new Ext.data.Record(defaultDropdownValue));}}
                });

                var supplierFilterCombo = new Ext.form.ComboBox({
                    store : suppliersStore,
                    //                renderTo: 'orgFilterDiv',
                    id:'filterOrgId',
                    width: 180,
                    listWidth: 180,
                    valueField : 'value',
                    displayField :'text',
                    typeAhead : true,
                    mode : 'local',
                    triggerAction : 'all',
                    valueNotFoundText : '--- ALL ---',
                    selectOnFocus : true,
                    listeners: {
                        select: reloadQueues,
                        blur: function () {
                            if(this.getRawValue() == "" ) {
                                this.clearValue();
                                reloadQueues();
                            }
                        }
                    }
                });
                supplierFilterCombo.render('orgFilterDiv');
                supplierFilterCombo.setValue(<s:property value="filterOrgId" />);
            }
            
    </s:elseif>

    if (document.getElementById('queueClaimTypeFilter')) {
        document.getElementById('queueClaimTypeFilter').innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;Claim Type Filter : &nbsp;&nbsp;';
    
        var claimTypeJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'text'},
                {name:'value'}
            ]
        });

        var claimTypes = Ext.util.JSON.decode('<s:property value="claimTypesJsonString" escape="false"/>');
        var claimTypesStore = new Ext.data.Store({
            data : claimTypes,
            reader : claimTypeJsonReader,
            listeners: {load: function() {this.insert(0, new Ext.data.Record(defaultDropdownValue));}}
        });

        var claimTypeFilterCombo = new Ext.form.ComboBox({
            store : claimTypesStore,
            id:'filterClaimTypeId',
            width: 180,
            listWidth: 180,
            valueField : 'value',
            displayField :'text',
            typeAhead : true,
            typeAhead : true,
            mode : 'local',
            triggerAction : 'all',
            valueNotFoundText : '--- ALL ---',
            value : -1,
            selectOnFocus : true,
            listeners: {
                select: reloadQueues,
                blur: function () {
                    if(this.getRawValue() == "" ) {
                        this.clearValue();
                        reloadQueues();
                    }
                }
            }
        });
        
        claimTypeFilterCombo.setValue(<s:property value="filterClaimTypeId" />);
        claimTypeFilterCombo.render('claimTypeFilterDiv');
    }
    filterName = '<s:property value="filterKey" />';
    title = '<s:property value="gridTitle" />';
    });

    function reloadQueues() {
        var orgCombo = Ext.ComponentMgr.get('filterOrgId');
        var selectedOrg = <s:property value="filterOrgId" />;
        if (orgCombo) {
            selectedOrg = orgCombo.getValue();
            Ext.state.Manager.set("filter_org_id",orgCombo.getValue());
        } 

        if (!selectedOrg)
            selectedOrg = -1;
        
        var ctCombo = Ext.ComponentMgr.get('filterClaimTypeId');
        var selectedClaimType = <s:property value="filterClaimTypeId" />;
        if (ctCombo) {
            selectedClaimType = ctCombo.getValue();
            Ext.state.Manager.set("filter_claim_type_id",ctCombo.getValue());
        } 
        var filterName = Ext.state.Manager.get("grid_filterName");
        var title = Ext.state.Manager.get("grid_main_title").replace("Queue: ","");
        refreshFilterPanelByOrgOrClaimType(filterName, title, selectedOrg, selectedClaimType);
    }
    
    function updateFilter(key, gridTitle) {
        var orgCombo = Ext.ComponentMgr.get('filterOrgId');
        var selectedOrg = <s:property value="filterOrgId" />;
        if (orgCombo) {
            selectedOrg = orgCombo.getValue();
        }
        
        var ctCombo = Ext.ComponentMgr.get('filterClaimTypeId');
        var selectedClaimType = <s:property value="filterClaimTypeId" />;
        if (ctCombo) {
            selectedClaimType = ctCombo.getValue();
        }
        
        Ext.state.Manager.set("grid_filterName",key);
        Ext.state.Manager.set("grid_title","Queue: "+gridTitle);
        filterName = key;
        title = gridTitle;
        return executeFilterByOrgAndClaimType(key, gridTitle, selectedOrg, selectedClaimType);
    }
    
    function refreshFilterPanelByOrgOrClaimType(filterName, title, orgId, claimTypeId) {
        var url = "/prv/p/getFilterRecordCounters.action";
        var param = {"filterOrgId":orgId, "filterClaimTypeId":claimTypeId, "filterName":filterName};
        ajax.loadHtml2(url, param, function(data){
            $("div#filterPanel2").html(data);
        });
        if (filterName)
            executeFilterByOrgAndClaimType(filterName, title, orgId, claimTypeId);
    }
    
    function executeFilterByOrgAndClaimType(filterName,gridTitle, orgId, claimTypeId) {
        updateManualInvoiceBatchUpdate(filterName);
        Ext.state.Manager.set("grid_isInboxShowHistory",true);
        isInboxShowHistory = true;
        Ext.state.Manager.set("grid_filterName",filterName);
        Ext.state.Manager.set("grid_title","Queue: "+gridTitle);
        ds.baseParams = {"filterName" : filterName, "filterOrgId" : orgId, "filterClaimTypeId":claimTypeId, searchHistory : true};
        doDataLoad(0, recordPerPage,Ext.state.Manager.get("grid_title"));
    }

</script>
<div id="filterPanel" style="float: left;">

    <table style="margin-top: 4px;" >
        <tr>
            <td><label id="queueOrgFilter" style="float: right;"></label></td>
            <td><div id="orgFilterDiv"></div></td>
        </tr>
        <tr>
            <td><label id="queueClaimTypeFilter" style="float: right;"></label></td>
            <td><div id="claimTypeFilterDiv"></div></td>
        </tr>
    </table>
    <div id="filterPanel2">
        <s:action name="getFilterRecordCounters" namespace="/prv/p"
            executeResult="true" />
    </div>
</div>
