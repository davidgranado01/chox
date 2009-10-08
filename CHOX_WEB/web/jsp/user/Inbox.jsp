<%-- 
    Document   : Inbox
    Created on : 10-Nov-2008, 22:13:54
    Author     : Emmanuel
--%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<head>
    <title>IDAS-CHOX</title>
    <link href="<%= request.getContextPath()%>/styles/chox.css" rel="stylesheet" type="text/css" media="all"/>        
    <link href="<%= request.getContextPath()%>/css/ext-all.css" rel="stylesheet" type="text/css" media="all"/>
    <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery-1.2.6.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.form.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/ext-jquery-adapter.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.blockUI.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.timer.js"></script>      
    <script src="<%= request.getContextPath()%>/scripts/ext-base.js" type="text/javascript"></script>
    <script src="<%= request.getContextPath()%>/scripts/ext-all.js" type="text/javascript"></script> 
    <script src="<%= request.getContextPath()%>/scripts/Application.js" type="text/javascript"></script> 
    <script src="<%= request.getContextPath()%>/scripts/general.js" type="text/javascript"></script> 
</head>

<script >
    
    var currentTabIndex;
    var tabs;
    var recordPerPage = 20;
    
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
            // {name:'lineOfBusiness'},
            {name:'workgroup'},
            {name:'supplierReference'},
            {name:'claimNumber'}, 
            {name:'lastModifiedDate', type: 'string', dateFormat:'timestamp'},
            {name:'insurer'},
            {name:'cho'}
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
    
    function showClaimByStatus(status)
    {     
        ds.baseParams = {
            
            supplierReference : '',
            supplierId : -1,
            insurerId : -1,
            invoiceNumber : '',
            claimNumber : '',
            vrn : '',
            claimUploadDateFrom : '',
            claimUploadDateTo : '',
            invoiceUploadDateFrom : '',
            invoiceUploadDateTo :  '',                
            hireDateFrom : '',
            hireDateTo : '',          
            status : status,
            //lineOfBusinessId : -1,
            workgroupId : -1,
            isAnomalies : '',
            ispenaltyChargeApplied : ''
        }
        ds.load(
        {
            params:
                {            
                start:0,
                limit:recordPerPage
            }
        });
    }  
    
    function showClaimByStatusWithSort(status,sort)
    {     
        ds.setDefaultSort(sort, 'status');
        ds.baseParams = {            
            supplierReference : '',
            supplierId : -1,
            insurerId : -1,
            invoiceNumber : '',
            claimNumber : '',
            vrn : '',
            claimUploadDateFrom : '',
            claimUploadDateTo : '',
            invoiceUploadDateFrom : '',
            invoiceUploadDateTo :  '',                
            hireDateFrom : '',
            hireDateTo : '',          
            status : status,
            //lineOfBusinessId : -1,
            workgroupId : -1,
            isAnomalies : '',
            ispenaltyChargeApplied : ''
        }
        ds.load(
        {
            params:
                {            
                start:0,
                limit:recordPerPage
            }
        });
    }  
    
    function showClaimIsAnomalies()
    {  
        ds.baseParams = {
            supplierReference : '',
            supplierId : -1,
            insurerId : -1,
            invoiceNumber : '',
            claimNumber : '',
            vrn : '',
            claimUploadDateFrom : '',
            claimUploadDateTo : '',
            invoiceUploadDateFrom : '',
            invoiceUploadDateTo :  '',                
            hireDateFrom : '',
            hireDateTo : '',         
            status : '',
            //lineOfBusinessId : -1,
            workgroupId : -1,
            isAnomalies : true,
            ispenaltyChargeApplied : ''
        }
        ds.load(
        {
            params:
                {            
                start:0,
                limit:recordPerPage
            }
        });
    }   
    
    function showClaimIspenaltyChargeApplied()
    {       
        ds.baseParams = {
            supplierReference : '',
            supplierId : -1,
            insurerId : -1,
            invoiceNumber : '',
            claimNumber : '',
            vrn : '',
            claimUploadDateFrom : '',
            claimUploadDateTo : '',
            invoiceUploadDateFrom : '',
            invoiceUploadDateTo :  '',                
            hireDateFrom : '',
            hireDateTo : '',          
            status : '',
            // lineOfBusinessId : -1,
            workgroupId : -1,
            isAnomalies : '',
            ispenaltyChargeApplied : true
        }
        ds.load(
        {
            params:
                {            
                start:0,
                limit:recordPerPage
            }
        });
        
    }  
    
    function doExportExcel(){
        window.location= "doExportExcel.action";
    }
    
    function searchClaim()
    {        
        var supplierReference = Ext.query('*[name$=supplierReference]')[0].value;
        var supplierId = Ext.query('*[name$=supplierId]').length > 0 ? Ext.query('*[name$=supplierId]')[0].value : -1;
        var insurerId = Ext.query('*[name$=insurerId]').length > 0 ? Ext.query('*[name$=insurerId]')[0].value : -1;
        var invoiceNumber = Ext.query('*[name$=invoiceNumber]')[0].value;
        var claimNumber = Ext.query('*[name$=claimNumber]')[0].value;
        var vrn = Ext.query('*[name$=vrn]')[0].value;
        var claimUploadDateFrom = Ext.query('*[name$=claimUploadDateFrom]')[0].value;
        var claimUploadDateTo = Ext.query('*[name$=claimUploadDateTo]')[0].value;
        var invoiceUploadDateFrom = Ext.query('*[name$=invoiceUploadDateFrom]')[0].value;
        var invoiceUploadDateTo = Ext.query('*[name$=invoiceUploadDateTo]')[0].value;
        var hireDateFrom = Ext.query('*[name$=hireDateFrom]')[0].value;
        var hireDateTo = Ext.query('*[name$=hireDateTo]')[0].value;    
        var status = Ext.query('*[name$=status]')[0].value;
        // var lineOfBusinessId = Ext.query('*[name$=lineOfBusiness]')[0].value;    
        var workgroupId = Ext.query('*[name$=workgroup]')[0].value;    
        
        ds.baseParams = {
            supplierReference : supplierReference,
            supplierId : supplierId,
            insurerId : insurerId,
            invoiceNumber : invoiceNumber,
            claimNumber : claimNumber,
            vrn : vrn,
            claimUploadDateFrom : claimUploadDateFrom,
            claimUploadDateTo : claimUploadDateTo,
            invoiceUploadDateFrom : invoiceUploadDateFrom,
            invoiceUploadDateTo : invoiceUploadDateTo,                
            hireDateFrom : hireDateFrom,
            hireDateTo : hireDateTo,         
            status : status,
            // lineOfBusinessId : lineOfBusinessId,
            isAnomalies : '',
            ispenaltyChargeApplied : '',
            workgroupId: workgroupId
        }
        
        ds.load(
        {
            params:
                {
                start:0,
                limit:recordPerPage
            }
        });
        
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
        
        var approvedInvoicesPaymentAction = new Ext.Action
        ({
            text: 'Update Claim(s) To Invoice Payment Logged',
            handler: function(){
                if(confirm('Are you sure you want to perform this action?'))
                {
                    var selectedRecords =  sm2.getSelections();  
                    var selectedIDs = $.map(selectedRecords, function(n){
                        return n.json.id;
                    });

                    var param = selectedIDs.join(",")

                    $.ajax({
                        url: "logInvoicePayments.action?selectedClaimIds=" + param,
                        success: function()
                        {
                            ds.reload();
                            refreshFilterPanel();
                        }
                    });
                }
            }
        }); 
    
       var clearBREApprovedInvoicesForPaymentAction = new Ext.Action
        ({
            text: 'Approve Claim(s) For Payment',
            handler: function(){
                if(confirm('Are you sure you want to perform this action?'))
                {
                    var selectedRecords =  sm2.getSelections();  
                    var selectedIDs = $.map(selectedRecords, function(n){
                        return n.json.id;
                    });

                    var param = selectedIDs.join(",")

                    $.ajax({
                        url: "clearBREApprovedInvoicesForPayment.action?selectedClaimIds=" + param,
                        success: function()
                        {
                            ds.reload();
                            refreshFilterPanel();
                        }
                    });
                }
            }
        }); 
        
       var doInvoicePaymentReceivedAction = new Ext.Action
        ({
            text: 'Update Claim(s) to Payment Received',
            handler: function(){
                if(confirm('Are you sure you want to perform this action?'))
                {
                    var selectedRecords =  sm2.getSelections();  
                    var selectedIDs = $.map(selectedRecords, function(n){
                        return n.json.id;
                    });

                    var param = selectedIDs.join(",")

                    $.ajax({
                        url: "doInvoicePaymentReceivedAction.action?selectedClaimIds=" + param,
                        success: function()
                        {
                            ds.reload();
                            refreshFilterPanel();
                        }
                    });
                }
            }
        });

        //Emm 28/08/2009
        //route claim from menu
        //only for user with role : Claim router

       var lobSelectionDlg;

       
        var doClaimRoutedAction = new Ext.Action
        ({
            text: 'Route Claim(s)',
            handler: function(){     

                    if(!lobSelectionDlg)
                    {
                            lobSelectionDlg =  new Ext.Window({
                                applyTo:'lobSelectionDlgHolder',
                                width:410,
                                modal: true,
                                closeAction:'hide',
                                plain: false,
                                title: 'Route Claim(s)',
                                resizable : false,
                                items: new Ext.Panel({
                                    applyTo: 'lobSelectionPanel'
                                }),
                                buttons: [{
                                        text:'ok',
                                        handler:function(){
                                            var selectedRecords =  sm2.getSelections();
                                            var selectedIDs = $.map(selectedRecords, function(n){
                                                return n.json.id;
                                            });
                                            var param = selectedIDs.join(",");
                                            $('form#routeClaimForm input[name="selectedClaimIds"]').val(param);
                                            
                                            $("form#routeClaimForm").validate(
                                            {
                                                errorLabelContainer: "#HMmessageBox",
                                                rules: {
                                                    lineOfBusiness:{
                                                        required:true
                                                    }
                                                },
                                                messages: {
                                                    lineOfBusiness:{
                                                        required:"You must select work grop.'"
                                                    }
                                                }
                                            });
                                            
                                            if($('form#routeClaimForm').valid()){

                                                var submitOption = {
                                                    clearForm: true,
                                                    success:function(){
                                                    ds.reload();
                                                    refreshFilterPanel();
                                                    lobSelectionDlg.hide();
                                                }};
                                                $("form#routeClaimForm").ajaxSubmit(submitOption);
                                                
                                            }
                                            else{
                                                propmtErrorMsg('Work group could not be blank.');
                                            }
                                        }
                                    },{
                                        text: 'Close',
                                        handler: function(){
                                            lobSelectionDlg.hide();
                                        }
                                    }]
                            });

                            lobSelectionDlg.addListener('beforeshow',
                                function(dialog){
                                    $('div#lobSelectionHolder').load('user/LineOfBusinessDropDownAction.action');
                                }
                            );
                        }

                    lobSelectionDlg.show(this);

                    

            }
        });

        //end route claim

        var actionMenu = new Ext.Toolbar.MenuButton({
            text: 'More actions',            
            tooltip: {text:'', title:'More actions'},
            menu : {items: [doClaimRoutedAction, clearBREApprovedInvoicesForPaymentAction, approvedInvoicesPaymentAction, doInvoicePaymentReceivedAction]}
        });
        
        actionMenu.on('arrowclick', function()
        {

            var selectedRecords = sm2.getSelections();
            
            
            <s:if test="IsApprovePaymentAccessibile">  
                if(isSelectedRecordsMatchGivenStatus(selectedRecords,'AwaitingInvoicePayment'))
                {   
                    approvedInvoicesPaymentAction.enable(); 
                }
                else
                {
                    approvedInvoicesPaymentAction.disable(); 
                }  
            </s:if>
            <s:else>
                approvedInvoicesPaymentAction.disable();
                
            </s:else>   
            
            <s:if test="IsClearBREApprovedInvoicesForPaymentAccessibile"> 
                if(isSelectedRecordsMatchGivenStatus(selectedRecords,'InvoiceApprovedByBRE'))
                {   
                    clearBREApprovedInvoicesForPaymentAction.enable(); 
                }
                else
                {
                    clearBREApprovedInvoicesForPaymentAction.disable(); 
                }  
            </s:if>
            <s:else>
                clearBREApprovedInvoicesForPaymentAction.disable();
            </s:else>

            <s:if test="IsDoInvoicePaymentReceivedAccessibile"> 
                if(isSelectedRecordsMatchGivenStatus(selectedRecords,'InvoicePaymentLogged'))
                {   
                    doInvoicePaymentReceivedAction.enable(); 
                }
                else
                {
                   doInvoicePaymentReceivedAction.disable(); 
                }  
            </s:if>
            <s:else>
                doInvoicePaymentReceivedAction.disable();
            </s:else>                
                
            <s:if test="IsDoClaimRoutedAccessibile"> 
                if(isSelectedRecordsMatchGivenStatus(selectedRecords,'ClaimUnacknowledgedUnrouted'))
                {   
                    doClaimRoutedAction.enable(); 
                }
                else
                {
                    doClaimRoutedAction.disable(); 
                }  
            </s:if>
            <s:else>
                doClaimRoutedAction.disable();
            </s:else>  
                
        }, this);
        
        var grid = new Ext.grid.GridPanel({
            loadMask: true,
            ds: ds,
            width: 960,
            columns: [
                sm2,
                {id:'Id', header: "Supplier Reference", width: 150, sortable: true, dataIndex: 'supplierReference', 
                    renderer:function(value,p,r){
                        return '<a href="openClaimDetail.action?id=' + r.data['id'] + '&tab=' + currentTabIndex + '">' + value + '</a>'}},               
                {header: "Insurer's VRN", width: 250, sortable: true, dataIndex: 'vehicleRegistration'},
                {header: "Claim Number", width: 250, sortable: true, dataIndex: 'claimNumber'}, 
                {header: "Invoice Amount", width: 250, sortable: true, dataIndex: 'invoiceAmount'},  
                {header: "Last Modified", width: 250, sortable: true, dataIndex: 'lastModifiedDate'},
                {header: "Status", width: 250, sortable: true, dataIndex: 'status'},
                {header: "Created By", width: 250, sortable: true, dataIndex: 'createdBy'},
                //{header: "LOB", width: 250, sortable: true, dataIndex: 'lineOfBusiness'},
                {header: "Workgroup", width: 250, sortable: true, dataIndex: 'workgroup'},
                {header: "CHO", width: 250, sortable: true, dataIndex: 'cho'},
                {header: "Insurer", width: 150, sortable: true, dataIndex: 'insurer'},
                {header: "Viewing", width: 150, sortable: false, dataIndex: 'id',renderer:function(value,p,r){
                        return '<input type="hidden" name="viewingId" value="' + value + '" /><label id="viewingLabel_' + value + '" class="std-label-ro">-</label>'}}
            ],
            sm:sm2,
            stripeRows: true,
            layout:'fit',
            autoHeight:true,
            enableHdMenu:false,
            title:'Claims', 
			viewConfig:{forceFit:true},bbar: pagingBar,
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
    }

    function random_number(){
        var min = 10000000;
        var max = 99999999;
        return (Math.round((max-min) * Math.random() + min));
    }

    function refreshViewingStatus()
    {
        var x = [];

        $("input[name='viewingId']").each(function (i) {
            var claimId = $(this).val();
            x.push(claimId);
        });  

        if(x.length > 0)
        {
            $.getJSON('checkViewingStatus.action?claimIds=' + x.join(',') + "&token=" + random_number(),
            function(data){

                $.each(data.results, function(i,result){
                    $("#viewingLabel_" + result.claimId).html(result.status);
                });

            });
        }

        t=setTimeout("refreshViewingStatus()",4000);
    }
    
    function loadDataFromSession()
    {
        $.get("getPageIndexOfCurrentSearch.action", function(data){
            var start = parseInt(data.trim());            
            if(start >= 0)
            {
                ds.load(
                {
                    params:
                        {
                        start:start,
                        limit:recordPerPage
                    }
                });
            }  
        }); 
    }
    
    function handleActivate(tab){
        
        $("#gridPanel").hide();
        
        if(tab.title == 'Inbox' || tab.title == 'Search'){
            ds.load({ params:{start:0,limit:0}});
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
    
    /*
    function clearForm(form) {
        // iterate over all of the inputs for the form
        // element that was passed in
        $(':input', form).each(function() {
            var type = this.type;
            var tag = this.tagName.toLowerCase(); // normalize case
            // it's ok to reset the value attr of text inputs,
            // password inputs, and textareas
            if (type == 'text' || type == 'password' || tag == 'textarea')
                this.value = "";
            // checkboxes and radios need to have their checked state cleared
            // but should *not* have their 'value' changed
            else if (type == 'checkbox' || type == 'radio')
                this.checked = false;
            // select elements need to have their 'selectedIndex' property set to -1
            // (this works for both single and multiple select elements)
            else if (tag == 'select')
                this.selectedIndex = -1;
        });
    };
    */
   
    function refreshFilterPanel()
    {
        $.get("getFilterRecordCounters.action", function(content){
           
        $("#filterPanel").html(content);
           
        }); 
    }

    Ext.onReady(function(){
        
        setupTabPanels();
        setupGrid();
        
        if(!<s:property value="isChoxAdmin"/>){
            refreshViewingStatus();
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
    <li><a href="<%=request.getContextPath()%>/j_acegi_logout" >|&nbsp;<b><s:property value="CurrentUserDesc" /></b> ( Log Off )</a></li>
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
              
            <div id="searchPanelTab" style="height:250px; background: #dfe8f6;" class="x-hide-display">
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
                <div class="excel-export"><form name="thisForm">
                <a href="javascript:doExportExcel();">Export To Excel</a></form></div>

                <div id="lobSelectionDlgHolder" class="x-hidden">
                    <div id="lobSelectionPanel">
                    <form id="routeClaimForm" action="<%=request.getContextPath()%>/user/doClaimRoutedAction.action" class="XXentity-form">

                        <input name="selectedClaimIds" type="hidden" />
                        <table class="selectionForm" cellspacing="0" cellpadding="0" border="0">
                            <tr>
                                <th colspan="2"><label>Please select the group to which claim(s) should be routed.</label></th>
                            </tr>
                            <tr>
                                <td><label>Work Group</label></td>
                                <td><div id="lobSelectionHolder"></div></td>
                            </tr>
                            <tr>
                                <td colspan="2"> &nbsp;</td>
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







