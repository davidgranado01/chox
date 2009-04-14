<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var gridviewJsonReader;
    var gridviewDataStore;
    var gridviewGrid;
    var gridviewData;
    var recordPerPage = 20;
    var selectedOrgTypeId = 1 ;
    
    Ext.onReady(function(){
        
       gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',   
            root: 'results', 
            fields:
            [
                {name:'id'},
                {name:'email'},
                {name:'name'},
                {name:'orgName'},
                {name:'status'},
                {name:'statusDesc'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });

        gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: 'user/getUser.action',method:'GET'}),
            reader:gridviewJsonReader      
        });
    
        gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:recordOnclick },
            store: gridviewData,
            loadMask: true,
            columns: [
                {header: "Email", width: 120, dataIndex: 'email', sortable: false, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>" + value + "</a>"}},
                {header: "Name", width: 90, dataIndex: 'name', sortable: false, resizable: true},
                {header: "Organisation", width: 80, dataIndex: 'orgName', sortable: false, resizable: true},
                {header: "Status", width: 50, dataIndex: 'statusDesc', sortable: false, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>" + value + "</a>"}},
                {header: "Created By", width: 100, dataIndex: 'createdBy', sortable: false, resizable: true},
                {header: "Created Date", width: 140, dataIndex: 'createdDate', sortable: false, resizable: true}
            ],
            renderTo:'gridviewGrid',
                width:630,
                autoHeight:true,
                enableHdMenu:false
            });

            var pagingBar = new Ext.PagingToolbar({
                pageSize: recordPerPage,
                store: gridviewData,
                displayInfo: true,
                displayMsg: 'Displaying records {0} - {1} of {2}',
                emptyMsg: "No record to display"
            });    
            
            loadGridViewList()

    }); 
    
    function recordOnclick(grid, rowIndex, columnIndex, e){

        var gridView = gridviewGrid.getStore().getAt(rowIndex);  // Get the Record
        
        if(columnIndex==0){
            alert("EDIT");
        }else if(columnIndex==3){
            triggerStatusUpdateRecord(gridView);
        }
    }
    
    function loadGridViewList(){
    
        gridviewData.load(
        {
            params:
            {
                orgType:selectedOrgTypeId,
                start:0,
                limit:recordPerPage
            }
        });
    }
    
    function doSelectChange(){
        selectedOrgTypeId = $("#orgType").val();
        loadGridViewList();
    }
    
    function triggerStatusUpdateRecord(gridView){
            
            var aletMsg = "Are you sure you want to inactive this user?";
            
            if(!gridView.get("status")){
                aletMsg = "Are you sure you want to activate this user?";
            }
            
            var deleteAtt = confirm(aletMsg);
            
            if(deleteAtt){
                var gridViewId = gridView.get("id");
                
                 $.ajax({
                   url: "doTriggerUserAccountStatus.action?objectId="+gridViewId,
                   success: loadGridViewList
                 });
            }
    }
    
    function createNewRecord(selectedPanel)
    {        
        $("#admin_param_panel").load("loadAdminPanel.action?adminPanelName=" + selectedPanel);
    }
    
</script>

<div>
<fieldset class="x-fieldset">
    <legend>User Management</legend>
    <div id="organisationGird">
        <div class="gridViewHeader">
            <table width="100%">
                <tr>
                    <td>
            Organisation Type: <select id="orgType" onchange="javascript:doSelectChange()">
                <option value="1">CHOX</option>
                <option value="2">Insurer</option>
                <option value="3">Credit Hire</option>
            </select>
                    </td>
                    <td align="right"><button type="button" onclick="javascript:createNewRecord('UserDetailMgmt');">New</button></td>
                </tr>
            </table>

        </div>
        <div id="gridviewGrid"></div>
    </div>
</fieldset>
</div>