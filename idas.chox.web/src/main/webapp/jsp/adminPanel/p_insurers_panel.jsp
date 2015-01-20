<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var insAdminTabIndex = 0;
    var InsurerMainPanelTabs;
    var insurerIsWorkgroupEnabled = true;

    Ext.onReady(function(){
        insurerIsWorkgroupEnabled = isTrue($("#insurerIsWorkgroupEnabled").val());

        if($("#tabIndex").val()!==null && $("#tabIndex").val()!==''){
            insAdminTabIndex = $("#tabIndex").val();
        }

        InsurerMainPanelTabs = new Ext.TabPanel({
            renderTo: 'InsurerMainPanel',
            height:615,
            width:775,
            border:true,
            loadMask:false,
            activeTab: insAdminTabIndex,
            items:[
                {contentEl:'insurerWorkgroupPanelTab', title:'Workgroups', disabled:(!insurerIsWorkgroupEnabled), listeners: {activate: handleActivate}, autoLoad: choxUpdateEl({url:'/prv/p/getInsurerWorkgroupPage.action', params:{"insurerId" : '<s:property value="CurrentUser.Insurer.id" />'}})},
                {contentEl:'insurerBrePanelTab', title:'BRE Band', listeners: {activate: handleActivate}, autoLoad: choxUpdateEl({url:'/prv/p/getInsurerBreBandPage.action', params:{"insurerId" : '<s:property value="CurrentUser.Insurer.id" />'}})},
                {contentEl:'insurerBreMappingPanelTab', title:'BRE Band Mapping', listeners: {activate: handleActivate}, autoLoad: choxUpdateEl({url:'/prv/p/getInsurerBreBandChorganisationMapping.action', params:{"insurerId" : '<s:property value="CurrentUser.Insurer.id" />'}})},
                {contentEl:'insurerVehicleClassCeilingTab', title:'Vehicle Class Ceilings', listeners: {activate: handleActivate}, autoLoad: choxUpdateEl({url:'/prv/p/getInsurerVehicleClassCeilingPage.action', params:{"insurerId" : '<s:property value="CurrentUser.Insurer.id" />'}})},
                {contentEl:'InsurerDiscountsTab1', id:"InsurerDiscountsTabId1", title:'Discounts', tabTip:'Insurer Discounts', disabled : !<s:property value="CurrentUser.Insurer.insurerDiscountEnable" />,listeners: {activate: handleActivate}, autoLoad: choxUpdateEl({url:'/prv/p/getInsurerDiscountPage.action', params:{"insurerId" : '<s:property value="CurrentUser.Insurer.id" />'}})}
            ]
        });
    });

    function handleActivate(tab){
        insAdminTabIndex = 0;
        if(InsurerMainPanelTabs){ insAdminTabIndex = InsurerMainPanelTabs.items.indexOf(InsurerMainPanelTabs.getActiveTab()); }
    }

</script>

<input name="tabIndex" id="tabIndex" type="hidden" value="<s:property value="tabIndex"/>"/>
<input name="insurerIsWorkgroupEnabled" id="insurerIsWorkgroupEnabled" type="hidden" value="<s:property value="insurerIsWorkgroupEnabled" />"/>

<div id="chox-admin-holder">

    <div id="chox-admin-col-div" style ="width:780px">
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