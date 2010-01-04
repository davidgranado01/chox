<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var adminTabIndex = 0;
    var InsurerMainPanelTabs;

    Ext.onReady(function(){
        
        if($("#tabIndex").val()!=null && $("#tabIndex").val()!=''){
            adminTabIndex = $("#tabIndex").val();
        }
        
        InsurerMainPanelTabs = new Ext.TabPanel({
            renderTo: 'InsurerMainPanel',
            height:610,
            width:730,
            border:true,
            loadMask:false,
            activeTab: adminTabIndex,
            items:[
                {contentEl:'insurerWorkgroupPanelTab', title:'Workgroups', listeners: {activate: handleActivate}, autoLoad: {url:"p/getInsurerWorkgroupPage.action?insurerId="+<s:property value="CurrentUser.insurer.id" />, scripts:true}},
                {contentEl:'insurerBrePanelTab', title:'BRE Band', listeners: {activate: handleActivate}, autoLoad: {url:"p/getInsurerBreBandPage.action?insurerId="+<s:property value="CurrentUser.insurer.id" />, scripts:true}},
                {contentEl:'insurerBreMappingPanelTab', title:'BRE Band Mapping', listeners: {activate: handleActivate}, autoLoad: {url:"p/getInsurerBreBandChorganisationMapping.action?insurerId="+<s:property value="CurrentUser.insurer.id" />, scripts:true}},
                {contentEl:'insurerVehicleClassCeilingTab', title:'Vehicle Class Ceilings', listeners: {activate: handleActivate}, autoLoad: {url:"p/getInsurerVehicleClassCeilingPage.action?insurerId="+<s:property value="CurrentUser.insurer.id" />, scripts:true}}
            ]
        });
    });
    
    function handleActivate(tab){
        adminTabIndex = 0;
        if(InsurerMainPanelTabs){ adminTabIndex = InsurerMainPanelTabs.items.indexOf(InsurerMainPanelTabs.getActiveTab()); }
    }

</script>

<input name="tabIndex" id="tabIndex" type="hidden" value="<s:property value="tabIndex"/>">

<div id="chox-admin-holder">

    <div id="chox-admin-col-div">
        <div id="header-title">
            <label>Insurer Name:
                <s:property value="CurrentUser.insurer.name" />
            </label>
        </div>
        <div id="InsurerMainPanel"/>
    </div>

    <div id="insurerWorkgroupPanelTab" class="x-hide-display"/>
    <div id="insurerBrePanelTab" class="x-hide-display"/>
    <div id="insurerBreMappingPanelTab" class="x-hide-display"/>
    <div id="insurerVehicleClassCeilingTab" class="x-hide-display"/>

</div>