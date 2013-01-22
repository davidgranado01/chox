<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var gridviewDataStore;
    var userManagementGrid;
    var userManagementgridStore;
    
    var selectedUserRoleId = -1;
    var selectedOrganisationTypeId = -1;
    var selectedOrganisationId = -1;

    Ext.onReady(function(){
        
    
            
        var gridviewJsonReader = new Ext.data.JsonReader({
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
                {name:'lastLoginDate'},
                {name:'role'},
                {name:'isExpired'}
            ]
        });

        userManagementgridStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getGridViewUser.action',method:'POST'}),
            reader:gridviewJsonReader,
            remoteSort: true,
            listeners:  {
                beforeload : function(){
                    userManagementgridStore.baseParams= {
                        organisationTypeId : $("#selectedOrganisationTypeId").val(),
                        organisationId : $("#selectedOrganisationId").val(),
                        userRoleId : $("#selectedUserRolesId").val()
                    };
                }
            }
        });
        userManagementgridStore.setDefaultSort('userName', 'asc');
        
            
        var userManagementPagingBar = new Ext.PagingToolbar({
            pageSize: (function(){return ($.browser.mozilla == true ? 22 : 23);}()),
            store: userManagementgridStore,
            displayInfo: true,
            displayMsg: 'Displaying users {0} - {1} of {2}'
            ,emptyMsg: "No user created."
            ,plugins: new Ext.ux.ProgressBarPager()
        });
            
        userManagementGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:recordOnclick },
            store: userManagementgridStore,
            enableHdMenu:false,
            layout:'fit',
            loadMask:true,
            viewConfig:{forceFit:true},
            columns: [
                {header: "User Name", width: 100, dataIndex: 'userName', sortable: true, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>" + value + "</a>"}},
                {header: "Name", width: 90, dataIndex: 'name', sortable: true, resizable: true},
                {header: "Email", width: 130, dataIndex: 'email', sortable: true, resizable: true},
                {header: "Organisation", width: 80, dataIndex: 'orgName', sortable: true, resizable: true},
                {header: "Active", width: 50, dataIndex: 'statusDesc', sortable: true, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>" + value + "</a>"}},
                {header: "Role", width: 150, dataIndex: 'role', sortable: false, resizable: true},
                {header: "Password Expired?", width: 100, dataIndex: 'isExpired', sortable: true, resizable: true,renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>" + value + "</a>"}},
                {header: "Last Login Date", width: 120, dataIndex: 'lastLoginDate', sortable: true, resizable: true}
            ],
            height:540,
            width: 775,
            bbar: userManagementPagingBar
        });

        userManagementGrid.render('gridviewGridHolderId');        
        onPageLoad();

    }); 

    function onPageLoad(){

        selectedOrganisationTypeId = $("#currentUserOrganisationType").val();
        $("#selectedOrganisationTypeId").val(selectedOrganisationTypeId);
        
        if($("#currentUserOrganisationType").val()>1){
            $("#selectedOrganisationTypeId").attr("disabled", true);
            selectedOrganisationId = $("#currentUserOrganisationId").val();
            
        }

        showOrganisationDropDownDiv();
        
    }

    function recordOnclick(grid, rowIndex, columnIndex, e){

        var gridView = userManagementGrid.getStore().getAt(rowIndex);
        
        if(columnIndex==0){
            loadSelectedRecord(grid, rowIndex, columnIndex, e);
        }else if(columnIndex==4){
            triggerStatusUpdateRecord(gridView);
        }else if(columnIndex==6){
            triggerIsExpiredUpdateRecord(gridView);
        }
        
    }

    function getParameters(){
        selectedOrganisationTypeId = $("#selectedOrganisationTypeId").val();
        selectedOrganisationId = $("#selectedOrganisationId").val();
        selectedUserRoleId = $("#selectedUserRolesId").val();
    }

    function loadGridViewList(){

        userManagementgridStore.removeAll();
        userManagementgridStore.load({
            params:
                {
                start:0, 
                 limit:(function(){return ($.browser.mozilla == true ? 22 : 23);}())
            }
        });

        $('div.chox-form-submit-result').html("");
    }

    function loadSelectedRecord(grid, rowIndex, columnIndex, e){

        getParameters();
        
        var gridView = userManagementGrid.getStore().getAt(rowIndex);
        var gridViewId = gridView.get("id");

        var target = "#admin_param_panel";
        var url = "<%= request.getContextPath()%>/prv/p/updateUserDetailPanel.action";
        var param = {"objectId":gridViewId,"organisationTypeId":selectedOrganisationTypeId};
        ajax.loadHtml2(url,param,function(data){
            $(target).html(data);
        });
    }

    function createNewRecord(){
        getParameters();
        var gridViewId = -1;
        var target = "#admin_param_panel";
        var url = "<%= request.getContextPath()%>/prv/p/updateUserDetailPanel.action";
        var param = {"objectId":gridViewId, "organisationTypeId":selectedOrganisationTypeId};
        ajax.loadHtml2(url,param,function(data){
            $(target).html(data);
        });
    }
   
    function doOrganisationTypeChange(){
        showOrganisationDropDownDiv();
    }
    
    function showOrganisationDropDownDiv(){
       
        selectedOrganisationTypeId = $("#selectedOrganisationTypeId").val();
        var target = "#organisationDropDownDiv";
        var url = "<%= request.getContextPath()%>/prv/p/OrganisationDropDownAction.action";
        var param = {"selectedOrganisationTypeId":selectedOrganisationTypeId};
       
        ajax.loadHtml2(url,param,function(data){
            $(target).html(data);
            if($("#currentUserOrganisationId").val()>1){
                selectedOrganisationId = $("#currentUserOrganisationId").val();
                $("#selectedOrganisationId").val(selectedOrganisationId);
                $("#selectedOrganisationId").attr("disabled", true);
            }
            showUserroleDropDown();
        });
    }
    
    function showUserroleDropDown() {

        selectedOrganisationTypeId = $("#selectedOrganisationTypeId").val();

        var target = "#userroleDropDownDiv";
        var url = "<%= request.getContextPath()%>/prv/p/WebUserRoleDropDownAction.action";
        var param = {"selectedOrganisationTypeId":selectedOrganisationTypeId};

        ajax.loadHtml2(url,param,function(data){
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

        Ext.MessageBox.confirm('Confirm', aletMsg,function(btn){
            if(btn=='yes'){
            var gridViewId = gridView.get("id");
            var url = "<%= request.getContextPath()%>/prv/p/doTriggerUserAccountStatus.action";
            var param = {"objectId":gridViewId};
            ajax.loadHtml2(url, param, doUserGridViewSubmitSucceed);
        }
        });
    }
    
    function triggerIsExpiredUpdateRecord(gridView){

        $('div.chox-form-submit-result').html("");
        
        var aletMsg = "Are you sure you want to cancel the password expired status?";
        if(gridView.get("isExpired") == "No"){
            aletMsg = "Are you sure you want to mark this user's password as expired?";
        }

        Ext.MessageBox.confirm('Confirm', aletMsg,function(btn){
            if(btn=='yes'){
            var gridViewId = gridView.get("id");
            var url = "<%= request.getContextPath()%>/prv/p/doTriggerPasswordExpiredStatus.action";
            var param = {"objectId":gridViewId};
            ajax.loadHtml2(url, param, doUserGridViewSubmitSucceed);
        }
        });
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
                    Ext.MessageBox.show({
                        title: '',
                        msg: response.result,
                        width:300,
                        buttons: Ext.MessageBox.OK
                    });
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
    <div id="chox-admin-col-div" style ="width:780">

        <div id="header-title"><label>User Management</label></div>

        <input name="currentUserOrganisationType" id="currentUserOrganisationType" type="hidden" value="<s:property value="currentUserOrganisationType" />">
        <input name="currentUserOrganisationId" id="currentUserOrganisationId" type="hidden" value="<s:property value="currentUserOrganisationId" />">

        <div class="admin-gridview-header">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td id="label">
                        <div class="label-block">
                            <p class="std-label">Organisation Type:</p>
                            <select id="selectedOrganisationTypeId" onchange="javascript:doOrganisationTypeChange()">
                                <option value="1">Sherwood Organisation</option>
                                <option value="2">Insurer Organisation</option>
                                <option value="3">Credit Hire Organisation</option>
                            </select>
                        </div>
                    </td>
                    <td id="buttons">
                        <button type="button" onclick="javascript:createNewRecord();">Add New User</button>
                    </td>
                </tr>
                <tr>
                    <td id="label">
                        <div id="organisationDropDownDiv" class="label-block" style="width:600px;"></div>
                    </td>
                </tr>
                <tr>
                    <td id="label">
                        <div id="userroleDropDownDiv" class="label-block"></div>
                    </td>
                </tr>


            </table>
        </div>
        <div class="chox-form-submit-result">&nbsp;</div>
        <div id="gridviewGridHolderId"></div>

    </div>
</div>