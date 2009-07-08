<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var gridviewJsonReader;
    var gridviewDataStore;
    var gridviewGrid;  
    var gridviewData;
    var recordPerPage = 20;

    Ext.onReady(function(){
        
       gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',   
            root: 'results', 
            fields:
            [
                {name:'id'},
                {name:'name'},  
                {name:'address'},
                {name:'vatNo'},
                {name:'companyNo'},
                {name:'authoritiyDelegated'},
                {name:'status'},
                {name:'statusDesc'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });     

       gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: 'user/getChorganisation.action',method:'GET'}),
            reader:gridviewJsonReader      
        });

        gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:recordOnclick },
            store: gridviewData,
            columns: [
                {header: "Name", width: 110, dataIndex: 'name', sortable: true, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='highlightItem'>" + value + "</a>"}},
                {header: "Address", width: 170, dataIndex: 'address', sortable: true, resizable: true},
                {header: "VAT No.", width: 80, dataIndex: 'vatNo', sortable: true, resizable: true},
                {header: "Active", width: 50, dataIndex: 'statusDesc', sortable: true, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='highlightItem'>" + value + "</a>"}},                
               // {header: "Company No.", width: 75, dataIndex: 'companyNo', sortable: false, resizable: true},
               // {header: "Authority", width: 55, dataIndex: 'authoritiyDelegated', sortable: false, resizable: true},
                {header: "Created By", width: 80, dataIndex: 'createdBy', sortable: true, resizable: true},
                {header: "Created Date", width: 140, dataIndex: 'createdDate', sortable: true, resizable: true}
            ],
            renderTo:'gridviewGrid',
                width:615,
                autoHeight:true,
                enableHdMenu:false
            });

            gridviewData.load(
            {
                params:
                {
                    gridviewtype : "InsurerChoBandMgmt"
                }
            });
    

    }); 
    
    function loadGridViewList(){

        gridviewData.load(
        {
            params:
            {
                start:0,
                limit:recordPerPage
            }
        });
        
    }
    
    function recordOnclick(grid, rowIndex, columnIndex, e){

        var gridView = gridviewGrid.getStore().getAt(rowIndex);  // Get the Record
        
        if(columnIndex==0){
            loadSelectedRecord(grid, rowIndex, columnIndex, e);
        }else if(columnIndex==3){
            triggerStatusUpdateRecord(gridView);
        }
    }
    
    function loadSelectedRecord(grid, rowIndex, columnIndex, e){
        var gridView = gridviewGrid.getStore().getAt(rowIndex);
        var gridViewId = gridView.get("id");
        $("#admin_param_panel").load("updateChorganisationDetailPanel.action?objectId=" + gridViewId);
    }

    function createNewRecord(){
         var gridViewId = -1;
        $("#admin_param_panel").load("updateChorganisationDetailPanel.action?objectId=" + gridViewId);
    }
    
    function triggerStatusUpdateRecord(gridView){
            
            var aletMsg = "Are you sure you want to make this Credit Hire Organisation inactive?";
            
            if(!gridView.get("status")){
                aletMsg = "Are you sure you want to make this Credit Hire Organisation active?";
            }
            
            var deleteAtt = confirm(aletMsg);
            
            if(deleteAtt){
                var gridViewId = gridView.get("id");
                
                 $.ajax({
                   url: "doTriggerCreditHireAccountStatus.action?objectId="+gridViewId,
                   success: loadGridViewList
                 });
            }
    }
    
</script>

<form id="formAdminParam" class="XXentity-form" name="formAdminParam">

<fieldset class="x-fieldset">
    <legend>Credit Hire Organisation Management</legend>
    <div id="organisationGird">
        <div class="gridViewHeader">
            <table width="100%">
                <tr>
                    <td></td>
                    <td align="right"><button type="button" onclick="javascript:createNewRecord();">Add New Credit Hire Organisation</button></td>
                </tr>
            </table>

        </div>        
        <div id="gridviewGrid" style="height:597px; overflow:auto;"></div>
    </div>
</fieldset>
</form> 