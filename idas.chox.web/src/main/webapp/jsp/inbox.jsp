<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<head>
    <title>CHOX</title>
    
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
        var currentClaimType;
        var grid;
        var ds;
        var exportIntervelId;
        var doClaimOwnerAction;
        var doClaimRoutedAction;
        var doInsurerClaimOwnerAction;
        var manualInvoiceFilter;
        var dashboardActionName;
        
        Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

        Ext.onReady(function(){
            
           // Ext.BLANK_IMAGE_URL = 'images/s.gif';
           // The 'setValue' function on the combo box doesn't work
           // as, fue to the asynchronous nature of the widget, the store may
           // not be loaded. Below is a patch to fix this problem.
           Ext.override(Ext.form.ComboBox, {
            setValue : function(v){
                //begin patch
                // Store not loaded yet? Set value when it *is* loaded.
                // Defer the setValue call until after the next load.
                if (this.store.getCount() == 0) {
                    this.store.on('load',
                    this.setValue.createDelegate(this, [v]), null, {single: true});
                    return;
                }
                //end patch
                var text = v;
                if(this.valueField){
                    var r = this.findRecord(this.valueField, v);
                    if(r){
                        text = r.data[this.displayField];
                    }else if(this.valueNotFoundText !== undefined){
                        text = this.valueNotFoundText;
                    }
                }
                this.lastSelectionText = text;
                if(this.hiddenField){
                    this.hiddenField.value = v;
                }
                Ext.form.ComboBox.superclass.setValue.call(this, text);
                this.value = v;
            }});

            <s:if test="isInsurer">
                dashboardActionName = "showInsurerBoardHeader";
            </s:if>
            <s:if test="isCHO">
                dashboardActionName = "showChoBoardHeader";
            </s:if>
            Ext.QuickTips.init();
            loadDataFromSession();
            setupGrid();
            setupTabPanels();
            grid.render('gridHolder');
            updateManualInvoiceBatchUpdate(Ext.state.Manager.get("grid_filterName"));
            <s:if test="isChoxAdmin!=true && enableActivityMonitor">
                var pingServerUrl = '<%=request.getContextPath()%>/prv/p/activityMonitoringAction.action';
                var checkStatusIUrl = '<%=request.getContextPath()%>/prv/p/checkViewingStatus.action';
                activityMonitor.setup(pingServerUrl, checkStatusIUrl,  <s:property value="activityMonitorRequestInterval"/>);
            </s:if>
            
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
                    {name:'choOwnerName'},
                    {name:'claimHasAttachment'}
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

            
            function updateManualInvoiceBatchUpdate(filterName) {
            
                // MANUAL INVOICE BATCH UPDATE LOGIC

                if (filterName == 'ManualInvoiceContested' || filterName == 'ManualInvoiceBRERejected' 
                    || filterName == 'ManualInvoiceBREApproved' || filterName == 'ManualInvoicesToBeAssigned') {
                   Ext.state.Manager.set("manualInvoiceFilter",true);
                   manualInvoiceFilter = true;
                   if (filterName == 'ManualInvoicesToBeAssigned') {
                        doClaimOwnerAction.setText('Assign Claim(s) Workgroup And Claim Owner');
                        doClaimRoutedAction.setText('Route Claim(s)');
                        doInsurerClaimOwnerAction.setText('Assign Claim(s) Owner');
                    } else {
                        doClaimOwnerAction.setText('Update Claim(s) Workgroup And Claim Owner');
                        if (<s:property value="insurerIsClaimOwnershipEnabled"/>) {
                            doClaimRoutedAction.setText('Route Claim(s)');
                        } else {
                            doClaimRoutedAction.setText('Re-Route Claim(s)');
                        }
                        doInsurerClaimOwnerAction.setText('Update Claim(s) Owner');
                    }
                    
                    if (!(<s:property value="isInsurer"/> && (!<s:property value="enableManualInvoiceWorkgroups"/> || !<s:property value="insurerIsWorkgroupEnabled"/>) 
                        && <s:property value="enableManualInvoiceOwnership"/>)) {
                        doInsurerClaimOwnerAction.setHidden(true);
                    } else {
                        doInsurerClaimOwnerAction.setHidden(false);
                    }
                }else {
                    Ext.state.Manager.set("manualInvoiceFilter",false);
                    manualInvoiceFilter = false;
                    doClaimOwnerAction.setText('Assign Claim(s) Owner');
                    doInsurerClaimOwnerAction.setText('Assign Claim(s) Owner');
                    doInsurerClaimOwnerAction.setHidden((<s:property value="isCHO"/> || !(<s:property value="isInsurer"/> 
                        && !<s:property value="insurerIsWorkgroupEnabled"/> && <s:property value="insurerIsClaimOwnershipEnabled"/>)));
                   
                   // Route Claim BATCH UPDATE LOGIC
                    if (filterName != 'NewClaimsToBerouted' && !<s:property value="insurerIsClaimOwnershipEnabled"/>) {
                        doClaimRoutedAction.setText('Re-Route Claim(s)'); 
                    } else {
                        doClaimRoutedAction.setHidden(<s:property value="isCHO"/>); 
                        doClaimRoutedAction.setText('Route Claim(s)'); 
                    }
                    // Route Claim BATCH UPDATE LOGIC ENDS.
                }
            }
            
            function refreshFilterPanel() {
                var url = "<%=request.getContextPath()%>/prv/p/getFilterRecordCounters.action";
                var param = {"filterOrgId":currentOrg, "filterClaimTypeId":currentClaimType};
                ajax.loadHtml2(url, param, function(data){
                    $("div#filterPanel2").html(data);
                });
            }
            
            function searchClaim(canSearchForData){

                var supplierReference = Ext.query('*[name$=supplierReference]')[0].value;
                if (Ext.getCmp('searchScreenSupplierComboId'))
                    var supplierIds = Ext.getCmp('searchScreenSupplierComboId').getValue().split(",");

                if (Ext.getCmp('searchScreenInsurerComboId'))
                    var insurerIds = Ext.getCmp('searchScreenInsurerComboId').getValue().split(",");

                var invoiceNumber = Ext.query('*[name$=invoiceNumber]')[0].value;
                var claimNumber = Ext.query('*[name$=claimNumber]')[0].value;
                var thirdPartyVrn = Ext.query('*[name$=thirdPartyVrn]')[0].value;
                var claimUploadDateFrom = Ext.query('*[name$=claimUploadDateFrom]')[0].value;
                var claimUploadDateTo = Ext.query('*[name$=claimUploadDateTo]')[0].value;

                var statusModifiedDateFrom = Ext.query('*[name$=statusModifiedDateFrom]')[0].value;
                var statusModifiedDateTo = Ext.query('*[name$=statusModifiedDateTo]')[0].value;

                var invoiceUploadDateFrom = Ext.query('*[name$=invoiceUploadDateFrom]')[0].value;
                var invoiceUploadDateTo = Ext.query('*[name$=invoiceUploadDateTo]')[0].value;
                var rentalStartDate = Ext.query('*[name$=rentalStartDate]')[0].value;
                var rentalEndDate = Ext.query('*[name$=rentalEndDate]')[0].value;
                var statuses = Ext.getCmp('statusSearchScreenComboId').getValue().split(",");

                if (Ext.getCmp('searchScreenWorkgroupComboId'))
                    var workgroupIds = Ext.getCmp('searchScreenWorkgroupComboId').getValue().split(",");

                var reviewRequiredDateFrom = Ext.query('*[name$=reviewRequiredDateFrom]')[0].value;
                var reviewRequiredDateTo = Ext.query('*[name$=reviewRequiredDateTo]')[0].value;

                if (Ext.getCmp('searchScreenClaimOwnerComboId'))
                    var claimOwnerIds = Ext.getCmp('searchScreenClaimOwnerComboId').getValue().split(",");

                if (Ext.getCmp('searchScreenSupplierClaimOwnerComboId'))
                    var supplierClaimOwnerIds = Ext.getCmp('searchScreenSupplierClaimOwnerComboId').getValue().split(",");

                var customerVrn = Ext.query('*[name$=customerVrn]')[0].value;
                var showOpenClaimsOnly = Ext.query('*[name$=showOpenClaimsOnly]')[0].checked;
                var isSupplementaryInvoiceOnly = Ext.query('*[name$=isSupplementaryInvoiceOnly]')[0].checked;
                var penaltyChargesAppliedOnly = Ext.query('*[name$=penaltyChargesAppliedOnly]')[0].checked;
                var liabilityStatuses = Ext.getCmp('liabilityStatusSearchScreenComboId').getValue().split(",");
                var claimTypes = Ext.getCmp('claimTypesSearchScreenComboId').getValue().split(",");

                ds.baseParams = {
                    /*
                     *  if canSearchForData is false then no data will be returned. this is mainly used to reset the search screen form.
                     */
                    canLoadData : canSearchForData,
                    searchHistory : true,
                    filterName : '',
                    supplierReference : supplierReference,
                    supplierIds : supplierIds,
                    insurerIds : insurerIds,
                    invoiceNumber : invoiceNumber,
                    claimNumber : claimNumber,
                    thirdPartyVrn : thirdPartyVrn,
                    claimUploadDateFrom : claimUploadDateFrom,
                    claimUploadDateTo : claimUploadDateTo,
                    statusModifiedDateFrom : statusModifiedDateFrom,
                    statusModifiedDateTo :  statusModifiedDateTo,
                    invoiceUploadDateFrom : invoiceUploadDateFrom,
                    invoiceUploadDateTo : invoiceUploadDateTo,
                    rentalStartDate : rentalStartDate,
                    rentalEndDate : rentalEndDate,
                    statuses : statuses,
                    workgroupIds : workgroupIds,
                    reviewRequiredDateFrom : reviewRequiredDateFrom,
                    reviewRequiredDateTo : reviewRequiredDateTo,
                    claimOwnerIds : claimOwnerIds,
                    supplierClaimOwnerIds : supplierClaimOwnerIds,
                    customerVrn : customerVrn,
                    showOpenClaimsOnly : showOpenClaimsOnly,
                    penaltyChargesAppliedOnly : penaltyChargesAppliedOnly,
                    liabilityStatuses : liabilityStatuses,
                    claimTypes : claimTypes,
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
                        if (tabs && tabs.getActiveTab().title == 'Inbox') {
                            var title = Ext.state.Manager.get("grid_main_title");
                            if (title.indexOf("Queue: ") != -1) {
                                title = title.replace("Queue: ","");
                                $("a:contains(" + title +")").html(title+" ("+ds.getTotalCount()+")");
                            }
                        }
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
                Ext.state.Manager.set("filter_claim_type_id",-1);
                Ext.state.Manager.set("filter_org_id",-1);
                isInboxShowHistory = false;
                isSearchShowHistory = false;
                manualInvoiceFilter = false;
        </s:if><s:else >
                isInboxShowHistory = Ext.state.Manager.get("grid_isInboxShowHistory");
                isSearchShowHistory = Ext.state.Manager.get("grid_isSearchShowHistory");
                manualInvoiceFilter = Ext.state.Manager.get("manualInvoiceFilter");
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
                doClaimRoutedAction = new Ext.Action({
                    text: 'Route Claim(s)',
                    hidden:<s:property value="isCHO"/>,
//                                || !(<s:property value="isInsurer"/> && <s:property value="insurerIsWorkgroupEnabled"/> && !<s:property value="insurerIsClaimOwnershipEnabled"/>),
//                             || (manualInvoiceFilter && !(<s:property value="isInsurer"/> && <s:property value="enableManualInvoiceWorkgroups"/> && (!<s:property value="enableManualInvoiceOwnership"/> || !<s:property value="insurerIsClaimOwnershipEnabled"/>))),
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
                                                        if (manualInvoiceFilter) {
                                                            formData.push({ name: 'name', value: 'assignManualInvoiceOwner' });
                                                        } else {
                                                            formData.push({ name: 'name', value: 'assignWorkgroup' });
                                                        }
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
                    text: 'Agree Quantum on Invoice(s)',
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
                var isHidden = <s:property value="isInsurer"/> || <s:property value="isChoxAdmin"/> || (<s:property value="isCHO"/> && !<s:property value="choIsClaimOwnershipEnabled"/>);
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

                /**** BATCH UPDATE - ASSIGN CLAIM OWNER (both claimOwnership and workgroup should be enabled)******/


                var claimOwnerSelectionDlg;
                doClaimOwnerAction = new Ext.Action({
                    text: 'Assign Claim(s) Workgroup And Claim Owner',
                    hidden: <s:property value="isCHO"/> || <s:property value="isChoxAdmin"/> 
                        || !(<s:property value="isInsurer"/> && <s:property value="insurerIsClaimOwnershipEnabled"/> && <s:property value="insurerIsWorkgroupEnabled"/>),
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
                                ({url : "<%= request.getContextPath()%>/prv/p/ClaimHandlerRoleUserDropDownAction2.action", method:'GET', params : {"workgroupId":-1,"insurerId":-1}}),
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
                                                            formData[1].value = claimOwnerCombo.getValue();
                                                        }
                                                        if (manualInvoiceFilter) {
                                                            formData.push({ name: 'name', value: 'assignManualInvoiceOwner' });
                                                        } else {
                                                            formData.push({ name: 'name', value: 'assignOwner' });
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
                                
                                var selectedRecords =  sm2.getSelections();
                                var selectedIDs = $.map(selectedRecords, function(n){ return n.json.id; });
                                var idsParam = selectedIDs.join(",");
                                var uniqueWorkgroupId;
                                // getUniqueWorkgroupId for the selected claims. implemented for  
                                // bug#1546 Bulk action 'Assign Claim Owner' should default to correct workgroup
                                Ext.Ajax.request({
                                    url: '<%= request.getContextPath()%>/prv/p/getUniqueWorkgroupId.action',
                                    params: {
                                        selectedClaimIds  : idsParam,
                                        nonce :'<%= session.getAttribute("SessionNonce")%>'
                                    },
                                    callback : function(options,success,response){
                                        if(response.responseText){
                                            var resp = Ext.util.JSON.decode(response.responseText);
                                            if(resp){
                                                uniqueWorkgroupId = resp.workgroupId;
                                            } else { 
                                                uniqueWorkgroupId = -1;
                                             }
                                           }
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
                                                if(uniqueWorkgroupId >= 1) {
                                                    workgroupCombo.setValue(uniqueWorkgroupId);
                                                } 
                                            }

                                            // GENERATE CLAIM OWNER
                                            if(uniqueWorkgroupId >= 1) { 
                                              claimOwnerStore.load({ params : {"workgroupId":uniqueWorkgroupId,"insurerId":insurerId}});
                                            } else {
                                              claimOwnerStore.load({ params : {"workgroupId":-1,"insurerId":insurerId}});
                                            }
                                            claimOwnerCombo.reset();
                                         }
                                     });
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
                doInsurerClaimOwnerAction = new Ext.Action({
                    text: 'Assign Claim(s) Owner',
                    hidden:<s:property value="isCHO"/> || <s:property value="isChoxAdmin"/> || (!manualInvoiceFilter && !(<s:property value="isInsurer"/> && !<s:property value="insurerIsWorkgroupEnabled"/> && <s:property value="insurerIsClaimOwnershipEnabled"/>)),
//                                || (manualInvoiceFilter && !(<s:property value="isInsurer"/> && (!<s:property value="enableManualInvoiceWorkgroups"/> || !<s:property value="insurerIsWorkgroupEnabled"/>) && <s:property value="enableManualInvoiceOwnership"/>)),
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
                                ({url : "<%= request.getContextPath()%>/prv/p/ClaimHandlerRoleUserDropDownAction2.action", method:'GET', params : {"workgroupId":-1,"insurerId":-1}}),
                                reader : claimOwnerReader
                            });

                            var claimOwnerCombo = new Ext.form.ComboBox({
                                store : claimOwnerStore,
                                width: 220,
                                renderTo: 'claimOwnerClaimHandlerRoleUserDropDownDiv1',
                                valueField : 'id',
                                id : 'claimOwnerId1',
                                hiddenName : 'claimOwnerId',
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
                                applyTo:'claimOwnerSelectionDlgHolder1',
                                layout:'fit',
                                width:410,
                                height:280,
                                modal: true,
                                closeAction:'hide',
                                plain: false,
                                title: 'Assign Claim(s) Owner',
                                resizable : false,
                                items: new Ext.Panel({
                                    applyTo: 'claimOwnerSelectionPanel1'
                                }),
                                buttons: [{
                                        text:'Ok',
                                        handler:function(){
                                            if(document.getElementById('claimOwnerId1').value === "--- Please Select ---"){
                                                Ext.Msg.alert("","please select Claim Owner");
                                            }else if(document.getElementById('claimOwnerId1').value!=''){
                                                var selectedRecords =  sm2.getSelections();
                                                var selectedIDs = $.map(selectedRecords, function(n){
                                                    return n.json.id;
                                                });
                                                var idsParam = selectedIDs.join(",");
                                                $('form#ownershipClaimForm1 input[name="selectedClaimIds"]').val(idsParam);
                                                
                                                var submitOption = {
                                                    clearForm: true,
                                                    beforeSubmit: function(formData, form, options) {

                                                        formData[1].value = claimOwnerCombo.getValue();
                                                        if (manualInvoiceFilter) {
                                                            formData.push({ name: 'name', value: 'assignManualInvoiceOwner' });
                                                        } else {
                                                            formData.push({ name: 'name', value: 'assignOwner' });
                                                        }
                                                        
                                                        

                                                    },
                                                    success:function(){
                                                        sm2.clearSelections();
                                                        claimOwnerCombo.reset();
                                                        ds.reload();
                                                        refreshFilterPanel();
                                                        insurerClaimOwnerSelectionDlg.hide();
                                                    }
                                                };

                                                $("form#ownershipClaimForm1").ajaxSubmit(submitOption);
                                            }
                                        }
                                    },{
                                        text: 'Close',
                                        handler: function(){
                                            // hide the error message box, which could be displayed,
                                            // so that it doesn't appear when we're opened again
                                            $("#ownershipClaimFormMessageBox1").hide();
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
                    hidden: (<s:property value="isCHO"/> 
                        || !(<s:property value="isInsurer"/> && !<s:property value="insurerIsWorkgroupEnabled"/> && <s:property value="insurerIsClaimOwnershipEnabled"/>)),
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
                    hidden: (<s:property value="isCHO"/> 
                        || !(<s:property value="isInsurer"/> && <s:property value="insurerIsClaimOwnershipEnabled"/> && <s:property value="insurerIsWorkgroupEnabled"/>)),
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
                
                var claimsExportToExcelTbar = new Ext.Toolbar({
                items:[{
                        text:'Export To Excel',
                        id : 'claimsExportToExcelButtonId',
                        disabled : !<s:property value="canExport" />,
                        handler : function() {
                            doExportExcel();
                        }
                    }]
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
                    enableColumnMove: false,
                    columns: [
                        sm2,
                        {header: "Supplier Ref", width: 100, sortable: true, dataIndex: 'supplierReference',
                            renderer:function(value,p,r){
                                return '<span style="text-decoration: underline; color: #15428B; font-size:12px; cursor: pointer;">' + value + '</span>'}},
                        {header: "Claim Type", width: 50, sortable: true, dataIndex: 'claimType'},
                        {header: "Claim No", width: 60, sortable: true, dataIndex: 'claimNumber'},
                        {header: "Insurer's Policy No", width: 60, sortable: true, dataIndex: 'policyNumber'},
                        {header: "Invoice Upload Date", width: 60, sortable: true, dataIndex: 'invoiceUploadDate'},
                        {header: "Status", width: 100, sortable: true, dataIndex: 'status'},
                        {header: "Total To Pay", width: 60, sortable: true, dataIndex: 'invoiceAmount', align: 'right'},
                        {header: "Workgroup", width: 100, sortable: true,hidden: (<s:property value="isInsurer"/> && !<s:property value="insurerIsWorkgroupEnabled"/>) , dataIndex: 'workgroup'},
                        {header: "Ins Owner", width: 50, sortable: true,hidden: (<s:property value="isInsurer"/> && !<s:property value="insurerIsClaimOwnershipEnabled"/> ), dataIndex: 'ownerName'},
                        {header: "CHO Owner", width: 50, sortable: true,hidden: (<s:property value="isCHO"/> && !<s:property value="choIsClaimOwnershipEnabled"/>), dataIndex: 'choOwnerName'},
                        {header: "Status Modified Date", width: 40, sortable: true, dataIndex: 'statusModifiedDate'},
                        {header: "Review Date", width: 40, sortable: true, dataIndex: 'reviewDate'},
                        {header: "CHO", width: 80, sortable: true, dataIndex: 'cho'},
                        {header: "Insurer", width: 80, sortable: true, dataIndex: 'insurer'},
                        {header: "Viewing", width: 30, sortable: false, dataIndex: 'id',renderer:function(value,p,r){
                                return '<input type="hidden" name="viewingId" value="' + value + '" /><label id="viewingLabel_' + value + '">-</label>'}},
                        {header: "", width : 40, sortable : false, dataIndex: 'claimHasAttachment', renderer : function(value, metaData, record, rowIndex, colIndex, store){
                                if(value){metaData.css = 'paperClip';} 
                            }}
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
                    tbar:[actionMenu, '->', claimsExportToExcelTbar]
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
                        {contentEl:'filterPanelTab', id:'inboxPanelTabId', title:'Inbox', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/getInboxTabPanel.action?rdn="+getRandomNumber(), scripts:true}},
                        {contentEl:'searchPanelTab', id:'searchPanelTabId', title:'Search', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/searchClaim.action?rdn="+getRandomNumber(), scripts:true}},
                        {contentEl:'reportPanelTab', id:'reportPanelTabId', title:'Reports', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/buildReport.action?rdn="+getRandomNumber(), scripts:true}},
                        {contentEl:'adminPanelTab', id:'adminPanelTabId', title:'Admin', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/adminFunction.action?rdn="+getRandomNumber(), scripts:true}},
                        {contentEl:'boardPanelTab', id:'boardPanelTabId', title:'Dashboard', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/"+dashboardActionName+".action?rdn="+getRandomNumber(), scripts:true}},
                        {contentEl:'xmlUploadTab', id:'xmlUploadTabId', title:'Claim/Invoice Upload', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/XmlUpload.action?rdn="+getRandomNumber(), scripts:true}}
                    ]
                });
                
        </s:if><s:else >
            
            <s:if test="menuAccessibility.isDashBoardMenuAccessibility!=true">

                    selectedIndex++;
            </s:if>

                    tabs = new Ext.TabPanel({
                        renderTo: 'tabPanel',
                        autoheight: true,
                        activeTab: selectedIndex,
                        items:[
                            {contentEl:'boardPanelTab', id:'boardPanelTabId', title:'Dashboard', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/"+dashboardActionName+".action?rdn="+getRandomNumber(), scripts:true}},
                            {contentEl:'filterPanelTab', id:'inboxPanelTabId', title:'Inbox', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/getInboxTabPanel.action?rdn="+getRandomNumber(), scripts:true}},
                            {contentEl:'searchPanelTab', id:'searchPanelTabId', title:'Search', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/searchClaim.action?rdn="+getRandomNumber(), scripts:true}},
                            {contentEl:'reportPanelTab', id:'reportPanelTabId', title:'Reports', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/buildReport.action?rdn="+getRandomNumber(), scripts:true}},
                            {contentEl:'adminPanelTab', id:'adminPanelTabId', title:'Admin', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/adminFunction.action?rdn="+getRandomNumber(), scripts:true}},
                            {contentEl:'xmlUploadTab', id:'xmlUploadTabId', title:'Claim/Invoice Upload', listeners: {activate: handleActivate}, autoLoad: {url:"<%=request.getContextPath()%>/prv/p/XmlUpload.action?rdn="+getRandomNumber(), scripts:true}}
                        ]
                    });



        </s:else>

        <s:if test="menuAccessibility.isDashBoardMenuAccessibility!=true">
                tabs.remove('boardPanelTabId', true);
        </s:if>

        <s:if test="menuAccessibility.isSearchMenuAccessibility!=true">
                tabs.remove('searchPanelTabId', true);
        </s:if>

        <s:if test="menuAccessibility.isInboxMenuAccessibility!=true">
                tabs.remove('inboxPanelTabId', true);
        </s:if>

        <s:if test="menuAccessibility.isReportMenuAccessibility!=true">
                tabs.remove('reportPanelTabId', true);
        </s:if>

        <s:if test="menuAccessibility.isAdminMenuAccessibility!=true">
                tabs.remove('adminPanelTabId', true);
        </s:if>
       
        <s:if test="menuAccessibility.isUploadMenuAccessibility!=true">
                tabs.remove('xmlUploadTabId', true);
        </s:if>

            }

            function handleActivate(tab){
                grid.hide();
                Ext.fly('gridPanel').addClass('x-hide-display');
                Ext.fly('xmlClaimsStatusGridDiv').addClass('x-hide-display');
                activityMonitor.clearViewingStatus(); 
            
                if(tab.title == 'Inbox' || tab.title == 'Search'){
                    <s:if test="isChoxAdmin!=true && enableActivityMonitor">
                            activityMonitor.refreshViewingStatus();
                    </s:if>
   
                    grid.show();
                    Ext.fly('gridPanel').removeClass('x-hide-display');
                    // below code is hack to clear search screen size being set up by extjs when switch between other tabs. (to-do item 6.10.5).
                    if (tab.title == 'Search') {
                        if(tabs) {
                            $($($('#searchPanelTabId').children()[0]).children()[0]).removeAttr("style");
                        }
                    }
                    
                    if(tab.title == 'Search'){
                        doClaimRoutedAction.setText('Route Claim(s)');
                    }
                    
                    if(tab.title == 'Inbox' && isInboxShowHistory){

                        ds.baseParams = {"filterName" : Ext.state.Manager.get("grid_filterName"), "filterOrgId" : Ext.state.Manager.get("filter_org_id"), "filterClaimTypeId": Ext.state.Manager.get("filter_claim_type_id")};
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

    <div id="boardPanelTab" class="x-hide-display"></div>
    <div id="filterPanelTab" class="x-hide-display"></div>
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
            <form id="routeClaimForm" action="<%=request.getContextPath()%>/prv/processBatchClaims.action?" class="XXentity-form">
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
                <form id="ownershipClaimForm" name="ownershipClaimForm" action="<%=request.getContextPath()%>/prv/processBatchClaims.action?" class="XXentity-form">
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
            
    <div id="claimOwnerSelectionDlgHolder1" class="x-hidden">
        <div id="claimOwnerSelectionPanel1">
                <form id="ownershipClaimForm1" name="ownershipClaimForm" action="<%=request.getContextPath()%>/prv/processBatchClaims.action?" class="XXentity-form">
                <input name="selectedClaimIds" type="hidden"/>
                <table class="selection-form" cellspacing="0" cellpadding="0" border="0">
                    <tr>
                        <th colspan="2"><label>Please assign the claim(s) to a Claim Owner.</label></th>
                    </tr>
                    <tr>
                        <td class="pop-claim-ownership-label" style="height:60px;"><label>Claim Owner</label></td>
                        <td class="pop-claim-ownership-column"><div id="claimOwnerClaimHandlerRoleUserDropDownDiv1"></div></td>
                    </tr>
                    <tr>
                        <td colspan="2"><div id="ownershipClaimFormMessageBox1" class="action-error-msg"/></td>
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
    <div id="gridPanel" class="x-hide-display"></div>

    <div id="xmlClaimsStatusGridDiv" class="x-hide-display">
        <div id="xmlClaimsStatusGrid"></div>
    </div>
</div>