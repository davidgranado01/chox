<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var userrole_gridviewJsonReader;
    var userrole_gridviewDataStore;
    var userrole_gridviewGrid;
    var userrole_gridviewData;

    Ext.onReady(function(){
        
        userrole_gridviewJsonReader = new Ext.data.JsonReader({
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

        userrole_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getUseroles.action?webUserId='+<s:property value="id" />, method:'POST'}),
            reader:userrole_gridviewJsonReader
        });
        
        userrole_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:userrole_recordOnclick },
            store: userrole_gridviewData,
            enableHdMenu:false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Role", width: 250, dataIndex: 'webUserroleName', sortable: false, resizable: true},
                {header: "Created By", width: 90, dataIndex: 'createdBy', sortable: false, resizable: true},
                {header: "Created Date", width: 130, dataIndex: 'createdDate', sortable: false, resizable: true},
                {header: "", width: 100, dataIndex: 'Remove', sortable: false, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>Remove</a>"}}
            ],
            renderTo:'gridviewGrid',
            height:420,
            width: 715
        });
  
        userrole_loadGridViewList()

    }); 

    function userrole_loadGridViewList(){
        userrole_gridviewData.load({params:{webUserId:<s:property value="id" />}});
    }

    function doAddNewWebUserRole(){

        var webUserId = <s:property value="id" />;
        var webUserRoleId = $("#userrolesId").val();

        if(webUserRoleId != null && webUserRoleId > 0){

            var url = "<%= request.getContextPath()%>/prv/p/addNewWebUserRoleMapping.action";
            var param = {"webUserRoleId":webUserRoleId,"webUserId":webUserId};
            ajax.loadHtml(url, param, onUserroleMappingSubmitResult);

        }else{
            
            triggerCss("div#CDUserroleMessageBox", true);
            $("div#CDUserroleMessageBox").html("please select user role");
        }
    }

    function userrole_recordOnclick(grid, rowIndex, columnIndex, e){
        var gridView = userrole_gridviewGrid.getStore().getAt(rowIndex);
        if(columnIndex==3){
            userrole_triggerStatusRemoveRecord(gridView);
        }
    }

    function userrole_triggerStatusRemoveRecord(gridView){
        
        var webUserId = <s:property value="id" />;
        var webUserUserRoleId = gridView.get("id");
        var webUserrolecode = gridView.get("webUserroleRole");
        var defaultdeleteMsg = "Are you sure you want to remove this role?";
        
        // ONLY PERFORM CHECK IF AND ONLY IF USER ARE REMOVING COM OR CH
        if((webUserrolecode=='ROLE_INS_CH' || webUserrolecode=='ROLE_INS_COM' || webUserrolecode=='ROLE_INS_FNOL')){

            var url = "<%= request.getContextPath()%>/prv/p/isWebUserRoleAllowToDelete.action";
            var param = {"webUserRoleCode":webUserrolecode,"webUserId":webUserId};
            
            ajax.loadHtml(url, param, function(responseText, statusText){

                var response = eval('(' + responseText.trim() + ')');
                var outputDiv = $('div#CDUserroleMessageBox');

                triggerCss("div#CDUserroleMessageBox", true);

                if(response)
                {
                    triggerCss("div#CDUserroleMessageBox", false);

                    if(response.isValid){

                        outputDiv.addClass("chox-form-submit-result");

                        if(response.resultType && response.resultType == 'Message')
                        {
                            outputDiv.append("<p>" + response.result + "</p>");
                        }
                        else if(response.resultType && response.resultType == 'YesNo'){

                            if(confirm(response.result)){
                                doRemoveWebUserRoleMapping(webUserUserRoleId);
                            }

                        }
                        else
                        {
                            doRemoveWebUserRoleMapping(webUserUserRoleId);
                        }
                    }
                    else
                    {
                        triggerCss("div#CDUserroleMessageBox", true);
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
            
        }else{
            
            if(confirm(defaultdeleteMsg)){        
                doRemoveWebUserRoleMapping(webUserUserRoleId);
            }
            
        }
    }

    function doRemoveWebUserRoleMapping(webUserUserRoleId){
        var url = "<%= request.getContextPath()%>/prv/p/removeWebUserRoleMapping.action";
        var param = {"webUserUserRoleId":webUserUserRoleId,"webUserId":<s:property value="id" />};
        ajax.loadHtml(url, param, onUserroleMappingSubmitResult);
    }
    
    function onUserroleMappingSubmitResult(responseText, statusText){

        var response = eval('(' + responseText.trim() + ')');
        var outputDiv = $('div#CDUserroleMessageBox');

        triggerCss("div#CDUserroleMessageBox", true);
        
        if(response)
        {
            triggerCss("div#CDUserroleMessageBox", false);
            
            if(response.isValid){

                outputDiv.addClass("chox-form-submit-result");
                
                if(response.resultType && response.resultType == 'Message')
                {
                    userrole_doRefreshPage();
                    alert(response.result);
                }
                else
                {
                    userrole_doRefreshPage();
                    alert("Your Changes Have Been Saved");
                }
            }
            else
            {
                triggerCss("div#CDUserroleMessageBox", true);
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

    }

    function userrole_doRefreshPage(){

        var tabIndex = 0;
        if(<s:property value="isChoxAdmin"/>){
            tabIndex = 2;
        }
        
        var target = "#admin_param_panel";
        var url = "<%= request.getContextPath()%>/prv/p/updateUserDetailPanel.action";
        var param = {"objectId":<s:property value="id" /> ,"organisationTypeId":<s:property value="organisationTypeId" />,"tabIndex":tabIndex};
        ajax.loadHtml(url,param,function(data){
            $(target).html(data);
        });
    }
    
</script>

<div class="status-info">
    Assign one or more user roles against this user by using the 'Add New Role' button below. The assignment of roles will dictate which work queues the user will see as well the user's access/permission rights.
</div>    
<div>
    <div class="grid-view-header">
        <table width="100%">
            <tr>
                <td>
                    <s:select
                        id="userrolesId"
                        name="userrolesId"
                        list="availableUserroles"
                        listKey="id"
                        listValue="name"
                        headerKey=""
                        headerValue="- Please Select -"
                        emptyOption="false">
                    </s:select><input type="button" onclick="javascript: doAddNewWebUserRole();" value="Add New Role"/>
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <div id="CDUserroleMessageBox" class="chox-form-submit-result"></div>
    <div id="gridviewGrid"></div>
</div>