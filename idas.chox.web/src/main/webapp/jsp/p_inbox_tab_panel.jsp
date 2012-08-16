<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var filterName;
    var title;
    var defaultDropdownValue={'value':-1,'text':'--- ALL ---'};

    Ext.onReady(function(){
    
    <s:if test="isCHO" > 
            document.getElementById('queueOrgFilter').innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;Insurer Filter : &nbsp;&nbsp;';

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
            insurerFilterCombo.setValue(-1);              
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
                supplierFilterCombo.setValue(-1);
            }
    </s:elseif>

    });

    function reloadQueues() {
        var orgCombo = Ext.ComponentMgr.get('filterOrgId');
        var selectedOrg = -1;
        if (orgCombo) {
            selectedOrg = orgCombo.getValue();
        }

        if (!selectedOrg)
            selectedOrg = -1;
        refreshFilterPanelByOrg(filterName, title, selectedOrg);
    }
    
    function updateFilter(key, gridTitle) {
        var orgCombo = Ext.ComponentMgr.get('filterOrgId');
        var selectedOrg = -1;
        if (orgCombo) {
            selectedOrg = orgCombo.getValue();
        }

        if (!selectedOrg)
            selectedOrg = -1;
        filterName = key;
        title = gridTitle;
        return executeFilterByOrg(key, gridTitle, selectedOrg);
    }
    
    

</script>
<div id="filterPanel" style="float: left;">
    <label id="queueOrgFilter" style="float: left;"></label>
    <div id="orgFilterDiv"></div>
    <div id="filterPanel2">
        <s:action name="getFilterRecordCounters" namespace="/prv/p" executeResult="true" />
    </div>
</div>
<s:if test="taskManagementEnabled">
    <div id="taskPanelDiv">
        <s:action name="getTaskPanel" namespace="/prv/p" executeResult="true" />
    </div>
</s:if>
