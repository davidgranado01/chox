<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<head>
    <title>CHOX</title>
    <script src="<%= request.getContextPath()%>/scripts/activityMonitor.js" type="text/javascript"></script>
    <script src="<%= request.getContextPath()%>/scripts/ProgressBarPager.js" type="text/javascript"></script>
    <script src="<%= request.getContextPath()%>/scripts/RowEditor.js" type="text/javascript"></script>
    <script type="text/javascript">

        var defaultDropdownValue={'value':-1,'text':'--- ALL ---'};
        var pagingBar;
        var currentTabIndex;
        var tabs;
        var recordPerPage = 20;
        var isShowHistory = <s:property value="showHistory"/>;
        var isInboxShowHistory;
        var isSearchShowHistory;
        var currentOrg;
        var grid;
        var ds;
        var exportIntervelId;

        Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

        Ext.onReady(function(){
            Ext.QuickTips.init();
            loadDataFromSession();
            setupGrid();
            setupTabPanels();
            grid.render('gridHolder');
            var pingServerUrl = '<%=request.getContextPath()%>/prv/p/activityMonitoringAction.action';
            var checkStatusIUrl = '<%=request.getContextPath()%>/prv/p/checkViewingStatus.action';
            activityMonitor.setup(pingServerUrl, checkStatusIUrl);

        <s:if test="isCHO" > 
                document.getElementById('queueOrgFilter').innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;Insurer Filter : &nbsp;&nbsp;';

                var insurersJsonReader = new Ext.data.JsonReader({
                    totalProperty: 'totalCount',
                    root: 'results',
                    fields:
                        [
                        {name:'text'},
                        {name: 'value'}
                    ]
                });

                var myinsurers = Ext.util.JSON.decode('<s:property value="insurersJsonString" escape="false"/>');
                var insurersStore = new Ext.data.Store({
                    data : myinsurers,
                    reader : insurersJsonReader,
                    listeners: {load: function() {this.insert(0, new Ext.data.Record(defaultDropdownValue));}}
                });


                var insurerFilterCombo = new Ext.form.ComboBox({
                    store : insurersStore,
                    //                    renderTo: 'orgFilterDiv',
                    id:'filterOrgId',
                    autoHeight: true,
                    autoWidth: false,
                    width: 180,
                    listWidth: 180,
                    valueField : 'value',
                    displayField :'text',
                    typeAhead : true,
                    mode : 'local',
                    triggerAction : 'all',
                    valueNotFoundText : '--- ALL ---',
                    selectOnFocus : true,
                    listeners: {
                        select: reloadQueues,
                        blur: function () {
                            if(this.getRawValue() == "" ) {
                                this.clearValue();
                                reloadQueues();
                            }
                        }
                    }
                });
                
                insurerFilterCombo.render('orgFilterDiv');
                insurerFilterCombo.setValue(-1);              
        </s:if>

        <s:elseif test="isInsurer" > 
                document.getElementById('queueOrgFilter').innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;CHO Filter : &nbsp;&nbsp;';
            
                var suppliersJsonReader = new Ext.data.JsonReader({
                    totalProperty: 'totalCount',
                    root: 'results',
                    fields:
                        [
                        {name:'text'},
                        {name:'value'}
                    ]
                });

                var mysuppliers = Ext.util.JSON.decode('<s:property value="suppliersJsonString" escape="false"/>');
                var suppliersStore = new Ext.data.Store({
                    data : mysuppliers,
                    reader : suppliersJsonReader,
                    listeners: {load: function() {this.insert(0, new Ext.data.Record(defaultDropdownValue));}}
                });

                var supplierFilterCombo = new Ext.form.ComboBox({
                    store : suppliersStore,
                    //                renderTo: 'orgFilterDiv',
                    id:'filterOrgId',
                    width: 180,
                    listWidth: 180,
                    valueField : 'value',
                    displayField :'text',
                    typeAhead : true,
                    mode : 'local',
                    triggerAction : 'all',
                    valueNotFoundText : '--- ALL ---',
                    selectOnFocus : true,
                    listeners: {
                        select: reloadQueues,
                        blur: function () {
                            if(this.getRawValue() == "" ) {
                                this.clearValue();
                                reloadQueues();
                            }
                        }
                    }
                });
                supplierFilterCombo.render('orgFilterDiv');
                supplierFilterCombo.setValue(-1);    
        </s:elseif>
                //        else
                //            document.getElementById('queueOrgFilter').innerHTML  = '';

        <s:if test="showSplash" >
                onShowBrowserWarning();
        </s:if>

            });

            var rd = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                //            idProperty: 'threadid',
                fields:[
                    {name:'id'},
                    {name:'status'},
                    {name:'createdBy'},
                    {name:'invoiceAmount'},
                    {name:'invoiceUploadDate'},
                    {name:'policyNumber'},
                    {name:'workgroup'},
                    {name:'supplierReference'},
                    {name:'claimNumber'},
                    {name:'claimType'},
                    {name:'statusModifiedDate', type: 'string', dateFormat:'timestamp'},
                    {name:'reviewDate', type: 'string', dateFormat:'timestamp'},
                    {name:'insurer'},
                    {name:'cho'},
                    {name:'isWorkgroupEditable', type:'boolean'},
                    {name:'isOwnershipEditable', type:'boolean'},
                    {name:'ownerName'},
                    {name:'choOwnerName'}
                ]
            });

            ds = new Ext.data.Store({
                proxy: new Ext.data.HttpProxy
                ({url: '<%= request.getContextPath()%>/prv/p/doSearchClaim.action',method:'POST'}),
                autoLoad:false,
                reader:rd,
                remoteSort: true,
                listeners:{beforeload:function(scope,options){

                        if(tabs){
                            if(tabs.getActiveTab().title == 'Inbox'){
                                Ext.state.Manager.set("inbox_grid_start", options.params.start);
                                Ext.state.Manager.set("inbox_grid_limit", options.params.limit);
                            }else if(tabs.getActiveTab().title == 'Search'){
                                Ext.state.Manager.set("search_grid_start", options.params.start);
                                Ext.state.Manager.set("search_grid_limit", options.params.limit);
                            }
                        }
                    },
                	load:function(){
                		//we check if  grid title is already set in that case we dont need to set it again
                		//- this check only kicks in user preses on inbox list.
              			if(ds !== undefined 
              					&& grid.title != undefined && grid.title != "" 
              					&& grid.title.indexOf(ds.getTotalCount()) == -1)
              			 	grid.setTitle(Ext.state.Manager.get("grid_main_title") +" ("+ds.getTotalCount()+")");
              		}
                }
            });


            ds.setDefaultSort('created', 'desc');

            Ext.BLANK_IMAGE_URL = '<%= request.getContextPath()%>/images/default/s.gif';

            function executeFilterByOrg(filterName,gridTitle, orgId) {
                Ext.state.Manager.set("grid_isInboxShowHistory",true);
                isInboxShowHistory = true;
                Ext.state.Manager.set("grid_filterName",filterName);
                Ext.state.Manager.set("grid_title","Queue: "+gridTitle);
                ds.baseParams = {"filterName" : filterName, "filterOrgId" : orgId, searchHistory : true};
                doDataLoad(0, recordPerPage,Ext.state.Manager.get("grid_title"));

            }

            function refreshFilterPanel() {
                var url = "<%=request.getContextPath()%>/prv/p/getFilterRecordCounters.action";
                var param = {"filterOrgId":currentOrg};
                ajax.loadHtml2(url, param, function(data){
                    $("div#filterPanel2").html(data);
                });
            }

            function refreshFilterPanelByOrg(filterName, title, orgId) {
                var url = "<%=request.getContextPath()%>/prv/p/getFilterRecordCounters.action";
                var param = {"filterOrgId":orgId};
                ajax.loadHtml2(url, param, function(data){
                    $("div#filterPanel2").html(data);
                });
                if (filterName)
                    executeFilterByOrg(filterName, title, orgId);
                currentOrg = orgId;
            }

            function searchClaim(canSearchForData){

                var supplierReference = Ext.query('*[name$=supplierReference]')[0].value;
                //            var supplierId = Ext.query('*[name$=supplierId]').length > 0 ? Ext.query('*[name$=supplierId]')[0].value : -1;
                var supplierId = -1;
                if (Ext.getCmp('searchScreenSupplierComboId'))
                    supplierId = Ext.getCmp('searchScreenSupplierComboId').getValue();
                if (supplierId==='') {
                    supplierId=-1;
                }
                //           var insurerId = Ext.query('*[name$=insurerId]').length > 0 ? Ext.query('*[name$=insurerId]')[0].value : -1;
                var insurerId = -1;
                if (Ext.getCmp('searchScreenInsurerComboId'))
                    insurerId = Ext.getCmp('searchScreenInsurerComboId').getValue();
                if (insurerId==='') {
                    insurerId=-1;
                }
                var invoiceNumber = Ext.query('*[name$=invoiceNumber]')[0].value;
                var claimNumber = Ext.query('*[name$=claimNumber]')[0].value;
                var thirdPartyVrn = Ext.query('*[name$=thirdPartyVrn]')[0].value;
                var claimUploadDateFrom = Ext.query('*[name$=claimUploadDateFrom]')[0].value;
                var claimUploadDateTo = Ext.query('*[name$=claimUploadDateTo]')[0].value;

                var statusModifiedDateFrom = Ext.query('*[name$=statusModifiedDateFrom]')[0].value;
                var statusModifiedDateTo = Ext.query('*[name$=statusModifiedDateTo]')[0].value;

                var invoiceUploadDateFrom = Ext.query('*[name$=invoiceUploadDateFrom]')[0].value;
                var invoiceUploadDateTo = Ext.query('*[name$=invoiceUploadDateTo]')[0].value;
                var hireDateFrom = Ext.query('*[name$=hireDateFrom]')[0].value;
                var hireDateTo = Ext.query('*[name$=hireDateTo]')[0].value;
                //            var status = Ext.query('*[name$=status]')[0].value;
                var status = Ext.getCmp('statusSearchScreenComboId').getValue();
                //            var workgroupId = Ext.query('*[name$=workgroup]')[0].value;
                var workgroupId = -1;
                if (Ext.getCmp('searchScreenWorkgroupComboId'))
                    workgroupId = Ext.getCmp('searchScreenWorkgroupComboId').getValue();
                if (workgroupId==='') {
                    workgroupId=-1;
                }
                var reviewRequiredDateFrom = Ext.query('*[name$=reviewRequiredDateFrom]')[0].value;
                var reviewRequiredDateTo = Ext.query('*[name$=reviewRequiredDateTo]')[0].value;
                //           var claimOwnerId = Ext.query('*[name$=searchClaimOwnerId]')[0].value;
                var claimOwnerId = -1;
                if (Ext.getCmp('searchScreenClaimOwnerComboId'))
                    claimOwnerId = Ext.getCmp('searchScreenClaimOwnerComboId').getValue();
                if (claimOwnerId==='') {
                    claimOwnerId=-1;
                }
                var supplierClaimOwnerId = -1;
                if (Ext.getCmp('searchScreenSupplierClaimOwnerComboId'))
                    supplierClaimOwnerId = Ext.getCmp('searchScreenSupplierClaimOwnerComboId').getValue();
                if (supplierClaimOwnerId==='') {
                    supplierClaimOwnerId=-1;
                }
                var customerVrn = Ext.query('*[name$=customerVrn]')[0].value;
                var isOpenClaim = Ext.query('*[name$=isOpenClaim]')[0].checked;
                var isSupplementaryInvoiceOnly = Ext.query('*[name$=isSupplementaryInvoiceOnly]')[0].checked;
                var penaltyChargesAppliedOnly = Ext.query('*[name$=penaltyChargesAppliedOnly]')[0].checked;
                var liabilityStatus = Ext.getCmp('liabilityStatusSearchScreenComboId').getValue();
                var claimType = Ext.getCmp('claimTypesSearchScreenComboId').getValue();

                ds.baseParams = {
                    /*
                     *  if canSearchForData is false then no data will be returned. this is mainly used to reset the search screen form.
                     */
                    canLoadData : canSearchForData,
                    searchHistory : true,
                    filterName : '',
                    supplierReference : supplierReference,
                    supplierId : supplierId,
                    insurerId : insurerId,
                    invoiceNumber : invoiceNumber,
                    claimNumber : claimNumber,
                    thirdPartyVrn : thirdPartyVrn,
                    claimUploadDateFrom : claimUploadDateFrom,
                    claimUploadDateTo : claimUploadDateTo,
                    statusModifiedDateFrom : statusModifiedDateFrom,
                    statusModifiedDateTo :  statusModifiedDateTo,
                    invoiceUploadDateFrom : invoiceUploadDateFrom,
                    invoiceUploadDateTo : invoiceUploadDateTo,
                    hireDateFrom : hireDateFrom,
                    hireDateTo : hireDateTo,
                    status : status,
                    workgroupId: workgroupId,
                    reviewRequiredDateFrom : reviewRequiredDateFrom,
                    reviewRequiredDateTo : reviewRequiredDateTo,
                    claimOwnerId : claimOwnerId,
                    supplierClaimOwnerId : supplierClaimOwnerId,
                    customerVrn : customerVrn,
                    isOpenClaim : isOpenClaim,
                    penaltyChargesAppliedOnly : penaltyChargesAppliedOnly,
                    liabilityStatus : liabilityStatus,
                    claimType : claimType,
                    isSupplementaryInvoiceOnly : isSupplementaryInvoiceOnly
                }
                if(canSearchForData){
                    Ext.state.Manager.set("grid_baseParams",ds.baseParams);
                    Ext.state.Manager.set("grid_isSearchShowHistory",true);
                    isSearchShowHistory = true;
                    doDataLoad(0, recordPerPage,"Search Result");
                }else{
                    Ext.state.Manager.set("grid_baseParams",null);
                    Ext.state.Manager.set("grid_isSearchShowHistory",false);
                    isSearchShowHistory = false;
                    doDataLoad(0, 0,"Claims");
                }

            }

            function doDataLoad(start, recordPerPage, titleMessage)
            {
                grid.setTitle(" ")
                ds.load(
                {
                    params:{start:start, limit:recordPerPage},
                    callback:function(){
                    	Ext.state.Manager.set("grid_main_title", titleMessage);
                        grid.setTitle(titleMessage+" ("+ds.getTotalCount()+")");
                    }
                });
            }

            function loadDataFromSession() {

        <s:if test="searchHistory!=true">
                Ext.state.Manager.set("grid_baseParams",null);
                Ext.state.Manager.set("grid_filterName",null);
                Ext.state.Manager.set("grid_isSearchShowHistory",false);
                Ext.state.Manager.set("grid_isInboxShowHistory",false);
                Ext.state.Manager.set("inbox_grid_start", 0);
                Ext.state.Manager.set("inbox_grid_limit", 0);
                Ext.state.Manager.set("search_grid_start", 0);
                Ext.state.Manager.set("search_grid_limit", 0);
                isInboxShowHistory = false;
                isSearchShowHistory = false;
        </s:if><s:else >
                isInboxShowHistory = Ext.state.Manager.get("grid_isInboxShowHistory");
                isSearchShowHistory = Ext.state.Manager.get("grid_isSearchShowHistory");
        </s:else>
            }

            function setupGrid(){
                var sm2 = new Ext.grid.CheckboxSelectionModel();

                pagingBar = new Ext.PagingToolbar({
                    pageSize: recordPerPage,
                    store: ds,
                    displayInfo: true,
                    displayMsg: 'Displaying claims {0} - {1} of {2}',
                    emptyMsg: "No claims to display"
                    ,plugins: new Ext.ux.ProgressBarPager()
                });


                /**** BATCH UPDATE - ROUTE CLAIM ********************************/
                var claimRoutedSelectionDlg;
                var doClaimRoutedAction = new Ext.Action({
                    text: 'Route Claim(s)',
                    hidden:<s:property value="isCHO"/>,
                    handler: function(){

                        if(!claimRoutedSelectionDlg)
                        {
                            var workgroupJsonReader = new Ext.data.JsonReader({
                                totalProperty: 'totalCount',
                                root: 'results',
                                fields:
                                    [
                                    {name:'text'},
                                    {name:'value'}
                                ]
                            });

                            var workgroupStore = new Ext.data.Store({
                                proxy : new Ext.data.HttpProxy
                                ({url : "<%= request.getContextPath()%>/prv/p/WorkgroupDropDownActionByInsurer.action", method:'GET', params:{claimId : sm2.getSelected().get('id')}}),
                                reader : workgroupJsonReader
                            });

                            var workgroupCombo = new Ext.form.ComboBox({
                                store: workgroupStore,
                                width: 220,
                                renderTo: 'claimRoutedSelectionHolder',
                                valueField: 'text',
                                id: 'workgroupId',
                                displayField:'value',
                                typeAhead: true,
                                autoWidth: true,
                                fieldLabel: 'Workgroup',
                                mode: 'local',
                                triggerAction: 'all',
                                emptyText: '--- Please Select ---',
                                //                                selectOnFocus: true,
                                forceSelection: true,
                                //                                allowBlank: false
                                listeners: {blur: function () {
                                        if(this.getRawValue() == "" ) {
                                            this.clearValue(); this.reset();
                                        }
                                    }}
                            });

                            // Add a validator to validate that a workgroup is selected.
                            // This is needed (since the switch to using extjs combobox for the workgroups)
                            // as the validation is performed against the displayed string rather than the workgroupID.
                            //                      console.log("Adding validator method.");
                            $.validator.addMethod("workgroupSelected",
                            function(value) {
                                //console.log("Validating: " + value);
                                if(value === "--- Please Select ---") {
                                    //                                   console.log("Returning false for value:" + value);
                                    return false;
                                }
                                //                                console.log("Returning true for value:" + value);
                                return true;
                            }, "You must select a 'Workgroup'");

                            claimRoutedSelectionDlg =  new Ext.Window({
                                applyTo:'claimRoutedSelectionDlgHolder',
                                layout:'fit',
                                width:410,
                                height:280,
                                closeAction:'hide',
                                plain: false,
                                modal: true,
                                title: 'Route Claim(s)',
                                resizable : false,
                                items: new Ext.Panel({
                                    applyTo: 'claimRoutedSelectionPanel'
                                }),
                                buttons: [{
                                        text:'Ok',
                                        handler:function(){
                                            //console.log("Ok clicked - validating");

                                            if($("form#routeClaimForm").valid()){
                                                //console.log("Passed validation...");
                                                var selectedRecords =  sm2.getSelections();
                                                var selectedIDs = $.map(selectedRecords, function(n){
                                                    return n.json.id;
                                                });
                                                var idsParam = selectedIDs.join(",");
                                                $('form#routeClaimForm input[name="selectedClaimIds"]').val(idsParam);

                                                var submitOption = {
                                                    clearForm: true,
                                                    beforeSubmit: function(formData, form, options) {
                                                        formData[1].value = workgroupCombo.getValue();
                                                    },
                                                    success:function(){
                                                        sm2.clearSelections();
                                                        workgroupCombo.reset();
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
                                            // hide the error message box, which could be displayed,
                                            // so that it doesn't appear when we're opened again
                                            $("#routeClaimFormMessageBox").hide();
                                            claimRoutedSelectionDlg.hide();
                                        }
                                    }]
                            });

                            //                        console.log("Adding 'beforeshow' listener");
                            claimRoutedSelectionDlg.addListener('beforeshow', function(dialog){
                                $("form#routeClaimForm").validate(
                                {
                                    rules: {
                                        // specify our validator (added above)
                                        workgroupId: {workgroupSelected: document.getElementById('workgroupId')}
                                    },
                                    messages: {
                                        workgroupId:{workgroupSelected:"You must select a 'Workgroup'."}
                                    },
                                    // send any error messages to our message container
                                    // (would default to the combo box otherwise)
                                    //                                errorContainer: '#routeClaimFormMessageBox',
                                    errorLabelContainer: '#routeClaimFormMessageBox'
                                });
                                //                            console.log("Loading store.");
                                workgroupStore.load({ params : {claimId : sm2.getSelected().get('id')}});
                                //                            console.log("Resetting combo");
                                workgroupCombo.reset();

                                //                            var target = "div#claimRoutedSelectionHolder";
                                //                            var url = "<%=request.getContextPath()%>/prv/p/WorkgroupDropDownActionByInsurer.action";
                                //                            ajax.loadHtml2(url, null, function(data){
                                //                                $(target).html(data);
                                //                            });

                            });
                        }

                        // claimRoutedSelectionDlg.show(this);
                        var selectedRecords =  sm2.getSelections();
                        var selectedIDs = $.map(selectedRecords, function(n){
                            return n.json.id;
                        });
                        var idsParam = selectedIDs.join(",");
                        validateSelectedClaimsDialog("routeClaims", idsParam, claimRoutedSelectionDlg);

                    }
                });

                /**** BATCH UPDATE - PAYMENT LOGGED ********************************/
                var approvedInvoicesPaymentAction = new Ext.Action
                ({
                    text: 'Update Claim(s) To Invoice Payment Logged',
                    hidden:<s:property value="isCHO"/>,
                    handler: function(){

                        var selectedRecords =  sm2.getSelections();
                        selectedRecords =  sm2.getSelections();
                        var selectedIDs = $.map(selectedRecords, function(n){
                            return n.json.id;
                        });
                        var idsParam = selectedIDs.join(",");

                        function processClaims(){

                            Ext.MessageBox.confirm('Confirm', 'Are you sure you want to perform this action?',function(btn){
                                if(btn=='yes')
                                {
                                    var url = "<%= request.getContextPath()%>/prv/processBatchClaims.action";
                                    var param = {"name":"invoicePaymentLogged","selectedClaimIds":idsParam};
                                    ajax.loadHtml2(url, param, function(data){
                                        refreshFilterPanel();
                                        sm2.clearSelections();
                                        ds.reload();
                                    });
                                 }
                            });
                        }

                        validateSelectedClaims("logInvoicePayment", idsParam, processClaims);
                    }
                });

                /**** BATCH UPDATE - CLEAN FOR PAYMENT ********************************/
                var clearBREApprovedInvoicesForPaymentAction = new Ext.Action
                ({
                    text: 'Approve Claim(s) For Payment',
                    hidden:<s:property value="isCHO"/>,
                    handler: function(){

                        var selectedRecords =  sm2.getSelections();
                        selectedRecords =  sm2.getSelections();
                        var selectedIDs = $.map(selectedRecords, function(n){
                            return n.json.id;
                        });
                        var idsParam = selectedIDs.join(",");

                        function processClaims(){
                            
                          Ext.MessageBox.confirm('Confirm', 'Are you sure you want to perform this action?',function(btn){
                            if(btn=='yes')
                            {
                                var url = "<%= request.getContextPath()%>/prv/processBatchClaims.action";
                                var param = {"name":"acceptInvoice","selectedClaimIds":idsParam};
                                ajax.loadHtml2(url, param, function(data){
                                    refreshFilterPanel();
                                    sm2.clearSelections();
                                    ds.reload();
                                });
                            }
                            
                            });
                        }

                        validateSelectedClaims("approveBREPassedClaim", idsParam, processClaims);
                    }
                });

                /**** BATCH UPDATE - PAYMENT RECEIVED ********************************/
                var doInvoicePaymentReceivedAction = new Ext.Action
                ({
                    text: 'Update Claim(s) To Payment Received',
                    hidden:<s:property value="isInsurer"/>,
                    handler: function(){
                        Ext.MessageBox.confirm('Confirm', 'Are you sure you want to perform this action?',function(btn){
                        if(btn=='yes')
                        {
                            var selectedRecords =  sm2.getSelections();
                            var selectedIDs = $.map(selectedRecords, function(n){
                                return n.json.id;
                            });

                            var idsParam = selectedIDs.join(",");
                            var url = "<%= request.getContextPath()%>/prv/processBatchClaims.action";
                            var param = {"name":"invoicePaymentReceived","selectedClaimIds":idsParam};
                            ajax.loadHtml2(url, param, function(data){
                                refreshFilterPanel();
                                sm2.clearSelections();
                                ds.reload();
                            });
                        }
                        });
                    }
                });

                /**** BATCH UPDATE - ASSIGN SUPPLIER CLAIM OWNER ********************************/
                var supplierClaimOwnerSelectionDlg;
                var isHidden = <s:property value="isInsurer"/> || (<s:property value="isCHO"/> && !<s:property value="choIsClaimOwnershipEnabled"/>);
                var doSupplierClaimOwnerAction = new Ext.Action
                ({
                    text: 'Assign Claim(s) Owner',
                    hidden: isHidden,
                    handler: function(){
                        if(!supplierClaimOwnerSelectionDlg)
                        {
                            var supplierClaimOwnerReader = new Ext.data.JsonReader({
                                totalProperty: 'totalCount',
                                root: 'results',
                                fields:
                                    [
                                    {name:'id'},
                                    {name:'name'}
                                ]
                            });

                            var supplierClaimOwnerStore = new Ext.data.Store({
                                proxy : new Ext.data.HttpProxy
                                ({url : "<%= request.getContextPath()%>/prv/p/SearchSupplierClaimOwnerDropDownAction.action", method:'GET', params : {"supplierId":-1}}),
                                reader : supplierClaimOwnerReader
                            });

                            var supplierClaimOwnerCombo = new Ext.form.ComboBox({
                                store : supplierClaimOwnerStore,
                                width: 220,
                                renderTo: 'supplierClaimOwnerDropDownDiv',
                                valueField : 'id',
                                id : 'supplierClaimOwnerId',
                                triggerAction: 'all',
                                displayField :'name',
                                typeAhead : true,
                                forceSelection: true,
                                mode : 'local',
                                emptyText : '--- Please Select ---',
                                listeners: { blur: function () {
                                        if(this.getRawValue() == "" ) {
                                            this.clearValue(); this.reset();
                                        }
                                    }}
                            });

                            supplierClaimOwnerSelectionDlg =  new Ext.Window({
                                applyTo:'supplierClaimOwnerSelectionDlgHolder',
                                layout:'fit',
                                width:410,
                                height:280,
                                modal: true,
                                closeAction:'hide',
                                plain: false,
                                title: 'Assign Claim(s) Owner',
                                resizable : false,
                                items: new Ext.Panel({
                                    applyTo: 'supplierClaimOwnerSelectionPanel'
                                }),
                                buttons: [{
                                        text:'Ok',
                                        handler:function(){

                                            if($("form#supplierOwnershipClaimForm").valid()){
                                                var selectedRecords =  sm2.getSelections();
                                                var selectedIDs = $.map(selectedRecords, function(n){
                                                    return n.json.id;
                                                });

                                                var idsParam = selectedIDs.join(",");
                                                $('form#supplierOwnershipClaimForm input[name="selectedClaimIds"]').val(idsParam);

                                                var submitOption = {
                                                    clearForm: true,
                                                    beforeSubmit: function(formData, form, options) {
                                                        formData[1].value = supplierClaimOwnerCombo.getValue();
                                                    },
                                                    success:function(){
                                                        sm2.clearSelections();
                                                        supplierClaimOwnerCombo.reset();
                                                        ds.reload();
                                                        refreshFilterPanel();
                                                        supplierClaimOwnerSelectionDlg.hide();
                                                    }
                                                };
                                                $("form#supplierOwnershipClaimForm").ajaxSubmit(submitOption);
                                            }

                                        }
                                    },{
                                        text: 'Close',
                                        handler: function(){
                                            // hide the error message box, which could be displayed,
                                            // so that it doesn't appear when we're opened again
                                            $("#supplierOwnershipClaimFormMessageBox").hide();
                                            supplierClaimOwnerSelectionDlg.hide();
                                        }
                                    }]
                            });
                            $.validator.addMethod("itemSelected",
                            function(value) {
                                if(value === "--- Please Select ---") {
                                    return false;
                                }
                                return true;
                            }, "You must select a 'Claim Owner'");

                            supplierClaimOwnerSelectionDlg.addListener('beforeshow', function(dialog){

                                $("form#supplierOwnershipClaimForm").validate(
                                {
                                    errorLabelContainer: "#supplierOwnershipClaimFormMessageBox",
                                    rules: {
                                        // specify our validator (added above)
                                        supplierClaimOwnerId: {itemSelected: document.getElementById('supplierClaimOwnerId')}
                                    },
                                    messages: {
                                        supplierClaimOwnerId: {itemSelected:"You must select a 'Claim Owner'."}
                                    }
                                });

                                // LOAD CLAIM OWNER
                                var supplierId = $("#userSupplierId").val();

                                // GENERATE CLAIM OWNER
                                supplierClaimOwnerStore.load({ params : {"supplierId":supplierId}});
                                supplierClaimOwnerCombo.reset();

                            });
                        }

                        // claimOwnerSelectionDlg.show(this);
                        var selectedRecords =  sm2.getSelections();
                        var selectedIDs = $.map(selectedRecords, function(n){ return n.json.id; });
                        var idsParam = selectedIDs.join(",");
                        validateSelectedClaimsDialog("supplierClaimOwnership", idsParam, supplierClaimOwnerSelectionDlg);
                    }
                });

                /**** BATCH UPDATE - ASSIGN CLAIM OWNER ********************************/


                var claimOwnerSelectionDlg;
                var doClaimOwnerAction = new Ext.Action({
                    text: 'Assign Claim(s) Owner',
                    hidden: <s:property value="isCHO"/> || (<s:property value="isInsurer"/> && !<s:property value="insurerIsWorkgroupEnabled"/>),
                    handler: function(){

                        if(!claimOwnerSelectionDlg)
                        {
                            var workgroupStore = -1;
                            var workgroupCombo = -1;
                            var isInsurerWorkgroupEnable = false;

                            if($("#userInsurerWorkgroupEnable").val()!=null && $("#userInsurerWorkgroupEnable").val()!=""){
                                isInsurerWorkgroupEnable = $("#userInsurerWorkgroupEnable").val();
                            }

                            var claimOwnerReader = new Ext.data.JsonReader({
                                totalProperty: 'totalCount',
                                root: 'results',
                                fields:
                                    [
                                    {name:'id'},
                                    {name:'name'}
                                ]
                            });

                            var claimOwnerStore = new Ext.data.Store({
                                proxy : new Ext.data.HttpProxy
                                ({url : "<%= request.getContextPath()%>/prv/p/SearchClaimHandlerRoleUserDropDownAction.action", method:'GET', params : {"workgroupId":-1,"insurerId":-1}}),
                                reader : claimOwnerReader
                            });

                            var claimOwnerCombo = new Ext.form.ComboBox({
                                store : claimOwnerStore,
                                width: 220,
                                renderTo: 'claimOwnerClaimHandlerRoleUserDropDownDiv',
                                valueField : 'id',
                                id : 'claimOwnerId',
                                displayField :'name',
                                typeAhead : true,
                                forceSelection: true,
                                //                            fieldLabel: 'Claim Owner',
                                mode : 'local',
                                triggerAction : 'all',
                                emptyText : '--- Please Select ---',
                                //                            selectOnFocus : true,
                                //                            allowBlank : true,
                                listeners: { blur: function () {
                                        if(this.getRawValue() == "" ) {
                                            this.clearValue(); this.reset();
                                        }
                                    }}
                            });


                            if(isInsurerWorkgroupEnable){

                                var workgroupJsonReader = new Ext.data.JsonReader({
                                    totalProperty: 'totalCount',
                                    root: 'results',
                                    fields:
                                        [
                                        {name:'text'},
                                        {name:'value'}
                                    ]
                                });

                                workgroupStore = new Ext.data.Store({
                                    proxy : new Ext.data.HttpProxy
                                    ({url : "<%= request.getContextPath()%>/prv/p/WorkgroupDropDownActionByInsurer2.action", method:'GET', params : {"insurerId":-1}}),
                                    reader : workgroupJsonReader
                                });

                                workgroupCombo = new Ext.form.ComboBox({
                                    store: workgroupStore,
                                    width: 220,
                                    renderTo: 'claimOwnerWorkgroupDropDownDiv',
                                    valueField: 'text',
                                    id: 'oasWorkgroupId',
                                    displayField:'value',
                                    //                                fieldLabel: 'Workgroup',
                                    typeAhead: true,
                                    mode: 'local',
                                    triggerAction: 'all',
                                    emptyText: '--- Please Select ---',
                                    //                                selectOnFocus: true,
                                    forceSelection: true,
                                    //                                allowBlank: false
                                    listeners: {select: function () {
                                            var workgroupId = -1;
                                            if (workgroupCombo.getValue() != null) {
                                                workgroupId = workgroupCombo.getValue();
                                            }
                                            var insurerId = $("#userInsurerId").val();
                                            claimOwnerCombo.reset();
                                            claimOwnerStore.removeAll();
                                            claimOwnerStore.load({ params : {"workgroupId":workgroupId,"insurerId":insurerId}});
                                        },
                                        blur: function () {
                                            if(this.getRawValue() == "" ) {
                                                this.clearValue(); this.reset();
                                                var insurerId = $("#userInsurerId").val();
                                                claimOwnerCombo.reset();
                                                claimOwnerStore.load({ params : {"workgroupId":-1,"insurerId":insurerId}});
                                            }
                                        }}
                                });

                                // Add a validator to validate that a workgroup is selected.
                                // This is needed (since the switch to using extjs combobox for the workgroups)
                                // as the validation is performed against the displayed string rather than the workgroupID.
                                $.validator.addMethod("itemSelected",
                                function(value) {
                                    if(value === "--- Please Select ---") {
                                        return false;
                                    }
                                    return true;
                                }, "You must select a 'Workgroup'");
                            }


                            claimOwnerSelectionDlg =  new Ext.Window({
                                applyTo:'claimOwnerSelectionDlgHolder',
                                layout:'fit',
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


                                                var selectedRecords =  sm2.getSelections();
                                                var selectedIDs = $.map(selectedRecords, function(n){
                                                    return n.json.id;
                                                });
                                                var idsParam = selectedIDs.join(",");
                                                $('form#ownershipClaimForm input[name="selectedClaimIds"]').val(idsParam);

                                                var submitOption = {
                                                    clearForm: true,
                                                    beforeSubmit: function(formData, form, options) {
                                                        if(isInsurerWorkgroupEnable) {
                                                            formData[1].value = workgroupCombo.getValue();
                                                            formData[2].value = claimOwnerCombo.getValue();
                                                        }
                                                        else {
                                                            // This needs checking - could be [1] or [2] ?
                                                            //                                                        console.log("Changing value (for claim owner) '" + formData[1].value + "' to :" + claimOwnerCombo.getValue());
                                                            formData[1].value = claimOwnerCombo.getValue();
                                                        }
                                                    },
                                                    success:function(){
                                                        sm2.clearSelections();
                                                        if(isInsurerWorkgroupEnable){
                                                            workgroupCombo.reset();
                                                        }
                                                        claimOwnerCombo.reset();
                                                        ds.reload();
                                                        refreshFilterPanel();
                                                        claimOwnerSelectionDlg.hide();
                                                    }
                                                };

                                                $("form#ownershipClaimForm").ajaxSubmit(submitOption);

                                            }

                                        }
                                    },{
                                        text: 'Close',
                                        handler: function(){
                                            // hide the error message box, which could be displayed,
                                            // so that it doesn't appear when we're opened again
                                            $("#ownershipClaimFormMessageBox").hide();
                                            claimOwnerSelectionDlg.hide();
                                        }
                                    }]
                            });

                            claimOwnerSelectionDlg.addListener('beforeshow', function(dialog){

                                $("form#ownershipClaimForm").validate(
                                {
                                    errorLabelContainer: "#ownershipClaimFormMessageBox",
                                    rules: {
                                        // specify our validator (added above)
                                        oasWorkgroupId: {itemSelected: document.getElementById('oasWorkgroupId')},
                                        claimOwnerId: {itemSelected: document.getElementById('claimOwnerId')}
                                    },
                                    messages: {
                                        oasWorkgroupId: {itemSelected:"You must select a 'Workgroup'."},
                                        claimOwnerId: {itemSelected:"You must select a 'Claim Owner'."}
                                    }
                                });

                                // LOAD WORKGROUP AND CLAIM OWNER
                                var insurerId = $("#userInsurerId").val();
                                if (isInsurerWorkgroupEnable){
                                    workgroupStore.load({ params : {"insurerId":insurerId}});
                                    workgroupCombo.reset();
                                }

                                // GENERATE CLAIM OWNER
                                claimOwnerStore.load({ params : {"workgroupId":-1,"insurerId":insurerId}});
                                claimOwnerCombo.reset();

                            });
                        }

                        // claimOwnerSelectionDlg.show(this);
                        var selectedRecords =  sm2.getSelections();
                        var selectedIDs = $.map(selectedRecords, function(n){ return n.json.id; });
                        var idsParam = selectedIDs.join(",");
                        validateSelectedClaimsDialog("claimOwnership", idsParam, claimOwnerSelectionDlg);
                    }
                });



                /////////////////////////////////////////////////////////////////////////////////////////////////////////
                /////////////////// Batch Assign claim(s) owner for insurer without workgroup enabled//////////////////////
                /////////////////////////////////////////////////////////////////////////////////////////////////////////


                var insurerClaimOwnerSelectionDlg;
                var doInsurerClaimOwnerAction = new Ext.Action({
                    text: 'Assign Claim(s) Owner',
                    hidden:(<s:property value="isCHO"/> || (<s:property value="isInsurer"/> && <s:property value="insurerIsWorkgroupEnabled"/> && !<s:property value="insurerIsClaimOwnershipEnabled"/>) || (<s:property value="isInsurer"/> && !<s:property value="insurerIsWorkgroupEnabled"/> && !<s:property value="insurerIsClaimOwnershipEnabled"/>) || (<s:property value="isInsurer"/> && <s:property value="insurerIsWorkgroupEnabled"/> && <s:property value="insurerIsClaimOwnershipEnabled"/>)),
                    handler: function(){

                        if(!insurerClaimOwnerSelectionDlg)
                        {

                            var claimOwnerReader = new Ext.data.JsonReader({
                                totalProperty: 'totalCount',
                                root: 'results',
                                fields:
                                    [
                                    {name:'id'},
                                    {name:'name'}
                                ]
                            });

                            var claimOwnerStore = new Ext.data.Store({
                                proxy : new Ext.data.HttpProxy
                                ({url : "<%= request.getContextPath()%>/prv/p/SearchClaimHandlerRoleUserDropDownAction.action", method:'GET', params : {"workgroupId":-1,"insurerId":-1}}),
                                reader : claimOwnerReader
                            });

                            var claimOwnerCombo = new Ext.form.ComboBox({
                                store : claimOwnerStore,
                                width: 220,
                                renderTo: 'claimOwnerClaimHandlerRoleUserDropDownDiv',
                                valueField : 'id',
                                id : 'claimOwnerId',
                                displayField :'name',
                                typeAhead : true,
                                forceSelection: true,
                                //                            fieldLabel: 'Claim Owner',
                                mode : 'local',
                                triggerAction : 'all',
                                emptyText : '--- Please Select ---',
                                //                            selectOnFocus : true,
                                //                            allowBlank : true,
                                listeners: { blur: function () {
                                        if(this.getRawValue() == "" ) {
                                            this.clearValue(); this.reset();
                                        }
                                    }}
                            });

                            insurerClaimOwnerSelectionDlg =  new Ext.Window({
                                applyTo:'claimOwnerSelectionDlgHolder',
                                layout:'fit',
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
                                            if(document.getElementById('claimOwnerId').value === "--- Please Select ---"){
                                                Ext.Msg.alert("","please select Claim Owner");
                                            }else if(document.getElementById('claimOwnerId').value!=''){
                                                var selectedRecords =  sm2.getSelections();
                                                var selectedIDs = $.map(selectedRecords, function(n){
                                                    return n.json.id;
                                                });
                                                var idsParam = selectedIDs.join(",");
                                                $('form#ownershipClaimForm input[name="selectedClaimIds"]').val(idsParam);

                                                var submitOption = {
                                                    clearForm: true,
                                                    beforeSubmit: function(formData, form, options) {

                                                        formData[1].value = claimOwnerCombo.getValue();

                                                    },
                                                    success:function(){
                                                        sm2.clearSelections();
                                                        claimOwnerCombo.reset();
                                                        ds.reload();
                                                        refreshFilterPanel();
                                                        insurerClaimOwnerSelectionDlg.hide();
                                                    }
                                                };

                                                $("form#ownershipClaimForm").ajaxSubmit(submitOption);
                                            }
                                        }
                                    },{
                                        text: 'Close',
                                        handler: function(){
                                            // hide the error message box, which could be displayed,
                                            // so that it doesn't appear when we're opened again
                                            $("#ownershipClaimFormMessageBox").hide();
                                            insurerClaimOwnerSelectionDlg.hide();
                                        }
                                    }]
                            });

                            insurerClaimOwnerSelectionDlg.addListener('beforeshow', function(dialog){




                                // LOAD WORKGROUP AND CLAIM OWNER
                                var insurerId = $("#userInsurerId").val();
                                // GENERATE CLAIM OWNER
                                claimOwnerStore.load({ params : {"workgroupId":-1,"insurerId":insurerId}});
                                claimOwnerCombo.reset();

                            });
                        }

                        // insurerClaimOwnerSelectionDlg.show(this);
                        var selectedRecords =  sm2.getSelections();
                        var selectedIDs = $.map(selectedRecords, function(n){ return n.json.id; });
                        var idsParam = selectedIDs.join(",");
                        validateSelectedClaimsDialog("insurerClaimOwnership", idsParam, insurerClaimOwnerSelectionDlg);
                    }
                });



                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                //                           BATCH UPDATE CLAIM(S) OWNER WHERE WORKGROUP IS NOT ENABLED BUT CLAIMOWNERSHIP             ////////
                ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



                var updateInsurerClaimOwnershipSelectionDlg;
                var doUpdateInsurerClaimOwnerAction = new Ext.Action({text: 'Update Claim(s) Owner',
                    hidden: (<s:property value="isCHO"/> || (<s:property value="isInsurer"/> && <s:property value="insurerIsWorkgroupEnabled"/> && !<s:property value="insurerIsClaimOwnershipEnabled"/>) || (<s:property value="isInsurer"/> && !<s:property value="insurerIsWorkgroupEnabled"/> && !<s:property value="insurerIsClaimOwnershipEnabled"/>) || (<s:property value="isInsurer"/> && <s:property value="insurerIsWorkgroupEnabled"/> && <s:property value="insurerIsClaimOwnershipEnabled"/>)),
                    handler: function(){

                        if(!updateInsurerClaimOwnershipSelectionDlg)
                        {
                            updateInsurerClaimOwnershipSelectionDlg = new Ext.Window({
                                applyTo:'couSelectionDlgHolder',
                                layout:'fit',
                                width:410,
                                height:280,
                                modal: true,
                                closeAction:'hide',
                                plain:false,
                                title: 'Update Claim(s) Owner',
                                resizable : false,
                                items: new Ext.Panel({applyTo: 'couSelectionPanel'}),
                                buttons: [{
                                        text:'Ok',
                                        handler:function(){

                                            if($('form#ClaimOwnershipUpdateForm').valid()){

                                                var selectedRecords =  sm2.getSelections();
                                                var selectedIDs = $.map(selectedRecords, function(n){ return n.json.id; });
                                                var idsParam = selectedIDs.join(",");
                                                $('form#ClaimOwnershipUpdateForm input[name="selectedClaimIds"]').val(idsParam);

                                                var submitOption = {
                                                    clearForm: true,
                                                    success:function(){
                                                        sm2.clearSelections();
                                                        ds.reload();
                                                        refreshFilterPanel();
                                                        updateInsurerClaimOwnershipSelectionDlg.hide();
                                                    }
                                                };

                                                $("form#ClaimOwnershipUpdateForm").ajaxSubmit(submitOption);

                                            }
                                        }
                                    },{
                                        text: 'Close',
                                        handler: function(){
                                            updateInsurerClaimOwnershipSelectionDlg.hide();
                                        }
                                    }]
                            });

                            updateInsurerClaimOwnershipSelectionDlg.addListener('beforeshow', function(dialog){

                                $("form#ClaimOwnershipUpdateForm").validate(
                                {
                                    rules: {

                                        claimOwnerId:{min:1}
                                    },
                                    messages: {

                                        claimOwnerId:{min:"You must select 'Claim Owner'"}
                                    }
                                });

                                var insurerId = $("#userInsurerId").val();


                                // GENERATE CLAIM OWNER
                                var target = "#couClaimHandlerRoleUserDropDownDiv";
                                var url = "<%=request.getContextPath()%>/prv/p/ClaimHandlerRoleUserDropDownAction.action";
                                var param = {"workgroupId":-1,"insurerId":insurerId};
                                ajax.loadHtml2(url,param,function(data){
                                    $(target).html(data);

                                });



                            });
                        }

                        var selectedRecords =  sm2.getSelections();
                        var selectedIDs = $.map(selectedRecords, function(n){
                            return n.json.id;
                        });
                        var idsParam = selectedIDs.join(",");
                        validateSelectedClaimsDialog("updateInsurerClaimOwner", idsParam, updateInsurerClaimOwnershipSelectionDlg);
                    }
                });






                // ***********************************************************************************************************************************
                // ***********************************************************************************************************************************

                var updateClaimOwnershipSelectionDlg;
                var doUpdateClaimOwnerAction = new Ext.Action({text: 'Update Claim(s) Workgroup And Claim Owner',
                    hidden: (<s:property value="isCHO"/> || (<s:property value="isInsurer"/> && !<s:property value="insurerIsClaimOwnershipEnabled"/> && <s:property value="insurerIsWorkgroupEnabled"/>) || (<s:property value="isInsurer"/> && !<s:property value="insurerIsWorkgroupEnabled"/> && <s:property value="insurerIsClaimOwnershipEnabled"/>)),
                    handler: function(){

                        if(!updateClaimOwnershipSelectionDlg)
                        {
                            updateClaimOwnershipSelectionDlg = new Ext.Window({
                                applyTo:'couSelectionDlgHolder',
                                layout:'fit',
                                width:410,
                                height:280,
                                modal: true,
                                closeAction:'hide',
                                plain:false,
                                title: 'Update Claim(s) Workgroup And Claim Owner',
                                resizable : false,
                                items: new Ext.Panel({applyTo: 'couSelectionPanel'}),
                                buttons: [{
                                        text:'Ok',
                                        handler:function(){

                                            if($('form#ClaimOwnershipUpdateForm').valid()){

                                                var selectedRecords =  sm2.getSelections();
                                                var selectedIDs = $.map(selectedRecords, function(n){ return n.json.id; });
                                                var idsParam = selectedIDs.join(",");
                                                $('form#ClaimOwnershipUpdateForm input[name="selectedClaimIds"]').val(idsParam);

                                                var submitOption = {
                                                    clearForm: true,
                                                    success:function(){
                                                        sm2.clearSelections();
                                                        ds.reload();
                                                        refreshFilterPanel();
                                                        updateClaimOwnershipSelectionDlg.hide();
                                                    }
                                                };

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
                                ajax.loadHtml2(url,param,function(data){
                                    $(target).html(data);
                                    if(isInsurerWorkgroupEnable){
                                        generateWorkgroup();
                                    }
                                });

                                function generateWorkgroup(){
                                    var target = "#couWorkgroupDropDownDiv";
                                    var url = "<%=request.getContextPath()%>/prv/p/UpdateWorkgroupDropDownActionByInsurer.action";
                                    var param = {};
                                    ajax.loadHtml2(url, param, function(data){
                                        $(target).html(data);
                                    });
                                }

                            });
                        }

                        var selectedRecords =  sm2.getSelections();
                        var selectedIDs = $.map(selectedRecords, function(n){
                            return n.json.id;
                        });
                        var idsParam = selectedIDs.join(",");
                        validateSelectedClaimsDialog("updateClaimWorkgroupAndOwner", idsParam, updateClaimOwnershipSelectionDlg);
                    }
                });

                var actionMenu = new Ext.SplitButton({
                    text: 'Batch Update',
                    tooltip: {text:'', title:'More actions'},
                    menu : {items: [
                            doClaimRoutedAction,
                            doInsurerClaimOwnerAction,
                            doClaimOwnerAction,
                            doSupplierClaimOwnerAction,
                            doUpdateClaimOwnerAction,
                            doUpdateInsurerClaimOwnerAction,
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
                    doInsurerClaimOwnerAction.disable();
                    doClaimOwnerAction.disable();
                    doSupplierClaimOwnerAction.disable();
                    doUpdateClaimOwnerAction.disable();
                    doUpdateInsurerClaimOwnerAction.disable();

                    var selectedRecords = sm2.getSelections();
                    var selectedIDs = $.map(selectedRecords, function(n){
                        return n.json.id;
                    });

                    if(selectedIDs.length>0){
                        var idsParam = selectedIDs.join(",");
                        validateBatchUpdateAccessRight(doInvoicePaymentReceivedAction, "doInvoicePaymentReceived", idsParam);
                        validateBatchUpdateAccessRight(approvedInvoicesPaymentAction, "logInvoicePayment", idsParam);
                        validateBatchUpdateAccessRight(clearBREApprovedInvoicesForPaymentAction, "approveBREPassedClaim", idsParam);
                        validateBatchUpdateAccessRight(doClaimRoutedAction, "routeClaims", idsParam);
                        validateBatchUpdateAccessRight(doInsurerClaimOwnerAction, "insurerClaimOwnership", idsParam);
                        validateBatchUpdateAccessRight(doClaimOwnerAction, "claimOwnership", idsParam);
                        validateBatchUpdateAccessRight(doSupplierClaimOwnerAction, "supplierClaimOwnership", idsParam);
                        validateBatchUpdateAccessRight(doUpdateClaimOwnerAction, "updateClaimWorkgroupAndOwner", idsParam);
                        validateBatchUpdateAccessRight(doUpdateInsurerClaimOwnerAction, "updateInsurerClaimOwner", idsParam);
                    }

                }, this);

                grid = new Ext.grid.GridPanel({
                    id : 'inboxClaimsGridId',
                    loadMask: true,
                    ds: ds,
                    listeners:  {cellclick: maskInboxScreen },
                    width: 1000,
                    columns: [
                        sm2,
                        {header: "Supplier Ref", width: 180, sortable: true, dataIndex: 'supplierReference',
                            renderer:function(value,p,r){
                                return '<a href="<%=request.getContextPath()%>/prv/openClaimDetail.action?id=' + r.data['id'] + '&tab=' + currentTabIndex + '"><u>' + value + '</u></a>'}},
                        {header: "Claim Type", width: 120, sortable: true, dataIndex: 'claimType'},
                        {header: "Claim No", width: 80, sortable: true, dataIndex: 'claimNumber'},
                        {header: "Insurer's Policy No", width: 90, sortable: true, dataIndex: 'policyNumber'},
                        {header: "Invoice Upload Date", width: 90, sortable: true, dataIndex: 'invoiceUploadDate'},
                        {header: "Status", width: 120, sortable: true, dataIndex: 'status'},
                        {header: "Total To Pay", width: 200, sortable: true, dataIndex: 'invoiceAmount', align: 'right'},
                        {header: "Workgroup", width: 100, sortable: true,hidden: (<s:property value="isInsurer"/> && !<s:property value="insurerIsWorkgroupEnabled"/>) , dataIndex: 'workgroup'},
                        {header: "Ins Owner", width: 90, sortable: true,hidden: (<s:property value="isInsurer"/> && !<s:property value="insurerIsClaimOwnershipEnabled"/> ), dataIndex: 'ownerName'},
                        {header: "CHO Owner", width: 90, sortable: true,hidden: (<s:property value="isCHO"/> && !<s:property value="choIsClaimOwnershipEnabled"/>), dataIndex: 'choOwnerName'},
                        {header: "Status Modified Date", width: 90, sortable: true, dataIndex: 'statusModifiedDate'},
                        {header: "Review Date", width: 90, sortable: true, dataIndex: 'reviewDate'},
                        {header: "CHO", width: 100, sortable: true, dataIndex: 'cho'},
                        {header: "Insurer", width: 100, sortable: true, dataIndex: 'insurer'},
                        {header: "Viewing", width: 60, sortable: false, dataIndex: 'id',renderer:function(value,p,r){
                                return '<input type="hidden" name="viewingId" value="' + value + '" /><label id="viewingLabel_' + value + '">-</label>'}}
                    ],
                    stateId:'chox_claim_grid',
                    stateful:true,
                    sm:sm2,
                    stripeRows:true,
                    layout:'fit',
                    autoHeight:true,
                    enableHdMenu:false,
                    title:' ',
                    viewConfig:{forceFit:true},
                    bbar: pagingBar,
                    tbar:[actionMenu]
                });
                //            grid.render('gridHolder');
            }

            function maskInboxScreen(grid, rowIndex, columnIndex){
                if(columnIndex == 1){
                    var record = grid.getStore().getAt(rowIndex);
                    Ext.get('inboxScreenDiv').mask("loading claim details ...");
                    /*
                     *  this is extra call to load claim details page. this will be called when column no one is clicked not the hiberlink. This make sure the page is not only masked but also loading claim details page.
                     */
                    window.location = '<%=request.getContextPath()%>/prv/openClaimDetail.action?id='+record.get('id')+ '&tab=' + currentTabIndex ;
                }
            }

            function validateBatchUpdateAccessRight(batchUpdateDlg, batchActionName, param){
                var url = '<%= request.getContextPath()%>/prv/p/checkBatchUpdateStatus.action';
                var param = {"batchUpdateAction":batchActionName, "selectedClaimIds":param};
                ajax.loadJson2(url, param, function(data){
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

        <s:if test="IsComUser || IsScrUser">

                tabs = new Ext.TabPanel({
                    renderTo: 'tabPanel',
                    autoheight:true,
                    activeTab: selectedIndex,
                    items:[
                        {contentEl:'filterPanelTab', title:'Inbox', listeners: {activate: handleActivate}},
                        {contentEl:'searchPanelTab', title:'Search', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/searchClaim.action?rdn="+getRandomNumber(), scripts:true}},
                        {contentEl:'reportPanelTab', id:'reportPanelTabId', title:'Reports', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/buildReport.action?rdn="+getRandomNumber(), scripts:true}},
                        {contentEl:'adminPanelTab', id:'adminPanelTabId', title:'Admin', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/adminFunction.action?rdn="+getRandomNumber(), scripts:true}},
                        {contentEl:'boardPanelTab', id:'boardPanelTabId', title:'Dashboard', listeners: {activate: handleActivate}},
                        {contentEl:'xmlUploadTab', id:'xmlUploadTabId', title:'Claim/Invoice Upload', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/XmlUpload.action?rdn="+getRandomNumber(), scripts:true}}
                    ]
                });
                
        </s:if><s:else >
            
            <s:if test="menuAccessibility.isDashBoardMenuAccessibility!=true">

                    selectedIndex++;
            </s:if>

                    tabs = new Ext.TabPanel({
                        renderTo: 'tabPanel',
                        autoheight:true,
                        activeTab: selectedIndex,
                        items:[
                            {contentEl:'boardPanelTab', id:'boardPanelTabId', title:'Dashboard', listeners: {activate: handleActivate}},
                            {contentEl:'filterPanelTab', id:'inboxPanelTabId', title:'Inbox', listeners: {activate: handleActivate}},
                            {contentEl:'searchPanelTab', title:'Search', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/searchClaim.action?rdn="+getRandomNumber(), scripts:true}},
                            {contentEl:'reportPanelTab', id:'reportPanelTabId', title:'Reports', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/buildReport.action?rdn="+getRandomNumber(), scripts:true}},
                            {contentEl:'adminPanelTab', id:'adminPanelTabId', title:'Admin', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/adminFunction.action?rdn="+getRandomNumber(), scripts:true}},
                            {contentEl:'xmlUploadTab', id:'xmlUploadTabId', title:'Claim/Invoice Upload', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/XmlUpload.action?rdn="+getRandomNumber(), scripts:true}}
                        ]
                    });



        </s:else>

        <s:if test="menuAccessibility.isDashBoardMenuAccessibility!=true">
                tabs.remove('boardPanelTabId', true);
        </s:if>

        <s:if test="menuAccessibility.isReportMenuAccessibility!=true">
                tabs.remove('reportPanelTabId', true);
        </s:if>

        <s:if test="menuAccessibility.isAdminMenuAccessibility!=true">
                tabs.remove('adminPanelTabId', true);
        </s:if>
        <s:if test="isUploadAllowed!=true">
                tabs.remove('xmlUploadTabId', true);
        </s:if>

            }

            function handleActivate(tab){
                grid.hide();
                Ext.fly('gridPanel').addClass('x-hide-display');
                Ext.fly('xmlClaimsStatusGridDiv').addClass('x-hide-display');
                activityMonitor.clearViewingStatus();
            
                if(tab.title == 'Inbox' || tab.title == 'Search'){
        <s:if test="isChoxAdmin!=true">
                    activityMonitor.refreshViewingStatus();
        </s:if>

                    grid.show();
                    Ext.fly('gridPanel').removeClass('x-hide-display');

                    if(tab.title == 'Inbox' && isInboxShowHistory){

                        ds.baseParams = {"filterName" : Ext.state.Manager.get("grid_filterName")};
                        doDataLoad(Ext.state.Manager.get("inbox_grid_start"), Ext.state.Manager.get("inbox_grid_limit"),Ext.state.Manager.get("grid_title"));

                    }else if(tab.title == 'Search' && isSearchShowHistory){

                        ds.baseParams = Ext.state.Manager.get("grid_baseParams");
                        doDataLoad(Ext.state.Manager.get("search_grid_start"), Ext.state.Manager.get("search_grid_limit"),"Search Result");
                    }else{
                        ds.baseParams = {canLoadData  : false};
                        doDataLoad(0, 0,"Claims");
                    }

                }

                else if(tab.title == 'Claim/Invoice Upload'){
                    Ext.fly('xmlClaimsStatusGridDiv').removeClass('x-hide-display');
                }

                if(tabs)
                {
                    currentTabIndex = tabs.items.indexOf(tabs.getActiveTab());
                }
            }

            function validateSelectedClaims(batchActionName, idsParam, processAction){

                var url = '<%= request.getContextPath()%>/prv/p/checkClaimsBatchUpdate.action';
                var param = {"batchUpdateAction":batchActionName, "selectedClaimIds":idsParam};

                ajax.loadJson2(url, param, function(data){
                    if(data.resultType=='YesNo'){
                        if(data.result=='yes'){
                            if(processAction!=null){processAction();}
                        }
                    }else if(data.resultType=='Message'){
                        Ext.MessageBox.show({
                            title: '',
                            msg: data.result,
                            width:300,
                            buttons: Ext.MessageBox.OK
                        });
                    }
                });
            }

            function validateSelectedClaimsDialog(batchActionName, idsParam, dialog){

                var url = '<%= request.getContextPath()%>/prv/p/checkClaimsBatchUpdate.action';
                var param = {"batchUpdateAction":batchActionName, "selectedClaimIds":idsParam};

                ajax.loadJson2(url, param, function(data){
                    if(data.resultType=='YesNo'){
                        if(data.result=='yes'){
                            dialog.show();
                        }
                    }else if(data.resultType=='Message'){
                        Ext.MessageBox.show({
                            title: '',
                            msg: data.result,
                            width:300,
                            buttons: Ext.MessageBox.OK
                        });
                    }
                });
            }

            
    </script>

</head>

<div id="inboxScreenDiv">
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

    <div id="filterPanelTab" class="x-hide-display">
        <div id="filterPanel" style="float: left;">
            <label id="queueOrgFilter" style="float: left;"></label>
            <div id="orgFilterDiv"></div>
            <div id="filterPanel2">
                <s:action name="getFilterRecordCounters" namespace="/prv/p" executeResult="true" />
            </div>
        </div>
        <s:if test="taskManagementEnabled">
            <div id="taskPanelDiv">
                <s:action name="getTaskPanel" namespace="/prv/p" executeResult="true" />
            </div>
        </s:if>
    </div>

    <div id="searchPanelTab" class="x-hide-display"></div>
    <div id="reportPanelTab" class="x-hide-display"></div>
    <div id="adminPanelTab" class="x-hide-display"></div>
    <div id="xmlUploadTab" class="x-hide-display"></div>
    <div id="gridHolder"></div>


    <input id="userInsurerId" name="userInsurerId" value="<s:property value="AuthenticatedUser.insurer.id"/>" type="hidden"/>
    <input id="userSupplierId" name="userSupplierId" value="<s:property value="AuthenticatedUser.Chorganisation.id"/>" type="hidden"/>
    <input id="userInsurerWorkgroupEnable" name="userInsurerWorkgroupEnable" value="<s:property value="AuthenticatedUser.insurer.workgroupEnable"/>" type="hidden"/>



    <div id="claimRoutedSelectionDlgHolder" class="x-hidden">
        <div id="claimRoutedSelectionPanel">
            <form id="routeClaimForm" action="<%=request.getContextPath()%>/prv/processBatchClaims.action?name=assignWorkgroup" class="XXentity-form">
                <input name="selectedClaimIds" type="hidden" />
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
                <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
            </form>
        </div>
    </div>

    <div id="claimOwnerSelectionDlgHolder" class="x-hidden">
        <div id="claimOwnerSelectionPanel">
            <form id="ownershipClaimForm" name="ownershipClaimForm" action="<%=request.getContextPath()%>/prv/processBatchClaims.action?name=assignOwner" class="XXentity-form">
                <input name="selectedClaimIds" type="hidden"/>
                <table class="selection-form" cellspacing="0" cellpadding="0" border="0">
                    <tr>
                        <th colspan="2"><label>Please assign the claim(s) to a Claim Owner.</label></th>
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
                <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
            </form>
        </div>
    </div>

    <div id="supplierClaimOwnerSelectionDlgHolder" class="x-hidden">
        <div id="supplierClaimOwnerSelectionPanel">
            <form id="supplierOwnershipClaimForm" name="supplierOwnershipClaimForm" action="<%=request.getContextPath()%>/prv/processBatchClaims.action?name=assignSupplierOwner" class="XXentity-form">
                <input name="selectedClaimIds" type="hidden"/>
                <table class="selection-form" cellspacing="0" cellpadding="0" border="0">
                    <tr>
                        <th colspan="2"><label>Please assign the claim(s) to a Claim Owner.</label></th>
                    </tr>
                    <tr>
                        <td class="pop-claim-ownership-label" style="height:60px;"><label>Claim Owner</label></td>
                        <td class="pop-claim-ownership-column"><div id="supplierClaimOwnerDropDownDiv"></div></td>
                    </tr>
                    <tr>
                        <td colspan="2"><div id="supplierOwnershipClaimFormMessageBox" class="action-error-msg"/></td>
                    </tr>
                </table>
                <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
            </form>
        </div>
    </div>

    <div id="couSelectionDlgHolder" class="x-hidden">
        <div id="couSelectionPanel">
            <form id="ClaimOwnershipUpdateForm" action="<%=request.getContextPath()%>/prv/p/doClaimOwnershipUpdateAction.action" class="XXentity-form">
                <input name="selectedClaimIds" type="hidden" />
                <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
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
    <div id="gridPanel" class="x-hide-display">
        <div class="excel-export">
            <form name="thisForm" action=""><a href="javascript:doExportExcel();">Export To Excel</a></form>
        </div>
    </div>

    <div id="xmlClaimsStatusGridDiv" class="x-hide-display">
        <div id="xmlClaimsStatusGrid"></div>
        <div class="excel-export" id="UploadedClaimDetailsExportId">
            <form action=""><a href="javascript:doExportUploadedClaimDetailsToExcel();">Export To Excel</a></form>
        </div>
    </div>
</div>