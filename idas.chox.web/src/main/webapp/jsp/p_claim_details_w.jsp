<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

var noticeDatePicker;
var signedByDatePicker;

$(document).ready(function(){
        noticeDatePicker = ui.dateField('gtaNoticeDate','<s:date format="dd/MM/yyyy" name="gtaNoticeDate" />','noticeDatePH');
        signedByDatePicker = ui.dateField('creditAgreementDate','<s:date format="dd/MM/yyyy" name="creditAgreementDate" />','signedByDatePH');


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
                    required:false,
                    dateITA:true
                },
                creditAgreementDate: {
                    required:false,
                    dateITA:true
                }
            },
            messages: {
                noticeDate: {
                    required:"You must supply a value for 'GTA 4.1 Notice Date'",
                    dateITA:"Invalid date format for 'GTA 4.1 Notice Date'"
                },
                signedByDate: {
                    required:"You must supply a value for 'Credit Agreement Signed by Customer Date'",
                    dateITA:"Invalid date format for 'Date'"
                }
            }
        });
        ui.ajaxForm(form,null,'html');
        
       
        
});

function saveChanges(){
		
   	var msgBox = $("#claimDetailsMsgBox");
   	if(noticeDatePicker.getValue() == "" || signedByDatePicker.getValue() == ""){
   		msgBox.empty()
   		if(noticeDatePicker.getValue() == "")
   			msgBox.append("You must supply a propper value for 'GTA 4.1 Notice Date'\n<br/>").show();
   		if(signedByDatePicker.getValue() == "")
   			msgBox.append("You must supply a propper value for 'Credit Agreement Signed by Customer Date'\n<br/>").show();
            return false;
   	} else {
   		msgBox.text("").show();
   		$("#formUpdateClaimDetailsForm").submit();
   	}
   
}

</script>

<form id="formUpdateClaimDetailsForm" action="<%=request.getContextPath()%>/prv/p/updateClaimDetails.action" class="XXentity-form">
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>

    <fieldset class="x-fieldset partial">
        <legend>Claim Details</legend>
        <div class="form-container" id="claimDetailsWId">
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Managing Repair?</label>
                    <s:checkbox name="managingRepair" />
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    GTA 4.1 Notice Date</label>
                <span id="noticeDatePH"></span>
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Credit Agreement Signed<br/>by Customer Date</label>
                <span id="signedByDatePH"></span>
            </div>
        <div class="chox-form-button">
            <input type="button" id="claimDetailsSubmitButtonId" value="Save Changes" onclick="return saveChanges();"/>
        </div>
        <div id="claimDetailsMsgBox" class="action-error-msg"><s:property value="actionError" /></div>
        <div class="chox-form-submit-result"><s:property value="actionResult" /></div>
        </div>
    </fieldset>
    <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
    <!--s:token/-->
</form>