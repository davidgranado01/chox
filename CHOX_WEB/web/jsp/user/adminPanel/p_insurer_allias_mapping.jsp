<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var allias_gridviewJsonReader;
    var allias_gridviewDataStore;
    var allias_gridviewGrid;
    var allias_gridviewData;
    var allias_recordPerPage = 20;
    var selectOrgId = <s:property value="selectOrgId" />;

    selectedPanel = 'InsurerAlliasMappingMgmt';
    
    Ext.onReady(function(){
      
       allias_gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',   
            root: 'results', 
            fields:
            [
                {name:'id'},
                {name:'name'},
                {name:'insurerName'},
                {name:'insurerId'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });

        allias_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: 'user/getInsurerAllias.action',method:'GET'}),
            reader:allias_gridviewJsonReader      
        });
    
        allias_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:allias_recordOnclick },
            store: allias_gridviewData,
            loadMask: true,
            columns: [
                {header: "Insurer", width: 100, dataIndex: 'insurerName', sortable: true, resizable: true},
                {header: "Allias Name", width: 180, dataIndex: 'name', sortable: true, resizable: true},
                {header: "Action", width: 80, dataIndex: 'Remove', sortable: true, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>Remove</a>"}},
                {header: "Created By", width: 100, dataIndex: 'createdBy', sortable: true, resizable: true},
                {header: "Created Date", width: 140, dataIndex: 'createdDate', sortable: true, resizable: true}
                

            ],
            renderTo:'allias_gridviewGrid',
                width:605,
                autoHeight:true,
                enableHdMenu:false
            });

            var pagingBar = new Ext.PagingToolbar({
                pageSize: allias_recordPerPage,
                store: allias_gridviewData,
                displayInfo: true,
                displayMsg: 'Displaying records {0} - {1} of {2}',
                emptyMsg: "No record to display"
            });    
            
            allias_loadGridViewList()

    }); 
    
    function allias_recordOnclick(grid, rowIndex, columnIndex, e){

        var gridView = allias_gridviewGrid.getStore().getAt(rowIndex);  // Get the Record

        if(columnIndex==2){
            allias_triggerStatusRemoveRecord(gridView);
        }
    }
    
    function allias_loadGridViewList(){
        
        allias_gridviewData.load(
        {
            params:
            {
                insurerId:selectOrgId
            }
        });
        
        $("#insurerAlliasName").val("");
    }
    
    function allias_doSelectChange(){
        allias_loadGridViewList();
    }
    
    function allias_triggerStatusAddRecord(){
        
        var insurerAlliasName = $("#insurerAlliasName").val();
        
        if(insurerAlliasName!=null && insurerAlliasName!="" && selectOrgId!=null && selectOrgId!=""){
            
            $("#CDInsurerAlliasMessageBox").html("");
            
            $.ajax({
               url: "addInsurerAllias.action?insurerId="+selectOrgId+"&insurerAlliasName="+insurerAlliasName,
               success: allias_onSubmitResponseReceived
            });
            
            return true;
            
        }else{
            $("#CDInsurerAlliasMessageBox").html("Please enter 'Insurer Allias Name'");
        }
        
        return false;
    }
    
    function allias_triggerStatusRemoveRecord(gridView){

        if(confirm("Are you sure you want to remove this allias?")){
            
            var gridViewId = gridView.get("id");

            $.ajax({
               url: "removeInsurerAllias.action?insurerAlliasId="+gridViewId,
               success: allias_onSubmitResponseReceived
            });
        }
    }
    
    function allias_onSubmitResponseReceived(responseText, statusText)  {
        responseText = responseText.trim();
        $("#CDInsurerAlliasMessageBox").html(responseText);
        allias_loadGridViewList();
    } 
    
</script>
<div>
    <div id="organisationGird">
        <div class="gridViewHeader">
            <table width="100%">
                <tr><td>
                        <div class="label-block">
                            <p class="std-label">Insurer Allias: </p> <input name="insurerAlliasName" id="insurerAlliasName" type="text">
                            <input type="submit" onclick="javascript: return allias_triggerStatusAddRecord();" value="Add"/>
                        </div>
                    </td></tr>
                <tr><td><div id="CDInsurerAlliasMessageBox" class="errorBox"></div></td></tr>
            </table>
        </div>
        <div id="allias_gridviewGrid" style="height:540px; overflow:auto;"></div>
    </div>


</div>