<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var adminTabs;
    var adminTabIndex=0;
    // var isNew = true;

    Ext.onReady(function(){

        // isNew = isTrue($("#isNew").val());
        $.validator.addMethod("regex", function(value, element, regexp) {
            var check = false;
            var re = new RegExp(regexp);
            return this.optional(element) || re.test(value);
        }, "Please check your input.");

        var form = $("#formUpdateChorganisationDetail");

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
                minimumPasswordLength:{ required:true, number:true, min:7, max:32 }
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
                minimumPasswordLength:{ required:"You must supply a value for 'Minimum Password Length'", number:"'Minimum Password Length", min:"'Minimum Password Length' cannot be less than 7", max:"'Minimum Password Length' cannot be larger than 32" }
            }
        });

        adminTabs = new Ext.TabPanel({
            renderTo: 'mainPanel',
            height:610,
            width:760,
            id:"tab",
            border:true,
            loadMask:false,
            activeTab: adminTabIndex,
            items:[
                {contentEl:'CHODetailPanelTab', id:"CHODetailPanelTabId", title:'Details', tabTip:'CHO Details',listeners: {activate: insHandleActivate}}
                //{contentEl:'ChoTpiPanelTab', id:"ChoTpiPanelTabId", activate:true, title:'TPI', tabTip:'Third Party Intervention', disabled:isNew, listeners: {activate: insHandleActivate}, autoLoad: {url:"p/getTpiPage.action?objectId="+<s:property value="objectId" />+"&rdn="+getRandomNumber(), scripts:true}},
            ]
        });

        <s:if test="fixedTransactionalFee" >
            //            console.log("Hiding Fixed Transactional Fee stuff");
            $("#fixedTransactionalFeeOpt").val("true");
            $("#FixedTransactionalValueDiv").show();
        </s:if><s:else >
            //            console.log("Showing Fixed Transaction stuff");
            $("#fixedTransactionalFeeOpt").val("false");
            $("#FixedTransactionalValueDiv").hide();
        </s:else>

        <s:if test="adjustDailyRateCharge" >
            //            console.log("Hiding Fixed Transactional Fee stuff");
            $("#adjustDailyRateChargeOpt").val("true");
            $("#DailyRateChargeLimitDiv").show();
            addValidationRuleDailyRateChargeLimit()
        </s:if><s:else >
            //            console.log("Showing Fixed Transaction stuff");
            $("#adjustDailyRateChargeOpt").val("false");
            $("#DailyRateChargeLimitDiv").hide();
        </s:else>


        ui.ajaxForm(form, function(responseText, statusText){

            var response = eval('(' + responseText.trim() + ')');

            if(response && response.isValid)
            {

                if(response.resultType && response.resultType == 'New'){
//                    alert("New Credit hire has been created");
                    Ext.Msg.minWidth = 300;
                    Ext.Msg.alert('New CHO','A new CHO has been created.');
                    var newObjectId = parseInt(response.result);
                    var target = "#admin_param_panel";
                    var url = "<%= request.getContextPath()%>/prv/p/updateChorganisationDetailPanel.action";
                    var param = {"objectId":newObjectId};
                    ajax.loadHtml2(url,param,function(data){
                        $(target).html(data);
                    });
                }
                else {
                    Ext.Msg.minWidth = 300;
                    Ext.Msg.alert('Save Changes','Your changes have been saved.');
                }
            }
        });
    });

    function insHandleActivate(tab){
        adminTabIndex = 0;
        if(adminTabs){ adminTabIndex = adminTabs.items.indexOf(adminTabs.getActiveTab()); }
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
            //            console.log("Showing Fixed Transaction stuff");
            $("#FixedTransactionalValueDiv").show();
        } else if (fixedTransactionalFee === 'false') {
            //            console.log("Hiding Fixed Transactional Fee stuff");
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

</script>
<input name="isNew" id="isNew" type="hidden" value="<s:property value="isNew" />"/>
<div id="chox-admin-holder" style="width: 800px">

    <div id="chox-admin-col-div">
        <div id="header-title">
            <label>CHO Name:
                <s:if test="!isNew"><s:property value="name" /> </s:if><s:else>Create New CHO</s:else>
                </label>
            </div>
            <div id="mainPanel"></div>


            <div id="CHODetailPanelTab" class="x-hide-display">
                <div class="sub-admin-tab-css">

                        <form id="formUpdateChorganisationDetail" name="formUpdateChorganisationDetail" action="<%= request.getContextPath()%>/prv/p/updateChorganisationDetail.action" class="XXentity-form" method="POST">

                    <input type="hidden" name="objectId" value='<s:property value="objectId"/>'/>

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
                            <input type="text" class="chox-ttxt" id="ForcePasswordChange" name="forcePasswordChange" value="<s:property value="forcePasswordChange" />"/>
                        </div>

                        <div class="chox-form-item" id="UniquePasswordHistoryDiv">
                            <label class="chox-form-std-label">Number Of Unique Passwords</label>
                            <input type="text" class="chox-ttxt" id="UniquePasswordHistory" name="uniquePasswordHistory" value="<s:property value="uniquePasswordHistory" />"/>
                        </div>

                        <div class="chox-form-item" id="MinimumPasswordLengthDiv">
                            <label class="chox-form-std-label">Minimum Password Length</label>
                            <input type="text" class="chox-ttxt" id="MinimumPasswordLength" name="minimumPasswordLength" value="<s:property value="minimumPasswordLength" />"/>
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
                            <tr><td>

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
                            </tr>
                        </table>
                        <div class="chox-form-button">
                            <input type="submit" value="Save Changes"/>
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
        <div id="ChoTpiPanelTab" class="x-hide-display"></div>
    </div>
</div>