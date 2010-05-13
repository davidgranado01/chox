<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">


    $(function(){

        var noticeDatePicker = ui.dateField('gtaNoticeDate','<s:date format="dd/MM/yyyy" name="gtaNoticeDate" />','noticeDatePH');
        var signedByDatePicker = ui.dateField('creditAgreementDate','<s:date format="dd/MM/yyyy" name="creditAgreementDate" />','signedByDatePH');


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
                    date:true
                },
                creditAgreementDate: {
                    required:false,
                    date:true
                }
            },
            messages: {
                noticeDate: {
                    required:"You must supply a value for 'GTA 4.1 Notice Date'",
                    date:"Invalid date format for 'GTA 4.1 Notice Date'"
                },
                signedByDate: {
                    required:"You must supply a value for 'Credit Agreement Signed by Customer Date'",
                    date:"Invalid date format for 'Date'"
                }
            }
        });
        ui.ajaxForm(form,null,'html');
    });
</script>

<form id="formUpdateClaimDetailsForm" action="<%=request.getContextPath()%>/prv/p/updateClaimDetails.action" class="XXentity-form">
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <input name="currentVersion" type="hidden" value="<s:property value="version" />" />

    <fieldset class="x-fieldset partial">
        <legend>Claim Details</legend>
        <div class="form-container">
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
                    Credit Agreement Signed by Customer Date</label>
                <span id="signedByDatePH"></span>
            </div>
        <div class="chox-form-button">
            <input type="submit" value="Save Changes" />
        </div>
        <div id="claimDetailsMsgBox" class="action-error-msg"><s:property value="actionError" /></div>
        <div class="chox-form-submit-result"><s:property value="actionResult" /></div>
        </div>
    </fieldset>
</form>