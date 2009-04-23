<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    

    
    var orgId = -1;
    var selectOrgId = <s:property value="selectOrgId" />;
    
    var gridviewJsonReader;
    var choGridviewJsonReader;
    
    var a_gridviewDataStore;
    var a_gridviewGrid;
    var a_gridviewData;
    
    var s_gridviewDataStore;
    var s_gridviewGrid;
    var s_gridviewData;
    
    Ext.onReady(function(){
        
       if(selectOrgId>0){
           $("#insurerId").val(selectOrgId);
       }
       
       choGridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',   
            root: 'results', 
            fields:
            [
                {name:'id'},
                {name:'name'},
		{name:'status'},
                {name:'statusDesc'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });
        
        gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',   
            root: 'results', 
            fields:
            [
                {name:'id'},
                {name:'insurerId'},
                {name:'chorganisationId'},
		{name:'insurerName'},
		{name:'chorganisationName'},
		{name:'status'},
                {name:'statusDesc'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });

        a_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: 'user/getAvailableInsurerChorganisation.action',method:'GET'}),
            reader:choGridviewJsonReader      
        });

        s_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: 'user/getSelectedInsurerChorganisation.action',method:'GET'}),
            reader:gridviewJsonReader      
        });
        
        
        a_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:recordOnclickAdd },
            store: a_gridviewData,
            loadMask: true,
            columns: [
                {header: "Name", width: 220, dataIndex: 'name', sortable: false, resizable: true},
                {header: "Action", width: 60, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>Add</a>"}}                
            ],
            renderTo:'a_gridviewGrid',
                width:280,
                autoHeight:true,
                enableHdMenu:false
            });
           
        s_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:recordOnclickRemove },
            store: s_gridviewData,
            loadMask: true,
            columns: [
                {header: "Name", width: 220, dataIndex: 'chorganisationName', sortable: false, resizable: true},
                {header: "Action", width: 60, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>Remove</a>"}}
            ],
            renderTo:'s_gridviewGrid',
                width:280,
                autoHeight:true,
                enableHdMenu:false
            });
            
            loadGridViewList();
    }); 
    
    function recordOnclickAdd(grid, rowIndex, columnIndex, e){
        
        var gridView = a_gridviewGrid.getStore().getAt(rowIndex);  // Get the Record
        
        if(columnIndex==1){
            doAddnewCredirHire(gridView);
        }
        /*
        if(columnIndex==0){
            loadSelectedRecord(grid, rowIndex, columnIndex, e);
        }else if(columnIndex==3){
            triggerStatusUpdateRecord(gridView);
        }
        */
    }
    
    function recordOnclickRemove(grid, rowIndex, columnIndex, e){
        
        var gridView = s_gridviewGrid.getStore().getAt(rowIndex);  // Get the Record
        
        if(columnIndex==1){
            triggerStatusInactiveRecord(gridView);
        }
    }
    
    function loadSelectedRecord(grid, rowIndex, columnIndex, e){
        
        /*
       var gridView = gridviewGrid.getStore().getAt(rowIndex);
       var gridViewId = gridView.get("id");
       alert("gridViewId:"+gridViewId);
       */
        // $("#admin_param_panel").load("updateUserDetailPanel.action?mode=Edit&objectId=" + gridViewId + "&orgTypeId=" + selectedOrgTypeId);
    }
    
    function loadGridViewList(){
        
        doParameters();
        
        a_gridviewData.load(
        {
            params:
            {
                insurerId:orgId
            }
        });
        
        s_gridviewData.load(
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
       // alert("doParameters>orgId:"+orgId);
    }
    
    function doSelectChange(){
        loadGridViewList();
    }
    
    function doAddnewCredirHire(gridView){
        
        doParameters();
        var gridViewId = gridView.get("id");
        
        $.ajax({
           url: "doAddNewInsurerChorganisation.action?chorganisationId="+gridViewId+"&insurerId="+orgId,
           success: doSelectChange
        });
    }
    
    function triggerStatusInactiveRecord(gridView){
        doParameters();
        var gridViewId = gridView.get("id");
        
        if(confirm("Are you sure you want to remove this credit hire?")){
         $.ajax({
           url: "doRemoveInsurerChorganisation.action?objectId="+gridViewId,
           success: doSelectChange
         });
        }
    }
    
    
</script>

<div>
<fieldset class="x-fieldset">
    <legend>Credit Hire Mapping</legend>
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
            </table>

        </div>
        <table>
            <tr><td valign="top">
                    <fieldset class="x-fieldset"><legend>Available</legend>
                <div id="a_gridviewGrid"></div>
                    </fieldset>
            </td><td valign="top">
                <fieldset class="x-fieldset">
                <div id="s_gridviewGrid"></div><legend>Selected</legend>
                </fieldset>
                </td></tr>
        </table>
        
    </div>
</fieldset>
</div>