<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var insDetailAdminTabIndex = 0;
    var insAdminTabs;
    var isNew = true;
    var insurerIsWorkgroupEnabled = <s:property value="insurerIsWorkgroupEnabled" />;
    var autoRoutingPolicyNumberEnabled = <s:property value="autoRoutingEnable"/>;
    var autoRoutingPrice = <s:property value="autoRoutingEnablePrice"/>;
    var disableDiscountTab = true;
    var wgrpJsonReader;
    var workgroupStore;
    var workgroupCombo;
    var claimOwnerReader;
    var claimOwnerStore;
    var claimOwnerCombo;
    var workgroupId=-1;
    var tpiEnable=false;
    var claimOwnerId=-1;
    var workgroupIdField=-1;
    var claimOwnerIdField=-1;
    var disableIPWhitelistTab = true;

    Ext.onReady(function(){

         Ext.override(Ext.form.ComboBox, {
             setValue : function(v){
                 //begin patch
                 // Store not loaded yet? Set value when it *is* loaded.
                 // Defer the setValue call until after the next load.
                 if (this.store.getCount() === 0) {
                     this.store.on('load',
                     this.setValue.createDelegate(this, [v]), null, {single: true});
                     return;
                 }
                 //end patch
                 var text = v;
                 if(this.valueField){
                     var r = this.findRecord(this.valueField, v);
                     if(r){
                         text = r.data[this.displayField];
                     }else if(this.valueNotFoundText !== undefined){
                         text = this.valueNotFoundText;
                     }
                 }
                 this.lastSelectionText = text;
                 if(this.hiddenField){
                     this.hiddenField.value = v;
                 }
                 Ext.form.ComboBox.superclass.setValue.call(this, text);
                 this.value = v;
             }});

        new Ext.ToolTip({ target: 'help-claimLocked', html: '"Enable claim locked" will restrict FNOL, COM, and CH users to only be able to edit claims that belong to them'});

            wgrpJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'text'},
                    {name:'value'}
                ]
            });

            workgroupStore = new choxDataStore({
                url : "/prv/p/WorkgroupDropDownActionByInsurer3.action", 
                params : {"orgId":'<s:property value="objectId"/>'},
                reader: wgrpJsonReader
            });

            workgroupCombo = new Ext.form.ComboBox({
                store: workgroupStore,
                width: 200,
                renderTo: 'workgroupComboDiv1',
                valueField: 'text',
                id: 'workgroupComboId',
                hiddenName: 'workgroupIdField',
                displayField:'value',
                typeAhead: true,
                mode: 'local',
                editable:false,
                triggerAction: 'all',
                emptyText: '<s:property value="workgroupIdFieldName"/>',
                forceSelection: true,
                listWidth: 200,
                selectOnFocus: true,
                listeners: {
                    select:function() {
                        if(this.getRawValue() === "") {
                            this.clearValue();
                            this.reset();
                            workgroupId = -1;
                            workgroupIdField=workgroupId;
                            doRenderClaimHandlerDropDown1(workgroupId);
                        }else {
                            workgroupId=this.value;
                            workgroupIdField=workgroupId;
                            doRenderClaimHandlerDropDown1(workgroupId);
                        }
                    }
                }
            });

        workgroupStore.load({params : {"orgId":'<s:property value="objectId"/>'}});
            
        claimOwnerReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'id'},
                {name:'name'}
            ]
        });

        claimOwnerStore = new choxDataStore({
            url : "/prv/p/SearchClaimHandlerRoleUserDropDownAction.action",
            params : {"workgroupId":workgroupId, "insurerId":'<s:property value="objectId"/>'},
            reader : claimOwnerReader
        });

        claimOwnerCombo = new Ext.form.ComboBox({
            store: claimOwnerStore,
            width: 200,
            renderTo: 'claimOwnerComboDiv1',
            valueField: 'id',
            id: 'claimOwnerComboId',
            hiddenName: 'claimOwnerIdField',
            displayField:'name',
            typeAhead: true,
            mode: 'local',
            listWidth: 200,
            forceSelection: true,
            triggerAction: 'all',
            emptyText: '<s:property value="claimOwnerIdFieldName"/>',
            listeners: {
                select: function () {
                    if(this.getRawValue() === "") {
                        this.clearValue();
                        this.reset();
                        claimOwnerId = -1;
                        claimOwnerIdField=claimOwnerId;
                    }else {
                        claimOwnerId = this.value;
                        claimOwnerIdField=claimOwnerId;
                    }
                }
            }
        });
        
        claimOwnerStore.load({ params : {"workgroupId":workgroupId, "insurerId":'<s:property value="objectId"/>'}});
       
       // The below jquery submit function only used for adding additional callback function which is used to set 
       // the default value to the claimOwner and Workgroup. The actual form submission 
       // is performed by ui.ajaxForm($("form#formUpdateInsurerDetail"), doSubmitInsurerSucceed);
       // The below submit will not submit the form because the above said(ui.ajaxform) call back function returns false. 
        $("#formUpdateInsurerDetail").submit(function(){
            if($("[name='claimOwnerIdField']").val() === ""){
                $("[name='claimOwnerIdField']").val(-1);
                }
            if($("[name='workgroupIdField']").val() === ""){
                $("[name='workgroupIdField']").val(-1);
                }
        });

        // CHECK PROCESS MODE
        isNew = isTrue($("#isNew").val());
        
        if(<s:property value="insurerDiscountEnable"/> && !isNew){
            disableDiscountTab = false;
        }
        
        if(<s:property value="enableIPWhitelist"/> && !isNew){
            disableIPWhitelistTab = false;
        }
        // ADD REGULAR EXPRESSION FOR FORM VALIDATION
        $.validator.addMethod("regex", function(value, element, regexp) {
            var check = false;
            var re = new RegExp(regexp);
            return this.optional(element) || re.test(value);
        }, "Please check your input.");

        // USER DETAIL FORM VALIDATION
        var form = $("form#formUpdateInsurerDetail");

        form.validate(
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
                ecdIncreaseTriggerPercentage:{ required:true, number:true, min:0, max:100},
                scsAgreedBenefitShareValue:{ required:false, number:true, min:0, max:100 },
                fixedTransactionalFeeValue:{ required:false, number:true, min:0 },
                forcePasswordChange:{ required:true, number:true, min:0 },
                uniquePasswordHistory:{ required:true, number:true, min:1, max:15 },
                minimumPasswordLength:{ required:true, number:true, min:6, max:32 },
                maxLoginAttempts:{ required:true, number:true, min:0 },
                blockTime:{ required:true, number:true, min:0 },
                blockedMessage:{ required:true}
            },
            messages:
                {
                name: {required:"You must supply a value for 'Name'" },
                adminHandlingCharge: { required:"You must supply a value for 'Admin Handling Charge'", number:"'Admin Handling Charge' must be numeric", min:"'Admin Handling Charge' cannot be less than zero" },
                ecdIncreaseTriggerPercentage: { required:"You must supply a value for 'ECD % Increase Trigger Point'", number:"'ECD % Increase Trigger Point' must be numeric", min:"'ECD % Increase Trigger Point' cannot be less than zero", max:"'ECD % Increase Trigger Point' cannot be higher than 100%" },
                vatNo:{ required:"You must supply a value for 'VAT No.'", number:"'VAT No.' must be number" },
                companyNo:{ required:"You must supply a value for 'Company No.'", number:"'Company No' must be number" },
                address1:{ required:"You must supply a value for 'Address 1'" },
                address2:{ required:"You must supply a value for 'Address 2'" },
                address4:{ required:"You must supply a value for 'County'", regex:"'County' must be letters only" },
                address5:{ required:"You must supply a value for 'Country'", regex:"'Country' must be letters only" },
                postcode:{ required:"You must supply a value for 'Postcode'" },
                phone:{ regex:"'Telephone Number' must be numeric" },
                scsAgreedBenefitShareValue:{ number:"'STS Agreed Benefit Share' must be numeric", min:"'STS Agreed Benefit Share' cannot be less than zero", max:"'STS Agreed Benefit Share' cannot be higher than 100%" },
                fixedTransactionalFeeValue:{ number:"'Fixed Transactional Fee' must be numeric", min:"'Fixed Transactional Fee' cannot be less than zero" },
                forcePasswordChange:{ required:"You must supply a value for 'Password Expiry Period'", number:"'Password Expiry Period' must be numeric", min:"'Password Expiry Period' cannot be less than zero" },
                uniquePasswordHistory:{ required:"You must supply a value for 'Number Of Unique Passwords'", number:"'Number Of Unique Passwords", min:"'Number Of Unique Passwords' cannot be less than one", max:"'Number Of Unique Passwords' cannot be larger than 15" },
                minimumPasswordLength:{ required:"You must supply a value for 'Minimum Password Length'", number:"'Minimum Password Length", min:"'Minimum Password Length' cannot be less than 6", max:"'Minimum Password Length' cannot be larger than 32" },
                maxLoginAttempts:{ required:"You must supply a value for 'Maximum login attempts'", number:"'Maximum login attempts' must be numeric", min:"'Maximum login attempts' cannot be less than 0"},
                blockTime:{ required:"You must supply a value for 'Account blocked period'", number:"'Account blocked period' must be numeric", min:"'Account blocked period' cannot be less than 0"},
                blockedMessage:{ required: "You must supply an 'Account blocked message'"}
            }
        });
        
        ui.ajaxForm($("form#formUpdateInsurerDetail"), doSubmitInsurerSucceed);

        getInsurerAdminTabIndex();
        doTpiEnableCheck();

        if(!autoRoutingPolicyNumberEnabled && !autoRoutingPrice){

            insAdminTabs = new Ext.TabPanel({
                renderTo: 'mainPanel',
                height:615,
                width:775,
                enableTabScroll : true,
                border:true,
                loadMask:false,
                activeTab: insDetailAdminTabIndex,
                items:[
                    {contentEl:'insurerDetailPanelTab', id:"insurerDetailPanelTabId", title:'Details', tabTip:'Insurer Details',listeners: {activate: insHandleActivate}},
                    {contentEl:'insurerAliasPanelTab', id:"insurerAliasPanelTabId", activate:true, title:'Alias', tabTip:'Insurer Alias', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: choxUpdateEl({url:'p/getInsurerAliasPage.action', params:{"insurerId" : '<s:property value="objectId" />'}})},
                    {contentEl:'insurerWorkgroupPanelTab', id:"insurerWorkgroupPanelTabId", title:'Workgroup', tabTip:'Insurer Workgroup', disabled:(isNew || !insurerIsWorkgroupEnabled), listeners: {activate: insHandleActivate}, autoLoad: choxUpdateEl({url:'p/getInsurerWorkgroupPage.action', params:{"insurerId" : '<s:property value="objectId" />'}})},
                    {contentEl:'insurerCreditHirePanelTab', id:"insurerCreditHirePanelTabId", title:'Credit Hire Mapping', tabTip:'Insurer Credit Hire Mapping', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: choxUpdateEl({url:'p/getInsurerChorganisationMappingPage.action', params:{"insurerId" : '<s:property value="objectId" />'}})},
                    {contentEl:'insurerBrePanelTab', id:"insurerBrePanelTabId", title:'BRE Band', tabTip:'Insurer BRE Band', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: choxUpdateEl({url:'p/getInsurerBreBandPage.action', params:{"insurerId" : '<s:property value="objectId" />'}})},
                    {contentEl:'insurerBreMappingPanelTab', id:"insurerBreMappingPanelTabId", title:'BRE Band Mapping', tabTip:'BRE Band Mapping', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: choxUpdateEl({url:'p/getInsurerBreBandChorganisationMapping.action', params:{"insurerId" : '<s:property value="objectId" />'}})},
                    {contentEl:'insurerVehicleClassCeilingTab', id:"insurerVehicleClassCeilingTabId", title:'Vehicle Class Ceilings', tabTip:'Insurer Vehicle Class Ceilings', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: choxUpdateEl({url:'p/getInsurerVehicleClassCeilingPage.action', params:{"insurerId" : '<s:property value="objectId" />'}})},
                    {contentEl:'insurerInteligentNoteTab', 
                        id:"insurerInteligentNoteTabId", 
                        title:'Intelligent Notes', tabTip:'Insurer Intelligent Notes', 
                        disabled:isNew, 
                        listeners: {activate: insHandleActivate}, 
                        autoLoad: choxUpdateEl({url:'p/getInteligentNotesPage.action', params:{"insurerId" : '<s:property value="objectId" />'}})},
                    {contentEl:'insurerAutoRoutingTab', id:"insurerAutoRoutingTabId", title:'Automatic Routing', tabTip:'Insurer Automatic Routing', disabled:true, listeners: {activate: insHandleActivate}, autoLoad: choxUpdateEl({url:'p/getInsurerAutomaticRoutingPage.action', params:{"insurerId" : '<s:property value="objectId" />'}})},
                    {contentEl:'InsurerDiscountsTab', id:"InsurerDiscountsTabId", title:'Discounts', tabTip:'Insurer Discounts', disabled: disableDiscountTab, listeners: {activate: insHandleActivate}, autoLoad: choxUpdateEl({url:'p/getInsurerDiscountPage.action', params:{"insurerId" : '<s:property value="objectId" />'}})},
                    {contentEl:'isnurerReasonOfRejectionTab', id:"reasonOfRejetictionTabId", title:'Rejection Reasons', tabTip:'Manage Reasons Of Rejection Per Insurer', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: choxUpdateEl({url:'p/getReasonsOfRejectionPage.action', params:{"insurerId" : '<s:property value="objectId" />'}})},
                    {contentEl:'IPWhitelistConfigTab', id:"IPWhitelistConfigTabId", title:'IP Whitelist', tabTip:'IP Whitelist Address', disabled: disableIPWhitelistTab, listeners: {activate: insHandleActivate}, autoLoad: choxUpdateEl({url:'p/getIPWhitelistPage.action', params:{"orgId" : '<s:property value="objectId" />', 'orgType' : 2}})}
                ]
            });

        }else{

            insAdminTabs = new Ext.TabPanel({
                renderTo: 'mainPanel',
                height:615,
                width:775,
                enableTabScroll : true,
                border:true,
                loadMask:false,
                activeTab: insDetailAdminTabIndex,
                items:[
                    {contentEl:'insurerDetailPanelTab', id:"insurerDetailPanelTabId", title:'Details', tabTip:'Insurer Details',listeners: {activate: insHandleActivate}},
                    {contentEl:'insurerAliasPanelTab', id:"insurerAliasPanelTabId", activate:true, title:'Alias', tabTip:'Insurer Alias', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: choxUpdateEl({url:'p/getInsurerAliasPage.action', params:{"insurerId" : '<s:property value="objectId" />'}})},
                    {contentEl:'insurerWorkgroupPanelTab', id:"insurerWorkgroupPanelTabId", title:'Workgroup', tabTip:'Insurer Workgroup', disabled:(isNew || !insurerIsWorkgroupEnabled), listeners: {activate: insHandleActivate}, autoLoad: choxUpdateEl({url:'p/getInsurerWorkgroupPage.action', params:{"insurerId" : '<s:property value="objectId" />'}})},
                    {contentEl:'insurerCreditHirePanelTab', id:"insurerCreditHirePanelTabId", title:'Credit Hire Mapping', tabTip:'Insurer Credit Hire Mapping', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: choxUpdateEl({url:'p/getInsurerChorganisationMappingPage.action', params:{"insurerId" : '<s:property value="objectId" />'}})},
                    {contentEl:'insurerBrePanelTab', id:"insurerBrePanelTabId", title:'BRE Band', tabTip:'Insurer BRE Band', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: choxUpdateEl({url:'p/getInsurerBreBandPage.action', params:{"insurerId" : '<s:property value="objectId" />'}})},
                    {contentEl:'insurerBreMappingPanelTab', id:"insurerBreMappingPanelTabId", title:'BRE Band Mapping', tabTip:'BRE Band Mapping', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: choxUpdateEl({url:'p/getInsurerBreBandChorganisationMapping.action', params:{"insurerId" : '<s:property value="objectId" />'}})},
                    {contentEl:'insurerVehicleClassCeilingTab', id:"insurerVehicleClassCeilingTabId", title:'Vehicle Class Ceilings', tabTip:'Insurer Vehicle Class Ceilings', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: choxUpdateEl({url:'p/getInsurerVehicleClassCeilingPage.action', params:{"insurerId" : '<s:property value="objectId" />'}})},
                    {contentEl:'insurerInteligentNoteTab', 
                        id:"insurerInteligentNoteTabId", 
                        title:'Intelligent Notes', 
                        tabTip:'Insurer Intelligent Notes', 
                        disabled:isNew, 
                        listeners: {activate: insHandleActivate}, autoLoad: choxUpdateEl({url:'p/getInteligentNotesPage.action', params:{"insurerId" : '<s:property value="objectId" />'}})},
                    {contentEl:'insurerAutoRoutingTab', id:"insurerAutoRoutingTabId", title:'Automatic Routing', tabTip:'Insurer Automatic Routing', disabled:(isNew || !insurerIsWorkgroupEnabled), listeners: {activate: insHandleActivate}, autoLoad: choxUpdateEl({url:'p/getInsurerAutomaticRoutingPage.action', params:{"insurerId" : '<s:property value="objectId" />'}})},
                    {contentEl:'InsurerDiscountsTab', id:"InsurerDiscountsTabId", title:'Discounts', tabTip:'Insurer Discounts', disabled: disableDiscountTab, listeners: {activate: insHandleActivate}, autoLoad: choxUpdateEl({url:'p/getInsurerDiscountPage.action', params:{"insurerId" : '<s:property value="objectId" />'}})},
                    {contentEl:'isnurerReasonOfRejectionTab', id:"reasonOfRejetictionTabId", title:'Rejection Reasons', tabTip:'Manage Reasons Of Rejection Per Insurer', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: choxUpdateEl({url:'p/getReasonsOfRejectionPage.action', params:{"insurerId" : '<s:property value="objectId" />'}})},
                    {contentEl:'IPWhitelistConfigTab', id:"IPWhitelistConfigTabId", title:'IP Whitelist', tabTip:'IP Whitelist Address', disabled: disableIPWhitelistTab, listeners: {activate: insHandleActivate}, autoLoad: choxUpdateEl({url:'p/getIPWhitelistPage.action', params:{"orgId" : '<s:property value="objectId" />', 'orgType' : 2}})}
                ]
            });

        }

        var isFixedTransactionalFee = <s:property value="fixedTransactionalFee"/>;
        doPageLoadCheck();

        if (isFixedTransactionalFee) {
            $("#fixedTransactionalFeeOpt").val("true");
            $("#CCDFixedTransactionalFeeValue").show();
            $("#CCDFixedTransactionalFeeManualValue").show();
            $("#CCDScsAgreedBenefitShareValue").hide();
            $("#CCDAhoAgreedBenefitValueDiv").hide();
        } else {
            $("#fixedTransactionalFeeOpt").val("false");
            $("#CCDFixedTransactionalFeeValue").hide();
            $("#CCDFixedTransactionalFeeManualValue").hide();
            $("#CCDScsAgreedBenefitShareValue").show();
            $("#CCDAhoAgreedBenefitValueDiv").show();
        }

        if (autoRoutingPolicyNumberEnabled) {
            $("select#autoRoutingEnableDropDownId").val("autoRoutingEnable");
        } else if(autoRoutingPrice){
            $("select#autoRoutingEnableDropDownId").val("autoRoutingEnablePrice");
        }else{
            $("select#autoRoutingEnableDropDownId").val("");
        }
        
        displayAutoRoutingTpiAndSusbscriberFields();
        
        $.validator.addMethod(
            "checkGtaRegexField",
            function(value, element) {
                    if ($('form#formUpdateInsurerDetail input[id="gtaAutoRoutingEnable"]:checked').val()
                            && $('#gtaExclusionId').val() === ""){
                        return false;
                    }
                return true;
            }
        );
        
        $.validator.addMethod(
                "checkTpiRegexField",
                function(value, element) {
                        if ($('form#formUpdateInsurerDetail input[id="tpiAutoRoutingEnable"]:checked').val()
                                && $('#tpiExclusionId').val() === ""){
                            return false;
                        }
                    return true;
                    }
            );
        
        $.validator.addMethod(
                "checkInsurerManualRegexField",
                function(value, element) {
                        if ($('form#formUpdateInsurerDetail input[id="insurerManualAutoRoutingEnable"]:checked').val()
                                && $('#insurerManualExclusionId').val() === ""){
                            return false;
                        }
                    return true;
                    }
            );
        
        $.validator.addMethod(
                "checkInsurerVsInsurerRegexField",
                function(value, element) {
                        if ($('form#formUpdateInsurerDetail input[id="insurerVsInsurerAutoRoutingEnable"]:checked').val()
                                && $('#insurerVsInsurerExclusionId').val() === ""){
                            return false;
                        }
                    return true;
                    }
            );
        
        $.validator.addMethod(
                "checkSubscriberRegexField",
                function(value, element) {
                        if ($('form#formUpdateInsurerDetail input[id="subscriberAutoRoutingEnable"]:checked').val()
                                && $('#subscriberExclusionId').val() === ""){
                            return false;
                        }
                    return true;
                    }
            );
        
        $.validator.addMethod(
                "checkFixedFeeRegexField",
                function(value, element) {
                        if ($('form#formUpdateInsurerDetail input[id="fixedFeeAutoRoutingEnable"]:checked').val()
                                && $('#fixedFeeExclusionId').val() === ""){
                            return false;
                        }
                    return true;
                    }
            );
      
        $.validator.addMethod(
                "checkCollaborationProtocolRegexField",
                function(value, element) {
                        if ($('form#formUpdateInsurerDetail input[id="collaborationProtocolAutoRoutingEnable"]:checked').val()
                                && $('#collaborationProtocolExclusionId').val() === ""){
                            return false;
                        }
                    return true;
                    }
            );
      
        if($('#CCDName').val() !== "")
            $('#nameField').hide();
    });
    
    function displayAutoRoutingTpiAndSusbscriberFields() {
        if($('form#formUpdateInsurerDetail input[name="thirdPartyInterventionActivated"]:checked').val())
            $("#tpiTr").show();
        else
            $("#tpiTr").hide();
        
        if($('form#formUpdateInsurerDetail input[name="allowSubscriberClaims"]:checked').val())
            $("#subscriberTr").show();
        else
            $("#subscriberTr").hide();
        
        if($('form#formUpdateInsurerDetail input[name="allowFixedFeeClaims"]:checked').val())
            $("#fixedFeeTr").show();
        else
            $("#fixedFeeTr").hide();
        
        if($('form#formUpdateInsurerDetail input[name="allowCollaborationProtocolClaims"]:checked').val())
            $("#collaborationProtocolTr").show();
        else
            $("#collaborationProtocolTr").hide();
        
        if($('form#formUpdateInsurerDetail input[name="invoiceUploadEnabled"]:checked').val())
            $("#insurerManualTr").show();
        else
            $("#insurerManualTr").hide();
    }
    
    function getInsurerAdminTabIndex(){
        if($("#tabIndex").val()!==null && $("#tabIndex").val()!==''){
            insDetailAdminTabIndex = $("#tabIndex").val();
        }
    }

    function doRenderClaimHandlerDropDown1(workgroupId){

        if((insurerIsWorkgroupEnabled && workgroupId>0) || !insurerIsWorkgroupEnabled){
            claimOwnerStore.removeAll();
            claimOwnerStore.load({ params : {"workgroupId":workgroupId, "insurerId":<s:property value="objectId"/>}});
            claimOwnerCombo.reset();
        }
    }

    function insHandleActivate(tab){
        insDetailAdminTabIndex = 0;
        if(insAdminTabs){ insDetailAdminTabIndex = insAdminTabs.items.indexOf(insAdminTabs.getActiveTab()); }
    }

    function doInsurerCancelBack(){
        var target = "#admin_param_panel";
        var url = "/prv/p/loadAdminPanel.action";
        var param = {"adminPanelName":"ChoxInsurerMgmtPanel"};
        ajax.loadHtml2(url,param,function(data){
            $(target).html(data);
        });
    }
    function doToggleInsurerDiscount(){
        
        if($('form#formUpdateInsurerDetail input[name="insurerDiscountEnable"]:checked').val()){
            disableDiscountTab = false;
        }else{
            disableDiscountTab = true;
        }
    }

    function doPageLoadCheck(){
        var claimWorkgroupEnable = doWorkgroupCheck();
        var claimOwnershipEnable = doOwnershipCheck();
        var supervisorEscalation = doSupervisorEscalationCheck();
        doEnableManualInvoiceCheck(claimWorkgroupEnable,claimOwnershipEnable);
        displayAutoRoutingTpiAndSusbscriberFields();

        if(!claimWorkgroupEnable && !claimOwnershipEnable){
            $("#ClaimLockedHolder").hide();
            $('form#formUpdateInsurerDetail input[name="claimLocked"]').attr('checked', false);
        }else{
            $("#ClaimLockedHolder").slideDown();
        }
    }
    
    function doSupervisorEscalationCheck(){
        var supervisorEscalationFlag = false;
        if($('form#formUpdateInsurerDetail input[name="supervisorEnable"]:checked').val()){
            supervisorEscalationFlag = true;
             $("#supervisorEscalationFields").slideDown();
         }else{
             $("#supervisorEscalationFields").hide();
        }
        return supervisorEscalationFlag;
    }

    function doWorkgroupCheck(){
        var claimWorkgroupEnable = false;
        if($('form#formUpdateInsurerDetail input[name="workgroupEnable"]:checked').val()){
            claimWorkgroupEnable = true;
            $("#AutomaticClaimRoutingHolder").slideDown();
            $("#invoiceWorkgroupId").slideDown();
            $("#manualInvoiceWorkgroupHolder").slideDown();
        }else{
            $("#AutomaticClaimRoutingHolder").hide();
            $("#invoiceWorkgroupId").hide();
            $('form#formUpdateInsurerDetail input[name="enableManualInvoiceWorkgroups"]').attr('checked', false);
            $("#manualInvoiceWorkgroupHolder").hide();
            $("select#autoRoutingEnableDropDownId").val("");
        }
        return claimWorkgroupEnable;
    }

    function doEnableManualInvoiceCheck(claimWorkgroupEnable,claimOwnershipEnable){
        
        if($('form#formUpdateInsurerDetail input[name="invoiceUploadEnabled"]:checked').val()){
            if (claimOwnershipEnable) {
              $("#manualInvoiceOwnershipHolder").slideDown();  
            }
            if (claimWorkgroupEnable) {
               $("#manualInvoiceWorkgroupHolder").slideDown(); 
            }
            
        }else{
            $("#manualInvoiceOwnershipHolder").hide();
            $("#manualInvoiceWorkgroupHolder").hide();
        }
    }
    
    function doTpiEnableCheck(){
        displayAutoRoutingTpiAndSusbscriberFields();
        var tpiEnableEnable = false;
        if($('form#formUpdateInsurerDetail input[name="thirdPartyInterventionActivated"]:checked').val()){
            tpiEnableEnable = true;
            $("#tpiIdentifierId").slideDown();
        }else{
            $("#tpiIdentifierId").hide();
        }
        return tpiEnableEnable;
    }

    function doOwnershipCheck(){
        var claimOwnershipEnable = false;
        if($('form#formUpdateInsurerDetail input[name="claimOwnershipEnable"]:checked').val()){
            claimOwnershipEnable = true;
            $("#TpiClaimOwnerId").slideDown();
            $("#manualInvoiceOwnershipHolder").slideDown();
        }else{
            $("#TpiClaimOwnerId").hide();
            $('form#formUpdateInsurerDetail input[name="enableManualInvoiceOwnership"]').attr('checked', false);
            $("#manualInvoiceOwnershipHolder").hide();
        }
        return claimOwnershipEnable;
    }

    function chargeMethodSelected(fixedTransactionalFee) {
        if (fixedTransactionalFee === 'true') {
            $("#CCDFixedTransactionalFeeValue").show();
            $("#CCDFixedTransactionalFeeManualValue").show();
            $("#CCDScsAgreedBenefitShareValue").hide();
            $("#CCDAhoAgreedBenefitValueDiv").hide();
        } else if (fixedTransactionalFee === 'false') {
            $("#CCDFixedTransactionalFeeValue").hide();
            $("#CCDFixedTransactionalFeeManualValue").hide();
            $("#CCDScsAgreedBenefitShareValue").show();
            $("#CCDAhoAgreedBenefitValueDiv").show();
        }
    }
    
    function doSubmitInsurerSucceed(responseText, statusText){

        var response = eval('(' + responseText.trim() + ')');
  
        if(response && response.isValid)
        {
            if(response.resultType && response.resultType === 'New'){

                Ext.Msg.minWidth = 300;
                Ext.Msg.alert('New Insurer Created','A new Insurer has been created.');

                updateInsurerDetailPanel(response.result);
            }else{
                
                updateInsurerDetailPanel('<s:property value="objectId"/>');

                Ext.Msg.minWidth = 300;
                Ext.Msg.alert('Save Changes','Your changes have been saved.');
                
            } // end of if else inner loop
        } else {
            Ext.MessageBox.show({
                title: '',
                msg: response.errors,
                width:300,
                buttons: Ext.MessageBox.OK,
                icon : Ext.MessageBox.ERROR
            });
        }
    }


    function updateInsurerDetailPanel(objectId) {
        var target = "#admin_param_panel";
        var url = "/prv/p/updateInsurerDetailPanel.action";
        var param = {"objectId":objectId};

        ajax.loadHtml2(url,param, function(data){
            $(target).html(data);
        });
    }

</script>

<input name="tabIndex" id="tabIndex" type="hidden" value="<s:property value="tabIndex" />"/>
<input name="isNew" id="isNew" type="hidden" value="<s:property value="isNew" />"/>
<input name="insurerIsWorkgroupEnabled" id="insurerIsWorkgroupEnabled" type="hidden" value="<s:property value="insurerIsWorkgroupEnabled" />"/>

<div id="chox-admin-holder" >
    <div id="chox-admin-col-div" style="width:780px">
        <div id="header-title">
            <label>Insurer Name:
                <s:if test="!isNew"><s:property value="name" /> </s:if><s:else>Create New Insurer</s:else>
                </label>
            </div>
            <div id="mainPanel"></div>
            <div id="insurerDetailPanelTab" class="x-hide-display">
                <div class="sub-admin-tab-css">
                        <form id="formUpdateInsurerDetail" name="formUpdateInsurerDetail" action="<%= request.getContextPath()%>/prv/p/updateInsurerDetail.action" class="XXentity-form" method="POST">
                    <input type="hidden" name="objectId" id="objectId" value='<s:property value="objectId"/>'/>
                
                    <div class="admin-form-container">
                        <fieldset class="x-fieldset">
                            <legend>Insurer Details</legend>
                            <div class="chox-form-item" id="nameField">
                                <label class="chox-form-std-label">Name<span class="mandatory">*</span></label>
                                <input type="text" class="chox-ttxt" id="CCDName" name="name" value="<s:property value="name" />"/>
                                <input type="hidden" name="originalName" id="originalName" value='<s:property value="name"/>'/>
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
                                <label class="chox-form-std-label">Active</label>
                                <s:checkbox name="status" value="status" />
                            </div>
                        </fieldset>

                        <fieldset class="x-fieldset">
                            <legend>Password Policy</legend>
                            <div class="chox-form-item" id="CCDForcePasswordChangeDiv">
                                <label class="chox-form-std-label">Password Expiry Period (Days)<span class="mandatory">*</span></label>
                                <input type="text" class="chox-ttxt" id="CCDForcePasswordChange" name="forcePasswordChange" value="<s:property value="forcePasswordChange" />"/>
                            </div>
    
                            <div class="chox-form-item" id="CCDUniquePasswordHistoryDiv">
                                <label class="chox-form-std-label">Number Of Unique Passwords<span class="mandatory">*</span></label>
                                <input type="text" class="chox-ttxt" id="CCDUniquePasswordHistory" name="uniquePasswordHistory" value="<s:property value="uniquePasswordHistory" />"/>
                            </div>
                        
                            <div class="chox-form-item" id="CCDMinimumPasswordLengthDiv">
                                <label class="chox-form-std-label">Minimum Password Length<span class="mandatory">*</span></label>
                                <input type="text" class="chox-ttxt" id="CCDMinimumPasswordLength" name="minimumPasswordLength" value="<s:property value="minimumPasswordLength" />"/>
                            </div>
    
                            <div class="chox-form-item" id="CCDMaximumLoginAttemptsDiv">
                                <label class="chox-form-std-label" style="margin-top : -7px;">Maximum login attempts<br/> (before account blocked)<span class="mandatory">*</span></label>
                                <input type="text" class="chox-ttxt" id="CCDMaximumLoginAttempts" name="maxLoginAttempts" value="<s:property value="maxLoginAttempts" />"/>
                            </div>
    
                            <div class="chox-form-item" id="CCDBlockTimeDiv">
                                <label class="chox-form-std-label">Account blocked period (in minutes)<span class="mandatory">*</span></label>
                                <input type="text" class="chox-ttxt" id="CCDBlockTime" name="blockTime" value="<s:property value="blockTime" />"/>
                            </div>
                            
                            <div class="chox-form-item" id="CCDBlockedMessageDiv">
                                <label class="chox-form-std-label">Account blocked message<span class="mandatory">*</span></label>
                                <textarea id="CCDBlockedMessage" name="blockedMessage" cols="30"  rows="3"><s:property value="blockedMessage" /></textarea>
                            </div>
                       </fieldset>
                       
                       <fieldset class="x-fieldset">
                            <legend>Workflow Parameters</legend>
                            
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">Enable Supervisor Escalation</label>
                                <s:checkbox name="supervisorEnable" value="supervisorEnable" onclick="doPageLoadCheck(this);" style="margin-left : 2px;" />
                            </div>
                            <div id="supervisorEscalationFields">
                                <div class="chox-form-item" id="nrOfDaysInvoiceUploadedDiv">
                                    <label class="chox-form-std-label">
                                        Days Since Invoice Upload to Escalate
                                    </label>
                                    <input type="text" class="chox-ttxt"
                                           id="nrOfDaysUploadedId" name="daysBeforeEscalated"
                                           value="<s:property value="daysBeforeEscalated" />" />
                                </div>
                                <div class="labelWithInputField">
                                    <div class="chox-form-item" id="timeEnteredToContestedInvDiv">
                                        <label class="chox-form-std-label" style="margin-top : -7px;">
                                            Number of Times Contested<br/> With CHO to Escalate 
                                        </label>
                                        <input type="text"
                                               class="chox-ttxt" id="timeEnteredToContId"
                                               name="timesInStatusContested"
                                               value="<s:property value="timesInStatusContested" />" />
                                    </div>
                                </div>
                            </div>
                      <table>
                             <tr>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable Engineers</label>
                                        <s:checkbox name="engineersEnable" value="engineersEnable" onclick="doPageLoadCheck(this);"/>
                                    </div>
                                </td>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable FNOL</label>
                                        <s:checkbox name="fnolEnable" value="fnolEnable" onclick="doPageLoadCheck(this);"/>
                                    </div>
                                </td>
                                
                            </tr>
                            <tr>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable Insurer Claims</label>
                                        <s:checkbox name="claimUploadEnabled" value="claimUploadEnabled" onclick="doPageLoadCheck(this);"/>
                                    </div>
                                </td>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable Insurer Invoices</label>
                                        <s:checkbox name="invoiceUploadEnabled" value="invoiceUploadEnabled" onclick="doPageLoadCheck(this);"/>
                                    </div>
                                </td>
                            </tr>
                          
                            <tr>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable Online Support Form</label>
                                        <s:checkbox name="onlineSupportEnable" value="onlineSupportEnable" onclick="doPageLoadCheck(this);" />
                                    </div>
                                </td>
                                 <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable Task Management</label>
                                        <s:checkbox name="taskManagementEnable" value="taskManagementEnable" onclick="doPageLoadCheck()" />
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Allow Subscriber Claims</label>
                                        <s:checkbox name="allowSubscriberClaims" value="allowSubscriberClaims" onclick="displayAutoRoutingTpiAndSusbscriberFields()"/>
                                    </div>
                                </td>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Allow Fixed Fee Claims</label>
                                        <s:checkbox name="allowFixedFeeClaims" value="allowFixedFeeClaims" onclick="displayAutoRoutingTpiAndSusbscriberFields()"/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Allow Collaboration Protocol Claims</label>
                                        <s:checkbox name="allowCollaborationProtocolClaims" value="allowCollaborationProtocolClaims" onclick="displayAutoRoutingTpiAndSusbscriberFields()"/>
                                    </div>
                                </td>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable Direct Invoice Upload (TPI)</label>
                                        <s:checkbox name="thirdPartyInterventionActivated" value="thirdPartyInterventionActivated" onclick="doTpiEnableCheck(this)"/>
                                    </div>
                                </td>
                            </tr>
                        </table>
                             <div class="chox-form-item" id="tpiIdentifierId">
                                    <label class="chox-form-std-label1">Invoice Identification String (TPI)</label>
                                    <input type="text" class="chox-ttxt" style="width: 200px; height:20px " id="tpiIdentifierId" name="tpiIdentificationString" value="<s:property value="tpiIdentificationString" />"/>
                                </div>
                        </fieldset>
                        
                        <fieldset class="x-fieldset">
                            <legend>Workgroup & Ownership</legend>
                            <table>
                                
                                <tr>
                                   <td colspan=2> 
                                       <div class="chox-form-item">
                                           <label class="chox-form-std-label">Enable Workgroup</label>
                                           <s:checkbox name="workgroupEnable" value="workgroupEnable" onclick="doPageLoadCheck(this);"/>
                                       </div>
                                    </td>
                                </tr>
                            
                               <tr>
                                   <td colspan=2>
                                        <div class="chox-form-item" id="AutomaticClaimRoutingHolder">
                                            <label class="chox-form-std-label">Automatic Claim Routing</label>
    
                                            <select id="autoRoutingEnableDropDownId" name="autoRoutingEnableId" >
                                                <option value="">--Disabled--</option>
                                                <option value="autoRoutingEnable">By Policy Number</option>
                                                <option value="autoRoutingEnablePrice">By Customer Vehicle Class Price</option>
                                            </select>
    
                                        </div>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <td>
                                        <div class="chox-form-item">
                                            <label class="chox-form-std-label">Enable Claim Ownership</label>
                                            <s:checkbox name="claimOwnershipEnable" value="claimOwnershipEnable" onclick="doPageLoadCheck(this);" />
                                        </div>
                                    </td>
                                </tr>
                                
                                 <tr>
                                   <td colspan=2>
                                        <div class="chox-form-item" id="ClaimLockedHolder">
                                            <label class="chox-form-std-label">Enable Claim Locked</label>
                                            <s:checkbox name="claimLocked" value="claimLocked" /><img id="help-claimLocked" class="help-icon" src="<%= request.getContextPath()%>/images/help.png"/>
                                        </div>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <td colspan=2>
                                        <div class="chox-form-item" id="manualInvoiceWorkgroupHolder">
                                            <label class="chox-form-std-label">Enable Insurer Invoice Workgroups</label>
                                            <s:checkbox name="enableManualInvoiceWorkgroups" id="enableManualInvoiceWorkgroupCheckboxId" value="enableManualInvoiceWorkgroups" />
                                        </div>
                                    </td>
                               </tr>
                               <tr>
                                    <td colspan=2>
                                        <div class="chox-form-item" id="manualInvoiceOwnershipHolder">
                                            <label class="chox-form-std-label">Enable Insurer Invoice Ownership</label>
                                            <s:checkbox name="enableManualInvoiceOwnership" id="enableManualInvoiceOwnershipCheckboxId" value="enableManualInvoiceOwnership" />
                                        </div>
                                    </td>
                                </tr>
                                 <tr>
                                    <td colspan="3">
                                    <br/>
                                        <div class="chox-form-item">
                                            <label class="chox-form-std-label1" style="width:620px; text-align: left;">
                                            Allow BRE Approved Invoices to move directly to 'AwaitingInvoicePayment' and be re-routed for 
                                            the following claim types (note an exclusion regex can optionally be specified which, 
                                            if matched on the claim number, will NOT move or re-route the invoice). 
                                            Note that Insurer Upload Invoices will be moved directly to 'ManualInvoiceBREApproved: </label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="40%">
                                        <div class="chox-form-item">
                                            <label class="chox-form-std-label">GTA</label>
                                            <s:checkbox name="gtaAutoRoutingEnable" id="gtaAutoRoutingEnable" value="gtaAutoRoutingEnable" />
                                        </div>
                                    </td>
                                    <td width="70%">
                                        <div class="chox-form-item">
                                            <label class="chox-form-std-label1" >Regex Exclusion pattern : </label>
                                            <input type="text" class="chox-ttxt" style="width: 150px; height:20px;" id="gtaExclusionId"  name="gtaRegexExpression" value="<s:property value="gtaRegexExpression" />"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr id="tpiTr">
                                    <td width="40%">
                                        <div class="chox-form-item" >
                                            <label class="chox-form-std-label">TPI</label>
                                            <s:checkbox name="tpiAutoRoutingEnable" id="tpiAutoRoutingEnable" value="tpiAutoRoutingEnable" />
                                        </div>
                                    </td>
                                    <td width="70%">
                                        <div class="chox-form-item" >
                                            <label class="chox-form-std-label1" >Regex Exclusion pattern : </label>
                                            <input type="text" class="chox-ttxt" style="width: 150px; height:20px; margin-top:6px;" id="tpiExclusionId"  name="tpiRegexExpression" value="<s:property value="tpiRegexExpression" />"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr id="subscriberTr">
                                    <td width="40%">
                                        <div class="chox-form-item" id="tpiOwnershipHolder">
                                            <label class="chox-form-std-label">Subscriber</label>
                                            <s:checkbox name="subscriberAutoRoutingEnable" id="subscriberAutoRoutingEnable" value="subscriberAutoRoutingEnable" />
                                        </div>
                                    </td>
                                    <td width="70%">
                                        <div class="chox-form-item" id="subscriberExclusionRegexId">
                                            <label class="chox-form-std-label1" >Regex Exclusion pattern : </label>
                                            <input type="text" class="chox-ttxt" style="width: 150px; height:20px; " id="subscriberExclusionId"  name="subscriberRegexExpression" value="<s:property value="subscriberRegexExpression" />"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr id="fixedFeeTr">
                                    <td width="40%">
                                        <div class="chox-form-item" id="fixedFeeHolder">
                                            <label class="chox-form-std-label">Fixed Fee</label>
                                            <s:checkbox name="fixedFeeAutoRoutingEnable" id="fixedFeeAutoRoutingEnable" value="fixedFeeAutoRoutingEnable" />
                                        </div>
                                    </td>
                                    <td width="70%">
                                        <div class="chox-form-item" >
                                            <label class="chox-form-std-label1" >Regex Exclusion pattern : </label>
                                            <input type="text" class="chox-ttxt" style="width: 150px; height:20px;" id="fixedFeeExclusionId"  name="fixedFeeRegexExpression" value="<s:property value="fixedFeeRegexExpression" />"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr id="collaborationProtocolTr">
                                    <td width="40%">
                                        <div class="chox-form-item" id="collaborationProtocolHolder">
                                            <label class="chox-form-std-label">Collaboration Protocol</label>
                                            <s:checkbox name="collaborationProtocolAutoRoutingEnable" id="collaborationProtocolAutoRoutingEnable" value="collaborationProtocolAutoRoutingEnable" />
                                        </div>
                                    </td>
                                    <td width="70%">
                                        <div class="chox-form-item" >
                                            <label class="chox-form-std-label1" >Regex Exclusion pattern : </label>
                                            <input type="text" class="chox-ttxt" style="width: 150px; height:20px;" id="collaborationProtocolExclusionId"  name="collaborationProtocolRegexExpression" value="<s:property value="collaborationProtocolRegexExpression" />"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="40%">
                                        <div class="chox-form-item" id="insurerVsInsurerHolder">
                                            <label class="chox-form-std-label">Insurer Vs Insurer</label>
                                            <s:checkbox name="insurerVsInsurerAutoRoutingEnable" id="insurerVsInsurerAutoRoutingEnable" value="insurerVsInsurerAutoRoutingEnable" />
                                        </div>
                                    </td>
                                    <td width="70%">
                                        <div class="chox-form-item" id="insurerVsInsurerExclusionRegexId">
                                            <label class="chox-form-std-label1" >Regex Exclusion pattern : </label>
                                            <input type="text" class="chox-ttxt" style="width: 150px; height:20px; " id="insurerVsInsurerExclusionId"  name="insurerVsInsurerRegexExpression" value="<s:property value="insurerVsInsurerRegexExpression" />"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr id="insurerManualTr">
                                    <td width="40%">
                                        <div class="chox-form-item" id="insurerManualOwnershipHolder">
                                            <label class="chox-form-std-label">Insurer Upload</label>
                                            <s:checkbox name="insurerManualAutoRoutingEnable" id="insurerManualAutoRoutingEnable" value="insurerManualAutoRoutingEnable" />
                                        </div>
                                    </td>
                                    <td width="70%">
                                        <div class="chox-form-item" id="insurerManualExclusionRegexId">
                                            <label class="chox-form-std-label1" >Regex Exclusion pattern : </label>
                                            <input type="text" class="chox-ttxt" style="width: 150px; height:20px;" id="insurerManualExclusionId"  name="insurerManualRegexExpression" value="<s:property value="insurerManualRegexExpression" />"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <br/>
                                        <br/>
                                        <div class="chox-form-item" id="invoiceWorkgroupId">
                                            <label class="chox-form-std-label1">Default Workgroup for Approved Invoices</label>
                                            <div id="workgroupComboDiv1"></div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div class="chox-form-item" id="TpiClaimOwnerId">
                                            <label class="chox-form-std-label1">Default Claim Owner for Approved Invoices</label>
                                            <div id="claimOwnerComboDiv1"></div>
                                        </div>
                                        
                                    </td>
                                </tr>
                                
                                
                            </table>
                       </fieldset>
                        
                        <fieldset class="x-fieldset">
                            <legend>Additional Parameters</legend>
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">Admin Handling Charge (£)<span class="mandatory">*</span></label>
                                <input type="text" class="chox-ttxt" id="CCDAdminHandlingCharge" name="adminHandlingCharge" value="<s:property value="adminHandlingCharge" />"/>
                            </div>
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">
                                    <select id="fixedTransactionalFeeOpt" name="fixedTransactionalFee" onchange="javascript:chargeMethodSelected(this.options[this.selectedIndex].value);">
                                        <option value="false">STS Agreed Benefit Share (%)</option>
                                        <option value="true">Fixed Transactional Fee (£)</option>
                                    </select>
                                </label>
                                <input type="text" class="chox-ttxt" id="CCDScsAgreedBenefitShareValue" name="scsAgreedBenefitShareValue" value="<s:property value="scsAgreedBenefitShareValue" />"/>
                                <input type="text" class="chox-ttxt" id="CCDFixedTransactionalFeeValue" name="fixedTransactionalFeeValue" value="<s:property value="fixedTransactionalFeeValue" />"/>
                            </div>
    
                            <div class="chox-form-item" id="CCDFixedTransactionalFeeManualValue">
                                <label class="chox-form-std-label">Manual Fixed Transactional Fee (£)</label>
                                <input type="text" class="chox-ttxt" id="CCDAhoAgreedBenefitValue" name="fixedTransactionalFeeManualValue" value="<s:property value="fixedTransactionalFeeManualValue" />"/>
                            </div>
                            <div class="chox-form-item" id="CCDAhoAgreedBenefitValueDiv">
                                <label class="chox-form-std-label">Agreed Benefit Value (£)</label>
                                <input type="text" class="chox-ttxt" id="CCDAhoAgreedBenefitValue" name="choAgreedBenefitValue" value="<s:property value="choAgreedBenefitValue" />"/>
                            </div>
                            
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">Related Insurer</label>
                                <s:select name="relatedInsurerId"
                                          list="RelatedInsurers"
                                          listKey="id"
                                          listValue="name"
                                          headerKey="-1"
                                          headerValue="--None--">
                                </s:select>
                            </div>
                             
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">ECD % Increase Trigger Point <span class="mandatory">*</span></label>
                                <input type="text" class="chox-ttxt" id="CCDEcdIncreaseTrigger" name="ecdIncreaseTriggerPercentage" value="<s:property value="ecdIncreaseTriggerPercentage" />"/>
                            </div>
                            
                            <div class="chox-form-item">
                                    <label class="chox-form-std-label">Enable CHO Discounts</label>
                                    <s:checkbox name="insurerDiscountEnable" value="insurerDiscountEnable" onclick="doToggleInsurerDiscount()" />
                                </div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Enable IP Whitelist</label>
                                    <s:checkbox name="enableIPWhitelist" value="enableIPWhitelist" onclick="doPageLoadCheck()" />
                            </div>
                            
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">Disable Private Notes</label>
                                <s:checkbox name="disablePrivateNotes" value="disablePrivateNotes" onclick="doToggleInsurerDiscount()" />
                            </div>
                            <div class="chox-form-item">
                                <label class="chox-form-std-label1">Restrict Export Functions For User Manager roles?</label>
                                <s:checkbox name="restrictExport" value="restrictExport" />
                            </div>
                         </fieldset>
                        
                        <div class="chox-form-button">
                            <input type="submit" value='Save Changes'/>
                            <input type="button" value='Cancel' class="cancel" onclick="return doInsurerCancelBack();" />
                        </div>
                        <div id="CDmessageBox" class="action-error-msg"><s:property value="actionError" /></div>
                        <div class="chox-form-submit-result"><s:property value="actionResult" /></div>
                        <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>-->
                    </div>
                </form>
            </div>
        </div>
                    
        <div id="insurerAliasPanelTab" class="x-hide-display"></div>
        <div id="insurerWorkgroupPanelTab" class="x-hide-display"></div>
        <div id="insurerCreditHirePanelTab" class="x-hide-display"></div>
        <div id="insurerBrePanelTab" class="x-hide-display"></div>
        <div id="insurerBreMappingPanelTab" class="x-hide-display"></div>
        <div id="insurerVehicleClassCeilingTab" class="x-hide-display"></div>
        <div id="insurerInteligentNoteTab" class="x-hide-display"></div>
        <div id="insurerAutoRoutingTab" class="x-hide-display"></div>
        <div id="InsurerDiscountsTab" class="x-hide-display"></div>
        <div id="IPWhitelistConfigTab" class="x-hide-display"></div>
        <div id="isnurerReasonOfRejectionTab" class="x-hide-display"></div>
    </div>
</div>