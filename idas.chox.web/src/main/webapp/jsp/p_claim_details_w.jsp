<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

var noticeDatePicker;
var signedByDatePicker;

Ext.onReady(function() {
        noticeDatePicker = ui.unvalidatedDateField('gtaNoticeDate','<s:date format="dd/MM/yyyy" name="gtaNoticeDate" />','noticeDatePH');
        signedByDatePicker = ui.unvalidatedDateField('creditAgreementDate','<s:date format="dd/MM/yyyy" name="creditAgreementDate" />','signedByDatePH');


        var form = $("#formUpdateClaimDetailsForm");

        var fsets =  $('legend',form);
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});

        form.validate(
        {
            errorLabelContainer: "#claimDetailsMsgBox",
            rules: {
                gtaNoticeDate: {
                    dateITA:true
                },
                creditAgreementDate: {
                    dateITA:true
                }
            },
            messages: {
                gtaNoticeDate: {
                    dateITA:"Invalid date format for 'GTA 4.1 Notice Date'"
                },
                creditAgreementDate: {
                    dateITA:"Invalid date format for 'Credit Agreement Signed by Customer Date'"
                }
            }
        });
        ui.ajaxForm(form,null,'html');
        $("#claimDetailsResultMsgBox").fadeOut(10000);
});

function saveChanges(){
		
//   	var msgBox = $("#claimDetailsMsgBox");
    var resultMsgBox = $("#claimDetailsResultMsgBox");
    resultMsgBox.empty();
   		
    if ($("form#formUpdateClaimDetailsForm").valid()) {
//        $("#formUpdateClaimDetailsForm").submit();
        choxJqueryHttpSubmit($("form#formUpdateClaimDetailsForm"));
    }
    
    if ($('#managingRepairCheckboxWriteScreenId').is(':checked') === true) {
        $("#managingRepairCheckId").prop('checked', true);
    } else {
        $("#managingRepairCheckId").prop('checked', false);
    }
}

</script>

<form id="formUpdateClaimDetailsForm" action="<%=request.getContextPath()%>/prv/p/updateClaimDetails.action" class="XXentity-form">
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>

    <fieldset class="x-fieldset partial">
        <legend>Claim Details</legend>
        <div class="form-container" id="claimDetailsWId">
            <div class="chox-form-item">
                <label class="chox-form-std-label">CHO Managing Repair?</label>
                <s:checkbox id="managingRepairCheckboxWriteScreenId" name="managingRepair" />
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">GTA 4.1 Notice Date</label>
                <span id="noticeDatePH"></span>
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Credit Agreement Signed<br/>by Customer Date</label>
                <span id="signedByDatePH"></span>
            </div>
        <div class="chox-form-button">
            <input type="button" id="claimDetailsSubmitButtonId" value="Save Changes" onclick="return saveChanges();"/>
        </div>
        <div id="claimDetailsMsgBox" class="action-error-msg"><s:property value="actionError" /></div>
        <div id="claimDetailsResultMsgBox" class="chox-form-submit-result"><s:property value="actionResult" /></div>
        </div>
    </fieldset>
    <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>-->
</form>