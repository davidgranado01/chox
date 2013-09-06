<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset">
    <legend>Claim Details</legend>
    <div style="display:none" class="form-container" id="claimDetailsRId">
        <table class="chox-table-form" >
        <tr>
            <td><label class="std-label-ro">CHO Managing Repair?</label></td>
            <td>&nbsp;</td>
            <td><label id="managingRepairCheckboxReadScreenId" class="std-data-ro"><s:property value="isManagingRepairDesc" /></label>
            <label id="managingRepairCheckboxReadScreenOriginalId" class="std-data-ro"><s:property value="managingRepairOriginalDesc" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">GTA 4.1 Notice Date</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:date name="gtaNoticeDate" format="dd MMM yyyy HH:mm"  /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Credit Agreement Signed by Customer Date</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:date name="creditAgreementDate" format="dd MMM yyyy HH:mm"  /></label></td>
        </tr>
        </table>
    </div>
</fieldset>
