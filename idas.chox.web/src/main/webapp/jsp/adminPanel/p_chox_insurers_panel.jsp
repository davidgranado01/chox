<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var insurer_gridviewJsonReader;
    var insurer_gridviewDataStore;
    var insurer_gridviewGrid;
    var insurer_gridviewData;

    Ext.onReady(function(){
    
        insurer_gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'id'},
                {name:'name'},
                {name:'vatNo'},
                {name:'companyNo'},
                {name:'status'},
                {name:'statusDesc'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });

        insurer_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getInsurers.action',method:'POST'}),
            reader:insurer_gridviewJsonReader
        });
    
        insurer_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:insurer_recordOnclick },
            store: insurer_gridviewData,
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Name", width: 180, dataIndex: 'name', sortable: true, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>" + value + "</a>"}},
                {header: "VAT No.", width: 120, dataIndex: 'vatNo', sortable: true, resizable: true},
                {header: "Company No.", width: 120, dataIndex: 'companyNo', sortable: true, resizable: true},
                {header: "Active", width: 50, dataIndex: 'statusDesc', sortable: true, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>" + value + "</a>"}},
                {header: "Created By", width:100, dataIndex: 'createdBy', sortable: true, resizable: true},
                {header: "Created Date", width:140, dataIndex: 'createdDate', sortable: true, resizable: true}
            ],
            height:585,
            width: 775
        });

        insurer_gridviewGrid.render('gridviewGridHolderId');
        insurer_loadGridViewList();
        
    }); 

    function insurer_loadGridViewList(){
        insurer_gridviewData.load({ params: { start:0} });
    }
    
    function insurer_recordOnclick(grid, rowIndex, columnIndex, e){
        var gridView = insurer_gridviewGrid.getStore().getAt(rowIndex);
        if(columnIndex==0){
            insurer_loadSelectedRecord(grid, rowIndex, columnIndex, e);
        }else if(columnIndex==3){
            triggerStatusUpdateInsurerRecord(gridView);
        }
    }

    function insurer_loadSelectedRecord(grid, rowIndex, columnIndex, e){
        
        var gridView = insurer_gridviewGrid.getStore().getAt(rowIndex);
        var gridViewId = gridView.get("id");
        var target = "#admin_param_panel";
        var url = "<%= request.getContextPath()%>/prv/p/updateInsurerDetailPanel.action";
        var param = {"objectId":gridViewId};

        ajax.loadHtml2(url,param,function(data){
            $(target).html(data);
        });
        
    }

    function createNewInsurerRecord(){
        
        var target = "#admin_param_panel";
        var url = "<%= request.getContextPath()%>/prv/p/updateInsurerDetailPanel.action";
        var param = {"objectId":-1};

        ajax.loadHtml2(url,param,function(data){
            $(target).html(data);
        });
        
    }

    function triggerStatusUpdateInsurerRecord(gridView){
            
        var aletMsg = "Are you sure you want to inactive this insurer?";
        if(!gridView.get("status")){
            aletMsg = "Are you sure you want to activate this insurer?";
        }
        Ext.MessageBox.confirm('Confirm', aletMsg,function(btn){
        if(btn=='yes'){
            var gridViewId = gridView.get("id");
            var url = "<%= request.getContextPath()%>/prv/p/doTriggerInsurerAccountStatus.action";
            var param = {"objectId":gridViewId};
            
            ajax.loadHtml2(url, param, insurer_loadGridViewList);

        }
        });
    }
    
</script>

<div id="chox-admin-holder">
    <div id="chox-admin-col-div" style ="width:780" >
        <div id="header-title"><label>Insurer Organisation Management</label></div>
        <form id="ChoxInsurerMgmtPanelForm" name="ChoxInsurerMgmtPanelForm" class="XXentity-form" action="POST">
            <div class="admin-gridview-header">
                <table cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td id="label"></td>
                        <td id="buttons"><button type="button" onclick="javascript:createNewInsurerRecord();">Add New Insurer</button></td>
                    </tr>
                </table>
            </div>
            <div id="gridviewGridHolderId"></div>
        </form>
    </div>
</div>