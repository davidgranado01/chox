<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset">
    <legend>Extras</legend>
    <div style="display:none" class="form-container"  id="extrasRId">
        <table class="chox-table-form">
        <tr>
            <td><label class="std-label-ro">
                CDW Fee</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="cdwFee" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">
                CDW Quantity</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="cdwQty" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Automatic Fee</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="automaticFee" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Automatic Quantity</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="automaticQty" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Additional Driver Fee</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="additionalDriverFee" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Additional Driver Quantity</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="additionalDriverQty" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Sat Nav Fee</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="satNavFee" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Sat Nav Quantity</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="satNavQty" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Estate Fee</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="estateFee" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Estate Quantity</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="estateQty" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Baby Seat Fee</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="babySeatFee" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Baby Seat Quantity
            </label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="babySeatQty" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Tow Bars Fee</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="towBarsFee" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Tow Bars Quantity</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="towBarsQty" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Non-standard Risk Ins. Premium Fee</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="nonStandardInsurancePremiumFee" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Non-standard Risk Ins. Premium Qty</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="nonStandardInsurancePremiumQty" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Cover Note Required For<br/>Customer's Own Insurance Policy?</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="coverNoteRequiredDesc" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Admin Fee</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="adminFee" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Admin Quantity</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="adminQty" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Roof Rack Fee</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="roofRackFee" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Roof Rack Quantity</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="roofRackQty" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Dual Control Fee</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="dualControlFee" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Dual Control Quantity</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="dualControlQty" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Delivery Collection Fee</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="deliveryCollectionFee" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Delivery Collection Fee Quantity</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="deliveryCollectionQty" /></label></td></tr>
        </table>
    </div>
</fieldset>
