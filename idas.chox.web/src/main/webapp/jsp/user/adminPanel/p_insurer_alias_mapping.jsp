<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var alias_gridviewJsonReader;
    var alias_gridviewDataStore;
    var alias_gridviewGrid;
    var alias_gridviewData;
    var alias_recordPerPage = 20;
    var selectOrgId = <s:property value="selectOrgId" />;

    selectedPanel = 'InsurerAliasMappingMgmt';
    
    Ext.onReady(function(){
      
       alias_gridviewJsonReader = new Ext.data.JsonReader({
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

        alias_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: 'user/getInsurerAlias.action',method:'GET'}),
            reader:alias_gridviewJsonReader      
        });
    
        alias_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:alias_recordOnclick },
            store: alias_gridviewData,
            loadMask: true,
            columns: [
                {header: "Insurer", width: 100, dataIndex: 'insurerName', sortable: true, resizable: true},
                {header: "Alias Name", width: 180, dataIndex: 'name', sortable: true, resizable: true},
                {header: "Action", width: 80, dataIndex: 'Remove', sortable: true, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>Remove</a>"}},
                {header: "Created By", width: 100, dataIndex: 'createdBy', sortable: true, resizable: true},
                {header: "Created Date", width: 140, dataIndex: 'createdDate', sortable: true, resizable: true}
                

            ],
            renderTo:'alias_gridviewGrid',
                width:605,
                autoHeight:true,
                enableHdMenu:false
            });

            var pagingBar = new Ext.PagingToolbar({
                pageSize: alias_recordPerPage,
                store: alias_gridviewData,
                displayInfo: true,
                displayMsg: 'Displaying records {0} - {1} of {2}',
                emptyMsg: "No record to display"
            });    
            
            alias_loadGridViewList()

    }); 
    
    function alias_recordOnclick(grid, rowIndex, columnIndex, e){

        var gridView = alias_gridviewGrid.getStore().getAt(rowIndex);  // Get the Record

        if(columnIndex==2){
            alias_triggerStatusRemoveRecord(gridView);
        }
    }
    
    function alias_loadGridViewList(){
        
        alias_gridviewData.load(
        {
            params:
            {
                insurerId:selectOrgId
            }
        });
        
        $("#insurerAliasName").val("");
    }
    
    function alias_doSelectChange(){
        alias_loadGridViewList();
    }
    
    function alias_triggerStatusAddRecord(){
        
        var insurerAliasName = $("#insurerAliasName").val();
        
        if(insurerAliasName!=null && insurerAliasName!="" && selectOrgId!=null && selectOrgId!=""){
            
            $("#CDInsurerAliasMessageBox").html("");
            
            $.ajax({
               url: "addInsurerAlias.action?insurerId="+selectOrgId+"&insurerAliasName="+insurerAliasName+uniqeToken(),
               success: alias_onSubmitResponseReceived
            });
            
            return true;
            
        }else{
            $("#CDInsurerAliasMessageBox").html("Please enter 'Insurer Alias Name'");
        }
        
        return false;
    }
    
    function alias_triggerStatusRemoveRecord(gridView){

        if(confirm("Are you sure you want to remove this alias?")){
            
            var gridViewId = gridView.get("id");

            $.ajax({
               url: "removeInsurerAlias.action?insurerAliasId="+gridViewId+uniqeToken(),
               success: alias_onSubmitResponseReceived
            });
        }
    }
    
    function alias_onSubmitResponseReceived(responseText, statusText)  {
        responseText = responseText.trim();
        $("#CDInsurerAliasMessageBox").html(responseText);
        alias_loadGridViewList();
    } 
    
</script>
<div>
    <div id="organisationGird">
        <div class="gridViewHeader">
            <table width="100%">
                <tr><td>
                        <div class="label-block">
                            <p class="std-label">Insurer Alias: </p> <input name="insurerAliasName" id="insurerAliasName" type="text">
                            <input type="submit" onclick="javascript: return alias_triggerStatusAddRecord();" value="Add"/>
                        </div>
                    </td></tr>
                <tr><td><div id="CDInsurerAliasMessageBox" class="errorBox"></div></td></tr>
            </table>
        </div>
        <div id="alias_gridviewGrid" class="admin-tab-grid-view"></div>
    </div>
</div>