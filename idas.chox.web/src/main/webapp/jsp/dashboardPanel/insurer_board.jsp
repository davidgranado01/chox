<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var dashBoardInsurerId = -1;
    var dashBoardWorkgroupStore = -1;
    var dashBoardWorkgroupCombo = -1;

    var dashBoardClaimOwnerStore = -1;
    var dashBoardClaimOwnerCombo = -1;

    var choCombo = -1;
    var choId = -1;
    var choOwnerStore = -1;
    var choOwnerCombo = -1;



    $(document).ready(function(){

        
        new Ext.ToolTip({target: 'tip0',html: 'Number of users registered and using CHOX'});
        new Ext.ToolTip({target: 'tipTitle',html: 'Selected Credit Hire Organisation(s) for dashboard data'});
        <s:if test="isInsurer!=true || (isInsurer && insurerIsWorkgroupEnabled)" >
            new Ext.ToolTip({target: 'tipTitle1',html: 'Selected Workgroup(s) for dashboard data'});
        </s:if>

         <s:if test="isInsurer!=true || (isInsurer && insurerIsClaimOwnershipEnabled)" >
            new Ext.ToolTip({target: 'tipTitle2',html: 'Selected Claim Owner(s) for dashboard data'});
         </s:if>
        Ext.QuickTips.init();


        // The 'setValue' function on the combo box doesn't work
        // as, due to the asynchronous nature of this widget, the store may
        // not be loaded. Below is a patch to fix this problem.
        // Note: this code

    
        // Add supplier/CHO drop-down menu
        <s:if test="isInsurer" > 
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
            var choStore = new Ext.data.Store({
                data : mysuppliers,
                reader : suppliersJsonReader
                
            });

            var choComboNumberOfSelectedRecord = 0;
            choCombo = new Ext.ux.form.SuperBoxSelect({
                store : choStore,
                width: 200,
                listWidth:200,
                valueField : 'text',
                id : 'choCombo',
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
                                    choComboNumberOfSelectedRecord ++;
                                    loadDashBoardData();
                                },
                    removeitem : function() {
                                    if (!this.getValue() && choComboNumberOfSelectedRecord >=1) {
                                        choComboNumberOfSelectedRecord = 0;
                                        loadDashBoardData();
                                    } else if (choComboNumberOfSelectedRecord >=1) {
                                        choComboNumberOfSelectedRecord --;
                                        loadDashBoardData();
                                    }
                                }
                }
               });
            
            choCombo.render('searchScreenCHODropDownDiv');
            // end of supplier/CHO drop-down menu
        </s:if>
        // workgroup start

        <s:if test="isInsurer!=true || (isInsurer && insurerIsWorkgroupEnabled)" >
            // Add Workgroup drop-down menu
            var workGroupJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'text'},
                    {name:'value'}
                ]
            });

            dashBoardWorkgroupStore = new Ext.data.Store({
                proxy : new Ext.data.HttpProxy
                ({url : "<%= request.getContextPath()%>/prv/p/WorkgroupDropDownActionByInsurer2.action", method:'GET', params : {"orgId":dashBoardInsurerId}}),
                reader : workGroupJsonReader
            });

            var workgroupComboNumberOfSelectedRecord = 0;
            dashBoardWorkgroupCombo = new Ext.ux.form.SuperBoxSelect({
                store : dashBoardWorkgroupStore,
                width: 200,
                listWidth:200,
                valueField : 'text',
                id : 'dashBoardWorkgroupCombo',
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
                                    workgroupComboNumberOfSelectedRecord ++;
                                    doInsurerSearchWorkgroupOnChange();
                                },
                    removeitem : function() {
                                    if (!this.getValue() && workgroupComboNumberOfSelectedRecord >=1) {
                                        workgroupComboNumberOfSelectedRecord = 0;
                                        doInsurerSearchWorkgroupOnChange();
                                    } else if (workgroupComboNumberOfSelectedRecord >=1) {
                                        workgroupComboNumberOfSelectedRecord --;
                                        doInsurerSearchWorkgroupOnChange();
                                    }
                                }
                }
            });
            dashBoardWorkgroupCombo.render('dashBoardWorkgroupComboDiv');
        </s:if>


        <s:if test="isInsurer!=true || (isInsurer && insurerIsClaimOwnershipEnabled)" >

            var claimOwnerReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'id'},
                    {name:'name'}
                ]
            });
            dashBoardClaimOwnerStore = new Ext.data.Store({
                proxy : new Ext.data.HttpProxy
                ({url : "<%= request.getContextPath()%>/prv/p/SearchClaimHandlerRoleUserDropDownAction.action", method:'GET', params : {"workgroupId":-1,"insurerId":-1}}),
                reader : claimOwnerReader
            });
            
            var claimOwnerComboNumberOfSelectedRecord = 0;
            dashBoardClaimOwnerCombo = new Ext.ux.form.SuperBoxSelect({
                store : dashBoardClaimOwnerStore,
                width: 200,
                listWidth:200,
                valueField : 'id',
                id : 'dashBoardClaimOwnerCombo',
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
                                    claimOwnerComboNumberOfSelectedRecord ++;
                                    loadDashBoardData();
                                },
                    removeitem : function() {
                                    if (!this.getValue() && claimOwnerComboNumberOfSelectedRecord >=1) {
                                        claimOwnerComboNumberOfSelectedRecord = 0;
                                        loadDashBoardData();
                                    } else if (claimOwnerComboNumberOfSelectedRecord >=1) {
                                        claimOwnerComboNumberOfSelectedRecord --;
                                        loadDashBoardData();
                                    }
                                }
                }
            });
            dashBoardClaimOwnerCombo.render('dashBoardClaimOwnerComboDiv');
        </s:if>
        // workgroup end

        doDashBoardInsurerSearchSelectOnChange();
       
        

    }); // end of onready function.



    function loadDashBoardData()
    {
        var choId = -1;
        if (Ext.getCmp('choCombo'))
            choId = Ext.getCmp('choCombo').getValue();
        if (choId==='') {
            choId=-1;
        }

        var workgroupId = -1;
        if (Ext.getCmp('dashBoardWorkgroupCombo'))
            workgroupId = Ext.getCmp('dashBoardWorkgroupCombo').getValue();
        if (workgroupId==='') {
            workgroupId=-1;
        }

        var claimOwnerId = -1;
        if (Ext.getCmp('dashBoardClaimOwnerCombo'))
            claimOwnerId = Ext.getCmp('dashBoardClaimOwnerCombo').getValue();
        if (claimOwnerId==='') {
            claimOwnerId=-1;
        }
        var param = {"supplierId":choId,"workgroupId":workgroupId,"claimOwnerId":claimOwnerId};

        
        $("#resultHolder").block();
        $.get("<%= request.getContextPath()%>/prv/p/showInsurerBoard.action",param, function(data){
            $("#resultHolder").html(data);

        });

       // doDashBoardInsurerSearchSelectOnChange();
    }
    function doUpdate(){

        var selectedValue = '-1';
        var selected = $("#dashboardSupplierId option:selected");
        if(selected.val() != ""){
            selectedValue = selected.val();
        }

        var param = {"supplierId":supplierId};
        $.post("<%= request.getContextPath()%>/prv/p/updateDashBoardSummary.action");
    }

    function doDashBoardInsurerSearchSelectOnChange(){
        if (dashBoardWorkgroupStore != -1) {

            // ToDo: if workgroups are disabled for insurer, disable workgroup menu
            // ToDo: if claim ownership is disabled for insurer, disable claim-owner menu
            dashBoardWorkgroupStore.removeAll();
            dashBoardWorkgroupStore.load({ params : {"orgId":dashBoardInsurerId}});
            dashBoardWorkgroupCombo.reset();
            dashBoardWorkgroupCombo.clearValue();
        }
        doDashBoardShowClaimHandler(-1, dashBoardInsurerId);

    }

    function setDefaultClaimOwner() {
        
         var isInsurerUser = <s:property value="isInsurer"/>;
         if (!isInsurerUser) return;
         var isClaimOwnershipEnabled = '<s:property value="AuthenticatedUser.insurer.claimOwnershipEnable"/>';

         if (isClaimOwnershipEnabled &&  <s:property value="isCH"/>) {
            claimOwnerCombo.setValue(<s:property value="AuthenticatedUser.id"/>);
         }
    }

    function doDashBoardShowClaimHandler(selectedWorkgroupId, selectedInsurerId){

     
        if (dashBoardClaimOwnerStore != -1) {
            dashBoardClaimOwnerStore.removeAll();
            dashBoardClaimOwnerStore.load({ params : {"workgroupId":selectedWorkgroupId,"insurerId":selectedInsurerId}});
            dashBoardClaimOwnerCombo.reset();
            dashBoardClaimOwnerCombo.clearValue();
        }

         // If claimownership is switched on and a claims handler
        // has logged in, then set the claim owner drop-down to
        // the current user
       // if(selectedWorkgroupId === -1)
        //    setDefaultClaimOwner();

            loadDashBoardData();
        

    }

    function doInsurerSearchWorkgroupOnChange(){

        var workgroupId = -1;
        if (dashBoardWorkgroupCombo!= -1 && dashBoardWorkgroupCombo.getValue() != null) {
            workgroupId = dashBoardWorkgroupCombo.getValue().split(",");
        }
        doDashBoardShowClaimHandler(workgroupId, dashBoardInsurerId);
        
    }
</script>

<div class="x-panel-bwrap chox-form-container">
    <fieldset class="x-fieldset">
        <legend>Insurer Admin Dashboard</legend>

        <div class="instruction-message">
            This dashboard displays a snapshot of claims in the
            system to date across a weekly, monthly and cumulative period.
            Results can be viewed for an individual CHO or across the entire
            CHO book. (Please hover over a dashboard item label to see an explanation of the numbers displayed)
        </div>

        <div class="dashboard" class="form-container">
            <table cellpadding="0" cellspacing="0" class="dashboard" border="0">
                <tr><th nowrap><label id="tip0">Number of Active Users</label></th><td colspan="2"><label class="std-data-ro"><s:property value="numberOfActiveUser"/></label></td></tr>
                <tr><th nowrap><label >Last Update Date</label></th><td colspan="2" nowrap="true"><label class="std-data-ro"><s:property value="lastProcessDate"/></label>
                    </td></tr>
                <tr>
                    <th nowrap><label id="tipTitle">Credit Hire Organisation</label></th>
                    <td><div id="searchScreenCHODropDownDiv"></div></td>
                </tr>

                <s:if test="isInsurer">
                <s:if test="insurerIsWorkgroupEnabled">
                <tr>
                    <th nowrap><label id="tipTitle1">Workgroup</label></th>
                    <td><div id="dashBoardWorkgroupComboDiv"></div></td>
                </tr>
                </s:if>
                <s:if test="insurerIsClaimOwnershipEnabled">
                <tr>
                    <th nowrap><label id="tipTitle2">Claim Owner</label></th>
                    <td><div id="dashBoardClaimOwnerComboDiv"></div></td>
                </tr>
                </s:if>
              </s:if>
            </table>
                <s:if test="uploadEnabled">
                    <div style="height:1020px; width:960px" id="resultHolder" name="resultHolder"></div>
                </s:if>
                <s:else>
                    <div style="height:790px; width:960px" id="resultHolder" name="resultHolder"></div>
                </s:else>
            
        </div>

    </fieldset>
</div>
