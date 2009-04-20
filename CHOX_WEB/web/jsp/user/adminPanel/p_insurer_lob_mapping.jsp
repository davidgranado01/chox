<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var gridviewJsonReader;
    var gridviewDataStore;
    var gridviewGrid;
    var gridviewData;
    var recordPerPage = 20;
    var selectedInsurerId = -1 ;
    
    selectedPanel = 'InsurerLineOfBusinessMappingMgmt';
    
    Ext.onReady(function(){
        
       gridviewJsonReader = new Ext.data.JsonReader({
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

        gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: 'user/getInsurerLineOfBusiness.action',method:'GET'}),
            reader:gridviewJsonReader      
        });

        gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:recordOnclick },
            store: gridviewData,
            loadMask: true,
            columns: [
                {header: "id", width: 90, dataIndex: 'id', sortable: false, resizable: true},
                {header: "name", width: 90, dataIndex: 'name', sortable: false, resizable: true},
                {header: "insurerId", width: 90, dataIndex: 'insurerId', sortable: false, resizable: true},
                {header: "insurerName", width: 90, dataIndex: 'insurerName', sortable: false, resizable: true},
                {header: "Status", width: 50, dataIndex: 'statusDesc', sortable: false, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>" + value + "</a>"}},
                {header: "Created By", width: 100, dataIndex: 'createdBy', sortable: false, resizable: true},
                {header: "Created Date", width: 140, dataIndex: 'createdDate', sortable: false, resizable: true}
            ],
            renderTo:'gridviewGrid',
                width:615,
                autoHeight:true,
                enableHdMenu:false
            });

            var pagingBar = new Ext.PagingToolbar({
                pageSize: recordPerPage,
                store: gridviewData,
                displayInfo: true,
                displayMsg: 'Displaying records {0} - {1} of {2}',
                emptyMsg: "No record to display"
            });    
            
            loadGridViewList()

    }); 
    
    function recordOnclick(grid, rowIndex, columnIndex, e){

        var gridView = gridviewGrid.getStore().getAt(rowIndex);  // Get the Record

        if(columnIndex==4){
            triggerStatusRemoveRecord(gridView);
        }
    }
    
    function loadGridViewList(){
        gridviewData.load(
        {
            params:
            {
                insurerId:selectedInsurerId
            }
        });
        $("#insurerAlliasName").val("");
        
    }
    
    function doSelectChange(){
        selectedInsurerId = $("#insurerId").val();
        loadGridViewList();
    }
    
    function triggerStatusAddRecord(){
        
        var insurerId = $("#insurerId").val()
        var iineOfBusinessName = $("#iineOfBusinessName").val();
        
        if(iineOfBusinessName!=null && iineOfBusinessName!="" && insurerId!=null && insurerId!=""){
            
            $("#CDInsurerLineOfBusinessMessageBox").html("");
            
            $.ajax({
               url: "addInsurerLineOfBusiness.action?insurerId="+insurerId+"&iineOfBusinessName="+iineOfBusinessName,
               success: loadGridViewList
            });
            
        }else{
            $("#CDInsurerLineOfBusinessMessageBox").html("Please enter 'Line Of Business Name'");
        }
    }
    
    function triggerStatusRemoveRecord(gridView){

        if(confirm("Are you sure you want to remove this allias?")){
            
            var gridViewId = gridView.get("id");
                
            $.ajax({
               url: "removeInsurerLineOfBusiness.action?lineOfBusinessId="+gridViewId,
               success: loadGridViewList
            });
        }
    }
    
</script>

<div>
<fieldset class="x-fieldset">
    <legend>Line Of Business Mapping</legend>
    <div id="organisationGird">
        <div class="gridViewHeader">
            <table width="100%">
                <tr>
                    <td>
                        <s:select 
                            id="insurerId"                                 
                            name="insurerId" 
                            list="insurers" 
                            listKey="id" 
                            listValue="name" 
                            headerKey=""
                            headerValue="--- ALL ---"
                            emptyOption="false"
                            onchange="javascript:doSelectChange();">
                        </s:select>
                    </td>
                    <td align="right"></td>
                </tr>
                <tr><td>Insurer Allias: <input name="iineOfBusinessName" id="iineOfBusinessName" type="text">
                    <input type="submit" onclick="javascript: triggerStatusAddRecord();" value="Add"/></td></tr>
                <tr><td><div id="CDInsurerLineOfBusinessMessageBox" class="errorBox"></div></td></tr>                
            </table>

        </div>
        <div id="gridviewGrid"></div>
    </div>
</fieldset>
</div>