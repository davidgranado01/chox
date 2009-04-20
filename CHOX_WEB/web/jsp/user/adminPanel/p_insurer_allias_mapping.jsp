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
    
    selectedPanel = 'InsurerAlliasMappingMgmt';
    
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
                {name:'insurerName'},
                {name:'insurerId'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });

        gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: 'user/getInsurerAllias.action',method:'GET'}),
            reader:gridviewJsonReader      
        });
    
        gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:recordOnclick },
            store: gridviewData,
            loadMask: true,
            columns: [
                {header: "Insurer", width: 80, dataIndex: 'insurerName', sortable: false, resizable: true},
                {header: "Allias Name", width: 180, dataIndex: 'name', sortable: false, resizable: true},
                {header: "Created By", width: 100, dataIndex: 'createdBy', sortable: false, resizable: true},
                {header: "Created Date", width: 140, dataIndex: 'createdDate', sortable: false, resizable: true}                ,
                {header: "", width: 100, dataIndex: 'Remove', sortable: false, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>Remove</a>"}}

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
        doParameters();
        gridviewData.load(
        {
            params:
            {
                insurerId:orgId
            }
        });
        $("#insurerAlliasName").val("");
    }
    
    function doSelectChange(){
        loadGridViewList();
    }
    
    
    function doParameters(){
        orgId = $("#insurerId").val();
    }
    
    function triggerStatusAddRecord(){
        
        doParameters();
        
        var insurerAlliasName = $("#insurerAlliasName").val();
        
        if(insurerAlliasName!=null && insurerAlliasName!="" && orgId!=null && orgId!=""){
            
            $("#CDInsurerAlliasMessageBox").html("");
            
            $.ajax({
               url: "addInsurerAllias.action?insurerId="+orgId+"&insurerAlliasName="+insurerAlliasName,
               success: onSubmitResponseReceived
            });
            
        }else{
            $("#CDInsurerAlliasMessageBox").html("Please enter 'Insurer Allias Name'");
        }
    }
    
    function triggerStatusRemoveRecord(gridView){

        if(confirm("Are you sure you want to remove this allias?")){
            
            var gridViewId = gridView.get("id");
                
            $.ajax({
               url: "removeInsurerAllias.action?insurerAlliasId="+gridViewId,
               success: onSubmitResponseReceived
            });
        }
    }
    

    function onSubmitResponseReceived(responseText, statusText)  {      
        responseText = responseText.trim();
        $("#CDInsurerAlliasMessageBox").html(responseText);
        loadGridViewList();
    } 
    
</script>



<div>
<fieldset class="x-fieldset">
    <legend>Insurer Allias Mapping</legend>
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
                            headerKey=""
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
                
                <tr><td>Insurer Allias: <input name="insurerAlliasName" id="insurerAlliasName" type="text">
                    <input type="submit" onclick="javascript: triggerStatusAddRecord();" value="Add"/></td></tr>
                <tr><td><div id="CDInsurerAlliasMessageBox" class="errorBox"></div></td></tr>
                <tr><td><div class="chox-form-submit-result"></div></td></tr>
            </table>

        </div>
        <div id="gridviewGrid"></div>
    </div>
</fieldset>
</div>