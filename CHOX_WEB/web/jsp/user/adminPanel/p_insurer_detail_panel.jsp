<%@ taglib uri="/struts-tags" prefix="s" %>

<link href="<%= request.getContextPath()%>/styles/chox.css" rel="stylesheet" type="text/css" media="all"/>        
<link href="<%= request.getContextPath()%>/css/ext-all.css" rel="stylesheet" type="text/css" media="all"/>

<script language="JavaScript">
        
        // var selectedPanel = "InsurerOrgMgmt";
        var adminCurrentTabIndex;
        var adminTabs;
        
        $(document).ready(function(){
            doFormValidation();
        }); 
        
        function doFormValidation(){
                    
            var validateFlag = $("#formUpdateInsurerDetail").validate(
            {
               errorLabelContainer: "#CDmessageBox",                
               rules: {
                 name:{
                     required:true
                 },
                 adminHandlingCharge:{
                     required:true, number:true, min:0
                 }
               },
               messages: {
                 name: {
                   required:"You must supply a value for 'Name'"
                 }, 
                 adminHandlingCharge: {
                   required:"You must supply a value for 'Admin Handling Charge'",
                   number:"'Admin Handling Charge' must be numeric",
                   min:"'Admin Handling Charge' cannot be less than zero"
                 }
                     
               },
               submitHandler: function(form) {
                    // $(form).ajaxSubmit(op);
               }
            });
            
            return validateFlag;
        }
        
        function doInsurerSubmit(){
            
            if(doFormValidation().form()){
            
                $("#admin_param_panel").block();
                
                var op = { 
                    beforeSubmit:  onBeforeSubmit,
                    success:onSubmitResponseReceived,
                    timeout: 3000,
                    error: onSubmitError
                };
                
                $("#formUpdateInsurerDetail").ajaxSubmit(op);
                
            }
        }
        
        function doInsurerBack(){
            $("#admin_param_panel").load("loadAdminPanel.action?adminPanelName=InsurerOrgMgmt");
        }
        
        function onBeforeSubmit(formData, jqForm, options) { 
        }

        function onSubmitResponseReceived(responseText, statusText){
            
            responseText = responseText.trim();
            var output = "Your changes have been saved.";
            
            if(responseText != "" && responseText != "1" && responseText.substring(0,9) == 'objectId:'){
                
                var newObjectId =  parseInt(responseText.substring(9,responseText.length));
                $("#admin_param_panel").load("updateInsurerDetailPanel.action?objectId=" + newObjectId);
                
            }else{
                output = responseText;
                $(".chox-form-submit-result").html(output);
            }
            
            $("#admin_param_panel").unblock();
        }

        function onSubmitError(XMLHttpRequest, textStatus, errorThrown) {
            $("#admin_param_panel").unblock();
            alert("Error");  
        }
        
        function setupTabPanels()
        {

           if(adminCurrentTabIndex==null || <s:property value="isNew"/>){
               adminCurrentTabIndex = 0;
           }
           
           adminTabs = new Ext.TabPanel({
           renderTo: 'mainPanel',
           height:630,
           autoScroll :true,
           activeTab: adminCurrentTabIndex,
           items:[
                {contentEl:'insurerDetailPanelTab', title:'Details',listeners: {activate: handleActivate}},
                {contentEl:'insurerAlliasPanelTab', title:'Allias', disabled:<s:property value="isNew"/>, listeners: {activate: handleActivate}},                   
                {contentEl:'insurerLobPanelTab', title:'Line Of Business', disabled:<s:property value="isNew"/>, listeners: {activate: handleActivate}},  
                {contentEl:'insurerCreditHirePanelTab', title:'Credit Hire Mapping', disabled:<s:property value="isNew"/>, listeners: {activate: handleActivate}},  
                {contentEl:'insurerBrePanelTab', title:'BRE Band', disabled:<s:property value="isNew"/>, listeners: {activate: handleActivate}},  
                {contentEl:'insurerBreMappingPanelTab', title:'BRE Band Mapping', disabled:<s:property value="isNew"/>, listeners: {activate: handleActivate}},  
            ]
           });
        }

        Ext.onReady(function(){        
            setupTabPanels();
        }); 

        function handleActivate(tab){
            
            adminCurrentTabIndex = 0;
            
            if(adminTabs)
            {
                adminCurrentTabIndex = adminTabs.items.indexOf(adminTabs.getActiveTab());
                onPageRefresh();
            }
            
        }

</script>

<div id="mainPanel" class="adminTabCss"></div>

<div id="insurerBrePanelTab" class="x-hide-display">
    <div class="subAdminTabCss">
        <s:action name="loadAdminPanel" executeResult="true">
            <s:param name="adminPanelName">InsurerChoBandMgmt</s:param>
            <s:param name="selectOrgId"><s:property value="objectId" /></s:param>        
        </s:action>
    </div>
</div>

<div id="insurerBreMappingPanelTab" class="x-hide-display">
    <div class="subAdminTabCss">
        <s:action name="loadAdminPanel" executeResult="true">
            <s:param name="adminPanelName">InsurerChoBandMappingMgmt</s:param>
            <s:param name="selectOrgId"><s:property value="objectId" /></s:param>        
        </s:action>
    </div>
</div>

<div id="insurerLobPanelTab" class="x-hide-display">
    <div class="subAdminTabCss">
        <s:action name="loadAdminPanel" executeResult="true">
            <s:param name="adminPanelName">InsurerLineOfBusinessMappingMgmt</s:param>
            <s:param name="selectOrgId"><s:property value="objectId" /></s:param>
        </s:action> 
    </div>
</div>

<div id="insurerCreditHirePanelTab" class="x-hide-display">
        <div class="subAdminTabCss">
        <s:action name="loadAdminPanel" executeResult="true">
            <s:param name="adminPanelName">InsurerOrgMappingMgmt</s:param>
            <s:param name="selectOrgId"><s:property value="objectId" /></s:param>        
        </s:action> 
    </div>
</div>

<div id="insurerAlliasPanelTab" class="x-hide-display">
    <div class="subAdminTabCss">
        <s:action name="loadAdminPanel" executeResult="true">
            <s:param name="adminPanelName">InsurerAlliasMappingMgmt</s:param>
            <s:param name="selectOrgId"><s:property value="objectId" /></s:param>        
        </s:action>
    </div>
</div>

<div id="insurerDetailPanelTab" class="x-hide-display">
  
    <div class="subAdminTabCss">
    <form id="formUpdateInsurerDetail" action="user/updateInsurerDetail.action" class="XXentity-form" onsubmit="return true;">
    
    <input type="hidden" name="objectId" id="objectId" value='<s:property value="objectId"/>'>
    
            <div class="form-container">
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Name<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDName" name="name" value="<s:property value="name" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Admin Handling Charge<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDAdminHandlingCharge" name="adminHandlingCharge" value="<s:property value="adminHandlingCharge" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Active</label>
                    <s:checkbox name="status" value="status" />
                </div>                   
                <div class="chox-form-button">
                    <input type="button" value="Save Changes" onclick="javascript: doInsurerSubmit();"/>
                    <input type="button" value="Cancel" class="cancel" onclick="javascript: doInsurerBack();" />
                </div>
                <div id="CDmessageBox" style="text-align:center"></div>  
                <div class="chox-form-submit-result"></div>  
            </div>
    </form>        
    </div>
</div>
