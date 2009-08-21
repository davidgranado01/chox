<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var choband_mapping_gridviewJsonReader;
    var choband_mapping_gridviewDataStore;
    var choband_mapping_gridviewGrid;
    var choband_mapping_gridviewData;
    var choband_mapping_recordPerPage = 20;
    var selectOrgId = <s:property value="selectOrgId" />;
    var selectBandId = -1;
    
    choband_mapping_selectedPanel = 'InsurerLineOfBusinessMappingMgmt';
    
    var choband_gridviewJsonReader;
    var choband_choGridviewJsonReader;
    
    var choband_a_gridviewDataStore;
    var choband_a_gridviewGrid;
    var choband_a_gridviewData;
    
    var choband_s_gridviewDataStore;
    var choband_s_gridviewGrid;
    var choband_s_gridviewData;
    
    Ext.onReady(function(){
       
       choband_choGridviewJsonReader = new Ext.data.JsonReader({
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
        
        choband_gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',   
            root: 'results', 
            fields:
            [
                {name:'id'},
                {name:'insurerId'},
                {name:'chorganisationId'},
		{name:'insurerName'},
		{name:'chorganisationName'},
                {name:'chorganisationStatus'},
                {name:'chorganisationStatusDesc'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });

        choband_a_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: 'user/getAvailableInsurerChoBandChorganisation.action',method:'GET'}),
            reader:choband_choGridviewJsonReader      
        });

        choband_s_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: 'user/getSelectedInsurerChoBandChorganisation.action',method:'GET'}),
            reader:choband_gridviewJsonReader      
        });
        
        choband_a_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:choband_recordOnclickAdd },
            store: choband_a_gridviewData,
            loadMask: true,
            columns: [
                {header: "Name", width: 180, dataIndex: 'name', sortable: true, resizable: true},
                {header: "", width: 70, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>Add</a>"}}                
            ],
            renderTo:'choband_a_gridviewGrid',
                width:290,
                autoHeight:true,
                enableHdMenu:false
            });

        choband_s_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:choband_recordOnclickRemove },
            store: choband_s_gridviewData,
            loadMask: true,
            columns: [
                {header: "Name", width: 180, dataIndex: 'chorganisationName', sortable: true, resizable: true},
                {header: "Active", width: 50, dataIndex: 'chorganisationStatusDesc', sortable: true, resizable: true},
                {header: "", width: 60, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>Remove</a>"}}
            ],
            renderTo:'choband_s_gridviewGrid',
                width:290,
                autoHeight:true,
                enableHdMenu:false
            });
            
            onChoBandPageRefresh()
    });
    
    function onChoBandPageRefresh(){
        showBreDropDown();
        doParameterRefresh();
        chobandMapping_loadGridViewList();
    }
    
    function chobandMapping_loadGridViewList(){
        
        choband_a_gridviewData.load(
        {
            params:
            {
                insurerId:selectOrgId
            }
        });
        
        choband_s_gridviewData.load(
        {
            params:
            {
                insurerId:selectOrgId,
                chobandId:selectBandId
            }
        });
    }
    
    function choband_recordOnclickAdd(grid, rowIndex, columnIndex, e){
        
        doParameterRefresh();
        
        if(selectBandId<=0){
            alert("Please select a BRE Band");
            return;
        }
        
        var gridView = choband_a_gridviewGrid.getStore().getAt(rowIndex);
        var gridViewId = gridView.get("id");
        
        if(columnIndex==1){
            $.ajax({
               url: "doAddNewBandChorganisationMapping.action?chorganisationId="+gridViewId+"&chobandId="+selectBandId,
               success: doBRESelectOnChange
            });
        }
    }

    function choband_recordOnclickRemove(grid, rowIndex, columnIndex, e){
        
        var gridView = choband_s_gridviewGrid.getStore().getAt(rowIndex);
        var gridViewId = gridView.get("id");
        
        if(columnIndex==2){
            $.ajax({
               url: "doRemoveBandChorganisationMapping.action?objectId="+gridViewId,
               success: doBRESelectOnChange
            });           
        }
    }
    
    function doBRESelectOnChange(){  
        doParameterRefresh();
        chobandMapping_loadGridViewList();
    }
    
    function doParameterRefresh(){
        selectBandId = $("#breBandId").val();
    }
    
    function showBreDropDown() {
        $("#chobandDropDownDiv").load("ChoBandDropDownAction.action?orgId=" + selectOrgId);
        $("#breBandId").val(-1);
    }
    
</script>

<div>
    <div id="organisationGird">
        <div class="gridViewHeader">
           <table width="100%">
                <tr>
                </tr>
                <tr>
                    <td colspan="2">
                        <div id="chobandDropDownDiv" class="label-block"></div> 
                    </td>
                    </tr>
                <tr><td colspan="2"><div id="CDChobandMappingMessageBox" class="errorBox"></div></td></tr>                
            <tr>
                <td valign="top">
                    <div class="girdViewLabel">Selected Credit Hire Organisations</div>
                    <div id="choband_s_gridviewGrid" class="girdViewObject"></div>
                </td>
                <td valign="top">
                    <div class="girdViewLabel">Available Credit Hire Organisations</div>
                    <div id="choband_a_gridviewGrid" class="girdViewObject"></div>
                </td>
            </tr>
            </table> 
        </div>
    </div>
</div>
