<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="idas.chox.core.search.ClaimSearchCriteria" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var insurerId = -1;
    var choInsurerCombo = -1;

    var dashBoardChoClaimOwnerStore = -1;
    var dashBoardChoClaimOwnerCombo = -1;


    Ext.onReady(function() {
            
        new Ext.ToolTip({target: 'tip0',html: 'Number of users registered and using CHOX'});
        new Ext.ToolTip({target: 'tipTitle',html: 'Selected Insurer(s) for dashboard data'});
        <s:if test="isCHO!=true || (isCHO && choIsClaimOwnershipEnabled)" >
            new Ext.ToolTip({target: 'tipTitle1',html: 'Selected Claim Owner(s) for dashboard data'});
        </s:if>
        Ext.QuickTips.init();

        // The 'setValue' function on the combo box doesn't work
        // as, due to the asynchronous nature of this widget, the store may
        // not be loaded. Below is a patch to fix this problem.
        // Note: this code

    
        // CHO Drop-Down
        <s:if test="isCHO" > 
            // Add insurers drop-down menu
            var choinsurersJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'text'},
                    {name:'value'}
                ]
            });

            
            var mychoinsurers = Ext.util.JSON.decode('<s:property value="insurersJsonString" escape="false"/>');
            var choInsurersStore = new Ext.data.Store({
                data : mychoinsurers,
                reader : choinsurersJsonReader
            });
            
            var choInsurerComboNumberOfSelectedRecord = 0;
            choInsurerCombo = new Ext.ux.form.SuperBoxSelect({
                store : choInsurersStore,
                width: 200,
                listWidth:200,
                valueField : 'text',
                id : 'choInsurerCombo',
                displayField :'value',
                typeAhead : true,
                mode : 'local',
                triggerAction : 'all',
                emptyText : '--- ALL ---',
                removeValuesFromStore : false,
                selectOnFocus : true,
//                allowBlank : true,
                forceSelection : true,
                listeners: {
                    select : function(){ 
                                    choInsurerComboNumberOfSelectedRecord ++;
                                    loadChoDashBoardData();
                                },
                    removeitem : function() {
                                    if (!this.getValue() && choInsurerComboNumberOfSelectedRecord >=1) {
                                        choInsurerComboNumberOfSelectedRecord = 0;
                                        loadChoDashBoardData();
                                    } else if (choInsurerComboNumberOfSelectedRecord >=1) {
                                        choInsurerComboNumberOfSelectedRecord --;
                                        loadChoDashBoardData();
                                    }
                                }
                }
            });

            choInsurerCombo.render('dashBoardChoInsurerDropDownDiv');
        </s:if> // end of Insurer drop-down menu

       <s:if test="isCHO!=true || (isCHO  && choIsClaimOwnershipEnabled) ">


             // Add CHO claim owner combo box
            var choClaimOwnerReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                [
                    {name:'id'},
                    {name:'name'}
                ]
            });

            dashBoardChoClaimOwnerStore = new choxDataStore({
                url : "/prv/p/SearchSupplierClaimOwnerDropDownAction.action",
                params : {"supplierId":-1},
                   // Don't know if this is neded (search code for this already exists
                   // - just uncomment this to add and it should work
                listeners: {load: function() {
                   <s:if test="isCHO" >
                       var notAssigned = new Array();
                       // this next assignment is ugly and should be removed/refactored at some point
                       notAssigned['id'] = '<%= ClaimSearchCriteria.CLAIM_OWNER_NOT_ASSIGNED %>';
                       notAssigned['name'] = 'NOT ASSIGNED';
                       this.insert(0, new Ext.data.Record(notAssigned));
                   </s:if>
                }},
                reader : choClaimOwnerReader
            });

            var choOwnerComboNumberOfSelectedRecord = 0;
            dashBoardChoClaimOwnerCombo = new Ext.ux.form.SuperBoxSelect({
                store : dashBoardChoClaimOwnerStore,
                width: 200,
                listWidth:200,
                valueField : 'id',
                id : 'dashBoardChoClaimOwnerCombo',
                displayField :'name',
                typeAhead : true,
                mode : 'local',
                triggerAction : 'all',
                emptyText : '--- ALL ---',
                removeValuesFromStore : false,
                selectOnFocus : true,
//                allowBlank : true,
                forceSelection : true,
                listeners: {
                        select : function(){ 
                                        choOwnerComboNumberOfSelectedRecord ++;
                                        loadChoDashBoardData();
                                    },
                        removeitem : function() {
                                        if (!this.getValue() && choOwnerComboNumberOfSelectedRecord >=1) {
                                            choOwnerComboNumberOfSelectedRecord = 0;
                                            loadChoDashBoardData();
                                        } else if (choOwnerComboNumberOfSelectedRecord >=1) {
                                            choOwnerComboNumberOfSelectedRecord --;
                                            loadChoDashBoardData();
                                        }
                                    }
                }
            });

            dashBoardChoClaimOwnerCombo.render('dashBoardChoClaimOwnerComboDiv');
        </s:if>

       doChoClaimOwnerHandler(insurerId);

    });
      
    function loadChoDashBoardData()
    {
        var insurerId = -1;
        if (Ext.getCmp('choInsurerCombo'))
            insurerId = Ext.getCmp('choInsurerCombo').getValue();
        if (insurerId==='') {
            insurerId=-1;
        }

        var claimOwnerId = -1;
        if (Ext.getCmp('dashBoardChoClaimOwnerCombo'))
            claimOwnerId = Ext.getCmp('dashBoardChoClaimOwnerCombo').getValue();
        if (claimOwnerId==='') {
            claimOwnerId=-1;
        }

        var param = {"insurerId":insurerId,"choClaimOwnerId":claimOwnerId};

      

        $("#resultHolder").block();
        
        var url = "/prv/p/showChoBoard.action";
        ajax.loadHtml2(url,param,function(data){
            $("#resultHolder").html(data);
        });
    }

    function doChoClaimOwnerHandler(selectedSupplierId){

        if (dashBoardChoClaimOwnerStore != -1) {
            dashBoardChoClaimOwnerCombo.reset();
            dashBoardChoClaimOwnerStore.removeAll();
            dashBoardChoClaimOwnerCombo.clearValue();
            dashBoardChoClaimOwnerStore.load({ params : {"supplierId":selectedSupplierId}});
        }

       loadChoDashBoardData();


    }

</script>  


<div class="x-panel-bwrap chox-form-container" id="dashboardId"> 
    <fieldset class="x-fieldset">
        <legend>CHO Admin Dashboard</legend>

        <div class="instruction-message">
            This dashboard displays a snapshot of claims in the system
            to date across a weekly, monthly and cumulative period. Results can
            be viewed for an individual Insurer or across the entire Insurer
            book. (Please hover over a dashboard item label to see an explanation of the numbers displayed)
        </div>

        <div class="form-container">
            <table cellpadding="0" cellspacing="0" class="dashboard" border="0">       
                <tr><th nowrap><label id="tip0">Number of Active Users</label></th><td colspan="2"><label class="std-data-ro"><s:property value="numberOfActiveUser"/></label></td></tr>
                <tr><th nowrap><label >Last Update Date</label></th><td colspan="2" nowrap="true"><label class="std-data-ro"><s:property value="lastProcessDate"/></label>
                    </td></tr>
                <tr>
                    <th nowrap><label id="tipTitle">Insurer</label></th>
                    <td><div id="dashBoardChoInsurerDropDownDiv"></div></td>
                </tr>
                <s:if test="isCHO">
                <s:if test="choIsClaimOwnershipEnabled">
                <tr>
                    <th nowrap><label id="tipTitle1">Claim Owner</label></th>
                    <td><div id="dashBoardChoClaimOwnerComboDiv"></div></td>
                </tr>
                </s:if>
                </s:if>
            </table>
            <div style="height:750px; width:960px" id="resultHolder" name="resultHolder"></div>
        </div>        

    </fieldset>
</div>
