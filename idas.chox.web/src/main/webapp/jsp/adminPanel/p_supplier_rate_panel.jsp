<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var supplierRateJsonReader;
    var supplierRateGridPanel;
    var supplierChoxDataStore;
    var supplierRatesPagingBar;

    Ext.onReady(function() {
        supplierRateSetupTabIndex = 0;
        supplierRateSetupPanelTabs = new Ext.TabPanel({
            renderTo: 'supplierRatePanel',
            height:660,
            width:775,
            border:true,
            loadMask:false,
            activeTab: supplierRateSetupTabIndex,
            items:[
                {contentEl:'newRateTab', title:'Upload', listeners: {activate: handleActivate}},
                {contentEl:'existingRateTab', title:'Supplier Rate', listeners: {activate: handleActivate}},
            ]
        });
        supplierRateSetupPanelTabs.setActiveTab(supplierRateSetupTabIndex);

        function handleActivate(tab) {
            supplierRateSetupTabIndex = 0;
            if (typeof supplierRateSetupPanelTabs !== 'undefined') {
                supplierRateSetupTabIndex = supplierRateSetupPanelTabs.items.indexOf(supplierRateSetupPanelTabs.getActiveTab());
            }
        }

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
            viewConfig:{forceFit:true},
            columns: [
                {header: "Insurer", width: 95, dataIndex: 'insurerName', sortable: true, resizable: true},
                {header: "CHO", width: 156, dataIndex: 'chorganisationName', sortable: true, resizable: true},
                {header: "Class", width: 95, dataIndex: 'vehicleClassName', sortable: true, resizable: true},
                {header: "Rate", width: 44, dataIndex: 'rate', sortable: true, resizable: true},
                {header: "Start Date", width: 112, dataIndex: 'startDate', sortable: true, resizable: true},
                {header: "Created Date", width: 112, dataIndex: 'createdDate', sortable: true, resizable: true},
                {header: "Created By", width: 112, dataIndex: 'createdBy', sortable: true, resizable: true}
            ],
            height:570,
            width: 765,
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


    //
    // Upload New CSV for Rate
    //
    var supplierRateStore = new Ext.data.ArrayStore({
        // store configs
        autoDestroy: true,
        storeId: 'supplierRateStore',
        // reader configs
        // idIndex: 0,
        // http://cdn.sencha.com/ext/gpl/3.4.1.1/docs/#!/api/Date
        fields: [
        'Insurer',
        'CHO',
        'Class',
        {name: 'Rate', type: 'float'},
        {name: 'StartDate', type: 'date', dateFormat: 'j/n/Y'},
        ]
    });

    var supplierRateGrid = new Ext.grid.GridPanel({
        store: supplierRateStore,
        columns: [{
                header: 'Insurer', dataIndex: 'Insurer', sortable: true, resizable: true
            }, {
                header: 'CHO', dataIndex: 'CHO', sortable: true, resizable: true
            }, {
                header: 'Class', dataIndex: 'Class', sortable: true, resizable: true
            }, {
                header: 'Rate', dataIndex: 'Rate', sortable: true, resizable: true
            }, {
                header: 'Start Date', dataIndex: 'StartDate', sortable: true, resizable: true, renderer: Ext.util.Format.dateRenderer('j/n/Y')
            }],
        enableHdMenu:false,
        enableColumnMove: false,
        layout:'fit',
        viewConfig:{forceFit:true},
        height:500,
        width: 765,
    });

    var fileInput = document.getElementById("supplierRateFile");
    fileInput.addEventListener('change', readCsvFile);

    function readCsvFile() {
        if (!fileInput.value || !fileInput.value.endsWith('.csv')) {
            return;
        }

        var reader = new FileReader();
        reader.onload = function (e) {
            var csv = parseCsv(reader.result);
            supplierRateStore.loadData(csv);
            supplierRateGrid.render('gridviewSupplierRateHolder');
        };
        // start reading the file. When it is done, calls the onload event defined above.
        reader.readAsBinaryString(fileInput.files[0]);
    };

    function parseCsv(csvString) {
        var result = [];

        var lines = csvString.split("\n");
        // ignore the first column
        for (var i = 1; i < lines.length; i++) {
            var line = lines[i];
            if (line.length > 0) {
                result.push(line.trim().split(","))
            }
        }
        // console.table(result);
        return result;
    }
</script>

<div id="chox-admin-holder">
    <div id="chox-admin-col-div" style ="width:780px">
        <div id="supplierRatePanel"></div>
    </div>

    <div id="existingRateTab" class="x-hide-display" style="background-color:#DFE8F6;height:100%;">
        <div id="header-title"><label>Supplier Rate</label></div>
        <div class="chox-form-submit-result">&nbsp;</div>
        <div id="gridviewGridHolderId"></div>
    </div>

    <div id="newRateTab" class="x-hide-display" style="background-color:#DFE8F6;height:100%;padding:10px 5px;">
        <form id="supplierRateForm" name="supplierRateForm" action="<%= request.getContextPath()%>/prv/p/uploadSupplierRate.action" method="POST" enctype="multipart/form-data">
        <div class="form-container">
            <fieldset class="x-fieldset">
            <table class="chox-form-item" cellpadding="0" cellspacing="0" border="0" width="100%">
                <tr>
                    <td width="200" align="right">
                        <label class="std-label-ro">File &nbsp;</label>
                    </td>
                    <td>
                        <div id="supplierRateWrapper">
                            <input id="supplierRateFile" name="supplierRateFile" type="file" size="20" autocomplete="off"
                                accept=".csv"
                                class="x-form-file x-form-field" title="Upload Supplier Rate CSV File." style="width: 200px;" />
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                    </td>
                    <td>
                        <div class="column-remark" style="padding:10px 0 10px 0;">
                            <input type="button" value="Confirm" />
                            &nbsp; Upload CSV file with maximum size of 2 MB.
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>
                        <s:if test="uploadFlag">
                            <input id="supplierRateButton" type="submit" value="Upload Supplier Rate" />
                        </s:if>
                        <s:else><br/>
                            <div class="action-error-msg"><b></b></div>
                        </s:else>
                    </td>
                </tr>
            </table>
            <div class="chox-form-submit-result" id="supplierRateResultId"/>
            <div class="action-error-msg" id="supplierRateMsgBox"/>
            </fieldset>
            <div id="gridviewSupplierRateHolder"></div>
        </div>
        </form>
    </div>
</div>