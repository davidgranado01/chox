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
                {contentEl:'existingRateTab', title:'Supplier Rates', listeners: {activate: handleActivate}},
                {contentEl:'newRateTab', title:'Upload', listeners: {activate: handleActivate}},
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
                    {name:'insurerName'},
                    {name:'chorganisationName'},
                    {name:'vehicleClassName'},
                    {name:'rate'},
                    {name:'startDate'},
                    {name:'createdBy'},
                    {name:'createdDate'},
                    {name:'id'}
                ]
        });

        supplierChoxDataStore = new choxDataStore({
            url: '/prv/p/getVehicleClassSpecialRates.action',
            reader:supplierRateJsonReader,
            remoteSort: true
        });

        supplierChoxDataStore.setDefaultSort('startDate', 'desc');

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
                {header: "Class", width: 95, dataIndex: 'vehicleClassName', sortable: false, resizable: true},
                {header: "Rate", width: 44, dataIndex: 'rate', sortable: true, resizable: false},
                {header: "Start Date", width: 112, dataIndex: 'startDate', sortable: true, resizable: true},
                {header: "Created Date", width: 112, dataIndex: 'createdDate', sortable: true, resizable: true},
                {header: "Created By", width: 112, dataIndex: 'createdBy', sortable: false, resizable: true},
                {
                    header: "", width: 112, dataIndex: 'id', sortable: false, renderer: function (value, p, r) {
                        return "<a href='#' class='high-light-item'> Delete</a>";
                    }
                }
            ],
            height:570,
            width: 765,
            bbar: supplierRatesPagingBar
        });

        loadGridViewList();
        supplierRateGridPanel.render('gridviewGridHolderId');
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
    }

    function reloadGridViewList(){
        supplierChoxDataStore.reload();
    }


    function recordOnclick(grid, rowIndex, columnIndex, e){
        // Delete record here

        if(columnIndex===7){
            deleteRecord(grid, rowIndex, columnIndex, e);
        }
    }

    function deleteRecord(grid, rowIndex, columnIndex, e){

        var gridView = supplierRateGridPanel.getStore().getAt(rowIndex);
        var gridViewId = gridView.get("id");

        var url = "/prv/p/deleteVehicleClassSpecialRate.action";
        var param = {"id":gridViewId};
        ajax.loadHtml2(url,param,function(data){
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

    var uploadRateGrid = new Ext.grid.GridPanel({
        store: uploadRateStore,
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

    var fileInput = document.getElementById("uploadRateFile");
    fileInput.addEventListener('change', readCsvFile);

    var csvContent;
    function readCsvFile() {
        if (!fileInput.value || !fileInput.value.endsWith('.csv')) {
            return;
        }

        var reader = new FileReader();
        reader.onload = function (e) {
            csvContent = parseCsv(reader.result);
            uploadRateStore.loadData(csvContent);
            uploadRateGrid.render('gridviewUploadRateHolder');
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

    function uploadCsv() {
        if (!csvContent || !csvContent.length) {
            return;
        }
        var supplierRates = [];
        for (var i = 0; i < csvContent.length; i++) {
            var line = csvContent[i];
            supplierRates.push(line.join(','));
        }

        var url = document.getElementById('uploadRateForm').getAttribute('action');
        var param = { "csvContent": supplierRates.join(';') };
        ajax.loadHtml2(url, param, function(data) {
            fileInput.value = '';
            uploadRateStore.loadData('');
            loadGridViewList();
        });
    }
</script>

<div id="chox-admin-holder">
    <div id="chox-admin-col-div" style ="width:780px">
        <div id="supplierRatePanel"></div>
    </div>

    <div id="existingRateTab" class="x-hide-display" style="background-color:#DFE8F6;height:100%;">
        <div class="chox-form-submit-result">&nbsp;</div>
        <div id="gridviewGridHolderId"></div>
    </div>

    <div id="newRateTab" class="x-hide-display" style="background-color:#DFE8F6;height:100%;padding:10px 5px;">
        <form id="uploadRateForm" name="uploadRateForm" action="<%= request.getContextPath()%>/prv/p/uploadSupplierRates.action" method="POST" enctype="multipart/form-data">
            <div class="form-container">
                <fieldset class="x-fieldset">
                    <table class="chox-form-item" cellpadding="0" cellspacing="0" border="0" width="100%">
                        <tr>
                            <td width="200" align="right">
                                <label class="std-label-ro">File &nbsp;</label>
                            </td>
                            <td>
                                <div id="uploadRateWrapper">
                                    <input id="uploadRateFile" name="uploadRateFile" type="file" size="20" autocomplete="off"
                                           accept=".csv"
                                           class="x-form-file x-form-field" title="Upload Supplier Rates CSV File." style="width: 200px;" />
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                            </td>
                            <td>
                                <div class="column-remark" style="padding:10px 0 10px 0;">
                                    <input type="button" value="Confirm" onclick="uploadCsv();" />
                                    &nbsp; Upload CSV file with maximum size of 2 MB.
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td>
                                <s:if test="uploadFlag">
                                    <input id="uploadRateButton" type="submit" value="Upload Supplier Rates" />
                                </s:if>
                                <s:else><br/>
                                    <div class="action-error-msg"><b></b></div>
                                </s:else>
                            </td>
                        </tr>
                    </table>
                    <div class="chox-form-submit-result" id="uploadRateResultId"/>
                    <div class="action-error-msg" id="uploadRateMsgBox"/>
                </fieldset>
                <div id="gridviewUploadRateHolder"></div>
            </div>
        </form>
    </div>
</div>