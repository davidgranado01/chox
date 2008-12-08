<%@ taglib uri="/struts-tags" prefix="s" %>


<fieldset class="x-fieldset">
    <legend>Invoice Detail</legend>
    
    
    <div style="display:none" class="form-container">
        
        
        <div class="chox-form-item">
            <label class="std-label-ro">
            Supplier Claims Handling #</label>
        <label class="std-data-ro"><s:property value="handlingInvoiceNo" /></label></div>
        
        <div class="chox-form-item">
            <label class="std-label-ro">
            Supplier Claim Invoice #</label>
        <label class="std-data-ro"><s:property value="claimInvoiceNo" /></label></div>                                               
        
        
        <div class="chox-form-item">
            <label class="std-label-ro">
            Hire Net</label>
        <label class="std-data-ro"><s:property value="hireNet" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
            Hire Vat</label>
        <label class="std-data-ro"><s:property value="hireVat" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
            Hire Gross</label>
        <label class="std-data-ro"><s:property value="hireGross" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
            Repair Net</label>
        <label class="std-data-ro"><s:property value="repairNet" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
            Repair Vat</label>
        <label class="std-data-ro"><s:property value="repairVat" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
            Repair Gross</label>
        <label class="std-data-ro"><s:property value="repairGross" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
            Engineer Fee Net</label>
        <label class="std-data-ro"><s:property value="engineerFeeNet" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
            Engieer Fee Vat</label>
        <label class="std-data-ro"><s:property value="engineerFeeVat" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
            Engineer Fee Gross</label>
        <label class="std-data-ro"><s:property value="engineerFeeGross" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
                Storage Recovery Net
            </label>
        <label class="std-data-ro"><s:property value="storageRecoveryNet" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
            Storage Recovery Vat</label>
        <label class="std-data-ro"><s:property value="storageRecoveryVat" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
            Storage Recovery Gross</label>
        <label class="std-data-ro"><s:property value="storageRecoveryGross" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
            Total Net</label>
        <label class="std-data-ro"><s:property value="totalNet" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
            Total Vat</label>
        <label class="std-data-ro"><s:property value="totalVat" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
            Total Gross</label>
        <label class="std-data-ro"><s:property value="totalGross" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
            Claims Handling Invoice Amount</label>
        <label class="std-data-ro"><s:property value="claimsHandlingInvoiceAmount" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
            Deduction For Claims Handling Fee</label>
        <label class="std-data-ro"><s:property value="deductionForClaimsHandlingFee" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
            Discount</label>
        <label class="std-data-ro"><s:property value="discount" /></label></div>
        <div class="chox-form-item">
            <label class="std-label-ro">
            Total To Pay</label>
        <label class="std-data-ro"><s:property value="totalToPay" /></label></div>    
        
        <div class="chox-form-item">
            <label class="std-label-ro">Excess Amount Collected From Policyholder</label><label class="std-data-ro">
            <s:property value="excessAmountCollected" /></label>
        </div> 
        <div class="chox-form-item">
            <label class="std-label-ro">VAT Amount Collected From Policyholder</label>
            <label class="std-data-ro"><s:property value="vatAmountCollected" /></label>
        </div> 
        <div class="chox-form-item">
            <label class="std-label-ro">Date Invoiced</label>
            <label class="std-data-ro"><s:date name="dateInvoiced" format="dd MMM yyyy hh:mm"  /></label>
        </div>                
        
    </div>
</fieldset>

