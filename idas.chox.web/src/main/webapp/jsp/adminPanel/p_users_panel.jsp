<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var gridviewJsonReader;
    var gridviewDataStore;
    var gridviewGrid;
    var gridviewData;

    var SelectedUserRoleId = -1;
    var SelectedOrganisationTypeId = -1;
    var SelectedOrganisationId = -1;

    Ext.onReady(function(){

        gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',   
            root: 'results', 
            fields:
                [
                {name:'id'},
                {name:'userName'},
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
            ({url: '<%= request.getContextPath()%>/prv/p/getGridViewUser.action',method:'POST'}),
            reader:gridviewJsonReader      
        });
       
        gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:recordOnclick },
            store: gridviewData,
            enableHdMenu:false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "User Name", width: 100, dataIndex: 'userName', sortable: true, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>" + value + "</a>"}},
                {header: "Name", width: 90, dataIndex: 'name', sortable: true, resizable: true},
                {header: "Email", width: 120, dataIndex: 'email', sortable: true, resizable: true},
                {header: "Organisation", width: 80, dataIndex: 'orgName', sortable: true, resizable: true},
                {header: "Active", width: 50, dataIndex: 'statusDesc', sortable: true, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>" + value + "</a>"}},
                {header: "Role", width: 150, dataIndex: 'role', sortable: true, resizable: true},
                {header: "Has Password Expired?", width: 140, dataIndex: 'isExpired', sortable: false, resizable: true,renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>" + value + "</a>"}}
            ],
            height:500,
            width: 730
        });

        gridviewGrid.render('gridviewGridHolderId');        
        onPageLoad();

    }); 

    function onPageLoad(){

        SelectedOrganisationTypeId = $("#CurrentUserOrganisationType").val();
        $("#SelectedOrganisationTypeId").val(SelectedOrganisationTypeId);
        
        if($("#CurrentUserOrganisationType").val()>1){
            $("#SelectedOrganisationTypeId").attr("disabled", true);
            SelectedOrganisationId = $("#CurrentUserOrganisationId").val();
            
        }

        showOrganisationDropDownDiv();
        
    }

    function recordOnclick(grid, rowIndex, columnIndex, e){

        var gridView = gridviewGrid.getStore().getAt(rowIndex);
        
        if(columnIndex==0){
            loadSelectedRecord(grid, rowIndex, columnIndex, e);
        }else if(columnIndex==4){
            triggerStatusUpdateRecord(gridView);
        }else if(columnIndex==6){
            triggerIsExpiredUpdateRecord(gridView);
        }
        
    }

    function getParameters(){
        SelectedOrganisationTypeId = $("#SelectedOrganisationTypeId").val();
        SelectedOrganisationId = $("#SelectedOrganisationId").val();
        SelectedUserRoleId = $("#SelectedUserrolesId").val();
    }

    function loadGridViewList(){

        getParameters();

        gridviewData.load({
            params:
                {
                organisationTypeId:SelectedOrganisationTypeId,
                organisationId:SelectedOrganisationId,
                userRoleId:SelectedUserRoleId
            }
        });

        $('div.chox-form-submit-result').html("");
    }

    function loadSelectedRecord(grid, rowIndex, columnIndex, e){

        getParameters();
        
        var gridView = gridviewGrid.getStore().getAt(rowIndex);
        var gridViewId = gridView.get("id");

        var target = "#admin_param_panel";
        var url = "<%= request.getContextPath()%>/prv/p/updateUserDetailPanel.action";
        var param = {"objectId":gridViewId,"organisationTypeId":SelectedOrganisationTypeId};
        ajax.loadHtml(url,param,function(data){
            $(target).html(data);
        });
    }

    function createNewRecord(){
        getParameters();
        var gridViewId = -1;
        var target = "#admin_param_panel";
        var url = "<%= request.getContextPath()%>/prv/p/updateUserDetailPanel.action";
        var param = {"objectId":gridViewId, "organisationTypeId":SelectedOrganisationTypeId};
        ajax.loadHtml(url,param,function(data){
            $(target).html(data);
        });
    }
   
    function doOrganisationTypeChange(){
        showOrganisationDropDownDiv();
    }
    
    function showOrganisationDropDownDiv(){
       
        SelectedOrganisationTypeId = $("#SelectedOrganisationTypeId").val();
        var target = "#organisationDropDownDiv";
        var url = "<%= request.getContextPath()%>/prv/p/OrganisationDropDownAction.action";
        var param = {"SelectedOrganisationTypeId":SelectedOrganisationTypeId};
       
        ajax.loadHtml(url,param,function(data){
            $(target).html(data);
            if($("#CurrentUserOrganisationId").val()>1){
                SelectedOrganisationId = $("#CurrentUserOrganisationId").val();
                $("#SelectedOrganisationId").val(SelectedOrganisationId);
                $("#SelectedOrganisationId").attr("disabled", true);
            }
            showUserroleDropDown();
        });
    }
    
    function showUserroleDropDown() {

        SelectedOrganisationTypeId = $("#SelectedOrganisationTypeId").val();

        var target = "#userroleDropDownDiv";
        var url = "<%= request.getContextPath()%>/prv/p/WebUserroleDropDownAction.action";
        var param = {"SelectedOrganisationTypeId":SelectedOrganisationTypeId};

        ajax.loadHtml(url,param,function(data){
            $(target).html(data);
            loadGridViewList();
        });

    }

    function triggerStatusUpdateRecord(gridView){

        $('div.chox-form-submit-result').html("");
        
        var aletMsg = "Are you sure you want to inactivate this user?";
        if(!gridView.get("status")){
            aletMsg = "Are you sure you want to activate this user?";
        }

        if(confirm(aletMsg)){
            var gridViewId = gridView.get("id");
            var url = "<%= request.getContextPath()%>/prv/p/doTriggerUserAccountStatus.action";
            var param = {"objectId":gridViewId};
            ajax.loadHtml(url, param, doUserGridViewSubmitSucceed);
        }
    }
    
    function triggerIsExpiredUpdateRecord(gridView){

        $('div.chox-form-submit-result').html("");
        
        var aletMsg = "Are you sure you want to cancel the password expired status?";
        if(gridView.get("isExpired") == "No"){
            aletMsg = "Are you sure you want to mark this user's password as expired?";
        }

        if(confirm(aletMsg)){
            var gridViewId = gridView.get("id");
            var url = "<%= request.getContextPath()%>/prv/p/doTriggerPasswordExpiredStatus.action";
            var param = {"objectId":gridViewId};
            ajax.loadHtml(url, param, doUserGridViewSubmitSucceed);
        }
    }

    function doUserGridViewSubmitSucceed(responseText, statusText){

        loadGridViewList();

        var response = eval('(' + responseText.trim() + ')');
        var outputDiv = $('div.chox-form-submit-result');
        
        if(response)
        {
            if(response.isValid){
                
                if(response.resultType && response.resultType == 'New')
                {
                    var newObjectId =  parseInt(response.result);
                    var hvObjectId = elementToBlock.find("input[name='objectId']");
                    hvObjectId.val(newObjectId);
                }
                else if(response.resultType && response.resultType == 'Message')
                {
                    alert(response.result);
                    //outputDiv.append("<p>" + response.result + "</p>");
                }
                else
                {
                    outputDiv.append("<p>Your changes have been saved.</p>");
                }

            }
            else
            {
                outputDiv.append('<p>There was an error:</p><ul class="submit-error">');

                $.each(response.errors, function() {
                    outputDiv.append("<li>");
                    outputDiv.append(this.toString());
                    outputDiv.append("</li>");
                });

                outputDiv.append("</ul>");
            }
        }
        else
        {
            outputDiv.append("Unknown Error Encountered, please try again.");
            outputDiv.addClass("submit-error");
        }

    }

</script>

<div id="chox-admin-holder">
    <div id="chox-admin-col-div">

        <div id="header-title"><label>User Management</label></div>

        <input name="CurrentUserOrganisationType" id="CurrentUserOrganisationType" type="hidden" value="<s:property value="CurrentUserOrganisationType" />">
        <input name="CurrentUserOrganisationId" id="CurrentUserOrganisationId" type="hidden" value="<s:property value="CurrentUserOrganisationId" />">

        <div class="admin-gridview-header">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td id="label">
                        <div class="label-block">
                            <p class="std-label">Organisation Type:</p>
                            <select id="SelectedOrganisationTypeId" onchange="javascript:doOrganisationTypeChange()">
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
        <div class="chox-form-submit-result">&nbsp;</div>
        <div id="gridviewGridHolderId"></div>

    </div>
</div>