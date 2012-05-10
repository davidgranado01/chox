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



    Ext.onReady(function(){

    	 Ext.override(Ext.form.ComboBox, {
             setValue : function(v){
                 //begin patch
                 // Store not loaded yet? Set value when it *is* loaded.
                 // Defer the setValue call until after the next load.
                 if (this.store.getCount() == 0) {
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

        new Ext.ToolTip({ target: 'help-claimLocked', html: '"Enable claim locked" will force FNOL, COM, and CH only allowed to edit the claims belong to them only'});

        
//        if(insurerIsWorkgroupEnabled) {

            wgrpJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'text'},
                    {name:'value'}
                ]
            });

            workgroupStore = new Ext.data.Store({
                proxy : new Ext.data.HttpProxy
                ({url : "<%= request.getContextPath()%>/prv/p/WorkgroupDropDownActionByInsurer2.action", method:'GET', params : {"orgId":'<s:property value="objectId"/>'}}),
                reader: wgrpJsonReader
            });

            workgroupCombo = new Ext.form.ComboBox({
                store: workgroupStore,
                width: 200,
                renderTo: 'workgroupComboDiv1',
                valueField: 'text',
                id: 'workgroupComboId',
                hiddenName: 'workgroupIdField',
//                 value:'',
                displayField:'value',
                typeAhead: true,
                mode: 'local',
                editable:false,
                triggerAction: 'all',
                emptyText: '<s:property value="workgroupIdFieldName"/>',
                forceSelection: true,
                listWidth: 200,
                selectOnFocus: true,
                forceSelection : true,
                listeners: {
                    select:function() {
                        if(this.getRawValue() == "") {
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
//        }
//        if(insurerIsClaimOwnershipEnabled) {
            
        claimOwnerReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'id'},
                {name:'name'}
            ]
        });

        claimOwnerStore = new Ext.data.Store({
            proxy : new Ext.data.HttpProxy
            ({url : "<%= request.getContextPath()%>/prv/p/SearchClaimHandlerRoleUserDropDownAction.action", method:'GET', params : {"workgroupId":workgroupId, "insurerId":'<s:property value="objectId"/>'}}),
            reader : claimOwnerReader
        });

        claimOwnerCombo = new Ext.form.ComboBox({
            store: claimOwnerStore,
            width: 200,
            renderTo: 'claimOwnerComboDiv1',
            valueField: 'id',
            id: 'claimOwnerComboId',
            hiddenName: 'claimOwnerIdField',
//             value:'',
            displayField:'name',
            typeAhead: true,
            mode: 'local',
            listWidth: 200,
            forceSelection: true,
            triggerAction: 'all',
            emptyText: '<s:property value="claimOwnerIdFieldName"/>',
            forceSelection : true,
            listeners: {
                select: function () {
                    if(this.getRawValue() == "") {
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
       
		$("#formUpdateInsurerDetail").submit(function(){
			if($("[name='claimOwnerIdField']").val() == ""){
				$("[name='claimOwnerIdField']").val(-1)
				}
			if($("[name='workgroupIdField']").val() == ""){
				$("[name='workgroupIdField']").val(-1)
				}
		});
		

        // CHECK PROCESS MODE
        isNew = isTrue($("#isNew").val());
        
        if(<s:property value="insurerDiscountEnable"/> && !isNew){
            disableDiscountTab = false;
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
                minimumPasswordLength:{ required:true, number:true, min:6, max:32 }
//                workgroupIdField:{comboSelection:workgroupId },
//                claimOwnerIdField:{claimOwnerSelection: claimOwnerId}
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
                scsAgreedBenefitShareValue:{ number:"'SCS Agreed Benefit Share' must be numeric", min:"'SCS Agreed Benefit Share' cannot be less than zero", max:"'SCS Agreed Benefit Share' cannot be higher than 100%" },
                fixedTransactionalFeeValue:{ number:"'Fixed Transactional Fee' must be numeric", min:"'Fixed Transactional Fee' cannot be less than zero" },
                forcePasswordChange:{ required:"You must supply a value for 'Password Expiry Period'", number:"'Password Expiry Period' must be numeric", min:"'Password Expiry Period' cannot be less than zero" },
                uniquePasswordHistory:{ required:"You must supply a value for 'Number Of Unique Passwords'", number:"'Number Of Unique Passwords", min:"'Number Of Unique Passwords' cannot be less than one", max:"'Number Of Unique Passwords' cannot be larger than 15" },
                minimumPasswordLength:{ required:"You must supply a value for 'Minimum Password Length'", number:"'Minimum Password Length", min:"'Minimum Password Length' cannot be less than 6", max:"'Minimum Password Length' cannot be larger than 32" }
//                workgroupIdField: {comboSelection:"You must supply a value for 'Workgroup'"},
//                claimOwnerIdField: {claimOwnerSelection:"You must supply a value for 'Claim Owner'"}
            }
        });

        
        
        ui.ajaxForm($("form#formUpdateInsurerDetail"), doSubmitInsurerSucceed);

        //ui.ajaxForm($("form#formUpdateInsurerDetail"), doInsurerSaveChanges);

        getInsurerAdminTabIndex();
        doTpiEnableCheck();


        if(!autoRoutingPolicyNumberEnabled && !autoRoutingPrice){

            insAdminTabs = new Ext.TabPanel({
                renderTo: 'mainPanel',
                height:615,
                width:775,
                enableTabScroll : true,
//                id:"tab",
                border:true,
                loadMask:false,
                activeTab: insDetailAdminTabIndex,
                items:[
                    {contentEl:'insurerDetailPanelTab', id:"insurerDetailPanelTabId", title:'Details', tabTip:'Insurer Details',listeners: {activate: insHandleActivate}},
                    {contentEl:'insurerAliasPanelTab', id:"insurerAliasPanelTabId", activate:true, title:'Alias', tabTip:'Insurer Alias', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerAliasPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerWorkgroupPanelTab', id:"insurerWorkgroupPanelTabId", title:'Workgroup', tabTip:'Insurer Workgroup', disabled:(isNew || !insurerIsWorkgroupEnabled), listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerWorkgroupPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerCreditHirePanelTab', id:"insurerCreditHirePanelTabId", title:'Credit Hire Mapping', tabTip:'Insurer Credit Hire Mapping', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerChorganisationMappingPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerBrePanelTab', id:"insurerBrePanelTabId", title:'BRE Band', tabTip:'Insurer BRE Band', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerBreBandPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerBreMappingPanelTab', id:"insurerBreMappingPanelTabId", title:'BRE Band Mapping', tabTip:'BRE Band Mapping', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerBreBandChorganisationMapping.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerVehicleClassCeilingTab', id:"insurerVehicleClassCeilingTabId", title:'Vehicle Class Ceilings', tabTip:'Insurer Vehicle Class Ceilings', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerVehicleClassCeilingPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerAutoRoutingTab', id:"insurerAutoRoutingTabId", title:'Automatic Routing', tabTip:'Insurer Automatic Routing', disabled:true, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerAutomaticRoutingPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'InsurerDiscountsTab', id:"InsurerDiscountsTabId", title:'Discounts', tabTip:'Insurer Discounts', disabled: disableDiscountTab, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerDiscountPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}}


                ]
            });

        }else{

            insAdminTabs = new Ext.TabPanel({
                renderTo: 'mainPanel',
                height:615,
                width:775,
                enableTabScroll : true,
//                id:"tabId",
                border:true,
                loadMask:false,
                activeTab: insDetailAdminTabIndex,
                items:[
                    {contentEl:'insurerDetailPanelTab', id:"insurerDetailPanelTabId", title:'Details', tabTip:'Insurer Details',listeners: {activate: insHandleActivate}},
                    {contentEl:'insurerAliasPanelTab', id:"insurerAliasPanelTabId", activate:true, title:'Alias', tabTip:'Insurer Alias', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerAliasPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerWorkgroupPanelTab', id:"insurerWorkgroupPanelTabId", title:'Workgroup', tabTip:'Insurer Workgroup', disabled:(isNew || !insurerIsWorkgroupEnabled), listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerWorkgroupPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerCreditHirePanelTab', id:"insurerCreditHirePanelTabId", title:'Credit Hire Mapping', tabTip:'Insurer Credit Hire Mapping', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerChorganisationMappingPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerBrePanelTab', id:"insurerBrePanelTabId", title:'BRE Band', tabTip:'Insurer BRE Band', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerBreBandPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerBreMappingPanelTab', id:"insurerBreMappingPanelTabId", title:'BRE Band Mapping', tabTip:'BRE Band Mapping', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerBreBandChorganisationMapping.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerVehicleClassCeilingTab', id:"insurerVehicleClassCeilingTabId", title:'Vehicle Class Ceilings', tabTip:'Insurer Vehicle Class Ceilings', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerVehicleClassCeilingPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerAutoRoutingTab', id:"insurerAutoRoutingTabId", title:'Automatic Routing', tabTip:'Insurer Automatic Routing', disabled:(isNew || !insurerIsWorkgroupEnabled), listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerAutomaticRoutingPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'InsurerDiscountsTab', id:"InsurerDiscountsTabId", title:'Discounts', tabTip:'Insurer Discounts', disabled: disableDiscountTab, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerDiscountPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}}
                
                
                ]
            });

        }

        var isFixedTransactionalFee = <s:property value="fixedTransactionalFee"/>;
        

        doPageLoadCheck();

        if (isFixedTransactionalFee) {
            //            console.log("Hiding Agreed Benefit stuff");
            $("#fixedTransactionalFeeOpt").val("true");
            $("#CCDFixedTransactionalFeeValue").show();
            $("#CCDScsAgreedBenefitShareValue").hide();
            $("#CCDAhoAgreedBenefitValueDiv").hide();
        } else {
            $("#fixedTransactionalFeeOpt").val("false");
            //            console.log("Hiding Fixed Transaction stuff");
            $("#CCDFixedTransactionalFeeValue").hide();
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

    });

    function getInsurerAdminTabIndex(){
        if($("#tabIndex").val()!=null && $("#tabIndex").val()!=''){
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
        var url = "<%= request.getContextPath()%>/prv/p/loadAdminPanel.action";
        var param = {"adminPanelName":"ChoxInsurerMgmtPanel"};
        ajax.loadHtml2(url,param,function(data){
            $(target).html(data);
        });
    }
    function doToggleInsurerDiscount(){
        
        if($('form#formUpdateInsurerDetail input[name="insurerDiscountEnable"]:checked').val()){
            disableDiscountTab = false;
//            insAdminTabs.getItem('InsurerDiscountsTabId').setDisabled(false);
        }else{
            disableDiscountTab = true;
//            insAdminTabs.getItem('InsurerDiscountsTabId').setDisabled(true);
        }
    }




    function doPageLoadCheck(){
        var claimWorkgroupEnable = doWorkgroupCheck();
        var claimOwnershipEnable = doOwnershipCheck();
        var supervisorEscalation = doSupervisorEscalationCheck();

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
            $("#tpiWorkgroupId").slideDown();
        }else{
            $("#AutomaticClaimRoutingHolder").hide();
            $("#tpiWorkgroupId").hide();
            $("select#autoRoutingEnableDropDownId").val("");
        }
        return claimWorkgroupEnable;
    }

    function doTpiEnableCheck(){
        var tpiEnableEnable = false;
        if($('form#formUpdateInsurerDetail input[name="thirdPartyInterventionActivated"]:checked').val()){
            tpiEnableEnable = true;
            //            $("#tpiWorkgroupId").slideDown();
            //            $("#TpiClaimOwnerId").slideDown();
            //            $("#tpiExclusionRegexId").slideDown();
            $("#tpiIdentifierId").slideDown();

        }else{
            //            $("#tpiWorkgroupId").hide();
            //            $("#TpiClaimOwnerId").hide();
            //            $("#tpiExclusionRegexId").hide();
            $("#tpiIdentifierId").hide();

            // $("select#autoRoutingEnableDropDownId").val("");
        }
        return tpiEnableEnable;
    }



    function doOwnershipCheck(){
        var claimOwnershipEnable = false;
        if($('form#formUpdateInsurerDetail input[name="claimOwnershipEnable"]:checked').val()){
            claimOwnershipEnable = true;
            $("#TpiClaimOwnerId").slideDown();
        }else{
            $("#TpiClaimOwnerId").hide();
        }
        return claimOwnershipEnable;
    }

    function chargeMethodSelected(fixedTransactionalFee) {
        if (fixedTransactionalFee === 'true') {
            //            console.log("fixedTransactionalFee selected.");
            $("#CCDFixedTransactionalFeeValue").show();
            $("#CCDScsAgreedBenefitShareValue").hide();
            $("#CCDAhoAgreedBenefitValueDiv").hide();
        } else if (fixedTransactionalFee === 'false') {
            //           console.log("AgreedBenefitShare selected.");
            $("#CCDFixedTransactionalFeeValue").hide();
            $("#CCDScsAgreedBenefitShareValue").show();
            $("#CCDAhoAgreedBenefitValueDiv").show();
        }
        //        else {
        //            console.log("unknown chatge method selected: " + fixedTransactionalFee);
        //        }

    }
    function doSubmitInsurerSucceed(responseText, statusText){

        var response = eval('(' + responseText.trim() + ')');
  
        if(response && response.isValid)
        {
            if(response.resultType && response.resultType == 'New'){

                Ext.Msg.minWidth = 300;
                Ext.Msg.alert('New Insurer Created','A new Insurer has been created.');

                updateInsurerDetailPanel(response.result);
            }else{
                
                updateInsurerDetailPanel('<s:property value="objectId"/>');

                Ext.Msg.minWidth = 300;
                Ext.Msg.alert('Save Changes','Your changes have been saved.');
                
            } // end of if else inner loop
        } else {
            updateInsurerDetailPanel('<s:property value="objectId"/>');
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
        var url = "<%= request.getContextPath()%>/prv/p/updateInsurerDetailPanel.action";
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
    <div id="chox-admin-col-div" style="width:780">
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
                            <label class="chox-form-std-label">
                                <select id="fixedTransactionalFeeOpt" name="fixedTransactionalFee" onchange="javascript:chargeMethodSelected(this.options[this.selectedIndex].value);">
                                    <option value="false">SCS Agreed Benefit Share (%)</option>
                                    <option value="true">Fixed Transactional Fee (£)</option>
                                </select>
                            </label>
                            <input type="text" class="chox-ttxt" id="CCDScsAgreedBenefitShareValue" name="scsAgreedBenefitShareValue" value="<s:property value="scsAgreedBenefitShareValue" />"/>
                            <input type="text" class="chox-ttxt" id="CCDFixedTransactionalFeeValue" name="fixedTransactionalFeeValue" value="<s:property value="fixedTransactionalFeeValue" />"/>
                        </div>

                        <div class="chox-form-item" id="CCDAhoAgreedBenefitValueDiv">
                            <label class="chox-form-std-label">Agreed Benefit Value (£)</label>
                            <input type="text" class="chox-ttxt" id="CCDAhoAgreedBenefitValue" name="choAgreedBenefitValue" value="<s:property value="choAgreedBenefitValue" />"/>
                        </div>

                        <div class="chox-form-item">
                            <label class="chox-form-std-label">
                                Related Insurer</label>
                                <s:select name="relatedInsurerId"
                                          list="RelatedInsurers"
                                          listKey="id"
                                          listValue="name"
                                          headerKey="-1"
                                          headerValue="--None--"></s:select>

                            </div>

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
                        <div class="chox-form-item">
                            <label class="chox-form-std-label">ECD % Increase Trigger Point <span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDEcdIncreaseTrigger" name="ecdIncreaseTriggerPercentage" value="<s:property value="ecdIncreaseTriggerPercentage" />"/>
                        </div>
  
						<table>
                            <tr>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable Claim Upload</label>
                                        <s:checkbox name="uploadEnabled" value="uploadEnabled"/>
                                    </div>
                                </td>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Allow Subscriber Claims</label>
                                        <s:checkbox name="allowSubscriberClaims" value="allowSubscriberClaims"/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable Engineers</label>
                                        <s:checkbox name="engineersEnable" value="engineersEnable" onclick="doPageLoadCheck(this);"/>
                                    </div>
                                </td>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Active</label>
                                        <s:checkbox name="status" value="status" />
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable FNOL</label>
                                        <s:checkbox name="fnolEnable" value="fnolEnable" onclick="doPageLoadCheck(this);"/>
                                    </div></td>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable Workgroup</label>
                                        <s:checkbox name="workgroupEnable" value="workgroupEnable" onclick="doPageLoadCheck(this);"/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable Claim Ownership</label>
                                        <s:checkbox name="claimOwnershipEnable" value="claimOwnershipEnable" onclick="doPageLoadCheck(this);" />
                                    </div></td>
                                <td>
                                    <div class="chox-form-item" id="ClaimLockedHolder">
                                        <label class="chox-form-std-label">Enable Claim Locked</label>
                                        <s:checkbox name="claimLocked" value="claimLocked" /><img id="help-claimLocked" class="help-icon" src="<%= request.getContextPath()%>/images/help.png"/>
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
                                    <div class="chox-form-item" id="AutomaticClaimRoutingHolder">
                                        <label class="chox-form-std-label">Automatic Claim Routing</label>

                                        <select id="autoRoutingEnableDropDownId"name="autoRoutingEnableId" >
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
                                        <label class="chox-form-std-label">Enable Direct Invoice Upload (TPI)</label>
                                        <s:checkbox name="thirdPartyInterventionActivated" value="thirdPartyInterventionActivated" onclick="doTpiEnableCheck(this)"/>
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
                                        <label class="chox-form-std-label">Enable CHO Discounts</label>
                                        <s:checkbox name="insurerDiscountEnable" value="insurerDiscountEnable" onclick="doToggleInsurerDiscount()" />
                                    </div>
                                </td>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable IP Whitelist</label>
                                        <s:checkbox name="enableIPWhitelist" value="enableIPWhitelist" onclick="doPageLoadCheck()" />
                                    </div>
                                </td>
                            </tr>
                        </table>
                        <table>
                            <tr>
                                <td>
                                    <div class="chox-form-item" id="tpiIdentifierId">
                                        <label class="chox-form-std-label1">Invoice Identification String (TPI)</label>
                                        <input type="text" class="chox-ttxt" style="width: 200px; height:20px " id="tpiIdentifierId" name="tpiIdentificationString" value="<s:property value="tpiIdentificationString" />"/>
                                    </div>
                                </td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="chox-form-item" id="tpiWorkgroupId">
                                        <label class="chox-form-std-label1">Default Workgroup for Approved Invoices (TPI & Insurer vs. Insurer)</label>
                                        <div id="workgroupComboDiv1"></div>
                                    </div>
                                </td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="chox-form-item" id="TpiClaimOwnerId">
                                        <label class="chox-form-std-label1">Default Claim Owner for Approved Invoices (TPI & Insurer vs. Insurer)</label>
                                        <div id="claimOwnerComboDiv1"></div>
                                    </div>
                                </td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="chox-form-item" id="tpiExclusionRegexId">
                                        <label class="chox-form-std-label1">Auto-routing Exclusion Regular Expression for Approved Invoices</label>
                                        <input type="text" class="chox-ttxt" style="width: 200px; height:20px " id="tpiExclusionId"  name="tpiRegexExpression" value="<s:property value="tpiRegexExpression" />"/>
                                    </div>
                                </td>
                                <td></td>
                            </tr>
                        </table>
                        <div class="chox-form-button">
                            <input type="submit" value='Save Changes'/>
                            <input type="button" value='Cancel' class="cancel" onclick="return doInsurerCancelBack();" />
                        </div>
                        <div id="CDmessageBox" class="action-error-msg"><s:property value="actionError" /></div>
                        <div class="chox-form-submit-result"><s:property value="actionResult" /></div>
                        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
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
        <div id="insurerAutoRoutingTab" class="x-hide-display"></div>
        <div id="InsurerDiscountsTab" class="x-hide-display"></div>
    </div>
</div>