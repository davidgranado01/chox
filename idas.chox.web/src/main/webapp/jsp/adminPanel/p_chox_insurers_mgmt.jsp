<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">
        
    var adminCurrentTabIndex = 0;
    var adminTabs;
    var isNew = false;
        
    $(document).ready(function(){

        var objectId = <s:property value="objectId"/>;
            
        if(objectId<0){
            isNew = true;
        }
            
        $.validator.addMethod(
        "regex", function(value, element, regexp) {
            var re = new RegExp(regexp);
            return this.optional(element) || re.test(value);
        }, "Please check your input."
    );
            
        doFormValidation();
        doOwnershipChange();
                
    });
        
    function doFormValidation(){
                    
        var validateFlag = $("#formUpdateInsurerDetail").validate(
        {
            errorLabelContainer: "#CDmessageBox",
            rules: {
                name:{ required:true },
                vatNo:{ required:true, number:true },
                companyNo:{ required:true, number:true },
                address1:{ required:true },
                address2:{ required:true },
                address4:{ required:true, regex: "^\\s*[a-zA-Z.,\\s]+\\s*$" },
                address5:{ required:true, regex: "^\\s*[a-zA-Z.,\\s]+\\s*$" },
                postcode:{ required:true },
                phone:{ regex:"^(\\(?\\+?[0-9]*\\)?)?[0-9_\\- \\(\\)]*$" },
                adminHandlingCharge:{ required:true, number:true, min:0 },
                choAgreedBenefitValue:{ required:true, number:true, min:0 },
                scsAgreedBenefitShareValue:{ required:true, number:true, min:0, max:100 }
            },
            messages: {
                name: {required:"You must supply a value for 'Name'" },
                adminHandlingCharge: { required:"You must supply a value for 'Admin Handling Charge'", number:"'Admin Handling Charge' must be numeric", min:"'Admin Handling Charge' cannot be less than zero" },
                vatNo:{ required:"You must supply a value for 'VAT No.'", number:"'VAT No.' must be number" },
                companyNo:{ required:"You must supply a value for 'Company No.'", number:"'Company No' must be number" },
                address1:{ required:"You must supply a value for 'Address 1'" },
                address2:{ required:"You must supply a value for 'Address 2'" },
                address4:{ required:"You must supply a value for 'County'", regex:"'County' must be letters only" },
                address5:{ required:"You must supply a value for 'Country'", regex:"'Country' must be letters only" },
                postcode:{ required:"You must supply a value for 'Postcode'" },
                phone:{ regex:"'Telephone Number' must be numeric" },
                choAgreedBenefitValue:{ required:"You must supply a value for 'Agreed Benefit Value'", number:"'Agreed Benefit Value' must be numeric", min:"'Agreed Benefit Value' cannot be less than zero" },
                scsAgreedBenefitShareValue:{ required:"You must supply a value for 'SCS Agreed Benefit Share'", number:"'SCS Agreed Benefit Share' must be numeric", min:"'SCS Agreed Benefit Share' cannot be less than zero", max:"'SCS Agreed Benefit Share' cannot be higher than 100%" }
            },
            submitHandler: function(form) {}
        });
            
        return validateFlag;
    }
        
    function doInsurerSubmit(){
            
        var confirmationMsg = "Do you wish to accept changes?";
            
        if(isNew){ confirmationMsg = "Are you sure you wish to add this insurer?"; }

        if(doFormValidation().form()){
            
            if(confirm(confirmationMsg)){

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
    }
        
    function doInsurerBack(){
        var sLocaltion = "#admin_param_panel";
        var sAction = "<%= request.getContextPath()%>/prv/p/loadAdminPanel.action";
        var sparameters = "adminPanelName=ChoxInsurerMgmtPanel";
        doSectionLoad(sLocaltion, sAction, sparameters);
    }
        
    function onBeforeSubmit(formData, jqForm, options) {
    }

    function onSubmitResponseReceived(responseText, statusText){
            
        var response = eval('(' + responseText.trim() + ')');
        var output = "Your changes have been saved";
            
        $("#chox-form-submit-result").attr("class", "chox-form-submit-result")
        $("#chox-form-submit-result").html("");
            
        if(response && response.isValid){

            if(response.resultType == "New" && response.result)
            {
                var newObjectId = parseInt(response.result);
                $("#admin_param_panel").load("<%= request.getContextPath()%>/prv/p/updateInsurerDetailPanel.action?objectId=" + newObjectId+uniqeToken());
                propmtMsg("Insurer", "New Insurer has been created. Please create a BRE Band for this Insurer using the BRE Band tab and associate Credit Hire Organisations to this via the BRE Band Mapping tab.");
            }
            else if(response.resultType && response.resultType == 'Message'){
                $("#chox-form-submit-result").attr("class", "submit-error")
                $("#chox-form-submit-result").html(response.result);
            }else{
                output = "Your changes have been saved.";
                $("#chox-form-submit-result").html(output);
            }
        }
        else if(response && response.errors){
                
            output = formErrorMessage(response.errors);
            $("#chox-form-submit-result").attr("class", "submit-error")
            $("#chox-form-submit-result").html(output);
                
        }
        else
        {
            output = "Unknown Error Encountered, please try again.";
            $("#chox-form-submit-result").attr("class", "submit-error");
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
            height:635,
            width:740,
            activeTab: adminCurrentTabIndex,
            items:[
                {contentEl:'insurerDetailPanelTab', title:'Details',listeners: {activate: insHandleActivate}},
                {contentEl:'insurerAliasPanelTab', title:'Alias', disabled:<s:property value="isNew"/>, listeners: {activate: insHandleActivate}},
                {contentEl:'insurerWorkgroupPanelTab', title:'Workgroup', disabled:<s:property value="isNew"/>, listeners: {activate: insHandleActivate}},
                {contentEl:'insurerCreditHirePanelTab', title:'Credit Hire Mapping', disabled:<s:property value="isNew"/>, listeners: {activate: insHandleActivate}},
                {contentEl:'insurerBrePanelTab', title:'BRE Band', disabled:<s:property value="isNew"/>, listeners: {activate: insHandleActivate}},
                {contentEl:'insurerBreMappingPanelTab', title:'BRE Band Mapping', disabled:<s:property value="isNew"/>, listeners: {activate: doBreMappingRefresh}},
                {contentEl:'insurerVehicleClassCeilingTab', title:'Vehicle Class Ceilings', disabled:<s:property value="isNew"/>, listeners: {activate: insHandleActivate}}
            ]   
        });
    }
       
    Ext.onReady(function(){
        setupTabPanels();
    });

    function doBreMappingRefresh(tab){
        insHandleActivate(tab);
        onBreBandPageRefresh();
    }
        
    function insHandleActivate(tab){
            
        adminCurrentTabIndex = 0;
            
        if(adminTabs)
        {
            adminCurrentTabIndex = adminTabs.items.indexOf(adminTabs.getActiveTab());
        }
            
    }

    function doOwnershipChange(){

        var claimOwnershipEnable = false;
        if($('form#formUpdateInsurerDetail input[name="claimOwnershipEnable"]:checked').val()){
            claimOwnershipEnable = true;
        }

        if(!claimOwnershipEnable){
            $('form#formUpdateInsurerDetail input[name="claimOwnershipLocked"]').attr('disabled', true);
            $('form#formUpdateInsurerDetail input[name="claimOwnershipLocked"]').attr('checked', false);
        }else{
            $('form#formUpdateInsurerDetail input[name="claimOwnershipLocked"]').attr('disabled', false);
        }

    }

       
</script>

<div id="mainPanel" class="admin-tab-css"></div>

<div id="insurerBrePanelTab" class="x-hide-display">
    <div class="sub-admin-tab-css">
        <s:action name="loadAdminPanel" executeResult="true">
            <s:param name="adminPanelName">InsurerBreBandMgmt</s:param>
            <s:param name="selectOrgId"><s:property value="objectId" /></s:param>        
        </s:action>
    </div>
</div>

<div id="insurerBreMappingPanelTab" class="x-hide-display">
    <div class="sub-admin-tab-css">
        <s:action name="loadAdminPanel" executeResult="true">
            <s:param name="adminPanelName">InsurerBreBandMappingMgmt</s:param>
            <s:param name="selectOrgId"><s:property value="objectId" /></s:param>        
        </s:action>
    </div>
</div>

<div id="insurerWorkgroupPanelTab" class="x-hide-display">
    <div class="sub-admin-tab-css">
        <s:action name="loadAdminPanel" executeResult="true">
            <s:param name="adminPanelName">InsurerWorkgroupMappingMgmt</s:param>
            <s:param name="selectOrgId"><s:property value="objectId" /></s:param>
        </s:action> 
    </div>
</div>

<div id="insurerCreditHirePanelTab" class="x-hide-display">
    <div class="sub-admin-tab-css">
        <s:action name="loadAdminPanel" executeResult="true">
            <s:param name="adminPanelName">InsurerOrgMappingMgmt</s:param>
            <s:param name="selectOrgId"><s:property value="objectId" /></s:param>        
        </s:action> 
    </div>
</div>

<div id="insurerAliasPanelTab" class="x-hide-display">
    <div class="sub-admin-tab-css">
        <s:action name="loadAdminPanel" executeResult="true">
            <s:param name="adminPanelName">InsurerAliasMappingMgmt</s:param>
            <s:param name="selectOrgId"><s:property value="objectId" /></s:param>        
        </s:action>
    </div>
</div>

<div id="insurerVehicleClassCeilingTab" class="x-hide-display">
    <div class="sub-admin-tab-css">

        <!--
        <s:action name="loadAdminPanel" executeResult="true">
            <s:param name="adminPanelName">InsurerVehicleClassCeiling</s:param>
            <s:param name="selectOrgId"><s:property value="objectId" /></s:param>
        </s:action>
        !-->
    </div>
</div>

<div id="insurerDetailPanelTab" class="x-hide-display">

    <div class="sub-admin-tab-css">

        <form id="formUpdateInsurerDetail" action="<%= request.getContextPath()%>/prv/p/updateInsurerDetail.action" class="XXentity-form" onsubmit="return true;">

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
                    <label class="chox-form-std-label">Telephone Number</label>
                    <input type="text" maxlength="50" class="chox-ttxt" id="CCDPhone" name="phone" value="<s:property value="phone" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Admin Handling Charge (£)<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDAdminHandlingCharge" name="adminHandlingCharge" value="<s:property value="adminHandlingCharge" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Agreed Benefit Value (£)<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDAhoAgreedBenefitValue" name="choAgreedBenefitValue" value="<s:property value="choAgreedBenefitValue" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">SCS Agreed Benefit Share (%)<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDScsAgreedBenefitShareValue" name="scsAgreedBenefitShareValue" value="<s:property value="scsAgreedBenefitShareValue" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Enable Workgroup</label>
                    <s:checkbox name="workgroupEnable" value="workgroupEnable" />
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Enable Automatic Claim Routing</label>
                    <s:checkbox name="autoRoutingEnable" value="autoRoutingEnable" />
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Enable Claim Ownership</label>
                    <s:checkbox name="claimOwnershipEnable" value="claimOwnershipEnable" onchange="javascript:doOwnershipChange();" /> Locked? <s:checkbox name="claimOwnershipLocked" value="claimOwnershipLocked" />
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Active</label>
                    <s:checkbox name="status" value="status" />
                </div>                   
                <div class="chox-form-button">

                    <input type="button" value='Save Changes' onclick="javascript: return doInsurerSubmit();"/>
                    <input type="button" value='Cancel' class="cancel" onclick="javascript: return doInsurerBack();" />

                </div>
                <div id="CDmessageBox" class="acknowledge-message-box"></div>

            </div>
            <br/>
            <div class="chox-form-submit-result" id="chox-form-submit-result"></div>

        </form>

    </div>



</div>
