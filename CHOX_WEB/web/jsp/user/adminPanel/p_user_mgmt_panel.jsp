<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var gridviewJsonReader;
    var gridviewDataStore;
    var gridviewGrid;
    var gridviewData;
    var recordPerPage = 20;
    
    var orgTypeId = -1;
    var orgId = -1;

   var selectOrgTypeId = <s:property value="selectOrgTypeId" />;
   var selectOrgId = <s:property value="selectOrgId" />;
   
    Ext.onReady(function(){
    
       //alert(selectOrgTypeId);
       
       if(selectOrgTypeId>0){
            $("#orgTypeId").val(selectOrgTypeId);
       }
       if(selectOrgId>0){
           $("#orgId").val(selectOrgId);
       }
       /*
       if(selectOrgId>0){
            orgId = selectOrgId;
            $("#orgId").val(selectOrgId);
       }
       */
      
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
                width:615,
                autoHeight:true,
                enableHdMenu:false,
                bbar: pagingBar
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

        var gridView = gridviewGrid.getStore().getAt(rowIndex);
        
        if(columnIndex==0){
            loadSelectedRecord(grid, rowIndex, columnIndex, e);
        }else if(columnIndex==3){
            triggerStatusUpdateRecord(gridView);
        }
    }

    function getParameters(){
        orgTypeId = $("#orgTypeId").val();
        orgId = $("#orgId").val();
    }
    
    function loadSelectedRecord(grid, rowIndex, columnIndex, e){
        var gridView = gridviewGrid.getStore().getAt(rowIndex);
        var gridViewId = gridView.get("id");
        getParameters();
        $("#admin_param_panel").load("updateUserDetailPanel.action?mode=Edit&objectId=" + gridViewId + "&orgTypeId=" + orgTypeId);
    }

    function createNewRecord(){
         var gridViewId = -1;
         getParameters();
        $("#admin_param_panel").load("updateUserDetailPanel.action?mode=New&objectId=" + gridViewId + "&orgTypeId=" + orgTypeId);
    }
    
    function loadGridViewList(){
    
        getParameters();
        
        gridviewData.load(
        {
            params:
            {
                orgTypeId:orgTypeId,
                orgId:orgId
            }
        });
    }
    
    function doSelectChange(){        
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
    

    
</script>

<fieldset class="x-fieldset">
    <legend>User Management</legend>
    <div id="organisationGird">
        <div class="gridViewHeader">
            <table width="100%">
                <tr>
                    <td><s:property value="orgTypeId" />
                           
<s:if test="isSelectable">
Organisation Type: <select id="orgTypeId" onchange="javascript:doSelectChange()">
                <option value="1">CHOX</option>
                <option value="2">Insurer</option>
                <option value="3">Credit Hire</option>
            </select>
</s:if>
<s:else>
    <input name="orgTypeId" id="orgTypeId" type="hidden" value="<s:property value="orgTypeId" />">
</s:else>

<input name="orgId" id="orgId" type="hidden" value="<s:property value="orgId" />">


                    </td>
                    <td align="right"><button type="button" onclick="javascript:createNewRecord();">New</button></td>
                </tr>
            </table>

        </div>
        <div id="gridviewGrid"></div>
    </div>
</fieldset>
</div>