<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var breband_mapping_gridviewJsonReader;
    var breband_mapping_gridviewDataStore;
    var breband_mapping_gridviewGrid;
    var breband_mapping_gridviewData;
    var breband_mapping_recordPerPage = 20;
    var selectOrgId = <s:property value="selectOrgId" />;
    var selectBandId = -1;
    
    var breband_gridviewJsonReader;
    var breband_choGridviewJsonReader;
    
    var breband_a_gridviewDataStore;
    var breband_a_gridviewGrid;
    var breband_a_gridviewData;
    
    var breband_s_gridviewDataStore;
    var breband_s_gridviewGrid;
    var breband_s_gridviewData;
    
    Ext.onReady(function(){
       
       breband_choGridviewJsonReader = new Ext.data.JsonReader({
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
        
        breband_gridviewJsonReader = new Ext.data.JsonReader({
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

        breband_a_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getAvailableInsurerBreBandChorganisation.action',method:'GET'}),
            reader:breband_choGridviewJsonReader
        });

        breband_s_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getSelectedInsurerBreBandChorganisation.action',method:'GET'}),
            reader:breband_gridviewJsonReader
        });
        
        breband_a_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:breband_recordOnclickAdd },
            store: breband_a_gridviewData,
            loadMask: true,
            columns: [
                {header: "Name", width: 180, dataIndex: 'name', sortable: true, resizable: true},
                {header: "", width: 70, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>Add</a>"}}                
            ],
            renderTo:'breband_a_gridviewGrid',
            width:300, height: 540
            });

        breband_s_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:breband_recordOnclickRemove },
            store: breband_s_gridviewData,
            loadMask: true,
            columns: [
                {header: "Name", width: 180, dataIndex: 'chorganisationName', sortable: true, resizable: true},
                {header: "Active", width: 50, dataIndex: 'chorganisationStatusDesc', sortable: true, resizable: true},
                {header: "", width: 60, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>Remove</a>"}}
            ],
            renderTo:'breband_s_gridviewGrid',
            width:300, height: 540
            });
            
            onBreBandPageRefresh()
    });
    
    function onBreBandPageRefresh(){
        showBreDropDown();
        doParameterRefresh();
        brebandMapping_loadGridViewList();
    }
    
    function brebandMapping_loadGridViewList(){
        
        breband_a_gridviewData.load(
        {
            params:
            {
                insurerId:selectOrgId
            }
        });
        
        breband_s_gridviewData.load(
        {
            params:
            {
                insurerId:selectOrgId,
                breBandId:selectBandId
            }
        });
    }
    
    function breband_recordOnclickAdd(grid, rowIndex, columnIndex, e){
        
        doParameterRefresh();
        
        if(selectBandId<=0){
            alert("Please select a BRE Band");
            return;
        }
        
        var gridView = breband_a_gridviewGrid.getStore().getAt(rowIndex);
        var gridViewId = gridView.get("id");
        
        if(columnIndex==1){
            $.ajax({
               url: "<%= request.getContextPath()%>/prv/p/doAddNewBandChorganisationMapping.action?chorganisationId="+gridViewId+"&breBandId="+selectBandId+uniqeToken(),
               success: doBRESelectOnChange
            });
        }
    }

    function breband_recordOnclickRemove(grid, rowIndex, columnIndex, e){
        
        var gridView = breband_s_gridviewGrid.getStore().getAt(rowIndex);
        var gridViewId = gridView.get("id");
        
        if(columnIndex==2){
            $.ajax({
               url: "<%= request.getContextPath()%>/prv/p/doRemoveBandChorganisationMapping.action?objectId="+gridViewId+uniqeToken(),
               success: doBRESelectOnChange
            });           
        }
    }
    
    function doBRESelectOnChange(){  
        doParameterRefresh();
        brebandMapping_loadGridViewList();
    }
    
    function doParameterRefresh(){
        selectBandId = $("#breBandId").val();
    }
    
    function showBreDropDown() {
        var sLocaltion = "#breBandDropDownDiv";
        var sAction = "<%= request.getContextPath()%>/prv/p/BreBandDropDownAction.action";
        var sparameters = "orgId=" + selectOrgId;
        doSectionLoad(sLocaltion, sAction, sparameters);
        $("#breBandId").val(-1);
    }
    
</script>

<div>
    <div id="organisationGird">
        <div class="gridViewHeader">
           <table width="100%">
                <tr>
                    <td colspan="2">
                        <div id="breBandDropDownDiv" class="label-block"></div>
                    </td>
                    </tr>
                <tr><td colspan="2"><div id="CDBrebandMappingMessageBox" class="errorBox"></div></td></tr>
            <tr>
                <td valign="top">
                    <div class="girdViewLabel">Selected Credit Hire Organisations</div>
                    <div id="breband_s_gridviewGrid" class="girdViewObject"></div>
                </td>
                <td valign="top">
                    <div class="girdViewLabel">Available Credit Hire Organisations</div>
                    <div id="breband_a_gridviewGrid" class="girdViewObject"></div>
                </td>
            </tr>
            </table> 
        </div>
    </div>
</div>
