<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(function(){
        
        var form = $("#formUpdateCustomerVehicleDamageForm");

        var fsets =  $('legend',form);
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});

        form.validate(
        {
            errorLabelContainer: "#customerVehicleDamageMsgBox",
            rules: {
                damage:{required:true}
            },
            messages: {
                damage:{required:"Please supply a valid value for Description"}
            }
        });
        
        ui.ajaxForm(form, null, 'html');
    });

</script>

<form id="formUpdateCustomerVehicleDamageForm" name="formUpdateCustomerVehicleDamageForm" action="<%=request.getContextPath()%>/prv/p/updateCustomerVehicleDamage.action" class="XXentity-form">
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <input name="currentVersion" type="hidden" value="<s:property value="version" />" />
    <fieldset class="x-fieldset partial">
        <legend> Vehicle Damage</legend>
        <div class="form-container">  
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
            <div id="customerVehicleDamageMsgBox" class="action-error-msg"><s:property value="actionError" /></div>
            <div class="chox-form-submit-result"><s:property value="actionResult" /></div>
        </div>
    </fieldset>
</form>    