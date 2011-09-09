<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var adminTabIndex = 0;
    var adminTabs;
    var isNew = true;
    var insurerIsWorkgroupEnabled = <s:property value="insurerIsWorkgroupEnabled" />;
    var policyNumber = <s:property value="autoRoutingEnable"/>;
    var vehicleClassPrice = <s:property value="autoRoutingEnablePrice"/>;
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

       

        new Ext.ToolTip({ target: 'help-claimLocked', html: '"Enable claim locked" will force FNOL, COM, and CH only allowed to edit the claims belong to them only'});


        if(insurerIsWorkgroupEnabled) {

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
                value:'<s:property value="workgroupIdFieldName"/>',
                displayField:'value',
                typeAhead: true,
                mode: 'local',
                editable:false,
                triggerAction: 'all',
                emptyText: '--- Please Select ---',
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
        }

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
            value:'<s:property value="claimOwnerIdFieldName"/>',
            displayField:'name',
            typeAhead: true,
            mode: 'local',
            listWidth: 200,
            forceSelection: true,
            triggerAction: 'all',
            emptyText: '--- Please Select ---',
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
        


        // CHECK PROCESS MODE
        isNew = isTrue($("#isNew").val());

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
                scsAgreedBenefitShareValue:{ required:false, number:true, min:0, max:100 },
                fixedTransactionalFeeValue:{ required:false, number:true, min:0 },
                forcePasswordChange:{ required:true, number:true, min:0 },
                uniquePasswordHistory:{ required:true, number:true, min:0, max:15 }
                //                workgroupIdField:{comboSelection:workgroupId },
                //                claimOwnerIdField:{claimOwnerSelection: claimOwnerId}
            },
            messages:
                {
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
                scsAgreedBenefitShareValue:{ number:"'SCS Agreed Benefit Share' must be numeric", min:"'SCS Agreed Benefit Share' cannot be less than zero", max:"'SCS Agreed Benefit Share' cannot be higher than 100%" },
                fixedTransactionalFeeValue:{ number:"'Fixed Transactional Fee' must be numeric", min:"'Fixed Transactional Fee' cannot be less than zero" },
                forcePasswordChange:{ required:"You must supply a value for 'Password Expiry Period'", number:"'Password Expiry Period' must be numeric", min:"'Password Expiry Period' cannot be less than zero" },
                uniquePasswordHistory:{ required:"You must supply a value for 'Forced Unique Password History'", number:"'Forced Unique Password History' must be numeric", min:"'Forced Unique Password History' cannot be less than zero", max:"'Forced Unique Password History' cannot be larger than 15" }
                //                workgroupIdField: {comboSelection:"You must supply a value for 'Workgroup'"},
                //                claimOwnerIdField: {claimOwnerSelection:"You must supply a value for 'Claim Owner'"}
            }
        });

        
        
        ui.ajaxForm($("form#formUpdateInsurerDetail"), doSubmitInsurerSucceed);

        //ui.ajaxForm($("form#formUpdateInsurerDetail"), doInsurerSaveChanges);

        getInsurerAdminTabIndex();
        doTpiEnableCheck();


        if(!policyNumber && !vehicleClassPrice){

            adminTabs = new Ext.TabPanel({
                renderTo: 'mainPanel',
                height:640,
                width:785,
                enableTabScroll : true,
                id:"tab",
                border:true,
                loadMask:false,
                activeTab: adminTabIndex,
                items:[
                    {contentEl:'insurerDetailPanelTab', id:"insurerDetailPanelTabId", title:'Details', tabTip:'Insurer Details',listeners: {activate: insHandleActivate}},
                    {contentEl:'insurerAliasPanelTab', id:"insurerAliasPanelTabId", activate:true, title:'Alias', tabTip:'Insurer Alias', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerAliasPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerWorkgroupPanelTab', id:"insurerWorkgroupPanelTabId", title:'Workgroup', tabTip:'Insurer Workgroup', disabled:(isNew || !insurerIsWorkgroupEnabled), listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerWorkgroupPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerCreditHirePanelTab', id:"insurerCreditHirePanelTabId", title:'Credit Hire Mapping', tabTip:'Insurer Credit Hire Mapping', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerChorganisationMappingPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerBrePanelTab', id:"insurerBrePanelTabId", title:'BRE Band', tabTip:'Insurer BRE Band', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerBreBandPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerBreMappingPanelTab', id:"insurerBreMappingPanelTabId", title:'BRE Band Mapping', tabTip:'BRE Band Mapping', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerBreBandChorganisationMapping.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerVehicleClassCeilingTab', id:"insurerVehicleClassCeilingTabId", title:'Vehicle Class Ceilings', tabTip:'Insurer Vehicle Class Ceilings', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerVehicleClassCeilingPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerAutoRoutingTab', id:"insurerAutoRoutingTabId", title:'Automatic Routing', tabTip:'Insurer Automatic Routing', disabled:true, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerAutomaticRoutingPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'InsurerDiscountsTab', id:"InsurerDiscountsTabId", title:'Discounts', tabTip:'Insurer Discounts', disabled: isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerDiscountPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}}


                ]
            });

        }else{

            adminTabs = new Ext.TabPanel({
                renderTo: 'mainPanel',
                height:640,
                width:785,
                enableTabScroll : true,
                id:"tabId",
                border:true,
                loadMask:false,
                activeTab: adminTabIndex,
                items:[
                    {contentEl:'insurerDetailPanelTab', id:"insurerDetailPanelTabId", title:'Details', tabTip:'Insurer Details',listeners: {activate: insHandleActivate}},
                    {contentEl:'insurerAliasPanelTab', id:"insurerAliasPanelTabId", activate:true, title:'Alias', tabTip:'Insurer Alias', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerAliasPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerWorkgroupPanelTab', id:"insurerWorkgroupPanelTabId", title:'Workgroup', tabTip:'Insurer Workgroup', disabled:(isNew || !insurerIsWorkgroupEnabled), listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerWorkgroupPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerCreditHirePanelTab', id:"insurerCreditHirePanelTabId", title:'Credit Hire Mapping', tabTip:'Insurer Credit Hire Mapping', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerChorganisationMappingPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerBrePanelTab', id:"insurerBrePanelTabId", title:'BRE Band', tabTip:'Insurer BRE Band', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerBreBandPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerBreMappingPanelTab', id:"insurerBreMappingPanelTabId", title:'BRE Band Mapping', tabTip:'BRE Band Mapping', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerBreBandChorganisationMapping.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerVehicleClassCeilingTab', id:"insurerVehicleClassCeilingTabId", title:'Vehicle Class Ceilings', tabTip:'Insurer Vehicle Class Ceilings', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerVehicleClassCeilingPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'insurerAutoRoutingTab', id:"insurerAutoRoutingTabId", title:'Automatic Routing', tabTip:'Insurer Automatic Routing', disabled:(isNew || !insurerIsWorkgroupEnabled), listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerAutomaticRoutingPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
                    {contentEl:'InsurerDiscountsTab', id:"InsurerDiscountsTabId", title:'Discounts', tabTip:'Insurer Discounts', disabled: isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getInsurerDiscountPage.action?insurerId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}}
                
                
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

       

        if (policyNumber) {

            $("select#autoRoutingEnableDropDownId").val("autoRoutingEnable");
        } else if(vehicleClassPrice){

            $("select#autoRoutingEnableDropDownId").val("autoRoutingEnablePrice");

        }else{

            $("select#autoRoutingEnableDropDownId").val("");
            
        }

    });

    function getInsurerAdminTabIndex(){
        if($("#tabIndex").val()!=null && $("#tabIndex").val()!=''){
            adminTabIndex = $("#tabIndex").val();
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
        adminTabIndex = 0;
        if(adminTabs){ adminTabIndex = adminTabs.items.indexOf(adminTabs.getActiveTab()); }
    }

    function doInsurerCancelBack(){
        var target = "#admin_param_panel";
        var url = "<%= request.getContextPath()%>/prv/p/loadAdminPanel.action";
        var param = {"adminPanelName":"ChoxInsurerMgmtPanel"};
        ajax.loadHtml2(url,param,function(data){
            $(target).html(data);
        });
    }
    function doInsurerSaveChanges(){

            
    
    }




    function doPageLoadCheck(){
        var claimWorkgroupEnable = doWorkgroupCheck();
        var claimOwnershipEnable = doOwnershipCheck();

        if(!claimWorkgroupEnable && !claimOwnershipEnable){
            $("#ClaimLockedHolder").slideUp();
            $('form#formUpdateInsurerDetail input[name="claimLocked"]').attr('checked', false);
        }else{
            $("#ClaimLockedHolder").slideDown();
        }
    }

    function doWorkgroupCheck(){
        var claimWorkgroupEnable = false;
        if($('form#formUpdateInsurerDetail input[name="workgroupEnable"]:checked').val()){
            claimWorkgroupEnable = true;
            $("#AutomaticClaimRoutingHolder").slideDown();
        }else{
            $("#AutomaticClaimRoutingHolder").slideUp();
            $("select#autoRoutingEnableDropDownId").val("");
        }
        return claimWorkgroupEnable;
    }

    function doTpiEnableCheck(){
        var tpiEnableEnable = false;
        if($('form#formUpdateInsurerDetail input[name="thirdPartyInterventionActivated"]:checked').val()){
            tpiEnableEnable = true;
            $("#tpiWorkgroupId").slideDown();
            $("#TpiClaimOwnerId").slideDown();
            $("#tpiExclusionRegexId").slideDown();
            $("#tpiIdentifierId").slideDown();

        }else{
            $("#tpiWorkgroupId").hide();
            $("#TpiClaimOwnerId").hide();
            $("#tpiExclusionRegexId").hide();
            $("#tpiIdentifierId").hide();

            // $("select#autoRoutingEnableDropDownId").val("");
        }
        return tpiEnableEnable;
    }



    function doOwnershipCheck(){
        var claimOwnershipEnable = false;
        if($('form#formUpdateInsurerDetail input[name="claimOwnershipEnable"]:checked').val()){
            claimOwnershipEnable = true;
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
        //var outputDiv = $('div.chox-form-submit-result');
  
        if(response && response.isValid)
        {

            if(response.resultType && response.resultType == 'New'){


                //Ext.getCom('insurerAutoRoutingTab').disable();
                alert("New Insurer has been created");

                var newObjectId = parseInt(response.result);
                var target = "#admin_param_panel";
                var url = "<%= request.getContextPath()%>/prv/p/updateInsurerDetailPanel.action";
                var param = {"objectId":newObjectId};

                ajax.loadHtml2(url,param, function(data){
                    $(target).html(data);
                });
            }else{
                

                var dropDownVal = $('#autoRoutingEnableDropDownId').val();

                

                var objectId = '<s:property value="objectId"/>';
                var target = "#admin_param_panel";
                var url = "<%= request.getContextPath()%>/prv/p/updateInsurerDetailPanel.action";
                var param = {"objectId":objectId};

                ajax.loadHtml2(url,param, function(data){
                    $(target).html(data);
                });
                //outputDiv.append("<p>Your changes have been saved.</p>");

                Ext.Msg.minWidth = 300;
                Ext.Msg.alert('SaveChanges','Your changes have been saved.');
                
            }
            

        }
    }

</script>

<input name="tabIndex" id="tabIndex" type="hidden" value="<s:property value="tabIndex" />"/>
<input name="isNew" id="isNew" type="hidden" value="<s:property value="isNew" />"/>
<input name="insurerIsWorkgroupEnabled" id="insurerIsWorkgroupEnabled" type="hidden" value="<s:property value="insurerIsWorkgroupEnabled" />"/>

<div id="chox-admin-holder" >
    <div id="chox-admin-col-div" style ="width:780">
        <div id="header-title">
            <label>Insurer Name:
                <s:if test="!isNew"><s:property value="name" /> </s:if><s:else>Create New Insurer</s:else>
            </label>
        </div>
        <div id="mainPanel"></div>
        <div id="insurerDetailPanelTab" class="x-hide-display">
            <div class="sub-admin-tab-css">
                <form id="formUpdateInsurerDetail" name="formUpdateInsurerDetail" action="<%= request.getContextPath()%>/prv/p/updateInsurerDetail.action" onsubmit="return true;" class="XXentity-form" method="POST">
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
                            <label class="chox-form-std-label">Password Expiry Period (in Days)</label>
                            <input type="text" class="chox-ttxt" id="CCDForcePasswordChange" name="forcePasswordChange" value="<s:property value="forcePasswordChange" />"/>
                        </div>

                        <div class="chox-form-item" id="CCDUniquePasswordHistoryDiv">
                            <label class="chox-form-std-label">Forced Unique Password History</label>
                            <input type="text" class="chox-ttxt" id="CCDUniquePasswordHistory" name="uniquePasswordHistory" value="<s:property value="uniquePasswordHistory" />"/>
                        </div>

                        <table>
                            <tr>
                                <td><div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable Engineers</label>
                                        <s:checkbox name="engineersEnable" value="engineersEnable" onchange="javascript:doPageLoadCheck();"/>
                                    </div></td>
                                <td><div class="chox-form-item">
                                        <label class="chox-form-std-label">Active</label>
                                        <s:checkbox name="status" value="status" />
                                    </div></td>
                            </tr>
                            <tr>
                                <td><div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable FNOL</label>
                                        <s:checkbox name="fnolEnable" value="fnolEnable" onchange="javascript:doPageLoadCheck();"/>
                                    </div></td>
                                <td><div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable Workgroup</label>
                                        <s:checkbox name="workgroupEnable" value="workgroupEnable" onchange="javascript:doPageLoadCheck();"/>
                                    </div></td>
                            </tr>
                            <tr>
                                <td><div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable Claim Ownership</label>
                                        <s:checkbox name="claimOwnershipEnable" value="claimOwnershipEnable" onchange="javascript:doPageLoadCheck();" />
                                    </div></td>
                                <td><div class="chox-form-item" id="ClaimLockedHolder">
                                        <label class="chox-form-std-label">Enable Claim Locked</label>
                                        <s:checkbox name="claimLocked" value="claimLocked" /><img id="help-claimLocked" class="help-icon" src="<%= request.getContextPath()%>/images/help.png"/>
                                    </div></td>
                            </tr>
                            <tr>
                                <td><div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable Online Support Form</label>
                                        <s:checkbox name="onlineSupportEnable" value="onlineSupportEnable" onchange="javascript:doPageLoadCheck();" />
                                    </div></td>
                                <td><div class="chox-form-item" id="AutomaticClaimRoutingHolder">
                                        <label class="chox-form-std-label">Automatic Claim Routing</label>

                                        <select id="autoRoutingEnableDropDownId"name="autoRoutingEnableId" >
                                            <option value="">--Disabled--</option>
                                            <option value="autoRoutingEnable">By Policy Number</option>
                                            <option value="autoRoutingEnablePrice">By Customer Vehicle Class Price</option>
                                        </select>

                                    </div></td>
                            </tr>
                            <tr>
                                <td><div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable Task Management</label>
                                        <s:checkbox name="taskManagementEnable" value="taskManagementEnable" onchange="javascript:doPageLoadCheck();" />
                                    </div></td>
                                <td>

                                </td>
                            </tr>
                        </table>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label">Enable Direct Invoice Upload</label>
                            <s:checkbox name="thirdPartyInterventionActivated" value="thirdPartyInterventionActivated" onclick="doTpiEnableCheck(this);"/>
                        </div>         
                        <s:if test="insurerIsWorkgroupEnabled">
                            <div class="chox-form-item" id="tpiWorkgroupId">
                                <label class="chox-form-std-label">Default Workgroup for Approved Invoices</label>
                                <div id="workgroupComboDiv1"></div>
                            </div>
                        </s:if>
                        <div class="chox-form-item" id="TpiClaimOwnerId">
                            <label class="chox-form-std-label">Default Claim Owner for Approved Invoices</label>
                            <div id="claimOwnerComboDiv1"></div>
                        </div>
                        <div class="chox-form-item" id="tpiExclusionRegexId">
                            <label class="chox-form-std-label">Auto-routing Exclusion Regular Expression</label>
                            <input type="text" class="chox-ttxt" id="tpiExclusionId"  name="tpiRegexExpression" value="<s:property value="tpiRegexExpression" />"/>
                        </div>
                        <div class="chox-form-item" id="tpiIdentifierId">
                            <label class="chox-form-std-label">Invoice (TPI) Identification String</label>
                            <input type="text" class="chox-ttxt" id="tpiIdentifierId" name="tpiIdentificationString" value="<s:property value="tpiIdentificationString" />"/>
                        </div>
                        
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