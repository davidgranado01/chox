<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    Ext.onReady(function() {

        var form = $("#formUpdateCustomerMitigationDetails");

        var fsets =  $('legend',form);
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});

        ui.ajaxForm(form,null,'html');
        
        $("#MSsuccessBox").fadeOut(10000);
    });

</script>

<form id="formUpdateCustomerMitigationDetails" action="<%=request.getContextPath()%>/prv/p/updateCustomerMitigation.action" class="XXentity-form">
    <input name="claimId" type="hidden" value="<s:property value="claimId" />" />
    <fieldset class="x-fieldset partial" >
        <legend>Mitigation Statement</legend>
        <div class="form-container" id="mitigationStatementWId">
            <div class="chox-form-item">
                <label class="chox-form-std-label">Access To Another Vehicle?</label>
                <s:checkbox id="mitigationStatementcanAccessOtherVehicleId" name="canAccessOtherVehicle" />
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label1">Other Vehicle Regularly Used By Someone Else?</label>
                <s:checkbox id="mitigationStatementotherVehicleUsedId" name="otherVehicleUsed" />
            </div>

            <br>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">What Is The Other Vehicle?</label>
                <input type="text" class="chox-ttxt" id="CCDOtherVehicle" name="otherVehicle"  value="<s:property value="otherVehicle" />" />
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Courtesy Car Entitlement?</label>
                <s:checkbox id="mitigationStatementcourtesyCarEntitledId" name="courtesyCarEntitled" />
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Specific Vehicle Required?</label>
                <s:checkbox id="mitigationStatementspecificVehicleRequiredEntitledId" name="specificVehicleRequired" />
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Why Is Specific Vehicle Required?</label>
                <input type="text" class="chox-ttxt" id="CCSpecificVehicleReason" name="specificVehicleReason" value="<s:property value="specificVehicleReason" />"/>
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Type Of Vehicle Required?</label>
                <input type="text" class="chox-ttxt" id="CCDTypeVehicleRequired" name="typeVehicleRequired" value="<s:property value="typeVehicleRequired" />" />
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Special Requirements?</label>
                <input type="text" class="chox-ttxt" id="CCDSpecialRequirements" name="specialRequirements" value="<s:property value="specialRequirements" />" />
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Average Daily Mileage</label>
                <input type="text" class="chox-ttxt" id="CCDAverageDailyMileage" name="averageDailyMileage" value="<s:property value="averageDailyMileage" />" />
            </div>

            <div class="chox-form-button">
                <input type="submit" id="mitigationStatementSubmitButtonId" value="Save Changes" />
            </div>
            <div id="CDmessageBox1" class="action-error-msg"><s:property value="actionError" /></div>
            <div id="MSsuccessBox" class="chox-form-submit-result"><s:property value="actionResult" /></div>
        </div>
    </fieldset>
    <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>-->
    <!--s:token/-->
</form>
