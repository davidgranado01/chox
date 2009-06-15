<%@ taglib uri="/struts-tags" prefix="s" %>

<link href="<%= request.getContextPath()%>/styles/chox.css" rel="stylesheet" type="text/css" media="all"/>        
<link href="<%= request.getContextPath()%>/css/ext-all.css" rel="stylesheet" type="text/css" media="all"/>

<script language="JavaScript">
        
        var selectedPanel = "InsurerPanelMgmt";
        var adminCurrentTabIndex = 0;
        var InsurerMainPanelTabs;
                
        function setupInsurerMainPanels()
        {
           
           InsurerMainPanelTabs = new Ext.TabPanel({
           renderTo: 'InsurerMainPanel',
           height:660,
           autoScroll :true,
           activeTab: 0,
           items:[
                {contentEl:'insurerBrePanelTab', title:'BRE Band', listeners: {activate: doSelectBreDetail}},
                {contentEl:'insurerBreMappingPanelTab', title:'BRE Band Mapping', listeners: {activate: doBreMappingRefresh}}
            ]
           });
        }

        Ext.onReady(function(){        
            setupInsurerMainPanels();
        }); 

        function doBreMappingRefresh(tab){
            handleActivate(tab);
            onChoBandPageRefresh();
        }
        
        function doSelectBreDetail(tab){
            handleActivate(tab);
            //$("#chobandDiv").load("loadAdminPanel.action?adminPanelName=InsurerChoBandMgmt&selectOrgId="+selectOrgId);
        }
        
        function handleActivate(tab){    
            adminCurrentTabIndex = 0;
            if(InsurerMainPanelTabs)
            {
                adminCurrentTabIndex = InsurerMainPanelTabs.items.indexOf(InsurerMainPanelTabs.getActiveTab());
            }
        }

</script>

<div id="InsurerMainPanel" class="adminTabCss"></div>


<div id="insurerBrePanelTab" class="x-hide-display">
    <div class="subAdminTabCss">
        <s:action name="loadAdminPanel" executeResult="true">
            <s:param name="adminPanelName">InsurerChoBandMgmt</s:param>
            <s:param name="selectOrgId"><s:property value="selectOrgId" /></s:param>        
        </s:action>
    </div>
</div>

<div id="insurerBreMappingPanelTab" class="x-hide-display">
    <div class="subAdminTabCss">
        <s:action name="loadAdminPanel" executeResult="true">
            <s:param name="adminPanelName">InsurerChoBandMappingMgmt</s:param>
            <s:param name="selectOrgId"><s:property value="selectOrgId" /></s:param>        
        </s:action>
    </div>
</div>
