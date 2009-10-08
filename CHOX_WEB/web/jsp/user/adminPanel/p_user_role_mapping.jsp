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
                {name:'webUserId'},
                {name:'webUserName'},
                {name:'webUserroleId'},
                {name:'webUserroleRole'},
                {name:'webUserroleName'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });

        gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: 'user/getUserole.action?orgTypeId='+<s:property value="orgTypeId" />+'&webUserId='+<s:property value="id" />,method:'GET'}),
            reader:gridviewJsonReader      
        });
        
        gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:recordOnclick },
            store: gridviewData,
            loadMask: true,
            columns: [
                {header: "Role", width: 250, dataIndex: 'webUserroleName', sortable: false, resizable: true},
                {header: "Created By", width: 90, dataIndex: 'createdBy', sortable: false, resizable: true},
                {header: "Created Date", width: 130, dataIndex: 'createdDate', sortable: false, resizable: true},
                {header: "", width: 100, dataIndex: 'Remove', sortable: false, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>Remove</a>"}}
            ],
            renderTo:'gridviewGrid',
                width:615,
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
        
        if(columnIndex==3){
            triggerStatusRemoveRecord(gridView);
        }
    }
    
    function loadGridViewList(){
    
        gridviewData.load(
        {
            params:
            {
                webUserId:<s:property value="id" />
            }
        });
    
    }
    
    function doSelectOnChange(){
        $("#CDUserroleMessageBox").html("");
    }
    
    function triggerStatusRemoveRecord(gridView){
        
        var webUserId = $("#webUserId").val();
        var userrolecode = gridView.get("webUserroleRole");
        var userrolename = gridView.get("webUserroleName");

        var deleteAttMsg = "Are you sure you want to remove this role?";
        $("#CDUserroleMessageBox").html("");
        
        var bFlag = isClaimHandlerGroup();

        
        if((userrolecode=='ROLE_INS_CH'||userrolecode=='ROLE_INS_CH_TL') && bFlag && !isWorkgroupDisabled){
            deleteAttMsg = "Delete '"+userrolename+"' role will delete all workgroup associated as well. Are you sure you want to remove this role";
        }
        
        if(confirm(deleteAttMsg)){

            var gridViewId = gridView.get("id");
             $.ajax({
               url: "removeRoleMapping.action?webUserUserRoleId="+gridViewId+"&webUserId="+webUserId,
               success: onUserroleMappingSubmitResult
             });
        }   
    }

    function isClaimHandlerGroup(){

        var totalRecord = gridviewGrid.getStore().getCount();
        
        var iClaimHandlerCount = 0;
        
        
        for (var iCount=0; iCount<totalRecord; iCount++){
            
            var userrolecode = gridviewGrid.getStore().getAt(iCount).get("webUserroleRole");

            if(userrolecode=='ROLE_INS_CH'||userrolecode=='ROLE_INS_CH_TL'){
                iClaimHandlerCount++;
            }

        }
        
        if(iClaimHandlerCount==1){
            return true;
        }

        return false;
    }
    
    function triggerStatusAddRecord(){

        var webUserId = $("#webUserId").val();
        var webUserRoleId = $("#userrolesId").val();
        
        if(webUserRoleId!=null && webUserRoleId>0){
            
            $("#CDUserroleMessageBox").html("");
            
            $.ajax({
               url: "addNewRoleMapping.action?webUserRoleId="+webUserRoleId+"&webUserId="+webUserId,
               success: onUserroleMappingSubmitResult
            });
            
        }else{

            $("#CDUserroleMessageBox").html("Please select user role.");
            
        }
    }  
    
    function onUserroleMappingSubmitResult(responseText, statusText){
        
        var response = eval('(' + responseText.trim() + ')');        
        
        if(response && response.isValid){
            
            if(response.resultType && response.resultType == 'Message'){
                propmtMsg("User Role", response.result);
            }
        }
        
        doRefreshPage();
    }
        
    function doRefreshPage(){
        $("#admin_param_panel").load("updateUserDetailPanel.action?mode=Edit&objectId=" + <s:property value="id" /> + "&orgTypeId=" + <s:property value="orgTypeId" /> + "&tabIndex="+userDetailTabIndex);
    }
    
</script>

            <div class="status-info">
                [ User Role Description ]
            </div>    
            
<div>
    
    <input id="webUserId" name ="webUserId" type="hidden" value="<s:property value="id" />">
    
    <div id="organisationGird">
        <div class="gridViewHeader">
            <table width="100%">
                <tr>
                    <td>
                        <s:select
                            id="userrolesId"                                 
                            name="userrolesId" 
                            list="userroles" 
                            listKey="id" 
                            listValue="name" 
                            headerKey=""
                            headerValue="--- ALL ---"
                            onchange="javascript: doSelectOnChange();"
                            emptyOption="false">
                        </s:select><input type="button" onclick="javascript: triggerStatusAddRecord();" value="Add New Role"/>
                    </td>
                    <td align="right"></td>
                </tr>
                <tr><td><div id="CDUserroleMessageBox" class="submit-error"></div></td></tr>
            </table>
        </div>
        
        <div id="gridviewGrid"></div>
    </div>

</div>