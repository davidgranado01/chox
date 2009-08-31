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
        
        var deleteAtt = confirm("Are you sure you want to remove this role?");
        
        $("#CDUserroleMessageBox").html("");
        
        if(deleteAtt){
            var gridViewId = gridView.get("id");
             $.ajax({
               url: "removeRoleMapping.action?webUserUserRoleId="+gridViewId,
               success: doRefreshPage
             });
        }
    }
    
    function triggerStatusAddRecord(){

        var webUserId = $("#webUserId").val()
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
        
        responseText = responseText.trim();
        
        if(responseText != ""){
            if(responseText.substring(0,2)=="1:"){
                confirm("Please assign line of business to the user in order to activate the user!");
            }
        }
        
        doRefreshPage();
    }
    
    function doRefreshPage(){
        $("#admin_param_panel").load("updateUserDetailPanel.action?mode=Edit&objectId=" + <s:property value="id" /> + "&orgTypeId=" + <s:property value="orgTypeId" /> + "&tabIndex="+userDetailTabIndex);
    }
    
</script>

<div>
    
    <input id="webUserId" name ="webUserId" type="hidden" value="<s:property value="id" />">
    
<fieldset class="x-fieldset">
    <legend>User Role</legend>
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
</fieldset>
</div>