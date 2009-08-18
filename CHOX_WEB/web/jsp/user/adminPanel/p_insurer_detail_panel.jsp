<%@ taglib uri="/struts-tags" prefix="s" %>

<link href="<%= request.getContextPath()%>/styles/chox.css" rel="stylesheet" type="text/css" media="all"/>        
<link href="<%= request.getContextPath()%>/css/ext-all.css" rel="stylesheet" type="text/css" media="all"/>

<script language="JavaScript">
        
        var adminCurrentTabIndex = 0;
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
                 vatNo:{
                     required:true
                 },
                 companyNo:{
                     required:true
                 },
                 address1:{
                     required:true
                 },
                 address2:{
                     required:true
                 },
                 address4:{
                     required:true
                 },
                 address5:{
                     required:true
                 },
                 postcode:{
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
                 },
                 vatNo:{
                     required:"You must supply a value for 'VAT No.'"
                 },
                 companyNo:{
                     required:"You must supply a value for 'Company No.'"
                 },
                 address1:{
                     required:"You must supply a value for 'Address 1'"
                 },
                 address2:{
                     required:"You must supply a value for 'Address 2'"
                 },
                 address4:{
                     required:"You must supply a value for 'County'"
                 },
                 address5:{
                     required:"You must supply a value for 'Country'"
                 },
                 postcode:{
                     required:"You must supply a value for 'Postcode'"
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
                confirm("New Insurer has been created!");
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
           height:660,
           autoScroll :true,
           activeTab: adminCurrentTabIndex,
           items:[
                {contentEl:'insurerDetailPanelTab', title:'Details',listeners: {activate: insHandleActivate}},
                {contentEl:'insurerAlliasPanelTab', title:'Alias', disabled:<s:property value="isNew"/>, listeners: {activate: insHandleActivate}},
                {contentEl:'insurerLobPanelTab', title:'Line Of Business', disabled:<s:property value="isNew"/>, listeners: {activate: insHandleActivate}},
                {contentEl:'insurerCreditHirePanelTab', title:'Credit Hire Mapping', disabled:<s:property value="isNew"/>, listeners: {activate: insHandleActivate}},
                {contentEl:'insurerBrePanelTab', title:'BRE Band', disabled:<s:property value="isNew"/>, listeners: {activate: insHandleActivate}},
                {contentEl:'insurerBreMappingPanelTab', title:'BRE Band Mapping', disabled:<s:property value="isNew"/>, listeners: {activate: doBreMappingRefresh}}
            ]
           });
        }
       
        Ext.onReady(function(){        
            setupTabPanels();
        }); 

        function doBreMappingRefresh(tab){
            insHandleActivate(tab);
            onChoBandPageRefresh();
        }
        
        function insHandleActivate(tab){
            
            adminCurrentTabIndex = 0;
            
            if(adminTabs)
            {
                adminCurrentTabIndex = adminTabs.items.indexOf(adminTabs.getActiveTab());
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
                        <label class="chox-form-std-label">VAT No.<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDVatNo" name="vatNo" value="<s:property value="vatNo" />"/>
                    </div>        
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Company No.<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDCompanyNo" name="companyNo" value="<s:property value="companyNo" />"/>
                    </div>                          
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Address 1<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDAddress1" name="address1" value="<s:property value="address1" />"/>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Address 2<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDAddress2" name="address2" value="<s:property value="address2" />"/>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Address 3</label>
                        <input type="text" class="chox-ttxt" id="CCDAddress3" name="address3" value="<s:property value="address3" />"/>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Postcode<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDPostcode" name="postcode" value="<s:property value="postcode" />"/>
                    </div>                      
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">County<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDAddress4" name="address4" value="<s:property value="address4" />"/>
                    </div>  
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Country<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDAddress5" name="address5" value="<s:property value="address5" />"/>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Phone</label>
                        <input type="text" maxlength="50" class="chox-ttxt" id="CCDPhone" name="phone" value="<s:property value="phone" />"/>
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
