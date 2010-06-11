<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset">
    <legend>Mitigation Statement</legend>
    <div style="display:none" class="form-container" id="mitigationStatementRId">
        <table class="chox-table-form">
        <tr>
            <td><label class="std-label-ro">Access To Another Vehicle?</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="canAccessOtherVehicleDesc" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Other Vehicle Regularly Used By Someone Else?</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="otherVehicleUsedDesc" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">What Is The Other Vehicle?</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="otherVehicle" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Courtesy Car Entitlement?</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="courtesyCarEntitledDesc" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Specific Vehicle Required?</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="specificVehicleRequiredDesc" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Why Is Specific Vehicle Required?</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="specificVehicleReason" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Type Of Vehicle Required?</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="typeVehicleRequired" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Special Requirements?</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="specialRequirements" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Average Daily Mileage</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="averageDailyMileage" /></label></td>
        </tr>
         </table>
    </div>
</fieldset>
