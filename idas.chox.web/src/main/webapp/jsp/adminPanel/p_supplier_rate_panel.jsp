<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var gridviewJsonReader;
    var gridviewGrid;
    var gridviewData;

    Ext.onReady(function(){

        gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                    {name:'id'},
                    {name:'insurerName'},
                    {name:'chorganisationName'},
                    {name:'vehicleClassName'},
                    {name:'rate'},
                    {name:'startDate', type: 'date', dateFormat:'d/m/Y H:i'},
                    {name:'createdBy'},
                    {name:'createdDate', type: 'date', dateFormat:'d/m/Y H:i'}
                ]
        });

        gridviewData = new choxDataStore({
            url: '/prv/p/getVehicleClassSpecialRates.action',
            reader:gridviewJsonReader
        });

        gridviewGrid = new Ext.grid.GridPanel({
            store: gridviewData,
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Insurer", width: 150, dataIndex: 'insurerName', sortable: true, resizable: true},
                {header: "CHO", width: 60, dataIndex: 'chorganisationName', sortable: true, resizable: true},
                {header: "Class", width: 170, dataIndex: 'vehicleClassName', sortable: true, resizable: true},
                {header: "Rate", width: 50, dataIndex: 'rate', sortable: true, resizable: true},
                {header: "Started Date", width: 140, dataIndex: 'startDate', sortable: true, resizable: true, renderer: Ext.util.Format.dateRenderer('d/m/Y H:i')},
                {header: "Created Date", width: 140, dataIndex: 'createdDate', sortable: true, resizable: true, renderer: Ext.util.Format.dateRenderer('d/m/Y H:i')},
                {header: "Created By", width: 80, dataIndex: 'createdBy', sortable: true, resizable: true}
            ],
            height:585,
            width: 775
        });

        gridviewGrid.render('gridviewGridHolderId');
        loadGridViewList();
    });

    function loadGridViewList(){
        gridviewData.load({
            params:
                {
                    start:0,
                    limit:(function(){return ($.browser.mozilla === true ? 22 : 23);}())
                }
        });

        $('div.chox-form-submit-result').html("");
    }


</script>

<div id="chox-admin-holder">
    <div id="chox-admin-col-div" style ="width:780px">
        <form id="ChoxChorganisationMgmtPanelForm" name="SupplierRateMgmtPanelForm" class="XXentity-form" action="" method="POST">
            <div id="header-title"><label>Supplier Rate</label></div>
            <div class="chox-form-submit-result">&nbsp;</div>
            <div id="gridviewGridHolderId"></div>
        </form>
    </div>
</div>