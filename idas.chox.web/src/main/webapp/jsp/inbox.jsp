<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<head>
    <title>IDAS-CHOX</title>
    <script src="<%= request.getContextPath()%>/scripts/activityMonitor.js" type="text/javascript"></script>

    <script type="text/javascript">

        var currentTabIndex;
        var tabs;
        var recordPerPage = 20;

        Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
        Ext.QuickTips.init();

        Ext.onReady(function(){
            setupTabPanels();
            setupGrid();
            var pingServerUrl = '<%=request.getContextPath()%>/prv/p/activityMonitoringAction.action';
            var checkStatusIUrl = '<%=request.getContextPath()%>/prv/p/checkViewingStatus.action';
            //activityMonitor.setup(pingServerUrl,checkStatusIUrl);
            loadDataFromSession();
        });

        var rd = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            idProperty: 'threadid',
            remoteSort: true,
            fields:[
                {name:'id'},
                {name:'status'},
                {name:'createdBy'},
                {name:'invoiceAmount'},
                {name:'vehicleRegistration'},
                {name:'policyNumber'},
                {name:'workgroup'},
                {name:'supplierReference'},
                {name:'claimNumber'},
                {name:'lastModifiedDate', type: 'string', dateFormat:'timestamp'},
                {name:'reviewDate', type: 'string', dateFormat:'timestamp'},
                {name:'insurer'},
                {name:'cho'},
                {name:'isWorkgroupEditable', type:'boolean'},
                {name:'isOwnershipEditable', type:'boolean'},
                {name:'ownerName'}
            ]
        });

        var ds = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/doSearchClaim.action',method:'POST'}),
            autoLoad:false,
            reader:rd,
            remoteSort: true
        });

        ds.addEvents('beforeload');

        ds.on('beforeload',function(scope,options){
            Ext.state.Manager.set("grid_start", options.params.start);
            Ext.state.Manager.set("grid_limit", options.params.limit);
            Ext.state.Manager.set("grid_baseParams",scope.baseParams);
        });

        ds.setDefaultSort('created', 'desc');

        Ext.BLANK_IMAGE_URL = '<%= request.getContextPath()%>/images/default/s.gif';

        function executeFilter(filterName)
        {
            ds.baseParams = {"filterName" : filterName};
            doDataLoad(0, recordPerPage);
        }

        function refreshFilterPanel()
        {
            var url = "<%=request.getContextPath()%>/prv/p/getFilterRecordCounters.action";
            ajax.loadHtml(url, null, function(data){
                $("div#filterPanel").html(data);
            });
        }

        function searchClaim()
        {
            var supplierReference = Ext.query('*[name$=supplierReference]')[0].value;
            var supplierId = Ext.query('*[name$=supplierId]').length > 0 ? Ext.query('*[name$=supplierId]')[0].value : -1;
            var insurerId = Ext.query('*[name$=insurerId]').length > 0 ? Ext.query('*[name$=insurerId]')[0].value : -1;
            var invoiceNumber = Ext.query('*[name$=invoiceNumber]')[0].value;
            var claimNumber = Ext.query('*[name$=claimNumber]')[0].value;
            var thirdPartyVrn = Ext.query('*[name$=thirdPartyVrn]')[0].value;
            var claimUploadDateFrom = Ext.query('*[name$=claimUploadDateFrom]')[0].value;
            var claimUploadDateTo = Ext.query('*[name$=claimUploadDateTo]')[0].value;
            var invoiceUploadDateFrom = Ext.query('*[name$=invoiceUploadDateFrom]')[0].value;
            var invoiceUploadDateTo = Ext.query('*[name$=invoiceUploadDateTo]')[0].value;
            var hireDateFrom = Ext.query('*[name$=hireDateFrom]')[0].value;
            var hireDateTo = Ext.query('*[name$=hireDateTo]')[0].value;
            var status = Ext.query('*[name$=status]')[0].value;
            var workgroupId = Ext.query('*[name$=workgroup]')[0].value;
            var reviewRequiredDateFrom = Ext.query('*[name$=reviewRequiredDateFrom]')[0].value;
            var reviewRequiredDateTo = Ext.query('*[name$=reviewRequiredDateTo]')[0].value;
            var claimOwnerId = Ext.query('*[name$=searchClaimOwnerId]')[0].value;
            var customerVrn = Ext.query('*[name$=customerVrn]')[0].value;
            var isOpenClaim = Ext.query('*[name$=isOpenClaim]')[0].checked;

            ds.baseParams = {
                filterName : '',
                supplierReference : supplierReference,
                supplierId : supplierId,
                insurerId : insurerId,
                invoiceNumber : invoiceNumber,
                claimNumber : claimNumber,
                thirdPartyVrn : thirdPartyVrn,
                claimUploadDateFrom : claimUploadDateFrom,
                claimUploadDateTo : claimUploadDateTo,
                invoiceUploadDateFrom : invoiceUploadDateFrom,
                invoiceUploadDateTo : invoiceUploadDateTo,
                hireDateFrom : hireDateFrom,
                hireDateTo : hireDateTo,
                status : status,
                workgroupId: workgroupId,
                reviewRequiredDateFrom : reviewRequiredDateFrom,
                reviewRequiredDateTo : reviewRequiredDateTo,
                claimOwnerId : claimOwnerId,
                customerVrn : customerVrn,
                isOpenClaim : isOpenClaim
            }

            doDataLoad(0, recordPerPage);
        }

        function doDataLoad(start, recordPerPage){

            ds.load(
            {
                params:
                    {
                    start:start,
                    limit:recordPerPage
                },
                callback:function(){
                    if(!<s:property value="isChoxAdmin"/>){
                        // activityMonitor.refreshViewingStatus();
                    }
                }
            });
        }

        function loadDataFromSession()
        {
            if(<s:property value="showHistory"/>){

                var start = Ext.state.Manager.get("grid_start");
                var recordPerPage = Ext.state.Manager.get("grid_limit");
                var baseParams =  Ext.state.Manager.get("grid_baseParams");

                ds.baseParams = baseParams;
                ds.load(
                {
                    params:
                        {
                        start:start,
                        limit:recordPerPage
                    },
                    callback:function(){
                        if(!<s:property value="isChoxAdmin"/>){
                            //activityMonitor.refreshViewingStatus();
                        }
                    }
                });
            }
        }

        function setupGrid(){
            var sm2 = new Ext.grid.CheckboxSelectionModel();

            var pagingBar = new Ext.PagingToolbar({
                pageSize: recordPerPage,
                store: ds,
                displayInfo: true,
                displayMsg: 'Displaying claims {0} - {1} of {2}',
                emptyMsg: "No claim to display"
            });


            /**** BATCH UPDATE - ROUTE CLAIM ********************************/
            var claimRoutedSelectionDlg;
            var doClaimRoutedAction = new Ext.Action({
                text: 'Route Claim(s)',
                hidden:<s:property value="isCHO"/>,
                handler: function(){
                    if(!claimRoutedSelectionDlg)
                    {
                        claimRoutedSelectionDlg =  new Ext.Window({
                            applyTo:'claimRoutedSelectionDlgHolder',
                            width:410,
                            height:280,
                            modal: true,
                            closeAction:'hide',
                            plain: false,
                            title: 'Route Claim(s)',
                            resizable : false,
                            items: new Ext.Panel({
                                applyTo: 'claimRoutedSelectionPanel'
                            }),
                            buttons: [{
                                    text:'Ok',
                                    handler:function(){

                                        if($("form#routeClaimForm").valid()){

                                            var selectedRecords =  sm2.getSelections();
                                            var selectedIDs = $.map(selectedRecords, function(n){
                                                return n.json.id;
                                            });

                                            var param = selectedIDs.join(",");
                                            $('form#routeClaimForm input[name="selectedClaimIds"]').val(param);

                                            var submitOption = {
                                                clearForm: true,
                                                success:function(){
                                                    sm2.clearSelections();
                                                    ds.reload();
                                                    refreshFilterPanel();
                                                    claimRoutedSelectionDlg.hide();
                                                }};

                                            $("form#routeClaimForm").ajaxSubmit(submitOption);

                                        }
                                    }
                                },{
                                    text: 'Close',
                                    handler: function(){
                                        claimRoutedSelectionDlg.hide();
                                    }
                                }]
                        });

                        claimRoutedSelectionDlg.addListener('beforeshow', function(dialog){

                            $("form#routeClaimForm").validate(
                            {
                                errorLabelContainer: "#routeClaimFormMessageBox",
                                rules: {
                                    workgroupId:{required:true}
                                },
                                messages: {
                                    workgroupId:{required:"You must select 'Workgroup'"}
                                }
                            });

                            var target = "div#claimRoutedSelectionHolder";
                            var url = "<%=request.getContextPath()%>/prv/p/WorkgroupDropDownActionByInsurer.action";
                            ajax.loadHtml(url, null, function(data){
                                $(target).html(data);
                            });

                        });
                    }

                    claimRoutedSelectionDlg.show(this);
                }
            });

            /**** BATCH UPDATE - PAYMENT LOGGED ********************************/
            var approvedInvoicesPaymentAction = new Ext.Action
            ({
                text: 'Update Claim(s) To Invoice Payment Logged',
                hidden:<s:property value="isCHO"/>,
                handler: function(){

                    if(confirm('Are you sure you want to perform this action?'))
                    {
                        var selectedRecords =  sm2.getSelections();
                        var selectedNotMyClaimsIDs = getErrorClaims(selectedRecords, true, true, null);

                        if(selectedNotMyClaimsIDs.length<=0){

                            selectedRecords =  sm2.getSelections();
                            var selectedIDs = $.map(selectedRecords, function(n){
                                return n.json.id;
                            });

                            var param = selectedIDs.join(",");
                            var url = "<%= request.getContextPath()%>/prv/processBatchClaims.action";
                            var param = {"name":"invoicePaymentLogged","selectedClaimIds":param};
                            ajax.loadHtml(url, param, function(data){
                                refreshFilterPanel();
                                sm2.clearSelections();
                                ds.reload();
                            });
                        }
                    }
                }
            });

            /**** BATCH UPDATE - CLEAN FOR PAYMENT ********************************/
            var clearBREApprovedInvoicesForPaymentAction = new Ext.Action
            ({
                text: 'Approve Claim(s) For Payment',
                hidden:<s:property value="isCHO"/>,
                handler: function(){
                    if(confirm('Are you sure you want to perform this action?'))
                    {
                        var selectedRecords =  sm2.getSelections();
                        var selectedNotMyClaimsIDs = getErrorClaims(selectedRecords, true, true, null);
                        if(selectedNotMyClaimsIDs.length<=0){

                            selectedRecords =  sm2.getSelections();
                            var selectedIDs = $.map(selectedRecords, function(n){
                                return n.json.id;
                            });

                            var param = selectedIDs.join(",");
                            var url = "<%= request.getContextPath()%>/prv/processBatchClaims.action";
                            var param = {"name":"acceptInvoice","selectedClaimIds":param};
                            ajax.loadHtml(url, param, function(data){
                                refreshFilterPanel();
                                sm2.clearSelections();
                                ds.reload();
                            });
                        }
                    }
                }
            });

            /**** BATCH UPDATE - PAYMENT RECEIVED ********************************/
            var doInvoicePaymentReceivedAction = new Ext.Action
            ({
                text: 'Update Claim(s) To Payment Received',
                hidden:<s:property value="isInsurer"/>,
                handler: function(){
                    if(confirm('Are you sure you want to perform this action?'))
                    {
                        var selectedRecords =  sm2.getSelections();
                        var selectedNotMyClaimsIDs = getErrorClaims(selectedRecords, true, true, null);

                        if(selectedNotMyClaimsIDs.length<=0){

                            selectedRecords =  sm2.getSelections();
                            var selectedIDs = $.map(selectedRecords, function(n){
                                return n.json.id;
                            });

                            var param = selectedIDs.join(",");
                            var url = "<%= request.getContextPath()%>/prv/processBatchClaims.action";
                            var param = {"name":"invoicePaymentReceived","selectedClaimIds":param};
                            ajax.loadHtml(url, param, function(data){
                                refreshFilterPanel();
                                sm2.clearSelections();
                                ds.reload();
                            });
                        }
                    }
                }
            });

            /**** BATCH UPDATE - ASSIGN CLAIM OWNER ********************************/
            var claimOwnerSelectionDlg;
            var doClaimOwnerAction = new Ext.Action({
                text: 'Assign Claim(s) Owner',
                hidden:<s:property value="isCHO"/>,
                handler: function(){

                    var selectedRecords =  sm2.getSelections();
                    var selectedNotMyClaimsIDs = getErrorClaims(selectedRecords, true, false, null);

                    if(selectedNotMyClaimsIDs.length<=0){

                        if(!claimOwnerSelectionDlg)
                        {
                            claimOwnerSelectionDlg =  new Ext.Window({
                                applyTo:'claimOwnerSelectionDlgHolder',
                                width:410,
                                height:280,
                                modal: true,
                                closeAction:'hide',
                                plain: false,
                                title: 'Assign Claim(s) Owner',
                                resizable : false,
                                items: new Ext.Panel({
                                    applyTo: 'claimOwnerSelectionPanel'
                                }),
                                buttons: [{
                                        text:'Ok',
                                        handler:function(){

                                            if($("form#ownershipClaimForm").valid()){

                                                selectedRecords =  sm2.getSelections();
                                                var selectedIDs = $.map(selectedRecords, function(n){
                                                    return n.json.id;
                                                });

                                                var param = selectedIDs.join(",");

                                                $('form#ownershipClaimForm input[name="selectedClaimIds"]').val(param);


                                                var submitOption = {
                                                    clearForm: true,
                                                    success:function(){
                                                        sm2.clearSelections();
                                                        ds.reload();
                                                        refreshFilterPanel();
                                                        claimOwnerSelectionDlg.hide();
                                                    }};

                                                $("form#ownershipClaimForm").ajaxSubmit(submitOption);
                                            }
                                        }
                                    },{
                                        text: 'Close',
                                        handler: function(){
                                            claimOwnerSelectionDlg.hide();
                                        }
                                    }]
                            });

                            claimOwnerSelectionDlg.addListener('beforeshow', function(dialog){

                                // SETUP FOR VALIDATION
                                $("form#ownershipClaimForm").validate(
                                {
                                    errorLabelContainer: "#ownershipClaimFormMessageBox",
                                    rules: {
                                        oasWorkgroupId:{min:1},
                                        claimOwnerId:{min:1}
                                    },
                                    messages: {
                                        oasWorkgroupId:{min:"You must select 'Workgroup'"},
                                        claimOwnerId:{min:"You must select 'Claim Owner'"}
                                    }
                                });

                                // LOAD WORKGROUP AND CLAIM OWNER
                                var isInsurerWorkgroupEnable = false;
                                if($("#userInsurerWorkgroupEnable").val()!=null && $("#userInsurerWorkgroupEnable").val()!=""){
                                    isInsurerWorkgroupEnable = $("#userInsurerWorkgroupEnable").val();
                                }

                                var insurerId = $("#userInsurerId").val();

                                // GENERATE CLAIM OWNER
                                var target = "#claimOwnerClaimHandlerRoleUserDropDownDiv";
                                var url = "<%=request.getContextPath()%>/prv/p/ClaimHandlerRoleUserDropDownAction.action";
                                var param = {"workgroupId":-1,"insurerId":insurerId};

                                ajax.loadHtml(url,param,function(data){
                                    $(target).html(data);
                                    if(isInsurerWorkgroupEnable){
                                        generateWorkgroup();
                                    }
                                });

                                function generateWorkgroup(){
                                    var target = "#claimOwnerWorkgroupDropDownDiv";
                                    var url = "<%=request.getContextPath()%>/prv/p/WorkgroupDropDownActionByUser.action";
                                    var param = {};
                                    ajax.loadHtml(url, param, function(data){
                                        $(target).html(data);
                                    });
                                }
                            });
                        }
                        claimOwnerSelectionDlg.show(this);
                    }
                }
            });

            var updateClaimOwnershipSelectionDlg;
            var doUpdateClaimOwnerAction = new Ext.Action({

                text: 'Update Claim(s) Workgroup And Claim Owner',
                hidden:<s:property value="isCHO"/>,
                handler: function(){

                    var allowStatuses = ["AwaitingCarHireInfo", "AwaitingInvoiceData", "AwaitingInvoicePayment",
                        "ClaimPending", "ClaimReferredToEngineer", "ClaimRejected",
                        "ClaimRejectionContested", "ClaimUnacknowledgedRouted", "ClaimUpdatedByEngineer",
                        "ContestedInvoiceReferredToCHO", "ContestedInvoiceReferredToInsurer", "InvoiceApprovedByBRE",
                        "InvoiceDataCalculationIncorrect", "InvoiceEscalated", "InvoiceEscalatedToHandler",
                        "InvoicePaymentLogged", "PaymentReceived", "ClaimReferredToFNOL"];

                    var selectedRecords =  sm2.getSelections();
                    var selectedNotMyClaimsIDs = getErrorClaims(selectedRecords, false, false, allowStatuses);

                    if(selectedNotMyClaimsIDs.length<=0){

                        if(!updateClaimOwnershipSelectionDlg)
                        {
                            updateClaimOwnershipSelectionDlg =  new Ext.Window({
                                applyTo:'couSelectionDlgHolder',
                                width:410,
                                height:280,
                                modal: true,
                                closeAction:'hide',
                                plain: false,
                                title: 'Update Claim(s) Workgroup And Claim Owner',
                                resizable : false,
                                items: new Ext.Panel({
                                    applyTo: 'couSelectionPanel'
                                }),
                                buttons: [{
                                        text:'Ok',
                                        handler:function(){

                                            if($('form#ClaimOwnershipUpdateForm').valid()){

                                                selectedRecords =  sm2.getSelections();
                                                var selectedIDs = $.map(selectedRecords, function(n){
                                                    return n.json.id;
                                                });

                                                var param = selectedIDs.join(",");

                                                $('form#ClaimOwnershipUpdateForm input[name="selectedClaimIds"]').val(param);

                                                var submitOption = {
                                                    clearForm: true,
                                                    success:function(){
                                                        sm2.clearSelections();
                                                        ds.reload();
                                                        refreshFilterPanel();
                                                        updateClaimOwnershipSelectionDlg.hide();
                                                    }};

                                                $("form#ClaimOwnershipUpdateForm").ajaxSubmit(submitOption);
                                            }
                                        }
                                    },{
                                        text: 'Close',
                                        handler: function(){
                                            updateClaimOwnershipSelectionDlg.hide();
                                        }
                                    }]
                            });

                            updateClaimOwnershipSelectionDlg.addListener('beforeshow', function(dialog){

                                $("form#ClaimOwnershipUpdateForm").validate(
                                {
                                    rules: {
                                        workgroupId:{min:1},
                                        claimOwnerId:{min:1}
                                    },
                                    messages: {
                                        workgroupId:{min:"You must select 'Workgroup'"},
                                        claimOwnerId:{min:"You must select 'Claim Owner'"}
                                    }
                                });

                                var insurerId = $("#userInsurerId").val();
                                var isInsurerWorkgroupEnable = false;

                                if($("#userInsurerWorkgroupEnable").val()!=null && $("#userInsurerWorkgroupEnable").val()!=""){
                                    isInsurerWorkgroupEnable = $("#userInsurerWorkgroupEnable").val();
                                }

                                // GENERATE CLAIM OWNER
                                var target = "#couClaimHandlerRoleUserDropDownDiv";
                                var url = "<%=request.getContextPath()%>/prv/p/ClaimHandlerRoleUserDropDownAction.action";
                                var param = {"workgroupId":-1,"insurerId":insurerId};
                                ajax.loadHtml(url,param,function(data){
                                    $(target).html(data);
                                    if(isInsurerWorkgroupEnable){
                                        generateWorkgroup();
                                    }
                                });
                                function generateWorkgroup(){
                                    var target = "#couWorkgroupDropDownDiv";
                                    var url = "<%=request.getContextPath()%>/prv/p/UpdateWorkgroupDropDownActionByInsurer.action";
                                    var param = {};
                                    ajax.loadHtml(url, param, function(data){
                                        $(target).html(data);
                                    });
                                }
                            });
                        }
                        updateClaimOwnershipSelectionDlg.show(this);
                    }
                }
            });

            var actionMenu = new Ext.Toolbar.MenuButton({
                text: 'Batch Update',
                tooltip: {text:'', title:'More actions'},
                menu : {items: [
                        doClaimRoutedAction,
                        doClaimOwnerAction,
                        doUpdateClaimOwnerAction,
                        clearBREApprovedInvoicesForPaymentAction,
                        approvedInvoicesPaymentAction,
                        doInvoicePaymentReceivedAction
                    ]}
            });

            actionMenu.on('arrowclick', function()
            {
                doInvoicePaymentReceivedAction.disable();
                approvedInvoicesPaymentAction.disable();
                clearBREApprovedInvoicesForPaymentAction.disable();
                doClaimRoutedAction.disable();
                doClaimOwnerAction.disable();
                doUpdateClaimOwnerAction.disable();

                var selectedRecords = sm2.getSelections();
                var selectedIDs = $.map(selectedRecords, function(n){
                    return n.json.id;
                });

                if(selectedIDs.length>0){
                    var param = selectedIDs.join(",");
                    validateBatchUpdateAccessRight(doInvoicePaymentReceivedAction, "doInvoicePaymentReceived", param);
                    validateBatchUpdateAccessRight(approvedInvoicesPaymentAction, "logInvoicePayment", param);
                    validateBatchUpdateAccessRight(clearBREApprovedInvoicesForPaymentAction, "approveBREPassedClaim", param);
                    validateBatchUpdateAccessRight(doClaimRoutedAction, "routeClaims", param);
                    validateBatchUpdateAccessRight(doClaimOwnerAction, "claimOwnership", param);
                    validateBatchUpdateAccessRight(doUpdateClaimOwnerAction, "updateClaimWorkgroupAndOwner", param);
                }

            }, this);

            var grid = new Ext.grid.GridPanel({
                loadMask: true,
                ds: ds,
                width: 960,
                columns: [
                    sm2,
                    {id:'Id', header: "Supplier Ref", width: 180, sortable: true, dataIndex: 'supplierReference',
                        renderer:function(value,p,r){
                            return '<a href="<%=request.getContextPath()%>/prv/openClaimDetail.action?id=' + r.data['id'] + '&tab=' + currentTabIndex + '">' + value + '</a>'}},
                    {header: "Insurer's VRN", width: 180, sortable: true, dataIndex: 'vehicleRegistration'},
                    {header: "Insurer's Policy No", width: 200, sortable: true, dataIndex: 'policyNumber'},
                    {header: "Claim No", width: 220, sortable: true, dataIndex: 'claimNumber'},
                    {header: "Status", width: 400, sortable: true, dataIndex: 'status'},
                    {header: "Workgroup", width: 150, sortable: true, dataIndex: 'workgroup'},
                    {header: "CHO", width: 80, sortable: true, dataIndex: 'cho'},
                    {header: "Insurer", width: 80, sortable: true, dataIndex: 'insurer'},
                    {header: "Last Modified", width: 180, sortable: true, dataIndex: 'lastModifiedDate'},
                    {header: "Review Date", width: 180, sortable: true, dataIndex: 'reviewDate'},
                    {header: "Invoice Amount", width: 200, sortable: true, dataIndex: 'invoiceAmount', align: 'right'},
                    {header: "Owner", width: 100, sortable: true, dataIndex: 'ownerName'},
                    {header: "Viewing", width: 80, sortable: false, dataIndex: 'id',renderer:function(value,p,r){
                            return '<input type="hidden" name="viewingId" value="' + value + '" /><label id="viewingLabel_' + value + '" class="std-label-ro">-</label>'}}
                ],
                stateId:'chox_claim_grid',
                stateful:true,
                sm:sm2,
                stripeRows:true,
                layout:'fit',
                autoHeight:true,
                enableHdMenu:false,
                title:'Claims',
                viewConfig:{forceFit:true},
                bbar: pagingBar,
                tbar:[actionMenu]
            });
            grid.render('gridHolder');
            grid.getSelectionModel().selectFirstRow();
        }

        function validateBatchUpdateAccessRight(batchUpdateDlg, batchActionName, param){
            var url = '<%= request.getContextPath()%>/prv/p/checkBatchUpdateStatus.action';
            var param = {"batchUpdateAction":batchActionName, "selectedClaimIds":param};
            ajax.loadJson(url, param, function(data){
                if(data.resultType=='YesNo'){
                    if(data.result=='yes'){
                        batchUpdateDlg.enable();
                    }
                }
            });
        }

        function setupTabPanels()
        {
            currentTabIndex = <s:property value="tab" />;
            var selectedIndex = currentTabIndex;
            if(!<s:property value="menuAccessibility.isDashBoardMenuAccessibility"/>){
                selectedIndex++;
            }

            tabs = new Ext.TabPanel({
                renderTo: 'tabPanel',
                autoheight:true,
                activeTab: selectedIndex,
                items:[
                    {contentEl:'boardPanelTab', id:'boardPanelTabId', title:'Dashboard', listeners: {activate: handleActivate}},
                    {contentEl:'filterPanelTab', title:'Inbox', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/getFilterRecordCounters.action", scripts:true}},
                    {contentEl:'searchPanelTab', title:'Search', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/searchClaim.action", scripts:true}},
                    {contentEl:'reportPanelTab', id:'reportPanelTabId', title:'Reports', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/buildReport.action", scripts:true}},
                    {contentEl:'adminPanelTab', id:'adminPanelTabId', title:'Admin', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/adminFunction.action", scripts:true}}
                ]
            });

            if(!<s:property value="menuAccessibility.isDashBoardMenuAccessibility"/>){
                tabs.remove('boardPanelTabId', true);
            }

            if(!<s:property value="menuAccessibility.isReportMenuAccessibility"/>){
                tabs.remove('reportPanelTabId', true);
            }

            if(!<s:property value="menuAccessibility.isAdminMenuAccessibility"/>){
                tabs.remove('adminPanelTabId', true);
            }

        }

        function handleActivate(tab){

            $("#gridPanel").hide();
            if(tab.title == 'Inbox' || tab.title == 'Search'){
                $("#gridPanel").show();
            }

            if(tabs)
            {
                currentTabIndex = tabs.items.indexOf(tabs.getActiveTab());
            }
        }

        function getErrorClaims(selectedRecords, isWorkgroupCheck, isOwnershipCheck, allowedStatuses)
        {

            var selectedNotMyClaimsIDs = $.map(selectedRecords, function(n){

                var isWgValid = true;
                var isOwValid = true;
                var isAllowedStatusesValid = true

                if(isWorkgroupCheck && !n.json.isWorkgroupEditable){
                    isWgValid = false;
                }

                if(isOwnershipCheck && !n.json.isOwnershipEditable){
                    isOwValid = false;
                }

                if(allowedStatuses!=null){

                    isAllowedStatusesValid = false;

                    for ( var i=0; i<allowedStatuses.length; i++){
                        if((n.json.status).toLowerCase()==(allowedStatuses[i]).toLowerCase()){
                            isAllowedStatusesValid = true;
                            break;
                        }
                    }
                }

                if(!isWgValid || !isOwValid || !isAllowedStatusesValid){

                    var supplierRef = n.json.supplierReference + " - ";

                    var errorMsg = ""
                    if(!isWgValid || !isOwValid){
                        errorMsg += "Not authorised"
                    }

                    if(!isAllowedStatusesValid){
                        if(errorMsg.length>0){
                            errorMsg += " and "
                        }
                        errorMsg += "Incorrect Status"
                    }
                    return supplierRef + errorMsg + "<br/>";
                }

            });

            if(selectedNotMyClaimsIDs.length>0){
                propmtMsg("", "Please de-select the tick box for following claim(s) <br/>" + selectedNotMyClaimsIDs.join(" "));
            }

            return selectedNotMyClaimsIDs;
        }

    </script>

</head>

<div id="tabPanel"></div>

<div id="boardPanelTab" class="x-hide-display">
    <div id="boardPanel">
        <s:if test="isInsurer">
            <s:action name="showInsurerBoardHeader" namespace="/prv/p" executeResult="true" />
        </s:if>
        <s:if test="isCHO">
            <s:action name="showChoBoardHeader" namespace="/prv/p" executeResult="true" />
        </s:if>
    </div>
</div>

<div id="filterPanelTab" class="x-hide-display"></div>
<div id="searchPanelTab" class="x-hide-display"></div>
<div id="reportPanelTab" class="x-hide-display"></div>
<div id="adminPanelTab" class="x-hide-display"></div>

<div id="gridPanel">
    <div id="gridHolder"></div>
    <input id="userInsurerId" name="userInsurerId" value="<s:property value="AuthenticatedUser.insurer.id"/>" type="hidden"/>
    <input id="userInsurerWorkgroupEnable" name="userInsurerWorkgroupEnable" value="<s:property value="AuthenticatedUser.insurer.workgroupEnable"/>" type="hidden"/>

    <div class="excel-export">
        <form name="thisForm" action=""><a href="javascript:doExportExcel();">Export To Excel</a></form>
    </div>

    <div id="claimRoutedSelectionDlgHolder" class="x-hidden">
        <div id="claimRoutedSelectionPanel">
            <form id="routeClaimForm" action="<%=request.getContextPath()%>/prv/processBatchClaims.action" class="XXentity-form">
                <input name="selectedClaimIds" type="hidden" />
                <s:hidden id="name" name="name" value="assignWorkgroup"/>
                <table class="selection-form" cellspacing="0" cellpadding="0" border="0">
                    <tr>
                        <th colspan="2"><label>Please select the 'Workgroup' in order to route the claim(s) to the relevant handling team.</label></th>
                    </tr>
                    <tr>
                        <td><label>Workgroup</label></td>
                        <td><div id="claimRoutedSelectionHolder"></div></td>
                    </tr>
                    <tr>
                        <td colspan="2"><div id="routeClaimFormMessageBox" class="action-error-msg"/></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>

    <div id="claimOwnerSelectionDlgHolder" class="x-hidden">
        <div id="claimOwnerSelectionPanel">
            <form id="ownershipClaimForm" name="ownershipClaimForm" action="<%=request.getContextPath()%>/prv/processBatchClaims.action" class="XXentity-form">
                <input name="selectedClaimIds" type="hidden" />
                <s:hidden id="name" name="name" value="assignOwner"/>
                <table class="selection-form" cellspacing="0" cellpadding="0" border="0">
                    <tr>
                        <th colspan="2"><label>Please assign the claim(s) with a Claim Owner.</label></th>
                    </tr>
                    <s:if test="AuthenticatedUser.insurer.workgroupEnable">
                        <tr>
                            <td class="pop-claim-ownership-label"><label>Workgroup</label></td>
                            <td class="pop-claim-ownership-column"><div id="claimOwnerWorkgroupDropDownDiv"></div></td>
                        </tr>
                    </s:if>
                    <tr>
                        <td class="pop-claim-ownership-label" style="height:60px;"><label>Claim Owner</label></td>
                        <td class="pop-claim-ownership-column"><div id="claimOwnerClaimHandlerRoleUserDropDownDiv"></div></td>
                    </tr>
                    <tr>
                        <td colspan="2"><div id="ownershipClaimFormMessageBox" class="action-error-msg"/></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>

    <div id="couSelectionDlgHolder" class="x-hidden">
        <div id="couSelectionPanel">
            <form id="ClaimOwnershipUpdateForm" action="<%=request.getContextPath()%>/prv/p/doClaimOwnershipUpdateAction.action" class="XXentity-form">
                <input name="selectedClaimIds" type="hidden" />
                <table class="selection-form" cellspacing="0" cellpadding="0" border="0" width="100%">
                    <tr>
                        <th colspan="2"><label>Please update the claim(s) with a Workgroup and Claim Owner.</label></th>
                    </tr>
                    <s:if test="AuthenticatedUser.insurer.workgroupEnable">
                        <tr>
                            <td class="pop-claim-ownership-label"><label>Workgroup</label></td>
                            <td class="pop-claim-ownership-column"><div id="couWorkgroupDropDownDiv"></div></td>
                        </tr>
                    </s:if>
                    <tr>
                        <td class="pop-claim-ownership-label" style="height:60px;"><label>Claim Owner</label></td>
                        <td class="pop-claim-ownership-column"><div id="couClaimHandlerRoleUserDropDownDiv"></div></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>