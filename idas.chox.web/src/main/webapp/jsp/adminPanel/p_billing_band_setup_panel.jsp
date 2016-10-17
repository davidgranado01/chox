<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var billingBandSetupTabIndex = 0;
    var billingBandSetupPanelTabs;

    Ext.onReady(function(){

        billingBandSetupTabIndex = 0;
        
        billingBandSetupPanelTabs = new Ext.TabPanel({
            renderTo: 'billingSetupPanel',
            height:660,
            width:775,
            border:true,
            loadMask:false,
            activeTab: billingBandSetupTabIndex,
            items:[
                {contentEl:'insurerBillingBandTab', title:'Insurer Billing Bands', listeners: {activate: handleActivate}, autoLoad: choxUpdateEl({url:'/prv/p/getInsurerBillingBandPage.action'})},
                {contentEl:'choBillingBandTab', title:'CHO Billing Bands', listeners: {activate: handleActivate}, autoLoad: choxUpdateEl({url:'/prv/p/getChoBillingBandPage.action'})},
                {contentEl:'insurerBillingBandMappingTab', title:'Insurer Billing Band Mapping', listeners: {activate: handleActivate}, autoLoad: choxUpdateEl({url:'/prv/p/getInsurerBillingBandMappingPage.action'})},
                {contentEl:'choBillingBandMappingTab', title:'CHO Billing Band Mapping', listeners: {activate: handleActivate}, autoLoad: choxUpdateEl({url:'/prv/p/getChoBillingBandMappingPage.action'})}
            ]
        });
        billingBandSetupPanelTabs.setActiveTab(billingBandSetupTabIndex);
    });

    function handleActivate(tab){
        billingBandSetupTabIndex = 0;
        if(billingBandSetupPanelTabs){ billingBandSetupTabIndex = billingBandSetupPanelTabs.items.indexOf(billingBandSetupPanelTabs.getActiveTab()); }
    }
    
</script>

<input name="tabIndex" id="tabIndex" type="hidden" value="<s:property value="tabIndex"/>"/>

<div id="chox-admin-holder">

    <div id="chox-admin-col-div" style ="width:780px">
        <div id="billingSetupPanel"></div>
    </div>

    <div id="insurerBillingBandTab" class="x-hide-display"></div>
    <div id="choBillingBandTab" class="x-hide-display"></div>
    <div id="insurerBillingBandMappingTab" class="x-hide-display"></div>
    <div id="choBillingBandMappingTab" class="x-hide-display"></div>

</div>