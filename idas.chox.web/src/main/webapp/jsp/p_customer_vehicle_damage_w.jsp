<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    Ext.onReady(function() {
        
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
        $("#CVDSucsessBox").fadeOut(10000);
    });
    
    function vehicleDamageSubmit() {
//        $("form#formUpdateCustomerVehicleDamageForm").submit();
        choxJqueryHttpSubmit($("form#formUpdateCustomerVehicleDamageForm"));
        // Update Hire Monitoring Total Loss field
        if ($('#customerVehicleDamageisTotalLossId').is(':checked') === 1) {
            $("#isTotalLostCheckId").prop('checked', true);
        } else {
            $("#isTotalLostCheckId").prop('checked', false);
        }
            
    }

</script>

<form id="formUpdateCustomerVehicleDamageForm" name="formUpdateCustomerVehicleDamageForm" action="<%=request.getContextPath()%>/prv/p/updateCustomerVehicleDamage.action" class="XXentity-form">
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <fieldset class="x-fieldset partial">
        <legend> Vehicle Damage</legend>
        <div class="form-container" id="customerVehicleDamageWId">
            <div class="chox-form-item">
                <label class="chox-form-std-label">Initial ECD</label>
                <label class="std-data-ro"><s:property value="initialECD" /></label>&nbsp;
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Total Loss</label>
                <s:checkbox id="customerVehicleDamageisTotalLossId" name="isTotalLossNew" />
                <label class="std-data-ro" id="customerVehicleTotalLossOriginalDescId"><s:property value="isTotalLossOriginalDesc" /></label>
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Is Usable?</label>
                <s:checkbox  id="customerVehicleDamageisUsableId" name="isUsable" />
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Description<span class="mandatory">*</span></label>
                <textarea class="chox-tta" id="IDDescription" cols="20" rows="5" name="damage"><s:property value="damage" /></textarea>
            </div>
            <div class="chox-form-button">
                <input type="button"  id="customerVehicleDamageSubmitButtonId" value="Save Changes" onclick="return vehicleDamageSubmit()"/>
            </div>
            <div id="customerVehicleDamageMsgBox" class="action-error-msg"><s:property value="actionError" /></div>
            <div id="CVDSucsessBox" class="chox-form-submit-result"><s:property value="actionResult" /></div>
        </div>
    </fieldset>
    <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>-->
    <!--s:token/-->
</form>    