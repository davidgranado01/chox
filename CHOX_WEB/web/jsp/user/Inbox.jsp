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
            {name:'lineOfBusiness'},
            {name:'supplierReference'},
            {name:'claimNumber'}, 
            {name:'createdDate', type: 'date', dateFormat: 'd/m/Y'},
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
    // var c = new Ext.DatePicker({renderTo: 'doSearchClaim_invoiceUploadDateFrom'});
    
    Ext.onReady(setupGrid); 
    
    function showClaimByStatus(status)
    {            
        ds.load(
        {
            params:
                {
                status : status,
                start:0,
                limit:1000
            }
        });
    }    
    
    function showClaimIsAnomalies()
    {  
        ds.baseParams = {
            isAnomalies : true
        }
        ds.load(
        {
            params:
                {            
                start:0,
                limit:10
            }
        });
    }   
    
    function showClaimIsPanaltyChargeApplied()
    {       
        ds.baseParams = {
            
            isPanaltyChargeApplied : true
        }
        ds.load(
        {
            params:
                {            
                start:0,
                limit:10
            }
        });
    }  
    
    function doExportExcel(){
        
        var popwin = window.open("doExportExcel.action", "Excel", "WIDTH=575,HEIGHT=500,RESIZABLE=No,SCROLLBARS=YES,TOOLBAR=NO,LEFT=200,TOP=100");
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
        var lineOfBusinessId = Ext.query('*[name$=lineOfBusiness]')[0].value;    
        
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
            lineOfBusinessId : lineOfBusinessId
        }
        
        ds.load(
        {
            params:
                {
                start:0,
                limit:10
            }
        });
        
    }
    
    function setupGrid(){
        Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
        Ext.QuickTips.init();         
        
        var pagingBar = new Ext.PagingToolbar({
            pageSize: 10,
            store: ds,
            displayInfo: true,
            displayMsg: 'Displaying topics {0} - {1} of {2}',
            emptyMsg: "No claim to display"
        });
        
        
        var grid = new Ext.grid.GridPanel({
            loadMask: true,
            ds: ds,
            width: 960,
            columns: [
                {id:'Id', header: "Supplier Reference", width: 150, sortable: true, dataIndex: 'supplierReference', 
                    renderer:function(value,p,r){
                        return '<a href="openClaimDetail.action?id=' + r.data['id'] + '">' + value + '</a>'}},               
                {header: "Insurer's VRN", width: 250, sortable: true, dataIndex: 'vehicleRegistration'},
                {header: "Claim Number", width: 250, sortable: true, dataIndex: 'claimNumber'}, 
                {header: "Invoice Amount", width: 250, sortable: true, 
                    dataIndex: 'invoiceAmount'},  
                {header: "Date Uploaded", width: 250, sortable: true, 
                    renderer: Ext.util.Format.dateRenderer('d/m/Y'), 
                    dataIndex: 'createdDate'},
                {header: "Status", width: 250, sortable: true, dataIndex: 'status'},
                {header: "Created By", width: 250, sortable: true, dataIndex: 'createdBy'},          
                {header: "LOB", width: 250, sortable: true, dataIndex: 'lineOfBusiness'},
                {header: "CHO", width: 250, sortable: true, dataIndex: 'cho'},
                {header: "Insurer", width: 150, sortable: true, dataIndex: 'insurer'},
                {header: "Viewing", width: 150, sortable: true, dataIndex: 'id',renderer:function(value,p,r){
                        return '<input type="hidden" name="viewingId" value="' + value + '" /><label id="viewingLabel_' + value + '" class="std-label-ro">-</label>'}}
            ],
            stripeRows: true,
            layout:'fit',
            autoHeight:true,
            enableHdMenu:false,
            title:'Claims', viewConfig:{forceFit:true},bbar: pagingBar
            
        });
        grid.render('gridPanel');
        grid.getSelectionModel().selectFirstRow();               
        
        var tabs = new Ext.TabPanel({
            renderTo: 'tabPanel',
            autoheight:true,
            activeTab: 0,
            items:[
                {contentEl:'filterPanelTab', title:'Inbox'},
                {contentEl:'searchPanelTab', title:'Search'}
            ]
        });  
        
        
        
        var claimUploadDateFromPicker = new Ext.form.DateField({
            name: 'claimUploadDateFrom',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            showWeekNumber: true
        });
        
        var claimUploadDateToPicker = new Ext.form.DateField({
            name: 'claimUploadDateTo',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            showWeekNumber: true
        });
        
        var invoiceUploadDateFromPicker = new Ext.form.DateField({
            name: 'invoiceUploadDateFrom',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            showWeekNumber: true
        });
        
        var invoiceUploadDateToPicker = new Ext.form.DateField({
            name: 'invoiceUploadDateTo',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            showWeekNumber: true
        });
        
        var hireDateFromPicker = new Ext.form.DateField({
            name: 'hireDateFrom',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            showWeekNumber: true
        });
        
        var hireDateToPicker = new Ext.form.DateField({
            name: 'hireDateTo',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            showWeekNumber: true
        });
        
        claimUploadDateFromPicker.render('claimUploadDateFromDiv');
        claimUploadDateToPicker.render('claimUploadDateToDiv');
        invoiceUploadDateFromPicker.render('invoiceUploadDateFromDiv');
        invoiceUploadDateToPicker.render('invoiceUploadDateToDiv');
        hireDateFromPicker.render('hireDateFromDiv');
        hireDateToPicker.render('hireDateToDiv');
    }     
    
    $(document).everyTime(3000, function() {
        refreshViewingStatus();
    });
    
    function refreshViewingStatus()
    {
        var x = [];
        
        $("input[name='viewingId']").each(function (i) {
            var claimId = $(this).val();
            x.push(claimId);
        });  
        
        if(x.length > 0)
        {
            $.getJSON('checkViewingStatus.action?claimIds=' + x.join(','),
            function(data){
                
                $.each(data.results, function(i,result){
                    $("#viewingLabel_" + result.claimId).html(result.status);
                });
                
            });
        }
    }
    
</script>
    

<body>
    
    
    <div class="outer" id="outerDiv">
        
        <div class="inner">
            
            
            <div id="chox-menu">
                <table cellpadding="0" cellspacing="0" border="0" width="100%">
                    <tr valign="middle">
                        <td>
                            <img src="<%= request.getContextPath()%>/images/chox_logo_small.jpg" style="display: inline; float: left" />
                        </td>
                        <td width="100%" align="right">
                            <div class="top-menu">
                                <a href="<s:url action="inbox"/>">Home</a>&nbsp;|&nbsp;
                                    <s:if test="isCHO">
                                    <a href='<s:url action="uploadClaims"/>'>XML Uploads</a>&nbsp;|&nbsp;
                                    </s:if> 
                                    <s:if test="isCHO">
                                        <a href="javascript:openFile('<%= request.getContextPath()%>','ChoHelp');">Help</a>
                                    </s:if>
                                    <s:else>
                                        <a href="javascript:openFile('<%= request.getContextPath()%>','InsHelp');">Help</a>
                                    </s:else>&nbsp;|&nbsp;
                                <a href="javascript:openFile('<%= request.getContextPath()%>','Support');">Support</a>&nbsp;|&nbsp; 
                                <a href="javascript:onOpenAbout();">About CHOX</a>&nbsp;|&nbsp;
                                <b><s:property value="CurrentUserDesc" /></b>&nbsp;&nbsp;<a href="<%=request.getContextPath()%>/j_acegi_logout">( Log Off )</a>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
            
            
            
            <div id="tabPanel">
                
                
                
                
                <div id="filterPanelTab">
                    
                    <ul class="inbox">
                        <s:if test="filterAccessibility.isRejectedClaimsAccessible">
                            <li><a href="javascript:showClaimByStatus('ClaimRejected');" >Rejected Claims (<s:property value="filterRecordCounter.rejectedClaimsCount" />)</a></li>
                        </s:if> 
                        <s:if test="filterAccessibility.isIncorrectInvoiceDataCalculationsAccessible">
                            <li><a href="javascript:showClaimByStatus('InvoiceDataCalculationIncorrect');" >Incorrect Invoice Data Calculations (<s:property value="filterRecordCounter.incorrectInvoiceDataCalculationsCount" />)</a></li>
                        </s:if> 
                        <s:if test="filterAccessibility.isContestedInvoicesReferredToCHOAccessible">
                            <li><a href="javascript:showClaimByStatus('ContestedInvoiceReferredToCHO');" >Contested Invoices Referred To CHO (<s:property value="filterRecordCounter.contestedInvoicesReferredToCHOCount" />)</a></li>
                        </s:if> 
                        <s:if test="filterAccessibility.isClaimsAwaitingHireMonitoringInformationAccessible">
                            <li><a href="javascript:showClaimByStatus('AwaitingCarHireInfo');" >Claims Awaiting Hire Monitoring Information (<s:property value="filterRecordCounter.claimsAwaitingHireMonitoringInformationCount" />)</a></li>
                        </s:if> 
                        <s:if test="filterAccessibility.isClaimsAwaitingAcknowledgementAccessible">
                            <li><a href="javascript:showClaimByStatus('ClaimUnacknowledgedRouted');" >Claims Awaiting Acknowledgement (<s:property value="filterRecordCounter.claimsAwaitingAcknowledgementCount" />)</a></li>
                        </s:if> 
                        <s:if test="filterAccessibility.isReSubmittedClaimsAwaitingAcknowledgementAccessible">
                            <li><a href="javascript:showClaimByStatus('ClaimRejectionContested');" >Re-Submitted Claims Awaiting Acknowledgement (<s:property value="filterRecordCounter.reSubmittedClaimsAwaitingAcknowledgementCount" />)</a></li>
                        </s:if>
                        <s:if test="filterAccessibility.isHireUpdateAnomaliesAccessible">
                            <li><a href="javascript:showClaimIsAnomalies();" >Hire Update Anomalies (<s:property value="filterRecordCounter.hireUpdateAnomaliesCount" />)</a></li>
                        </s:if>
                        <s:if test="filterAccessibility.isNewClaimsToBeroutedAccessible">
                            <li><a href="javascript:showClaimByStatus('ClaimUnacknowledgedUnrouted');" >New Claims to be Routed (<s:property value="filterRecordCounter.newClaimsToBeroutedCount" />)</a></li>
                        </s:if> 
                        <s:if test="filterAccessibility.isApprovedInvoicesAwaitingPaymentAccessible">
                            <li><a href="javascript:showClaimByStatus('AwaitingInvoicePayment');" >Approved Invoices Awaiting Payment (<s:property value="filterRecordCounter.approvedInvoicesAwaitingPaymentCount" />)</a></li>
                        </s:if>
                        <s:if test="filterAccessibility.isEscalatedInvoicesAccessible">
                            <li><a href="javascript:showClaimByStatus('InvoiceEscalated');" >Escalated Invoices (<s:property value="filterRecordCounter.escalatedInvoicesCount" />)</a></li>
                        </s:if> 
                        <s:if test="filterAccessibility.isContestedInvoicesReferredToInsurerAccessible">
                            <li><a href="javascript:showClaimByStatus('ContestedInvoiceReferredToInsurer');" >Contested Invoices Referred To Insurer (<s:property value="filterRecordCounter.contestedInvoicesReferredToInsurerCount" />)</a></li>
                        </s:if>
                        <s:if test="filterAccessibility.isInvoicesApprovedByBREAccessible">
                            <li><a href="javascript:showClaimByStatus('InvoiceApprovedByBRE');" >Invoices Approved By BRE (<s:property value="filterRecordCounter.invoicesApprovedByBRECount" />)</a></li>
                        </s:if>
                        <s:if test="filterAccessibility.isClaimReferredToEngineerAccessible">
                            <li><a href="javascript:showClaimByStatus('ClaimReferredToEngineer');" >Claim Referred To Engineer (<s:property value="filterRecordCounter.ClaimReferredToEngineerCount" />)</a></li>
                        </s:if>
                        <s:if test="filterAccessibility.isClaimReferredToFNOLAccessible">
                            <li><a href="javascript:showClaimByStatus('ClaimReferredToFNOL');" >Claims To Be Registered (<s:property value="filterRecordCounter.ClaimReferredToFNOLCount" />)</a></li>
                        </s:if>          
                        <s:if test="filterAccessibility.isPenaltyChargesAppliedAccessible">
                            <li><a href="javascript:showClaimIsPanaltyChargeApplied();" >Penalty Charges To Be Applied (<s:property value="filterRecordCounter.PenaltyChargesAppliedCount" />)</a></li>
                        </s:if>    
                    </ul>
                    
                </div>
                <div id="searchPanelTab" class="x-hide-display">
                    <div id="searchPanel">
                        
                        <s:action name="searchClaim" namespace="/user" executeResult="true" /> 
                        
                    </div>
                </div>
                    
            </div>
            
            <div id="gridPanel">
            </div>
            <div class="excel-export"><form name="thisForm">
            <a href="javascript:doExportExcel();">Export To Excel</a></form></div>
        </div>
        
<div class="footerText">
©2008 Sherwood Compliance Services Ltd | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','Copyright');" class="footerText">Copyright</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','PrivacyPolicy');" class="footerText">Privacy Policy</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','TermsOfService');" class="footerText">Terms of Service</a></div>
</body>







