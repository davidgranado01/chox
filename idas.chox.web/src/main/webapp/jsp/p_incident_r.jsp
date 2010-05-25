<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset">
    <legend>Incident Details</legend>
    <div style="display:none" class="form-container" id="incidenDetailsRId">
        <table class="chox-table-form">
        <tr>
            <td><label class="std-label-ro">Date / Time</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:date name="date" format="dd MMM yyyy HH:mm"  /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Location</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="location" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Police Involved?</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="isPoliceInvolvedDesc" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Description</label></td>
            <td>&nbsp;</td>
            <td><div class="std-data-ro-big"><s:property value="incidentDescription" /></div></td>
        </tr>
        </table>
    </div>
</fieldset>