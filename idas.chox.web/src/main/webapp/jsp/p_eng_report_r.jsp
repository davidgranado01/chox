<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset">
    <legend>Engineer Report</legend>
    <div style="display:none" class="form-container"  id="engineerReportRId">
        <table class="chox-table-form">
        <tr>
            <td><label class="std-label-ro">
                Estimated Labour Amount</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="labourAmount" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Estimated Total Repair Amount</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro">£<s:property value="totalAmount" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Estimated Days Under Repair</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="days" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Usable?</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="isUsableDesc" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Name</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="name" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Company</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="company" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Engineer Address 1</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="address1" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Engineer Address 2</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="address2" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Engineer Address 3</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="address3" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Engineer Address 4</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="address4" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Engineer Address 5</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="address5" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Engineer Postcode</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="postcode" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Engineer Telephone</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="telephone" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Engineer Email</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="email" /></label></td></tr>
        </table>
    </div>
</fieldset>
