<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var lob_gridviewJsonReader;
    var lob_gridviewDataStore;
    var lob_gridviewGrid;
    var lob_gridviewData;
    var lob_recordPerPage = 20;
    var orgId = -1;
    var selectOrgId = <s:property value="selectOrgId" />;
    
    lob_selectedPanel = 'InsurerLineOfBusinessMappingMgmt';
    
    Ext.onReady(function(){

       lob_gridviewJsonReader = new Ext.data.JsonReader({
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

        lob_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: 'user/getInsurerLineOfBusiness.action',method:'GET'}),
            reader:lob_gridviewJsonReader      
        });

        lob_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:lob_recordOnclick },
            store: lob_gridviewData,
            loadMask: true,
            columns: [
                {header: "Insurer", width: 100, dataIndex: 'insurerName', sortable: true, resizable: true},
                {header: "Line Of Business", width: 180, dataIndex: 'name', sortable: true, resizable: true},
                {header: "Active", width: 80, dataIndex: 'statusDesc', sortable: true, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>" + value + "</a>"}},
                {header: "Created By", width: 100, dataIndex: 'createdBy', sortable: true, resizable: true},
                {header: "Created Date", width: 140, dataIndex: 'createdDate', sortable: true, resizable: true}
            ],
            renderTo:'lob_gridviewGrid',
                width:605,
                autoHeight:true,
                enableHdMenu:false
            });

            var pagingBar = new Ext.PagingToolbar({
                pageSize: lob_recordPerPage,
                store: lob_gridviewData,
                displayInfo: true,
                displayMsg: 'Displaying records {0} - {1} of {2}',
                emptyMsg: "No record to display"
            });    
            
            lob_loadGridViewList()

    }); 
    
    function lob_recordOnclick(grid, rowIndex, columnIndex, e){

        var gridView = lob_gridviewGrid.getStore().getAt(rowIndex);  // Get the Record

        if(columnIndex==2){
            lob_triggerStatusRemoveRecord(gridView);
        }
    }
    
    function lob_loadGridViewList(){
        
        lob_gridviewData.load(
        {
            params:
            {
                insurerId:selectOrgId
            }
        });
        
        $("#lineOfBusinessName").val("");
    }
    
    function lob_triggerStatusAddRecord(){
        
        var lineOfBusinessName = $("#lineOfBusinessName").val();
        
        if(lineOfBusinessName!=null && lineOfBusinessName!="" && selectOrgId!=null && selectOrgId>0){

            $("#CDInsurerLineOfBusinessMessageBox").html("");
            
            $.ajax({
               url: "addInsurerLineOfBusiness.action?insurerId="+selectOrgId+"&lineOfBusinessName="+lineOfBusinessName+uniqeToken(),
               success: lob_onSubmitResponseReceived
            });
            
        }else{
            $("#CDInsurerLineOfBusinessMessageBox").html("Please enter 'Line Of Business Name'");
        }
    }
    
    function lob_triggerStatusRemoveRecord(gridView){

            var gridViewId = gridView.get("id");
                
            $.ajax({
               url: "removeInsurerLineOfBusiness.action?lineOfBusinessId="+gridViewId+uniqeToken(),
               success: lob_onSubmitResponseReceived
            });
    }
    
    function lob_onSubmitResponseReceived(responseText, statusText)  {         
        responseText = responseText.trim();
        $("#CDInsurerLineOfBusinessMessageBox").html(responseText);
        lob_loadGridViewList();
    }
    
</script>

<div>

    <div id="organisationGird">
        <div class="gridViewHeader">
            <table width="100%">
                <tr>
                </tr>
                <tr><td>
                        <div class="label-block">
                    <p class="std-label">Line of Business: </p> <input name="lineOfBusinessName" id="lineOfBusinessName" type="text">
                    <input type="button" onclick="javascript:return lob_triggerStatusAddRecord();" value="Add"/>
                    </div>
                    </td></tr>
                <tr><td><div id="CDInsurerLineOfBusinessMessageBox" class="errorBox"></div></td></tr>                
            </table>

        </div>
        <div id="lob_gridviewGrid" style="height:540px; overflow:auto;"></div>
    </div>

</div>