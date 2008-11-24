<%-- 
    Document   : Inbox
    Created on : 10-Nov-2008, 22:13:54
    Author     : Emmanuel
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<head>
    <title><decorator:title default="Inbox"/></title>
    <decorator:head/>
</head>

<script >
    
    

    var rd = new Ext.data.JsonReader({
        totalProperty: 'totalCount',   
        root: 'results', 
        fields:[
            {name:'id'},
            {name:'status'},
            {name:'invoiceAmount'},
            {name:'vehicleRegistration'},
            {name:'policyNumber'},
            {name:'supplierReference'},
            {name:'claimNumber'},
            {name:'created', type: 'date', dateFormat: 'd/m/Y'},
            {name:'insurer'},
            {name:'cho'},
        ]
    });
        
    var ds = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy
        ({url: 'user/doSearchClaim.action',method:'POST'}),
        reader:rd        
    });
    
    // var c = new Ext.DatePicker({renderTo: 'doSearchClaim_invoiceUploadDateFrom'});

    Ext.onReady(setupGrid);
    
    function renderMoney(n) {
        v = (Math.round((v-0)*100))/100;
        v = (v == Math.floor(v)) ? v + ".00" : ((v*10 == Math.floor(v*10)) ? v + "0" : v);
        return (v + ' €').replace(/\./, ',');
    } 
    
    function showClaimByStatus(status)
    {            
        ds.load(
        {
            params:
                {
                status : status
            }
        });
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
        var lineOfBusiness = Ext.query('*[name$=lineOfBusiness]')[0].value;    

        ds.load(
        {
            params:
                {
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
                lineOfBusiness : lineOfBusiness
            }
        });        
        
    }

    function setupGrid(){
        Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
        Ext.QuickTips.init();         
               
        ds.load(
        {
            params:
                {
                status : ''
            }
        });

        var grid = new Ext.grid.GridPanel({
            loadMask: true,
            ds: ds,
            width: 960,
            columns: [
                {id:'Id', header: "Supplier Reference", width: 150, sortable: true, dataIndex: 'supplierReference', 
                    renderer:function(value,p,r){
                        return '<a href="openClaimDetail.action?id=' + r.data['id'] + '">' + value + '</a>'}},  
                {header: "Claim Number", width: 250, sortable: true, dataIndex: 'claimNumber'},                
                {header: "VRN", width: 250, sortable: true, dataIndex: 'vehicleRegistration'},
                {header: "Invoice Amount", width: 250, sortable: true, 
                    dataIndex: 'invoiceAmount' },
                {header: "Date Uploaded", width: 250, sortable: true, 
                    renderer: Ext.util.Format.dateRenderer('d/m/Y'), 
                    dataIndex: 'created'},
                {header: "Status", width: 250, sortable: true, dataIndex: 'status'},
                {header: "LOB", width: 250, sortable: true, dataIndex: 'lineOfBusiness'},
                {header: "CHO", width: 250, sortable: true, dataIndex: 'cho'},
                {header: "Insurer", width: 150, sortable: true, dataIndex: 'insurer'}
            ],
            stripeRows: true,
            layout:'fit',
            autoHeight:true,
            title:'Claims',viewConfig:{forceFit:true}  
        });               
        grid.render('gridPanel');
        grid.getSelectionModel().selectFirstRow();               
        
        var tabs = new Ext.TabPanel({
            renderTo: 'tabPanel',
            height:230,
            activeTab: 0,
            items:[
                {contentEl:'filterPanelTab', title:'Filter'},
                {contentEl:'searchPanelTab', title:'Search'}
            ]
        });  

        var claimTabPanel = new Ext.Panel({
            renderTo: 'claimPanel',
            layout:'fit',            
            items:[
                tabs,grid
            ]
        }); 
        
        var claimUploadDateFromPicker = new Ext.form.DateField({
            name: 'claimUploadDateFrom',
            width: 185,
            allowBlank: true,
            format: 'd/m/Y',
            showWeekNumber: true
        });
        
        var claimUploadDateToPicker = new Ext.form.DateField({
            name: 'claimUploadDateTo',
            width: 185,
            allowBlank: true,
            format: 'd/m/Y',
            showWeekNumber: true
        });
        
        var invoiceUploadDateFromPicker = new Ext.form.DateField({
            name: 'invoiceUploadDateFrom',
            width: 185,
            allowBlank: true,
            format: 'd/m/Y',
            showWeekNumber: true
        });
        
        var invoiceUploadDateToPicker = new Ext.form.DateField({
            name: 'invoiceUploadDateTo',
            width: 185,
            allowBlank: true,
            format: 'd/m/Y',
            showWeekNumber: true
        });
        
        var hireDateFromPicker = new Ext.form.DateField({
            name: 'hireDateFrom',
            width: 185,
            allowBlank: true,
            format: 'd/m/Y',
            showWeekNumber: true
        });
        
        var hireDateToPicker = new Ext.form.DateField({
            name: 'hireDateTo',
            width: 185,
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


</script>

<div id="claimPanel">      
    
    
    <s:if test="AuthenticatedUser !=null">  
        <table width="100%">
            <tr>
                <td>
                    <p>Welcome <b><s:property value="AuthenticatedUser.Username" /></b></p>
                    <s:if test="isCHO=true">
                        <div>
                            Would you like to upload Your claims? <a href="<s:url action="uploadClaims"/>">click here</a>            
                        </div>
                    </s:if>
                </td>
                <td width="50px">
                    <a href="<%=request.getContextPath()%>/j_acegi_logout">Log Off</a>
                </td>
            </tr>
        </table>
    </s:if>
    
    <dir id="tabPanel"></dir> 
    
    <div id="gridPanel"></div>
    
    <div id="filterPanelTab" class="filterPanelTab">
        <ul class="filterlist">
            <li><a href="javascript:showClaimByStatus('ClaimUnacknowledged');" >Claims Awaiting Acknowledgement (<s:property value="unacknowledgedClaimCount" />)</a></li> 
            <li><a href="javascript:showClaimByStatus('ClaimAcknowledged');" >Acknowledged Claims (<s:property value="acknowledgedClaimCount" />)</a></li>
            <li><a href="javascript:showClaimByStatus('ClaimUnrouted');" >Unrouted Claims (<s:property value="claimUnrountedCount" />)</a></li>
            <li><a href="javascript:showClaimByStatus('ClaimRejectionAccepted');" >Rejected Claims (<s:property value="claimRejectedCount" />)</a></li>
            <li><a href="javascript:showClaimByStatus('ClaimRejected');" >Accepted Rejected Claims (<s:property value="claimRejectedAcceptedCount" />)</a></li>
            <li><a href="javascript:showClaimByStatus('AwaitingInvoiceData');" >Claims Awaiting Claims Handling Payment (<s:property value="awaitingInvoiceCount" />)</a></li>
            <li><a href="javascript:showClaimByStatus('?');" >DA Payment Logged (?)</a></li>
            <li><a href="javascript:showClaimByStatus('AwaitingPaymentPack');" >Claims Awaiting Invoice Payment (<s:property value="awaitingPaymentPackCount" />)</a></li>
            <li><a href="javascript:showClaimByStatus('InvoiceRejectionAccepted');" >Accepted Rejected Invoices (<s:property value="invoiceRejectedAcceptedCount" />)</a></li>
            <li><a href="javascript:showClaimByStatus('InvoiceApproved');" >Approved Invoices Ready For payment (<s:property value="invoiceApprovedPackCount" />)</a></li>
            <li><a href="javascript:showClaimByStatus('InvoicePaymentLogged');" >Invoice Payment Logged (<s:property value="invoicePaymentLoggedCount" />)</a></li>
        </ul>
        
    </div>
    <div id="searchPanelTab" class="x-hide-display">
        <div id="searchPanel">
            
        </div>
        <s:action name="searchClaim" namespace="/user" executeResult="true" /> 
        
    </div>    
    
    
</div>




