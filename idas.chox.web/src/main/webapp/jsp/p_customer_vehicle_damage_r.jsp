<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset">
    <legend>Vehicle Damage</legend>
    <div style="display:none" class="form-container" id="customerVehicleDamageRId">
        <table class="chox-table-form">
        <tr>
            <td><label class="std-label-ro">Initial ECD</label></td>
             <td>&nbsp;</td>
           <td><label class="std-data-ro"><s:property value="InitialECDDesc" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Total Loss</label></td>
            <td>&nbsp;</td>
            <td><label id="customerVehicleTotalLossId" class="std-data-ro"><s:property value="isTotalLossDesc" /></label>
                <label id="customerVehicleTotalLossOriginalId" class="std-data-ro"><s:property value="isTotalLossOriginalDesc" /></label>
            </td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Is Usable</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="isUsableDesc" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Description</label></td>
            <td>&nbsp;</td>
            <td><div class="std-data-ro-big"><s:property value="damage" /></div></td>
        </tr>
        </table>
    </div>
</fieldset>