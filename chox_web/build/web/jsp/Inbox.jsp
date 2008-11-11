<%-- 
    Document   : Inbox
    Created on : 10-Nov-2008, 22:13:54
    Author     : Emmanuel
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<head>
    <title><decorator:title default="Struts Starter"/></title>
    <link href="<s:url value='styles/main.css'/>" rel="stylesheet" type="text/css" media="all"/>
    <link href="<s:url value='css/ext-all.css'/>" rel="stylesheet" type="text/css" media="all"/>
    <link href="<s:url value='css/xtheme-gray.css'/>" rel="stylesheet" type="text/css" media="all"/>
    <script src="<s:url value='scripts/ext-base.js'/>" type="text/javascript"></script>
    <script src="<s:url value='scripts/ext-all.js'/>" type="text/javascript"></script>    
    <decorator:head/>
</head>

<script >
    Ext.onReady(function(){
        Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
        Ext.QuickTips.init();
        
        
        
        var rd = new Ext.data.JsonReader({
            totalProperty: 'totalCount',   
            root: 'results', 
            fields:[
                {name:'id'},
                {name:'claimStatus'},
                {name:'vehicleRegistration'},
                {name:'policyNumber'}
            ]
        });
        var ds = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: 'getAllClaims.action',method:'POST'}),
            reader:rd
        });
        ds.load();

        var grid = new Ext.grid.GridPanel({
            ds: ds,
            columns: [
                {id:'Id',header: "Id", width: 150, sortable: true, dataIndex: 'id'},
                {header: "Status", width: 250, sortable: true, dataIndex: 'claimStatus'},
                {header: "Vehicle Registration", width: 250, sortable: true, dataIndex: 'vehicleRegistration'},
                {header: "Policy Number", width: 150, sortable: true, dataIndex: 'policyNumber'}
            ],
            stripeRows: false,
            height:450,
            width:800,
            title:'Claims'
        });
    
        grid.render('gridPanel');

        grid.getSelectionModel().selectFirstRow();
                
        
        var tabs = new Ext.TabPanel({
            renderTo: 'tabPanel',
            width:800,
            height:120,
            frame:true,
            activeTab: 0,
            items:[
                {contentEl:'filterPanelTab', title:'Filter'},
                {contentEl:'searchPanelTab', title:'Search'}
            ]
        });  

        var claimTabPanel = new Ext.Panel({
            title: 'My Inbox',
            renderTo: 'claimPanel',
            width:800,
            items:[
                tabs,grid
            ]
        });
        
       
    });
</script>
<div id="claimPanel" style="width:100%">      
    
    <dir id="tabPanel"></dir> 
    
    <div id="filterPanelTab">
        <ul>
            <li><a href="#">Unacknowledged claims (?)</a></li>
            <li><a href="#">Unrouted claims (?)</a></li>
            <li><a href="#">Repair Overdue Claims (?)</a></li>
        </ul>
    </div>
    <div id="searchPanelTab"></div>    
    
    <div id="gridPanel"></div>
</div>
<div id="invoicePanel"></div>


