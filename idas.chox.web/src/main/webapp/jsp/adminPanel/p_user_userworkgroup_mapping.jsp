<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var userworkgroup_gridviewJsonReader;
    var userworkgroup_gridviewDataStore;
    var userworkgroup_gridviewGrid;
    var userworkgroup_gridviewData;
   
    Ext.onReady(function(){

        userworkgroup_gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',   
            root: 'results', 
            fields:
                [
                {name:'id'},
                {name:'workgroupId'},
                {name:'name'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });

        userworkgroup_gridviewData = new choxDataStore({
            url: '/prv/p/getUserWorkgroups.action',
            params : {'webUserId' : <s:property value="webUserId" />},
            reader:userworkgroup_gridviewJsonReader
        });

        userworkgroup_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:userworkgroup_recordOnclick },
            store: userworkgroup_gridviewData,
            renderTo:'userworkgroup_gridviewGrid',
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Workgroup", width: 200, dataIndex: 'name', sortable: true, resizable: true},
                {header: "Created By", width: 200, dataIndex: 'createdBy', sortable: true, resizable: true},
                {header: "Created Date", width: 250, dataIndex: 'createdDate', sortable: true, resizable: true},
                {header: "", width: 110, dataIndex: 'Remove', sortable: false, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>Remove</a>";}}
            ],
            height:450,
            width: 760
        });

        userworkgroup_loadGridViewList();

    });

    function userworkgroup_loadGridViewList(){
        userworkgroup_gridviewData.load();
    }

    function doAddNewWorkgroup(){

        var workgroupId = $("#workgroupId").val();

        if(workgroupId!==null && workgroupId>0){

            var url = "/prv/p/addUserWorkgroupMapping.action";
            var param = {"workgroupId":workgroupId,"webUserId":<s:property value="webUserId" />};
            ajax.loadHtml2(url, param, onUserWorkgroupMappingSubmitResult);

        }else{
            
            triggerCss("div#CDUserWorkgroupMessageBox", true);
            $("div#CDUserWorkgroupMessageBox").html("please select user workgroup");
        }

    }

    function userworkgroup_recordOnclick(grid, rowIndex, columnIndex, e){
        var gridView = userworkgroup_gridviewGrid.getStore().getAt(rowIndex);
        if(columnIndex===3){
            workgroup_triggerStatusRemoveRecord(gridView);
        }
    }

    function workgroup_triggerStatusRemoveRecord(gridView){

        triggerCss("div#CDUserWorkgroupMessageBox", true);
        
        var userWorkgroupId = gridView.get("id");
        var workgroupId = gridView.get("workgroupId");
        
        var url = "/prv/p/isUserWorkgroupAllowToDelete.action";
        var param = {"userWorkgroupId":userWorkgroupId,"webUserId":<s:property value="webUserId" />};

        ajax.loadHtml2(url, param, function(responseText, statusText){
            
            var response = eval('(' + responseText.trim() + ')');
            var outputDiv = $('div#CDUserWorkgroupMessageBox');

            if(response)
            {
                triggerCss("div#CDUserWorkgroupMessageBox", false);

                if(response.isValid){

                    outputDiv.addClass("chox-form-submit-result");

                    if(response.resultType && response.resultType === 'Message')
                    {
                        outputDiv.append("<p>" + response.result + "</p>");
                    }
                    else if(response.resultType && response.resultType === 'YesNo'){

                        Ext.MessageBox.confirm('Confirm', response.result,function(btn){
                        if(btn==='yes'){
                            doRemoveWebUserWorkgroup(workgroupId);
                        }
                        });
                    }
                    else
                    {
                        doRemoveWebUserWorkgroup(workgroupId);
                    }
                }
                else
                {
                    triggerCss("div#CDUserWorkgroupMessageBox", true);
                    $.each(response.errors, function() {
                        outputDiv.append(this.toString());
                    });
                }
            }
            else
            {
                outputDiv.append("Unknown Error Encountered, please try again.");
                outputDiv.addClass("submit-error");
            }
        });
    }
    
    function doRemoveWebUserWorkgroup(workgroupId){
        var url = "/prv/p/removeUserWorkgroupMapping.action";
        var param = {"workgroupId":workgroupId, "webUserId":<s:property value="webUserId" />};
        ajax.loadHtml2(url, param, onUserWorkgroupMappingSubmitResult);
    }
    
    function onUserWorkgroupMappingSubmitResult(responseText, statusText){

        var response = eval('(' + responseText.trim() + ')');
        var outputDiv = $('div#CDUserWorkgroupMessageBox');

        triggerCss("div#CDUserWorkgroupMessageBox", true);

        if(response)
        {
            triggerCss("div#CDUserWorkgroupMessageBox", false);

            if(response.isValid){

                outputDiv.addClass("chox-form-submit-result");

                if(response.resultType && response.resultType === 'Message')
                {
                    userworkgroup_doRefreshPage();
                    Ext.MessageBox.show({
                        title: '',
                        msg: response.result,
                        width:300,
                        buttons: Ext.MessageBox.OK
                    });
                }
                else
                {
                    userworkgroup_doRefreshPage();
                    //                    alert("Your Changes Have Been Saved");
                }
            }
            else
            {
                triggerCss("div#CDUserWorkgroupMessageBox", true);
                $.each(response.errors, function() {
                    Ext.MessageBox.show({
                        title: '',
                        msg: this.toString(),
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                });
                userworkgroup_doRefreshPage();
            }
        }
        else
        {
            outputDiv.append("Unknown Error Encountered, please try again.");
            outputDiv.addClass("submit-error");
        }
    }
    
    function userworkgroup_doRefreshPage(){
        var tabIndex = 3;
        
        var target = "#admin_param_panel";
        var url = "/prv/p/updateUserDetailPanel.action";
        var param = {"objectId":<s:property value="webUserId" /> ,"organisationTypeId":<s:property value="organisationTypeId" />,"tabIndex":tabIndex};
        ajax.loadHtml2(url,param,function(data){
            $(target).html(data);
            if(userDetailPanelTabs){
                userDetailPanelTabs.activate(tabIndex);
            }
        });
        //        userworkgroup_gridviewData.load({params:{webUserId:<s:property value="webUserId" />}});
    }

    
</script>
<div class="sub-admin-tab-css">
    <div class="status-info">
        Assign one or more workgroups against this user by using the 'Add New Workgroup' button below. The assignment of workgroups will control claim visibility/permissions, only claims that have been assigned to a workgroup that has been assigned to the said user will be seen by the said user.
    </div>
    <div>
        <div>
            <div class="grid-view-header">
                <table width="100%">
                    <tr>
                        <td align="center">
                            <s:select
                                id="workgroupId"
                                name="workgroupId"
                                list="availableWorkgroups"
                                listKey="id"
                                listValue="name"
                                headerKey="-1"
                                headerValue="--- Please Select ---"
                                emptyOption="false">
                            </s:select><input type="button" value="Add New Workgroup" onclick="javascript: doAddNewWorkgroup();"/>
                        </td>
                        <td align="right"></td>
                    </tr>
                </table>
            </div>
            <div id="CDUserWorkgroupMessageBox" class="chox-form-submit-result"></div>
            <div id="userworkgroup_gridviewGrid"></div>
        </div>
    </div>
</div>