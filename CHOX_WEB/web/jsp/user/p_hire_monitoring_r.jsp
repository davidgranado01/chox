<%@ taglib uri="/struts-tags" prefix="s" %>


<fieldset class="x-fieldset">
    <legend>Hire Monitoring</legend>
    <div style="display:none" class="form-container">
        <div class="chox-form-item">
            <label class="std-label-ro">
            Original ECD</label>
        <label class="std-data-ro"><s:property value="customer.InitialECDDesc" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Name Of Repairer
            </label>
        <label class="std-data-ro"><s:property value="nameOfRepairer" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
            Repair Book In Date</label>
        <label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairBookInDate" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
            Inspection Booked Date</label>
        <label class="std-data-ro"><s:date format="dd/MM/yyyy" name="inspectionBookedDate" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
            Inspection Date</label>
        <label class="std-data-ro"><s:date format="dd/MM/yyyy" name="inspectionDate" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
            Total Loss Check</label>
        <label class="std-data-ro"><s:property value="isTotalLossDesc" /></label></div>            
        <div class="chox-form-item">
            <label class="std-label-ro">
            Repair Completion Date</label>
        <label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairCompletionDate" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
            Name of IME</label>
        <label class="std-data-ro"><s:property value="nameOfIme" /></label></div>
        
    </div>
</fieldset>
