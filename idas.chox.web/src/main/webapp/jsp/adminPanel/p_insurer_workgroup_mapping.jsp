<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var workgroup_gridviewJsonReader;
    var workgroup_gridviewDataStore;
    var workgroup_gridviewGrid;
    var workgroup_gridviewData;
    
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
            ({url: '<%= request.getContextPath()%>/prv/p/getInsurerWorkgroup.action',method:'POST'}),
            reader:workgroup_gridviewJsonReader      
        });

        workgroup_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:workgroup_recordOnclick },
            store: workgroup_gridviewData,
            enableHdMenu:false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Insurer", width: 100, dataIndex: 'insurerName', sortable: true, resizable: true},
                {header: "Workgroup", width: 180, dataIndex: 'name', sortable: true, resizable: true},
                {header: "Active", width: 80, dataIndex: 'statusDesc', sortable: true, resizable: true, renderer:function(value,p,r){ return "<a href='#' class='high-light-item'>" + value + "</a>"}},
                {header: "Action", width: 80, dataIndex: 'Remove', sortable: true, resizable: true, renderer:function(value,p,r){ return "<a href='#' class='high-light-item'>Remove</a>"}},
                {header: "Created By", width: 100, dataIndex: 'createdBy', sortable: true, resizable: true},
                {header: "Created Date", width: 140, dataIndex: 'createdDate', sortable: true, resizable: true}
            ],
            renderTo:'workgroup_gridviewGrid',
            height:420,
            width: 715
        });
            
        workgroup_loadGridViewList();

    }); 

    function workgroup_loadGridViewList(){
        workgroup_gridviewData.load({ params : { insurerId:<s:property value="insurerId" /> } });
    }

    function workgroup_recordOnclick(grid, rowIndex, columnIndex, e){
        var gridView = workgroup_gridviewGrid.getStore().getAt(rowIndex);
        if(columnIndex==2){
            workgroup_triggerStatusUpdateRecord(gridView);
        }else if(columnIndex==3){
            workgroup_triggerStatusRemoveRecord(gridView);
        }
    }

    function workgroup_triggerStatusAddRecord(){
       
        var workgroupName = $("#workgroupName").val();
        
        if(workgroupName!=null && workgroupName!=""){
            
            var url = "<%= request.getContextPath()%>/prv/p/addNewInsurerWorkgroup.action";
            var param = {"insurerId":<s:property value="insurerId" />,"workgroupName":workgroupName};
            ajax.loadHtml(url, param, workgroup_onSubmitResponseReceived);

        }else{
            
            triggerCss("div#CDInsurerWorkgroupMessageBox", true);
            $("div#CDInsurerWorkgroupMessageBox").html("Please enter 'Workgroup Name'");
            
        }
        
    }
   
    function workgroup_triggerStatusUpdateRecord(gridView){

        var workgroupId = gridView.get("id");
        var url = "<%= request.getContextPath()%>/prv/p/triggerInsurerWorkgroupStatus.action";
        var param = {"insurerId":<s:property value="insurerId" />,"workgroupId":workgroupId};
        ajax.loadHtml(url, param, workgroup_onSubmitResponseReceived);

    }
    
    function workgroup_triggerStatusRemoveRecord(gridView){

        if(confirm("Are you sure you want to remove this Workgroup?")){

            var workgroupId = gridView.get("id");

            var url = "<%= request.getContextPath()%>/prv/p/removeInsurerWorkgroup.action";
            var param = {"insurerId":<s:property value="insurerId" />,"workgroupId":workgroupId};
            ajax.loadHtml(url, param, workgroup_onSubmitResponseReceived);
        }

    }
    
    function workgroup_onSubmitResponseReceived(responseText, statusText)  {
        
        var response = eval('(' + responseText.trim() + ')');
        var outputDiv = $('div#CDInsurerWorkgroupMessageBox');

        triggerCss("div#CDInsurerWorkgroupMessageBox", true);

        if(response)
        {
            triggerCss("div#CDInsurerWorkgroupMessageBox", false);

            if(response.isValid){

                outputDiv.addClass("chox-form-submit-result");

                if(response.resultType && response.resultType == 'Message')
                {
                    alert(response.result);
                    insurerWorkgroup_doRefreshPage();
                }
                else
                {
                    alert("Your Changes Have Been Saved");
                    insurerWorkgroup_doRefreshPage();
                }

            }
            else
            {
                triggerCss("div#CDInsurerWorkgroupMessageBox", true);
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

    function insurerWorkgroup_doRefreshPage(){

        var tabIndex = 0;
        var target = "#admin_param_panel";
        var url = "<%= request.getContextPath()%>/prv/p/loadAdminPanel.action";
        var param = {"adminPanelName":"InsurerPanelMgmt","tabIndex":tabIndex};
        
        if(<s:property value="isChoxAdmin"/>){
            tabIndex = 2;
            url = "<%= request.getContextPath()%>/prv/p/updateInsurerDetailPanel.action";
            var param = {"objectId":<s:property value="insurerId" />,"tabIndex":tabIndex};
        }
        
        ajax.loadHtml(url,param,function(data){
            $(target).html(data);
        });
        
    }
    
</script>
<div class="sub-admin-tab-css">

    <div class="status-info">
        {Workgroup}
    </div>

    <div class="grid-view-header">

        <table width="100%">
            <tr><td>
                    <div class="label-block">
                        <p class="std-label">Workgroup: </p><input name="workgroupName" id="workgroupName" type="text">
                        <input type="button" onclick="javascript:return workgroup_triggerStatusAddRecord();" value="Add"/>
                    </div>
                </td></tr>
        </table>

    </div>
    <div id="CDInsurerWorkgroupMessageBox" class="chox-form-submit-result"></div>
    <div id="workgroup_gridviewGrid"></div>
</div>


