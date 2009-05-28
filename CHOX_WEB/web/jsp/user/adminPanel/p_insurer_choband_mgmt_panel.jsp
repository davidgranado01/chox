<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var choband_gridviewJsonReader;
    var choband_gridviewDataStore;
    var choband_gridviewGrid;
    var choband_gridviewData;

    var recordPerPage = 20;
    var selectOrgId = <s:property value="selectOrgId" />;
    
    Ext.onReady(function(){
    
        choband_gridviewJsonReader = new Ext.data.JsonReader({
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

    choband_gridviewData = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy
        ({url: 'user/getInsurerChoBand.action',method:'GET'}),
        reader:choband_gridviewJsonReader
    });
    
    choband_gridviewGrid = new Ext.grid.GridPanel({
        listeners:  {cellclick:choband_recordOnclick },
        store: choband_gridviewData,
        columns: [
            {header: "Insurer", width: 100, dataIndex: 'insurerName', sortable: false, resizable: true},
            {header: "Band", width: 240, dataIndex: 'name', sortable: false, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>" + value + "</a>"}},
            {header: "Created By", width: 110, dataIndex: 'createdBy', sortable: false, resizable: true},
            {header: "Created Date", width: 150, dataIndex: 'createdDate', sortable: false, resizable: true}
        ],
        renderTo:'choband_gridviewGrid',
            width:620,
            autoHeight:true,
            enableHdMenu:false
        });

        choband_gridviewData.load(
        {
            params:
            {
                gridviewtype : "creditHireOrgMgmt",
                insurerId:selectOrgId
            }
        });
    }); 
    
    function choband_recordOnclick(grid, rowIndex, columnIndex, e){

        var gridView = choband_gridviewGrid.getStore().getAt(rowIndex);
        
        if(columnIndex==1){
            choband_loadSelectedRecord(grid, rowIndex, columnIndex, e);
        }  
    }
    
    function choband_loadSelectedRecord(grid, rowIndex, columnIndex, e){
        var gridView = choband_gridviewGrid.getStore().getAt(rowIndex);
        var gridViewId = gridView.get("id");
        $("#chobandDiv").load("updateInsurerChoBandDetailPanel.action?objectId=" + gridViewId + "&insurerId=" + selectOrgId);
    }

    function choband_createNewRecord(){
        var gridViewId = -1;
        $("#chobandDiv").load("updateInsurerChoBandDetailPanel.action?objectId=" + gridViewId + "&insurerId=" + selectOrgId);
    }
    
    function choband_loadGridViewList(){
        choband_gridviewData.load(
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

<div id="chobandDiv" name="chobandDiv">
        <div id="organisationGird">
            <div class="gridViewHeader">
                
                <table width="100%">
                    <tr>
                        <td></td>
                        <td align="right"><button type="button" onclick="javascript:choband_createNewRecord();">New</button></td>
                    </tr>
                </table>
            </div>
            <div id="choband_gridviewGrid"></div>
        </div>
</div>