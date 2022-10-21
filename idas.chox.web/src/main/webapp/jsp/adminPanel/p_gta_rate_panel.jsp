<%--
  Created by IntelliJ IDEA.
  User: ABiswas
  Date: 6/22/2022
  Time: 2:34 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">


    var gtaRateJsonReader;
    var gtaRateGridPanel;
    var gtaChoxDataStore;
    var gtaRatePagingBar;


    Ext.onReady(function () {
        gtaRateSetupTabIndex = 0;
        gtaRateSetupPanelTabs = new Ext.TabPanel({
            renderTo: 'gtaRatePanel',
            height: 660,
            width: 775,
            border: true,
            loadMask: false,
            activeTab: gtaRateSetupTabIndex,
            items: [
                {contentEl: 'existingRateTab', title: 'GTA Rates', listeners: {activate: handleActivate}},
                {contentEl: 'newRateTab', title: 'Upload', listeners: {activate: handleActivate}},
            ]
        });
        gtaRateSetupPanelTabs.setActiveTab(gtaRateSetupTabIndex);

        function handleActivate(tab) {
            gtaRateSetupTabIndex = 0;
            if (typeof gtaRateSetupPanelTabs !== 'undefined') {
                gtaRateSetupTabIndex = gtaRateSetupPanelTabs.items.indexOf(gtaRateSetupPanelTabs.getActiveTab());
            }
        }

        gtaRateJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                    {name: 'vehicleClassName'},
                    {name: 'rate'},
                    {name: 'startDate'},
                    {name: 'createdBy'},
                    {name: 'createdDate'},
                    {name: 'age'},
                    {name: 'id'},
                    {name: 'showDeleteLink'}
                ]
        });

        gtaChoxDataStore = new choxDataStore({
            url: '/prv/p/getVehicleClassGTARates.action',
            reader: gtaRateJsonReader,
            remoteSort: true
        });

        gtaChoxDataStore.setDefaultSort('startDate', 'desc');

        gtaRatePagingBar = new Ext.PagingToolbar({
            pageSize: (function () {
                return ($.browser.mozilla === true ? 22 : 23);
            }()),
            store: gtaChoxDataStore,
            displayInfo: true,
            displayMsg: 'Displaying rates {0} - {1} of {2}',
            emptyMsg: "No rates to display.",
            plugins: new Ext.ux.ProgressBarPager()
        });

        gtaRateGridPanel = new Ext.grid.GridPanel({
            listeners: {cellclick: recordOnclick},
            store: gtaChoxDataStore,
            enableHdMenu: false,
            enableColumnMove: false,
            layout: 'fit',
            viewConfig: {forceFit: true},
            columns: [

                {header: "Class", width: 95, dataIndex: 'vehicleClassName', sortable: false, resizable: true},
                {
                    header: "Rate",
                    width: 44,
                    dataIndex: 'rate',
                    sortable: false,
                    resizable: true,
                    renderer: Ext.util.Format.numberRenderer('00.00')
                },
                {header: "Start Date", width: 112, dataIndex: 'startDate', sortable: true, resizable: true},
                {header: "Created Date", width: 112, dataIndex: 'createdDate', sortable: true, resizable: true},
                {header: "Created By", width: 112, dataIndex: 'createdBy', sortable: false, resizable: true},
                {header: "Age", width: 66, dataIndex: 'age', sortable: false, resizable: true},
                {
                    header: "", width: 56, dataIndex: 'showDeleteLink', sortable: false, renderer: function (value, p, r) {
                        if (value)
                            return "<a href='#' class='high-light-item'> Delete</a>";
                    }
                }
            ],
            height: 570,
            width: 765,
            bbar: gtaRatePagingBar
        });

        loadGridViewList();
        gtaRateGridPanel.render('gridviewGridHolderId');
    });

    function loadGridViewList() {
        gtaChoxDataStore.removeAll();
        gtaChoxDataStore.load({
            params:
                {
                    start: 0,
                    limit: (function () {
                        return ($.browser.mozilla === true ? 22 : 23);
                    }())
                }
        });
    }

    function reloadGridViewList() {
        gtaChoxDataStore.reload();
    }


    function recordOnclick(grid, rowIndex, columnIndex, e) {
        // Delete record here

        if (columnIndex === 6) {
            deleteRecord(grid, rowIndex, columnIndex, e);
        }
    }

    function deleteRecord(grid, rowIndex, columnIndex, e) {

        var gridView = gtaRateGridPanel.getStore().getAt(rowIndex);

        if (!gridView.data.showDeleteLink)
            return;

        var gridViewId = gridView.get("id");

        var url = "/prv/p/deleteVehicleClassPriceRate.action";
        var param = {"id": gridViewId};
        ajax.loadHtml2(url, param, function (data) {
            reloadGridViewList();
        });
    }

    //
    // Upload New CSV for Rate
    //
    var uploadRateStore = new Ext.data.ArrayStore({
        // store configs
        autoDestroy: true,
        storeId: 'uploadRateStore',
        // reader configs
        idIndex: 5,
        // http://cdn.sencha.com/ext/gpl/3.4.1.1/docs/#!/api/Date
        fields: [
            'Class',
            {name: 'Rate'},
            {name: 'StartDate', type: 'date', dateFormat: 'j/n/Y'},
            'Age',
            'Row'
        ]
    });

    var uploadRateGrid = new Ext.grid.GridPanel({
        store: uploadRateStore,
        columns: [{
            header: 'Row', width: 50, dataIndex: 'Row', sortable: true, renderer: function (value, p, r) {
                return "<span id='supplier_rates_" + value + "'>" + value + "</span>";
            }
        }, {
            header: 'Class', dataIndex: 'Class', sortable: true, resizable: true
        }, {
            header: 'Rate', dataIndex: 'Rate', sortable: true, resizable: true,
            renderer: function(value,p,r) {
                if (value.charAt(0) == 'Â' && value.charAt(1) == '£')
                    return value.slice(2);
                else if (value.charAt(0) == '£')
                    return value.slice(1);
                else
                    return value;
            }
        }, {
            header: 'Start Date',
            dataIndex: 'StartDate',
            sortable: true,
            resizable: true,
            renderer: Ext.util.Format.dateRenderer('j/n/Y')
        }, {
            header: 'Age', dataIndex: 'Age', sortable: true, resizable: true
        }],
        enableHdMenu: false,
        enableColumnMove: false,
        layout: 'fit',
        viewConfig: {forceFit: true},
        height: 500,
        width: 765,
    });

    var fileInputSupplierRates = document.getElementById("uploadRateFile");
    fileInputSupplierRates.addEventListener('change', readCsvFile);
    var uploadSupplierRatesBtn = document.getElementById('uploadSupplierRatesBtn');
    var csvContentGTARates;

    function readCsvFile() {
        if (!fileInputSupplierRates.value || !fileInputSupplierRates.value.endsWith('.csv')) {
            return;
        }

        var reader = new FileReader();
        reader.onload = function (e) {
            csvContentGTARates = parseCsv(reader.result);
            uploadRateStore.loadData(csvContentGTARates);
            uploadRateGrid.render('gridviewUploadRateHolder');
            uploadSupplierRatesBtn.disabled = false;
        };
        // start reading the file. When it is done, calls the onload event defined above.
        reader.readAsBinaryString(fileInputSupplierRates.files[0]);
    };

    function parseCsv(csvString) {
        var result = [];

        var lines = csvString.split("\n");
        // ignore the first column
        for (var i = 1; i < lines.length; i++) {
            var line = lines[i];
            if (line.length > 0) {
                var rates = line.trim().split(",");
                rates.push(i);  // add an indexes for error display
                result.push(rates);
            }
        }
        // console.table(result);
        return result;
    }

    function uploadCsv() {
        if (!csvContentGTARates || !csvContentGTARates.length) {
            return;
        }
        var gtaRates = [];
        for (var i = 0; i < csvContentGTARates.length; i++) {
            var line = csvContentGTARates[i];
            gtaRates.push(line.join(','));
        }

        var url = document.getElementById('uploadRateForm').getAttribute('action');
        var param = {"csvContent": gtaRates.join(';')};
        ajax.loadHtml2(url, param, function (data) {
            var result = data.split(':');
            fileInputSupplierRates.value = '';
            uploadSupplierRatesBtn.disabled = true;
            // uploadRateStore.loadData('');
            document.getElementById('uploadSupplierRatesMessage').innerHTML = result[0];
            if (result[1]) {
                var ids = result[1].trim().split(',');
                for (var i = 0; i < ids.length; i++) {
                    var id = ids[i].trim();
                    if (id) {
                        var idElement = document.getElementById('supplier_rates_' + id);
                        if (idElement) {
                            idElement.closest('table').style.backgroundColor = 'red';
                        }
                    }
                }
            }

            loadGridViewList();
        });
    }
</script>

<div id="chox-admin-holder">
    <div id="chox-admin-col-div" style="width:780px">
        <div id="gtaRatePanel"></div>
    </div>

    <div id="existingRateTab" class="x-hide-display" style="background-color:#DFE8F6;height:100%;">
        <div class="chox-form-submit-result">&nbsp;</div>
        <div id="gridviewGridHolderId"></div>
    </div>

    <div id="newRateTab" class="x-hide-display" style="background-color:#DFE8F6;height:100%;padding:10px 5px;">
        <form id="uploadRateForm" name="uploadRateForm"
              action="<%= request.getContextPath()%>/prv/p/uploadGTARates.action" method="POST"
              enctype="multipart/form-data">
            <div class="form-container">
                <fieldset class="x-fieldset">
                    <table class="chox-form-item" cellpadding="0" cellspacing="0" border="0" width="100%">
                        <tr>
                            <td width="200" align="right">
                                <label class="std-label-ro">File &nbsp;</label>
                            </td>
                            <td>
                                <div id="uploadRateWrapper">
                                    <input id="uploadRateFile" name="uploadRateFile" type="file" size="20"
                                           autocomplete="off"
                                           accept=".csv"
                                           class="x-form-file x-form-field" title="Upload Supplier Rates CSV File."
                                           style="width: 200px;"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                            </td>
                            <td>
                                <div class="column-remark" style="padding:10px 0 10px 0;">
                                    <input id="uploadSupplierRatesBtn" disabled='disabled' type="button" value="Confirm"
                                           onclick="uploadCsv();"/>
                                    &nbsp; Upload CSV file with maximum size of 2 MB.
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td>
                                <div class="action-error-msg"><b id="uploadSupplierRatesMessage"></b></div>
                            </td>
                        </tr>
                    </table>
                </fieldset>
                <div id="gridviewUploadRateHolder"></div>
            </div>
        </form>
    </div>
</div>
