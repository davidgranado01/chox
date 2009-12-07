<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var breband_gridviewJsonReader;
    var breband_gridviewDataStore;
    var breband_gridviewGrid;
    var breband_gridviewData;

    var recordPerPage = 20;
    var selectOrgId = <s:property value="selectOrgId" />;
    
    Ext.onReady(function(){
    
        breband_gridviewJsonReader = new Ext.data.JsonReader({
        totalProperty: 'totalCount',   
        root: 'results', 
        fields:
        [
            {name:'id'},
            {name:'name'},
            {name:'insurerName'},
            {name:'status'},
            {name:'statusDesc'},
            {name:'createdBy'},
            {name:'createdDate'}
        ]
    });

    breband_gridviewData = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy
        ({url: 'user/getInsurerBreBand.action',method:'GET'}),
        reader:breband_gridviewJsonReader
    });
    
    breband_gridviewGrid = new Ext.grid.GridPanel({
        listeners:  {cellclick:breband_recordOnclick },
        store: breband_gridviewData,
        columns: [
            {header: "Insurer", width: 100, dataIndex: 'insurerName', sortable: true, resizable: true},
            {header: "Band", width: 240, dataIndex: 'name', sortable: true, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>" + value + "</a>"}},
            {header: "Created By", width: 110, dataIndex: 'createdBy', sortable: true, resizable: true},
            {header: "Created Date", width: 150, dataIndex: 'createdDate', sortable: true, resizable: true}
        ],
        renderTo:'breband_gridviewGrid',
            width:640,
            autoHeight:true,
            enableHdMenu:false
        });

        breband_gridviewData.load(
        {
            params:
            {
                gridviewtype : "creditHireOrgMgmt",
                insurerId:selectOrgId
            }
        });
    }); 
    
    function breband_recordOnclick(grid, rowIndex, columnIndex, e){

        var gridView = breband_gridviewGrid.getStore().getAt(rowIndex);
        
        if(columnIndex==1){
            breband_loadSelectedRecord(grid, rowIndex, columnIndex, e);
        }  
    }
    
    function breband_loadSelectedRecord(grid, rowIndex, columnIndex, e){
        var gridView = breband_gridviewGrid.getStore().getAt(rowIndex);
        var gridViewId = gridView.get("id");
        var sLocaltion = "#breBandDiv";
        var sAction = "updateInsurerBreBandDetailPanel.action";
        var sparameters = "objectId=" + gridViewId + "&insurerId=" + selectOrgId;
        doSectionLoad(sLocaltion, sAction, sparameters);
    }

    function breband_createNewRecord(){
        var gridViewId = -1;
        var sLocaltion = "#breBandDiv";
        var sAction = "updateInsurerBreBandDetailPanel.action";
        var sparameters = "objectId=" + gridViewId + "&insurerId=" + selectOrgId;
        doSectionLoad(sLocaltion, sAction, sparameters);
    }
    
    function breband_loadGridViewList(){
        breband_gridviewData.load(
        {
            params:
            {
                start:0,
                limit:recordPerPage,
                insurerId:selectOrgId
            }
        });
    }

</script>

<div id="breBandDiv" name="breBandDiv">
    
        <div id="organisationGird">
            
            <div class="gridViewHeader">
                
                <table width="100%">
                    <tr>
                        <td></td>
                        <td align="right"><button type="button" onclick="javascript:breband_createNewRecord();">Add New Band</button></td>
                    </tr>
                </table>
            </div>
            
            <div id="breband_gridviewGrid" class="admin-tab-grid-view"></div>
            
        </div>
    
</div>