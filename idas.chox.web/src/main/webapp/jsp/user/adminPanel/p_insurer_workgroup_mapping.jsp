<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var workgroup_gridviewJsonReader;
    var workgroup_gridviewDataStore;
    var workgroup_gridviewGrid;
    var workgroup_gridviewData;
    var workgroup_recordPerPage = 20;
    var orgId = -1;
    var selectOrgId = <s:property value="selectOrgId" />;
    
    workgroup_selectedPanel = 'InsurerWorkgroupMappingMgmt';
    
    Ext.onReady(function(){

       workgroup_gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',   
            root: 'results', 
            fields:
            [
                {name:'id'},
                {name:'name'},
                {name:'insurerId'},
                {name:'insurerName'},
                {name:'status'},
                {name:'statusDesc'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });

        workgroup_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: 'user/getInsurerWorkgroup.action',method:'POST'}),
            reader:workgroup_gridviewJsonReader      
        });

        workgroup_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:workgroup_recordOnclick },
            store: workgroup_gridviewData,
            loadMask: true,
            columns: [
                {header: "Insurer", width: 100, dataIndex: 'insurerName', sortable: true, resizable: true},
                {header: "Workgroup", width: 180, dataIndex: 'name', sortable: true, resizable: true},
                {header: "Active", width: 80, dataIndex: 'statusDesc', sortable: true, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>" + value + "</a>"}},
                {header: "Action", width: 80, dataIndex: 'Remove', sortable: true, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>Remove</a>"}},       
                {header: "Created By", width: 100, dataIndex: 'createdBy', sortable: true, resizable: true},
                {header: "Created Date", width: 140, dataIndex: 'createdDate', sortable: true, resizable: true}
            ],
            renderTo:'workgroup_gridviewGrid',
            height: 540,
            width: 720
            });
            
            workgroup_loadGridViewList();

    }); 
    
    function workgroup_recordOnclick(grid, rowIndex, columnIndex, e){

        var gridView = workgroup_gridviewGrid.getStore().getAt(rowIndex);  // Get the Record

        if(columnIndex==2){
            workgroup_triggerStatusTriggerRecord(gridView);
        }
        
        if(columnIndex==3){
            workgroup_triggerStatusRemoveRecord(gridView);
        }
    }
    
    function workgroup_loadGridViewList(){
       
        workgroup_gridviewData.load(
        {
            params:
            {
                insurerId:selectOrgId
            }
        });
        
        $("#workgroupName").val("");
    }
    
    function workgroup_triggerStatusAddRecord(){
       
        var workgroupName = $("#workgroupName").val();
        
        if(workgroupName!=null && workgroupName!="" && selectOrgId!=null && selectOrgId>0){
            
            $.ajax({
               url: "addInsurerWorkgroup.action?insurerId="+selectOrgId+"&workgroupName="+workgroupName+uniqeToken(),
               success: workgroup_onSubmitResponseReceived
            });
            
        }else{
            $("#CDInsurerWorkgroupMessageBox").html("Please enter 'Workgroup Name'");
        }
    }
   
    function workgroup_triggerStatusTriggerRecord(gridView){
        
        var gridViewId = gridView.get("id");

        $.ajax({
           url: "triggerInsurerWorkgroup.action?insurerId="+selectOrgId+"&workgroupId="+gridViewId+uniqeToken(),
           success: workgroup_onSubmitResponseReceived
        });
    }
    
    function workgroup_triggerStatusRemoveRecord(gridView){
        
        var gridViewId = gridView.get("id");

        $.ajax({
           url: "removeInsurerWorkgroup.action?insurerId="+selectOrgId+"&workgroupId="+gridViewId+uniqeToken(),
           success: workgroup_onSubmitResponseReceived
        });
    }
    
    function workgroup_onSubmitResponseReceived(responseText, statusText)  {     
        
        response = eval('(' + responseText.trim() + ')');

        var outputDiv =  $('#CDInsurerWorkgroupMessageBox');
        outputDiv.html('');
        outputDiv.removeClass();
        
        if(response)
        {
            if(response.isValid){
                
                outputDiv.addClass("submit-acknowledge");
                
                if(response.resultType && response.resultType == 'New')
                {                            
                    // var newObjectId =  parseInt(response.result);
                }
                else if(response.resultType && response.resultType == 'Message')
                {
                    outputDiv.append("<p>" + response.result + "</p>");
                }
                else
                {
                    outputDiv.append("<p>Your changes have been saved.</p>");
                }

            }
            else
            {
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
                
            }
        }
        else
        {
            outputDiv.append("Unknown Error Encountered, please try again.");
            outputDiv.addClass("submit-error");
        }
        
        workgroup_loadGridViewList();
        
    }
    
</script>

    <div id="organisationGird">

        <div class="gridViewHeader">
            
            <table width="100%">
                <tr><td>
                    <div class="label-block">
                    <p class="std-label">Workgroup: </p><input name="workgroupName" id="workgroupName" type="text">
                    <input type="button" onclick="javascript:return workgroup_triggerStatusAddRecord();" value="Add"/>
                    </div>
                    </td></tr>
                <tr><td><div id="CDInsurerWorkgroupMessageBox"></div></td></tr>
            </table>

        </div>
        
        <div id="workgroup_gridviewGrid" class="admin-tab-grid-view"></div>
        
    </div>

