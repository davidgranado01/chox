<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset" width="">
    <legend>Incident Details</legend>
    <div style="display:none" class="form-container" id="incidenDetailsRId">
        <table class="chox-table-form">
        <tr>
            <td><label class="std-label-ro">Date / Time</label></td>
            <td>&nbsp;</td>
            <td width="378px"><label class="std-data-ro"><s:property value="dateTime"/></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Location</label></td>
            <td>&nbsp;</td>
            <td width="378px" style="display:fixed;overflow-wrap:break-word;-ms-word-break:break-all;word-break:break-all;word-break:break-word;"><label class="std-data-ro"><s:property value="location" /></label></td>
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
        