<%@ taglib uri="/struts-tags" prefix="s" %>

<link href="<%= request.getContextPath()%>/css/chox.css" rel="stylesheet" type="text/css" media="all"/>
<link href="<%= request.getContextPath()%>/css/ext-all.css" rel="stylesheet" type="text/css" media="all"/>

<script type="text/javascript">
        
        var selectedPanel = "InsurerPanelMgmt";
        var adminCurrentTabIndex = 0;
        var InsurerMainPanelTabs;
                
        function setupInsurerMainPanels()
        {
           
           InsurerMainPanelTabs = new Ext.TabPanel({
           renderTo: 'InsurerMainPanel',
           activeTab: 0,
           height:655,
           items:[
               {contentEl:'insurerWorkgroupPanelTab', title:'Workgroups', listeners: {activate: doSelectDetail}},
               {contentEl:'insurerBrePanelTab', title:'BRE Band', listeners: {activate: doSelectDetail}},
               {contentEl:'insurerBreMappingPanelTab', title:'BRE Band Mapping', listeners: {activate: doBreMappingRefresh}},
               {contentEl:'insurerVehicleClassCeilingTab', title:'Vehicle Class Ceilings', listeners: {activate: doSelectDetail}}
           ]
           });
        }

        Ext.onReady(function(){
            setupInsurerMainPanels();
        });

        function doBreMappingRefresh(tab){
            handleActivate(tab);
            onBreBandPageRefresh();
        }
        
        function doSelectDetail(tab){
            handleActivate(tab);
        }
        
        function doSelectDetail(tab){
            handleActivate(tab);
        }
        
        function handleActivate(tab){    
            adminCurrentTabIndex = 0;
            if(InsurerMainPanelTabs)
            {
                adminCurrentTabIndex = InsurerMainPanelTabs.items.indexOf(InsurerMainPanelTabs.getActiveTab());
            }
        }

</script>

<div id="InsurerMainPanel" class="admin-tab-css"></div>

<div id="insurerWorkgroupPanelTab" class="x-hide-display">
    <div class="sub-admin-tab-css">
        <s:action name="loadAdminPanel" executeResult="true">
            <s:param name="adminPanelName">InsurerWorkgroupMappingMgmt</s:param>
            <s:param name="selectOrgId"><s:property value="selectOrgId" /></s:param>
        </s:action> 
    </div>
</div>

<div id="insurerBrePanelTab" class="x-hide-display">
    <div class="sub-admin-tab-css">
        <s:action name="loadAdminPanel" executeResult="true">
            <s:param name="adminPanelName">InsurerBreBandMgmt</s:param>
            <s:param name="selectOrgId"><s:property value="selectOrgId" /></s:param>        
        </s:action>
    </div>
</div>

<div id="insurerBreMappingPanelTab" class="x-hide-display">
    <div class="sub-admin-tab-css">
        <s:action name="loadAdminPanel" executeResult="true">
            <s:param name="adminPanelName">InsurerBreBandMappingMgmt</s:param>
            <s:param name="selectOrgId"><s:property value="selectOrgId" /></s:param>        
        </s:action>
    </div>
</div>

<div id="insurerVehicleClassCeilingTab" class="x-hide-display">
    <div class="sub-admin-tab-css">
        <s:action name="loadAdminPanel" executeResult="true">
            <s:param name="adminPanelName">InsurerVehicleClassCeiling</s:param>
            <s:param name="selectOrgId"><s:property value="selectOrgId" /></s:param>
        </s:action>
     </div>
</div>
