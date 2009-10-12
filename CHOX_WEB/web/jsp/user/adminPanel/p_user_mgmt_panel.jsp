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
    var userRoleId = -1;
    
    var selectOrgTypeId = <s:property value="selectOrgTypeId" />;
    var selectOrgId = <s:property value="selectOrgId" />;
    var isCHOXAdmin = <s:property value="isCHOXAdmin" />;

    Ext.onReady(function(){
    
       if(selectOrgTypeId>0){
            $("#orgTypeId").val(selectOrgTypeId);
       }
       
       if(selectOrgId>0){
           $("#orgId").val(selectOrgId);
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
            renderTo:'gridviewGrid',
                width:615,
                autoHeight:true,
                enableHdMenu:false
            });
            
            pageRefresh();

    }); 
    
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

        orgId ="";
        userRoleId = -1;
        
        orgTypeId = $("#orgTypeId").val();
        orgId = $("#orgId").val();
        userRoleId = $("#userrolesId").val();

        if(isOrgShow()){
            orgId = $("#organisationId").val();
        }
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
        
        gridviewData.load(
        {
            params:
            {
                start:0,
                limit:recordPerPage,
                orgTypeId:orgTypeId,
                orgId:orgId,
                userRoleId:userRoleId
            }
        });
    }
    
    function doSelectChange(){
        
        $("#organisationId").val("");
        $("#userrolesId").val("");
        
        pageRefresh();
    }
    
    function pageRefresh(){
        
        getParameters();
        showUserroleDropDown();
        showOrganisationDropDownDiv();
        loadGridViewList();
        
    }
    
    function doUseroleSelected(){
        getParameters();
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
                url: "doTriggerUserAccountStatus.action?objectId="+gridViewId,
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
                   url: "doTriggerPasswordExpiredStatus.action?objectId="+gridViewId,
                   success: loadGridViewList
                 });
            }
    }

    function onUpdateUserSubmitResult(responseText, statusText){
        loadGridViewList();
    }
    
    function showUserroleDropDown() {
        $("#userroleDropDownDiv").load("UserroleDropDownAction.action?orgTypeId=" + orgTypeId);
    }

    function showOrganisationDropDownDiv() {
        
        if(isOrgShow()){
            $("#organisationDropDownDiv").load("OrganisationDropDownAction.action?orgTypeId=" + orgTypeId);
        }else{
            $("#organisationDropDownDiv").html("");
        }
        
    }

    function isOrgShow(){

        var isAllow = false;

        if(isCHOXAdmin && (orgTypeId==2 || orgTypeId==3)){
            isAllow = true;
        }

        return isAllow;
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
            <div class="label-block">
            <p class="std-label">Organisation Type: </p>
            <select id="orgTypeId" onchange="javascript:doSelectChange()">
                <option value="1">Sherwood Organisation</option>
                <option value="2">Insurer Organisation</option>
                <option value="3">Credit Hire Organisation</option>
            </select>
            </div>
        </s:if>
        <s:else>
            <input name="orgTypeId" id="orgTypeId" type="hidden" value="<s:property value="orgTypeId" />">
        </s:else>
    
    <div id="organisationDropDownDiv" class="label-block"></div>
    <div id="userroleDropDownDiv" class="label-block"></div>
    
    <input name="orgId" id="orgId" type="hidden" value="<s:property value="orgId" />">

                    </td>
                    <td align="right" valign="bottom" width="50%"><button type="button" onclick="javascript:createNewRecord();" style="white-space: nowrap;">Add New User</button></td>
                </tr>
            </table>

        </div>
        <div id="gridviewGrid" style="height:570px; overflow:auto;"></div>
    </div>
</fieldset>