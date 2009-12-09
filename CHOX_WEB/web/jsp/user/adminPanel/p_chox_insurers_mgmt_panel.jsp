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
                {name:'vatNo'},
                {name:'companyNo'},
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
                {header: "Name", width: 160, dataIndex: 'name', sortable: true, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='highlightItem'>" + value + "</a>"}},
                {header: "VAT No.", width: 110, dataIndex: 'vatNo', sortable: true, resizable: true},
                {header: "Company No.", width: 120, dataIndex: 'companyNo', sortable: true, resizable: true},
                {header: "Active", width: 50, dataIndex: 'statusDesc', sortable: true, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='highlightItem'>" + value + "</a>"}},
                {header: "Created By", width:80, dataIndex: 'createdBy', sortable: true, resizable: true},
                {header: "Created Date", width:140, dataIndex: 'createdDate', sortable: true, resizable: true}
            ],
            height: 575,
            width: 720
        });

        gridviewGrid.render('gridviewGridHolderId');
        
        gridviewData.load(
        {
            params:
            {

            }
        });
        
    }); 
    
    function recordOnclick(grid, rowIndex, columnIndex, e){

        var gridView = gridviewGrid.getStore().getAt(rowIndex);
        
        if(columnIndex==0){
            loadSelectedRecord(grid, rowIndex, columnIndex, e);
        }else if(columnIndex==3){
            triggerStatusUpdateRecord(gridView);
        }
        
    }
    
    function loadSelectedRecord(grid, rowIndex, columnIndex, e){
        var gridView = gridviewGrid.getStore().getAt(rowIndex);
        var gridViewId = gridView.get("id");
        $("#admin_param_panel").load("updateInsurerDetailPanel.action?objectId=" + gridViewId+uniqeToken());
    }

    function createNewRecord(){
        var gridViewId = -1;
        $("#admin_param_panel").load("updateInsurerDetailPanel.action?objectId=" + gridViewId+uniqeToken());
    }
    
    function loadGridViewList(){
        gridviewData.load({ params: { start:0, limit:recordPerPage } });
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
               url: "doTriggerInsurerAccountStatus.action?objectId="+gridViewId+uniqeToken(),
               success: loadGridViewList
             });
        }
    }
    
</script>

<div id="chox-admin-holder">
    <form id="ChoxInsurerMgmtPanelForm" name="ChoxInsurerMgmtPanelForm" class="XXentity-form" action="POST">
        <fieldset class="x-fieldset">
            
            <legend>Insurer Organisation Management</legend>

                <div class="admin-gridview-header">
                    <table>
                        <tr>
                            <td id="label"></td>
                            <td id="buttons"><button type="button" onclick="javascript:createNewRecord();">Add New Insurer</button></td>
                        </tr>
                    </table>
                </div>
            
                <div id="gridviewGridHolderId"></div>

        </fieldset>
    </form> 
    
</div>
