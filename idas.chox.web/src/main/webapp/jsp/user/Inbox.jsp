<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<head>
    <title>IDAS-CHOX</title>
    <link href="<%= request.getContextPath()%>/css/chox.css" rel="stylesheet" type="text/css" media="all"/>
    <link href="<%= request.getContextPath()%>/css/ext-all.css" rel="stylesheet" type="text/css" media="all"/>
    <script src="<%= request.getContextPath()%>/scripts/jquery/jquery-1.2.6.js" type="text/javascript" ></script>
    <script src="<%= request.getContextPath()%>/scripts/jquery/jquery.form.js" type="text/javascript" ></script>
    <script src="<%= request.getContextPath()%>/scripts/jquery/ext-jquery-adapter.js" type="text/javascript" ></script>
    <script src="<%= request.getContextPath()%>/scripts/jquery/jquery.blockUI.js" type="text/javascript" ></script>
    <script src="<%= request.getContextPath()%>/scripts/jquery/jquery.timer.js" type="text/javascript" ></script>
    <script src="<%= request.getContextPath()%>/scripts/ext-base.js" type="text/javascript"></script>
    <script src="<%= request.getContextPath()%>/scripts/ext-all.js" type="text/javascript"></script> 
    <script src="<%= request.getContextPath()%>/scripts/general.js" type="text/javascript"></script>
    <script src="<%= request.getContextPath()%>/scripts/Application.js" type="text/javascript"></script>

    <script src="<%= request.getContextPath()%>/scripts/form.js" type="text/javascript"></script>
    <script src="<%= request.getContextPath()%>/scripts/ajax.js" type="text/javascript"></script>
    <script src="<%= request.getContextPath()%>/scripts/activityMonitor.js" type="text/javascript"></script>
    
</head>

<script type="text/javascript">
    
    var currentTabIndex;
    var tabs;
    var recordPerPage = 20;
    var isCho = <s:property value="isCHO"/>;
    var isInsurer = <s:property value="isInsurer"/>;

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
        ({url: 'user/doSearchClaim.action',method:'POST'}),
        reader:rd,
        remoteSort: true
    });
    ds.setDefaultSort('created', 'desc');

    Ext.BLANK_IMAGE_URL = '<%= request.getContextPath()%>/images/default/s.gif';     

    function showClaimByStatus(status, isWorkgroupCheck, isOwnerShipCheck)
    {

        ds.baseParams = {
            
            supplierReference : '',
            supplierId : -1,
            insurerId : -1,
            invoiceNumber : '',
            claimNumber : '',
            thirdPartyVrn : '',
            claimUploadDateFrom : '',
            claimUploadDateTo : '',
            invoiceUploadDateFrom : '',
            invoiceUploadDateTo :  '',                
            hireDateFrom : '',
            hireDateTo : '',          
            status : status,
            workgroupId : -1,
            isAnomalies : '',
            ispenaltyChargeApplied : '',
            reviewRequiredDateFrom:'',
            reviewRequiredDateTo:'',
            isWorkgroupCheck:isWorkgroupCheck,
            isOwnerShipCheck:isOwnerShipCheck,
            claimOwnerId:-1,
            customerVrn:'',
            isOpenClaim:''
        }

        doDataLoad(0, recordPerPage, true);

    }  
    
    function showClaimByStatusWithSort(status, sort, isWorkgroupCheck, isOwnerShipCheck)
    {     
        ds.setDefaultSort(sort, 'status');
        ds.baseParams = {            
            supplierReference : '',
            supplierId : -1,
            insurerId : -1,
            invoiceNumber : '',
            claimNumber : '',
            thirdPartyVrn : '',
            claimUploadDateFrom : '',
            claimUploadDateTo : '',
            invoiceUploadDateFrom : '',
            invoiceUploadDateTo :  '',                
            hireDateFrom : '',
            hireDateTo : '',          
            status : status,
            workgroupId : -1,
            isAnomalies : '',
            ispenaltyChargeApplied : '',
            reviewRequiredDateFrom:'',
            reviewRequiredDateTo:'',
            isWorkgroupCheck:isWorkgroupCheck,
            isOwnerShipCheck:isOwnerShipCheck,
            claimOwnerId:-1,
            customerVrn:'',
            isOpenClaim:''
        }

        doDataLoad(0, recordPerPage, true);

    }  
    
    function showClaimIsAnomalies(isWorkgroupCheck, isOwnerShipCheck)
    {   
        ds.baseParams = {
            supplierReference : '',
            supplierId : -1,
            insurerId : -1,
            invoiceNumber : '',
            claimNumber : '',
            thirdPartyVrn : '',
            claimUploadDateFrom : '',
            claimUploadDateTo : '',
            invoiceUploadDateFrom : '',
            invoiceUploadDateTo :  '',                
            hireDateFrom : '',
            hireDateTo : '',         
            status : '',
            workgroupId : -1,
            isAnomalies : true,
            ispenaltyChargeApplied : '',
            reviewRequiredDateFrom:'',
            reviewRequiredDateTo:'',
            isWorkgroupCheck:isWorkgroupCheck,
            isOwnerShipCheck:isOwnerShipCheck,
            claimOwnerId:-1,
            customerVrn:'',
            isOpenClaim:''
        }

        doDataLoad(0, recordPerPage, true);

    }   
    
    function showClaimIspenaltyChargeApplied()
    {       
        ds.baseParams = {
            supplierReference : '',
            supplierId : -1,
            insurerId : -1,
            invoiceNumber : '',
            claimNumber : '',
            thirdPartyVrn : '',
            claimUploadDateFrom : '',
            claimUploadDateTo : '',
            invoiceUploadDateFrom : '',
            invoiceUploadDateTo :  '',                
            hireDateFrom : '',
            hireDateTo : '',          
            status : '',
            workgroupId : -1,
            isAnomalies : '',
            ispenaltyChargeApplied : true,
            reviewRequiredDateFrom:'',
            reviewRequiredDateTo:'',
            isWorkgroupCheck:false,
            isOwnerShipCheck:false,
            claimOwnerId:-1,
            customerVrn:'',
            isOpenClaim:''
        }

        doDataLoad(0, recordPerPage, true);
        
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
            isAnomalies : '',
            ispenaltyChargeApplied : '',
            workgroupId: workgroupId,
            reviewRequiredDateFrom : reviewRequiredDateFrom,
            reviewRequiredDateTo : reviewRequiredDateTo,
            isWorkgroupCheck:false,
            isOwnerShipCheck:false,
            claimOwnerId : claimOwnerId,
            customerVrn : customerVrn,
            isOpenClaim : isOpenClaim
        }

        doDataLoad(0, recordPerPage, true);
        
    }
    
    function setupGrid(){
        
        Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
        Ext.QuickTips.init();    

        var sm2 = new Ext.grid.CheckboxSelectionModel();
        
        var pagingBar = new Ext.PagingToolbar({
            pageSize: recordPerPage,
            store: ds,
            displayInfo: true,
            displayMsg: 'Displaying claims {0} - {1} of {2}',
            emptyMsg: "No claim to display"
        });

        function getErrorClaims(selectedRecords, isWorkgroupCheck, isOwnershipCheck, allowedStatuses){

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
                //propmtMsg("", "You are not authorised to action claim(s) " + selectedNotMyClaimsIDs.join(", ") + ", please de-select the tick box for this claim(s)");
                propmtMsg("", "Please de-select the tick box for following claim(s) <br/>" + selectedNotMyClaimsIDs.join(" "));
            }

            return selectedNotMyClaimsIDs;
        }
        
        var approvedInvoicesPaymentAction = new Ext.Action
        ({
            text: 'Update Claim(s) To Invoice Payment Logged',
            hidden:isCho,
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
                        
                        var param = selectedIDs.join(",")

                        $.ajax({
                            url: "logInvoicePayments.action?selectedClaimIds=" + param,
                            success: function()
                            {
                                sm2.clearSelections();
                                ds.reload();
                                refreshFilterPanel();
                            }
                        });
                    }
                }
            }
        }); 
    
        var clearBREApprovedInvoicesForPaymentAction = new Ext.Action
        ({
            text: 'Approve Claim(s) For Payment',
            hidden:isCho,
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

                        var param = selectedIDs.join(",")

                        $.ajax({
                            url: "clearBREApprovedInvoicesForPayment.action?selectedClaimIds=" + param,
                            success: function()
                            {
                                sm2.clearSelections();
                                ds.reload();
                                refreshFilterPanel();
                            }
                        });
                    }
                }
            }
        }); 
        
        var doInvoicePaymentReceivedAction = new Ext.Action
        ({
            text: 'Update Claim(s) To Payment Received',
            hidden:isInsurer,
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

                        var param = selectedIDs.join(",")

                        $.ajax({
                            url: "doInvoicePaymentReceivedAction.action?selectedClaimIds=" + param,
                            success: function()
                            {
                                sm2.clearSelections();
                                ds.reload();
                                refreshFilterPanel();
                            }
                        });
                        
                    }
                }
            }
        });

        var wgSelectionDlg;
        var doClaimRoutedAction = new Ext.Action({
           
            text: 'Route Claim(s)',
            hidden:isCho,
            handler: function(){

                if(!wgSelectionDlg)
                {
                    wgSelectionDlg =  new Ext.Window({
                        applyTo:'wgSelectionDlgHolder',
                        width:410,
                        height:280,
                        modal: true,
                        closeAction:'hide',
                        plain: false,
                        title: 'Route Claim(s)',
                        resizable : false,
                        items: new Ext.Panel({
                            applyTo: 'wgSelectionPanel'
                        }),
                        buttons: [{
                                text:'Ok',
                                handler:function(){

                                    if($("form#routeClaimForm #workgroupId").val()!=""){
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
                                                wgSelectionDlg.hide();
                                            }};
                                        
                                        $("form#routeClaimForm").ajaxSubmit(submitOption);

                                    }else{
                                        propmtErrorMsg('Workgroup could not be blank.');
                                    }

                                }
                            },{
                                text: 'Close',
                                handler: function(){
                                    wgSelectionDlg.hide();
                                }
                            }]
                    });

                    wgSelectionDlg.addListener('beforeshow',
                    function(dialog){

                        var sLocaltion = "div#wgSelectionHolder";
                        var sAction = "GetWorkgroupOnlyDropDownActionByInsurer.action";
                        var sparameters = "";
                        doSectionLoad(sLocaltion, sAction, sparameters);
                                
                    }
                );
                }
                wgSelectionDlg.show(this);
            }
        });

        var coSelectionDlg;
        var doClaimOwnerAction = new Ext.Action({

            text: 'Assign Claim(s) Owner',
            hidden:isCho,
            handler: function(){

                var selectedRecords =  sm2.getSelections();
                var selectedNotMyClaimsIDs = getErrorClaims(selectedRecords, true, false, null);

                if(selectedNotMyClaimsIDs.length<=0){

                    if(!coSelectionDlg)
                    {
                        coSelectionDlg =  new Ext.Window({
                            applyTo:'coSelectionDlgHolder',
                            width:410,
                            height:280,
                            modal: true,
                            closeAction:'hide',
                            plain: false,
                            title: 'Assign Claim(s) Owner',
                            resizable : false,
                            items: new Ext.Panel({
                                applyTo: 'coSelectionPanel'
                            }),
                            buttons: [{
                                    text:'Ok',
                                    handler:function(){

                                        $("form#ownershipClaimForm").validate(
                                        {
                                            rules: {
                                                workgroupId:{required:isClaimOwnerWorkgroupFieldValid},
                                                claimOwnerId:{min:1}
                                            },
                                            messages: {
                                                workgroupId:{required:"You must select 'Workgroup'"},
                                                claimOwnerId:{min:"You must select 'Claim Owner'"}
                                            }
                                        });

                                        function isClaimOwnerWorkgroupFieldValid(){
                                            var bFlag = false;
                                            var isInsurerWorkgroupEnable = false;

                                            if($("#userInsurerWorkgroupEnable").val()!=null && $("#userInsurerWorkgroupEnable").val()!=""){
                                                isInsurerWorkgroupEnable = $("#userInsurerWorkgroupEnable").val();
                                            }

                                            if(isInsurerWorkgroupEnable && ($("#workgroupId").val()<=0 || $("#workgroupId").val()=="")){
                                                bFlag = true;
                                            }

                                            return bFlag;
                                        }
                                    
                                        if($('form#ownershipClaimForm').valid()){

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
                                                    coSelectionDlg.hide();
                                                }};

                                            $("form#ownershipClaimForm").ajaxSubmit(submitOption);
                                        }
                                    }
                                },{
                                    text: 'Close',
                                    handler: function(){
                                        coSelectionDlg.hide();
                                    }
                                }]
                        });

                        coSelectionDlg.addListener('beforeshow',
                        function(dialog){

                            var insurerId = $("#userInsurerId").val();
                            var isInsurerWorkgroupEnable = false;

                            if($("#userInsurerWorkgroupEnable").val()!=null && $("#userInsurerWorkgroupEnable").val()!=""){
                                isInsurerWorkgroupEnable = $("#userInsurerWorkgroupEnable").val();
                            }

                            if(isInsurerWorkgroupEnable){
                                var sLocaltion = "div#coSelectionHolder";
                                var sAction = "user/GetWorkgroupDropDownActionByInsurer.action";
                                var sparameters = "";
                                doSectionLoad(sLocaltion, sAction, sparameters);
                            }

                            var sLocaltion = "#coClaimHandlerRoleUserDropDownDiv";
                            var sAction = "user/ClaimHandlerRoleUserDropDownAction.action";
                            var sparameters = "workgroupId=-1&insurerId="+insurerId;
                            doSectionLoad(sLocaltion, sAction, sparameters);
                        }

                    );
                    }

                    coSelectionDlg.show(this);

                }
            }
        });

        var updateClaimOwnershipSelectionDlg;
        var doUpdateClaimOwnerAction = new Ext.Action({

            text: 'Update Claim(s) Workgroup And Claim Owner',
            hidden:isCho,
            handler: function(){

                var allowStatuses = ["AwaitingCarHireInfo", "AwaitingInvoiceData", "AwaitingInvoicePayment",
                    "ClaimPending", "ClaimReferredToEngineer", "ClaimRejected",
                    "ClaimRejectionContested", "ClaimUnacknowledgedRouted", "ClaimUpdatedByEngineer",
                    "ContestedInvoiceReferredToCHO", "ContestedInvoiceReferredToInsurer", "InvoiceApprovedByBRE",
                    "InvoiceDataCalculationIncorrect", "InvoiceEscalated", "InvoiceEscalatedToHandler",
                    "InvoicePaymentLogged", "PaymentReceived"];

                var selectedRecords =  sm2.getSelections();
                var selectedNotMyClaimsIDs = getErrorClaims(selectedRecords, true, false, allowStatuses);

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

                                        $("form#ClaimOwnershipUpdateForm").validate(
                                        {
                                            rules: {
                                                workgroupId:{required:isUpdateClaimOwnerWorkgroupFieldValid},
                                                claimOwnerId:{min:1}
                                            },
                                            messages: {
                                                workgroupId:{required:"You must select 'Workgroup'"},
                                                claimOwnerId:{min:"You must select 'Claim Owner'"}
                                            }
                                        });

                                        function isUpdateClaimOwnerWorkgroupFieldValid(){
                                            var bFlag = false;

                                            var isInsurerWorkgroupEnable = false;

                                            if($("#userInsurerWorkgroupEnable").val()!=null && $("#userInsurerWorkgroupEnable").val()!=""){
                                                isInsurerWorkgroupEnable = $("#userInsurerWorkgroupEnable").val();
                                            }

                                            if(isInsurerWorkgroupEnable && ($("#workgroupId").val()<=0 || $("#workgroupId").val()=="")){
                                                bFlag = true;
                                            }

                                            return bFlag;
                                        }

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

                        updateClaimOwnershipSelectionDlg.addListener('beforeshow',
                        function(dialog){
                            var insurerId = $("#userInsurerId").val();
                            var isInsurerWorkgroupEnable = false;

                            if($("#userInsurerWorkgroupEnable").val()!=null && $("#userInsurerWorkgroupEnable").val()!=""){
                                isInsurerWorkgroupEnable = $("#userInsurerWorkgroupEnable").val();
                            }

                            if(isInsurerWorkgroupEnable){
                                var sLocaltion = "div#couSelectionHolder";
                                var sAction = "user/GetCouWorkgroupDropDownActionByInsurer.action";
                                var sparameters = "";
                                doSectionLoad(sLocaltion, sAction, sparameters);
                            }

                            var sLocaltion = "#couClaimHandlerRoleUserDropDownDiv";
                            var sAction = "user/ClaimHandlerRoleUserDropDownAction.action";
                            var sparameters = "workgroupId=-1&insurerId="+insurerId;
                            doSectionLoad(sLocaltion, sAction, sparameters);
                        }
                    );
                    }

                    updateClaimOwnershipSelectionDlg.show(this);

                }
            }
        });

        var actionMenu = new Ext.Toolbar.MenuButton({
            text: 'More actions',            
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

            var selectedRecords = sm2.getSelections();

            var isApprovePaymentAccessibile = <s:property value="IsApprovePaymentAccessibile"/>;
            if(isApprovePaymentAccessibile){
                    
                if(isSelectedRecordsMatchGivenStatus(selectedRecords,'AwaitingInvoicePayment'))
                {   
                    approvedInvoicesPaymentAction.enable(); 
                }
                else
                {
                    approvedInvoicesPaymentAction.disable(); 
                }
                
            }else{

                approvedInvoicesPaymentAction.disable();
            }
            
            var isClearBREApprovedInvoicesForPaymentAccessibile = <s:property value="IsClearBREApprovedInvoicesForPaymentAccessibile"/>;
            if(isClearBREApprovedInvoicesForPaymentAccessibile){
                
                if(isSelectedRecordsMatchGivenStatus(selectedRecords,'InvoiceApprovedByBRE'))
                {   
                    clearBREApprovedInvoicesForPaymentAction.enable(); 
                }
                else
                {
                    clearBREApprovedInvoicesForPaymentAction.disable(); 
                }
                
            }else{
                
                clearBREApprovedInvoicesForPaymentAction.disable();
                
            }

            var isDoInvoicePaymentReceivedAccessibile = <s:property value="IsDoInvoicePaymentReceivedAccessibile"/>;
            if(isDoInvoicePaymentReceivedAccessibile){
                if(isSelectedRecordsMatchGivenStatus(selectedRecords,'InvoicePaymentLogged'))
                {   
                    doInvoicePaymentReceivedAction.enable(); 
                }
                else
                {
                    doInvoicePaymentReceivedAction.disable();
                }  
            }else{
                doInvoicePaymentReceivedAction.disable();
            }

            var isDoClaimRoutedAccessibile = <s:property value="IsDoClaimRoutedAccessibile"/>;
            if(isDoClaimRoutedAccessibile){
                
                if(isSelectedRecordsMatchGivenStatus(selectedRecords,'ClaimUnacknowledgedUnrouted'))
                {   
                    doClaimRoutedAction.enable(); 
                }
                else
                {
                    doClaimRoutedAction.disable(); 
                }
                
            }else{
                doClaimRoutedAction.disable();
            }

            var IsDoClaimOwnershipAccessibile = <s:property value="IsDoClaimOwnershipAccessibile"/>;
            if(IsDoClaimOwnershipAccessibile){

                if(isSelectedRecordsMatchGivenStatus(selectedRecords,'ClaimUnacknowledgedUnassigned'))
                {
                    doClaimOwnerAction.enable();
                }
                else
                {
                    doClaimOwnerAction.disable();
                }

            }else{
                doClaimOwnerAction.disable();
            }

            var isDoUpdateClaimOwnershipAccessibile = <s:property value="IsDoUpdateClaimOwnershipAccessibile"/>;

            if(isDoUpdateClaimOwnershipAccessibile){

                if(isInsurer && selectedRecords.length > 0){
                    doUpdateClaimOwnerAction.enable();
                }else{
                    doUpdateClaimOwnerAction.disable();
                }

            }else{
                doUpdateClaimOwnerAction.disable();
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
                        return '<a href="openClaimDetail.action?id=' + r.data['id'] + '&tab=' + currentTabIndex + '">' + value + '</a>'}},               
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
            sm:sm2,
            stripeRows: true,
            layout:'fit',
            autoHeight:true,
            enableHdMenu:false,
            title:'Claims', 
            viewConfig:{forceFit:true},
            bbar: pagingBar,
            tbar:[actionMenu]
            
        });

        ds.on('load',function()
        {
            $('.x-grid3-hd-checker').removeClass('x-grid3-hd-checker-on');
        });
        
        grid.render('gridHolder');
        grid.getSelectionModel().selectFirstRow();

    }

    function isSelectedRecordsMatchGivenStatus(selectedRecords,status)
    {
        if(selectedRecords.length > 0)
        {
            for(i = 0; i < selectedRecords.length; i ++)       
            {
                var s = selectedRecords[i].json.status;
                if(status != s)
                {
                    return false;
                }        
            }   
            return true;
        }
        else
        {
            return false;
        }      
    }
    
    
    function setupTabPanels()
    {

        currentTabIndex = <s:property value="tab" />;

        tabs = new Ext.TabPanel({
            renderTo: 'tabPanel',
            autoheight:true,
            activeTab: currentTabIndex,
            items:[
                {title:'', id:'emptyTabId', hidden:true, listeners: {activate: handleActivate}},
    <s:if test="menuAccessibility.isDashBoardMenuAccessibility">
                    {contentEl:'boardPanelTab', title:'Dashboard', listeners: {activate: handleActivate}},
    </s:if>
                    {contentEl:'filterPanelTab', title:'Inbox', listeners: {activate: handleActivate}},
                    {contentEl:'searchPanelTab', title:'Search', listeners: {activate: handleActivate}}
    <s:if test="menuAccessibility.isReportMenuAccessibility">
                    ,{contentEl:'reportPanelTab', title:'Reports', listeners: {activate: handleActivate}}
    </s:if>
    <s:if test="menuAccessibility.isAdminMenuAccessibility">
                    ,{contentEl:'adminPanelTab', title:'Admin', listeners: {activate: handleActivate}}
    </s:if>
                ]
            });

            tabs.remove('emptyTabId', true);

        }

        function random_number(){
            var min = 10000000;
            var max = 99999999;
            return (Math.round((max-min) * Math.random() + min));
        }
             
        function loadDataFromSession()
        {
            $.get("getPageIndexOfCurrentSearch.action?rdt=" + random_number(), function(data){
                var start = parseInt(data.trim());
                if(start >= 0)
                {
                    doDataLoad(start, recordPerPage, true);
                }
            });
        }
    
        function handleActivate(tab){
        
            $("#gridPanel").hide();

            if(tab.title == 'Inbox' || tab.title == 'Search'){
                doDataLoad(0, 0, null);
                $("#gridPanel").show();
            }

            if(tabs)
            {
                currentTabIndex = tabs.items.indexOf(tabs.getActiveTab());
            }
        
    <s:if test="menuAccessibility.isAdminMenuAccessibility">
            $("#admin_param_panel").load("loadAdminPanel.action?adminPanelName=NONE");
    </s:if>
            
            $('.x-grid3-hd-checker').removeClass('x-grid3-hd-checker-on');
        
        }
   
        function refreshFilterPanel()
        {
            ajax.loadHtml("getFilterRecordCounters.action",null,function(data){
                $("#filterPanel").html(data);
            });
        }
    
        function doDataLoad(start, recordPerPage, isSearched){

            ds.load(
            {
                params:
                    {
                    start:start,
                    limit:recordPerPage,
                    isSearched:isSearched
                }
            });
        }

        Ext.onReady(function(){

            setupTabPanels();
            setupGrid();
            
            var isChoxAdmin = <s:property value="isChoxAdmin"/>;
            if(!isChoxAdmin){
                activityMonitor.refreshViewingStatus();
            }

            loadDataFromSession();
        });

</script>

<body>
    <div class="outer" id="outerDiv">

        <div class="inner">

            <div id="chox-menu">

                <table cellpadding="0" cellspacing="0" border="0" width="100%">
                    <tr valign="middle">
                        <td>
                            <img src="<%= request.getContextPath()%>/images/chox_logo_small.jpg" style="display: inline; float: left" alt="" />
                        </td>
                        <td width="100%" align="right">

                            <ul id="top-menu">
                                <li><a href="<s:url action="inbox"/>">&nbsp;Home&nbsp;</a></li>
                                <li><a href="<s:url action="openUserAccount" />">|&nbsp;Settings&nbsp;</a></li>
                                <s:if test="isCHO"><li><a href='<s:url action="uploadClaims"/>'>|&nbsp;XML Uploads&nbsp;</a></li></s:if>
                                <s:if test="!isChoxAdmin"><li><a href="javascript:openHelpFile('<%= request.getContextPath()%>',<s:property value="roleTypeForHelpFile" />);">|&nbsp;Help&nbsp;</a></li></s:if>
                                <li><a href="#" onmouseover="mopen('m2')" onmouseout="mclosetime()">|&nbsp;Support&nbsp;</a>
                                    <div id="m2" onmouseover="mcancelclosetime()" onmouseout="mclosetime()">
                                        <a href="javascript:openSupportFile('<%= request.getContextPath()%>');">Support Procedure</a>
                                        <a href="<s:url action="onlineSupport"/>">Online Support Form</a>
                                    </div></li>
                                <li><a href="javascript:onOpenAbout();">|&nbsp;About CHOX&nbsp;</a></li>
                                <li><a href="<%=request.getContextPath()%>/j_spring_security_logout" >|&nbsp;<b><s:property value="CurrentUserDesc" /></b> ( Log Off )</a></li>
                            </ul>

                            <div style="clear:both"></div>

                        </td>
                    </tr>
                </table>   
            </div>

            <div id="tabPanel"></div>

            <s:if test="menuAccessibility.isDashBoardMenuAccessibility">
                <div id="boardPanelTab" class="x-hide-display">
                    <div id="boardPanel">
                        <s:if test="isInsurer">
                            <s:action name="showInsurerBoardHeader" namespace="/user" executeResult="true" />
                        </s:if> 
                        <s:if test="isCHO">
                            <s:action name="showChoBoardHeader" namespace="/user" executeResult="true" />
                        </s:if>
                    </div>
                </div>
            </s:if>

            <div id="filterPanelTab" class="x-hide-display">
                <div id="filterPanel">
                    <s:action name="getFilterRecordCounters" namespace="/user" executeResult="true" />
                </div>
            </div>

            <div id="searchPanelTab" style="background: #dfe8f6; height:340px;" class="x-hide-display">
                <div id="searchPanel">
                    <s:action name="searchClaim" namespace="/user" executeResult="true" /> 
                </div>
            </div>

            <s:if test="menuAccessibility.isReportMenuAccessibility">
                <div id="reportPanelTab" class="x-hide-display">
                    <div id="reportPanel">
                        <s:action name="buildReport" namespace="/user" executeResult="true" />
                    </div>
                </div>
            </s:if>

            <s:if test="menuAccessibility.isAdminMenuAccessibility">
                <div id="adminPanelTab" class="x-hide-display">
                    <div id="adminPanel">
                        <s:action name="adminFunction" namespace="/user" executeResult="true" />
                    </div>
                </div>
            </s:if>

            <div id="gridPanel">

                <div id="gridHolder"></div>

                <div class="excel-export">
                    <form name="thisForm" action=""><a href="javascript:doExportExcel();">Export To Excel</a></form>
                </div>

                <div id="wgSelectionDlgHolder" class="x-hidden">
                    <div id="wgSelectionPanel">
                        <form id="routeClaimForm" action="<%=request.getContextPath()%>/user/doClaimRoutedAction.action" class="XXentity-form">
                            <input name="selectedClaimIds" type="hidden" />
                            <table class="selectionForm" cellspacing="0" cellpadding="0" border="0">
                                <tr>
                                    <th colspan="2"><label>Please select the 'Workgroup' in order to route the claim(s) to the relevant handling team.</label></th>
                                </tr>
                                <tr>
                                    <td><label>Workgroup</label></td>
                                    <td><div id="wgSelectionHolder"></div></td>
                                </tr>
                            </table>
                        </form>
                    </div>
                </div>

                <div id="coSelectionDlgHolder" class="x-hidden">
                    <div id="coSelectionPanel">
                        <form id="ownershipClaimForm" action="<%=request.getContextPath()%>/user/doClaimOwnershipAction.action" class="XXentity-form">

                            <input name="selectedClaimIds" type="hidden" />
                            <input id="userInsurerId" name="userInsurerId" value="<s:property value="AuthenticatedUser.user.insurer.id"/>" type="hidden"/>
                            <input id="userInsurerWorkgroupEnable" name="userInsurerWorkgroupEnable" value="<s:property value="AuthenticatedUser.user.insurer.workgroupEnable"/>" type="hidden"/>

                            <table class="selectionForm" cellspacing="0" cellpadding="0" border="0" width="100%">
                                <tr>
                                    <th colspan="2"><label>Please assign the claim(s) with a Claim Owner.</label></th>
                                </tr>
                                <s:if test="AuthenticatedUser.user.insurer.workgroupEnable">
                                    <tr>
                                        <td class="pop-claim-ownership-label"><label>Workgroup</label></td>
                                        <td class="pop-claim-ownership-column"><div id="coSelectionHolder"></div></td>
                                    </tr>
                                </s:if>
                                <tr>
                                    <td class="pop-claim-ownership-label" style="height:60px;"><label>Claim Owner</label></td>
                                    <td class="pop-claim-ownership-column"><div id="coClaimHandlerRoleUserDropDownDiv"></div></td>
                                </tr>
                            </table>
                        </form>
                    </div>
                </div>

                <div id="couSelectionDlgHolder" class="x-hidden">
                    <div id="couSelectionPanel">
                        <form id="ClaimOwnershipUpdateForm" action="<%=request.getContextPath()%>/user/doClaimOwnershipUpdateAction.action" class="XXentity-form">

                            <input name="selectedClaimIds" type="hidden" />
                            <input id="userInsurerId" name="userInsurerId" value="<s:property value="AuthenticatedUser.user.insurer.id"/>" type="hidden"/>
                            <input id="userInsurerWorkgroupEnable" name="userInsurerWorkgroupEnable" value="<s:property value="AuthenticatedUser.user.insurer.workgroupEnable"/>" type="hidden"/>

                            <table class="selectionForm" cellspacing="0" cellpadding="0" border="0" width="100%">
                                <tr>
                                    <th colspan="2"><label>Please update the claim(s) with a Workgroup and Claim Owner.</label></th>
                                </tr>
                                <s:if test="AuthenticatedUser.user.insurer.workgroupEnable">
                                    <tr>
                                        <td class="pop-claim-ownership-label"><label>Workgroup</label></td>
                                        <td class="pop-claim-ownership-column"><div id="couSelectionHolder"></div></td>
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

        </div>

    </div>

    <div class="footerText">
        ©2009 Sherwood Compliance Services Ltd | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','Copyright');" class="footerText">Copyright</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','PrivacyPolicy');" class="footerText">Privacy Policy</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','TermsOfService');" class="footerText">Terms of Service</a>
    </div>

</body>