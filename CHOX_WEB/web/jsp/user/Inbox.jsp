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
            {name:'claimStatus'},
            {name:'invoiceAmount'},
            {name:'vehicleRegistration'},
            {name:'policyNumber'},
            {name:'supplierReference'},
            {name:'created'},
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
    
    function showClaimByStatus(status)
    {    
        ds.load(
        {
            params:
                {
                status: status
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
                status: 'All'
            }
        });

        var grid = new Ext.grid.GridPanel({
            loadMask: true,
            ds: ds,
            columns: [
                {id:'Id',header: "Claim Number", width: 250, sortable: true, dataIndex: 'id', 
                    renderer:function(value){
                        return '<a href="index.action">' + value + '</a>'}},
                {header: "Supplier Reference No.", width: 150, sortable: true, dataIndex: 'supplierReference'},                
                {header: "VRN", width: 250, sortable: true, dataIndex: 'vehicleRegistration'},
                {header: "Invoice Amount", width: 250, sortable: true, dataIndex: 'invoiceAmount'},
                {header: "Date Uploaded", width: 250, sortable: true, 
                    renderer: Ext.util.Format.dateRenderer('Y/m/d'), 
                    dataIndex: 'created'},
                {header: "Status", width: 250, sortable: true, dataIndex: 'claimStatus'},
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
            height:380,
            activeTab: 0,
            items:[
                {contentEl:'filterPanelTab',height: 35, title:'Filter'},
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

        <%-- var myForm = new Ext.form.FormPanel({
            renderTo:"searchPanel",
            title:"Basic Form",
            width:600,
            frame:true,
            items: [
                new Ext.form.TextField({
                    id:"supplierReference",
                    fieldLabel:"Supplier Reference",
                    width:275
                }),
                new Ext.form.TextField({
                    id:"supplierId",
                    fieldLabel:"Supplier Name",
                    width:275
                }),
                 new Ext.form.TextField({
                    id:"supplierId",
                    fieldLabel:"Insurer Name",
                    width:275
                }),
                new Ext.form.TextField({
                    id:"invoiceNumber",
                    fieldLabel:"Invoice Numnber",
                    width:275
                }),
                new Ext.form.TextField({
                    id:"claimNumber",
                    fieldLabel:"Claim Number",
                    width:275
                }),
                new Ext.form.TextField({
                    id:"vrn",
                    fieldLabel:"VRN",
                    width:275
                }),
                new Ext.form.TextField({
                    id:"claimUploadDateFrom",
                    fieldLabel:"Claim Upload Date From",
                    width:275
                }),
                 new Ext.form.TextField({
                    id:"claimUploadDateTo",
                    fieldLabel:"Claim Upload Date To",
                    width:275
                }),
                 new Ext.form.TextField({
                    id:"invoiceUploadDateFrom",
                    fieldLabel:"Invoice Upload Date From",
                    width:275
                }),
                 new Ext.form.TextField({
                    id:"invoiceUploadDateTo",
                    fieldLabel:"Invoice Upload Date To",
                    width:275
                }),
                 new Ext.form.TextField({
                    id:"hireDateFrom",
                    fieldLabel:"Hire Date From",
                    width:275
                }),
                 new Ext.form.TextField({
                    id:"hireDateTo",
                    fieldLabel:"Hire Date To",
                    width:275
                }),
                 new Ext.form.TextField({
                    id:"status",
                    fieldLabel:"Status",
                    width:275
                }),
                 new Ext.form.TextField({
                    id:"lineOfBusiness",
                    fieldLabel:"Line of Business",
                    width:275
                }),
                
            ],
            buttons: [
                {text:"Cancel"},
                {text:"Save"}
            ]
        }); --%>
    } 


</script>

<div id="claimPanel">      
    <s:if test="isCHO">
        <a>Upload Your Claims? <a href="<s:url action="uploadClaims" namespace="user"/>">click here</a>
        <br />
    </s:if>
    <dir id="tabPanel"></dir> 
    
    <div id="gridPanel"></div>
    
    <div id="filterPanelTab" class="filterPanelTab">
        <ul class="filterlist">
            <li><a href="javascript:showClaimByStatus('ClaimUnacknowledged');" >Claims Awaiting Acknowledgement (<s:property value="unacknowledgedClaimCount" />)</a></li> 
            <li><a href="javascript:showClaimByStatus('ClaimAcknowledged');" >Acknowledged Claims (<s:property value="acknowledgedClaimCount" />)</a></li>
            <li><a href="javascript:showClaimByStatus('');" >Rejected Claims</a></li>
            <li><a href="javascript:showClaimByStatus('');" >Accepted Rejected Claims</a></li>
            <li><a href="javascript:showClaimByStatus('');" >Claims Awaiting Claims Handling Payment</a></li>
            <li><a href="javascript:showClaimByStatus('');" >DA Payment Logged</a></li>
            <li><a href="javascript:showClaimByStatus('');" >Claims Awaiting Invoice Payment</a></li>
            <li><a href="javascript:showClaimByStatus('');" >Accepted Rejected Invoices</a></li>
            <li><a href="javascript:showClaimByStatus('');" >Approved Invoices Ready For payment</a></li>
            <li><a href="javascript:showClaimByStatus('');" >Invoice Payment Logged</a></li>
        </ul>
        
    </div>
    <div id="searchPanelTab" class="x-hide-display">
        <div id="searchPanel">
            
        </div>
       <s:action name="searchClaim" namespace="/user" executeResult="true" /> 
        
    </div>    
    
    
</div>




