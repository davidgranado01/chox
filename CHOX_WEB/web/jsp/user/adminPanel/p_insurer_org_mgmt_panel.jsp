<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var gridviewJsonReader;
    var gridviewDataStore;
    var gridviewGrid;  
    var gridviewData;
    var recordPerPage = 20;
    var win;
    var selectedObjectId;
                
    Ext.onReady(function(){
    
   gridviewJsonReader = new Ext.data.JsonReader({
        totalProperty: 'totalCount',   
        root: 'results', 
        fields:
        [
            {name:'id'},
            {name:'name'},
            {name:'status'},
            {name:'statusDesc'},
            {name:'createdBy'},
            {name:'createdDate'}
        ]
    });     

    gridviewData = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy
        ({url: 'user/getInsurer.action',method:'GET'}),
        reader:gridviewJsonReader      
    });
    
    gridviewGrid = new Ext.grid.GridPanel({
        listeners:  {cellclick:recordOnclick },
        store: gridviewData,
        columns: [
            {header: "Name", width: 200, dataIndex: 'name', sortable: false, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>" + value + "</a>"}},
            {header: "Status", width: 100, dataIndex: 'statusDesc', sortable: false, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>" + value + "</a>"}},
            {header: "Created By", width: 110, dataIndex: 'createdBy', sortable: false, resizable: true},
            {header: "Created Date", width: 150, dataIndex: 'createdDate', sortable: false, resizable: true}
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
                gridviewtype : "insurerOrgMgmt"
            }
        });
    }); 
    
    function recordOnclick(grid, rowIndex, columnIndex, e){

        var gridView = gridviewGrid.getStore().getAt(rowIndex);
        
        if(columnIndex==0){
            loadSelectedRecord(grid, rowIndex, columnIndex, e);
        }else if(columnIndex==1){
            triggerStatusUpdateRecord(gridView);
        }
    }
    
    function loadSelectedRecord(grid, rowIndex, columnIndex, e){
        var gridView = gridviewGrid.getStore().getAt(rowIndex);
        var gridViewId = gridView.get("id");
        $("#admin_param_panel").load("updateInsurerDetailPanel.action?objectId=" + gridViewId);
    }

    function createNewRecord(){
        var gridViewId = -1;
        $("#admin_param_panel").load("updateInsurerDetailPanel.action?objectId=" + gridViewId);
    }
    
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

    function triggerStatusUpdateRecord(gridView){
            
        var aletMsg = "Are you sure you want to inactive this insurer?";

        if(!gridView.get("status")){
            aletMsg = "Are you sure you want to activate this insurer?";
        }

        var deleteAtt = confirm(aletMsg);

        if(deleteAtt){
            var gridViewId = gridView.get("id");

             $.ajax({
               url: "doTriggerInsurerAccountStatus.action?objectId="+gridViewId,
               success: loadGridViewList
             });
        }
    }
    
</script>

<div>
    
    <form id="formAdminParam" class="XXentity-form" name="formAdminParam">
    <fieldset class="x-fieldset">
        <legend>Insurer Organisation Management</legend>
        <div id="organisationGird">
            <div class="gridViewHeader">
                <table width="100%">
                    <tr>
                        <td></td>
                        <td align="right"><button type="button" onclick="javascript:createNewRecord();">New</button></td>
                    </tr>
                </table>
            </div>
            <div id="gridviewGrid" style="height:570px; overflow:auto;" ></div>
        </div>
    </fieldset>
    </form> 
    
</div>
