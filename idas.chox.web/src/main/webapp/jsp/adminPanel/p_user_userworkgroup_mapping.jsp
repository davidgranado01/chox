<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var userworkgroup_gridviewJsonReader;
    var userworkgroup_gridviewDataStore;
    var userworkgroup_gridviewGrid;
    var userworkgroup_gridviewData;
    var recordPerPage = 20;
    var selectedOrgTypeId = 1 ;
   
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

        userworkgroup_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getUserWorkgroup.action?webUserId='+<s:property value="id" />+uniqeToken(), method:'POST'}),
            reader:userworkgroup_gridviewJsonReader
        });
        
        userworkgroup_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:userworkgroup_recordOnclick },
            store: userworkgroup_gridviewData,
            loadMask: true,
            columns: [
                {header: "Workgroup", width: 250, dataIndex: 'name', sortable: true, resizable: true},
                {header: "Created By", width: 90, dataIndex: 'createdBy', sortable: true, resizable: true},
                {header: "Created Date", width: 130, dataIndex: 'createdDate', sortable: true, resizable: true},
                {header: "", width: 100, dataIndex: 'Remove', sortable: false, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>Remove</a>"}}
            ],
            renderTo:'userworkgroup_gridviewGrid',
            width:700,
            height:450
        });
            
        userworkgroup_loadGridViewList()

    }); 
       
    function userworkgroup_recordOnclick(grid, rowIndex, columnIndex, e){

        var gridView = userworkgroup_gridviewGrid.getStore().getAt(rowIndex);
        
        if(columnIndex==3){
            workgroup_triggerStatusRemoveRecord(gridView);
        }
    }

    function userworkgroup_loadGridViewList(){
    
        userworkgroup_gridviewData.load(
        {
            params:
            {
                webUserId:<s:property value="id" />
            }
        });
    
    }

    function workgroup_triggerStatusRemoveRecord(gridView){

        $("#CDUserWorkgroupMessageBox").html("");

        var gridViewId = gridView.get("id");
        var gridViewWorkgroupId = gridView.get("workgroupId");
                
        $.getJSON("<%= request.getContextPath()%>/prv/p/isWorkgroupAllowToDelete.action?workgroupId="+gridViewWorkgroupId+"&webUserId="+<s:property value="id" />+uniqeToken(), function(data){
            if(!data.isAllowToDelete){
                
                if(confirm(data.warningMsg)){
                    doDeleteUserWorkgroup(gridViewId);
                }
                
            }else{

                if(confirm(data.warningMsg)){
                    doDeleteUserWorkgroup(gridViewId);
                }
            }
        });

    }
    
    function doDeleteUserWorkgroup(userWorkgroupId){
         $.ajax({
           url: "<%= request.getContextPath()%>/prv/p/removeUserWorkgroupMapping.action?userWorkgroupId="+userWorkgroupId+"&webUserId="+<s:property value="id" />+uniqeToken(),
           success: userworkgroup_doRefreshPage
         });
    }

    function userworkgroup_doSelectOnChange(){
        $("#CDUserWorkgroupMessageBox").html("");
    }

    function userworkgroup_AddRecord(){
    
        var webUserId = $("#webUserId").val()
        var workgroupId = $("#workgroupId").val();
        
        if(workgroupId!=null && workgroupId>0){
            
            $("#CDUserWorkgroupMessageBox").html("");
            
            $.ajax({
               url: "<%= request.getContextPath()%>/prv/p/addUserWorkgroupMapping.action?workgroupId="+workgroupId+"&webUserId="+webUserId+uniqeToken(),
               success: userworkgroup_doRefreshPage
            });
            
        }else{
            $("#CDUserWorkgroupMessageBox").html("Please select workgroup");
        }
        
    }
    
    function userworkgroup_doRefreshPage(responseText, statusText){

        var response = eval('(' + responseText.trim() + ')');
        var outputDiv =  $('#CDUserWorkgroupMessageBox');
        outputDiv.html('');
        outputDiv.removeClass();
        
        var refreshGirdView = true;

        if(response)
        {
            if(!response.isValid){

               outputDiv.addClass("submit-error");

               if(response.errors.lenght>1){

                    outputDiv.append("<p>Error have been encountered:</p><ul>");

                    jQuery.each(response.errors, function(i, val) {
                        outputDiv.append("<li>");
                        outputDiv.append(val);
                        outputDiv.append("</li>");
                    });

                    outputDiv.append("</ul>");

               }else{
                   outputDiv.append("<p>" + response.errors + "</p>");
               }

               refreshGirdView = false;

            }
        }
        else
        {
            outputDiv.append("Unknown Error Encountered, please try again.");
            outputDiv.addClass("submit-error");
            refreshGirdView = false;
        }

        if(refreshGirdView){
            var sLocaltion = "#admin_param_panel";
            var sAction = "<%= request.getContextPath()%>/prv/p/updateUserDetailPanel.action";
            var sparameters = "mode=Edit&objectId=" + <s:property value="id" /> + "&orgTypeId=" + <s:property value="orgTypeId" /> + "&tabIndex=" + userDetailTabIndex;
            doSectionLoad(sLocaltion, sAction, sparameters);
        }
    }
    
    
</script>

            <div class="status-info">
Assign one or more workgroups against this user by using the 'Add New Workgroup' button below. The assignment of workgroups will control claim visibility/permissions, only claims that have been assigned to a workgroup that has been assigned to the said user will be seen by the said user.
            </div>    
            
<div>
    
<input id="webUserId" name ="webUserId" type="hidden" value="<s:property value="id" />">
    <div>
        <div class="gridViewHeader">
            <table width="100%">
                <tr>
                    <td>User: <b><s:property value="displayName" /></b></td>
                </tr>
                <tr>
                    <td>
                        <s:select
                            id="workgroupId"                                 
                            name="workgroupId" 
                            list="workgroups" 
                            listKey="id" 
                            listValue="name" 
                            headerKey=""
                            headerValue="--- ALL ---"
                            onchange="javascript: doSelectOnChange();"
                            emptyOption="false">
                        </s:select><input type="button" onclick="javascript: userworkgroup_AddRecord();" value="Add New Workgroup"/>
                    </td>
                    <td align="right"></td>
                </tr>
                <tr><td><div id="CDUserWorkgroupMessageBox" class="submit-error"></div></td></tr>
            </table>
        </div>
        
        <div id="userworkgroup_gridviewGrid"></div>
    </div>

</div>
