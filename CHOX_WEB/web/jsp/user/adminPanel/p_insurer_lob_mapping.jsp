<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var gridviewJsonReader;
    var gridviewDataStore;
    var gridviewGrid;
    var gridviewData;
    var recordPerPage = 20;
    
    var orgId = -1;
    var selectOrgId = <s:property value="selectOrgId" />;
    
    selectedPanel = 'InsurerLineOfBusinessMappingMgmt';
    
    Ext.onReady(function(){
        
       if(selectOrgId>0){
           $("#insurerId").val(selectOrgId);
       }

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
                {header: "Line Of Business", width: 150, dataIndex: 'name', sortable: true, resizable: true},
                {header: "insurer", width: 150, dataIndex: 'insurerName', sortable: true, resizable: true},
                {header: "Status", width: 60, dataIndex: 'statusDesc', sortable: true, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>" + value + "</a>"}},
                {header: "Created By", width: 100, dataIndex: 'createdBy', sortable: true, resizable: true},
                {header: "Created Date", width: 140, dataIndex: 'createdDate', sortable: true, resizable: true}
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

        if(columnIndex==2){
            triggerStatusRemoveRecord(gridView);
        }
    }
    
    function loadGridViewList(){
        
        doParameters();
        
        gridviewData.load(
        {
            params:
            {
                insurerId:orgId
            }
        });
        
        $("#lineOfBusinessName").val("");
        
    }
    
    function doParameters(){
        orgId = $("#insurerId").val();
    }
    
    function doSelectChange(){
        loadGridViewList();
    }
    
    function triggerStatusAddRecord(){
        
        doParameters();
        
        var lineOfBusinessName = $("#lineOfBusinessName").val();
        
        if(lineOfBusinessName!=null && lineOfBusinessName!="" && orgId!=null && orgId>0){
            
            $("#CDInsurerLineOfBusinessMessageBox").html("");
            
            $.ajax({
               url: "addInsurerLineOfBusiness.action?insurerId="+orgId+"&lineOfBusinessName="+lineOfBusinessName,
               success: onSubmitResponseReceived
            });
            
        }else{
            $("#CDInsurerLineOfBusinessMessageBox").html("Please enter 'Line Of Business Name'");
        }
    }
    
    function triggerStatusRemoveRecord(gridView){

            var gridViewId = gridView.get("id");
                
            $.ajax({
               url: "removeInsurerLineOfBusiness.action?lineOfBusinessId="+gridViewId,
               success: onSubmitResponseReceived
            });
        
    }
    
    function onSubmitResponseReceived(responseText, statusText)  {      
        responseText = responseText.trim();
        $("#CDInsurerLineOfBusinessMessageBox").html(responseText);
        loadGridViewList();
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
                        <s:if test="isSelectable">
                        <s:select 
                            id="insurerId"                                 
                            name="insurerId" 
                            list="insurers" 
                            listKey="id" 
                            listValue="name" 
                            headerKey="-1"
                            headerValue="--- ALL ---"
                            emptyOption="false"
                            onchange="javascript:doSelectChange();">
                        </s:select>
                        </s:if>
                        <s:else>
                            <input name="insurerId" id="insurerId" type="hidden" value="<s:property value="insurerId" />">
                        </s:else>
                    </td>
                    <td align="right"></td>
                </tr>
                <tr><td>Line of Business: <input name="lineOfBusinessName" id="lineOfBusinessName" type="text">
                    <input type="button" onclick="javascript: triggerStatusAddRecord();" value="Add"/></td></tr>
                <tr><td><div id="CDInsurerLineOfBusinessMessageBox" class="errorBox"></div></td></tr>                
            </table>

        </div>
        <div id="gridviewGrid"></div>
    </div>
</fieldset>
</div>