<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var supplierRateJsonReader;
    var supplierRateGridPanel;
    var supplierChoxDataStore;
    var supplierRatesPagingBar;

    Ext.onReady(function(){

        supplierRateJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                    {name:'id'},
                    {name:'insurerName'},
                    {name:'chorganisationName'},
                    {name:'vehicleClassName'},
                    {name:'rate'},
                    {name:'startDate'},
                    {name:'createdBy'},
                    {name:'createdDate'}
                ]
        });

        supplierChoxDataStore = new choxDataStore({
            url: '/prv/p/getVehicleClassSpecialRates.action',
            reader:supplierRateJsonReader,
            remoteSort: true
        });

        supplierRatesPagingBar = new Ext.PagingToolbar({
            pageSize: (function(){return ($.browser.mozilla === true ? 22 : 23);}()),
            store: supplierChoxDataStore,
            displayInfo: true,
            displayMsg: 'Displaying rates {0} - {1} of {2}',
            emptyMsg: "No rates to display.",
            plugins: new Ext.ux.ProgressBarPager()
        });

        supplierRateGridPanel = new Ext.grid.GridPanel({
            listeners:  {cellclick:recordOnclick },
            store: supplierChoxDataStore,
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            loadMask:true,
            viewConfig:{forceFit:true},
            columns: [
                {header: "Insurer", width: 150, dataIndex: 'insurerName', sortable: true, resizable: true},
                {header: "CHO", width: 60, dataIndex: 'chorganisationName', sortable: true, resizable: true},
                {header: "Class", width: 170, dataIndex: 'vehicleClassName', sortable: true, resizable: true},
                {header: "Rate", width: 50, dataIndex: 'rate', sortable: true, resizable: true},
                {header: "Start Date", width: 140, dataIndex: 'startDate', sortable: true, resizable: true},
                {header: "Created Date", width: 140, dataIndex: 'createdDate', sortable: true, resizable: true},
                {header: "Created By", width: 80, dataIndex: 'createdBy', sortable: true, resizable: true}
            ],
            height:585,
            width: 775,
            bbar: supplierRatesPagingBar
        });

        supplierRateGridPanel.render('gridviewGridHolderId');
        loadGridViewList();
    });

    function loadGridViewList(){
        supplierChoxDataStore.removeAll();
        supplierChoxDataStore.load({
            params:
                {
                    start:0,
                    limit:(function(){return ($.browser.mozilla === true ? 22 : 23);}())
                }
        });

        $('div.chox-form-submit-result').html("");
    }


    function recordOnclick(grid, rowIndex, columnIndex, e){
        // Delete record here
    }

</script>

<div id="chox-admin-holder">
    <div id="chox-admin-col-div" style="width:780px">
        <div id="header-title"><label>Supplier Rate</label></div>
        <div class="chox-form-submit-result">&nbsp;</div>
        <div id="gridviewGridHolderId"></div>
    </div>
</div>