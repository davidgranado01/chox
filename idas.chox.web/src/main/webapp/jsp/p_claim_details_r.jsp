<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset">
    <legend>Claim Details</legend>
    <div style="display:none" class="form-container">
        <div class="chox-form-item">
            <label class="std-label-ro">Managing Repair?</label>
            <label class="std-data-ro"><s:property value="isManagingRepairDesc" /></label>
        </div>
        <div class="chox-form-item">
            <label class="std-label-ro">GTA 4.1 Notice Date</label>
            <label class="std-data-ro"><s:date name="gtaNoticeDate" format="dd MMM yyyy HH:mm"  /></label>
        </div>
        <div class="chox-form-item">
            <label class="std-label-ro">Credit Agreement Signed by Customer Date</label>
            <label class="std-data-ro"><s:date name="creditAgreementDate" format="dd MMM yyyy HH:mm"  /></label>
        </div>
    </div>
</fieldset>
