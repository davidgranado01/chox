<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var gridviewJsonReader;
    var gridviewDataStore;
    var gridviewGrid;
    var gridviewData;
    var recordPerPage = 20;

    var userRoleId = -1;
    var orgTypeId = <s:property value="OrgTypeId" />;
    var organisationId = <s:property value="OrgId" />;
    var isChoxAdmin = <s:property value="IsChoxAdmin" />;

    Ext.onReady(function(){

        if(!isChoxAdmin){
            $("#organisationId").val(organisationId);
            $("#orgTypeId").val(orgTypeId);
        }
        
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
                {name:'createdDate'},
                {name:'role'},
                {name:'isExpired'}
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
                {header: "Email", width: 120, dataIndex: 'email', sortable: true, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>" + value + "</a>"}},
                {header: "Name", width: 90, dataIndex: 'name', sortable: true, resizable: true},
                {header: "Organisation", width: 80, dataIndex: 'orgName', sortable: true, resizable: true},
                {header: "Active", width: 50, dataIndex: 'statusDesc', sortable: true, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>" + value + "</a>"}},
                {header: "Role", width: 150, dataIndex: 'role', sortable: true, resizable: true},
                {header: "Has Password Expired?", width: 140, dataIndex: 'isExpired', sortable: false, resizable: true,renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>" + value + "</a>"}}
            ],
            width: 720,
            height: 510
        });

        gridviewGrid.render('gridviewGridHolderId');
            
        pageRefresh();

    }); 

    function pageRefresh(){
        getParameters();
        showOrganisationDropDownDiv();
        showUserroleDropDown();
        loadGridViewList();
    }

    function recordOnclick(grid, rowIndex, columnIndex, e){

        var gridView = gridviewGrid.getStore().getAt(rowIndex);
        
        if(columnIndex==0){
            loadSelectedRecord(grid, rowIndex, columnIndex, e);
        }else if(columnIndex==3){
            triggerStatusUpdateRecord(gridView);
        }
        else if(columnIndex==5){
            triggerIsExpiredUpdateRecord(gridView);
        }
    }

    function getParameters(){
        
        orgTypeId = $("#orgTypeId").val();
        userRoleId = $("#userrolesId").val();
        
        if(isChoxAdmin){
            organisationId=$("#organisationId").val();
        }
        
    }
    
    function loadSelectedRecord(grid, rowIndex, columnIndex, e){

        getParameters();
        
        var gridView = gridviewGrid.getStore().getAt(rowIndex);
        var gridViewId = gridView.get("id");
        var sLocaltion = "#admin_param_panel";
        var sAction = "updateUserDetailPanel.action";
        var sparameters = "mode=Edit&objectId=" + gridViewId + "&orgTypeId=" + orgTypeId;
        doSectionLoad(sLocaltion, sAction, sparameters);
        
    }

    function createNewRecord(){
        
        getParameters();
        
        var gridViewId = -1;
        var sLocaltion = "#admin_param_panel";
        var sAction = "updateUserDetailPanel.action";
        var sparameters = "mode=New&objectId=" + gridViewId + "&orgTypeId=" + orgTypeId;
        doSectionLoad(sLocaltion, sAction, sparameters);
    }
    
    function showUserroleDropDown() {
        $("#userroleDropDownDiv").load("UserroleDropDownAction.action?orgTypeId=" + orgTypeId + uniqeToken());
    }

    function showOrganisationDropDownDiv(){
        $("#organisationDropDownDiv").load("OrganisationDropDownAction.action?orgTypeId=" + orgTypeId + uniqeToken(), function() {
            if(!isChoxAdmin){
                $("#orgTypeId").attr("disabled", true);
                $("#organisationId").attr("disabled", true);
            }
        });
    }
    
    function loadGridViewList(){
        
        
        getParameters();

        gridviewData.load(
        {
            params:
            {
                orgTypeId:orgTypeId,
                orgId:organisationId,
                userRoleId:userRoleId
            }
        });
    }
    
    function doOrganisationTypeChange(){
        pageRefresh();
    }
    
    function doDropDownOnChange(){

        
        loadGridViewList();
    }

    function triggerStatusUpdateRecord(gridView){
            
        var aletMsg = "Are you sure you want to inactivate this user?";

        if(!gridView.get("status")){
            aletMsg = "Are you sure you want to activate this user?";
        }

        var deleteAtt = confirm(aletMsg);

        if(deleteAtt){

            var gridViewId = gridView.get("id");

            $.ajax({
                url: "doTriggerUserAccountStatus.action?objectId="+gridViewId+uniqeToken(),
                success: onUpdateUserSubmitResult
            });
        }
        
    }
    
    function triggerIsExpiredUpdateRecord(gridView){
            
            var aletMsg = "Are you sure you want to cancel the password expired status?";
            if(gridView.get("isExpired") == "No"){
                aletMsg = "Are you sure you want to mark this user's password as expired?";
            }
            
            var deleteAtt = confirm(aletMsg);
            
            if(deleteAtt){
                var gridViewId = gridView.get("id");
                
                 $.ajax({
                   url: "doTriggerPasswordExpiredStatus.action?objectId="+gridViewId+uniqeToken(),
                   success: loadGridViewList
                 });
            }
    }

    function onUpdateUserSubmitResult(responseText, statusText){

        var response = eval('(' + responseText.trim() + ')');

        if(response)
        {
            if(response.isValid){

                if(response.resultType && response.resultType == 'Message')
                {
                    propmtMsg("", response.result)
                }

            }
        }
        else
        {
            propmtErrorMsg("Unknown Error Encountered, please try again.");
        }
        
        loadGridViewList();
        
    }
    

   
</script>
    
<div id="chox-admin-holder">
    
    <fieldset class="x-fieldset">
    <legend>User Management</legend>
        
    <input name="orgId" id="orgId" type="hidden" value="<s:property value="orgId" />">

    <div class="admin-gridview-header">
        <table>
            <tr>
                <td id="label">

                    <div class="label-block">
                    <p class="std-label">Organisation Type:</p>
                    <select id="orgTypeId" onchange="javascript:doOrganisationTypeChange()">
                        <option value="1">Sherwood Organisation</option>
                        <option value="2">Insurer Organisation</option>
                        <option value="3">Credit Hire Organisation</option>
                    </select>
                    </div>
                    <div id="organisationDropDownDiv" class="label-block"></div>
                    <div id="userroleDropDownDiv" class="label-block"></div>

                </td>
                <td id="buttons">
                    <button type="button" onclick="javascript:createNewRecord();" style="white-space: nowrap;">Add New User</button>
                </td>
            </tr>
        </table>
    </div>

    <div id="gridviewGridHolderId"></div>
            
    </fieldset>
    
</div>