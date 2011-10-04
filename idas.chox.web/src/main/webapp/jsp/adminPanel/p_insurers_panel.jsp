<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var adminTabIndex = 0;
    var InsurerMainPanelTabs;
    var insurerIsWorkgroupEnabled = true;

    Ext.onReady(function(){
        insurerIsWorkgroupEnabled = isTrue($("#insurerIsWorkgroupEnabled").val())

        if($("#tabIndex").val()!=null && $("#tabIndex").val()!=''){
            adminTabIndex = $("#tabIndex").val();
        }

        InsurerMainPanelTabs = new Ext.TabPanel({
            renderTo: 'InsurerMainPanel',
            height:615,
            width:775,
            border:true,
            loadMask:false,
            activeTab: adminTabIndex,
            items:[
                {contentEl:'insurerWorkgroupPanelTab', title:'Workgroups', disabled:(!insurerIsWorkgroupEnabled), listeners: {activate: handleActivate}, autoLoad: {url:"p/getInsurerWorkgroupPage.action?insurerId="+<s:property value="CurrentUser.Insurer.id" />+"&rdn="+getRandomNumber(), scripts:true}},
                {contentEl:'insurerBrePanelTab', title:'BRE Band', listeners: {activate: handleActivate}, autoLoad: {url:"p/getInsurerBreBandPage.action?insurerId="+<s:property value="CurrentUser.Insurer.id" />+"&rdn="+getRandomNumber(), scripts:true}},
                {contentEl:'insurerBreMappingPanelTab', title:'BRE Band Mapping', listeners: {activate: handleActivate}, autoLoad: {url:"p/getInsurerBreBandChorganisationMapping.action?insurerId="+<s:property value="CurrentUser.Insurer.id" />+"&rdn="+getRandomNumber(), scripts:true}},
                {contentEl:'insurerVehicleClassCeilingTab', title:'Vehicle Class Ceilings', listeners: {activate: handleActivate}, autoLoad: {url:"p/getInsurerVehicleClassCeilingPage.action?insurerId="+<s:property value="CurrentUser.Insurer.id" />+"&rdn="+getRandomNumber(), scripts:true}},
                {contentEl:'InsurerDiscountsTab1', id:"InsurerDiscountsTabId1", title:'Discounts', tabTip:'Insurer Discounts', disabled : !<s:property value="CurrentUser.Insurer.insurerDiscountEnable" />,listeners: {activate: handleActivate}, autoLoad: {url:"p/getInsurerDiscountPage.action?insurerId="+<s:property value="CurrentUser.Insurer.id" />+"&rdn="+getRandomNumber(), scripts:true}}
            ]
        });
    });

    function handleActivate(tab){
        adminTabIndex = 0;
        if(InsurerMainPanelTabs){ adminTabIndex = InsurerMainPanelTabs.items.indexOf(InsurerMainPanelTabs.getActiveTab()); }
    }

</script>

<input name="tabIndex" id="tabIndex" type="hidden" value="<s:property value="tabIndex"/>"/>
<input name="insurerIsWorkgroupEnabled" id="insurerIsWorkgroupEnabled" type="hidden" value="<s:property value="insurerIsWorkgroupEnabled" />"/>

<div id="chox-admin-holder">

    <div id="chox-admin-col-div" style ="width:780">
        <div id="header-title">
            <label>Insurer Name:
                <s:property value="CurrentUser.Insurer.name" />
            </label>
        </div>
        <div id="InsurerMainPanel"></div>
    </div>

    <div id="insurerWorkgroupPanelTab" class="x-hide-display"></div>
    <div id="insurerBrePanelTab" class="x-hide-display"></div>
    <div id="insurerBreMappingPanelTab" class="x-hide-display"></div>
    <div id="insurerVehicleClassCeilingTab" class="x-hide-display"></div>
    <div id="InsurerDiscountsTab1" class="x-hide-display"></div>

</div>