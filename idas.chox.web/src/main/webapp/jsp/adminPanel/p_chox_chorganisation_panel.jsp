<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var gridviewJsonReader;
    var gridviewDataStore;
    var gridviewGrid;  
    var gridviewData;

    Ext.onReady(function(){
        
        gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',   
            root: 'results', 
            fields:
                [
                {name:'id'},
                {name:'name'},  
                {name:'address'},
                {name:'vatNo'},
                {name:'companyNo'},
                {name:'authoritiyDelegated'},
                {name:'status'},
                {name:'statusDesc'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });     

        gridviewData = new choxDataStore({
            url: '/prv/p/getChorganisations.action',
            reader:gridviewJsonReader      
        });

        gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:recordOnclick },
            store: gridviewData,
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Name", width: 150, dataIndex: 'name', sortable: true, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>" + value + "</a>";}},
                {header: "VAT No.", width: 60, dataIndex: 'vatNo', sortable: true, resizable: true},
                {header: "Address", width: 170, dataIndex: 'address', sortable: true, resizable: true},
                {header: "Active", width: 50, dataIndex: 'statusDesc', sortable: true, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>" + value + "</a>";}},
                {header: "Created By", width: 80, dataIndex: 'createdBy', sortable: true, resizable: true},
                {header: "Created Date", width: 140, dataIndex: 'createdDate', sortable: true, resizable: true}
            ],
            height:585,
            width: 775
        });

        gridviewGrid.render('gridviewGridHolderId');
        loadGridViewList();
    
    }); 
    
    function loadGridViewList(){
        gridviewData.load({ params: { start:0} });
    }
    
    function recordOnclick(grid, rowIndex, columnIndex, e){
        
        var gridView = gridviewGrid.getStore().getAt(rowIndex);
        
        if(columnIndex===0){
            loadSelectedRecord(grid, rowIndex, columnIndex, e);
        }else if(columnIndex===3){
            triggerStatusUpdateRecord(gridView);
        }
        
    }

    function loadSelectedRecord(grid, rowIndex, columnIndex, e){

        var gridView = gridviewGrid.getStore().getAt(rowIndex);
        var gridViewId = gridView.get("id");
        var target = "#admin_param_panel";
        var url = "/prv/p/updateChorganisationDetailPanel.action";
        var param = {"objectId":gridViewId};

        ajax.loadHtml2(url,param,function(data){
            $(target).html(data);
        });

    }

    function createNewRecord(){

        var target = "#admin_param_panel";
        var url = "/prv/p/updateChorganisationDetailPanel.action";
        var param = {"objectId":-1};

        ajax.loadHtml2(url,param,function(data){
            $(target).html(data);
        });

    }

    function triggerStatusUpdateRecord(gridView){
            
        var aletMsg = "Are you sure you want to make this Credit Hire Organisation inactive?";
        if(!gridView.get("status")){
            aletMsg = "Are you sure you want to make this Credit Hire Organisation active?";
        }
        Ext.MessageBox.confirm('Confirm', aletMsg,function(btn){
        if(btn==='yes'){
            var gridViewId = gridView.get("id");
            var url = "/prv/p/doTriggerCreditHireAccountStatus.action";
            var param = {"objectId":gridViewId};
            ajax.loadHtml2(url,param,loadGridViewList);
        }
       }); 
    }

</script>

<div id="chox-admin-holder">
    <div id="chox-admin-col-div" style ="width:780px" >
        <div id="header-title"><label>Credit Hire Organisation Management</label></div>
        <form id="ChoxChorganisationMgmtPanelForm" name="ChoxChorganisationMgmtPanelForm" class="XXentity-form" action="" method="POST">
            <div class="admin-gridview-header">
                <table cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td id="label"></td>
                        <td id="buttons"><button type="button" onclick="javascript:createNewRecord();">Add New Credit Hire Organisation</button></td>
                    </tr>
                </table>

            </div>
            <div id="gridviewGridHolderId"></div>
        </form>
    </div>
</div>