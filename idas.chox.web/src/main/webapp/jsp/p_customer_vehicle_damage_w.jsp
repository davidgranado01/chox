<%-- 
    Document   : p_customer_vehicle_damage_w
    Created on : 08-Dec-2008, 09:53:36
    Author     : Emmanuel
--%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">

        $(document).ready(function(){
            $("#formUpdateCustomerVehicleDamageForm").validate(
            {
                errorLabelContainer: "#INCmessageBox",                
                rules: {  
                    damage:{required:true}
                },
                messages: {
                    damage:{required:"Please supply a valid value for Description"}
                },
                submitHandler: function(form) {
                    $(form).ajaxSubmit(globalEntityFormOptions);
                }
            }); 
        });  

</script>

<form id="formUpdateCustomerVehicleDamageForm" name="formUpdateCustomerVehicleDamageForm" action="<%=request.getContextPath()%>/prv/p/updateCustomerVehicleDamage.action" class="XXentity-form">
    <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
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
            <div id="INCmessageBox" class="errorBox"></div>
            <div class="chox-form-submit-result"></div>             
        </div>
    </fieldset>
</form>    