<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var choAdminTabs;
    var choAdminTabIndex=0;
    var isNew = true;
    var insurerUploadOnly = false;
    var disableIPWhitelistTab = true;
    // var isNew = true;

    Ext.onReady(function(){

        // isNew = isTrue($("#isNew").val());
        $.validator.addMethod("regex", function(value, element, regexp) {
            var check = false;
            var re = new RegExp(regexp);
            return this.optional(element) || re.test(value);
        }, "Please check your input.");

        var form = $("#formUpdateChorganisationDetail");
        
        isNew = isTrue($("#isNew").val());
        insurerUploadOnly = <s:property value="insurerUploadOnly" />;
        
        if(<s:property value="enableIPWhitelist"/> && !isNew){
            disableIPWhitelistTab = false;
        }
        
        if(document.getElementById('insurerUploadOnlyCheckBoxId').checked){
             markFieldReadOnly();            
        }
          
        form.validate(
        {
            errorLabelContainer: "#CDmessageBox",
            rules: {
                name:{required:true },
                vatNo:{required:true, number:true },
                companyNo:{ required:true, number:true },
                address1:{ required:true },
                address2:{ required:true },
                address4:{ required:true, regex: "^\\s*[a-zA-Z.,\\s]+\\s*$" },
                address5:{ required:true, regex: "^\\s*[a-zA-Z,.\\s]+\\s*$" },
                postcode:{ required:true },
                phone:{ regex:"^(\\(?\\+?[0-9]*\\)?)?[0-9_\\- \\(\\)]*$"},
                fixedTransactionalFeeValue:{ required:false, number:true, min:0 },
                dailyRateChargeLimit:{number:true, min:1 },
                forcePasswordChange:{ required:true, number:true, min:0 },
                uniquePasswordHistory:{ required:true, number:true, min:1, max:15 },
                minimumPasswordLength:{ required:true, number:true, min:6, max:32 },
                maxLoginAttempts:{ required:true, number:true, min:0 },
                blockTime:{ required:true, number:true, min:0 },
                blockedMessage:{ required:true}
       },
            messages: {
                name:{required:"You must supply a value for 'Name'"},
                vatNo:{required:"You must supply a value for 'VAT No.'", number:"'VAT No.' must be number"},
                companyNo:{ required:"You must supply a value for 'Company No.'", number:"'Company No' must be number"},
                address1:{ required:"You must supply a value for 'Address 1'"},
                address2:{ required:"You must supply a value for 'Address 2'"},
                address4:{ required:"You must supply a value for 'County'", regex:"'County' must be letters only"},
                address5:{ required:"You must supply a value for 'Country'", regex:"'Country' must be letters only" },
                postcode:{ required:"You must supply a value for 'Postcode'" },
                phone:{ regex:"'Telephone Number' must be numeric" },
                fixedTransactionalFeeValue:{ number:"'Fixed Transactional Fee' must be numeric", min:"'Fixed Transactional Fee' cannot be less than zero" },
                dailyRateChargeLimit:{ number:"'Maximum Adjustment Value' must be numeric", min:"'Maximum Adjustment Value' must be greater than zero" },
                forcePasswordChange:{ required:"You must supply a value for 'Password Expiry Period'", number:"'Password Expiry Period' must be numeric", min:"'Password Expiry Period' cannot be less than zero" },
                uniquePasswordHistory:{ required:"You must supply a value for 'Number Of Unique Passwords'", number:"'Number Of Unique Passwords' must be numeric", min:"'Number Of Unique Passwords' cannot be less than one", max:"'Number Of Unique Passwords' cannot be larger than 15" },
                minimumPasswordLength:{ required:"You must supply a value for 'Minimum Password Length'", number:"'Minimum Password Length' must be numeric", min:"'Minimum Password Length' cannot be less than 6", max:"'Minimum Password Length' cannot be larger than 32" },
                maxLoginAttempts:{ required:"You must supply a value for 'Maximum login attempts'", number:"'Maximum login attempts' must be numeric", min:"'Maximum login attempts' cannot be less than 0"},
                blockTime:{ required:"You must supply a value for 'Account blocked period'", number:"'Account blocked period' must be numeric", min:"'Account blocked period' cannot be less than 0"},
                blockedMessage:{ required: "You must supply an 'Account blocked message'"}
            }
        });

        choAdminTabs = new Ext.TabPanel({
            renderTo: 'choDetailMainPanel',
            height:615,
            width:775,
//            id:"tab",
            border:true,
            loadMask:false,
            activeTab: choAdminTabIndex,
            items:[
                {contentEl:'CHODetailPanelTab', id:"CHODetailPanelTabId", title:'Details', tabTip:'CHO Details',listeners: {activate: choHandleActivate}},
                {contentEl:'CHOAliasPanelTab', id:"CHOAliasPanelTabId", title:'Alias', tabTip:'CHO Alias', disabled: !((!isNew && insurerUploadOnly) ? true : false), listeners: {activate: choHandleActivate}, autoLoad: {url:"p/getChoAliasPage.action?choId="+<s:property value="objectId" />, scripts:true}},
                {contentEl:'IPWhitelistConfigTab', id:"IPWhitelistConfigTabId", title:'IP Whitelist', tabTip:'IP Whitelist Address', disabled: disableIPWhitelistTab, listeners: {activate: choHandleActivate}, autoLoad: {url:"p/getIPWhitelistPage.action?orgId="+<s:property value="objectId" />+"&orgType=3"+"&nonce="+'<%= session.getAttribute("SessionNonce")%>', scripts:true}}
                //{contentEl:'ChoTpiPanelTab', id:"ChoTpiPanelTabId", activate:true, title:'TPI', tabTip:'Third Party Intervention', disabled:isNew, listeners: {activate: choHandleActivate}, autoLoad: {url:"p/getTpiPage.action?objectId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
            ]
        });

    <s:if test="fixedTransactionalFee" >
            $("#fixedTransactionalFeeOpt").val("true");
            $("#FixedTransactionalValueDiv").show();
    </s:if><s:else >
            $("#fixedTransactionalFeeOpt").val("false");
            $("#FixedTransactionalValueDiv").hide();
    </s:else>

    <s:if test="adjustDailyRateCharge" >
            $("#adjustDailyRateChargeOpt").val("true");
            $("#DailyRateChargeLimitDiv").show();
            addValidationRuleDailyRateChargeLimit()
    </s:if><s:else >
            $("#adjustDailyRateChargeOpt").val("false");
            $("#DailyRateChargeLimitDiv").hide();
    </s:else>


            ui.ajaxForm(form, function(responseText, statusText){
                var response = eval('(' + responseText.trim() + ')');
                if(response && response.isValid)
                {
                    if(response.resultType && response.resultType == 'New'){
                        Ext.Msg.minWidth = 300;
                        Ext.Msg.alert('New CHO','A new CHO has been created.');
                        updateCHODetailPanel(parseInt(response.result));
                    }
                    else if('<s:property value="id" />'!=''){
                        Ext.Msg.minWidth = 300;
                        Ext.Msg.alert('Save Changes','Your changes have been saved.');
                        updateCHODetailPanel('<s:property value="id" />');
                    }
                } else {
                    Ext.MessageBox.show({
                        title: '',
                        msg: response.errors,
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }
            });
            
            if($('#CCDName').val() != "")
                $('#nameField').hide();
            
        });
        
        function updateCHODetailPanel(objectId) {
            var target = "#admin_param_panel";
            var url = "<%= request.getContextPath()%>/prv/p/updateChorganisationDetailPanel.action";
            var param = {"objectId":objectId};
            ajax.loadHtml2(url,param, function(data){
                $(target).html(data);
            });
        }

        function choHandleActivate(tab){
            choAdminTabIndex = 0;
            if(choAdminTabs){ choAdminTabIndex = choAdminTabs.items.indexOf(choAdminTabs.getActiveTab()); }
        }

        function doChorganisationCancelBack(){
            var target = "#admin_param_panel";
            var url = "<%= request.getContextPath()%>/prv/p/loadAdminPanel.action";
            var param = {"adminPanelName":"ChoxCreditHireMgmtPanel"};
            ajax.loadHtml2(url,param,function(data){
                $(target).html(data);
            });
        }

        function chargeMethodSelected(fixedTransactionalFee) {
            if (fixedTransactionalFee === 'true') {
                $("#FixedTransactionalValueDiv").show();
            } else if (fixedTransactionalFee === 'false') {
                $("#FixedTransactionalValueDiv").hide();
            }
        }

        function dailyRateMethodSelected(adjustDailyRateCharge) {
            if (adjustDailyRateCharge === 'true') {
                $("#DailyRateChargeLimitDiv").show();
                addValidationRuleDailyRateChargeLimit();
            } else if (adjustDailyRateCharge === 'false') {
                removeValidationRuleDailyRateChargeLimit();
                $("#DailyRateChargeLimitDiv").hide();
            }
        }

        function addValidationRuleDailyRateChargeLimit(){
            $("form#formUpdateChorganisationDetail #CCDDailyRateChargeLimit").rules("add", {
                required: true,
                messages: {required: "You must supply a value for 'Maximum Adjustment Value'"}
            });
        }

        function removeValidationRuleDailyRateChargeLimit(){
            $("form#formUpdateChorganisationDetail #CCDDailyRateChargeLimit").rules("remove", "required");
        }
        
        function doInsurerUploadOnlyValidationSetup() {
       
            var settings = $('form#formUpdateChorganisationDetail').validate().settings;
            
            if(document.getElementById('insurerUploadOnlyCheckBoxId').checked){
                delete settings.rules.companyNo;
                delete settings.rules.vatNo;
                delete settings.rules.address1;
                delete settings.rules.address2;
                delete settings.rules.address4;
                delete settings.rules.address5;
                delete settings.rules.postcode;
                delete settings.rules.uniquePasswordHistory;
                delete settings.rules.minimumPasswordLength;
            } else {
                $('form#formUpdateChorganisationDetail #CCDVatNo' ).rules("add", {required: true});
                $('form#formUpdateChorganisationDetail #CCDCompanyNo' ).rules("add", {required: true});
                $('form#formUpdateChorganisationDetail #CCDAddress1' ).rules("add", {required: true});
                $('form#formUpdateChorganisationDetail #CCDAddress2' ).rules("add", {required: true});
                $('form#formUpdateChorganisationDetail #CCDPostcode' ).rules("add", {required: true});
                $('form#formUpdateChorganisationDetail #CCDAddress4' ).rules("add", {required: true});
                $('form#formUpdateChorganisationDetail #CCDAddress5' ).rules("add", {required: true});
                $('form#formUpdateChorganisationDetail #CCDUniquePasswordHistory' ).rules("add", {required: true});
                $('form#formUpdateChorganisationDetail #CCDMinimumPasswordLength' ).rules("add", {required: true});
                $('form#formUpdateChorganisationDetail #CCDMaximumLoginAttemptsCho' ).rules("add", {required: true});
                $('form#formUpdateChorganisationDetail #CCDBlockTimeCho' ).rules("add", {required: true});
                $('form#formUpdateChorganisationDetail #CCDBlockedMessage' ).rules("add", {required: true});
            }

       }
       
       function markFieldReadOnly(){
           
             $('form#formUpdateChorganisationDetail #CCDVatNo').attr('readonly', true);
             $('form#formUpdateChorganisationDetail #CCDCompanyNo' ).attr('readonly', true);
             $('form#formUpdateChorganisationDetail #CCDAddress1' ).attr('readonly', true);
             $('form#formUpdateChorganisationDetail #CCDAddress2' ).attr('readonly', true);
             $('form#formUpdateChorganisationDetail #CCDAddress3' ).attr('readonly', true);
             $('form#formUpdateChorganisationDetail #CCDPostcode' ).attr('readonly', true);
             $('form#formUpdateChorganisationDetail #CCDAddress4' ).attr('readonly', true);
             $('form#formUpdateChorganisationDetail #CCDAddress5' ).attr('readonly', true);
             $('form#formUpdateChorganisationDetail #CCDPhone' ).attr('readonly', true);
             $('form#formUpdateChorganisationDetail #CCDForcePasswordChange' ).attr('readonly', true);
             $('form#formUpdateChorganisationDetail #CCDUniquePasswordHistory' ).attr('readonly', true);
             $('form#formUpdateChorganisationDetail #CCDMinimumPasswordLength' ).attr('readonly', true);
             $('form#formUpdateChorganisationDetail #CCDMaximumLoginAttemptsCho' ).attr('readonly', true);
             $('form#formUpdateChorganisationDetail #CCDBlockTimeCho' ).attr('readonly', true);
             $('form#formUpdateChorganisationDetail #CCDBlockedMessage' ).attr('readonly', true);
             
             $('form#formUpdateChorganisationDetail #CCDVatNo').css('background','#e4e4e4');
             $('form#formUpdateChorganisationDetail #CCDCompanyNo' ).css('background','#e4e4e4');
             $('form#formUpdateChorganisationDetail #CCDAddress1' ).css('background','#e4e4e4');
             $('form#formUpdateChorganisationDetail #CCDAddress2' ).css('background','#e4e4e4');
             $('form#formUpdateChorganisationDetail #CCDAddress3' ).css('background','#e4e4e4');
             $('form#formUpdateChorganisationDetail #CCDPostcode' ).css('background','#e4e4e4');
             $('form#formUpdateChorganisationDetail #CCDAddress4' ).css('background','#e4e4e4');
             $('form#formUpdateChorganisationDetail #CCDAddress5' ).css('background','#e4e4e4');
             $('form#formUpdateChorganisationDetail #CCDPhone' ).css('background','#e4e4e4');
             $('form#formUpdateChorganisationDetail #CCDForcePasswordChange' ).css('background','#e4e4e4');
             $('form#formUpdateChorganisationDetail #CCDUniquePasswordHistory' ).css('background','#e4e4e4');
             $('form#formUpdateChorganisationDetail #CCDMinimumPasswordLength' ).css('background','#e4e4e4');  
             $('form#formUpdateChorganisationDetail #CCDMaximumLoginAttemptsCho' ).css('background','#e4e4e4');  
             $('form#formUpdateChorganisationDetail #CCDBlockTimeCho' ).css('background','#e4e4e4');  
             $('form#formUpdateChorganisationDetail #CCDBlockedMessage' ).css('background','#e4e4e4');  
       }
       
       function markFieldEditable(){
           
             $('form#formUpdateChorganisationDetail #CCDVatNo').attr('readonly', false);
             $('form#formUpdateChorganisationDetail #CCDCompanyNo' ).attr('readonly', false);
             $('form#formUpdateChorganisationDetail #CCDAddress1' ).attr('readonly', false);
             $('form#formUpdateChorganisationDetail #CCDAddress2' ).attr('readonly', false);
             $('form#formUpdateChorganisationDetail #CCDAddress3' ).attr('readonly', false);
             $('form#formUpdateChorganisationDetail #CCDPostcode' ).attr('readonly', false);
             $('form#formUpdateChorganisationDetail #CCDAddress4' ).attr('readonly', false);
             $('form#formUpdateChorganisationDetail #CCDAddress5' ).attr('readonly', false);
             $('form#formUpdateChorganisationDetail #CCDForcePasswordChange' ).attr('readonly', false);
             $('form#formUpdateChorganisationDetail #CCDUniquePasswordHistory' ).attr('readonly', false);
             $('form#formUpdateChorganisationDetail #CCDMinimumPasswordLength' ).attr('readonly', false);  
             $('form#formUpdateChorganisationDetail #CCDMaximumLoginAttemptsCho' ).attr('readonly', false);
             $('form#formUpdateChorganisationDetail #CCDBlockTimeCho' ).attr('readonly', false);  
             $('form#formUpdateChorganisationDetail #CCDBlockedMessage' ).attr('readonly', false);  
             
             $('form#formUpdateChorganisationDetail #CCDVatNo').css('background','#ffffff');
             $('form#formUpdateChorganisationDetail #CCDCompanyNo' ).css('background','#ffffff');
             $('form#formUpdateChorganisationDetail #CCDAddress1' ).css('background','#ffffff');
             $('form#formUpdateChorganisationDetail #CCDAddress2' ).css('background','#ffffff');
             $('form#formUpdateChorganisationDetail #CCDAddress3' ).css('background','#ffffff');
             $('form#formUpdateChorganisationDetail #CCDPostcode' ).css('background','#ffffff');
             $('form#formUpdateChorganisationDetail #CCDAddress4' ).css('background','#ffffff');
             $('form#formUpdateChorganisationDetail #CCDAddress5' ).css('background','#ffffff');
             $('form#formUpdateChorganisationDetail #CCDPhone' ).css('background','#ffffff');
             $('form#formUpdateChorganisationDetail #CCDForcePasswordChange' ).css('background','#ffffff');
             $('form#formUpdateChorganisationDetail #CCDUniquePasswordHistory' ).css('background','#ffffff');
             $('form#formUpdateChorganisationDetail #CCDMinimumPasswordLength' ).css('background','#ffffff');
             $('form#formUpdateChorganisationDetail #CCDMaximumLoginAttemptsCho' ).css('background','#ffffff');
             $('form#formUpdateChorganisationDetail #CCDBlockTimeCho' ).css('background','#ffffff');
             $('form#formUpdateChorganisationDetail #CCDBlockedMessage' ).css('background','#ffffff');
       }
       
       function setDefaultValueForMandatoryField(){
           
             Ext.get('CCDVatNo').dom.value = '0'; 
             Ext.get('CCDCompanyNo').dom.value = '0';
             Ext.get('CCDAddress1').dom.value = 'xxxxxxx';
             Ext.get('CCDAddress2').dom.value = 'xxxxxxx';
             Ext.get('CCDPostcode').dom.value = 'xxxxxxx';
             Ext.get('CCDAddress4').dom.value = 'xxxxxxx';
             Ext.get('CCDAddress5').dom.value = 'xxxxxxx';
             Ext.get('CCDUniquePasswordHistory').dom.value = '1';
             Ext.get('CCDMinimumPasswordLength').dom.value = '8';
             Ext.get('CCDMaximumLoginAttemptsCho').dom.value = '0';
             Ext.get('CCDBlockTimeCho').dom.value = '0';
             Ext.get('CCDBlockedMessage').dom.value = 'Not Used';
       }
       
       function onInsurerUploadOnlyChecked(){
          if(isNew){
             setDefaultValueForMandatoryField();
          }
          if(document.getElementById('insurerUploadOnlyCheckBoxId').checked){
             markFieldReadOnly();
          }else if(!document.getElementById('insurerUploadOnlyCheckBoxId').checked){
             markFieldEditable();
          }
       }
</script>
<input name="isNew" id="isNew" type="hidden" value="<s:property value="isNew" />"/>
<div id="chox-admin-holder">

    <div id="chox-admin-col-div" style="width:780px">
        <div id="header-title">
            <label>CHO Name:
                <s:if test="!isNew"><s:property value="name" /> </s:if><s:else>Create New CHO</s:else>
            </label>
        </div>
        <div id="choDetailMainPanel"></div>


        <div id="CHODetailPanelTab" class="x-hide-display">
            <div class="sub-admin-tab-css">

                <form id="formUpdateChorganisationDetail" name="formUpdateChorganisationDetail" action="<%= request.getContextPath()%>/prv/p/updateChorganisationDetail.action" class="XXentity-form" method="POST">

                    <input type="hidden" name="objectId" value='<s:property value="objectId"/>'/>

                    <div class="admin-form-container">
                        <div class="chox-form-item" id="nameField">
                            <label class="chox-form-std-label">Name<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDName" name="name" value="<s:property value="name" />"/>
                            <input type="hidden" name="originalName" value="<s:property value="name" />"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label">Used for Insurer Upload Only</label>
                            <s:checkbox id="insurerUploadOnlyCheckBoxId" name="insurerUploadOnly" value="insurerUploadOnly" onclick="onInsurerUploadOnlyChecked()"/>
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
                            <label class="chox-form-std-label">Use Fixed Transactional Fee?</label>
                            <select id="fixedTransactionalFeeOpt" name="fixedTransactionalFee" onchange="javascript:chargeMethodSelected(this.options[this.selectedIndex].value);">
                                <option value="false">No</option>
                                <option value="true">Yes</option>
                            </select>
                        </div>
                        <div class="chox-form-item" id="FixedTransactionalValueDiv">
                            <label class="chox-form-std-label">Fixed Transactional Fee (£)</label>
                            <input type="text" class="chox-ttxt" id="CCDFixedTransactionalFeeValue" name="fixedTransactionalFeeValue" value="<s:property value="fixedTransactionalFeeValue" />"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label">Allow Automatic Daily<br/>Rate Charge Adjustment?</label>
                            <select id="adjustDailyRateChargeOpt" name="adjustDailyRateCharge" onchange="javascript:dailyRateMethodSelected(this.options[this.selectedIndex].value);">
                                <option value="false">No</option>
                                <option value="true">Yes</option>
                            </select>
                        </div>
                        <div>&nbsp;</div>
                        <div class="chox-form-item" id="DailyRateChargeLimitDiv">
                            <label class="chox-form-std-label">Maximum Adjustment Value (in pence)</label>
                            <input type="text" class="chox-ttxt" id="CCDDailyRateChargeLimit" name="dailyRateChargeLimit" value="<s:property value="dailyRateChargeLimit" />"/>
                        </div>

                        <div class="chox-form-item" id="ForcePasswordChangeDiv">
                            <label class="chox-form-std-label">Password Expiry Period (Days)</label>
                            <input type="text" class="chox-ttxt" id="CCDForcePasswordChange" name="forcePasswordChange" value="<s:property value="forcePasswordChange" />"/>
                        </div>

                        <div class="chox-form-item" id="UniquePasswordHistoryDiv">
                            <label class="chox-form-std-label">Number Of Unique Passwords</label>
                            <input type="text" class="chox-ttxt" id="CCDUniquePasswordHistory" name="uniquePasswordHistory" value="<s:property value="uniquePasswordHistory" />"/>
                        </div>

                        <div class="chox-form-item" id="MinimumPasswordLengthDiv">
                            <label class="chox-form-std-label">Minimum Password Length</label>
                            <input type="text" class="chox-ttxt" id="CCDMinimumPasswordLength" name="minimumPasswordLength" value="<s:property value="minimumPasswordLength" />"/>
                        </div>

                        <div class="chox-form-item" id="MaximumLoginAttemptsDiv">
                            <label class="chox-form-std-label" style="margin-top : -7px;">Maximum login attempts<br/> (before account blocked)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDMaximumLoginAttemptsCho" name="maxLoginAttempts" value="<s:property value="maxLoginAttempts" />"/>
                        </div>

                        <div class="chox-form-item" id="BlockTimeDiv">
                            <label class="chox-form-std-label">Account blocked period (in minutes)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDBlockTimeCho" name="blockTime" value="<s:property value="blockTime" />"/>
                        </div>

                        <div class="chox-form-item" id="CCDBlockedMessageDiv">
                            <label class="chox-form-std-label">Account blocked message<span class="mandatory">*</span></label>
                            <textarea id="CCDBlockedMessage" name="blockedMessage" cols="30"  rows="3"><s:property value="blockedMessage" /></textarea>
                        </div>

                        <table><tr>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable Delegated Authority</label>
                                        <s:checkbox name="delegatedAuthority" value="delegatedAuthority" />
                                    </div>
                                </td>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable Claim Ownership</label>
                                        <s:checkbox name="claimOwnershipEnable" value="claimOwnershipEnable" />
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>

                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable Task Management</label>
                                        <s:checkbox name="taskManagementEnable" value="taskManagementEnable" />
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
                                        <label class="chox-form-std-label">Enable Direct Invoice Upload (TPI)</label>
                                        <s:checkbox name="thirdPartyInterventionActivated" value="thirdPartyInterventionActivated" />
                                    </div>
                                </td>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable Automatic Penalty Charges</label>
                                        <s:checkbox name="autoPenaltyChargeEnabled" value="autoPenaltyChargeEnabled" />
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable Subscriber Claims</label>
                                        <s:checkbox name="enableSubscriberClaims" value="enableSubscriberClaims" />
                                    </div>
                                </td>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable Fixed Fee Claims</label>
                                        <s:checkbox name="enableFixedFeeClaims" value="enableFixedFeeClaims" />
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Disable Private Notes</label>
                                        <s:checkbox name="disablePrivateNotes" value="disablePrivateNotes" onclick="doToggleInsurerDiscount()" />
                                    </div>
                                </td>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Restrict Export Functions<br/> For Operative Users?</label>
                                        <s:checkbox name="restrictExport" value="restrictExport" />
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Enable IP Whitelist</label>
                                        <s:checkbox name="enableIPWhitelist" value="enableIPWhitelist" />
                                    </div>
                                </td>
                                <td></td>
                            </tr>
                        </table>
                        <div class="chox-form-button">
                            <input type="submit" value="Save Changes" onclick="doInsurerUploadOnlyValidationSetup();"/>
                            <input type="button" value="Cancel" class="cancel" onclick="javascript: doChorganisationCancelBack();" />
                        </div>
                        <div class="chox-form-submit-result">&nbsp;</div>
                        <div id="CDmessageBox" class="action-error-msg"></div>
                    </div>
                    <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
                    <!--s:token/-->
                </form>

            </div>
        </div>
                    
        <div id="CHOAliasPanelTab" class="x-hide-display"></div>
        <div id="ChoTpiPanelTab" class="x-hide-display"></div>
        <div id="IPWhitelistConfigTab" class="x-hide-display"></div>
    </div>
</div>
