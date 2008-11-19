<%-- 
    Document   : Inbox
    Created on : 10-Nov-2008, 22:13:54
    Author     : Emmanuel
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<head>
    <title><decorator:title default="Struts Starter"/></title>
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
        ({url: 'getClaimsbyStatus.action',method:'POST'}),
        reader:rd        
    });

    Ext.onReady(setupGrid);
    
    function showAllClaim()
    {
        ds.load(
        {
            params:
                {
                status: 'All'
            }
        });
    }
     
    function showAuthorizedClaim()
    {
        ds.load(
        {
            params:
                {
                status: 'Authorized'
            }
        });

    }     
     
    function showAwaitingAuthorizationClaim()
    {       
        ds.load(
        {
            params:
                {
                status: 'Awaiting Authorization'
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
            ds: ds,
            columns: [
                {id:'Id',header: "Supplier Reference No.", width: 150, sortable: true, dataIndex: 'supplierReference'},
                {header: "Claim Number", width: 250, sortable: true, dataIndex: 'id'},
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
    }    
</script>
<div id="claimPanel">      
    
    <dir id="tabPanel"></dir> 
    
    <div id="gridPanel"></div>
    
    <div id="filterPanelTab">
        <ul>
            <li><a href="javascript:showAllClaim();">All claims (?)</a></li>
            <li><a href="javascript:showAuthorizedClaim();">Acknowledged claims (?)</a></li>
            <li><a href="javascript:showAwaitingAuthorizationClaim();">Unacknowledged claims (?)</a></li>
        </ul>
    </div>
    <div id="searchPanelTab" class="x-hide-display">
        Search
    </div>    
    
    
</div>




