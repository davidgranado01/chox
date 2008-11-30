<%@ taglib uri="/struts-tags" prefix="s" %>


<form id="formUpdateIncident" action="user/updateInvoice.action" class="entity-form">
    <input type="hidden" name="objectId" value='<s:property value="id"/>'>
    <fieldset class="x-fieldset">
        <legend>Invoice Detail</legend>
        
        
        <div style="display:none" class="form-container">
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Supplier Claims Handling #</label>
            <input type="text" class="chox-txt"  name="handlingInvoiceNo" value='<s:property value="handlingInvoiceNo" />'/></div>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Supplier Claim Invoice #</label>
            <input type="text" class="chox-ttxt"  name="claimInvoiceNo" value='<s:property value="claimInvoiceNo" />'/></div>                                             
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Hire Net</label>
            <input type="text" class="chox-tnum"  name="hireNet" value='<s:property value="hireNet" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Hire Vat</label>
            <input type="text" class="chox-tnum"  name="hireVat" value='<s:property value="hireVat" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Hire Gross</label>
            <input type="text" class="chox-tnum"  name="hireGross" value='<s:property value="hireGross" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Repair Net</label>
            <input type="text" class="chox-tnum"  name="repairNet" value='<s:property value="repairNet" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Repair Vat</label>
            <input type="text" class="chox-tnum"  name="repairVat" value='<s:property value="repairVat" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Repair Gross</label>
            <input type="text" class="chox-tnum"  name="repairGross" value='<s:property value="repairGross" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engineer Fee Net</label>
            <input type="text" class="chox-tnum"  name="engineerFeeNet" value='<s:property value="engineerFeeNet" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engieer Fee Vat</label>
            <input type="text" class="chox-tnum"  name="engineerFeeNet" value='<s:property value="engineerFeeNet" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engineer Fee Gross</label>
            <input type="text" class="chox-tnum"  name="engineerFeeVat" value='<s:property value="engineerFeeVat" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Storage Recovery Net
                </label>
            <input type="text" class="chox-tnum"  name="storageRecoveryNet" value='<s:property value="storageRecoveryNet" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Storage Recovery Vat</label>
            <input type="text" class="chox-tnum"  name="storageRecoveryVat" value='<s:property value="storageRecoveryVat" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Storage Recovery Gross</label>
            <input type="text" class="chox-tnum"  name="storageRecoveryGross" value='<s:property value="storageRecoveryGross" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Total Net</label>
            <input type="text" class="chox-tnum"  name="totalNet" value='<s:property value="totalNet" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Total Vat</label>
            <input type="text" class="chox-tnum"  name="totalVat" value='<s:property value="totalVat" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Total Gross</label>
            <input type="text" class="chox-tnum"  name="totalGross" value='<s:property value="totalGross" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Claims Handling Invoice Amount</label>
            <input type="text" class="chox-tnum"  name="claimsHandlingInvoiceAmount" value='<s:property value="claimsHandlingInvoiceAmount" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Deduction For Claims Handling Fee</label>
            <input type="text" class="chox-tnum"  name="deductionForClaimsHandlingFee" value='<s:property value="deductionForClaimsHandlingFee" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Discount</label>
            <input type="text" class="chox-tnum"  name="discount" value='<s:property value="discount" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Total To Pay</label>
            <input type="text" class="chox-tnum"  name="totalToPay" value='<s:property value="totalToPayA" />'/></div>
            <div class="chox-form-button">
                <input type="submit" value="Save Changes" />
            </div>
            <div class="chox-form-submit-result">&nbsp;</div>                                              
        </div>
    </fieldset>
</form>