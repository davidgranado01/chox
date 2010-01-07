<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(function(){
        var form = $("#formUpdateCustomerVehicleDamageForm");
        form.validate(
        {
            errorLabelContainer: "#INCmessageBox",
            rules: {
                damage:{required:true}
            },
            messages: {
                damage:{required:"Please supply a valid value for Description"}
            }
        });
        ui.ajaxForm(form);
    });

</script>

<form id="formUpdateCustomerVehicleDamageForm" name="formUpdateCustomerVehicleDamageForm" action="<%=request.getContextPath()%>/prv/p/updateCustomerVehicleDamage.action" class="XXentity-form">
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <fieldset class="x-fieldset">
        <legend> Vehicle Damage</legend>
        <div style="display:none" class="form-container">  
            <div class="chox-form-item">
                <label class="chox-form-std-label">Initial ECD</label>
                <label class="std-data-ro"><s:property value="initialECD" /></label>&nbsp;
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Total Loss</label>
                <s:checkbox name="isTotalLoss" />
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Is Usable?</label>
                <s:checkbox name="isUsable" />
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Description<span class="mandatory">*</span></label>
                <textarea class="chox-tta" id="IDDescription" cols="20" rows="5" name="damage"><s:property value="damage" /></textarea>
            </div>
            <div class="chox-form-button">
                <input type="submit" value="Save Changes" />
            </div>
            <div id="INCmessageBox" class="action-error-msg"></div>
            <div class="chox-form-submit-result"></div>             
        </div>
    </fieldset>
</form>    