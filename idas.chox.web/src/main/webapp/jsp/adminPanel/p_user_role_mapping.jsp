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
            ({url: '<%= request.getContextPath()%>/prv/p/getUserole.action?orgTypeId='+<s:property value="orgTypeId" />, method:'POST'}),
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
            width:700,
            height:460
        });
  
        loadGridViewList()

    }); 
    
    function recordOnclick(grid, rowIndex, columnIndex, e){

        var gridView = gridviewGrid.getStore().getAt(rowIndex);
        
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

        $("#CDUserroleMessageBox").html("");
        
        var webUserId = $("#webUserId").val();
        var gridViewId = gridView.get("id");
        var webUserrolecode = gridView.get("webUserroleRole");
        var defaultdeleteMsg = "Are you sure you want to remove this role?";
        
        // ONLY PERFORM CHECK IF AND ONLY IF USER ARE REMOVING COM OR CH
        if((webUserrolecode=='ROLE_INS_CH' || webUserrolecode=='ROLE_INS_COM' || webUserrolecode=='ROLE_INS_FNOL')){
            
            $.getJSON("<%= request.getContextPath()%>/prv/p/isRoleAllowToDelete.action?webUserRoleCode="+webUserrolecode+"&webUserId="+webUserId+uniqeToken(), function(data){
                if(!data.isAllowToDelete){
                    alert(data.warningMsg);
                }else{
                    if(confirm(data.warningMsg)){
                        doDeleteUserRoleMapping(gridViewId, webUserId);
                    }
                }
            });
        }else{
            if(confirm(defaultdeleteMsg)){        
                doDeleteUserRoleMapping(gridViewId, webUserId);
            }
        }
    }


    function doDeleteUserRoleMapping(gridViewId, webUserId){
    
        $.ajax({
            url: "<%= request.getContextPath()%>/prv/p/removeRoleMapping.action?webUserUserRoleId="+gridViewId+"&webUserId="+webUserId+uniqeToken(),
            success: onUserroleMappingSubmitResult
        });

    }
    
    function triggerStatusAddRecord(){

        var webUserId = $("#webUserId").val();
        var webUserRoleId = $("#userrolesId").val();
        
        if(webUserRoleId!=null && webUserRoleId>0){
            
            $("#CDUserroleMessageBox").html("");
            
            $.ajax({
                url: "<%= request.getContextPath()%>/prv/p/addNewRoleMapping.action?webUserRoleId="+webUserRoleId+"&webUserId="+webUserId+uniqeToken(),
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
                alert(response.result);
            }
        }
        
        doRefreshPage();
    }

    function doRefreshPage(){       
        var sLocaltion = "#admin_param_panel";
        var sAction = "<%= request.getContextPath()%>/prv/p/updateUserDetailPanel.action";
        var sparameters = "mode=Edit&objectId=" + <s:property value="id" /> + "&orgTypeId=" + <s:property value="orgTypeId" /> + "&tabIndex=" + userDetailTabIndex;
        doSectionLoad(sLocaltion, sAction, sparameters);
    }
    
</script>

<div class="status-info">
    Assign one or more user roles against this user by using the 'Add New Role' button below. The assignment of roles will dictate which work queues the user will see as well the user's access/permission rights.
</div>    

<div>

    <input id="webUserId" name ="webUserId" type="hidden" value="<s:property value="id" />">

    <div id="organisationGird">
        <div class="gridViewHeader">
            <table width="100%">
                <tr>
                    <td>User: <b><s:property value="displayName" /></b></td>
                </tr>
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