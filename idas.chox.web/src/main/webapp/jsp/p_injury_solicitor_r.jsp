<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset">
    <legend>Injury Solicitor</legend>
    <div style="display:none" class="form-container" id="injurySolicitorRId">
        <table class="chox-table-form">
        <tr>
            <td><label class="std-label-ro">
                Name</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="solicitor.name" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Address 1</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="solicitor.address1" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Address 2</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="solicitor.address2" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Address 3</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="solicitor.address3" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Address 4</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="solicitor.address4" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Address 5</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="solicitor.address5" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Postcode</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="solicitor.postcode" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Telephone Day</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="solicitor.telephone" /></label></td></tr>
        </table>
    </div>
</fieldset>