<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var insChoAvailable_gridviewJsonReader;
    var insChoAvailable_gridviewGrid;
    var insChoAvailable_gridviewData;

    var insChoSelected_gridviewJsonReader;
    var insChoSelected_gridviewGrid;
    var insChoSelected_gridviewData;
    
    Ext.onReady(function(){

        insChoAvailable_gridviewJsonReader = new Ext.data.JsonReader({
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
        
        insChoAvailable_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getAvailableInsurerChorganisation.action',method:'POST'}),
            reader:insChoAvailable_gridviewJsonReader
        });

        insChoAvailable_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:insCho_recordOnclickAddNewCreditHire},
            store: insChoAvailable_gridviewData,
            renderTo:'insChoAvailable_gridviewGrid',
            enableHdMenu:false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Name", width: 220, dataIndex: 'name', sortable: true, resizable: true},
                {header: "", width: 60, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){ return "<a href='#' class='highlightItem'>Add</a>"}}
            ],
            height:460,
            width: 340
        });

        insChoSelected_gridviewJsonReader = new Ext.data.JsonReader({
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
                {name:'status'},
                {name:'statusDesc'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });
        
        insChoSelected_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getSelectedInsurerChorganisation.action',method:'POST'}),
            reader:insChoSelected_gridviewJsonReader
        });
        
        insChoSelected_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:insCho_recordOnclickRemoveCreditHire},
            store: insChoSelected_gridviewData,
            renderTo:'ins_cho_s_gridviewGrid',
            enableHdMenu:false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Name", width: 170, dataIndex: 'chorganisationName', sortable: true, resizable: true},
                {header: "Active", width: 50, dataIndex: 'chorganisationStatusDesc', sortable: true, resizable: true},
                {header: "", width: 60, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='highlightItem'>Remove</a>"}}
            ],
            height:460,
            width: 340
        });

        insCho_loadGridViewList();
    }); 

    function insCho_loadGridViewList(){
        insChoAvailable_gridviewData.load({params:{insurerId:<s:property value="insurerId" />}});
        insChoSelected_gridviewData.load({params:{insurerId:<s:property value="insurerId" />}});
    }

    function insCho_recordOnclickAddNewCreditHire(grid, rowIndex, columnIndex, e){
        
        var gridView = insChoAvailable_gridviewGrid.getStore().getAt(rowIndex);
        if(columnIndex==1){
            var chorganisationId = gridView.get("id");
            var url = "<%= request.getContextPath()%>/prv/p/doAddNewInsurerChorganisation.action";
            var param = {"insurerId":<s:property value="insurerId" />,"chorganisationId":chorganisationId};
            ajax.loadHtml(url, param, insCho_loadGridViewList);
        }
        
    }
    
    function insCho_recordOnclickRemoveCreditHire(grid, rowIndex, columnIndex, e){
        var gridView = insChoSelected_gridviewGrid.getStore().getAt(rowIndex);
        if(columnIndex==2){
            if(confirm("Are you sure you want to remove this credit hire organisation?")){
                var insurerChorganisationId = gridView.get("id");
                var url = "<%= request.getContextPath()%>/prv/p/doRemoveInsurerChorganisation.action";
                var param = {"insurerChorganisationId":insurerChorganisationId};
                ajax.loadHtml(url, param, insCho_loadGridViewList);
            }
        }
    }

</script>

<div class="sub-admin-tab-css">
    <div class="status-info">
        {Credit Hire Mapping}
    </div>

    <table width="100%">
        <tr>
            <td valign="top">
                <label class="gird-view-label">Selected Credit Hire Organisations</label>
                <div id="ins_cho_s_gridviewGrid"></div>
            </td>
            <td valign="top">
                <label class="gird-view-label">Available Credit Hire Organisations</label>
                <div id="insChoAvailable_gridviewGrid"></div>
            </td>
        </tr>
    </table>

</div>  