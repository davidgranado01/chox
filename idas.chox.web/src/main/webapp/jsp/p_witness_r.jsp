<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset">
    <legend>Witness Details</legend>
    <div style="display:none" class="form-container" id="witnessDetailsRId">
        <table class="chox-table-form">
        <tr>
            <td><label class="std-label-ro">
                Name</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="name" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Address 1</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="address1" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Address 2</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="address2" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Address 3</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="address3" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Address 4</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="address4" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Address 5</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="address5" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Postcode</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="postcode" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Telephone Day</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="telephoneDay" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Telephone Evening</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="telephoneEvening" /></label></td></tr>
        </table>
    </div>
</fieldset>
