<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset">
    <legend>Vehicle Damage</legend>
        <div style="display:none" class="form-container"> 
        <div class="chox-form-item">
            <label class="std-label-ro">Initial ECD</label>
            <label class="std-data-ro"><s:date format="dd/MM/yyyy" name="initialECD" /></label>
        </div>        
        <div class="chox-form-item">
            <label class="std-label-ro">Total Loss</label>
            <label class="std-data-ro"><s:property value="isTotalLossDesc" /></label>
        </div>
        <div class="chox-form-item">
            <label class="std-label-ro">Is Usable</label>
            <label class="std-data-ro"><s:property value="isUsableDesc" /></label>
        </div>
        <div class="chox-form-item">
            <label class="std-label-ro">Description</label>
            <div class="std-data-ro-big"><s:property value="damage" /></div>
        </div>                       
    </div>
</fieldset>