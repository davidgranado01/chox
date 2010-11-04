<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(function(){

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
                fixedTransactionalFeeValue:{ required:false, number:true, min:0 }
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
                fixedTransactionalFeeValue:{ number:"'Fixed Transactional Fee' must be numeric", min:"'Fixed Transactional Fee' cannot be less than zero" }
            }
        });

        if (<s:property value="fixedTransactionalFee" />) {
//            console.log("Hiding Fixed Transactional Fee stuff");
            $("#fixedTransactionalFeeOpt").val("true");
            $("#FixedTransactionalValueDiv").show();
        } else {
//            console.log("Showing Fixed Transaction stuff");
            $("#fixedTransactionalFeeOpt").val("false");
            $("#FixedTransactionalValueDiv").hide();
        }

        ui.ajaxForm(form, function(responseText, statusText){

            var response = eval('(' + responseText.trim() + ')');

            if(response && response.isValid)
            {

                if(response.resultType && response.resultType == 'New'){
                    alert("New Credit hire has been created");
                    var newObjectId = parseInt(response.result);
                    var target = "#admin_param_panel";
                    var url = "<%= request.getContextPath()%>/prv/p/updateChorganisationDetailPanel.action";
                    var param = {"objectId":newObjectId};
                    ajax.loadHtml(url,param,function(data){
                        $(target).html(data);
                    });
                }
            }
        });
    });

    function doChorganisationCancelBack(){
        var target = "#admin_param_panel";
        var url = "<%= request.getContextPath()%>/prv/p/loadAdminPanel.action";
        var param = {"adminPanelName":"ChoxCreditHireMgmtPanel"};
        ajax.loadHtml(url,param,function(data){
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

</script>

<div id="chox-admin-holder">
    <div id="chox-admin-col-div">
        <div id="header-title"><label>Credit Hire Detail</label></div>
        <form id="formUpdateChorganisationDetail" name="formUpdateChorganisationDetail" action="<%= request.getContextPath()%>/prv/p/updateChorganisationDetail.action" class="XXentity-form" method="POST">

            <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>

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
                    <label class="chox-form-std-label">Enable Delegated Authority</label>
                    <s:checkbox name="delegatedAuthority" value="delegatedAuthority" />
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Enable Claim Ownership</label>
                    <s:checkbox name="claimOwnershipEnable" value="claimOwnershipEnable" />
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Enable Task Management</label>
                    <s:checkbox name="taskManagementEnable" value="taskManagementEnable" />
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Active</label>
                    <s:checkbox name="status" value="status" />
                </div>
                <div class="chox-form-button">
                    <input type="submit" value="Save Changes"/>
                    <input type="button" value="Cancel" class="cancel" onclick="javascript: doChorganisationCancelBack();" />
                </div>
                <div class="chox-form-submit-result">&nbsp;</div>
                <div id="CDmessageBox" class="action-error-msg"></div>
            </div>
    <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
    <!--s:token/-->
        </form>
    </div>
</div>